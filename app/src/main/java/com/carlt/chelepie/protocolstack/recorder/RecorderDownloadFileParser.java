package com.carlt.chelepie.protocolstack.recorder;


import com.carlt.chelepie.appsdk.AppsdkUtils;
import com.carlt.chelepie.control.DaoPieDownloadControl;
import com.carlt.chelepie.control.RecorderControl;
import com.carlt.chelepie.data.recorder.FileInfo;
import com.carlt.chelepie.data.recorder.PieDownloadInfo;
import com.carlt.chelepie.data.recorder.PieInfo;
import com.carlt.chelepie.manager.DeviceConnectManager;
import com.carlt.chelepie.systemconfig.ActionConfig;
import com.carlt.doride.data.BaseResponseInfo;
import com.carlt.doride.preference.UseInfoLocal;
import com.carlt.doride.utils.FileUtil;
import com.carlt.doride.utils.LocalConfig;
import com.carlt.doride.utils.Log;
import com.carlt.sesame.utility.MyTimeUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

/**
 * 下载文件Parser
 * 
 * @author @Y.yun
 */
public class RecorderDownloadFileParser extends RecorderBaseParserNew<BaseResponseInfo> {
	private PieDownloadInfo mInfo;
	private RecorderControl.GetTranslateProgressCallback listener;

	public void setListener(RecorderControl.GetTranslateProgressCallback listener) {
		this.listener = listener;
	}
	private DaoPieDownloadControl mDownControl = DaoPieDownloadControl.getInstance();
	private boolean isContinue = true;
	private byte[] bufs;
	//子链接id
	private long clientId;

	public void setContinue(boolean isContinue) {
		this.isContinue = isContinue;
	}

	public RecorderDownloadFileParser(RecorderControl.GetTranslateProgressCallback listener, PieDownloadInfo info) {
		super(null, BaseResponseInfo.class);
		MSG_FAIL = "文件下载失败";
		MSG_SUCC = "文件下载成功";
		mRequestID = ActionConfig.MID_REPLAY_AND_DOWNLOAD;
		this.listener = listener;
		mInfo = info;
	}

	@Override
	protected String creatPost() {
		return CreatPost(mMap);
	}

	@Override
	public void run() {
		if (!DeviceConnectManager.isDeviceConnect()) {
			Erro(MSG_ERRO);
			return;
		}

		mInfo.setCreateTime(MyTimeUtil.getFormatTime(new Date()));
		mInfo.setStatus(PieDownloadInfo.STATUS_LOADING);
		if (listener != null) {
			listener.onTranslateProgress(mInfo);
		}
//			long stopflag = AppsdkUtils.CPDownloadStop(ActionConfig.getSeqNum());
		//认领请求
			FileInfo fileInfo = new FileInfo();
			fileInfo.setEndTime(mInfo.getEndTime());
			fileInfo.setFileLength(mInfo.getTotalLen());
			fileInfo.setFilename(mInfo.getFileSrcName());
			fileInfo.setFileNo(mInfo.getFileNo());
			fileInfo.setMinRecordType(mInfo.getMinRecordType());
			fileInfo.setRecordType(mInfo.getRecordType());
			fileInfo.setStartTime(mInfo.getStartTime());
			
			int streamType = 0;
			if(mInfo.getFileName().contains("jpg")){
				streamType = 1;
			}
			fileInfo.setStreamType(streamType);
			long flag = AppsdkUtils.CPDownloadFileByName(fileInfo, mSqnum);
		if (flag != 0) {
			Erro(MSG_ERRO);
			return;
		}
		//获取到握手信息
		byte[] buf = readResult(ActionConfig.MID_REPLAY_AND_DOWNLOAD);
		if (buf[0] != 0) {
			Erro(MSG_ERRO);
			return;
		}
		// 创建子链接
		clientId = AppsdkUtils.InitStreamClient(AppsdkUtils.CLIENTKKEY_FILE);
		if (clientId == -1) {
			Erro(MSG_ERRO);
			return;
		}
		//发送握手信息
		byte[] bufHand = new byte[buf.length-1];
		System.arraycopy(buf, 1, bufHand, 0, buf.length-1);
		long handShakeFlag = AppsdkUtils.SendHandShake(clientId, new String(bufHand));
		if (handShakeFlag != 0) {
			Erro(MSG_ERRO);
			return;
		}
		String account = UseInfoLocal.getUseInfo().getAccount();
		String deviceString = PieInfo.getInstance().getDeviceName();
		if (deviceString == null) {
			Erro("没有获取到设备ID");
			return;
		}
		mInfo.setAccout(account);
		mInfo.setDeviceName(deviceString);
		String dir = LocalConfig.DIR_CROP;
		if (mInfo.getStoreType() == PieDownloadInfo.STORE_CROP) {
			dir = LocalConfig.DIR_CROP;
		} else if (mInfo.getStoreType() == PieDownloadInfo.STORE_CAPTURE) {
			dir = LocalConfig.DIR_CAPTURE;
		} else if (mInfo.getStoreType() == PieDownloadInfo.STORE_EVENT) {
			dir = LocalConfig.DIR_EVENT;
		} else if (mInfo.getStoreType() == PieDownloadInfo.STORE_THUMBNAIL) {
			dir = LocalConfig.DIR_THUMBNAIL;
		}else if (mInfo.getStoreType() == PieDownloadInfo.STORE_ALLLISTS) {
			dir = LocalConfig.DIR_ALLLISTS;
		}else if (mInfo.getStoreType() == PieDownloadInfo.STORE_TEMP) {
			dir = LocalConfig.DIR_TEMP;
		}

		dir = LocalConfig.GetMediaPath(account, deviceString, dir);
		if (dir == null) {
			Erro("写文件错误！");
			return;
		}
		String path = dir + mInfo.getFileName();
		mInfo.setLocalPath(path);
		// 视频
		downloadH264(path);
	}

	/**
	 * @param path
	 */
	public void downloadH264(String path) {
		int len = 0;
		FileOutputStream stream = null;
		try {
				File file = new File(path);
				if (!file.exists()) {
					Log.e("TestFile", "Create the file:" + path);
					file.createNewFile();
				}
			stream = new FileOutputStream(file);
			do {
				bufs = AppsdkUtils.ReadClientFrame(clientId);
				if (bufs == null) {
					continue;
				}

				if (bufs[0] != 0) {
					bufs = null;
					break;
				}

				int wlen = bufs.length - 1;
				byte[] framBuf = new byte[bufs.length - 1];
				System.arraycopy(bufs, 1, framBuf, 0, framBuf.length);
				stream.write(framBuf);
				bufs = null;
				len += wlen;
				mInfo.setDownloadLen(len);
				mDownControl.update(mInfo);
				// 写文件
				if(null != listener){
					listener.onTranslateProgress(mInfo);
				}
				int percent = (int) ((float) (mInfo.getDownloadLen())/ mInfo.getTotalLen() * 100);
				if (percent >= 100) {
					percent = 99;
				}
				if (percent > 80) {
					long endFlag = AppsdkUtils.GetEndflag(clientId);
					//等于1，表示正常下载完成
					if (endFlag == AppsdkUtils.ENDFLAG_SUCC && mInfo.getDownloadLen() > 0) {
							try {
								stream.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
						Success(mBaseResponseInfo);
						return;
					}else if(endFlag == AppsdkUtils.ENDFLAG_FAIL){
						try {
							stream.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
						Erro("下载失败");
						return;
					}
				}
			} while (DeviceConnectManager.isDeviceConnect() && isContinue);

			if (!isContinue || !DeviceConnectManager.isDeviceConnect()) {
				Erro("被停止掉了");
				stream.close();
				return;
			}

		} catch (Exception e) {
			try {
				stream.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}

	
		try {
			stream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Erro("下载失败");
	}

	@Override
	protected void parser() throws Exception {
		getErroCode();
	}

	@Override
	protected synchronized void Erro(String msg) {
		Log.e("info", "Erro");
		long stopflag = AppsdkUtils.CPDownloadStop(ActionConfig.getSeqNum());
		if(clientId != 0 || clientId != -1){
			AppsdkUtils.CloseStreamClient(AppsdkUtils.CLIENTKKEY_FILE,clientId);
		}
		FileUtil.deleteFile(new File(mInfo.getLocalPath()));
		mInfo.setStatus(PieDownloadInfo.STATUS_STOP);
		mDownControl.update(mInfo);
		if (listener == null) {
			return;
		}
		mBaseResponseInfo.setFlag(BaseResponseInfo.ERRO);
		mBaseResponseInfo.setInfo(msg);
		listener.onErro(mBaseResponseInfo);
		listener = null;
	}

	@Override
	protected void Success(BaseResponseInfo mBaseResponseInfo) {
		mInfo.setSuccTime(MyTimeUtil.getFormatTime(new Date()));
		mInfo.setStatus(PieDownloadInfo.STATUS_FINISHED);
		mDownControl.update(mInfo);
		long stopflag = AppsdkUtils.CPDownloadStop(ActionConfig.getSeqNum());
		AppsdkUtils.CloseStreamClient(AppsdkUtils.CLIENTKKEY_FILE,clientId);
		if (listener == null) {
			return;
		}

		listener.onFinished(mInfo);
		listener = null;
	}

	public void stopDownload() {
		listener = null;
		isContinue = false;
		long stopflag = AppsdkUtils.CPDownloadStop(ActionConfig.getSeqNum());
		if(clientId != 0 || clientId != -1){
			AppsdkUtils.CloseStreamClient(AppsdkUtils.CLIENTKKEY_FILE,clientId);
		}
		mInfo.setStatus(PieDownloadInfo.STATUS_STOP);
	}

}

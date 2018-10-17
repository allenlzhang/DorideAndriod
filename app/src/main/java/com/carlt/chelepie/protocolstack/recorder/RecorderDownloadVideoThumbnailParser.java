package com.carlt.chelepie.protocolstack.recorder;


import com.carlt.chelepie.appsdk.AppsdkUtils;
import com.carlt.chelepie.data.recorder.BaseResponseInfo;
import com.carlt.chelepie.data.recorder.FileInfo;
import com.carlt.chelepie.data.recorder.PieDownloadInfo;
import com.carlt.chelepie.data.recorder.PieInfo;
import com.carlt.chelepie.manager.DeviceConnectManager;
import com.carlt.chelepie.systemconfig.ActionConfig;
import com.carlt.doride.preference.UseInfoLocal;
import com.carlt.doride.utils.FileUtil;
import com.carlt.doride.utils.LocalConfig;
import com.carlt.doride.utils.Log;
import com.carlt.sesame.control.CPControl;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * 下载视频对应的缩略图
 * 
 * @author @Y.yun
 */
public class RecorderDownloadVideoThumbnailParser extends
		RecorderBaseParserNew<BaseResponseInfo> {
	private List<PieDownloadInfo> mPinfos;

	boolean mIsContinue = true;

	public void setContinue(boolean mIsContinue) {
		this.mIsContinue = mIsContinue;
	}

	
	private String TAG = "DownloadVideoThumbnailParser";

	/**
	 * 已存在的图片不需要下载
	 */
	public static final String ISEXIST = "isExist";

	private long clientId;

	public RecorderDownloadVideoThumbnailParser(CPControl.GetResultListCallback listener,
			List<PieDownloadInfo> listsPinfo) {
		super(listener, BaseResponseInfo.class);
		MSG_FAIL = "下载视频缩略图失败";
		MSG_SUCC = "下载视频缩略图失败";
		mRequestID = ActionConfig.MID_REPLAY_AND_DOWNLOAD;
		this.mPinfos = listsPinfo;
	}

	@Override
	public void run() {
		if (!DeviceConnectManager.isDeviceConnect()) {
			Erro(MSG_ERRO);
			return;
		}

		for (int i = 0; i < mPinfos.size(); i++) {
			if (mIsContinue) {
				PieDownloadInfo mPinfo = mPinfos.get(i);
				// 视频文件对应的缩略图文件
				Log.i(TAG, "开始循环下载:" + mPinfos.toString());
				
				int type = 0;
				if (mPinfo.getStoreType() == PieDownloadInfo.STORE_CAPTURE) {
					type = 1;
				} else if (mPinfo.getStoreType() == PieDownloadInfo.STORE_EVENT) {
					type = 2;
				}

				String account = UseInfoLocal.getUseInfo().getAccount();
				String deviceString = PieInfo.getInstance().getDeviceName();
				
				String fileName =  mPinfo.getFileName().replace("mp4", "jpg").replace("video", "thm");
				String path = LocalConfig.GetMediaPath(account, deviceString,
						LocalConfig.DIR_THUMBNAIL) + fileName;
				if (FileUtil.isExist(path)) {
					Success(ISEXIST);
					Log.i(TAG, "ISEXIST");
					continue;
				}
				FileOutputStream out = null;
				try {

					FileInfo fileInfo = new FileInfo();
					fileInfo.setEndTime(mPinfo.getEndTime());
					fileInfo.setFileLength(mPinfo.getTotalLen());
					fileInfo.setFilename(mPinfo.getFileName());
					fileInfo.setFileNo(mPinfo.getFileNo());
					fileInfo.setMinRecordType(1);// 子类型：1：表示缩略图（当streamType为
													// 1：图片时有效）
					fileInfo.setRecordType(mPinfo.getRecordType());
					fileInfo.setStartTime(mPinfo.getStartTime());
					fileInfo.setStreamType(1);// 0表示视频，1图片
					// 认领请求
					long flag = AppsdkUtils.CPDownloadFileByName(fileInfo,mSqnum);
					if (flag != 0) {
						Erro(MSG_ERRO);
						continue;
					}
					// 获取到握手信息
					byte[] buf = readResult(ActionConfig.MID_REPLAY_AND_DOWNLOAD);
					if (buf[0] != 0) {
						Erro(MSG_ERRO);
						return;
					}
					clientId = AppsdkUtils.InitStreamClient(AppsdkUtils.CLIENTKKEY_THUMBNAIL);
					if (clientId == -1) {
						Erro(MSG_ERRO);
						continue;
					}
					// 发送握手信息
					byte[] bufHand = new byte[buf.length - 1];
					System.arraycopy(buf, 1, bufHand, 0, buf.length - 1);
					long handShakeFlag = AppsdkUtils.SendHandShake(clientId,
							new String(bufHand));
					if (handShakeFlag != 0) {
						Erro(MSG_ERRO);
						return;
					}

					if (deviceString == null) {
						Erro("没有获取到设备ID");
						return;
					}

					mPinfo.setThumbnailPath(path);
					int length = 0;
					out = new FileOutputStream(path);
					
					int errflag = 0;
					do {
						buf = AppsdkUtils.ReadClientFrame(clientId);
						if (buf[0] != 0) {
							errflag = buf[0];
							break;
						}
						byte[] framBuf = new byte[buf.length - 1];
						System.arraycopy(buf, 1, framBuf, 0, framBuf.length);
						out.write(framBuf);
						length += framBuf.length;
						mPinfo.setDownloadLen(length);
						
						//缩略图，只读一个包
						if (length > 0) {
							try {
								out.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
							mBaseResponseInfo.setInfo("下载完成");
							Success(mBaseResponseInfo);
							errflag = 1;
						}
						
					} while (DeviceConnectManager.isDeviceConnect()&& mIsContinue && errflag == 0);
					errflag = 0;
				} catch (Exception ex) {
					Log.e(TAG, "Socket发送消息失败=" + ex);
					Erro(MSG_ERRO);
				} finally {
					try {
						if (out != null) {
							out.close();
						}
					} catch (IOException e1) {
						Log.e(TAG, "Download__IOException");
					}
				}
			//等于0，表示正常下载完成
//			if (AppsdkUtils.GetEndflag(clientId) == AppsdkUtils.ENDFLAG_SUCC && mPinfo.getDownloadLen() > 0) {
//				Success(mBaseResponseInfo);
//				continue;
//			}
//			Erro("下载失败");
			}
			Log.e(TAG, "跳出if");
		}
		Log.e(TAG, "跳出循环for");
	}

	@Override
	protected void parser() throws Exception {
	}

	@Override
	protected long sendMsg() {
		return -1;
	}

	@Override
	protected void Success(BaseResponseInfo mBaseResponseInfo) {
		Log.e(TAG, "Success:"+mBaseResponseInfo.getInfo());
		if (!ISEXIST.equals(mBaseResponseInfo.getInfo())) {
			AppsdkUtils.CPDownloadStop(ActionConfig.getSeqNum());
			try {
				AppsdkUtils.CloseStreamClient(AppsdkUtils.CLIENTKKEY_THUMBNAIL,
						clientId);
			} catch (Exception ex) {
				Log.e(TAG, "Success:Exception");
			}
		}
		super.Success(mBaseResponseInfo);
	}

	@Override
	protected void Erro(BaseResponseInfo mBaseResponseInfo) {
		Log.e(TAG, "Erro:"+mBaseResponseInfo.getInfo());
		AppsdkUtils.CPDownloadStop(ActionConfig.getSeqNum());
		if (clientId != 0 || clientId != -1) {

			try {
				AppsdkUtils.CloseStreamClient(AppsdkUtils.CLIENTKKEY_THUMBNAIL,
						clientId);
			} catch (Exception ex) {
				Log.e(TAG, "Erro:Exception");
			}
		}
		super.Erro(mBaseResponseInfo);
	}

	@Override
	protected String creatPost() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void stopDownLoadVideoThumbnail(){
		AppsdkUtils.CPDownloadStop(ActionConfig.getSeqNum());
		if (clientId != 0 || clientId != -1) {
			try {
				AppsdkUtils.CloseStreamClient(AppsdkUtils.CLIENTKKEY_THUMBNAIL,
						clientId);
			} catch (Exception ex) {
				Log.e(TAG, "Erro:Exception");
			}
		}
		
		mIsContinue = false;
		
	}

}

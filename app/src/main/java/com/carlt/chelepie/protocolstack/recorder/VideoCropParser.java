package com.carlt.chelepie.protocolstack.recorder;


import com.carlt.chelepie.appsdk.AppsdkUtils;
import com.carlt.chelepie.control.DaoPieDownloadControl;
import com.carlt.chelepie.control.RecorderControl;
import com.carlt.chelepie.data.recorder.BaseResponseInfo;
import com.carlt.chelepie.data.recorder.CropInfo;
import com.carlt.chelepie.data.recorder.PieDownloadInfo;
import com.carlt.chelepie.data.recorder.PieInfo;
import com.carlt.chelepie.manager.DeviceConnectManager;
import com.carlt.chelepie.systemconfig.ActionConfig;
import com.carlt.doride.preference.UseInfoLocal;
import com.carlt.doride.systemconfig.RuningConfig;
import com.carlt.doride.utils.FileUtil;
import com.carlt.doride.utils.LocalConfig;
import com.carlt.doride.utils.Log;
import com.carlt.doride.utils.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author @Y.yun 视频剪裁
 */
public class VideoCropParser extends RecorderBaseParserNew<BaseResponseInfo> {

	RecorderControl.GetTranslateProgressCallback mListener;
	// 开始时间
	private String startTime;
	// 结束时间
	private String endTime;

	private int length;
	private DaoPieDownloadControl mDownControl = DaoPieDownloadControl
			.getInstance();
	private boolean mIsContiune = true;
	private PieDownloadInfo mInfo;
	private byte[] bufs;

	private long clientId;
	
	//文件长度
	int len = 0;

	public VideoCropParser(RecorderControl.GetTranslateProgressCallback listener,
                           String startTime, String endTime) {
		super(null, BaseResponseInfo.class);
		this.startTime = startTime;
		this.endTime = endTime;
		this.mListener = listener;
		MSG_FAIL = "视频剪裁失败";
		MSG_SUCC = "视频剪裁成功";
	}

	@Override
	protected String creatPost() {
		return CreatPost(mMap);
	}

	@Override
	protected void parser() throws Exception {
		CropInfo mCropIno = CropInfo.getInstance();
		mCropIno.setFileName(mJson.optString("filename"));
		mCropIno.setFileSize(mJson.optString("filesize"));
		getErroCode();
	}

	@Override
	protected void Success(BaseResponseInfo mBaseResponseInfo) {
		super.Success(mBaseResponseInfo);
	}

	@Override
	public void run() {
		RuningConfig.ISCropRuning = true;
		if (!DeviceConnectManager.isDeviceConnect()) {
			cropError(MSG_ERRO);
			return;
		}
		mIsContiune = true;
		try {
			bufs = null;
			try {
				if(clientId!=0 || clientId != -1){
					AppsdkUtils.CloseStreamClient(AppsdkUtils.CLIENTKKEY_CUT_VEDIO,clientId);
				}
			} catch (Exception ex) {
				Log.i(TAG, ex.getMessage());
			}

			// 认领请求
			long flag = AppsdkUtils.CPDownloadByTime(startTime, endTime, mSqnum);
			if (flag != 0) {
				Erro(MSG_ERRO);
				return;
			}
			// 获取到握手信息
			byte[] buf = readResult(ActionConfig.MID_REPLAY_AND_DOWNLOAD);
			if (buf[0] != 0) {
				Erro(MSG_ERRO);
				return;
			}
			// 创建子链接
			clientId = AppsdkUtils.InitStreamClient(AppsdkUtils.CLIENTKKEY_CUT_VEDIO);
			if (clientId == -1) {
				Erro(MSG_ERRO);
				return;
			}
			// 发送握手信息
			byte[] bufHand = new byte[buf.length - 1];
			System.arraycopy(buf, 1, bufHand, 0, buf.length - 1);
			long handShakeFlag = AppsdkUtils.SendHandShake(clientId,new String(bufHand));
			if (handShakeFlag != 0) {
				Erro(MSG_ERRO);
				return;
			}

			mInfo = new PieDownloadInfo();
			mInfo.setStoreType(PieDownloadInfo.STORE_CROP);

			// 去掉非数字
			String regEx = "[^0-9]";
			Pattern p = Pattern.compile(regEx);
			Matcher m1 = p.matcher(startTime);
			String s1 = m1.replaceAll("").trim();
			Matcher m2 = p.matcher(endTime);
			String s2 = m2.replaceAll("").trim();

			String s = "";
			if (!StringUtils.isEmpty(s2) && s2.length() > 6) {
				s = s2.substring(s2.length() - 4);
			}
			mInfo.setFileName(s1 + "_" + s + ".mp4");
			mInfo.setFileSrcName(mInfo.getFileName());
			mInfo.setStartTime(startTime);
			mInfo.setEndTime(endTime);
			mInfo.setType(PieDownloadInfo.TYPE_H264);
			String account = UseInfoLocal.getUseInfo().getAccount();
			String deviceString = PieInfo.getInstance().getDeviceName();
			mInfo.setAccout(account);
			mInfo.setDeviceName(deviceString);
			String dir = LocalConfig.GetMediaPath(account, deviceString,LocalConfig.DIR_CROP);
			if (dir == null) {
				cropError("写文件错误！");
				return;
			}

			Log.e("Crop", mInfo.toString());
			String path = dir + mInfo.getFileName();
			// 创建文件
			// 开始下载文件
			mInfo.setLocalPath(path);
			FileOutputStream stream = null;
			try {
				File file = new File(path);
				if (!file.exists()) {
					Log.e("Crop", "Create the file:" + path);
					file.createNewFile();
				}
				 stream = new FileOutputStream(file);

				do {
					bufs = AppsdkUtils.ReadClientFrame(clientId);
					
//					Log.e("Crop", "bufs.length:"+bufs.length);
					if (bufs[0] != 0) {
						bufs = null;
						Log.e("Crop", "剪裁获取到视频为空");
						break;
					}
					mListener.onUpdateProgress(bufs[1]);
					byte[] framBuf = new byte[bufs.length - 2];
					System.arraycopy(bufs, 2, framBuf, 0, framBuf.length);
					stream.write(framBuf);
					int wlen = framBuf.length - 1;
					bufs = null;
					len += wlen;
					mInfo.setDownloadLen(len);
					// 写文件
					if (mListener != null) {
						// 除以计算的长度
						mListener.onTranslateProgress(mInfo);
					}
				} while (DeviceConnectManager.isDeviceConnect() && mIsContiune);

			} catch (Exception e) {
				Log.e("Crop", "Error on writeFilToSD.");
				cropError("被停止掉了");
				return;
			}
			if(null!=stream){
				stream.close();
			}
			if (!mIsContiune || !DeviceConnectManager.isDeviceConnect()) {
				cropError("被停止掉了");
				return;
			}

			if (AppsdkUtils.GetEndflag(clientId) == AppsdkUtils.ENDFLAG_SUCC && mInfo.getDownloadLen() > 0) {
				Log.e("Crop", "GetEndflag 1");
				cropSucc();
				return;
			}
			cropError("写文件错误！");
		} catch (Exception ex) {
			Log.e(TAG, "Socket发送消息失败=" + ex);
			cropError(MSG_FAIL);
			return;
		}
	}

	public synchronized void cropError(String msg) {
		Log.e("Crop", "Erro");
		try {
			AppsdkUtils.CPDownloadStop(ActionConfig.getSeqNum());
			if(clientId!=0 || clientId != -1){
				AppsdkUtils.CloseStreamClient(AppsdkUtils.CLIENTKKEY_CUT_VEDIO,clientId);
			}
		} catch (Exception ex) {
			Log.e("Crop", "Exception");
		}
		if (mInfo != null) {
			mDownControl.delete(mInfo);
			FileUtil.deleteFile(new File(mInfo.getLocalPath()));
		}
		mBaseResponseInfo.setFlag(BaseResponseInfo.ERRO);
		mBaseResponseInfo.setInfo(msg);
		RuningConfig.ISCropRuning = false;
		if (mListener != null) {
			mListener.onErro(mBaseResponseInfo);
			mListener = null;
		}
	}

	public void stopCrop() {
		if(mIsContiune){
			mIsContiune = false;
		}
		RuningConfig.ISCropRuning = false;
		AppsdkUtils.CPDownloadStop(ActionConfig.getSeqNum());
		if(clientId!=0 || clientId != -1){
			AppsdkUtils.CloseStreamClient(AppsdkUtils.CLIENTKKEY_CUT_VEDIO,clientId);
		}
	}

	public void cropSucc() {
		Log.e("Crop", "Download Succ");

		try {
			AppsdkUtils.CPDownloadStop(ActionConfig.getSeqNum());
			if(clientId!=0 || clientId != -1){
				AppsdkUtils.CloseStreamClient(AppsdkUtils.CLIENTKKEY_CUT_VEDIO,clientId);
			}
		} catch (Exception ex) {
			Log.e("Crop", "Download cropSucc");
		}

		if (mInfo != null && mInfo.getDownloadLen() > 0) {
			mInfo.setTotalLen(len);
			mInfo.setStatus(PieDownloadInfo.STATUS_FINISHED);
			Log.e("Crop", mInfo.toString());
			mDownControl.insert(mInfo);
		}

		mBaseResponseInfo.setFlag(BaseResponseInfo.SUCCESS);
		mBaseResponseInfo.setInfo(MSG_SUCC);
		Log.e("Crop", mInfo.toString());
		RuningConfig.ISCropRuning = false;
		if (mListener != null) {
			mListener.onFinished(mBaseResponseInfo);
			mListener = null;
		}
	}

}

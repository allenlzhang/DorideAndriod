package com.carlt.chelepie.protocolstack.recorder;


import android.os.SystemClock;
import android.util.Log;


import com.carlt.chelepie.appsdk.AppsdkUtils;
import com.carlt.chelepie.data.recorder.PieDownloadInfo;
import com.carlt.chelepie.data.recorder.PieInfo;
import com.carlt.chelepie.download.PieDownloadControl;
import com.carlt.chelepie.manager.DeviceConnectManager;
import com.carlt.chelepie.systemconfig.ActionConfig;
import com.carlt.chelepie.utils.BitConverter;
import com.carlt.doride.data.BaseResponseInfo;
import com.carlt.doride.preference.UseInfoLocal;
import com.carlt.doride.protocolparser.BaseParser;
import com.carlt.doride.systemconfig.RuningConfig;
import com.carlt.doride.utils.StringUtils;
import com.carlt.sesame.control.CPControl;
import com.carlt.sesame.utility.MyTimeUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;


/**
 * 拍照
 * @author liu
 *
 */
public class TakePhotoParser extends RecorderBaseParserNew<BaseResponseInfo> {

	public TakePhotoParser(BaseParser.ResultCallback listener) {
		super(listener, BaseResponseInfo.class);
		mRequestID = ActionConfig.MID_CAPTURE_FILE-1;
		MSG_FAIL = "抓拍失败";
		MSG_SUCC = "抓拍成功,文件已加入下载列表";
		RuningConfig.ISCaptureRuning = true;
	}

	@Override
	protected String creatPost() {
		mMap.put("action", "take_photos");
		return CreatPost(mMap);
	}

	@Override
	protected void parser() throws Exception {
		getErroCode();
		
		//抓拍有可能会返回两个文件，一个视频一个图片，父类里已经拿到一个
		paserCapture();
		if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
			Success(mBaseResponseInfo);
		}
		
		//这里再拿一次
		try {
			byte[] result = null;
			result = readResult(mRequestID);
			if (result == null || result[0] == NOT_FOUND) {
				Erro(MSG_ERRO2);
				return;
			}
			if (result[0] != 0) {
				Erro(MSG_ERRO3);
				return;
			}
			// 得到结果
			String s1 = new String(new String(result, 3, result.length - 3));
			Log.e("info", "Socket接收到的数据==" + s1);
			mJson = new JSONObject(s1);
			mBaseResponseInfo.setFlag(BaseResponseInfo.SUCCESS);
			paserCapture();
			
		} catch (Exception e) {
			Log.e("info", "Socket接收数据出错==" + e);
			mBaseResponseInfo.setFlag(BaseResponseInfo.ERRO);
			mBaseResponseInfo.setInfo("解析出错");
		}
		
		
		
	}

	private void paserCapture() throws JSONException {
		if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
			int bSnapshot = mJson.getInt("streamType");//录像数据流类型 0:视频 ,1:图片
			String picFileName = mJson.getString("filename");
			int picFileLen = mJson.getInt("fileLength");
			if (picFileLen == 0) {
				mBaseResponseInfo.setFlag(BaseResponseInfo.ERRO);
				mBaseResponseInfo.setInfo("抓拍失败 SDCard 异常。。。");
				return;
			}

			int bRecord = mJson.getInt("recordType");//录像主类型 0:全天定时录制，1: 命令抓拍触发，2：碰撞事件触发
			String account = UseInfoLocal.getUseInfo().getAccount();
			String deviceName = PieInfo.getInstance().getDeviceName();

			Log.e("DEBUG","抓拍返回："+ mJson.toString());
			String recFileName = mJson.getString("filename");
			int recFileLen = mJson.getInt("fileLength");
			String startTime = mJson.getString("startTime");
			String endTime = mJson.getString("endTime");
			int streamType = mJson.getInt("streamType");
			int recordType = mJson.getInt("recordType");
			int minRecordType = mJson.getInt("minRecordType");
			int fileno = mJson.getInt("fileNo");
			if (bSnapshot == 0 && bRecord == 1) {
				PieDownloadInfo pdVideo = new PieDownloadInfo();
				pdVideo.setType(PieDownloadInfo.TYPE_H264);
				pdVideo.setStoreType(PieDownloadInfo.STORE_CAPTURE);
				pdVideo.setFileSrcName(recFileName);
				pdVideo.setCreateTime(MyTimeUtil.getFormatTime(new Date()));
				pdVideo.setTotalLen(recFileLen);
				pdVideo.setStatus(PieDownloadInfo.STATUS_UNLOAD);
				String filename = StringUtils.spliteTime(recFileName);
				pdVideo.setStartTime(startTime);
				pdVideo.setEndTime(endTime);
				pdVideo.setFileName(filename);
				pdVideo.setAccout(account);
				pdVideo.setDeviceName(deviceName);
				pdVideo.setMinRecordType(minRecordType);
				pdVideo.setStreamType(streamType);
				pdVideo.setRecordType(recordType);
				pdVideo.setFileNo(fileno);
				PieDownloadControl.media_AddToDownload(pdVideo);
				mBaseResponseInfo.setInfo("抓拍视频加入下载队列");
			}else if(bSnapshot == 1 && bRecord == 1){
				PieDownloadInfo pdImg = new PieDownloadInfo();
				pdImg.setType(PieDownloadInfo.TYPE_JPG);
				pdImg.setStoreType(PieDownloadInfo.STORE_CAPTURE);
				pdImg.setFileSrcName(picFileName);
				pdImg.setCreateTime(MyTimeUtil.getFormatTime(new Date()));
				pdImg.setTotalLen(picFileLen);
				String filename = StringUtils.spliteTime(recFileName);
				pdImg.setStartTime(startTime);
				pdImg.setFileName(filename);
				pdImg.setStatus(PieDownloadInfo.STATUS_UNLOAD);
				pdImg.setEndTime(endTime);
				pdImg.setAccout(account);
				pdImg.setDeviceName(deviceName);
				pdImg.setMinRecordType(minRecordType);
				pdImg.setStreamType(streamType);
				pdImg.setRecordType(recordType);
				pdImg.setFileNo(fileno);
				PieDownloadControl.media_AddToDownload(pdImg);
				mBaseResponseInfo.setInfo("抓拍图片加入下载队列");
			}
				else{
				mBaseResponseInfo.setFlag(BaseResponseInfo.ERRO);
				mBaseResponseInfo.setInfo("抓拍失败，没有视频文件");
			}
		}
	}

	@Override
	protected void Success(BaseResponseInfo mBaseResponseInfo) {
		RuningConfig.ISCaptureRuning = false;
		super.Success(mBaseResponseInfo);
	}

	@Override
	protected void Erro(BaseResponseInfo mBaseResponseInfo) {
		RuningConfig.ISCaptureRuning = false;
		super.Erro(mBaseResponseInfo);
	}

	@Override
	protected long sendMsg() {
		long result = AppsdkUtils.CCapture(mSqnum);
		if (result == 0) {
			mBaseResponseInfo.setFlag(BaseResponseInfo.SUCCESS);
			mBaseResponseInfo.setInfo("通知抓拍成功");
			Success(mBaseResponseInfo);
		}
		return result;
	}
	
	/**
	 * 从返回命令池中查找结果
	 * 
	 * @param res
	 * @return 未找到或超过10s 返回null 找到返回结果
	 */
	public byte[] readResult(short res) {
		long currentMills = System.currentTimeMillis();
		boolean isTimeout = false;
		boolean flag = false;
		byte[] result = null;

		//抓拍会拿到两个文件
		long timeOut = SOTIMEOUT2 == -1 ? SOTIMEOUT : SOTIMEOUT2;
		while (DeviceConnectManager.isDeviceConnect() && !isTimeout) {

			if (System.currentTimeMillis() - currentMills > timeOut) {
				isTimeout = true;
			}
			synchronized (DeviceConnectManager.Buffers) {
				// 没有读取超时
				for (int i = 0; i < DeviceConnectManager.Buffers.size(); i++) {
					try {
						byte[] temp = DeviceConnectManager.Buffers.get(i);
						int index = 1;
						short action = BitConverter.littleEndianReadShort(temp, index);
						String str = new String(temp, 3, temp.length - 3);
						JSONObject job = new JSONObject(str);
						int seqNum = job.optInt("seqNum");
						if (action == res + 1) {
							result = temp;
							flag = true;
							DeviceConnectManager.Buffers.remove(temp);
							break;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					flag = false;
				}
			}
			if (flag || isTimeout) {
				break;
			}

			try {
				// 没找到 未超时 休息两毫秒 接着找
				SystemClock.sleep(40);
			} catch (Exception e) {
			}
		}

		if (!flag || isTimeout) {
			DeviceConnectManager.Buffers.clear();
			return new byte[] { NOT_FOUND };
		}
		return result;
	}

}


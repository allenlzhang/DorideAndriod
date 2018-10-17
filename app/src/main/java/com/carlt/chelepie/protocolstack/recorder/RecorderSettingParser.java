package com.carlt.chelepie.protocolstack.recorder;


import com.carlt.chelepie.appsdk.AppsdkUtils;
import com.carlt.chelepie.data.recorder.BaseResponseInfo;
import com.carlt.chelepie.data.recorder.PieInfo;
import com.carlt.chelepie.manager.DeviceConnectManager;
import com.carlt.chelepie.systemconfig.ActionConfig;
import com.carlt.doride.utils.Log;
import com.carlt.sesame.control.CPControl;

import org.json.JSONObject;

/**
 * @author @Y.yun 获取设置信息
 * 
 */
public class RecorderSettingParser extends RecorderBaseParserNew<BaseResponseInfo> {

	PieInfo mPieInfo;

	// 获取分辨率
	// 获取是否录音
	// 获取是否亮度，饱和度，对比度
	// 获取SDCard信息
	public RecorderSettingParser(CPControl.GetResultListCallback listener) {
		super(listener, BaseResponseInfo.class);
		MSG_FAIL = "获取设置信息失败";
		MSG_SUCC = "获取设置信息成功";
		mRequestID = ActionConfig.MID_GET_VEDIO_DISPLAY_SIZE;
		mPieInfo = PieInfo.getInstance();
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
		try {
			int ok = 0;
			//获取分辨率
			long result = AppsdkUtils.CGetVideoSize(mSqnum);
			if (result != 0) {
				Erro(MSG_ERRO);
				return;
			}
			byte[] bufs = readResult(ActionConfig.MID_GET_VEDIO_DISPLAY_SIZE);
			if (bufs[0] == 0) {
				// 获取分辨率成功
				mJson = new JSONObject(new String(bufs, 3, bufs.length - 3));
				ok = mJson.getInt("iRet");
				if (ok == 100) {
					int res = mJson.getInt("iResolution");
					mPieInfo.setResolution(res);
				}

				Log.i(TAG, "分辨率:" + new String(bufs, 3, bufs.length - 3));
			}

			// 获取是否录音
			result = AppsdkUtils.CGetAudioConfig(mSqnum);
			if (result != 0) {
				Erro(MSG_ERRO);
				return;
			}
			bufs = readResult(ActionConfig.MID_GET_VOICE_STATUS);
			if (bufs[0] == 0) {
				// 获取是否录制音频成功
				// {"bAudioEnable":1,"iRet":100,"reserve":0,"reserved":"","subcmd":2,"uiSessionId":1}
				mJson = new JSONObject(new String(bufs, 3, bufs.length - 3));
				ok = mJson.getInt("iRet");
				if (ok == 100) {
					int audioEnable = mJson.getInt("isAudioEnable");
					mPieInfo.setAudioEnable(audioEnable);
				}
				Log.i(TAG, "是否录制音频 " + new String(bufs, 3, bufs.length - 3));
			}
			// 获取亮度，饱和度，对比度
			// {"acutance":2056,"acutance0":2056,"brightness":50,"brightness0":50,"constrast":50,"constrast0":50,"enable":0,"enable0":0,"endHour":24,"endHour0":24,"endMinute":0,"endMinute0":0,"endSecond":0,"endSecond0":0,"gain":0,"gain0":0,"hue":50,"hue0":50,"iEnable":1,"iEnable0":0,"iRet":100,"reserve":0,"saturation":50,"saturation0":50,"startHour":0,"startHour0":0,"startMinute":0,"startMinute0":0,"startSecond":0,"startSecond0":0,"subcmd":2,"uiSessionId":1,"whitebalance":128,"whitebalance0":128}
			result = AppsdkUtils.CGetVideoColor(mSqnum);
			if (result != 0) {
				Erro(MSG_ERRO);
				return;
			}
			bufs = readResult(ActionConfig.MID_GET_VEDIO_DISPLAY_RGB);
			if (bufs[0] == 0) {
				mJson = new JSONObject(new String(bufs, 3, bufs.length - 3));
				if (ok == 100) {
					int brightness = mJson.getInt("brightness");// 亮度
					int constrast = mJson.getInt("contrast");// 对比度
					int saturation = mJson.getInt("saturation");// 饱和度

					mPieInfo.setBrightness(brightness);
					mPieInfo.setConstrast(constrast);
					mPieInfo.setSaturation(saturation);
				}
				Log.i(TAG, "对比度，亮度，饱和度 " + new String(bufs, 3, bufs.length - 3));
			}

			result = AppsdkUtils.CGetStorageInfo(mSqnum);
			if (result != 0) {
				Erro(MSG_ERRO);
				return;
			}

			bufs = readResult(ActionConfig.MID_SYS_INFO);
			if (bufs[0] == 0) {
				mJson = new JSONObject(new String(bufs, 3, bufs.length - 3));
				ok = mJson.getInt("iRet");
				if (ok == 100) {
					int Total = mJson.getInt("all");// 总容量
					int remainSpace = mJson.getInt("left");// 剩余容量
					// 获取到总共的,剩余的
					mPieInfo.setTotalSpace(Total);
					mPieInfo.setRemainSpace(remainSpace);
				}
				Log.i(TAG, "获取存储信息 " + new String(bufs, 3, bufs.length - 3));
			}
			result = AppsdkUtils.CGetCaptureRecord(mSqnum);
			if (result != 0) {
				Erro(MSG_ERRO);
				return;
			}

			bufs = readResult(ActionConfig.MID_GET_CAPTURE_VEDIO_OR_NOT);
			if (bufs[0] == 0) {
				mJson = new JSONObject(new String(bufs, 3, bufs.length - 3));
				ok = mJson.getInt("iRet");
				if (ok == 100) {
					int bRecordEn = mJson.getInt("iRecord");// 总容量
					// 抓拍关联视频
					mPieInfo.setRecodEnable(bRecordEn);
					
				}
				Log.i(TAG, "获取是否抓拍 " + new String(bufs, 3, bufs.length - 3));
			}

		} catch (Exception ex) {
			Log.e(TAG, "Socket发送消息失败=" + ex);
			Erro(MSG_ERRO);
		}

		Success(mBaseResponseInfo);
	}

	@Override
	protected void parser() throws Exception {
		getErroCode();
	}

}

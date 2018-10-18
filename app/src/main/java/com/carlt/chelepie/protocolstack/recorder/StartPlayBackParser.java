package com.carlt.chelepie.protocolstack.recorder;


import com.carlt.chelepie.appsdk.AppsdkUtils;
import com.carlt.chelepie.data.recorder.PieInfo;
import com.carlt.chelepie.manager.DeviceConnectManager;
import com.carlt.chelepie.systemconfig.ActionConfig;
import com.carlt.doride.data.BaseResponseInfo;
import com.carlt.doride.protocolparser.BaseParser;
import com.carlt.doride.utils.Log;
import com.carlt.sesame.control.CPControl;
import com.carlt.sesame.utility.MyTimeUtil;

import org.json.JSONObject;

/**
 * @author @Y.yun 开启回放流程
 */
public class StartPlayBackParser extends RecorderBaseParserNew<BaseResponseInfo> {
	private boolean mic_enable;
	private boolean mic_gain;
	private String startTime;
	private String endTime = MyTimeUtil.getPlayBackEndTime();
	
	private long clientId;

	public StartPlayBackParser(BaseParser.ResultCallback listener, String startTime) {
		super(listener, BaseResponseInfo.class);
		this.startTime = startTime;
		MSG_FAIL = "回放失败";
		MSG_SUCC = "回放成功";
	}

	@Override
	protected String creatPost() {
		return CreatPost(mMap);
	}

	@Override
	protected void parser() throws Exception {
		getErroCode();
	}

	@Override
	protected void Success(BaseResponseInfo mBaseResponseInfo) {
		PieInfo mPieInfo = PieInfo.getInstance();
		mPieInfo.setMicEnhance(mic_enable);
		mPieInfo.setMicGain(mic_gain);
		super.Success(mBaseResponseInfo);
	}

	@Override
	public void run() {
		if (!DeviceConnectManager.isDeviceConnect()) {
			Erro(MSG_ERRO);
			return;
		}
		Log.e("playtime", "startTime:"+startTime);
		try {
			byte[] buf = null;
			try {
				if(clientId!=0){
				// 重新开始直播之前先把子线路关闭下
				AppsdkUtils.CloseStreamClient(AppsdkUtils.CLIENTKKEY_REPLAY, clientId);
				}
			} catch (Exception ex) {
				Log.i(TAG, ex.getMessage());
			}
			long flag = AppsdkUtils.CPDownloadStop(ActionConfig.getSeqNum());
//				AppsdkUtils.CPStop(ActionConfig.getSeqNum());

			long result = AppsdkUtils.CPReady(startTime, endTime,mSqnum);
			if (result != 0) {
				Erro(MSG_ERRO3);
				return;
			}
			buf = readResult(ActionConfig.MID_REPLAY_AND_DOWNLOAD);
			if (buf[0] != 0) {
				Erro(MSG_FAIL);
				return;
			}

			clientId = AppsdkUtils.InitStreamClient(AppsdkUtils.CLIENTKKEY_REPLAY);
			if (clientId == -1) {
				Erro(MSG_ERRO);
				return;
			}
			// 发送握手信息
			byte[] bufHand = new byte[buf.length - 1];
			System.arraycopy(buf, 1, bufHand, 0, buf.length - 1);
			
			long handShakeFlag = AppsdkUtils.SendHandShake(clientId, new String(bufHand));
			if (handShakeFlag != 0) {
				Erro(MSG_ERRO);
				return;
			}
			
			mJson = new JSONObject(new String(buf, 3, buf.length - 3));
			parser();
			Success(mBaseResponseInfo);
		} catch (Exception ex) {
			Log.e(TAG, "Socket发送消息失败=" + ex);
			Erro(MSG_FAIL);
			return;
		}

	}

}

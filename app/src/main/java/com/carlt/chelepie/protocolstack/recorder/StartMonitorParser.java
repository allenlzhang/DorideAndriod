package com.carlt.chelepie.protocolstack.recorder;


import com.carlt.chelepie.appsdk.AppsdkUtils;
import com.carlt.chelepie.data.recorder.PieInfo;
import com.carlt.chelepie.manager.DeviceConnectManager;
import com.carlt.chelepie.systemconfig.ActionConfig;
import com.carlt.doride.data.BaseResponseInfo;
import com.carlt.doride.protocolparser.BaseParser;
import com.carlt.doride.utils.Log;
import com.carlt.sesame.control.CPControl;
import com.orhanobut.logger.Logger;

import org.json.JSONObject;


/**
 * @author @Y.yun 开启直播流程
 */
public class StartMonitorParser extends RecorderBaseParserNew<BaseResponseInfo> {
	private boolean mic_enable;
	private boolean mic_gain;
	
	private long clientId = -1;
	
	public StartMonitorParser(BaseParser.ResultCallback listener) {
		super(listener, BaseResponseInfo.class);
		mRequestID = ActionConfig.MID_MONITOR;
		MSG_FAIL = "开启直播失败";
		MSG_SUCC = "开启直播成功";
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
		
		Log.e("CHELEPIE_Monitor", "进来直播了。。。" + Thread.currentThread().getId() + "||" + Thread.currentThread().getName());
		try {
			try {
				//停止直播
//				Appsdk.CMStop();
//				//重新开始直播之前先把子线路关闭下
				AppsdkUtils.CloseStreamClient(AppsdkUtils.CLIENTKKEY_MONITOR, clientId);
			} catch (Exception ex) {
				Log.i(TAG, ex.getMessage());
			}

			// 认领子线程
			long result = AppsdkUtils.CMReady(0, mSqnum);
			if(result != 0){
				Erro(MSG_FAIL);
				return;
			}
			byte[] buf = readResult(ActionConfig.MID_MONITOR);
			if (buf[0] != 0) {
//				DeviceConnectManager.Buffers.clear();
				Erro(MSG_FAIL);
				return;
			}
			
			clientId = AppsdkUtils.InitStreamClient(AppsdkUtils.CLIENTKKEY_MONITOR);
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
	//		Logger.e(mJson + "=============直播链接握手信息返回=================");
			parser();
			mBaseResponseInfo.setFlag(BaseResponseInfo.SUCCESS);
			mBaseResponseInfo.setInfo(MSG_SUCC);
			Success(mBaseResponseInfo);
		} catch (Exception ex) {
			Log.e(TAG, "Socket发送消息失败=" + ex);
			Erro(MSG_ERRO);
		}

	}
	
	

}

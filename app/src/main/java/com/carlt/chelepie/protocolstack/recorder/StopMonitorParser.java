package com.carlt.chelepie.protocolstack.recorder;


import com.carlt.chelepie.appsdk.AppsdkUtils;
import com.carlt.chelepie.data.recorder.PieInfo;
import com.carlt.chelepie.systemconfig.ActionConfig;
import com.carlt.doride.data.BaseResponseInfo;
import com.carlt.doride.protocolparser.BaseParser;
import com.carlt.sesame.control.CPControl;

/**
 * @author @Y.yun 停止直播
 */
public class StopMonitorParser extends RecorderBaseParserNew<BaseResponseInfo> {
	private boolean mic_enable;
	private boolean mic_gain;

	public StopMonitorParser(BaseParser.ResultCallback listener) {
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
	protected long sendMsg() {
		return AppsdkUtils.CMStop(mSqnum);
	}
	

}

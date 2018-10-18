package com.carlt.chelepie.protocolstack.recorder;


import com.carlt.chelepie.appsdk.AppsdkUtils;
import com.carlt.chelepie.systemconfig.ActionConfig;
import com.carlt.doride.data.BaseResponseInfo;
import com.carlt.doride.protocolparser.BaseParser;
import com.carlt.sesame.control.CPControl;

/**
 * @author @Y.yun 停止回放
 */
public class StopPlayBackParser extends RecorderBaseParserNew<BaseResponseInfo> {
	public StopPlayBackParser(BaseParser.ResultCallback listener) {
		super(listener, BaseResponseInfo.class);
		MSG_FAIL = "回放失败";
		MSG_SUCC = "回放成功";
		mRequestID = ActionConfig.MID_REPLAY_AND_DOWNLOAD;
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
		super.Success(mBaseResponseInfo);
	}
	@Override
	protected long sendMsg() {
		return AppsdkUtils.CPStop(mSqnum);
	}


}

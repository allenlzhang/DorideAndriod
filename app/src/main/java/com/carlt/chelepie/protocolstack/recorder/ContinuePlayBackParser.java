package com.carlt.chelepie.protocolstack.recorder;


import com.carlt.chelepie.appsdk.AppsdkUtils;
import com.carlt.chelepie.systemconfig.ActionConfig;
import com.carlt.doride.data.BaseResponseInfo;
import com.carlt.doride.protocolparser.BaseParser;
import com.carlt.sesame.control.CPControl;

/**
 * @author @Y.yun 暂停回放
 */
public class ContinuePlayBackParser extends RecorderBaseParserNew<BaseResponseInfo> {

	public ContinuePlayBackParser(BaseParser.ResultCallback listener) {
		super(listener, BaseResponseInfo.class);
		mRequestID = ActionConfig.MID_REPLAY_AND_DOWNLOAD;
		MSG_FAIL = "暂停回放失败";
		MSG_SUCC = "暂停回放成功";
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
		long result = AppsdkUtils.CPContinue(mSqnum);
		return result;
	}

}

package com.carlt.chelepie.protocolstack.recorder;


import com.carlt.chelepie.appsdk.AppsdkUtils;
import com.carlt.chelepie.systemconfig.ActionConfig;
import com.carlt.doride.data.BaseResponseInfo;
import com.carlt.doride.protocolparser.BaseParser;
import com.carlt.sesame.control.CPControl;

public class RecorderTimeParser extends RecorderBaseParserNew {
	long mTime = 0;
	public RecorderTimeParser(BaseParser.ResultCallback listener, long time) {
		super(listener, BaseResponseInfo.class);
		mRequestID = ActionConfig.MID_NOTIFY_TIME;
		mTime = time;
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
	protected long sendMsg() {
		if(mTime ==0 ){
			long result = AppsdkUtils.CSetTime(System.currentTimeMillis(),1, mSqnum);
			return result;
		}else{
			long result = AppsdkUtils.CSetTime(mTime, 1,mSqnum);
			return result;
		}
	}

}

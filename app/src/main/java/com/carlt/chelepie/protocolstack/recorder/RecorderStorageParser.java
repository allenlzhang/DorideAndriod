package com.carlt.chelepie.protocolstack.recorder;


import com.carlt.chelepie.appsdk.AppsdkUtils;
import com.carlt.chelepie.data.recorder.PieInfo;
import com.carlt.chelepie.systemconfig.ActionConfig;
import com.carlt.doride.data.BaseResponseInfo;
import com.carlt.doride.protocolparser.BaseParser;
import com.carlt.sesame.control.CPControl;

/**
 * @author @Y.yun 获取设置信息
 * 
 */
public class RecorderStorageParser extends RecorderBaseParserNew<BaseResponseInfo> {

	PieInfo mPieInfo;

	// 获取SDCard信息
	public RecorderStorageParser(BaseParser.ResultCallback listener) {
		super(listener, BaseResponseInfo.class);
		MSG_FAIL = "获取设置信息失败";
		MSG_SUCC = "获取设置信息成功";
		mRequestID = ActionConfig.MID_SYS_INFO;
		mPieInfo = PieInfo.getInstance();
	}

	@Override
	protected String creatPost() {
		return CreatPost(mMap);
	}

	@Override
	protected void parser() throws Exception {
		getErroCode();
		if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
			int Total = 0;
			int remainSpace = 0;
			Total = mJson.getInt("all");
			remainSpace = mJson.getInt("left");
			// 获取到总共的,剩余的
			mPieInfo.setTotalSpace(Total);
			mPieInfo.setRemainSpace(remainSpace);
		}
	}

	@Override
	protected long sendMsg() {
		return AppsdkUtils.CGetStorageInfo(mSqnum);
	}

}

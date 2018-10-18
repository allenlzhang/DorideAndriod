package com.carlt.chelepie.protocolstack.recorder;


import com.carlt.chelepie.appsdk.AppsdkUtils;
import com.carlt.chelepie.data.recorder.PieInfo;
import com.carlt.chelepie.systemconfig.ActionConfig;
import com.carlt.doride.data.BaseResponseInfo;
import com.carlt.doride.protocolparser.BaseParser;
import com.carlt.sesame.control.CPControl;

/**
 *
 * 设置抓拍是否录制视频
 * 
 * 1,录制 0，不录制
 * 
 * @author @Y.yun
 * 
 */
public class RecorderCaptureRecordParser extends RecorderBaseParserNew<BaseResponseInfo> {

	PieInfo mInfo = PieInfo.getInstance();
	int iRecord = mInfo.getRecodEnable() == 0 ? 1 : 0;

	public RecorderCaptureRecordParser(BaseParser.ResultCallback listener) {
		super(listener, BaseResponseInfo.class);
		mRequestID = ActionConfig.MID_CONFIG_CATCH_VEDIO_OR_NOT;
		MSG_SUCC = "设置自动下载拍照前后共10s短视频成功！";
		MSG_FAIL = "设置自动下载拍照前后共10s短视频失败。。。";
	}

	@Override
	protected String creatPost() {
		mMap.put("action", "restore_factory");
		return CreatPost(mMap);
	}

	@Override
	protected void parser() throws Exception {
		getErroCode();
	}
	
	@Override
	protected void Success(BaseResponseInfo mBaseResponseInfo) {
		mInfo.setRecodEnable(iRecord);
		super.Success(mBaseResponseInfo);
	}

	@Override
	protected long sendMsg() {
		return AppsdkUtils.CSetCaptureRecord(iRecord,mSqnum);
	}

}

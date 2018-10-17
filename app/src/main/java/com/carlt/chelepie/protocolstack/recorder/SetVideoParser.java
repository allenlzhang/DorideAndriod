package com.carlt.chelepie.protocolstack.recorder;


import com.carlt.chelepie.appsdk.AppsdkUtils;
import com.carlt.chelepie.data.recorder.BaseResponseInfo;
import com.carlt.chelepie.data.recorder.PieInfo;
import com.carlt.chelepie.systemconfig.ActionConfig;
import com.carlt.sesame.control.CPControl;

/**
 * 设置视频
 * 
 * clip_enable 拍照关联视频开关 quality 画质区分低，中，高 0:SD,1:HD,2:FHD quality_level 码率
 * 视频配置 0:1080p,1:720p
 */
public class SetVideoParser extends RecorderBaseParserNew<BaseResponseInfo> {
	private int quality;

	public void setQuality(int quality) {
		this.quality = quality;
	}

	public SetVideoParser(CPControl.GetResultListCallback listener) {
		super(listener, BaseResponseInfo.class);
		MSG_SUCC = "设置画质成功";
		MSG_FAIL = "设置画质失败";
		mRequestID = ActionConfig.MID_CONFIG_VEDIO_DISPLAY_SIZE;
	}

	@Override
	protected String creatPost() {
		mMap.put("action", "set_video");
		return CreatPost(mMap);
	}

	@Override
	protected void parser() throws Exception {
		getErroCode();
	}

	@Override
	protected void Success(BaseResponseInfo mBaseResponseInfo) {
		PieInfo mPieInfo = PieInfo.getInstance();
		mPieInfo.setResolution(quality);
		super.Success(mBaseResponseInfo);
	}

	@Override
	protected long sendMsg() {
		return AppsdkUtils.CSetVideoSize(quality,mSqnum);
	}

}

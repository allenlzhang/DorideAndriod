package com.carlt.chelepie.protocolstack.recorder;


import com.carlt.chelepie.appsdk.AppsdkUtils;
import com.carlt.chelepie.data.recorder.BaseResponseInfo;
import com.carlt.chelepie.data.recorder.PieInfo;
import com.carlt.chelepie.systemconfig.ActionConfig;
import com.carlt.sesame.control.CPControl;

public class SetImgParser extends RecorderBaseParserNew<BaseResponseInfo> {
	private int brightness;
	private int contrast;
	private int saturate;
	PieInfo mPieInfo = PieInfo.getInstance();

	public void setBrightness(int brightness) {
		this.brightness = brightness;
	}

	public void setContrast(int contrast) {
		this.contrast = contrast;
	}

	public void setSaturate(int saturate) {
		this.saturate = saturate;
	}

	public SetImgParser(CPControl.GetResultListCallback listener) {
		super(listener, BaseResponseInfo.class);
		mRequestID = ActionConfig.MID_CONFIG_SET;
	}

	@Override
	protected String creatPost() {
		mMap.put("action", "set_image");
		return CreatPost(mMap);
	}

	@Override
	protected void parser() throws Exception {
		getErroCode();
	}

	@Override
	protected void Success(BaseResponseInfo mBaseResponseInfo) {
		PieInfo mPieInfo = PieInfo.getInstance();
		mPieInfo.setBrightness(brightness);
		mPieInfo.setConstrast(contrast);
		mPieInfo.setSaturation(saturate);
		super.Success(mBaseResponseInfo);
	}

	@Override
	protected long sendMsg() {
//		return AppsdkUtils.CSetVideoColor(brightness, contrast, saturate, mPieInfo.getHue(), mPieInfo.getGain(), mPieInfo.getWhitebalance(), mPieInfo.getAcutance(),mSqnum);
		return AppsdkUtils.CSetVideoColor(brightness, contrast, saturate, mSqnum);
	}

}

package com.carlt.chelepie.protocolstack.recorder;


import com.carlt.chelepie.appsdk.AppsdkUtils;
import com.carlt.chelepie.data.recorder.PieInfo;
import com.carlt.chelepie.systemconfig.ActionConfig;
import com.carlt.doride.data.BaseResponseInfo;
import com.carlt.doride.protocolparser.BaseParser;
import com.carlt.sesame.control.CPControl;

/**
 * @author @Y.yun 设置音频录像是否录制音频 0 不录, 1 可用
 */
public class SetAudioParser extends RecorderBaseParserNew<BaseResponseInfo> {
	private int mic_enable;
	PieInfo mPieInfo = PieInfo.getInstance();

	public SetAudioParser(BaseParser.ResultCallback listener) {
		super(listener, BaseResponseInfo.class);
		mic_enable = mPieInfo.getAudioEnable() == 0 ? 1 : 0;
		mRequestID = ActionConfig.MID_CONFIG_VOICE_STATUS;
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
		mPieInfo.setAudioEnable(mic_enable);
		super.Success(mBaseResponseInfo);
	}

	@Override
	protected long sendMsg() {
		return AppsdkUtils.CSetAudioConfig(mic_enable,mSqnum);
	}

}

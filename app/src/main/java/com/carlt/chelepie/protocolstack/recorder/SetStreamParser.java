package com.carlt.chelepie.protocolstack.recorder;


import com.carlt.chelepie.data.recorder.BaseResponseInfo;
import com.carlt.chelepie.data.recorder.PieInfo;
import com.carlt.sesame.control.CPControl;

public class SetStreamParser extends RecorderBaseParserNew {
	private int audio_enable;

	public void setAudio_enable(int audio_enable) {
		this.audio_enable = audio_enable;
//		mMap.put("mic_gain", setJsonBoolean(audio_enable));
	}

	public SetStreamParser(CPControl.GetResultListCallback listener, Class mClass) {
		super(listener, mClass);
	}

	@Override
	protected String creatPost() {
		mMap.put("action", "set_stream");
		return CreatPost(mMap);
	}

	@Override
	protected void parser() throws Exception {
		getErroCode();
	}

	@Override
	protected void Success(BaseResponseInfo mBaseResponseInfo) {
		PieInfo mPieInfo = PieInfo.getInstance();
		mPieInfo.setAudioEnable(audio_enable);
		super.Success(mBaseResponseInfo);
	}
	
	@Override
	protected long sendMsg() {
		return super.sendMsg();
	}

}

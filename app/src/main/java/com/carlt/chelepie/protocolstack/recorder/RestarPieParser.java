package com.carlt.chelepie.protocolstack.recorder;


import com.carlt.sesame.control.CPControl;

public class RestarPieParser extends RecorderBaseParserNew {
	public RestarPieParser(CPControl.GetResultListCallback listener, Class mClass) {
		super(listener, mClass);
	}

	@Override
	protected String creatPost() {
		mMap.put("action", "reset_board");
		return CreatPost(mMap);
	}

	@Override
	protected void parser() throws Exception {
		getErroCode();
	}

}

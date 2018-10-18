package com.carlt.chelepie.protocolstack.recorder;


import com.carlt.doride.protocolparser.BaseParser;
import com.carlt.sesame.control.CPControl;

public class RestarPieParser extends RecorderBaseParserNew {
	public RestarPieParser(BaseParser.ResultCallback listener, Class mClass) {
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

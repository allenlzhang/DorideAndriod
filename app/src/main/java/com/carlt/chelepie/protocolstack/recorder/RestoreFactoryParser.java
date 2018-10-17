package com.carlt.chelepie.protocolstack.recorder;


import com.carlt.sesame.control.CPControl;

public class RestoreFactoryParser extends RecorderBaseParserNew {
	public RestoreFactoryParser(CPControl.GetResultListCallback listener, Class mClass) {
		super(listener, mClass);
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

}

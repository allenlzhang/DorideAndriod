package com.carlt.sesame.protocolstack.career;

import com.carlt.sesame.protocolstack.BaseParser;

public class ChallengeStartParser extends BaseParser {

	private String id;

	public String getReturn() {
		return id;
	}

	@Override
	protected void parser() {
		id = mJson.optString("data");

	}
}

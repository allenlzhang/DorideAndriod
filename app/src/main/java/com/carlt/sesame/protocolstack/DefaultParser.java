package com.carlt.sesame.protocolstack;

import org.json.JSONObject;

public class DefaultParser extends BaseParser {

	private JSONObject s;

	@Override
	public JSONObject getReturn() {
		return s;
	}

	@Override
	protected void parser() {
		s = mJson.optJSONObject("data");
	}

	public String getValue(String key) {
		if (s != null) {

			return s.optString(key);
		}
		return "";

	}

}

package com.carlt.sesame.protocolstack;

import org.json.JSONObject;

public class DeviceIsOnlineParser extends BaseParser {
	private boolean online = false;

	public Boolean getReturn() {
		return online;
	}

	@Override
	protected void parser() {
		JSONObject mJSON_data = mJson.optJSONObject("data");
		int isonline = mJSON_data.optInt("isonline");
		if (isonline == 1) {
			online = true;
		} else {
			online = false;
		}

	}


}

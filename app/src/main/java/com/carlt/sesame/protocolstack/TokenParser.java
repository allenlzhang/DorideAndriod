package com.carlt.sesame.protocolstack;

import com.carlt.sesame.data.LoginInfo;

import org.json.JSONObject;

/**
 * @author user
 *  解析注册信息
 */
public class TokenParser extends BaseParser {

	public TokenParser() {
	}

	@Override
	protected void parser() {
		JSONObject mJSON_data = mJson.optJSONObject("data");
		LoginInfo.setToken((mJSON_data.optString("access_token", "")));
		LoginInfo.setExpiresIn(mJSON_data.optString("expires_in", ""));
	}
}

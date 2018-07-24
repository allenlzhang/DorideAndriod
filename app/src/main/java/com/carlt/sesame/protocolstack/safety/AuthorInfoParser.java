package com.carlt.sesame.protocolstack.safety;

import com.carlt.sesame.data.safety.AuthorInfo;
import com.carlt.sesame.protocolstack.BaseParser;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 主设备授权页面信息解析
 * @author Administrator
 *
 */

public class AuthorInfoParser extends BaseParser {

	private AuthorInfo mAutherInfo = new AuthorInfo();

	public AuthorInfo getReturn() {
		return mAutherInfo;
	}

	@Override
	protected void parser() {
		try {
			JSONObject mJSON_data = mJson.getJSONObject("data");
			mAutherInfo.setMobile_id(mJSON_data.optString("authorize_deviceid",""));
			mAutherInfo.setMobile_name(mJSON_data.optString("authorize_name",""));
			mAutherInfo.setAddtime(mJSON_data.optString("addtime",""));
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}
}

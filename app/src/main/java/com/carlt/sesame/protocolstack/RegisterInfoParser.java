package com.carlt.sesame.protocolstack;

import com.carlt.sesame.data.SesameLoginInfo;
import com.carlt.sesame.preference.TokenInfo;

import org.json.JSONObject;

/**
 * @author user
 *  解析注册信息
 */
public class RegisterInfoParser extends BaseParser {

	public RegisterInfoParser() {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void parser() {
		JSONObject mJSON_data = mJson.optJSONObject("data");
		JSONObject member = mJSON_data.optJSONObject("member");
		SesameLoginInfo.setUseId((member.optString("id", "")));
		SesameLoginInfo.setDealerId((member.optString("dealerid", "")));
		SesameLoginInfo.setRealname((member.optString("realname", "")));
		SesameLoginInfo.setGender((member.optString("gender", "")));
		SesameLoginInfo.setMobile((member.optString("mobile", "")));
		SesameLoginInfo.setStatus((member.optString("status", "")));
		SesameLoginInfo.setAvatar_img((member.optString("avatar_img", "")));
		SesameLoginInfo.setToken((member.optString("access_token", "")));
		TokenInfo.setToken(member.optString("access_token", ""));
		SesameLoginInfo.setExpiresIn((member.optString("expires_in", "")));
		SesameLoginInfo.setOriginate(member.optString("originate", "0"));
		SesameLoginInfo.setLastlogin(member.optString("lastlogin", "0"));
		SesameLoginInfo.setLoginoauth(member.optString("loginoauth", ""));
		SesameLoginInfo.setLogintimes(member.optString("logintimes", "0"));
		SesameLoginInfo.setCreatedate(member.optString("createdate", ""));
		SesameLoginInfo.setAvatar_img((member.optString("avatar_img", "")));
		SesameLoginInfo.setLifetime((member.optString("lifetime", "")));
		SesameLoginInfo.setCanQueryVio((member.optString("canQueryVio", "")));
		SesameLoginInfo.setDeviceidstring("");
		SesameLoginInfo.setDeviceActivate(false);

	}
}

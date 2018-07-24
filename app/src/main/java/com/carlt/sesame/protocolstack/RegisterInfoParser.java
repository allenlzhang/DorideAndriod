package com.carlt.sesame.protocolstack;

import com.carlt.sesame.data.LoginInfo;
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
		LoginInfo.setUseId((member.optString("id", "")));
		LoginInfo.setDealerId((member.optString("dealerid", "")));
		LoginInfo.setRealname((member.optString("realname", "")));
		LoginInfo.setGender((member.optString("gender", "")));
		LoginInfo.setMobile((member.optString("mobile", "")));
		LoginInfo.setStatus((member.optString("status", "")));
		LoginInfo.setAvatar_img((member.optString("avatar_img", "")));
		LoginInfo.setToken((member.optString("access_token", "")));
		TokenInfo.setToken(member.optString("access_token", ""));
		LoginInfo.setExpiresIn((member.optString("expires_in", "")));
		LoginInfo.setOriginate(member.optString("originate", "0"));
		LoginInfo.setLastlogin(member.optString("lastlogin", "0"));
		LoginInfo.setLoginoauth(member.optString("loginoauth", ""));
		LoginInfo.setLogintimes(member.optString("logintimes", "0"));
		LoginInfo.setCreatedate(member.optString("createdate", ""));
		LoginInfo.setAvatar_img((member.optString("avatar_img", "")));
		LoginInfo.setLifetime((member.optString("lifetime", "")));
		LoginInfo.setCanQueryVio((member.optString("canQueryVio", ""))); 
		LoginInfo.setDeviceidstring("");
		LoginInfo.setDeviceActivate(false);

	}
}

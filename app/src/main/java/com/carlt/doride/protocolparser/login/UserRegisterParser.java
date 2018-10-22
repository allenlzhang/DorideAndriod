package com.carlt.doride.protocolparser.login;

import com.carlt.doride.data.login.UserRegisterInfo;
import com.carlt.doride.model.LoginInfo;
import com.carlt.doride.protocolparser.BaseParser;
import com.carlt.sesame.data.SesameLoginInfo;
import com.carlt.sesame.preference.TokenInfo;
import com.google.gson.JsonObject;

/**
 * Created by marller on 2018\3\27 0027.
 */

public class UserRegisterParser extends BaseParser {


    private UserRegisterInfo mUserRegisterInfo=new UserRegisterInfo();

    public UserRegisterParser(ResultCallback callback) {
        super(callback);
    }

    @Override
    protected void parser() throws Exception {
        JsonObject jsonObject = mJson.getAsJsonObject("data");
        JsonObject registerJobj = jsonObject.getAsJsonObject("member");
        LoginInfo.setUseId(registerJobj.get("id").getAsString());
        LoginInfo.setDealerId(registerJobj.get("dealerid").getAsString());
        SesameLoginInfo.setDealerId(registerJobj.get("dealerid").getAsString());

        LoginInfo.setRealname(registerJobj.get("realname").getAsString());
        LoginInfo.setGender(registerJobj.get("gender").getAsString());
        LoginInfo.setMobile(registerJobj.get("mobile").getAsString());
        LoginInfo.setAvatar_img(registerJobj.get("avatar_id").getAsString());
        String access_token = registerJobj.get("access_token").getAsString();
        LoginInfo.setAccess_token(access_token);
        SesameLoginInfo.setAccess_token(access_token);
        SesameLoginInfo.setToken(access_token);
        TokenInfo.setToken(access_token);
//        DorideApplication.TOKEN = access_token;
        LoginInfo.setExpiresIn(registerJobj.get("expires_in").getAsString());
        LoginInfo.setOriginate(registerJobj.get("originate").getAsString());
        LoginInfo.setLastlogin(registerJobj.get("lastlogin").getAsString());
        LoginInfo.setLoginoauth(registerJobj.get("loginoauth").getAsString());
        LoginInfo.setLogintimes(registerJobj.get("logintimes").getAsString());
        LoginInfo.setCreatedate(registerJobj.get("createdate").getAsString());
        LoginInfo.setDeviceidstring("");
        LoginInfo.setCarname("");
        LoginInfo.setDeviceActivate(false);
    }
}
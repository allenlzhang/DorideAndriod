package com.carlt.yema.protocolparser.login;

import com.carlt.yema.data.login.UserRegisterInfo;
import com.carlt.yema.model.LoginInfo;
import com.carlt.yema.protocolparser.BaseParser;
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
        LoginInfo.setRealname(registerJobj.get("realname").getAsString());
        LoginInfo.setGender(registerJobj.get("gender").getAsString());
        LoginInfo.setMobile(registerJobj.get("mobile").getAsString());
        LoginInfo.setAvatar_img(registerJobj.get("avatar_id").getAsString());
        String access_token = registerJobj.get("access_token").getAsString();
        LoginInfo.setAccess_token(access_token);
//        YemaApplication.TOKEN = access_token;
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

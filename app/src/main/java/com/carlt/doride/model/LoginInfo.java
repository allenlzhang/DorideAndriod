package com.carlt.doride.model;

import com.carlt.doride.data.BaseResponseInfo;
import com.carlt.doride.http.retrofitnet.model.GetCarInfo;
import com.carlt.doride.http.retrofitnet.model.UserInfo;
import com.carlt.sesame.preference.TokenInfo;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017/10/10 0010.
 */

public class LoginInfo extends BaseResponseInfo {

    public static void Destroy() {
        JSONObject destroy = null;
        try {
            destroy = new JSONObject("{}");
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        UserInfo.getInstance().realName = destroy.optString("realname", "");
        UserInfo.getInstance().gender = destroy.optInt("gender", 0);
        UserInfo.getInstance().avatarFile = destroy.optString("avatar_img", "");
        UserInfo.getInstance().mobile = destroy.optString("mobile", "");
        UserInfo.getInstance().dealerId = destroy.optInt("expires_in", 0);
        UserInfo.getInstance().id = destroy.optInt("uid", 0);
        GetCarInfo.getInstance().optionid = destroy.optInt("optionid", 0);
        GetCarInfo.getInstance().styleId = destroy.optInt("brandCarid", 0);
        GetCarInfo.getInstance().deviceNum = destroy.optString("deviceidstring", "");
        GetCarInfo.getInstance().carNO = destroy.optString("carno", "");
        GetCarInfo.getInstance().isNextMain = destroy.optInt("isMainten");

        TokenInfo.setToken(destroy.optString("access_token", ""));
        GetCarInfo.getInstance().vin = "";
    }


}

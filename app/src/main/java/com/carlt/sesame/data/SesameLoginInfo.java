package com.carlt.sesame.data;

import com.carlt.doride.http.retrofitnet.model.GetCarInfo;
import com.carlt.doride.http.retrofitnet.model.UserInfo;
import com.carlt.sesame.preference.TokenInfo;

import org.json.JSONException;
import org.json.JSONObject;

public class SesameLoginInfo {


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
        TokenInfo.setToken("");
        UserInfo.getInstance().dealerId = destroy.optInt("expires_in", 0);
        UserInfo.getInstance().id = destroy.optInt("uid", 0);
        GetCarInfo.getInstance().optionid = destroy.optInt("optionid", 0);
        GetCarInfo.getInstance().styleId = destroy.optInt("brandCarId", 0);
        GetCarInfo.getInstance().city = destroy.optString("city", "");
        GetCarInfo.getInstance().deviceNum = destroy.optString("deviceidstring", "");
        GetCarInfo.getInstance().carNO = destroy.optString("carno", "");
        GetCarInfo.getInstance().carName = destroy.optString("carname", "");
        GetCarInfo.getInstance().carLogo = destroy.optString("carlogo", "");
        GetCarInfo.getInstance().brandid = destroy.optInt("brandid", 0);

        GetCarInfo.getInstance().maintenMiles = destroy.optInt("mainten_miles", 0);
        GetCarInfo.getInstance().maintenDate = destroy.optInt("mainten_time", 0);
        GetCarInfo.getInstance().maintenNextMiles = destroy.optInt("mainten_next_miles", 0);
        GetCarInfo.getInstance().maintenNextDate = destroy.optInt("mainten_next_date", 0);
        GetCarInfo.getInstance().isNextMain = destroy.optInt("isMainten");

        UserInfo.getInstance().userFreeze = destroy.optInt("is_freezing",1);
        UserInfo.getInstance().isAuthen = destroy.optString("is_authen");
        UserInfo.getInstance().authenName = destroy.optString("authen_name", "");
        UserInfo.getInstance().authenCard = destroy.optString("authen_card", "");
        GetCarInfo.getInstance().vin = destroy.optString("standcarno", "");

    }


}

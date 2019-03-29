package com.carlt.sesame.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.carlt.doride.DorideApplication;
import com.carlt.doride.R;
import com.carlt.doride.http.retrofitnet.model.GetCarInfo;
import com.carlt.doride.http.retrofitnet.model.UserInfo;
import com.carlt.doride.utils.MyTimeUtils;
import com.carlt.sesame.data.car.CarMainFunInfo;
import com.carlt.sesame.data.remote.RemoteMainInfo;
import com.carlt.sesame.preference.TokenInfo;

import org.json.JSONException;
import org.json.JSONObject;

public class SesameLoginInfo {

    public static final String PREF_CAR = "sesame_car_pref";


    private static SharedPreferences car_pref = DorideApplication.getAppContext()
            .getSharedPreferences(PREF_CAR, Context.MODE_PRIVATE);

    private static String remoteControl = "";

    // 最近一条日报、周报、月报信息
    private static String lately_day = "";

    private static int car_year;// 车款年限
    public final static int CAR_YEAR_2016 = 2016;
    public final static int CAR_YEAR_2018 = 2018;

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
        GetCarInfo.getInstance().deviceidstring = destroy.optString("deviceidstring", "");
        GetCarInfo.getInstance().carNO = destroy.optString("carno", "");
        GetCarInfo.getInstance().carName = destroy.optString("carname", "");
        GetCarInfo.getInstance().carLogo = destroy.optString("carlogo", "");
        GetCarInfo.getInstance().brandid = destroy.optInt("brandid", 0);

        SesameLoginInfo.setLately_day((destroy.optString("day", "")));

        GetCarInfo.getInstance().maintenMiles = destroy.optInt("mainten_miles", 0);
        GetCarInfo.getInstance().maintenDate = destroy.optInt("mainten_time", 0);
        GetCarInfo.getInstance().maintenNextMiles = destroy.optInt("mainten_next_miles", 0);
        GetCarInfo.getInstance().maintenNextDate = destroy.optInt("mainten_next_date", 0);
        GetCarInfo.getInstance().isNextMain = destroy.optInt("isMainten");

        UserInfo.getInstance().userFreeze = destroy.optInt("is_freezing",1);
        UserInfo.getInstance().isAuthen = destroy.optString("is_authen");
        UserInfo.getInstance().authenName = destroy.optString("authen_name", "");
        UserInfo.getInstance().authenCard = destroy.optString("authen_card", "");
        GetCarInfo.getInstance().standCarNo = destroy.optString("standcarno", "");

        SesameLoginInfo.setCar_year(destroy.optInt("authen_card", 0));

    }

    public static String getLately_day() {
        lately_day = car_pref.getString("lately_day", lately_day);
        return lately_day;
    }

    public static void setLately_day(String lately_day) {
        SesameLoginInfo.lately_day = lately_day;
        car_pref.edit().putString("lately_day", lately_day).commit();
    }

    public static void setRemoteControl(String control) {
        SesameLoginInfo.remoteControl = control;
        car_pref.edit().putString("remoteControl", remoteControl).commit();
    }

    public static String getRemoteControl() {
        remoteControl = car_pref.getString("remoteControl", remoteControl);
        return remoteControl;
    }

    public static int getCar_year() {
        car_year = car_pref.getInt("year", CAR_YEAR_2016);
        return car_year;
    }

    public static void setCar_year(int car_year) {
        SesameLoginInfo.car_year = car_year;
        car_pref.edit().putInt("year", car_year).commit();
    }

}

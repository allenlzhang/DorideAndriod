package com.carlt.sesame.protocolstack;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.carlt.doride.DorideApplication;
import com.carlt.sesame.data.VersionInfo;

import org.json.JSONObject;

public class VersionInfoParser extends BaseParser {
    private VersionInfo mVersionInfo = new VersionInfo();

    public VersionInfo getReturn() {
        return mVersionInfo;
    }

    @Override
    protected void parser() {
        JSONObject mJSON_data = mJson.optJSONObject("data");

        //if(mJSON_data!=null){
        mVersionInfo.setStatus(mJSON_data.optInt("status"));
        mVersionInfo.setFilepath(mJSON_data.optString("filepath", ""));
        mVersionInfo.setRemark(mJSON_data.optString("info", ""));
        mVersionInfo.setVersion(mJSON_data.optString("version", ""));
        mVersionInfo.info = mJSON_data.optString("info", "");
        mVersionInfo.latest_version = mJSON_data.optString("latest_version", "");
        saveLastVersionInfo(mVersionInfo);


    }

    public static void saveLastVersionInfo(VersionInfo s) {
        SharedPreferences sp = DorideApplication.getInstance().getSharedPreferences("UPDATEINFO", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString("info", s.getRemark());
        edit.putInt("status", s.getStatus());
        edit.putString("filepath", s.getFilepath());
        edit.putString("version", s.getVersion());
        if (!TextUtils.isEmpty(s.getRemark())) {
            edit.putString("lastversionUpdateInfo", s.getRemark());//拿到的最新一个的版本更新信息
        }
        edit.apply();
    }
}

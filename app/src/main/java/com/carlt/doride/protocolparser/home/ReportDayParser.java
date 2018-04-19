package com.carlt.doride.protocolparser.home;

import com.carlt.doride.data.BaseResponseInfo;
import com.carlt.doride.data.home.ReportDayInfo;
import com.carlt.doride.protocolparser.BaseParser;
import com.carlt.doride.utils.ILog;
import com.google.gson.JsonObject;

/**
 * Created by Marlon on 2018/3/22.
 */

public class ReportDayParser extends BaseParser<ReportDayInfo> {

    ReportDayInfo mInfo = new ReportDayInfo();

    public ReportDayParser(ResultCallback callback) {
        super(callback);
    }

    @Override
    protected void parser() {
        try {
            JsonObject mJSON_data = mJson.getAsJsonObject("data");
            if (mJSON_data != null) {
                JsonObject member = mJSON_data.getAsJsonObject("member");
                mInfo.setRealname(member.get("realname").getAsString());
                mInfo.setAvatar_img(member.get("avatar_img").getAsString());
                mInfo.setGender(member.get("gender").getAsInt());
                JsonObject report = mJSON_data.getAsJsonObject("report");
                mInfo.setId(report.get("id").getAsString());
                mInfo.setSumfuel(report.get("sumfuel").getAsString());
                mInfo.setAvgfuel(report.get("avgfuel").getAsString());
                mInfo.setSumtime(report.get("sumtime").getAsString());
                mInfo.setSummiles(report.get("summiles").getAsString());
                mInfo.setMaxspeed(report.get("maxspeed").getAsString());
                mInfo.setAvgspeed(report.get("avgspeed").getAsString());
            }
            mBaseResponseInfo.setValue(mInfo);
        }catch (Exception e){
            ILog.e(TAG, "--e==" + e);
            mBaseResponseInfo.setFlag(BaseResponseInfo.ERRO);
            mBaseResponseInfo.setInfo(MSG_ERRO);
        }
    }
}

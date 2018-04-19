package com.carlt.doride.protocolparser.home;

import com.carlt.doride.data.BaseResponseInfo;
import com.carlt.doride.data.home.ReportDayLogInfo;
import com.carlt.doride.protocolparser.BaseParser;
import com.carlt.doride.utils.ILog;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Marlon on 2018/3/23.
 */

public class ReportDayLogParser extends BaseParser<List<ReportDayLogInfo>> {
    List<ReportDayLogInfo> logInfos = new ArrayList<>();
    public ReportDayLogParser(ResultCallback callback) {
        super(callback);
    }

    @Override
    protected void parser(){
        try {
            JsonObject mJSON_data = mJson.getAsJsonObject("data");
            if (mJSON_data != null) {
                JsonArray mJSON_list = mJSON_data.getAsJsonArray("list");
                for (int i = 0; i < mJSON_list.size(); i++) {
                    ReportDayLogInfo info = new ReportDayLogInfo();
                    JsonObject object = (JsonObject) mJSON_list.get(i);
                    info.setStarttime(object.get("starttime").getAsString());
                    info.setStopTime(object.get("stopTime").getAsString());
                    info.setTime(object.get("time").getAsString());
                    info.setFuel(object.get("fuel").getAsString());
                    info.setAvgfuel(object.get("avgfuel").getAsString());
                    info.setAvgspeed(object.get("avgspeed").getAsString());
                    info.setMaxspeed(object.get("maxspeed").getAsString());
                    info.setMiles(object.get("miles").getAsString());
                    info.setGpsStartTime(object.get("gpsStartTime").getAsString());
                    info.setGpsStopTime(object.get("gpsStopTime").getAsString());
                    info.setRunSn(object.get("runSn").getAsString());
                    logInfos.add(info);
                }
                mBaseResponseInfo.setValue(logInfos);
            }

        }catch (Exception e){
            ILog.e(TAG, "--e==" + e);
            mBaseResponseInfo.setFlag(BaseResponseInfo.ERRO);
            mBaseResponseInfo.setInfo(MSG_ERRO);
        }
    }
}

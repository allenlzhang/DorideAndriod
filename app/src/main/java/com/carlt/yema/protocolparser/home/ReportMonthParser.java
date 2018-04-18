package com.carlt.yema.protocolparser.home;

import com.carlt.yema.data.BaseResponseInfo;
import com.carlt.yema.data.home.ReportMonthInfo;
import com.carlt.yema.protocolparser.BaseParser;
import com.carlt.yema.utils.ILog;
import com.google.gson.JsonObject;

/**
 * Created by Marlon on 2018/3/23.
 */

public class ReportMonthParser extends BaseParser {

    ReportMonthInfo mInfo = new ReportMonthInfo();

    public ReportMonthParser(ResultCallback callback) {
        super(callback);
    }

    @Override
    protected void parser(){
        try {
            JsonObject mJSON_data = mJson.getAsJsonObject("data");
            if (mJSON_data != null) {
                JsonObject report = mJSON_data.getAsJsonObject("report");
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

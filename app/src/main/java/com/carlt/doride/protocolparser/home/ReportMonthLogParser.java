package com.carlt.doride.protocolparser.home;

import com.carlt.doride.data.BaseResponseInfo;
import com.carlt.doride.data.home.ReportMonthLogInfo;
import com.carlt.doride.protocolparser.BaseParser;
import com.carlt.doride.utils.ILog;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Marlon on 2018/3/23.
 */

public class ReportMonthLogParser extends BaseParser<List<ReportMonthLogInfo>> {

    List<ReportMonthLogInfo> infoList = new ArrayList<>();

    public ReportMonthLogParser(ResultCallback callback) {
        super(callback);
    }

    @Override
    protected void parser(){
        try{
            JsonObject mJSON_data = mJson.getAsJsonObject("data");
            if(mJSON_data!=null){
                JsonArray mJSON_reportList = mJSON_data.getAsJsonArray("reportList");
                for (int i = 0; i <mJSON_data.size() ; i++) {
                    JsonObject report = (JsonObject) mJSON_reportList.get(i);
                    ReportMonthLogInfo mInfo = new ReportMonthLogInfo();
                    mInfo.setSumtime(report.get("sumtime").getAsString());
                    mInfo.setSumfuel(report.get("sumfuel").getAsString());
                    mInfo.setSummiles(report.get("summiles").getAsString());
                    mInfo.setDate(report.get("date").getAsString());
                    mInfo.setMonth(report.get("month").getAsString());
                    infoList.add(mInfo);
                }
                mBaseResponseInfo.setValue(infoList);
            }
        }catch (Exception e){
            ILog.e(TAG, "--e==" + e);
            mBaseResponseInfo.setFlag(BaseResponseInfo.ERRO);
            mBaseResponseInfo.setInfo(MSG_ERRO);
        }
    }
}

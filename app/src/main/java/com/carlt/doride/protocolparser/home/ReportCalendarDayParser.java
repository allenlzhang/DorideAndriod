package com.carlt.doride.protocolparser.home;

import com.carlt.doride.data.BaseResponseInfo;
import com.carlt.doride.data.home.ReportCalendarMonthInfo;
import com.carlt.doride.protocolparser.BaseParser;
import com.carlt.doride.utils.ILog;
import com.carlt.doride.utils.MyParse;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Marlon on 2018/3/26.
 */

public class ReportCalendarDayParser extends BaseParser<List<ReportCalendarMonthInfo>> {

    List<ReportCalendarMonthInfo> list = new ArrayList<>();

    public ReportCalendarDayParser(ResultCallback callback) {
        super(callback);
    }

    @Override
    protected void parser(){
        try {
            JsonArray mJSON_data = mJson.getAsJsonArray("data");
            for (int i = 0; i <mJSON_data.size() ; i++) {
                JsonObject object = (JsonObject) mJSON_data.get(i);
                ReportCalendarMonthInfo info = new ReportCalendarMonthInfo();
                info.setDate(object.get("day").getAsString());
                String avgpoint = object.get("avgpoint").getAsString();
                info.setAvgpoint(avgpoint);
                info.setPointColor(MyParse.getColorByPoint(avgpoint));
                list.add(info);
            }
            mBaseResponseInfo.setValue(list);
        }catch (Exception e){
            ILog.e(TAG, "--e==" + e);
            mBaseResponseInfo.setFlag(BaseResponseInfo.ERRO);
            mBaseResponseInfo.setInfo(MSG_ERRO);
        }
    }
}

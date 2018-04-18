package com.carlt.yema.protocolparser.home;

import android.util.Log;

import com.carlt.yema.data.BaseResponseInfo;
import com.carlt.yema.data.ReportGpsInfo;
import com.carlt.yema.protocolparser.BaseParser;
import com.carlt.yema.utils.ILog;
import com.google.gson.JsonArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Marlon on 2018/3/28.
 */

public class ReportGpsParser extends BaseParser<List<ReportGpsInfo>> {

    List<ReportGpsInfo> mGpsInfos = new ArrayList<>();

    public ReportGpsParser(ResultCallback callback) {
        super(callback);
    }

    @Override
    protected void parser(){
        try{
            JsonArray mJSON_data = mJson.getAsJsonArray("data");

            Log.i("C_DEBUG", mJson.toString().getBytes().length+">>>>>>>>>>>>>");
            if (mJSON_data != null) {
                int length = mJSON_data.size();
                for (int i = 0; i < length; i++) {
                    JsonArray mInfo=mJSON_data.get(i).getAsJsonArray();
                    ReportGpsInfo mGpsInfo=new ReportGpsInfo();
                    mGpsInfo.setLongitude(mInfo.get(0).getAsDouble());
                    mGpsInfo.setLatitude(mInfo.get(1).getAsDouble());
                    mGpsInfo.setTime_stamp(mInfo.get(2).getAsLong());
                    mGpsInfo.setSpeed(mInfo.get(3).getAsInt()+"");
                    mGpsInfo.setPositional_accuracy(mInfo.get(4).getAsInt());

                    mGpsInfos.add(mGpsInfo);
                }
                mBaseResponseInfo.setValue(mGpsInfos);
            }


        }catch (Exception e){
            ILog.e(TAG, "--e==" + e);
            mBaseResponseInfo.setFlag(BaseResponseInfo.ERRO);
            mBaseResponseInfo.setInfo(MSG_ERRO);
        }
    }
}


package com.carlt.sesame.protocolstack.career;

import android.util.Log;

import com.carlt.sesame.data.career.ReportGpsInfo;
import com.carlt.sesame.protocolstack.BaseParser;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * @author user 轨迹信息
 */
public class GpsInfoListParser extends BaseParser {
    private ArrayList<ReportGpsInfo> mGpsInfos=new ArrayList<ReportGpsInfo>();
    
    public ArrayList<ReportGpsInfo> getReturn(){
        return mGpsInfos;
    }
    
    
    public GpsInfoListParser() {
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void parser() {
        JSONArray mJSON_data = mJson.optJSONArray("data");
        
        Log.i("C_DEBUG", mJson.toString().getBytes().length+">>>>>>>>>>>>>");
        if (mJSON_data != null) {
            int length = mJSON_data.length();
            for (int i = 0; i < length; i++) {
                JSONArray mInfo=mJSON_data.optJSONArray(i);
                ReportGpsInfo mGpsInfo=new ReportGpsInfo();
                mGpsInfo.setLongitude(mInfo.optDouble(0));
                mGpsInfo.setLatitude(mInfo.optDouble(1));
                mGpsInfo.setTime_stamp(mInfo.optLong(2));
                mGpsInfo.setSpeed(mInfo.optInt(3)+"");
                mGpsInfo.setPositional_accuracy(mInfo.optInt(4));
                
                mGpsInfos.add(mGpsInfo);
            }
        }
    }
    
}

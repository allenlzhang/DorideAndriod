
package com.carlt.sesame.protocolstack.career;

import com.carlt.sesame.data.career.CarLogInfo;
import com.carlt.sesame.protocolstack.BaseParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CarLogListParser extends BaseParser {

    private ArrayList<CarLogInfo> mCarLogInfoList = new ArrayList<CarLogInfo>();

    public ArrayList<CarLogInfo> getReturn() {
        return mCarLogInfoList;
    }

    @Override
    protected void parser() throws JSONException {
        JSONObject mJSON_obj = mJson.getJSONObject("data");
        JSONArray mJSON_data = mJSON_obj.getJSONArray("list");
        int listCount = mJSON_obj.getInt("listcount");
        for (int i = 0; i < mJSON_data.length(); i++) {
            CarLogInfo mCarLogInfo = new CarLogInfo();
            JSONObject temp = (JSONObject)mJSON_data.get(i);
            mCarLogInfo.setStarttime(temp.optString("starttime"));
            mCarLogInfo.setStopTime(temp.optString("stopTime"));
            mCarLogInfo.setTime(temp.optString("time"));
            mCarLogInfo.setListCount(listCount);

            int fuel = temp.optInt("fuel");
            String s1 = "";
            if (fuel < 100) {
                s1 = fuel + "Wh";
            } else {
                double f = fuel / 1000.0;

                s1 = String.format("%.1f", f) + "kWh";
            }

            mCarLogInfo.setFuel(s1);

            double avgspeed = temp.optDouble("avgspeed");

            mCarLogInfo.setAvgspeed(String.format("%.1f", avgspeed));

            mCarLogInfo.setMaxspeed(temp.optString("maxspeed"));
            mCarLogInfo.setMiles(temp.optString("miles"));
            mCarLogInfo.setBrake(temp.optString("brake"));
            mCarLogInfo.setTurn(temp.optString("turn"));
            mCarLogInfo.setSpeedup(temp.optString("speedup"));
            mCarLogInfo.setOverspeed(temp.optString("overspeed"));

            double avgfuel = temp.optDouble("avgfuel");

            mCarLogInfo.setAvgfuel(String.format("%.1f", avgfuel));

            mCarLogInfo.setPoint(temp.optInt("point") + "");

            mCarLogInfo.setGpsStartTime(temp.optString("gpsStartTime"));
            mCarLogInfo.setGpsStopTime(temp.optString("gpsStopTime"));
            mCarLogInfo.setRunSn(temp.optString("runSn"));

            mCarLogInfoList.add(mCarLogInfo);
        }

    }

}

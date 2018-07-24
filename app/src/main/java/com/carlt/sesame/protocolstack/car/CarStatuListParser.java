
package com.carlt.sesame.protocolstack.car;

import com.carlt.sesame.data.car.CarStatuInfo;
import com.carlt.sesame.protocolstack.BaseParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CarStatuListParser extends BaseParser {

    private ArrayList<CarStatuInfo> mCarStatuList = new ArrayList<CarStatuInfo>();

    private int isrunning = -1;

    public int getIsrunning() {
        return isrunning;
    }

    public ArrayList<CarStatuInfo> getReturn() {
        return mCarStatuList;
    }

    @Override
    protected void parser() {
        try {
            JSONObject mJSON_data = mJson.getJSONObject("data");
            isrunning = mJSON_data.optInt("isrunning");
            JSONArray mJSON_list = mJSON_data.getJSONArray("list");
            for (int i = 0; i < mJSON_list.length(); i++) {
                CarStatuInfo mCarStatuInfo = new CarStatuInfo();
                JSONObject temp = (JSONObject)mJSON_list.get(i);
                mCarStatuInfo.setName(temp.optString("name"));
                mCarStatuInfo.setValue(temp.optString("value"));
                mCarStatuInfo.setUnit(temp.optString("company"));
                mCarStatuList.add(mCarStatuInfo);
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}

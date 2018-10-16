
package com.carlt.sesame.protocolstack.car;

import com.carlt.sesame.data.car.CarModeInfo;
import com.carlt.sesame.protocolstack.BaseParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 拉取车型列表（一级）
 * @author Administrator
 *
 */
public class CarModeInfoListParser extends BaseParser {

    private ArrayList<CarModeInfo> mCarModeInfoList = new ArrayList<CarModeInfo>();

    public ArrayList<CarModeInfo> getReturn() {
        return mCarModeInfoList;
    }

    @Override
    protected void parser() {
        try {
            JSONArray mArry = mJson.names();
            JSONArray mJSON_data = mJson.getJSONArray("data");
            for (int i = 0; i < mJSON_data.length(); i++) {
                CarModeInfo mCarModeInfo = new CarModeInfo();
                JSONObject temp = (JSONObject)mJSON_data.get(i);
                mCarModeInfo.setId(temp.optString("id"));
                mCarModeInfo.setPid(temp.optString("pid"));
                mCarModeInfo.setTitle(temp.optString("title"));
                mCarModeInfo.setTitle_py(temp.optString("title_py"));
                mCarModeInfo.setCarlogo(temp.optString("carlogo"));
                mCarModeInfo.setType(CarModeInfo.TYPE_FIRST);
                mCarModeInfoList.add(mCarModeInfo);
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}

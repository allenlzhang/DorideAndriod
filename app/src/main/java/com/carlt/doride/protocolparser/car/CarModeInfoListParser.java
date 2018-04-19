
package com.carlt.doride.protocolparser.car;


import com.carlt.doride.data.car.CarModeInfo;
import com.carlt.doride.protocolparser.BaseParser;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;

/**
 * 拉取车型列表（一级）
 * @author Administrator
 *
 */
public class CarModeInfoListParser extends BaseParser {

    private ArrayList<CarModeInfo> mCarModeInfoList = new ArrayList<CarModeInfo>();

    public CarModeInfoListParser(ResultCallback callback) {
        super(callback);
    }

    public ArrayList<CarModeInfo> getReturn() {
        return mCarModeInfoList;
    }

    @Override
    protected void parser() {
        JsonArray mJSON_data = mJson.getAsJsonArray("data");
        for (int i = 0; i < mJSON_data.size(); i++) {
            JsonObject modeItem=mJSON_data.get(i).getAsJsonObject();
            JsonArray modelist=modeItem.getAsJsonArray("data");
            for (int j = 0; j < modelist.size(); j++) {
                CarModeInfo mCarModeInfo = new CarModeInfo();
                JsonObject temp = (JsonObject)modelist.get(j);
                mCarModeInfo.setId(temp.get("id").getAsString());
                mCarModeInfo.setTitle(temp.get("title").getAsString());
                mCarModeInfo.setType(CarModeInfo.TYPE_FIRST);
                mCarModeInfoList.add(mCarModeInfo);
            }
        }
        mBaseResponseInfo.setValue(mCarModeInfoList);
    }
}

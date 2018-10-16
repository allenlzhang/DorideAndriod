
package com.carlt.sesame.protocolstack.car;

import com.carlt.sesame.data.car.CarModeInfo;
import com.carlt.sesame.protocolstack.BaseParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 针对获取车系列表解析(二级)
 * 
 * @author daisy
 */
public class CarModeInfoListV2Parser extends BaseParser {

    private ArrayList<CarModeInfo> mCarModeInfoList = new ArrayList<CarModeInfo>();

    public ArrayList<CarModeInfo> getReturn() {
        return mCarModeInfoList;
    }

    @Override
    protected void parser() {
        try {
            JSONArray mJSON_data = mJson.getJSONArray("data");

            int l = mJSON_data.length();
            for (int i = 0; i < l; i++) {
                JSONObject mData = (JSONObject)mJSON_data.get(i);
                CarModeInfo mInfoSub = new CarModeInfo();
                mInfoSub.setTitleSub(true);
                mInfoSub.setId(mData.optString("id"));
                mInfoSub.setTitle(mData.optString("title"));
                mInfoSub.setType(CarModeInfo.TYPE_SECOND);
                mCarModeInfoList.add(mInfoSub);

                JSONArray mList = mData.getJSONArray("data");
                if (mList != null) {
                    int len = mList.length();
                    for (int j = 0; j < len; j++) {
                        JSONObject mDataChild = (JSONObject)mList.get(j);
                        CarModeInfo mInfoChild = new CarModeInfo();
                        mInfoChild.setId(mDataChild.optString("id"));
                        mInfoChild.setTitle(mDataChild.optString("title"));
                        mInfoChild.setType(CarModeInfo.TYPE_SECOND);
                        mCarModeInfoList.add(mInfoChild);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}

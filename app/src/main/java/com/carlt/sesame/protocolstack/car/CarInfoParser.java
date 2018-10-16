
package com.carlt.sesame.protocolstack.car;

import com.carlt.sesame.data.car.CarInfo;
import com.carlt.sesame.protocolstack.BaseParser;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 获取违章车辆解析
 * @author Daisy
 *
 */
public class CarInfoParser extends BaseParser {

    private CarInfo mCarInfo = new CarInfo();

    public CarInfo getReturn() {
        return mCarInfo;
    }

    @Override
    protected void parser() {
        try {
            JSONObject mJSON_data = mJson.getJSONObject("data");
            mCarInfo.setId(mJSON_data.optString("id"));
            mCarInfo.setCityCode(mJSON_data.optString("cityCode"));
            mCarInfo.setCarNo(mJSON_data.optString("carno"));
            mCarInfo.setEngineNo(mJSON_data.optString("engineno"));
            mCarInfo.setStandcarNo(mJSON_data.optString("standcarno"));
            mCarInfo.setRegistNo(mJSON_data.optString("registno"));
            mCarInfo.setAddtime(mJSON_data.optString("addtime"));
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}

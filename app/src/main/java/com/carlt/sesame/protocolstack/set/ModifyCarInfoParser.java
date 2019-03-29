
package com.carlt.sesame.protocolstack.set;

import com.carlt.doride.http.retrofitnet.model.GetCarInfo;
import com.carlt.doride.utils.MyTimeUtils;
import com.carlt.sesame.data.SesameLoginInfo;
import com.carlt.sesame.data.car.CarMainInfo;
import com.carlt.sesame.protocolstack.BaseParser;

import org.json.JSONException;
import org.json.JSONObject;

public class ModifyCarInfoParser extends BaseParser {

    private CarMainInfo mCarMainInfo = new CarMainInfo();

    public CarMainInfo getReturn() {
        return mCarMainInfo;
    }

    @Override
    protected void parser() {
        try {
            JSONObject mJSON_data = mJson.getJSONObject("data");

            try {
                GetCarInfo.getInstance().maintenNextMiles = mJSON_data.getInt("mainten_next_miles");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                GetCarInfo.getInstance().maintenNextDate = mJSON_data.getInt("mainten_next_date");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {

                String imt = mJSON_data.getString("isNextMain");
                GetCarInfo.getInstance().isNextMain = Integer.parseInt(imt);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public String getValue(String key) {
        if (mJson != null) {

            return mJson.optString(key);
        }
        return "";

    }
}

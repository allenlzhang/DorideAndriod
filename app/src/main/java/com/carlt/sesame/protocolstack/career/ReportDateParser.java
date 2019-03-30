
package com.carlt.sesame.protocolstack.career;

import com.carlt.sesame.data.SesameLoginInfo;
import com.carlt.sesame.data.career.CareerInfo;
import com.carlt.sesame.protocolstack.BaseParser;

import org.json.JSONException;
import org.json.JSONObject;

public class ReportDateParser extends BaseParser {

    private CareerInfo mCareerInfo = new CareerInfo();

    public CareerInfo getReturn() {
        return mCareerInfo;
    }

    @Override
    protected void parser() {
        try {
            JSONObject mJSON_data = mJson.getJSONObject("data");
//            SesameLoginInfo.setLately_day(mJSON_data.optString("day"));

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}

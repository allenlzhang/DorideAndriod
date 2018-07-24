
package com.carlt.sesame.protocolstack.career;

import com.carlt.sesame.data.LoginInfo;
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
            LoginInfo.setLately_day(mJSON_data.optString("day"));
            LoginInfo.setLately_week(mJSON_data.optString("week"));
            LoginInfo.setLately_month(mJSON_data.optString("month"));

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}

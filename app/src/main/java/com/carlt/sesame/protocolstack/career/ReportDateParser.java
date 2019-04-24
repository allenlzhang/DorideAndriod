
package com.carlt.sesame.protocolstack.career;

import com.carlt.sesame.protocolstack.BaseParser;

import org.json.JSONException;
import org.json.JSONObject;

public class ReportDateParser extends BaseParser {

    private JSONObject data = new JSONObject();

    public JSONObject getReturn() {
        return data;
    }

    @Override
    protected void parser() {
        try {
            JSONObject mJSON_data = mJson.getJSONObject("data");
            data = mJSON_data;
            //            SesameLoginInfo.setLately_day(mJSON_data.optString("day"));

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}

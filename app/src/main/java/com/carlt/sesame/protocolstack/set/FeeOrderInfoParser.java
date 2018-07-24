
package com.carlt.sesame.protocolstack.set;

import com.carlt.sesame.data.set.FeeOrderInfo;
import com.carlt.sesame.protocolstack.BaseParser;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 支付订单信息解析
 * 
 * @author Administrator
 */
public class FeeOrderInfoParser extends BaseParser {

    private FeeOrderInfo mFeeOrderInfo = new FeeOrderInfo();

    public FeeOrderInfo getReturn() {
        return mFeeOrderInfo;
    }

    @Override
    protected void parser() {
        try {
            JSONObject mJSON_data = mJson.getJSONObject("data");

            mFeeOrderInfo.setParam(mJSON_data.optString("request_param", ""));
            mFeeOrderInfo.setUrl(mJSON_data.optString("notify_url", ""));

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

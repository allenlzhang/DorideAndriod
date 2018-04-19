
package com.carlt.doride.protocolparser.home;


import com.carlt.doride.data.BaseResponseInfo;
import com.carlt.doride.model.LoginInfo;
import com.carlt.doride.protocolparser.BaseParser;
import com.carlt.doride.utils.ILog;
import com.google.gson.JsonObject;


public class ReportDateParser extends BaseParser {


    public ReportDateParser(ResultCallback callback) {
        super(callback);
    }


    @Override
    protected void parser() {
        try {
            JsonObject mJSON_data = mJson.getAsJsonObject("data");
            LoginInfo.setLately_day(mJSON_data.get("day").getAsString());
            LoginInfo.setLately_week(mJSON_data.get("week").getAsString());
            LoginInfo.setLately_month(mJSON_data.get("month").getAsString());

        } catch (Exception e) {
            ILog.e(TAG, "--e==" + e);
            mBaseResponseInfo.setFlag(BaseResponseInfo.ERRO);
            mBaseResponseInfo.setInfo(MSG_ERRO);
        }

    }
    //
    // public BaseResponseInfo getBaseResponseInfo(String url, String post) {
    // try {
    // InputStream in =
    // CPApplication.ApplicationContext.getAssets().open("json1.txt");
    // mJson = new JSONObject(FileUtil.ToString(in));
    // Log.e("info", "Http响应--" + mJson);
    // mBaseResponseInfo.setFlag(mJson.getString("code"));
    // mBaseResponseInfo.setInfo(mJson.getString("msg"));
    // } catch (Exception e) {
    // Log.e("info", "BaseParser--e==" + e);
    // }
    // if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
    // parser();
    // }
    // return mBaseResponseInfo;
    //
    // }
}

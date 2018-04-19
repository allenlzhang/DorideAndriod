package com.carlt.doride.protocolparser.home;

import com.carlt.doride.data.BaseResponseInfo;
import com.carlt.doride.protocolparser.BaseParser;
import com.carlt.doride.utils.ILog;
import com.google.gson.JsonArray;

/**
 * Created by Marlon on 2018/3/28.
 */

public class RemindDefaultParser extends BaseParser {

    public RemindDefaultParser(ResultCallback callback) {
        super(callback);
    }

    @Override
    protected void parser() {

        try {
            JsonArray mJSON_data = mJson.getAsJsonArray("data");
            if (mJSON_data != null) {
                mBaseResponseInfo.setValue(mJSON_data);
            }
        } catch (Exception e) {
            ILog.e(TAG, "--e==" + e);
            mBaseResponseInfo.setFlag(BaseResponseInfo.ERRO);
            mBaseResponseInfo.setInfo(MSG_ERRO);
        }
    }
}

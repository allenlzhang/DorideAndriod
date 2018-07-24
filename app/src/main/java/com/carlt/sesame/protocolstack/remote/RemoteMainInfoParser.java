
package com.carlt.sesame.protocolstack.remote;

import com.carlt.sesame.data.remote.RemoteMainInfo;
import com.carlt.sesame.protocolstack.BaseParser;
import com.carlt.sesame.utility.MyParse;

import org.json.JSONObject;

public class RemoteMainInfoParser extends BaseParser {

    private RemoteMainInfo mRemoteMainInfo = new RemoteMainInfo();

    public RemoteMainInfo getReturn() {

        return mRemoteMainInfo;
    }

    @Override
    protected void parser() {
        JSONObject mJSON_data = mJson.optJSONObject("data");
        if (mJSON_data != null) {
            mRemoteMainInfo.setStatus(MyParse.parseInt(mJSON_data.optString("car_status", "-1")));
            String isDeviceBefore = mJSON_data.optString("before_device");
            if (isDeviceBefore != null && isDeviceBefore.equals("1")) {
                mRemoteMainInfo.setDeviceBefore(true);
            } else {
                mRemoteMainInfo.setDeviceBefore(false);
            }
        }

    }
}

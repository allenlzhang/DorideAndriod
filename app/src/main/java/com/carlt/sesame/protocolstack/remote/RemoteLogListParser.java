
package com.carlt.sesame.protocolstack.remote;

import com.carlt.sesame.data.remote.RemoteLogInfo;
import com.carlt.sesame.data.remote.RemoteLogListInfo;
import com.carlt.sesame.protocolstack.BaseParser;
import com.carlt.sesame.utility.MyParse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 远程操作记录日志解析
 * 
 * @author Administrator
 */
public class RemoteLogListParser extends BaseParser {

    private RemoteLogListInfo mRemoteLogListInfo = new RemoteLogListInfo();

    public RemoteLogListInfo getReturn() {
        return mRemoteLogListInfo;
    }

    @Override
    protected void parser() {
        try {
            JSONObject mJSON_data = mJson.getJSONObject("data");
            JSONArray mJSON_infos = mJSON_data.getJSONArray("list");
            for (int i = 0; i < mJSON_infos.length(); i++) {
                RemoteLogInfo mInfo = new RemoteLogInfo();
                JSONObject temp = (JSONObject)mJSON_infos.get(i);
                int logtype = MyParse.parseInt(temp.optString("logtype", ""));
                mInfo.setLogtype(logtype);
                mInfo.setDevice_name(temp.optString("log_device_name", ""));
                mInfo.setResult(temp.optString("log_result", ""));
                mInfo.setLogtime(temp.optString("logtime", ""));

                mRemoteLogListInfo.addmRemoteLogInfoList(mInfo);
            }
            int i = mJSON_data.optInt("has_next");
            boolean hasMore = true;
            if (i == -1) {
                hasMore = false;
            } else if (i == 1) {
                hasMore = true;
            }
            mRemoteLogListInfo.setHasMore(hasMore);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}

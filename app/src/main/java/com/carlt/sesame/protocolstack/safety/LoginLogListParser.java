package com.carlt.sesame.protocolstack.safety;

import com.carlt.sesame.data.safety.LoginLogInfo;
import com.carlt.sesame.data.safety.LoginLogListInfo;
import com.carlt.sesame.protocolstack.BaseParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 设备登录记录日志解析
 * @author Administrator
 *
 */
public class LoginLogListParser extends BaseParser {

	private LoginLogListInfo mLoginLogListInfo = new LoginLogListInfo();

	public LoginLogListInfo getReturn() {
		return mLoginLogListInfo;
	}

	@Override
	protected void parser() {
		try {
			JSONObject mJSON_data = mJson.getJSONObject("data");
			JSONArray mJSON_infos=mJSON_data.getJSONArray("list");
			for (int i = 0; i < mJSON_infos.length(); i++) {
				LoginLogInfo mInfo = new LoginLogInfo();
				JSONObject temp = (JSONObject) mJSON_infos.get(i);
				mInfo.setType(temp.optString("is_main",""));
				mInfo.setName(temp.optString("login_device_name",""));
				mInfo.setModel(temp.optString("login_model",""));
				mInfo.setTime(temp.optString("login_time",""));
				
				mLoginLogListInfo.addmLoginLogInfoList(mInfo);
			}
			int i=mJSON_data.optInt("has_next");
			boolean hasMore = true;
			if(i==-1){
			    hasMore=false;
			}else if(i==1){
			    hasMore=true;
			}
			mLoginLogListInfo.setHasMore(hasMore);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}

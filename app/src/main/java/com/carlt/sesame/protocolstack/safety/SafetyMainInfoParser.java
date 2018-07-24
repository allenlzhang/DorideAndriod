package com.carlt.sesame.protocolstack.safety;

import com.carlt.sesame.data.safety.SafetyMainInfo;
import com.carlt.sesame.protocolstack.BaseParser;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 主设备授权页面信息解析
 * @author Administrator
 *
 */

public class SafetyMainInfoParser extends BaseParser {

	private SafetyMainInfo mSafetyMainInfo = new SafetyMainInfo();

	public SafetyMainInfo getReturn() {
		return mSafetyMainInfo;
	}

	@Override
	protected void parser() {
		try {
			JSONObject mJSON_data = mJson.getJSONObject("data");
			String has_authorize=mJSON_data.optString("has_authorize","");
			
			if(has_authorize.equals("1")){
			    mSafetyMainInfo.setHasAuthorize(true);
			}else if(has_authorize.equals("0")){
			    mSafetyMainInfo.setHasAuthorize(false);
			}
			
			String lesspwd_switch=mJSON_data.optString("lesspwd_switch","");
			if(lesspwd_switch.equals("1")){
                mSafetyMainInfo.setNoneedpsw(true);
            }else if(has_authorize.equals("0")){
                mSafetyMainInfo.setNoneedpsw(false);
            }
			
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}
}

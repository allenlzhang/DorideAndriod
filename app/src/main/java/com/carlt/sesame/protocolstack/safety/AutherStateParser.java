package com.carlt.sesame.protocolstack.safety;

import com.carlt.sesame.data.safety.AuthorResultInfo;
import com.carlt.sesame.protocolstack.BaseParser;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 子设备获取授权状态
 * @author Administrator
 *
 */
public class AutherStateParser extends BaseParser {

	private AuthorResultInfo mAutherResultInfo=new AuthorResultInfo();

	public AuthorResultInfo getReturn() {
		return mAutherResultInfo;
	}

	@Override
	protected void parser() {
		try {
		    JSONObject mJSON_data = mJson.getJSONObject("data");
		    if(mJSON_data!=null){
		        mAutherResultInfo.setResult_status(mJSON_data.optString("isallow",""));
		    }
			
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}
}

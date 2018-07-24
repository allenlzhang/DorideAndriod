package com.carlt.sesame.protocolstack.career;

import com.carlt.sesame.data.car.CheckFaultInfo;
import com.carlt.sesame.protocolstack.BaseParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DiagnoseListParser extends BaseParser {

	private ArrayList<CheckFaultInfo> mCheckFaultInfoList = new ArrayList<CheckFaultInfo>();

	public ArrayList<CheckFaultInfo> getReturn() {
		return mCheckFaultInfoList;
	}

	@Override
	protected void parser() {
		try {
			JSONArray mJSON_data = mJson.getJSONArray("data");
			for (int i = 0; i < mJSON_data.length(); i++) {
				CheckFaultInfo mCheckFaultInfo = new CheckFaultInfo();
				JSONObject temp = (JSONObject) mJSON_data.get(i);
				String code = temp.optString("code");
				mCheckFaultInfo.setCode(code);
				mCheckFaultInfo.setCn(temp.optString("cn"));
				mCheckFaultInfo.setEn(temp.optString("en"));
				mCheckFaultInfo.setScope(temp.optString("scope"));
				mCheckFaultInfo.setContent(temp.optString("content"));
				mCheckFaultInfoList.add(mCheckFaultInfo);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}

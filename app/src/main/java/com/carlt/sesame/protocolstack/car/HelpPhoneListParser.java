package com.carlt.sesame.protocolstack.car;

import com.carlt.sesame.data.car.HelpPhoneInfo;
import com.carlt.sesame.protocolstack.BaseParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HelpPhoneListParser extends BaseParser {

	private ArrayList<HelpPhoneInfo> mHelpPhoneInfoList = new ArrayList<HelpPhoneInfo>();

	public ArrayList<HelpPhoneInfo> getReturn() {
		return mHelpPhoneInfoList;
	}

	@Override
	protected void parser() {
		try {
			HelpPhoneInfo mHelpPhoneInfo;
			JSONArray mJSON_data = mJson.getJSONArray("data");
			for (int i = 0; i < mJSON_data.length(); i++) {
				JSONObject temp = (JSONObject) mJSON_data.get(i);
				mHelpPhoneInfo = new HelpPhoneInfo();
				mHelpPhoneInfo.setFlag(true);
				mHelpPhoneInfo.setName(temp.optString("type"));
				mHelpPhoneInfoList.add(mHelpPhoneInfo);
				JSONArray mJSON_list = temp.getJSONArray("list");
				for (int j = 0; j < mJSON_list.length(); j++) {
					JSONObject temp1 = (JSONObject) mJSON_list.get(j);
					mHelpPhoneInfo = new HelpPhoneInfo();
					mHelpPhoneInfo.setFlag(false);
					mHelpPhoneInfo.setName(temp1.optString("name"));
					mHelpPhoneInfo.setPhone(temp1.optString("mobile"));
					mHelpPhoneInfoList.add(mHelpPhoneInfo);
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}

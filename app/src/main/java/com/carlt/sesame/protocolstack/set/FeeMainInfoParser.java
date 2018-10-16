package com.carlt.sesame.protocolstack.set;

import com.carlt.sesame.data.set.FeeMainInfo;
import com.carlt.sesame.data.set.FeeTypeInfo;
import com.carlt.sesame.protocolstack.BaseParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FeeMainInfoParser extends BaseParser {

	private FeeMainInfo mFeeMainInfo = new FeeMainInfo();

	private ArrayList<FeeTypeInfo> mFeeTypeInfos = new ArrayList<FeeTypeInfo>();

	public FeeMainInfo getReturn() {
		return mFeeMainInfo;
	}

	@Override
	protected void parser() {
		try {
			JSONObject mJSON_data = mJson.getJSONObject("data");

			mFeeMainInfo.setServiceEndDate(mJSON_data.optString("serviceEndDate", ""));
			String timeIsRed = mJSON_data.optString("timeIsRed", "");
			if (timeIsRed.equals("1")) {
				mFeeMainInfo.setTimeIsRed(true);
			} else if (timeIsRed.equals("0")) {
				mFeeMainInfo.setTimeIsRed(false);
			}

			JSONArray mJson_list = mJSON_data.getJSONArray("list");
			if (mJson_list != null) {
				int length = mJson_list.length();
				for (int i = 0; i < length; i++) {
					JSONObject mJsonObject = mJson_list.getJSONObject(i);
					FeeTypeInfo mTypeInfo = new FeeTypeInfo();
					mTypeInfo.setName(mJsonObject.optString("renew_year", ""));
					mTypeInfo.setCost(mJsonObject.optString("renew_cost", ""));

					mFeeTypeInfos.add(mTypeInfo);
				}
			}
			mFeeMainInfo.setmFeeTypeInfos(mFeeTypeInfos);

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

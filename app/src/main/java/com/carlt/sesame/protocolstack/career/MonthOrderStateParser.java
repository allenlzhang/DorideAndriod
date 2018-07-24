package com.carlt.sesame.protocolstack.career;

import com.carlt.sesame.data.career.MonthOrderStateInfo;
import com.carlt.sesame.protocolstack.BaseParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MonthOrderStateParser extends BaseParser {

	private ArrayList<MonthOrderStateInfo> mMonthOrderStateInfoList = new ArrayList<MonthOrderStateInfo>();

	public ArrayList<MonthOrderStateInfo> getReturn() {
		return mMonthOrderStateInfoList;
	}

	@Override
	protected void parser() {
		try {
			JSONArray mJSON_data = mJson.getJSONArray("data");
			for (int i = 0; i < mJSON_data.length(); i++) {
				MonthOrderStateInfo mMonthOrderStateInfo = new MonthOrderStateInfo();
				JSONObject temp = (JSONObject) mJSON_data.get(i);
				mMonthOrderStateInfo.setId(temp.optString("id"));
				mMonthOrderStateInfo.setDate(temp.optString("date"));
				mMonthOrderStateInfo.setLeft(temp.optInt("left"));
				mMonthOrderStateInfoList.add(mMonthOrderStateInfo);

			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}

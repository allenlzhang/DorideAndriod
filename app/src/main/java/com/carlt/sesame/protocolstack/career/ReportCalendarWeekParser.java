package com.carlt.sesame.protocolstack.career;

import com.carlt.sesame.data.career.ReportCalendarWeekInfo;
import com.carlt.sesame.protocolstack.BaseParser;
import com.carlt.sesame.utility.MyParse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ReportCalendarWeekParser extends BaseParser {
	private ArrayList<ReportCalendarWeekInfo> mWeekInfoList = new ArrayList<ReportCalendarWeekInfo>();

	@Override
	public ArrayList<ReportCalendarWeekInfo> getReturn() {
		return mWeekInfoList;
	}

	@Override
	protected void parser() {
		try {
			JSONArray mJSON_data = mJson.getJSONArray("data");
			for (int i = 0; i < mJSON_data.length(); i++) {
				JSONObject temp = (JSONObject) mJSON_data.get(i);
				ReportCalendarWeekInfo mWeekInfo = new ReportCalendarWeekInfo();
				mWeekInfo.setStartDay(temp.optString("sunday"));
				mWeekInfo.setEndDay(temp.optString("saturday"));
				mWeekInfo.setId(temp.optString("yearweek"));

				String avgpoint = temp.optString("avgpoint");
				mWeekInfo.setPoint(avgpoint);
				mWeekInfo.setPointColor(MyParse.getColorByPoint(avgpoint));

				mWeekInfoList.add(mWeekInfo);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}

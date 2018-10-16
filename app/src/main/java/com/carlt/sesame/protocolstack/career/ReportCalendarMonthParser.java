package com.carlt.sesame.protocolstack.career;

import com.carlt.sesame.data.career.ReportCalendarMonthInfo;
import com.carlt.sesame.protocolstack.BaseParser;
import com.carlt.sesame.utility.MyParse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ReportCalendarMonthParser extends BaseParser {
	private ArrayList<ReportCalendarMonthInfo> mCalendar = new ArrayList<ReportCalendarMonthInfo>();

	@Override
	public ArrayList<ReportCalendarMonthInfo> getReturn() {
		return mCalendar;
	}

	@Override
	protected void parser() {
		try {
			JSONArray mJSON_data = mJson.getJSONArray("data");
			for (int i = 0; i < mJSON_data.length(); i++) {
				JSONObject temp = (JSONObject) mJSON_data.get(i);
				ReportCalendarMonthInfo object = new ReportCalendarMonthInfo();
				int month = temp.optInt("month");
				String avgpoint = temp.optString("avgpoint");
				object.setDate(month);
				object.setAvgpoint(avgpoint);
				object.setPointColor(MyParse.getColorByPoint(avgpoint));
				mCalendar.add(object);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}

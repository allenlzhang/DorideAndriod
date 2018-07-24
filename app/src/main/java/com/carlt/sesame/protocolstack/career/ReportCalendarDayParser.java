package com.carlt.sesame.protocolstack.career;

import com.carlt.sesame.data.career.ReportCalendarDayInfo;
import com.carlt.sesame.protocolstack.BaseParser;
import com.carlt.sesame.utility.MyParse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ReportCalendarDayParser extends BaseParser {
	private ArrayList<ReportCalendarDayInfo> mCalendar = new ArrayList<ReportCalendarDayInfo>();

	@Override
	public ArrayList<ReportCalendarDayInfo> getReturn() {
		return mCalendar;
	}

	@Override
	protected void parser() {
		try {
			JSONArray mJSON_data = mJson.getJSONArray("data");
			for (int i = 0; i < mJSON_data.length(); i++) {
				JSONObject temp = (JSONObject) mJSON_data.get(i);
				ReportCalendarDayInfo info = new ReportCalendarDayInfo();
				info.setDate(temp.optString("day"));
				String avgpoint = temp.optString("avgpoint");
				info.setAvgpoint(avgpoint);
				info.setPointColor(MyParse.getColorByPoint(avgpoint));

				mCalendar.add(info);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}

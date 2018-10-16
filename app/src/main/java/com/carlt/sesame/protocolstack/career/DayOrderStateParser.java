package com.carlt.sesame.protocolstack.career;

import com.carlt.sesame.data.career.DayOrderStateInfo;
import com.carlt.sesame.protocolstack.BaseParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class DayOrderStateParser extends BaseParser {

	private ArrayList<DayOrderStateInfo> mDayOrderStateInfoList = new ArrayList<DayOrderStateInfo>();

	public ArrayList<DayOrderStateInfo> getReturn() {
		return mDayOrderStateInfoList;
	}

	@Override
	protected void parser() {
		try {
			JSONArray mJSON_data = mJson.getJSONObject("data").getJSONArray(
					"content");
			for (int i = 0; i < mJSON_data.length(); i++) {
				DayOrderStateInfo mDayOrderStateInfo = new DayOrderStateInfo();
				JSONObject temp = (JSONObject) mJSON_data.get(i);
				String time = temp.optString("time");
				mDayOrderStateInfo.setTime(time);
				try {
					double p = Double.parseDouble(time.split("-")[0].replace(
							":", "."));
					mDayOrderStateInfo.setPreference(p);
				} catch (Exception e) {
					// TODO: handle exception
				}
				mDayOrderStateInfo.setTotal(temp.optInt("total"));
				mDayOrderStateInfo.setUsed(temp.optInt("used"));
				mDayOrderStateInfoList.add(mDayOrderStateInfo);
			}
			DayOrderStateInfo mDayOrderStateInfo = new DayOrderStateInfo();
			String time = "上午";
			double p = 0;
			mDayOrderStateInfo.setTime(time);
			mDayOrderStateInfo.setPreference(p);
			mDayOrderStateInfo.setFlag(true);
			mDayOrderStateInfoList.add(mDayOrderStateInfo);

			mDayOrderStateInfo = new DayOrderStateInfo();
			time = "下午";
			p = 12.61;
			mDayOrderStateInfo.setTime(time);
			mDayOrderStateInfo.setPreference(p);
			mDayOrderStateInfo.setFlag(true);
			mDayOrderStateInfoList.add(mDayOrderStateInfo);

			// 排序
			Collections.sort(mDayOrderStateInfoList, comparator);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	Comparator<DayOrderStateInfo> comparator = new Comparator<DayOrderStateInfo>() {
		public int compare(DayOrderStateInfo s1, DayOrderStateInfo s2) {

			if (s1.getPreference() > s2.getPreference()) {
				return 1;
			}
			return -1;
		}
	};

}

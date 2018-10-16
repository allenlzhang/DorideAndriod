package com.carlt.sesame.protocolstack.car;

import com.carlt.sesame.data.car.MaintainLogInfo;
import com.carlt.sesame.protocolstack.BaseParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RecommendMaintainParser extends BaseParser {
	private ArrayList<MaintainLogInfo> mList = new ArrayList<MaintainLogInfo>();

	public ArrayList<MaintainLogInfo> getReturn() {
		return mList;
	}

	@Override
	protected void parser() {
		try {

			JSONArray mJSON_data = mJson.getJSONArray("data");
			for (int i = 0; i < mJSON_data.length(); i++) {
				MaintainLogInfo info = new MaintainLogInfo();
				JSONObject temp = (JSONObject) mJSON_data.get(i);
				info.setTitle(temp.optString("title"));
				info.setRemarks(temp.optString("remarks"));
				info.setNextMiles(temp.optString("nextMiles"));
				info.setNextDate(temp.optString("nextDate"));
				info.setIsCommend(temp.optInt("isCommend"));
				info.setIsOpen(MaintainLogInfo.CLOSE);
				mList.add(info);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

}

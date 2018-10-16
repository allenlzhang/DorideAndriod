package com.carlt.sesame.protocolstack.set;

import com.carlt.sesame.data.set.PushSetInfo;
import com.carlt.sesame.protocolstack.BaseParser;

import org.json.JSONException;
import org.json.JSONObject;

public class PushSetParser extends BaseParser {

	private PushSetInfo mPushSetInfo = new PushSetInfo();

	public PushSetInfo getReturn() {
		return mPushSetInfo;
	}

	@Override
	protected void parser() {
		try {
			JSONObject mJSON_data = mJson.getJSONObject("data");
			mPushSetInfo.setStartup(mJSON_data.optInt("startup"));
			mPushSetInfo.setDayreport(mJSON_data.optInt("dayreport"));
			mPushSetInfo.setWeekreport(mJSON_data.optInt("weekreport"));
			mPushSetInfo.setMonthreport(mJSON_data.optInt("monthreport"));
			mPushSetInfo.setSecurity(mJSON_data.optInt("security"));
			mPushSetInfo.setTrouble(mJSON_data.optInt("trouble"));
			mPushSetInfo.setDealer(mJSON_data.optInt("dealer"));

			mPushSetInfo.setLostconnect(mJSON_data.optInt("lostconnect"));
			mPushSetInfo.setGotmedal(mJSON_data.optInt("gotmedal"));
			mPushSetInfo.setNewrecord(mJSON_data.optInt("newrecord"));
			mPushSetInfo.setLicense(mJSON_data.optInt("license"));
			mPushSetInfo.setVibstrength(mJSON_data.optInt("vibstrength"));
			mPushSetInfo.setBuzz(mJSON_data.optInt("DBBuzzerSw"));

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}

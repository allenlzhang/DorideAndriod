package com.carlt.sesame.protocolstack.car;

import com.carlt.sesame.data.SesameLoginInfo;
import com.carlt.sesame.data.car.CarMainInfo;
import com.carlt.sesame.protocolstack.BaseParser;

import org.json.JSONException;
import org.json.JSONObject;

public class CarMainParser extends BaseParser {

	private CarMainInfo mCarMainInfo = new CarMainInfo();

	public CarMainInfo getReturn() {
		return mCarMainInfo;
	}

	@Override
	protected void parser() {
		try {
			JSONObject mJSON_data = mJson.getJSONObject("data");

			int tireable = mJSON_data.optInt("tireable");
			if (tireable == 0) {
				mCarMainInfo.setTireable(false);
			} else {
				mCarMainInfo.setTireable(true);
			}

			String isRuning=mJSON_data.optString("isrunning");
			mCarMainInfo.setRunning(isRuning);
			mCarMainInfo.charging_status=mJSON_data.optString("charging_status");

			mCarMainInfo.setSoc(mJSON_data.optString("SOC"));
			mCarMainInfo.setSoh(mJSON_data.optString("SOH"));
			
			int tirepressure = mJSON_data.optInt("tirepressure");
			mCarMainInfo.setTirepressure(tirepressure);
			mCarMainInfo.setSafetycount(mJSON_data.optInt("safetycount"));
			mCarMainInfo.setSafetymsg(mJSON_data.optString("safetymsg"));
			mCarMainInfo
					.setLastchecktime(mJSON_data.optString("lastchecktime"));
			mCarMainInfo.setLastcheckscore(mJSON_data
					.optString("lastcheckscore"));
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}
}

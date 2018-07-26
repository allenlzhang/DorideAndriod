package com.carlt.sesame.protocolstack.set;

import com.carlt.doride.DorideApplication;
import com.carlt.doride.R;
import com.carlt.sesame.data.LoginInfo;
import com.carlt.sesame.data.car.CarMainInfo;
import com.carlt.sesame.protocolstack.BaseParser;

import org.json.JSONException;
import org.json.JSONObject;

public class SetCarInfoParser extends BaseParser {

	private CarMainInfo mCarMainInfo = new CarMainInfo();

	public CarMainInfo getReturn() {
		return mCarMainInfo;
	}

	@Override
	protected void parser() {
		try {
			JSONObject mJSON_data = mJson.getJSONObject("data");
			LoginInfo.setCarname(mJSON_data.optString("carname"));
			LoginInfo.setCarlogo(mJSON_data.optString("carlogo"));
			LoginInfo.setBrandid(mJSON_data.optString("brandid"));
			LoginInfo.setOptionid(mJSON_data.optString("optionid"));
			LoginInfo.setCarid(mJSON_data.optString("carid"));
			LoginInfo.setDeviceidstring(mJSON_data.optString("deviceidstring"));

			LoginInfo.setDealerId(mJSON_data.optString("dealerid"));
			int secretaryid = mJSON_data.optInt("secretaryid", 1);
			if (secretaryid == 1) {
				LoginInfo.setSecretaryImg(R.drawable.secretary_female);
				LoginInfo.setSecretaryName(DorideApplication.ApplicationContext.getResources().getString(R.string.register_secretary_girl));

			} else {
				LoginInfo.setSecretaryImg(R.drawable.secretary_male);
				LoginInfo.setSecretaryName(DorideApplication.ApplicationContext.getResources().getString(R.string.register_secretary_boy));
			}

			int membercarId = mJSON_data.optInt("id");
			LoginInfo.setBindCar(membercarId > 0);

			String isSupSpecFunc = mJSON_data.optString("isSupSpecFunc", "");
			LoginInfo.setSupport(isSupSpecFunc.equals("1"));
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}
}

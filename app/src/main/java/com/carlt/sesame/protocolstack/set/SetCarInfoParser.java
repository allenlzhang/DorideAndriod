package com.carlt.sesame.protocolstack.set;

import com.carlt.doride.DorideApplication;
import com.carlt.doride.R;
import com.carlt.sesame.data.SesameLoginInfo;
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
			SesameLoginInfo.setCarname(mJSON_data.optString("carname"));
			SesameLoginInfo.setCarlogo(mJSON_data.optString("carlogo"));
			SesameLoginInfo.setBrandid(mJSON_data.optString("brandid"));
			SesameLoginInfo.setOptionid(mJSON_data.optString("optionid"));
			SesameLoginInfo.setCarid(mJSON_data.optString("carid"));
			SesameLoginInfo.setDeviceidstring(mJSON_data.optString("deviceidstring"));

			SesameLoginInfo.setDealerId(mJSON_data.optString("dealerid"));
			int secretaryid = mJSON_data.optInt("secretaryid", 1);
			if (secretaryid == 1) {
				SesameLoginInfo.setSecretaryImg(R.drawable.secretary_female);
				SesameLoginInfo.setSecretaryName(DorideApplication.ApplicationContext.getResources().getString(R.string.register_secretary_girl));

			} else {
				SesameLoginInfo.setSecretaryImg(R.drawable.secretary_male);
				SesameLoginInfo.setSecretaryName(DorideApplication.ApplicationContext.getResources().getString(R.string.register_secretary_boy));
			}

			int membercarId = mJSON_data.optInt("id");
			SesameLoginInfo.setBindCar(membercarId > 0);

			String isSupSpecFunc = mJSON_data.optString("isSupSpecFunc", "");
			SesameLoginInfo.setSupport(isSupSpecFunc.equals("1"));
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}
}

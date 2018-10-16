package com.carlt.sesame.protocolstack.car;

import com.carlt.sesame.data.car.TirepressureInfo;
import com.carlt.sesame.protocolstack.BaseParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TirepressureParser extends BaseParser {

	private TirepressureInfo mTirepressureInfo = new TirepressureInfo();

	public TirepressureInfo getReturn() {
		return mTirepressureInfo;
	}

	@Override
	protected void parser() {
		try {
			JSONObject mJSON_date = mJson.getJSONObject("data");
			String logdate = mJSON_date.optString("logdate");
			mTirepressureInfo.setTime(logdate);
			mTirepressureInfo
					.setTirepressure(mJSON_date.optInt("tirepressure"));
			mTirepressureInfo.setProgress(mJSON_date.optInt("progress"));

			JSONArray mJSON_list = mJSON_date.getJSONArray("list");
			for (int i = 0; i < mJSON_list.length(); i++) {
				JSONObject mTirp = mJSON_list.getJSONObject(i);
				int s = mTirp.optInt("abnormal");
				int c = mTirp.optInt("coefficient");
				switch (mTirp.getInt("position")) {
				case 1:
					mTirepressureInfo.setState1(s);
					mTirepressureInfo.setCoefficient1(c);
					// mTirepressureInfo.setWs1(w);
					break;
				case 2:
					mTirepressureInfo.setState2(s);
					mTirepressureInfo.setCoefficient2(c);
					// mTirepressureInfo.setWs2(w);
					break;
				case 3:
					mTirepressureInfo.setState3(s);
					mTirepressureInfo.setCoefficient3(c);
					// mTirepressureInfo.setWs3(w);

					break;
				case 4:
					mTirepressureInfo.setState4(s);
					mTirepressureInfo.setCoefficient4(c);
					// mTirepressureInfo.setWs4(w);
					break;
				}

			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


}

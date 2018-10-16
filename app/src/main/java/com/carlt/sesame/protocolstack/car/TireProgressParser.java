package com.carlt.sesame.protocolstack.car;

import com.carlt.sesame.data.car.TirepressureInfo;
import com.carlt.sesame.protocolstack.BaseParser;

import org.json.JSONException;
import org.json.JSONObject;

public class TireProgressParser extends BaseParser {

	private TirepressureInfo mTirepressureInfo = new TirepressureInfo();

	public TirepressureInfo getReturn() {
		return mTirepressureInfo;
	}

	@Override
	protected void parser() {
		try {
			JSONObject mJSON_date = mJson.getJSONObject("data");
			mTirepressureInfo
					.setTirepressure(mJSON_date.optInt("tirepressure"));
			mTirepressureInfo.setProgress(mJSON_date.optInt("progress"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


}

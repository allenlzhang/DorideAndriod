package com.carlt.sesame.protocolstack.car;

import com.carlt.sesame.data.car.CarMainInfo;
import com.carlt.sesame.data.car.TirepressureInfo;
import com.carlt.sesame.protocolstack.BaseParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Daisy
 */

public class TiredirectParser extends BaseParser {

	private TirepressureInfo mTirepressureInfo = new TirepressureInfo();

	public TirepressureInfo getReturn() {
		return mTirepressureInfo;
	}

	@Override
	protected void parser() {
		try {
			JSONArray mJSON_list = mJson.getJSONArray("data");
			boolean isNormal=true;//胎压是否正常
			for (int i = 0; i < mJSON_list.length(); i++) {
				JSONObject mTirp = mJSON_list.getJSONObject(i);
				int s = mTirp.optInt("pressure_status");
				if(s==0){
					isNormal=false;
					s=1;
				}else if(s==1){
					s=0;
				}
				int c = mTirp.optInt("pressure_value");
				String unit=mTirp.optString("pressure_unit");
				switch (i) {
				case 0:
					mTirepressureInfo.setState1(s);
					mTirepressureInfo.setCoefficient1(c);
					mTirepressureInfo.setUnit1(unit);
					break;
				case 1:
					mTirepressureInfo.setState2(s);
					mTirepressureInfo.setCoefficient2(c);
					mTirepressureInfo.setUnit2(unit);
					break;
				case 2:
					mTirepressureInfo.setState3(s);
					mTirepressureInfo.setCoefficient3(c);
					mTirepressureInfo.setUnit3(unit);

					break;
				case 3:
					mTirepressureInfo.setState4(s);
					mTirepressureInfo.setCoefficient4(c);
					mTirepressureInfo.setUnit4(unit);
					break;
				}

			}
			if(isNormal){
				mTirepressureInfo.setTirepressure(CarMainInfo.NORMAL);
			}else{
			mTirepressureInfo.setTirepressure(CarMainInfo.ABNORMAL);}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


}

package com.carlt.sesame.protocolstack.car;

import com.carlt.sesame.data.car.CarInfo;
import com.carlt.sesame.protocolstack.BaseParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 违章车辆列表解析
 * @author Daisy
 *
 */
public class CarInfoListParser extends BaseParser {

	private ArrayList<CarInfo> mCarInfos = new ArrayList<CarInfo>();

	public ArrayList<CarInfo> getReturn() {
		return mCarInfos;
	}

	@Override
	protected void parser() {
		try {
			JSONArray mJSON_list = mJson.getJSONArray("data");
			for (int i = 0; i < mJSON_list.length(); i++) {
				CarInfo mCarInfo = new CarInfo();
				JSONObject temp = (JSONObject) mJSON_list.get(i);
				mCarInfo.setId(temp.optString("id"));
				mCarInfo.setCityCode(temp.optString("cityCode"));
				mCarInfo.setCarNo(temp.optString("carno"));
				mCarInfo.setCityName(temp.optString("cityName"));
				mCarInfo.setEngineNo(temp.optString("engineno"));
				mCarInfo.setStandcarNo(temp.optString("standcarno"));
				mCarInfo.setRegistNo(temp.optString("registno"));
				mCarInfo.setType(temp.optString("ismycar"));
				mCarInfo.setAddtime(temp.optString("addtime"));
				mCarInfos.add(mCarInfo);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}

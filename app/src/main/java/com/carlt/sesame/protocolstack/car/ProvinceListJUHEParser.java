package com.carlt.sesame.protocolstack.car;

import com.carlt.sesame.control.DaoControl;
import com.carlt.sesame.data.car.ProvinceInfo;
import com.carlt.sesame.data.set.CityStringInfo;
import com.carlt.sesame.utility.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class ProvinceListJUHEParser {

	private ArrayList<ProvinceInfo> mProvinceInfoList = new ArrayList<ProvinceInfo>();

	public ArrayList<ProvinceInfo> getmProvinceInfoList() {
		return mProvinceInfoList;
	}

	public ProvinceListJUHEParser() {
		try {
			DaoControl dao = DaoControl.getInstance();
			ArrayList<CityStringInfo> list = dao.getCityStringInfoList();
			String json = "";

			if (list.size() > 0) {
				json = list.get(0).getTxt();
				JSONObject mJson = new JSONObject(json);
				JSONObject mJSON_data = mJson.getJSONObject("result");
				JSONArray arry_codes = mJSON_data.names();
				for (int i = 0; i < arry_codes.length(); i++) {
					ProvinceInfo mProvinceInfo = new ProvinceInfo();
					String code = arry_codes.get(i).toString();
					JSONObject mJSON_province = mJSON_data.getJSONObject(code);
					String name = mJSON_province.getString("province");
					mProvinceInfo.setCode(code);
					mProvinceInfo.setName(name);
					mProvinceInfoList.add(mProvinceInfo);
				}
			}
		} catch (Exception e) {
			Log.e("info", "BaseParser--e==" + e);
		}
	}

}

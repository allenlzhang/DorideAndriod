package com.carlt.sesame.protocolstack.car;

import com.carlt.doride.DorideApplication;
import com.carlt.sesame.control.DaoControl;
import com.carlt.sesame.data.car.CityInfo;
import com.carlt.sesame.data.set.CityStringInfo;
import com.carlt.sesame.utility.FileUtil;
import com.carlt.sesame.utility.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class AllCityInfoListParser {

	private HashMap<String, CityInfo> mtempMap = new HashMap<String, CityInfo>();

	public HashMap<String, CityInfo> getmCityInfoList() {
		return mtempMap;
	}

	public AllCityInfoListParser() {
		try {
			String json = "";
			InputStream in = null;
			DaoControl dao = DaoControl.getInstance();
			ArrayList<CityStringInfo> list = dao.getCityStringInfoList();
			if (list.size() > 0) {
				json = list.get(0).getTxt();
			} else {
				in = DorideApplication.ApplicationContext.getAssets().open("json_province.txt");
				json = FileUtil.ToString(in);
			}

			JSONObject mJson_All = new JSONObject(json);
			JSONObject mJSON_data_ALL = mJson_All.getJSONObject("result");

			JSONArray arry_codes = mJSON_data_ALL.names();
			for (int i = 0; i < arry_codes.length(); i++) {
				String code = arry_codes.get(i).toString();
				JSONObject mJSON_province_temp = mJSON_data_ALL.getJSONObject(code);
				JSONArray mJSON_cityArry_temp = mJSON_province_temp.getJSONArray("citys");
				for (int k = 0; k < mJSON_cityArry_temp.length(); k++) {
					JSONObject city = (JSONObject) mJSON_cityArry_temp.get(k);
					CityInfo mCityInfo = new CityInfo();
					mCityInfo.setName(city.optString("city_name"));
					mCityInfo.setCode(city.optString("city_code"));
					mCityInfo.setAbbr(city.optString("abbr"));
					mCityInfo.setEngine(city.optString("engine"));
					mCityInfo.setEngineno(city.optString("engineno"));
					mCityInfo.setClassa(city.optString("class"));
					mCityInfo.setClassno(city.optString("classno"));
					mCityInfo.setRegist(city.optString("regist"));
					mCityInfo.setRegistno(city.optString("registno"));
					mtempMap.put(city.optString("city_code"), mCityInfo);
				}
			}

		} catch (Exception e) {
			Log.e("info", "BaseParser--e==" + e);
		}
	}

}

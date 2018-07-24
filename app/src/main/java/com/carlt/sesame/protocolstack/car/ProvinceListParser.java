package com.carlt.sesame.protocolstack.car;

import com.carlt.sesame.control.DaoControl;
import com.carlt.sesame.data.car.ProvinceInfo;
import com.carlt.sesame.data.set.CityStringInfo;
import com.carlt.sesame.utility.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ProvinceListParser {

	private ArrayList<ProvinceInfo> mProvinceInfoList = new ArrayList<ProvinceInfo>();

	public ArrayList<ProvinceInfo> getmProvinceInfoList() {
		return mProvinceInfoList;
	}

	public ProvinceListParser() {
		try {
			DaoControl dao = DaoControl.getInstance();
			ArrayList<CityStringInfo> list = dao.getCity2StringInfoList();
			String json = "";

			if(list.size() > 0){
				json = list.get(0).getTxt();
				JSONArray jarray = new JSONArray(json);
				for (int i = 0; i < jarray.length(); i++) {
					ProvinceInfo mProvinceInfo = new ProvinceInfo();
					JSONObject obj = jarray.getJSONObject(i);
					mProvinceInfo.setCode(obj.optString("ProvinceID"));
					mProvinceInfo.setName(obj.optString("ProvinceName"));
					mProvinceInfo.setProvincePrefix(obj.optString("ProvincePrefix"));
					mProvinceInfoList.add(mProvinceInfo);
				}
			}
		} catch (Exception e) {
			Log.e("info", "BaseParser--e==" + e);
		}
	}

}

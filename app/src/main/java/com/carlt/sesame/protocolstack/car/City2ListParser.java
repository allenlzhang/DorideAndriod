package com.carlt.sesame.protocolstack.car;

import android.text.TextUtils;

import com.carlt.sesame.control.DaoControl;
import com.carlt.sesame.data.car.CityInfo;
import com.carlt.sesame.data.set.CityStringInfo;
import com.carlt.sesame.utility.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * @author Yyun
 * 
 *         车行易
 *
 */
public class City2ListParser {

	private ArrayList<CityInfo> mCityInfoList = new ArrayList<CityInfo>();
	private String[] rmCity = { "北京", "上海", "深圳", "西安", "杭州", "广州", "沈阳", "南京", "武汉", "成都" };

	public ArrayList<CityInfo> getmCityInfoList() {
		return mCityInfoList;
	}

	// type:0--热门城市，1--通过省代码获取城市
	public City2ListParser(int type, String provinceCode) {
		try {
			String json = "";
			DaoControl dao = DaoControl.getInstance();
			ArrayList<CityStringInfo> list = dao.getCity2StringInfoList();
			if (list.size() < 0) {
				return;
			}

			json = list.get(0).getTxt();
			if (type == 0) {
				JSONArray jarray = new JSONArray(json);
				for (int i = 0; i < jarray.length(); i++) {
					if (mCityInfoList.size() == rmCity.length) {
						break;
					}
					JSONObject jobj = jarray.getJSONObject(i);
					JSONArray cityArray = jobj.getJSONArray("Cities");
					for (int j = 0; j < cityArray.length(); j++) {
						JSONObject obj = cityArray.getJSONObject(j);
						String name = obj.optString("Name");
						if (!TextUtils.isEmpty(name)) {
							for (int l = 0; l < rmCity.length; l++) {
								if (name.contains(rmCity[l])) {
									CityInfo mCityInfo = new CityInfo();
									mCityInfo.setName(obj.optString("Name"));
									mCityInfo.setCode(obj.optString("CityID"));
									mCityInfo.setAbbr(obj.optString("CarNumberPrefix"));
									int carCode = obj.optInt("CarCodeLen");
									if (carCode == 0) {
										mCityInfo.setClassa("0");
										mCityInfo.setClassno("0");
									} else {
										mCityInfo.setClassa("1");
										if (carCode == 99) {
											mCityInfo.setClassno("0");
										} else {
											mCityInfo.setClassno("" + carCode);
										}
									}

									int engine = obj.optInt("CarEngineLen");
									if (engine == 0) {
										mCityInfo.setEngine("0");
										mCityInfo.setEngineno("0");
									} else {
										mCityInfo.setEngine("1");
										if (engine == 99) {
											mCityInfo.setEngineno("0");
										} else {
											mCityInfo.setEngineno("" + engine);
										}
									}
									int regist = obj.optInt("CarOwnerLen");
									if (regist == 0) {
										mCityInfo.setRegist("0");
										mCityInfo.setRegistno("0");
									} else {
										mCityInfo.setRegist("1");
										if (regist == 99) {
											mCityInfo.setRegistno("0");
										} else {
											mCityInfo.setRegistno("" + regist);
										}
									}
									mCityInfoList.add(mCityInfo);
									break;
								}
							}
						}
					}
				}
			} else {
				JSONArray jarray = new JSONArray(json);
				for (int i = 0; i < jarray.length(); i++) {
					JSONObject jobj = jarray.getJSONObject(i);
					String id = jobj.optString("ProvinceID");
					if (id.equals(provinceCode)) {
						JSONArray cityArray = jobj.getJSONArray("Cities");
						for (int j = 0; j < cityArray.length(); j++) {
							JSONObject obj = cityArray.getJSONObject(j);
							CityInfo mCityInfo = new CityInfo();
							mCityInfo.setName(obj.optString("Name"));
							mCityInfo.setCode(obj.optString("CityID"));
							mCityInfo.setAbbr(obj.optString("CarNumberPrefix"));
							int carCode = obj.optInt("CarCodeLen");
							if (carCode == 0) {
								mCityInfo.setClassa("0");
								mCityInfo.setClassno("0");
							} else {
								mCityInfo.setClassa("1");
								if (carCode == 99) {
									mCityInfo.setClassno("0");
								} else {
									mCityInfo.setClassno("" + carCode);
								}
							}

							int engine = obj.optInt("CarEngineLen");
							if (engine == 0) {
								mCityInfo.setEngine("0");
								mCityInfo.setEngineno("0");
							} else {
								mCityInfo.setEngine("1");
								if (engine == 99) {
									mCityInfo.setEngineno("0");
								} else {
									mCityInfo.setEngineno("" + engine);
								}
							}
							int regist = obj.optInt("CarOwnerLen");
							if (regist == 0) {
								mCityInfo.setRegist("0");
								mCityInfo.setRegistno("0");
							} else {
								mCityInfo.setRegist("1");
								if (regist == 99) {
									mCityInfo.setRegistno("0");
								} else {
									mCityInfo.setRegistno("" + regist);
								}
							}
							mCityInfoList.add(mCityInfo);
						}
					}
				}
			}

		} catch (Exception e) {
			Log.e("info", "BaseParser--e==" + e);
		}
	}

}

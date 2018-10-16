package com.carlt.sesame.protocolstack.car;

import com.carlt.sesame.control.DaoControl;
import com.carlt.sesame.data.car.CityInfo;
import com.carlt.sesame.data.set.CityStringInfo;
import com.carlt.sesame.utility.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class CityListJUHEParser {

    private ArrayList<CityInfo> mCityInfoList = new ArrayList<CityInfo>();
    private ArrayList<CityInfo> mRmCityInfoList = new ArrayList<CityInfo>();
    private HashMap<String, CityInfo> mtempMap = new HashMap<String, CityInfo>();
    private String[] rmCity = {"北京", "上海", "深圳", "西安", "杭州", "广州", "苏州", "南京", "武汉", "重庆"};

    public ArrayList<CityInfo> getmCityInfoList() {
        return mCityInfoList;
    }

    // type:0--热门城市，1--通过省代码获取城市
    public CityListJUHEParser(int type, String provinceCode) {
        try {
            String json = "";
            DaoControl dao = DaoControl.getInstance();
            ArrayList<CityStringInfo> list = dao.getCityStringInfoList();
            if (list.size() > 0) {
                json = list.get(0).getTxt();
                JSONObject mJson_All = new JSONObject(json);
                JSONObject mJSON_data_ALL = mJson_All.getJSONObject("result");
                if (type == 0) {
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
//                            mtempMap.put(city.optString("city_code"), mCityInfo);
                            for (int l = 0; l < rmCity.length; l++) {
                                if (mCityInfo.getName().contains(rmCity[l])) {
                                    mRmCityInfoList.add(mCityInfo);
                                    break;
                                }
                            }
                        }
                    }

                    mCityInfoList = mRmCityInfoList;
                } else {
                    JSONObject mJSON_province = mJSON_data_ALL.getJSONObject(provinceCode);
                    JSONArray mJSON_cityArry = mJSON_province.getJSONArray("citys");
                    for (int i = 0; i < mJSON_cityArry.length(); i++) {
                        JSONObject city = (JSONObject) mJSON_cityArry.get(i);
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
                        mCityInfoList.add(mCityInfo);
                    }
                }
            }
        } catch (Exception e) {
            Log.e("info", "解析聚合城市列表出错BaseParser--e==" + e);
        }
    }

}

package com.carlt.sesame.protocolstack.career;

import com.carlt.sesame.data.BaseResponseInfo;
import com.carlt.sesame.data.career.WeatherInfo;
import com.carlt.sesame.protocolstack.BaseParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WeatherInfoParser extends BaseParser {

	private WeatherInfo mWeatherInfo = new WeatherInfo();

	public WeatherInfo getReturn() {
		return mWeatherInfo;
	}

	@Override
	protected void parser() {

		try {
			int resultCode = 0;
			int errorCode = mJson.optInt("error");
			String msg = mJson.optString("reason");
			mBaseResponseInfo.setInfo(msg);
			if (errorCode == 0) {
				resultCode = BaseResponseInfo.SUCCESS;
			}

			mBaseResponseInfo.setFlag(resultCode);
			if (resultCode != BaseResponseInfo.SUCCESS) {
				return;
			}

			JSONArray mJSON_data = mJson.getJSONArray("results");
			JSONObject mWeatherData = mJSON_data.getJSONObject(0);
			JSONArray datas = mWeatherData.getJSONArray("weather_data");
			JSONObject mWeather = datas.getJSONObject(0);
			// mWeatherInfo.setCity_code(mJSON_data.optString("city_code"));
			// mWeatherInfo.setCity_name(mJSON_data.optString("city_name"));
			// mWeatherInfo.setDate(mJSON_data.optString("date"));
			// mWeatherInfo.setTime(mJSON_data.optString("time"));
			// mWeatherInfo.setMoon(mJSON_data.optString("moon"));

			mWeatherInfo.setTemperature(mWeather.optString("temperature"));
			mWeatherInfo.setHumidity(mWeather.optString("humidity"));
			mWeatherInfo.setInfo(mWeather.optString("info"));
			mWeatherInfo.setDirect(mWeather.optString("direct"));
			mWeatherInfo.setWeather(mWeather.optString("weather"));
			String date = mWeather.optString("date");
			date = date.substring(date.indexOf("：") + 1, date.indexOf("℃"));
			mWeatherInfo.setRealTempure(date);

		} catch (JSONException e) {
			mBaseResponseInfo.setFlag(BaseResponseInfo.ERRO);
			mBaseResponseInfo.setInfo(MSG_ERRO);
			e.printStackTrace();
		}

	}
}

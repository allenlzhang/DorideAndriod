package com.carlt.doride.data.home;

public class WeatherInfo {

	private String city_code = "";
	private String city_name = "";
	private String date = "";
	private String time = "";
	private String moon = "";
	// 温度
	private String temperature = "";
	// 湿度
	private String humidity = "";
	private String info = "";
	// 风力
	private String direct = "";
	
	private String weather = "";
	
	private String realTempure = "";

	public String getRealTempure() {
		return realTempure;
	}

	public void setRealTempure(String realTempure) {
		this.realTempure = realTempure;
	}

	public String getWeather() {
		return weather;
	}

	public void setWeather(String weather) {
		this.weather = weather;
	}

	public String getCity_code() {
		return city_code;
	}

	public void setCity_code(String city_code) {
		this.city_code = city_code;
	}

	public String getCity_name() {
		return city_name;
	}

	public void setCity_name(String city_name) {
		this.city_name = city_name;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getMoon() {
		return moon;
	}

	public void setMoon(String moon) {
		this.moon = moon;
	}

	public String getTemperature() {
		return temperature;
	}

	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}

	public String getHumidity() {
		return humidity;
	}

	public void setHumidity(String humidity) {
		this.humidity = humidity;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getDirect() {
		return direct;
	}

	public void setDirect(String direct) {
		this.direct = direct;
	}

}

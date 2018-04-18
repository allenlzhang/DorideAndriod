package com.carlt.yema.data.home;


import com.carlt.yema.data.BaseResponseInfo;

public class ReportDayInfo extends BaseResponseInfo {
	//用户真实姓名
	private String realname;
	//头像地址
	private String avatar_img;
	//
	private int gender;
	//
	private String id;
	//
	private String sumfuel;
	//
	private String avgfuel;
	//
	private String sumtime;
	//
	private String summiles;
	//
	private String maxspeed;
	//
	private String avgspeed;

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public String getAvatar_img() {
		return avatar_img;
	}

	public void setAvatar_img(String avatar_img) {
		this.avatar_img = avatar_img;
	}

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSumfuel() {
		return sumfuel;
	}

	public void setSumfuel(String sumfuel) {
		this.sumfuel = sumfuel;
	}

	public String getAvgfuel() {
		return avgfuel;
	}

	public void setAvgfuel(String avgfuel) {
		this.avgfuel = avgfuel;
	}

	public String getSumtime() {
		return sumtime;
	}

	public void setSumtime(String sumtime) {
		this.sumtime = sumtime;
	}

	public String getSummiles() {
		return summiles;
	}

	public void setSummiles(String summiles) {
		this.summiles = summiles;
	}

	public String getMaxspeed() {
		return maxspeed;
	}

	public void setMaxspeed(String maxspeed) {
		this.maxspeed = maxspeed;
	}

	public String getAvgspeed() {
		return avgspeed;
	}

	public void setAvgspeed(String avgspeed) {
		this.avgspeed = avgspeed;
	}
}

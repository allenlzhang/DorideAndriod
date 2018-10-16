package com.carlt.sesame.data.set;

public class PushSetInfo {

	public PushSetInfo() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * 推送设置信息 0:不允许 ,1:允许
	 **/
	// 是否推送车辆启动提醒
	private int startup;
	// 是否推送日报
	private int dayreport;
	// 是否推周报
	private int weekreport;
	// 是否推月报
	private int monthreport;

	// 是否推故障
	private int trouble;

	// 是否推送4S店优惠券及活动
	private int dealer;

	// 是否推失去连接消息
	private int lostconnect;

	// 是否推获取勋章
	private int gotmedal;

	// 是否推创造记录
	private int newrecord;

	// 是否推驾驶证等级
	private int license;
	// 是否推刮擦拖車
	private int security;
	// 是否推振动 (强度1 - 3)
	private int vibstrength;
	// 推送增加蜂鸣器0关 1开
	private int buzz;

	public int getDayreport() {
		return dayreport;
	}

	public void setDayreport(int dayreport) {
		this.dayreport = dayreport;
	}

	public int getWeekreport() {
		return weekreport;
	}

	public void setWeekreport(int weekreport) {
		this.weekreport = weekreport;
	}

	public int getMonthreport() {
		return monthreport;
	}

	public void setMonthreport(int monthreport) {
		this.monthreport = monthreport;
	}

	public int getSecurity() {
		return security;
	}

	public void setSecurity(int security) {
		this.security = security;
	}

	public int getDealer() {
		return dealer;
	}

	public void setDealer(int dealer) {
		this.dealer = dealer;
	}

	public int getStartup() {
		return startup;
	}

	public void setStartup(int startup) {
		this.startup = startup;
	}

	public int getTrouble() {
		return trouble;
	}

	public void setTrouble(int trouble) {
		this.trouble = trouble;
	}

	public int getLostconnect() {
		return lostconnect;
	}

	public void setLostconnect(int lostconnect) {
		this.lostconnect = lostconnect;
	}

	public int getGotmedal() {
		return gotmedal;
	}

	public void setGotmedal(int gotmedal) {
		this.gotmedal = gotmedal;
	}

	public int getNewrecord() {
		return newrecord;
	}

	public void setNewrecord(int newrecord) {
		this.newrecord = newrecord;
	}

	public int getLicense() {
		return license;
	}

	public void setLicense(int license) {
		this.license = license;
	}

	public int getVibstrength() {
		return vibstrength;
	}

	public void setVibstrength(int vibstrength) {
		this.vibstrength = vibstrength;
	}

	public int getBuzz() {
		return buzz;
	}

	public void setBuzz(int buzz) {
		this.buzz = buzz;
	}

}

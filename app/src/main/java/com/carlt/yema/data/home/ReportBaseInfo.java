package com.carlt.yema.data.home;

public class ReportBaseInfo {
	// 主键
	private String id;

	// 用户ID
	private String userId;

	// 行车总时间
	private String sumtime_hour;

	// 行车总时间
	private String sumtime_min;

	// 行车总时间
	private String sumtime_str;

	// 总里程
	private String sumMiles;

	// 总油耗
	private String sumFuel;

	// 平均百公里油耗
	private String avgFuel;

	// 最高时速
	private String maxSpeed;

	// 平均时速
	private String avgSpeed;

	// 每百公里刹车次数
	private int brake;

	// 每百公里急转弯次数
	private int turn;

	// 每百公里加速次数
	private int speedup;

	// 每百公里高转速次数
	private int overSpeed;

	// 同型车刹车次数
	private int typebrake;

	// 同型车急转弯次数
	private int typeturn;

	// 同型车加速次数
	private int typespeedup;

	// 同型车高转速次数
	private int typeoverSpeed;

	// 同型车油耗
	private String typeAvgFuel;

	// 所有平均油耗
	private String allAvgFuel;

	// "brakedesc": "刹车太猛了",
	private String brakedesc;

	// "turndesc": "转弯太猛了",
	private String turndesc;

	// "speedupdesc": "加速太猛了",
	private String speedupdesc;

	// "overspeeddesc": "您是着急投胎吗？"
	private String overspeeddesc;

	// 报告所属日期
	private String reportDate;

	// 数据生成时间
	private int createTime;

	private String shareLink;

	private String shareTitle;

	private String shareText;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getReportDate() {
		return reportDate;
	}

	public void setReportDate(String reportDate) {
		this.reportDate = reportDate;
	}

	public int getCreateTime() {
		return createTime;
	}

	public void setCreateTime(int createTime) {
		this.createTime = createTime;
	}

	public int getTypebrake() {
		return typebrake;
	}

	public void setTypebrake(int typebrake) {
		this.typebrake = typebrake;
	}

	public int getTypeturn() {
		return typeturn;
	}

	public void setTypeturn(int typeturn) {
		this.typeturn = typeturn;
	}

	public int getTypespeedup() {
		return typespeedup;
	}

	public void setTypespeedup(int typespeedup) {
		this.typespeedup = typespeedup;
	}

	public int getTypeoverSpeed() {
		return typeoverSpeed;
	}

	public void setTypeoverSpeed(int typeoverSpeed) {
		this.typeoverSpeed = typeoverSpeed;
	}


	public String getTypeAvgFuel() {
		return typeAvgFuel;
	}

	public void setTypeAvgFuel(String typeAvgFuel) {
		this.typeAvgFuel = typeAvgFuel;
	}


	public String getAllAvgFuel() {
		return allAvgFuel;
	}

	public void setAllAvgFuel(String allAvgFuel) {
		this.allAvgFuel = allAvgFuel;
	}

	public String getBrakedesc() {
		return brakedesc;
	}

	public void setBrakedesc(String brakedesc) {
		this.brakedesc = brakedesc;
	}

	public String getTurndesc() {
		return turndesc;
	}

	public void setTurndesc(String turndesc) {
		this.turndesc = turndesc;
	}

	public String getSpeedupdesc() {
		return speedupdesc;
	}

	public void setSpeedupdesc(String speedupdesc) {
		this.speedupdesc = speedupdesc;
	}

	public String getOverspeeddesc() {
		return overspeeddesc;
	}

	public void setOverspeeddesc(String overspeeddesc) {
		this.overspeeddesc = overspeeddesc;
	}

	public String getSumtime_hour() {
		return sumtime_hour;
	}

	public void setSumtime_hour(String sumtime_hour) {
		this.sumtime_hour = sumtime_hour;
	}

	public String getSumtime_min() {
		return sumtime_min;
	}

	public void setSumtime_min(String sumtime_min) {
		this.sumtime_min = sumtime_min;
	}

	public String getSumtime_str() {
		return sumtime_str;
	}

	public void setSumtime_str(String sumtime_str) {
		this.sumtime_str = sumtime_str;
	}

	public int getBrake() {
		return brake;
	}

	public void setBrake(int brake) {
		this.brake = brake;
	}

	public int getTurn() {
		return turn;
	}

	public void setTurn(int turn) {
		this.turn = turn;
	}

	public int getSpeedup() {
		return speedup;
	}

	public void setSpeedup(int speedup) {
		this.speedup = speedup;
	}

	public int getOverSpeed() {
		return overSpeed;
	}

	public void setOverSpeed(int overSpeed) {
		this.overSpeed = overSpeed;
	}

	public String getSumMiles() {
		return sumMiles;
	}

	public void setSumMiles(String sumMiles) {
		this.sumMiles = sumMiles;
	}

	public String getSumFuel() {
		return sumFuel;
	}

	public void setSumFuel(String sumFuel) {
		this.sumFuel = sumFuel;
	}

	public String getAvgFuel() {
		return avgFuel;
	}

	public void setAvgFuel(String avgFuel) {
		this.avgFuel = avgFuel;
	}

	public String getMaxSpeed() {
		return maxSpeed;
	}

	public void setMaxSpeed(String maxSpeed) {
		this.maxSpeed = maxSpeed;
	}

	public String getAvgSpeed() {
		return avgSpeed;
	}

	public void setAvgSpeed(String avgSpeed) {
		this.avgSpeed = avgSpeed;
	}

	public String getShareLink() {
		return shareLink;
	}

	public void setShareLink(String shareLink) {
		this.shareLink = shareLink;
	}

	public String getShareTitle() {
		return shareTitle;
	}

	public void setShareTitle(String shareTitle) {
		this.shareTitle = shareTitle;
	}

	public String getShareText() {
		return shareText;
	}

	public void setShareText(String shareText) {
		this.shareText = shareText;
	}

}

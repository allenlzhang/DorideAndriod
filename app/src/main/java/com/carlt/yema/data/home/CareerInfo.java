
package com.carlt.yema.data.home;


import com.carlt.yema.data.BaseResponseInfo;

public class CareerInfo extends BaseResponseInfo {

    // 第XXX天登陆
    private int lefttime;


    // 挑战总数
    private String challengeTotal;

    // 已完成挑战数
    private String challengeFinished;

    //最近一次有数据的驾驶得分
    private String daypoint;// 最近一次日报得分

    private String weekpoint;// 最近一次周报得分

    private String monthpoint;// 最近一次月报得分

    // 驾驶证等级图标
    private String licenceImg;

    // 驾驶证等级
    private String licenceLevel;

    // 驾驶证距离升级百分比
    private int licencePercent;

    // 总油耗
    private String sumfuel;

    // 总油耗描述
    private String sumfueldesc;

    private String summiles;
    // 总里程描述
    private String summilesdesc;
    
    private String sumtime;
    private String sumtimedesc;

    public String getSumtime() {
		return sumtime;
	}

	public void setSumtime(String sumtime) {
		this.sumtime = sumtime;
	}

	public String getSumtimedesc() {
		return sumtimedesc;
	}

	public void setSumtimedesc(String sumtimedesc) {
		this.sumtimedesc = sumtimedesc;
	}

	// 平均速度
    private String avgspeed;

    public String getSummiles() {
		return summiles;
	}

	public void setSummiles(String summiles) {
		this.summiles = summiles;
	}

	// 平均速度描述
    private String avgspeeddesc;

    // 平均油耗
    private String avgfuel;

    // 平均油耗描述
    private String avgfueldesc;

    // 车秘书消息条数
    private String unreadmessage;

    // 车秘书最新消息内容
    private String latestmessage;

    public int getLefttime() {
        return lefttime;
    }

    public void setLefttime(int lefttime) {
        this.lefttime = lefttime;
    }


    public String getChallengeTotal() {
        return challengeTotal;
    }

    public void setChallengeTotal(String challengeTotal) {
        this.challengeTotal = challengeTotal;
    }

    public String getChallengeFinished() {
        return challengeFinished;
    }

    public void setChallengeFinished(String challengeFinished) {
        this.challengeFinished = challengeFinished;
    }

    public String getDaypoint() {
        return daypoint;
    }

    public void setDaypoint(String daypoint) {
        this.daypoint = daypoint;
    }

    public String getWeekpoint() {
        return weekpoint;
    }

    public void setWeekpoint(String weekpoint) {
        this.weekpoint = weekpoint;
    }

    public String getMonthpoint() {
        return monthpoint;
    }

    public void setMonthpoint(String monthpoint) {
        this.monthpoint = monthpoint;
    }

    public String getLicenceImg() {
        return licenceImg;
    }

    public void setLicenceImg(String licenceImg) {
        this.licenceImg = licenceImg;
    }

    public String getLicenceLevel() {
        return licenceLevel;
    }

    public void setLicenceLevel(String licenceLevel) {
        this.licenceLevel = licenceLevel;
    }

    public int getLicencePercent() {
        return licencePercent;
    }

    public void setLicencePercent(int licencePercent) {
        this.licencePercent = licencePercent;
    }

    public String getSumfueldesc() {
        return sumfueldesc;
    }

    public void setSumfueldesc(String sumfueldesc) {
        this.sumfueldesc = sumfueldesc;
    }

    public String getSummilesdesc() {
        return summilesdesc;
    }

    public void setSummilesdesc(String summilesdesc) {
        this.summilesdesc = summilesdesc;
    }

    public String getAvgspeed() {
        return avgspeed;
    }

    public void setAvgspeed(String avgspeed) {
        this.avgspeed = avgspeed;
    }

    public String getAvgspeeddesc() {
        return avgspeeddesc;
    }

    public void setAvgspeeddesc(String avgspeeddesc) {
        this.avgspeeddesc = avgspeeddesc;
    }

    public String getAvgfuel() {
        return avgfuel;
    }

    public void setAvgfuel(String avgfuel) {
        this.avgfuel = avgfuel;
    }

    public String getAvgfueldesc() {
        return avgfueldesc;
    }

    public void setAvgfueldesc(String avgfueldesc) {
        this.avgfueldesc = avgfueldesc;
    }

    public String getUnreadmessage() {
        return unreadmessage;
    }

    public void setUnreadmessage(String unreadmessage) {
        this.unreadmessage = unreadmessage;
    }

    public String getLatestmessage() {
        return latestmessage;
    }

    public void setLatestmessage(String latestmessage) {
        this.latestmessage = latestmessage;
    }

    public String getSumfuel() {
        return sumfuel;
    }

    public void setSumfuel(String sumfuel) {
        this.sumfuel = sumfuel;
    }

}

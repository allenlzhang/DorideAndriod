
package com.carlt.doride.data.home;

import java.io.Serializable;

public class CarLogInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	// 起始时间
    private String starttime;

    // 结束时间
    private String stopTime;

    // 用时
    private String time;

    // 油耗
    private String fuel;

    // 平均油耗
    private String avgfuel;

    // 平均速度
    private String avgspeed;

    // 最大速度
    private String maxspeed;

    // 行驶距离
    private String miles;

    // 急刹车
    private String brake;

    // 急转弯
    private String turn;

    // 急加速
    private String speedup;

    // 高转速
    private String overspeed;

    // 驾驶成绩
    private String point;
    
    //获取轨迹回放的起始时间
    private String gpsStartTime;
    
    //获取轨迹回放的结束时间
    private String gpsStopTime;
    
    private String runSn;//GPS序号
    
    private int listCount;
    
    public int getListCount() {
		return listCount;
	}

	public void setListCount(int listCount) {
		this.listCount = listCount;
	}

	public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getStopTime() {
        return stopTime;
    }

    public void setStopTime(String stopTime) {
        this.stopTime = stopTime;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getFuel() {
        return fuel;
    }

    public void setFuel(String fuel) {
        this.fuel = fuel;
    }

    public String getAvgfuel() {
        return avgfuel;
    }

    public void setAvgfuel(String avgfuel) {
        this.avgfuel = avgfuel;
    }

    public String getAvgspeed() {
        return avgspeed;
    }

    public void setAvgspeed(String avgspeed) {
        this.avgspeed = avgspeed;
    }

    public String getMaxspeed() {
        return maxspeed;
    }

    public void setMaxspeed(String maxspeed) {
        this.maxspeed = maxspeed;
    }

    public String getMiles() {
        return miles;
    }

    public void setMiles(String miles) {
        this.miles = miles;
    }

    public String getBrake() {
        return brake;
    }

    public void setBrake(String brake) {
        this.brake = brake;
    }

    public String getTurn() {
        return turn;
    }

    public void setTurn(String turn) {
        this.turn = turn;
    }

    public String getSpeedup() {
        return speedup;
    }

    public void setSpeedup(String speedup) {
        this.speedup = speedup;
    }

    public String getOverspeed() {
        return overspeed;
    }

    public void setOverspeed(String overspeed) {
        this.overspeed = overspeed;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public String getGpsStartTime() {
        return gpsStartTime;
    }

    public void setGpsStartTime(String gpsStartTime) {
        this.gpsStartTime = gpsStartTime;
    }

    public String getGpsStopTime() {
        return gpsStopTime;
    }

    public void setGpsStopTime(String gpsStopTime) {
        this.gpsStopTime = gpsStopTime;
    }

    public String getRunSn() {
        return runSn;
    }

    public void setRunSn(String runSn) {
        this.runSn = runSn;
    }

}

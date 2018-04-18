package com.carlt.yema.data.home;

import java.io.Serializable;

/**
 * Created by Marlon on 2018/3/23.
 */

public class ReportDayLogInfo implements Serializable {
/**
 * "starttime": "02:27",
 "stopTime": "03:27",
 "time": "50分钟",
 "fuel": "5.2L",
 "avgfuel": "5.1L/100KM",
 "avgspeed": "50KM/H",
 "maxspeed": "120KM/H",
 "miles": "50公里",
 "gpsStartTime":"1455419954",
 "gpsStopTime":"1455425054",
 "runSn": "1001",
 */
    private String starttime;
    private String stopTime;
    private String time;
    private String fuel;
    private String avgfuel;
    private String avgspeed;
    private String maxspeed;
    private String miles;
    private String gpsStartTime;
    private String gpsStopTime;
    private String runSn;

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

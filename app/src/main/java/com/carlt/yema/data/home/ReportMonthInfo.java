package com.carlt.yema.data.home;


import com.carlt.yema.data.BaseResponseInfo;

/**
 * Created by Marlon on 2018/3/23.
 */

public class ReportMonthInfo extends BaseResponseInfo {
    /**
     * "avgfuel": "0",//月平均百公里油耗
     "sumtime": 48,//行车总时间
     "maxspeed": 55,//最高时速
     "summiles": "216.0",//总里程
     "sumfuel": 0,//总油耗
     "avgspeed": "270.0",//平均时速
     */
    private String avgfuel;
    private String sumtime;
    private String maxspeed;
    private String summiles;
    private String sumfuel;
    private String avgspeed;

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

    public String getMaxspeed() {
        return maxspeed;
    }

    public void setMaxspeed(String maxspeed) {
        this.maxspeed = maxspeed;
    }

    public String getSummiles() {
        return summiles;
    }

    public void setSummiles(String summiles) {
        this.summiles = summiles;
    }

    public String getSumfuel() {
        return sumfuel;
    }

    public void setSumfuel(String sumfuel) {
        this.sumfuel = sumfuel;
    }

    public String getAvgspeed() {
        return avgspeed;
    }

    public void setAvgspeed(String avgspeed) {
        this.avgspeed = avgspeed;
    }
}

package com.carlt.doride.data.home;

/**
 * Created by Marlon on 2018/3/23.
 */

public class ReportMonthLogInfo {
    /**
     * "sumfuel": 1231,    //总油耗，单位L（升），类型int
     * "summiles": 3,       //总里程，单位km（千米），类型int
     * "sumtime": 3,        //总时间，单位h（小时），类型int
     * "month":1             //月份，类型int
     * "date":"2017-09-30"  //日期
     */
    private String sumfuel;
    private String summiles;
    private String sumtime;
    private String month;
    private String date;

    public String getSumfuel() {
        return sumfuel;
    }

    public void setSumfuel(String sumfuel) {
        this.sumfuel = sumfuel;
    }

    public String getSummiles() {
        return summiles;
    }

    public void setSummiles(String summiles) {
        this.summiles = summiles;
    }

    public String getSumtime() {
        return sumtime;
    }

    public void setSumtime(String sumtime) {
        this.sumtime = sumtime;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}

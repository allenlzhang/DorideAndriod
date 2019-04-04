package com.carlt.doride.http.retrofitnet.model;

/**
 * Created by Marlon on 2019/4/3.
 */
public class CarInfoRsp {
    public int obd; // 仪表盘总里程
    public String enduranceMile = ""; // 续航里程，单位km
    public float minEnduranceMile ; // 最小续航里程，单位km
    public float maxEnduranceMile; // 最大续航里程，单位km
    public String avgSpeed = ""; // 平均速度，单位km/h
    public String avgFuel = ""; // 平均油耗，单位L/100km
    public String leftFuel = ""; // 剩余油量，单位L
    public String vBat = ""; // 蓄电池电压，单位V
    public String runningTime = ""; //行驶时间 单位小时
    public BaseErr err;
}

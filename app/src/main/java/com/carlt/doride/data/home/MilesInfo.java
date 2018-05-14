package com.carlt.doride.data.home;


/**
 * Created by Marlon on 2018/3/27.
 */

public class MilesInfo{
    /**
     * "avgFuel": "45.0",   平均油耗。
     "avgSpeed": "90.6",    平均速度。
     "enduranceMile": "200.3",  续航里程。
     "leftFuel": "43",      剩余油量
     "obd": "108.2",        仪表盘里程。
     "runningTime": "21",   行驶时间
     "vBat": "2"            单体电压
     */
    //仪表盘里程
    private String obd;
    //续航里程
    private String enduranceMile;
    //平均速度
    private String avgSpeed;
    //平均油耗
    private String avgFuel;
    //剩余油耗
    private String leftDuel;
    //行驶时间
    private String runningTime;
    //单体电压
    private String vBat;

    public String getLeftDuel() {
        return leftDuel;
    }

    public void setLeftDuel(String leftDuel) {
        this.leftDuel = leftDuel;
    }

    public String getRunningTime() {
        return runningTime;
    }

    public void setRunningTime(String runningTime) {
        this.runningTime = runningTime;
    }

    public String getvBat() {
        return vBat;
    }

    public void setvBat(String vBat) {
        this.vBat = vBat;
    }

    public String getObd() {
        return obd;
    }

    public void setObd(String obd) {
        this.obd = obd;
    }

    public String getEnduranceMile() {
        return enduranceMile;
    }

    public void setEnduranceMile(String enduranceMile) {
        this.enduranceMile = enduranceMile;
    }

    public String getAvgSpeed() {
        return avgSpeed;
    }

    public void setAvgSpeed(String avgSpeed) {
        this.avgSpeed = avgSpeed;
    }

    public String getAvgFuel() {
        return avgFuel;
    }

    public void setAvgFuel(String avgFuel) {
        this.avgFuel = avgFuel;
    }
}

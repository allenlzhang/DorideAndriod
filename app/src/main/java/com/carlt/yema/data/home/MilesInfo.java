package com.carlt.yema.data.home;


/**
 * Created by Marlon on 2018/3/27.
 */

public class MilesInfo{
    /**
     * "obd":1960.5,
     "enduranceMile":19.5,
     "avgSpeed":56.8,
     "avgFuel":6.5,
     */
    //仪表盘里程
    private double obd;
    //续航里程
    private double enduranceMile;
    //平均速度
    private double avgSpeed;
    //平均油耗
    private double avgFuel;

    public double getObd() {
        return obd;
    }

    public void setObd(double obd) {
        this.obd = obd;
    }

    public double getEnduranceMile() {
        return enduranceMile;
    }

    public void setEnduranceMile(double enduranceMile) {
        this.enduranceMile = enduranceMile;
    }

    public double getAvgSpeed() {
        return avgSpeed;
    }

    public void setAvgSpeed(double avgSpeed) {
        this.avgSpeed = avgSpeed;
    }

    public double getAvgFuel() {
        return avgFuel;
    }

    public void setAvgFuel(double avgFuel) {
        this.avgFuel = avgFuel;
    }
}

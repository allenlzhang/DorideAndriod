package com.carlt.doride.data.car;

import com.carlt.doride.data.BaseResponseInfo;

/**
 *
 * 座驾页实时车况
 * Created by liu on 2018/3/30.
 */

public class CarMilesInfo extends BaseResponseInfo {

    /**
     * avgFuel : 45.0
     * avgSpeed : 90.6
     * enduranceMile : 200.3
     * leftFuel : 
     * obd : 108.2
     * runningTime : 21
     * vBat : 
     */

    public String avgFuel;
    public String avgSpeed;
    public String enduranceMile;
    public String minEnduranceMile;
    public String maxEnduranceMile;
    public String leftFuel;
    public String obd;
    public String runningTime;
    public String vBat;

    @Override
    public String toString() {
        return "CarMilesInfo{" +
                "avgFuel='" + avgFuel + '\'' +
                ", avgSpeed='" + avgSpeed + '\'' +
                ", enduranceMile='" + enduranceMile + '\'' +
                ", minEnduranceMile='" + minEnduranceMile + '\'' +
                ", maxEnduranceMile='" + maxEnduranceMile + '\'' +
                ", leftFuel='" + leftFuel + '\'' +
                ", obd='" + obd + '\'' +
                ", runningTime='" + runningTime + '\'' +
                ", vBat='" + vBat + '\'' +
                '}';
    }
}

package com.carlt.doride.data.car;

import com.carlt.doride.data.BaseResponseInfo;
import com.carlt.doride.http.retrofitnet.model.BaseErr;

/**
 * 0 是不亮； 1 是 亮
 * Created by liu on 2018/3/30.
 * "ABS":    0,
 * "ESP" :  0,
 * "EPB":   0,
 * "SRS":   0,
 * "EPS":   0,
 * "TPMS":  0,
 * "WATERTMP":0,
 * "SVS":0,
 * "EOBD":0
 */

public class WaringLampInfo extends BaseResponseInfo {
    public static final int     LIGHT      = 1;
    public static final int     NOT_BRIGHT = 0;
    public static final long    NO_LIGHT   = 4294967295L;
    /**
     * ABS : 1
     * ENGINELAMP : 0
     * EPB : 1
     * ESP : 1
     * Grade : 37
     * MTLAMP : 0
     * SRS : 1
     * TPMS : 1
     * WATERTMP : 1
     */

    public              long    ABS        = -1;
    public              long    ENGINELAMP = -1;
    public              long    EPB        = -1;
    public              long    ESP        = -1;
    public              long    grade      = -1;
    public              long    MTLAMP     = -1;
    public              long    SRS        = -1;
    public              long    TPMS       = -1;
    public              long    WATERTMP   = -1;
    public              long    CheckTime  = -1;
    public              BaseErr err;

    @Override
    public String toString() {
        return "WaringLampInfo{" +
                "ABS=" + ABS +
                ", ENGINELAMP=" + ENGINELAMP +
                ", EPB=" + EPB +
                ", ESP=" + ESP +
                ", Grade=" + grade +
                ", MTLAMP=" + MTLAMP +
                ", SRS=" + SRS +
                ", TPMS=" + TPMS +
                ", WATERTMP=" + WATERTMP +
                ", CheckTime=" + CheckTime +
                '}';
    }
}

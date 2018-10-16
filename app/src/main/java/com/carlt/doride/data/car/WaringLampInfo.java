package com.carlt.doride.data.car;

import com.carlt.doride.data.BaseResponseInfo;

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
    public static final int LIGHT      = 1;
    public static final int NOT_BRIGHT = 0;

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

    public int ABS;
    public int ENGINELAMP;
    public int EPB;
    public int ESP;
    public int Grade;
    public int MTLAMP;
    public int SRS;
    public int TPMS;
    public int WATERTMP;


}

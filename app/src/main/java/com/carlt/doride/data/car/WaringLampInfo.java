package com.carlt.doride.data.car;

import com.carlt.doride.data.BaseResponseInfo;

/**
 *
 * 0 是不亮； 1 是 亮
 * Created by liu on 2018/3/30.
 *   "ABS":    0,
 "ESP" :  0,
 "EPB":   0,
 "SRS":   0,
 "EPS":   0,
 "TPMS":  0,
 "WATERTMP":0,
 "SVS":0,
 "EOBD":0
 */

public class WaringLampInfo extends BaseResponseInfo {

    private int ABS ;
    private int ESP ;
    private int EPB ;
    private int SRS ;
    private int EPS ;
    private int TPMS ;
    private int WATERTMP ;
    private int SVS ;
    private int EOBD ;

    public static final int LIGHT = 1;
    public static final int NOT_BRIGHT =0;

    public int getABS() {
        return ABS;
    }

    public void setABS(int ABS) {
        this.ABS = ABS;
    }

    public int getESP() {
        return ESP;
    }

    public void setESP(int ESP) {
        this.ESP = ESP;
    }

    public int getEPB() {
        return EPB;
    }

    public void setEPB(int EPB) {
        this.EPB = EPB;
    }

    public int getSRS() {
        return SRS;
    }

    public void setSRS(int SRS) {
        this.SRS = SRS;
    }

    public int getEPS() {
        return EPS;
    }

    public void setEPS(int EPS) {
        this.EPS = EPS;
    }

    public int getTPMS() {
        return TPMS;
    }

    public void setTPMS(int TPMS) {
        this.TPMS = TPMS;
    }

    public int getWATERTMP() {
        return WATERTMP;
    }

    public void setWATERTMP(int WATERTMP) {
        this.WATERTMP = WATERTMP;
    }

    public int getSVS() {
        return SVS;
    }

    public void setSVS(int SVS) {
        this.SVS = SVS;
    }

    public int getEOBD() {
        return EOBD;
    }

    public void setEOBD(int EOBD) {
        this.EOBD = EOBD;
    }
}

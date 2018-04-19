package com.carlt.doride.data.set;

/**
 * Created by Marlon on 2018/4/2.
 */

public class MsgManagerInfo {
    /**
     * "report": "1",
     "class2_6201": "0",
     "class2_6202": "0",
     */
    private int report;  //是否允许推送行车报表
    private int class2_6201;   //保险到期提醒开关
    private int class2_6202;  //年检到期提醒开关

    public int getReport() {
        return report;
    }

    public void setReport(int report) {
        this.report = report;
    }

    public int getClass2_6201() {
        return class2_6201;
    }

    public void setClass2_6201(int class2_6201) {
        this.class2_6201 = class2_6201;
    }

    public int getClass2_6202() {
        return class2_6202;
    }

    public void setClass2_6202(int class2_6202) {
        this.class2_6202 = class2_6202;
    }
}

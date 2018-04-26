package com.carlt.doride.data.car;

import com.carlt.doride.data.BaseResponseInfo;

/**
 * Created by liu on 2018/3/28.
 */

public class CarIndexInfo extends BaseResponseInfo {

    private String carname;//车款名称
    private String safetymsg;//	最后一条安防提醒内容
    private String isrunning;//isrunning
    public int last_fault_score;//isrunning

    public String getCarname() {
        return carname;
    }

    public void setCarname(String carname) {
        this.carname = carname;
    }

    public String getSafetymsg() {
        return safetymsg;
    }

    public void setSafetymsg(String safetymsg) {
        this.safetymsg = safetymsg;
    }

    public String getIsrunning() {
        return isrunning;
    }

    public void setIsrunning(String isrunning) {
        this.isrunning = isrunning;
    }
}

package com.carlt.sesame.data.car;

import com.carlt.sesame.data.BaseResponseInfo;

/**
 * Created by liu on 2018/4/8.
 */

public class BindDeviceInfo extends BaseResponseInfo {

    private String isbind;
    private String need_pin;

    public String getIsbind() {
        return isbind;
    }

    public void setIsbind(String isbind) {
        this.isbind = isbind;
    }

    public String getNeed_pin() {
        return need_pin;
    }

    public void setNeed_pin(String need_pin) {
        this.need_pin = need_pin;
    }
}

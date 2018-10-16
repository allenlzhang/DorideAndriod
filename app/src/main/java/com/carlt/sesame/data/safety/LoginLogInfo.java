
package com.carlt.sesame.data.safety;

import com.carlt.sesame.data.BaseResponseInfo;

/**
 * 近期登录记录info
 * 
 * @author Administrator
 */
public class LoginLogInfo extends BaseResponseInfo {

    private String name;// 手机名称

    private String type;// 是否为主机 0否 1是

    public final static String TYPE_CHILD = "0";// 子机

    public final static String TYPE_MAIN = "1";// 主机

    private String time;// 登录时间

    private String model;// 手机型号

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }
}

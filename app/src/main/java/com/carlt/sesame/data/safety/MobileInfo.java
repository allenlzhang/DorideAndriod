
package com.carlt.sesame.data.safety;

import com.carlt.sesame.data.BaseResponseInfo;

/**
 * 授权设备info
 * @author Administrator
 *
 */
public class MobileInfo extends BaseResponseInfo {
    
    private String id;//主键id(删除时用)
    
    private String mobile_id;//手机唯一标识id（授权处理时用）

    private String name;// 手机名称

    private String time;// 登录时间

    private String model;// 手机型号
    
    private String authorize_type;//授权类型，1未处理，2已授权
    
    public final static String AUTHORIZE_TYPE_UNDO="1";//1未处理
    
    public final static String AUTHORIZE_TYPE_DONE="2";//2已授权
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    public String getMobile_id() {
        return mobile_id;
    }

    public void setMobile_id(String mobile_id) {
        this.mobile_id = mobile_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getAuthorize_type() {
        return authorize_type;
    }

    public void setAuthorize_type(String authorize_type) {
        this.authorize_type = authorize_type;
    }
    
}

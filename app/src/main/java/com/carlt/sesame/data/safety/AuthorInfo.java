
package com.carlt.sesame.data.safety;

import com.carlt.sesame.data.BaseResponseInfo;

/**
 * 授权信息
 * 
 * @author Administrator
 */
public class AuthorInfo extends BaseResponseInfo {
	public static final int Error_Message = 0;// 设备出现异常的推送
    public final static int REQUEST_AUTHOR = 1;// 推送中请求推送类别

    private String mobile_id;// 请求授权设备的唯一标识id

    private String mobile_name;// 请求授权设备的用户名

    private String addtime;// 请求时间

    public String getMobile_id() {
        return mobile_id;
    }

    public void setMobile_id(String mobile_id) {
        this.mobile_id = mobile_id;
    }

    public String getMobile_name() {
        return mobile_name;
    }

    public void setMobile_name(String mobile_name) {
        this.mobile_name = mobile_name;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }
}

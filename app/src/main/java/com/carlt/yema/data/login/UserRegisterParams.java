package com.carlt.yema.data.login;

/**
 * Created by marller on 2018\3\27 0027.
 */

public class UserRegisterParams {
    private String mobile;
    private String password;
    private String validate;
    private static final String originate="1";
    private String move_deviceid;
    private String move_device_name;
    private String invite;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getValidate() {
        return validate;
    }

    public void setValidate(String validate) {
        this.validate = validate;
    }

    public String getOriginate() {
        return originate;
    }

    public String getMove_deviceid() {
        return move_deviceid;
    }

    public void setMove_deviceid(String move_deviceid) {
        this.move_deviceid = move_deviceid;
    }

    public String getMove_device_name() {
        return move_device_name;
    }

    public void setMove_device_name(String move_device_name) {
        this.move_device_name = move_device_name;
    }

    public String getInvite() {
        return invite;
    }

    public void setInvite(String invite) {
        this.invite = invite;
    }
}

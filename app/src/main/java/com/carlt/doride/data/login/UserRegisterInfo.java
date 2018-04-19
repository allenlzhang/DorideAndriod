package com.carlt.doride.data.login;

import com.carlt.doride.data.BaseResponseInfo;

/**
 * Created by marller on 2018\3\27 0027.
 */

public class UserRegisterInfo extends BaseResponseInfo {
    String id;    //用户ID
    String dealerid;    //经销商ID
    String username;    //用户名
    String realname;    //姓名
    String gender;    //性别(3:保密 1:男 2:女)
    String mobile;//手机号
    String status;//状态：-1禁用,0未验证,1已验证
    String weixinbind;//是否绑定微信: 1 微信账户绑定
    String clwbind;//是否绑定设备：2 APP设备未绑定,1 APP设备绑定
    String regip;//注册IP
    String avatar_id;//头像ID
    String originate;//注册来源，0：网站，1，APP-安卓，2：APP-IOS，3：微信
    String lastlogin;//上次登录时间
    String loginoauth;//登录授权oauth
    String logintimes;//登录次数
    String createdate;//注册时间
    String lifetime;//注册截止今日第几天
    String access_token;//授权登陆token

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDealerid() {
        return dealerid;
    }

    public void setDealerid(String dealerid) {
        this.dealerid = dealerid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getWeixinbind() {
        return weixinbind;
    }

    public void setWeixinbind(String weixinbind) {
        this.weixinbind = weixinbind;
    }

    public String getClwbind() {
        return clwbind;
    }

    public void setClwbind(String clwbind) {
        this.clwbind = clwbind;
    }

    public String getRegip() {
        return regip;
    }

    public void setRegip(String regip) {
        this.regip = regip;
    }

    public String getAvatar_id() {
        return avatar_id;
    }

    public void setAvatar_id(String avatar_id) {
        this.avatar_id = avatar_id;
    }

    public String getOriginate() {
        return originate;
    }

    public void setOriginate(String originate) {
        this.originate = originate;
    }

    public String getLastlogin() {
        return lastlogin;
    }

    public void setLastlogin(String lastlogin) {
        this.lastlogin = lastlogin;
    }

    public String getLoginoauth() {
        return loginoauth;
    }

    public void setLoginoauth(String loginoauth) {
        this.loginoauth = loginoauth;
    }

    public String getLogintimes() {
        return logintimes;
    }

    public void setLogintimes(String logintimes) {
        this.logintimes = logintimes;
    }

    public String getCreatedate() {
        return createdate;
    }

    public void setCreatedate(String createdate) {
        this.createdate = createdate;
    }

    public String getLifetime() {
        return lifetime;
    }

    public void setLifetime(String lifetime) {
        this.lifetime = lifetime;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(String expires_in) {
        this.expires_in = expires_in;
    }

    String expires_in;//授权生命周期
}

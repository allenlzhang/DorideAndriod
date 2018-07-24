
package com.carlt.sesame.data;

import java.io.Serializable;

/**
 * 本地保存的用户使用App的数据结构(游客版)
 * 
 * @author daisy
 */
public class VisitorInfo implements Serializable {

    private int times = 0;// 登录次数

    private String account;// 登录账号

    private String password;// 登录密码
    
    private String passwordRemote;// 远程密码
    
    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordRemote() {
        return passwordRemote;
    }

    public void setPasswordRemote(String passwordRemote) {
        this.passwordRemote = passwordRemote;
    }
}

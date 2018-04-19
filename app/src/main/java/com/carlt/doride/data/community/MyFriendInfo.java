
package com.carlt.doride.data.community;

import java.io.Serializable;

public class MyFriendInfo implements Serializable {

    // id
    private String id;

    // realname
    private String realname;

    private String username;

    // 头像URL
    private String img;

    // 车标
    private String carlogo;

    // 0保密1男2女
    private int gender;

    // 车款
    private String car;


    // 等级名称
    private String licencename;

    // 分享需要的属性
    private String shareLink;

    private String shareTitle;

    private String shareText;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getCar() {
        return car;
    }

    public void setCar(String car) {
        this.car = car;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCarlogo() {
        return carlogo;
    }

    public void setCarlogo(String carlogo) {
        this.carlogo = carlogo;
    }

    public String getLicencename() {
        return licencename;
    }

    public void setLicencename(String licencename) {
        this.licencename = licencename;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getShareLink() {
        return shareLink;
    }

    public void setShareLink(String shareLink) {
        this.shareLink = shareLink;
    }

    public String getShareTitle() {
        return shareTitle;
    }

    public void setShareTitle(String shareTitle) {
        this.shareTitle = shareTitle;
    }

    public String getShareText() {
        return shareText;
    }

    public void setShareText(String shareText) {
        this.shareText = shareText;
    }

}

package com.carlt.yema.data.car;

import com.carlt.yema.data.BaseResponseInfo;

/**
 * Created by liu on 2018/3/29.
 * life/securityMessage 返回
 *
 *   "relid": "12077",
 "pushstarttime": "00:00:00",
 "pushendtime": "23:59:59",
 "title": "异常振动",
 "content": "哈哈，你的车辆大众朗逸 2014款 1.6L 自动运动版，于2014-08-13 09:53:00，发生异常振动",
 "img": "",
 "class1": "21",
 "class2": "2101",
 "createdate": "2014-08-13 09:54",
 "istop": "0"
 */

public class SaftyMsgInfo extends BaseResponseInfo {

    private String relid;
    private String pushstarttime;
    private String pushendtime;
    private String title;
    private String content;
    private String img;
    private String class1;
    private String class2;
    private String createdate;
    private String istop;


    public String getRelid() {
        return relid;
    }

    public void setRelid(String relid) {
        this.relid = relid;
    }

    public String getPushstarttime() {
        return pushstarttime;
    }

    public void setPushstarttime(String pushstarttime) {
        this.pushstarttime = pushstarttime;
    }

    public String getPushendtime() {
        return pushendtime;
    }

    public void setPushendtime(String pushendtime) {
        this.pushendtime = pushendtime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getClass1() {
        return class1;
    }

    public void setClass1(String class1) {
        this.class1 = class1;
    }

    public String getClass2() {
        return class2;
    }

    public void setClass2(String class2) {
        this.class2 = class2;
    }

    public String getCreatedate() {
        return createdate;
    }

    public void setCreatedate(String createdate) {
        this.createdate = createdate;
    }

    public String getIstop() {
        return istop;
    }

    public void setIstop(String istop) {
        this.istop = istop;
    }

    @Override
    public String toString() {
        return "SaftyMsgInfo{" +
                "relid='" + relid + '\'' +
                ", pushstarttime='" + pushstarttime + '\'' +
                ", pushendtime='" + pushendtime + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", img='" + img + '\'' +
                ", class1='" + class1 + '\'' +
                ", class2='" + class2 + '\'' +
                ", createdate='" + createdate + '\'' +
                ", istop='" + istop + '\'' +
                '}';
    }
}

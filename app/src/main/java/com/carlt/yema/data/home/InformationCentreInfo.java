package com.carlt.yema.data.home;

/**
 * Created by Marlon on 2018/3/16.
 */

public class InformationCentreInfo {

    // 分类名
    private String name;
    // 分类code
    private int id;
    // 最后一条消息
    private String lastmsg;
    // 此分类下未读消息个数
    private int msgcount;
    // 最后一条消息时间
    private String msgdate;
    // 图片
    private int img;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;

    }

    public String getLastmsg() {
        return lastmsg;
    }

    public void setLastmsg(String lastmsg) {
        this.lastmsg = lastmsg;
    }

    public int getMsgcount() {
        return msgcount;
    }

    public void setMsgcount(int msgcount) {
        this.msgcount = msgcount;
    }

    public String getMsgdate() {
        return msgdate;
    }

    public void setMsgdate(String msgdate) {
        this.msgdate = msgdate;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

}

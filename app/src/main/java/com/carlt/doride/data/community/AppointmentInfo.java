
package com.carlt.doride.data.community;

public class AppointmentInfo {

    // 预约类型---保养
    public final static int TYPE_MAINTENANCE = 2;

    // 预约类型---维修
    public final static int TYPE_REPAIR = 1;

    // 预约类型
    protected int tyep;

    // 当前状态---未受理
    public final static int STATUS_UNACCEPTANCE = 0;

    // 当前状态---已受理
    public final static int STATUS_ACCEPTANCEED = 1;

    // 当前状态---待评价
    public final static int STATUS_UNEVALUATION = 2;

    // 当前状态---已完成
    public final static int STATUS_FINISHED = 3;

    // 当前状态---预约失败
    public final static int STATUS_ERRO = 4;

    // 当前状态
    protected int status;

    // 状态显示文字
    protected String status_show;

    // 预约描述
    protected String des;

    // ID
    protected String id;

    // 花费
    protected String spent;

    // 几颗星
    protected int star;

    // 时间描述
    protected String timeDes;

    // 预约时间
    protected String time;

    // 预约日期
    protected String date;

    public String getTimeDes() {
        return timeDes;
    }

    public void setTimeDes(String timeDes) {
        this.timeDes = timeDes;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getTyep() {
        return tyep;
    }

    public void setTyep(int tyep) {
        this.tyep = tyep;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStatus_show() {
        return status_show;
    }

    public void setStatus_show(String status_show) {
        this.status_show = status_show;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSpent() {
        return spent;
    }

    public void setSpent(String spent) {
        this.spent = spent;
    }

    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
    }

}

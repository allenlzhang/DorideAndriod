package com.carlt.yema.data.remote;

/**
 * Created by liu on 2018/3/31.
 * <p>
 * 远程操作日志 返回集合数据
 * 11: 远程闪灯鸣笛（声光寻车）
 * 21: 远程解锁
 * 22: 远程落锁
 * 31: 远程启动
 * 41: 远程熄火
 * 51: 远程开启空调
 * 52: 远程关闭空调
 * 53: 远程开启空调/一键除霜
 * 54: 远程开启空调/最大制冷
 * 55: 远程开启空调/最大制热
 * 56: 远程开启空调/负离子
 * 57: 远程开启空调/座舱清洁
 * 58: 远程温度调节
 * 61: 远程开窗
 * 62: 远程关窗
 * 63：远程开启天窗
 * 64：远程关闭天窗
 * 65： 开启天窗
 * 66： 关闭天窗
 * 71: 远程开启后备箱
 * 81: 远程开启座椅加热
 * 82: 远程关闭座椅加热
 * 91: 远程开启空气净化
 * 92: 远程关闭空气净化
 * 93 远程立即充电
 * 94 远程定时冲电
 * 95 远程停止充电
 * 96 取消定时充电
 * <p>
 * "logtype":"31",
 * "log_device_name":"xiaoming",
 * "log_result":"1", //操作结果 1成功 其他失败
 * "logtime":"2016-02-24 16:32"
 */

public class RemoteLogInfo {

    public final static String RESULT_SUCCESS="1";//成功

    private int logtype ;
    private String log_device_name ;
    private String log_result ;
    private String logtime ;


    public int getLogtype() {
        return logtype;
    }

    public void setLogtype(int logtype) {
        this.logtype = logtype;
    }

    public String getLog_device_name() {
        return log_device_name;
    }

    public void setLog_device_name(String log_device_name) {
        this.log_device_name = log_device_name;
    }

    public String getLog_result() {
        return log_result;
    }

    public void setLog_result(String log_result) {
        this.log_result = log_result;
    }

    public String getLogtime() {
        return logtime;
    }

    public void setLogtime(String logtime) {
        this.logtime = logtime;
    }
}

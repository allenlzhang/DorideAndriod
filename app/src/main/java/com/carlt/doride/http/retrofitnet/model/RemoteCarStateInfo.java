package com.carlt.doride.http.retrofitnet.model;

/**
 * Description:
 * Company    : carlt
 * Author     : zhanglei
 * Date       : 2019/4/3 11:08
 */
public class RemoteCarStateInfo {

    /**
     * AC : 2
     * ACTemp : 4294967300
     * bonnet : 255
     * doorClose : 0,0,0,0,1
     * doorCloseStatus : 1
     * doorLock : 1,1,1,1,1
     * doorLockStatus : 1
     * skyWindowStatus : 2
     * window : 0,0,0,0,2
     * windowStatus : 4294967295
     */

    public long    AC              = -1;// 空调模式 1-开启全自动；2-关闭；3-一键除霜；4-最大制冷；5-最大制热；6-负离子；7-座舱清洁；8-其他开启模式
    public long    purify          = -1;  // 空气净化状态 0-关闭 1-开启
    public String  ACTemp;
    public long    bonnet          = -1;    // 引擎盖状态
    public long    engine          = -1;  // 发动机状态 0-熄火 1-启动
    public long    seathot         = -1;  // 座椅加热状态
    public long    alarm           = -1;  // 报警状态
    public String  doorClose;      // 车门锁止 左前 右前 左后 右后 尾门（或后备箱）；0-关，1-开，255-不支持
    public long    doorCloseStatus = -1; // 车门锁止状态 0-锁 1-不锁
    public String  doorLock;              // 车门开关；左前 右前 左后 右后 尾门（或后备箱），引擎盖；0-关，1-开，255-不支持
    public long    doorLockStatus  = -1;   // 车门开关状态；0-关，1-开
    public long    skyWindowStatus = -1;   // 天窗 0-关，1-开，２-开翘，255 不支持
    public String  window;              // 车窗位置 左前 右前 左后 右后 天窗；0-关，1-开，2-天窗开撬（目前是B17-X7项目），255-不支持
    public long    windowStatus;    // 车窗 0-全关，1-全开，255 不支持
    public int     recTime;    // 历史上报时间
    public BaseErr err;

}

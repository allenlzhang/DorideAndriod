package com.carlt.doride.http.retrofitnet.model;

/**
 * Created by Marlon on 2019/3/29.
 */
public class CarConfigRes {
    private static CarConfigRes carConfigRes = null;
    private CarConfigRes(){}
    public static CarConfigRes getInstance(){
        if (carConfigRes == null){
            synchronized (CarConfigRes.class){
                if (carConfigRes == null){
                    carConfigRes = new CarConfigRes();
                }
            }
        }
        return carConfigRes;
    }
    //车款配置信息
    public int configAutoStartTop;              //自动启停
    public int autoRiseWindow;                  //自动升窗
    public int autoLocked;                      //自动落锁
    public int remindWindowUnclosed;            //车窗未关提醒
    public int remindDoorUnclosed;              //车门未关提醒
    public int remindDoorUnlocked;              //车门未锁提醒
    public int remindPrevention;                //防盗报警提醒
    public int remindPstSupervise;              //是否支持胎压检测
    public int remoteAirconditioner;            //远程空调
    public int remoteStart;                     //是否支持远程启动
    public int remoteLocked;                    //远程解落锁
    public int remoteRiseWindow;                //远程升关窗
    public int remoteCarLocating;               //是否支持声光寻车
    public int remoteTrunk;                     //远程后备箱
    public int remoteTrunkOn;                   //远程后备箱开启
    public int remoteTrunkOff;                  //远程后备箱关闭
    public int remoteSkylight;                  //远程开关天窗
    public int remoteNavSync;                   //导航同步
    public int remoteCharger;                   //是否支持远程充电
    public int remoteSkylightPry;               //远程开关天窗撬
    public int remoteSeatHeating;               //远程前排座椅加热
    public int remoteDirectPressure;            //直式胎压
    public int remoteIndirectPressure;          //间接胎压
    public int remoteLocating;                  //定位寻车
    public int remoteVehicleCondition;          //实时车况
    public int remoteConditionDetection;        //车况检测
    public int remoteMaintenancePlan;           //养护计划
    public int hasTachograph;                   //支持记录仪
    public String remoteAirconditionerItem;     //远程空调支持项
    //空调配置信息
    public int remoteAirconditionerAuto;        //全自动
    public int remoteAirconditionerMaxHeat;     //最大制热
    public int remoteAirconditionerMaxCold;     //最大制冷
    public int remoteAirconditionerClearFrost;  //一键除霜
    public int remoteAirconditionerTempCtl;     //温度控制
    public int remoteAirconditionerAnion;       //负离子
    public int remoteAirconditionerClearSeat;   //座舱清洁
    public int remoteAirconditionerTempDisplay; //温度展示
    public BaseErr err;

    public void setCarConfigRes(CarConfigRes carConfigRes){
        configAutoStartTop = carConfigRes.configAutoStartTop;
        autoRiseWindow = carConfigRes.autoRiseWindow;
        autoLocked = carConfigRes.autoLocked;
        remindWindowUnclosed = carConfigRes.remindWindowUnclosed;
        remindDoorUnclosed = carConfigRes.remindDoorUnclosed;
        remindDoorUnlocked = carConfigRes.remindDoorUnlocked;
        remindPrevention = carConfigRes.remindPrevention;
        remindPstSupervise = carConfigRes.remindPstSupervise;
        remoteAirconditioner = carConfigRes.remoteAirconditioner;
        remoteStart = carConfigRes.remoteStart;
        remoteLocked = carConfigRes.remoteLocked;
        remoteRiseWindow = carConfigRes.remoteRiseWindow;
        remoteCarLocating = carConfigRes.remoteCarLocating;
        remoteTrunk = carConfigRes.remoteTrunk;
        remoteTrunkOn = carConfigRes.remoteTrunkOn;
        remoteTrunkOff = carConfigRes.remoteTrunkOff;
        remoteSkylight = carConfigRes.remoteSkylight;
        remoteNavSync = carConfigRes.remoteNavSync;
        remoteCharger = carConfigRes.remoteCharger;
        remoteSkylightPry = carConfigRes.remoteSkylightPry;
        remoteSeatHeating = carConfigRes.remoteSeatHeating;
        remoteDirectPressure = carConfigRes.remoteDirectPressure;
        remoteIndirectPressure = carConfigRes.remoteIndirectPressure;
        remoteLocating = carConfigRes.remoteLocating;
        remoteVehicleCondition = carConfigRes.remoteVehicleCondition;
        remoteConditionDetection = carConfigRes.remoteConditionDetection;
        remoteMaintenancePlan = carConfigRes.remoteMaintenancePlan;
        hasTachograph = carConfigRes.hasTachograph;
        remoteAirconditionerItem = carConfigRes.remoteAirconditionerItem;
        remoteAirconditionerAuto = carConfigRes.remoteAirconditionerAuto;
        remoteAirconditionerMaxHeat = carConfigRes.remoteAirconditionerMaxHeat;
        remoteAirconditionerMaxCold = carConfigRes.remoteAirconditionerMaxCold;
        remoteAirconditionerClearFrost = carConfigRes.remoteAirconditionerClearFrost;
        remoteAirconditionerTempCtl = carConfigRes.remoteAirconditionerTempCtl;
        remoteAirconditionerAnion = carConfigRes.remoteAirconditionerAnion;
        remoteAirconditionerClearSeat = carConfigRes.remoteAirconditionerClearSeat;
        remoteAirconditionerTempDisplay = carConfigRes.remoteAirconditionerTempDisplay;
    }

    public void initCarConfigRes(){
        configAutoStartTop = 0;
        autoRiseWindow = 0;
        autoLocked = 0;
        remindWindowUnclosed = 0;
        remindDoorUnclosed = 0;
        remindDoorUnlocked = 0;
        remindPrevention = 0;
        remindPstSupervise = 0;
        remoteAirconditioner = 0;
        remoteStart = 0;
        remoteLocked = 0;
        remoteRiseWindow = 0;
        remoteCarLocating = 0;
        remoteTrunk = 0;
        remoteTrunkOn = 0;
        remoteTrunkOff = 0;
        remoteSkylight = 0;
        remoteNavSync = 0;
        remoteCharger = 0;
        remoteSkylightPry = 0;
        remoteSeatHeating = 0;
        remoteDirectPressure = 0;
        remoteIndirectPressure = 0;
        remoteLocating = 0;
        remoteVehicleCondition = 0;
        remoteConditionDetection = 0;
        remoteMaintenancePlan = 0;
        hasTachograph = 0;
        remoteAirconditionerItem = "";
        remoteAirconditionerAuto = 0;
        remoteAirconditionerMaxHeat = 0;
        remoteAirconditionerMaxCold = 0;
        remoteAirconditionerClearFrost = 0;
        remoteAirconditionerTempCtl = 0;
        remoteAirconditionerAnion = 0;
        remoteAirconditionerClearSeat = 0;
        remoteAirconditionerTempDisplay = 0;

    }
}

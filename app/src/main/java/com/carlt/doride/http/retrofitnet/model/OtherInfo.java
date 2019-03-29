package com.carlt.doride.http.retrofitnet.model;

import com.carlt.sesame.data.car.CarMainFunInfo;
import com.carlt.sesame.data.remote.RemoteMainInfo;

/**
 * Created by Marlon on 2019/3/28.
 */
public class OtherInfo {
    private static OtherInfo otherInfo = null;
    private OtherInfo(){}
    public static OtherInfo getInstance(){
        if (otherInfo == null){
            synchronized (OtherInfo.class){
                if (otherInfo == null){
                    otherInfo = new OtherInfo();
                }
            }
        }
        return otherInfo;
    }
    private String limit_warning = "";//红点提醒

    // 是否是主机
    private boolean isMain = false;

    private String cityName = "";

    private RemoteMainInfo remoteMainInfo;// 远程首页数据
    private CarMainFunInfo carMainFunInfo;// 座驾首页支持的功能数据

    // 远程音效开关是否打开
    private boolean isRemoteSoundOpen;

    public String getLimit_warning() {
        return limit_warning;
    }

    public void setLimit_warning(String limit_warning) {
        this.limit_warning = limit_warning;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public boolean isMain() {
        return isMain;
    }

    public void setIsMain(boolean isMain) {
        this.isMain = isMain;
    }

    public RemoteMainInfo getRemoteMainInfo() {
        return remoteMainInfo;
    }

    public void setRemoteMainInfo(RemoteMainInfo remoteMainInfo) {
        this.remoteMainInfo = remoteMainInfo;
    }

    public CarMainFunInfo getCarMainFunInfo() {
        return carMainFunInfo;
    }

    public void setCarMainFunInfo(CarMainFunInfo carMainFunInfo) {
        this.carMainFunInfo = carMainFunInfo;
    }

    public boolean isRemoteSoundOpen() {
        return isRemoteSoundOpen;
    }

    public void setRemoteSoundOpen(boolean remoteSoundOpen) {
        isRemoteSoundOpen = remoteSoundOpen;
    }



    public void initInfo(){
        setLimit_warning("");
        setIsMain(false);
        setCityName("");
        setRemoteMainInfo(null);
        setCarMainFunInfo(null);
        setRemoteSoundOpen(false);
    }
}

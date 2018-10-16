package com.carlt.sesame.data.car;

import com.carlt.sesame.data.BaseResponseInfo;

import java.io.Serializable;

public class CarInfo extends BaseResponseInfo implements Serializable{
    
    private String id;//主键id
    private String cityCode;//城市code
    private String cityName;//城市名称
    private String carNo;//车牌号
    private String engineNo;//发动机号
    private String standcarNo;//车架号
    private String registNo;//登记证书号
    private String type;//车辆类型
    public final static String TYPE_MY="1";
    public final static String TYPE_OTHER="0";
    private String addtime;//添加时间
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getCityCode() {
        return cityCode;
    }
    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }
    public String getCityName() {
        return cityName;
    }
    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
    public String getCarNo() {
        return carNo;
    }
    public void setCarNo(String carNo) {
        this.carNo = carNo;
    }
    public String getEngineNo() {
        return engineNo;
    }
    public void setEngineNo(String engineNo) {
        this.engineNo = engineNo;
    }
    public String getStandcarNo() {
        return standcarNo;
    }
    public void setStandcarNo(String standcarNo) {
        this.standcarNo = standcarNo;
    }
    public String getRegistNo() {
        return registNo;
    }
    public void setRegistNo(String registNo) {
        this.registNo = registNo;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getAddtime() {
        return addtime;
    }
    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }
    
}

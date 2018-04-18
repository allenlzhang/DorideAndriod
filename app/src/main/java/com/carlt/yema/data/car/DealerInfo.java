package com.carlt.yema.data.car;

import com.carlt.yema.data.BaseResponseInfo;

/**
 * Created by marller on 2018\4\2 0002.
 */

public class DealerInfo extends BaseResponseInfo {
    private String dealerAddress;//经销商地址
    private String dealerName;//经销商名称
    private String dealerMap;//经销商百度经纬度坐标
    private String dealerTel;//经销商服务电话
    private String serviceTel;//经销商销售电话（暂用作卡尔特客服电话）

    public String getDealerAddress() {
        return dealerAddress;
    }

    public void setDealerAddress(String dealerAddress) {
        this.dealerAddress = dealerAddress;
    }

    public String getDealerName() {
        return dealerName;
    }

    public void setDealerName(String dealerName) {
        this.dealerName = dealerName;
    }

    public String getDealerMap() {
        return dealerMap;
    }

    public void setDealerMap(String dealerMap) {
        this.dealerMap = dealerMap;
    }

    public String getDealerTel() {
        return dealerTel;
    }

    public void setDealerTel(String dealerTel) {
        this.dealerTel = dealerTel;
    }

    public String getServiceTel() {
        return serviceTel;
    }

    public void setServiceTel(String serviceTel) {
        this.serviceTel = serviceTel;
    }
}

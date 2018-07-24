
package com.carlt.sesame.data.set;

import com.carlt.sesame.data.BaseResponseInfo;

/**
 * 近期登录记录info
 * 
 * @author Administrator
 */
public class FeeLogInfo extends BaseResponseInfo {

    private String name;// 手机名称

    private String datePay;// 续费日期

    private String fee;// 金额

    private String dateFromto;// 服务时间段

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDatePay() {
        return datePay;
    }

    public void setDatePay(String datePay) {
        this.datePay = datePay;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getDateFromto() {
        return dateFromto;
    }

    public void setDateFromto(String dateFromto) {
        this.dateFromto = dateFromto;
    }
}

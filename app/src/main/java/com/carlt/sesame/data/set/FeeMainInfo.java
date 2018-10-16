
package com.carlt.sesame.data.set;

import java.util.ArrayList;

/**
 * 续费主页info
 * 
 * @author Administrator
 */
public class FeeMainInfo {
    ArrayList<FeeTypeInfo> mFeeTypeInfos;//费用类型列表

    String serviceEndDate;// 服务截至日期

    boolean timeIsRed;// 服务时间是否为红色

    public ArrayList<FeeTypeInfo> getmFeeTypeInfos() {
        return mFeeTypeInfos;
    }

    public void setmFeeTypeInfos(ArrayList<FeeTypeInfo> mFeeTypeInfos) {
        this.mFeeTypeInfos = mFeeTypeInfos;
    }

    public String getServiceEndDate() {
        return serviceEndDate;
    }

    public void setServiceEndDate(String serviceEndDate) {
        this.serviceEndDate = serviceEndDate;
    }

    public boolean isTimeIsRed() {
        return timeIsRed;
    }

    public void setTimeIsRed(boolean timeIsRed) {
        this.timeIsRed = timeIsRed;
    }
}

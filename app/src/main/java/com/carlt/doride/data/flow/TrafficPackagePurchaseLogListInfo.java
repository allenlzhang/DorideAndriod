
package com.carlt.doride.data.flow;

import java.util.ArrayList;

/**
 * 近期登录记录列表info
 * @author Administrator
 *
 */
public class TrafficPackagePurchaseLogListInfo{

    private int limit;

    private int offset;

    private boolean has_next;//是否有下一页




    private ArrayList<TrafficPackagePurchaseLogInfo> mLogInfoList=new ArrayList<>();



    public void addAllTrafficPackagePurchaseLogList(ArrayList<TrafficPackagePurchaseLogInfo> mList) {
        mLogInfoList.addAll(mList);
    }

    public void addTrafficPackagePurchaseLogInfo(TrafficPackagePurchaseLogInfo mTrafficPackagePurchaseLogInfo){
        mLogInfoList.add(mTrafficPackagePurchaseLogInfo);
    }

    public ArrayList<TrafficPackagePurchaseLogInfo> getmTrafficPackagePurchaseLogInfoList() {
        return mLogInfoList;
    }


    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public boolean isHas_next() {
        return has_next;
    }

    public void setHas_next(boolean has_next) {
        this.has_next = has_next;
    }

    @Override
    public String toString() {
        return "TrafficPackagePurchaseLogListInfo{" +
                "limit=" + limit +
                ", offset=" + offset +
                ", has_next=" + has_next +
                ", mLogInfoList=" + mLogInfoList +
                '}';
    }
}

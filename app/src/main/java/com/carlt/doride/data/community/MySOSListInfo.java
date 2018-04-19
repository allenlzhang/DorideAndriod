
package com.carlt.doride.data.community;

import java.util.ArrayList;

public class MySOSListInfo {

    private int limit;

    private int offset;

    private ArrayList<SOSInfo> mSOSInfoList = new ArrayList<SOSInfo>();

    public ArrayList<SOSInfo> getmSOSInfoList() {
        return mSOSInfoList;
    }

    public void addmSOSInfoList(SOSInfo mSOSInfo) {
        mSOSInfoList.add(mSOSInfo);
    }

    public void addmSOSInfoList(ArrayList<SOSInfo> mList) {
        mSOSInfoList.addAll(mList);
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
}

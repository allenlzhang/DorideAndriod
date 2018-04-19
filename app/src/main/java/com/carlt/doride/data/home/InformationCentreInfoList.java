package com.carlt.doride.data.home;

import com.carlt.doride.data.BaseResponseInfo;

import java.util.ArrayList;

/**
 * Created by Marlon on 2018/3/16.
 */

public class InformationCentreInfoList extends BaseResponseInfo{
    private ArrayList<InformationCentreInfo> mAllList = new ArrayList<InformationCentreInfo>();

    // 未读消息个数
    private int unreadCount;

    public ArrayList<InformationCentreInfo> getmAllList() {
        return mAllList;
    }

    public void addmAllList(InformationCentreInfo mInfo) {
        this.mAllList.add(mInfo);
    }

    public void addmAllList(ArrayList<InformationCentreInfo> list) {
        this.mAllList.addAll(list);
    }

    public int getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(int unreadCount) {
        this.unreadCount = unreadCount;
    }

}

package com.carlt.yema.data.home;

import java.util.ArrayList;

/**
 * Created by Marlon on 2018/3/16.
 */

public class InformationCategoryInfoList {
    private ArrayList<InformationCategoryInfo> mAllList = new ArrayList<InformationCategoryInfo>();

    // 未读消息个数
    private String unreadCount;

    public ArrayList<InformationCategoryInfo> getmAllList() {
        return mAllList;
    }

    public void addmAllList(InformationCategoryInfo mInfo) {
        this.mAllList.add(mInfo);
    }

    public void addmAllList(ArrayList<InformationCategoryInfo> list) {
        this.mAllList.addAll(list);
    }

    public String getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(String unreadCount) {
        this.unreadCount = unreadCount;
    }

}

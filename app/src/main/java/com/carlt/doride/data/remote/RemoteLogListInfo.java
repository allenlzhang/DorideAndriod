package com.carlt.doride.data.remote;

import java.util.ArrayList;

/**
 * 远程操作历史记录返回data
 * Created by liu on 2018/3/31.
 */

public class RemoteLogListInfo {
    private ArrayList<RemoteLogInfo> list;

    public ArrayList<RemoteLogInfo> getList() {
        return list;
    }

    public void setList(ArrayList<RemoteLogInfo> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "RemoteLogListInfo{" +
                "list=" + list +
                '}';
    }
}

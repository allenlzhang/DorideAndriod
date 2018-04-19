package com.carlt.doride.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by yyun on 17-11-2.
 */

public class ListModel<T> {
    @SerializedName("list")
    private List<T> list;
    @SerializedName("count")
    private int count;

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }


}

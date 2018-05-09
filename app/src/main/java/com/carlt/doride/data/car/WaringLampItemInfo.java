package com.carlt.doride.data.car;

/**
 * Created by Marlon on 2018/3/31.
 */

public class WaringLampItemInfo {
    private int    iconState;
    public  int    icon;
    private String txt;
    private int    color;

    public int getIconState() {
        return iconState;
    }

    public void setIconState(int iconState) {
        this.iconState = iconState;
    }

    public String getTxt() {
        return txt;
    }

    public void setTxt(String txt) {
        this.txt = txt;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}

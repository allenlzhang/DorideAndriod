
package com.carlt.sesame.data;

public class ReportChartParamInfo {

    // 公用属性
    float locationX;// X坐标

    float locationY; // Y坐标

    int color;// 图形的色值

    // 线的属性
    float lineStartX;// 起始X坐标

    float lineStartY; // 起始 Y坐标

    float lineEndX;// 起始X坐标

    float lineEndY; // 起始Y坐标

    // 文字的属性
    String text;// 显示的文字
    float textSize;//文字大小

    public float getLocationX() {
        return locationX;
    }

    public void setLocationX(float locationX) {
        this.locationX = locationX;
    }

    public float getLocationY() {
        return locationY;
    }

    public void setLocationY(float locationY) {
        this.locationY = locationY;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public float getLineStartX() {
        return lineStartX;
    }

    public void setLineStartX(float lineStartX) {
        this.lineStartX = lineStartX;
    }

    public float getLineStartY() {
        return lineStartY;
    }

    public void setLineStartY(float lineStartY) {
        this.lineStartY = lineStartY;
    }

    public float getLineEndX() {
        return lineEndX;
    }

    public void setLineEndX(float lineEndX) {
        this.lineEndX = lineEndX;
    }

    public float getLineEndY() {
        return lineEndY;
    }

    public void setLineEndY(float lineEndY) {
        this.lineEndY = lineEndY;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public float getTextSize() {
        return textSize;
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
    }
    
}

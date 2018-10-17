package com.carlt.chelepie.data.recorder;

/**
 * 设置属性info
 * @author Administrator
 *
 */
public class PieAttrInfo {
    String name;//属性名
    int value;//属性值
    boolean isSelected;//是否已选择
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getValue() {
        return value;
    }
    public void setValue(int value) {
        this.value = value;
    }
    public boolean isSelected() {
        return isSelected;
    }
    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }
    
}

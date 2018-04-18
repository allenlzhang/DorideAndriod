package com.carlt.yema.data.home;
/**
 * 月报单个数据Info
 * @author Daisy
 *
 */
public class ReportMonthDataInfo {
	private String color;//item前需要展示的color颜色
	private String title;//标题
	private String value;//数值
	private String unit;//单位
	
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
}

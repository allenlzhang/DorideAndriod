package com.carlt.sesame.data.career;

public class MonthOrderStateInfo {
	private String id;
	// 日期
	private String date;
	// left:剩余可用预约数 大于零可以预约
	private int left;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public int getLeft() {
		return left;
	}
	public void setLeft(int left) {
		this.left = left;
	}
	
	
}

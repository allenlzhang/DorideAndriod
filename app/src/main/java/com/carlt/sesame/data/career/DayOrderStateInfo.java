package com.carlt.sesame.data.career;


public class DayOrderStateInfo {
	// 当前时间总共可用工位数
	private int total;
	// 已被预约的工位数，如果total - used =0，则工位已全部预约，当前时间不可再预约
	private int used;
	// 时间
	private String time;

	// 此字段仅用于界面显示，标识是否为上下午标题
	private boolean flag = false;

	// 排序索引，界面用不到
	private double preference;

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getUsed() {
		return used;
	}

	public void setUsed(int used) {
		this.used = used;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public double getPreference() {
		return preference;
	}

	public void setPreference(double preference) {
		this.preference = preference;
	}

}

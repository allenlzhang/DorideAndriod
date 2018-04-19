package com.carlt.doride.data.home;



/**
 * 月报统计信息（总油耗、总里程、总时间）
 * @author Daisy
 *
 */
public class MonthStatisticInfo{
	private int sumfuel;//总油耗
	private int summiles;//总里程
	private int sumtime;//总时间
	private int month;//总时间
	private String date;//月报对应日期，对应月份最后一天 ，如 2017-07-31
	public int getSumfuel() {
		return sumfuel;
	}
	public void setSumfuel(int sumfuel) {
		this.sumfuel = sumfuel;
	}
	public int getSummiles() {
		return summiles;
	}
	public void setSummiles(int summiles) {
		this.summiles = summiles;
	}
	public int getSumtime() {
		return sumtime;
	}
	public void setSumtime(int sumtime) {
		this.sumtime = sumtime;
	}
	public int getMonth() {
		return month;
	}
	public void setMonth(int month) {
		this.month = month;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
}

package com.carlt.yema.data.home;



import com.carlt.yema.data.BaseResponseInfo;

import java.util.ArrayList;

/**
 * 月报统计信息--一年数据list
 * 
 * @author Daisy
 * 
 */
public class MonthStatisticChartInfo extends BaseResponseInfo {
	private int sumfuel_total;// 总油耗（插上盒子到当期日期数据总和）
	private int summiles_total;// 总里程（插上盒子到当期日期数据总和）
	private int sumtime_total;// 总时间（插上盒子到当期日期数据总和）
	private int maxValue_fuel;// 列表中数据的最大值--油耗
	private int maxValue_miles;// 列表中数据的最大值--里程
	private int maxValue_time;// 列表中数据的最大值--行驶时间

	private int maxValue_fuel_show;// 列表中展示数据的最大值--油耗
	private int maxValue_miles_show;// 列表中展示数据的最大值--里程
	private int maxValue_time_show;// 列表中展示数据的最大值--行驶时间

	private ArrayList<MonthStatisticInfo> monthStatisticInfos = new ArrayList<MonthStatisticInfo>();

	public int getSumfuel_total() {
		return sumfuel_total;
	}

	public void setSumfuel_total(int sumfuel_total) {
		this.sumfuel_total = sumfuel_total;
	}

	public int getSummiles_total() {
		return summiles_total;
	}

	public void setSummiles_total(int summiles_total) {
		this.summiles_total = summiles_total;
	}

	public int getSumtime_total() {
		return sumtime_total;
	}

	public void setSumtime_total(int sumtime_total) {
		this.sumtime_total = sumtime_total;
	}

	public int getMaxValue_fuel() {
		return maxValue_fuel;
	}

	public void setMaxValue_fuel(int maxValue_fuel) {
		this.maxValue_fuel = maxValue_fuel;
	}

	public int getMaxValue_miles() {
		return maxValue_miles;
	}

	public void setMaxValue_miles(int maxValue_miles) {
		this.maxValue_miles = maxValue_miles;
	}

	public int getMaxValue_time() {
		return maxValue_time;
	}

	public void setMaxValue_time(int maxValue_time) {
		this.maxValue_time = maxValue_time;
	}

	public int getMaxValue_fuel_show() {
		return maxValue_fuel_show;
	}

	public void setMaxValue_fuel_show(int maxValue_fuel_show) {
		this.maxValue_fuel_show = maxValue_fuel_show;
	}

	public int getMaxValue_miles_show() {
		return maxValue_miles_show;
	}

	public void setMaxValue_miles_show(int maxValue_miles_show) {
		this.maxValue_miles_show = maxValue_miles_show;
	}

	public int getMaxValue_time_show() {
		return maxValue_time_show;
	}

	public void setMaxValue_time_show(int maxValue_time_show) {
		this.maxValue_time_show = maxValue_time_show;
	}

	public ArrayList<MonthStatisticInfo> getMonthStatisticInfos() {
		return monthStatisticInfos;
	}

	public void setMonthStatisticInfos(
			ArrayList<MonthStatisticInfo> monthStatisticInfos) {
		this.monthStatisticInfos = monthStatisticInfos;
	}

	public void addMonthStatisticInfos(MonthStatisticInfo monthStatisticInfo) {
		this.monthStatisticInfos.add(monthStatisticInfo);
	}

}

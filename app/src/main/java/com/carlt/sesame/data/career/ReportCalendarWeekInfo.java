package com.carlt.sesame.data.career;

public class ReportCalendarWeekInfo {
	// 起始日期
	private String startDay;
	// 结束日期
	private String endDay;
	// ID
	private String id;
	// 得分
	private String point;
	// 分数颜色
	private int pointColor;

	public String getStartDay() {
		return startDay;
	}

	public void setStartDay(String startDay) {
		this.startDay = startDay;
	}

	public String getEndDay() {
		return endDay;
	}

	public void setEndDay(String endDay) {
		this.endDay = endDay;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPoint() {
		return point;
	}

	public void setPoint(String point) {
		this.point = point;
	}

	public int getPointColor() {
		return pointColor;
	}

	public void setPointColor(int pointColor) {
		this.pointColor = pointColor;
	}

}

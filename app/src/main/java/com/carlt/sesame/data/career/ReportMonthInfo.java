package com.carlt.sesame.data.career;

public class ReportMonthInfo extends ReportBaseInfo {
	// 平均得分
	private String avgPoint;
	// 与上月比较的变化，增加为正，减少为负，值百分比，如20
	private int avgPointcompare;
	// 驾驶诊断的标签，多个用逗号隔开
	private String tag;
	// "时间都去哪儿啦",
	private String sumtimedesc;
	// "能绕地球两圈了",
	private String summilesdesc;
	// "油费够买10包泡面了？",
	private String sumfueldesc;
	// "乌龟",
	private String avgspeeddesc;
	// "太快了〜〜",
	private String maxspeeddesc;

	public String getSumtimedesc() {
		return sumtimedesc;
	}

	public void setSumtimedesc(String sumtimedesc) {
		this.sumtimedesc = sumtimedesc;
	}

	public String getSummilesdesc() {
		return summilesdesc;
	}

	public void setSummilesdesc(String summilesdesc) {
		this.summilesdesc = summilesdesc;
	}

	public String getSumfueldesc() {
		return sumfueldesc;
	}

	public void setSumfueldesc(String sumfueldesc) {
		this.sumfueldesc = sumfueldesc;
	}

	public String getAvgspeeddesc() {
		return avgspeeddesc;
	}

	public void setAvgspeeddesc(String avgspeeddesc) {
		this.avgspeeddesc = avgspeeddesc;
	}

	public String getMaxspeeddesc() {
		return maxspeeddesc;
	}

	public void setMaxspeeddesc(String maxspeeddesc) {
		this.maxspeeddesc = maxspeeddesc;
	}

	public String getAvgPoint() {
		return avgPoint;
	}

	public void setAvgPoint(String avgPoint) {
		this.avgPoint = avgPoint;
	}

	public int getAvgPointcompare() {
		return avgPointcompare;
	}

	public void setAvgPointcompare(int avgPointcompare) {
		this.avgPointcompare = avgPointcompare;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

}

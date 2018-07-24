package com.carlt.sesame.data.career;

public class ReportDayInfo extends ReportBaseInfo {
	// 得分
	private String point;
	// 得分描述
	private String pointdesc;
	// 排名，百分比，0-100
	private int rank;

	// 本日行驶次数
	private int runtimes;

	// 驾驶诊断的标签，多个用逗号隔开
	private String tag;

	// 最佳显示文字
	private String best;
	
	private String sumTime;
	private String sumTimeStr;

	public String getSumTime() {
		return sumTime;
	}

	public void setSumTime(String sumTime) {
		this.sumTime = sumTime;
	}

	public String getSumTimeStr() {
		return sumTimeStr;
	}

	public void setSumTimeStr(String sumTimeStr) {
		this.sumTimeStr = sumTimeStr;
	}

	public int getRuntimes() {
		return runtimes;
	}

	public void setRuntimes(int runtimes) {
		this.runtimes = runtimes;
	}

	public String getPoint() {
		return point;
	}

	public void setPoint(String point) {
		this.point = point;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getBest() {
		return best;
	}

	public void setBest(String best) {
		this.best = best;
	}

	public String getPointdesc() {
		return pointdesc;
	}

	public void setPointdesc(String pointdesc) {
		this.pointdesc = pointdesc;
	}

}

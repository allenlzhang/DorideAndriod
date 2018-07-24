package com.carlt.sesame.data.career;

import java.util.ArrayList;

public class ReportWeekInfo extends ReportBaseInfo {
	// 得分
	private String avgpoint;
	// 与上月比较的变化，增加为正，减少为负，值百分比，如20
	private int avgpointcompare;
	// 驾驶诊断的标签，多个用逗号隔开
	private String tag;
	// 排名，百分比，0-100
	private int rank;
	// 得分描述
	private String pointdesc;

	private ArrayList<ReportWeekChatInfo> mChatList = new ArrayList<ReportWeekChatInfo>();

	public String getPointdesc() {
		return pointdesc;
	}

	public void setPointdesc(String pointdesc) {
		this.pointdesc = pointdesc;
	}

	public String getAvgpoint() {
		return avgpoint;
	}

	public void setAvgpoint(String avgpoint) {
		this.avgpoint = avgpoint;
	}

	public int getAvgpointcompare() {
		return avgpointcompare;
	}

	public void setAvgpointcompare(int avgpointcompare) {
		this.avgpointcompare = avgpointcompare;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public ArrayList<ReportWeekChatInfo> getmChatList() {
		return mChatList;
	}

	public void AddmChatList(ReportWeekChatInfo mInfo) {
		mChatList.add(mInfo);
	}

}

package com.carlt.yema.data.home;

import java.util.ArrayList;

/**
 * 20170711 v1.4.0 新版月报信息
 * 
 * @author Daisy
 * 
 */
public class ReportMonthInfoNew {
	private String realname;// 用户名真实姓名
	private String gender;// 性别(3:保密 1:男 2:女)
	private String avatar_img;// 头像地址
	private int count;// 本月行车次数
	private String avgpoint;// 平均得分
	private String maxpoint;// 最高得分
	private String avgpointcompare;// /与上月比较的变化，增加为正，减少为负
	private int brake;// 本月刹车次数
	private int turn;// 本月急转弯次数
	private String speedup;// 本月加速次数
	private String overspeed;// 本月高转速次数
	private String avgfuel;// 月平均百公里油耗

	private int sumtime;// 行车总时间
	private int maxspeed;// 最高时速
	private String summiles;// 总里程
	private int sumfuel;// 总油耗
	private String avgspeed;// 平均时速
	private int ismessaged;// 是否生成报表消息，0：未生成，1：已生成
	private String tag;// 驾驶诊断的标签
	private String reportdate;// 报告所属日期
	private long createtime;// 报告创建日期
	private String sumtimedesc;// 时间都去哪了，0%的时间都在路上
	private String summilesdesc;// 可以绕太湖跑0.8圈了
	private String sumfueldesc;// 本月油费0元，可以买0包方便面了
	private String avgspeeddesc;// 平均速度匹配老鹰
	private String maxspeeddesc;// 谢谢你没给高速公路添堵
	private String brakedesc;// 这下省出不少钱，晚上可以加个菜了！
	private String turndesc;// 车开的很艺术，过弯的弧线太优美了！
	private String speedupdesc;// 低调起跑，稳健行驶，深藏功与名！
	private String pointdesc;// 本月车技堪比舒马赫
	private String overspeeddesc;// 转速不高，发动机心跳平缓情绪稳定!
	private int sumtime_hour;// 总时长-小时
	private int sumtime_min;// 总时长-分钟
	private String sumtime_str;// 总时长文字展示

	private ArrayList<ReportMonthDataInfo> monthDataInfos = new ArrayList<ReportMonthDataInfo>();

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getAvatar_img() {
		return avatar_img;
	}

	public void setAvatar_img(String avatar_img) {
		this.avatar_img = avatar_img;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getAvgpoint() {
		return avgpoint;
	}

	public void setAvgpoint(String avgpoint) {
		this.avgpoint = avgpoint;
	}

	public String getMaxpoint() {
		return maxpoint;
	}

	public void setMaxpoint(String maxpoint) {
		this.maxpoint = maxpoint;
	}

	public String getAvgpointcompare() {
		return avgpointcompare;
	}

	public void setAvgpointcompare(String avgpointcompare) {
		this.avgpointcompare = avgpointcompare;
	}

	public int getBrake() {
		return brake;
	}

	public void setBrake(int brake) {
		this.brake = brake;
	}

	public int getTurn() {
		return turn;
	}

	public void setTurn(int turn) {
		this.turn = turn;
	}

	public String getSpeedup() {
		return speedup;
	}

	public void setSpeedup(String speedup) {
		this.speedup = speedup;
	}

	public String getOverspeed() {
		return overspeed;
	}

	public void setOverspeed(String overspeed) {
		this.overspeed = overspeed;
	}

	public String getAvgfuel() {
		return avgfuel;
	}

	public void setAvgfuel(String avgfuel) {
		this.avgfuel = avgfuel;
	}

	public int getSumtime() {
		return sumtime;
	}

	public void setSumtime(int sumtime) {
		this.sumtime = sumtime;
	}

	public int getMaxspeed() {
		return maxspeed;
	}

	public void setMaxspeed(int maxspeed) {
		this.maxspeed = maxspeed;
	}

	public String getSummiles() {
		return summiles;
	}

	public void setSummiles(String summiles) {
		this.summiles = summiles;
	}

	public int getSumfuel() {
		return sumfuel;
	}

	public void setSumfuel(int sumfuel) {
		this.sumfuel = sumfuel;
	}

	public String getAvgspeed() {
		return avgspeed;
	}

	public void setAvgspeed(String avgspeed) {
		this.avgspeed = avgspeed;
	}

	public int getIsmessaged() {
		return ismessaged;
	}

	public void setIsmessaged(int ismessaged) {
		this.ismessaged = ismessaged;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getReportdate() {
		return reportdate;
	}

	public void setReportdate(String reportdate) {
		this.reportdate = reportdate;
	}

	public long getCreatetime() {
		return createtime;
	}

	public void setCreatetime(long createtime) {
		this.createtime = createtime;
	}

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

	public String getBrakedesc() {
		return brakedesc;
	}

	public void setBrakedesc(String brakedesc) {
		this.brakedesc = brakedesc;
	}

	public String getTurndesc() {
		return turndesc;
	}

	public void setTurndesc(String turndesc) {
		this.turndesc = turndesc;
	}

	public String getSpeedupdesc() {
		return speedupdesc;
	}

	public void setSpeedupdesc(String speedupdesc) {
		this.speedupdesc = speedupdesc;
	}

	public String getPointdesc() {
		return pointdesc;
	}

	public void setPointdesc(String pointdesc) {
		this.pointdesc = pointdesc;
	}

	public String getOverspeeddesc() {
		return overspeeddesc;
	}

	public void setOverspeeddesc(String overspeeddesc) {
		this.overspeeddesc = overspeeddesc;
	}

	public int getSumtime_hour() {
		return sumtime_hour;
	}

	public void setSumtime_hour(int sumtime_hour) {
		this.sumtime_hour = sumtime_hour;
	}

	public int getSumtime_min() {
		return sumtime_min;
	}

	public void setSumtime_min(int sumtime_min) {
		this.sumtime_min = sumtime_min;
	}

	public String getSumtime_str() {
		return sumtime_str;
	}

	public void setSumtime_str(String sumtime_str) {
		this.sumtime_str = sumtime_str;
	}

	public void addMonthDataInfos(ReportMonthDataInfo reportMonthDataInfo) {
		this.monthDataInfos.add(reportMonthDataInfo);
	}

	public ArrayList<ReportMonthDataInfo> getMonthDataInfos() {
		return monthDataInfos;
	}
}

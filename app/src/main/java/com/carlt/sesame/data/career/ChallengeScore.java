package com.carlt.sesame.data.career;

import java.io.Serializable;
import java.util.ArrayList;

public class ChallengeScore implements Serializable {
	// 车秘书对话框内容
	private String name;
	// 本次成绩
	private String score;
	// 单位
	private String scoreunit;
	// 积分
	private String credit;
	// 急刹车次数
	private String chDBBrake;
	// 急加速次数
	private String chDBAcce;
	// 急转弯次数
	private String chDBTurn;
	// 高转速次数 （高转速）
	private String chDBHES;
	/** 评分挑战用到参数 **/
	// 评分挑战下的文字描述 "您的车技相当于F1车手"
	private String pointdesc;
	/** 省时挑战用到参数 **/
	// 行车时间
	private String time;
	// 最高速度
	private String maxSpeed;
	// 平均时速
	private String avgSpeed;
	/** 加速挑战用到参数 **/
	// 本次成绩战胜百分之多少车主
	private String winPercent;
	// 急速表现相当于
	private String speedLike;
	// 超高速里程 暂时没用到不知道哪里
	private String chDBHVS;
	// 分享需要的属性
	private String shareLink;
	private String shareTitle;
	private String shareText;
	private ArrayList<MedalInfo> mGotMedalList = new ArrayList<MedalInfo>();
	private ArrayList<RecordInfo> mGotRecordList = new ArrayList<RecordInfo>();

	public final static int STATUS_ING = 1;
	public final static int STATUS_INTERRUPT = 2;
	public final static int STATUS_SUCCESS = 3;
	public final static int STATUS_ERRO = 4;
	// 1 挑战中 2 挑战中断 3 挑战结束且成功 4 挑战结束但失败
	private int status;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public String getCredit() {
		return credit;
	}

	public void setCredit(String credit) {
		this.credit = credit;
	}

	public String getChDBBrake() {
		return chDBBrake;
	}

	public void setChDBBrake(String chDBBrake) {
		this.chDBBrake = chDBBrake;
	}

	public String getChDBAcce() {
		return chDBAcce;
	}

	public void setChDBAcce(String chDBAcce) {
		this.chDBAcce = chDBAcce;
	}

	public String getChDBTurn() {
		return chDBTurn;
	}

	public void setChDBTurn(String chDBTurn) {
		this.chDBTurn = chDBTurn;
	}

	public String getChDBHES() {
		return chDBHES;
	}

	public void setChDBHES(String chDBHES) {
		this.chDBHES = chDBHES;
	}

	public String getChDBHVS() {
		return chDBHVS;
	}

	public void setChDBHVS(String chDBHVS) {
		this.chDBHVS = chDBHVS;
	}

	public String getPointdesc() {
		return pointdesc;
	}

	public void setPointdesc(String pointdesc) {
		this.pointdesc = pointdesc;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getMaxSpeed() {
		return maxSpeed;
	}

	public void setMaxSpeed(String maxSpeed) {
		this.maxSpeed = maxSpeed;
	}

	public String getAvgSpeed() {
		return avgSpeed;
	}

	public void setAvgSpeed(String avgSpeed) {
		this.avgSpeed = avgSpeed;
	}

	public String getWinPercent() {
		return winPercent;
	}

	public void setWinPercent(String winPercent) {
		this.winPercent = winPercent;
	}

	public String getSpeedLike() {
		return speedLike;
	}

	public void setSpeedLike(String speedLike) {
		this.speedLike = speedLike;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getShareLink() {
		return shareLink;
	}

	public void setShareLink(String shareLink) {
		this.shareLink = shareLink;
	}

	public String getShareTitle() {
		return shareTitle;
	}

	public void setShareTitle(String shareTitle) {
		this.shareTitle = shareTitle;
	}

	public String getShareText() {
		return shareText;
	}

	public void setShareText(String shareText) {
		this.shareText = shareText;
	}

	public String getScoreunit() {
		return scoreunit;
	}

	public void setScoreunit(String scoreunit) {
		this.scoreunit = scoreunit;
	}

	public ArrayList<MedalInfo> getmGotMedalLis() {
		return mGotMedalList;
	}

	public void AddmGotMedalLis(MedalInfo mMedalInfo) {
		mGotMedalList.add(mMedalInfo);
	}

	public ArrayList<RecordInfo> getmGotRecordList() {
		return mGotRecordList;
	}

	public void AddmGotRecordList(RecordInfo mRecordInfo) {
		mGotRecordList.add(mRecordInfo);
	}

}

package com.carlt.yema.data.community;


import com.carlt.yema.data.career.MedalInfo;

import java.io.Serializable;
import java.util.ArrayList;

public class FriendDetialInfo implements Serializable {
	// 好友id
	private String id;

	// 是否为好友 1：是，其他：否
	private int isfriend;

	// 头像URL
	private String avatar_img;

	// 姓名
	private String realname;

	// 性别 1男2女3保密
	private String gender;

	// 积分
	private String credit;

	// 车辆名称
	private String carname;

	// 车标
	private String carlogo;

	// 总里程数
	private String summiles;

	// 总油耗
	private String sumfuel;

	/** 驾驶证情况 **/

	// 等级
	private String level;

	// 驾驶证图片
	private String totemactive;

	// 驾驶证名称
	private String name;

	// 驾驶证距离升级百分比
	private int licencePercent;

	// 驾驶证标签
	private String tag;


	// 车友勋章列表
	private ArrayList<MedalInfo> mList;

	// 分享需要的属性
	private String shareLink;

	private String shareTitle;

	private String shareText;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAvatar_img() {
		return avatar_img;
	}

	public void setAvatar_img(String avatar_img) {
		this.avatar_img = avatar_img;
	}

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

	public String getCredit() {
		return credit;
	}

	public void setCredit(String credit) {
		this.credit = credit;
	}

	public String getCarname() {
		return carname;
	}

	public void setCarname(String carname) {
		this.carname = carname;
	}

	public String getSummiles() {
		return summiles;
	}

	public void setSummiles(String summiles) {
		this.summiles = summiles;
	}

	public String getSumfuel() {
		return sumfuel;
	}

	public void setSumfuel(String sumfuel) {
		this.sumfuel = sumfuel;
	}



	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getTotemactive() {
		return totemactive;
	}

	public void setTotemactive(String totemactive) {
		this.totemactive = totemactive;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getCarlogo() {
		return carlogo;
	}

	public void setCarlogo(String carlogo) {
		this.carlogo = carlogo;
	}

	public int getIsfriend() {
		return isfriend;
	}

	public void setIsfriend(int isfriend) {
		this.isfriend = isfriend;
	}

	public ArrayList<MedalInfo> getmList() {
		return mList;
	}

	public void setmList(ArrayList<MedalInfo> mList) {
		this.mList = mList;
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

	public int getLicencePercent() {
		return licencePercent;
	}

	public void setLicencePercent(int licencePercent) {
		this.licencePercent = licencePercent;
	}

}

package com.carlt.sesame.data.career;

import java.io.Serializable;

public class UserLicenseInfo implements Serializable {

	/** 驾驶证信息 **/

	// 编号（显示用）
	public String licencenumber;

	// 发证日期
	public String licencedate;

	// 等级名称
	public String licenceName;

	// 驾驶证等级图标
	private String licenceImg;

	// 驾驶证等级
	private String licenceLevel;

	// 驾驶证距离升级百分比
	private int licencePercent;

	// 驾驶证描述
	public String licenceTag;

	// 积分
	public String credit;
	// 行车总油耗
	private String sumfuel;

	private UserMediaInfo mUserMediaInfo;

	// 分享时标题
	private String ShareTitle;

	// 分享时正文
	private String ShareText;

	// 分享时链接
	private String ShareLink;

	public String getShareTitle() {
		return ShareTitle;
	}

	public void setShareTitle(String shareTitle) {
		ShareTitle = shareTitle;
	}

	public String getShareText() {
		return ShareText;
	}

	public void setShareText(String shareText) {
		ShareText = shareText;
	}

	public String getShareLink() {
		return ShareLink;
	}

	public void setShareLink(String shareLink) {
		ShareLink = shareLink;
	}

	public UserMediaInfo getmUserMediaInfo() {
		return mUserMediaInfo;
	}

	public void setmUserMediaInfo(UserMediaInfo mUserMediaInfo) {
		this.mUserMediaInfo = mUserMediaInfo;
	}

	public String getLicencenumber() {
		return licencenumber;
	}

	public void setLicencenumber(String licencenumber) {
		this.licencenumber = licencenumber;
	}

	public String getLicencedate() {
		return licencedate;
	}

	public void setLicencedate(String licencedate) {
		this.licencedate = licencedate;
	}

	public String getLicenceName() {
		return licenceName;
	}

	public void setLicenceName(String licenceName) {
		this.licenceName = licenceName;
	}

	public String getLicenceImg() {
		return licenceImg;
	}

	public void setLicenceImg(String licenceImg) {
		this.licenceImg = licenceImg;
	}

	public String getLicenceTag() {
		return licenceTag;
	}

	public void setLicenceTag(String licenceTag) {
		this.licenceTag = licenceTag;
	}

	public String getCredit() {
		return credit;
	}

	public void setCredit(String credit) {
		this.credit = credit;
	}

	public String getLicenceLevel() {
		return licenceLevel;
	}

	public void setLicenceLevel(String licenceLevel) {
		this.licenceLevel = licenceLevel;
	}

	public int getLicencePercent() {
		return licencePercent;
	}

	public void setLicencePercent(int licencePercent) {
		this.licencePercent = licencePercent;
	}

	public String getSumfuel() {
		return sumfuel;
	}

	public void setSumfuel(String sumfuel) {
		this.sumfuel = sumfuel;
	}

}

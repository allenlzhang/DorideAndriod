package com.carlt.doride.data.community;


import com.carlt.doride.data.career.ChallengeInfo;

public class ShareItemInfo {
	public final static int TYPE_LICENCE = 1;
	public final static int TYPE_MEDAL = 2;
	public final static int TYPE_RECORD = 3;
	public final static int TYPE_CHALLENGE = 4;
	public final static int TYPE_SHARE = 5;
	public final static int TYPE_LICENCE_SHARE = 6;
	// 注：为应对将来可能增加新种类的可能性，现有版本开发时，对非1-5的类型应不予显示，以避免可能出现的格式不正确程序异常问题
	// 1 车友升级驾驶证
	// 2 车友解锁勋章
	// 3 车友创造新记录
	// 4 车友完成挑战项
	// 5 车友各种晒出来的信息
	private int type;
	// 内容
	private String content = "";
	// 发表时间
	private String feedtime = "";
	// 点赞次数
	private String poketimes = "";
	// 当前用户是否顶过
	private boolean ispoked;
	// 头像URL
	private String iconUrl;

	/** 驾驶证(1)，勋章(2)需用到的信息 **/
	// 图片URL
	private String imgUrl = "";
	// 图片描述
	private String imgDesc = "";

	/** 车友创造新记录(3)需用到的信息 **/
	// 记录名称
	private String recordName = "";
	// 破记录前旧值
	private String recordOldvalue = "";
	// 新记录值
	private String recordNewvalue = "";
	// 单位
	private String recordUnit = "";

	/** 车友完成挑战项(4)需用到的信息 **/
	private ChallengeInfo mChallengeInfo;

	private String id = "";

	/** 车友晒(5)需用到的信息 **/
	private String link;

	private String shareText;
	// 驾驶证ID
	private String licenceLevelId = "";
	// 勋章ID
	private String medalid = "";

	private MyFriendInfo mMyFriendInfo;
	//点赞文字信息
	private String pokedUserStr;

	public String getPokedUserStr() {
		return pokedUserStr;
	}

	public void setPokedUserStr(String pokedUserStr) {
		this.pokedUserStr = pokedUserStr;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getFeedtime() {
		return feedtime;
	}

	public void setFeedtime(String feedtime) {
		this.feedtime = feedtime;
	}

	public String getPoketimes() {
		return poketimes;
	}

	public void setPoketimes(String poketimes) {
		this.poketimes = poketimes;
	}

	public boolean isIspoked() {
		return ispoked;
	}

	public void setIspoked(boolean ispoked) {
		this.ispoked = ispoked;
	}

	public String getShareText() {
		return shareText;
	}

	public void setShareText(String shareText) {
		this.shareText = shareText;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getImgDesc() {
		return imgDesc;
	}

	public void setImgDesc(String imgDesc) {
		this.imgDesc = imgDesc;
	}

	public String getRecordName() {
		return recordName;
	}

	public void setRecordName(String recordName) {
		this.recordName = recordName;
	}

	public String getRecordOldvalue() {
		return recordOldvalue;
	}

	public void setRecordOldvalue(String recordOldvalue) {
		this.recordOldvalue = recordOldvalue;
	}

	public String getRecordNewvalue() {
		return recordNewvalue;
	}

	public void setRecordNewvalue(String recordNewvalue) {
		this.recordNewvalue = recordNewvalue;
	}

	public ChallengeInfo getmChallengeInfo() {
		return mChallengeInfo;
	}

	public void setmChallengeInfo(ChallengeInfo mChallengeInfo) {
		this.mChallengeInfo = mChallengeInfo;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLicenceLevelId() {
		return licenceLevelId;
	}

	public void setLicenceLevelId(String licenceLevelId) {
		this.licenceLevelId = licenceLevelId;
	}

	public String getMedalid() {
		return medalid;
	}

	public void setMedalid(String medalid) {
		this.medalid = medalid;
	}

	public MyFriendInfo getmMyFriendInfo() {
		return mMyFriendInfo;
	}

	public void setmMyFriendInfo(MyFriendInfo mMyFriendInfo) {
		this.mMyFriendInfo = mMyFriendInfo;
	}

	public String getRecordUnit() {
		return recordUnit;
	}

	public void setRecordUnit(String recordUnit) {
		this.recordUnit = recordUnit;
	}

}

package com.carlt.doride.data.community;

import java.util.ArrayList;

public class FriendPKInfo {

	private ArrayList<PKItemInfo> mPKItemInfoList;

	private String shareTitle;
	private String shareText;
	private String shareLink;
	// 哪边胜出
	private int stute;

	public int getStute() {
		return stute;
	}

	public void setStute(int stute) {
		this.stute = stute;
	}

	public ArrayList<PKItemInfo> getmPKItemInfoList() {
		return mPKItemInfoList;
	}

	public void setmPKItemInfoList(ArrayList<PKItemInfo> mPKItemInfoList) {
		this.mPKItemInfoList = mPKItemInfoList;
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

	public String getShareLink() {
		return shareLink;
	}

	public void setShareLink(String shareLink) {
		this.shareLink = shareLink;
	}

}

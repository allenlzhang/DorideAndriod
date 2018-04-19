package com.carlt.doride.data.community;

import java.util.ArrayList;

public class FriendFeedInfo {

	private ArrayList<ShareItemInfo> mShareItemInfoList = new ArrayList<ShareItemInfo>();

	private int friendCount;

	// 消息ID
	private String feedid;

	// 消息内容
	private String noticeMessage;

	private int limit;

	private int offset;

	public ArrayList<ShareItemInfo> getmShareItemInfoList() {
		return mShareItemInfoList;
	}

	public void AddmShareItemInfoList(ShareItemInfo mShareItemInfo) {
		mShareItemInfoList.add(mShareItemInfo);
	}

	public void AddmShareItemInfoList(ArrayList<ShareItemInfo> list) {
		mShareItemInfoList.addAll(list);
	}

	public int getFriendCount() {
		return friendCount;
	}

	public void setFriendCount(int friendCount) {
		this.friendCount = friendCount;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public String getFeedid() {
		return feedid;
	}

	public void setFeedid(String feedid) {
		this.feedid = feedid;
	}

	public String getNoticeMessage() {
		return noticeMessage;
	}

	public void setNoticeMessage(String noticeMessage) {
		this.noticeMessage = noticeMessage;
	}

}

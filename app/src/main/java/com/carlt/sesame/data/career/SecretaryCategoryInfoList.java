package com.carlt.sesame.data.career;

import java.util.ArrayList;

public class SecretaryCategoryInfoList {

	private ArrayList<SecretaryCategoryInfo> mAllList = new ArrayList<SecretaryCategoryInfo>();

	// 未读消息个数
	private int unreadCount;

	public ArrayList<SecretaryCategoryInfo> getmAllList() {
		return mAllList;
	}

	public void addmAllList(SecretaryCategoryInfo mInfo) {
		this.mAllList.add(mInfo);
	}

	public void addmAllList(ArrayList<SecretaryCategoryInfo> list) {
		this.mAllList.addAll(list);
	}

	public int getUnreadCount() {
		return unreadCount;
	}

	public void setUnreadCount(int unreadCount) {
		this.unreadCount = unreadCount;
	}

}

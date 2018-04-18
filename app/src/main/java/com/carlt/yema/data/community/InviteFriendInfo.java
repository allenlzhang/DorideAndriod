package com.carlt.yema.data.community;

import java.util.ArrayList;

public class InviteFriendInfo {

	private String name;

	private ArrayList<String> phoneList;

	private boolean isFriend;

	// 只有当isFriend为True时才有此参数
	private String uid;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<String> getPhoneList() {
		return phoneList;
	}

	public void setPhoneList(ArrayList<String> phoneList) {
		this.phoneList = phoneList;
	}

	public boolean isFriend() {
		return isFriend;
	}

	public void setFriend(boolean isFriend) {
		this.isFriend = isFriend;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

}

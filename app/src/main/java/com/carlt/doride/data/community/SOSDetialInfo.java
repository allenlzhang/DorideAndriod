package com.carlt.doride.data.community;

import java.util.ArrayList;

public class SOSDetialInfo extends SOSInfo {
	// 回复内容
	private String store_reply;
	// 图片URL
	private ArrayList<String> mImgList = new ArrayList<String>();
	// 地址
	private String address;
	// 经纬度
	private String addr_point;

	public String getStore_reply() {
		return store_reply;
	}

	public void setStore_reply(String store_reply) {
		this.store_reply = store_reply;
	}

	public ArrayList<String> getmImgList() {
		return mImgList;
	}

	public void addmImgList(String mImg) {
		mImgList.add(mImg);
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAddr_point() {
		return addr_point;
	}

	public void setAddr_point(String addr_point) {
		this.addr_point = addr_point;
	}

}

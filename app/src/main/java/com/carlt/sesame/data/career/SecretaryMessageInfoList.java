package com.carlt.sesame.data.career;

import java.util.ArrayList;

public class SecretaryMessageInfoList {
	private ArrayList<SecretaryMessageInfo> mAllList = new ArrayList<SecretaryMessageInfo>();

	private int offset;

	public ArrayList<SecretaryMessageInfo> getmAllList() {
		return mAllList;
	}

	public void addmAllList(SecretaryMessageInfo mInfo) {
		this.mAllList.add(mInfo);
	}

	public void addmAllList(ArrayList<SecretaryMessageInfo> list) {
		this.mAllList.addAll(list);
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

}

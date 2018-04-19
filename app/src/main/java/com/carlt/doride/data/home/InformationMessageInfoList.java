package com.carlt.doride.data.home;

import java.util.ArrayList;

public class InformationMessageInfoList {
	private ArrayList<InformationMessageInfo> mAllList = new ArrayList<InformationMessageInfo>();

	private int offset;

	public ArrayList<InformationMessageInfo> getmAllList() {
		return mAllList;
	}

	public void addmAllList(InformationMessageInfo mInfo) {
		this.mAllList.add(mInfo);
	}

	public void addmAllList(ArrayList<InformationMessageInfo> list) {
		this.mAllList.addAll(list);
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

}

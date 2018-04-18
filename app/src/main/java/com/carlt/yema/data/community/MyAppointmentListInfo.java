package com.carlt.yema.data.community;

import java.util.ArrayList;

public class MyAppointmentListInfo {

	private int limit;

	private int offset;

	private ArrayList<AppointmentInfo> mAppointmentInfoList = new ArrayList<AppointmentInfo>();

	public ArrayList<AppointmentInfo> getmAppointmentInfoList() {
		return mAppointmentInfoList;
	}

	public void addmAppointmentInfoList(AppointmentInfo mInfo) {
		mAppointmentInfoList.add(mInfo);
	}

	public void addmAppointmentInfoList(ArrayList<AppointmentInfo> mList) {
		mAppointmentInfoList.addAll(mList);
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
}

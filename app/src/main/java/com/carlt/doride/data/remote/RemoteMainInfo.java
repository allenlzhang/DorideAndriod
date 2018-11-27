package com.carlt.doride.data.remote;


import com.carlt.doride.data.BaseResponseInfo;

import java.util.ArrayList;

/**
 * 远程首页info
 * 
 * @author Administrator
 * 
 */
public class RemoteMainInfo extends BaseResponseInfo {
	int status;// 车辆状态

	public final static int STATUS_CHARGE = 1;// 上电
	public final static int STATUS_START = 2;// 启动
	public final static int STATUS_STOP = 3;// 熄火
	public final static int STATUS_SLEEP = 4;// 休眠
	public final static int STATUS_OUTAGE = 5;// 断电

	// 设备真实顺序 0后装，1前装，2后装2016款，3大迈X7
	private static String deviceCategory;

	private String functionCount;// 远程支持项总数
	public int hasTachograph;// 远程支持项总数

	private RemoteFunInfo mFunInfoStart;// 启动功能
	private RemoteFunInfo mFunInfoStop;// 熄火功能

	private ArrayList<RemoteFunInfo> mRemoteFunInfos = new ArrayList<RemoteFunInfo>();// 远程功能列表
	
	private AirMainInfo mAirMainInfo;//空调对话框info

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getDeviceCategory() {
		return deviceCategory;
	}

	public void setDeviceCategory(String deviceCategory) {
		this.deviceCategory = deviceCategory;
	}

	public String getFunctionCount() {
		return functionCount;
	}

	public void setFunctionCount(String functionCount) {
		this.functionCount = functionCount;
	}

	public RemoteFunInfo getmFunInfoStart() {
		return mFunInfoStart;
	}

	public void setmFunInfoStart(RemoteFunInfo mFunInfoStart) {
		this.mFunInfoStart = mFunInfoStart;
	}

	public RemoteFunInfo getmFunInfoStop() {
		return mFunInfoStop;
	}

	public void setmFunInfoStop(RemoteFunInfo mFunInfoStop) {
		this.mFunInfoStop = mFunInfoStop;
	}

	public ArrayList<RemoteFunInfo> getmRemoteFunInfos() {
		return mRemoteFunInfos;
	}

	public void addmRemoteFunInfos(RemoteFunInfo mRemoteFunInfo) {
		this.mRemoteFunInfos.add(mRemoteFunInfo);
	}

	public AirMainInfo getmAirMainInfo() {
		return mAirMainInfo;
	}

	public void setmAirMainInfo(AirMainInfo mAirMainInfo) {
		this.mAirMainInfo = mAirMainInfo;
	}

	//导航同步
	private String navigationSync;
	//直式胎压检测
	private String directPSTsupervise;

	public String getNavigationSync() {
		return navigationSync;
	}

	public void setNavigationSync(String navigationSync) {
		this.navigationSync = navigationSync;
	}

	public String getDirectPSTsupervise() {
		return directPSTsupervise;
	}

	public void setDirectPSTsupervise(String directPSTsupervise) {
		this.directPSTsupervise = directPSTsupervise;
	}

	@Override
	public String toString() {
		return "RemoteMainInfo{" +
				"status=" + status +
				", functionCount='" + functionCount + '\'' +
				", mFunInfoStart=" + mFunInfoStart +
				", mFunInfoStop=" + mFunInfoStop +
				", mRemoteFunInfos=" + mRemoteFunInfos +
				", mAirMainInfo=" + mAirMainInfo +
				", navigationSync='" + navigationSync + '\'' +
				", directPSTsupervise='" + directPSTsupervise + '\'' +
				'}';
	}
}

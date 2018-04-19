package com.carlt.doride.data.remote;

import com.carlt.doride.data.BaseResponseInfo;

import java.util.ArrayList;

/**
 * 空调对话框info
 * 
 * @author Administrator
 *
 */
public class AirMainInfo extends BaseResponseInfo {

	private String functionCount;// 空调支持项总数
	private String state;// 空调当前状态
	private boolean isShowTemp;//是否展示温度
	private String currentTemp;//车辆当前温度
	private boolean isGetCurrentTempSuccess;//是否成功获取当前温度

	private ArrayList<RemoteFunInfo> mRemoteFunInfos = new ArrayList<RemoteFunInfo>();// 空调功能列表

	public String getFunctionCount() {
		return functionCount;
	}

	public void setFunctionCount(String functionCount) {
		this.functionCount = functionCount;
	}

	public ArrayList<RemoteFunInfo> getmRemoteFunInfos() {
		return mRemoteFunInfos;
	}

	public void addmRemoteFunInfos(RemoteFunInfo mRemoteFunInfo) {
		this.mRemoteFunInfos.add(mRemoteFunInfo);
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public boolean isShowTemp() {
		return isShowTemp;
	}

	public void setShowTemp(boolean isShowTemp) {
		this.isShowTemp = isShowTemp;
	}

	public String getCurrentTemp() {
		return currentTemp;
	}

	public void setCurrentTemp(String currentTemp) {
		this.currentTemp = currentTemp;
	}

	public boolean isGetCurrentTempSuccess() {
		return isGetCurrentTempSuccess;
	}

	public void setGetCurrentTempSuccess(boolean isGetCurrentTempSuccess) {
		this.isGetCurrentTempSuccess = isGetCurrentTempSuccess;
	}

}

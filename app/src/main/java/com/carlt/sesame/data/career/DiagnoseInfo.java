package com.carlt.sesame.data.career;

import com.carlt.sesame.data.car.CheckFaultInfo;

import java.util.ArrayList;

public class DiagnoseInfo {
	// 错误信息列表
	private ArrayList<CheckFaultInfo> mCheckFaultInfoList;
	// 推荐顾问
	private RecommendSalesInfo mRecommendSalesInfo;

	public ArrayList<CheckFaultInfo> getmCheckFaultInfoList() {
		return mCheckFaultInfoList;
	}

	public void setmCheckFaultInfoList(
			ArrayList<CheckFaultInfo> mCheckFaultInfoList) {
		this.mCheckFaultInfoList = mCheckFaultInfoList;
	}

	public RecommendSalesInfo getmRecommendSalesInfo() {
		return mRecommendSalesInfo;
	}

	public void setmRecommendSalesInfo(RecommendSalesInfo mRecommendSalesInfo) {
		this.mRecommendSalesInfo = mRecommendSalesInfo;
	}

}

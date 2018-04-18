package com.carlt.yema.data.car;


import com.carlt.yema.data.BaseResponseInfo;
import com.carlt.yema.data.remote.RemoteFunInfo;

import java.util.ArrayList;

/**
 * 座驾主页支持功能info
 * 
 * @author Administrator
 * 
 */
public class CarMainFunInfo extends BaseResponseInfo {

	private ArrayList<RemoteFunInfo> mCarmainFunInfos = new ArrayList<RemoteFunInfo>();// 座驾首页功能列表

	public ArrayList<RemoteFunInfo> getmCarmainFunInfos() {
		return mCarmainFunInfos;
	}

	public void addmCarmainFunInfos(RemoteFunInfo mRemoteFunInfo) {
		this.mCarmainFunInfos.add(mRemoteFunInfo);
	}
}

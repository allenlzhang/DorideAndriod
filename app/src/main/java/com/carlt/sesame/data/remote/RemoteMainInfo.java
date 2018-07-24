package com.carlt.sesame.data.remote;

import com.carlt.sesame.data.BaseResponseInfo;

import java.util.ArrayList;

/**
 * 远程首页info
 * @author Administrator
 *
 */
public class RemoteMainInfo extends BaseResponseInfo{
    int status;//车辆状态
    
    public final static  int STATUS_CHARGE=1;//上电
    public final static  int STATUS_START=2;//启动
    public final static  int STATUS_STOP=3;//熄火
    public final static  int STATUS_SLEEP=4;//休眠
    public final static  int STATUS_OUTAGE=5;//断电
    
    private String functionCount;// 远程支持项总数
    
    boolean isDeviceBefore;//是否是前装设备
    
    private ArrayList<RemoteFunInfo> mRemoteFunInfos = new ArrayList<RemoteFunInfo>();// 远程功能列表
    
    private AirMainInfo mAirMainInfo;//空调对话框info
    
    
    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }
    
    public String getFunctionCount() {
		return functionCount;
	}
	public void setFunctionCount(String functionCount) {
		this.functionCount = functionCount;
	}
	public boolean isDeviceBefore() {
        return isDeviceBefore;
    }
    public void setDeviceBefore(boolean isDeviceBefore) {
        this.isDeviceBefore = isDeviceBefore;
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
}

package com.carlt.sesame.data.remote;

import com.carlt.sesame.data.BaseResponseInfo;

public class ChargeStatusInfo extends BaseResponseInfo{

	private int status;//充电状态
	private String chargeTimeDes;//定时时间描述
	private int chargeTime;//定时时间
	
	public final static int STATU_UNCHARGED=0;//非定时充电状态
	public final static int STATU_CHARGING=1;//处于定时充电状态
	
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getChargeTimeDes() {
		return chargeTimeDes;
	}
	public void setChargeTimeDes(String chargeTimeDes) {
		this.chargeTimeDes = chargeTimeDes;
	}
	public int getChargeTime() {
		return chargeTime;
	}
	public void setChargeTime(int chargeTime) {
		this.chargeTime = chargeTime;
	}
}

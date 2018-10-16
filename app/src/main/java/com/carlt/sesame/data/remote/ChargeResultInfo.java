package com.carlt.sesame.data.remote;

import com.carlt.sesame.data.BaseResponseInfo;

/**
 * 定时充电信息
 * @author Daisy
 *
 */
public class ChargeResultInfo extends BaseResponseInfo{
	private String chargeTimeDes;//定时时间描述

	public String getChargeTimeDes() {
		return chargeTimeDes;
	}

	public void setChargeTimeDes(String chargeTimeDes) {
		this.chargeTimeDes = chargeTimeDes;
	}
	
}

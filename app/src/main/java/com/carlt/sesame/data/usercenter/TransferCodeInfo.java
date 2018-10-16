package com.carlt.sesame.data.usercenter;

import com.carlt.sesame.data.BaseResponseInfo;

public class TransferCodeInfo extends BaseResponseInfo {

	private static final long serialVersionUID = 1L;

	private String qrCode;

	public String getQrCode() {
		return qrCode;
	}

	public void setQrCode(String qrCode) {
		this.qrCode = qrCode;
	}

}

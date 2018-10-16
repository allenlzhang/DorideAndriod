package com.carlt.sesame.protocolstack.usercenter;

import com.carlt.sesame.data.usercenter.TransferCodeInfo;
import com.carlt.sesame.protocolstack.BaseParser;

import org.json.JSONObject;

/**
 * 检验VPIN码解析
 * @author Administrator
 *
 */
public class TransferQrCodeParser extends BaseParser {
	private TransferCodeInfo mTransferCode ;

	public TransferCodeInfo getReturn() {
		return mTransferCode;
	}

	@Override
	protected void parser() {
		JSONObject mJSON_data = mJson.optJSONObject("data");
		String mCode = mJSON_data.optString("outtingid");
		mTransferCode = new TransferCodeInfo();
		mTransferCode.setQrCode(mCode);
	}


}

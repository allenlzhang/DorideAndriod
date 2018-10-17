package com.carlt.chelepie.protocolstack.recorder;


import com.carlt.chelepie.data.recorder.UpgradeInfo;
import com.carlt.doride.protocolparser.BaseParser;
import com.google.gson.JsonObject;


/**
 * 获取固件升级Url
 * 
 * @author Administrator
 *
 */
public class UpdateFileParser extends BaseParser {
	UpgradeInfo mUpInfo;

	public UpdateFileParser(ResultCallback callback) {
		super(callback);
	}

	public UpdateFileParser(ResultCallback callback, Class clazz) {
		super(callback, clazz);
	}

	public UpgradeInfo getReturn() {
		return mUpInfo;
	}

	@Override
	protected void parser() throws Exception {
		mUpInfo = new UpgradeInfo();
		JsonObject obj = mJson.getAsJsonObject("data");
		String is_upgrade = obj.get("is_upgrade").getAsString();
		mUpInfo.isUpgrade = "1".equals(is_upgrade);//1 是需要升级
		if (mUpInfo.isUpgrade) {
			mUpInfo.url = obj.get("fileUrl").getAsString();
			mUpInfo.size = obj.get("fileSize").getAsInt();
			mUpInfo.fileDes = obj.get("fileDes").getAsString();
		}
	}

}

package com.carlt.sesame.protocolstack;

import com.carlt.sesame.data.AdvertiseInfo;

import org.json.JSONObject;

/**
 * 广告信息解析
 * @author Daisy
 *
 */
public class AdvertiseInfoParser extends BaseParser {
	private AdvertiseInfo mAdvertiseInfo = new AdvertiseInfo();

	public AdvertiseInfo getReturn() {
		return mAdvertiseInfo;
	}

	@Override
	protected void parser() {
		JSONObject mJSON_data = mJson.optJSONObject("data");
		
		mAdvertiseInfo.setId(mJSON_data.optString("id"));
		mAdvertiseInfo.setFilePath(mJSON_data.optString("filePath"));
        
	}
}

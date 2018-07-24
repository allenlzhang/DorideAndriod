package com.carlt.sesame.protocolstack.set;

import com.carlt.sesame.data.UploadImgInfo;
import com.carlt.sesame.protocolstack.BaseParser;

import org.json.JSONException;
import org.json.JSONObject;

public class UploadImgParser extends BaseParser {

	private UploadImgInfo mUploadImgInfo = new UploadImgInfo();

	public UploadImgInfo getReturn() {
		return mUploadImgInfo;
	}

	@Override
	protected void parser() {
		try {
			JSONObject mJSON_data = mJson.getJSONObject("data");
			mUploadImgInfo.setId(mJSON_data.optString("id"));
			mUploadImgInfo.setFilePath(mJSON_data.optString("filePath"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}

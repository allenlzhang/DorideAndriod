package com.carlt.sesame.protocolstack.community;

import com.carlt.sesame.data.community.SOSDetialInfo;
import com.carlt.sesame.protocolstack.BaseParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MySOSDetialListParser extends BaseParser {
	private SOSDetialInfo mSOSDetialInfo = new SOSDetialInfo();

	public SOSDetialInfo getReturn() {
		return mSOSDetialInfo;
	}

	@Override
	protected void parser() {
		try {
			JSONObject mJSON_data = mJson.getJSONObject("data");
			mSOSDetialInfo.setId(mJSON_data.optString("id"));
			mSOSDetialInfo.setCreate_time(mJSON_data.optString("create_time"));
			mSOSDetialInfo.setImagesCount(mJSON_data.optInt("imagesCount"));
			String info = mJSON_data.optString("info");
			if (info == null || info.equals("null")) {
				info = "";
			}
			mSOSDetialInfo.setInfo(info);
			mSOSDetialInfo.setNeed_sos(mJSON_data.optInt("need_sos"));
			mSOSDetialInfo.setState(mJSON_data.optInt("state"));
			String store_reply = mJSON_data.optString("store_reply");
			if (store_reply == null || store_reply.equals("null")) {
				store_reply = "";
			}
			mSOSDetialInfo.setStore_reply(store_reply);
			mSOSDetialInfo.setAddress(mJSON_data.optString("addr_detail"));
			mSOSDetialInfo.setAddr_point(mJSON_data.optString("addr_point"));
			JSONArray mJSON_images = mJSON_data.optJSONArray("images");
			for (int i = 0; i < mJSON_images.length(); i++) {
				String url = (String) mJSON_images.get(i);
				mSOSDetialInfo.addmImgList(url);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

}

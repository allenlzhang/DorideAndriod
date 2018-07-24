package com.carlt.sesame.protocolstack.community;

import com.carlt.sesame.data.community.MySOSListInfo;
import com.carlt.sesame.data.community.SOSInfo;
import com.carlt.sesame.protocolstack.BaseParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MySOSListParser extends BaseParser {

	private MySOSListInfo mMySOSListInfo = new MySOSListInfo();

	public MySOSListInfo getReturn() {
		return mMySOSListInfo;
	}

	@Override
	protected void parser() {
		try {

			JSONArray mJSON_data = mJson.getJSONArray("data");
			for (int i = 0; i < mJSON_data.length(); i++) {

				JSONObject temp = (JSONObject) mJSON_data.get(i);
				SOSInfo mSOSInfo = new SOSInfo();
				mSOSInfo.setId(temp.optString("id"));
				mSOSInfo.setCreate_time(temp.optString("create_time"));
				mSOSInfo.setImagesCount(temp.optInt("imagesCount"));
				mSOSInfo.setInfo(temp.optString("info"));
				mSOSInfo.setNeed_sos(temp.optInt("need_sos"));
				mSOSInfo.setState(temp.optInt("state"));
				mMySOSListInfo.addmSOSInfoList(mSOSInfo);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}


}

package com.carlt.sesame.protocolstack.community;

import com.carlt.sesame.data.community.InvitePrizeInfo;
import com.carlt.sesame.protocolstack.BaseParser;

import org.json.JSONArray;
import org.json.JSONObject;

public class InvitePrizeParser extends BaseParser {
	InvitePrizeInfo mInvitePrizeInfo = new InvitePrizeInfo();

	public InvitePrizeInfo getReturn() {
		return mInvitePrizeInfo;
	}

	@Override
	protected void parser() {
		JSONArray mJSON_data = mJson.optJSONArray("data");
		if (mJSON_data.length() > 0) {
			for (int i = 0; i < mJSON_data.length(); i++) {
				JSONObject temp = (JSONObject) mJSON_data.opt(i);
				String name = temp.optString("name");
				String img = temp.optString("img");
				mInvitePrizeInfo.AddmInvitePrizeList(name, img);
			}
		}
	}

}

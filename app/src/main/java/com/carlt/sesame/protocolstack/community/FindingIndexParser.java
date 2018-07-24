package com.carlt.sesame.protocolstack.community;

import com.carlt.sesame.data.community.FindingIndexInfo;
import com.carlt.sesame.protocolstack.BaseParser;

import org.json.JSONException;
import org.json.JSONObject;

public class FindingIndexParser extends BaseParser {

	private FindingIndexInfo mFindingIndexInfo = new FindingIndexInfo();

	public FindingIndexInfo getReturn() {
		return mFindingIndexInfo;
	}

	@Override
	protected void parser() {
		try {
			JSONObject mJSON_data = mJson.getJSONObject("data");
			int hasNewMessage = mJSON_data.optInt("hasNewMessage");
			mFindingIndexInfo.setHasNewMessage(hasNewMessage);

			String latestMessage = mJSON_data.optString("latestMessage");
			mFindingIndexInfo.setLatestMessage(latestMessage);

			int friends = mJSON_data.optInt("friends");
			mFindingIndexInfo.setFriends(friends);

			int hasNewPrize = mJSON_data.optInt("hasNewPrize");
			mFindingIndexInfo.setHasNewPrize(hasNewPrize);

			String avatarImg=mJSON_data.optString("avatarImg");
			mFindingIndexInfo.setAvatarImg(avatarImg);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}

package com.carlt.sesame.protocolstack.community;

import com.carlt.sesame.control.DaoControl;
import com.carlt.sesame.data.community.FriendFeedDetialInfo;
import com.carlt.sesame.protocolstack.BaseParser;

import org.json.JSONException;
import org.json.JSONObject;

public class FeedDetialInfoParser extends BaseParser {

	private FriendFeedDetialInfo mFriendFeedDetialInfo = new FriendFeedDetialInfo();

	public FriendFeedDetialInfo getReturn() {

		return mFriendFeedDetialInfo;
	}

	@Override
	protected void parser() {
		try {

			JSONObject mJSON_data = mJson.getJSONObject("data");
			DaoControl mDaoControl = DaoControl.getInstance();
			mFriendFeedDetialInfo = GenerateShareItemInfo(mDaoControl,
					mJSON_data);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}

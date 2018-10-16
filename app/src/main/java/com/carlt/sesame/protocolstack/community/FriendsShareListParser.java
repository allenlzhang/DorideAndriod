package com.carlt.sesame.protocolstack.community;

import com.carlt.sesame.control.DaoControl;
import com.carlt.sesame.data.community.FriendFeedInfo;
import com.carlt.sesame.protocolstack.BaseParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FriendsShareListParser extends BaseParser {

	private FriendFeedInfo mFriendFeedInfo = new FriendFeedInfo();

	public FriendFeedInfo getReturn() {
		return mFriendFeedInfo;
	}

	@Override
	protected void parser() {
		try {

			JSONObject mJSON_data = mJson.getJSONObject("data");
			mFriendFeedInfo.setFriendCount(mJSON_data.optInt("friends"));
			JSONObject mJSON_notice = mJSON_data.optJSONObject("notice");
			if (mJSON_notice != null) {
				mFriendFeedInfo.setFeedid(mJSON_notice.optString("feedid"));
				mFriendFeedInfo.setNoticeMessage(mJSON_notice
						.optString("noticeMessage"));
			}
			
			JSONArray list = mJSON_data.optJSONArray("list");
			DaoControl mDaoControl = DaoControl.getInstance();
			for (int i = 0; i < list.length(); i++) {

				JSONObject temp = (JSONObject) list.get(i);

				mFriendFeedInfo.AddmShareItemInfoList(GenerateShareItemInfo(
						mDaoControl, temp));
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}

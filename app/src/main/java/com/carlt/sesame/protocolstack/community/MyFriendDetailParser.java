package com.carlt.sesame.protocolstack.community;

import com.carlt.sesame.control.CPControl;
import com.carlt.sesame.control.DaoControl;
import com.carlt.sesame.data.career.GotMediaInfo;
import com.carlt.sesame.data.career.LicenceLevelInfo;
import com.carlt.sesame.data.career.MedalInfo;
import com.carlt.sesame.data.community.FriendDetialInfo;
import com.carlt.sesame.protocolstack.BaseParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MyFriendDetailParser extends BaseParser {

	public MyFriendDetailParser() {
		// TODO Auto-generated constructor stub
	}

	private FriendDetialInfo mFriendDetialInfo = new FriendDetialInfo();
	private ArrayList<GotMediaInfo> mRuleList = new ArrayList<GotMediaInfo>();

	public FriendDetialInfo getReturn() {
		return mFriendDetialInfo;
	}

	@Override
	protected void parser() {
		try {
			JSONObject mJSON_data = mJson.getJSONObject("data");
			JSONObject mJSON_member = mJSON_data.getJSONObject("member");
			mFriendDetialInfo.setAvatar_img(mJSON_member
					.optString("avatar_img"));
			mFriendDetialInfo.setRealname(mJSON_member.optString("realname"));
			mFriendDetialInfo.setGender(mJSON_member.optString("gender"));
			mFriendDetialInfo.setId(mJSON_member.optString("id"));
			
			JSONObject mJSON_membercar = mJSON_data.getJSONObject("membercar");
			mFriendDetialInfo.setCarname(mJSON_membercar.optString("carname"));
			mFriendDetialInfo.setCarlogo(mJSON_membercar.optString("carlogo"));
			mFriendDetialInfo.setTag(mJSON_membercar.optString("tag"));
			mFriendDetialInfo.setCredit(mJSON_membercar.optString("credit"));
			mFriendDetialInfo.setSummiles(mJSON_membercar.optString("summiles"));
			mFriendDetialInfo.setSumfuel(mJSON_membercar.optString("sumfuel"));

			mFriendDetialInfo.setLicencePercent(mJSON_membercar.optInt("levelPercent"));
			String licenceId = 	mJSON_membercar.optString("licencelevelid");
			DaoControl mDaoControl=DaoControl.getInstance();
			LicenceLevelInfo mLicenceLevelInfo = mDaoControl
					.getLicenceLevelById(licenceId);
			mFriendDetialInfo.setTotemactive(mLicenceLevelInfo.getIconUrl2());
			mFriendDetialInfo.setLevel(mLicenceLevelInfo.getLevel() + "");
			mFriendDetialInfo.setName(mLicenceLevelInfo.getName());
			mFriendDetialInfo.setIsfriend(mJSON_data.optInt("isfriend"));

			JSONObject mJSON_medal = mJSON_data.getJSONObject("medal");
			JSONArray media_list = mJSON_medal.optJSONArray("list");
			// 获取用户已获得勋章的ID

			for (int i = 0; i < media_list.length(); i++) {
				JSONObject media_info = (JSONObject) media_list.opt(i);
				GotMediaInfo mGotMediaInfo = new GotMediaInfo();
				mGotMediaInfo.setMedalid(media_info.optString("medalid"));
				mGotMediaInfo.setUnlocktime(media_info.optString("unlocktime"));
				mRuleList.add(mGotMediaInfo);
			}
			ArrayList<MedalInfo> mList = CPControl.MedalSort(mRuleList);
			mFriendDetialInfo.setmList(mList);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}

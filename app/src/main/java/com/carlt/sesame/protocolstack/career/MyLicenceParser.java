package com.carlt.sesame.protocolstack.career;

import com.carlt.doride.http.retrofitnet.model.UserInfo;
import com.carlt.sesame.control.CPControl;
import com.carlt.sesame.control.DaoControl;
import com.carlt.sesame.data.SesameLoginInfo;
import com.carlt.sesame.data.career.GotMediaInfo;
import com.carlt.sesame.data.career.LicenceLevelInfo;
import com.carlt.sesame.data.career.UserLicenseInfo;
import com.carlt.sesame.data.career.UserMediaInfo;
import com.carlt.sesame.protocolstack.BaseParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MyLicenceParser extends BaseParser {

	private UserLicenseInfo mLicenseInfo = new UserLicenseInfo();

	private ArrayList<GotMediaInfo> mGotMediaInfoList = new ArrayList<GotMediaInfo>();

	public UserLicenseInfo getReturn() {
		return mLicenseInfo;
	}

	@Override
	protected void parser() throws JSONException {
		DaoControl mDaoControl = DaoControl.getInstance();
		JSONObject data = mJson.optJSONObject("data");

		JSONObject mJSON_member = data.optJSONObject("member");
		UserInfo.getInstance().realName  = mJSON_member.optString("realname");
		UserInfo.getInstance().gender = mJSON_member.optInt("gender");

		JSONObject membercar = data.optJSONObject("membercar");
		mLicenseInfo.setCredit(membercar.optString("credit"));
		mLicenseInfo.setLicencenumber(membercar.optString("licencenumber"));
		mLicenseInfo.setLicencedate(membercar.optString("licencedate"));
		String licenceId = membercar.optString("licencelevelid");
		LicenceLevelInfo mLicenceLevelInfo = mDaoControl
				.getLicenceLevelById(licenceId);
		mLicenseInfo.setLicenceImg(mLicenceLevelInfo.getIconUrl2());
		mLicenseInfo.setLicenceLevel(mLicenceLevelInfo.getLevel() + "");
		mLicenseInfo.setLicenceName(mLicenceLevelInfo.getName());
		mLicenseInfo.setLicencePercent(membercar.optInt("levelPercent"));
		mLicenseInfo.setSumfuel(membercar.optString("sumfuel"));
		mLicenseInfo.setLicenceTag(membercar.optString("tag"));

		JSONObject medal = data.optJSONObject("medal");
		UserMediaInfo mUserMediaInfo = new UserMediaInfo();
		JSONArray media_list = medal.optJSONArray("list");
		for (int i = 0; i < media_list.length(); i++) {
			JSONObject media_info = (JSONObject) media_list.opt(i);
			GotMediaInfo mGotMediaInfo = new GotMediaInfo();
			mGotMediaInfo.setMedalid(media_info.optString("medalid"));
			mGotMediaInfo.setUnlocktime(media_info.optString("unlocktime"));
			mGotMediaInfoList.add(mGotMediaInfo);
		}
		mUserMediaInfo.setmList(CPControl.MedalSort(mGotMediaInfoList));
		mUserMediaInfo.setMediaShareTitle(medal.optString("sharetitle"));
		mUserMediaInfo.setMediaShareText(medal.optString("sharetext"));
		mUserMediaInfo.setMediaShareLink(medal.optString("sharelink"));
		mLicenseInfo.setmUserMediaInfo(mUserMediaInfo);

//		mLicenseInfo.setShareTitle(medal.optString("sharetitle"));
//        mLicenseInfo.setShareText(medal.optString("sharetext"));
//        mLicenseInfo.setShareLink(medal.optString("sharelink"));
		
         mLicenseInfo.setShareTitle(data.optString("sharetitle"));
         mLicenseInfo.setShareText(data.optString("sharetext"));
         mLicenseInfo.setShareLink(data.optString("sharelink"));

	}
}


package com.carlt.sesame.protocolstack;

import com.carlt.sesame.control.DaoControl;
import com.carlt.sesame.data.career.ChallengeInfo;
import com.carlt.sesame.data.career.LicenceLevelInfo;
import com.carlt.sesame.data.career.MedalInfo;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * @author user 解析注册信息
 */
public class UserOtherInfoParser extends BaseParser {

    public UserOtherInfoParser() {
        // TODO Auto-generated constructor stub
    }

    @Override
	protected void parser() {
		JSONObject mJSON_data = mJson.optJSONObject("data");
		
		if(mJSON_data!=null){
		// 勋章入库
		DaoControl mDaoControl = DaoControl.getInstance();
		JSONArray medal = mJSON_data.optJSONArray("medal");
		for (int i = 0; i < medal.length(); i++) {
			MedalInfo mMedalInfo = new MedalInfo();
			JSONObject temp = (JSONObject) medal.opt(i);
			mMedalInfo.setId(temp.optString("id"));
			mMedalInfo.setName(temp.optString("name"));
			mMedalInfo.setIconUrl1(temp.optString("icon"));
			mMedalInfo.setIconUrl2(temp.optString("iconactive"));
			mMedalInfo.setDescription(temp.optString("description"));
			mMedalInfo.setLevel(temp.optInt("level"));
			mDaoControl.insertMedal(mMedalInfo);
		}
		// 挑战入库
		JSONArray challenge = mJSON_data.optJSONArray("challenge");
		for (int i = 0; i < challenge.length(); i++) {
			ChallengeInfo mChallengeInfo = new ChallengeInfo();
			JSONObject temp = (JSONObject) challenge.opt(i);
			mChallengeInfo.setId(temp.optString("id"));
			mChallengeInfo.setType(temp.optInt("type"));
			mChallengeInfo.setFuel(temp.optString("fuel"));
			mChallengeInfo.setMiles(temp.optString("miles"));
			mChallengeInfo.setTime(temp.optString("time"));
			mChallengeInfo.setSpeed(temp.optString("speed"));
			mChallengeInfo.setPoint(temp.optString("point"));
			mChallengeInfo.setSort(temp.optInt("sort"));
			mChallengeInfo.setName(temp.optString("name"));
			mChallengeInfo.setStatus(temp.optString("status"));
			mChallengeInfo.setInfo(temp.optString("info"));
			mDaoControl.insertChallenge(mChallengeInfo);
		}

		// 驾驶证等级信息入库
		JSONArray licence = mJSON_data.optJSONArray("licence");
		for (int i = 0; i < licence.length(); i++) {
			LicenceLevelInfo mLicenceLevelInfo = new LicenceLevelInfo();
			JSONObject temp = (JSONObject) licence.opt(i);
			mLicenceLevelInfo.setId(temp.optString("id"));
			mLicenceLevelInfo.setName(temp.optString("name"));
			mLicenceLevelInfo.setPoint(temp.optInt("point"));
			mLicenceLevelInfo.setIconUrl1(temp.optString("totem"));
			mLicenceLevelInfo.setIconUrl2(temp.optString("totemactive"));
			mLicenceLevelInfo.setLevel(temp.optInt("level"));
			mDaoControl.insertLicenceLevel(mLicenceLevelInfo);
		}
		}
	}
}

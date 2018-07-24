package com.carlt.sesame.protocolstack.career;

import com.carlt.sesame.control.DaoControl;
import com.carlt.sesame.data.career.ChallengeScore;
import com.carlt.sesame.data.career.MedalInfo;
import com.carlt.sesame.data.career.RecordInfo;
import com.carlt.sesame.protocolstack.BaseParser;
import com.carlt.sesame.utility.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ChallengeScoreParser extends BaseParser {

	private ChallengeScore mChallengeScore = new ChallengeScore();

	public ChallengeScore getReturn() {
		return mChallengeScore;
	}

	@Override
	protected void parser() {
		try {
			JSONObject mJSON_data = mJson.getJSONObject("data");
			mChallengeScore.setName(mJSON_data.optString("name"));
			mChallengeScore.setScore(mJSON_data.optString("score"));
			mChallengeScore.setScoreunit(mJSON_data.optString("scoreunit"));
			mChallengeScore.setChDBBrake(mJSON_data.optString("chDBBrake"));
			mChallengeScore.setChDBAcce(mJSON_data.optString("chDBAcce"));
			mChallengeScore.setChDBTurn(mJSON_data.optString("chDBTurn"));
			mChallengeScore.setChDBHES(mJSON_data.optString("chDBHES"));
			mChallengeScore.setChDBHVS(mJSON_data.optString("chDBHVS"));
			mChallengeScore.setTime(mJSON_data.optString("time"));
			mChallengeScore.setMaxSpeed(mJSON_data.optString("maxSpeed"));
			mChallengeScore.setAvgSpeed(mJSON_data.optString("avgSpeed"));
			mChallengeScore.setWinPercent(mJSON_data.optString("winPercent"));
			mChallengeScore.setAvgSpeed(mJSON_data.optString("avgSpeed"));
			mChallengeScore.setAvgSpeed(mJSON_data.optString("avgSpeed"));
			mChallengeScore.setPointdesc(mJSON_data.optString("pointdesc"));
			mChallengeScore.setShareText(mJSON_data.optString("sharetext"));
			mChallengeScore.setShareTitle(mJSON_data.optString("sharetitle"));
			mChallengeScore.setShareLink(mJSON_data.optString("sharelink"));
			mChallengeScore.setStatus(mJSON_data.optInt("status"));
			mChallengeScore.setSpeedLike(mJSON_data.optString("speedlike"));
			mChallengeScore.setCredit(mJSON_data.optString("credit"));

			DaoControl mDaoControl = DaoControl.getInstance();
			// 获得勋章
			JSONArray medal_list = mJSON_data.optJSONArray("medal");
			if (medal_list != null) {
				for (int i = 0; i < medal_list.length(); i++) {
					String id = medal_list.get(i) + "";
					MedalInfo mMedalInfo = mDaoControl.getMedalById(id);
					if (mMedalInfo != null) {
						mMedalInfo.setIsgot(true);
						mChallengeScore.AddmGotMedalLis(mMedalInfo);
					}
				}
			}

			// 获得新纪录
			JSONArray record_list = mJSON_data.optJSONArray("record");
			if (record_list != null) {
				for (int i = 0; i < record_list.length(); i++) {
					JSONObject record = record_list.optJSONObject(i);
					RecordInfo mRecordInfo = new RecordInfo();
					mRecordInfo.setRecordname(record.optString("recordname"));
					mRecordInfo.setOldvalue(record.optString("oldvalue"));
					mRecordInfo.setNewvalue(record.optString("newvalue"));
					mRecordInfo.setUnit(record.optString("unit"));
					mChallengeScore.AddmGotRecordList(mRecordInfo);
				}
			}
			Log.e("info", "");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}

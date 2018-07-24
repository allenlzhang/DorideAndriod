package com.carlt.sesame.protocolstack.career;

import com.carlt.sesame.control.DaoControl;
import com.carlt.sesame.data.career.ChallengeInfo;
import com.carlt.sesame.protocolstack.BaseParser;
import com.carlt.sesame.utility.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 挑战模式列表解析
 * 
 * @author Administrator
 */
public class ChallengeListParser extends BaseParser {

	private ArrayList<ChallengeInfo> mChallengeList = new ArrayList<ChallengeInfo>();

	public ArrayList<ChallengeInfo> getReturn() {
		return mChallengeList;
	}

	@Override
	protected void parser() {
		try {
			DaoControl mDaoControl = DaoControl.getInstance();
			ArrayList<ChallengeInfo> mChallengeListLocal = mDaoControl.getChallengeList();
			JSONArray challenge_list = mJson.optJSONArray("data");
			int size = 0;
			if (null != mChallengeListLocal) {
				size = mChallengeListLocal.size();
			}
			int length = challenge_list.length();
			for (int j = 0; j < length; j++) {
				JSONObject challenge = (JSONObject) challenge_list.get(j);

				String challengeId = challenge.optString("id");
				String status = challenge.optString("status");

				for (int i = 0; i < size; i++) {
					ChallengeInfo mInfo = mChallengeListLocal.get(i);
					if (mInfo.getId().equals(challengeId)) {
						mInfo.setStatus(status);
						mDaoControl.updataChallenge(mInfo);
						break;
					}

				}
			}
			mChallengeList = mDaoControl.getChallengeList();
			Log.e("info", "");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}

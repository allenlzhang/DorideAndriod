package com.carlt.sesame.protocolstack.career;

import com.carlt.sesame.data.career.SecretaryMessageInfo;
import com.carlt.sesame.protocolstack.BaseParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SafetyMessageListParser extends BaseParser {

	private ArrayList<SecretaryMessageInfo> mSecretaryMessageInfoList = new ArrayList<SecretaryMessageInfo>();

	public ArrayList<SecretaryMessageInfo> getReturn() {
		return mSecretaryMessageInfoList;
	}

	@Override
	protected void parser() {
		try {
			JSONArray mJSON_data = mJson.getJSONArray("data");
			for (int i = 0; i < mJSON_data.length(); i++) {
				SecretaryMessageInfo mInfo = new SecretaryMessageInfo();
				JSONObject temp = (JSONObject) mJSON_data.get(i);
				mInfo.setId(temp.optString("id"));
				mInfo.setUserid(temp.optString("userid"));
				mInfo.setDealerid(temp.optString("dealerid"));
				mInfo.setIsoffical(temp.optInt("isoffical"));
				mInfo.setTitle(temp.optString("title"));
				mInfo.setRelid(temp.optString("relid"));
				String content = temp.optString("content");
				mInfo.setContent(content);
				mInfo.setDetial_flag(SecretaryMessageInfo.FLAG_NONE);
				int class1 = temp.optInt("class1");
				mInfo.setClass1(class1);
				mInfo.setClass2(temp.optInt("class2"));
				mInfo.setCreatedate(temp.optString("createdate"));
				mSecretaryMessageInfoList.add(mInfo);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}

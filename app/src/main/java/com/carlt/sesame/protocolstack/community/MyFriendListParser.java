package com.carlt.sesame.protocolstack.community;

import com.carlt.sesame.data.community.MyFriendInfo;
import com.carlt.sesame.protocolstack.BaseParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MyFriendListParser extends BaseParser {
	private ArrayList<MyFriendInfo> mList = new ArrayList<MyFriendInfo>();

	public ArrayList<MyFriendInfo> getReturn() {
		return mList;
	}

	@Override
	protected void parser() {
		try {

			JSONArray mJSON_data = mJson.getJSONArray("data");
			for (int i = 0; i < mJSON_data.length(); i++) {
				MyFriendInfo info = new MyFriendInfo();
				JSONObject temp = (JSONObject) mJSON_data.get(i);
				info.setId(temp.optString("userid"));
				info.setUsername(temp.optString("username"));
				info.setRealname(temp.optString("realname"));
				info.setGender(temp.optInt("gender"));
				info.setImg(temp.optString("avatar_img"));

				info.setLicencename(temp.optString("licencename"));
				info.setCarlogo(temp.optString("carlogo"));

				mList.add(info);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

}

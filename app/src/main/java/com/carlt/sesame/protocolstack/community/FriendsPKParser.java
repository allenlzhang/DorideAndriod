package com.carlt.sesame.protocolstack.community;

import com.carlt.sesame.data.community.FriendPKInfo;
import com.carlt.sesame.data.community.PKItemInfo;
import com.carlt.sesame.protocolstack.BaseParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FriendsPKParser extends BaseParser {

	private FriendPKInfo mFriendPKInfo = new FriendPKInfo();

	public FriendPKInfo getReturn() {
		return mFriendPKInfo;
	}

	private String[] title = { "单日最高驾驶得分", "最高时速", "总里程", "总油耗", "百公里油耗",
			"平均时速", "单日最高里程", "单日最高油耗" };

	private String[] code = { "maxpoint", "maxspeed", "summiles", "sumfuel",
			"avgfuel", "avgspeed", "maxmiles", "maxfuel" };
	private String[] unit = { "分", "公里/小时", "公里", "升", "升/百公里", "公里/小时", "公里",
			"升" };

	@Override
	protected void parser() {
		try {
			ArrayList<PKItemInfo> mList = new ArrayList<PKItemInfo>();
			int r = 0;
			JSONObject mJSON_data = mJson.getJSONObject("data");
			for (int i = 0; i < code.length; i++) {
				PKItemInfo mFriendsPKInfo = new PKItemInfo();
				mFriendsPKInfo.setTitle(title[i]);
				double left = mJSON_data.optJSONObject("left").optDouble(
						code[i]);
				double right = mJSON_data.optJSONObject("right").optDouble(
						code[i]);
				if (left < 0) {
					left = 0;
				}
				if (right < 0) {
					right = 0;
				}
				if (i == 4) {
					// 小的获胜的情况，百公里油耗
					if (right > left) {

						mFriendsPKInfo.setStatus(1);
					} else if (right < left) {
						r++;
						mFriendsPKInfo.setStatus(2);
					} else {
						mFriendsPKInfo.setStatus(3);
					}
				} else {
					if (left > right) {
						mFriendsPKInfo.setStatus(1);
					} else if (left < right) {
						r++;
						mFriendsPKInfo.setStatus(2);
					} else {
						mFriendsPKInfo.setStatus(3);
					}
				}
				if (i == 0 || i == 1 || i == 2 || i == 6) {
					mFriendsPKInfo.setLeft(String.format("%.0f", left));
					mFriendsPKInfo.setRight(String.format("%.0f", right));
				} else {
					mFriendsPKInfo.setLeft(String.format("%.1f", left));
					mFriendsPKInfo.setRight(String.format("%.1f", right));
				}

				mFriendsPKInfo.setUnit(unit[i]);
				mList.add(mFriendsPKInfo);
			}
			mFriendPKInfo.setmPKItemInfoList(mList);
			if (r >= code.length / 2) {
				mFriendPKInfo.setStute(2);
			} else {
				mFriendPKInfo.setStute(1);
			}

			mFriendPKInfo.setShareText(mJSON_data.optString("sharetext"));
			mFriendPKInfo.setShareTitle(mJSON_data.optString("sharetitle"));
			mFriendPKInfo.setShareLink(mJSON_data.optString("sharelink"));

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}

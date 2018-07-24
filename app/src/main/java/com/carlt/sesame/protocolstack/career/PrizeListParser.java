package com.carlt.sesame.protocolstack.career;

import com.carlt.sesame.data.career.PrizeInfo;
import com.carlt.sesame.protocolstack.BaseParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

public class PrizeListParser extends BaseParser {

	private ArrayList<PrizeInfo> mPrizeInfoList = new ArrayList<PrizeInfo>();

	public ArrayList<PrizeInfo> getReturn() {
		return mPrizeInfoList;
	}

	@Override
	protected void parser() {
		try {
			JSONArray mJSON_data = mJson.getJSONArray("data");
			Date now = new Date(System.currentTimeMillis());
			long currentTimeMillis = now.getTime();
			long ONEDAY = 1000 * 60 * 60 * 24;
			for (int i = 0; i < mJSON_data.length(); i++) {
				PrizeInfo mPrizeInfo = new PrizeInfo();
				JSONObject temp = (JSONObject) mJSON_data.get(i);
				mPrizeInfo.setId(temp.optString("id"));
				mPrizeInfo.setName(temp.optString("name"));
				mPrizeInfo.setCode(temp.optString("prize_code"));
				String expiredate = temp.optString("expiredate");
				String[] e = expiredate.split("-");
				if (e != null && e.length == 3) {
					Date Instance = new Date();
					Instance.setYear(Integer.parseInt(e[0]) - 1900);
					Instance.setMonth(Integer.parseInt(e[1]) - 1);
					Instance.setDate(Integer.parseInt(e[2]));
					long t = Instance.getTime();
					long offset = t - currentTimeMillis;
					if (offset < 0) {
						expiredate = "已过期";
					}
				}

				mPrizeInfo.setExpiredate(expiredate);
				mPrizeInfo.setCreatetime(temp.optString("createtime"));
				mPrizeInfo.setDescription(temp.optString("description"));
				mPrizeInfo.setIconUrl(temp.optString("url"));
				mPrizeInfo.setShareTitle(temp.optString("sharetitle"));
				mPrizeInfo.setShareText(temp.optString("sharetext"));
				mPrizeInfo.setShareLink(temp.optString("sharelink"));

				mPrizeInfoList.add(mPrizeInfo);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}

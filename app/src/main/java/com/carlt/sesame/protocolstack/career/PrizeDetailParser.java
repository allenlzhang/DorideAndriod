package com.carlt.sesame.protocolstack.career;

import com.carlt.sesame.data.career.PrizeInfo;
import com.carlt.sesame.protocolstack.BaseParser;

import org.json.JSONException;
import org.json.JSONObject;

public class PrizeDetailParser extends BaseParser {

	private PrizeInfo mPrizeInfo = new PrizeInfo();

	public PrizeInfo getReturn() {
		return mPrizeInfo;
	}

	@Override
	protected void parser() {
		try {

			JSONObject mJSON_data = mJson.getJSONObject("data");
			mPrizeInfo.setId(mJSON_data.optString("id"));
			mPrizeInfo.setName(mJSON_data.optString("name"));
			mPrizeInfo.setIconUrl(mJSON_data.optString("url"));
			mPrizeInfo.setDescription(mJSON_data.optString("description"));
			mPrizeInfo.setExpiredate(mJSON_data.optString("expiredate"));
			mPrizeInfo.setCreatetime(mJSON_data.optString("createtime"));
			mPrizeInfo.setDealer_name(mJSON_data.optString("dealer_name"));
			mPrizeInfo.setDealer_info(mJSON_data.optString("dealer_info"));
			mPrizeInfo.setDealer_tel(mJSON_data.optString("dealer_tel"));
			mPrizeInfo.setCode(mJSON_data.optString("prize_code"));
			mPrizeInfo
					.setUsedescription(mJSON_data.optString("usedescription"));
			mPrizeInfo.setShareText(mJSON_data.optString("sharetext"));
			mPrizeInfo.setShareTitle(mJSON_data.optString("sharetitle"));
			mPrizeInfo.setShareLink(mJSON_data.optString("sharelink"));

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}

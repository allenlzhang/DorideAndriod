package com.carlt.sesame.protocolstack.career;

import com.carlt.sesame.data.career.RecommendSalesInfo;
import com.carlt.sesame.protocolstack.BaseParser;
import com.carlt.sesame.utility.Log;

import org.json.JSONObject;

public class RecommendSalesParser extends BaseParser {

	private RecommendSalesInfo mRecommendSalesInfo = new RecommendSalesInfo();

	public RecommendSalesInfo getReturn() {
		return mRecommendSalesInfo;
	}

	@Override
	protected void parser() {
		Log.e("info", "aaa");
		JSONObject mJSON_data = mJson.optJSONObject("data");
		mRecommendSalesInfo.setId(mJSON_data.optString("id"));
		mRecommendSalesInfo.setName(mJSON_data.optString("name"));
		mRecommendSalesInfo.setMobile(mJSON_data.optString("mobile"));
		mRecommendSalesInfo.setImgUrl(mJSON_data.optString("img"));
		mRecommendSalesInfo.setPosition(mJSON_data.optString("position"));
		mRecommendSalesInfo.setCompany(mJSON_data.optString("company"));

	}
}

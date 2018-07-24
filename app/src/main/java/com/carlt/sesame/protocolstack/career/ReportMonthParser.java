package com.carlt.sesame.protocolstack.career;

import com.carlt.sesame.data.career.ReportMonthInfo;
import com.carlt.sesame.protocolstack.BaseParser;

import org.json.JSONObject;

public class ReportMonthParser extends BaseParser {

	private ReportMonthInfo mReportMonthInfo = new ReportMonthInfo();

	public ReportMonthInfo getReturn() {
		return mReportMonthInfo;
	}

	@Override
	protected void parser() {

		JSONObject mJSON_data = mJson.optJSONObject("data");
		JSONObject cartype = mJSON_data.optJSONObject("cartype");
		if (cartype != null) {
			mReportMonthInfo.setTypebrake(cartype.optInt("brake"));
			mReportMonthInfo.setTypeturn(cartype.optInt("turn"));
			mReportMonthInfo.setTypespeedup(cartype.optInt("speedup"));
			mReportMonthInfo.setTypeoverSpeed(cartype.optInt("overspeed"));
			double avgfuel = cartype.optDouble("avgfuel");
			mReportMonthInfo.setTypeAvgFuel(String.format("%.1f", avgfuel));
		}
		JSONObject all = mJSON_data.optJSONObject("all");
		if (all != null) {

			double avgfuel = all.optDouble("avgfuel");
			mReportMonthInfo.setAllAvgFuel(String.format("%.1f", avgfuel));
		}
		JSONObject user = mJSON_data.optJSONObject("user");
		if (user != null) {
			mReportMonthInfo.setId(user.optString("id"));
			mReportMonthInfo.setUserId(user.optString("userid"));
			mReportMonthInfo.setAvgPoint(user.optString("avgpoint"));
			mReportMonthInfo.setAvgPointcompare(user.optInt("avgpointcompare"));
			mReportMonthInfo.setBrake(user.optInt("brake"));
			mReportMonthInfo.setTurn(user.optInt("turn"));
			mReportMonthInfo.setSpeedup(user.optInt("speedup"));
			mReportMonthInfo.setOverSpeed(user.optInt("overspeed"));

			double avgfuel = user.optDouble("avgfuel");
			mReportMonthInfo.setAvgFuel(String.format("%.1f", avgfuel));
			mReportMonthInfo.setSumtime_hour(user.optString("sumtime_hour"));
			mReportMonthInfo.setSumtime_min(user.optString("sumtime_min"));
			mReportMonthInfo.setSumtime_str(user.optString("sumtime_str"));
			mReportMonthInfo.setSumMiles(user.optString("summiles"));

			mReportMonthInfo.setSumFuel(user.optString("sumfuel"));

			mReportMonthInfo.setMaxSpeed(user.optInt("maxspeed") + "");

			double avgspeed = user.optDouble("avgspeed");
			mReportMonthInfo.setAvgSpeed(String.format("%.1f", avgspeed));
			mReportMonthInfo.setTag(user.optString("tag"));
			mReportMonthInfo.setReportDate(user.optString("reportdate"));
			mReportMonthInfo.setCreateTime(user.optInt("createtime"));
			mReportMonthInfo.setBrakedesc(user.optString("brakedesc"));
			mReportMonthInfo.setTurndesc(user.optString("turndesc"));
			mReportMonthInfo.setSpeedupdesc(user.optString("speedupdesc"));
			mReportMonthInfo.setOverspeeddesc(user.optString("overspeeddesc"));

			mReportMonthInfo.setSumtimedesc(user.optString("sumtimedesc"));
			mReportMonthInfo.setSummilesdesc(user.optString("summilesdesc"));
			mReportMonthInfo.setSumfueldesc(user.optString("sumfueldesc"));
			mReportMonthInfo.setAvgspeeddesc(user.optString("avgspeeddesc"));
			mReportMonthInfo.setMaxspeeddesc(user.optString("maxspeeddesc"));
			mReportMonthInfo.setShareLink(user.optString("sharelink"));
			mReportMonthInfo.setShareText(user.optString("sharetext"));
			mReportMonthInfo.setShareTitle(user.optString("sharetitle"));

		}
	}
}

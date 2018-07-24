package com.carlt.sesame.protocolstack.career;

import com.carlt.sesame.data.career.ReportDayInfo;
import com.carlt.sesame.protocolstack.BaseParser;

import org.json.JSONObject;

public class ReportDayParser extends BaseParser {

	private ReportDayInfo mReportDayInfo = new ReportDayInfo();

	public ReportDayInfo getReturn() {
		return mReportDayInfo;
	}

	@Override
	protected void parser() {

		JSONObject mJSON_data = mJson.optJSONObject("data");
		JSONObject cartype = mJSON_data.optJSONObject("cartype");
		if (cartype != null) {
			mReportDayInfo.setTypebrake(cartype.optInt("brake"));
			mReportDayInfo.setTypeturn(cartype.optInt("turn"));
			mReportDayInfo.setTypespeedup(cartype.optInt("speedup"));
			mReportDayInfo.setTypeoverSpeed(cartype.optInt("overspeed"));
			double avgfuel = cartype.optDouble("avgfuel");
			mReportDayInfo.setTypeAvgFuel(String.format("%.1f", avgfuel));
		}
		JSONObject all = mJSON_data.optJSONObject("all");
		if (all != null) {

			double avgfuel = all.optDouble("avgfuel");
			mReportDayInfo.setAllAvgFuel(String.format("%.1f", avgfuel));

		}

		JSONObject user = mJSON_data.optJSONObject("user");
		if (user != null) {
			mReportDayInfo.setId(user.optString("id"));
			mReportDayInfo.setUserId(user.optString("userid"));
			mReportDayInfo.setPointdesc(user.optString("pointdesc"));
			mReportDayInfo.setPoint(user.optString("avgpoint"));
			mReportDayInfo.setRank(user.optInt("rank"));
			mReportDayInfo.setSumtime_hour(user.optString("sumtime_hour"));
			mReportDayInfo.setSumtime_min(user.optString("sumtime_min"));
			mReportDayInfo.setSumtime_str(user.optString("sumtime_str"));
			mReportDayInfo.setSumTime(user.optString("sumtime"));

			mReportDayInfo.setMaxSpeed(user.optInt("maxspeed") + "");

			double avgspeed = user.optDouble("avgspeed");

			mReportDayInfo.setAvgSpeed(String.format("%.1f", avgspeed));

			int sumfuel = user.optInt("sumfuel");
			String s1 = "";
			if (sumfuel < 100) {
				s1 = sumfuel + "Wh";
			} else {
				double f = sumfuel / 1000.0;

				s1 = String.format("%.1f", f) + "kWh";
			}
			
			mReportDayInfo.setSumFuel(s1);
			mReportDayInfo.setSumMiles(user.optString("summiles"));
			mReportDayInfo.setBrake(user.optInt("brake"));
			mReportDayInfo.setTurn(user.optInt("turn"));
			mReportDayInfo.setSpeedup(user.optInt("speedup"));
			mReportDayInfo.setOverSpeed(user.optInt("overspeed"));
			mReportDayInfo.setReportDate(user.optString("reportdate"));
			mReportDayInfo.setCreateTime(user.optInt("createtime"));
			int isweekbest = user.optInt("isweekbest", 0);
			int ismonthbest = user.optInt("ismonthbest", 0);
			int isbest = user.optInt("isbest", 0);
			if (isweekbest == 1) {
				mReportDayInfo.setBest("本周最佳");
			}
			if (ismonthbest == 1) {
				mReportDayInfo.setBest("本月最佳");
			}
			if (isbest == 1) {
				mReportDayInfo.setBest("历史最佳");
			}
			mReportDayInfo.setRuntimes(user.optInt("runtimes"));
			mReportDayInfo.setTag(user.optString("tag"));
			double avgfuel = user.optDouble("avgfuel");
			mReportDayInfo.setAvgFuel(String.format("%.1f", avgfuel));
			mReportDayInfo.setBrakedesc(user.optString("brakedesc"));
			mReportDayInfo.setTurndesc(user.optString("turndesc"));
			mReportDayInfo.setSpeedupdesc(user.optString("speedupdesc"));
			mReportDayInfo.setOverspeeddesc(user.optString("overspeeddesc"));

			mReportDayInfo.setShareLink(user.optString("sharelink"));
			mReportDayInfo.setShareText(user.optString("sharetext"));
			mReportDayInfo.setShareTitle(user.optString("sharetitle"));

		}
	}

}

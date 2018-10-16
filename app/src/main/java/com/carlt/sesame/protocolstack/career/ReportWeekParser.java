package com.carlt.sesame.protocolstack.career;

import com.carlt.sesame.data.career.ReportWeekChatInfo;
import com.carlt.sesame.data.career.ReportWeekInfo;
import com.carlt.sesame.protocolstack.BaseParser;
import com.carlt.sesame.utility.MyParse;

import org.json.JSONArray;
import org.json.JSONObject;

public class ReportWeekParser extends BaseParser {

	private ReportWeekInfo mReportWeekInfo = new ReportWeekInfo();

	public ReportWeekInfo getReturn() {
		return mReportWeekInfo;
	}

	@Override
	protected void parser() {

		JSONObject mJSON_data = mJson.optJSONObject("data");

		JSONObject cartype = mJSON_data.optJSONObject("cartype");
		if (cartype != null) {

			mReportWeekInfo.setTypebrake(cartype.optInt("brake"));
			mReportWeekInfo.setTypeturn(cartype.optInt("turn"));
			mReportWeekInfo.setTypespeedup(cartype.optInt("speedup"));
			mReportWeekInfo.setTypeoverSpeed(cartype.optInt("overspeed"));
			double avgfuel = cartype.optDouble("avgfuel");
			mReportWeekInfo.setTypeAvgFuel(String.format("%.1f", avgfuel));
		}
		JSONObject all = mJSON_data.optJSONObject("all");
		if (all != null) {
			double avgfuel = all.optDouble("avgfuel");
			mReportWeekInfo.setAllAvgFuel(String.format("%.1f", avgfuel));
		}

		JSONObject user = mJSON_data.optJSONObject("user");
		if (user != null) {
			mReportWeekInfo.setId(user.optString("id"));
			mReportWeekInfo.setUserId(user.optString("userid"));
			mReportWeekInfo.setAvgpoint(user.optString("avgpoint"));
			mReportWeekInfo.setPointdesc(user.optString("pointdesc"));
			mReportWeekInfo.setAvgpointcompare(user.optInt("avgpointcompare"));
			mReportWeekInfo.setBrake(user.optInt("brake"));
			mReportWeekInfo.setTurn(user.optInt("turn"));
			mReportWeekInfo.setSpeedup(user.optInt("speedup"));
			mReportWeekInfo.setOverSpeed(user.optInt("overspeed"));

			double avgfuel = user.optDouble("avgfuel");
			mReportWeekInfo.setAvgFuel(String.format("%.1f", avgfuel));
			mReportWeekInfo.setSumtime_hour(user.optString("sumtime_hour"));
			mReportWeekInfo.setSumtime_min(user.optString("sumtime_min"));
			mReportWeekInfo.setSumtime_str(user.optString("sumtime_str"));

			mReportWeekInfo.setMaxSpeed(user.optInt("maxspeed") + "");

			mReportWeekInfo.setSumMiles(user.optString("summiles"));

			int sumfuel = user.optInt("sumfuel");
			String s1 = "";
			if (sumfuel < 100) {
				s1 = sumfuel + "ML";
			} else {
				double f = sumfuel / 1000.0;

				s1 = String.format("%.2f", f) + "L";
			}
			mReportWeekInfo.setSumFuel(s1);
			mReportWeekInfo.setTag(user.optString("tag"));

			double avgspeed = user.optDouble("avgspeed");
			mReportWeekInfo.setAvgSpeed(String.format("%.1f", avgspeed));
			mReportWeekInfo.setReportDate(user.optString("reportdate"));
			mReportWeekInfo.setCreateTime(user.optInt("createtime"));
			mReportWeekInfo.setBrakedesc(user.optString("brakedesc"));
			mReportWeekInfo.setTurndesc(user.optString("turndesc"));
			mReportWeekInfo.setSpeedupdesc(user.optString("speedupdesc"));
			mReportWeekInfo.setOverspeeddesc(user.optString("overspeeddesc"));
			mReportWeekInfo.setShareLink(user.optString("sharelink"));
			mReportWeekInfo.setShareText(user.optString("sharetext"));
			mReportWeekInfo.setShareTitle(user.optString("sharetitle"));

			JSONArray pointList = user.optJSONArray("pointList");
			if (pointList != null) {
				for (int i = 0; i < pointList.length(); i++) {
					ReportWeekChatInfo mReportWeekChatInfo = new ReportWeekChatInfo();
					JSONObject pointItem = (JSONObject) pointList.opt(i);

					int ap = pointItem.optInt("avgpoint");
					String rd = pointItem.optString("reportdate");
					mReportWeekChatInfo.setPoint(ap);
					mReportWeekChatInfo.setColor(MyParse.getColorByPoint(ap));
					mReportWeekChatInfo.setDate(rd.substring(5));
					mReportWeekInfo.AddmChatList(mReportWeekChatInfo);
				}
			}

		}
	}
}

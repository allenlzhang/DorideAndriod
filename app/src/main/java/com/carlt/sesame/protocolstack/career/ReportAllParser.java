package com.carlt.sesame.protocolstack.career;

import com.carlt.sesame.data.career.ReportAllInfo;
import com.carlt.sesame.protocolstack.BaseParser;
import com.carlt.sesame.utility.MyParse;

import org.json.JSONException;
import org.json.JSONObject;

public class ReportAllParser extends BaseParser {

	private ReportAllInfo mReportAllInfo = new ReportAllInfo();

	public ReportAllInfo getReturn() {
		return mReportAllInfo;
	}

	@Override
	protected void parser() {
		try {
			JSONObject mJSON_data = mJson.getJSONObject("data");
			JSONObject mJSON_user = mJSON_data.getJSONObject("user");
			mReportAllInfo.setId(mJSON_user.optString("id"));
			mReportAllInfo.setUserId(mJSON_user.optString("userid"));
			String sumtime = mJSON_user.optString("sumtime");
			int st = MyParse.parseInt(sumtime);
			if (st < 0) {
				st = 0;
			} else if (st > 999999) {
				st = 999999;
			}
			mReportAllInfo.setSumTime(st + "");
			mReportAllInfo.setAvgPoint(mJSON_user.optString("avgpoint"));
			mReportAllInfo.setRank(mJSON_user.optString("rank"));
			mReportAllInfo.setSumFuel(mJSON_user.optString("sumfuel"));
			mReportAllInfo.setSumMiles(mJSON_user.optString("summiles"));
			mReportAllInfo.setAvgSpeed(mJSON_user.optString("avgspeed"));
			mReportAllInfo.setAvgFuel(mJSON_user.optString("avgfuel"));
			mReportAllInfo.setMaxSpeed(mJSON_user.optString("maxspeed"));
			mReportAllInfo
					.setMaxSpeedtime(mJSON_user.optString("maxspeedtime"));
			mReportAllInfo.setMinFuel(mJSON_user.optString("minfuel"));
			mReportAllInfo.setMinFueltime(mJSON_user.optString("minfueltime"));
			mReportAllInfo.setMaxPoint(mJSON_user.optString("maxpoint"));
			mReportAllInfo
					.setMaxPointtime(mJSON_user.optString("maxpointtime"));

			double maxavgspeed = mJSON_user.optDouble("maxavgspeed");
			mReportAllInfo.setMaxAvgspeed(String.format("%.1f", maxavgspeed));
			mReportAllInfo.setMaxavgspeedtime(mJSON_user
					.optString("maxavgspeedtime"));

			mReportAllInfo.setMaxSumtime(mJSON_user.optString("maxsumtime"));
			mReportAllInfo.setMaxsumtimetime(mJSON_user
					.optString("maxsumtimetime"));

			mReportAllInfo.setMaxMiles(mJSON_user.optString("maxmiles"));
			mReportAllInfo
					.setMaxMilestime(mJSON_user.optString("maxmilestime"));
			mReportAllInfo.setMaxFuel(mJSON_user.optString("maxfuel"));
			mReportAllInfo.setMaxFueltime(mJSON_user.optString("maxfueltime"));

			mReportAllInfo.setPointdesc(mJSON_user.optString("pointdesc"));
			mReportAllInfo.setSumfueldesc(mJSON_user.optString("sumfueldesc"));
			mReportAllInfo
					.setSummilesdesc(mJSON_user.optString("summilesdesc"));
			mReportAllInfo
					.setAvgspeeddesc(mJSON_user.optString("avgspeeddesc"));
			mReportAllInfo.setAvgfueldesc(mJSON_user.optString("avgfueldesc"));
			mReportAllInfo.setIsshared(mJSON_user.optString("isshared"));
			mReportAllInfo.setShareText(mJSON_user.optString("sharetext"));
			mReportAllInfo.setShareTitle(mJSON_user.optString("sharetitle"));
			mReportAllInfo.setShareLink(mJSON_user.optString("sharelink"));

			// /** cartype **/
			// JSONObject mJSON_cartype = mJSON_data.getJSONObject("cartype");
			// mReportAllInfo.setCartypemaxmaxspeed(mJSON_cartype
			// .optString("maxmaxspeed"));
			// mReportAllInfo.setCartypeavgmaxspeed(mJSON_cartype
			// .optString("avgmaxspeed"));
			// mReportAllInfo.setCartypeminminfuel(mJSON_cartype
			// .optString("minminfuel"));
			// mReportAllInfo.setCartypeavgminfuel(mJSON_cartype
			// .optString("avgminfuel"));
			// mReportAllInfo.setCartypemaxmaxmiles(mJSON_cartype
			// .optString("maxmaxmiles"));
			// mReportAllInfo.setCartypeavgmaxmiles(mJSON_cartype
			// .optString("avgmaxmiles"));
			// mReportAllInfo.setCartypemaxmaxfuel(mJSON_cartype
			// .optString("maxmaxfuel"));
			// mReportAllInfo.setCartypeavgmaxfuel(mJSON_cartype
			// .optString("avgmaxfuel"));
			// mReportAllInfo.setCartypemaxmaxpoint(mJSON_cartype
			// .optString("maxmaxpoint"));
			// mReportAllInfo.setCartypeavgmaxpoint(mJSON_cartype
			// .optString("avgmaxpoint"));
			// mReportAllInfo.setCartypemaxmaxavgspeed(mJSON_cartype
			// .optString("maxmaxavgspeed"));
			// mReportAllInfo.setCartypeavgmaxavgspeed(mJSON_cartype
			// .optString("avgmaxavgspeed"));
			// mReportAllInfo.setCartypemaxmaxsumtime(mJSON_cartype
			// .optString("maxmaxsumtime"));
			// mReportAllInfo.setCartypeavgmaxsumtime(mJSON_cartype
			// .optString("avgmaxsumtime"));
			// mReportAllInfo
			// .setCartypeAvgfuel(mJSON_cartype.optString("avgfuel"));
			// mReportAllInfo.setCartypeMaxmiles(mJSON_cartype
			// .optString("maxmiles"));
			// mReportAllInfo.setCartypeAvgspeed(mJSON_cartype
			// .optString("avgspeed"));
			// mReportAllInfo.setCartypeAvgmiles(mJSON_cartype
			// .optString("avgmiles"));

			/** all **/
			JSONObject mJSON_all = mJSON_data.getJSONObject("all");
			mReportAllInfo
					.setAllmaxmaxspeed(mJSON_all.optString("maxmaxspeed"));
			mReportAllInfo
					.setAllavgmaxspeed(mJSON_all.optString("avgmaxspeed"));
			mReportAllInfo.setAllminminfuel(mJSON_all.optString("minminfuel"));
			mReportAllInfo.setAllavgminfuel(mJSON_all.optString("avgminfuel"));
			mReportAllInfo
					.setAllmaxmaxmiles(mJSON_all.optString("maxmaxmiles"));
			mReportAllInfo
					.setAllavgmaxmiles(mJSON_all.optString("avgmaxmiles"));
			mReportAllInfo.setAllmaxmaxfuel(mJSON_all.optString("maxmaxfuel"));
			mReportAllInfo.setAllavgmaxfuel(mJSON_all.optString("avgmaxfuel"));
			mReportAllInfo
					.setAllmaxmaxpoint(mJSON_all.optString("maxmaxpoint"));
			mReportAllInfo
					.setAllavgmaxpoint(mJSON_all.optString("avgmaxpoint"));

			double avgmaxavgspeed = mJSON_all.optDouble("avgmaxavgspeed");
			mReportAllInfo.setAllavgmaxavgspeed(String.format("%.1f",
					avgmaxavgspeed));

			double maxmaxavgspeed = mJSON_all.optDouble("maxmaxavgspeed");
			mReportAllInfo.setAllmaxmaxavgspeed(String.format("%.1f",
					maxmaxavgspeed));

			mReportAllInfo.setAllmaxmaxsumtime(mJSON_all
					.optString("maxmaxsumtime"));
			mReportAllInfo.setAllavgmaxsumtime(mJSON_all
					.optString("avgmaxsumtime"));
			mReportAllInfo.setAllAvgfuel(mJSON_all.optString("avgfuel"));
			mReportAllInfo.setAllMaxmiles(mJSON_all.optString("maxmiles"));
			mReportAllInfo.setAllAvgspeed(mJSON_all.optString("avgspeed"));
			mReportAllInfo.setAllAvgmiles(mJSON_all.optString("avgmiles"));

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}

package com.carlt.doride.protocolparser.home;


import com.carlt.doride.data.BaseResponseInfo;
import com.carlt.doride.data.home.MonthStatisticChartInfo;
import com.carlt.doride.data.home.MonthStatisticInfo;
import com.carlt.doride.protocolparser.BaseParser;
import com.carlt.doride.utils.ILog;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * 月报统计数据解析
 * 
 * @author Daisy
 * 
 */
public class ReportMonthStatisticParser extends BaseParser<MonthStatisticChartInfo> {

	MonthStatisticChartInfo mMonthStatisticChartInfo = new MonthStatisticChartInfo();
	private int[] sumfuels = new int[12];
	private int[] summiless = new int[12];
	private int[] sumtimes = new int[12];

	public ReportMonthStatisticParser(ResultCallback callback) {
		super(callback);
	}


	@Override
	protected void parser() {
		try {
			JsonObject data=mJson.getAsJsonObject("data");
			JsonArray reportList = data.getAsJsonArray("reportList");
			for (int i = 0; i < reportList.size(); i++) {
				MonthStatisticInfo mInfo = new MonthStatisticInfo();
				JsonObject mJsonObject = (JsonObject) reportList.get(i);
				mInfo.setSumfuel(mJsonObject.get("sumfuel").getAsInt());
				mInfo.setSummiles(mJsonObject.get("summiles").getAsInt());
				mInfo.setSumtime(mJsonObject.get("sumtime").getAsInt());
				mInfo.setMonth(mJsonObject.get("month").getAsInt());
				mInfo.setDate(mJsonObject.get("date").getAsString());

				sumfuels[i] = mJsonObject.get("sumfuel").getAsInt();
				summiless[i] = mJsonObject.get("summiles").getAsInt();
				sumtimes[i] = mJsonObject.get("sumtime").getAsInt();

				mMonthStatisticChartInfo.addMonthStatisticInfos(mInfo);
			}
			JsonObject total = data.getAsJsonObject("total");
			mMonthStatisticChartInfo.setSumfuel_total(total.get("sumfuel").getAsInt());
			mMonthStatisticChartInfo
					.setSummiles_total(total.get("summiles").getAsInt());
			mMonthStatisticChartInfo.setSumtime_total(total.get("sumtime").getAsInt());
			
			int maxValue_fuel=getMaxValue(sumfuels);
			mMonthStatisticChartInfo.setMaxValue_fuel(maxValue_fuel);
			int maxValue_miles=getMaxValue(summiless);
			mMonthStatisticChartInfo.setMaxValue_miles(maxValue_miles);
			int maxValue_time=getMaxValue(sumtimes);
			mMonthStatisticChartInfo.setMaxValue_time(maxValue_time);
			
			mMonthStatisticChartInfo.setMaxValue_fuel_show(getMaxValueShow(maxValue_fuel));
			mMonthStatisticChartInfo.setMaxValue_miles_show(getMaxValueShow(maxValue_miles));
			mMonthStatisticChartInfo.setMaxValue_time_show(getMaxValueShow(maxValue_time));
			mBaseResponseInfo.setValue(mMonthStatisticChartInfo);
		} catch (Exception e) {
			ILog.e(TAG, "--e==" + e);
			mBaseResponseInfo.setFlag(BaseResponseInfo.ERRO);
			mBaseResponseInfo.setInfo(MSG_ERRO);
		}
	}

	private int getMaxValue(int values[]) {
		int max = values[0];
		for (int i = 1; i < values.length; i++) {
			if (values[i] > max) {
				max = values[i];
			}
		}
		return max;
	}
	
	private int getMaxValueShow(int maxValue) {
		int maxValueShow = 0;
		float temp=(float) (maxValue/50.0);
		maxValueShow= Math.round(temp)*50+50;
		return maxValueShow;
	}

//	 public BaseResponseInfo getBaseResponseInfo(String url, String post) {
//	 try {
//	 InputStream in = CPApplication.ApplicationContext.getAssets().open(
//	 "json_report.txt");
//	 mJson = new JSONObject(FileUtil.ToString(in));
//	 Log.e("info", "Http响应--" + mJson);
//	 mBaseResponseInfo.setFlag(mJson.getString("code"));
//	 mBaseResponseInfo.setInfo(mJson.getString("msg"));
//	 } catch (Exception e) {
//	 Log.e("info", "BaseParser--e==" + e);
//	 }
//	 if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
//	 parser();
//	 }
//	 return mBaseResponseInfo;
//	
//	 }
}

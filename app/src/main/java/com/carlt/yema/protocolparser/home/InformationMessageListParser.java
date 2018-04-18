package com.carlt.yema.protocolparser.home;


import com.carlt.yema.data.BaseResponseInfo;
import com.carlt.yema.data.home.InformationMessageInfo;
import com.carlt.yema.data.home.InformationMessageInfoList;
import com.carlt.yema.protocolparser.BaseParser;
import com.carlt.yema.utils.ILog;
import com.carlt.yema.utils.MyParse;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.math.BigDecimal;

public class InformationMessageListParser extends BaseParser<InformationMessageInfoList> {

	private InformationMessageInfoList mSecretaryMessageInfoList = new InformationMessageInfoList();

	public InformationMessageListParser(ResultCallback callback) {
		super(callback);
	}


	@Override
	protected void parser() {
		try {
			JsonArray mJSON_data = mJson.getAsJsonArray("data");
			for (int i = 0; i < mJSON_data.size(); i++) {
				InformationMessageInfo mInfo = new InformationMessageInfo();
				JsonObject temp = (JsonObject) mJSON_data.get(i);
				mInfo.setId(temp.get("id").getAsString());
				mInfo.setIsoffical(temp.get("isoffical").getAsInt());
				mInfo.setTitle(temp.get("title").getAsString());
				mInfo.setRelid(temp.get("relid").getAsString());

				String content = temp.get("content").getAsString();
				mInfo.setContent(content);
				mInfo.setDetial_flag(InformationMessageInfo.FLAG_NONE);
				if (content != null && !content.equals("")) {
					if (content.length() > InformationMessageInfo.ReferenceSize) {
						mInfo.setDetial_flag(InformationMessageInfo.FLAG_REFERENCE);
						String reference = content.substring(0,
								InformationMessageInfo.ReferenceSize - 10)
								+ "...";
						mInfo.setContentReference(reference);
					}
				}
				int class1 = temp.get("class1").getAsInt();
				int class2 = temp.get("class2").getAsInt();
				mInfo.setClass1(class1);
				mInfo.setClass2(class2);
				mInfo.setCreatedate(temp.get("createdate").getAsString());

				mInfo.setImg(temp.get("img").getAsString());
				mInfo.setMiles(temp.get("miles").getAsString());

				String fuel_value = temp.get("fuel").getAsString();
				float fuel= MyParse.parseFloat(fuel_value);
				if (fuel < 100) {
					fuel_value = fuel+"毫升";
				} else {
					fuel = (float) (fuel / 1000.0);
					BigDecimal bd = new BigDecimal(fuel);
					bd = bd.setScale(1, BigDecimal.ROUND_HALF_UP);
					fuel_value = bd + "升";
				}
				mInfo.setFuel(fuel_value);

				if (temp.get("istop").getAsInt() == 1) {
					mInfo.setIstop(true);
				} else {
					mInfo.setIstop(false);
				}

				mInfo.setPoint(temp.get("point").getAsString());
				mInfo.setAvgfuel(temp.get("avgfuel").getAsString());
				mInfo.setSumtime(temp.get("sumtime").getAsString());
				mInfo.setMaxspeed(temp.get("maxspeed").getAsString());

				mInfo.setDate(temp.get("date").getAsString());
				mInfo.setIsgot(temp.get("isgot").getAsInt());

				mSecretaryMessageInfoList.addmAllList(mInfo);

			}
			mBaseResponseInfo.setValue(mSecretaryMessageInfoList);
		} catch (Exception e) {
			ILog.e(TAG, "--e==" + e);
			mBaseResponseInfo.setFlag(BaseResponseInfo.ERRO);
			mBaseResponseInfo.setInfo(MSG_ERRO);
		}

	}

	//
	// public BaseResponseInfo getBaseResponseInfo(String url, String post) {
	// try {
	// InputStream in = CPApplication.ApplicationContext.getAssets().open(
	// "json_order2.txt");
	// mJson = new JSONObject(FileUtil.ToString(in));
	// Log.e("info", "Http响应--" + mJson);
	// mBaseResponseInfo.setFlag(mJson.getString("code"));
	// mBaseResponseInfo.setInfo(mJson.getString("msg"));
	// } catch (Exception e) {
	// Log.e("info", "BaseParser--e==" + e);
	// }
	// if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
	// parser();
	// }
	// return mBaseResponseInfo;
	//
	// }
}

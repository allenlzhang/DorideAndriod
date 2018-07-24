package com.carlt.sesame.protocolstack.career;

import com.carlt.sesame.data.career.SecretaryMessageInfo;
import com.carlt.sesame.data.career.SecretaryMessageInfoList;
import com.carlt.sesame.protocolstack.BaseParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SecretaryMessageListParser extends BaseParser {

	private SecretaryMessageInfoList mSecretaryMessageInfoList = new SecretaryMessageInfoList();

	public SecretaryMessageInfoList getReturn() {
		return mSecretaryMessageInfoList;
	}

	@Override
	protected void parser() {
		try {
			JSONArray mJSON_data = mJson.getJSONArray("data");
			for (int i = 0; i < mJSON_data.length(); i++) {
				SecretaryMessageInfo mInfo = new SecretaryMessageInfo();
				JSONObject temp = (JSONObject) mJSON_data.get(i);
				mInfo.setId(temp.optString("id"));
				mInfo.setUserid(temp.optString("userid"));
				mInfo.setDealerid(temp.optString("dealerid"));
				mInfo.setIsoffical(temp.optInt("isoffical"));
				mInfo.setTitle(temp.optString("title"));
				mInfo.setRelid(temp.optString("relid"));

				String content = temp.optString("content");
				mInfo.setContent(content);
				mInfo.setDetial_flag(SecretaryMessageInfo.FLAG_NONE);
				if (content != null && !content.equals("")) {
					if (content.length() > SecretaryMessageInfo.ReferenceSize) {
						mInfo.setDetial_flag(SecretaryMessageInfo.FLAG_REFERENCE);
						String reference = content.substring(0,
								SecretaryMessageInfo.ReferenceSize - 10)
								+ "...";
						mInfo.setContentReference(reference);
					}
				}
				int class1 = temp.optInt("class1");
				int class2 = temp.optInt("class2");
				mInfo.setClass1(class1);
				mInfo.setClass2(class2);
				mInfo.setCreatedate(temp.optString("createdate"));

				mInfo.setImg(temp.optString("img"));
				mInfo.setMiles(temp.optString("miles"));

				int fuel = temp.optInt("fuel");
				String s1 = "";
				if (fuel < 100) {
					s1 = fuel + "瓦时";//"毫升";
				} else {
					double f = fuel / 1000.0;

					s1 = String.format("%.2f", f) + "度";//"升";
				}
				mInfo.setFuel(s1);

				if (temp.optInt("istop") == 1) {
					mInfo.setIstop(true);
				} else {
					mInfo.setIstop(false);
				}

				mInfo.setPoint(temp.optString("point"));
				mInfo.setAvgfuel(temp.optString("electric_avgfuel"));
				mInfo.setSumtime(temp.optString("sumtime"));
				mInfo.setMaxspeed(temp.optString("maxspeed"));

				mInfo.setCreatedate(temp.optString("createdate"));
				mInfo.setDate(temp.optString("date"));
				mInfo.setIsgot(temp.optInt("isgot"));

				mSecretaryMessageInfoList.addmAllList(mInfo);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}

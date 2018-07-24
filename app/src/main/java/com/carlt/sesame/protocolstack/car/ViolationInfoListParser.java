package com.carlt.sesame.protocolstack.car;

import com.carlt.sesame.data.BaseResponseInfo;
import com.carlt.sesame.data.car.ViolationInfo;
import com.carlt.sesame.protocolstack.BaseParser;
import com.carlt.sesame.utility.MyParse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ViolationInfoListParser extends BaseParser {

	private ArrayList<ViolationInfo> mViolationList = new ArrayList<ViolationInfo>();

	public ArrayList<ViolationInfo> getReturn() {
		return mViolationList;
	}

	@Override
	protected void parser() {
		try {
			int resultCode = mJson.optInt("resultcode");
			int errorCode = mJson.optInt("errorCode");
			String msg = mJson.optString("reason");
			mBaseResponseInfo.setInfo(msg);
			if (errorCode >= 10001 && errorCode <= 10021) {
				resultCode = 205;
			}
			
			mBaseResponseInfo.setFlag(resultCode);
			if (resultCode != BaseResponseInfo.SUCCESS) {
				return;
			}

			JSONArray mJSON_data = mJson.getJSONObject("result").getJSONArray("lists");
			for (int i = 0; i < mJSON_data.length(); i++) {
				ViolationInfo mViolationInfo = new ViolationInfo();
				JSONObject temp = (JSONObject) mJSON_data.get(i);
				mViolationInfo.setDate(temp.optString("date"));
				mViolationInfo.setArea(temp.optString("area"));
				mViolationInfo.setAct(temp.optString("act"));
				mViolationInfo.setCode(temp.optString("code"));
				mViolationInfo.setFen(MyParse.parseInt(temp.optString("fen")) + "");
				mViolationInfo.setMoney(MyParse.parseInt(temp.optString("money")) + "");
				mViolationInfo.setHandled(temp.optString("handled"));
				mViolationInfo.setShareText(temp.optString("sharetext", ""));
				mViolationInfo.setShareTitle(temp.optString("sharetitle", ""));
				mViolationInfo.setShareLink(temp.optString("sharelink", ""));

				mViolationList.add(mViolationInfo);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}

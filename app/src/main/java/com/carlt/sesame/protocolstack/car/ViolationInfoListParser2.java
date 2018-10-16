package com.carlt.sesame.protocolstack.car;

import com.carlt.sesame.data.BaseResponseInfo;
import com.carlt.sesame.data.car.ViolationInfo;
import com.carlt.sesame.protocolstack.BaseParser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ViolationInfoListParser2 extends BaseParser {

	private ArrayList<ViolationInfo> mViolationList = new ArrayList<ViolationInfo>();

	public ArrayList<ViolationInfo> getReturn() {
		return mViolationList;
	}

	@Override
	protected void parser() {
		try {
			int resultCode = mJson.optInt("ErrorCode");
			String msg = mJson.optString("ErrMessage");
			boolean success = mJson.optBoolean("Success");
			mBaseResponseInfo.setInfo(msg);
			if(resultCode == 0 && success){
				resultCode = 200;
			}else{
				resultCode = 205;
			}
			
			mBaseResponseInfo.setFlag(resultCode);
			if (resultCode != BaseResponseInfo.SUCCESS) {
				return;
			}
			
			boolean hasData = mJson.optBoolean("HasData");
			if(hasData){
				JSONArray mJSON_data = mJson.getJSONArray("Records");
				for (int i = 0; i < mJSON_data.length(); i++) {
					ViolationInfo mViolationInfo = new ViolationInfo();
					JSONObject temp = (JSONObject) mJSON_data.get(i);
					mViolationInfo.setDate(temp.optString("Time"));
					mViolationInfo.setArea(temp.optString("Location"));
					mViolationInfo.setAct(temp.optString("Reason"));
					mViolationInfo.setCode(temp.optString("Code"));
					mViolationInfo.setFen(temp.optString("Degree"));
					mViolationInfo.setMoney(temp.optString("count"));
					mViolationInfo.setHandled(temp.optString("status"));
					mViolationInfo.setShareText("");
					mViolationInfo.setShareTitle("");
					mViolationInfo.setShareLink("");

					mViolationList.add(mViolationInfo);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}

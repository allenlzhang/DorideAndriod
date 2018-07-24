package com.carlt.sesame.protocolstack.car;

import com.carlt.sesame.data.car.CheckFaultInfo;
import com.carlt.sesame.protocolstack.BaseParser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class CheckFaultListParser extends BaseParser {

	private ArrayList<CheckFaultInfo> mistakeListP = new ArrayList<CheckFaultInfo>();
	private ArrayList<CheckFaultInfo> mistakeListB = new ArrayList<CheckFaultInfo>();
	private ArrayList<CheckFaultInfo> mistakeListC = new ArrayList<CheckFaultInfo>();
	private ArrayList<CheckFaultInfo> mistakeListU = new ArrayList<CheckFaultInfo>();

	public ArrayList<CheckFaultInfo> getMistakeListP() {
		return mistakeListP;
	}

	public void setMistakeListP(ArrayList<CheckFaultInfo> mistakeListP) {
		this.mistakeListP = mistakeListP;
	}

	public ArrayList<CheckFaultInfo> getMistakeListB() {
		return mistakeListB;
	}

	public void setMistakeListB(ArrayList<CheckFaultInfo> mistakeListB) {
		this.mistakeListB = mistakeListB;
	}

	public ArrayList<CheckFaultInfo> getMistakeListC() {
		return mistakeListC;
	}

	public void setMistakeListC(ArrayList<CheckFaultInfo> mistakeListC) {
		this.mistakeListC = mistakeListC;
	}

	public ArrayList<CheckFaultInfo> getMistakeListU() {
		return mistakeListU;
	}

	public void setMistakeListU(ArrayList<CheckFaultInfo> mistakeListU) {
		this.mistakeListU = mistakeListU;
	}

	private String id;

	private String point;

	private int isrunning = -1;

	private String shareTitle;
	private String shareText;
	private String shareLink;

	public String getShareTitle() {
		return shareTitle;
	}

	public String getShareText() {
		return shareText;
	}

	public String getShareLink() {
		return shareLink;
	}

	@Override
	protected void parser() throws Exception {

		JSONObject mJSON_data = mJson.getJSONObject("data");
		id = mJSON_data.optString("id");
		point = mJSON_data.optString("point");
		isrunning = mJSON_data.optInt("isrunning");

		shareTitle = mJSON_data.optString("sharetitle");
		shareText = mJSON_data.optString("sharetext");
		shareLink = mJSON_data.optString("sharelink");

		JSONArray mJSON_list = mJSON_data.getJSONArray("list");
		for (int i = 0; i < mJSON_list.length(); i++) {
			CheckFaultInfo mCheckFaultInfo = new CheckFaultInfo();
			JSONObject temp = (JSONObject) mJSON_list.get(i);
			String code = temp.optString("code");
			mCheckFaultInfo.setCode(code);
			mCheckFaultInfo.setCn(temp.optString("cn"));
			mCheckFaultInfo.setEn(temp.optString("en"));
			mCheckFaultInfo.setScope(temp.optString("scope"));
			mCheckFaultInfo.setContent(temp.optString("content"));
			mCheckFaultInfo.setFlag(CheckFaultInfo.STATE2);
			if (code.startsWith("P")) {
				mistakeListP.add(mCheckFaultInfo);
			} else if (code.startsWith("B")) {
				mistakeListB.add(mCheckFaultInfo);
			} else if (code.startsWith("C")) {
				mistakeListC.add(mCheckFaultInfo);
			} else if (code.startsWith("U")) {
				mistakeListU.add(mCheckFaultInfo);
			}
		}

	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPoint() {
		return point;
	}

	public void setPoint(String point) {
		this.point = point;
	}

	public int getIsrunning() {
		return isrunning;
	}

	public void setIsrunning(int isrunning) {
		this.isrunning = isrunning;
	}

}

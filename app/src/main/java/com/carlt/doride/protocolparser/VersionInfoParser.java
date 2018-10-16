package com.carlt.doride.protocolparser;


import com.carlt.doride.data.VersionInfo;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class VersionInfoParser extends BaseParser {
	private VersionInfo mVersionInfo = new VersionInfo();

	public VersionInfoParser(ResultCallback callback) {
		super(callback);
	}

	public VersionInfo getReturn() {
		return mVersionInfo;
	}

	@Override
	protected void parser() {
//		test();
		JsonObject mJSON_data = mJson.getAsJsonObject("data");
		mVersionInfo.setStatus(mJSON_data.get("status").getAsInt());
		mVersionInfo.setFilepath(mJSON_data.get("filepath").getAsString());
		mVersionInfo.setRemark(mJSON_data.get("info").getAsString());
		mBaseResponseInfo.setValue(mVersionInfo);
	}


	void test(){
		String data="{\n" +
				"    \"code\": 200,\n" +
				"    \"request\": \"$version/comm/appUpdate\",\n" +
				"    \"data\": {\n" +
				"    \"info\": \"改了好多功能\",\n" +
				"    \"filepath\": \"http://wzlinkapi.geni4s.com/upfiles/mobile/CarParticle_v2.0.apk\",\n" +
				"    \"version\": \"101\",\n" +
				"    \"status\": \"3\"\n" +
				"    },\n" +
				"    \"msg\": \"\"\n" +
				"}";
		JsonParser jp = new JsonParser();
		JsonElement element = jp.parse(data);
		mJson = element.getAsJsonObject();
	}

}

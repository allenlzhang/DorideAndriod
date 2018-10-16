package com.carlt.sesame.protocolstack.safety;

import com.carlt.sesame.data.safety.MobileInfo;
import com.carlt.sesame.data.safety.MobileListInfo;
import com.carlt.sesame.protocolstack.BaseParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 授权设备列表解析
 * @author Administrator
 *
 */
public class MobileListParser extends BaseParser {

	private MobileListInfo mMobileListInfo = new MobileListInfo();

	public MobileListInfo getReturn() {
		return mMobileListInfo;
	}

	@Override
	protected void parser() {
		try {
		    JSONObject mJSON_data = mJson.getJSONObject("data");
		    JSONArray mJSON_infos=mJSON_data.getJSONArray("list");
			for (int i = 0; i < mJSON_infos.length(); i++) {
				MobileInfo mInfo = new MobileInfo();
				JSONObject temp = (JSONObject) mJSON_infos.get(i);
				mInfo.setId(temp.optString("id",""));
				mInfo.setMobile_id(temp.optString("authorize_deviceid",""));
				mInfo.setName(temp.optString("authorize_name",""));
				mInfo.setModel(temp.optString("authorize_model",""));
				mInfo.setTime(temp.optString("addtime",""));
				
				mInfo.setAuthorize_type(temp.optString("authorize_type",""));
				
				mMobileListInfo.addmMobileInfoList(mInfo);
			}
			int i=mJSON_data.optInt("has_next");
			boolean hasMore = true;
			if(i==-1){
			    hasMore=false;
			}else if(i==1){
			    hasMore=true;
			}
			mMobileListInfo.setHasMore(hasMore);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}

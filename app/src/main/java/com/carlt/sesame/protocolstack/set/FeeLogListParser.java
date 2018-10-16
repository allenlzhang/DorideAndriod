package com.carlt.sesame.protocolstack.set;

import com.carlt.sesame.data.set.FeeLogInfo;
import com.carlt.sesame.data.set.FeeLogListInfo;
import com.carlt.sesame.protocolstack.BaseParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 续费记录日志解析
 * @author Administrator
 *
 */
public class FeeLogListParser extends BaseParser {

	private FeeLogListInfo mFeeLogListInfo = new FeeLogListInfo();

	public FeeLogListInfo getReturn() {
		return mFeeLogListInfo;
	}

	@Override
	protected void parser() {
		try {
			JSONObject mJSON_data = mJson.getJSONObject("data");
			JSONArray mJSON_infos=mJSON_data.getJSONArray("list");
			for (int i = 0; i < mJSON_infos.length(); i++) {
				FeeLogInfo mInfo = new FeeLogInfo();
				JSONObject temp = (JSONObject) mJSON_infos.get(i);
				mInfo.setName(temp.optString("log_name",""));
				mInfo.setFee(temp.optString("renew_cost",""));
				mInfo.setDatePay(temp.optString("addtime",""));
				mInfo.setDateFromto(temp.optString("renew_expire",""));
				
				mFeeLogListInfo.addmFeeLogInfoList(mInfo);
			}
			int i=mJSON_data.optInt("has_next");
			boolean hasMore = true;
			if(i==-1){
			    hasMore=false;
			}else if(i==1){
			    hasMore=true;
			}
			mFeeLogListInfo.setHasMore(hasMore);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}

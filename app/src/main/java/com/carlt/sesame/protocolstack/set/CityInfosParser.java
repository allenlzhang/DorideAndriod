package com.carlt.sesame.protocolstack.set;

import com.carlt.sesame.control.DaoControl;
import com.carlt.sesame.data.BaseResponseInfo;
import com.carlt.sesame.data.set.CityStringInfo;
import com.carlt.sesame.protocolstack.BaseParser;
import com.carlt.sesame.utility.MyTimeUtil;

import java.util.ArrayList;
import java.util.Date;

public class CityInfosParser extends BaseParser {


	@Override
	protected void parser() {
		try {
			int resultCode = mJson.optInt("resultcode");
			String msg = mJson.optString("reason");
			mBaseResponseInfo.setFlag(resultCode);
			mBaseResponseInfo.setInfo(msg);
			if (resultCode != BaseResponseInfo.SUCCESS) {
				return;
			}

//			JSONObject mJSON_data = mJson.getJSONObject("result");
			String citys = mJson.toString();
			DaoControl dao = DaoControl.getInstance();
			ArrayList<CityStringInfo> infos = dao.getCityStringInfoList();
			CityStringInfo info = null;
			boolean isFirst = false;
			if(infos.size() > 1){
				info = infos.get(0);
			}else{
				isFirst = true;
				info = new CityStringInfo();
			}
			info.setTxt(citys);
			info.setTime(MyTimeUtil.commonFormat.format(new Date()));
			if(isFirst){
				dao.insertCityStringInfo(info);
			}else{
				dao.updataCityStringInfo(info);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}

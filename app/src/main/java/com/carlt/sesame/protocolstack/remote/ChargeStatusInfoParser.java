
package com.carlt.sesame.protocolstack.remote;

import com.carlt.sesame.data.remote.ChargeStatusInfo;
import com.carlt.sesame.protocolstack.BaseParser;
import com.carlt.sesame.utility.MyTimeUtil;

import org.json.JSONObject;

/**
 * 充电状态解析
 * @author Daisy
 *
 */
public class ChargeStatusInfoParser extends BaseParser {

    private ChargeStatusInfo mCarStateInfos = new ChargeStatusInfo();

    public ChargeStatusInfo getReturn() {

        return mCarStateInfos;
    }

    public ChargeStatusInfoParser() {
    }

    @Override
    protected void parser() {
        JSONObject mJSON_data = mJson.optJSONObject("data");
        if (mJSON_data != null) {
        	int status=mJSON_data.optInt("status");
        	mCarStateInfos.setStatus(status);
        	int chargetime= mJSON_data.optInt("chargetime");
        	String hhmm= MyTimeUtil.getDateFormat5(chargetime);
        	String chargeTimeDes="";
        	if(MyTimeUtil.getIsToday(chargetime)){
        		chargeTimeDes="今天"+hhmm+"开始充电";
        	}else{
        		if(MyTimeUtil.getIsTomorrow(chargetime)){
        			chargeTimeDes="明天"+hhmm+"开始充电";
        		}else{
        			chargeTimeDes="";
        		}
        	}
        	mCarStateInfos.setChargeTimeDes(chargeTimeDes);
        	mCarStateInfos.setChargeTime(chargetime);
        }

    }

}

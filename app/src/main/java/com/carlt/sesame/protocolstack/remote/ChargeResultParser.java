
package com.carlt.sesame.protocolstack.remote;

import com.carlt.sesame.data.remote.ChargeResultInfo;
import com.carlt.sesame.protocolstack.BaseParser;
import com.carlt.sesame.utility.MyTimeUtil;

/**
 * 充电结果解析
 * @author Daisy
 *
 */
public class ChargeResultParser extends BaseParser {

    private ChargeResultInfo mCarResultInfo = new ChargeResultInfo();

    public ChargeResultInfo getReturn() {

        return mCarResultInfo;
    }

    public ChargeResultParser() {
    }

    @Override
    protected void parser() {
    	int chargetime=mJson.optInt("data");
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
    	mCarResultInfo.setChargeTimeDes(chargeTimeDes);
    }

}

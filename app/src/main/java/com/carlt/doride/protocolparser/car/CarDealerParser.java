package com.carlt.doride.protocolparser.car;

import com.carlt.doride.data.car.DealerInfo;
import com.carlt.doride.protocolparser.BaseParser;
import com.google.gson.JsonObject;

/**
 * Created by marller on 2018\4\2 0002.
 */

/**
 * 经销商信息
 *
 * */
public class CarDealerParser extends BaseParser {

    DealerInfo mDealerInfo=new DealerInfo();

    public CarDealerParser(ResultCallback callback) {
        super(callback);
    }

    @Override
    protected void parser() throws Exception {
        JsonObject data=mJson.getAsJsonObject("data");
        mDealerInfo.setDealerName(data.get("name").getAsString());
        mDealerInfo.setDealerAddress(data.get("addres").getAsString());
        mDealerInfo.setDealerMap(data.get("map").getAsString());
        mDealerInfo.setDealerTel(data.get("tel").getAsString());
        mDealerInfo.setServiceTel(data.get("tel1").getAsString());
        mBaseResponseInfo.setValue(mDealerInfo);
    }
}

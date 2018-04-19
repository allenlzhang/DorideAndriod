package com.carlt.doride.protocolparser.car;

import com.carlt.doride.data.car.CarSettingInfo;
import com.carlt.doride.protocolparser.BaseParser;
import com.google.gson.JsonObject;

/**
 * Created by marller on 2018\4\12 0012.
 */

public class CarSettingInfoParser extends BaseParser {

    CarSettingInfo carSettingInfo=new CarSettingInfo();

    public CarSettingInfoParser(ResultCallback callback) {
        super(callback);
    }

    @Override
    protected void parser() throws Exception {
        JsonObject data=mJson.getAsJsonObject("data");
        carSettingInfo.setBrandid(data.get("brandid").getAsString());
        carSettingInfo.setBuydate(data.get("buydate").getAsString());
        carSettingInfo.setCarid(data.get("carid").getAsString());
        carSettingInfo.setCarname(data.get("carname").getAsString());
        carSettingInfo.setInsurance_time(data.get("insurance_time").getAsString());
        carSettingInfo.setMainten_date(data.get("mainten_date").getAsString());
        carSettingInfo.setMainten_miles(data.get("mainten_miles").getAsString());
        carSettingInfo.setMainten_next_date(data.get("mainten_next_date").getAsString());
        carSettingInfo.setMainten_next_miles(data.get("mainten_next_miles").getAsString());
        carSettingInfo.setModelid(data.get("modelid").getAsString());
        carSettingInfo.setOptionid(data.get("optionid").getAsString());
        carSettingInfo.setRegister_time(data.get("register_time").getAsString());
        carSettingInfo.setSummiles(data.get("summiles").getAsString());
        mBaseResponseInfo.setValue(carSettingInfo);
    }
}

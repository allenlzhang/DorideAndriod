package com.carlt.yema.protocolparser;

import com.carlt.yema.data.DeviceUpdateInfo;
import com.google.gson.JsonObject;

/**
 * Created by marller on 2018\3\27 0027.
 */

public class DeviceUpdateInfoParser extends BaseParser {

    DeviceUpdateInfo updateInfo=new DeviceUpdateInfo();

    public DeviceUpdateInfoParser(ResultCallback callback) {
        super(callback);
    }

    @Override
    protected void parser() throws Exception {
        JsonObject data=mJson.getAsJsonObject("data");
        boolean isupgrade=data.get("isupgrade").getAsString().equals("1")?true:false;
        updateInfo.setUpgrade(isupgrade);
        boolean upgradeing=data.get("upgradeing").getAsString().equals("1")?true:false;
        updateInfo.setUpgrading(upgradeing);
        updateInfo.setUpgradetime(data.get("upgradetime").getAsString());
        mBaseResponseInfo.setValue(updateInfo);
    }
}

package com.carlt.sesame.protocolstack;

import com.carlt.sesame.data.DeviceUpdateInfo;

import org.json.JSONObject;

public class DeviceUpdateInfoParser extends BaseParser {
	private DeviceUpdateInfo mDeviceUpdateInfo = new DeviceUpdateInfo();

	public DeviceUpdateInfo getReturn() {
		return mDeviceUpdateInfo;
	}

	@Override
	protected void parser() {
		JSONObject mJSON_data = mJson.optJSONObject("data");
		String upgrade=mJSON_data.optString("isupgrade");
		boolean isUpgrade = false;
		if(upgrade!=null&&upgrade.length()>0){
		    if(upgrade.equals("0")){
		        isUpgrade=false;
	        }else if(upgrade.equals("1")){
	            isUpgrade=true;
	        } 
		}
		mDeviceUpdateInfo.setUpgrade(isUpgrade);
		
		mDeviceUpdateInfo.setUpgradetime(mJSON_data.optString("upgradetime"));
		String upgradeing=mJSON_data.optString("upgradeing");
        boolean isUpgrading = false;
        if(upgradeing!=null&&upgradeing.length()>0){
            if(upgradeing.equals("0")){
                isUpgrading=false;
            }else if(upgradeing.equals("1")){
                isUpgrading=true;
            } 
        }
        mDeviceUpdateInfo.setUpgrading(isUpgrading);
        
	}
}

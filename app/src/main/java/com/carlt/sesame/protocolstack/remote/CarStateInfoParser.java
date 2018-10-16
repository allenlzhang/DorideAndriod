package com.carlt.sesame.protocolstack.remote;

import com.carlt.sesame.data.remote.CarStateInfo;
import com.carlt.sesame.protocolstack.BaseParser;

import org.json.JSONObject;

import java.util.ArrayList;

public class CarStateInfoParser extends BaseParser {

	private ArrayList<CarStateInfo> mCarStateInfos = new ArrayList<CarStateInfo>();

	private String mDeviceType;// 设备类型

	public ArrayList<CarStateInfo> getReturn() {

		return mCarStateInfos;
	}

	public CarStateInfoParser(String deviceType) {
		mDeviceType = deviceType;
	}

	@Override
	protected void parser() {
		JSONObject mJSON_data = mJson.optJSONObject("data");
		if (mJSON_data != null) {
			String[] names = CarStateInfo.names;
			int[] iconId_opens = CarStateInfo.iconId_opens_carmain;
			int[] iconId_closes = CarStateInfo.iconId_closes_carmain;
			int length = names.length;
			String[] apiAttrNames = { "doorLockStatus", "doorCloseStatus",
					"engine", "AC" };
			String[] stateClose = { "已锁", "已关", "已熄火", "关闭" };
			String[] stateOpen = { "未锁", "未关", "已启动", "已开启" };

			String[] apiAttrNamesAfter = { "doorLockStatus", "doorCloseStatus",
					"engine" };
			String[] stateCloseAfter = { "已锁", "已关", "已熄火" };
			String[] stateOpenAfter = { "未锁", "未关", "已启动" };
			int lengthAfter = apiAttrNamesAfter.length;
			// if (mDeviceType.equals(LoginInfo.DEVICETYPE_BEFORE)
			// || mDeviceType.equals(LoginInfo.DEVICETYPE_AFTER2016)) {
			// // 前装设备or后装2016款
			for (int i = 0; i < length; i++) {
				CarStateInfo mInfo = new CarStateInfo();
				mInfo.setName(names[i]);
				String state = mJSON_data.optString(apiAttrNames[i], "");
				if (i == 3) {
					if (state.equals("2")) {
						mInfo.setIconId(iconId_closes[i]);
						mInfo.setStateDes(stateClose[i]);
					} else {
						mInfo.setIconId(iconId_opens[i]);
						mInfo.setStateDes(stateOpen[i]);
						String temp = mJSON_data.optString("ACTemp");
						if (temp != null && !temp.equals("")) {
							double temp_double = Double.valueOf(temp);
							int temp_int = (int) Math.rint(temp_double);
							mInfo.setValue(temp_int + "℃");
						} else {
							mInfo.setValue("--℃");
						}

					}
				} else {
					if (state.equals("0")) {
						mInfo.setIconId(iconId_closes[i]);
						mInfo.setStateDes(stateClose[i]);
					} else {
						mInfo.setIconId(iconId_opens[i]);
						mInfo.setStateDes(stateOpen[i]);
					}
				}
				mCarStateInfos.add(mInfo);
			}
			// }

			// else {
			// // 后装设备
			// for (int i = 0; i < lengthAfter; i++) {
			// CarStateInfo mInfo = new CarStateInfo();
			// mInfo.setName(names[i]);
			// String state = mJSON_data.optString(apiAttrNamesAfter[i], "");
			// if (state.equals("0")) {
			// mInfo.setIconId(iconId_closes[i]);
			// mInfo.setStateDes(stateCloseAfter[i]);
			// } else {
			// mInfo.setIconId(iconId_opens[i]);
			// mInfo.setStateDes(stateOpenAfter[i]);
			// }
			// mCarStateInfos.add(mInfo);
			// }
			// }

		}

	}

}

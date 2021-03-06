package com.carlt.sesame.ui.activity.car;

import android.text.TextUtils;

import com.carlt.doride.R;
import com.carlt.doride.http.retrofitnet.model.OtherInfo;
import com.carlt.sesame.data.SesameLoginInfo;
import com.carlt.sesame.data.car.CarMainFunInfo;
import com.carlt.sesame.data.remote.AirMainInfo;
import com.carlt.sesame.data.remote.RemoteFunInfo;
import com.carlt.sesame.data.remote.RemoteMainInfo;
import com.carlt.sesame.protocolstack.BaseParser;

import org.json.JSONObject;

public class CarConfigParser extends BaseParser {

	private RemoteMainInfo mRemoteMainInfo = new RemoteMainInfo();
	private CarMainFunInfo mCarMainFunInfo = new CarMainFunInfo();
	private AirMainInfo mAirMainInfo = new AirMainInfo();
	private int supportCountAir = 0;
	private int supportCount = 0;

	@Override
	public Object getReturn() {
		return mRemoteMainInfo;
	}

	@Override
	protected void parser() {
		JSONObject mJSON_data = mJson.optJSONObject("data");
		if (mJSON_data != null) {
			RemoteFunInfo mFunInfo;
			String state = "";

			// mFunInfo = new RemoteFunInfo();
			// mFunInfo.setId("-2");
			// mFunInfo.setApi_field("remoteStart");
			// mFunInfo.setName("start");
			// mFunInfo.setIcon_id(0);
			// mFunInfo.setState(mJSON_data.optInt("remoteStart") + "");
			// mRemoteMainInfo.setmFunInfoStart(mFunInfo);
			//
			// mFunInfo = new RemoteFunInfo();
			// mFunInfo.setId("-1");
			// mFunInfo.setApi_field("remoteStart");
			// mFunInfo.setName("stop");
			// mFunInfo.setIcon_id(0);
			// mFunInfo.setState(mJSON_data.optInt("remoteStart") + "");
			// mRemoteMainInfo.setmFunInfoStop(mFunInfo);

			// mFunInfo = new RemoteFunInfo();
			// mFunInfo.setId("1");
			// mFunInfo.setApi_field("remoteTrunk");
			// mFunInfo.setName("开启后备箱");
			// mFunInfo.setIcon_id(R.drawable.trunck);
			// state = mJSON_data.optInt("remoteTrunk") + "";
			// mFunInfo.setState(state);
			// if (state.equals(RemoteFunInfo.STATE_SUPPORT)) {
			// mRemoteMainInfo.addmRemoteFunInfos(mFunInfo);
			// }

			// mFunInfo=new RemoteFunInfo();
			// mFunInfo.setId("2");
			// mFunInfo.setApi_field("remoteCloseAircon");
			// mFunInfo.setName("关闭空调");
			// mFunInfo.setIcon_id(R.drawable.air_condition_close);
			// mFunInfo.setState(mRemote.optString("remoteCloseAircon"));
			// state=mRemote.optString("remoteCloseAircon");
			// mFunInfo.setState(state);
			// if(state.equals(RemoteFunInfo.STATE_SUPPORT)){
			// mRemoteMainInfo.addmRemoteFunInfos(mFunInfo);
			// }

			mFunInfo = new RemoteFunInfo();
			mFunInfo.setId(RemoteFunInfo.FUNC_UNLOCK);
			mFunInfo.setName("解锁");
			mFunInfo.setIcon_id(R.drawable.remote_new_unlock_bg);
			state = mJSON_data.optInt("remoteLocked") + "";
			mFunInfo.setState(state);
			if (state.equals(RemoteFunInfo.STATE_SUPPORT)) {
				mRemoteMainInfo.addmRemoteFunInfos(mFunInfo);
				supportCount++;
			}

			mFunInfo = new RemoteFunInfo();
			mFunInfo.setId(RemoteFunInfo.FUNC_LOCK);
			mFunInfo.setName("落锁");
			mFunInfo.setIcon_id(R.drawable.remote_new_lock_bg);
			state = mJSON_data.optInt("remoteLocked") + "";
			mFunInfo.setState(state);
			if (state.equals(RemoteFunInfo.STATE_SUPPORT)) {
				mRemoteMainInfo.addmRemoteFunInfos(mFunInfo);
				supportCount++;
			}
			
			mFunInfo = new RemoteFunInfo();
			mFunInfo.setId(RemoteFunInfo.FUNC_FIND);
			mFunInfo.setName("一键寻车");
			mFunInfo.setIcon_id(R.drawable.remote_new_find_bg);
			state = mJSON_data.optInt("SLCarLocating") + "";
			mFunInfo.setState(state);
			if (state.equals(RemoteFunInfo.STATE_SUPPORT)) {
				mRemoteMainInfo.addmRemoteFunInfos(mFunInfo);
				supportCount++;
			}
			
			mFunInfo = new RemoteFunInfo();
			mFunInfo.setId(RemoteFunInfo.FUNC_AIR);
			mFunInfo.setName("空调");
			mFunInfo.setIcon_id(R.drawable.remote_new_air_bg);
			state = mJSON_data.optInt("remoteAirconditioner") + "";
			mFunInfo.setState(state);
			if (state.equals(RemoteFunInfo.STATE_SUPPORT)) {
				mRemoteMainInfo.addmRemoteFunInfos(mFunInfo);
				supportCount++;
			}
			
			mFunInfo = new RemoteFunInfo();
			mFunInfo.setId(RemoteFunInfo.FUNC_CHARGE);
			mFunInfo.setName("充电");
			mFunInfo.setIcon_id(R.drawable.remote_new_charge_bg);
			state = mJSON_data.optInt("remoteCharger") + "";
			mFunInfo.setState(state);
			if (state.equals(RemoteFunInfo.STATE_SUPPORT)) {
				mRemoteMainInfo.addmRemoteFunInfos(mFunInfo);
				supportCount++;
			}

			mRemoteMainInfo.setFunctionCount(supportCount+"");
			OtherInfo.getInstance().setRemoteMainInfo(mRemoteMainInfo);

			mFunInfo = new RemoteFunInfo();
			mFunInfo.setId("0");
			mFunInfo.setName("胎压监测");
			mFunInfo.setIcon_id(R.drawable.icon_tire);
			state = mJSON_data.optInt("PSTsupervise") + "";
			mFunInfo.setState(state);
			if (state.equals(RemoteFunInfo.STATE_SUPPORT)) {
				mCarMainFunInfo.addmCarmainFunInfos(mFunInfo);
			}

			mFunInfo = new RemoteFunInfo();
			mFunInfo.setId("1");
			mFunInfo.setName("导航同步");
			mFunInfo.setIcon_id(R.drawable.icon_navigation);
			state = mJSON_data.optInt("navigationSync") + "";
			mFunInfo.setState(state);
			if (state.equals(RemoteFunInfo.STATE_SUPPORT)) {
				mCarMainFunInfo.addmCarmainFunInfos(mFunInfo);
			}
			OtherInfo.getInstance().setCarMainFunInfo(mCarMainFunInfo);

			String remote_airconditioner_item = mJSON_data
					.optString("remoteAirconditioner_item");
			if (!TextUtils.isEmpty(remote_airconditioner_item)) {
				String[] items = remote_airconditioner_item.split(",");
				int index;
				 index = remote_airconditioner_item.indexOf("1");
				 if (index != -1) {
				 mFunInfo = new RemoteFunInfo();
				 mFunInfo.setId(RemoteFunInfo.MODE_AUTO);
				 mFunInfo.setName("全自动");
				 mFunInfo.setIcon_id(R.drawable.remote_auto);
				 mFunInfo.setIcon_id_seleced(R.drawable.remote_auto_selected);
				 mFunInfo.setIcon_id_seleced_no(R.drawable.remote_auto_selected_no);
				 state = RemoteFunInfo.STATE_SUPPORT;
				 mFunInfo.setState(state);
//				 mFunInfo.setTemperature("24");
				 if (state.equals(RemoteFunInfo.STATE_SUPPORT)) {
				 mAirMainInfo.addmRemoteFunInfos(mFunInfo);
				 }
				
				 supportCount++;
				 }

				index = remote_airconditioner_item.indexOf("5");
				if (index != -1) {
					mFunInfo = new RemoteFunInfo();
					mFunInfo.setId(RemoteFunInfo.MODE_HEAT);
					mFunInfo.setName("最大制热");
					mFunInfo.setIcon_id(R.drawable.remote_hot);
					mFunInfo.setIcon_id_seleced(R.drawable.remote_hot_selected);
					mFunInfo.setIcon_id_seleced_no(R.drawable.remote_hot_selected_no);
					state = RemoteFunInfo.STATE_SUPPORT;
					mFunInfo.setState(state);
//					mFunInfo.setTemperature("32");
					if (state.equals(RemoteFunInfo.STATE_SUPPORT)) {
						mAirMainInfo.addmRemoteFunInfos(mFunInfo);
					}

					supportCountAir++;
				}

				index = remote_airconditioner_item.indexOf("4");
				if (index != -1) {
					mFunInfo = new RemoteFunInfo();
					mFunInfo.setId(RemoteFunInfo.MODE_COLD);
					mFunInfo.setName("最大制冷");
					mFunInfo.setIcon_id(R.drawable.remote_cold);
					mFunInfo.setIcon_id_seleced(R.drawable.remote_cold_selected);
					mFunInfo.setIcon_id_seleced_no(R.drawable.remote_cold_selected_no);
					state = RemoteFunInfo.STATE_SUPPORT;
					mFunInfo.setState(state);
//					mFunInfo.setTemperature("18");
					if (state.equals(RemoteFunInfo.STATE_SUPPORT)) {
						mAirMainInfo.addmRemoteFunInfos(mFunInfo);
					}

					supportCountAir++;
				}

				index = remote_airconditioner_item.indexOf("3");
				if (index != -1) {
					mFunInfo = new RemoteFunInfo();
					mFunInfo.setId(RemoteFunInfo.MODE_FROG);
					mFunInfo.setName("一键除霜");
					mFunInfo.setIcon_id(R.drawable.remote_frost);
					mFunInfo.setIcon_id_seleced(R.drawable.remote_frost_selected);
					mFunInfo.setIcon_id_seleced_no(R.drawable.remote_frost_selected_no);
					state = RemoteFunInfo.STATE_SUPPORT;
					mFunInfo.setState(state);
//					mFunInfo.setTemperature("32");
					if (state.equals(RemoteFunInfo.STATE_SUPPORT)) {
						mAirMainInfo.addmRemoteFunInfos(mFunInfo);
					}

					supportCountAir++;
				}

				 index = remote_airconditioner_item.indexOf("7");
				 if (index != -1) {
				 mFunInfo = new RemoteFunInfo();
				 mFunInfo.setId(RemoteFunInfo.MODE_CLEAN);
				 mFunInfo.setName("座舱清洁");
				 mFunInfo.setIcon_id(R.drawable.remote_clean);
				 mFunInfo.setIcon_id_seleced(R.drawable.remote_clean_selected);
				 mFunInfo.setIcon_id_seleced_no(R.drawable.remote_clean_selected_no);
				 state = RemoteFunInfo.STATE_SUPPORT;
				 mFunInfo.setState(state);
//				 mFunInfo.setTemperature("22");
				 if (state.equals(RemoteFunInfo.STATE_SUPPORT)) {
				 mAirMainInfo.addmRemoteFunInfos(mFunInfo);
				 }
				
				 supportCount++;
				 }

				 index = remote_airconditioner_item.indexOf("6");
				 if (index != -1) {
				 mFunInfo = new RemoteFunInfo();
				 mFunInfo.setId(RemoteFunInfo.MODE_ANION);
				 mFunInfo.setName("负离子");
				 mFunInfo.setIcon_id(R.drawable.remote_anion);
				 mFunInfo.setIcon_id_seleced(R.drawable.remote_anion_selected);
				 mFunInfo.setIcon_id_seleced_no(R.drawable.remote_anion_selected_no);
				 state = RemoteFunInfo.STATE_SUPPORT;
				 mFunInfo.setState(state);
//				 mFunInfo.setTemperature("22");
				 if (state.equals(RemoteFunInfo.STATE_SUPPORT)) {
				 mAirMainInfo.addmRemoteFunInfos(mFunInfo);
				 }
				
				 supportCount++;
				 }

				 index = remote_airconditioner_item.indexOf("8");
				 if (index != -1) {
				 mFunInfo = new RemoteFunInfo();
				 mFunInfo.setId(RemoteFunInfo.MODE_TEMPREGULATION);
				 mFunInfo.setName("温度调节");
				 mFunInfo.setIcon_id(R.drawable.remote_regulation);
				 mFunInfo.setIcon_id_seleced(R.drawable.remote_regulation_selected);
				 mFunInfo.setIcon_id_seleced_no(R.drawable.remote_regulation_selected_no);
				 state = RemoteFunInfo.STATE_SUPPORT;
				 mFunInfo.setState(state);
//				 mFunInfo.setTemperature("18");
				 if (state.equals(RemoteFunInfo.STATE_SUPPORT)) {
				 mAirMainInfo.addmRemoteFunInfos(mFunInfo);
				 }
				
				 supportCount++;
				 }

				index = remote_airconditioner_item.indexOf("2");
				if (index != -1) {
					mFunInfo = new RemoteFunInfo();
					mFunInfo.setId(RemoteFunInfo.MODE_CLOSE);
					mFunInfo.setName("关闭空调");
					mFunInfo.setIcon_id(R.drawable.remote_close_air2);
					mFunInfo.setIcon_id_seleced(R.drawable.icon_close_air_press);
					mFunInfo.setIcon_id_seleced_no(R.drawable.icon_close_air);
					state = RemoteFunInfo.STATE_SUPPORT;
					mFunInfo.setState(state);
//					mFunInfo.setTemperature("--");
					if (state.equals(RemoteFunInfo.STATE_SUPPORT)) {
						mAirMainInfo.addmRemoteFunInfos(mFunInfo);
					}
					supportCountAir++;
				}

				index = remote_airconditioner_item.indexOf("9");
				if (index != -1||remote_airconditioner_item.indexOf("8") != -1) {
					mAirMainInfo.setShowTemp(true);
				} else {
					mAirMainInfo.setShowTemp(false);
				}

				mAirMainInfo.setFunctionCount(supportCountAir + "");

				mRemoteMainInfo.setmAirMainInfo(mAirMainInfo);

			}
		}
	}
	
}

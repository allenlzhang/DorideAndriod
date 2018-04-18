package com.carlt.yema.protocolparser.home;

import com.carlt.yema.R;
import com.carlt.yema.data.BaseResponseInfo;
import com.carlt.yema.data.home.InformationCategoryInfo;
import com.carlt.yema.data.home.InformationCategoryInfoList;
import com.carlt.yema.data.home.InformationMessageInfo;
import com.carlt.yema.protocolparser.BaseParser;
import com.carlt.yema.utils.ILog;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;


public class InformationCentreInfoListParser extends BaseParser<InformationCategoryInfoList> {

	private InformationCategoryInfoList mSecretaryCategoryInfoList;

	public InformationCentreInfoListParser(ResultCallback callback) {
		super(callback);
	}

	@Override
	protected void parser() {
		mSecretaryCategoryInfoList = new InformationCategoryInfoList();
		try {
			JsonObject mJSON_data = mJson.getAsJsonObject("data");
			JsonArray mJSON_list = mJSON_data.getAsJsonArray("list");
			for (int i = 0; i < mJSON_list.size(); i++) {
				InformationCategoryInfo mInfo = new InformationCategoryInfo();
				JsonObject temp = (JsonObject) mJSON_list.get(i);
				mInfo.setName(temp.get("name").getAsString());
				int id = temp.get("id").getAsInt();
				mInfo.setId(id);
				switch (id) {
				case InformationMessageInfo.C1_T1:
					// 11 用车消息
					mInfo.setImg(R.drawable.icon_secretary_car);
					break;
//				case InformationMessageInfo.C1_T2:
//					// 21 安防提醒
////					mInfo.setImg(R.drawable.icon_secretary_safe_2);
//					break;
				case InformationMessageInfo.C1_T3:
					// 31 奖品活动
//					mInfo.setImg(R.drawable.secretary_reward);
					break;
				case InformationMessageInfo.C1_T4:
					// 41 行车消息
					mInfo.setImg(R.drawable.icon_secretary_car_log);
					break;
//				case InformationMessageInfo.C1_T5:
//					// 51 故障提醒
////					mInfo.setImg(R.drawable.secretary_error);
//					break;

				case InformationMessageInfo.C1_T6:
					// 61 保养提醒
					mInfo.setImg(R.drawable.icon_secretary_maintain);
					break;
				case InformationMessageInfo.C1_T7:
					// 62 车辆关怀
					mInfo.setImg(R.drawable.icon_secretary_car_care);
					break;
				}
				String lastmsg = temp.get("lastmsg").getAsString();
				if (lastmsg == null || lastmsg.equals("")) {
					lastmsg = "";
				}
				mInfo.setLastmsg(lastmsg);
				String c = temp.get("msgcount").getAsString();
				mInfo.setMsgcount(c);
				mInfo.setMsgdate(temp.get("msgdate").getAsString());
				mSecretaryCategoryInfoList.addmAllList(mInfo);
			}
			mSecretaryCategoryInfoList.setUnreadCount(mJSON_data.get("unreadmessage").getAsString());
			mBaseResponseInfo.setValue(mSecretaryCategoryInfoList);
		} catch (Exception e) {
			ILog.e(TAG, "--e==" + e);
			mBaseResponseInfo.setFlag(BaseResponseInfo.ERRO);
			mBaseResponseInfo.setInfo(MSG_ERRO);
		}

	}
}

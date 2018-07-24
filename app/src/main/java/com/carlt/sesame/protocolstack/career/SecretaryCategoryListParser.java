package com.carlt.sesame.protocolstack.career;

import com.carlt.sesame.R;
import com.carlt.sesame.data.career.SecretaryCategoryInfo;
import com.carlt.sesame.data.career.SecretaryCategoryInfoList;
import com.carlt.sesame.data.career.SecretaryMessageInfo;
import com.carlt.sesame.protocolstack.BaseParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SecretaryCategoryListParser extends BaseParser {

	private SecretaryCategoryInfoList mSecretaryCategoryInfoList = new SecretaryCategoryInfoList();

	public SecretaryCategoryInfoList getReturn() {
		return mSecretaryCategoryInfoList;
	}

	@Override
	protected void parser() {
		try {

			JSONObject mJSON_data = mJson.getJSONObject("data");
			JSONArray mJSON_list = mJSON_data.getJSONArray("list");
			for (int i = 0; i < mJSON_list.length(); i++) {
				SecretaryCategoryInfo mInfo = new SecretaryCategoryInfo();
				JSONObject temp = (JSONObject) mJSON_list.get(i);
				mInfo.setName(temp.optString("name"));
				int id = temp.optInt("id");
				mInfo.setId(id);
				switch (id) {
				case SecretaryMessageInfo.C1_T1:
					// 11 用车提醒
					mInfo.setImg(R.drawable.icon_secretary_car_1);
					break;
				case SecretaryMessageInfo.C1_T2:
					// 21 安防提醒
					mInfo.setImg(R.drawable.icon_secretary_safe_2);
					break;
				case SecretaryMessageInfo.C1_T3:
					// 31 奖品活动
					mInfo.setImg(R.drawable.secretary_reward);
					break;
				case SecretaryMessageInfo.C1_T4:
					// 41 行车信息
					mInfo.setImg(R.drawable.icon_secretary_car_log_3);
					break;
				case SecretaryMessageInfo.C1_T5:
					// 51 故障提醒
					mInfo.setImg(R.drawable.secretary_error);
					break;

				case SecretaryMessageInfo.C1_T6:
					// 61 养护提醒
					mInfo.setImg(R.drawable.icon_secretary_maintain_0);
					break;
				case SecretaryMessageInfo.C1_T9:
					// 99 官方消息
					mInfo.setImg(R.drawable.icon_secretary_offical_4);
					break;
				}
				String lastmsg = temp.optString("lastmsg");
				if (lastmsg == null || lastmsg.equals("")) {
					lastmsg = "";
				}
				mInfo.setLastmsg(lastmsg);
				int c = temp.optInt("msgcount");
				mInfo.setMsgcount(c);
				mInfo.setMsgdate(temp.optString("msgdate"));
				mSecretaryCategoryInfoList.addmAllList(mInfo);
			}
			mSecretaryCategoryInfoList.setUnreadCount(mJSON_data.optInt("unreadmessage"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}

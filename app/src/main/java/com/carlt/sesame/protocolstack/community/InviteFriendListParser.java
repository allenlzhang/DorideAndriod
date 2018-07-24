package com.carlt.sesame.protocolstack.community;

import android.database.Cursor;
import android.provider.ContactsContract;

import com.carlt.doride.DorideApplication;
import com.carlt.sesame.data.community.InviteFriendInfo;
import com.carlt.sesame.protocolstack.BaseParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class InviteFriendListParser extends BaseParser {
	private ArrayList<InviteFriendInfo> mList = new ArrayList<InviteFriendInfo>();

	public ArrayList<InviteFriendInfo> getReturn() {
		return mList;
	}

	@Override
	protected void parser() {
		try {
			getContactInfo();
			JSONArray mJSON_data = mJson.getJSONArray("data");
			if (mJSON_data.length() > 0) {
				for (int i = 0; i < mJSON_data.length(); i++) {
					JSONObject temp = (JSONObject) mJSON_data.get(i);
					String phoneNum = temp.optString("mobile");
					if (phoneNum != null && phoneNum.length() > 0) {
						for (int j = 0; j < mList.size(); j++) {
							InviteFriendInfo info = mList.get(j);
							ArrayList<String> phoneList = info.getPhoneList();
							if (phoneList != null) {
								for (int k = 0; k < phoneList.size(); k++) {
									String p = phoneList.get(k).replaceAll(" ",
											"");
									if (p.equals(phoneNum)) {
										info.setFriend(true);
										String uid = temp.optString("userid");
										info.setUid(uid);
										break;
									}
								}

							}
						}

					}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	private void getContactInfo() {
		boolean hasPhone;// 用户是否有电话
		// 获得通讯录信息 ，URI是ContactsContract.Contacts.CONTENT_URI
		Cursor cursor = DorideApplication.ApplicationContext.getContentResolver()
				.query(ContactsContract.Contacts.CONTENT_URI, null, null, null,
						null);
		while (cursor.moveToNext()) {
			InviteFriendInfo mInviteFriendInfo = new InviteFriendInfo();

			ArrayList<String> phoneList = new ArrayList<String>();
			String name = "";
			// 获得通讯录中每个联系人的ID
			String contactId = cursor.getString(cursor
					.getColumnIndex(ContactsContract.Contacts._ID));
			// 获得通讯录中联系人的名字
			name = cursor
					.getString(cursor
							.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));

			// 查看给联系人是否有电话，返回结果是String类型，1表示有，0表是没有
			String phoneCode = cursor
					.getString(cursor
							.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
			if (phoneCode.equalsIgnoreCase("1"))
				hasPhone = true;
			else
				hasPhone = false;

			// 如果有电话，根据联系人的ID查找到联系人的电话，电话可以是多个
			if (hasPhone) {
				Cursor phones = DorideApplication.ApplicationContext
						.getContentResolver()
						.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
								null,
								ContactsContract.CommonDataKinds.Phone.CONTACT_ID
										+ " = " + contactId, null, null);
				phones.moveToFirst();
				String phoneNumber = phones
						.getString(phones
								.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
				phoneList.add(phoneNumber);
				while (phones.moveToNext()) {
					phoneNumber = phones
							.getString(phones
									.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
					phoneList.add(phoneNumber);
				}
				phones.close();
			} else {
				phoneList = null;
			}
			mInviteFriendInfo.setName(name);
			mInviteFriendInfo.setPhoneList(phoneList);
			mInviteFriendInfo.setFriend(false);
			mList.add(mInviteFriendInfo);
		}
		cursor.close();
	}

}

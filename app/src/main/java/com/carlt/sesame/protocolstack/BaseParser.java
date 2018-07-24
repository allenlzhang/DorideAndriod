
package com.carlt.sesame.protocolstack;

import com.carlt.sesame.control.ActivityControl;
import com.carlt.sesame.control.CPControl;
import com.carlt.sesame.control.CPControl.GetResultListCallback;
import com.carlt.sesame.control.DaoControl;
import com.carlt.sesame.data.BaseResponseInfo;
import com.carlt.sesame.data.UseInfo;
import com.carlt.sesame.data.career.ChallengeInfo;
import com.carlt.sesame.data.career.LicenceLevelInfo;
import com.carlt.sesame.data.career.MedalInfo;
import com.carlt.sesame.data.community.FriendFeedDetialInfo;
import com.carlt.sesame.data.community.MyFriendInfo;
import com.carlt.sesame.data.community.ShareItemInfo;
import com.carlt.sesame.http.HttpImgPostor;
import com.carlt.sesame.http.HttpPostor;
import com.carlt.sesame.preference.UseInfoLocal;
import com.carlt.sesame.utility.Log;

import org.apache.http.entity.mime.content.FileBody;
import org.json.JSONObject;

import java.io.File;

public abstract class BaseParser {
	public final static String MSG_ERRO = "网络不稳定，请稍后再试";

	protected BaseResponseInfo mBaseResponseInfo = new BaseResponseInfo();

	protected JSONObject mJson;

	private String mResult;

	public String getmResult() {
		return mResult;
	}

	public BaseResponseInfo getBaseResponseInfo(String url, String post) {
		HttpPostor mHttpPostor = new HttpPostor();
		if (!mHttpPostor.connect(url, post)) {
			int c = mHttpPostor.getCode();
			if (c == 0) {
				mBaseResponseInfo.setInfo(MSG_ERRO);
			} else {
				mBaseResponseInfo.setInfo(MSG_ERRO + c);
			}

			return mBaseResponseInfo;
		}

		try {
			String s1 = mHttpPostor.getResult();
			Log.e("http", "Http响应--" + s1);
			mJson = new JSONObject(s1);
			mBaseResponseInfo.setFlag(mJson.getString("code"));
			mBaseResponseInfo.setInfo(mJson.getString("msg"));
			//测试代码
            // if(mJson.getString("request").equals("/120/carRelated/remoteIndex")||mJson.getString("request").equals("/120/user/checkIsUpgrade")){
            // mBaseResponseInfo.setFlag(1020);
            // }
			//测试代码结束
		} catch (Exception e) {
			Log.e("http", "BaseParser--e==" + e);
		}
		if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
			try {
				parser();
			} catch (Exception e) {
				Log.e("http", "解析错误--e==" + e);
				Log.e("http", "解析错误--e==" + e);
				Log.e("http", "解析错误--e==" + e);
				Log.e("http", "解析错误--e==" + e);
			}

		} else if (mBaseResponseInfo.getFlag() == BaseResponseInfo.NO_TOKEN) {
			Log.e("info", "mBaseResponseInfo.getFlag()==" + mBaseResponseInfo.getFlag());
			ActivityControl.onTokenDisable();
			for (int i = 0; i < 100; i++) {
				Log.e("info", "延长处理时间--"+i);
			}
		} else if (mBaseResponseInfo.getFlag() == BaseResponseInfo.TOKEN_DISABLE) {
			Log.e("info", "TOKEN DISABLE mBaseResponseInfo.getFlag()==" + mBaseResponseInfo.getFlag());
			Log.e("info", "TOKEN DISABLE mBaseResponseInfo.getFlag()========NOTOKEN");
			mBaseResponseInfo.setInfo(null);
			UseInfo mUseInfo = UseInfoLocal.getUseInfo();
			String account = mUseInfo.getAccount();
			String password = mUseInfo.getPassword();
			if (account != null && account.length() > 0 && password != null && password.length() > 0) {
				CPControl.GetToken(new GetResultListCallback() {

					@Override
					public void onFinished(Object o) {
						Log.e("info", "TOKEN DISABLE mBaseResponseInfo.getFlag()========NOTOKEN  GET   FINISHED");
					}

					@Override
					public void onErro(Object o) {
						// 跳蹬陆
						Log.e("info", "TOKEN DISABLE mBaseResponseInfo.getFlag()========NOTOKEN  GET   ERROR");
						ActivityControl.onTokenDisable();
						for (int i = 0; i < 100; i++) {
							Log.e("info", "延长处理时间--"+i);
						}
					}
				}, true);
			} else {
				Log.e("info", "TOKEN DISABLE mBaseResponseInfo.getFlag()========NOTOKEN  NO ACCOUNT");
				ActivityControl.onTokenDisable();
				for (int i = 0; i < 100; i++) {
					Log.e("info", "延长处理时间--"+i);
				}
			}
		}
		return mBaseResponseInfo;

	}

	/**
	 * 调用第三方接口，将调用成功与否的结果放在parser方法中
	 * 
	 * @param url
	 * @param post
	 * @return
	 */
	public BaseResponseInfo getOtherBaseResponseInfo(String url, String post) {
		HttpPostor mHttpPostor = new HttpPostor();
		if (!mHttpPostor.connect(url, post)) {
			int c = mHttpPostor.getCode();
			if (c == 0) {
				mBaseResponseInfo.setInfo(MSG_ERRO);
			} else {
				mBaseResponseInfo.setInfo(MSG_ERRO + c);
			}

			return mBaseResponseInfo;
		}

		try {
			mResult = mHttpPostor.getResult();
			mJson = new JSONObject(mResult);
		} catch (Exception e) {
			Log.e("http", "BaseParser--e==" + e);
		}

		try {
			parser();
		} catch (Exception e) {
			Log.e("http", "解析错误--e==" + e);
			Log.e("http", "解析错误--e==" + e);
			Log.e("http", "解析错误--e==" + e);
			Log.e("http", "解析错误--e==" + e);
		}

		return mBaseResponseInfo;

	}

	public BaseResponseInfo getOtherBaseResponseInfo_get(String url, String post) {
		HttpPostor mHttpPostor = new HttpPostor();
		if (!mHttpPostor.connect_get(url, post)) {
			int c = mHttpPostor.getCode();
			if (c == 0) {
				mBaseResponseInfo.setInfo(MSG_ERRO);
			} else {
				mBaseResponseInfo.setInfo(MSG_ERRO + c);
			}

			return mBaseResponseInfo;
		}

		try {
			mResult = mHttpPostor.getResult();
			mJson = new JSONObject(mResult);
		} catch (Exception e) {
			Log.e("http", "BaseParser--e==" + e);
		}

		try {
			parser();
		} catch (Exception e) {
			Log.e("http", "解析错误--e==" + e);
			Log.e("http", "解析错误--e==" + e);
			Log.e("http", "解析错误--e==" + e);
			Log.e("http", "解析错误--e==" + e);
		}

		return mBaseResponseInfo;

	}

	public BaseResponseInfo getBaseResponseInfo(String url, String action, File mFile) {

		HttpImgPostor mHttpPostor = new HttpImgPostor();
		FileBody fileBody = new FileBody(mFile);
		if (!mHttpPostor.connect(url, action, fileBody)) {
			mBaseResponseInfo.setInfo(MSG_ERRO);
			return mBaseResponseInfo;
		}

		try {
			String s1 = mHttpPostor.getResult();
			Log.e("http", "Http响应--" + s1);
			mJson = new JSONObject(s1);
			mBaseResponseInfo.setFlag(mJson.getString("code"));
			mBaseResponseInfo.setInfo(mJson.getString("msg"));
		} catch (Exception e) {
			Log.e("http", "BaseParser--e==" + e);
		}
		if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
			try {
				parser();
			} catch (Exception e) {
				Log.e("http", "解析错误--e==" + e);
			}

		} else if (mBaseResponseInfo.getFlag() == BaseResponseInfo.NO_TOKEN) {
			ActivityControl.onTokenDisable();
		}
		return mBaseResponseInfo;

	}

	protected abstract void parser() throws Exception;

	public Object getReturn() {
		return null;
	}

	protected FriendFeedDetialInfo GenerateShareItemInfo(DaoControl mDaoControl, JSONObject temp) {

		FriendFeedDetialInfo info = new FriendFeedDetialInfo();
		MyFriendInfo mMyFriendInfo = new MyFriendInfo();
		mMyFriendInfo.setUsername(temp.optString("username"));
		mMyFriendInfo.setRealname(temp.optString("realname"));
		mMyFriendInfo.setGender(temp.optInt("gender"));
		mMyFriendInfo.setImg(temp.optString("avatar_img"));
		mMyFriendInfo.setLicencename(temp.optString("licencename"));
		mMyFriendInfo.setCarlogo(temp.optString("carlogo"));
		mMyFriendInfo.setId(temp.optString("userid"));
		info.setmMyFriendInfo(mMyFriendInfo);

		info.setId(temp.optString("id"));

		info.setContent(temp.optString("content"));
		info.setPoketimes(temp.optString("poketimes"));
		info.setFeedtime(temp.optString("feedtime"));
		info.setPokedUserStr(temp.optString("pokedUserStr"));

		if (temp.optInt("ispoked") == 0) {
			info.setIspoked(false);
		} else {
			info.setIspoked(true);
		}

		int type = temp.optInt("type");
		info.setType(type);
		switch (type) {
		case ShareItemInfo.TYPE_LICENCE:
			String licenceLevelId = temp.optString("licencelevelid");
			info.setLicenceLevelId(licenceLevelId);
			LicenceLevelInfo mLicenceLevelInfo = mDaoControl.getLicenceLevelById(licenceLevelId);
			if (mLicenceLevelInfo != null) {
				info.setImgDesc(mLicenceLevelInfo.getName());
				info.setImgUrl(mLicenceLevelInfo.getIconUrl2());
			}
			break;
		case ShareItemInfo.TYPE_MEDAL:
			String medalid = temp.optString("medalid");
			info.setMedalid(medalid);
			MedalInfo mMedalInfo = mDaoControl.getMedalById(medalid);
			info.setImgDesc(mMedalInfo.getName());
			info.setImgUrl(mMedalInfo.getIconUrl2());

			break;
		case ShareItemInfo.TYPE_RECORD:
			info.setRecordName(temp.optString("recordname"));
			info.setRecordOldvalue(temp.optString("oldvalue"));
			info.setRecordNewvalue(temp.optString("newvalue"));
			info.setRecordUnit(temp.optString("unit"));

			break;
		case ShareItemInfo.TYPE_CHALLENGE:
			String challengeid = temp.optString("challengeid");
			ChallengeInfo mChallengeInfo = mDaoControl.getChallengeById(challengeid);
			info.setmChallengeInfo(mChallengeInfo);
			break;
		case ShareItemInfo.TYPE_SHARE:
			info.setShareText(temp.optString("sharetext"));
			info.setLink(temp.optString("sharelink"));

			break;
		case ShareItemInfo.TYPE_LICENCE_SHARE:
			String licenceLevelId1 = temp.optString("licencelevelid");
			info.setLicenceLevelId(licenceLevelId1);
			LicenceLevelInfo mLicenceLevelInfo1 = mDaoControl.getLicenceLevelById(licenceLevelId1);
			if (mLicenceLevelInfo1 != null) {
				info.setImgDesc(mLicenceLevelInfo1.getName());
				info.setImgUrl(mLicenceLevelInfo1.getIconUrl2());
			}

			break;

		}
		return info;
	}

	public JSONObject getmJson() {
		return mJson;
	}


}

package com.carlt.yema.push;

import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.carlt.yema.MainActivity;
import com.carlt.yema.control.ActivityControl;
import com.carlt.yema.data.BaseResponseInfo;
import com.carlt.yema.data.UseInfo;
import com.carlt.yema.data.car.SecretaryMessageInfo;
import com.carlt.yema.model.LoginInfo;
import com.carlt.yema.preference.UseInfoLocal;
import com.carlt.yema.protocolparser.BaseParser;
import com.carlt.yema.ui.activity.home.RemindActivity;

public class PushService extends Service {

	public final static String CLASS1 = "class1";

	@Override
	public IBinder onBind(Intent intent) {
		Log.e("info", "onBind");
		return null;
	}

	@Override
	public void onCreate() {
		Log.e("info", "onCreate");
		super.onCreate();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		if (intent == null) {
			// swithAction(0);
			return;
		}
		final int class1 = intent.getIntExtra(CLASS1, 0);
		if (LoginInfo.getAccess_token() != null && LoginInfo.getAccess_token().length() > 0) {
			swithAction(class1);
		} else {

			UseInfo mUseInfo = UseInfoLocal.getUseInfo();
			String account = mUseInfo.getAccount();
			String password = mUseInfo.getPassword();
			if (account != null && account.length() > 0 && password != null
					&& password.length() > 0) {
				// 直接调用登录接口
				final Handler mHandler = new Handler() {

					@Override
					public void handleMessage(Message msg) {
						switch (msg.what) {
						case 0:
							swithAction(class1);
							ActivityControl.initXG();
							break;

						}
					}

				};
				BaseParser.ResultCallback listener = new BaseParser.ResultCallback() {

					@Override
					public void onSuccess(BaseResponseInfo bInfo) {
						mHandler.sendEmptyMessage(0);
					}

					@Override
					public void onError(BaseResponseInfo bInfo) {
						mHandler.sendEmptyMessage(1);
					}
				};
				//TODO 登录接口
//				CPControl.GetLogin(account, password, listener);

			}
		}
		super.onStart(intent, startId);
	}

	private void swithAction(int class1) {
		Intent i = new Intent(PushService.this, RemindActivity.class);
		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_NEW_TASK);
		i.putExtra(RemindActivity.TIPS_TYPE, class1);

		Log.e("info", "swithAction-class1==" + class1);
		switch (class1) {
		case LoginInfo.Error_Message:
			Activity activity = ActivityControl.getTopActivity();
			if (activity != null) {
				Intent mIntentError = new Intent(Intent.ACTION_MAIN);
				mIntentError.addCategory(Intent.CATEGORY_LAUNCHER);
				mIntentError.setComponent(new ComponentName(this
						.getPackageName(), activity.getClass()
						.getCanonicalName()));
				mIntentError.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
						| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
				startActivity(mIntentError);
			}
			return;
		case LoginInfo.Author_Message:
			if (LoginInfo.isMain()) {
				// 主机-跳转至授权页面
//				UUToast.showUUToast();
//				Intent mIntent = new Intent(PushService.this,
//						AuthorActivity.class);
//				mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
//						| Intent.FLAG_ACTIVITY_NEW_TASK);
//				startActivity(mIntent);
				Log.e("info", "跳转至授权页面");
			} else {
				// 子机-跳转至主页面
				Intent mIntent = new Intent(PushService.this,
						MainActivity.class);
				mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
						| Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(mIntent);
			}
			return;
		case LoginInfo.Feetip_Message:
			// 服务提醒
			//if (LoginInfo.isMain()) {
				// 主机-跳转至服务购买
//				Intent mIntent = new Intent(PushService.this,
//						ManageFeeActivity.class);
//				mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
//						| Intent.FLAG_ACTIVITY_NEW_TASK);
//				startActivity(mIntent);
			Log.e("info", "跳转至服务购买页面");
//			} else {
//				// 子机-跳转至主页面
//				Intent mIntent = new Intent(PushService.this,
//						MainActivity.class);
//				mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
//						| Intent.FLAG_ACTIVITY_NEW_TASK);
//				startActivity(mIntent);
//			}
			return;

		case SecretaryMessageInfo.C1_T1:
			// 11 用车提醒
			i.putExtra(RemindActivity.TIPS_TITLE, "用车提醒");
			break;
		case SecretaryMessageInfo.C1_T2:
			// 21 安防故障
			i.putExtra(RemindActivity.TIPS_TITLE, "安防提醒");
			break;
		case SecretaryMessageInfo.C1_T3:
			// 31 奖品活动
			i.putExtra(RemindActivity.TIPS_TITLE, "奖品活动");
			break;
		case SecretaryMessageInfo.C1_T4:
			// 41 行车信息
			i.putExtra(RemindActivity.TIPS_TITLE, "行车信息");
			break;
		case SecretaryMessageInfo.C1_T5:
			// 51 故障提醒
			i.putExtra(RemindActivity.TIPS_TITLE, "故障提醒");
			break;
		case SecretaryMessageInfo.C1_T9:
			// 99 官方消息
			i.putExtra(RemindActivity.TIPS_TITLE, "官方消息");
			break;
		}
		startActivity(i);
	}

}

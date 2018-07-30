package com.carlt.sesame.push;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import com.carlt.sesame.control.ActivityControl;
import com.carlt.sesame.control.CPControl;
import com.carlt.sesame.control.CPControl.GetResultListCallback;
import com.carlt.sesame.data.SesameLoginInfo;
import com.carlt.sesame.data.UseInfo;
import com.carlt.sesame.data.career.SecretaryMessageInfo;
import com.carlt.sesame.data.safety.AuthorInfo;
import com.carlt.sesame.preference.UseInfoLocal;
import com.carlt.sesame.ui.SesameMainActivity;
import com.carlt.sesame.ui.activity.career.SecretaryTipsActivity;
import com.carlt.sesame.ui.activity.safety.AuthorActivity;
import com.carlt.sesame.utility.Log;

public class PushService extends Service {

	public final static String CLASS1 = "class1";
	public final static String CLASS2 = "class2";

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
		final int class2 = intent.getIntExtra(CLASS2, 0);
		if (SesameLoginInfo.getToken() != null && SesameLoginInfo.getToken().length() > 0) {
			swithAction(class1, class2);
		} else {

			UseInfo mUseInfo = UseInfoLocal.getUseInfo();
			String account = mUseInfo.getAccount();
			String password = mUseInfo.getPassword();
			if (account != null && account.length() > 0 && password != null
					&& password.length() > 0) {
				// 直接调用登录接口
				@SuppressLint("HandlerLeak") final Handler mHandler = new Handler() {

					@Override
					public void handleMessage(Message msg) {
						switch (msg.what) {
						case 0:
							swithAction(class1, class2);
							ActivityControl.initXG();
							break;

						}
					}

				};
				GetResultListCallback listener = new GetResultListCallback() {

					@Override
					public void onFinished(Object o) {
						// TODO Auto-generated method stub
						mHandler.sendEmptyMessage(0);
					}

					@Override
					public void onErro(Object o) {
						mHandler.sendEmptyMessage(1);

					}
				};
				CPControl.GetLogin(account, password, listener);

			}
			// 没有保存用户名和密码
			// else {
			// Intent login = new Intent(context, LoginActivity.class);
			// showNotification(context, login, title);
			// }
		}
		super.onStart(intent, startId);
	}

	private void swithAction(int class1, int class2) {
		Intent i = new Intent(PushService.this, SecretaryTipsActivity.class);

		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_NEW_TASK);
		i.putExtra(SecretaryTipsActivity.TIPS_TYPE, class1);

		Log.e("push", "swithAction-class1==" + class1);
		switch (class1) {
		case AuthorInfo.Error_Message:
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
		case AuthorInfo.REQUEST_AUTHOR:
			if (SesameLoginInfo.isMain()) {
				// 主机-跳转至授权页面
				Intent mIntent = new Intent(PushService.this,
						AuthorActivity.class);
				mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
						| Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(mIntent);
			} else {
				// 子机-跳转至主页面
				Intent mIntent = new Intent(PushService.this,
						SesameMainActivity.class);
				mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
						| Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(mIntent);
			}
			return;
		case SesameLoginInfo.Feetip_Message:
			// 服务提醒
			if (SesameLoginInfo.isMain()) {
				// 主机-跳转至服务购买
//				Intent mIntent = new Intent(PushService.this,
//						ManageFeeActivity.class);
//				mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
//						| Intent.FLAG_ACTIVITY_NEW_TASK);
//				startActivity(mIntent);
			} else {
				// 子机-跳转至主页面
				Intent mIntent = new Intent(PushService.this,
						SesameMainActivity.class);
				mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
						| Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(mIntent);
			}
			return;

		case SecretaryMessageInfo.C1_T1:
			// 11 用车提醒
			Log.e("push", "class1=="+class1);
			Log.e("push", "class2=="+class2);
			if (class2 == SecretaryMessageInfo.C1_T1_T6) {
				// 充电成功
				Intent mIntent = new Intent(PushService.this,
						SesameMainActivity.class);
				mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
						| Intent.FLAG_ACTIVITY_NEW_TASK);
				mIntent.putExtra(SesameMainActivity.CURRENT_TAB, 0);
				startActivity(mIntent);
				return;
			} else if (class2 == SecretaryMessageInfo.C1_T1_T7) {
				// 充电失败
				Intent mIntent = new Intent(PushService.this,
						SesameMainActivity.class);
				mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
						| Intent.FLAG_ACTIVITY_NEW_TASK);
				mIntent.putExtra(SesameMainActivity.CURRENT_TAB, 2);
				startActivity(mIntent);
				return;
			} else {
				i.putExtra(SecretaryTipsActivity.TIPS_TITLE, "用车提醒");
			}
			break;
		case SecretaryMessageInfo.C1_T2:
			// 21 安防故障
			i.putExtra(SecretaryTipsActivity.TIPS_TITLE, "安防提醒");
			break;
		case SecretaryMessageInfo.C1_T3:
			// 31 奖品活动
			i.putExtra(SecretaryTipsActivity.TIPS_TITLE, "奖品活动");
			break;
		case SecretaryMessageInfo.C1_T4:
			// 41 行车信息
			i.putExtra(SecretaryTipsActivity.TIPS_TITLE, "行车信息");
			break;
		case SecretaryMessageInfo.C1_T5:
			// 51 故障提醒
			i.putExtra(SecretaryTipsActivity.TIPS_TITLE, "故障提醒");
			break;
		case SecretaryMessageInfo.C1_T9:
			// 99 官方消息
			i.putExtra(SecretaryTipsActivity.TIPS_TITLE, "官方消息");
			break;
		}
		startActivity(i);
	}

}

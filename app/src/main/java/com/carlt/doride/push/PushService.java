package com.carlt.doride.push;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.carlt.doride.MainActivity;
import com.carlt.doride.control.ActivityControl;
import com.carlt.doride.data.BaseResponseInfo;
import com.carlt.doride.data.UseInfo;
import com.carlt.doride.data.car.SecretaryMessageInfo;
import com.carlt.doride.model.LoginInfo;
import com.carlt.doride.preference.UseInfoLocal;
import com.carlt.doride.protocolparser.BaseParser;
import com.carlt.doride.ui.activity.home.RemindActivity;
import com.carlt.sesame.preference.TokenInfo;

public class PushService extends Service {
    public static final int Error_Message = 0;// 设备出现异常的推送

    public static final int Feetip_Message = 2;// 续费提醒推送
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
        if (TokenInfo.getToken() != null && TokenInfo.getToken().length() > 0) {
            swithAction(class1);
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
            case PushService.Error_Message:
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
            case PushService.Feetip_Message:
                // 服务提醒
                Log.e("info", "跳转至服务购买页面");
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

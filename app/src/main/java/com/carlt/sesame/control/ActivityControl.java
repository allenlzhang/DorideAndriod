package com.carlt.sesame.control;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.blankj.utilcode.util.SPUtils;
import com.carlt.doride.DorideApplication;
import com.carlt.doride.R;
import com.carlt.doride.ui.activity.login.UserLoginActivity;
import com.carlt.sesame.data.SesameLoginInfo;
import com.carlt.sesame.data.UseInfo;
import com.carlt.sesame.preference.TokenInfo;
import com.carlt.sesame.preference.UseInfoLocal;
import com.carlt.sesame.systemconfig.URLConfig;
import com.carlt.sesame.ui.activity.base.BaseActivity;
import com.carlt.sesame.ui.view.PopBoxCreat;
import com.carlt.sesame.ui.view.PopBoxCreat.DialogWithTitleClick;
import com.carlt.sesame.utility.LoginChecker;
import com.tencent.android.tpush.XGBasicPushNotificationBuilder;
import com.tencent.android.tpush.XGPushConfig;
import com.tencent.android.tpush.XGPushManager;
import com.tencent.android.tpush.service.XGPushServiceV3;

import java.util.ArrayList;
import java.util.List;

public class ActivityControl {

    private static List<Activity> mActivityList = new ArrayList<Activity>();

    public static void addActivity(Activity activity) {
        if (null != activity) {
            mActivityList.add(activity);
        }
    }

    public static boolean anyActivtyShowing() {
        for (int i = 0; i < mActivityList.size(); i++) {
            BaseActivity ba = (BaseActivity) mActivityList.get(i);
            if (ba != null && !ba.isFinishing() && ba.IsShowing()) {
                return true;
            }
        }
        return false;
    }

    public static void removeActivity(Activity activity) {
        if (null != activity) {
            mActivityList.remove(activity);
        }
    }

    public static Activity getTopActivity() {
        if (mActivityList != null) {
            int size = mActivityList.size();
            return mActivityList.get(size - 1);
        }
        return null;
    }

    /**
     * 信鸽注册（改方法在登录成功后调用）
     */
//    public static void initXG() {
//
//        Context mContext = DorideApplication.getAppContext();
//        // 新建自定义样式
//        XGBasicPushNotificationBuilder build = new XGBasicPushNotificationBuilder();
//        // 设置自定义样式属性，该属性对对应的编号生效，指定后不能修改。
//        build.setIcon(R.drawable.ic_launcher);
//        // 设置声音
//        build.setSound(RingtoneManager.getActualDefaultRingtoneUri(mContext,
//                RingtoneManager.TYPE_ALARM));
//        // 振动
//        build.setDefaults(Notification.DEFAULT_VIBRATE);
//        // 是否可清除
//        build.setFlags(Notification.FLAG_AUTO_CANCEL);
//
//        XGPushConfig.enableDebug(mContext, true);
//        Log.e("info", "userId====" + SesameLoginInfo.getUseId());
//        if (DorideApplication.Formal_Version) {
//            XGPushManager.registerPush(mContext, SesameLoginInfo.getUseId());
//            // 设置通知样式，样式编号为2，即build_id为2，可通过后台脚本指定
//            XGPushManager.setPushNotificationBuilder(mContext, 2, build);
//            XGPushManager.setTag(mContext, SesameLoginInfo.getDealerId());
//            if (SesameLoginInfo.getPush_prizeinfo_flag() == 1) {
//                XGPushManager.setTag(mContext, SesameLoginInfo.getDealerId() + "_31");
//            }
//        } else {
//            switch (URLConfig.flag) {
//                case URLConfig.VERSION_FORMAL:
//                    //正式服
//                    XGPushManager.registerPush(mContext, SesameLoginInfo.getUseId());
//                    XGPushManager.setPushNotificationBuilder(mContext, 2, build);
//                    XGPushManager.setTag(mContext, SesameLoginInfo.getDealerId());
//                    if (SesameLoginInfo.getPush_prizeinfo_flag() == 1) {
//                        XGPushManager.setTag(mContext, SesameLoginInfo.getDealerId() + "_31");
//                    }
//                    break;
//
//                case URLConfig.VERSION_PREPARE:
//                    //预发布服
//                    XGPushManager.registerPush(mContext, SesameLoginInfo.getUseId());
//                    XGPushManager.setPushNotificationBuilder(mContext, 2, build);
//                    XGPushManager.setTag(mContext, SesameLoginInfo.getDealerId());
//                    if (SesameLoginInfo.getPush_prizeinfo_flag() == 1) {
//                        XGPushManager.setTag(mContext, SesameLoginInfo.getDealerId() + "_31");
//                    }
//                    break;
//                case URLConfig.VERSION_TEST:
//                    //测试服
//
//                    XGPushManager.registerPush(mContext, "t_" + SesameLoginInfo.getUseId());
//                    // XGPushManager.registerPush(this, "t_" +
//                    // LoginInfo.getToken());
//                    Log.e("info", "LoginInfo.getToken()==" + SesameLoginInfo.getToken());
//
//                    XGPushManager.setPushNotificationBuilder(mContext, 2, build);
//                    XGPushManager.setTag(mContext, "t_" + SesameLoginInfo.getDealerId());
//                    if (SesameLoginInfo.getPush_prizeinfo_flag() == 1) {
//                        XGPushManager.setTag(mContext, "t_" + SesameLoginInfo.getDealerId() + "_31");
//                    }
//
//                    break;
//            }
//        }
//        Intent service = new Intent(mContext, XGPushServiceV3.class);
//        mContext.startService(service);
//    }

    public static void exit(Context context) {

        DialogWithTitleClick click = new DialogWithTitleClick() {

            @Override
            public void onRightClick() {
                // 取消

            }

            @Override
            public void onLeftClick() {
                // 退出
                onExit();
                System.exit(0);
            }
        };
        PopBoxCreat.createDialogWithTitle(context, "提示", "是否要退出?", "", "确定", "取消", click);
    }

    public static void logout(final Context context) {
        DialogWithTitleClick click = new DialogWithTitleClick() {

            @Override
            public void onRightClick() {
                // 取消

            }

            @Override
            public void onLeftClick() {
                // 注销
                onLogout(context);
            }
        };
        PopBoxCreat.createDialogWithTitle(context, "提示", "是否要注销?", "", "确定", "取消", click);

    }

    // 注销操作
    public static void onLogout(Context context) {
        onExit();
        //XGPushManager.unregisterPush(context);//注销会产生Bug，故120版本取消
        UseInfo mUseInfo = UseInfoLocal.getUseInfo();
        mUseInfo.setPassword("");
        UseInfoLocal.setUseInfo(mUseInfo);
        TokenInfo.setToken("");
        Intent mIntent = new Intent(context, UserLoginActivity.class);
        mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(mIntent);
        SPUtils.getInstance().clear();

    }

    /******** 退出操作 ****************/
    public static void onExit() {
        TokenInfo.setToken("");
        int size = mActivityList.size();
        for (int i = 0; i < size; i++) {
            if (null != mActivityList.get(i)) {
                mActivityList.get(i).finish();
            }
        }
        mActivityList.clear();
        DorideApplication.getInstance().setShowDragFlag(false);
        SesameLoginInfo.Destroy();
        LoginChecker.stopCheck();
    }

    public static void onTokenDisable() {
        for (Activity activity : mActivityList) {
            if (activity instanceof UserLoginActivity) {
                BaseActivity base = (BaseActivity) activity;
                if (base.IsShowing()) {
                    return;
                }
            }
        }
        handler.sendEmptyMessage(1);
    }

    @SuppressLint("HandlerLeak")
    static Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                Intent mIntent = new Intent(DorideApplication.getAppContext(), UserLoginActivity.class);
                mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                DorideApplication.getAppContext().startActivity(mIntent);
            }
        }
    };
}

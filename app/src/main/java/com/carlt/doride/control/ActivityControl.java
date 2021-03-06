package com.carlt.doride.control;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;

import com.blankj.utilcode.util.LogUtils;
import com.carlt.doride.DorideApplication;
import com.carlt.doride.R;
import com.carlt.doride.base.BaseActivity;
import com.carlt.doride.data.BaseResponseInfo;
import com.carlt.doride.data.UseInfo;
import com.carlt.doride.http.retrofitnet.model.CarConfigRes;
import com.carlt.doride.http.retrofitnet.model.ContactsInfo;
import com.carlt.doride.http.retrofitnet.model.GetCarInfo;
import com.carlt.doride.http.retrofitnet.model.OtherInfo;
import com.carlt.doride.http.retrofitnet.model.UserInfo;
import com.carlt.doride.preference.UseInfoLocal;
import com.carlt.doride.protocolparser.BaseParser;
import com.carlt.doride.systemconfig.URLConfig;
import com.carlt.doride.ui.activity.login.UserLoginActivity;
import com.carlt.doride.ui.activity.setting.CarModeListActivity;
import com.carlt.doride.ui.activity.setting.CarTypeListActivity;
import com.carlt.doride.ui.view.PopBoxCreat;
import com.carlt.doride.ui.view.PopBoxCreat.DialogWithTitleClick;
import com.carlt.doride.utils.SharepUtil;
import com.carlt.sesame.preference.TokenInfo;
import com.orhanobut.logger.Logger;
import com.tencent.android.tpush.XGBasicPushNotificationBuilder;
import com.tencent.android.tpush.XGPushConfig;
import com.tencent.android.tpush.XGPushManager;
import com.tencent.android.tpush.service.XGPushServiceV3;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * 管理全局activity
 */
public class ActivityControl {

    public static List<Activity> mActivityList = new ArrayList<>();
    private static UserInfo userInfo = UserInfo.getInstance();
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

    public static void removeAllActivity() {
        for (Activity activity : mActivityList) {
            activity.finish();
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
     * 信鸽注册（该方法在登录成功后调用）
     */
    public static void initXG() {
        Logger.e("---信鸽注册");
        Context mContext = DorideApplication.getInstanse();
        // 新建自定义样式
        XGBasicPushNotificationBuilder build = new XGBasicPushNotificationBuilder();
        // 设置自定义样式属性，该属性对对应的编号生效，指定后不能修改。
        build.setIcon(R.mipmap.ic_launcher);
        // 设置声音
        build.setSound(RingtoneManager.getActualDefaultRingtoneUri(mContext,
                RingtoneManager.TYPE_ALARM));
        // 振动
        build.setDefaults(Notification.DEFAULT_VIBRATE);
        // 是否可清除
        build.setFlags(Notification.FLAG_AUTO_CANCEL);
        String useId;
        String dealerId;
        if (GetCarInfo.getInstance().carType == 1) {
            useId = userInfo.id+"";
            dealerId = userInfo.dealerId+"";
        } else {
            useId = userInfo.id+"";
            dealerId = userInfo.dealerId+"";
        }
        XGPushConfig.enableDebug(mContext, true);
        Log.e("info", "userId====" + useId);
        if (DorideApplication.Formal_Version) {
            XGPushManager.registerPush(mContext, useId);
            // 设置通知样式，样式编号为2，即build_id为2，可通过后台脚本指定
            XGPushManager.setPushNotificationBuilder(mContext, 2, build);
            XGPushManager.setTag(mContext, dealerId);
                XGPushManager.setTag(mContext, dealerId + "_31");
        } else {
            switch (URLConfig.flag) {
                case URLConfig.VERSION_FORMAL:
                    // 正式服
                    XGPushManager.registerPush(mContext, useId);
                    XGPushManager.setPushNotificationBuilder(mContext, 2, build);
                    XGPushManager.setTag(mContext, dealerId);
                        XGPushManager.setTag(mContext, dealerId
                                + "_31");
                    break;
                case URLConfig.VERSION_PREPARE:
                    // 预发布服
                    XGPushManager.registerPush(mContext, useId);
                    XGPushManager.setPushNotificationBuilder(mContext, 2, build);
                    XGPushManager.setTag(mContext, dealerId);
                        XGPushManager.setTag(mContext, dealerId
                                + "_31");
                    break;
                case URLConfig.VERSION_TEST:
                    // 测试服
                    XGPushManager.registerPush(mContext, "t_" + useId);
                    XGPushManager.setPushNotificationBuilder(mContext, 2, build);
                    XGPushManager.setTag(mContext, "t_" + dealerId);
                        XGPushManager.setTag(mContext,
                                "t_" + dealerId + "_31");
                    break;
            }
        }
        Intent service = new Intent(mContext, XGPushServiceV3.class);
        mContext.startService(service);
    }

    public static void exit(Context context) {

        DialogWithTitleClick click = new DialogWithTitleClick() {

            @Override
            public void onRightClick() {
                // 取消

            }

            @Override
            public void onLeftClick() {
                // 退出
                GetCarInfo.getInstance().initCarInfo();
                SharepUtil.cleanKey(URLConfig.CAR_INFO);
                OtherInfo.getInstance().initInfo();
                onExit();
                System.exit(0);
            }
        };
        //		PopBoxCreat.createDialogNotitle(context, "提示", "是否要退出?", "", "确定",
        //				"取消", click);
        PopBoxCreat.createDialogNotitle(context, "温馨提示", "是否要退出?", "确定", "取消", click);
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
                UserInfo.getInstance().remotePwdSwitch = 0;
                onLogout(context);
            }
        };
        //        PopBoxCreat.createDialogWithTitle(context, "提示", "是否要注销?", "", "确定",
        //                "取消", click);
        PopBoxCreat.createDialogNotitle(context, "温馨提示", "是否要注销?", "确定", "取消", click);
    }

    // 注销操作
    public static void onLogout(Context context) {
        onExit();
        // XGPushManager.unregisterPush(context);//使用反注册会出现Bug,只要调用后台的注销信鸽token的接口就行了。v1.4.0版本把这一行注释掉
        UseInfo mUseInfo = UseInfoLocal.getUseInfo();
        mUseInfo.setPassword("");
        UseInfoLocal.setUseInfo(mUseInfo);
        //		DorideApplication.TOKEN = "";
        TokenInfo.setToken("");

        UserInfo.getInstance().initUserInfo();
        GetCarInfo.getInstance().initCarInfo();
        SharepUtil.cleanAllKey();
        OtherInfo.getInstance().initInfo();
        ContactsInfo.getInstance().initContactsInfo();
        CarConfigRes.getInstance().initCarConfigRes();
        LogUtils.e(GetCarInfo.getInstance().vin);
        Intent mIntent = new Intent(context, UserLoginActivity.class);
        //		mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(mIntent);


    }

    /******** 退出操作 ****************/
    public static void onExit() {
        if (!TextUtils.isEmpty(TokenInfo.getToken())) {
            CPControl.GetUnRigisterXgTokenResult(DorideApplication.NIMEI,
                    new BaseParser.ResultCallback() {
                        @Override
                        public void onSuccess(BaseResponseInfo bInfo) {
                            //							DorideApplication.TOKEN = "";
                            Log.e("info", "注销信鸽成功");
                        }

                        @Override
                        public void onError(BaseResponseInfo bInfo) {
                            //							DorideApplication.TOKEN = "";
                            Log.e("info", "注销信鸽失败");
                        }
                    });
        }
        int size = mActivityList.size();
        for (int i = 0; i < size; i++) {
            if (null != mActivityList.get(i)) {
                mActivityList.get(i).finish();
            }
        }
        saveExitTime();
        mActivityList.clear();
    }

    public static void clearAllActivity() {
        int size = mActivityList.size();
        for (int i = 0; i < size; i++) {
            if (null != mActivityList.get(i)) {
                mActivityList.get(i).finish();
            }
        }
        mActivityList.clear();
    }


    // 授权后逻辑
    public static void bePushAside(Context context) {
        int size = mActivityList.size();
        for (int i = 0; i < size; i++) {
            if (null != mActivityList.get(i)) {
                mActivityList.get(i).finish();
            }
        }
        mActivityList.clear();
        UseInfo mUseInfo = UseInfoLocal.getUseInfo();
        mUseInfo.setPassword("");
        UseInfoLocal.setUseInfo(mUseInfo);
        //		DorideApplication.TOKEN = "";
        Intent mIntent = new Intent(context, UserLoginActivity.class);
        mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(mIntent);
    }

    public static void onTokenDisable() {
//        for (Activity activity : mActivityList) {
//            if (activity instanceof UserLoginActivity) {
//                BaseActivity base = (BaseActivity) activity;
//                if (base.IsShowing()) {
//                    return;
//                }
//            }
//        }
//        handler.sendEmptyMessage(1);
        // Intent mIntent = new Intent(getApplicationContext(),
        // LoginActivity.class);
        GetCarInfo.getInstance().initCarInfo();
        SharepUtil.cleanAllKey();
        OtherInfo.getInstance().initInfo();
        clearAllActivity();
        Intent mIntent = new Intent(DorideApplication.getInstanse(),
                UserLoginActivity.class);
        mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        DorideApplication.getInstanse().startActivity(mIntent);
    }

    @SuppressLint("HandlerLeak")
    static Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (msg.what == 1) {
                Intent mIntent = new Intent(DorideApplication.getInstanse(),
                        UserLoginActivity.class);
                mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                DorideApplication.getInstanse().startActivity(mIntent);
            }
        }
    };

    public static void onStopService(final Context context) {

    }

    public static void passwdToggle(Context context, EditText input, ImageView toggleView, String tag) {
        if (!TextUtils.isEmpty(tag)) {
            if (tag.equals("on")) {
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                toggleView.setImageDrawable(context.getResources().getDrawable(R.mipmap.passwd_off));
                toggleView.setTag("off");
            } else {
                input.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                toggleView.setImageDrawable(context.getResources().getDrawable(R.mipmap.passwd_on));
                toggleView.setTag("on");
            }
        }
    }

    public static void saveExitTime() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String extiLoginTime = sdf.format(date);
        SharedPreferences.Editor editor = DorideApplication.getAppContext().getSharedPreferences("LastLoginTime", MODE_PRIVATE).edit();
        editor.putString("LoginTime" + UserInfo.getInstance().mobile, extiLoginTime);
        editor.apply();
    }

    public static void finishAllCarSelectActivity() {
        for (Activity activity : mActivityList) {
            if (activity instanceof CarTypeListActivity) {
                activity.finish();
            } else if (activity instanceof CarModeListActivity) {
                activity.finish();
            } else {
                //do nothing
            }
        }
    }
}

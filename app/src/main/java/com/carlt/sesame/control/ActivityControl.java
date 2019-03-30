package com.carlt.sesame.control;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.blankj.utilcode.util.SPUtils;
import com.carlt.doride.DorideApplication;
import com.carlt.doride.control.CPControl;
import com.carlt.doride.data.BaseResponseInfo;
import com.carlt.doride.http.retrofitnet.model.CarConfigRes;
import com.carlt.doride.http.retrofitnet.model.ContactsInfo;
import com.carlt.doride.http.retrofitnet.model.GetCarInfo;
import com.carlt.doride.http.retrofitnet.model.OtherInfo;
import com.carlt.doride.http.retrofitnet.model.UserInfo;
import com.carlt.doride.model.LoginInfo;
import com.carlt.doride.protocolparser.BaseParser;
import com.carlt.doride.ui.activity.login.UserLoginActivity;
import com.carlt.sesame.data.SesameLoginInfo;
import com.carlt.sesame.data.UseInfo;
import com.carlt.sesame.preference.TokenInfo;
import com.carlt.sesame.preference.UseInfoLocal;
import com.carlt.sesame.ui.activity.base.BaseActivity;
import com.carlt.sesame.ui.view.PopBoxCreat;
import com.carlt.sesame.ui.view.PopBoxCreat.DialogWithTitleClick;
import com.carlt.sesame.utility.LoginChecker;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

public class ActivityControl {

    public static List<Activity> mActivityList = new ArrayList<Activity>();

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
        Logger.e("---" + TokenInfo.getToken());
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
        TokenInfo.setToken("");
        int size = mActivityList.size();
        for (int i = 0; i < size; i++) {
            if (null != mActivityList.get(i)) {
                mActivityList.get(i).finish();
            }
        }
        mActivityList.clear();
        DorideApplication.getInstance().setShowDragFlag(false);

        UserInfo.getInstance().initUserInfo();
        GetCarInfo.getInstance().initCarInfo();
        OtherInfo.getInstance().initInfo();
        ContactsInfo.getInstance().initContactsInfo();
        CarConfigRes.getInstance().initCarConfigRes();

//        LoginChecker.stopCheck();
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

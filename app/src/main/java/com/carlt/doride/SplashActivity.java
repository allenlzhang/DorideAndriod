package com.carlt.doride;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.carlt.doride.base.BaseActivity;
import com.carlt.doride.control.ActivityControl;
import com.carlt.doride.control.LoginControl;
import com.carlt.doride.data.BaseResponseInfo;
import com.carlt.doride.data.UseInfo;
import com.carlt.doride.data.VersionInfo;
import com.carlt.doride.http.retrofitnet.model.GetCarInfo;
import com.carlt.doride.model.LoginInfo;
import com.carlt.doride.preference.UseInfoLocal;
import com.carlt.doride.protocolparser.BaseParser.ResultCallback;
import com.carlt.doride.protocolparser.VersionInfoParser;
import com.carlt.doride.push.MessageReceiver;
import com.carlt.doride.systemconfig.URLConfig;
import com.carlt.doride.ui.activity.login.UserLoginActivity;
import com.carlt.doride.ui.view.DownloadView;
import com.carlt.doride.ui.view.PopBoxCreat;
import com.carlt.doride.ui.view.PopBoxCreat.DialogWithTitleClick;
import com.carlt.doride.ui.view.UUToast;
import com.carlt.doride.ui.view.UUUpdateDialog;
import com.carlt.doride.utils.FileUtil;
import com.carlt.doride.utils.LocalConfig;
import com.carlt.doride.utils.Log;
import com.carlt.sesame.control.CPControl;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class SplashActivity extends BaseActivity {

    private int useTimes;// 用户使用app的次数

    private UseInfo mUseInfo;// 本地记录用户使用app情况

    private String account;// 登录账户

    private String password;// 登录密码

    private VersionInfo mVersionInfo;// 升级信息

    private final static long interval = 30 * 1000;// 友盟统计-时间间隔

    long mMills = 0;
//    private MessageReceiver mReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fullScreen(this);
        setContentView(R.layout.activity_splash);
        mHandler.sendEmptyMessageDelayed(0, 1500);

    }

    private void registerXG() {
        //        IntentFilter filter = new IntentFilter();
        //        filter.addAction("com.tencent.android.tpush.action.PUSH_MESSAGE");
        //        filter.addAction("com.tencent.android.tpush.action.FEEDBACK");
        //        mReceiver = new MessageReceiver();
        //        registerReceiver(mReceiver, filter);
        Intent intent = new Intent(this, MessageReceiver.class);
        intent.setAction("com.tencent.android.tpush.action.PUSH_MESSAGE");
        intent.setAction("com.tencent.android.tpush.action.FEEDBACK");
        sendBroadcast(intent);
        Logger.e("======MessageReceiver");
    }


    protected String[] needPermissions = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CALL_PHONE

    };

    private void splash() {
        FileUtil.openOrCreatDir(LocalConfig.mImageCacheSavePath_SD);
        FileUtil.openOrCreatDir(LocalConfig.mImageCacheSavePath_Absolute);
        FileUtil.openOrCreatDir(LocalConfig.mDownLoadFileSavePath_SD);
        FileUtil.openOrCreatDir(LocalConfig.mDownLoadFileSavePath_Absolute);
        FileUtil.openOrCreatDir(LocalConfig.mErroLogSavePath_SD);
        FileUtil.openOrCreatDir(LocalConfig.mTracksSavePath_SD);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            getVersion();
//            jumpLogic();
        } else {
            requestPermissions(this, needPermissions, new BaseActivity.RequestPermissionCallBack() {
                @Override
                public void granted() {
                    FileUtil.openOrCreatDir(LocalConfig.mImageCacheSavePath_SD);
                    FileUtil.openOrCreatDir(LocalConfig.mImageCacheSavePath_Absolute);
                    FileUtil.openOrCreatDir(LocalConfig.mDownLoadFileSavePath_SD);
                    FileUtil.openOrCreatDir(LocalConfig.mDownLoadFileSavePath_Absolute);
                    FileUtil.openOrCreatDir(LocalConfig.mErroLogSavePath_SD);
                    FileUtil.openOrCreatDir(LocalConfig.mTracksSavePath_SD);
                    getVersion();
//                    jumpLogic();
                }

                @Override
                public void denied() {
                    //                UUToast.showUUToast(SplashActivity.this, "未获取到权限，定位功能不可用");
                    UUToast.showUUToast(DorideApplication.getInstanse(), "未获取到权限，应用即将退出");
                    mHandler.sendEmptyMessageDelayed(10, 1000);
                    //                finish();

                }
            });
        }

        mUseInfo = UseInfoLocal.getUseInfo();
        useTimes = mUseInfo.getTimes();
        account = mUseInfo.getAccount();
        password = mUseInfo.getPassword();
        //        CPControl.GetVersion(listener_version);

        //        jumpLogic();

        // 检查更新聚合城市信息列表
        com.carlt.sesame.control.CPControl.GetCityInfos();
    }

    private void getVersion() {
        VersionInfoParser parser = new VersionInfoParser(versionCallback);
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("softtype", "android");
        params.put("version", DorideApplication.Version + "");
        parser.executePost(URLConfig.getM_GET_APP_UPDATE(), params);
    }

    ResultCallback versionCallback = new ResultCallback() {
        @Override
        public void onSuccess(BaseResponseInfo bInfo) {
            mVersionInfo = (VersionInfo) bInfo.getValue();
            int status = mVersionInfo.getStatus();
            //            status = VersionInfo.STATUS_ENABLE;
            switch (status) {
                case VersionInfo.STATUS_ENABLE:
                    // 不升级
                    jumpLogic();
                    break;
                case VersionInfo.STATUS_ABLE:
                    // 强制升级
                    dialogUpdateAble();
                    break;
                case VersionInfo.STATUS_CHOSE:
                    // 可选升级
                    dialogUpdateChose();
                    break;
            }
        }

        @Override
        public void onError(BaseResponseInfo bInfo) {
            dialogErro();
        }
    };

    @Override
    protected void onResume() {

        super.onResume();
    }

    /**
     * 获取升级信息失败对话框
     */
    private void dialogErro() {
        DialogWithTitleClick click = new DialogWithTitleClick() {

            @Override
            public void onLeftClick() {
                getVersion();
            }

            @Override
            public void onRightClick() {
                ActivityControl.onExit();
            }
        };

        String content = getResources().getString(R.string.update_erro);
        if (SplashActivity.this.isFinishing()) {
            return;
        }
        PopBoxCreat.createDialogWithNodismiss(SplashActivity.this, "温馨提示",
                content, "", "重试", "退出", click);

    }

    /**
     * 跳转逻辑
     */
    private void jumpLogic() {
        // if (useTimes == 0) {
        // // 第一次使用，显示引导页
        // mHandler.sendEmptyMessageDelayed(0, 1500);
        // } else {
        if (account != null && account.length() > 0 && password != null
                && password.length() > 0) {
            // 不是第一次使用
            // 直接调用登录接口
//            CPControl.GetLogin(account, password, listener_login);
            LoginControl.Login(account,password);
            LoginControl.setCallback(callback);
        } else {

            long duration = 3000 - (System.currentTimeMillis() - mMills);
            mHandler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    // 不是第一次使用
                    // 跳转至登录页面
                    Intent mIntent2 = new Intent(SplashActivity.this,
                            UserLoginActivity.class);
                    startActivity(mIntent2);
                    finish();
                }
            }, duration > 0 ? duration : 0);

        }
        // }
    }
com.carlt.sesame.control.CPControl.GetResultListCallback callback = new CPControl.GetResultListCallback() {
    @Override
    public void onFinished(Object o) {
        final Message msg = new Message();
        msg.what = 3;
        msg.obj = o;
        long duration = 3000 - (System.currentTimeMillis() - mMills);
        mHandler.postDelayed(new Runnable() {

            @Override
            public void run() {
                mHandler.sendMessage(msg);
            }
        }, duration > 0 ? duration : 0);
    }

    @Override
    public void onErro(Object o) {
        Message msg = new Message();
        msg.what = 4;
        msg.obj = o;
        mHandler.sendMessage(msg);
    }
};
    private void showDownloadView(String apkUrl) {
        DownloadView mDownloadView = new DownloadView(SplashActivity.this);
        mDownloadView.showView(apkUrl);
    }

    /**
     * 强制升级对话框
     */
    private void dialogUpdateAble() {
        final String url = mVersionInfo.getFilepath();

        DialogWithTitleClick click = new DialogWithTitleClick() {

            @Override
            public void onLeftClick() {
                if (url != null && url.length() > 0) {
                    showDownloadView(url);
                }
            }

            @Override
            public void onRightClick() {
                // 退出
                //                ActivityControl.onExit();
                ActivityControl.clearAllActivity();
            }
        };

        String content = mVersionInfo.getRemark();
        if (content != null && content.length() > 0) {
            PopBoxCreat.createDialogWithNodismiss(SplashActivity.this, "升级提示",
                    content, "", "升级", "退出", click);
        } else {
            content = getResources().getString(R.string.update_2);
            PopBoxCreat.createDialogWithNodismiss(SplashActivity.this, "升级提示",
                    content, "", "升级", "退出", click);
        }
    }

    /**
     * 可选升级对话框
     */
    private void dialogUpdateChose() {
        final String url = mVersionInfo.getFilepath();
        DialogWithTitleClick click = new DialogWithTitleClick() {

            @Override
            public void onLeftClick() {
                // 升级
                // if (url != null && url.length() > 0) {
                // Intent intent = new Intent();
                // intent.setAction("android.intent.action.VIEW");
                // Uri content_url = Uri.parse(url);
                // intent.setData(content_url);
                // startActivity(intent);
                //
                // ActivityControl.onExit();
                // }

                if (url != null && url.length() > 0) {
                    showDownloadView(url);
                }
            }

            @Override
            public void onRightClick() {
                // 以后再说
                jumpLogic();
            }
        };

        String content = mVersionInfo.getRemark();
        if (content != null && content.length() > 0) {
            PopBoxCreat.createDialogWithNodismiss(SplashActivity.this, "升级提示",
                    content, "", "升级", "以后再说", click);
        } else {
            content = getResources().getString(R.string.update_1);
            PopBoxCreat.createDialogWithNodismiss(SplashActivity.this, "升级提示",
                    content, "", "升级", "以后再说", click);
        }
    }


    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    splash();
                    break;
                case 3:
                    useTimes++;
                    ActivityControl.initXG();
                    LoginControl.mDialogUpdateListener = mDUpdateListener;
                    LoginControl.logic(SplashActivity.this);
                    if (GetCarInfo.getInstance().isUpgrade!=1) {
                        finish();
                    }
                    break;
                case 4:
                    UUToast.showUUToast(SplashActivity.this, "登录错误："
                            + ((String) msg.obj));
                    Intent mIntent4 = new Intent(SplashActivity.this,
                            UserLoginActivity.class);
                    finish();
                    overridePendingTransition(R.anim.enter_alpha, R.anim.exit_alpha);
                    startActivity(mIntent4);
                    break;
                case 10:
                    finish();
                    break;


            }

            super.handleMessage(msg);
        }
    };

    private ResultCallback listener_login = new ResultCallback() {

        @Override
        public void onSuccess(BaseResponseInfo o) {

            String dataValue = (String) o.getValue();
            JSONObject mJSON_data = null;
            try {
                mJSON_data = new JSONObject(dataValue);


                LoginControl.parseLoginInfo(mJSON_data);
                final Message msg = new Message();
                msg.what = 3;
                msg.obj = o;
                long duration = 3000 - (System.currentTimeMillis() - mMills);
                mHandler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        mHandler.sendMessage(msg);
                    }
                }, duration > 0 ? duration : 0);
            } catch (JSONException e) {
                e.printStackTrace();
                Message msg = new Message();
                msg.what = 4;
                msg.obj = o;
                mHandler.sendMessage(msg);
            }
        }

        @Override
        public void onError(BaseResponseInfo o) {
            Message msg = new Message();
            msg.what = 4;
            msg.obj = o;
            mHandler.sendMessage(msg);

        }

    };

    UUUpdateDialog.DialogUpdateListener mDUpdateListener = new UUUpdateDialog.DialogUpdateListener() {

        @Override
        public void onSuccess() {
            LoginControl.mDialogUpdateListener = null;
            finish();
        }

        @Override
        public void onFailed() {
            LoginControl.mDialogUpdateListener = null;
            Intent mIntent = new Intent(SplashActivity.this, UserLoginActivity.class);
            startActivity(mIntent);
        }
    };

    private void fullScreen(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                //5.x开始需要把颜色设置透明，否则导航栏会呈现系统默认的浅灰色
                Window window = activity.getWindow();
                View decorView = window.getDecorView();
                //两个 flag 要结合使用，表示让应用的主体内容占用系统状态栏的空间
                int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
                decorView.setSystemUiVisibility(option);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.TRANSPARENT);
                //导航栏颜色也可以正常设置
//                window.setNavigationBarColor(Color.TRANSPARENT);
            } else {
                Window window = activity.getWindow();
                WindowManager.LayoutParams attributes = window.getAttributes();
                int flagTranslucentStatus = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
                int flagTranslucentNavigation = WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION;
                attributes.flags |= flagTranslucentStatus;
//                attributes.flags |= flagTranslucentNavigation;
                window.setAttributes(attributes);
            }
        }
    }
}

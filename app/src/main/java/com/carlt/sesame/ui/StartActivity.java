package com.carlt.sesame.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.carlt.doride.DorideApplication;
import com.carlt.sesame.R;
import com.carlt.sesame.control.ActivityControl;
import com.carlt.sesame.control.CPControl;
import com.carlt.sesame.control.CPControl.GetResultListCallback;
import com.carlt.sesame.control.LoginControl;
import com.carlt.sesame.data.BaseResponseInfo;
import com.carlt.sesame.data.LoginInfo;
import com.carlt.sesame.data.UseInfo;
import com.carlt.sesame.data.VersionInfo;
import com.carlt.sesame.preference.UseInfoLocal;
import com.carlt.sesame.systemconfig.LocalConfig;
import com.carlt.sesame.ui.activity.base.BaseActivity;
import com.carlt.sesame.ui.activity.usercenter.login.LoginActivity;
import com.carlt.sesame.ui.view.DownloadView;
import com.carlt.sesame.ui.view.PopBoxCreat;
import com.carlt.sesame.ui.view.PopBoxCreat.DialogWithTitleClick;
import com.carlt.sesame.ui.view.UUUpdateDialog.DialogUpdateListener;
import com.carlt.sesame.utility.FileUtil;
import com.carlt.sesame.utility.UUToast;

public class StartActivity extends BaseActivity {
    private static final int REQUEST_PHONE_STATE = 14;
    private int useTimes;// 用户使用app的次数

    private UseInfo mUseInfo;// 本地记录用户使用app情况

    private String account;// 登录账户

    private String password;// 登录密码

    private VersionInfo mVersionInfo;// 升级信息

    private final static long interval = 30 * 1000;// 友盟统计-时间间隔

    long mMills = 0;
    private String imei;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        mMills = System.currentTimeMillis();
        //        mHandler.sendEmptyMessageDelayed(0, 1500);
        //        getPermission();
        //		/** 发送策略 */
        //		MobclickAgent.updateOnlineConfig(StartActivity.this);
        //		/** 设置session统计间隔 */
        //		MobclickAgent.setSessionContinueMillis(interval);
        //		/** 设置是否对日志信息进行加密, 默认false(不加密). */
        //		AnalyticsConfig.enableEncrypt(true);
        //		/** 禁止默认的页面统计方式 */
        //		MobclickAgent.openActivityDurationTrack(false);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            //            getVersion();
            //            toast("不需要动态获取权限");
            FileUtil.openOrCreatDir(LocalConfig.mImageCacheSavePath_SD);
            FileUtil.openOrCreatDir(LocalConfig.mImageCacheSavePath_Absolute);
            FileUtil.openOrCreatDir(LocalConfig.mDownLoadFileSavePath_SD);
            FileUtil.openOrCreatDir(LocalConfig.mDownLoadFileSavePath_Absolute);
            FileUtil.openOrCreatDir(LocalConfig.mErroLogSavePath_SD);
            FileUtil.openOrCreatDir(LocalConfig.mTracksSavePath_SD);

            TelephonyManager TelephonyMgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
            DorideApplication.NIMEI = TelephonyMgr.getDeviceId();
            initData();
        } else {
            requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.ACCESS_COARSE_LOCATION}, new RequestPermissionCallBack() {
                @SuppressLint("MissingPermission")
                @Override
                public void granted() {
                    FileUtil.openOrCreatDir(LocalConfig.mImageCacheSavePath_SD);
                    FileUtil.openOrCreatDir(LocalConfig.mImageCacheSavePath_Absolute);
                    FileUtil.openOrCreatDir(LocalConfig.mDownLoadFileSavePath_SD);
                    FileUtil.openOrCreatDir(LocalConfig.mDownLoadFileSavePath_Absolute);
                    FileUtil.openOrCreatDir(LocalConfig.mErroLogSavePath_SD);
                    FileUtil.openOrCreatDir(LocalConfig.mTracksSavePath_SD);
                    TelephonyManager TelephonyMgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
                    DorideApplication.NIMEI = TelephonyMgr.getDeviceId();
                    //                    mUseInfo = UseInfoLocal.getUseInfo();
                    //                    useTimes = mUseInfo.getTimes();
                    //                    account = mUseInfo.getAccount();
                    //                    password = mUseInfo.getPassword();
                    initData();
                }

                @Override
                public void denied() {
                    Log.e("----", "权限被拒");
                    UUToast.showUUToast(StartActivity.this, "未获取到权限，应用即将退出");
                    mHandler.sendEmptyMessageDelayed(5, 1500);
                }
            });

        }
        TelephonyManager TelephonyMgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        DorideApplication.NIMEI = TelephonyMgr.getDeviceId();
    }

    private void initData() {
        //        FileUtil.openOrCreatDir(LocalConfig.mImageCacheSavePath_SD);
        //        FileUtil.openOrCreatDir(LocalConfig.mImageCacheSavePath_Absolute);
        //        FileUtil.openOrCreatDir(LocalConfig.mDownLoadFileSavePath_SD);
        //        FileUtil.openOrCreatDir(LocalConfig.mDownLoadFileSavePath_Absolute);
        //        FileUtil.openOrCreatDir(LocalConfig.mErroLogSavePath_SD);
        //        FileUtil.openOrCreatDir(LocalConfig.mTracksSavePath_SD);
        mUseInfo = UseInfoLocal.getUseInfo();
        useTimes = mUseInfo.getTimes();
        account = mUseInfo.getAccount();
        password = mUseInfo.getPassword();

        CPControl.GetVersion(listener_version);
        // 检查更新聚合城市信息列表
        CPControl.GetCityInfos();
        //车行易
        //		CPControl.GetCityInfos2();
        //        动态申请获取IMEI权限

    }

    //    private void getPermission() {
    //        //Android6.0需要动态获取权限
    //        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
    //            //            toast("需要动态获取权限");
    //            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_PHONE_STATE);
    //        } else {
    //            //            toast("不需要动态获取权限");
    //            TelephonyManager TelephonyMgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
    //            DorideApplication.NIMEI = TelephonyMgr.getDeviceId();
    //            initData();
    //        }
    //
    //    }

    //    /**
    //     * 获取权限的监听
    //     */
    //    @Override
    //    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
    //        if (requestCode == REQUEST_PHONE_STATE && grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
    //            TelephonyManager TelephonyMgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
    //            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
    //                Log.e("----", "权限被拒");
    //                finish();
    //                System.exit(0);
    //                return;
    //            }
    //            DorideApplication.NIMEI = TelephonyMgr.getDeviceId();
    //            initData();
    //        }
    //    }

    /**
     * 跳转逻辑
     */
    private void jumpLogic() {
        // if (useTimes == 0) {
        // // 第一次使用，显示引导页
        // mHandler.sendEmptyMessageDelayed(0, 1500);
        // } else {



        if (account != null && account.length() > 0 && password != null && password.length() > 0) {
            // 不是第一次使用
            // 直接调用登录接口
            if (TextUtils.isEmpty(DorideApplication.NIMEI)) {
                UUToast.showUUToast(DorideApplication.getAppContext(), "权限未获取到，请在权限管理打开获取手机IMEI权限");
                System.exit(0);
                return;
            }
            CPControl.GetLogin(account, password, listener_login);
        } else {

            long duration = 3000 - (System.currentTimeMillis() - mMills);
            mHandler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    // 不是第一次使用
                    // 跳转至登录页面
                    Intent mIntent2 = new Intent(StartActivity.this, LoginActivity.class);
                    startActivity(mIntent2);
                    finish();
                }
            }, duration > 0 ? duration : 0);

        }
        // }
    }

    /**
     * 获取升级信息失败对话框
     */
    private void dialogErro() {
        DialogWithTitleClick click = new DialogWithTitleClick() {

            @Override
            public void onLeftClick() {
                CPControl.GetVersion(listener_version);
            }

            @Override
            public void onRightClick() {
                ActivityControl.onExit();
            }
        };

        String content = getResources().getString(R.string.update_erro);
        PopBoxCreat.createDialogWithNodismiss(StartActivity.this, "提示", content, "", "重试", "退出", click);

    }

    /**
     * 强制升级对话框
     */
    private void dialogUpdateAble() {
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
                // 退出
                ActivityControl.onExit();
            }
        };

        String content = mVersionInfo.getRemark();
        if (content != null && content.length() > 0) {
            PopBoxCreat.createDialogWithNodismiss(StartActivity.this, "升级信息", content, "", "升级", "退出", click);
        } else {
            content = getResources().getString(R.string.update_2);
            PopBoxCreat.createDialogWithNodismiss(StartActivity.this, "升级信息", content, "", "升级", "退出", click);
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
//                SPUtils.getInstance().put("isUpdate",false);
                jumpLogic();
            }
        };

        String content = mVersionInfo.getRemark();
        if (content != null && content.length() > 0) {
            PopBoxCreat.createDialogWithNodismiss(StartActivity.this, "升级信息", content, "", "升级", "以后再说", click);
        } else {
            content = getResources().getString(R.string.update_1);
            PopBoxCreat.createDialogWithNodismiss(StartActivity.this, "升级信息", content, "", "升级", "以后再说", click);
        }
    }

    private void showDownloadView(String apkUrl) {
        DownloadView mDownloadView = new DownloadView(StartActivity.this);
        mDownloadView.showView(apkUrl);
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    // 第一次使用，显示引导页
                    Intent mIntent1 = new Intent(StartActivity.this, IntroductionActivity.class);
                    startActivity(mIntent1);
                    finish();
                    break;
                case 1:
                    // 获取升级信息成功
                    mVersionInfo = (VersionInfo) msg.obj;
                    int status = mVersionInfo.getStatus();
                    LogUtils.e("mVersionInfo====="+mVersionInfo.toString());
                    switch (status) {
                        case VersionInfo.STATUS_ENABLE:
                            // 不升级
                            SPUtils.getInstance().put("VersionStatus", VersionInfo.STATUS_ENABLE);
//                            SPUtils.getInstance().put("info", mVersionInfo.info);
//                            SPUtils.getInstance().put("version", mVersionInfo.latest_version);
                            jumpLogic();
                            break;
                        case VersionInfo.STATUS_ABLE:
                            // 强制升级
                            SPUtils.getInstance().put("info", mVersionInfo.info);
                            SPUtils.getInstance().put("version", mVersionInfo.latest_version);
                            dialogUpdateAble();
                            break;
                        case VersionInfo.STATUS_CHOSE:
                            // 可选升级
                            SPUtils.getInstance().put("info", mVersionInfo.info);
                            SPUtils.getInstance().put("version", mVersionInfo.latest_version);
                            dialogUpdateChose();
                            break;
                    }

                    break;
                case 2:
                    // 获取升级信息失败
                    dialogErro();
                    break;
                case 3:
                    useTimes++;
                    ActivityControl.initXG();
                    LoginControl.mDialogUpdateListener = mDUpdateListener;
                    LoginControl.logic(StartActivity.this);
                    if (!LoginInfo.isUpgradeing()) {
                        finish();
                    }
                    break;
                case 4:
                    BaseResponseInfo mBaseResponseInfo = (BaseResponseInfo) msg.obj;
                    UUToast.showUUToast(StartActivity.this, "登录错误：" + mBaseResponseInfo.getInfo());
                    Intent mIntent4 = new Intent(StartActivity.this, LoginActivity.class);
                    finish();
                    overridePendingTransition(R.anim.enter_alpha, R.anim.exit_alpha);
                    startActivity(mIntent4);
                    break;
                case 5:
                    finish();
                    break;
                case 7:
//                    finish();

                    break;

            }

            super.handleMessage(msg);
        }
    };

    public static int getLocalVersion(Context ctx) {
        int localVersion = 0;
        try {
            PackageInfo packageInfo = ctx.getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(ctx.getPackageName(), 0);
            localVersion = packageInfo.versionCode;
            //            LogUtil.d("TAG", "本软件的版本号。。" + localVersion);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return localVersion;
    }

    private GetResultListCallback listener_version = new GetResultListCallback() {

        @Override
        public void onFinished(Object o) {
            final Message msg = new Message();
            msg.what = 1;
            msg.obj = o;

            long duration = 2500 - (System.currentTimeMillis() - mMills);
            mHandler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    // 不是第一次使用
                    // 跳转至登录页面
                    mHandler.sendMessage(msg);
                }
            }, duration > 0 ? duration : 0);
        }

        @Override
        public void onErro(Object o) {
            Message msg = new Message();
            msg.what = 2;
            msg.obj = o;
            mHandler.sendMessage(msg);

        }
    };

    private GetResultListCallback listener_login = new GetResultListCallback() {

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

    DialogUpdateListener mDUpdateListener = new DialogUpdateListener() {

        @Override
        public void onSuccess() {
            LoginControl.mDialogUpdateListener = null;
            finish();
        }

        @Override
        public void onFailed() {
            LoginControl.mDialogUpdateListener = null;
            Intent mIntent = new Intent(context, LoginActivity.class);
            startActivity(mIntent);
        }
    };

}

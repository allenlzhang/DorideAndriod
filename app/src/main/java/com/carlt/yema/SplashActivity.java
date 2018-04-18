package com.carlt.yema;

import android.content.Intent;
import android.icu.util.VersionInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.carlt.yema.base.BaseActivity;
import com.carlt.yema.control.ActivityControl;
import com.carlt.yema.control.CPControl;
import com.carlt.yema.control.LoginControl;
import com.carlt.yema.data.BaseResponseInfo;
import com.carlt.yema.data.UseInfo;
import com.carlt.yema.model.LoginInfo;
import com.carlt.yema.preference.UseInfoLocal;
import com.carlt.yema.protocolparser.BaseParser;
import com.carlt.yema.ui.activity.login.UserLoginActivity;
import com.carlt.yema.ui.view.UUToast;
import com.carlt.yema.utils.FileUtil;
import com.carlt.yema.utils.LocalConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class SplashActivity extends BaseActivity implements Callback {

    private int useTimes;// 用户使用app的次数

    private UseInfo mUseInfo;// 本地记录用户使用app情况

    private String account;// 登录账户

    private String password;// 登录密码

    private VersionInfo mVersionInfo;// 升级信息

    private final static long interval = 30 * 1000;// 友盟统计-时间间隔

    long mMills = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    private void splash() {
        FileUtil.openOrCreatDir(LocalConfig.mImageCacheSavePath_SD);
        FileUtil.openOrCreatDir(LocalConfig.mImageCacheSavePath_Absolute);
        FileUtil.openOrCreatDir(LocalConfig.mDownLoadFileSavePath_SD);
        FileUtil.openOrCreatDir(LocalConfig.mDownLoadFileSavePath_Absolute);
        FileUtil.openOrCreatDir(LocalConfig.mErroLogSavePath_SD);
        FileUtil.openOrCreatDir(LocalConfig.mTracksSavePath_SD);

        mUseInfo = UseInfoLocal.getUseInfo();
        useTimes = mUseInfo.getTimes();
        account = mUseInfo.getAccount();
        password = mUseInfo.getPassword();
        jumpLogic();
    }

    @Override
    protected void onResume() {
        mHandler.sendEmptyMessageDelayed(0,3000);
        super.onResume();
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
            CPControl.GetLogin(account, password, listener_login);
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
                    LoginControl.logic(SplashActivity.this);
                    if (!LoginInfo.isUpgradeing()) {
                        finish();
                    }
                    break;
                case 4:
                    BaseResponseInfo mBaseResponseInfo = (BaseResponseInfo) msg.obj;
                    UUToast.showUUToast(SplashActivity.this, "登录错误："
                            + mBaseResponseInfo.getInfo());
                    Intent mIntent4 = new Intent(SplashActivity.this,
                            UserLoginActivity.class);
                    finish();
                    overridePendingTransition(R.anim.enter_alpha, R.anim.exit_alpha);
                    startActivity(mIntent4);
                    break;

            }

            super.handleMessage(msg);
        }
    };

    private BaseParser.ResultCallback listener_version = new BaseParser.ResultCallback() {

        @Override
        public void onSuccess(BaseResponseInfo o) {

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
        public void onError(BaseResponseInfo o) {
            Message msg = new Message();
            msg.what = 2;
            msg.obj = o;
            mHandler.sendMessage(msg);
        }

    };

    private BaseParser.ResultCallback listener_login = new BaseParser.ResultCallback() {

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

    @Override
    public void onFailure(Call call, IOException e) {

    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {

    }


}

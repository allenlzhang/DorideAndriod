
package com.carlt.sesame.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.carlt.doride.DorideApplication;
import com.carlt.doride.R;
import com.carlt.sesame.control.CPControl;
import com.carlt.sesame.control.CPControl.GetResultListCallback;
import com.carlt.sesame.data.AdvertiseInfo;
import com.carlt.sesame.data.BaseResponseInfo;
import com.carlt.sesame.data.SesameLoginInfo;
import com.carlt.sesame.data.VersionInfo;
import com.carlt.sesame.ui.activity.base.BaseActivityGroup;
import com.carlt.sesame.ui.activity.car.CarMainActivity;
import com.carlt.sesame.ui.activity.career.CareerMainActivity;
import com.carlt.sesame.ui.activity.remote.RemoteMainNewActivity;
import com.carlt.sesame.ui.activity.setting.SettingMainActivity;
import com.carlt.sesame.ui.view.PopBoxCreat;
import com.carlt.sesame.ui.view.PopBoxCreat.DialogWithTitleClick;
import com.carlt.sesame.ui.view.UUFeetipDialog;
import com.carlt.sesame.utility.Log;
import com.carlt.sesame.utility.LoginChecker;
import com.carlt.sesame.utility.UUToast;

import java.io.File;
import java.lang.reflect.Method;

public class SesameMainActivity extends BaseActivityGroup implements OnCheckedChangeListener {
    private LinearLayout container;

    public static RadioButton[] tab;

    private int mCurrentTab = 0;

    private static Method[] hideMethods;

    private static ImageView dot4;

    boolean isTransferDialog;

    public final static String CURRENT_TAB = "current_tab";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // initXG();
        GetResultListCallback listener = new GetResultListCallback() {

            @Override
            public void onFinished(Object o) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onErro(Object o) {
                // TODO Auto-generated method stub

            }
        };

        CPControl.GetSecretaryCategoryResult(listener11);
        CPControl.GetUserOtherInfoResult(null);
        CPControl.GetCarConfigResult(null);
        LoginChecker.startCheck();
        setContentView(R.layout.activity_main_layout);
        container = (LinearLayout) findViewById(R.id.main_containerBody);
        dot4 = (ImageView) findViewById(R.id.main_tab4_dot);
        initRadios();
        try {
            mCurrentTab = getIntent().getIntExtra(CURRENT_TAB, 0);
            Log.e("info", "mCurrentTab=" + mCurrentTab);
        } catch (Exception e) {
            // TODO: handle exception
        }

        tab[mCurrentTab].setChecked(true);
        setCurrentView(mCurrentTab);
        //setCurrentView(0);

        isTransferDialog = getIntent().getBooleanExtra("showTransferDialog", false);
            // 非游客登录版
            if (SesameLoginInfo.isMain()) {
                // CPControl.GetExtInfoResult(listener_fee);
            } else {
                if (isTransferDialog) {
                    PopBoxCreat.createDialogNotitleOneBtn(SesameMainActivity.this, "过户成功", "可以正常登陆芝麻乐园", "确定", new DialogWithTitleClick() {

                        @Override
                        public void onRightClick() {

                        }

                        @Override
                        public void onLeftClick() {

                        }
                    });
                }
            }


        String localVersion = "";
        try {
            PackageInfo packageInfo = getApplicationContext().getPackageManager().getPackageInfo(getPackageName(), 0);
            localVersion = packageInfo.versionName;
            int versionCode = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        String lasterVersion = SPUtils.getInstance().getString("version");
        String info = SPUtils.getInstance().getString("info");
        int isShow = SPUtils.getInstance().getInt("isShow");
        int versionStatus = SPUtils.getInstance().getInt("VersionStatus",0);
        boolean isUpdate = SPUtils.getInstance().getBoolean("isUpdate", true);
        LogUtils.e("------" + versionStatus + ",lasterVersion===" + lasterVersion);
        if (versionStatus == 1) {
            if (TextUtils.isEmpty(lasterVersion)) {
                return;
            }
            if (DorideApplication.Version == Integer.valueOf(lasterVersion)) {
                //最新版本和当前版本一致，说明是最新版本，只是提示升级时的info
                PopBoxCreat.createDialogNotitleOneBtn(this, "版本更新" + localVersion, info, "确定", new DialogWithTitleClick() {
                    @Override
                    public void onLeftClick() {
                    }

                    @Override
                    public void onRightClick() {
                    }
                });
            } else {
                //不一致，请求接口
                if (isShow != 1) {
                    CPControl.GetCurVersion(new GetResultListCallback() {
                        @Override
                        public void onFinished(Object o) {
                            Message msg = new Message();
                            msg.what = 4;
                            msg.obj = o;
                            mHandler.sendMessage(msg);
                        }

                        @Override
                        public void onErro(Object o) {
                            //                        Message msg = new Message();
                            //                        msg.what = 8;
                            //                        msg.obj = o;
                            //                        mHandler.sendMessage(msg);
                        }
                    });
                }


            }
            SPUtils.getInstance().remove("info");
            SPUtils.getInstance().remove("version");
            SPUtils.getInstance().remove("VersionStatus");
        }


    }

    GetResultListCallback listener11 = new GetResultListCallback() {

        @Override
        public void onFinished(Object o) {

        }

        @Override
        public void onErro(Object o) {
        }
    };

    GetResultListCallback listener_fee = new GetResultListCallback() {

        @Override
        public void onFinished(Object o) {
            Message msg = new Message();
            msg.what = 0;
            msg.obj = o;
            mHandler.sendMessage(msg);
        }

        @Override
        public void onErro(Object o) {
            Message msg = new Message();
            msg.what = 1;
            msg.obj = o;
            mHandler.sendMessage(msg);
        }
    };

    GetResultListCallback listener_ads = new GetResultListCallback() {

        @Override
        public void onFinished(Object o) {
            Message msg = new Message();
            msg.what = 2;
            msg.obj = o;
            mHandler.sendMessage(msg);
        }

        @Override
        public void onErro(Object o) {
            Message msg = new Message();
            msg.what = 3;
            msg.obj = o;
            mHandler.sendMessage(msg);
        }
    };

    public void setCurrentView(int index) {

        mCurrentTab = index;
        for (int i = 0; i < 4; i++) {
            if (i == index) {
                tab[i].setTextColor(getResources().getColor(R.color.blue_txt));
            } else {
                tab[i].setTextColor(getResources().getColor(R.color.text_color_gray1));
            }
        }

        if (container == null)
            container = (LinearLayout) findViewById(R.id.main_containerBody);
        Intent intent = null;
        switch (index) {

            case 0:
                intent = new Intent(SesameMainActivity.this, CareerMainActivity.class);
                break;

            case 1:
                intent = new Intent(SesameMainActivity.this, CarMainActivity.class);
                // intent = new Intent(SesameMainActivity.this,
                // CarMainGeneralActivity.class);
                break;

            case 2:
                intent = new Intent(SesameMainActivity.this, RemoteMainNewActivity.class);
                break;

            case 3:
                intent = new Intent(SesameMainActivity.this, SettingMainActivity.class);
                break;
        }
        container.removeAllViews();
        View view = getLocalActivityManager().startActivity("tab" + index, intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)).getDecorView();
        // view = new View(this);
        // view.setBackgroundColor(color_lv);
        view.setLayoutParams(new LayoutParams(-1, -1));

        container.addView(view);

    }

    private void initRadios() {
        tab = new RadioButton[4];
        tab[0] = ((RadioButton) findViewById(R.id.main_tab1));
        tab[0].setOnCheckedChangeListener(this);
        tab[1] = ((RadioButton) findViewById(R.id.main_tab2));
        tab[1].setOnCheckedChangeListener(this);
        tab[2] = ((RadioButton) findViewById(R.id.main_tab3));
        tab[2].setOnCheckedChangeListener(this);
        tab[3] = ((RadioButton) findViewById(R.id.main_tab4));
        tab[3].setOnCheckedChangeListener(this);

        //在120版本把这段代码注释掉
        // if ("1".equals(LoginInfo.getRemoteControl())) {
        // findViewById(R.id.main_tab3).setVisibility(View.VISIBLE);
        // }

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        if (isChecked) {
            switch (buttonView.getId()) {
                case R.id.main_tab1:
                    setCurrentView(0);
                    tab[0].setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.tab_my_selected), null, null);
                    tab[1].setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.tab_car), null, null);
                    tab[2].setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.tab_remote), null, null);
                    tab[3].setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.ic_setting_tab_normal), null, null);
                    break;
                case R.id.main_tab2:
                    setCurrentView(1);
                    tab[0].setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.tab_my), null, null);
                    tab[1].setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.tab_car_selected), null, null);
                    tab[2].setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.tab_remote), null, null);
                    tab[3].setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.ic_setting_tab_normal), null, null);
                    break;
                case R.id.main_tab3:
                    setCurrentView(2);
                    tab[0].setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.tab_my), null, null);
                    tab[1].setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.tab_car), null, null);
                    tab[2].setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.tab_remote_selected), null, null);
                    tab[3].setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.ic_setting_tab_normal), null, null);
                    break;

                case R.id.main_tab4:
                    setCurrentView(3);
                    tab[0].setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.tab_my), null, null);
                    tab[1].setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.tab_car), null, null);
                    tab[2].setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.tab_remote), null, null);
                    tab[3].setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.ic_setting_tab_select), null, null);
                    break;

            }

        }
    }

    public static boolean is_root() {

        boolean res = false;

        try {
            if ((!new File("/system/bin/su").exists()) && (!new File("/system/xbin/su").exists())) {
                res = false;
            } else {
                res = true;
            }
            ;
        } catch (Exception e) {

        }
        return res;
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("info", "mainactiv____onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("info", "mainactiv____onDestroy()");
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            UUFeetipDialog mUuFeetipDialog = new UUFeetipDialog(SesameMainActivity.this);
            switch (msg.what) {
                case 0:
                    // if (LoginInfo.isMain()) {
                    // if (LoginInfo.isServiceExpire()) {
                    // // 服务费用将要到期,获取续费广告
                    // //CPControl.GetAdvert(CPControl.ADVERT_TYPE_FEE,
                    // listener_ads);
                    // dot4.setVisibility(View.VISIBLE);
                    // } else {
                    // dot4.setVisibility(View.GONE);
                    // }
                    // } else {
                    // dot4.setVisibility(View.GONE);
                    // }

                    break;
                case 1:
                    BaseResponseInfo mInfo = (BaseResponseInfo) msg.obj;
                    if (mInfo != null) {
                        String message = mInfo.getInfo();
                        if (message != null && !message.equals("")) {
                            UUToast.showUUToast(SesameMainActivity.this, message);
                        }
                    }
                    break;
                case 2:
                    AdvertiseInfo mAdvertiseInfo = (AdvertiseInfo) msg.obj;
                    String filePath = "";
                    if (mAdvertiseInfo != null) {
                        filePath = mAdvertiseInfo.getFilePath();
                    }
                    mUuFeetipDialog.setFileUrl(filePath);
                    mUuFeetipDialog.setWindowManager(getWindowManager());
                    mUuFeetipDialog.show();
                    break;
                case 3:
                    if (mUuFeetipDialog != null && mUuFeetipDialog.isShowing()) {
                        mUuFeetipDialog.dismiss();
                    }
                    break;
                case 4:
                    Object obj = msg.obj;
                    LogUtils.e("===" + obj.toString());
                    VersionInfo mCurVersionInfo = (VersionInfo) msg.obj;
                    //                    CurVersionInfo versionInfo = new CurVersionInfo();
                    //                    try {
                    //                        JSONObject jo = new JSONObject(obj.toString());
                    //                        JSONObject mJSON_data = jo.optJSONObject("data");
                    //                        versionInfo.info = mJSON_data.optString("info", "");
                    //                        versionInfo.version = mJSON_data.optString("version", "");
                    //                    } catch (JSONException e) {
                    //                        e.printStackTrace();
                    //                    }


                    //if(mJSON_data!=null){

                    LogUtils.e("===" + mCurVersionInfo.toString());
                    PopBoxCreat.createDialogNotitleOneBtn(SesameMainActivity.this, "版本更新" + mCurVersionInfo.getVersion(), mCurVersionInfo.info, "确定", new DialogWithTitleClick() {
                        @Override
                        public void onLeftClick() {
                        }

                        @Override
                        public void onRightClick() {
                        }
                    });
                    SPUtils.getInstance().put("isShow", 1);
                    break;
            }
            if (isTransferDialog && !mUuFeetipDialog.isShowing()) {
                PopBoxCreat.createDialogNotitleOneBtn(SesameMainActivity.this, "过户成功", "可以正常登陆芝麻乐园", "确定", new DialogWithTitleClick() {

                    @Override
                    public void onRightClick() {

                    }

                    @Override
                    public void onLeftClick() {

                    }
                });
            }
        }
    };

    /*
     * 刷新续费状态
     */
    public static void setDotVisiable() {
    }

    /*
     * 刷新远程功能是否展示状态
     */
    // public static void setRemoteVisible() {
    // if ("1".equals(LoginInfo.getRemoteControl())) {
    // tab[2].setVisibility(View.VISIBLE);
    // }else{
    // tab[2].setVisibility(View.GONE);
    // }
    // }

}

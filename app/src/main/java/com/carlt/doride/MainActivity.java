package com.carlt.doride;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.carlt.doride.base.BaseActivity;
import com.carlt.doride.control.ActivityControl;
import com.carlt.doride.data.BaseResponseInfo;
import com.carlt.doride.data.flow.TrafficPackageWarnningInfo;
import com.carlt.doride.model.LoginInfo;
import com.carlt.doride.systemconfig.URLConfig;
import com.carlt.doride.ui.fragment.CarMainFragment;
import com.carlt.doride.ui.fragment.CarMainFragment2;
import com.carlt.doride.ui.fragment.HomeFragment;
import com.carlt.doride.ui.fragment.RecorderMainFragment;
import com.carlt.doride.ui.fragment.RemoteMainFragment;
import com.carlt.doride.ui.fragment.SettingMainFragment;
import com.carlt.doride.ui.view.UUToast;
import com.carlt.doride.utils.FileUtil;
import com.carlt.doride.utils.LocalConfig;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 主页面
 */
public class MainActivity extends BaseActivity implements View.OnClickListener {

    private HomeFragment         mHomeFragment;
    private CarMainFragment      mCarMainFragment;
    private CarMainFragment2     mCarMainFragment2;
    private RemoteMainFragment   mRemoteMainFragment;
    private RecorderMainFragment mRecorderMainFragemnt;
    private SettingMainFragment  mSettingMainFragment;

    private LinearLayout mTabHome;
    private LinearLayout mTabCar;
    private LinearLayout mTabRemote;
    private LinearLayout mTabPie;
    private LinearLayout mTabSetting;
    private LinearLayout mTabllPie;

    private ImageView mIvTabHome;
    private ImageView mIvTabCar;
    private ImageView mIvTabRemote;
    private ImageView mIvTabPie;
    private ImageView mIvTabSetting;

    private TextView mTxtTabHome;
    private TextView mTxtTabCar;
    private TextView mTxtTabRemote;
    private TextView mTxtTabPie;
    private TextView mTxtTabSetting;

    private FragmentManager mFragmentManager;
    private int             deviceisnew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);
        init();
        mFragmentManager = getSupportFragmentManager();
        setTabSelection(0);
        LogUtils.e("LoginInfo.getTbox_type()====" + LoginInfo.getTbox_type());
        if (LoginInfo.getTbox_type().equals("4G")) {
            initFlowInfo();
        }
    }

    private void initFlowInfo() {
        //        getFlowProductList();
        //        loadingDataUI();
        OkGo.<String>post(URLConfig.getmTrafficWarnningUrl())
                .params("client_id", URLConfig.getClientID())
                .params("dealerId", LoginInfo.getDealerId())
                .params("token", LoginInfo.getAccess_token())
                .params("deviceType", "android")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        LogUtils.e(response.body());
                        if (response.isSuccessful()) {
                            parseFlowInfoJson(response);
                        }

                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        //                        loadonErrorUI(new BaseResponseInfo());
                    }
                });
    }

    public static final String tip = "本月您的免费流量额度已经耗尽，已停止您本月设备的流量共享服务(次月恢复)。本月如需使用该服务，请充值流量加油包";

    private void parseFlowInfoJson(Response<String> response) {
        if (response != null) {
            JSONObject jo = null;
            try {
                jo = new JSONObject(response.body());
                int code = jo.getInt("code");
                String msg = jo.getString("msg");
                Gson gson = new Gson();
                if (code == 200) {
                    TrafficPackageWarnningInfo warnningInfo = gson.fromJson(response.body(), TrafficPackageWarnningInfo.class);
                    LoginInfo.setFlowWarn(warnningInfo.data.limit_warning);
                    String residual_data = warnningInfo.data.residual_data;
                    //                    residual_data = "0";
                    if (Double.valueOf(residual_data) <= 0) {
                        //本月流量已用完
                        //                        PopBoxCreat.createDialogNotitle(MainActivity.this, "温馨提示",
                        //                                tip, "立即购买", "我知道了", , true);
                        //                        PopBoxCreat.createDialogNotitleOneBtn(MainActivity.this, "温馨提示", tip, "确定", new PopBoxCreat.DialogWithTitleClick() {
                        //                            @Override
                        //                            public void onLeftClick() {
                        //
                        //                            }
                        //
                        //                            @Override
                        //                            public void onRightClick() {
                        //
                        //                            }
                        //                        });
                        isTodayFirstLogin();
                    }
                } else {
                    BaseResponseInfo baseResponseInfo = new BaseResponseInfo();
                    baseResponseInfo.setFlag(BaseResponseInfo.ERRO);
                    baseResponseInfo.setInfo(msg);
                    //                    loadonErrorUI(baseResponseInfo);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }


    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        super.onNewIntent(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        deviceisnew = LoginInfo.getDeviceisnew();
        //        deviceisnew=1;
        LogUtils.e("deviceisnew========" + deviceisnew);
    }

    protected String[] needPermissions = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
            //            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            //            Manifest.permission.READ_EXTERNAL_STORAGE,
            //            Manifest.permission.READ_PHONE_STATE
    };

    private void init() {
        mTabHome = $ViewByID(R.id.tab_ll_home);
        mTabCar = $ViewByID(R.id.tab_ll_car);
        mTabRemote = $ViewByID(R.id.tab_ll_remote);
        mTabPie = $ViewByID(R.id.tab_ll_pie);
        mTabSetting = $ViewByID(R.id.tab_ll_setting);
        mTabllPie = $ViewByID(R.id.tab_ll_pie);
        mIvTabHome = $ViewByID(R.id.tab_iv_home);
        mIvTabCar = $ViewByID(R.id.tab_iv_car);
        mIvTabRemote = $ViewByID(R.id.tab_iv_remote);
        mIvTabPie = $ViewByID(R.id.tab_iv_pie);
        mIvTabSetting = $ViewByID(R.id.tab_iv_setting);
        mTxtTabHome = $ViewByID(R.id.tab_txt_home);
        mTxtTabCar = $ViewByID(R.id.tab_txt_car);
        mTxtTabRemote = $ViewByID(R.id.tab_txt_remote);
        mTxtTabPie = $ViewByID(R.id.tab_txt_pie);
        mTxtTabSetting = $ViewByID(R.id.tab_txt_setting);
        mTabHome.setOnClickListener(this);
        mTabCar.setOnClickListener(this);
        mTabRemote.setOnClickListener(this);
        mTabPie.setOnClickListener(this);
        mTabSetting.setOnClickListener(this);

        requestPermissions(this, needPermissions, new BaseActivity.RequestPermissionCallBack() {
            @Override
            public void granted() {
                FileUtil.openOrCreatDir(LocalConfig.mImageCacheSavePath_SD);
                FileUtil.openOrCreatDir(LocalConfig.mImageCacheSavePath_Absolute);
                FileUtil.openOrCreatDir(LocalConfig.mDownLoadFileSavePath_SD);
                FileUtil.openOrCreatDir(LocalConfig.mDownLoadFileSavePath_Absolute);
                FileUtil.openOrCreatDir(LocalConfig.mErroLogSavePath_SD);
                FileUtil.openOrCreatDir(LocalConfig.mTracksSavePath_SD);
            }

            @Override
            public void denied() {
                UUToast.showUUToast(MainActivity.this, "未获取到权限，定位功能不可用");
                UUToast.showUUToast(DorideApplication.getInstanse(), "未获取到权限，存储权限不能用");
            }
        });
        //        requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, new RequestPermissionCallBack() {
        //            @Override
        //            public void granted() {
        //
        //            }
        //
        //            @Override
        //            public void denied() {
        //                UUToast.showUUToast(DorideApplication.getInstanse(), "未获取到权限，存储权限不能用");
        //            }
        //        });
        int tachograph = LoginInfo.getTachograph();
        //        tachograph = 0;
        if (tachograph == 1) {
            //支持记录仪
            mTabllPie.setVisibility(View.VISIBLE);
        } else {
            //不支持
            mTabllPie.setVisibility(View.GONE);
        }
    }


    private void setTabSelection(int index) {
        clearSelection();
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        hideFragments(transaction);
        switch (index) {
            case 0:
                mIvTabHome.setImageResource(R.drawable.tab_home_selected);
                mTxtTabHome.setTextColor(getResources().getColor(R.color.blue_txt));
                if (mHomeFragment == null) {
                    mHomeFragment = new HomeFragment();
                    transaction.add(R.id.content, mHomeFragment);
                } else {
                    transaction.show(mHomeFragment);
                }
                transaction.commit();
                break;
            case 1:
                mIvTabCar.setImageResource(R.drawable.tab_car_selected);
                mTxtTabCar.setTextColor(getResources().getColor(R.color.blue_txt));

                switch (deviceisnew) {
                    case 0:
                        // 不是新车型
                        if (mCarMainFragment == null) {
                            mCarMainFragment = new CarMainFragment();
                            transaction.add(R.id.content, mCarMainFragment);
                        } else {
                            transaction.show(mCarMainFragment);
                        }
                        break;
                    case 1:
                        // 是新车型
                        if (mCarMainFragment2 == null) {
                            mCarMainFragment2 = new CarMainFragment2();
                            transaction.add(R.id.content, mCarMainFragment2);
                        } else {
                            transaction.show(mCarMainFragment2);
                        }
                        break;
                    default:
                        break;
                }

                transaction.commit();
                break;
            case 2:
                mIvTabRemote.setImageResource(R.drawable.tab_remote_selected);
                mTxtTabRemote.setTextColor(getResources().getColor(R.color.blue_txt));
                if (mRemoteMainFragment == null) {
                    mRemoteMainFragment = new RemoteMainFragment();
                    transaction.add(R.id.content, mRemoteMainFragment);
                } else {
                    transaction.show(mRemoteMainFragment);
                }
                transaction.commit();
                break;
            case 3:
                mIvTabPie.setImageResource(R.drawable.tab_pie_selected);
                mTxtTabPie.setTextColor(getResources().getColor(R.color.blue_txt));
                if (mRecorderMainFragemnt == null) {
                    mRecorderMainFragemnt = new RecorderMainFragment();
                    transaction.add(R.id.content, mRecorderMainFragemnt);
                } else {
                    transaction.show(mRecorderMainFragemnt);
                }
                transaction.commit();
                break;
            case 4:
                mIvTabSetting.setImageResource(R.drawable.ic_setting_tab_select);
                mTxtTabSetting.setTextColor(getResources().getColor(R.color.blue_txt));
                if (mSettingMainFragment == null) {
                    mSettingMainFragment = new SettingMainFragment();
                    transaction.add(R.id.content, mSettingMainFragment);
                } else {
                    transaction.show(mSettingMainFragment);
                }
                transaction.commit();
                break;
            default:
                break;
        }
    }

    private void clearSelection() {
        mIvTabHome.setImageResource(R.drawable.tab_home);
        mTxtTabHome.setTextColor(getResources().getColor(R.color.text_color_gray1));
        mIvTabCar.setImageResource(R.drawable.tab_car);
        mTxtTabCar.setTextColor(getResources().getColor(R.color.text_color_gray1));
        mIvTabRemote.setImageResource(R.drawable.tab_remote);
        mTxtTabRemote.setTextColor(getResources().getColor(R.color.text_color_gray1));
        mIvTabPie.setImageResource(R.drawable.tab_pie);
        mTxtTabPie.setTextColor(getResources().getColor(R.color.text_color_gray1));
        mIvTabSetting.setImageResource(R.drawable.ic_setting_tab_normal);
        mTxtTabSetting.setTextColor(getResources().getColor(R.color.text_color_gray1));
    }

    private void hideFragments(FragmentTransaction transaction) {
        if (mHomeFragment != null) {
            transaction.hide(mHomeFragment);
        }
        if (mCarMainFragment != null) {
            //            switch (deviceisnew) {
            //                case 0:
            //                    transaction.hide(mCarMainFragment);
            //                    break;
            //                case 1:
            //                    transaction.hide(mCarMainFragment2);
            //                    break;
            //                default:
            //                    break;
            //            }
            transaction.hide(mCarMainFragment);
        }
        if (mCarMainFragment2 != null) {
            transaction.hide(mCarMainFragment2);
        }
        if (mRemoteMainFragment != null) {
            transaction.hide(mRemoteMainFragment);
        }
        if (mRecorderMainFragemnt != null) {
            transaction.hide(mRecorderMainFragemnt);
        }
        if (mSettingMainFragment != null) {
            transaction.hide(mSettingMainFragment);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            ActivityControl.exit(this);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tab_ll_home:
                setTabSelection(0);
                break;
            case R.id.tab_ll_car:
                setTabSelection(1);
                break;
            case R.id.tab_ll_remote:
                setTabSelection(2);
                break;
            case R.id.tab_ll_pie:
                setTabSelection(3);
                break;
            case R.id.tab_ll_setting:
                setTabSelection(4);
                break;
        }
    }

    private void isTodayFirstLogin() {
        String todayTime = null;
        // 取
        SharedPreferences preferences = getSharedPreferences("LastLoginTime",
                MODE_PRIVATE);
        String lastTime = preferences.getString(
                "LoginTime" + LoginInfo.getMobile(), "2018-04-02");
        // Toast.makeText(MainActivity.this, "value="+date,
        // Toast.LENGTH_SHORT).show();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");// 设置日期格式
        todayTime = df.format(new Date());// 获取当前的日期
        LogUtils.e("lastTime----" + lastTime);
        LogUtils.e("todayTime---" + todayTime);
        LogUtils.e("DorideApplication.isTrafficTipsShow---" + DorideApplication.isTrafficTipsShow);
        if (!lastTime.equals(todayTime) && DorideApplication.isTrafficTipsShow) { // 如果两个时间段相等
            com.carlt.doride.ui.view.PopBoxCreat.createTrafficDialogNotitle(MainActivity.this, false);
            ActivityControl.saveExitTime();
        }
    }
}

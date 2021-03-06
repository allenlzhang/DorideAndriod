package com.carlt.doride;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.carlt.doride.base.BaseActivity;
import com.carlt.doride.control.ActivityControl;
import com.carlt.doride.data.BaseResponseInfo;
import com.carlt.doride.data.carflow.CheckBindCarIdInfo;
import com.carlt.doride.data.flow.TrafficPackageWarnningInfo;
import com.carlt.doride.data.remote.RemoteMainInfo;
import com.carlt.doride.http.retrofitnet.model.GetCarInfo;
import com.carlt.doride.http.retrofitnet.model.OtherInfo;
import com.carlt.doride.http.retrofitnet.model.UserInfo;
import com.carlt.doride.protocolparser.BaseParser;
import com.carlt.doride.protocolparser.CarOperationConfigParser;
import com.carlt.doride.systemconfig.URLConfig;
import com.carlt.doride.ui.fragment.CarMainFragment;
import com.carlt.doride.ui.fragment.CarMainFragment2;
import com.carlt.doride.ui.fragment.FragmentFactory;
import com.carlt.doride.ui.fragment.HomeFragment;
import com.carlt.doride.ui.fragment.RecorderMainFragment;
import com.carlt.doride.ui.fragment.RemoteMainFragment;
import com.carlt.doride.ui.fragment.SettingMainFragment;
import com.carlt.doride.ui.view.UUToast;
import com.carlt.doride.utils.FileUtil;
import com.carlt.doride.utils.ILog;
import com.carlt.doride.utils.LocalConfig;
import com.carlt.sesame.preference.TokenInfo;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

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

    /**
     * 记录仪升级文件路径
     */
    public static String localUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //        initSavedInstanceState(savedInstanceState);
        //        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);
        init();
        mFragmentManager = getSupportFragmentManager();
        setTabSelection(0);
        //        if (LoginInfo.getTbox_type().equals("4G")) {
        initFlowInfo();
        //        }
        //        initCarFlow();
    }


    private String carid = "2216301";

    private void initCarFlow() {
        //        carid = LoginInfo.getCarid();
        OkGo.<String>post(URLConfig.getCAR_CHECK_BIND_URL())
                .params("carid", Integer.valueOf(carid))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        LogUtils.e(response.body());
                        //                        parseCheckJson(response);
                        //                        loadingDialog.dismiss();
                        Gson gson = new Gson();
                        CheckBindCarIdInfo checkBindInfo = gson.fromJson(response.body(), CheckBindCarIdInfo.class);
                        if (checkBindInfo.code == 0) {
                            if (checkBindInfo.data != null) {
                            }
                            //                                llCarFlowRecharge.setVisibility(View.VISIBLE);
                            //                                lineCarFlow.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        LogUtils.e(response);
                        //                        loadingDialog.dismiss();
                    }
                });
    }

    private void initFlowInfo() {
        //        getFlowProductList();
        //        loadingDataUI();
        OkGo.<String>post(URLConfig.getmTrafficWarnningUrl())
                .params("client_id", URLConfig.getClientID())
                .params("dealerId", UserInfo.getInstance().dealerId)
                .params("token", TokenInfo.getToken())
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
                    OtherInfo.getInstance().setLimit_warning(warnningInfo.data.limit_warning);
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
        deviceisnew = GetCarInfo.getInstance().dorcenCarDisplay;
        //        deviceisnew = 0;
        try {
            localUrl = getIntent().getExtras().getString("filePath");

        } catch (Exception e) {
            // TODO: handle exception
        }
        Log.e("localUrl", "onResume: " + localUrl);
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

        remoteConfig();


    }

    //    private void initSavedInstanceState(Bundle savedInstanceState) {
    //        if (savedInstanceState != null) {
    //            int currentIndex = savedInstanceState.getInt("currentItem", 0);
    //            setTabSelection(currentIndex);
    //        }
    //
    //    }

    //    private int mCurrentItem = 0;
    //
    //    @Override
    //    protected void onSaveInstanceState(Bundle outState) {
    //        super.onSaveInstanceState(outState);
    //        outState.putInt("currentItem", mCurrentItem);
    //
    //    }

    //    @Override
    //    protected void onRestoreInstanceState(Bundle savedInstanceState) {
    //        super.onRestoreInstanceState(savedInstanceState);
    //        int currentIndex = savedInstanceState.getInt("currentItem", 0);
    //        setTabSelection(currentIndex);
    //    }

    CarOperationConfigParser carOperationConfigParser;

    private void remoteConfig() {
        //        String activateCode = getIntent().getStringExtra("activateCode");
        //        Logger.e("========" + activateCode);
        //        if (TextUtils.isEmpty(activateCode)) {


        //        if (DorideApplication.getInstanse().getRemoteMainInfo() == null) {
        carOperationConfigParser = new CarOperationConfigParser<String>(new BaseParser.ResultCallback() {
            @Override
            public void onSuccess(BaseResponseInfo bInfo) {
                DorideApplication.getInstanse().setRemoteMainInfo(carOperationConfigParser.getReturn());


                ILog.e(TAG, "onSuccess parser2 " + carOperationConfigParser.getReturn());
                //                loadSuss();
                if (carOperationConfigParser != null) {
                    RemoteMainInfo aReturn = carOperationConfigParser.getReturn();
                    Logger.e("hasTachograph=============" + aReturn.hasTachograph);
                    if (aReturn.hasTachograph == 1) {
                        //支持记录仪
                        mTabllPie.setVisibility(View.VISIBLE);
                    } else {
                        //不支持
                        mTabllPie.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onError(BaseResponseInfo bInfo) {
                ILog.e(TAG, "onError" + bInfo.toString());
                //                loadonErrorUI((BaseResponseInfo) bInfo);
                //                UUToast.showUUToast(getActivity(), bInfo.getInfo());
            }
        });
        HashMap params2 = new HashMap();
        String m_car_curcarconfig_url = URLConfig.getM_CAR_CURCARCONFIG_URL();
        String replace = m_car_curcarconfig_url.replace("126", "130");
        carOperationConfigParser.executePost(replace, params2);
        //        } else {
        //            mTabllPie.setVisibility(View.GONE);
        //        }
    }


    private void setTabSelection(int index) {
        //        mCurrentItem = index;
        clearSelection();
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        hideFragments(transaction);
        switch (index) {
            case 0:
                mIvTabHome.setImageResource(R.drawable.tab_home_selected);
                mTxtTabHome.setTextColor(getResources().getColor(R.color.blue_txt));
                //                setIndexFragment(0);
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
                //                setIndexFragment(1);
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
                //                setIndexFragment(2);
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
                //                setIndexFragment(3);
                if (mRecorderMainFragemnt == null) {
                    mRecorderMainFragemnt = new RecorderMainFragment();
                    transaction.add(R.id.content, mRecorderMainFragemnt);
                } else {
                    transaction.show(mRecorderMainFragemnt);
                }
                mRecorderMainFragemnt.upGradeFilePath = localUrl;
                transaction.commit();
                break;
            case 4:
                //                setIndexFragment(4);
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

    private void setIndexFragment(int index) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.content, FragmentFactory.getFragment(index));
        ft.commitAllowingStateLoss();
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
                if (mHomeFragment != null && mHomeFragment.isVisible()) {
                    return;
                }
                setTabSelection(0);
                break;
            case R.id.tab_ll_car:
                if (mCarMainFragment != null && mCarMainFragment.isVisible()) {
                    return;
                }
                if (mCarMainFragment2 != null && mCarMainFragment2.isVisible()) {
                    return;
                }
                setTabSelection(1);
                break;
            case R.id.tab_ll_remote:
                if (mRemoteMainFragment != null && mRemoteMainFragment.isVisible()) {
                    return;
                }
                setTabSelection(2);
                break;
            case R.id.tab_ll_pie:
                if (mRecorderMainFragemnt != null && mRecorderMainFragemnt.isVisible()) {
                    return;
                }
                setTabSelection(3);
                break;
            case R.id.tab_ll_setting:
                if (mSettingMainFragment != null && mSettingMainFragment.isVisible()) {
                    return;
                }
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
                "LoginTime" + UserInfo.getInstance().mobile, "2018-04-02");
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

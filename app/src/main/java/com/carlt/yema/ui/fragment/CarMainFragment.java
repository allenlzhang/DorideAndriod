package com.carlt.yema.ui.fragment;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.carlt.yema.R;
import com.carlt.yema.YemaApplication;
import com.carlt.yema.base.BaseActivity;
import com.carlt.yema.base.BaseFragment;
import com.carlt.yema.data.BaseResponseInfo;
import com.carlt.yema.data.car.CarIndexInfo;
import com.carlt.yema.data.remote.RemoteFunInfo;
import com.carlt.yema.protocolparser.BaseParser;
import com.carlt.yema.protocolparser.CarOperationConfigParser;
import com.carlt.yema.protocolparser.DefaultParser;
import com.carlt.yema.systemconfig.URLConfig;
import com.carlt.yema.ui.activity.carstate.CarSaftyListActivity;
import com.carlt.yema.ui.activity.carstate.CarStateNowActivity;
import com.carlt.yema.ui.activity.carstate.CarTiresStateActivity;
import com.carlt.yema.ui.activity.carstate.FindCarActivity;
import com.carlt.yema.ui.activity.carstate.LocationSynchronizeActivity;
import com.carlt.yema.ui.activity.carstate.MainTestingActivity;
import com.carlt.yema.ui.view.UUToast;
import com.carlt.yema.utils.ILog;
import com.carlt.yema.utils.StringUtils;

import java.util.HashMap;

/**
 * Created by liu on 2018/3/16.
 */

public class CarMainFragment extends BaseFragment implements View.OnClickListener {
    private static String TAG = "CarMainFragment";

    private CarIndexInfo carinfo;
    private TextView view1;
    private TextView view2;
    private TextView view3;
    private View viewSafetyLay;
    private View viewRedDot;
    private TextView viewSafetyText;
    private View viewMainTainLay;
    private View viewMainState;
    private TextView headTxt;
    private TextView titleTV;
    public final static String CARMAIN_SAFETY = "com.carlt.yema.carmain.safety";// 安防action

    private CarmainBroadCastReceiver mReceiver;
    private boolean isTire;
    private boolean isCarlocation;

    @Override
    protected View inflateView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.layout_carmain, null, false);
        return view;
    }

    @Override
    public void init(View view) {
        //  胎压监测
        view1 = $ViewByID(R.id.car_main_txt_tire);
        // 定位寻车
        view2 = $ViewByID(R.id.car_main_txt_findcar);
        //导航同步
        view3 = $ViewByID(R.id.car_main_txt_carlocation);
        //安防提醒
        viewSafetyLay = $ViewByID(R.id.car_main_lay_safety);
        viewRedDot = $ViewByID(R.id.car_main_lay_safety_lay2_dot2);
        viewSafetyText = $ViewByID(R.id.car_main_txt_safety);
        //车况检测
        viewMainTainLay = $ViewByID(R.id.car_main_lay_maintain);
        //实时车况
        viewMainState = $ViewByID(R.id.car_state_iv);
        titleTV = $ViewByID(R.id.carmian_title);
        headTxt = $ViewByID(R.id.layout_sub_head_txt);//
        titleTV.setText("野马汽车品牌");
        view1.setOnClickListener(this);
        view2.setOnClickListener(this);
        view3.setOnClickListener(this);
        viewSafetyLay.setOnClickListener(this);
        viewMainTainLay.setOnClickListener(this);
        viewMainState.setOnClickListener(this);
        isTire = false;
        isCarlocation = false;
        mReceiver = new CarmainBroadCastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(CARMAIN_SAFETY);
        getActivity().registerReceiver(mReceiver, filter);
    }

    class CarmainBroadCastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            loadData();
        }

    }


    CarOperationConfigParser carOperationConfigParser;


    @Override
    public void loadData() {
        //CARINDEX 以及支持的配置项
        BaseParser parser = new DefaultParser<CarIndexInfo>(new BaseParser.ResultCallback() {
            @Override
            public void onSuccess(BaseResponseInfo bInfo) {
                carinfo = ((BaseResponseInfo<CarIndexInfo>) bInfo).getValue();
                remoteConfig();
                ILog.e(TAG, "onSuccess" + bInfo.toString());
            }

            @Override
            public void onError(BaseResponseInfo bInfo) {
                ILog.e(TAG, "onError" + bInfo.toString());
                actLoadError((BaseResponseInfo) bInfo);
            }
        }, CarIndexInfo.class);
        HashMap params = new HashMap();
        parser.executePost(URLConfig.getM_CAR_MAIN_URL(), params);
    }

    private void remoteConfig() {
        if (YemaApplication.getInstanse().getRemoteMainInfo() == null) {
            carOperationConfigParser = new CarOperationConfigParser<String>(new BaseParser.ResultCallback() {
                @Override
                public void onSuccess(BaseResponseInfo bInfo) {
                    YemaApplication.getInstanse().setRemoteMainInfo(carOperationConfigParser.getReturn());
                    ILog.e(TAG, "onSuccess parser2 " + carOperationConfigParser.getReturn());
                    loadSuss();
                }

                @Override
                public void onError(BaseResponseInfo bInfo) {
                    ILog.e(TAG, "onError" + bInfo.toString());
                    actLoadError((BaseResponseInfo) bInfo);
                }
            });
            HashMap params2 = new HashMap();
            carOperationConfigParser.executePost(URLConfig.getM_CAR_CURCARCONFIG_URL(), params2);
        } else {
            loadSuss();
        }
    }

    private void loadSuss() {
        if (null != carinfo) {
            if (!TextUtils.isEmpty(carinfo.getCarname())) {
                titleTV.setText(carinfo.getCarname());
            }
            if (TextUtils.equals("1", carinfo.getIsrunning())) {//1 表示行驶中，0 表示不在行驶中
                headTxt.setText("您的爱车正在行驶中");
            } else if (TextUtils.equals("0", carinfo.getIsrunning())) {
                headTxt.setText("您的爱车正在休息");
            }
            if (!StringUtils.isEmpty(carinfo.getSafetymsg())) {
                viewSafetyText.setText(carinfo.getSafetymsg());
            } else {
                viewSafetyText.setText("暂无新消息");
            }
        }
        //是否支持胎压监测
        if (TextUtils.equals(YemaApplication.getInstanse().getRemoteMainInfo().getDirectPSTsupervise(), RemoteFunInfo.STATE_SUPPORT)) {
            isTire = true;
            Drawable top = getResources().getDrawable(R.drawable.tire_car_main_selecter);
            view1.setCompoundDrawablesWithIntrinsicBounds(null, top, null, null);
        } else {
            isTire = false;
            Drawable top = getResources().getDrawable(R.mipmap.tire_car_main_unpress);
            view1.setCompoundDrawablesWithIntrinsicBounds(null, top, null, null);
        }
        //是否支持导航同步
        if (TextUtils.equals(YemaApplication.getInstanse().getRemoteMainInfo().getNavigationSync(), RemoteFunInfo.STATE_SUPPORT)) {
            isCarlocation = true;
            Drawable top = getResources().getDrawable(R.drawable.daohang_car_main_selecter);
            view3.setCompoundDrawablesWithIntrinsicBounds(null, top, null, null);
        } else {
            isCarlocation = false;
            Drawable top = getResources().getDrawable(R.mipmap.navigate_carmain_unpress);
            view3.setCompoundDrawablesWithIntrinsicBounds(null, top, null, null);
        }


    }

    /**
     * 需要进行检测的权限数组
     */
    protected String[] needPermissions = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
//            Manifest.permission.WRITE_EXTERNAL_STORAGE,
//            Manifest.permission.READ_EXTERNAL_STORAGE,
//            Manifest.permission.READ_PHONE_STATE
    };

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.car_main_txt_tire://胎压监测
                if (isTire) {
                    Intent mIntent = new Intent(getActivity(), CarTiresStateActivity.class);
                    startActivity(mIntent);
                } else {
                    UUToast.showUUToast(getContext(), "暂不支持该功能");
                }
                break;
            case R.id.car_main_txt_findcar://定位寻车
                ((BaseActivity) getActivity()).requestPermissions(getActivity(), needPermissions, new BaseActivity.RequestPermissionCallBack() {
                    @Override
                    public void granted() {
                        Intent mIntent1 = new Intent(getActivity(), FindCarActivity.class);
                        startActivity(mIntent1);
                    }

                    @Override
                    public void denied() {
                        UUToast.showUUToast(getActivity(), "未获取到权限，定位功能不可用");
                    }
                });

                break;
            case R.id.car_main_txt_carlocation://导航同步
                if (isCarlocation) {
                    ((BaseActivity) getActivity()).requestPermissions(getActivity(), needPermissions, new BaseActivity.RequestPermissionCallBack() {
                        @Override
                        public void granted() {
                            Intent mIntent2 = new Intent(getActivity(), LocationSynchronizeActivity.class);
                            startActivity(mIntent2);
                        }

                        @Override
                        public void denied() {
                            UUToast.showUUToast(getActivity(), "未获取到权限，导航同步功能不可用");
                        }
                    });
                } else {
                    UUToast.showUUToast(getContext(), "暂不支持该功能");
                }

                break;
            case R.id.car_main_lay_safety://安防提醒
                Intent mIntent3 = new Intent(getActivity(), CarSaftyListActivity.class);
                mIntent3.putExtra("safetymsg", carinfo.getSafetymsg());
                startActivity(mIntent3);
                break;
            case R.id.car_main_lay_maintain://车况检测报告
                Intent mIntent4 = new Intent(getActivity(), MainTestingActivity.class);
                startActivity(mIntent4);
                break;
            case R.id.car_state_iv:
                Intent mIntent5 = new Intent(getActivity(), CarStateNowActivity.class);
                startActivity(mIntent5);
                break;
            default:
                break;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        try {
            getActivity().unregisterReceiver(mReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

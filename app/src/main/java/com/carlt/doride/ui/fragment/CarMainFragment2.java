package com.carlt.doride.ui.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.LogUtils;
import com.carlt.chelepie.control.WIFIControl;
import com.carlt.doride.DorideApplication;
import com.carlt.doride.R;
import com.carlt.doride.base.BaseActivity;
import com.carlt.doride.base.BaseFragment;
import com.carlt.doride.control.CPControl;
import com.carlt.doride.data.BaseResponseInfo;
import com.carlt.doride.data.PictrueInfo;
import com.carlt.doride.data.car.CarIndexInfo;
import com.carlt.doride.data.car.CarMilesInfo;
import com.carlt.doride.data.remote.RemoteFunInfo;
import com.carlt.doride.protocolparser.BaseParser;
import com.carlt.doride.protocolparser.CarOperationConfigParser;
import com.carlt.doride.protocolparser.DefaultParser;
import com.carlt.doride.protocolparser.DefaultStringParser;
import com.carlt.doride.systemconfig.URLConfig;
import com.carlt.doride.ui.activity.carstate.CarSaftyListActivity;
import com.carlt.doride.ui.activity.carstate.CarStateNowActivity;
import com.carlt.doride.ui.activity.carstate.CarTiresStateActivity;
import com.carlt.doride.ui.activity.carstate.FindCarActivity;
import com.carlt.doride.ui.activity.carstate.LocationSynchronizeActivity;
import com.carlt.doride.ui.activity.carstate.MainTestingActivity;
import com.carlt.doride.ui.view.UUToast;
import com.carlt.doride.utils.ILog;
import com.carlt.doride.utils.LoadLocalImageUtil;
import com.carlt.doride.utils.StringUtils;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import java.util.HashMap;

/**
 * Created by liu on 2018/3/16.
 */

public class CarMainFragment2 extends BaseFragment implements View.OnClickListener,WIFIControl.WIFIConnectListener {
    private static String TAG = "CarMainFragment";

    private CarIndexInfo carinfo;
    private TextView     view1;
    private TextView     view2;
    private TextView     view3;
    private View         viewSafetyLay;
    private View         viewRedDot;
    private TextView     viewSafetyText;
    private View         viewMainTainLay;
    private ImageView    viewMainState;
    private TextView     headTxt;
    private TextView     titleTV;
    public final static String CARMAIN_SAFETY = "com.carlt.doride.carmain.safety";// 安防action

    //    private CarmainBroadCastReceiver mReceiver;
    private boolean   isTire;
    private boolean   isCarlocation;
    private TextView  tvOil;
    private TextView  tvRenewal;
    private TextView  tvBattery;
    private TextView  tvCarState;
    private TextView  tvScore;
    private ImageView ivBg;

    @Override
    protected View inflateView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_carmain_layout2, null, false);
        return view;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            WIFIControl.rigisterWIFIConnectListener(this);
            WIFIControl.DisConnectChelePai();
            loadData();
            //            remoteConfig();
        }else {
            WIFIControl.unRigisterWIFIConnectListener(this);
        }
    }

    private View mLoadingView;// 加载View

    @Override
    public void init(View view) {
        getActivateStatus("正在激活中");
        mLoadingView = view.findViewById(R.id.laoding_lay_main);
        ivBg = view.findViewById(R.id.ivBg);

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
        //        油量
        tvOil = $ViewByID(R.id.tvOil);
        //        里程
        tvRenewal = $ViewByID(R.id.tvRenewal);
        //        电量
        tvBattery = $ViewByID(R.id.tvBattery);
        //        汽车状态
        tvCarState = $ViewByID(R.id.tvCarState);
        tvScore = $ViewByID(R.id.car_main_txt_maintain);
        titleTV.setText("大乘汽车品牌");
        view1.setOnClickListener(this);
        view2.setOnClickListener(this);
        view3.setOnClickListener(this);
        viewSafetyLay.setOnClickListener(this);
        viewMainTainLay.setOnClickListener(this);
        viewMainState.setOnClickListener(this);
        isTire = false;
        isCarlocation = false;
        //        mReceiver = new CarmainBroadCastReceiver();
        //        IntentFilter filter = new IntentFilter();
        //        filter.addAction(CARMAIN_SAFETY);
        //        getActivity().registerReceiver(mReceiver, filter);

    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }

    @Override
    public void onWIFIChange(int action) {
        mHandler.sendEmptyMessage(action);
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
        upgradeProgram();
        loadingDataUI();
        BaseParser parser = new DefaultParser<CarIndexInfo>(new BaseParser.ResultCallback() {
            @Override
            public void onSuccess(BaseResponseInfo bInfo) {
                loadSuccessUI();
                carinfo = ((BaseResponseInfo<CarIndexInfo>) bInfo).getValue();
                remoteConfig();
                ILog.e(TAG, "onSuccess---" + bInfo.toString());
            }

            @Override
            public void onError(BaseResponseInfo bInfo) {
                ILog.e(TAG, "onError" + bInfo.toString());
                UUToast.showUUToast(getActivity(), bInfo.getInfo());
                loadonErrorUI((BaseResponseInfo) bInfo);
            }
        }, CarIndexInfo.class);
        HashMap params = new HashMap();
        parser.executePost(URLConfig.getM_CAR_MAIN_URL(), params);

        getBgImage();
        getCarImage();

        CPControl.getMilesInfos(mListener);

    }

    private void getCarImage() {
        BaseParser parser1 = new DefaultStringParser(new BaseParser.ResultCallback() {
            @Override
            public void onSuccess(BaseResponseInfo bInfo) {
                LogUtils.e("parser1=======" + bInfo.toString());
                String json = bInfo.getValue().toString();
                //                parseJson1(json);
                Gson gson = new Gson();
                PictrueInfo pictrueInfo = gson.fromJson(json, PictrueInfo.class);
                LoadLocalImageUtil.getInstance().displayFromWeb(pictrueInfo.filePath, viewMainState, R.drawable.car_state);
            }

            @Override
            public void onError(BaseResponseInfo bInfo) {

            }
        });
        HashMap params1 = new HashMap();
        params1.put("app_softtype", "1");
        params1.put("position", "21");
        parser1.executePost(URLConfig.getM_GET_APPSPICS_URL(), params1);
    }


    private void getBgImage() {
        BaseParser parser1 = new DefaultStringParser(new BaseParser.ResultCallback() {
            @Override
            public void onSuccess(BaseResponseInfo bInfo) {
                LogUtils.e("parser1=======" + bInfo.toString());
                String json = bInfo.getValue().toString();
                Gson gson = new Gson();
                PictrueInfo pictrueInfo = gson.fromJson(json, PictrueInfo.class);
                LoadLocalImageUtil.getInstance().displayFromWeb(pictrueInfo.filePath, ivBg, R.drawable.bg);
            }

            @Override
            public void onError(BaseResponseInfo bInfo) {

            }
        });
        HashMap params1 = new HashMap();
        params1.put("app_softtype", "1");
        params1.put("position", "20");
        parser1.executePost(URLConfig.getM_GET_APPSPICS_URL(), params1);
    }


    //    private void loadingOverUI() {
    //        mLoadingView.setVisibility(View.GONE);
    //    }
    //
    //    protected void loadingUI() {
    //        mLoadingView.setBackgroundResource(R.drawable.transparent_bg);
    //        mLoadingView.setVisibility(View.VISIBLE);
    //    }

    @SuppressLint("HandlerLeak")
    private Handler                   mHandler  = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    //                    请求成功
                    BaseResponseInfo info = (BaseResponseInfo) msg.obj;
                    Gson gson = new Gson();
                    CarMilesInfo carMilesInfo = gson.fromJson(info.getValue().toString(), CarMilesInfo.class);
                    Logger.e(carMilesInfo.toString());
                    if (TextUtils.isEmpty(carMilesInfo.leftFuel)) {
                        tvOil.setText("--");
                    } else {
                        tvOil.setText(carMilesInfo.leftFuel);

                    }

                    if (TextUtils.isEmpty(carMilesInfo.maxEnduranceMile) || TextUtils.isEmpty(carMilesInfo.minEnduranceMile)) {
                        if (TextUtils.isEmpty(carMilesInfo.enduranceMile)) {
                            tvRenewal.setText("--");
                        } else {
                            tvRenewal.setText(carMilesInfo.enduranceMile);
                        }
                    } else {
                        tvRenewal.setText(carMilesInfo.minEnduranceMile.concat("-").concat(carMilesInfo.maxEnduranceMile));
                    }


//                    if (TextUtils.isEmpty(carMilesInfo.enduranceMile)) {
//                        tvRenewal.setText("--");
//                    } else {
//                        tvRenewal.setText(carMilesInfo.enduranceMile);
//                    }

                    if (TextUtils.isEmpty(carMilesInfo.vBat)) {
                        tvBattery.setText("--");
                    } else {
                        tvBattery.setText(carMilesInfo.vBat);

                    }
                    break;
                case 1:
                    //                    请求失败
                    tvOil.setText("--");
                    tvRenewal.setText("--");
                    tvBattery.setText("--");
                    break;
                default:
                    break;
            }
        }
    };
    private BaseParser.ResultCallback mListener = new BaseParser.ResultCallback() {

        @Override
        public void onSuccess(BaseResponseInfo bInfo) {
            //            mHandler.sendEmptyMessage(0);
            Message message = Message.obtain();
            message.obj = bInfo;
            message.what = 0;
            mHandler.sendMessage(message);
            Logger.e(bInfo.toString());
        }

        @Override
        public void onError(BaseResponseInfo bInfo) {
            Logger.e(bInfo.toString());
            Message msg = new Message();
            msg.what = 1;
            msg.obj = bInfo;
            mHandler.sendMessage(msg);
        }
    };

    private void remoteConfig() {
        //        if (DorideApplication.getInstanse().getRemoteMainInfo() == null) {
        carOperationConfigParser = new CarOperationConfigParser<String>(new BaseParser.ResultCallback() {
            @Override
            public void onSuccess(BaseResponseInfo bInfo) {
                DorideApplication.getInstanse().setRemoteMainInfo(carOperationConfigParser.getReturn());
                ILog.e(TAG, "onSuccess parser2 " + carOperationConfigParser.getReturn());
                loadSuss();
            }

            @Override
            public void onError(BaseResponseInfo bInfo) {
                ILog.e(TAG, "onError" + bInfo.toString());
                loadonErrorUI((BaseResponseInfo) bInfo);
                UUToast.showUUToast(getActivity(), bInfo.getInfo());
            }
        });
        HashMap params2 = new HashMap();
        carOperationConfigParser.executePost(URLConfig.getM_CAR_CURCARCONFIG_URL(), params2);
        //        } else {
        //            loadSuss();
        //        }
    }

    private void loadSuss() {
        loadSuccessUI();
        if (null != carinfo) {
            if (!TextUtils.isEmpty(carinfo.getCarname())) {
                titleTV.setSelected(true);
                titleTV.setText(carinfo.getCarname());

            }
            if (TextUtils.equals("1", carinfo.getIsrunning())) {//1 表示行驶中，0 表示不在行驶中
                headTxt.setText("您的爱车正在行驶中");
                tvCarState.setText("爱车行驶中");
            } else if (TextUtils.equals("0", carinfo.getIsrunning())) {
                headTxt.setText("爱车休息中");
                tvCarState.setText("爱车休息中");
            }else if(carinfo.getIsrunning().equals("2")){
                headTxt.setText("爱车已上电");
                tvCarState.setText("爱车已上电");
            }
            if (!StringUtils.isEmpty(carinfo.getSafetymsg())) {
                viewSafetyText.setText(carinfo.getSafetymsg());
            } else {
                viewSafetyText.setText("暂无新消息");
            }
            int last_fault_score = carinfo.last_fault_score;
            if (last_fault_score == -1) {
                tvScore.setText("请先进行检测");
            } else {
                tvScore.setText("上次自检得分：" + String.valueOf(last_fault_score));
            }

        }
        //是否支持胎压监测
        if (TextUtils.equals(DorideApplication.getInstanse().getRemoteMainInfo().getDirectPSTsupervise(), RemoteFunInfo.STATE_SUPPORT)) {
            isTire = true;
            Drawable top = getResources().getDrawable(R.drawable.tire_car_main_selecter);
            view1.setCompoundDrawablesWithIntrinsicBounds(null, top, null, null);
        } else {
            isTire = false;
            Drawable top = getResources().getDrawable(R.mipmap.tire_car_main_unpress);
            view1.setCompoundDrawablesWithIntrinsicBounds(null, top, null, null);
        }
        //是否支持导航同步
        if (TextUtils.equals(DorideApplication.getInstanse().getRemoteMainInfo().getNavigationSync(), RemoteFunInfo.STATE_SUPPORT)) {
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
                    UUToast.showUUToast(getActivity(), "暂不支持该功能");
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
                    UUToast.showUUToast(getActivity(), "暂不支持该功能");
                }

                break;
            case R.id.car_main_lay_safety://安防提醒
                if (null == carinfo) {
                    UUToast.showUUToast(getActivity(), "获取失败", Toast.LENGTH_SHORT);
                    return;
                }
                Intent mIntent3 = new Intent(getActivity(), CarSaftyListActivity.class);
                mIntent3.putExtra("safetymsg", carinfo.getSafetymsg());
                startActivity(mIntent3);
                break;
            case R.id.car_main_lay_maintain://车况检测报告
                Intent mIntent4 = new Intent(getActivity(), MainTestingActivity.class);
                startActivity(mIntent4);
                break;
            case R.id.car_state_iv:
                //                实时车况
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
            //            getActivity().unregisterReceiver(mReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

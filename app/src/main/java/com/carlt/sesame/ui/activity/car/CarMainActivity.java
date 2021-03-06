package com.carlt.sesame.ui.activity.car;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.carlt.doride.DorideApplication;
import com.carlt.doride.R;
import com.carlt.doride.data.PictrueInfo;
import com.carlt.doride.http.retrofitnet.BaseMvcObserver;
import com.carlt.doride.http.retrofitnet.model.CarConfigRes;
import com.carlt.doride.http.retrofitnet.model.GetCarInfo;
import com.carlt.doride.http.retrofitnet.model.OtherInfo;
import com.carlt.doride.http.retrofitnet.model.RemoteCarStateInfo;
import com.carlt.doride.protocolparser.BaseParser;
import com.carlt.doride.protocolparser.DefaultStringParser;
import com.carlt.doride.utils.LoadLocalImageUtil;
import com.carlt.sesame.control.ActivityControl;
import com.carlt.sesame.control.CPControl;
import com.carlt.sesame.control.CPControl.GetResultListCallback;
import com.carlt.sesame.data.BaseResponseInfo;
import com.carlt.sesame.data.SesameLoginInfo;
import com.carlt.sesame.data.car.CarMainFunInfo;
import com.carlt.sesame.data.car.CarMainFuncInfo;
import com.carlt.sesame.data.car.CarMainInfo;
import com.carlt.sesame.data.remote.CarStateInfo;
import com.carlt.sesame.data.remote.RemoteFunInfo;
import com.carlt.sesame.protocolstack.remote.CarStateParser;
import com.carlt.sesame.systemconfig.OnDateChageConfig;
import com.carlt.sesame.ui.activity.base.BaseActivity;
import com.carlt.sesame.ui.activity.base.LoadingActivityWithTitle;
import com.carlt.sesame.ui.activity.career.MaintainLogActivity;
import com.carlt.sesame.ui.adapter.car.CarMainAdapter;
import com.carlt.sesame.ui.view.MyGridView;
import com.carlt.sesame.ui.view.PopBoxCreat;
import com.carlt.sesame.ui.view.SectorView;
import com.carlt.sesame.utility.MyParse;
import com.carlt.sesame.utility.UUToast;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class CarMainActivity extends LoadingActivityWithTitle implements
        OnClickListener {

    private ImageView mImageViewSecretary;// 车秘书头像
    private ImageView ivCarMainBg;// 车秘书头像

    private TextView mTextViewSecretary;// 提醒消息

    private TextView mTextViewHead;// 标题头文字

    private TextView mCarState;// 车辆状态

    // 一般品牌显示UI

    private TextView mTxtSoc;// 电池电量

    private TextView mTxtSoh;// 电池健康度

    private View mProSoc;// 电池电量

    private View mProSocOut;// 电池电量背景

    private View mProSoh;// 电池健康度

    private View mProSohOut;// 电池健康度背景

    private View mState;//实时车况整个View

    private SectorView mSvSoc;// 电池电量

    private SectorView mSvSoh;// 电池健康度

    private MyGridView mGridFuncs;// 底部功能按钮grid
    private TextView   carMainTxtCarstate;

    private CarMainInfo mCarMainInfo;

    private Dialog mDialog;

    private CarmainBroadCastReceiver mReceiver;

    private CarMainAdapter mAdapter;

    private ArrayList<CarMainFuncInfo> mCarMainFuncInfos;
    private ArrayList<CarMainFuncInfo> mCarMainFuncInfosInit;

    public final static String CARMAIN_SAFETY  = "com.hz17car.carmain.safety";// 安防action
    public final static String CARMAIN_CARYEAR = "com.hz17car.carmain.caryear";// 车款action

    private String funcNames[]     = {"一键求援", "违章查询", "安防提醒", "定位寻车", "导航同步", "胎压监测"};
    private String funcNames2016[] = {"一键求援", "违章查询", "安防提醒"};
    private int    icons[]         = {R.drawable.icon_phone, R.drawable.icon_magnifier,
            R.drawable.icon_safety, R.drawable.icon_findcar,
            R.drawable.icon_navigation, R.drawable.icon_tire};

    private int     iconsNormal[] = {R.drawable.icon_phone_normal, R.drawable.icon_magnifier_normal,
            R.drawable.icon_safety_normal, R.drawable.icon_findcar_normal,
            R.drawable.icon_navigation_normal, R.drawable.icon_tire_selected_normal};
    private int     icons2016[]   = {R.drawable.icon_phone, R.drawable.icon_magnifier, R.drawable.icon_safety};
    private boolean isShowDots[]  = {false, false, false, false, false, false};

    private int    count;
    private String clickSize; //用来统计可以点击的 图标个数

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_main);
        setTitleView(R.layout.head_car_main);
        // 生成广播处理
        mReceiver = new CarmainBroadCastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(CARMAIN_SAFETY);
        filter.addAction(CARMAIN_CARYEAR);
        registerReceiver(mReceiver, filter);
        initTitle();
        initSubTitle();
        init();
        LoadData();
    }

    class CarmainBroadCastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            CPControl.GetCarMainResult(listener);
        }

    }

    private void initTitle() {
        mTextViewHead = (TextView) findViewById(R.id.car_main_txt_head2);
        mCarState = (TextView) findViewById(R.id.car_main_txt_state);
        mCarState.setOnClickListener(this);
        if (GetCarInfo.getInstance().carName != null
                && !GetCarInfo.getInstance().carName.equals("")) {
            mTextViewHead.setText(GetCarInfo.getInstance().carName);
        }
    }

    private void initSubTitle() {
        mImageViewSecretary = (ImageView) findViewById(R.id.layout_sub_head_img);
        mTextViewSecretary = (TextView) findViewById(R.id.layout_sub_head_txt);
    }

    private void init() {

        mGridFuncs = (MyGridView) findViewById(R.id.car_main_grid_funcs);
        ivCarMainBg = findViewById(R.id.ivCarMainBg);
        mGridFuncs.setFocusable(false);
        mTxtSoc = (TextView) findViewById(R.id.car_main_txt_soc);
        mTxtSoh = (TextView) findViewById(R.id.car_main_txt_soh);
        mProSoc = findViewById(R.id.car_main_pro_soc);
        mProSocOut = findViewById(R.id.car_main_frame_soc);
        mProSoh = findViewById(R.id.car_main_pro_soh);
        mProSohOut = findViewById(R.id.car_main_frame_soh);
        mSvSoc = (SectorView) findViewById(R.id.car_main_sectorview_soc);
        mSvSoh = (SectorView) findViewById(R.id.car_main_sectorview_soh);
        carMainTxtCarstate = (TextView) findViewById(R.id.car_main_txt_carstate);
        mState = findViewById(R.id.car_main_lay_carstate);

        mState.setOnClickListener(this);


        mGridFuncs.setPadding(0, DorideApplication.dpToPx(10), 0, 0);
        mCarMainFuncInfosInit = new ArrayList<>();
        mAdapter = new CarMainAdapter(CarMainActivity.this);
        for (int i = 0; i < funcNames.length; i++) {
            CarMainFuncInfo mInfo = new CarMainFuncInfo();
            mInfo.setId(i);
            mInfo.setName(funcNames[i]);
            mInfo.setIcon(icons[i]);
            mInfo.setShowDot(isShowDots[i]);
            mInfo.hasPermissions = false;
            mCarMainFuncInfosInit.add(mInfo);

        }
        mAdapter.setmDataList(mCarMainFuncInfosInit);
        mGridFuncs.setAdapter(mAdapter);
        mGridFuncs.setOnItemClickListener(mItemClickListener);

    }

    private void getBgImage() {
        BaseParser parser1 = new DefaultStringParser(new BaseParser.ResultCallback() {
            @Override
            public void onSuccess(com.carlt.doride.data.BaseResponseInfo bInfo) {
                LogUtils.e("parser1=======" + bInfo.toString());
                String json = bInfo.getValue().toString();
                Gson gson = new Gson();
                PictrueInfo pictrueInfo = gson.fromJson(json, PictrueInfo.class);
                LoadLocalImageUtil.getInstance().displayFromWeb(pictrueInfo.filePath, ivCarMainBg, R.drawable.car_main_bg);
            }

            @Override
            public void onError(com.carlt.doride.data.BaseResponseInfo bInfo) {

            }
        });
        HashMap params1 = new HashMap();
        params1.put("app_softtype", "1");
        params1.put("position", "20");
        parser1.executePost(com.carlt.sesame.systemconfig.URLConfig.getM_GET_APPSPICS_URL(), params1);
    }

    private void getCarImage() {
        BaseParser parser1 = new DefaultStringParser(new BaseParser.ResultCallback() {
            @Override
            public void onSuccess(com.carlt.doride.data.BaseResponseInfo bInfo) {
                LogUtils.e("parser1=======" + bInfo.toString());
                String json = bInfo.getValue().toString();
                //                parseJson1(json);
                Gson gson = new Gson();
                PictrueInfo pictrueInfo = gson.fromJson(json, PictrueInfo.class);
                //                LoadLocalImageUtil.getInstance().displayFromWeb(pictrueInfo.filePath, viewMainState, R.drawable.car_state);
                Glide.with(CarMainActivity.this).load(pictrueInfo.filePath).asBitmap().into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        Drawable drawable = new BitmapDrawable(resource);
                        mState.setBackgroundDrawable(drawable);
                    }
                });
            }

            @Override
            public void onError(com.carlt.doride.data.BaseResponseInfo bInfo) {

            }
        });
        HashMap params1 = new HashMap();
        params1.put("app_softtype", "1");
        params1.put("position", "21");
        parser1.executePost(com.carlt.sesame.systemconfig.URLConfig.getM_GET_APPSPICS_URL(), params1);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mCarMainFuncInfosInit != null) {
            for (int i = 0; i < mCarMainFuncInfosInit.size(); i++) {
                mCarMainFuncInfosInit.get(i).hasPermissions = false;

            }
        }

        if (count > 0) {
            CPControl.GetCarMainResult(listener);
        }
        count++;

        if (OnDateChageConfig.ModifyCarChanged) {
            OnDateChageConfig.ModifyCarChanged = false;
            Log.e("info", "carname_bbbbbbbbb==" + GetCarInfo.getInstance().carName);
            if (GetCarInfo.getInstance().carName != null
                    && !GetCarInfo.getInstance().carName.equals("")) {
                mTextViewHead.setText(GetCarInfo.getInstance().carName);
            }
        }

        try {
            mTextViewHead.requestFocus();// 获取焦点
        } catch (Exception e) {
        }
    }

    ClipDrawable clip_soc = new ClipDrawable(new ColorDrawable(), Gravity.LEFT,
            ClipDrawable.HORIZONTAL);

    ClipDrawable clip_soh = new ClipDrawable(new ColorDrawable(), Gravity.LEFT,
            ClipDrawable.HORIZONTAL);

    @Override
    protected void LoadSuccess(Object data) {

        mCarMainFuncInfos = new ArrayList<CarMainFuncInfo>();

//        if (year == 2016) {
//            clickSize = "012";
//            for (int i = 0; i < funcNames2016.length; i++) {
//
//                mCarMainFuncInfosInit.get(i).setIcon(icons[i]);
//                mCarMainFuncInfosInit.get(i).hasPermissions = true;
//
//            }
//        } else {
            clickSize = "0123";
            for (int i = 0; i < 4; i++) {

                mCarMainFuncInfosInit.get(i).setIcon(icons[i]);
                mCarMainFuncInfosInit.get(i).hasPermissions = true;
            }
            //胎压监测、导航同步走车款配置接口
            CarMainFunInfo mCarMainFunInfo = OtherInfo.getInstance().getCarMainFunInfo();

            if (mCarMainFunInfo != null) {
                ArrayList<RemoteFunInfo> mRemoteFunInfos = mCarMainFunInfo.getmCarmainFunInfos();
                int size = mRemoteFunInfos.size();

                if (mCarMainFunInfo != null && size > 0) {
                    for (int i = 0; i < size; i++) {
                        RemoteFunInfo mRemoteFunInfo = mRemoteFunInfos.get(i);
                        String name = mRemoteFunInfo.getId();
                        if (name.equals("0")) {

                            mCarMainFuncInfosInit.get(5).setIcon(icons[5]);
                            mCarMainFuncInfosInit.get(5).hasPermissions = true;
                            clickSize = clickSize + "5";
                            continue;
                        }
                        if (name.equals("1")) {

                            mCarMainFuncInfosInit.get(4).setIcon(icons[4]);
                            mCarMainFuncInfosInit.get(4).hasPermissions = true;
                            clickSize = clickSize + "4";
                            continue;
                        }
                    }

                }
            }
//        }
        mCarMainInfo = (CarMainInfo) data;
        if (mCarMainInfo != null) {
            //            if (mCarMainInfo.isRunning().equals("1")) {
            //                mTextViewSecretary.setText("您的爱车正在行驶");
            //                carMainTxtCarstate.setText("您的爱车正在行驶");
            //            } else if (mCarMainInfo.isRunning().equals("0")) {
            //                mTextViewSecretary.setText("您的爱车正在休息");
            //                carMainTxtCarstate.setText("您的爱车正在休息");
            //            } else if (mCarMainInfo.isRunning().equals("2")) {
            //                mTextViewSecretary.setText("您的爱车已上电");
            //                carMainTxtCarstate.setText("您的爱车已上电");
            //            }
            switch (mCarMainInfo.isRunning()) {
                case "0":
                    //停止
                    switch (mCarMainInfo.charging_status) {
                        case "1":
                            mTextViewSecretary.setText("爱车正在充电中");
                            carMainTxtCarstate.setText("爱车正在充电中");
                            break;
                        case "2":
                            break;
                        case "3":
                            mTextViewSecretary.setText("爱车正在休息中");
                            carMainTxtCarstate.setText("爱车正在休息中");
                            break;
                        case "4":
                            mTextViewSecretary.setText("爱车充电已完成");
                            carMainTxtCarstate.setText("爱车充电已完成");
                            break;
                    }
                    break;
                case "1":
                    //行驶
                    switch (mCarMainInfo.charging_status) {
                        case "1":
                            break;
                        case "2":
                            mTextViewSecretary.setText("爱车正在充电中");
                            carMainTxtCarstate.setText("爱车正在充电中");
                            break;
                        case "3":
                            //                            mTextViewSecretary.setText("爱车正在行驶中");
                            //                            carMainTxtCarstate.setText("爱车正在行驶中");
                            //                            break;
                        case "4":
                            mTextViewSecretary.setText("爱车正在行驶中");
                            carMainTxtCarstate.setText("爱车正在行驶中");
                            break;
                    }
                    break;
                case "2":
                    //上电
                    switch (mCarMainInfo.charging_status) {
                        case "1":
                            mTextViewSecretary.setText("爱车正在充电中");
                            carMainTxtCarstate.setText("爱车正在充电中");
                            break;
                        case "2":
                            //                            mTextViewSecretary.setText("爱车正在充电中");
                            //                            carMainTxtCarstate.setText("爱车正在充电中");
                            break;
                        case "3":
                            mTextViewSecretary.setText("爱车已上电");
                            carMainTxtCarstate.setText("爱车已上电");
                            break;
                        case "4":
                            mTextViewSecretary.setText("爱车充电已完成");
                            carMainTxtCarstate.setText("爱车充电已完成");
                            break;
                    }
                    break;
            }
            // 是否有安防提醒信息
            int safetycount = mCarMainInfo.getSafetycount();

            String socValue = mCarMainInfo.getSoc();
            String sohValue = mCarMainInfo.getSoh();

            int soc = MyParse.parseInt(socValue);
            int soh = MyParse.parseInt(sohValue);
            // 测试代码
            // soc = 60;
            // soh = 10;
            // 测试代码结束
            int offset = (int) (4 * DorideApplication.ScreenDensity);
            int widthSocOut = mProSocOut.getLayoutParams().width - offset;
            int widthSoc = soc * widthSocOut / 100;
            mProSoc.setBackgroundColor(MyParse.getColorByPersent(soc));
            mProSoc.setLayoutParams(new FrameLayout.LayoutParams(widthSoc,
                    FrameLayout.LayoutParams.MATCH_PARENT));

            int widthSohOut = mProSohOut.getLayoutParams().width;
            int widthSoh = soh * widthSohOut / 100;
            mProSoh.setBackgroundColor(MyParse.getColorByPersent(soh));
            mProSoh.setLayoutParams(new FrameLayout.LayoutParams(widthSoh,
                    FrameLayout.LayoutParams.MATCH_PARENT));

            mTxtSoc.setText("SOC:" + soc + "%");
            mTxtSoh.setText("SOH:" + soh + "%");

            mSvSoc.setPersent(soc);
            mSvSoh.setPersent(soh);

            for (CarMainFuncInfo carMainFuncInfo : mCarMainFuncInfos) {
                String name = carMainFuncInfo.getName();
                if (name.equals("安防提醒")) {
                    if (safetycount > 0) {
                        carMainFuncInfo.setShowDot(true);
                    } else {
                        carMainFuncInfo.setShowDot(false);
                    }
                }
            }
            //			RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(DorideApplication.dpToPx(186), DorideApplication.dpToPx(186));
            //			lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
//            if (year = 2016) {
                //				lp.setMargins(0, DorideApplication.dpToPx(40), 0, DorideApplication.dpToPx(30));
                //				mState.setLayoutParams(lp);
//                mGridFuncs.setPadding(0, DorideApplication.dpToPx(10), 0, 0);
//            } else if (year = 2018) {
                //				lp.setMargins(0, DorideApplication.dpToPx(22), 0, 0);
                //				mState.setLayoutParams(lp);
                mGridFuncs.setPadding(0, 0, 0, 0);
//            }
            if (mAdapter == null) {
                //		mAdapter = new CarMainAdapter(CarMainActivity.this, mCarMainFuncInfos);
                mGridFuncs.setAdapter(mAdapter);
                mGridFuncs.setOnItemClickListener(mItemClickListener);
            }
            //	mAdapter.setmDataList(mCarMainFuncInfosInit);
            //	mAdapter.setmDataList(mCarMainFuncInfos);
            mAdapter.notifyDataSetChanged();
        }
        super.LoadSuccess(data);
    }

    @Override
    protected void LoadErro(Object erro) {
        // TODO Auto-generated method stub
        super.LoadErro(erro);
    }

    @Override
    protected void LoadData() {
        super.LoadData();

        CPControl.GetCarMainResult(listener);
        getBgImage();
        getCarImage();
    }

    /**
     * 需要进行检测的权限数组
     */
    protected String[]            needPermissions    = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            //            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            //            Manifest.permission.READ_EXTERNAL_STORAGE,
            //            Manifest.permission.READ_PHONE_STATE
    };
    private   OnItemClickListener mItemClickListener = new OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            int funcId = mCarMainFuncInfosInit.get(position).getId();

            if (!clickSize.contains(funcId + "")) {
                UUToast.showUUToast(CarMainActivity.this, "该车型暂不支持!!");
                return;
            }
            switch (funcId) {
                case 0:
                    // 跳转至一键求援

                    Intent mIntent0 = new Intent(CarMainActivity.this, CarForHelpActivity.class);
                    startActivity(mIntent0);
                    break;

                case 1:
                    // 跳转至违章查询
                    Intent mIntent1 = new Intent(CarMainActivity.this, CarFillIllegalActivity.class);
                    mIntent1.putExtra(CarFillIllegalActivity.CLASS_NAME, CarMainActivity.this.getClass().getName());
                    startActivity(mIntent1);
                    break;
                case 2:
                    // 跳转至安防提醒
                    Intent mIntent2 = new Intent(CarMainActivity.this, CarSafetyActivity.class);
                    if (mCarMainInfo != null) {
                        int safetycount = mCarMainInfo.getSafetycount();
                        mIntent2.putExtra(CarSafetyActivity.SAFETY_COUNT, safetycount);
                    }
                    startActivity(mIntent2);
                    break;
                case 3:
                    requestPermissions(CarMainActivity.this, needPermissions, new BaseActivity.RequestPermissionCallBack() {
                        @Override
                        public void granted() {
                            // 跳转至自动寻车
                            Intent mIntent3 = new Intent(CarMainActivity.this, FindCarActivity.class);
                            startActivity(mIntent3);
                        }

                        @Override
                        public void denied() {
                            UUToast.showUUToast(CarMainActivity.this, "未获取到权限，定位功能不可用");
                        }
                    });

                    break;
                case 4:
                    requestPermissions(CarMainActivity.this, needPermissions, new BaseActivity.RequestPermissionCallBack() {
                        @Override
                        public void granted() {
                            // 跳转至导航同步到车
                            Intent mIntent4 = new Intent(CarMainActivity.this, NavigationTocarActivity.class);
                            startActivity(mIntent4);
                        }

                        @Override
                        public void denied() {
                            UUToast.showUUToast(CarMainActivity.this, "未获取到权限，定位功能不可用");
                        }
                    });

                    break;
                case 5:
                    // 跳转至胎压监测
                    Intent mIntent5 = new Intent(CarMainActivity.this, CarTirePressureActivity.class);
                    startActivity(mIntent5);
                    break;
            }

        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.car_main_lay_carstate:
                // 跳转至实时车况
                Intent mIntent1 = new Intent(CarMainActivity.this,
                        CarConditionActivity.class);
                startActivity(mIntent1);


                break;

            case R.id.car_main_lay_maintain:
                // 跳转至养护计划
                Intent mIntent3 = new Intent(this, MaintainLogActivity.class);
                startActivity(mIntent3);
                break;
            case R.id.car_main_txt_state:
                showWaitingDialog("正在获取车辆状态。。。");
//                CPControl.GetRemoteCarState(mListener_states);
                carState();
                break;
        }
    }

    /**
     * 车辆远程状态
     */
    private void carState(){
        Map<String,Object> param = new HashMap<>();
        Map<String,Object> commParam = new HashMap<>();
        commParam.put("carId",GetCarInfo.getInstance().id);
        commParam.put("deviceID",GetCarInfo.getInstance().deviceNum);
        param.put("base",commParam);
        addDisposable(mApiService.carState(param), new BaseMvcObserver<RemoteCarStateInfo>() {
            @Override
            public void onSuccess(RemoteCarStateInfo result) {
                Message msg = new Message();
                if (result.err != null){
                    msg.what = 3;
                    msg.obj = TextUtils.isEmpty(result.err.msg)?"获取车辆状态失败":result.err.msg;
                    mHandler.sendMessage(msg);
                }else {
                    msg.what = 2;
                    msg.obj = new CarStateParser().parser(result);
                    mHandler.sendMessage(msg);
                }

            }

            @Override
            public void onError(String msg) {
                Message message = new Message();
                message.what = 3;
                message.obj = msg;
                mHandler.sendMessage(message);
            }
        });
    }


    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2:
                    // 获取车辆状态成功
                    ArrayList<CarStateInfo> mDataList = (ArrayList<CarStateInfo>) msg.obj;
                    if (mDataList != null && mDataList.size() > 0) {
                        PopBoxCreat.showUUCarStateDialog(context, mDataList);
                        dissmissWaitingDialog();
                    } else {
                        dissmissWaitingDialog();
                        UUToast.showUUToast(context, "暂未获取到车辆状态数据");
                    }
                    break;
                case 3:
                    dissmissWaitingDialog();
                    ToastUtils.showShort((String) msg.obj);
//                    BaseResponseInfo mInfo = (BaseResponseInfo) msg.obj;
//                    if (mInfo != null) {
//                        UUToast.showUUToast(context, mInfo.getInfo());
//                    }
                    break;
            }
        }

    };

    // 获取远程状态
    private GetResultListCallback mListener_states = new GetResultListCallback() {

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

    public void showWaitingDialog(String msg) {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }

        if (msg == null) {
            msg = "正在连接爱车...";
        }

        mDialog = PopBoxCreat.createDialogWithProgress(context, msg);
        mDialog.show();
    }

    public void dissmissWaitingDialog() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            ActivityControl.exit(CarMainActivity.this);
            return true;
        }
        return super.onKeyDown(keyCode, event);

    }
}

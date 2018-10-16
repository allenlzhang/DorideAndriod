package com.carlt.sesame.ui.activity.remote;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.carlt.doride.DorideApplication;
import com.carlt.doride.R;
import com.carlt.doride.ui.activity.login.UpDateActivity;
import com.carlt.sesame.control.CPControl;
import com.carlt.sesame.control.CPControl.GetResultListCallback;
import com.carlt.sesame.control.VisitorControl;
import com.carlt.sesame.data.BaseResponseInfo;
import com.carlt.sesame.data.SesameLoginInfo;
import com.carlt.sesame.data.remote.AirMainInfo;
import com.carlt.sesame.data.remote.CarStateInfo;
import com.carlt.sesame.data.remote.RemoteFunInfo;
import com.carlt.sesame.data.remote.RemoteMainInfo;
import com.carlt.sesame.ui.activity.base.LoadingActivityWithTitle;
import com.carlt.sesame.ui.activity.safety.RealNameActivity;
import com.carlt.sesame.ui.activity.safety.RemotePswResetActivity3;
import com.carlt.sesame.ui.adapter.remote.RemoteFunctionsAdapter;
import com.carlt.sesame.ui.adapter.remote.RemoteStatesAdapter;
import com.carlt.sesame.ui.view.PopBoxCreat;
import com.carlt.sesame.ui.view.PopBoxCreat.DialogWithTitleClick;
import com.carlt.sesame.ui.view.UUAirConditionDialog;
import com.carlt.sesame.utility.Log;
import com.carlt.sesame.utility.PlayRadio;
import com.carlt.sesame.utility.UUToast;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class RemoteMainActivity extends LoadingActivityWithTitle implements
        OnItemClickListener, OnClickListener, OnTouchListener {
    public final static String ACTION_REMOTE_SETPSW = "com.carlt.sesame.action_remote_setpsw";

    public final static String ACTION_REMOTE_RESETPSW = "com.carlt.sesame.action_remote_resetpsw";

    public final static String ACTION_REMOTE_FORGETPSW = "com.carlt.sesame.action_remote_forgetpsw ";

    private TextView mTxtState;// 车辆状态

    private TextView mTxtRecorder;// 远程记录

    private TextView mTxtEngine;// 发动机状态

    private ImageView mImgStart;// 远程启动

    private ImageView mImgStop;// 远程熄火

    private View mViewStopmask;// 熄火按钮蒙版

    private GridView mGridView;

    private View mViewNormal;// 车辆状态未出现是的View

    // 汽车状态相关
    private View mViewState;// 汽车状态

    private GridView mGridViewState;// 汽车状态

    private ImageView mImgArrow;// 收起箭头

    // 汽车状态相关结束

    private RemoteFunctionsAdapter mAdapter;

    private RemoteStatesAdapter mAdapterStates;

    private Dialog mDialog;

    private String password;

    private int selectedPos;// 被点击的item的pos

    boolean isReCall = false;

    UUAirConditionDialog airDialog;

    private long startTime;// 第一次点击按钮时间

    private boolean isFirstClick = true;// 是否是第一次点击

    private final static long INVALID_DURATION = 5 * 60 * 1000;// 密码实效时长

    private RemoteReceiver mReceiver;

    private String deviceType;// 设备类型

    private PlayRadio mPlayRadio;

    private VisitorControl mVContorl;

    private int lastOpt = -1;

    private final static String names[]               = {"最大制热", "最大制冷", "一键除霜", "关闭空调"};
    private final static int    icon_ids[]            = {R.drawable.remote_hot,
            R.drawable.remote_cold, R.drawable.remote_frost,
            R.drawable.remote_close_air2};
    private final static int    icon_id_seleceds[]    = {
            R.drawable.sesame_remote_hot_selected, R.drawable.sesame_remote_cold_selected,
            R.drawable.sesame_remote_frost_selected, R.drawable.sesame_icon_close_air_press};
    private final static int    icon_id_seleced_nos[] = {
            R.drawable.sesame_remote_hot_selected_no,
            R.drawable.sesame_remote_cold_selected_no,
            R.drawable.sesame_remote_frost_selected_no, R.drawable.sesame_icon_close_air};
    private final static String temps[]               = {"32", "18", "32", "--"};

    private AirMainInfo airMainInfo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sesame_activity_remote_main);
        setTitleView(R.layout.head_remote);
        registerBeforeGoToBackGround(this);
        mPlayRadio = PlayRadio.getInstance(RemoteMainActivity.this);
        init();

        airMainInfo = new AirMainInfo();
        airMainInfo.setShowTemp(true);
        for (int i = 0; i < names.length; i++) {
            RemoteFunInfo mInfo = new RemoteFunInfo();
            mInfo.setName(names[i]);
            mInfo.setIcon_id(icon_ids[i]);
            mInfo.setIcon_id_seleced(icon_id_seleceds[i]);
            mInfo.setIcon_id_seleced_no(icon_id_seleced_nos[i]);
            mInfo.setTemperature(temps[i]);
            airMainInfo.addmRemoteFunInfos(mInfo);
        }

        // 生成广播处理
        mReceiver = new RemoteReceiver();
        IntentFilter filter = new IntentFilter();
        mVContorl = new VisitorControl(this);
        filter.addAction(ACTION_REMOTE_SETPSW);
        filter.addAction(ACTION_REMOTE_RESETPSW);
        filter.addAction(ACTION_REMOTE_FORGETPSW);
        registerReceiver(mReceiver, filter);

    }

    private void init() {
        mTxtState = (TextView) findViewById(R.id.remote_main_txt_state);
        mTxtRecorder = (TextView) findViewById(R.id.remote_main_txt_recorder);
        mGridView = (GridView) findViewById(R.id.remote_main_gridView_function);
        mViewNormal = findViewById(R.id.remote_main_lay_normal);
        mViewState = findViewById(R.id.remote_main_lay_state);
        mGridViewState = (GridView) findViewById(R.id.remote_main_gridView_state);
        mImgArrow = (ImageView) findViewById(R.id.remote_main_img_arrow);
        mImgStart = (ImageView) findViewById(R.id.remote_main_img_start);
        mImgStop = (ImageView) findViewById(R.id.remote_main_img_stop);
        mViewStopmask = findViewById(R.id.remote_main_view_stop_mask);

        mTxtState.setOnClickListener(this);
        mTxtRecorder.setOnClickListener(this);
        mImgStart.setOnClickListener(this);
        mImgStop.setOnClickListener(this);
        mViewStopmask.setOnClickListener(this);
        mViewNormal.setOnClickListener(this);

        mViewNormal.setOnTouchListener(this);

        mImgArrow.setOnClickListener(this);

        mAdapter = new RemoteFunctionsAdapter(RemoteMainActivity.this);

    }

    /**
     * 调用远程接口
     */
    private void GetResult() {
        int opt = -1;
        if (deviceType.equals(SesameLoginInfo.DEVICETYPE_BEFORE)
                || deviceType.equals(SesameLoginInfo.DEVICETYPE_AFTER2016)) {
            // 前装设备or后装2016款
            if (selectedPos != 1) {
                showWaitingDialog(null);
            }

            switch (selectedPos) {
                case -2:
                    // 远程启动
                    lastOpt = 0;
                    CPControl.GetRemoteStart(mListener);
                    break;
                case -1:
                    // 远程熄火
                    lastOpt = 1;
                    CPControl.GetCancelRemoteStart(mListener);
                    break;
                case 0:
                    // 闪灯鸣笛
                    lastOpt = 2;
                    mPlayRadio.playClickVoice(R.raw.remote_finding);
                    CPControl.GetCarLocating(mListener);
                    break;
                case 1:
                    // 远程开启空调
                    lastOpt = 3;
                    if (!isReCall) {
                        if (airDialog == null || !airDialog.isShowing()) {
                            airDialog = new UUAirConditionDialog(this,
                                    airMainInfo);
                            airDialog.mListener = mListener;
                            airDialog.mHandler = mHandler;
                            airDialog.show();
                        }
                    } else {
                        airDialog.reCall();
                    }
                    break;
                case 2:
                    // 远程关闭空调
                    lastOpt = 4;
                    CPControl.GetRemoteAir(mListener, "2");
                    break;
                case 3:
                    // 远程开启后备箱
                    lastOpt = 5;
                    CPControl.GetRemoteTrunk(mListener);
                    break;
                case 4:
                    // 远程解锁
                    lastOpt = 6;
                    CPControl.GetRemoteLock("1", mListener);
                    break;
                case 5:
                    // 远程落锁
                    lastOpt = 7;
                    CPControl.GetRemoteLock("2", mListener);
                    break;
            }
        } else {
            // 后装设备
            showWaitingDialog(null);
            switch (selectedPos) {
                case -2:
                    // 远程启动
                    lastOpt = 0;
                    CPControl.GetRemoteStart(mListener);
                    break;
                case -1:
                    // 远程熄火
                    lastOpt = 1;
                    CPControl.GetCancelRemoteStart(mListener);
                    break;
                case 0:
                    // 闪灯鸣笛
                    lastOpt = 2;
                    CPControl.GetCarLocating(mListener);
                    break;
                case 1:
                    // 远程开启后备箱
                    lastOpt = 5;
                    CPControl.GetRemoteTrunk(mListener);
                    break;
                case 2:
                    // 远程解锁
                    lastOpt = 6;
                    CPControl.GetRemoteLock("1", mListener);
                    break;
                case 3:
                    // 远程落锁
                    lastOpt = 7;
                    CPControl.GetRemoteLock("2", mListener);
                    break;
            }
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.remote_main_txt_state:
                Log.e("info", "车辆状态点击22222222222222");
                showWaitingDialog("正在获取车辆状态。。。");
                CPControl.GetRemoteCarState(mListener_states,
                        SesameLoginInfo.getDeviceType());
                break;
            case R.id.remote_main_txt_recorder:
                // 远程记录
                Log.e("info", "远程记录22222222222222");
                Intent mIntent = new Intent(RemoteMainActivity.this,
                        RemoteLogActivity.class);
                startActivity(mIntent);
                break;
            case R.id.remote_main_img_arrow:
                // 收起状态栏
                mViewState.setVisibility(View.GONE);
                break;
            case R.id.remote_main_img_start:
                // 远程启动
                selectedPos = -2;
                clickLogic();
                break;
            case R.id.remote_main_img_stop:
                // 远程熄火
                selectedPos = -1;
                clickLogic();
                break;
            case R.id.remote_main_lay_normal:
                // 车辆状态view消失
                dismissCarstateView();
                break;
        }
    }

    public class RemoteReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ACTION_REMOTE_SETPSW)) {
                // 设置密码成功
                startTime = System.currentTimeMillis();
                isFirstClick = false;
            } else if (action.equals(ACTION_REMOTE_RESETPSW)) {
                // 修改密码成功
                isFirstClick = true;
            } else if (action.equals(ACTION_REMOTE_FORGETPSW)) {
                // 重置密码成功
                isFirstClick = true;
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        selectedPos = position;
        clickLogic();
    }

    @Override
    protected void onResume() {
        super.onResume();
        LoadData();
    }

    @Override
    protected void LoadSuccess(Object data) {
        RemoteMainInfo mRemoteMainInfo = (RemoteMainInfo) data;
        if (mRemoteMainInfo != null) {

            // isDeviceBefore = mRemoteMainInfo.isDeviceBefore();
            // 测试用
            // isDeviceBefore=true;
            // 测试结束

            // 由于ios的加载机制 前装后装字段放到登录接口
            // 测试用
            // LoginInfo.setDeviceType(LoginInfo.DEVICETYPE_BEFORE);
            // 测试代码结束

            deviceType = SesameLoginInfo.getDeviceType();
            mAdapter.setDeviceType(deviceType);
            mAdapter.notifyDataSetChanged();
            mGridView.setAdapter(mAdapter);
            mGridView.setOnItemClickListener(this);
            if (deviceType.equals(SesameLoginInfo.DEVICETYPE_BEFORE)
                    || deviceType.equals(SesameLoginInfo.DEVICETYPE_AFTER2016)) {
                // 前装设备or后装2016款
                mGridView.setNumColumns(3);
                mGridViewState.setNumColumns(4);
            } else {
                // 后装设备
                mGridView.setNumColumns(2);
                mGridViewState.setNumColumns(3);
            }

            // switch (mRemoteMainInfo.getStatus()) {
            // case RemoteMainInfo.STATUS_START:
            // // 启动
            // mImgStart.setImageResource(R.drawable.remote_start_disable);
            // mImgStop.setImageResource(R.drawable.remote_stop);
            // mTxtEngine.setText("车辆已启动");
            // mViewStopmask.setVisibility(View.GONE);
            //
            // mImgStart.setClickable(false);
            // mImgStop.setClickable(true);
            // break;
            //
            // case RemoteMainInfo.STATUS_STOP:
            // // 熄火
            // mImgStart.setImageResource(R.drawable.remote_start);
            // mImgStop.setImageResource(R.drawable.remote_stop_disable);
            // mTxtEngine.setText("远程启动车辆");
            // mViewStopmask.setVisibility(View.VISIBLE);
            //
            // mImgStart.setClickable(true);
            // mImgStop.setClickable(false);
            // break;
            // default:
            // // 熄火
            // mImgStart.setImageResource(R.drawable.remote_start);
            // mImgStop.setImageResource(R.drawable.remote_stop_disable);
            // mTxtEngine.setText("远程启动车辆");
            // mViewStopmask.setVisibility(View.VISIBLE);
            //
            // mImgStart.setClickable(true);
            // mImgStop.setClickable(false);
            // break;
            // }
        } else {
            mImgStart.setImageResource(R.drawable.sesame_remote_start);
            mImgStop.setImageResource(R.drawable.remote_stop_disable);
            mTxtEngine.setText("远程启动车辆");
            mViewStopmask.setVisibility(View.VISIBLE);

            mImgStart.setClickable(true);
            mImgStop.setClickable(false);
        }
        super.LoadSuccess(data);
    }

    @Override
    protected void LoadErro(Object erro) {
        super.LoadErro(erro);
    }

    @Override
    protected void LoadData() {
        super.LoadData();
        CPControl.GetRemoteMainResult(listener);
    }

    /**
     * 点击逻辑
     */
    private void clickLogic() {
        boolean hasRemotePswMd5 = SesameLoginInfo.isSetRemotePwd();
        if (mViewState.getVisibility() == View.VISIBLE) {
            // 车辆状态view打开
            mViewState.setVisibility(View.GONE);
        } else {
            if (hasRemotePswMd5) {
                Log.e("info", "remotemain_isFirstClick==" + isFirstClick);
                if (isFirstClick) {
                    showEditDialog();
                } else {
                    if (SesameLoginInfo.isNoneedpsw()) {
                        if (getTimeOutStatus()) {
                            showEditDialog();
                        } else {
                            GetResult();
                        }
                    } else {
                        showEditDialog();
                    }

                }
            } else {
                DialogWithTitleClick click = new DialogWithTitleClick() {
                    @Override
                    public void onRightClick() {
                        Log.e("info",
                                "LoginInfo.isAuthen==" + SesameLoginInfo.isAuthen());
                        if (SesameLoginInfo.isAuthen()) {
                            Intent mIntent = new Intent(
                                    RemoteMainActivity.this,
                                    RemotePswResetActivity3.class);
                            mIntent.putExtra(RemotePswResetActivity3.TYPE,
                                    RemotePswResetActivity3.TYPE_REMOTE);
                            startActivity(mIntent);
                        } else {
                            Intent mIntent = new Intent(
                                    RemoteMainActivity.this,
                                    RealNameActivity.class);
                            mIntent.putExtra(RealNameActivity.TYPE,
                                    RealNameActivity.TYPE_REMOTE);
                            startActivity(mIntent);
                        }
                    }

                    @Override
                    public void onLeftClick() {
                        // TODO Auto-generated method stub

                    }
                };
                PopBoxCreat.createDialogNotitle(RemoteMainActivity.this,
                        "设置远程控制", "为保障车辆安全请先设置远程控制密码", "取消", "设置密码", click);

            }
        }
    }

    /**
     * 输入密码Dialog
     */
    private void showEditDialog() {
        LayoutInflater inflater = LayoutInflater.from(RemoteMainActivity.this);
        View view = inflater.inflate(R.layout.dialog_withedit_new, null);
        final Dialog dialogI = new Dialog(RemoteMainActivity.this,
                R.style.dialog);
        final EditText editPassword = (EditText) view
                .findViewById(R.id.dialog_withedit_new_edt);

        ImageView imgCancel = (ImageView) view
                .findViewById(R.id.dialog_withedit_new_cancel);
        TextView btn1 = (TextView) view
                .findViewById(R.id.dialog_withedit_new_btn1);
        TextView btn2 = (TextView) view
                .findViewById(R.id.dialog_withedit_new_btn2);

        // 正常模式
        editPassword.setEnabled(true);
        editPassword.setFocusableInTouchMode(true);
        editPassword.requestFocus();

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            public void run() {
                InputMethodManager inputManager = (InputMethodManager) editPassword
                        .getContext().getSystemService(
                                Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(editPassword, 0);
            }

        }, 100);

        btn2.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                password = editPassword.getText().toString();
                Log.e("info", "isFirstClick==" + isFirstClick);
                if (password == null || password.length() < 1) {
                    UUToast.showUUToast(RemoteMainActivity.this, "您的密码不能为空哦...");
                    return;
                }
                if (isFirstClick) {
                    startTime = System.currentTimeMillis();
                    isFirstClick = false;
                }

                showWaitingDialog("正在验证您的远程密码...");
                CPControl.GetRemotePswVerify(password, mListener_verify);
                dialogI.dismiss();
            }
        });

        btn1.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                dialogI.dismiss();
            }
        });

        imgCancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dialogI.dismiss();
            }
        });

        int w = (int) (DorideApplication.ScreenDensity * 300);
        ViewGroup.LayoutParams parm = new ViewGroup.LayoutParams(w,
                LayoutParams.WRAP_CONTENT);
        dialogI.setContentView(view, parm);
        dialogI.show();
    }

    private boolean getTimeOutStatus() {
        boolean isTimeOut = false;

        long currentTime = System.currentTimeMillis();
        long diffrence = currentTime - startTime;
        if (diffrence > INVALID_DURATION) {
            isTimeOut = true;
            isFirstClick = true;
        } else {
            isTimeOut = false;
        }
        return isTimeOut;
    }

    private GetResultListCallback mListener = new GetResultListCallback() {

        @Override
        public void onFinished(Object o) {
            mHandler.sendEmptyMessage(0);
        }

        @Override
        public void onErro(Object o) {
            Message msg = new Message();
            msg.what = 1;
            msg.obj = o;
            mHandler.sendMessage(msg);

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

    // 校验远程密码
    private GetResultListCallback mListener_verify = new GetResultListCallback() {

        @Override
        public void onFinished(Object o) {
            Message msg = new Message();
            msg.what = 4;
            msg.obj = o;
            mHandler.sendMessage(msg);
        }

        @Override
        public void onErro(Object o) {
            Message msg = new Message();
            msg.what = 5;
            msg.obj = o;
            mHandler.sendMessage(msg);

        }
    };

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    // 成功
                    // if (selectedPos == -2) {
                    // // 启动
                    // mImgStart.setImageResource(R.drawable.remote_start_disable);
                    // mImgStop.setImageResource(R.drawable.remote_stop);
                    // mTxtEngine.setText("车辆已启动");
                    // mViewStopmask.setVisibility(View.GONE);
                    //
                    // mImgStart.setClickable(false);
                    // mImgStop.setClickable(true);
                    // } else if (selectedPos == -1) {
                    // // 熄火
                    // mImgStart.setImageResource(R.drawable.remote_start);
                    // mImgStop.setImageResource(R.drawable.remote_stop_disable);
                    // mTxtEngine.setText("远程启动车辆");
                    // mViewStopmask.setVisibility(View.VISIBLE);
                    //
                    // mImgStart.setClickable(true);
                    // mImgStop.setClickable(false);
                    // }

                    switch (lastOpt) {
                        case 0:
                            mPlayRadio.playClickVoice(R.raw.remote_start);
                            lastOpt = -1;
                            break;
                        case 1:
                            mPlayRadio.playClickVoice(R.raw.remote_stop);
                            lastOpt = -1;
                            break;
                        case 2:
                            mPlayRadio.playClickVoice(R.raw.remote_finding);
                            lastOpt = -1;
                            break;
                        case 3:
                            mPlayRadio.playClickVoice(R.raw.remote_air);
                            lastOpt = -1;
                            break;
                        case 4:
                            mPlayRadio.playClickVoice(R.raw.remote_air);
                            lastOpt = -1;
                            break;
                        case 5:
                            mPlayRadio.playClickVoice(R.raw.remote_trunk);
                            lastOpt = -1;
                            break;
                        case 6:
                            mPlayRadio.playClickVoice(R.raw.remote_unlock);
                            lastOpt = -1;
                            break;
                        case 7:
                            mPlayRadio.playClickVoice(R.raw.remote_lock);
                            lastOpt = -1;
                            break;
                    }

                    dissmissWaitingDialog();
                    if (airDialog != null) {
                        isReCall = false;
                        airDialog.dismiss();
                    }

                    //				UUToast.showUUToast(RemoteMainActivity.this, "操作成功");
                    ToastUtils.showShort("操作成功");
                    break;

                case 1:
                    dissmissWaitingDialog();
                    if (airDialog != null && airDialog.isShowing()) {
                        airDialog.dismiss();
                    }
                    BaseResponseInfo mInfo1 = (BaseResponseInfo) msg.obj;
                    if (mInfo1 != null) {
                        if (mInfo1.getFlag() == 1020) {
                            Intent intent  = new Intent(RemoteMainActivity.this,UpDateActivity.class);
                            startActivity(intent);
//                            PopBoxCreat.showUUUpdateDialog(context, null);
                        } else {
                            //						UUToast.showUUToast(RemoteMainActivity.this,
                            //								mInfo1.getInfo());
                            ToastUtils.showShort(mInfo1.getInfo());
                        }
                    }
                    break;

                case 2:
                    // 获取车辆状态成功
                    // ArrayList<CarStateInfo> mDataList = (ArrayList<CarStateInfo>)
                    // msg.obj;
                    // if (mDataList != null && mDataList.size() > 0) {
                    // if (mAdapterStates == null) {
                    // mAdapterStates = new RemoteStatesAdapter(
                    // RemoteMainActivity.this, mDataList);
                    // mGridViewState.setAdapter(mAdapterStates);
                    // } else {
                    // mAdapterStates.setmDataList(mDataList);
                    // mAdapterStates.notifyDataSetChanged();
                    // }
                    // dissmissWaitingDialog();
                    // mViewState.setVisibility(View.VISIBLE);
                    // } else {
                    // dissmissWaitingDialog();
                    // mViewState.setVisibility(View.GONE);
                    // UUToast.showUUToast(RemoteMainActivity.this, "暂未获取到车辆状态数据");
                    // }
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
                    BaseResponseInfo mInfo = (BaseResponseInfo) msg.obj;
                    if (mInfo != null) {
//                        UUToast.showUUToast(RemoteMainActivity.this,
//                                mInfo.getInfo());
                        ToastUtils.showShort(mInfo.getInfo());
                    }
                    break;
                case 4:
                    dissmissWaitingDialog();
                    mHandler.sendEmptyMessage(10);
                    break;
                case 5:
                    dissmissWaitingDialog();
                    BaseResponseInfo mInfo2 = (BaseResponseInfo) msg.obj;
                    if (mInfo2 != null) {
//                        UUToast.showUUToast(RemoteMainActivity.this,
//                                mInfo2.getInfo());
                        ToastUtils.showShort(mInfo2.getInfo());
                    }
                    isFirstClick = true;
                    break;
                case 10:
                    GetResult();
                    break;
                case 11:
                    showWaitingDialog(null);
                    // 调用接口
                    break;

            }
        }

    };

    @Override
    public void doBeforeGoToBackground() {
        isFirstClick = true;
    }

    public void showWaitingDialog(String msg) {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }

        if (msg == null) {
            msg = "正在连接爱车...";
        }

        mDialog = PopBoxCreat.createDialogWithProgress(RemoteMainActivity.this,
                msg);
        mDialog.show();
    }

    public void dissmissWaitingDialog() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        isFirstClick = true;
        Log.e("info", "RemoteMainActivity____onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("info", "RemoteMainActivity____onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unRegisterBeforeGoToBackGround(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Log.e("info", "event.getAction()==" + event.getAction());
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            dismissCarstateView();
            return true;
        } else {
            return false;
        }
    }

    private void dismissCarstateView() {
        if (mViewState.getVisibility() == View.VISIBLE) {
            mViewState.setVisibility(View.GONE);
        }
    }

    public void updateCarState(ArrayList<CarStateInfo> mdatas) {
        if (mAdapterStates == null) {
            mAdapterStates = new RemoteStatesAdapter(context, mdatas);
            mGridViewState.setAdapter(mAdapterStates);
        }

        mAdapterStates.setmDataList(mdatas);
        mAdapterStates.notifyDataSetChanged();

    }
}

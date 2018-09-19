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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.carlt.doride.DorideApplication;
import com.carlt.doride.R;
import com.carlt.doride.ui.activity.login.UpDateActivity;
import com.carlt.sesame.control.CPControl;
import com.carlt.sesame.control.CPControl.GetResultListCallback;
import com.carlt.sesame.data.BaseResponseInfo;
import com.carlt.sesame.data.SesameLoginInfo;
import com.carlt.sesame.data.remote.AirMainInfo;
import com.carlt.sesame.data.remote.CarStateInfo;
import com.carlt.sesame.data.remote.ChargeStatusInfo;
import com.carlt.sesame.data.remote.RemoteFunInfo;
import com.carlt.sesame.data.remote.RemoteMainInfo;
import com.carlt.sesame.ui.activity.base.LoadingActivityWithTitle;
import com.carlt.sesame.ui.activity.safety.RealNameActivity;
import com.carlt.sesame.ui.activity.safety.RemotePswResetActivity3;
import com.carlt.sesame.ui.view.PopBoxCreat;
import com.carlt.sesame.ui.view.PopBoxCreat.DialogWithTitleClick;
import com.carlt.sesame.ui.view.UUAirConditionDialog;
import com.carlt.sesame.ui.view.UUChargeDialog;
import com.carlt.sesame.utility.PlayRadio;
import com.carlt.sesame.utility.UUToast;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class RemoteMainNewActivity extends LoadingActivityWithTitle implements OnClickListener {
    public final static String ACTION_REMOTE_SETPSW = "com.carlt.sesame.action_remote_setpsw";
    public final static String ACTION_REMOTE_RESETPSW = "com.carlt.sesame.action_remote_resetpsw";
    public final static String ACTION_REMOTE_FORGETPSW = "com.carlt.sesame.action_remote_forgetpsw ";

    private TextView mTxtState;// 车辆状态
    private TextView mTxtRecorder;// 远程记录

    private TextView mTxtItem1One;

    private TextView mTxtItem1Two;
    private TextView mTxtItem2Two;

    private TextView mTxtItem1Three;
    private TextView mTxtItem2Three;
    private TextView mTxtItem3Three;

    private TextView mTxtItem1Four;
    private TextView mTxtItem2Four;
    private TextView mTxtItem3Four;
    private TextView mTxtItem4Four;

    private TextView mTxtItem1Five;
    private TextView mTxtItem2Five;
    private TextView mTxtItem3Five;
    private TextView mTxtItem4Five;
    private TextView mTxtItem5Five;

    private View mViewNoData;//没有支持项
    private View mViewOne;// 展示1个按钮
    private View mViewTwo;// 展示2个按钮
    private View mViewThree;// 展示3个按钮
    private View mViewFour;// 展示4个按钮
    private View mViewFive;// 展示5个按钮

    private final static long INVALID_DURATION = 5 * 60 * 1000;// 密码实效时长
    private RemoteReceiver mReceiver;
    private PlayRadio      mPlayRadio;
    private int lastOpt = -1;
    boolean isFirstClick;
    String password;
    long startTime;
    Dialog mDialog;

    private final static String names[] = {"最大制热", "最大制冷", "一键除霜", "关闭空调"};
    private final static int icon_ids[] = {R.drawable.sesame_remote_hot,
            R.drawable.sesame_remote_cold, R.drawable.sesame_remote_frost,
            R.drawable.remote_close_air2};
    private final static int icon_id_seleceds[] = {
            R.drawable.sesame_remote_hot_selected, R.drawable.sesame_remote_cold_selected,
            R.drawable.sesame_remote_frost_selected, R.drawable.sesame_icon_close_air_press};
    private final static int icon_id_seleced_nos[] = {
            R.drawable.sesame_remote_hot_selected_no,
            R.drawable.sesame_remote_cold_selected_no,
            R.drawable.sesame_remote_frost_selected_no, R.drawable.sesame_icon_close_air};
    private final static String temps[] = {"32", "18", "32", "--"};
    private final static String ids[] = {"5", "4", "3", "8"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remote_main_new);
      // setTitleView(R.layout.head_remote);
        // right remote_history_iv  left  state_car_iv
        setTitleView(R.layout.layout_title_remote);
        registerBeforeGoToBackGround(this);
        mPlayRadio = PlayRadio.getInstance(RemoteMainNewActivity.this);
        init();
        // 生成广播处理
        mReceiver = new RemoteReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_REMOTE_SETPSW);
        filter.addAction(ACTION_REMOTE_RESETPSW);
        filter.addAction(ACTION_REMOTE_FORGETPSW);
        registerReceiver(mReceiver, filter);
        LoadData();
    }

    private void init() {
        mTxtState = (TextView) findViewById(R.id.state_car_iv);
        mTxtState.setOnClickListener(this);
        mTxtRecorder = (TextView) findViewById(R.id.remote_history_iv);
        mTxtRecorder.setOnClickListener(this);

        mTxtItem1One = (TextView) findViewById(R.id.remote_new_txt_item1_one);
        mTxtItem1One.setOnClickListener(this);

        mTxtItem1Two = (TextView) findViewById(R.id.remote_new_txt_item1_two);
        mTxtItem2Two = (TextView) findViewById(R.id.remote_new_txt_item2_two);
        mTxtItem1Two.setOnClickListener(this);
        mTxtItem2Two.setOnClickListener(this);

        mTxtItem1Three = (TextView) findViewById(R.id.remote_new_txt_item1_three);
        mTxtItem2Three = (TextView) findViewById(R.id.remote_new_txt_item2_three);
        mTxtItem3Three = (TextView) findViewById(R.id.remote_new_txt_item3_three);
        mTxtItem1Three.setOnClickListener(this);
        mTxtItem2Three.setOnClickListener(this);
        mTxtItem3Three.setOnClickListener(this);

        mTxtItem1Four = (TextView) findViewById(R.id.remote_new_txt_item1_four);
        mTxtItem2Four = (TextView) findViewById(R.id.remote_new_txt_item2_four);
        mTxtItem3Four = (TextView) findViewById(R.id.remote_new_txt_item3_four);
        mTxtItem4Four = (TextView) findViewById(R.id.remote_new_txt_item4_four);
        mTxtItem1Four.setOnClickListener(this);
        mTxtItem2Four.setOnClickListener(this);
        mTxtItem3Four.setOnClickListener(this);
        mTxtItem4Four.setOnClickListener(this);

        mTxtItem1Five = (TextView) findViewById(R.id.remote_new_txt_item1_five);
        mTxtItem2Five = (TextView) findViewById(R.id.remote_new_txt_item2_five);
        mTxtItem3Five = (TextView) findViewById(R.id.remote_new_txt_item3_five);
        mTxtItem4Five = (TextView) findViewById(R.id.remote_new_txt_item4_five);
        mTxtItem5Five = (TextView) findViewById(R.id.remote_new_txt_item5_five);
        mTxtItem1Five.setOnClickListener(this);
        mTxtItem2Five.setOnClickListener(this);
        mTxtItem3Five.setOnClickListener(this);
        mTxtItem4Five.setOnClickListener(this);
        mTxtItem5Five.setOnClickListener(this);

        mViewNoData = findViewById(R.id.remote_new_lay_nodata);
        mViewOne = findViewById(R.id.remote_new_lay_one);
        mViewTwo = findViewById(R.id.remote_new_lay_two);
        mViewThree = findViewById(R.id.remote_new_lay_three);
        mViewFour = findViewById(R.id.remote_new_lay_four);
        mViewFive = findViewById(R.id.remote_new_lay_five);
    }

    /**
     * 调用远程接口
     */
    private UUAirConditionDialog mAirConditionDialog;

    private void GetResult() {
        showWaitingDialog(null);
        switch (lastOpt) {
            case 0:
                CPControl.GetRemoteLock("1", mListener);
                break;
            case 1:
                CPControl.GetRemoteLock("2", mListener);
                break;
            case 2:
                CPControl.GetCarLocating(mListener);
                break;
            case 3:
                // 弹出空调页面-TODO
                // mDialog.dismiss();
                // AirMainInfo airMainInfo = new AirMainInfo();
//			mAirMainInfo.setShowTemp(true);
                mHandler.sendEmptyMessage(12);
                // 测试数据
                // airMainInfo.setState("1");
                // 测试数据结束
                // for (int i = 0; i < names.length; i++) {
                // RemoteFunInfo mInfo = new RemoteFunInfo();
                // mInfo.setId(ids[i]);
                // mInfo.setName(names[i]);
                // mInfo.setIcon_id(icon_ids[i]);
                // mInfo.setIcon_id_seleced(icon_id_seleceds[i]);
                // mInfo.setIcon_id_seleced_no(icon_id_seleced_nos[i]);
                // mInfo.setTemperature(temps[i]);
                // airMainInfo.addmRemoteFunInfos(mInfo);
                // }
                // mAirConditionDialog = new UUAirConditionDialog(this,
                // mAirMainInfo);
                // mAirConditionDialog.mListener = mListener;
                // mAirConditionDialog.mHandler = mHandler;
                // mAirConditionDialog.show();
                break;
            case 4:
                // 弹出充电页面-TODO
                CPControl.GetRemoteChargeStatus(mListener_chargeStatus);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        RemoteFunInfo mRemoteFunInfo = (RemoteFunInfo) v.getTag();
        if (mRemoteFunInfo != null) {
            String RemoteFunInfoID = mRemoteFunInfo.getId();
            if (RemoteFunInfoID.equals(RemoteFunInfo.FUNC_UNLOCK)) {
                // 解锁
                lastOpt = 0;
            } else if (RemoteFunInfoID.equals(RemoteFunInfo.FUNC_LOCK)) {
                // 落锁
                lastOpt = 1;
            } else if (RemoteFunInfoID.equals(RemoteFunInfo.FUNC_FIND)) {
                // 声光寻车
                lastOpt = 2;
            } else if (RemoteFunInfoID.equals(RemoteFunInfo.FUNC_AIR)) {
                // 空调
                lastOpt = 3;
            } else if (RemoteFunInfoID.equals(RemoteFunInfo.FUNC_CHARGE)) {
                // 充电
                lastOpt = 4;
            }
            clickLogic();
            return;
        }
        switch (v.getId()) {
            case R.id.state_car_iv:
                showWaitingDialog("正在获取车辆状态...");
                CPControl.GetRemoteCarState(mListener_states,
                        SesameLoginInfo.getDeviceType());
                break;
            case R.id.remote_history_iv:
                // 远程记录
                Intent mIntent = new Intent(RemoteMainNewActivity.this,
                        RemoteLogActivity.class);
                startActivity(mIntent);
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
    protected void onResume() {
        super.onResume();
        LoadData();
    }

    private ArrayList<RemoteFunInfo> mRemoteFunInfos;
    private AirMainInfo              mAirMainInfo;

    @Override
    protected void LoadSuccess(Object data) {
        RemoteMainInfo mRemoteMainInfo = (RemoteMainInfo) data;
        if (mRemoteMainInfo != null) {
            mAirMainInfo = mRemoteMainInfo.getmAirMainInfo();
            mRemoteFunInfos = mRemoteMainInfo.getmRemoteFunInfos();
            int size = mRemoteFunInfos.size();
            RemoteFunInfo remoteFunInfo1;
            RemoteFunInfo remoteFunInfo2;
            RemoteFunInfo remoteFunInfo3;
            RemoteFunInfo remoteFunInfo4;
            RemoteFunInfo remoteFunInfo5;
            switch (size) {
                case 0:
                    mViewNoData.setVisibility(View.VISIBLE);
                    mViewOne.setVisibility(View.GONE);
                    mViewTwo.setVisibility(View.GONE);
                    mViewThree.setVisibility(View.GONE);
                    mViewFour.setVisibility(View.GONE);
                    mViewFive.setVisibility(View.GONE);
                    break;
                case 1:
                    mViewNoData.setVisibility(View.GONE);
                    mViewOne.setVisibility(View.VISIBLE);
                    mViewTwo.setVisibility(View.GONE);
                    mViewThree.setVisibility(View.GONE);
                    mViewFour.setVisibility(View.GONE);
                    mViewFive.setVisibility(View.GONE);

                    remoteFunInfo1 = mRemoteFunInfos.get(0);

                    mTxtItem1One.setTag(remoteFunInfo1);

                    mTxtItem1One.setText(remoteFunInfo1.getName());
                    mTxtItem1One.setBackgroundResource(remoteFunInfo1.getIcon_id());

                    break;
                case 2:
                    mViewNoData.setVisibility(View.GONE);
                    mViewOne.setVisibility(View.GONE);
                    mViewTwo.setVisibility(View.VISIBLE);
                    mViewThree.setVisibility(View.GONE);
                    mViewFour.setVisibility(View.GONE);
                    mViewFive.setVisibility(View.GONE);

                    remoteFunInfo1 = mRemoteFunInfos.get(0);
                    remoteFunInfo2 = mRemoteFunInfos.get(1);

                    mTxtItem1Two.setTag(remoteFunInfo1);
                    mTxtItem2Two.setTag(remoteFunInfo2);

                    mTxtItem1Two.setText(remoteFunInfo1.getName());
                    mTxtItem1Two.setBackgroundResource(remoteFunInfo1.getIcon_id());

                    mTxtItem2Two.setText(remoteFunInfo2.getName());
                    mTxtItem2Two.setBackgroundResource(remoteFunInfo2.getIcon_id());

                    break;
                case 3:
                    mViewNoData.setVisibility(View.GONE);
                    mViewOne.setVisibility(View.GONE);
                    mViewTwo.setVisibility(View.GONE);
                    mViewThree.setVisibility(View.VISIBLE);
                    mViewFour.setVisibility(View.GONE);
                    mViewFive.setVisibility(View.GONE);

                    remoteFunInfo1 = mRemoteFunInfos.get(0);
                    remoteFunInfo2 = mRemoteFunInfos.get(1);
                    remoteFunInfo3 = mRemoteFunInfos.get(2);

                    mTxtItem1Three.setTag(remoteFunInfo1);
                    mTxtItem2Three.setTag(remoteFunInfo2);
                    mTxtItem3Three.setTag(remoteFunInfo3);

                    mTxtItem1Three.setText(remoteFunInfo1.getName());
                    mTxtItem1Three.setBackgroundResource(remoteFunInfo1
                            .getIcon_id());

                    mTxtItem2Three.setText(remoteFunInfo2.getName());
                    mTxtItem2Three.setBackgroundResource(remoteFunInfo2
                            .getIcon_id());

                    mTxtItem3Three.setText(remoteFunInfo3.getName());
                    mTxtItem3Three.setBackgroundResource(remoteFunInfo3
                            .getIcon_id());
                    break;
                case 4:
                    mViewNoData.setVisibility(View.GONE);
                    mViewOne.setVisibility(View.GONE);
                    mViewTwo.setVisibility(View.GONE);
                    mViewThree.setVisibility(View.GONE);
                    mViewFour.setVisibility(View.VISIBLE);
                    mViewFive.setVisibility(View.GONE);

                    remoteFunInfo1 = mRemoteFunInfos.get(0);
                    remoteFunInfo2 = mRemoteFunInfos.get(1);
                    remoteFunInfo3 = mRemoteFunInfos.get(2);
                    remoteFunInfo4 = mRemoteFunInfos.get(3);

                    mTxtItem1Four.setTag(remoteFunInfo1);
                    mTxtItem2Four.setTag(remoteFunInfo2);
                    mTxtItem3Four.setTag(remoteFunInfo3);
                    mTxtItem4Four.setTag(remoteFunInfo4);

                    mTxtItem1Four.setText(remoteFunInfo1.getName());
                    mTxtItem1Four
                            .setBackgroundResource(remoteFunInfo1.getIcon_id());

                    mTxtItem2Four.setText(remoteFunInfo2.getName());
                    mTxtItem2Four
                            .setBackgroundResource(remoteFunInfo2.getIcon_id());

                    mTxtItem3Four.setText(remoteFunInfo3.getName());
                    mTxtItem3Four
                            .setBackgroundResource(remoteFunInfo3.getIcon_id());

                    mTxtItem4Four.setText(remoteFunInfo4.getName());
                    mTxtItem4Four
                            .setBackgroundResource(remoteFunInfo4.getIcon_id());
                    break;
                case 5:
                    mViewNoData.setVisibility(View.GONE);
                    mViewOne.setVisibility(View.GONE);
                    mViewTwo.setVisibility(View.GONE);
                    mViewThree.setVisibility(View.GONE);
                    mViewFour.setVisibility(View.GONE);
                    mViewFive.setVisibility(View.VISIBLE);

                    remoteFunInfo1 = mRemoteFunInfos.get(0);
                    remoteFunInfo2 = mRemoteFunInfos.get(1);
                    remoteFunInfo3 = mRemoteFunInfos.get(2);
                    remoteFunInfo4 = mRemoteFunInfos.get(3);
                    remoteFunInfo5 = mRemoteFunInfos.get(4);

                    mTxtItem1Five.setTag(remoteFunInfo1);
                    mTxtItem2Five.setTag(remoteFunInfo2);
                    mTxtItem3Five.setTag(remoteFunInfo3);
                    mTxtItem4Five.setTag(remoteFunInfo4);
                    mTxtItem5Five.setTag(remoteFunInfo5);

                    mTxtItem1Five.setText(remoteFunInfo1.getName());
                    mTxtItem1Five
                            .setBackgroundResource(remoteFunInfo1.getIcon_id());

                    mTxtItem2Five.setText(remoteFunInfo2.getName());
                    mTxtItem2Five
                            .setBackgroundResource(remoteFunInfo2.getIcon_id());

                    mTxtItem3Five.setText(remoteFunInfo3.getName());
                    mTxtItem3Five
                            .setBackgroundResource(remoteFunInfo3.getIcon_id());

                    mTxtItem4Five.setText(remoteFunInfo4.getName());
                    mTxtItem4Five
                            .setBackgroundResource(remoteFunInfo4.getIcon_id());

                    mTxtItem5Five.setText(remoteFunInfo5.getName());
                    mTxtItem5Five
                            .setBackgroundResource(remoteFunInfo5.getIcon_id());
                    break;
            }
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
        com.carlt.doride.control.CPControl.GetCarConfigResult(listener);
    }

    /**
     * 点击逻辑
     */
    private void clickLogic() {
        boolean hasRemotePswMd5 = SesameLoginInfo.isSetRemotePwd();
        // 车辆状态view打开
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
                    Log.e("info", "LoginInfo.isAuthen==" + SesameLoginInfo.isAuthen());
                    if (SesameLoginInfo.isAuthen()) {
                        Intent mIntent = new Intent(RemoteMainNewActivity.this, RemotePswResetActivity3.class);
                        mIntent.putExtra(RemotePswResetActivity3.TYPE,
                                RemotePswResetActivity3.TYPE_REMOTE);
                        startActivity(mIntent);
                    } else {
                        Intent mIntent = new Intent(RemoteMainNewActivity.this, RealNameActivity.class);
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
            PopBoxCreat.createDialogNotitle(RemoteMainNewActivity.this,
                    "设置远程控制", "为保障车辆安全请先设置远程控制密码", "取消", "设置密码", click);

        }
    }

    /**
     * 输入密码Dialog
     */
    private void showEditDialog() {
        LayoutInflater inflater = LayoutInflater
                .from(RemoteMainNewActivity.this);
        View view = inflater.inflate(R.layout.dialog_withedit_new, null);
        final Dialog dialogI = new Dialog(RemoteMainNewActivity.this,
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
                    UUToast.showUUToast(RemoteMainNewActivity.this,
                            "您的密码不能为空哦...");
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

    // 充电状态
    private GetResultListCallback mListener_chargeStatus = new GetResultListCallback() {

        @Override
        public void onFinished(Object o) {
            Message msg = new Message();
            msg.what = 6;
            msg.obj = o;
            mHandler.sendMessage(msg);
        }

        @Override
        public void onErro(Object o) {
            Message msg = new Message();
            msg.what = 7;
            msg.obj = o;
            mHandler.sendMessage(msg);

        }
    };

    // 获取远程状态
    private GetResultListCallback mListener_states = new GetResultListCallback() {

        @Override
        public void onFinished(Object o) {
            Message msg = new Message();
            msg.what = 8;
            msg.obj = o;
            mHandler.sendMessage(msg);
        }

        @Override
        public void onErro(Object o) {
            Message msg = new Message();
            msg.what = 9;
            msg.obj = o;
            mHandler.sendMessage(msg);

        }
    };

    // 获取车辆温度
    private GetResultListCallback mListener_temp = new GetResultListCallback() {

        @Override
        public void onFinished(Object o) {
            Message msg = new Message();
            msg.what = 14;
            msg.obj = o;
            mHandler.sendMessage(msg);
        }

        @Override
        public void onErro(Object o) {
            Message msg = new Message();
            msg.what = 15;
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
                    // 远程操作结果
                    switch (lastOpt) {
                        case 0:

             //               mPlayRadio.playClickVoice(R.raw.remote_unlock);

                            lastOpt = -1;
                            break;
                        case 1:
                //            mPlayRadio.playClickVoice(R.raw.remote_lock);
                            lastOpt = -1;
                            break;
                        case 2:
               //             mPlayRadio.playClickVoice(R.raw.remote_finding);
                            lastOpt = -1;
                            break;
                        case 3:
                            if (mAirConditionDialog != null) {
                                mAirConditionDialog.dismiss();
                            }
                 //           mPlayRadio.playClickVoice(R.raw.remote_air);
                            lastOpt = -1;
                            break;
                        case 4:
                            break;
                    }
                    dissmissWaitingDialog();
//                    UUToast.showUUToast(RemoteMainNewActivity.this, "操作成功");
                    ToastUtils.showShort("操作成功");
                    break;

                case 1:
                    dissmissWaitingDialog();
                    if (mAirConditionDialog != null) {
                        mAirConditionDialog.dismiss();
                    }
                    BaseResponseInfo mInfo1 = (BaseResponseInfo) msg.obj;
                    if (mInfo1 != null) {
                        if (mInfo1.getFlag() == 1020) {
                            Intent intent  = new Intent(RemoteMainNewActivity.this,UpDateActivity.class);
                            startActivity(intent);
//                            PopBoxCreat.showUUUpdateDialog(context, null);
                        } else {
//                            UUToast.showUUToast(RemoteMainNewActivity.this,
//                                    mInfo1.getInfo());
                            ToastUtils.showShort(mInfo1.getInfo());
                        }
                    }
                    break;

                case 3:
                    dissmissWaitingDialog();
                    BaseResponseInfo mInfo = (BaseResponseInfo) msg.obj;
                    if (mInfo != null) {
//                        UUToast.showUUToast(RemoteMainNewActivity.this,
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
//                        UUToast.showUUToast(RemoteMainNewActivity.this,
//                                mInfo2.getInfo());
                        ToastUtils.showShort(mInfo2.getInfo());
                    }
                    isFirstClick = true;
                    break;
                case 6:
                    // 获取充电状态成功
                    dissmissWaitingDialog();
                    sendEmptyMessageDelayed(1000, 100);
                    ChargeStatusInfo chargeStatusInfo = (ChargeStatusInfo) msg.obj;
                    UUChargeDialog mChargeDialog = new UUChargeDialog(context,
                            chargeStatusInfo);
                    mChargeDialog.show();
                    break;
                case 7:
                    // 获取充电状态失败
                    dissmissWaitingDialog();
                    BaseResponseInfo mInfo3 = (BaseResponseInfo) msg.obj;
                    if (mInfo3 != null) {
                        ToastUtils.showShort(mInfo3.getInfo());
                    }
                    break;
                case 8:
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
                case 9:
                    // 获取车辆状态失败
                    dissmissWaitingDialog();
                    BaseResponseInfo mInfo4 = (BaseResponseInfo) msg.obj;
                    if (mInfo4 != null) {
                        UUToast.showUUToast(RemoteMainNewActivity.this,
                                mInfo4.getInfo());
                    }
                    break;
                case 10:
                    GetResult();
                    break;
                case 11:
                    showWaitingDialog(null);
                    // 调用接口
                    break;
                case 12:
                    CPControl.GetRemoteCarTemp(mListener_temp, mAirMainInfo);
                    break;
                case 13:
                    dissmissWaitingDialog();
                    // 获取远程空调功能失败
                    BaseResponseInfo mInfo7 = (BaseResponseInfo) msg.obj;
                    if (mInfo7 != null) {
                        ToastUtils.showShort(mInfo7.getInfo());
                    }
                    break;
                case 14:
                    // 获取远程车辆温度成功
                    dissmissWaitingDialog();
                    mAirMainInfo = (AirMainInfo) msg.obj;
//				mAirMainInfo.setShowTemp(true);
                    mAirConditionDialog = new UUAirConditionDialog(
                            RemoteMainNewActivity.this, mAirMainInfo);
                    mAirConditionDialog.mListener = mListener;
                    mAirConditionDialog.mHandler = mHandler;
                    mAirConditionDialog.show();
                    break;
                case 15:
                    // 获取远程车辆温度失败
                    dissmissWaitingDialog();
                    mAirMainInfo = (AirMainInfo) msg.obj;
//				mAirMainInfo.setShowTemp(true);
                    mAirConditionDialog = new UUAirConditionDialog(
                            RemoteMainNewActivity.this, mAirMainInfo);
                    mAirConditionDialog.mListener = mListener;
                    mAirConditionDialog.mHandler = mHandler;
                    mAirConditionDialog.show();
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

        mDialog = PopBoxCreat.createDialogWithProgress(
                RemoteMainNewActivity.this, msg);
        mDialog.show();
        Log.e("info", "showWaitingDialog--------------");
    }

    public void dissmissWaitingDialog() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
            Log.e("info", "dissmissWaitingDialog--------------");
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

}

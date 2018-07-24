
package com.carlt.sesame.ui.activity.car;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.carlt.sesame.R;
import com.carlt.sesame.control.CPControl;
import com.carlt.sesame.control.CPControl.GetResultListCallback;
import com.carlt.sesame.data.BaseResponseInfo;
import com.carlt.sesame.data.LoginInfo;
import com.carlt.sesame.data.car.CarMainInfo;
import com.carlt.sesame.data.car.TirepressureInfo;
import com.carlt.sesame.preference.EntryInfoLocal;
import com.carlt.sesame.ui.activity.base.LoadingActivityWithTitle;
import com.carlt.sesame.ui.view.CreateDialogTips;
import com.carlt.sesame.ui.view.CreateDialogTips.DialogBtnClick;
import com.carlt.sesame.ui.view.PopBoxCreat;
import com.carlt.sesame.ui.view.PopBoxCreat.DialogWithTitleClick;
import com.carlt.sesame.ui.view.TirePressureView;
import com.carlt.sesame.utility.UUToast;

/**
 * 座驾-胎压监测页面
 * 
 * @author daisy
 */
public class CarTirePressureActivity extends LoadingActivityWithTitle {
    private ImageView back;// 头部返回键

    private TextView title;// 标题文字

    private TextView txtRight;// 头部右侧文字
    private ImageView imgRight;// 头部右侧图标

    private ImageView mImageViewSecretary;// 车秘书头像

    private TextView mTextViewSecretary;// 车秘书提醒消息

    private TirePressureView mWheelView1;// 车胎轮速-左前

    private TirePressureView mWheelView2;// 车胎轮速-右前

    private TirePressureView mWheelView3;// 车胎轮速-左后

    private TirePressureView mWheelView4;// 车胎轮速-右后

    private RelativeLayout mLayoutBottom;// 底部学习view

    private RelativeLayout mLayoutBottomPro;// //底部激活进度

    private TextView mTxtActivate;// 胎压监测功能激活与否描述文字

    private ImageView mImageView;// 自学习功能

    private TextView mTxtPro;// 显示激活进度文字

    private ProgressBar mProgressBar;// 激活进度条

    private Dialog mDialog;

    private final static long delay = 10 * 1000;// 激活时向后台发送请求的时间间隔

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_tire_pressure);
        setTitleView(R.layout.head_back);
        initTitle();
        initSubtitle();
        init();
        LoadData();

    }

    private void initTitle() {
        back = (ImageView)findViewById(R.id.head_back_img1);
        title = (TextView)findViewById(R.id.head_back_txt1);
        txtRight = (TextView)findViewById(R.id.head_back_txt2);
        imgRight = (ImageView) findViewById(R.id.head_back_img2);

        back.setImageResource(R.drawable.arrow_back);
        title.setText("胎压监测");
        txtRight.setVisibility(View.GONE);
		imgRight.setVisibility(View.VISIBLE);
		imgRight.setImageResource(R.drawable.icon_refresh);

        back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

        imgRight.setOnClickListener(mViewClickListener);
    }

    private void initSubtitle() {
        mImageViewSecretary = (ImageView)findViewById(R.id.layout_sub_head_img);
        mTextViewSecretary = (TextView)findViewById(R.id.layout_sub_head_txt);
    }

    private void init() {

        mWheelView1 = (TirePressureView)findViewById(R.id.activity_car_tire_pressure_lay1);
        mWheelView2 = (TirePressureView)findViewById(R.id.activity_car_tire_pressure_lay2);
        mWheelView3 = (TirePressureView)findViewById(R.id.activity_car_tire_pressure_lay3);
        mWheelView4 = (TirePressureView)findViewById(R.id.activity_car_tire_pressure_lay4);
        mTxtActivate = (TextView)findViewById(R.id.activity_car_tire_pressure_txt_activate);

        mImageView = (ImageView)findViewById(R.id.activity_car_tire_pressure_img_activate);

        mLayoutBottom = (RelativeLayout)findViewById(R.id.activity_car_tire_pressure_bottom);
        mLayoutBottomPro = (RelativeLayout)findViewById(R.id.activity_car_tire_pressure_lay_progress);

        mTxtPro = (TextView)findViewById(R.id.activity_car_tire_pressure_txt_progress);
        mProgressBar = (ProgressBar)findViewById(R.id.activity_car_tire_pressure_progress);

        mImageView.setOnClickListener(mClickListener);

    }

    private void initEntryInfo(int state) {
        String userId = LoginInfo.getUseId();
        int times = 0;
        if (userId != null && userId.length() > 0) {
            times = EntryInfoLocal.getEntryTimes(userId);
            if (times > 0) {

            } else {
                if (state == CarMainInfo.ACTIVATIONING) {
                    // 激活中
                    //showDialogTips();
                } else {

                }
            }
        }

        times++;
        EntryInfoLocal.setEntryTimes(times);
    }

    @Override
    protected void LoadSuccess(Object data) {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }

        if (data != null) {
            TirepressureInfo info = (TirepressureInfo)data;

            int tirepressure = info.getTirepressure();
            String time = info.getTime();
            switch (tirepressure) {
                case CarMainInfo.ABNORMAL:
                    // 异常
                    String string1 = "胎压出现异常，请立即停车检查！";
                    if (time != null) {
                        string1 = string1 + "数据获取时间：" + time;
                    }
                    mTextViewSecretary.setText(string1);

                    normalOrNot(info);
                    break;
                case CarMainInfo.NORMAL:
                    // 正常
                    String string2 = "胎压正常！请继续欢快的奔跑吧！";
                    if (time != null) {
                        string2 = string2 + "数据获取时间：" + time;
                    }
                    mTextViewSecretary.setText(string2);
                    mTextViewSecretary.setText(string2);
                    normalOrNot(info);
                    break;
                case CarMainInfo.UNACTIVATION:
                    // 未激活
                    unactivated();
                    break;
                case CarMainInfo.ACTIVATIONING:
                    // 激活中
                    activationing(info);
                    mHandler.sendEmptyMessage(10);
                    break;
                case CarMainInfo.CONNECTERRO:
                    // 终端抓取胎压数据失败
                    connectERRO();
                    break;
            }
            initEntryInfo(tirepressure);
        }
        super.LoadSuccess(data);
    }

    @Override
    protected void LoadErro(Object erro) {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
        super.LoadErro(erro);
    }

    @Override
    protected void LoadData() {
        super.LoadData();
        CPControl.GetTireDirectResult(listener);

    }

    private OnClickListener mViewClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.head_back_img2:
                    // 右上角刷新按钮
                    if (mDialog == null) {
                        mDialog = PopBoxCreat.createDialogWithProgress(
                                CarTirePressureActivity.this, "获取数据");
                    }
                    mDialog.show();
                    CPControl.GetTireDirectResult(listener);
                    break;

            }

        }
    };

    private OnClickListener mClickListener = new OnClickListener() {

        @Override
        public void onClick(View arg0) {
            // 弹出自学习提示框
            DialogWithTitleClick click = new DialogWithTitleClick() {

                @Override
                public void onRightClick() {
                    // 取消

                }

                @Override
                public void onLeftClick() {
                    // 确定
                    if (mDialog == null) {
                        mDialog = PopBoxCreat.createDialogWithProgress(
                                CarTirePressureActivity.this, "加载中...");
                    }
                    mDialog.show();
                    CPControl.GetTirepresLearnResult(listener_learn);
                }
            };
            PopBoxCreat.createDialogWithTitle(CarTirePressureActivity.this, "提示",
                    "胎压监测的激活会在行驶过程中自动完成，请务必确认各轮胎压力正常再进行激活操作，您确定要激活吗？", "", "确定", "取消", click);
        }
    };

    /**
     * 弹出使用说明提示框
     */
    private void showDialogTips() {
        DialogBtnClick click = new DialogBtnClick() {

            @Override
            public void onClick() {
                // TODO Auto-generated method stub

            }
        };
        CreateDialogTips.createDialogTips(CarTirePressureActivity.this, click);
    }

    private GetResultListCallback listener_learn = new GetResultListCallback() {

        @Override
        public void onFinished(Object o) {
            Message msg = new Message();
            msg.what = 0;
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

    /**
     * 刷新激活进度回调
     */
    private GetResultListCallback listener_progress = new GetResultListCallback() {

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

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            BaseResponseInfo mBaseResponseInfo;
            String responseInfo;
            switch (msg.what) {
                case 0:
                    // 激活下发成功
                    if (mDialog != null && mDialog.isShowing()) {
                        mDialog.dismiss();
                    }
                    showDialogTips();
                    mHandler.sendEmptyMessage(10);
                    UUToast.showUUToast(CarTirePressureActivity.this, "开始激活!");
                    break;

                case 1:
                    if (mDialog != null && mDialog.isShowing()) {
                        mDialog.dismiss();
                    }
                    mBaseResponseInfo = (BaseResponseInfo)msg.obj;
                    responseInfo = mBaseResponseInfo.getInfo();
                    if (responseInfo != null && responseInfo.length() > 0) {
                        UUToast.showUUToast(CarTirePressureActivity.this,
                                mBaseResponseInfo.getInfo());
                    } else {
                        UUToast.showUUToast(CarTirePressureActivity.this, "获取激活进度失败");
                    }

                    break;

                case 2:
                    // 获取激活进度成功
                    TirepressureInfo info = (TirepressureInfo)msg.obj;
                    activationing(info);

                    int progress = info.getProgress();
                    if (progress == 100) {
                        DialogWithTitleClick click = new DialogWithTitleClick() {

                            @Override
                            public void onLeftClick() {
                                // 获取胎压数据
                                LoadData();
                            }

                            @Override
                            public void onRightClick() {
                                // TODO Auto-generated method stub

                            }
                        };
                        PopBoxCreat.createDialogWithTitle(CarTirePressureActivity.this, "激活成功",
                                "盒子已经激活成功啦！快去获取胎压数据吧！", "", "好", "", click);
                    } else {
                        mHandler.sendEmptyMessageDelayed(10, delay);
                    }

                    break;
                case 3:
                    // 获取激活进度失败
                    mBaseResponseInfo = (BaseResponseInfo)msg.obj;
                    responseInfo = mBaseResponseInfo.getInfo();
                    if (responseInfo != null && responseInfo.length() > 0) {
                        UUToast.showUUToast(CarTirePressureActivity.this,
                                mBaseResponseInfo.getInfo());
                    } else {
                        UUToast.showUUToast(CarTirePressureActivity.this, "获取激活进度失败");
                    }

                    break;
                case 10:
                    // 调用激活进度接口
                    CPControl.GetTireProgressResult(listener_progress);
                    break;

            }
        };
    };

    /**
     * 异常或正常
     */
    private void normalOrNot(TirepressureInfo info) {
        txtRight.setOnClickListener(mViewClickListener);
        txtRight.setTextColor(getResources().getColor(R.color.text_color_gray3));

        // 1不正常，0 正常
        int state;
        int stateNew;
        state = info.getState1();
        if (state == 1) {
            stateNew = TirePressureView.ABNORMAL;
        } else {
            stateNew = TirePressureView.NORMAL;
        }
        mWheelView1.setTireState(stateNew,info.getCoefficient1(),info.getUnit1());
        mWheelView1.setTireValue(info.getCoefficient1());

        state = info.getState2();
        if (state == 1) {
            stateNew = TirePressureView.ABNORMAL;
        } else {
            stateNew = TirePressureView.NORMAL;
        }
        mWheelView2.setTireState(stateNew,info.getCoefficient2(),info.getUnit2());
        mWheelView2.setTireValue(info.getCoefficient2());

        state = info.getState3();
        if (state == 1) {
            stateNew = TirePressureView.ABNORMAL;
        } else {
            stateNew = TirePressureView.NORMAL;
        }
        mWheelView3.setTireState(stateNew,info.getCoefficient3(),info.getUnit3());
        mWheelView3.setTireValue(info.getCoefficient3());

        state = info.getState4();
        if (state == 1) {
            stateNew = TirePressureView.ABNORMAL;
        } else {
            stateNew = TirePressureView.NORMAL;
        }
        mWheelView4.setTireState(stateNew,info.getCoefficient4(),info.getUnit4());
        mWheelView4.setTireValue(info.getCoefficient4());

        mTxtActivate.setText("如果您的爱车换过轮胎或打过气，请重新激活胎压监测功能！ ");
        mImageView.setImageResource(R.drawable.tire_pressure_activate);
        mImageView.setClickable(true);

        mLayoutBottom.setVisibility(View.GONE);
        mLayoutBottomPro.setVisibility(View.GONE);
    }

    /**
     * 激活中
     */
    private void activationing(TirepressureInfo info) {
        txtRight.setOnClickListener(null);
        txtRight.setTextColor(getResources().getColor(R.color.text_color_gray1));

        int tireValue = 0;
        mWheelView1.setTireState(TirePressureView.ACTIVATIONING,info.getCoefficient1(),info.getUnit1());
        mWheelView1.setTireValue(tireValue);

        mWheelView2.setTireState(TirePressureView.ACTIVATIONING,info.getCoefficient2(),info.getUnit2());
        mWheelView2.setTireValue(tireValue);

        mWheelView3.setTireState(TirePressureView.ACTIVATIONING,info.getCoefficient3(),info.getUnit3());
        mWheelView3.setTireValue(tireValue);

        mWheelView4.setTireState(TirePressureView.ACTIVATIONING,info.getCoefficient4(),info.getUnit4());
        mWheelView4.setTireValue(tireValue);

        mTextViewSecretary.setBackgroundResource(R.drawable.head_sub_tip_bg3);
        mTextViewSecretary.setText("胎压监测激活中!行驶过程中才会执行激活程序,熄火后会暂停激活，直到下次启动后再继续执行");

        mLayoutBottom.setVisibility(View.GONE);
        mLayoutBottomPro.setVisibility(View.GONE);

        mTxtPro.setText("激活进度：" + info.getProgress() + "%");
        mProgressBar.setProgress(info.getProgress());
    }

    /**
     * 未激活
     */
    private void unactivated() {
        txtRight.setOnClickListener(mViewClickListener);
        txtRight.setTextColor(getResources().getColor(R.color.text_color_gray3));

        int tireValue = 0;
        mWheelView1.setTireState(TirePressureView.UNACTIVATION,-1,"");
        mWheelView1.setTireValue(tireValue);

        mWheelView2.setTireState(TirePressureView.UNACTIVATION,-1,"");
        mWheelView2.setTireValue(tireValue);

        mWheelView3.setTireState(TirePressureView.UNACTIVATION,-1,"");
        mWheelView3.setTireValue(tireValue);

        mWheelView4.setTireState(TirePressureView.UNACTIVATION,-1,"");
        mWheelView4.setTireValue(tireValue);

        mTextViewSecretary.setBackgroundResource(R.drawable.head_sub_tip_bg3);
        mTextViewSecretary.setText("您还没有激活胎压监测功能，您可点击底部的红色按钮进行激活");

        mTxtActivate.setText("您还没有激活胎压监测功能，快点击右侧按钮激活吧！");
        mImageView.setImageResource(R.drawable.tire_pressure_activate);
        mImageView.setClickable(true);

        mLayoutBottom.setVisibility(View.GONE);
        mLayoutBottomPro.setVisibility(View.GONE);

        // 弹出自学习提示框
        DialogWithTitleClick click = new DialogWithTitleClick() {

            @Override
            public void onRightClick() {
                // 取消

            }

            @Override
            public void onLeftClick() {
                // 确定
                if (mDialog == null) {
                    mDialog = PopBoxCreat.createDialogWithProgress(CarTirePressureActivity.this,
                            "加载中...");
                }
                mDialog.show();
                CPControl.GetTirepresLearnResult(listener_learn);
            }
        };
        PopBoxCreat.createDialogWithTitle(CarTirePressureActivity.this, "提示",
                "胎压监测的激活会在行驶过程中自动完成，请务必确认各轮胎压力正常再进行激活操作，您确定要激活吗？", "", "确定", "取消", click);

    }

    // 获取数据失败
    private void connectERRO() {
        int tireValue = 0;
        mWheelView1.setTireState(TirePressureView.CONNECT_ERRO,-1,"");
        mWheelView1.setTireValue(tireValue);

        mWheelView2.setTireState(TirePressureView.CONNECT_ERRO,-1,"");
        mWheelView2.setTireValue(tireValue);

        mWheelView3.setTireState(TirePressureView.CONNECT_ERRO,-1,"");
        mWheelView3.setTireValue(tireValue);

        mWheelView4.setTireState(TirePressureView.CONNECT_ERRO,-1,"");
        mWheelView4.setTireValue(tireValue);

        mTextViewSecretary.setBackgroundResource(R.drawable.head_sub_tip_bg3);
        mTextViewSecretary.setText("未收到响应数据");

        mTxtActivate.setText("如果您的车换过胎或打过气，请重新激活胎压监测功能！ ");
        mImageView.setImageResource(R.drawable.tire_pressure_activate);
        mImageView.setClickable(true);

        mLayoutBottom.setVisibility(View.GONE);
        mLayoutBottomPro.setVisibility(View.GONE);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeMessages(0);
        mHandler.removeMessages(1);
        mHandler.removeMessages(2);
        mHandler.removeMessages(10);
    }
}

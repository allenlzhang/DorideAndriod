
package com.carlt.sesame.ui.activity.usercenter.login;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.carlt.sesame.R;
import com.carlt.sesame.control.ActivityControl;
import com.carlt.sesame.control.CPControl;
import com.carlt.sesame.control.CPControl.GetResultListCallback;
import com.carlt.sesame.control.LoginControl;
import com.carlt.sesame.data.BaseResponseInfo;
import com.carlt.sesame.data.LoginInfo;
import com.carlt.sesame.data.UseInfo;
import com.carlt.sesame.preference.UseInfoLocal;
import com.carlt.sesame.ui.activity.base.BaseActivity;
import com.carlt.sesame.ui.activity.usercenter.SelectCarBindActivity;
import com.carlt.sesame.ui.view.PopBoxCreat;
import com.carlt.sesame.ui.view.PopBoxCreat.DialogWithTitleClick;
import com.carlt.sesame.ui.view.UUTimerDialog;
import com.carlt.sesame.utility.StringUtils;
import com.carlt.sesame.utility.UUToast;

/**
 * 激活盒子页面
 * 
 * @author daisy
 */

public class ActivateActivity extends BaseActivity {
    private ImageView back;// 头部返回键

    private TextView title;// 标题文字

    private TextView txtRight;// 头部右侧文字

    private ImageView mImageViewSecretary;// 车秘书头像

    private TextView mTextViewSecretary;// 提醒消息

    private TextView mTxtActivate;// 激活盒子
    private EditText mViewET;// PIN 编辑框

    private View mViewSaleafter;// 后装页面

    private View mViewSalebefore;// 前装页面

    private UUTimerDialog mDialog;

    public final static String ID = "id";

    public final static String FROM_NAME = "from_name";

    public final static String CLASS_START = "com.carlt.sesame.ui.StartActivity";// 从启动页跳转过来的

    public final static String CLASS_LOGIN = "com.carlt.sesame.ui.activity.usercenter.login.LoginActivity";// 从登录页跳转过来的

    public final static String CLASS_SELECTCARBIND = "com.carlt.sesame.ui.activity.usercenter.SelectCarBindActivity";// 从注册选车绑定页跳转过来的

    public final static String CLASS_SCANCODE = "com.carlt.sesame.ui.activity.usercenter.TwoDemisonCodeActivity";// 从扫描二维码页面

    public final static String CLASS_DEVICE_UPDATE = "com.carlt.sesame.ui.activity.usercenter.login.DeviceUpdateActivity";// 从固件升级页跳转过来的
    
    public final static String CLASS_DEVICE_REBIND = "com.carlt.sesame.ui.activity.setting.ManageReBindActivity";//重新绑定

    private String fromName;// 纪录从何处跳转的

    private String need_pin;// 是否需要输入pin码 1不需要 0需要

    private long isOnlineTime = 1000 * 3;

    private long delayedTime = 1000 * 3;

    private long gapTime = 1000 * 1;

    private int times = 0;// 拉取次数

    private boolean isCheckThreadRun = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activate_device);

        try {
            fromName = getIntent().getStringExtra(FROM_NAME);
            need_pin = getIntent().getStringExtra("need_pin");
        } catch (Exception e) {
            // TODO: handle exception
        }

        initTitle();
        initSubTitle();
        init();

    }

    private void initTitle() {
        back = (ImageView)findViewById(R.id.head_back_img1);
        title = (TextView)findViewById(R.id.head_back_txt1);
        txtRight = (TextView)findViewById(R.id.head_back_txt2);

        back.setImageResource(R.drawable.arrow_back);
        title.setText("激活设备");
        txtRight.setVisibility(View.GONE);
        back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                back();
            }
        });

    }

    private void initSubTitle() {
        mImageViewSecretary = (ImageView)findViewById(R.id.layout_sub_head_img);
        mTextViewSecretary = (TextView)findViewById(R.id.layout_sub_head_txt);

        mTextViewSecretary.setText("设备绑定成功！激活设备后就能使用芝麻乐园的全部功能啦！");
    }

    private void init() {
        mTxtActivate = (TextView)findViewById(R.id.activity_activate_device_btn);

        mViewSaleafter = findViewById(R.id.activate_device_lay_saleafter);
        mViewSalebefore = findViewById(R.id.activate_device_lay_salebefore);
        mViewET = (EditText) findViewById(R.id.activity_bind_editpin);

        mTxtActivate.setOnClickListener(mClickListener);
//        if (LoginInfo.isInstallorder()) {
//            // 为前装盒子
//            mViewSaleafter.setVisibility(View.GONE);
//            mViewSalebefore.setVisibility(View.VISIBLE);
//        } else {
//            // 为后装盒子
//            mViewSalebefore.setVisibility(View.GONE);
//            mViewSaleafter.setVisibility(View.VISIBLE);
//        }

        if(TextUtils.equals(need_pin,"0")){
            mViewET.setVisibility(View.VISIBLE);
        }else{
            mViewET.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onDestroy() {
        isCheckThreadRun = false;
        super.onDestroy();
    }

    private String pin = "";//设备pin

    private OnClickListener mClickListener = new OnClickListener() {

        @Override
        public void onClick(View arg0) {
            DialogWithTitleClick click = new DialogWithTitleClick() {

                @Override
                public void onLeftClick() {
                    String text = mViewET.getText().toString();
                    if(StringUtils.isEmpty(text) && TextUtils.equals(need_pin,"0")){// 0 是不需要
                        UUToast.showUUToast(ActivateActivity.this, "请输入正确PIN码");
                    }else {
                        // 调用激活盒子接口
                        pin = text;
                        mDialog = PopBoxCreat.createUUTimerDialog(ActivateActivity.this, "设备正在激活中(根据网络情况可能需要3-5分钟，请耐心等待)");
                            mDialog.show();
                            listener_time = System.currentTimeMillis();
                            ActivateCount++;
                            mTextViewSecretary.setText("已收到激活请求，正在连接芝麻盒子…");
                            CPControl.GetDeviceidActivateResult(text, listener);
                    }
                }

                @Override
                public void onRightClick() {
                    // TODO Auto-generated method stub

                }
            };
            PopBoxCreat.createDialogWithTitle(ActivateActivity.this, "激活", "您确定激活设备吗？", "", "确定",
                    "取消", click);
        }
    };

    private long listener_time;

    GetResultListCallback listener_relogin = new GetResultListCallback() {

        @Override
        public void onFinished(Object o) {
            mHandler.sendEmptyMessage(1003);
        }

        @Override
        public void onErro(Object o) {
            mHandler.sendEmptyMessage(2003);
        }

    };

    GetResultListCallback listener = new GetResultListCallback() {

        @Override
        public void onFinished(Object o) {
            mHandler.sendEmptyMessageDelayed(0, delayedTime);
        }

        @Override
        public void onErro(Object o) {
            boolean t = (System.currentTimeMillis() - listener_time) > ONEMIN;
            BaseResponseInfo mBaseResponseInfo = (BaseResponseInfo)o;

            int flagCode = mBaseResponseInfo.getFlag();


            if (flagCode == 2997 && !t) {
                //继续请求
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            sleep(gapTime);
                        } catch (Exception e) {
                            // TODO: handle exception
                        }
                        CPControl.GetDeviceidActivateResult(pin, listener);
                    }
                }.start();
            }else{
                Message msg = new Message();
                msg.what = 1;
                msg.obj = o;
                mHandler.sendMessage(msg);
            }

        }
    };

    GetResultListCallback listener_isOnline = new GetResultListCallback() {

        @Override
        public void onFinished(Object o) {
            boolean f = (Boolean)o;
            boolean t = (System.currentTimeMillis() - listener_isOnline_time) > (ONEMIN * 2);
            if (f || t) {
                mHandler.sendEmptyMessage(2);
            } else {

                new Thread() {
                    @Override
                    public void run() {
                        try {
                            sleep(isOnlineTime);
                        } catch (Exception e) {
                            // TODO: handle exception
                        }
                        CPControl.GetDeviceidIsOnLineResult(listener_isOnline);
                    }
                }.start();

            }
        }

        @Override
        public void onErro(Object o) {
            // TODO Auto-generated method stub

        }
    };

    private final static long ONEMIN = 1000 * 60;

    private long listener_isOnline_time;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    // 下发激活指令成功
                    if (mDialog != null) {
                        mDialog.goneWatch();
                        mDialog.setTitleText("提示");
                        mDialog.setContentText("芝麻盒子正在更新配置，这个过程大约需要半分钟，请耐心等待");
                    }

                    listener_isOnline_time = System.currentTimeMillis();
                    CPControl.GetDeviceidIsOnLineResult(listener_isOnline);
                    break;
                case 1:
                    BaseResponseInfo mBaseResponseInfo = (BaseResponseInfo)msg.obj;
                    ErroSwitch(mBaseResponseInfo);
                    break;
                case 2:
                    // Intent mIntent = new Intent(ActivateActivity.this,
                    // ActivateSuccessActivity.class);
                    // startActivity(mIntent);
                    // 重新登录一次获取信息
                    reLogin();
                    break;
                case 1003:
                    UUToast.showUUToast(ActivateActivity.this, "芝麻盒子已成功激活");
                    ActivityControl.initXG();
                    LoginControl.logic(ActivateActivity.this);
                    finish();
                    break;
                case 2003:
                    reLogin();
                    break;

            }
        }

        /**
		 * 
		 */
        private void reLogin() {
            if (times < 2) {
                UseInfo mUseInfo = UseInfoLocal.getUseInfo();
                String account = mUseInfo.getAccount();
                String password = mUseInfo.getPassword();
                CPControl.GetLogin(account, password, listener_relogin);
            } else {
                UUToast.showUUToast(ActivateActivity.this, "芝麻盒子已成功激活");
                LoginControl.logic(ActivateActivity.this);
                finish();
            }
            times++;
        }

    };

    private final static String e1 = "芝麻盒子连接不上，请尝试在手机信号良好的地方重新激活";

    private final static String e2 = "貌似不是很顺利哦，请拨打电话4006-506-507咨询客服";

    private final static String e3 = "请先将爱车熄火，再重新点击激活";

    private int ActivateCount;

    private void ErroSwitch(BaseResponseInfo mBaseResponseInfo) {
        int code = mBaseResponseInfo.getFlag();
        // 测试用
        // code=1021;
        if (code == 1020) {
            // UUToast.showUUToast(this, mBaseResponseInfo.getInfo());
            // Intent intent = new Intent(this, DeviceUpdateActivity.class);
            // startActivity(intent);
            // finish();
			PopBoxCreat.showUUUpdateDialog(ActivateActivity.this, null);
        } else if (code == 1021) {
            // PIN码验证失败，跳转至选择车辆绑定页面
            if (mDialog != null && mDialog.isShowing()) {
                mDialog.dismiss();
                mDialog = null;
            }
            UUToast.showUUToast(this, mBaseResponseInfo.getInfo());
//            Intent intent = new Intent(this, SelectCarBindActivity.class);
//            intent.putExtra(SelectCarBindActivity.FROM_NAME, ActivateActivity.this.getClass()
//                    .getName());
//            startActivity(intent);
//            finish();
        } else {
            if (mDialog != null && mDialog.isShowing()) {
                mDialog.dismiss();
                mDialog = null;
            }
            mTextViewSecretary.setText("盒子绑定成功！激活盒子后就能使用芝麻乐园的全部功能啦！");
            UUToast.showUUToast(this, mBaseResponseInfo.getInfo());
        }

    }

    private void back() {
        if (fromName != null) {


        	if(!TextUtils.isEmpty(fromName) && fromName.equals(CLASS_DEVICE_REBIND)){
                LoginInfo.setIsJumptoBind(LoginInfo.noJumptoBind);
                ActivityControl.onLogout(context);
        	}else{
        		
        		if("1".equals(LoginInfo.getDevicetype())){//更换设备登陆进来的
                  Intent mIntent = new Intent(ActivateActivity.this, LoginActivity.class);
                  startActivity(mIntent);
                  finish();
        		}else{
        			// 跳转至选车型输入序列号二合一页面
                    LoginInfo.setIsJumptoBind(LoginInfo.noJumptoBind);
                    Intent mIntent = new Intent(ActivateActivity.this, SelectCarBindActivity.class);
                    mIntent.putExtra(SelectCarBindActivity.FROM_NAME, ActivateActivity.this.getClass()
                            .getName());
                    startActivity(mIntent);
                    finish();
        		}
        	}
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            back();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}

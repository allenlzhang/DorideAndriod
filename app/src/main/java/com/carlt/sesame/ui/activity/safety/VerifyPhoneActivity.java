
package com.carlt.sesame.ui.activity.safety;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.carlt.doride.R;
import com.carlt.sesame.control.ActivityControl;
import com.carlt.sesame.control.CPControl;
import com.carlt.sesame.control.CPControl.GetResultListCallback;
import com.carlt.sesame.data.BaseResponseInfo;
import com.carlt.sesame.data.LoginInfo;
import com.carlt.sesame.ui.activity.base.BaseActivity;
import com.carlt.sesame.ui.view.GetValidateView;
import com.carlt.sesame.ui.view.PopBoxCreat;
import com.carlt.sesame.ui.view.PopBoxCreat.DialogWithTitleClick;
import com.carlt.sesame.ui.view.ValidateEditText;
import com.carlt.sesame.utility.UUToast;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 手机号码验证
 * 
 * @author Daisy
 */
public class VerifyPhoneActivity extends BaseActivity implements OnClickListener {

    private ImageView back;// 头部返回键

    private TextView title;// 标题文字

    private ValidateEditText mEdtPhoneOld;// 旧主机号码

    private ValidateEditText mEdtPhoneNew;// 新主机号码

    private ValidateEditText mEdtCode;// 验证码

    private TextView mTxtCode;// 发送验证码

    private TextView mBtnOption;// 更换主机按钮

    private View mViewPhoneNew;// 旧主机号码外层view

    private Dialog mDialog;

    private String mobile_name;// 手机名称

    private String mobile_id;// 手机唯一标识id

    public final static String MOBILE_NAME = "mobile_name";

    public final static String MOBILE_ID = "mobile_id";

    boolean isMain;// 当前手机是否是主机

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone);

        isMain = LoginInfo.isMain();
        try {
            mobile_name = getIntent().getStringExtra(MOBILE_NAME);
            mobile_id = getIntent().getStringExtra(MOBILE_ID);
        } catch (Exception e) {
            // TODO: handle exception
        }

        initTitle();
        init();
    }

    private void initTitle() {
        back = (ImageView)findViewById(R.id.head_back_img1);
        title = (TextView)findViewById(R.id.head_back_txt1);

        title.setText("主机验证");

        back.setImageResource(R.drawable.arrow_back);
        back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void init() {
        mEdtPhoneOld = (ValidateEditText)findViewById(R.id.verify_phone_edt_phoneold);
        mEdtCode = (ValidateEditText)findViewById(R.id.verify_phone_edt_code);
        mEdtPhoneNew = (ValidateEditText)findViewById(R.id.verify_phone_edt_phonenew);
        
        
        mEdtPhoneOld.setEditHint("请输入旧手机号码");
        mEdtPhoneOld.setmType(ValidateEditText.TYPE_PON);
        mEdtPhoneOld.setNextEditText(mEdtPhoneNew);
        
        mEdtPhoneNew.setEditHint("请输入新主机手机号码");
        mEdtPhoneNew.setmType(ValidateEditText.TYPE_PON);
        mEdtPhoneNew.setNextEditText(mEdtCode);
        
        
        mEdtCode.setEditHint("请输入验证码");
        mTxtCode = (TextView)findViewById(R.id.verify_phone_txt_code);
        mBtnOption = (TextView)findViewById(R.id.verify_phone_txt_option);

        mViewPhoneNew = findViewById(R.id.verify_phone_lay_phonenew);

        if (LoginInfo.isMain()) {
            mViewPhoneNew.setVisibility(View.GONE);
        } else {
            mViewPhoneNew.setVisibility(View.VISIBLE);
        }

        mTxtCode.setOnClickListener(this);
        mBtnOption.setOnClickListener(this);
    }

    /**
     * 倒计时
     */
    private int count = 60;

    private Timer timer = new Timer();

    private TimerTask task;

    private GetValidateView mValidateView;// 播报语音验证码页面

    String phoneNum;// 手机号

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.verify_phone_txt_code:
                // 获取验证码
                if (LoginInfo.isMain()) {
                	mEdtPhoneOld.validateEdit();
                    phoneNum = mEdtPhoneOld.getText().toString();
                } else {
                	mEdtPhoneNew.validateEdit();
                    phoneNum = mEdtPhoneNew.getText().toString();
                }
                if (phoneNum != null && phoneNum.length() == 11) {
                    //                    判斷輸入手機號是否為当前账号手机号
                    String currentPhone = LoginInfo.getUsername();
                    Log.e("currentPhone----", currentPhone);
                    if ( !currentPhone.equals(phoneNum)) {
                        UUToast.showUUToast(this, "你输入的手机号非当前账号的手机号");
                        return;
                    }
                    CPControl.GetMessageValidateResult(CPControl.VALIDATE_TYPE_AUTHORMAIN,
                            phoneNum, listener_code);
                    count = 60;
                    mTxtCode.setText(count + "秒后重发");
                    mTxtCode.setClickable(false);
                    mTxtCode.setBackgroundResource(R.drawable.btn_code_gray);

                    task = new TimerTask() {

                        @Override
                        public void run() {
                            Message msg = new Message();
                            msg.what = 100;
                            mHandler.sendMessage(msg);

                        }
                    };
                    timer.schedule(task, 1000, 1000);
                } else {
                    UUToast.showUUToast(VerifyPhoneActivity.this, "请输入正确的手机号");
                }
                break;

            case R.id.verify_phone_txt_option:
            	mEdtPhoneOld.validateEdit();
            	mEdtPhoneNew.validateEdit();
            	mEdtCode.validateEdit();
                // 验证手机相关信息
                String phoneOld = mEdtPhoneOld.getText().toString();
                String phoneNew = mEdtPhoneNew.getText().toString();
                String phoneCode = mEdtCode.getText().toString();
                if (phoneOld == null || phoneOld.length() <= 0) {
                    UUToast.showUUToast(VerifyPhoneActivity.this, "您还没有填写您的主机电话号码哦...");
                    return;
                } else if (phoneCode == null || phoneCode.length() <= 0) {
                    UUToast.showUUToast(VerifyPhoneActivity.this, "您还没有填写验证码哦...");
                    return;
                } else {
                    if (LoginInfo.isMain()) {
                        if (mDialog == null) {
                            mDialog = PopBoxCreat.createDialogWithProgress(
                                    VerifyPhoneActivity.this, "处理中...");
                        }
                        mDialog.show();
                        // 调用验证码验证接口
                        CPControl.GetChangeMainDeviceResult(CPControl.CHANGE_BY_PHONE, "", "",
                                phoneOld, phoneOld, phoneCode, mobile_id, mobile_name,
                                listener_change);
                    } else {
                        if (phoneNew == null || phoneNew.length() <= 0) {
                            UUToast.showUUToast(VerifyPhoneActivity.this, "您还没有填写您的新主机电话号码哦...");
                            return;
                        } else {
                            if (mDialog == null) {
                                mDialog = PopBoxCreat.createDialogWithProgress(
                                        VerifyPhoneActivity.this, "处理中...");
                            }
                            mDialog.show();
                            // 调用验证主机号码接口
                            CPControl.GetChangeMainDeviceResult(CPControl.CHANGE_BY_PHONE, "", "",
                                    phoneOld, phoneNew, phoneCode, mobile_id, mobile_name,
                                    listener_change);
                        }
                    }
                }
                break;
        }
    }

    // listener-获取验证码
    private GetResultListCallback listener_code = new GetResultListCallback() {

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

    // listener-更换主机
    private GetResultListCallback listener_change = new GetResultListCallback() {

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
            BaseResponseInfo mInfo;
            String infoMsg;
            switch (msg.what) {
                case 0:
                    // 获取验证码成功
                    UUToast.showUUToast(VerifyPhoneActivity.this, msg.obj.toString());
                    break;
                case 1:
                    // 获取验证码失败
                    // 停止计时
                    if (timer != null) {
                        if (task != null) {
                            task.cancel();
                        }
                    }
                    mTxtCode.setClickable(true);
                    mTxtCode.setText(R.string.usercenter_push_validate1);
                    mTxtCode.setBackgroundResource(R.drawable.btn_code_bg);
                    mInfo = (BaseResponseInfo)msg.obj;
                    if (mInfo != null) {
                        String info = mInfo.getInfo();
                        if (info != null && info.length() > 0) {
                            infoMsg = info;
                        } else {
                            infoMsg = "获取验证码失败...";
                        }
                    } else {
                        infoMsg = "获取验证码失败...";
                    }

                    int flag = mInfo.getFlag();
                    if (flag == BaseResponseInfo.VALIDATE_LIMIT) {
                        if (mValidateView == null) {
                            mValidateView = new GetValidateView(VerifyPhoneActivity.this,
                                    CPControl.VALIDATE_TYPE_AUTHORMAIN, phoneNum);
                        }
                        mValidateView.showMenu();
                    } else {
                        UUToast.showUUToast(VerifyPhoneActivity.this, infoMsg);
                    }
                    break;
                case 2:
                    if (mDialog != null) {
                        mDialog.dismiss();
                    }
                    DialogWithTitleClick click = new DialogWithTitleClick() {

                        @Override
                        public void onRightClick() {
                            // TODO Auto-generated method stub

                        }

                        @Override
                        public void onLeftClick() {
                            ActivityControl.onLogout(VerifyPhoneActivity.this);
                            finish();
                        }
                    };
                    StringBuffer mBuffer = new StringBuffer();
                    mBuffer.append("手机");
                    mBuffer.append("\"");
                    mBuffer.append(mobile_name);
                    mBuffer.append("\"");
                    mBuffer.append("成为主机");
                    PopBoxCreat.createDialogNotitle(VerifyPhoneActivity.this, "主机更换成功",
                            mBuffer.toString(), "确认", "", click, true);
                    break;

                case 3:
                    if (mDialog != null) {
                        mDialog.dismiss();
                    }
                    mInfo = (BaseResponseInfo)msg.obj;
                    if (mInfo != null) {
                        String info = mInfo.getInfo();
                        if (info != null && info.length() > 0) {
                            infoMsg = info;
                        } else {
                            infoMsg = "主机更换失败...";
                        }
                    } else {
                        infoMsg = "主机更换失败...";
                    }

                    UUToast.showUUToast(VerifyPhoneActivity.this, infoMsg);
                    break;
                case 100:
                    count--;
                    if (count > 0) {
                        mTxtCode.setText(count + "秒后重发");
                    } else {
                        if (timer != null) {
                            if (task != null) {
                                task.cancel();
                            }
                        }
                        mTxtCode.setClickable(true);
                        mTxtCode.setText(R.string.usercenter_push_validate1);
                        mTxtCode.setBackgroundResource(R.drawable.btn_code_bg);
                    }
                    break;
            }
        }
    };
}

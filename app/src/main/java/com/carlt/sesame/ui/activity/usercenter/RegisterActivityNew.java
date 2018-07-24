
package com.carlt.sesame.ui.activity.usercenter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.carlt.sesame.R;
import com.carlt.sesame.control.CPControl;
import com.carlt.sesame.control.CPControl.GetResultListCallback;
import com.carlt.sesame.control.LoginControl;
import com.carlt.sesame.data.BaseResponseInfo;
import com.carlt.sesame.data.LoginInfo;
import com.carlt.sesame.data.RegisteInfo;
import com.carlt.sesame.data.UseInfo;
import com.carlt.sesame.preference.UseInfoLocal;
import com.carlt.sesame.ui.activity.base.BaseActivity;
import com.carlt.sesame.ui.activity.setting.WebActivity;
import com.carlt.sesame.ui.view.GetValidateView;
import com.carlt.sesame.ui.view.MenuImageShow;
import com.carlt.sesame.ui.view.PopBoxCreat;
import com.carlt.sesame.ui.view.ValidateEditText;
import com.carlt.sesame.utility.CheckInfo;
import com.carlt.sesame.utility.UUToast;

import java.util.Timer;
import java.util.TimerTask;

public class RegisterActivityNew extends BaseActivity {

    private ValidateEditText mEditPhone;// 输入手机号

    private ValidateEditText mEditPhoneCode;// 输入验证码

    private TextView mSendMsg;// 发送验证码

    private View mViewSendMsg;// 发送验证码的外部layout

    private ValidateEditText mEditPassword;// 输入密码
    private ValidateEditText mEditPasswordSure;// 密码确认

    private EditText mEditInvitation;// 输入邀请码

    private ImageView mImgInvitation;// 输入邀请码提示按钮

    private View mViewInvitation;// 输入邀请码外层layout

    private CheckBox mCheckBox;// 免责申明的选择框

    private TextView mTxtDeclaration;// 免责申明

    private GetValidateView mValidateView;// 获取语音验证码view

    private TextView mBtn;// 提交按钮

    private RegisteInfo mRegisteInfo = new RegisteInfo();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_new);

        initTitle();
        init();
    }

    private void initTitle() {
        ImageView back = (ImageView)findViewById(R.id.head_back_img1);
        TextView title = (TextView)findViewById(R.id.head_back_txt1);
        TextView txtRight = (TextView)findViewById(R.id.head_back_txt2);

        back.setImageResource(R.drawable.arrow_back);
        title.setText("注册");
        txtRight.setVisibility(View.GONE);

        back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void init() {
        mEditPhone = (ValidateEditText)findViewById(R.id.register_edit1);
        mEditPhoneCode = (ValidateEditText)findViewById(R.id.register_edit2);
        mEditPassword = (ValidateEditText)findViewById(R.id.register_edit3);
        mEditPasswordSure = (ValidateEditText)findViewById(R.id.register_edit4);
        
        mEditPhone.setEditHint("请填写手机号码");
        mEditPhone.setmLength(11);
        mEditPhone.setmType(ValidateEditText.TYPE_PON);        
        mEditPhone.setNextEditText(mEditPhoneCode);
        
        
        mEditPhoneCode.setEditHint("请输入验证码");
        mEditPhoneCode.setmType(ValidateEditText.TYPE_NUM);
        mEditPhoneCode.setNextEditText(mEditPassword);
        
        
        mEditPassword.setEditHint("请设置您的密码");
        mEditPassword.setmType(ValidateEditText.TYPE_PDT);
        mEditPassword.setNextEditText(mEditPasswordSure);
        
        
        mEditPasswordSure.setEditHint("请再次输入密码");
        mEditPasswordSure.setmType(ValidateEditText.TYPE_COFT);
        mEditPasswordSure.setmConfirmEdit(mEditPassword.getmEditText());
        
        mEditInvitation = (EditText)findViewById(R.id.register_edit5);

        mSendMsg = (TextView)findViewById(R.id.register_send);

        mImgInvitation = (ImageView)findViewById(R.id.register_img);

        mViewSendMsg = findViewById(R.id.register_relative1);
        mViewInvitation = findViewById(R.id.register_relative2);

        mEditPhoneCode.setOnFocusChangeListener(mChangeListener);
        mEditInvitation.setOnFocusChangeListener(mChangeListener);

        mCheckBox = (CheckBox)findViewById(R.id.register_check);
        mTxtDeclaration = (TextView)findViewById(R.id.register_txt_declaration);

        mBtn = (TextView)findViewById(R.id.register_btn);

        mSendMsg.setOnClickListener(mClickListener);
        mImgInvitation.setOnClickListener(mClickListener);
        mTxtDeclaration.setOnClickListener(mClickListener);
        mBtn.setOnClickListener(mClickListener);

        mCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mBtn.setBackgroundResource(R.drawable.bottom_btn_blue);
                    mBtn.setClickable(true);
                } else {
                    mBtn.setBackgroundResource(R.drawable.bottom_btn_gray);
                    mBtn.setClickable(false);
                }

            }
        });
    }

    private OnFocusChangeListener mChangeListener = new OnFocusChangeListener() {

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            switch (v.getId()) {

                case R.id.register_edit2:
                    // 验证码
                    if (hasFocus) {
                        mViewSendMsg.setBackgroundResource(R.drawable.edittext_bg_focused);
                    } else {
                        mViewSendMsg.setBackgroundResource(R.drawable.edittext_bg_focused_no);
                    }
                    break;

                case R.id.register_edit4:
                    // 邀请码
                    if (hasFocus) {
                        mViewInvitation.setBackgroundResource(R.drawable.edittext_bg_focused);
                    } else {
                        mViewInvitation.setBackgroundResource(R.drawable.edittext_bg_focused_no);
                    }
                    break;
            }

        }
    };

    /**
     * 倒计时
     */
    private int count = 60;

    private Timer timer = new Timer();

    private TimerTask task;

    // 获取短信验证码
    GetResultListCallback listener_msg = new GetResultListCallback() {

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

    // 注册接口回调
    GetResultListCallback listener_register = new GetResultListCallback() {

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

    private String phoneNum;

    private final static String URL_PROVISION = "http://m.cheler.com/domy.html";// 服务条款URL

    private OnClickListener mClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.register_send:
                    // 发送验证码按钮
                	mEditPhone.validateEdit();
                    phoneNum = mEditPhone.getText().toString();
                    if (phoneNum != null && phoneNum.length() == 11) {
                        CPControl.GetMessageValidateResult(CPControl.VALIDATE_TYPE_REGISTER,
                                mEditPhone.getText().toString(), listener_msg);
                        count = 60;
                        mSendMsg.setText(count + "秒后重发");
                        mSendMsg.setClickable(false);
                        mSendMsg.setBackgroundResource(R.drawable.btn_code_gray);

                        task = new TimerTask() {

                            @Override
                            public void run() {
                                Message msg = new Message();
                                msg.what = 10;
                                mHandler.sendMessage(msg);

                            }
                        };
                        timer.schedule(task, 1000, 1000);
                    } else {
                        UUToast.showUUToast(RegisterActivityNew.this, "请输入正确的手机号");
                    }
                    break;
                case R.id.register_img:
                    // 填写邀请码提示图标
                    MenuImageShow mMenuImageShow = new MenuImageShow(RegisterActivityNew.this);
                    mMenuImageShow.showMenu(R.drawable.input_tips_bg, mImgInvitation,
                            "有邀请码的用户购买盒子可以享受一定的折扣，邀请码可以向已购买盒子的好友索取");
                    break;
                case R.id.register_txt_declaration:
                    // 跳转至服务条款页面
                    Intent mIntentWeb = new Intent(RegisterActivityNew.this, WebActivity.class);
                    mIntentWeb.putExtra(WebActivity.URL_INFO, URL_PROVISION);
                    startActivity(mIntentWeb);
                    break;

                case R.id.register_btn:
                	
                    mEditPhone.validateEdit();
                    mEditPhoneCode.validateEdit();
                    mEditPassword.validateEdit();
                    mEditPasswordSure.validateEdit();
                	LoginInfo.Destroy();
                    // 调用注册接口
                    String mobile = mEditPhone.getText().toString();
                    if (mobile != null && mobile.length() == 11) {
                        // 手机号
                        mRegisteInfo.setMobile(mobile);
                    } else {
                        UUToast.showUUToast(RegisterActivityNew.this, "手机号不正确");
                        return;
                    }
                    String validate = mEditPhoneCode.getText().toString();
                    if (validate != null && validate.length() > 0) {
                        // 验证码
                        mRegisteInfo.setValidate(validate);
                    } else {
                        UUToast.showUUToast(RegisterActivityNew.this, "验证码不能为空");
                        return;
                    }

                    String passWord = mEditPassword.getText().toString();
                    String passWordSure=mEditPasswordSure.getText().toString();
                    String checkPsw=CheckInfo.checkPassword(passWord);
                    // if (checkPsw.equals(CheckInfo.CORRECT_PSWLENTH)) {
                    // mRegisteInfo.setPassWord(passWord);
                    // } else {
                    // UUToast.showUUToast(RegisterActivityNew.this,
                    // checkPsw);
                    // return;
                    // }
                    
                    if(!checkPsw.equals(CheckInfo.CORRECT_PSWLENTH)){
                        // UUToast.showUUToast(RegisterActivityNew.this,
                        // getResources().getString(R.string.error_password));
                        UUToast.showUUToast(RegisterActivityNew.this,
                               checkPsw);
                        return;
                    }else if(passWordSure==null||passWordSure.equals("")){
                        UUToast.showUUToast(RegisterActivityNew.this,
                                "请再次输入您的密码");
                        return;
                    }else if(!passWordSure.equals(passWord)){
                        UUToast.showUUToast(RegisterActivityNew.this,
                                "您两次输入的密码不一致,请重新输入...");
                        return;
                    }else{
                        mRegisteInfo.setPassWord(passWord);
                    }

                    String invite = mEditInvitation.getText().toString();
                    if (invite != null) {
                        mRegisteInfo.setInvite(invite);
                    }

                    mDialog = PopBoxCreat
                            .createDialogWithProgress(RegisterActivityNew.this, "正在加载");
                    mDialog.show();
                    CPControl.GetRegisteResult(mRegisteInfo, listener_register);
                    break;
            }

        }
    };

    private Dialog mDialog;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            BaseResponseInfo mBaseResponseInfo = null;
            switch (msg.what) {
                case 0:
                    UUToast.showUUToast(RegisterActivityNew.this, msg.obj.toString());
                    break;

                case 1:
                    // 停止计时
                    if (timer != null) {
                        if (task != null) {
                            task.cancel();
                        }
                    }
                    mSendMsg.setClickable(true);
                    mSendMsg.setText(R.string.usercenter_push_validate1);
                    mSendMsg.setBackgroundResource(R.drawable.btn_code_bg);

                    mBaseResponseInfo = (BaseResponseInfo)msg.obj;
                    int flag = mBaseResponseInfo.getFlag();
                    if (flag == BaseResponseInfo.VALIDATE_LIMIT) {
                        if (mValidateView == null) {
                            mValidateView = new GetValidateView(RegisterActivityNew.this,
                                    CPControl.VALIDATE_TYPE_REGISTER, phoneNum);
                        }
                        mValidateView.setClickStatus(true);
                        mValidateView.showMenu();
                    } else {
                        UUToast.showUUToast(RegisterActivityNew.this, "验证码获取失败:"
                                + mBaseResponseInfo.getInfo());
                    }
                    break;
                case 2:
                    if (mDialog != null && mDialog.isShowing()) {
                        mDialog.dismiss();
                    }
                    UseInfo mUseInfo = UseInfoLocal.getUseInfo();
                    mUseInfo.setAccount(mRegisteInfo.getMobile());
                    mUseInfo.setPassword(mRegisteInfo.getPassWord());
                    UseInfoLocal.setUseInfo(mUseInfo);

                    UUToast.showUUToast(RegisterActivityNew.this, "注册成功！");
                    LoginInfo.setVpin(mRegisteInfo.getMobile(), "");
                    LoginControl.logic(RegisterActivityNew.this);
                    finish();
                    break;
                case 3:
                    if (mDialog != null && mDialog.isShowing()) {
                        mDialog.dismiss();
                    }
                    mBaseResponseInfo = (BaseResponseInfo)msg.obj;
                    UUToast.showUUToast(RegisterActivityNew.this, "不好意思，注册失败，请稍候再试:"
                            + mBaseResponseInfo.getInfo());
                    break;

                case 10:
                    count--;
                    if (count > 0) {
                        mSendMsg.setText(count + "秒后重发");
                    } else {
                        if (timer != null) {
                            if (task != null) {
                                task.cancel();
                            }
                        }
                        mSendMsg.setClickable(true);
                        mSendMsg.setText(R.string.usercenter_push_validate1);
                        mSendMsg.setBackgroundResource(R.drawable.btn_code_bg);
                    }
                    break;
            }
        }
    };

}

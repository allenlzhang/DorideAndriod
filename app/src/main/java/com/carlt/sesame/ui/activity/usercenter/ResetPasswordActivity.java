
package com.carlt.sesame.ui.activity.usercenter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.carlt.doride.R;
import com.carlt.doride.ui.activity.login.UserLoginActivity;
import com.carlt.sesame.control.CPControl;
import com.carlt.sesame.control.CPControl.GetResultListCallback;
import com.carlt.sesame.data.BaseResponseInfo;
import com.carlt.sesame.data.SesameLoginInfo;
import com.carlt.sesame.ui.activity.base.BaseActivity;
import com.carlt.sesame.ui.view.GetValidateView;
import com.carlt.sesame.ui.view.PopBoxCreat;
import com.carlt.sesame.ui.view.ValidateEditText;
import com.carlt.sesame.utility.CheckInfo;
import com.carlt.sesame.utility.UUToast;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 重设密码页面
 * @author daisy
 */
public class ResetPasswordActivity extends BaseActivity implements OnClickListener {
    private ImageView back;// 头部返回键

    private TextView title;// 标题文字

    private TextView txtRight;// 头部右侧文字

    private ValidateEditText mEditText1;// 手机号

    private ValidateEditText mEditText2;// 验证码

    private ValidateEditText mEditText3;// 新密码

    private ValidateEditText mEditText4;// 新密码确认

    private TextView mTextView1;// 发送验证码

    private TextView mTextView2;// 重设密码

    private Intent mIntent;

    private Dialog mDialog;// 加载

    private GetValidateView mValidateView;// 播报语音验证码页面

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usercenter_reset_password);
        initTitle();
        init();
    }

    private void initTitle() {
        back = (ImageView) findViewById(R.id.head_back_img1);
        title = (TextView) findViewById(R.id.head_back_txt1);
        txtRight = (TextView) findViewById(R.id.head_back_txt2);

        back.setImageResource(R.drawable.arrow_back);
        title.setText("重设登录密码");
        txtRight.setVisibility(View.GONE);

        back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void init() {
        mEditText1 = (ValidateEditText) findViewById(R.id.activity_usercenter_reset_password_edit1);
        mEditText2 = (ValidateEditText) findViewById(R.id.activity_usercenter_reset_password_edit2);
        mEditText3 = (ValidateEditText) findViewById(R.id.activity_usercenter_reset_password_edit3);
        mEditText4 = (ValidateEditText) findViewById(R.id.activity_usercenter_reset_password_edit4);

        mEditText1.setEditHint("请填写手机号");
        mEditText1.setmLength(11);
        mEditText1.setmType(ValidateEditText.TYPE_NUM);
        mEditText1.setNextEditText(mEditText2);


        mEditText2.setEditHint("请输入验证码");
        mEditText2.setmType(ValidateEditText.TYPE_NUM);
        mEditText2.setNextEditText(mEditText3);


        mEditText3.setEditHint("请输入新密码");
        mEditText3.setmType(ValidateEditText.TYPE_PDT);
        mEditText3.setNextEditText(mEditText4);


        mEditText4.setEditHint("再次输入密码");
        mEditText4.setmType(ValidateEditText.TYPE_COFT);
        mEditText4.setmConfirmEdit(mEditText3.getmEditText());

        mTextView1 = (TextView) findViewById(R.id.activity_usercenter_reset_password_txt1);
        mTextView2 = (TextView) findViewById(R.id.activity_usercenter_reset_password_txt2);

        mTextView1.setOnClickListener(this);
        mTextView2.setOnClickListener(this);
    }

    // 获取短信验证码的回调
    GetResultListCallback listener1 = new GetResultListCallback() {

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

    // 修改密码回调
    GetResultListCallback listener3 = new GetResultListCallback() {

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

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            BaseResponseInfo mBaseResponseInfo = null;
            switch (msg.what) {
                case 0:
                    UUToast.showUUToast(ResetPasswordActivity.this, msg.obj.toString());
                    mTextView2.setClickable(true);
                    break;
                case 1:
                    // 停止计时
                    if (timer != null) {
                        if (task != null) {
                            task.cancel();
                        }
                    }
                    mTextView1.setClickable(true);
                    mTextView1.setText(R.string.usercenter_push_validate1);
                    mTextView1.setBackgroundResource(R.drawable.btn_code_bg);

                    mBaseResponseInfo = (BaseResponseInfo) msg.obj;
                    int flag = mBaseResponseInfo.getFlag();
                    if (flag == BaseResponseInfo.VALIDATE_LIMIT) {
                        if (mValidateView == null) {
                            mValidateView = new GetValidateView(ResetPasswordActivity.this,
                                    CPControl.VALIDATE_TYPE_FINDPASSWORD, phoneNum);
                        }
                        mValidateView.showMenu();
                    } else {
                        UUToast.showUUToast(ResetPasswordActivity.this, "获取验证码失败："
                                + mBaseResponseInfo.getInfo());
                    }
                    break;

                case 2:
                    if (mDialog != null && mDialog.isShowing()) {
                        mDialog.dismiss();
                    }
                    UUToast.showUUToast(ResetPasswordActivity.this, "新密码已设置成功!");
                    mIntent = new Intent(ResetPasswordActivity.this, UserLoginActivity.class);
                    ResetPasswordActivity.this.startActivity(mIntent);
                    break;
                case 3:
                    if (mDialog != null && mDialog.isShowing()) {
                        mDialog.dismiss();
                    }
                    mBaseResponseInfo = (BaseResponseInfo) msg.obj;
                    if (mBaseResponseInfo != null) {
                        String info = mBaseResponseInfo.getInfo();
                        if (info != null && info.length() > 0) {
                            UUToast.showUUToast(ResetPasswordActivity.this, info);
                        } else {
                            UUToast.showUUToast(ResetPasswordActivity.this, "新密码设置失败...");
                        }
                    } else {
                        UUToast.showUUToast(ResetPasswordActivity.this, "新密码设置失败...");
                    }

                    resetTextView();
                    break;
                case 10:
                    count--;
                    if (count > 0) {
                        mTextView1.setText(count + "秒后重发");
                    } else {
                        if (timer != null) {
                            if (task != null) {
                                task.cancel();
                            }
                        }
                        mTextView1.setClickable(true);
                        mTextView1.setText(R.string.usercenter_push_validate1);
                        mTextView1.setBackgroundResource(R.drawable.btn_code_bg);
                    }
                    break;
                default:
                    break;
            }
        }

    };

    /**
     * 重置发送验证码按钮
     */
    private void resetTextView() {
        mTextView1.setClickable(true);
        mTextView1.setText(R.string.usercenter_push_validate1);
    }

    /**
     * 倒计时
     */
    private int count = 60;

    private Timer timer = new Timer();

    private TimerTask task;

    private String phoneNum = null;// 手机号

    private String validate = null;// 验证码

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.activity_usercenter_reset_password_txt1:
                // 发送验证码
                mEditText1.validateEdit();
                phoneNum = mEditText1.getText().toString();
                String mobile = SesameLoginInfo.getMobile();
                LogUtils.e("====" + mobile);
                if (phoneNum.equals(mobile)) {
                    CPControl.GetMessageValidateResult(CPControl.VALIDATE_TYPE_FINDPASSWORD,
                            phoneNum, listener1);
                    count = 60;
                    mTextView1.setText(count + "秒后重发");
                    mTextView1.setClickable(false);
                    mTextView1.setBackgroundResource(R.drawable.btn_code_gray);

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
                    UUToast.showUUToast(ResetPasswordActivity.this, "请输入正确的手机号");
                }
                break;
            case R.id.activity_usercenter_reset_password_txt2:
                mEditText1.validateEdit();
                mEditText2.validateEdit();
                mEditText3.validateEdit();
                mEditText4.validateEdit();

                // 重设密码
                phoneNum = mEditText1.getText().toString();
                validate = mEditText2.getText().toString();
                String newPassword = mEditText3.getText().toString();
                String newPassword2 = mEditText4.getText().toString();

                String checkPswNew = CheckInfo.checkPassword(newPassword);
                String checkPswNew2 = CheckInfo.checkPassword(newPassword2);

                if (phoneNum == null || phoneNum.length() == 0) {
                    UUToast.showUUToast(ResetPasswordActivity.this, "您的手机号不正确哦！");
                    return;
                }
                if (validate == null || validate.length() == 0) {
                    UUToast.showUUToast(ResetPasswordActivity.this, "您的验证码不正确哦！");
                    return;
                }
                if (!checkPswNew.equals(CheckInfo.CORRECT_PSWLENTH)) {
                    UUToast.showUUToast(ResetPasswordActivity.this, "您的新" + checkPswNew);
                    return;
                }
                if (!checkPswNew2.equals(CheckInfo.CORRECT_PSWLENTH)) {
                    UUToast.showUUToast(ResetPasswordActivity.this, "您再次输入的新" + checkPswNew2);
                    return;
                }
                // if (newPassword==null||newPassword.length()<1) {
                // UUToast.showUUToast(ResetPasswordActivity.this,
                // "您还没有填写您的新密码...");
                // return;
                // }
                // if (newPassword2==null||newPassword2.length()<1) {
                // UUToast.showUUToast(ResetPasswordActivity.this,
                // "请再次输入您的新密码...");
                // return;
                // }
                if (!newPassword2.equals(newPassword)) {
                    UUToast.showUUToast(ResetPasswordActivity.this, "您两次输入的密码不一致，请重新输入...");
                    return;
                }
                if (validate == null || validate.length() < 1) {
                    UUToast.showUUToast(ResetPasswordActivity.this, "您还没有输入验证码...");
                    return;
                }
                CPControl.GetRetrievePasswordResult(phoneNum, newPassword, validate, listener3);
                if (mDialog == null) {
                    mDialog = PopBoxCreat.createDialogWithProgress(this, "正在验证信息");
                }
                mDialog.show();
                break;

            default:
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);

    }

    @Override
    protected void onDestroy() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (task != null) {
            task.cancel();
            task = null;
        }
        super.onDestroy();
    }
}

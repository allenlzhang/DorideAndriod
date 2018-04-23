package com.carlt.doride.ui.activity.setting;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.carlt.doride.R;
import com.carlt.doride.base.LoadingActivity;
import com.carlt.doride.data.BaseResponseInfo;
import com.carlt.doride.data.UseInfo;
import com.carlt.doride.model.LoginInfo;
import com.carlt.doride.preference.UseInfoLocal;
import com.carlt.doride.protocolparser.BaseParser;
import com.carlt.doride.protocolparser.DefaultStringParser;
import com.carlt.doride.systemconfig.URLConfig;
import com.carlt.doride.ui.activity.login.UserLoginActivity;
import com.carlt.doride.ui.view.UUToast;
import com.carlt.doride.utils.StringUtils;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class ResetCetifiedPhoneActivity extends LoadingActivity implements View.OnClickListener {

    private EditText reset_phone_input;//新手机号

    private TextView reset_phone_commit;//修改提交

    private EditText reset_code_input;//新手机验证码输入框

    private TextView reset_verification_send;//新手机验证码发送按钮

    public final static String CODE_INFO = "code_info";

    private String phoneNum;

    private Dialog mDialog;
    /*
    * 倒计时
	*/
    private int count = 60;

    private Timer timer = new Timer();

    private TimerTask task;

    private String vCode;
    private String code;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_cetified_phone);
        intent = getIntent();
        initTitle("修改手机号码");
        initComponent();
    }

    private void initComponent() {
        reset_verification_send = findViewById(R.id.reset_verification_send);
        reset_verification_send.setOnClickListener(this);
        reset_phone_commit = findViewById(R.id.reset_phone_commit);
        reset_phone_commit.setOnClickListener(this);
        reset_phone_input = findViewById(R.id.reset_phone_input);
        reset_code_input = findViewById(R.id.reset_code_input);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.reset_verification_send:
                // 获取验证码
                phoneNum = reset_phone_input.getText().toString();
                if (phoneNum != null && phoneNum.length() == 11) {
                    String phoneOld = LoginInfo.getMobile();
                    if (phoneNum.equals(phoneOld)) {
                        UUToast.showUUToast(ResetCetifiedPhoneActivity.this, "您输入的是旧手机号哦！");
                        return;
                    }
                    resetVCodeSendRequset(phoneNum);
                    count = 60;
                    reset_verification_send.setText(count + "秒后重发");
                    reset_verification_send.setClickable(false);
                    reset_verification_send.setBackgroundResource(R.drawable.verification_send_bg);

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
                    UUToast.showUUToast(ResetCetifiedPhoneActivity.this, "请输入正确的手机号");
                }
                break;
            case R.id.reset_phone_commit:
                if (intent!=null) {
                    code = intent.getStringExtra(CODE_INFO);
                }
                phoneNum = reset_phone_input.getText().toString();
                vCode = reset_code_input.getText().toString();
                if (isCommitInvalid(phoneNum, vCode, code)) {
                    authenticationPhone(vCode, phoneNum);
                }
                break;
        }
    }

    private void resetVCodeSendRequset(String mobile) {
        DefaultStringParser parser = new DefaultStringParser(vCodeCallback);
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("mobile", mobile);
        params.put("type", "4");
        params.put("voiceVerify", "0");
        parser.executePost(URLConfig.getM_AUTH_SET_VALIDATE(), params);
    }

    private BaseParser.ResultCallback vCodeCallback = new BaseParser.ResultCallback() {
        @Override
        public void onSuccess(BaseResponseInfo bInfo) {
            // 获取验证码成功
            UUToast.showUUToast(ResetCetifiedPhoneActivity.this, "验证码已发送成功！");
        }

        @Override
        public void onError(BaseResponseInfo bInfo) {
            // 获取验证码失败
            // 停止计时
            if (timer != null) {
                if (task != null) {
                    task.cancel();
                }
            }
            reset_verification_send.setClickable(true);
            reset_verification_send.setText("重发验证码");
            reset_verification_send.setBackgroundResource(R.drawable.verification_send_bg);
            String info = bInfo.getInfo();
            UUToast.showUUToast(ResetCetifiedPhoneActivity.this, "验证码获取失败:" + info);
        }
    };

    /**
     * 提交修改接口请求
     */
    private void authenticationPhone(String vCode, String mobile) {
        DefaultStringParser parser = new DefaultStringParser(commitCallback);
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("oldValidate", code);
        params.put("newMobile", mobile);
        params.put("newValidate", vCode);
        parser.executePost(URLConfig.getM_EDIT_MOBILE(), params);
    }

    private BaseParser.ResultCallback commitCallback = new BaseParser.ResultCallback() {
        @Override
        public void onSuccess(BaseResponseInfo bInfo) {
            // 验证当前手机成功
            if (mDialog != null && mDialog.isShowing()) {
                mDialog.dismiss();
            }
            UUToast.showUUToast(ResetCetifiedPhoneActivity.this, "手机号修改成功");
            LoginInfo.setMobile(reset_phone_input.getText().toString());
            UseInfo mUseInfo = UseInfoLocal.getUseInfo();
            mUseInfo.setAccount(reset_phone_input.getText().toString());
            UseInfoLocal.setUseInfo(mUseInfo);
            Intent  loginIntent=new Intent(ResetCetifiedPhoneActivity.this, UserLoginActivity.class);
            startActivity(loginIntent);
            finish();
        }

        @Override
        public void onError(BaseResponseInfo bInfo) {
            // 验证当前手机失败
            if (mDialog != null && mDialog.isShowing()) {
                mDialog.dismiss();
            }
            String info = bInfo.getInfo().toString();
            if (info != null && info.length() > 0) {
                UUToast.showUUToast(ResetCetifiedPhoneActivity.this, "手机号修改失败：" + info);
            } else {
                UUToast.showUUToast(ResetCetifiedPhoneActivity.this, "手机号修改失败...");
            }
        }
    };

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 10:
                    count--;
                    if (count > 0) {
                        reset_verification_send.setText(count + "秒后重发");
                    } else {
                        if (timer != null) {
                            if (task != null) {
                                task.cancel();
                            }
                        }
                        reset_verification_send.setClickable(true);
                        reset_verification_send.setText("重发验证码");
                        reset_verification_send.setBackgroundResource(R.drawable.verification_send_bg);
                    }
                    break;
            }
        }

    };

    private boolean isCommitInvalid(String phone, String vcode, String oldVCode) {
        if (TextUtils.isEmpty(phone) || !StringUtils.checkCellphone(phone)) {
            UUToast.showUUToast(this, getResources().getString(R.string.cell_phone_error));
            return false;
        } else if (TextUtils.isEmpty(vcode)) {
            UUToast.showUUToast(this, "验证码不能为空");
            return false;
        } else if (vcode.length() < 6) {
            UUToast.showUUToast(this, "验证码错误");
            return false;
        } else if (TextUtils.isEmpty(oldVCode)) {
            UUToast.showUUToast(this, "旧手机验证码不能为空");
            return false;
        } else if (oldVCode.length() < 6) {
            UUToast.showUUToast(this, "旧手机验证码错误");
            return false;
        } else {
            return true;
        }

    }
}

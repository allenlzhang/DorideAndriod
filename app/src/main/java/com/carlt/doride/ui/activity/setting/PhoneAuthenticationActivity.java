package com.carlt.doride.ui.activity.setting;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.carlt.doride.R;
import com.carlt.doride.base.LoadingActivity;
import com.carlt.doride.data.BaseResponseInfo;
import com.carlt.doride.model.LoginInfo;
import com.carlt.doride.protocolparser.BaseParser;
import com.carlt.doride.protocolparser.DefaultStringParser;
import com.carlt.doride.systemconfig.URLConfig;
import com.carlt.doride.ui.view.PopBoxCreat;
import com.carlt.doride.ui.view.UUToast;
import com.carlt.doride.utils.StringUtils;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class PhoneAuthenticationActivity extends LoadingActivity implements View.OnClickListener {

    private ImageView back;//返回按钮
    private TextView title;//标题

    private EditText certified_phone_input;//原手机号
    private EditText certified_code_input;//验证码

    private TextView certified_input_commit;//鉴权提交
    private TextView certified_verification_send;//验证码

    private Dialog mDialog;

    private String phoneNum;
    /*
    * 倒计时
	*/
    private int count = 60;

    private Timer timer = new Timer();

    private TimerTask task;
    private String validate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_authentication);
        initTitle("修改手机号码");
        initComponent();
    }

    private void initComponent() {
        certified_input_commit = findViewById(R.id.certified_input_commit);
        certified_input_commit.setOnClickListener(this);
        certified_verification_send = findViewById(R.id.certified_verification_send);
        certified_verification_send.setOnClickListener(this);
        certified_phone_input = findViewById(R.id.certified_phone_input);
        certified_code_input = findViewById(R.id.certified_code_input);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.certified_verification_send:
                phoneNum = certified_phone_input.getText().toString();
                if (phoneNum != null && StringUtils.checkCellphone(phoneNum)) {
                    String phoneOld = LoginInfo.getMobile();
                    if (!phoneNum.equals(phoneOld)) {
                        UUToast.showUUToast(PhoneAuthenticationActivity.this, "您输入的手机号不是您当前的手机号码，请重新输入...");
                        return;
                    }
                    getVCodeRequest(phoneNum);
                    count = 60;
                    certified_verification_send.setText(count + "秒后重发");
                    certified_verification_send.setClickable(false);
                    certified_verification_send.setBackgroundResource(R.drawable.verification_send_pressed_no);

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
                    UUToast.showUUToast(PhoneAuthenticationActivity.this, "请输入正确的手机号");
                }
                break;
            case R.id.certified_input_commit:
                phoneNum = certified_phone_input.getText().toString();
                // 修改手机号码接口
                 validate = certified_code_input.getText().toString();
                if (isCommitInvalid(phoneNum, validate)) {

                    if (mDialog == null) {
                        mDialog = PopBoxCreat.createDialogWithProgress(PhoneAuthenticationActivity.this, "提交中...");
                    }
                    mDialog.show();
                    authenticationPhone(validate,phoneNum);
                }
                break;
        }
    }

    /**
     * 获取验证码接口请求
     */
    private void getVCodeRequest(String mobile) {
        DefaultStringParser parser = new DefaultStringParser(vCodeCallback);
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("mobile", mobile);
        params.put("type", "6");
        params.put("voiceVerify", "0");
        parser.executePost(URLConfig.getM_AUTH_SET_VALIDATE(), params);
    }

    private BaseParser.ResultCallback vCodeCallback = new BaseParser.ResultCallback() {
        @Override
        public void onSuccess(BaseResponseInfo bInfo) {
            // 获取验证码成功
            UUToast.showUUToast(PhoneAuthenticationActivity.this, "验证码已发送成功！");
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
            certified_verification_send.setClickable(true);
            certified_verification_send.setText("重发验证码");
            certified_verification_send.setBackgroundResource(R.drawable.verification_send_pressed_no);
            int flag = bInfo.getFlag();
            UUToast.showUUToast(PhoneAuthenticationActivity.this, "验证码获取失败:" + bInfo.getInfo());
        }
    };

    /**
     * 提交修改接口请求
     */
    private void authenticationPhone(String vCode, String mobile) {
        DefaultStringParser parser = new DefaultStringParser(commitCallback);
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("oldValidate", vCode);
        params.put("oldMobile", mobile);
        parser.executePost(URLConfig.getM_AUTH_MOBILE(), params);
    }

    private BaseParser.ResultCallback commitCallback = new BaseParser.ResultCallback() {
        @Override
        public void onSuccess(BaseResponseInfo bInfo) {
            // 验证当前手机成功
            if (mDialog != null && mDialog.isShowing()) {
                mDialog.dismiss();
            }
            UUToast.showUUToast(PhoneAuthenticationActivity.this, "验证成功");
            Intent mIntent = new Intent(PhoneAuthenticationActivity.this, ResetCetifiedPhoneActivity.class);
            mIntent.putExtra(ResetCetifiedPhoneActivity.CODE_INFO, validate);
            startActivity(mIntent);
            finish();
        }

        @Override
        public void onError(BaseResponseInfo bInfo) {
            // 验证当前手机失败
            if (mDialog != null && mDialog.isShowing()) {
                mDialog.dismiss();
            }
            String info = bInfo.getInfo();
            if (info != null && info.length() > 0) {
                UUToast.showUUToast(PhoneAuthenticationActivity.this, "验证失败：" + info);
            } else {
                UUToast.showUUToast(PhoneAuthenticationActivity.this, "验证失败...");
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
                        certified_verification_send.setText(count + "秒后重发");
                    } else {
                        if (timer != null) {
                            if (task != null) {
                                task.cancel();
                            }
                        }
                        certified_verification_send.setClickable(true);
                        certified_verification_send.setText("重发验证码");
                        certified_verification_send.setBackgroundResource(R.drawable.verification_send_pressed_no);
                    }
                    break;
            }
        }

    };

    private boolean isCommitInvalid(String phone, String vcode) {
        if (TextUtils.isEmpty(phone) || !StringUtils.checkCellphone(phone)) {
            UUToast.showUUToast(this, getResources().getString(R.string.cell_phone_error));
            return false;
        } else if (TextUtils.isEmpty(vcode)) {
            UUToast.showUUToast(this, "验证码不能为空");
            return false;
        } else if (vcode.length() < 6) {
            UUToast.showUUToast(this, "验证码错误");
            return false;
        } else {
            return true;
        }

    }
}

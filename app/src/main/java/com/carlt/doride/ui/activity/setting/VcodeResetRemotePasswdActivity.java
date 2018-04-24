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
import com.carlt.doride.ui.view.PwdEditText;
import com.carlt.doride.ui.view.UUToast;
import com.carlt.doride.utils.CipherUtils;
import com.carlt.doride.utils.StringUtils;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class VcodeResetRemotePasswdActivity extends LoadingActivity implements View.OnClickListener {

    private EditText certified_phone_input;//手机输入框
    private EditText certified_code_input;//验证码输入框

    private PwdEditText new_remote_passwd;//输入远程密码
    private PwdEditText new_remote_passwd_again;//再次输入远程密码

    private TextView certified_verification_send;//验证码发送按钮
    private TextView reset_remote_commit;//确认按钮

    private String mobile;//手机号码
    private String vCode;//验证码
    private String passwd;//密码
    private String confirmPasswd;//再次输入的密码

    private Dialog mDialog;

    /*
    * 倒计时
	*/
    private int count = 60;

    private Timer timer = new Timer();

    private TimerTask task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vcode_reset_remote_passwd);
        initTitle("修改远程密码");
        initComponent();
    }

    private void initComponent() {
        certified_phone_input = $ViewByID(R.id.remote_phone_input);
        certified_code_input = $ViewByID(R.id.remote_code_input);
        new_remote_passwd = $ViewByID(R.id.new_remote_passwd);
        new_remote_passwd_again = $ViewByID(R.id.new_remote_vcode_passwd_again);

        certified_verification_send = $ViewByID(R.id.remote_verification_send);
        certified_verification_send.setOnClickListener(this);
        reset_remote_commit = $ViewByID(R.id.forget_reset_remote_commit);
        reset_remote_commit.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.remote_verification_send:
                mobile = certified_phone_input.getText().toString();
                if (mobile != null && StringUtils.checkCellphone(mobile)) {
                    String phoneOld = LoginInfo.getMobile();
                    if (!mobile.equals(phoneOld)) {
                        UUToast.showUUToast(VcodeResetRemotePasswdActivity.this, "您输入的手机号不是您当前的手机号码，请重新输入...");
                        return;
                    }
                    getVCodeRequest(mobile);
                    count = 60;
                    certified_verification_send.setText(count + "秒后重发");
                    certified_verification_send.setClickable(false);
                    certified_verification_send.setBackgroundResource(R.drawable.verification_sending_bg);

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
                    UUToast.showUUToast(VcodeResetRemotePasswdActivity.this, "请输入正确的手机号");
                }
                break;
            case R.id.forget_reset_remote_commit:
                mobile = certified_phone_input.getText().toString();
                passwd=new_remote_passwd.getText().toString();
                confirmPasswd=new_remote_passwd_again.getText().toString();
                vCode=certified_code_input.getText().toString();
                if (isCommitInvalid(mobile,vCode,passwd,confirmPasswd)) {
                    editPasswdCommitRequest();
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
        params.put("type", "7");
        params.put("voiceVerify", "0");
        parser.executePost(URLConfig.getM_AUTH_SET_VALIDATE(), params);
    }

    private BaseParser.ResultCallback vCodeCallback = new BaseParser.ResultCallback() {
        @Override
        public void onSuccess(BaseResponseInfo bInfo) {
            // 获取验证码成功
            UUToast.showUUToast(VcodeResetRemotePasswdActivity.this, "验证码已发送成功！");
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
            certified_verification_send.setBackgroundResource(R.drawable.verification_send_bg);
            if (!TextUtils.isEmpty(bInfo.getInfo())) {
                UUToast.showUUToast(VcodeResetRemotePasswdActivity.this, "验证码获取失败:" + bInfo.getInfo());
            } else {
                UUToast.showUUToast(VcodeResetRemotePasswdActivity.this, "验证码获取失败:");
            }

        }
    };

    private void editPasswdCommitRequest(){
        UseInfo mUseInfo = UseInfoLocal.getUseInfo();
        DefaultStringParser parser=new DefaultStringParser(commitCallback);
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("mobile", mobile);
        params.put("validate_code",vCode);
        params.put("remote_pwd", CipherUtils.md5(confirmPasswd));
        params.put("password", mUseInfo.getPassword());
        parser.executePost(URLConfig.getM_FORGET_REMOTE_PWD(),params);
    }

    private BaseParser.ResultCallback commitCallback = new BaseParser.ResultCallback() {
        @Override
        public void onSuccess(BaseResponseInfo bInfo) {
            // 获取验证码成功
            loadSuccessUI();
            UUToast.showUUToast(VcodeResetRemotePasswdActivity.this, "远程控制密码修改成功");
            Intent intent = new Intent(VcodeResetRemotePasswdActivity.this, AccountSecurityActivity.class);
            startActivity(intent);
            finish();
        }

        @Override
        public void onError(BaseResponseInfo bInfo) {
            loadSuccessUI();
            if (bInfo != null && !TextUtils.isEmpty(bInfo.getInfo())) {
                UUToast.showUUToast(VcodeResetRemotePasswdActivity.this, bInfo.getInfo());
            } else {
                UUToast.showUUToast(VcodeResetRemotePasswdActivity.this, "远程控制密码修改失败");
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
                        certified_verification_send.setBackgroundResource(R.drawable.verification_send_bg);
                    }
                    break;
            }
        }

    };


    /**
     * 判断原始密码、新密码、再次输入新密码是否合法
     * */
    private boolean isCommitInvalid(String phone, String vcode, String passwd, String passwdAgain) {
        if (TextUtils.isEmpty(phone) || !StringUtils.checkCellphone(phone)) {
            UUToast.showUUToast(this, getResources().getString(R.string.cell_phone_error));
            return false;
        } else if (TextUtils.isEmpty(vcode)) {
            UUToast.showUUToast(this, "验证码不能为空");
            return false;
        }else if(TextUtils.isEmpty(passwd)||passwd.length()<6||!StringUtils.isNumber(passwd)){
            UUToast.showUUToast(this, "密码至少为6位数字");
            return false;
        } else if (!passwd.equals(passwdAgain)) {
            UUToast.showUUToast(VcodeResetRemotePasswdActivity.this, "两次输入密码不一致，请重新输入");
            return false;
        } else {
            return true;
        }

    }
}

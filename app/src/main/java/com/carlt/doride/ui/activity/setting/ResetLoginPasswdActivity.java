package com.carlt.doride.ui.activity.setting;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.carlt.doride.R;
import com.carlt.doride.base.LoadingActivity;
import com.carlt.doride.control.ActivityControl;
import com.carlt.doride.data.BaseResponseInfo;
import com.carlt.doride.data.UseInfo;
import com.carlt.doride.model.LoginInfo;
import com.carlt.doride.preference.UseInfoLocal;
import com.carlt.doride.protocolparser.BaseParser.ResultCallback;
import com.carlt.doride.protocolparser.DefaultStringParser;
import com.carlt.doride.systemconfig.URLConfig;
import com.carlt.doride.ui.activity.login.UserLoginActivity;
import com.carlt.doride.ui.view.UUToast;

import java.util.HashMap;


public class ResetLoginPasswdActivity extends LoadingActivity implements View.OnClickListener {

    private ImageView new_passwd_input_toggle;
    private ImageView new_passwd_input_again_toggle;

    private EditText old_passwd_input;//原始密码输入框
    private EditText new_passwd_input;//新密码密码输入框
    private EditText new_passwd_again_input;//新密码再次输入框

    private TextView reset_passwd_commit;

    String passwd;
    String newPasswd;
    String confirmPasswd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_login_passwd);
        initTitle("修改登录密码");
        initComponent();
    }

    private void initComponent() {
        reset_passwd_commit = $ViewByID(R.id.reset_passwd_commit);
        reset_passwd_commit.setOnClickListener(this);

        old_passwd_input = $ViewByID(R.id.old_passwd_input);
        new_passwd_input = $ViewByID(R.id.new_passwd_input);
        new_passwd_again_input = $ViewByID(R.id.new_passwd_again_input);

        new_passwd_input_toggle = $ViewByID(R.id.new_passwd_input_toggle);
        new_passwd_input_toggle.setOnClickListener(this);
        new_passwd_input_again_toggle = $ViewByID(R.id.new_passwd_input_again_toggle);
        new_passwd_input_again_toggle.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.reset_passwd_commit:
                passwd = old_passwd_input.getText().toString();
                newPasswd = new_passwd_input.getText().toString();
                confirmPasswd = new_passwd_again_input.getText().toString();
                if (isCommitInvalid(passwd, newPasswd, confirmPasswd)) {
                    editPasswdRequest();
                }
                break;
            case R.id.new_passwd_input_toggle:
                ActivityControl.passwdToggle(this, new_passwd_input, new_passwd_input_toggle, view.getTag().toString());
                if (!TextUtils.isEmpty(new_passwd_input.getText().toString())) {
                    new_passwd_input.setSelection(new_passwd_input.getText().toString().length());
                }
                break;
            case R.id.new_passwd_input_again_toggle:
                ActivityControl.passwdToggle(this, new_passwd_again_input, new_passwd_input_again_toggle, view.getTag().toString());
                if (!TextUtils.isEmpty(new_passwd_again_input.getText().toString())) {
                    new_passwd_again_input.setSelection(new_passwd_again_input.getText().toString().length());
                }
                break;
        }
    }

    private void editPasswdRequest() {
        DefaultStringParser parser = new DefaultStringParser(editCallback);
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("dealerId", LoginInfo.getDealerId());
        params.put("oldpassword", passwd);
        params.put("newspassword", confirmPasswd);
        parser.executePost(URLConfig.getM_USERCENTER_EDIT_PWD(), params);
    }

    private ResultCallback editCallback = new ResultCallback() {
        @Override
        public void onSuccess(BaseResponseInfo bInfo) {
            UseInfo mUseInfo = UseInfoLocal.getUseInfo();
            mUseInfo.setPassword(new_passwd_again_input.getText().toString());
            UseInfoLocal.setUseInfo(mUseInfo);
            UUToast.showUUToast(ResetLoginPasswdActivity.this, "密码修改成功");
            Intent loginIntent = new Intent(ResetLoginPasswdActivity.this, UserLoginActivity.class);
            startActivity(loginIntent);
            finish();
        }

        @Override
        public void onError(BaseResponseInfo bInfo) {
            if (null != bInfo && !TextUtils.isEmpty(bInfo.getInfo())) {
                UUToast.showUUToast(ResetLoginPasswdActivity.this, bInfo.getInfo());
            } else {
                UUToast.showUUToast(ResetLoginPasswdActivity.this, bInfo.getInfo());
            }

        }
    };


        /**
         * 判断原始密码、新密码、再次输入新密码是否合法
         * */
        private boolean isCommitInvalid(String passwd, String newPasswd, String confirmPasswd) {
            if (TextUtils.isEmpty(passwd)) {
                UUToast.showUUToast(this, "原始密码不能为空",500);
                return false;
            } else if (TextUtils.isEmpty(newPasswd) || newPasswd.length() < 6) {
                UUToast.showUUToast(this, "新密码长度至少为6位",500);
                return false;
            } else if (TextUtils.isEmpty(confirmPasswd) || !newPasswd.equals(confirmPasswd)) {
                UUToast.showUUToast(this, "两次输入密码不一致",500);
                return false;
            } else {
                return true;
            }

        }

    }

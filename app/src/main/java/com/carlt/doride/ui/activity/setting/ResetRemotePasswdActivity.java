package com.carlt.doride.ui.activity.setting;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.carlt.doride.R;
import com.carlt.doride.base.LoadingActivity;
import com.carlt.doride.data.BaseResponseInfo;
import com.carlt.doride.protocolparser.BaseParser;
import com.carlt.doride.protocolparser.DefaultStringParser;
import com.carlt.doride.systemconfig.URLConfig;
import com.carlt.doride.ui.view.PwdEditText;
import com.carlt.doride.ui.view.UUToast;
import com.carlt.doride.utils.CipherUtils;
import com.carlt.doride.utils.StringUtils;

import java.util.HashMap;

public class ResetRemotePasswdActivity extends LoadingActivity implements View.OnClickListener {

    private PwdEditText old_remote_passwd;
    private PwdEditText new_remote_passwd;
    private PwdEditText new_remote_passwd_again;

    private TextView reset_remote_commit;

    private String passwd;
    private String newPasswd;
    private String confirmPasswd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_remote_passwd);
        initComponent();
        initTitle("修改远程密码");
    }


    private void initComponent() {
        old_remote_passwd = $ViewByID(R.id.reset_old_remote_passwd);
        new_remote_passwd = $ViewByID(R.id.reset_new_remote_passwd);
        new_remote_passwd_again = $ViewByID(R.id.reset_new_remote_passwd_again);

        reset_remote_commit = $ViewByID(R.id.reset_remote_commit);
        reset_remote_commit.setOnClickListener(this);

        setDismissFocus(old_remote_passwd);
        setDismissFocus(new_remote_passwd);
        setDismissFocus(new_remote_passwd_again);

    }

    @SuppressLint("ClickableViewAccessibility")
    private void setDismissFocus(final PwdEditText view) {
        view.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                // rel.setFocusable(true);
                // 如果xml文件里面没设置，就需要在这里设置
                // rel.setFocusableInTouchMode(true);
                view.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                return false;
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.reset_remote_commit:
                passwd = old_remote_passwd.getText().toString();
                newPasswd = new_remote_passwd.getText().toString();
                confirmPasswd = new_remote_passwd_again.getText().toString();
                if (isCommitInvalid(passwd, newPasswd, confirmPasswd)) {
                    editPasswdRequest();
                }
                break;
        }
    }

    private void editPasswdRequest() {
        DefaultStringParser parser = new DefaultStringParser(editCallback);
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("old_remote_pwd", CipherUtils.md5(passwd));
        params.put("new_remote_pwd", CipherUtils.md5(confirmPasswd));
        parser.executePost(URLConfig.getM_RESET_REMOTE_PWD(), params);
    }

    private BaseParser.ResultCallback editCallback = new BaseParser.ResultCallback() {
        @Override
        public void onSuccess(BaseResponseInfo bInfo) {
            UUToast.showUUToast(ResetRemotePasswdActivity.this, "远程控制密码修改成功");
            //            Intent intent = new Intent(ResetRemotePasswdActivity.this, AccountSecurityActivity.class);
            //            startActivity(intent);
            finish();
        }

        @Override
        public void onError(BaseResponseInfo bInfo) {
            if (TextUtils.isEmpty(bInfo.getInfo())) {
                UUToast.showUUToast(ResetRemotePasswdActivity.this, "远程控制密码修改失败");
            } else {
                UUToast.showUUToast(ResetRemotePasswdActivity.this, bInfo.getInfo());
            }
        }
    };

    /**
     * 判断原始密码、新密码、再次输入新密码是否合法
     */
    private boolean isCommitInvalid(String passwd, String newPasswd, String confirmPasswd) {
        if (TextUtils.isEmpty(passwd) || passwd.length() < 6) {
            UUToast.showUUToast(this, "原远程操作密码不正确");
            return false;
        } else if (TextUtils.isEmpty(newPasswd) || newPasswd.length() < 6 || !StringUtils.isNumber(newPasswd)) {
            UUToast.showUUToast(this, "新远程操作密码至少为6位数字");
            return false;
        } else if (!newPasswd.equals(confirmPasswd)) {
            UUToast.showUUToast(this, "两次输入密码不一致");
            return false;
        } else {
            return true;
        }

    }
}

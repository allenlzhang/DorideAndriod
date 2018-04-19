package com.carlt.doride.ui.activity.setting;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.carlt.doride.R;
import com.carlt.doride.base.BaseActivity;
import com.carlt.doride.data.BaseResponseInfo;
import com.carlt.doride.protocolparser.BaseParser;
import com.carlt.doride.protocolparser.DefaultStringParser;
import com.carlt.doride.systemconfig.URLConfig;
import com.carlt.doride.ui.view.PasswordView;
import com.carlt.doride.ui.view.UUToast;
import com.carlt.doride.utils.CipherUtils;
import com.carlt.doride.utils.StringUtils;

import java.util.HashMap;

public class ResetRemotePasswdActivity extends BaseActivity implements View.OnClickListener {

    private ImageView back;
    private TextView title;

    private PasswordView old_remote_passwd;
    private PasswordView new_remote_passwd;
    private PasswordView new_remote_passwd_again;

    private TextView reset_remote_commit;

    private String passwd;
    private String newPasswd;
    private String confirmPasswd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_remote_passwd);
        initComponent();
    }

    private void initComponent() {
        back = $ViewByID(R.id.back);
        back.setOnClickListener(this);

        title = $ViewByID(R.id.title);
        title.setText("修改远程密码");

        old_remote_passwd = $ViewByID(R.id.reset_old_remote_passwd);
        new_remote_passwd = $ViewByID(R.id.reset_new_remote_passwd);
        new_remote_passwd_again = $ViewByID(R.id.reset_new_remote_passwd_again);

        reset_remote_commit = $ViewByID(R.id.reset_remote_commit);
        reset_remote_commit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.reset_remote_commit:
                passwd = old_remote_passwd.getPassword();
                newPasswd = new_remote_passwd.getPassword();
                confirmPasswd = new_remote_passwd_again.getPassword();
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
            Intent intent = new Intent(ResetRemotePasswdActivity.this, AccountSecurityActivity.class);
            startActivity(intent);
        }

        @Override
        public void onError(BaseResponseInfo bInfo) {
            switch (bInfo.getFlag()) {
                case 1004:
                    UUToast.showUUToast(ResetRemotePasswdActivity.this, "原始密码错误");
                    break;
                case 1014:
                    UUToast.showUUToast(ResetRemotePasswdActivity.this, "已设置过该密码");
                    break;
                default:
                    UUToast.showUUToast(ResetRemotePasswdActivity.this, "密码修改失败");
                    break;
            }

        }
    };

    /**
     * 判断原始密码、新密码、再次输入新密码是否合法
     */
    private boolean isCommitInvalid(String passwd, String newPasswd, String confirmPasswd) {
        if (TextUtils.isEmpty(passwd)) {
            UUToast.showUUToast(this, "原始密码不能为空");
            return false;
        } else if (StringUtils.isNumber(passwd)) {
            UUToast.showUUToast(this, "密码必须为数字");
            return false;
        }else if (TextUtils.isEmpty(newPasswd) || newPasswd.length() < 6) {
            UUToast.showUUToast(this, "新密码长度至少为6位");
            return false;
        } else if (StringUtils.isNumber(newPasswd)) {
            UUToast.showUUToast(this, "密码必须为数字");
            return false;
        }else if (TextUtils.isEmpty(newPasswd) || !newPasswd.equals(confirmPasswd)) {
            UUToast.showUUToast(this, "两次输入密码不一致");
            return false;
        } else {
            return true;
        }

    }
}

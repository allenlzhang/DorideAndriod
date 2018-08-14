package com.carlt.sesame.ui.activity.setting;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.carlt.doride.R;
import com.carlt.doride.base.LoadingActivity;
import com.carlt.sesame.ui.activity.usercenter.ResetPasswordActivity;


public class SesameLoginPasswdManageActivity extends LoadingActivity implements View.OnClickListener {

    private View login_passwd_remember;//记得原密码
    private View login_passwd_forget;//忘记原密码

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sesame_login_passwd_manage);
        initTitle("登录密码管理");
        initComponent();
    }

    private void initComponent() {
        login_passwd_remember = $ViewByID(R.id.login_passwd_remember);
        login_passwd_remember.setOnClickListener(this);
        login_passwd_forget = $ViewByID(R.id.login_passwd_forget);
        login_passwd_forget.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_passwd_remember:
                //                Intent resetLoginPasswd=new Intent(this,ResetLoginPasswdActivity.class);
                //                startActivity(resetLoginPasswd);
                Intent mIntent1 = new Intent(SesameLoginPasswdManageActivity.this,
                        EditUserinfoDetailActivity.class);
                mIntent1.putExtra(EditUserinfoDetailActivity.EDIT_TYPE,
                        EditUserinfoDetailActivity.TYPE_PASSWORD);
                startActivity(mIntent1);
                break;
            case R.id.login_passwd_forget:
                Intent resetLoginPasswdByPhone = new Intent(this, ResetPasswordActivity.class);
                startActivity(resetLoginPasswdByPhone);
                break;
        }
    }
}

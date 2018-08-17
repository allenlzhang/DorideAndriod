package com.carlt.doride.ui.activity.setting;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.carlt.doride.R;
import com.carlt.doride.base.LoadingActivity;
import com.carlt.doride.model.LoginInfo;
import com.carlt.doride.ui.activity.remote.RemotePswResetActivity3;
import com.carlt.doride.ui.view.UUToast;

public class RemotePasswdManageActivity extends LoadingActivity implements View.OnClickListener {

    private View remote_passwd_remember;//记得原密码
    private View remote_passwd_forget;//忘记原密码
    private View remote_set_passwd;//设置远程密码

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remote_passwd_manage);
        initTitle("远程密码管理");
        initComponent();
    }

    private void initComponent() {
        remote_passwd_remember = $ViewByID(R.id.remote_passwd_remember);
        remote_passwd_remember.setOnClickListener(this);
        remote_passwd_forget = $ViewByID(R.id.remote_passwd_forget);
        remote_passwd_forget.setOnClickListener(this);
        remote_set_passwd = $ViewByID(R.id.remote_set_passwd);
        remote_set_passwd.setOnClickListener(this);


    }

    @Override
    protected void onResume() {
        super.onResume();
        if (LoginInfo.isSetRemotePwd()) {
            remote_set_passwd.setVisibility(View.GONE);
        } else {
            remote_set_passwd.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.remote_passwd_remember:
                if (LoginInfo.isSetRemotePwd()) {

                    Intent resetLoginPasswd = new Intent(this, ResetRemotePasswdActivity.class);
                    startActivity(resetLoginPasswd);
                } else {
                    UUToast.showUUToast(this, "请先设置远程密码");
                    //                    go2SettingPwdActivity();
                    return;
                }

                break;
            case R.id.remote_passwd_forget:
                if (LoginInfo.isSetRemotePwd()) {
                    Intent resetLoginPasswdByPhone = new Intent(this, VcodeResetRemotePasswdActivity.class);
                    startActivity(resetLoginPasswdByPhone);
                } else {
                    UUToast.showUUToast(this, "请先设置远程密码");
                    //                    go2SettingPwdActivity();
                    return;
                }

                break;
            case R.id.remote_set_passwd:
                go2SettingPwdActivity();
                break;
        }
        //        finish();
    }

    private void go2SettingPwdActivity() {
        Intent setRemotePasswd = new Intent(this, RemotePswResetActivity3.class);
        setRemotePasswd.putExtra(RemotePswResetActivity3.TYPE, RemotePswResetActivity3.TYPE_SAFETY);
        startActivity(setRemotePasswd);
    }
}

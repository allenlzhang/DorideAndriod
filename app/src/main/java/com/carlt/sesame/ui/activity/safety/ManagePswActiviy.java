
package com.carlt.sesame.ui.activity.safety;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.carlt.doride.R;
import com.carlt.doride.http.retrofitnet.model.UserInfo;
import com.carlt.sesame.data.SesameLoginInfo;
import com.carlt.sesame.ui.activity.base.BaseActivity;
import com.carlt.sesame.ui.activity.setting.SesameLoginPasswdManageActivity;

/**
 * 密码管理
 * 
 * @author Administrator
 */
public class ManagePswActiviy extends BaseActivity implements OnClickListener{

    private ImageView back;// 头部返回键

    private TextView title;// 标题文字

    private View mViewLogin;// 登录密码管理

    private View mViewRemote;// 远程控制密码管理
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_psw);
        initTitle();
        init();
    }
    
    private void initTitle() {
        back = (ImageView)findViewById(R.id.head_back_img1);
        title = (TextView)findViewById(R.id.head_back_txt1);

        title.setText("密码管理");

        back.setImageResource(R.drawable.arrow_back);
        back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void init() {
        mViewLogin = findViewById(R.id.psw_lay_login);
        mViewRemote = findViewById(R.id.psw_lay_remote);
        
        //1.2.0版本去掉
		// if("1".equals(LoginInfo.getRemoteControl())){
		// mViewRemote.setVisibility(View.VISIBLE);
		// findViewById(R.id.line1).setVisibility(View.VISIBLE);
		// }

        mViewLogin.setOnClickListener(this);
        mViewRemote.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.psw_lay_login:
                // 登录密码
//                Intent mIntent1 = new Intent(ManagePswActiviy.this,
//                        EditUserinfoDetailActivity.class);
//                mIntent1.putExtra(EditUserinfoDetailActivity.EDIT_TYPE,
//                        EditUserinfoDetailActivity.TYPE_PASSWORD);
                Intent mIntent1 = new Intent(ManagePswActiviy.this,
                        SesameLoginPasswdManageActivity.class);

                startActivity(mIntent1);
                break;

            case R.id.psw_lay_remote:
             // 远程密码管理
                Intent mIntent2 = null;
                if (UserInfo.getInstance().isAuthen.equals("1")) {
                    // 已经实名制认证
                    boolean isSetRemotepsw= UserInfo.getInstance().isSetRemotePwd == 1;
                    if (isSetRemotepsw) {
                        // 已设置过远程密码
                        mIntent2 = new Intent(ManagePswActiviy.this, RemotePswActivity.class);
                    } else {
                        // 未设置过远程密码
                        mIntent2 = new Intent(ManagePswActiviy.this,
                                RemotePswResetActivity3.class);
                        mIntent2.putExtra(RemotePswResetActivity3.TYPE,
                                RemotePswResetActivity3.TYPE_SAFETY);
                    }
                } else {
                    // 未经实名制认证
                    mIntent2 = new Intent(ManagePswActiviy.this, RealNameActivity.class);
                    mIntent2.putExtra(RealNameActivity.TYPE, RealNameActivity.TYPE_UNAUTHER);
                }
                // // 测试代码
                // mIntent4 = new Intent(SafetyMainActivity.this,
                // RemotePswActivity.class);
                // // 测试结束
                startActivity(mIntent2);
                break;
        }
        
    }


}

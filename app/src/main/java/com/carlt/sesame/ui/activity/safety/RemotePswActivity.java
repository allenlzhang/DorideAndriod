
package com.carlt.sesame.ui.activity.safety;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.carlt.sesame.R;
import com.carlt.sesame.ui.activity.base.BaseActivity;

/**
 * 远程控制密码管理
 * 
 * @author Administrator
 */
public class RemotePswActivity extends BaseActivity implements OnClickListener {
    private ImageView back;// 头部返回键

    private TextView title;// 标题文字

    private View mViewEdit;// 修改远程控制密码

    private View mViewForget;// 忘记远程控制密码

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remotepsw);

        initTitle();
        init();
    }

    private void initTitle() {
        back = (ImageView)findViewById(R.id.head_back_img1);
        title = (TextView)findViewById(R.id.head_back_txt1);

        title.setText("远程控制密码管理");

        back.setImageResource(R.drawable.arrow_back);
        back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void init() {
        mViewEdit = findViewById(R.id.remotepsw_lay_edit);
        mViewForget = findViewById(R.id.remotepsw_lay_forget);

        mViewEdit.setOnClickListener(this);
        mViewForget.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.remotepsw_lay_edit:
                // 修改
                Intent mIntent1 = new Intent(RemotePswActivity.this, RemotePswEditActivity.class);
                startActivity(mIntent1);
                break;

            case R.id.remotepsw_lay_forget:
                // 忘记
                Intent mIntent2 = new Intent(RemotePswActivity.this, RemotePswResetActivity1.class);
                startActivity(mIntent2);
                break;
        }
    }

}

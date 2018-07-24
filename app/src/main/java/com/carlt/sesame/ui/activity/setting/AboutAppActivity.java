
package com.carlt.sesame.ui.activity.setting;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.carlt.doride.DorideApplication;
import com.carlt.sesame.R;
import com.carlt.sesame.ui.activity.base.BaseActivity;

/**
 * 设置-关于芝麻
 * 
 * @author daisy
 */
public class AboutAppActivity extends BaseActivity {
    private ImageView back;// 头部返回键

    private TextView title;// 标题文字

    private TextView mTxtVersion;// 版本号

    private TextView mTxtProvision;// 服务条款

    private Dialog mDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutzotry);
        initTitle();
        init();
    }

    private void initTitle() {
        back = (ImageView)findViewById(R.id.head_back_img1);
        title = (TextView)findViewById(R.id.head_back_txt1);

        title.setText("关于芝麻乐园");

        back.setImageResource(R.drawable.arrow_back);
        back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void init() {
        mTxtVersion = (TextView)findViewById(R.id.about_app_txt_version);
        mTxtProvision = (TextView)findViewById(R.id.about_app_txt_provision);

        mTxtVersion.setText("APP版本V"+ DorideApplication.VersionName);
        //mTxtProvision.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);

        mTxtProvision.setOnClickListener(mClickListener);
    }

    private final static String URL_PROVISION = "http://m.cheler.com/domy.html";// 服务条款URL

    private OnClickListener mClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.about_app_txt_provision:
                    // 服务条款
                    Intent mIntent1 = new Intent(AboutAppActivity.this, WebActivity.class);
                    mIntent1.putExtra(WebActivity.URL_INFO, URL_PROVISION);
                    startActivity(mIntent1);
                    break;
            }

        }
    };
}

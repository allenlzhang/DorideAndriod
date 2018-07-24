
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
 * 验证方式选择页面
 * 
 * @author Daisy
 */
public class VerifyActivity extends BaseActivity implements OnClickListener {

    private ImageView back;// 头部返回键

    private TextView title;// 标题文字

    private View mViewIdCard;// 验证身份证

    private View mViewPhone;// 验证手机号
    
    private String mobile_name;// 手机名称

    private String mobile_id;// 手机唯一标识id

    public final static String MOBILE_NAME = "mobile_name";

    public final static String MOBILE_ID = "mobile_id";

    public final static String MOBILE_NUM = "mobile_num";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);
        try {
            mobile_name = getIntent().getStringExtra(VerifyActivity.MOBILE_NAME);
            mobile_id = getIntent().getStringExtra(VerifyActivity.MOBILE_ID);
            
        } catch (Exception e) {
            // TODO: handle exception
        }
        initTitle();
        init();
    }

    private void initTitle() {
        back = (ImageView)findViewById(R.id.head_back_img1);
        title = (TextView)findViewById(R.id.head_back_txt1);

        title.setText("安全校验");

        back.setImageResource(R.drawable.arrow_back);
        back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void init() {
        mViewIdCard = findViewById(R.id.verify_lay_idcard);
        mViewPhone = findViewById(R.id.verify_lay_phone);

        mViewIdCard.setOnClickListener(this);
        mViewPhone.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.verify_lay_idcard:
                // 跳转至验证身份证页面
                Intent mIntent1 = new Intent(VerifyActivity.this, VerifyIdentityActivity.class);
                mIntent1.putExtra(MOBILE_NAME, mobile_name);
                mIntent1.putExtra(MOBILE_ID, mobile_id);
                startActivity(mIntent1);
                break;

            case R.id.verify_lay_phone:
                // 跳转至验证手机号页面
                Intent mIntent2 = new Intent(VerifyActivity.this, VerifyPhoneActivity.class);
                mIntent2.putExtra(MOBILE_NAME, mobile_name);
                mIntent2.putExtra(MOBILE_ID, mobile_id);
                startActivity(mIntent2);
                break;
        }
    }
}

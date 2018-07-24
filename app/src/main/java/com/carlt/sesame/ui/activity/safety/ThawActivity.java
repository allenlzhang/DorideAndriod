
package com.carlt.sesame.ui.activity.safety;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.carlt.doride.DorideApplication;
import com.carlt.doride.R;
import com.carlt.sesame.control.CPControl;
import com.carlt.sesame.control.CPControl.GetResultListCallback;
import com.carlt.sesame.data.BaseResponseInfo;
import com.carlt.sesame.data.LoginInfo;
import com.carlt.sesame.data.VisitorInfo;
import com.carlt.sesame.preference.UseInfoVisitor;
import com.carlt.sesame.ui.MainActivity;
import com.carlt.sesame.ui.activity.base.BaseActivity;
import com.carlt.sesame.ui.activity.usercenter.login.LoginActivity;
import com.carlt.sesame.ui.view.PopBoxCreat;
import com.carlt.sesame.utility.Log;
import com.carlt.sesame.utility.UUToast;

/**
 * 解锁冻结账号
 * 
 * @author Administrator
 */
public class ThawActivity extends BaseActivity implements OnClickListener {
    private ImageView back;// 头部返回键

    private TextView title;// 标题文字

    private TextView txtRight;// 头部右侧文字

    private ImageView mImgIcon;// 图标

    private TextView mTxtDes1;// 第一行文字描述

    private TextView mTxtDes2;// 第二行文字描述

    private TextView mTxtAccount;// 当前账号

    private EditText mEdtLoginPsw;// 登录密码

    private TextView mTxtOpt;// 操作按钮

    private View mViewPsw;// 登录密码输入框

    private Dialog mDialog;

    private boolean isFreeze;

    public final static String FROM_NAME = "from_name";

    public final static String CLASS_SAFEMAIN = "com.carlt.sesame.ui.activity.safety.SafetyMainActivity";// 从安全主页面

    private String fromName;// 纪录从何处跳转的

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thaw);
        try {
            fromName = getIntent().getStringExtra(FROM_NAME);
            Log.e("info", "fromName==" + fromName);
        } catch (Exception e) {
            // TODO: handle exception
        }
        initTitle();
        init();
        setContent();
    }

    private void initTitle() {
        back = (ImageView)findViewById(R.id.head_back_img1);
        title = (TextView)findViewById(R.id.head_back_txt1);
        txtRight = (TextView)findViewById(R.id.head_back_txt2);

        back.setImageResource(R.drawable.arrow_back);
        title.setText("解锁冻结账号");
        txtRight.setVisibility(View.VISIBLE);
        txtRight.setText("退出登录");
        back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
        txtRight.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(ThawActivity.this, LoginActivity.class);
                startActivity(mIntent);
                finish();
            }
        });
    }

    private void init() {
        mImgIcon = (ImageView)findViewById(R.id.thaw_img_icon);

        mTxtAccount = (TextView)findViewById(R.id.thaw_txt_account);
        mTxtDes1 = (TextView)findViewById(R.id.thaw_txt_des1);
        mTxtDes2 = (TextView)findViewById(R.id.thaw_txt_des2);
        mTxtOpt = (TextView)findViewById(R.id.thaw_txt_option);

        mEdtLoginPsw = (EditText)findViewById(R.id.thaw_edt_loginpsw);

        mViewPsw = findViewById(R.id.thaw_lay_loginpsw);

            // 正常模式
            mEdtLoginPsw.setEnabled(true);

        mTxtOpt.setOnClickListener(this);
    }

    private void setContent() {
        isFreeze = LoginInfo.isFreezing();
        if (isFreeze) {
            mViewPsw.setVisibility(View.VISIBLE);
            mTxtAccount.setVisibility(View.VISIBLE);

            mImgIcon.setImageResource(R.drawable.safe_freezed_release);
            mTxtDes1.setText("解除账号冻结");
            mTxtDes2.setText("请确认账号安全后再解除冻结");

            String mobile = LoginInfo.getMobile();
            if (mobile != null && mobile.length() == 11) {
                String s1 = mobile.substring(0, 3);
                String s2 = mobile.substring(7);
                StringBuffer mBuffer = new StringBuffer("当前账号 ");
                mBuffer.append(s1);
                mBuffer.append("****");
                mBuffer.append(s2);
                mTxtAccount.setText(mBuffer);
            } else {
                mTxtAccount.setText("当前账号 " + "--");
            }
            mTxtOpt.setText("下一步");
        } else {
            mViewPsw.setVisibility(View.GONE);
            mTxtAccount.setVisibility(View.GONE);

            mImgIcon.setImageResource(R.drawable.safe_authored);
            mTxtDes1.setText("操作成功");
            mTxtDes2.setText("账号可以正常使用了");
            mTxtOpt.setText("确定");
        }
    }

    private GetResultListCallback listener = new GetResultListCallback() {

        @Override
        public void onFinished(Object o) {
            Message msg = new Message();
            msg.what = 0;
            msg.obj = o;
            mHandler.sendMessage(msg);
        }

        @Override
        public void onErro(Object o) {
            Message msg = new Message();
            msg.what = 1;
            msg.obj = o;
            mHandler.sendMessage(msg);
        }
    };

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            BaseResponseInfo mInfo = (BaseResponseInfo)msg.obj;
            switch (msg.what) {
                case 0:
                    // 解冻成功
                    if (mDialog != null && mDialog.isShowing()) {
                        mDialog.dismiss();
                    }
                    if (mInfo != null) {
                        String info = mInfo.getInfo();
                        if (info != null && info.length() > 0) {
                            UUToast.showUUToast(ThawActivity.this, info);
                        } else {
                            UUToast.showUUToast(ThawActivity.this, "解除冻结成功...");
                        }
                    } else {
                        UUToast.showUUToast(ThawActivity.this, "解除冻结成功...");
                    }
                    setContent();
                    break;

                case 1:
                    // 解冻失败
                    if (mDialog != null && mDialog.isShowing()) {
                        mDialog.dismiss();
                    }

                    if (mInfo != null) {
                        String info = mInfo.getInfo();
                        if (info != null && info.length() > 0) {
                            UUToast.showUUToast(ThawActivity.this, info);
                        } else {
                            UUToast.showUUToast(ThawActivity.this, "解除冻结失败...");
                        }
                    } else {
                        UUToast.showUUToast(ThawActivity.this, "解除冻结失败...");
                    }
                    break;
            }
        }

    };

    @Override
    public void onClick(View v) {
        if (isFreeze) {
            String psw = mEdtLoginPsw.getText().toString();
            if (psw == null || psw.length() <= 0) {
                UUToast.showUUToast(ThawActivity.this, "您还没有输入您的登录密码哦...");
                return;
            }
            if (mDialog == null) {
                mDialog = PopBoxCreat.createDialogWithProgress(ThawActivity.this, "提交中...");
            }
            mDialog.show();
            CPControl.GetFreezingResult("0", psw, listener);
        } else {
            if (fromName.equals(CLASS_SAFEMAIN)) {
                Intent mIntent = new Intent(ThawActivity.this, SafetyMainActivity.class);
                startActivity(mIntent);
            } else {
                Intent mIntent = new Intent(ThawActivity.this, MainActivity.class);
                startActivity(mIntent);
            }
            finish();
        }
    }

}

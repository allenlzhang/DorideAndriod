
package com.carlt.sesame.ui.activity.safety;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.carlt.doride.R;
import com.carlt.doride.control.LoginControl;
import com.carlt.doride.http.retrofitnet.model.UserInfo;
import com.carlt.sesame.control.ActivityControl;
import com.carlt.sesame.control.CPControl;
import com.carlt.sesame.control.CPControl.GetResultListCallback;
import com.carlt.sesame.data.BaseResponseInfo;
import com.carlt.sesame.ui.activity.base.BaseActivity;
import com.carlt.sesame.ui.view.PopBoxCreat;
import com.carlt.sesame.utility.Log;
import com.carlt.sesame.utility.UUToast;

/**
 * 快速冻结账号
 * @author Administrator
 */
public class FreezeActivity extends BaseActivity implements OnClickListener {

    private ImageView back;// 头部返回键

    private TextView title;// 标题文字

    private TextView txtRight;// 头部右侧文字

    private ImageView mImgIcon;// 图标

    private TextView mTxtDes1;// 第一行文字描述

    private TextView mTxtDes2;// 第二行文字描述

    private TextView mTxtOpt;// 操作按钮

    private boolean isFreeze;

    private Dialog mDialog;

    public final static String FROM_NAME = "from_name";

    public final static String CLASS_SAFEMAIN = "com.carlt.sesame.ui.activity.safety.SafetyMainActivity";// 从安全主页面

    private String fromName;// 纪录从何处跳转的

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_freeze);

        try {
            fromName = getIntent().getStringExtra(FROM_NAME);
            Log.e("info", "fromName==" + fromName);
        } catch (Exception e) {
            // TODO: handle exception
        }
        initTitle();
        init();
    }

    private void initTitle() {
        back = (ImageView) findViewById(R.id.head_back_img1);
        title = (TextView) findViewById(R.id.head_back_txt1);
        txtRight = (TextView) findViewById(R.id.head_back_txt2);

        title.setText("快速冻结账号");
        txtRight.setVisibility(View.GONE);

    }

    private void init() {
        mImgIcon = (ImageView) findViewById(R.id.freeze_img_icon);

        mTxtDes1 = (TextView) findViewById(R.id.freeze_txt_des1);
        mTxtDes2 = (TextView) findViewById(R.id.freeze_txt_des2);
        mTxtOpt = (TextView) findViewById(R.id.freeze_txt_option);

    }

    private void setContent() {
        isFreeze = UserInfo.getInstance().userFreeze == 2;
        if (isFreeze) {
            // 已冻结
            back.setVisibility(View.GONE);

            mImgIcon.setImageResource(R.drawable.safe_freezed);
            mTxtDes1.setText("账号已经冻结");
            mTxtDes2.setText("账号冻结解除前任何人都无法使用大乘智享");
            mTxtOpt.setText("解冻账号");
        } else {
            // 未冻结
            back.setVisibility(View.VISIBLE);
            back.setImageResource(R.drawable.arrow_back);
            back.setOnClickListener(v -> {
                if (fromName.equals(CLASS_SAFEMAIN)) {
                    finish();
                } else {
                    LoginControl.logic(FreezeActivity.this);
                }
            });

            mImgIcon.setImageResource(R.drawable.safe_freezed_no);
            mTxtDes1.setText("瞬间保护账号安全");

            String mobile = UserInfo.getInstance().mobile;
            if (mobile != null && mobile.length() == 11) {
                String s1 = mobile.substring(0, 3);
                String s2 = mobile.substring(7);
                StringBuffer mBuffer = new StringBuffer("当前账号 ");
                mBuffer.append(s1);
                mBuffer.append("****");
                mBuffer.append(s2);
                mTxtDes2.setText(mBuffer);
            } else {
                mTxtDes2.setText("当前账号 " + "--");
            }
            mTxtOpt.setText("立即冻结");
        }

        mTxtOpt.setOnClickListener(this);
    }

    private GetResultListCallback listener = new GetResultListCallback() {

        @Override
        public void onFinished(Object o) {
            Message msg = new Message();
            msg.obj = o;
            msg.what = 0;
            mHandler.sendMessage(msg);
        }

        @Override
        public void onErro(Object o) {
            Message msg = new Message();
            msg.obj = o;
            msg.what = 1;
            mHandler.sendMessage(msg);
        }
    };

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            BaseResponseInfo mInfo = (BaseResponseInfo) msg.obj;
            switch (msg.what) {
                case 0:
                    // 冻结成功
                    if (mDialog != null && mDialog.isShowing()) {
                        mDialog.dismiss();
                    }
                    if (mInfo != null) {
                        String info = mInfo.getInfo();
                        if (info != null && info.length() > 0) {
                            UUToast.showUUToast(FreezeActivity.this, info);
                        } else {
                            UUToast.showUUToast(FreezeActivity.this, "冻结成功！");
                        }
                    } else {
                        UUToast.showUUToast(FreezeActivity.this, "冻结成功！");
                    }

                    UserInfo.getInstance().userFreeze = 2;
                    setContent();
                    break;

                case 1:
                    // 冻结失败
                    if (mDialog != null && mDialog.isShowing()) {
                        mDialog.dismiss();
                    }

                    if (mInfo != null) {
                        String info = mInfo.getInfo();
                        if (info != null && info.length() > 0) {
                            UUToast.showUUToast(FreezeActivity.this, info);
                        } else {
                            UUToast.showUUToast(FreezeActivity.this, "冻结失败...");
                        }
                    } else {
                        UUToast.showUUToast(FreezeActivity.this, "冻结失败...");
                    }
                    break;
            }
        }

    };

    @Override
    protected void onResume() {
        super.onResume();
        setContent();
    }

    @Override
    public void onClick(View v) {
        // 点击操作按钮
        if (isFreeze) {
            // 已冻结-解除冻结
            Intent mIntent = new Intent(FreezeActivity.this, ThawActivity.class);
            mIntent.putExtra(ThawActivity.FROM_NAME, fromName);
            startActivity(mIntent);
        } else {
            // 未冻结-调用冻结接口
            if (mDialog == null) {
                mDialog = PopBoxCreat.createDialogWithProgress(FreezeActivity.this, "提交中...");
            }
            mDialog.show();
            CPControl.GetFreezingResult("1", "", listener);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Log.e("info", "KYActivity");
            if (isFreeze) {
                ActivityControl.exit(this);
                return true;
            } else {
                if (fromName.equals(CLASS_SAFEMAIN)) {
                    finish();
                } else {
                    LoginControl.logic(FreezeActivity.this);
                }
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

}

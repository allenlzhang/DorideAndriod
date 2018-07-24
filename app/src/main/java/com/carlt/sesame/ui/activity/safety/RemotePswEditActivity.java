
package com.carlt.sesame.ui.activity.safety;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.carlt.doride.R;
import com.carlt.sesame.control.CPControl;
import com.carlt.sesame.control.CPControl.GetResultListCallback;
import com.carlt.sesame.data.BaseResponseInfo;
import com.carlt.sesame.ui.activity.base.BaseActivity;
import com.carlt.sesame.ui.activity.remote.RemoteMainNewActivity;
import com.carlt.sesame.ui.view.PopBoxCreat;
import com.carlt.sesame.ui.view.PwdEditText;
import com.carlt.sesame.ui.view.PwdEditText.OnInputListener;
import com.carlt.sesame.utility.UUToast;

/**
 * 远程解锁密码详情-修改
 * 
 * @author Administrator
 */
public class RemotePswEditActivity extends BaseActivity implements OnClickListener {

    private ImageView back;// 头部返回键

    private TextView title;// 标题文字

    private TextView mTxtTitle1;// 小标题-原密码

    private TextView mTxtTitle2;// 小标题-新密码

    private TextView mTxtTitle3;// 小标题-确认新密码

    private PwdEditText mPwdEdt1;// 密码编辑框-原密码

    private PwdEditText mPwdEdt2;// 密码编辑框-新密码

    private PwdEditText mPwdEdt3;// 密码编辑框-新密码确认

    private TextView mTxtEdt;// 修改按钮

    private Dialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remotepsw_detail);

        initTitle();
        init();
    }

    private void initTitle() {
        back = (ImageView)findViewById(R.id.head_back_img1);
        title = (TextView)findViewById(R.id.head_back_txt1);

        title.setText("修改远程控制密码");

        back.setImageResource(R.drawable.arrow_back);
        back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void init() {
        mTxtTitle1 = (TextView)findViewById(R.id.remotepsw_detail_txt_title1);
        mTxtTitle2 = (TextView)findViewById(R.id.remotepsw_detail_txt_title2);
        mTxtTitle3 = (TextView)findViewById(R.id.remotepsw_detail_txt_title3);

        mPwdEdt1 = (PwdEditText)findViewById(R.id.remotepsw_detail_pwdedt1);
        mPwdEdt2 = (PwdEditText)findViewById(R.id.remotepsw_detail_pwdedt2);
        mPwdEdt3 = (PwdEditText)findViewById(R.id.remotepsw_detail_pwdedt3);

        mTxtEdt = (TextView)findViewById(R.id.remotepsw_detail_txt_edit);

        mTxtTitle1.setText("请输入原密码");
        mTxtTitle2.setText("请输入新密码");
        mTxtTitle3.setText("再次输入新密码");

        mTxtEdt.setText("修改");
        mTxtEdt.setOnClickListener(this);
        // mTxtEdt.setBackgroundResource(R.drawable.bottom_btn_bg_gray);
        // mTxtEdt.setClickable(false);

        mPwdEdt1.setOnInputListener(mInputFinishListener);
        mPwdEdt2.setOnInputListener(mInputFinishListener);
        mPwdEdt3.setOnInputListener(mInputFinishListener);
    }

    private boolean isInputFinish1;

    private boolean isInputFinish2;

    private boolean isInputFinish3;

    private OnInputListener mInputFinishListener = new OnInputListener() {

        @Override
        public void onInputChange(int viewID, int length, String password, boolean isFinished) {
            if (isFinished) {
                switch (viewID) {
                    case R.id.remotepsw_detail_pwdedt1:
                        mPwdEdt1.clearFocus();
                        mPwdEdt2.requestFocus();
                        mPwdEdt2.setFocusable(true);
                        mPwdEdt2.setFocusableInTouchMode(true);
                        break;

                    case R.id.remotepsw_detail_pwdedt2:
                        mPwdEdt2.clearFocus();
                        mPwdEdt3.requestFocus();
                        mPwdEdt3.setFocusable(true);
                        mPwdEdt3.setFocusableInTouchMode(true);
                        break;
                    case R.id.remotepsw_detail_pwdedt3:

                        break;
                }
            }
            // switch (viewID) {
            // case R.id.remotepsw_detail_pwdedt1:
            // isInputFinish1 = isFinished;
            // if (!isFinished) {
            // mTxtEdt.setBackgroundResource(R.drawable.bottom_btn_bg_gray);
            // mTxtEdt.setClickable(false);
            // mTxtEdt.setOnClickListener(RemotePswEditActivity.this);
            // } else {
            // if (isInputFinish1 && isInputFinish2) {
            // mTxtEdt.setBackgroundResource(R.drawable.bottom_btn_blue);
            // mTxtEdt.setClickable(true);
            // mTxtEdt.setOnClickListener(RemotePswEditActivity.this);
            // } else {
            // mTxtEdt.setBackgroundResource(R.drawable.bottom_btn_bg_gray);
            // mTxtEdt.setClickable(false);
            // mTxtEdt.setOnClickListener(RemotePswEditActivity.this);
            // }
            // }
            //
            // break;
            //
            // case R.id.remotepsw_detail_pwdedt2:
            // isInputFinish2 = isFinished;
            // if (!isFinished) {
            // mTxtEdt.setBackgroundResource(R.drawable.bottom_btn_bg_gray);
            // mTxtEdt.setClickable(false);
            // mTxtEdt.setOnClickListener(RemotePswEditActivity.this);
            // } else {
            // if (isInputFinish1 && isInputFinish3) {
            // mTxtEdt.setBackgroundResource(R.drawable.bottom_btn_blue);
            // mTxtEdt.setClickable(true);
            // mTxtEdt.setOnClickListener(RemotePswEditActivity.this);
            // } else {
            // mTxtEdt.setBackgroundResource(R.drawable.bottom_btn_bg_gray);
            // mTxtEdt.setClickable(false);
            // mTxtEdt.setOnClickListener(RemotePswEditActivity.this);
            // }
            // }
            // break;
            // case R.id.remotepsw_detail_pwdedt3:
            // isInputFinish3 = isFinished;
            // if (!isFinished) {
            // mTxtEdt.setBackgroundResource(R.drawable.bottom_btn_bg_gray);
            // mTxtEdt.setClickable(false);
            // mTxtEdt.setOnClickListener(RemotePswEditActivity.this);
            // } else {
            // if (isInputFinish1 && isInputFinish2) {
            // mTxtEdt.setBackgroundResource(R.drawable.bottom_btn_blue);
            // mTxtEdt.setClickable(true);
            // mTxtEdt.setOnClickListener(RemotePswEditActivity.this);
            // } else {
            // mTxtEdt.setBackgroundResource(R.drawable.bottom_btn_bg_gray);
            // mTxtEdt.setClickable(false);
            // mTxtEdt.setOnClickListener(RemotePswEditActivity.this);
            // }
            // }
            // break;
            // }
        }
    };

    @Override
    public void onClick(View v) {
        String pswInput = mPwdEdt1.getText().toString();
        String pswNew = mPwdEdt2.getText().toString();
        String pswNew2 = mPwdEdt3.getText().toString();

        if (pswInput == null || pswInput.length() != 6) {
            UUToast.showUUToast(RemotePswEditActivity.this, "您的原密码应为6位，请重新输入...");
            return;
        } else if (pswNew == null || pswNew.length() != 6) {
            UUToast.showUUToast(RemotePswEditActivity.this, "您的新密码应为6位，请重新输入...");
            return;
        } else if (pswNew2 == null || pswNew2.length() != 6) {
            UUToast.showUUToast(RemotePswEditActivity.this, "您再次输入的新密码应为6位，请重新输入...");
            return;
        } else if (!pswNew.equals(pswNew2)) {
            UUToast.showUUToast(RemotePswEditActivity.this, "您两次输入的新密码不一致，请重新输入...");
            return;
        } else {
            if (mDialog == null) {
                mDialog = PopBoxCreat.createDialogWithProgress(RemotePswEditActivity.this,
                        "数据提交中...");
            }
            mDialog.show();
            CPControl.GetResetRemotePwdResult(pswInput, pswNew, mListener);
        }
    }

    private GetResultListCallback mListener = new GetResultListCallback() {

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
            switch (msg.what) {
                case 0:
                    // 修改成功
                    if (mDialog != null && mDialog.isShowing()) {
                        mDialog.dismiss();
                    }
                    BaseResponseInfo mInfo = (BaseResponseInfo)msg.obj;
                    if (mInfo != null) {
                        String info = mInfo.getInfo();
                        if (info != null && info.length() > 0) {
                            UUToast.showUUToast(RemotePswEditActivity.this, info);
                        } else {
                            UUToast.showUUToast(RemotePswEditActivity.this, "远程密码修改成功！");
                        }
                    } else {
                        UUToast.showUUToast(RemotePswEditActivity.this, "远程密码修改成功！");
                    }
                    RemotePswEditActivity.this.finish();

                    Intent mIntent = new Intent();
                    mIntent.setAction(RemoteMainNewActivity.ACTION_REMOTE_RESETPSW);
                    sendBroadcast(mIntent);
                    break;

                case 1:
                    // 修改失败
                    if (mDialog != null && mDialog.isShowing()) {
                        mDialog.dismiss();
                    }
                    BaseResponseInfo mInfo1 = (BaseResponseInfo)msg.obj;
                    if (mInfo1 != null) {
                        String info = mInfo1.getInfo();
                        if (info != null && info.length() > 0) {
                            UUToast.showUUToast(RemotePswEditActivity.this, info);
                        } else {
                            UUToast.showUUToast(RemotePswEditActivity.this, "远程密码修改失败...");
                        }
                    } else {
                        UUToast.showUUToast(RemotePswEditActivity.this, "远程密码修改失败...");
                    }
                    break;
            }
        }

    };

}

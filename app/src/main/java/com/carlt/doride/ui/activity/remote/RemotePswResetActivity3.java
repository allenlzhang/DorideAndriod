
package com.carlt.doride.ui.activity.remote;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.carlt.doride.R;
import com.carlt.doride.base.LoadingActivity;
import com.carlt.doride.control.CPControl;
import com.carlt.doride.data.BaseResponseInfo;
import com.carlt.doride.model.LoginInfo;
import com.carlt.doride.preference.RemotePswInfo;
import com.carlt.doride.protocolparser.BaseParser;
import com.carlt.doride.ui.fragment.RemoteMainFragment;
import com.carlt.doride.ui.view.PopBoxCreat;
import com.carlt.doride.ui.view.PwdEditText;
import com.carlt.doride.ui.view.UUToast;
import com.carlt.doride.utils.StringUtils;


/**
 * 忘记远程解锁密码-step3设置新密码
 * @author Administrator
 */
public class RemotePswResetActivity3 extends LoadingActivity implements OnClickListener {

    private TextView mTxtTitle1;// 小标题-新密码

    private TextView mTxtTitle2;// 小标题-新密码确认

    private PwdEditText mPwdEdt1;// 密码编辑框-新密码

    private PwdEditText mPwdEdt2;// 密码编辑框-新密码确认

    private TextView mTxtEdt;// 修改按钮

    private Dialog mDialog;

    public final static String VALIDATE = "validate";

    public final static String INFO_NAME = "info_name";

    public final static String INFO_IDCARD = "info_idcard";

    public final static String TYPE = "from";

    public final static int TYPE_REALNAME = 1;// 从实名认证

    public final static int TYPE_FORGET = 2;// 从忘记远程密码

    public final static int TYPE_REMOTE = 3;// 从远程页面

    public final static int TYPE_SAFETY = 4;// 从安全-密码管理跳转过来

    private int type;

    private String validate;// 验证码

    String name;// 姓名

    String idcard;// 身份证号

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remotepsw_reset3);

        try {
            type = getIntent().getIntExtra(TYPE, -1);
            validate = getIntent().getStringExtra(VALIDATE);
            name = getIntent().getStringExtra(INFO_NAME);
            idcard = getIntent().getStringExtra(INFO_IDCARD);
        } catch (Exception e) {
            Log.e("info", getClass().getName() + ":" + e);
        }

        initMyTitle();
        init();
    }

    private void initMyTitle() {

        switch (type) {
            case TYPE_REALNAME:
                initTitle("设置远程控制密码");
                break;
            case TYPE_FORGET:
                initTitle("重置远程控制密码");
                break;
            case TYPE_REMOTE:
                initTitle("设置远程控制密码");
                break;
            case TYPE_SAFETY:
                initTitle("设置远程控制密码");
                break;
            default:
                initTitle("设置远程控制密码");

                break;
        }

    }

    private void init() {
        mTxtTitle1 = findViewById(R.id.remotepsw_reset3_txt_title1);
        mTxtTitle2 = findViewById(R.id.remotepsw_reset3_txt_title2);

        mPwdEdt1 = findViewById(R.id.remotepsw_reset3_pwdedt1);
        mPwdEdt2 = findViewById(R.id.remotepsw_reset3_pwdedt2);

        mTxtEdt = findViewById(R.id.remotepsw_reset3_txt_edit);

        switch (type) {
            case TYPE_REALNAME:
                mTxtEdt.setText("完成");
                break;

            case TYPE_FORGET:
                mTxtEdt.setText("修改");
                break;
            case TYPE_REMOTE:
                mTxtEdt.setText("完成");
                break;
            case TYPE_SAFETY:
                mTxtEdt.setText("完成");
                break;
        }

        mTxtTitle1.setText("请输入新密码");
        mTxtTitle2.setText("请再次输入新密码");
        //        mPwdEdt1.setOnInputListener(mInputListener);
        //        mPwdEdt2.setOnInputListener(mInputListener);

        // mTxtEdt.setBackgroundResource(R.drawable.bottom_btn_bg_gray);
        // mTxtEdt.setClickable(false);
        mTxtEdt.setOnClickListener(this);
    }

    private boolean isInputFinish1;

    private boolean isInputFinish2;

    private PwdEditText.OnInputListener mInputListener = new PwdEditText.OnInputListener() {

        @Override
        public void onInputChange(int viewID, int length, String password, boolean isFinished) {

        }
    };

    @Override
    public void onClick(View v) {
        String pswNew1 = mPwdEdt1.getText().toString().trim();
        String pswNew2 = mPwdEdt2.getText().toString().trim();
        if (StringUtils.isEmpty(pswNew1)) {
            UUToast.showUUToast(RemotePswResetActivity3.this, "密码不能为空");
            return;
        } else if (pswNew1.length() < 6) {
            UUToast.showUUToast(this, "密码至少为6位数字");
        } else if (!pswNew1.equals(pswNew2)) {
            UUToast.showUUToast(RemotePswResetActivity3.this, "两次输入密码不一致，请重新输入");
            return;
        } else {
            if (mDialog == null) {
                mDialog = PopBoxCreat.createDialogWithProgress(RemotePswResetActivity3.this,
                        "数据提交中...");
            }
            mDialog.show();

            remote_pwd = pswNew1;
            switch (type) {
                case TYPE_REALNAME:
                    CPControl.GetSetRemotePwdResult(pswNew1, listener_set);
                    break;
                case TYPE_FORGET:
                    String mobile = LoginInfo.getMobile();
                    CPControl.GetForgetRemotePwdResult(name, idcard, mobile, pswNew1, validate, listener_forget);
                    break;
                case TYPE_REMOTE:
                    CPControl.GetSetRemotePwdResult(pswNew1, listener_set);
                    break;
                case TYPE_SAFETY:
                    CPControl.GetSetRemotePwdResult(pswNew1, listener_set);
                    break;
            }
        }
    }

    // 设置密码
    private BaseParser.ResultCallback listener_set = new BaseParser.ResultCallback() {

        @Override
        public void onSuccess(BaseResponseInfo bInfo) {
            Message msg = new Message();
            msg.what = 0;
            msg.obj = bInfo;
            mHandler.sendMessage(msg);
        }

        @Override
        public void onError(BaseResponseInfo bInfo) {
            Message msg = new Message();
            msg.what = 1;
            msg.obj = bInfo;
            mHandler.sendMessage(msg);
        }


    };

    // 忘记密码
    private BaseParser.ResultCallback listener_forget = new BaseParser.ResultCallback() {

        @Override
        public void onSuccess(BaseResponseInfo o) {
            Message msg = new Message();
            msg.what = 2;
            msg.obj = o;
            mHandler.sendMessage(msg);
        }

        @Override
        public void onError(BaseResponseInfo o) {
            Message msg = new Message();
            msg.what = 3;
            msg.obj = o;
            mHandler.sendMessage(msg);
        }

    };

    String remote_pwd;

    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    // 设置密码成功
                    if (mDialog != null && mDialog.isShowing()) {
                        mDialog.dismiss();
                    }

                    RemotePswInfo.setRemotePsw(remote_pwd);
                    LoginInfo.setSetRemotePwd(true);
                    BaseResponseInfo mInfo = (BaseResponseInfo) msg.obj;
                    if (mInfo != null) {
                        String info = mInfo.getInfo();
                        if (info != null && info.length() > 0) {
                            UUToast.showUUToast(RemotePswResetActivity3.this, info);
                        } else {
                            UUToast.showUUToast(RemotePswResetActivity3.this, "设置远程密码成功！");
                        }
                    } else {
                        UUToast.showUUToast(RemotePswResetActivity3.this, "设置远程密码成功！");
                    }
                    if (type == TYPE_REMOTE) {
                        Intent mIntent = new Intent();
                        mIntent.setAction(RemoteMainFragment.ACTION_REMOTE_SETPSW);
                        sendBroadcast(mIntent);
                    }
                    finish();
                    break;
                case 1:
                    // 设置密码失败
                    if (mDialog != null && mDialog.isShowing()) {
                        mDialog.dismiss();
                    }
                    LoginInfo.setSetRemotePwd(false);
                    BaseResponseInfo mInfo1 = (BaseResponseInfo) msg.obj;
                    if (mInfo1 != null) {
                        String info = mInfo1.getInfo();
                        if (info != null && info.length() > 0) {
                            UUToast.showUUToast(RemotePswResetActivity3.this, info);
                        } else {
                            UUToast.showUUToast(RemotePswResetActivity3.this, "设置远程密码失败...");
                        }
                    } else {
                        UUToast.showUUToast(RemotePswResetActivity3.this, "设置远程密码失败...");
                    }
                    break;

                case 2:
                    // 重置密码成功
                    if (mDialog != null && mDialog.isShowing()) {
                        mDialog.dismiss();
                    }
                    RemotePswInfo.setRemotePsw(remote_pwd);
                    LoginInfo.setSetRemotePwd(true);
                    BaseResponseInfo mInfo2 = (BaseResponseInfo) msg.obj;
                    if (mInfo2 != null) {
                        String info = mInfo2.getInfo();
                        if (info != null && info.length() > 0) {
                            UUToast.showUUToast(RemotePswResetActivity3.this, info);
                        } else {
                            UUToast.showUUToast(RemotePswResetActivity3.this, "重置远程密码成功！");
                        }
                    } else {
                        UUToast.showUUToast(RemotePswResetActivity3.this, "重置远程密码成功！");
                    }
                    Intent mIntent2 = new Intent();
                    mIntent2.setAction(RemoteMainFragment.ACTION_REMOTE_FORGETPSW);
                    sendBroadcast(mIntent2);
                    finish();
                    break;
                case 3:
                    // 重置密码失败
                    if (mDialog != null && mDialog.isShowing()) {
                        mDialog.dismiss();
                    }
                    BaseResponseInfo mInfo3 = (BaseResponseInfo) msg.obj;
                    if (mInfo3 != null) {
                        String info = mInfo3.getInfo();
                        if (info != null && info.length() > 0) {
                            UUToast.showUUToast(RemotePswResetActivity3.this, info);
                        } else {
                            UUToast.showUUToast(RemotePswResetActivity3.this, "重置远程密码失败...");
                        }
                    } else {
                        UUToast.showUUToast(RemotePswResetActivity3.this, "重置远程密码失败...");
                    }

                    break;
                default:
                    break;
            }
        }

    };
}

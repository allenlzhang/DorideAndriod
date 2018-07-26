
package com.carlt.sesame.ui.activity.safety;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.carlt.doride.DorideApplication;
import com.carlt.doride.R;
import com.carlt.sesame.control.ActivityControl;
import com.carlt.sesame.control.CPControl;
import com.carlt.sesame.control.CPControl.GetResultListCallback;
import com.carlt.sesame.data.BaseResponseInfo;
import com.carlt.sesame.data.LoginInfo;
import com.carlt.sesame.ui.activity.base.BaseActivity;
import com.carlt.sesame.ui.view.PopBoxCreat;
import com.carlt.sesame.ui.view.PopBoxCreat.DialogWithTitleClick;
import com.carlt.sesame.ui.view.ValidateEditText;
import com.carlt.sesame.utility.UUToast;

/**
 * 身份验证
 * 
 * @author Daisy
 */
public class VerifyIdentityActivity extends BaseActivity implements OnClickListener {

    private ImageView back;// 头部返回键

    private TextView title;// 标题文字

    private ValidateEditText mEdtName;// 用户姓名

    private ValidateEditText mEdtIdCard;// 用户身份证号

    private ValidateEditText mEdtRemotepsw;// 远程控制密码

    private TextView mBtnOption;// 更换主机按钮

    private View mViewRemotepsw;// 远程控制密码

    private Dialog mDialog;

    private String mobile_name;// 手机名称

    private String mobile_id;// 手机唯一标识id

    private String mobile_num;// 手机号

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_identity);
        try {
            mobile_name = getIntent().getStringExtra(VerifyActivity.MOBILE_NAME);
            mobile_id = getIntent().getStringExtra(VerifyActivity.MOBILE_ID);
            mobile_num = getIntent().getStringExtra(VerifyActivity.MOBILE_NUM);
        } catch (Exception e) {
            // TODO: handle exception
        }
        initTitle();
        init();
    }

    private void initTitle() {
        back = (ImageView)findViewById(R.id.head_back_img1);
        title = (TextView)findViewById(R.id.head_back_txt1);

        title.setText("身份验证");

        back.setImageResource(R.drawable.arrow_back);
        back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void init() {
        mEdtName = (ValidateEditText)findViewById(R.id.verify_identity_edt_name);
        mEdtIdCard = (ValidateEditText)findViewById(R.id.verify_identity_edt_IDCard);
        mEdtRemotepsw = (ValidateEditText)findViewById(R.id.verify_identity_edt_remotepsw);
        
        mEdtName.setEditHint("请输入姓名");
        mEdtName.setNextEditText(mEdtIdCard);
        
        
        mEdtIdCard.setEditHint("请输入身份证号");
        mEdtIdCard.setmType(ValidateEditText.TYPE_IDN);
        mEdtIdCard.setNextEditText(mEdtRemotepsw);
        
        mEdtRemotepsw.setEditHint("请输入远程控制密码");
        mEdtRemotepsw.setmLength(6);
        mEdtRemotepsw.setmType(ValidateEditText.TYPE_PDN);
        mBtnOption = (TextView)findViewById(R.id.verify_identity_txt_option);

        mViewRemotepsw = findViewById(R.id.verify_identity_lay_remotepsw);

        if (LoginInfo.isMain()) {
            mViewRemotepsw.setVisibility(View.GONE);
            mBtnOption.setText("更换主机");
        } else {
            mViewRemotepsw.setVisibility(View.VISIBLE);
            mBtnOption.setText("确定登录");
        }

        mBtnOption.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
    	mEdtName.validateEdit();
    	mEdtIdCard.validateEdit();
    	mEdtRemotepsw.validateEdit();
        String authen_name = mEdtName.getText().toString();
        String authen_card = mEdtIdCard.getText().toString();
        String remote_psw = mEdtRemotepsw.getText().toString();
        if (authen_name == null || authen_name.length() <= 0) {
            UUToast.showUUToast(VerifyIdentityActivity.this, "您还没有填写您的真实姓名哦...");
            return;
        } else if (authen_card == null || authen_card.length() <= 0) {
            UUToast.showUUToast(VerifyIdentityActivity.this, "您还没有填写您的身份证号码哦...");
            return;
        } else {
            if (LoginInfo.isMain()) {
                if (mDialog == null) {
                    mDialog = PopBoxCreat.createDialogWithProgress(VerifyIdentityActivity.this,
                            "处理中...");
                }
                mDialog.show();
                // 调用更换主机接口
                CPControl.GetChangeMainDeviceResult(CPControl.CHANGE_BY_ID, authen_name,
                        authen_card, "", "", "", mobile_id, mobile_name, listener_change);
            } else {
                if (remote_psw == null || remote_psw.length() <= 0) {
                    UUToast.showUUToast(VerifyIdentityActivity.this, "您还没有填写您的远程控制密码哦...");
                    return;
                } else {
                    if (mDialog == null) {
                        mDialog = PopBoxCreat.createDialogWithProgress(VerifyIdentityActivity.this,
                                "处理中...");
                    }
                    mDialog.show();
                    // 调用无授权登录接口
                    CPControl.GetNoAuthorizeLoginResult(mobile_num, authen_name, authen_card,
                            remote_psw, DorideApplication.NIMEI, DorideApplication.MODEL_NAME,
                            DorideApplication.MODEL, listener_verify_unauthorized);
                }
            }
        }
    }

    // listener验证用户身份信息
    private GetResultListCallback listener_verify = new GetResultListCallback() {

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

    // listener验证用户身份信息-无授权登录
    private GetResultListCallback listener_verify_unauthorized = new GetResultListCallback() {

        @Override
        public void onFinished(Object o) {
            Message msg = new Message();
            msg.what = 2;
            msg.obj = o;
            mHandler.sendMessage(msg);
        }

        @Override
        public void onErro(Object o) {
            Message msg = new Message();
            msg.what = 3;
            msg.obj = o;
            mHandler.sendMessage(msg);
        }
    };

    // listener-更换主机
    private GetResultListCallback listener_change = new GetResultListCallback() {

        @Override
        public void onFinished(Object o) {
            Message msg = new Message();
            msg.what = 4;
            msg.obj = o;
            mHandler.sendMessage(msg);
        }

        @Override
        public void onErro(Object o) {
            Message msg = new Message();
            msg.what = 5;
            msg.obj = o;
            mHandler.sendMessage(msg);
        }
    };

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            BaseResponseInfo mInfo;
            String infoMsg;
            switch (msg.what) {
                case 2:
                    // 调用无授权登录成功
                    // 返回登录页
//                    setResult(LoginActivity.REQUEST_CODE + 1);
                    finish();
                    break;

                case 3:
                    // 调用无授权登录失败
                    if (mDialog != null) {
                        mDialog.dismiss();
                    }
                    mInfo = (BaseResponseInfo)msg.obj;
                    if (mInfo != null) {
                        String info = mInfo.getInfo();
                        if (info != null && info.length() > 0) {
                            infoMsg = info;
                        } else {
                            infoMsg = "验证失败...";
                        }
                    } else {
                        infoMsg = "验证失败...";
                    }

                    UUToast.showUUToast(VerifyIdentityActivity.this, infoMsg);
                    break;
                case 4:
                    if (mDialog != null) {
                        mDialog.dismiss();
                    }
                    DialogWithTitleClick click = new DialogWithTitleClick() {

                        @Override
                        public void onRightClick() {
                            // TODO Auto-generated method stub

                        }

                        @Override
                        public void onLeftClick() {
                            ActivityControl.onLogout(VerifyIdentityActivity.this);
                            finish();
                        }
                    };
                    StringBuffer mBuffer = new StringBuffer();
                    mBuffer.append("手机");
                    mBuffer.append("\"");
                    mBuffer.append(mobile_name);
                    mBuffer.append("\"");
                    mBuffer.append("成为主机");
                    PopBoxCreat.createDialogNotitle(VerifyIdentityActivity.this, "主机更换成功",
                            mBuffer.toString(), "确认", "", click, true);
                    break;

                case 5:
                    if (mDialog != null) {
                        mDialog.dismiss();
                    }
                    mInfo = (BaseResponseInfo)msg.obj;
                    if (mInfo != null) {
                        String info = mInfo.getInfo();
                        if (info != null && info.length() > 0) {
                            infoMsg = info;
                        } else {
                            infoMsg = "主机更换失败...";
                        }
                    } else {
                        infoMsg = "主机更换失败...";
                    }

                    UUToast.showUUToast(VerifyIdentityActivity.this, infoMsg);
                    break;

            }
        }

    };
}

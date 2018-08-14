
package com.carlt.sesame.ui.activity.setting;

import android.annotation.SuppressLint;
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
import com.carlt.doride.data.UseInfo;
import com.carlt.doride.model.LoginInfo;
import com.carlt.doride.preference.UseInfoLocal;
import com.carlt.doride.ui.activity.login.UserLoginActivity;
import com.carlt.sesame.control.CPControl;
import com.carlt.sesame.control.CPControl.GetResultListCallback;
import com.carlt.sesame.data.BaseResponseInfo;
import com.carlt.sesame.data.SesameLoginInfo;
import com.carlt.sesame.ui.activity.base.BaseActivity;
import com.carlt.sesame.ui.view.GetValidateView;
import com.carlt.sesame.ui.view.PopBoxCreat;
import com.carlt.sesame.ui.view.ValidateEditText;
import com.carlt.sesame.utility.UUToast;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 修改手机号-第二步（输入新手机号、验证码）
 * 
 * @author daisy
 */
public class EditPhoneActivity2 extends BaseActivity {
    private ImageView back;// 头部返回键

    private TextView title;// 标题文字

    private TextView txtRight;// 头部右侧文字

    private ValidateEditText mEdtPhone;// 新手机号

    private ValidateEditText mEdtCode;// 验证码

    private TextView mGetCode;// 获取验证码按钮

    private TextView mTxtTips;// 提示

    private View mBtnSure;// 确定按钮

    private GetValidateView mValidateView;// 获取语音验证码view

    private Dialog mDialog;

    private String code;// 从第一步传入的code

    public final static String CODE_INFO = "code_info";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_editphone);

        try {
            code = getIntent().getStringExtra(CODE_INFO);
        } catch (Exception e) {
            // TODO: handle exception
        }

        init();
        initTitle();
    }

    private void init() {

        mEdtPhone = (ValidateEditText)findViewById(R.id.editphone_edt_phone);
        mEdtCode = (ValidateEditText)findViewById(R.id.editphone_edt_code);
        mGetCode = (TextView)findViewById(R.id.editphone_txt_getcode);
        mTxtTips = (TextView)findViewById(R.id.editphone_txt_tips);
        mBtnSure = findViewById(R.id.editphone_view_sure);

        mEdtPhone.setEditHint("请输入新手机号码");
        mEdtPhone.setmLength(11);
        mEdtPhone.setmType(ValidateEditText.TYPE_PON);
        mEdtPhone.setNextEditText(mEdtCode);
        
        mEdtCode.setEditHint("请输入验证码");
        mEdtCode.setmType(ValidateEditText.TYPE_CODE);
        mTxtTips.setText("验证码将发送到新手机上");

        mGetCode.setOnClickListener(mClickListener);
        mBtnSure.setOnClickListener(mClickListener);
    }

    private void initTitle() {
        back = (ImageView)findViewById(R.id.head_back_img1);
        title = (TextView)findViewById(R.id.head_back_txt1);
        txtRight = (TextView)findViewById(R.id.head_back_txt2);

        back.setImageResource(R.drawable.arrow_back);
        title.setText("修改手机号码");
        txtRight.setVisibility(View.GONE);

        back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private String phoneNum;// 新手机号

    private OnClickListener mClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
        	mEdtPhone.validateEdit();
            phoneNum = mEdtPhone.getText().toString();
            switch (v.getId()) {
                case R.id.editphone_txt_getcode:
                    // 获取验证码
                    if (phoneNum != null && phoneNum.length() == 11) {
                        String phoneOld = SesameLoginInfo.getMobile();
                        if (phoneNum.equals(phoneOld)) {
                            UUToast.showUUToast(EditPhoneActivity2.this, "您输入的是旧手机号哦！");
                            return;
                        }
                        CPControl.GetMessageValidateResult(CPControl.VALIDATE_TYPE_EDITPHONE,
                                phoneNum, listener_msg);
                        count = 60;
                        mGetCode.setText(count + "秒后重发");
                        mGetCode.setClickable(false);
                        mGetCode.setBackgroundResource(R.drawable.btn_code_gray);

                        task = new TimerTask() {

                            @Override
                            public void run() {
                                Message msg = new Message();
                                msg.what = 10;
                                mHandler.sendMessage(msg);

                            }
                        };
                        timer.schedule(task, 1000, 1000);

                    } else {
                        UUToast.showUUToast(EditPhoneActivity2.this, "请输入正确的手机号");
                    }
                    break;

                case R.id.editphone_view_sure:
                    // 修改手机号码接口
                    mEdtCode.validateEdit();
                    mEdtPhone.validateEdit();
                    String validate = mEdtCode.getText().toString();
                    if (phoneNum == null || phoneNum.length() != 11) {
                        UUToast.showUUToast(EditPhoneActivity2.this, "请输入正确的手机号");
                        return;
                    } else if (validate == null || validate.length() <= 0) {
                        UUToast.showUUToast(EditPhoneActivity2.this, "请输入正确的验证码");
                        return;
                    } else {
                        if (mDialog == null) {
                            mDialog = PopBoxCreat.createDialogWithProgress(EditPhoneActivity2.this,
                                    "提交中...");
                        }
                        mDialog.show();
                        CPControl.getEditPhoneNum222(phoneNum, validate, code, listener_editphone);
                    }
                    break;
            }

        }
    };

    /**
     * 倒计时
     */
    private int count = 60;

    private Timer timer = new Timer();

    private TimerTask task;

    // 获取短信验证码
    GetResultListCallback listener_msg = new GetResultListCallback() {

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

    // 编辑密码接口回调
    GetResultListCallback listener_editphone = new GetResultListCallback() {

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

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            BaseResponseInfo mBaseResponseInfo;
            switch (msg.what) {
                case 0:
                    // 获取验证码成功
                    UUToast.showUUToast(EditPhoneActivity2.this, "验证码已发送成功！");
                    break;

                case 1:
                    // 获取验证码失败
                    // 停止计时
                    if (timer != null) {
                        if (task != null) {
                            task.cancel();
                        }
                    }
                    mGetCode.setClickable(true);
                    mGetCode.setText("重发验证码");
                    mGetCode.setBackgroundResource(R.drawable.btn_code_bg);

                    mBaseResponseInfo = (BaseResponseInfo)msg.obj;
                    int flag = mBaseResponseInfo.getFlag();
                    if (flag == BaseResponseInfo.VALIDATE_LIMIT) {
                        if (mValidateView == null) {
                            mValidateView = new GetValidateView(EditPhoneActivity2.this,
                                    CPControl.VALIDATE_TYPE_EDITPHONE, phoneNum);
                        }
                        mValidateView.setClickStatus(true);
                        mValidateView.showMenu();
                    } else {
                        UUToast.showUUToast(EditPhoneActivity2.this,
                                "验证码获取失败:" + mBaseResponseInfo.getInfo());
                    }
                    break;
                case 2:
                    // 修改手机号成功
                    if (mDialog != null && mDialog.isShowing()) {
                        mDialog.dismiss();
                    }
                    UUToast.showUUToast(EditPhoneActivity2.this, "手机号修改成功");
                    LoginInfo.setMobile(mEdtPhone.getText().toString());
                    UseInfo mUseInfo = UseInfoLocal.getUseInfo();
                    mUseInfo.setAccount(mEdtPhone.getText().toString());
                    UseInfoLocal.setUseInfo(mUseInfo);
                    Intent loginIntent=new Intent(EditPhoneActivity2.this, UserLoginActivity.class);
                    startActivity(loginIntent);
                    finish();
                    break;
                case 3:
                    // 修改手机号失败
                    if (mDialog != null && mDialog.isShowing()) {
                        mDialog.dismiss();
                    }
                    mBaseResponseInfo = (BaseResponseInfo)msg.obj;
                    String info = mBaseResponseInfo.getInfo();
                    if (info != null && info.length() > 0) {
                        UUToast.showUUToast(EditPhoneActivity2.this, "手机号修改失败:" + info);
                    } else {
                        UUToast.showUUToast(EditPhoneActivity2.this, "手机号修改失败...");
                    }

                    break;
                case 10:
                    count--;
                    if (count > 0) {
                        mGetCode.setText(count + "秒后重发");
                    } else {
                        if (timer != null) {
                            if (task != null) {
                                task.cancel();
                            }
                        }
                        mGetCode.setClickable(true);
                        mGetCode.setText("重发验证码");
                        mGetCode.setBackgroundResource(R.drawable.btn_code_bg);
                    }
                    break;
            }
        }

    };
}

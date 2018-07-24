
package com.carlt.sesame.ui.activity.setting;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.carlt.doride.R;
import com.carlt.sesame.control.ActivityControl;
import com.carlt.sesame.control.CPControl;
import com.carlt.sesame.control.CPControl.GetResultListCallback;
import com.carlt.sesame.data.BaseResponseInfo;
import com.carlt.sesame.data.LoginInfo;
import com.carlt.sesame.http.AsyncImageLoader;
import com.carlt.sesame.ui.activity.base.BaseActivity;
import com.carlt.sesame.ui.view.GetValidateView;
import com.carlt.sesame.ui.view.PopBoxCreat;
import com.carlt.sesame.ui.view.ValidateEditText;
import com.carlt.sesame.utility.CheckInfo;
import com.carlt.sesame.utility.UUToast;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 设置-修改资料-每一项详情页面
 * 
 * @author daisy
 */
public class EditUserinfoDetailActivity extends BaseActivity {
    private ImageView back;// 头部返回键

    private TextView title;// 标题文字

    private TextView txtRight;// 头部右侧文字

    private View mView1;// 修改头像layout

    private View mView3;// 修改密码layout

    private View mView4;// 修改姓名/昵称layout

    private View mView5;// 修改性别layout

    private TextView mBtnChange;// 修改按钮

    // 编辑头像
    private ImageView mHeadIconImg;// 用户头像

    private TextView mHeadIconTxt1;// 从相册选

    private TextView mHeadIconTxt2;// 重新照

    // 编辑密码
    private ValidateEditText mPasswordEdt1;// 旧密码

    private ValidateEditText mPasswordEdt2;// 新密码

    private ValidateEditText mPasswordEdt3;// 新密码确认

    // 编辑用户名
    private EditText mNameEdt;// 用户名

    // 编辑性别

    private RadioGroup mRadioGroup;

    private RadioButton mSexRadio1;// 男

    private RadioButton mSexRadio2;// 女
    
    private RadioButton mSexRadio3;// 保密

    private AsyncImageLoader mAsyncImageLoader = AsyncImageLoader.getInstance();

    private Dialog mDialog;

    private GetValidateView mValidateView;// 播报语音验证码页面

    private int type;

    public final static int TYPE_HEAD_ICON = 1001;

    public final static int TYPE_PASSWORD = 1003;

    public final static int TYPE_NAME = 1004;

    public final static int TYPE_SEX = 1005;

    public final static String EDIT_TYPE = "edit_type";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_userinfo_detail);

        type = getIntent().getIntExtra(EDIT_TYPE, 0);
        mDialog = PopBoxCreat.createDialogWithProgress(EditUserinfoDetailActivity.this, "努力加载中...");

        initTitle();
        init();
    }

    private void initTitle() {
        back = (ImageView)findViewById(R.id.head_back_img1);
        title = (TextView)findViewById(R.id.head_back_txt1);
        txtRight = (TextView)findViewById(R.id.head_back_txt2);

        back.setImageResource(R.drawable.arrow_back);
        switch (type) {
            case TYPE_HEAD_ICON:
                title.setText("修改头像");
                break;
            case TYPE_PASSWORD:
                title.setText("修改登录密码");
                break;
            case TYPE_NAME:
                title.setText("修改姓名");
                break;
            case TYPE_SEX:
                title.setText("修改性别");
                break;
        }

        txtRight.setVisibility(View.GONE);

        back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void init() {

        mView1 = findViewById(R.id.activity_setting_userinfo_detail_layout_head_icon);
        mView3 = findViewById(R.id.activity_setting_userinfo_detail_layout_password);
        mView4 = findViewById(R.id.activity_setting_userinfo_detail_layout_name);
        mView5 = findViewById(R.id.activity_setting_userinfo_detail_layout_sex);
        mBtnChange = (TextView)findViewById(R.id.activity_setting_userinfo_detail_btn);

        mBtnChange.setOnClickListener(mClickListener);

        switch (type) {
            case TYPE_HEAD_ICON:
                mView1.setVisibility(View.VISIBLE);
                initHeadIcon();
                break;
            case TYPE_PASSWORD:
                mView3.setVisibility(View.VISIBLE);
                initPassword();
                break;
            case TYPE_NAME:
                mView4.setVisibility(View.VISIBLE);
                initName();
                break;
            case TYPE_SEX:
                mView5.setVisibility(View.VISIBLE);
                mBtnChange.setVisibility(View.GONE);
                initSex();
                break;
        }

    }

    /**
     * 设置头像部分
     */
    private void initHeadIcon() {
        mHeadIconImg = (ImageView)findViewById(R.id.activity_setting_userinfo_detail_head_icon_img);
        mHeadIconTxt1 = (TextView)findViewById(R.id.activity_setting_userinfo_detail_head_icon_txt1);
        mHeadIconTxt2 = (TextView)findViewById(R.id.activity_setting_userinfo_detail_head_icon_txt2);

        if (LoginInfo.getAvatar_img() != null && LoginInfo.getAvatar_img().length() > 0) {
            if (mAsyncImageLoader.getBitmapByUrl(LoginInfo.getAvatar_img()) != null) {
                mHeadIconImg.setImageBitmap(mAsyncImageLoader.getBitmapByUrl(LoginInfo
                        .getAvatar_img()));
            }
        } else {
            mHeadIconImg.setImageResource(R.drawable.icon_default_head);
        }

        mHeadIconTxt1.setOnClickListener(mClickListener);
        mHeadIconTxt2.setOnClickListener(mClickListener);
    }

    /**
     * 设置密码部分
     */
    private void initPassword() {
        mPasswordEdt1 = (ValidateEditText)findViewById(R.id.activity_setting_userinfo_detail_password_edt1);
        mPasswordEdt2 = (ValidateEditText)findViewById(R.id.activity_setting_userinfo_detail_password_edt2);
        mPasswordEdt3 = (ValidateEditText)findViewById(R.id.activity_setting_userinfo_detail_password_edt3);
        
        mPasswordEdt1.setEditHint("请填写旧密码");
        mPasswordEdt1.setmType(ValidateEditText.TYPE_PDT);
        mPasswordEdt1.setNextEditText(mPasswordEdt2);
        
        
        mPasswordEdt2.setEditHint("请填写新密码");
        mPasswordEdt2.setmType(ValidateEditText.TYPE_PDT);
        mPasswordEdt2.setNextEditText(mPasswordEdt3);
        
        
        mPasswordEdt3.setEditHint("再次填写新密码");
        mPasswordEdt3.setmType(ValidateEditText.TYPE_COFT);
        mPasswordEdt3.setmConfirmEdit(mPasswordEdt3.getmEditText());
    }

    /**
     * 设置用户名部分
     */
    private void initName() {
        mNameEdt = (EditText)findViewById(R.id.activity_setting_userinfo_detail_name_edt1);

        mNameEdt.setText(LoginInfo.getRealname());

        mNameEdt.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View arg0, boolean focusable) {
                if (focusable) {
                    mNameEdt.setText("");
                    mNameEdt.setHint("");
                } else {
                    mNameEdt.setText(LoginInfo.getRealname());
                }
            }
        });

    }

    /**
     * 设置性别部分
     */
    private void initSex() {
        mRadioGroup = (RadioGroup)findViewById(R.id.activity_setting_userinfo_detail_layout_sex);
        mSexRadio1 = (RadioButton)findViewById(R.id.activity_setting_userinfo_detail_sex_radio1);
        mSexRadio2 = (RadioButton)findViewById(R.id.activity_setting_userinfo_detail_sex_radio2);
        mSexRadio3 = (RadioButton)findViewById(R.id.activity_setting_userinfo_detail_sex_radio3);

        if (LoginInfo.getGender().equals("1")) {
            // 男
            mSexRadio1.setChecked(true);
        } else if (LoginInfo.getGender().equals("2")) {
            // 女
            mSexRadio2.setChecked(true);
        }else{
        	// 保密
            mSexRadio3.setChecked(true);
        }

        mRadioGroup.setOnCheckedChangeListener(mOnCheckedChangeListener);
    }

    /**
     * 倒计时
     */
    private int count = 60;

    private Timer timer = new Timer();

    private TimerTask task;

    private String phoneNum;

    private OnClickListener mClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.activity_setting_userinfo_detail_btn:
                    // 确定按钮
                    postInfo();
                    break;

            }
        }
    };

    /**
     * 提交用户修改的数据
     */
    private void postInfo() {
        switch (type) {
            case TYPE_HEAD_ICON:
                break;
            case TYPE_PASSWORD:
            	
            	mPasswordEdt1.validateEdit();
            	mPasswordEdt2.validateEdit();
            	mPasswordEdt3.validateEdit();
            	
                String oldPassword;
                String newPassword;
                String newPassword2;

                oldPassword = mPasswordEdt1.getText().toString();
                String checkPsw=CheckInfo.checkPassword(oldPassword);
                if (!checkPsw.equals(CheckInfo.CORRECT_PSWLENTH)) {
                    UUToast.showUUToast(EditUserinfoDetailActivity.this,
                            "您的旧"+checkPsw);
                    return;
                } 

                newPassword = mPasswordEdt2.getText().toString();
                newPassword2 = mPasswordEdt3.getText().toString();
                String checkPswNew=CheckInfo.checkPassword(newPassword);
                String checkPswNew2=CheckInfo.checkPassword(newPassword2);
                if (!checkPswNew.equals(CheckInfo.CORRECT_PSWLENTH)) {
                    UUToast.showUUToast(EditUserinfoDetailActivity.this,
                            "您的新"+checkPswNew);
                    return;
                } else if(!checkPswNew2.equals(CheckInfo.CORRECT_PSWLENTH)){
                    UUToast.showUUToast(EditUserinfoDetailActivity.this,
                            "您再次输入的新"+checkPswNew2);
                    return;
                }else if (!(newPassword.equals(newPassword2))) {
                    UUToast.showUUToast(EditUserinfoDetailActivity.this, "您两次输入的新密码不一致，请重新输入...");
                    return;
                }
                if (mDialog == null) {
                    mDialog = PopBoxCreat.createDialogWithProgress(EditUserinfoDetailActivity.this,
                            "努力加载中...");
                }
                mDialog.show();
                CPControl.GetEditPasswordResult(oldPassword, newPassword, listener);
                break;
            case TYPE_NAME:
                String name = mNameEdt.getText().toString();
                String erroInfo = CheckInfo.checkName(name);
                if (erroInfo.equals("")) {
                    if (!name.equals(LoginInfo.getRealname())) {
                        mDialog.show();
                        CPControl.GetUpadeNameResult(name, listener);
                    } else {
                        UUToast.showUUToast(EditUserinfoDetailActivity.this, "信息修改成功");
                        finish();
                    }
                } else {
                    UUToast.showUUToast(EditUserinfoDetailActivity.this, erroInfo);
                }
                break;
        }
    }

    private OnCheckedChangeListener mOnCheckedChangeListener = new OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int id) {
            String sex = null;
            if (mSexRadio1.getId() == id) {
                sex = "1";
            } else if (mSexRadio2.getId() == id) {
                sex = "2";
            }else if (mSexRadio3.getId() == id){
            	sex = "3";
            }
            if (sex != null) {
                CPControl.GetUpadeGenderResult(sex, listener);
            }
        }
    };

    private String newData;

    private GetResultListCallback listener = new GetResultListCallback() {

        @Override
        public void onFinished(Object o) {
            newData = (String)o;
            Message msg = new Message();
            msg.what = 0;
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

    // 短信获取验证码的回调
    private GetResultListCallback listener1 = new GetResultListCallback() {

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

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (mDialog != null) {
                mDialog.dismiss();
            }
            BaseResponseInfo mBaseResponseInfo = null;
            switch (msg.what) {
                case 0:
                    UUToast.showUUToast(EditUserinfoDetailActivity.this, "信息修改成功");
                    int resultCode = 0;
                    if (type == TYPE_NAME) {
                        resultCode = EditUserinfoActivity.REQUESTCODE + 4;
                    } else if (type == TYPE_SEX) {
                        resultCode = EditUserinfoActivity.REQUESTCODE + 5;
                    } else if (type == TYPE_PASSWORD){
                        ActivityControl.onLogout(EditUserinfoDetailActivity.this);
                    }

                    if (newData != null) {
                        Intent mIntent = new Intent();
                        mIntent.putExtra(EditUserinfoActivity.NEWDATA, newData);
                        setResult(resultCode, mIntent);
                    }

                    finish();
                    break;

                case 1:
                    mBaseResponseInfo = (BaseResponseInfo)msg.obj;
                    if (mBaseResponseInfo != null && mBaseResponseInfo.getInfo() != null) {
                        UUToast.showUUToast(EditUserinfoDetailActivity.this,
                                mBaseResponseInfo.getInfo());
                    } else {
                        UUToast.showUUToast(EditUserinfoDetailActivity.this, "信息修改失败");
                    }

                    break;
                case 2:
                    UUToast.showUUToast(EditUserinfoDetailActivity.this, msg.obj.toString());
                    break;
            }
        };
    };

    public void OnImgLoadFinished(String url, android.graphics.Bitmap mBitmap) {
        super.OnImgLoadFinished(url, mBitmap);

        if (LoginInfo.getAvatar_img().equals(url)) {
            mHeadIconImg.setImageBitmap(mBitmap);
        }
    };

    @Override
    protected void onDestroy() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (task != null) {
            task.cancel();
            task = null;
        }
        super.onDestroy();
    }
}

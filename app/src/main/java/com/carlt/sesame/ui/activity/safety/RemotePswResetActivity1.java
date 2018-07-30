
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
import com.carlt.sesame.data.SesameLoginInfo;
import com.carlt.sesame.ui.activity.base.BaseActivity;
import com.carlt.sesame.ui.view.PopBoxCreat;
import com.carlt.sesame.ui.view.ValidateEditText;
import com.carlt.sesame.utility.UUToast;

/**
 * 忘记远程控制密码-step1实名认证
 * 
 * @author Administrator
 */
public class RemotePswResetActivity1 extends BaseActivity implements OnClickListener {
    private ImageView back;// 头部返回键

    private TextView title;// 标题文字

    private ValidateEditText mEdtName;// 姓名

    private ValidateEditText mEdtIDCard;// 身份证号

    private TextView mTxtSubtitle;// 小标题

    private TextView mBtnOption;// 操作按钮（"下一步","提交"）

    private View mViewAutherTodo;// 未实名认证

    private View mViewAuthered;// 已实名认证

    private Dialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_realname);

        initTitle();
        init();
    }

    private void initTitle() {
        back = (ImageView)findViewById(R.id.head_back_img1);
        title = (TextView)findViewById(R.id.head_back_txt1);

        title.setText("重置远程控制密码");

        back.setImageResource(R.drawable.arrow_back);
        back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void init() {
        mEdtName = (ValidateEditText)findViewById(R.id.realname_edt_name);
        mEdtIDCard = (ValidateEditText)findViewById(R.id.realname_edt_IDCard);

        mEdtName.setEditHint("请输入姓名");
        mEdtName.setmType(ValidateEditText.TYPE_CODE);
        mEdtName.setNextEditText(mEdtIDCard);
        
        mEdtIDCard.setEditHint("请输入身份证号码");
        mEdtIDCard.setmType(ValidateEditText.TYPE_IDN);
        
        mTxtSubtitle = (TextView)findViewById(R.id.realname_txt_title);
        mBtnOption = (TextView)findViewById(R.id.realname_txt_option);

        mViewAutherTodo = findViewById(R.id.realname_lay_authertodo);
        mViewAuthered = findViewById(R.id.realname_lay_authered);

        mViewAutherTodo.setVisibility(View.VISIBLE);
        mViewAuthered.setVisibility(View.GONE);

        mTxtSubtitle.setText("为了保证远程操作的安全性，请先进行身份验证");
        mBtnOption.setText("下一步");
        mBtnOption.setOnClickListener(this);
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
            switch (msg.what) {
                case 0:
                    // 认证成功
                    if (mDialog != null && mDialog.isShowing()) {
                        mDialog.dismiss();
                    }
                    BaseResponseInfo mInfo = (BaseResponseInfo)msg.obj;
                    if (mInfo != null) {
                        String info = mInfo.getInfo();
                        if (info != null && info.length() > 0) {
                            UUToast.showUUToast(RemotePswResetActivity1.this, info);
                        } else {
                            UUToast.showUUToast(RemotePswResetActivity1.this, "认证成功！");
                        }
                    } else {
                        UUToast.showUUToast(RemotePswResetActivity1.this, "认证成功！");
                    }
                    String name = mEdtName.getText().toString();
                    String idcard = mEdtIDCard.getText().toString();
                    Intent mIntent = new Intent(RemotePswResetActivity1.this,
                            RemotePswResetActivity2.class);
                    mIntent.putExtra(RemotePswResetActivity2.INFO_NAME, name);
                    mIntent.putExtra(RemotePswResetActivity2.INFO_IDCARD, idcard);
                    startActivity(mIntent);
                    finish();
                    break;

                case 1:
                    // 认证失败
                    if (mDialog != null && mDialog.isShowing()) {
                        mDialog.dismiss();
                    }
                    BaseResponseInfo mInfo1 = (BaseResponseInfo)msg.obj;
                    if (mInfo1 != null) {
                        String info = mInfo1.getInfo();
                        if (info != null && info.length() > 0) {
                            UUToast.showUUToast(RemotePswResetActivity1.this, info);
                        } else {
                            UUToast.showUUToast(RemotePswResetActivity1.this, "认证失败...");
                        }
                    } else {
                        UUToast.showUUToast(RemotePswResetActivity1.this, "认证失败...");
                    }
                    break;
            }
        }
    };

    @Override
    public void onClick(View v) {
    	mEdtName.validateEdit();
    	mEdtIDCard.validateEdit();
        // 调用实名认证接口
        String name = mEdtName.getText().toString();
        String idCard = mEdtIDCard.getText().toString();
        if (name == null || name.length() <= 0) {
            UUToast.showUUToast(RemotePswResetActivity1.this, "请输入您的真实姓名哦！");
            return;
        } else if (idCard == null || idCard.length() != 18) {
            UUToast.showUUToast(RemotePswResetActivity1.this, "请输入您的身份证信息哦！");
            return;
        } else {
            int lengthEdit = idCard.length();
            String idCardPrefix = idCard.substring(0, 6);
            String idCardSuffix = idCard.substring(lengthEdit - 2, lengthEdit);
            if (SesameLoginInfo.isAuthen()) {
                String authorName = SesameLoginInfo.getAuthen_name();
                String authorCard = SesameLoginInfo.getAuthen_card();
                int lengthAuthor = authorCard.length();
                String authorCardPrefix = authorCard.substring(0, 6);
                String authorCardSuffix = authorCard.substring(lengthAuthor - 2, lengthAuthor);
                // if (!name.equals(authorName)) {
                // UUToast.showUUToast(RemotePswResetActivity1.this,
                // "您输入的姓名与已认证的不符，请重新输入...");
                // return;
                // } else if (!CheckInfo.checkIdCard(idCard)) {
                // UUToast.showUUToast(RemotePswResetActivity1.this,
                // "请输入正确的身份证号...");
                // return;
                // } else if (!idCardPrefix.equals(authorCardPrefix)
                // || !idCardSuffix.equals(authorCardSuffix)) {
                // UUToast.showUUToast(RemotePswResetActivity1.this,
                // "您输入的身份证号与已认证的不符，请重新输入...");
                // return;
                // }

                if (name == null || name.equals("")) {
                    UUToast.showUUToast(RemotePswResetActivity1.this, "您还没有填写您的真实姓名哦...");
                    return;
                } else if (idCard == null || idCard.equals("")) {
                    UUToast.showUUToast(RemotePswResetActivity1.this, "您还没有填写您的身份证号码哦...");
                    return;
                } else {
                    if (mDialog == null) {
                        mDialog = PopBoxCreat.createDialogWithProgress(
                                RemotePswResetActivity1.this, "提交中...");
                    }
                    mDialog.show();
                    CPControl.GetAuthenticationResult(name, idCard, listener);
                }
            } else {
                if (mDialog == null) {
                    mDialog = PopBoxCreat.createDialogWithProgress(RemotePswResetActivity1.this,
                            "提交中...");
                }
                mDialog.show();
                CPControl.GetRealNameResult(name, idCard, listener);
            }
        }
    }

}

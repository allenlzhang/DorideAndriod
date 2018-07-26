
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
import com.carlt.sesame.control.ActivityControl;
import com.carlt.sesame.control.CPControl;
import com.carlt.sesame.control.CPControl.GetResultListCallback;
import com.carlt.sesame.data.BaseResponseInfo;
import com.carlt.sesame.data.safety.AuthorInfo;
import com.carlt.sesame.ui.SesameMainActivity;
import com.carlt.sesame.ui.activity.base.LoadingActivityWithTitle;
import com.carlt.sesame.ui.view.PopBoxCreat;
import com.carlt.sesame.utility.UUToast;

/**
 * 授权子设备页面
 * 
 * @author Administrator
 */
public class AuthorActivity extends LoadingActivityWithTitle implements OnClickListener {

    private ImageView back;// 头部返回键

    private TextView title;// 标题文字

    private TextView txtRight;// 头部右侧文字

    private ImageView mImgIcon;// 图标

    private TextView mTxtMobilename;// 子机名称

    private TextView mTxtDes;// 文字描述

    private TextView mTxtPermit;// 允许按钮

    private TextView mTxtRefuse;// 拒绝按钮

    private AuthorInfo mAuthorInfo;// 授权信息

    private Dialog mDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_author);
        setTitleView(R.layout.head_back);

        initTitle();
        init();
        // LoadData();
    }

    private void initTitle() {
        back = (ImageView)findViewById(R.id.head_back_img1);
        title = (TextView)findViewById(R.id.head_back_txt1);
        txtRight = (TextView)findViewById(R.id.head_back_txt2);

        title.setText("授权处理");
        txtRight.setVisibility(View.GONE);

        back.setImageResource(R.drawable.cross_bg);
        back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // 跳转至主页面
                Intent mIntent = new Intent(AuthorActivity.this, SesameMainActivity.class);
                startActivity(mIntent);
                finish();
            }
        });

    }

    private void init() {
        mImgIcon = (ImageView)findViewById(R.id.author_img);
        mTxtMobilename = (TextView)findViewById(R.id.author_txt_mobilename);
        mTxtDes = (TextView)findViewById(R.id.author_txt_des);
        mTxtPermit = (TextView)findViewById(R.id.author_txt_permit);
        mTxtRefuse = (TextView)findViewById(R.id.author_txt_refuse);

        mTxtPermit.setOnClickListener(this);
        mTxtRefuse.setOnClickListener(this);
    }

    @Override
    protected void LoadSuccess(Object data) {
        mAuthorInfo = (AuthorInfo)data;
        if (mAuthorInfo != null) {
            String mobileName = mAuthorInfo.getMobile_name();
            if (mobileName != null && mobileName.length() > 0) {
                mTxtMobilename.setText(mobileName + "提出授权请求");
            } else {
                // 拒绝授权-跳转至主页面
                Intent mIntent = new Intent(AuthorActivity.this, SesameMainActivity.class);
                startActivity(mIntent);
                finish();
            }
        } else {
            // 拒绝授权-跳转至主页面
            Intent mIntent = new Intent(AuthorActivity.this, SesameMainActivity.class);
            startActivity(mIntent);
            finish();
        }
        super.LoadSuccess(data);
    }

    @Override
    protected void LoadErro(Object erro) {
        super.LoadErro(erro);
    }

    @Override
    protected void LoadData() {
        super.LoadData();
        CPControl.GetAuthorizePageResult(listener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LoadData();
    }

    /**
     * 处理授权请求
     */
    private GetResultListCallback listener_deal = new GetResultListCallback() {

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
                    // 授权成功
                    BaseResponseInfo mInfo = (BaseResponseInfo)msg.obj;
                    if (mInfo != null) {
                        String info = mInfo.getInfo();
                        if (info != null && info.length() > 0) {
                            UUToast.showUUToast(AuthorActivity.this, info);
                        } else {
                            UUToast.showUUToast(AuthorActivity.this, "处理成功！");
                        }
                    } else {
                        UUToast.showUUToast(AuthorActivity.this, "处理成功！");
                    }
                    if (isallow != null) {
                        if (isallow.equals("1")) {
                            // 允许授权-走注销逻辑
                            ActivityControl.onLogout(AuthorActivity.this);
                        } else if (isallow.equals("2")) {
                            // 拒绝授权-跳转至主页面
                            Intent mIntent = new Intent(AuthorActivity.this, SesameMainActivity.class);
                            startActivity(mIntent);
                            finish();
                        }
                    }

                    break;

                case 1:
                    // 授权失败
                    BaseResponseInfo mInfo1 = (BaseResponseInfo)msg.obj;
                    if (mInfo1 != null) {
                        String info = mInfo1.getInfo();
                        if (info != null && info.length() > 0) {
                            UUToast.showUUToast(AuthorActivity.this, info);
                        } else {
                            UUToast.showUUToast(AuthorActivity.this, "处理失败...");
                        }
                    } else {
                        UUToast.showUUToast(AuthorActivity.this, "处理失败...");
                    }
                    break;
            }
        }

    };

    private String isallow = "";

    @Override
    public void onClick(View v) {
        String msgInfo="";
        switch (v.getId()) {
            case R.id.author_txt_permit:
                // 允许
                isallow = "1";
                msgInfo="授权中...";
                break;

            case R.id.author_txt_refuse:
                // 拒绝
                isallow = "2";
                msgInfo="处理中...";
                break;
        }
        String mobileId = mAuthorInfo.getMobile_id();
        if (mDialog == null) {
            mDialog = PopBoxCreat.createDialogWithProgress(AuthorActivity.this, msgInfo);
        }
        mDialog.show();
        CPControl.GetDealAuthorize(isallow, mobileId, listener_deal);
    }

}

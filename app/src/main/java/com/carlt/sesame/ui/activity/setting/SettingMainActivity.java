
package com.carlt.sesame.ui.activity.setting;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.carlt.doride.R;
import com.carlt.doride.http.retrofitnet.model.ContactsInfo;
import com.carlt.doride.http.retrofitnet.model.OtherInfo;
import com.carlt.doride.http.retrofitnet.model.UserInfo;
import com.carlt.doride.ui.activity.setting.AboutDorideActivity;
import com.carlt.sesame.control.ActivityControl;
import com.carlt.sesame.data.SesameLoginInfo;
import com.carlt.sesame.http.AsyncImageLoader;
import com.carlt.sesame.ui.SesameMainActivity;
import com.carlt.sesame.ui.activity.base.BaseActivity;
import com.carlt.sesame.ui.activity.safety.SafetyMainActivity;

/**
 * 设置页面
 * @author daisy
 */
public class SettingMainActivity extends BaseActivity {

    private View mView1;// 修改资料

    private View mView2;// 车辆管理

    private View mView3;// 设备管理

    private View mView4;// 消息管理

    private View mView5;// 违章管理

    private View mView6;// 关于芝麻

    private View mView7;// 客服热线

    private View mView9;// 版本介绍

    private View mViewSafety;// 安全中心

    private View mViewFee;// 续费管理

    private View mViewLine5Bg;// 通用管理下方横线的白色背景
    private View mViewLine5;// 通用管理下方横线

    private TextView mTxtName;// 用户名

    private TextView mTextView1;// 客服电话

    private TextView mTextView2;// 退出登录

    private ImageView mImgHead;// 用户头像

    private AsyncImageLoader mAsyncImageLoader = AsyncImageLoader.getInstance();

    private String phoneNum = "";// 客服电话

    private String phoneNumService;

    private ImageView mImgDot;// 有续费提醒时的红点儿

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sesame_activity_setting_main);
        init();
    }

    private void init() {

        mView1 = findViewById(R.id.activity_setting_layout1);
        mView2 = findViewById(R.id.activity_setting_layout2);
        mView3 = findViewById(R.id.activity_setting_layout3);
        mView4 = findViewById(R.id.activity_setting_layout4);
        mView5 = findViewById(R.id.activity_setting_layout5);
        mView6 = findViewById(R.id.activity_setting_layout6);
        mView7 = findViewById(R.id.activity_setting_layout7);
        mViewSafety = findViewById(R.id.activity_setting_layout_safety);
        mViewFee = findViewById(R.id.activity_setting_layout_fee);
        mView9 = findViewById(R.id.activity_setting_layout9);

        mViewLine5Bg = findViewById(R.id.line_h5_bg);
        mViewLine5 = findViewById(R.id.line_h5);

        mTxtName = (TextView) findViewById(R.id.activity_setting_txt_name);
        mTextView1 = (TextView) findViewById(R.id.activity_setting_txt1);
        mTextView2 = (TextView) findViewById(R.id.activity_setting_txt2);

        mImgHead = (ImageView) findViewById(R.id.activity_setting_img_head);
        mImgDot = (ImageView) findViewById(R.id.activity_setting_img_dot);

        if ("1".equals(SesameLoginInfo.getRemoteControl())) {
            findViewById(R.id.line_h4).setVisibility(View.VISIBLE);
            findViewById(R.id.activity_setting_layout5).setVisibility(View.VISIBLE);
        }

        mView1.setOnClickListener(mClickListener);
        mView2.setOnClickListener(mClickListener);
        mView3.setOnClickListener(mClickListener);
        mView4.setOnClickListener(mClickListener);
        mView5.setOnClickListener(mClickListener);
        mView6.setOnClickListener(mClickListener);
        mView7.setOnClickListener(mClickListener);
        mView9.setOnClickListener(mClickListener);
        mViewSafety.setOnClickListener(mClickListener);
        mViewFee.setOnClickListener(mClickListener);

        mViewFee.setVisibility(View.GONE);
        vLineAboutBg = findViewById(R.id.activity_setting_line_aboutbg);
        vLineAbout = findViewById(R.id.activity_setting_line_about);

        mTextView1.setText("");
        mTextView2.setOnClickListener(mClickListener);

    }

    private void loadData() {
        String name = UserInfo.getInstance().realName;
        if (name != null && name.length() > 0) {
            mTxtName.setText(name);
        }

        String imgUrl = UserInfo.getInstance().avatarFile;
        if (imgUrl != null && imgUrl.length() > 0) {
            Bitmap mDrawable = mAsyncImageLoader.getBitmapByUrl(imgUrl);
            if (mDrawable != null) {
                mImgHead.setImageBitmap(mDrawable);
            } else {
                mImgHead.setImageResource(R.drawable.icon_default_head);
            }
        } else {
            mImgHead.setImageResource(R.drawable.icon_default_head);
        }
        if (OtherInfo.getInstance().isMain()) {
            // mViewFee.setVisibility(View.VISIBLE);
            // mViewLine5Bg.setVisibility(View.VISIBLE);
            // mViewLine5.setVisibility(View.VISIBLE);

                mImgDot.setVisibility(View.GONE);
        } else {
            // mViewFee.setVisibility(View.GONE);
            mViewLine5Bg.setVisibility(View.GONE);
            mViewLine5.setVisibility(View.GONE);
        }

    }

    private OnClickListener mClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.activity_setting_layout1:
                    // 跳转至修改资料
                    Intent mIntent1 = new Intent(SettingMainActivity.this, EditUserinfoActivity.class);
                    startActivity(mIntent1);
                    break;
                case R.id.activity_setting_layout2:
                    // 跳转至车辆管理
                    Intent mIntent2 = new Intent(SettingMainActivity.this, ManageCarActivity.class);
                    startActivity(mIntent2);
                    break;
                case R.id.activity_setting_layout3:
                    // 跳转至消息管理
                    Intent mIntent4 = new Intent(SettingMainActivity.this, ManageMessageActivity.class);
                    startActivity(mIntent4);
                    break;
                case R.id.activity_setting_layout4:
                    // 跳转至设备管理-TODO
                    Intent mIntent = new Intent(context, ManageDeviceActivity.class);
                     startActivity(mIntent);
                    break;
                case R.id.activity_setting_layout5:
                    // 跳转至通用管理-TODO
                    Intent intent = new Intent(context, ManageCommonActivity.class);
                    startActivity(intent);
                    break;
                case R.id.activity_setting_layout6:
                    // 跳转至关于芝麻(点击后链接到 m.cheler.com)
                    // String url = "http://m.cheler.com";
                    // Uri uri = Uri.parse(url);
                    // Intent intent1 = new Intent(Intent.ACTION_VIEW, uri);
                    Intent intent1 = new Intent(SettingMainActivity.this, AboutDorideActivity.class);
                    startActivity(intent1);
                    break;
                case R.id.activity_setting_layout7:

                    Intent intent7 = new Intent(SettingMainActivity.this, ManageCustomerServiceActivity.class);
                    startActivity(intent7);
                    // 拨打客服电话
                    break;
                case R.id.activity_setting_layout9:
                    //                    版本介绍
//                    Intent intentUpdataDetail = new Intent(SettingMainActivity.this,
//                            UpdataDetailActivity.class);
//                    startActivity(intentUpdataDetail);
                    // 拨打客服电话
                    break;
                case R.id.activity_setting_txt2:
                    // 退出登录
                    ActivityControl.logout(SettingMainActivity.this);
                    break;

                case R.id.activity_setting_layout_safety:
                    // 跳转至安全中心
                    Intent intent8 = new Intent(SettingMainActivity.this, SafetyMainActivity.class);
                    startActivity(intent8);
                    break;
                case R.id.activity_setting_layout_fee:
                    // 跳转至续费管理
//                    Intent intent9 = new Intent(SettingMainActivity.this, ManageFeeActivity.class);
//                    startActivity(intent9);
                    break;
            }
        }
    };

    private View vLineAboutBg;

    private View vLineAbout;


    @Override
    protected void onStart() {
        super.onStart();
        loadData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SesameMainActivity.setDotVisiable();
        phoneNum = ContactsInfo.getInstance().salesHotLine;
        phoneNumService = ContactsInfo.getInstance().serviceHotLine;
        if (TextUtils.isEmpty(phoneNum) && TextUtils.isEmpty(phoneNumService)) {
            mView7.setVisibility(View.GONE);
            vLineAboutBg.setVisibility(View.GONE);
            vLineAbout.setVisibility(View.GONE);
        } else {
            mView7.setVisibility(View.VISIBLE);
            vLineAboutBg.setVisibility(View.VISIBLE);
            vLineAbout.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void OnImgLoadFinished(String url, Bitmap mBitmap) {
        super.OnImgLoadFinished(url, mBitmap);
        if (url.equals(UserInfo.getInstance().avatarFile) && mBitmap != null) {
            mImgHead.setImageBitmap(mBitmap);
        }
    }
}

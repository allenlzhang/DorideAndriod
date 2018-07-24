
package com.carlt.sesame.ui.activity.career;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.carlt.sesame.R;
import com.carlt.sesame.control.CPControl;
import com.carlt.sesame.data.LoginInfo;
import com.carlt.sesame.data.career.ChallengeInfo;
import com.carlt.sesame.data.career.ChallengeScore;
import com.carlt.sesame.http.AsyncImageLoader;
import com.carlt.sesame.map.ChallengeMapActivity;
import com.carlt.sesame.ui.activity.base.LoadingActivityWithTitle;

/**
 * 挑战-历史最佳成绩展示页面
 * 
 * @author daisy
 */
public class ChallengeHistoryBestActivity extends LoadingActivityWithTitle {
    private ImageView back;// 头部返回键

    private TextView title;// 标题文字

    private TextView txtRight;// 头部右侧文字

    // 最佳成绩区域

    private TextView mTextViewBest1;// 最佳成绩分数

    private TextView mTextViewBest2;// 最佳成绩单位

    private TextView mTextViewBest3;// 最佳成绩描述

    // 用户信息区域
    private ImageView mImageViewUserinfo1;// 用户头像

    private ImageView mImageViewUserinfo2;// 用户性别icon

    private ImageView mImageViewUserinfo3;// 用户座驾icon

    private TextView mTextViewUserinfo;// 用户姓名

    // 底部区域（两块）
    private TextView mTextViewBottom21;// 本次成绩战胜车主

    private TextView mTextViewBottom22;// 百米成绩约等于

    // 底部区域（三块）
    TextView mTextViewBottom31;// 行车时间

    TextView mTextViewBottom32;// 最高速度

    TextView mTextViewBottom33;// 平均速度

    // 底部区域（四块）

    private TextView mTextViewBottom41;// 急刹车

    private TextView mTextViewBottom42;// 急刹车次数

    private TextView mTextViewBottom43;// 急转弯

    private TextView mTextViewBottom44;// 急转弯次数

    private TextView mTextViewBottom45;// 急加速

    private TextView mTextViewBottom46;// 急加速次数

    private TextView mTextViewBottom47;// 高转速

    private TextView mTextViewBottom48;// 高转速次数

    private View mMainView;// 要添加的view

    private LinearLayout mMainLayout;// 中间布局

    private View mBottomSharing;// 底部分享控件

    private LayoutInflater mInflater;

    private ChallengeInfo mChallengeInfo;

    private ChallengeScore mChallengeScore;

    private AsyncImageLoader mAsyncImageLoader = AsyncImageLoader.getInstance();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge_finished);
        setTitleView(R.layout.head_back);

        mInflater = LayoutInflater.from(this);

        try {
            mChallengeInfo = (ChallengeInfo)getIntent().getSerializableExtra("ChallengeInfo");
        } catch (Exception e) {

        }

        initTitle();
        init();
        LoadData();
    }

    private void initTitle() {
        back = (ImageView)findViewById(R.id.head_back_img1);
        title = (TextView)findViewById(R.id.head_back_txt1);
        txtRight = (TextView)findViewById(R.id.head_back_txt2);

        back.setImageResource(R.drawable.arrow_back);

        title.setText("最佳成绩");

        txtRight.setText("挑战自己");
        txtRight.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (mChallengeInfo != null) {
                    Intent mIntent1 = new Intent(ChallengeHistoryBestActivity.this,
                            ChallengeMapActivity.class);
                    mIntent1.putExtra("ChallengeInfo", mChallengeInfo);
                    startActivity(mIntent1);
                    finish();
                }
            }
        });
        back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void init() {
        mMainLayout = (LinearLayout)findViewById(R.id.activity_challenge_finished_layout2);
        mBottomSharing = findViewById(R.id.activity_challenge_finished_button);

        mMainView = mInflater.inflate(R.layout.layout_challenge_best, null);
        mMainLayout.addView(mMainView);
        initBest();

        mMainView = mInflater.inflate(R.layout.layout_challenge_userinfo, null);
        mMainLayout.addView(mMainView);
        initUserinfo();

        if (mChallengeInfo != null) {
            switch (mChallengeInfo.getType()) {
                case ChallengeInfo.TYPE_ACCELERATE:
                    // 加速
                    mMainView = mInflater.inflate(R.layout.layout_challenge_bottom2, null);
                    mMainLayout.addView(mMainView);
                    initBottom2();
                    break;
                case ChallengeInfo.TYPE_SCORE:
                    // 评分
                    mMainView = mInflater.inflate(R.layout.layout_challenge_bottom4, null);
                    mMainLayout.addView(mMainView);
                    initBottom4();
                    break;
                case ChallengeInfo.TYPE_TIME:
                    // 省时
                    mMainView = mInflater.inflate(R.layout.layout_challenge_bottom3, null);
                    mMainLayout.addView(mMainView);
                    initBottom3();
                    break;
                case ChallengeInfo.TYPE_OIL:
                    // 油耗
                    mMainView = mInflater.inflate(R.layout.layout_challenge_bottom4, null);
                    mMainLayout.addView(mMainView);
                    initBottom4();
                    break;
            }
        }

        mBottomSharing.setOnClickListener(mClickListener);
    }

    // 初始化最佳成绩部分
    private void initBest() {
        mTextViewBest1 = (TextView)findViewById(R.id.layout_challenge_best_txt1);
        mTextViewBest2 = (TextView)findViewById(R.id.layout_challenge_best_txt2);
        mTextViewBest3 = (TextView)findViewById(R.id.layout_challenge_best_txt3);
    }

    // 初始化用户信息部分
    private void initUserinfo() {
        mImageViewUserinfo1 = (ImageView)findViewById(R.id.layout_challenge_userinfo_img1);
        mImageViewUserinfo2 = (ImageView)findViewById(R.id.layout_challenge_userinfo_img2);
        mImageViewUserinfo3 = (ImageView)findViewById(R.id.layout_challenge_userinfo_img3);

        mTextViewUserinfo = (TextView)findViewById(R.id.layout_challenge_userinfo_txt1);

        if (LoginInfo.getRealname() != null) {
            mTextViewUserinfo.setText(LoginInfo.getRealname());
        }

        // 用户头像
        if (LoginInfo.getAvatar_img() != null && LoginInfo.getAvatar_img().length() > 0) {
            if (mAsyncImageLoader.getBitmapByUrl(LoginInfo.getAvatar_img()) != null) {
                mImageViewUserinfo1.setImageBitmap(mAsyncImageLoader
                        .getBitmapByUrl(LoginInfo.getAvatar_img()));
            }
        } else {
            mImageViewUserinfo1.setImageResource(R.drawable.icon_default_head);
        }

        if (LoginInfo.getGender().equals(LoginInfo.GENDER_NAN)) {
            // 男
            mImageViewUserinfo2.setImageResource(R.drawable.icon_sex_male);
        } else if (LoginInfo.getGender().equals(LoginInfo.GENDER_NV)) {
            // 女
            mImageViewUserinfo2.setImageResource(R.drawable.icon_sex_female);
        } else {
            //保密
            mImageViewUserinfo2.setImageResource(R.drawable.icon_sex_secret);
        }

        // 用户车型icon
        if (LoginInfo.getCarlogo() != null && LoginInfo.getCarlogo().length() > 0) {
            if (mAsyncImageLoader.getBitmapByUrl(LoginInfo.getCarlogo()) != null) {
                mImageViewUserinfo3.setImageBitmap(mAsyncImageLoader
                        .getBitmapByUrl(LoginInfo.getCarlogo()));
            }
        } else {
            mImageViewUserinfo3.setImageResource(R.drawable.default_car_small);
        }
    }

    // 初始化底部部分（两块）
    private void initBottom2() {
        // 本次成绩战胜车主
        mTextViewBottom21 = (TextView)findViewById(R.id.layout_challenge_bottom2_txt1);
        // 百米成绩约等于
        mTextViewBottom22 = (TextView)findViewById(R.id.layout_challenge_bottom2_txt2);
    }

    // 初始化底部部分（三块）
    private void initBottom3() {
        // 行车时间
        mTextViewBottom31 = (TextView)findViewById(R.id.layout_challenge_bottom3_txt1);
        // 最高速度
        mTextViewBottom32 = (TextView)findViewById(R.id.layout_challenge_bottom3_txt2);
        // 平均速度
        mTextViewBottom33 = (TextView)findViewById(R.id.layout_challenge_bottom3_txt3);
    }

    // 初始化底部部分（四块）
    private void initBottom4() {
        mTextViewBottom41 = (TextView)findViewById(R.id.layout_challenge_bottom4_txt1);
        mTextViewBottom42 = (TextView)findViewById(R.id.layout_challenge_bottom4_txt2);
        mTextViewBottom43 = (TextView)findViewById(R.id.layout_challenge_bottom4_txt3);
        mTextViewBottom44 = (TextView)findViewById(R.id.layout_challenge_bottom4_txt4);
        mTextViewBottom45 = (TextView)findViewById(R.id.layout_challenge_bottom4_txt5);
        mTextViewBottom46 = (TextView)findViewById(R.id.layout_challenge_bottom4_txt6);
        mTextViewBottom47 = (TextView)findViewById(R.id.layout_challenge_bottom4_txt7);
        mTextViewBottom48 = (TextView)findViewById(R.id.layout_challenge_bottom4_txt8);
    }

    @Override
    protected void LoadData() {
        super.LoadData();
        if (mChallengeInfo != null) {
            CPControl.GetChallengeBestResult(mChallengeInfo.getId(), listener);
        }

    }

    @Override
    protected void LoadSuccess(Object data) {
        mChallengeScore = (ChallengeScore)data;

        if (mChallengeScore != null) {
            String mString;

            // 成绩
            mString = mChallengeScore.getScore();
            if (mString != null && mString.length() > 0) {
                mTextViewBest1.setText(mString);
            }

            // 成绩单位
            mString = mChallengeScore.getScoreunit();
            if (mString != null && mString.length() > 0) {
                mTextViewBest2.setText(mString);
            }

            // 挑战名称
            mString = mChallengeInfo.getName();
            if (mString != null && mString.length() > 0) {
                StringBuffer mStringBuffer = new StringBuffer();
                mStringBuffer.append("挑战");
                mStringBuffer.append(mString);
                mStringBuffer.append("成功");
                mTextViewBest3.setText(mStringBuffer.toString());
            }

            if (mChallengeInfo != null) {
                switch (mChallengeInfo.getType()) {
                    case ChallengeInfo.TYPE_ACCELERATE:
                        // 加速
                        // 本次成绩战胜车主
                        mString = mChallengeScore.getWinPercent();
                        if (mString != null && mString.length() > 0) {
                            mTextViewBottom21.setText(mString);
                        }

                        // 百米成绩约等于
                        mString = mChallengeScore.getSpeedLike();
                        if (mString != null && mString.length() > 0) {
                            mTextViewBottom22.setText(mString);
                        }
                        break;
                    case ChallengeInfo.TYPE_SCORE:
                        // 评分
                        // 急刹车
                        mString = mChallengeScore.getChDBBrake();
                        if (mString != null && mString.length() > 0) {
                            mTextViewBottom42.setText(mString);
                        }
                        // 急转弯
                        mString = mChallengeScore.getChDBTurn();
                        if (mString != null && mString.length() > 0) {
                            mTextViewBottom44.setText(mString);
                        }
                        // 急加速
                        mString = mChallengeScore.getChDBAcce();
                        if (mString != null && mString.length() > 0) {
                            mTextViewBottom46.setText(mString);
                        }
                        // 高转速
                        mString = mChallengeScore.getChDBHES();
                        if (mString != null && mString.length() > 0) {
                            mTextViewBottom48.setText(mString);
                        }
                        break;
                    case ChallengeInfo.TYPE_TIME:
                        // 省时
                        // 行车时间
                        mString = mChallengeScore.getTime();
                        if (mString != null && mString.length() > 0) {
                            mTextViewBottom31.setText(mString);
                        }

                        // 最高速度
                        mString = mChallengeScore.getMaxSpeed();
                        if (mString != null && mString.length() > 0) {
                            mTextViewBottom32.setText(mString);
                        }

                        // 平均速度
                        mString = mChallengeScore.getAvgSpeed();
                        if (mString != null && mString.length() > 0) {
                            mTextViewBottom33.setText(mString);
                        }
                        break;
                    case ChallengeInfo.TYPE_OIL:
                        // 油耗
                        // 急刹车
                        mString = mChallengeScore.getChDBBrake();
                        if (mString != null && mString.length() > 0) {
                            mTextViewBottom42.setText(mString);
                        }
                        // 急转弯
                        mString = mChallengeScore.getChDBTurn();
                        if (mString != null && mString.length() > 0) {
                            mTextViewBottom44.setText(mString);
                        }
                        // 急加速
                        mString = mChallengeScore.getChDBAcce();
                        if (mString != null && mString.length() > 0) {
                            mTextViewBottom46.setText(mString);
                        }
                        // 高转速
                        mString = mChallengeScore.getChDBHES();
                        if (mString != null && mString.length() > 0) {
                            mTextViewBottom48.setText(mString);
                        }
                        break;
                }
            }
        }
        super.LoadSuccess(data);
    }

    @Override
    protected void LoadErro(Object erro) {

        super.LoadErro(erro);
    }

    private OnClickListener mClickListener = new OnClickListener() {

        @Override
        public void onClick(View arg0) {
            // 分享按钮
//            ShareControl.share(ChallengeHistoryBestActivity.class,
//                    ChallengeHistoryBestActivity.this, mChallengeScore.getShareTitle(),
//                    mChallengeScore.getShareText(), mChallengeScore.getShareLink(), mMainLayout);

        }
    };

    @Override
    public void OnImgLoadFinished(String url, Bitmap mBitmap) {
        super.OnImgLoadFinished(url, mBitmap);
        if (url.equals(LoginInfo.getAvatar_img())) {
            mImageViewUserinfo1.setImageBitmap(mBitmap);
        } else if (url.equals(LoginInfo.getCarlogo())) {
            mImageViewUserinfo3.setImageBitmap(mBitmap);
        }
    }
}

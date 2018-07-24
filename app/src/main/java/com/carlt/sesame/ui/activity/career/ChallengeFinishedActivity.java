
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
import com.carlt.sesame.data.LoginInfo;
import com.carlt.sesame.data.career.ChallengeInfo;
import com.carlt.sesame.data.career.ChallengeScore;
import com.carlt.sesame.data.career.MedalInfo;
import com.carlt.sesame.data.career.RecordInfo;
import com.carlt.sesame.http.AsyncImageLoader;
import com.carlt.sesame.map.ChallengeMapActivity;
import com.carlt.sesame.ui.activity.base.LoadingActivityWithTitle;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 挑战结果页面
 * 
 * @author daisy
 */
public class ChallengeFinishedActivity extends LoadingActivityWithTitle implements OnClickListener {
    private ImageView back;// 头部返回键

    private TextView title;// 标题文字

    private TextView txtRight;// 头部右侧文字

    private TextView mTextError;// 数据出错显示的view

    // 副标题头
    private ImageView mImageViewSecretary;// 车秘书头像

    private TextView mTextViewSecretary;// 车秘书提示消息

    // 用户信息区域
    private ImageView mImageViewUserinfo1;// 用户头像

    private ImageView mImageViewUserinfo2;// 用户性别icon

    private ImageView mImageViewUserinfo3;// 用户座驾icon

    private TextView mTextViewUserinfo;// 用户姓名

    // 获得勋章区域
    private ImageView mImageViewMedal;// 勋章icon

    private TextView mTextViewMedal1;// 创纪录类别描述

    private TextView mTextViewMedal2;// 原数据

    private TextView mTextViewMedal3;// 新纪录数据

    private TextView mTextViewMedal4;// 勋章类别

    // view
    private View mSubHead;// 副标题

    private View mMainView;// 要添加的view

    private LinearLayout mMainLayout;// 中间布局

    private View mBottomSharing;// 底部分享控件

    private LayoutInflater mInflater;

    private ChallengeScore mChallengeScore;

    private ChallengeInfo mChallengeInfo;

    private ArrayList<MedalInfo> mArrayListMedal;// 勋章列表

    private ArrayList<RecordInfo> mArrayListRecord;// 创纪录列表

    private AsyncImageLoader mAsyncImageLoader = AsyncImageLoader.getInstance();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge_finished);
        setTitleView(R.layout.head_back);
        try {
            mChallengeInfo = (ChallengeInfo)getIntent().getSerializableExtra("ChallengeInfo");
            mChallengeScore = (ChallengeScore)getIntent().getSerializableExtra("ChallengeScore");
        } catch (Exception e) {

        }
        mTextError = (TextView)findViewById(R.id.activity_challenge_finished_error);
        initTitle();
        init();
        LoadData();

    }

    private void initTitle() {
        back = (ImageView)findViewById(R.id.head_back_img1);
        title = (TextView)findViewById(R.id.head_back_txt1);
        txtRight = (TextView)findViewById(R.id.head_back_txt2);

        back.setImageResource(R.drawable.arrow_back);

        title.setText("挑战结果");

        if (mChallengeScore.getStatus() == ChallengeScore.STATUS_INTERRUPT) {
            txtRight.setText("再来一次");
        } else {
            txtRight.setText("挑战自己");
        }
        txtRight.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (mChallengeInfo != null) {
                    Intent mIntent1 = new Intent(ChallengeFinishedActivity.this,
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

    private void initSubTitle() {
        mImageViewSecretary = (ImageView)findViewById(R.id.layout_sub_head_img);
        mTextViewSecretary = (TextView)findViewById(R.id.layout_sub_head_txt);

        mImageViewSecretary.setImageResource(LoginInfo.getSecretaryImg());
        if (mChallengeScore != null) {
            mTextError.setVisibility(View.GONE);
            switch (mChallengeScore.getStatus()) {
                case ChallengeScore.STATUS_INTERRUPT:
                    // 中断
                    String mString1 = "啊哦，挑战中断了！中断原因，手动中断或车辆熄火！";
                    mTextViewSecretary.setText(mString1);
                    break;
                case ChallengeScore.STATUS_SUCCESS:
                    // 结束且成功
                    StringBuffer mStringBuffer = new StringBuffer();
                    mStringBuffer.append("噢耶，挑战");
                    mStringBuffer.append(mChallengeScore.getName());
                    mStringBuffer.append("成功！+");
                    mStringBuffer.append(mChallengeScore.getCredit());
                    mStringBuffer.append("积分");
                    mTextViewSecretary.setText(mStringBuffer.toString());
                    break;
                case ChallengeScore.STATUS_ERRO:
                    // 结束但失败
                    String mString2 = "太遗憾了，本次挑战失败！别灰心哦";
                    mTextViewSecretary.setText(mString2);
                    break;
            }
        } else {
            mTextError.setVisibility(View.VISIBLE);
            mTextError.setText("哦噢，获取挑战结果出了点小问题...");
        }
    }

    private void init() {
        mInflater = LayoutInflater.from(this);
        mMainLayout = (LinearLayout)findViewById(R.id.activity_challenge_finished_layout2);

        mBottomSharing = findViewById(R.id.activity_challenge_finished_button);
        mBottomSharing.setOnClickListener(this);

        if (mChallengeScore != null) {
            mTextError.setVisibility(View.GONE);
            switch (mChallengeScore.getStatus()) {
                case ChallengeScore.STATUS_INTERRUPT:
                    // 2 挑战中断
                    mBottomSharing.setVisibility(View.INVISIBLE);

                    mSubHead = mInflater.inflate(R.layout.head_sub2, null);
                    mMainLayout.addView(mSubHead);
                    initSubTitle();

                    mMainView = mInflater.inflate(R.layout.layout_challenge_circle, null);
                    mMainLayout.addView(mMainView);
                    initCircle();
                    break;

                case ChallengeScore.STATUS_SUCCESS:
                    // 3 挑战结束且成功
                    initChallengeSuccess();
                    break;
                case ChallengeScore.STATUS_ERRO:
                    // 4 挑战结束但失败
                    initChallengeError();
                    break;
            }
        } else {
            mTextError.setVisibility(View.VISIBLE);
            mTextError.setText("哦噢，获取挑战结果出了点小问题...");
        }
    }

    // 初始化挑战结束且成功部分
    private void initChallengeSuccess() {
        mArrayListRecord = mChallengeScore.getmGotRecordList();
        mArrayListMedal = mChallengeScore.getmGotMedalLis();

        int sizeRecord = 0;
        int sizeMedal = 0;

        if (mArrayListRecord != null) {
            sizeRecord = mArrayListRecord.size();
        }
        if (mArrayListMedal != null) {
            sizeMedal = mArrayListMedal.size();
        }

        if (mChallengeInfo != null) {
            switch (mChallengeInfo.getType()) {
                case ChallengeInfo.TYPE_ACCELERATE:
                    // 成功-加速
                    mSubHead = mInflater.inflate(R.layout.head_sub1, null);
                    mMainLayout.addView(mSubHead);
                    initSubTitle();

                    mMainView = mInflater.inflate(R.layout.layout_challenge_circle, null);
                    mMainLayout.addView(mMainView);
                    initCircle();

                    mMainView = mInflater.inflate(R.layout.layout_challenge_userinfo, null);
                    mMainLayout.addView(mMainView);
                    initUserinfo();

                    if (sizeRecord > 0 || sizeMedal > 0) {
                        // 成功创纪录、得勋章
                        for (int i = 0; i < sizeRecord; i++) {
                            mMainView = getRecordView(i);
                            mMainLayout.addView(mMainView);
                        }

                        for (int i = 0; i < sizeMedal; i++) {
                            mMainView = getMedalView(i);
                            mMainLayout.addView(mMainView);
                        }
                    }

                    mMainView = mInflater.inflate(R.layout.layout_challenge_bottom2, null);
                    mMainLayout.addView(mMainView);
                    initBottom2();
                    break;

                case ChallengeInfo.TYPE_SCORE:
                    // 成功-评分
                    mSubHead = mInflater.inflate(R.layout.head_sub1, null);
                    mMainLayout.addView(mSubHead);
                    initSubTitle();

                    mMainView = mInflater.inflate(R.layout.layout_challenge_circle, null);
                    mMainLayout.addView(mMainView);
                    initCircle();

                    mMainView = mInflater.inflate(R.layout.layout_challenge_userinfo, null);
                    mMainLayout.addView(mMainView);
                    initUserinfo();

                    if (sizeRecord > 0 || sizeMedal > 0) {
                        // 成功创纪录、得勋章
                        for (int i = 0; i < sizeRecord; i++) {
                            mMainView = getRecordView(i);
                            mMainLayout.addView(mMainView);
                        }

                        for (int i = 0; i < sizeMedal; i++) {
                            mMainView = getMedalView(i);
                            mMainLayout.addView(mMainView);
                        }
                    }

                    mMainView = mInflater.inflate(R.layout.layout_challenge_bottom4, null);
                    mMainLayout.addView(mMainView);
                    initBottom4();
                    break;
                case ChallengeInfo.TYPE_TIME:
                    // 成功-省时
                    mSubHead = mInflater.inflate(R.layout.head_sub1, null);
                    mMainLayout.addView(mSubHead);
                    initSubTitle();

                    mMainView = mInflater.inflate(R.layout.layout_challenge_circle, null);
                    mMainLayout.addView(mMainView);
                    initCircle();

                    mMainView = mInflater.inflate(R.layout.layout_challenge_userinfo, null);
                    mMainLayout.addView(mMainView);
                    initUserinfo();

                    if (sizeRecord > 0 || sizeMedal > 0) {
                        // 成功创纪录、得勋章
                        for (int i = 0; i < sizeRecord; i++) {
                            mMainView = getRecordView(i);
                            mMainLayout.addView(mMainView);
                        }

                        for (int i = 0; i < sizeMedal; i++) {
                            mMainView = getMedalView(i);
                            mMainLayout.addView(mMainView);
                        }
                    }

                    mMainView = mInflater.inflate(R.layout.layout_challenge_bottom3, null);
                    mMainLayout.addView(mMainView);
                    initBottom3();
                    break;
                case ChallengeInfo.TYPE_OIL:
                    // 成功-油耗
                    mSubHead = mInflater.inflate(R.layout.head_sub1, null);
                    mMainLayout.addView(mSubHead);
                    initSubTitle();

                    mMainView = mInflater.inflate(R.layout.layout_challenge_circle, null);
                    mMainLayout.addView(mMainView);
                    initCircle();

                    mMainView = mInflater.inflate(R.layout.layout_challenge_userinfo, null);
                    mMainLayout.addView(mMainView);
                    initUserinfo();

                    if (sizeRecord > 0 || sizeMedal > 0) {
                        // 成功创纪录、得勋章
                        for (int i = 0; i < sizeRecord; i++) {
                            mMainView = getRecordView(i);
                            mMainLayout.addView(mMainView);
                        }

                        for (int i = 0; i < sizeMedal; i++) {
                            mMainView = getMedalView(i);
                            mMainLayout.addView(mMainView);
                        }
                    }

                    mMainView = mInflater.inflate(R.layout.layout_challenge_bottom4, null);
                    mMainLayout.addView(mMainView);
                    initBottom4();
                    break;
            }
        }
    }

    // 初始化挑战结束但失败
    private void initChallengeError() {
        if (mChallengeInfo != null) {
            switch (mChallengeInfo.getType()) {
                case ChallengeInfo.TYPE_ACCELERATE:
                    // 失败-加速
                    mSubHead = mInflater.inflate(R.layout.head_sub2, null);
                    mMainLayout.addView(mSubHead);
                    initSubTitle();

                    mMainView = mInflater.inflate(R.layout.layout_challenge_circle, null);
                    mMainLayout.addView(mMainView);
                    initCircle();

                    mMainView = mInflater.inflate(R.layout.layout_challenge_userinfo, null);
                    mMainLayout.addView(mMainView);
                    initUserinfo();

                    mMainView = mInflater.inflate(R.layout.layout_challenge_bottom2, null);
                    mMainLayout.addView(mMainView);
                    initBottom2();
                    break;

                case ChallengeInfo.TYPE_SCORE:
                    // 失败-评分
                    mSubHead = mInflater.inflate(R.layout.head_sub2, null);
                    mMainLayout.addView(mSubHead);
                    initSubTitle();

                    mMainView = mInflater.inflate(R.layout.layout_challenge_circle, null);
                    mMainLayout.addView(mMainView);
                    initCircle();

                    mMainView = mInflater.inflate(R.layout.layout_challenge_userinfo, null);
                    mMainLayout.addView(mMainView);
                    initUserinfo();

                    mMainView = mInflater.inflate(R.layout.layout_challenge_bottom4, null);
                    mMainLayout.addView(mMainView);
                    initBottom4();
                    break;
                case ChallengeInfo.TYPE_TIME:
                    // 失败-省时
                    mSubHead = mInflater.inflate(R.layout.head_sub2, null);
                    mMainLayout.addView(mSubHead);
                    initSubTitle();

                    mMainView = mInflater.inflate(R.layout.layout_challenge_circle, null);
                    mMainLayout.addView(mMainView);
                    initCircle();

                    mMainView = mInflater.inflate(R.layout.layout_challenge_userinfo, null);
                    mMainLayout.addView(mMainView);
                    initUserinfo();

                    mMainView = mInflater.inflate(R.layout.layout_challenge_bottom3, null);
                    mMainLayout.addView(mMainView);
                    initBottom3();
                    break;
                case ChallengeInfo.TYPE_OIL:
                    // 失败-油耗
                    mSubHead = mInflater.inflate(R.layout.head_sub2, null);
                    mMainLayout.addView(mSubHead);
                    initSubTitle();

                    mMainView = mInflater.inflate(R.layout.layout_challenge_circle, null);
                    mMainLayout.addView(mMainView);
                    initCircle();

                    mMainView = mInflater.inflate(R.layout.layout_challenge_userinfo, null);
                    mMainLayout.addView(mMainView);
                    initUserinfo();

                    mMainView = mInflater.inflate(R.layout.layout_challenge_bottom4, null);
                    mMainLayout.addView(mMainView);
                    initBottom4();
                    break;
            }
        }
    }

    // 初始化成绩圆圈部分
    private void initCircle() {
        // 成绩圆圈背景
        ImageView mImageViewCircle = (ImageView)findViewById(R.id.layout_challenge_circle_img);
        // 成绩类型
        TextView mTextViewCircle1 = (TextView)findViewById(R.id.layout_challenge_circle_txt1);
        // 成绩分数
        TextView mTextViewCircle2 = (TextView)findViewById(R.id.layout_challenge_circle_txt2);
        // 成绩描述
        TextView mTextViewCircle3 = (TextView)findViewById(R.id.layout_challenge_circle_txt3);

        View mViewCircleLine = findViewById(R.id.layout_challenge_circle_line);

        switch (mChallengeScore.getStatus()) {
            case ChallengeScore.STATUS_INTERRUPT:
                // 挑战中断
                mImageViewCircle.setImageResource(R.drawable.challenge_unfinished_bg);
                mViewCircleLine.setVisibility(View.GONE);

                mTextViewCircle1.setText("挑战中断");
                mTextViewCircle1.setTextSize(this.getResources().getDimension(
                        R.dimen.text_size_small_s2));
                mTextViewCircle1.setTextColor(this.getResources()
                        .getColor(R.color.text_color_gray3));

                mTextViewCircle2.setText("无法生成结果");
                mTextViewCircle2.setTextSize(16);
                mTextViewCircle2.setTextColor(this.getResources()
                        .getColor(R.color.text_color_gray1));
                break;

            case ChallengeScore.STATUS_SUCCESS:
                // 完成且成功
                mImageViewCircle.setImageResource(R.drawable.challenge_finished_success);
                String score1 = mChallengeScore.getScore();
                if (score1 != null && score1.length() > 0) {
                    mTextViewCircle2.setText(score1);
                }
                if (mChallengeInfo.getType() == ChallengeInfo.TYPE_SCORE) {
                    // 成功-评分
                    String pointdesc = mChallengeScore.getPointdesc();
                    if (pointdesc != null && pointdesc.length() > 0) {
                        mTextViewCircle3.setVisibility(View.VISIBLE);
                        mTextViewCircle3.setText(pointdesc);
                    }
                }

                break;
            case ChallengeScore.STATUS_ERRO:
                // 完成但失败
                mImageViewCircle.setImageResource(R.drawable.challenge_finished_fault);
                String score2 = mChallengeScore.getScore();
                if (score2 != null && score2.length() > 0) {
                    mTextViewCircle2.setText(score2);
                }
                break;
        }
    }

    // 初始化用户信息部分
    private void initUserinfo() {
        // 用户头像
        mImageViewUserinfo1 = (ImageView)findViewById(R.id.layout_challenge_userinfo_img1);
        // 用户性别
        mImageViewUserinfo2 = (ImageView)findViewById(R.id.layout_challenge_userinfo_img2);
        // 用户座驾icon
        mImageViewUserinfo3 = (ImageView)findViewById(R.id.layout_challenge_userinfo_img3);
        // 用户姓名
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
        }else{
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
        TextView mTextViewBottom21 = (TextView)findViewById(R.id.layout_challenge_bottom2_txt1);
        // 百米成绩约等于
        TextView mTextViewBottom22 = (TextView)findViewById(R.id.layout_challenge_bottom2_txt2);

        String winPercent = mChallengeScore.getWinPercent();
        String speedLike = mChallengeScore.getSpeedLike();

        if (winPercent != null && winPercent.length() > 0) {
            mTextViewBottom21.setText(winPercent);
        }
        if (speedLike != null && speedLike.length() > 0) {
            mTextViewBottom22.setText(speedLike);
        }
    }

    // 初始化底部部分（三块）
    private void initBottom3() {
        // 行车时间
        TextView mTextViewBottom31 = (TextView)findViewById(R.id.layout_challenge_bottom3_txt1);
        // 最高速度
        TextView mTextViewBottom32 = (TextView)findViewById(R.id.layout_challenge_bottom3_txt2);
        // 平均速度
        TextView mTextViewBottom33 = (TextView)findViewById(R.id.layout_challenge_bottom3_txt3);

        String time = mChallengeScore.getTime();
        String maxSpeed = mChallengeScore.getMaxSpeed();
        String avgSpeed = mChallengeScore.getAvgSpeed();

        if (time != null && time.length() > 0) {
            mTextViewBottom31.setText(time);
        }
        if (maxSpeed != null && maxSpeed.length() > 0) {
            mTextViewBottom32.setText(maxSpeed);
        }
        if (avgSpeed != null && avgSpeed.length() > 0) {
            mTextViewBottom33.setText(avgSpeed);
        }
    }

    // 初始化底部部分（四块）
    private void initBottom4() {
        // 急刹车
        TextView mTextViewBottom41 = (TextView)findViewById(R.id.layout_challenge_bottom4_txt1);
        // 急刹车次数
        TextView mTextViewBottom42 = (TextView)findViewById(R.id.layout_challenge_bottom4_txt2);
        // 急转弯
        TextView mTextViewBottom43 = (TextView)findViewById(R.id.layout_challenge_bottom4_txt3);
        // 急转弯次数
        TextView mTextViewBottom44 = (TextView)findViewById(R.id.layout_challenge_bottom4_txt4);
        // 急加速
        TextView mTextViewBottom45 = (TextView)findViewById(R.id.layout_challenge_bottom4_txt5);
        // 急加速次数
        TextView mTextViewBottom46 = (TextView)findViewById(R.id.layout_challenge_bottom4_txt6);
        // 高转速
        TextView mTextViewBottom47 = (TextView)findViewById(R.id.layout_challenge_bottom4_txt7);
        // 高转速次数
        TextView mTextViewBottom48 = (TextView)findViewById(R.id.layout_challenge_bottom4_txt8);

        if (mChallengeScore != null) {
            // 急刹车
            String brake = mChallengeScore.getChDBBrake();
            String turn = mChallengeScore.getChDBTurn();
            String acce = mChallengeScore.getChDBAcce();
            String hes = mChallengeScore.getChDBHES();

            if (brake != null && brake.length() > 0) {
                mTextViewBottom42.setText(brake);
            }
            // 急转弯
            if (turn != null && turn.length() > 0) {
                mTextViewBottom44.setText(turn);
            }
            // 急加速
            if (acce != null && acce.length() > 0) {
                mTextViewBottom46.setText(acce);
            }
            // 高转速
            if (hes != null && hes.length() > 0) {
                mTextViewBottom48.setText(hes);
            }
        }

    }

    // 初始化纪录部分
    private View getRecordView(int positon) {
        View recordView = mInflater.inflate(R.layout.layout_challenge_record, null);

        TextView mTextViewRecord1 = (TextView)recordView
                .findViewById(R.id.layout_challenge_record_txt1);
        TextView mTextViewRecord2 = (TextView)recordView
                .findViewById(R.id.layout_challenge_record_txt2);
        TextView mTextViewRecord3 = (TextView)recordView
                .findViewById(R.id.layout_challenge_record_txt3);
        TextView mTextViewRecord4 = (TextView)recordView
                .findViewById(R.id.layout_challenge_record_txt4);
        TextView mTextViewRecord5 = (TextView)recordView
                .findViewById(R.id.layout_challenge_record_txt5);

        RecordInfo recordinfo = mArrayListRecord.get(positon);

        String string;
        // 纪录名
        string = recordinfo.getRecordname();
        if (string != null && string.length() > 0) {
            mTextViewRecord1.setText(string);
        }

        // 旧成绩
        string = recordinfo.getOldvalue();
        if (string != null && string.length() > 0) {
            mTextViewRecord2.setText(string);
        }

        // 旧成绩单位
        string = recordinfo.getUnit();
        if (string != null && string.length() > 0) {
            mTextViewRecord3.setText(string);
        }

        // 新成绩
        string = recordinfo.getNewvalue();
        if (string != null && string.length() > 0) {
            mTextViewRecord4.setText(string);
        }

        // 新成绩单位
        string = recordinfo.getUnit();
        if (string != null && string.length() > 0) {
            mTextViewRecord5.setText(string);
        }

        return recordView;
    }

    // 初始化勋章部分
    private HashMap<String, ImageView> mMapMedal = new HashMap<String, ImageView>();

    private View getMedalView(int positon) {
        View medalView = mInflater.inflate(R.layout.layout_challenge_medal, null);

        ImageView mImageViewMedal = (ImageView)medalView
                .findViewById(R.id.layout_challenge_medal_img);

        TextView mTextViewMedal1 = (TextView)medalView
                .findViewById(R.id.layout_challenge_medal_txt1);

        MedalInfo medalInfo = mArrayListMedal.get(positon);
        String string;
        string = medalInfo.getIconUrl2();
        if (string != null && string.length() > 0) {
            if (mAsyncImageLoader.getBitmapByUrl(string) != null) {
                mImageViewMedal.setImageBitmap(mAsyncImageLoader.getBitmapByUrl(string));
            }
        } else {
            mImageViewMedal.setImageResource(R.drawable.icon_default_medal);
        }
        mMapMedal.put(string, mImageViewMedal);

        // 勋章名称
        string = medalInfo.getName();
        if (string != null && string.length() > 0) {
            mTextViewMedal1.setText(string);
        }

        return medalView;

    }

    @Override
    protected void LoadSuccess(Object data) {
        super.LoadSuccess(data);
    }

    @Override
    protected void LoadErro(Object erro) {

        super.LoadErro(erro);
    }

    @Override
    protected void LoadData() {
        super.LoadData();
        LoadSuccess(null);
    }

    @Override
    public void onClick(View v) {
        // 分享按钮
//        ShareControl.share(ChallengeFinishedActivity.class, ChallengeFinishedActivity.this,
//                mChallengeScore.getShareTitle(), mChallengeScore.getShareText(),
//                mChallengeScore.getShareLink(), mMainLayout);
    }

    @Override
    public void OnImgLoadFinished(String url, Bitmap mBitmap) {
        super.OnImgLoadFinished(url, mBitmap);
        if (url.equals(LoginInfo.getAvatar_img())) {
            mImageViewUserinfo1.setImageBitmap(mBitmap);
        } else if (url.equals(LoginInfo.getCarlogo())) {
            mImageViewUserinfo3.setImageBitmap(mBitmap);
        } else if (mMapMedal.get(url) != null) {
            ImageView mImgMedal = mMapMedal.get(url);
            mImgMedal.setImageBitmap(mBitmap);
        }
    }
}

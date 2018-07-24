
package com.carlt.sesame.ui.activity.career.report.day;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.carlt.sesame.R;
import com.carlt.sesame.data.LoginInfo;
import com.carlt.sesame.data.career.ReportDayInfo;
import com.carlt.sesame.http.AsyncImageLoader;
import com.carlt.sesame.ui.activity.career.report.BaseFragment;
import com.carlt.sesame.ui.activity.career.report.ReportDayLogActivity;
import com.carlt.sesame.utility.GetTypeFace;
import com.carlt.sesame.utility.MyParse;

public class ReportDayFragment1 extends BaseFragment {

    private TextView mTextView1;// 用户姓名

    private TextView mTextView2;// 用户当天行驶次数

    private TextView mTextView4;// 分数

    private TextView mTextView5;// 文字说明

    private TextView mTextViewBest;// 最佳

    private ImageView mImageView1;// 用户头像

    private ImageView mImageView2;// 用户性别

    private ImageView mImageView3;// 用户车型

    private ImageView mImageView4;// 向下指示箭头

    private View convertView;

    private AsyncImageLoader mAsyncImageLoader;

    private TranslateAnimation mAnimation;// 箭头上下移动动画

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        convertView = inflater.inflate(R.layout.layout_report_day1, null);
        mTextView1 = (TextView)convertView.findViewById(R.id.layout_report_day1_txt1);
        mTextView2 = (TextView)convertView.findViewById(R.id.layout_report_day1_txt2);
        mTextView4 = (TextView)convertView.findViewById(R.id.layout_report_day1_txt4);
        mTextView5 = (TextView)convertView.findViewById(R.id.layout_report_day1_txt5);
        mTextViewBest = (TextView)convertView.findViewById(R.id.layout_report_day1_txt_best);

        mImageView1 = (ImageView)convertView.findViewById(R.id.layout_report_day1_img1);
        mImageView2 = (ImageView)convertView.findViewById(R.id.layout_report_day1_img2);
        mImageView3 = (ImageView)convertView.findViewById(R.id.layout_report_day1_img3);
        mImageView4 = (ImageView)convertView.findViewById(R.id.layout_report_day1_img_next);

        mAsyncImageLoader = AsyncImageLoader.getInstance();

        mAnimation = new TranslateAnimation(0, 0, 10, 0);
        mAnimation.setDuration(500);
        mAnimation.setRepeatCount(-1);
        mAnimation.setRepeatMode(TranslateAnimation.REVERSE);

        LoadDate();
        return convertView;
    }

    private OnClickListener mClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            // 跳转到行车日志
            Intent mIntent = new Intent(getActivity(), ReportDayLogActivity.class);
            mIntent.putExtra(ReportDayLogActivity.DAY_LOG_DATE, mReportDayInfo.getReportDate());
            startActivity(mIntent);
        }
    };

    private ReportDayInfo mReportDayInfo;

    @Override
    protected void LoadDate() {
        try {

            mTextView1.setText(LoginInfo.getRealname());

            if (mReportDayInfo.getRuntimes() > 0) {
                mTextView2.setText("今日共行驶" + mReportDayInfo.getRuntimes() + "次");
                mTextView2.setClickable(true);
                mTextView2.setOnClickListener(mClickListener);
            } else {
                mTextView2.setText("今天您还没有开车哦");
                mTextView2.setClickable(false);
            }

            mTextView4.setText(mReportDayInfo.getPoint() + "");
            mTextView4.setTypeface(GetTypeFace.typefaceBold(null));
            mTextView4.setTextColor(MyParse.getColorByPoint(mReportDayInfo.getPoint()));

            String best = mReportDayInfo.getBest();
            if (best != null && best.length() > 0) {
                mTextViewBest.setVisibility(View.VISIBLE);
                mTextViewBest.setText(best);
            } else {
                mTextViewBest.setVisibility(View.GONE);
            }

            String pointdesc = mReportDayInfo.getPointdesc();
            if (pointdesc != null && pointdesc.length() > 0) {
                mTextView5.setText(pointdesc);
            }
            if (!LoginInfo.getAvatar_img().equals("")) {
                if (mAsyncImageLoader.getBitmapByUrl(LoginInfo.getAvatar_img()) != null) {
                    mImageView1.setImageBitmap(mAsyncImageLoader
                            .getBitmapByUrl(LoginInfo.getAvatar_img()));
                }
            } else {
                mImageView1.setImageResource(R.drawable.icon_default_head);
            }
            if (LoginInfo.getGender().equals(LoginInfo.GENDER_NAN)) {
                mImageView2.setImageResource(R.drawable.icon_sex_male);
            } else if (LoginInfo.getGender().equals(LoginInfo.GENDER_NV)) {
                mImageView2.setImageResource(R.drawable.icon_sex_female);
            } else {
                mImageView2.setImageResource(R.drawable.icon_sex_secret);
            }

            if (!LoginInfo.getCarlogo().equals("")) {
                if (mAsyncImageLoader.getBitmapByUrl(LoginInfo.getCarlogo()) != null) {
                    mImageView3.setImageBitmap(mAsyncImageLoader.getBitmapByUrl(LoginInfo.getCarlogo()));
                }
            } else {
                mImageView3.setImageResource(R.drawable.default_car_small);
            }

        } catch (Exception e) {
            // Log.e("info", getClass() + "==" + e);
        }

    }

    @Override
    public void refreshImage(String url, Bitmap mBitmap) {
        super.refreshImage(url, mBitmap);
        if (url.equals(LoginInfo.getAvatar_img())) {
            mImageView1.setImageBitmap(mBitmap);
        } else if (url.equals(LoginInfo.getCarlogo())) {
            mImageView3.setImageBitmap(mBitmap);
        }
    }

    @Override
    public void setData(Object mInfo) {
        mReportDayInfo = (ReportDayInfo)mInfo;
        LoadDate();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAnimation != null) {
            mImageView4.setAnimation(mAnimation);
            mImageView4.startAnimation(mAnimation);
        }
    }

    @Override
    public void onPause() {
        if (mImageView4.getAnimation() != null) {
            mImageView4.clearAnimation();
        }
        super.onPause();
    }

    @Override
    public void onStop() {
        if (mImageView4.getAnimation() != null) {
            mImageView4.clearAnimation();
        }
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        if (mImageView4.getAnimation() != null) {
            mImageView4.clearAnimation();
        }
        super.onDestroyView();
    }

}

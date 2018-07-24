
package com.carlt.sesame.ui.activity.career.report.month;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.carlt.sesame.R;
import com.carlt.sesame.data.LoginInfo;
import com.carlt.sesame.data.career.ReportMonthInfo;
import com.carlt.sesame.http.AsyncImageLoader;
import com.carlt.sesame.ui.activity.career.report.BaseFragment;
import com.carlt.sesame.utility.GetTypeFace;
import com.carlt.sesame.utility.MyParse;

public class ReportMonthFragment1 extends BaseFragment {
    private TextView mTextView1;// 用户姓名

    private TextView mTextView2;// 月份

    private TextView mTextView3;// 当月平均驾驶得分

    private TextView mTextView4;// 文字说明

    private TextView mTextView5;// 平均得分较上月变化

    private ImageView mImageView1;// 用户头像

    private ImageView mImageView2;// 用户性别

    private ImageView mImageView3;// 用户车型

    private ImageView mImageView4;// 分页滑动提示按钮

    private AsyncImageLoader mAsyncImageLoader;

    private TranslateAnimation mAnimation;// 箭头上下移动动画

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View convertView = inflater.inflate(R.layout.layout_report_month1, null);

        mTextView1 = (TextView)convertView.findViewById(R.id.layout_report_month1_txt1);
        mTextView2 = (TextView)convertView.findViewById(R.id.layout_report_month1_txt2);
        mTextView3 = (TextView)convertView.findViewById(R.id.layout_report_month1_txt3);
        mTextView4 = (TextView)convertView.findViewById(R.id.layout_report_month1_txt4);
        mTextView5 = (TextView)convertView.findViewById(R.id.layout_report_month1_txt5);

        mImageView1 = (ImageView)convertView.findViewById(R.id.layout_report_month1_img1);
        mImageView2 = (ImageView)convertView.findViewById(R.id.layout_report_month1_img2);
        mImageView3 = (ImageView)convertView.findViewById(R.id.layout_report_month1_img3);
        mImageView4 = (ImageView)convertView.findViewById(R.id.layout_report_month1_img_next);

        mAsyncImageLoader = AsyncImageLoader.getInstance();

        mAnimation = new TranslateAnimation(0, 0, 10, 0);
        mAnimation.setDuration(500);
        mAnimation.setRepeatCount(-1);
        mAnimation.setRepeatMode(TranslateAnimation.REVERSE);

        LoadDate();
        return convertView;
    }

    private ReportMonthInfo mReportMonthInfo;

    @Override
    public void LoadDate() {
        try {
            mTextView1.setText(LoginInfo.getRealname());
            if (null != mReportMonthInfo.getReportDate()
                    && !mReportMonthInfo.getReportDate().equals("")) {
                String[] s1 = mReportMonthInfo.getReportDate().split("-");
                StringBuffer mStringBuffer = new StringBuffer();
                if (s1.length > 1) {
                    mStringBuffer.append(s1[0]);
                    mStringBuffer.append("年");
                    mStringBuffer.append(s1[1]);
                    mStringBuffer.append("月行车报告");
                    mTextView2.setText(mStringBuffer.toString());
                } else {
                    mTextView2.setText("月行车报告");
                }

            }

            mTextView3.setText(mReportMonthInfo.getAvgPoint() + "");
            mTextView3.setTypeface(GetTypeFace.typefaceBold(null));
            mTextView3.setTextColor(MyParse.getColorByPoint(mReportMonthInfo.getAvgPoint()));

            if (mReportMonthInfo.getAvgPointcompare() > 0) {
                mTextView4.setText("车技较上月提升");
                mTextView5.setText(mReportMonthInfo.getAvgPointcompare() + "%");
                mTextView5.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_score_up,
                        0);
            } else if (mReportMonthInfo.getAvgPointcompare() == 0) {
                mTextView4.setText("您的水平很稳定");
            } else if (mReportMonthInfo.getAvgPointcompare() == 0) {
                mTextView4.setText("车技较上月下降");
                mTextView5.setText(mReportMonthInfo.getAvgPointcompare() + "%");
                mTextView5.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                        R.drawable.arrow_score_down, 0);
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
            }else{
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
    public void setData(Object o) {
        mReportMonthInfo = (ReportMonthInfo)o;
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

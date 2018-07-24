
package com.carlt.sesame.ui.activity.career.report.week;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.carlt.sesame.R;
import com.carlt.sesame.data.LoginInfo;
import com.carlt.sesame.data.career.ReportWeekChatInfo;
import com.carlt.sesame.data.career.ReportWeekInfo;
import com.carlt.sesame.http.AsyncImageLoader;
import com.carlt.sesame.ui.activity.career.report.BaseFragment;
import com.carlt.sesame.ui.activity.career.report.ReportChartView;
import com.carlt.sesame.utility.GetTypeFace;
import com.carlt.sesame.utility.MyParse;
import com.carlt.sesame.utility.MyTimeUtil;

import java.util.ArrayList;

public class ReportWeekFragment1 extends BaseFragment {

    private TextView mTextView1;// 用户姓名

    private TextView mTextView2;// 与上月比较的变化，增加为正，减少为负，值百分比，如20

    private TextView mTextView3;// 日期

    private TextView mTextView4;// 分数

    private TextView mTextView5;// 文字说明

    private ImageView mImageView1;// 用户头像

    private ImageView mImageView2;// 用户性别

    private ImageView mImageView3;// 用户车型

    private ImageView mImageView4;// 向下指示箭头

    private View mLineH;// 折线图和上方视图的分割线

    private ReportChartView mReportChartView;// 图表视图

    private AsyncImageLoader mAsyncImageLoader;

    private TranslateAnimation mAnimation;// 箭头上下移动动画

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View convertView = inflater.inflate(R.layout.layout_report_week1, null);

        mTextView1 = (TextView)convertView.findViewById(R.id.layout_report_week1_txt1);
        mTextView2 = (TextView)convertView.findViewById(R.id.layout_report_week1_txt2);
        mTextView3 = (TextView)convertView.findViewById(R.id.layout_report_week1_txt3);
        mTextView4 = (TextView)convertView.findViewById(R.id.layout_report_week1_txt4);
        mTextView5 = (TextView)convertView.findViewById(R.id.layout_report_week1_txt5);

        mImageView1 = (ImageView)convertView.findViewById(R.id.layout_report_week1_img1);
        mImageView2 = (ImageView)convertView.findViewById(R.id.layout_report_week1_img2);
        mImageView3 = (ImageView)convertView.findViewById(R.id.layout_report_week1_img3);
        mImageView4 = (ImageView)convertView.findViewById(R.id.layout_report_week1_img_next);

        mLineH = convertView.findViewById(R.id.layout_report_week1_line_h);

        mReportChartView = (ReportChartView)convertView
                .findViewById(R.id.layout_report_week1_reportchartview);

        mAsyncImageLoader = AsyncImageLoader.getInstance();

        mAnimation = new TranslateAnimation(0, 0, 10, 0);
        mAnimation.setDuration(500);
        mAnimation.setRepeatCount(-1);
        mAnimation.setRepeatMode(TranslateAnimation.REVERSE);

        LoadDate();
        return convertView;
    }

    private ReportWeekInfo mReportWeekInfo;

    ArrayList<ReportWeekChatInfo> dataList;

    @Override
    public void LoadDate() {
        try {
            mTextView1.setText(LoginInfo.getRealname());
            if (mReportWeekInfo.getAvgpointcompare() > 0)
                mTextView2.setText("车技较上月提升" + mReportWeekInfo.getAvgpointcompare() + "%");
            else if (mReportWeekInfo.getAvgpointcompare() == 0)
                mTextView2.setText("您的水平很稳定");
            else if (mReportWeekInfo.getAvgpointcompare() < 0)
                mTextView2.setText("车技较上月下降" + -mReportWeekInfo.getAvgpointcompare() + "%");

            String reportDate = mReportWeekInfo.getReportDate();
            if (reportDate != null && reportDate.length() > 0) {
                // 该周第一天的日期
                String firstdayOfWeek = MyTimeUtil.getFirstdayOfWeek(reportDate);
                // 该周最后一天的日期
                String lastdayOfWeek = MyTimeUtil.getLastDayOfWeek(reportDate);
                String[] strings;
                if (firstdayOfWeek != null && firstdayOfWeek.length() > 0) {
                    strings = firstdayOfWeek.split("-");
                    if (strings != null && strings.length > 2) {
                        StringBuffer mStringBuffer1 = new StringBuffer();
                        mStringBuffer1.append(strings[1]);
                        mStringBuffer1.append("-");
                        mStringBuffer1.append(strings[2]);

                        firstdayOfWeek = mStringBuffer1.toString();
                    }
                }
                if (lastdayOfWeek != null && lastdayOfWeek.length() > 0) {
                    strings = lastdayOfWeek.split("-");
                    if (strings != null && strings.length > 2) {
                        StringBuffer mStringBuffer2 = new StringBuffer();
                        mStringBuffer2.append(strings[1]);
                        mStringBuffer2.append("-");
                        mStringBuffer2.append(strings[2]);

                        lastdayOfWeek = mStringBuffer2.toString();
                    }
                }

                StringBuffer mStringBuffer = new StringBuffer();
                mStringBuffer.append(firstdayOfWeek);
                mStringBuffer.append("至");
                mStringBuffer.append(lastdayOfWeek);
                mStringBuffer.append("行车报告");
                mTextView3.setText(mStringBuffer.toString());
            }

            mTextView4.setText(mReportWeekInfo.getAvgpoint() + "");
            mTextView4.setTypeface(GetTypeFace.typefaceBold(null));
            mTextView4.setTextColor(MyParse.getColorByPoint(mReportWeekInfo.getAvgpoint()));

            String pointdesc = mReportWeekInfo.getPointdesc();
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

            dataList = mReportWeekInfo.getmChatList();
            if (dataList != null && dataList.size() > 1) {
                Message msg = new Message();
                msg.what = 1;
                msg.obj = dataList;
                mHandler.sendMessage(msg);

                mLineH.setVisibility(View.VISIBLE);
                mReportChartView.setVisibility(View.VISIBLE);
                mImageView4.setVisibility(View.GONE);
                if (mImageView4.getAnimation() != null) {
                    mImageView4.clearAnimation();
                }
            } else {
                mLineH.setVisibility(View.GONE);
                mReportChartView.setVisibility(View.GONE);
                mImageView4.setVisibility(View.VISIBLE);
            }

        } catch (Exception e) {
            // Log.e("info", getClass() + "==" + e);
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    ArrayList<ReportWeekChatInfo> dataList = (ArrayList<ReportWeekChatInfo>)msg.obj;

                    mReportChartView.setDataList(dataList);
                    mReportChartView.setColorTxtCircle(Color.WHITE);
                    mReportChartView.setColorLine(Color.parseColor("#AAAAAA"));
                    mReportChartView.setColorTxtBottom(Color.parseColor("#999999"));
                    mReportChartView.setTxtSizeCircle(20);
                    mReportChartView.setTxtSizeBottom(20);
                    mReportChartView.invalidate();
                    break;

            }
        };
    };

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
        mReportWeekInfo = (ReportWeekInfo)o;
        LoadDate();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAnimation != null) {
            if (dataList != null && dataList.size() > 1) {
                mImageView4.clearAnimation();
            } else {
                mImageView4.setAnimation(mAnimation);
                mImageView4.startAnimation(mAnimation);
            }
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

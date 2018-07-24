
package com.carlt.sesame.ui.activity.career;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.carlt.sesame.R;
import com.carlt.sesame.control.CPControl;
import com.carlt.sesame.data.LoginInfo;
import com.carlt.sesame.data.career.ReportAllInfo;
import com.carlt.sesame.ui.activity.base.BaseLoadingView;
import com.carlt.sesame.ui.activity.career.report.ReportActivity;
import com.carlt.sesame.ui.view.CircleValueBar;
import com.carlt.sesame.utility.GetTypeFace;
import com.carlt.sesame.utility.MyParse;

import java.util.ArrayList;

/**
 * 行车报告-总报
 * 
 * @author daisy
 */
public class ReportSummaryView extends BaseLoadingView implements OnClickListener {

    private TextView mTextView1;// 日均驾驶得分

    private TextView mTextView2;// 日均驾驶得分描述1

    private TextView mTextView3;// 日均驾驶得分描述2

    private TextView mTextView4;// 最高时速

    private TextView mTextView5;// 最高时速时间

    private TextView mTextView6;// 日最低油耗

    private TextView mTextView7;// 日最低油耗时间

    private TextView mTextView8;// 单日最高里程

    private TextView mTextView9;// 单日最高里程时间

    private TextView mTextView10;// 日最高耗油量

    private TextView mTextView11;// 日最高耗油量时间

    private TextView mTextView12;// 日最高得分

    private TextView mTextView13;// 日最高得分时间

    private TextView mTextView14;// 日最高平均时速

    private TextView mTextView15;// 日最高平均时速时间

    private TextView mTextView16;// 日最多行车时间

    private TextView mTextView17;// 日最多行车时间的时间

    private TextView mTxtPrompt;// 无数据时的提示语

    private TextView mTextReportGrid1; // 总油耗

    private TextView mTextReportGrid2; // 总油耗描述

    private TextView mTextReportGrid3; // 总里程

    private TextView mTextReportGrid4; // 总里程描述

    private TextView mTextReportGrid5; // 最高时速

    private TextView mTextReportGrid6; // 最高时速描述

    private TextView mTextReportGrid7; // 平均油耗

    private TextView mTextReportGrid8; // 平均油耗描述

    private CircleValueBar mValueBar1;// 最高时速对比条

    private CircleValueBar mValueBar2;// 日最低油耗对比条

    private CircleValueBar mValueBar3;// 单日最高里程对比条

    private CircleValueBar mValueBar4;// 日最多耗油对比条

    private CircleValueBar mValueBar5;// 日最高得分对比条

    private CircleValueBar mValueBar6;// 日最高平均时速对比条

    private CircleValueBar mValueBar7;// 日最多行车时间（单位：分钟）

    private View btn1;

    private View btn2;

    private View btn3;

    private View mViewData;// 总报数据部分

    private View mViewValuebars;// 总报底部的对比数据

    private ArrayList<TextView> mTextViews;// 显示行车时间

    private ReportAllInfo mReportAllInfo;// 行车总报

    private Resources mResources;

    public ReportSummaryView(Context context) {
        super(context);
        setContent(R.layout.layout_report_summary);
        mResources = context.getResources();
        init();

        // LoadData();

    }

    private void init() {
        mTextView1 = (TextView)findViewById(R.id.layout_report_all_txt1);
        mTextView2 = (TextView)findViewById(R.id.layout_report_all_txt2);
        mTextView3 = (TextView)findViewById(R.id.layout_report_all_txt3);
        mTextView4 = (TextView)findViewById(R.id.layout_report_all_txt4);
        mTextView5 = (TextView)findViewById(R.id.layout_report_all_txt5);
        mTextView6 = (TextView)findViewById(R.id.layout_report_all_txt6);
        mTextView7 = (TextView)findViewById(R.id.layout_report_all_txt7);
        mTextView8 = (TextView)findViewById(R.id.layout_report_all_txt8);
        mTextView9 = (TextView)findViewById(R.id.layout_report_all_txt9);
        mTextView10 = (TextView)findViewById(R.id.layout_report_all_txt10);
        mTextView11 = (TextView)findViewById(R.id.layout_report_all_txt11);
        mTextView12 = (TextView)findViewById(R.id.layout_report_all_txt12);
        mTextView13 = (TextView)findViewById(R.id.layout_report_all_txt13);
        mTextView14 = (TextView)findViewById(R.id.layout_report_all_txt14);
        mTextView15 = (TextView)findViewById(R.id.layout_report_all_txt15);
        mTextView16 = (TextView)findViewById(R.id.layout_report_all_txt16);
        mTextView17 = (TextView)findViewById(R.id.layout_report_all_txt17);
        mTxtPrompt = (TextView)findViewById(R.id.layout_report_summary_txt_prompt);

        mTextViews = new ArrayList<TextView>();
        mTextViews.add((TextView)findViewById(R.id.layout_report_all_txt_number1));
        mTextViews.add((TextView)findViewById(R.id.layout_report_all_txt_number2));
        mTextViews.add((TextView)findViewById(R.id.layout_report_all_txt_number3));
        mTextViews.add((TextView)findViewById(R.id.layout_report_all_txt_number4));
        mTextViews.add((TextView)findViewById(R.id.layout_report_all_txt_number5));
        mTextViews.add((TextView)findViewById(R.id.layout_report_all_txt_number6));

        mTextReportGrid1 = (TextView)findViewById(R.id.layout_report_grid_linear1_txt1);
        mTextReportGrid2 = (TextView)findViewById(R.id.layout_report_grid_linear1_txt2);
        mTextReportGrid3 = (TextView)findViewById(R.id.layout_report_grid_linear2_txt1);
        mTextReportGrid4 = (TextView)findViewById(R.id.layout_report_grid_linear2_txt2);
        mTextReportGrid5 = (TextView)findViewById(R.id.layout_report_grid_linear3_txt1);
        mTextReportGrid6 = (TextView)findViewById(R.id.layout_report_grid_linear3_txt2);
        mTextReportGrid7 = (TextView)findViewById(R.id.layout_report_grid_linear4_txt1);
        mTextReportGrid8 = (TextView)findViewById(R.id.layout_report_grid_linear4_txt2);

        mValueBar1 = (CircleValueBar)findViewById(R.id.layout_report_all_valuebar1);
        mValueBar2 = (CircleValueBar)findViewById(R.id.layout_report_all_valuebar2);
        mValueBar3 = (CircleValueBar)findViewById(R.id.layout_report_all_valuebar3);
        mValueBar4 = (CircleValueBar)findViewById(R.id.layout_report_all_valuebar4);
        mValueBar5 = (CircleValueBar)findViewById(R.id.layout_report_all_valuebar5);
        mValueBar6 = (CircleValueBar)findViewById(R.id.layout_report_all_valuebar6);
        mValueBar7 = (CircleValueBar)findViewById(R.id.layout_report_all_valuebar7);

        btn1 = findViewById(R.id.layout_report_summary_month);
        btn2 = findViewById(R.id.layout_report_summary_week);
        btn3 = findViewById(R.id.layout_report_summary_day);

        mViewData = findViewById(R.id.layout_report_summary_grid);
        mViewValuebars = findViewById(R.id.layout_report_all_valuebars);

        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
    }

    ArrayList<String> mStrings = new ArrayList<String>();

    @Override
    protected void LoadSuccess(Object data) {
        mReportAllInfo = (ReportAllInfo)data;

        String avgPoint = mReportAllInfo.getAvgPoint();
        if (avgPoint != null && avgPoint.length() > 0) {
            if (MyParse.parseInt(avgPoint) < 0) {
                mViewData.setVisibility(View.GONE);
                mTxtPrompt.setVisibility(View.VISIBLE);
            } else {
                mViewData.setVisibility(View.VISIBLE);
                mTxtPrompt.setVisibility(View.GONE);
                LoadMyData();
                String maxPoint = mReportAllInfo.getMaxPoint();
                if (maxPoint != null && maxPoint.length() > 0) {
                    if (MyParse.parseInt(maxPoint) < 0) {
                        mViewValuebars.setVisibility(View.GONE);
                    } else {
                        mViewValuebars.setVisibility(View.VISIBLE);
                        LoadConpareData();
                    }
                }
            }
        }

        super.LoadSuccess(data);

    }

    private void LoadMyData() {
        mTextView1.setText(mReportAllInfo.getAvgPoint());
        mTextView1.setTypeface(GetTypeFace.typefaceBold(null));
        int point = MyParse.parseInt(mReportAllInfo.getAvgPoint());
        mTextView1.setTextColor(MyParse.getColorByPoint(point));

        if (mReportAllInfo.getPointdesc() != null) {
            mTextView2.setText(mReportAllInfo.getPointdesc());
        }
        if (mReportAllInfo.getRank() != null) {
            mTextView3.setText(mReportAllInfo.getRank() + "%");
        }

        if (mReportAllInfo.getSumTime() != null && !mReportAllInfo.getSumTime().equals("")) {
            for (int i = 0; i < mReportAllInfo.getSumTime().length(); i++) {
                mStrings.add(mReportAllInfo.getSumTime().substring(i, i + 1));
            }
            for (int i = 0; i < 6 && i < mStrings.size(); i++) {
                mTextViews.get(6 - mStrings.size() + i).setText(mStrings.get(i));
            }
        }

        String s1;

        s1 = mReportAllInfo.getSumFuel();
        if (s1 != null) {
            SpannableStringBuilder mStringBuilder = new SpannableStringBuilder();
            mStringBuilder.append(s1);
            mStringBuilder.append("升");
            mStringBuilder.setSpan(new AbsoluteSizeSpan(26), s1.length(), s1.length() + 1,
                    Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            mStringBuilder.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.text_color_gray1)), s1.length(),
                    s1.length() + 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);

            mTextReportGrid1.setText(mStringBuilder);
        }
        if (mReportAllInfo.getSumfueldesc() != null) {
            mTextReportGrid2.setText(mReportAllInfo.getSumfueldesc());
        }

        s1 = mReportAllInfo.getSumMiles();
        if (s1 != null) {
            SpannableStringBuilder mStringBuilder = new SpannableStringBuilder();
            mStringBuilder.append(s1);
            mStringBuilder.append("公里");
            mStringBuilder.setSpan(new AbsoluteSizeSpan(26), s1.length(), s1.length() + 2,
                    Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            mStringBuilder.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.text_color_gray1)), s1.length(),
                    s1.length() + 2, Spannable.SPAN_INCLUSIVE_INCLUSIVE);

            mTextReportGrid3.setText(mStringBuilder);
        }
        if (mReportAllInfo.getSummilesdesc() != null) {
            mTextReportGrid4.setText(mReportAllInfo.getSummilesdesc());
        }

        s1 = mReportAllInfo.getAvgSpeed();
        if (s1 != null) {
            SpannableStringBuilder mStringBuilder = new SpannableStringBuilder();
            mStringBuilder.append(s1);
            mStringBuilder.append("公里/小时");
            mStringBuilder.setSpan(new AbsoluteSizeSpan(26), s1.length(), s1.length() + 5,
                    Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            mStringBuilder.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.text_color_gray1)), s1.length(),
                    s1.length() + 4, Spannable.SPAN_INCLUSIVE_INCLUSIVE);

            mTextReportGrid5.setText(mStringBuilder);
        }
        if (mReportAllInfo.getAvgspeeddesc() != null) {
            mTextReportGrid6.setText(mReportAllInfo.getAvgspeeddesc());
        }

        s1 = mReportAllInfo.getAvgFuel();
        if (s1 != null) {
            SpannableStringBuilder mStringBuilder = new SpannableStringBuilder();
            mStringBuilder.append(s1);
            mStringBuilder.append("升/百公里");

            mStringBuilder.setSpan(new AbsoluteSizeSpan(26), s1.length(), s1.length() + 5,
                    Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            mStringBuilder.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.text_color_gray1)), s1.length(),
                    s1.length() + 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            mTextReportGrid7.setText(mStringBuilder);
        }
        if (mReportAllInfo.getAvgfueldesc() != null) {
            mTextReportGrid8.setText(mReportAllInfo.getAvgfueldesc());
        }
    }

    private void LoadConpareData() {
        if (mReportAllInfo.getMaxSpeed() != null) {
            mTextView4.setText(mReportAllInfo.getMaxSpeed() + "公里/小时");
        }
        if (mReportAllInfo.getMaxSpeedtime() != null) {
            mTextView5.setText(mReportAllInfo.getMaxSpeedtime());
        }
        if (mReportAllInfo.getMinFuel() != null) {
            mTextView6.setText(mReportAllInfo.getMinFuel() + "升/百公里");
        }
        if (mReportAllInfo.getMinFueltime() != null) {
            mTextView7.setText(mReportAllInfo.getMinFueltime());
        }
        if (mReportAllInfo.getMaxMiles() != null) {
            mTextView8.setText(mReportAllInfo.getMaxMiles() + "公里");
        }
        if (mReportAllInfo.getMaxMilestime() != null) {
            mTextView9.setText(mReportAllInfo.getMaxMilestime());
        }
        if (mReportAllInfo.getMaxFuel() != null) {
            mTextView10.setText(mReportAllInfo.getMaxFuel() + "升");
        }
        if (mReportAllInfo.getMaxFueltime() != null) {
            mTextView11.setText(mReportAllInfo.getMaxFueltime());
        }
        if (mReportAllInfo.getMaxPoint() != null) {
            mTextView12.setText(mReportAllInfo.getMaxPoint() + "分");
        }
        if (mReportAllInfo.getMaxPointtime() != null) {
            mTextView13.setText(mReportAllInfo.getMaxPointtime());
        }
        if (mReportAllInfo.getMaxAvgspeed() != null) {
            mTextView14.setText(mReportAllInfo.getMaxAvgspeed() + "公里/小时");
        }
        if (mReportAllInfo.getMaxavgspeedtime() != null) {
            mTextView15.setText(mReportAllInfo.getMaxavgspeedtime());
        }
        if (mReportAllInfo.getMaxSumtime() != null) {
            mTextView16.setText(mReportAllInfo.getMaxSumtime() + "分钟");
        }
        if (mReportAllInfo.getMaxsumtimetime() != null) {
            mTextView17.setText(mReportAllInfo.getMaxsumtimetime());
        }

        mValueBar1.setValueWithMax(MyParse.parseInt(mReportAllInfo.getMaxSpeed()),
                MyParse.parseInt(mReportAllInfo.getAllavgmaxspeed()),
                MyParse.parseInt(mReportAllInfo.getAllmaxmaxspeed()));

        mValueBar2.setValueWithMin(MyParse.parseFloat(mReportAllInfo.getMinFuel()),
                MyParse.parseFloat(mReportAllInfo.getAllavgminfuel()),
                MyParse.parseFloat(mReportAllInfo.getAllminminfuel()));

        mValueBar3.setValueWithMax(MyParse.parseInt(mReportAllInfo.getMaxMiles()),
                MyParse.parseInt(mReportAllInfo.getAllavgmaxmiles()),
                MyParse.parseInt(mReportAllInfo.getAllmaxmaxmiles()));

        mValueBar4.setValueWithMax(MyParse.parseFloat(mReportAllInfo.getMaxFuel()),
                MyParse.parseFloat(mReportAllInfo.getAllavgmaxfuel()),
                MyParse.parseFloat(mReportAllInfo.getAllmaxmaxfuel()));

        mValueBar5.setValueWithMax(MyParse.parseInt(mReportAllInfo.getMaxPoint()),
                MyParse.parseInt(mReportAllInfo.getAllavgmaxpoint()),
                MyParse.parseInt(mReportAllInfo.getAllmaxmaxpoint()));

        mValueBar6.setValueWithMax(MyParse.parseFloat(mReportAllInfo.getMaxAvgspeed()),
                MyParse.parseFloat(mReportAllInfo.getAllavgmaxavgspeed()),
                MyParse.parseFloat(mReportAllInfo.getAllmaxmaxavgspeed()));

        mValueBar7.setValueWithMax(MyParse.parseInt(mReportAllInfo.getMaxSumtime()),
                MyParse.parseInt(mReportAllInfo.getAllavgmaxsumtime()),
                MyParse.parseInt(mReportAllInfo.getAllmaxmaxsumtime()));

    }

    @Override
    protected void LoadErro(Object erro) {
        super.LoadErro(erro);
    }

    @Override
    protected void LoadData() {
        super.LoadData();
        CPControl.GetAllReportResult(listener);
    }

    @Override
    public void onClick(View v) {
        Intent mIntent = null;
        String data;
        switch (v.getId()) {
            case R.id.layout_report_summary_month:
                mIntent = new Intent(mContext, ReportActivity.class);
                data = LoginInfo.getLately_month();
                mIntent.putExtra(ReportActivity.MONTH_INITIAL, data);
                mIntent.putExtra("c", 0);
                break;
            case R.id.layout_report_summary_week:
                mIntent = new Intent(mContext, ReportActivity.class);
                data = LoginInfo.getLately_week();
                mIntent.putExtra(ReportActivity.WEEK_INITIAL, data);
                mIntent.putExtra("c", 1);
                break;
            case R.id.layout_report_summary_day:
                mIntent = new Intent(mContext, ReportActivity.class);
                data = LoginInfo.getLately_day();
                mIntent.putExtra(ReportActivity.DAY_INITIAL, data);
                mIntent.putExtra("c", 2);
                break;
        }

        if (mIntent != null) {
            mContext.startActivity(mIntent);
        }

    }

    @Override
    public void onShare() {
        super.onShare();
        if (mReportAllInfo != null) {
//            ShareControl.share(ReportSummaryView.class, (Activity)mContext,
//                    mReportAllInfo.getShareTitle(), mReportAllInfo.getShareText(),
//                    mReportAllInfo.getShareLink(), R.drawable.friends_share_img);
            CPControl.GetReportShareResult(mReportAllInfo.getShareTitle(),
                    mReportAllInfo.getShareText(), mReportAllInfo.getShareLink(), "",
                    CPControl.REPORT_ALL);
        }

    }
}

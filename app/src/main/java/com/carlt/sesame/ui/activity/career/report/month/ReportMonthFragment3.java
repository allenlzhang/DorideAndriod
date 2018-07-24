
package com.carlt.sesame.ui.activity.career.report.month;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.carlt.sesame.R;
import com.carlt.sesame.data.career.ReportMonthInfo;
import com.carlt.sesame.ui.activity.career.report.BaseFragment;
import com.carlt.sesame.utility.GetTypeFace;

public class ReportMonthFragment3 extends BaseFragment {
    private TextView mTextView1;// 本月行车时间-小时

    private TextView mTextView2;// 本月行车时间-分钟

    private TextView mTextView3;// 本月行车时间描述

    private TextView mTextViewHour;// 时间单位-小时

    private TextView mTextViewMin;// 时间单位-分钟

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View convertView = inflater.inflate(R.layout.layout_report_month3, null);

        mTextView1 = (TextView)convertView.findViewById(R.id.layout_report_month3_txt1);
        mTextView2 = (TextView)convertView.findViewById(R.id.layout_report_month3_txt2);
        mTextView3 = (TextView)convertView.findViewById(R.id.layout_report_month3_txt3);

        mTextViewHour = (TextView)convertView.findViewById(R.id.layout_report_month3_txt_hour);
        mTextViewMin = (TextView)convertView.findViewById(R.id.layout_report_month3_txt_min);
        LoadDate();
        return convertView;
    }

    private ReportMonthInfo mReportMonthInfo;

    public void LoadDate() {
        try {
            mTextView1.setTypeface(GetTypeFace.typefaceBold(null));
            mTextView2.setTypeface(GetTypeFace.typefaceBold(null));
            
            String sumtime = mReportMonthInfo.getSumtime_str();
            if (sumtime != null && sumtime.length() > 0) {
                int index_hour = sumtime.indexOf("小");
                String hour = "";
                if (index_hour >= 0) {
                    hour = sumtime.substring(0, index_hour);
                }

                int index_min = sumtime.indexOf("分");

                String min = "";
                if (index_min > 0) {
                    if (index_hour >= 0) {
                        index_hour = index_hour + 2;
                        if (index_hour < sumtime.length()) {
                            min = sumtime.substring(index_hour, index_min);
                        }

                    } else {
                        min = sumtime.substring(0, index_min);
                    }
                }

                if (hour != null && hour.length() > 0) {
                    mTextView1.setText(hour);
                } else {
                    mTextView1.setVisibility(View.GONE);
                    mTextViewHour.setVisibility(View.GONE);
                }
                if (min != null && min.length() > 0) {
                    mTextView2.setText(min);
                } else {
                    mTextView2.setVisibility(View.GONE);
                    mTextViewMin.setVisibility(View.GONE);
                }
            }

            mTextView3.setText(mReportMonthInfo.getSumtimedesc());

        } catch (Exception e) {
            // Log.e("info", getClass() + "==" + e);
        }
    }

    @Override
    public void setData(Object o) {
        mReportMonthInfo = (ReportMonthInfo)o;
        LoadDate();
    }
}

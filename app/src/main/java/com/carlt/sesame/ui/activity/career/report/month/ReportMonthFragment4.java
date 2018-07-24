
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

public class ReportMonthFragment4 extends BaseFragment {
    private TextView mTextView1;// 本月行车里程

    private TextView mTextView2;// 本月行车里程描述

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View convertView = inflater.inflate(R.layout.layout_report_month4, null);

        mTextView1 = (TextView)convertView.findViewById(R.id.layout_report_month4_txt1);
        mTextView2 = (TextView)convertView.findViewById(R.id.layout_report_month4_txt2);

        LoadDate();
        return convertView;
    }

    private ReportMonthInfo mReportMonthInfo;

    @Override
    public void LoadDate() {
        try {

            mTextView1.setText(mReportMonthInfo.getSumMiles() + "");
            mTextView1.setTypeface(GetTypeFace.typefaceBold(null));
            
            mTextView2.setText(mReportMonthInfo.getSummilesdesc());
            // mTextView2.setText(GetTextDescription.getStringReportTotalMileage(mReportMonthInfo
            // .getSumMiles()));
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

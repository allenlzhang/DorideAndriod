
package com.carlt.sesame.ui.activity.career.report.month;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.carlt.sesame.R;
import com.carlt.sesame.data.career.ReportMonthInfo;
import com.carlt.sesame.ui.activity.career.report.BaseFragment;
import com.carlt.sesame.ui.view.CircleValueBar;
import com.carlt.sesame.utility.GetTypeFace;
import com.carlt.sesame.utility.Log;
import com.carlt.sesame.utility.MyParse;

public class ReportMonthFragment5 extends BaseFragment {
    private TextView mTextView1;// 本月总油耗

    private TextView mTextView2;// 本月总油耗描述

    private TextView mTextView3;// 本月平均油耗

    private CircleValueBar mValueBar;

    private TextView layout_report_month5_txt1_danwei;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View convertView = inflater.inflate(R.layout.layout_report_month5, null);

        mTextView1 = (TextView)convertView.findViewById(R.id.layout_report_month5_txt1);
        mTextView2 = (TextView)convertView.findViewById(R.id.layout_report_month5_txt2);
        mTextView3 = (TextView)convertView.findViewById(R.id.layout_report_month5_txt3);
        layout_report_month5_txt1_danwei = (TextView)convertView
                .findViewById(R.id.layout_report_month5_txt1_danwei);
        mValueBar = (CircleValueBar)convertView.findViewById(R.id.layout_report_month5_valueBar);

        LoadDate();
        return convertView;
    }

    private ReportMonthInfo mReportMonthInfo;

    @Override
    public void LoadDate() {
        try {

            mTextView1.setTypeface(GetTypeFace.typefaceBold(null));
            int sumfuel = MyParse.parseInt(mReportMonthInfo.getSumFuel());
            if (sumfuel < 100) {

                mTextView1.setText(sumfuel + "");
                layout_report_month5_txt1_danwei.setText("毫升");
            } else {
                double f = sumfuel / 1000.0;
                mTextView1.setText(String.format("%.2f", f));
                layout_report_month5_txt1_danwei.setText("升");
            }

            mTextView2.setText(mReportMonthInfo.getSumfueldesc());

            mTextView3.setText(mReportMonthInfo.getAvgFuel() + "");
            mTextView3.setTypeface(GetTypeFace.typefaceBold(null));

            String avgFuel = mReportMonthInfo.getAvgFuel();
            float avgFuelF = 0;
            if (avgFuel != null && avgFuel.length() > 0) {
                avgFuelF = MyParse.parseFloat(avgFuel);
            }

            String allAvgFuel = mReportMonthInfo.getAllAvgFuel();
            float allAvgFuelF = 0;
            if (allAvgFuel != null && allAvgFuel.length() > 0) {
                allAvgFuelF = MyParse.parseFloat(allAvgFuel);
            }

            String typeAvgFuel = mReportMonthInfo.getTypeAvgFuel();
            float typeAvgFuelF = 0;
            if (typeAvgFuel != null && typeAvgFuel.length() > 0) {
                typeAvgFuelF = MyParse.parseFloat(typeAvgFuel);
            }
            mValueBar.setValueWithType(avgFuelF, typeAvgFuelF, allAvgFuelF);
        } catch (Exception e) {

            Log.e("info", getClass() + "==" + e);
        }
    }

    @Override
    public void setData(Object o) {
        mReportMonthInfo = (ReportMonthInfo)o;
        LoadDate();
    }
}

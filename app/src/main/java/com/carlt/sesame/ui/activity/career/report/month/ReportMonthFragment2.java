
package com.carlt.sesame.ui.activity.career.report.month;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.carlt.doride.DorideApplication;
import com.carlt.sesame.R;
import com.carlt.sesame.data.career.ReportMonthInfo;
import com.carlt.sesame.ui.activity.career.report.BaseFragment;
import com.carlt.sesame.utility.GetTypeFace;

public class ReportMonthFragment2 extends BaseFragment {
    private TextView mTextView1;// 本月最高时速

    private TextView mTextView2;// 最高时速描述

    private TextView mTextView3;// 本月平均时速

    private TextView mTextView4;// 平均时速匹配动物描述

    private ImageView mImageView;// 平均时速匹配动物Icon

    private Resources mResources;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View convertView = inflater.inflate(R.layout.layout_report_month2, null);

        mTextView1 = (TextView)convertView.findViewById(R.id.layout_report_month2_txt1);
        mTextView2 = (TextView)convertView.findViewById(R.id.layout_report_month2_txt2);
        mTextView3 = (TextView)convertView.findViewById(R.id.layout_report_month2_txt3);
        mTextView4 = (TextView)convertView.findViewById(R.id.layout_report_month2_txt4);

        mImageView = (ImageView)convertView.findViewById(R.id.layout_report_month2_img);

        mResources = DorideApplication.ApplicationContext.getResources();
        LoadDate();
        return convertView;
    }

    private ReportMonthInfo mReportMonthInfo;

    @Override
    public void LoadDate() {

        try {

            mTextView1.setText(mReportMonthInfo.getMaxSpeed() + "");
            mTextView1.setTypeface(GetTypeFace.typefaceBold(null));

            if (null != mReportMonthInfo.getMaxspeeddesc()) {
                mTextView2.setText(mReportMonthInfo.getMaxspeeddesc());
            }

            mTextView3.setText(mReportMonthInfo.getAvgSpeed() + "");
            mTextView3.setTypeface(GetTypeFace.typefaceBold(null));

            if (null != mReportMonthInfo.getAvgspeeddesc()) {
                mTextView4.setText(mReportMonthInfo.getAvgspeeddesc());
            }
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

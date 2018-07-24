
package com.carlt.sesame.ui.activity.career.report.day;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.carlt.doride.DorideApplication;
import com.carlt.sesame.R;
import com.carlt.sesame.data.career.ReportDayInfo;
import com.carlt.sesame.ui.activity.career.report.BaseFragment;
import com.carlt.sesame.ui.view.CircleValueBar;
import com.carlt.sesame.utility.MyParse;

public class ReportDayFragment3 extends BaseFragment {

    private CircleValueBar mValueBar;

    private TextView mTextView1;// 总体评价内容

    private TextView mTextView2;// 评价（高、中、低）

    private TextView mTextView3;// 行车时间

    private TextView mTextView4;// 最高速度

    private TextView mTextView5;// 平均时速

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View convertView = inflater.inflate(R.layout.layout_report_day3, null);
        mValueBar = (CircleValueBar)convertView.findViewById(R.id.layout_report_day3_valuebar);

        mTextView1 = (TextView)convertView.findViewById(R.id.layout_report_day3_txt1);
        mTextView2 = (TextView)convertView.findViewById(R.id.layout_report_day3_txt2);
        mTextView3 = (TextView)convertView.findViewById(R.id.layout_report_day3_txt3);
        mTextView4 = (TextView)convertView.findViewById(R.id.layout_report_day3_txt4);
        mTextView5 = (TextView)convertView.findViewById(R.id.layout_report_day3_txt5);

        LoadDate();
        return convertView;
    }

    private ReportDayInfo mReportDayInfo;

    @Override
    public void LoadDate() {
        try {
            String avgFuel = mReportDayInfo.getAvgFuel();
            float avgFuelF = 0;
            String sumMiles = mReportDayInfo.getSumMiles();
            String sumFuel = mReportDayInfo.getSumFuel();
            if (sumMiles != null && sumMiles.length() > 0 && sumFuel != null
                    && sumFuel.length() > 0 && avgFuel != null && avgFuel.length() > 0) {
                StringBuffer mStringBuffer = new StringBuffer("今日行驶");
                mStringBuffer.append(sumMiles);
                mStringBuffer.append("公里，油耗");
                mStringBuffer.append(sumFuel);
                mStringBuffer.append("，平均油耗");
                mStringBuffer.append(avgFuel);
                mStringBuffer.append("升/百公里。");
                mTextView1.setText(mStringBuffer);

                avgFuelF = MyParse.parseFloat(avgFuel);
            } else {
                mTextView1.setText("本周行驶 公里，油耗 升，平均油耗 升/百公里。");
            }

            String typeAvgFuel = mReportDayInfo.getTypeAvgFuel();
            float typeAvgFuelF = 0;
            if (typeAvgFuel != null && typeAvgFuel.length() > 0) {
                typeAvgFuelF = MyParse.parseFloat(typeAvgFuel);
            }

            if (avgFuelF > typeAvgFuelF) {
                mTextView2.setText("高");
                mTextView2.setBackgroundDrawable(DorideApplication.ApplicationContext.getResources()
                        .getDrawable(R.drawable.text_bg_cycle_red));
            } else {
                mTextView2.setText("低");
                mTextView2.setBackgroundDrawable(DorideApplication.ApplicationContext.getResources()
                        .getDrawable(R.drawable.text_bg_cycle_green));
            }

            String sumtime = mReportDayInfo.getSumtime_str();
            if (sumtime != null && sumtime.length() > 0) {
                mTextView3.setText(sumtime);
            } else {
                mTextView3.setText("分钟");
            }

            String maxSpeed = mReportDayInfo.getMaxSpeed();
            if (maxSpeed != null && maxSpeed.length() > 0) {
                mTextView4.setText(mReportDayInfo.getMaxSpeed() + "公里/小时");
            } else {
                mTextView4.setText("公里/小时");
            }

            String avgSpeed = mReportDayInfo.getAvgSpeed();
            if (avgSpeed != null && avgSpeed.length() > 0) {
                mTextView5.setText(mReportDayInfo.getAvgSpeed() + "公里/小时");
            } else {
                mTextView5.setText("公里/小时");
            }

            String allAvgFuel = mReportDayInfo.getAllAvgFuel();
            float allAvgFuelF = 0;
            if (allAvgFuel != null && allAvgFuel.length() > 0) {
                allAvgFuelF = MyParse.parseFloat(allAvgFuel);
            }
            mValueBar.setValueWithType(avgFuelF, typeAvgFuelF, allAvgFuelF);

        } catch (Exception e) {
            // Log.e("info", getClass() + "==" + e);
        }

    }

    @Override
    public void setData(Object o) {
        mReportDayInfo = (ReportDayInfo)o;
        LoadDate();

    }
}

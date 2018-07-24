
package com.carlt.sesame.ui.activity.career.report.week;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.carlt.sesame.R;
import com.carlt.sesame.data.career.ReportWeekInfo;
import com.carlt.sesame.ui.activity.career.report.BaseFragment;

import java.util.ArrayList;

public class ReportWeekFragment2 extends BaseFragment {
    private TextView mTextLable1;// 驾驶诊断标签1

    private TextView mTextLable2;// 驾驶诊断标签2

    private TextView mTextLable3;// 驾驶诊断标签3

    private TextView mTextMy1;// 急刹车次数-我

    private TextView mTextType1;// 急刹车次数-同型车

    private TextView mTextDesc1;// 急刹车评价描述

    private TextView mTextMy2;// 急转弯次数-我

    private TextView mTextType2;// 急转弯次数-同型车

    private TextView mTextDesc2;// 急转弯评价描述

    private TextView mTextMy3;// 急加速次数-我

    private TextView mTextType3;// 急加速次数-同型车

    private TextView mTextDesc3;// 急加速评价描述

    private TextView mTextMy4;// 高转速次数-我

    private TextView mTextType4;// 高转速次数-同型车

    private TextView mTextDesc4;// 高转速评价描述

    private ImageView mImageView1;// 赞/不赞图标-急刹车

    private ImageView mImageView2;// 赞/不赞图标-急转弯

    private ImageView mImageView3;// 赞/不赞图标-急加速

    private ImageView mImageView4;// 赞/不赞图标-高转速

    private int[] valueMy = new int[4];

    private int[] valueType = new int[4];

    private ArrayList<SeekBar> mSeekBars;// 对比条（依次为：急刹车、急转弯、急加速、高转速）

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View convertView = inflater.inflate(R.layout.layout_report_week2, null);

        mTextLable1 = (TextView)convertView.findViewById(R.id.layout_report_week2_lable1);
        mTextLable2 = (TextView)convertView.findViewById(R.id.layout_report_week2_lable2);
        mTextLable3 = (TextView)convertView.findViewById(R.id.layout_report_week2_lable3);

        mTextMy1 = (TextView)convertView.findViewById(R.id.layout_report_week2_relative1_txt2);
        mTextType1 = (TextView)convertView.findViewById(R.id.layout_report_week2_relative1_txt3);
        mTextDesc1 = (TextView)convertView.findViewById(R.id.layout_report_week2_relative1_txt4);

        mTextMy2 = (TextView)convertView.findViewById(R.id.layout_report_week2_relative2_txt2);
        mTextType2 = (TextView)convertView.findViewById(R.id.layout_report_week2_relative2_txt3);
        mTextDesc2 = (TextView)convertView.findViewById(R.id.layout_report_week2_relative2_txt4);

        mTextMy3 = (TextView)convertView.findViewById(R.id.layout_report_week2_relative3_txt2);
        mTextType3 = (TextView)convertView.findViewById(R.id.layout_report_week2_relative3_txt3);
        mTextDesc3 = (TextView)convertView.findViewById(R.id.layout_report_week2_relative3_txt4);

        mTextMy4 = (TextView)convertView.findViewById(R.id.layout_report_week2_relative4_txt2);
        mTextType4 = (TextView)convertView.findViewById(R.id.layout_report_week2_relative4_txt3);
        mTextDesc4 = (TextView)convertView.findViewById(R.id.layout_report_week2_relative4_txt4);

        mImageView1 = (ImageView)convertView.findViewById(R.id.layout_report_week2_relative1_img);
        mImageView2 = (ImageView)convertView.findViewById(R.id.layout_report_week2_relative2_img);
        mImageView3 = (ImageView)convertView.findViewById(R.id.layout_report_week2_relative3_img);
        mImageView4 = (ImageView)convertView.findViewById(R.id.layout_report_week2_relative4_img);

        mSeekBars = new ArrayList<SeekBar>();

        mSeekBars
                .add((SeekBar)convertView.findViewById(R.id.layout_report_week2_relative1_seekbar));
        mSeekBars
                .add((SeekBar)convertView.findViewById(R.id.layout_report_week2_relative2_seekbar));
        mSeekBars
                .add((SeekBar)convertView.findViewById(R.id.layout_report_week2_relative3_seekbar));
        mSeekBars
                .add((SeekBar)convertView.findViewById(R.id.layout_report_week2_relative4_seekbar));

        return convertView;
    }

    ReportWeekInfo mReportWeekInfo;

    @Override
    public void LoadDate() {
        try {
            String tag = mReportWeekInfo.getTag();
            if (null != tag && tag.length() > 0) {
                String[] tags = tag.split(",");

                mTextLable1.setText(tags[0]);
                mTextLable1.setVisibility(View.VISIBLE);

                if (tags.length == 2) {
                    mTextLable2.setText(tags[1]);
                    mTextLable2.setVisibility(View.VISIBLE);
                }
                if (tags.length >= 3) {
                    mTextLable2.setText(tags[1]);
                    mTextLable2.setVisibility(View.VISIBLE);

                    mTextLable3.setText(tags[2]);
                    mTextLable3.setVisibility(View.VISIBLE);
                }
            }

            valueMy[0] = mReportWeekInfo.getBrake();
            valueMy[1] = mReportWeekInfo.getTurn();
            valueMy[2] = mReportWeekInfo.getSpeedup();
            valueMy[3] = mReportWeekInfo.getOverSpeed();

            valueType[0] = mReportWeekInfo.getTypebrake();
            valueType[1] = mReportWeekInfo.getTypeturn();
            valueType[2] = mReportWeekInfo.getTypespeedup();
            valueType[3] = mReportWeekInfo.getTypeoverSpeed();

            mTextMy1.setText(valueMy[0] + "");
            mTextMy2.setText(valueMy[1] + "");
            mTextMy3.setText(valueMy[2] + "");
            mTextMy4.setText(valueMy[3] + "");

            mTextType1.setText(valueType[0] + "");
            mTextType2.setText(valueType[1] + "");
            mTextType3.setText(valueType[2] + "");
            mTextType4.setText(valueType[3] + "");

            // mTextDesc1.setText(GetTextDescription.getStringReportDrivingTestDetail(
            // mReportWeekInfo.getBrake(), GetTextDescription.TYPE_BRAKE));
            // mTextDesc2.setText(GetTextDescription.getStringReportDrivingTestDetail(
            // mReportWeekInfo.getTurn(), GetTextDescription.TYPE_SWERVE));
            // mTextDesc3.setText(GetTextDescription.getStringReportDrivingTestDetail(
            // mReportWeekInfo.getSpeedup(),
            // GetTextDescription.TYPE_ACCELERATE));
            // mTextDesc4.setText(GetTextDescription.getStringReportDrivingTestDetail(
            // mReportWeekInfo.getOverSpeed(),
            // GetTextDescription.TYPE_OVERSPEED));

            mTextDesc1.setText(mReportWeekInfo.getBrakedesc());
            mTextDesc2.setText(mReportWeekInfo.getTurndesc());
            mTextDesc3.setText(mReportWeekInfo.getSpeedupdesc());
            mTextDesc4.setText(mReportWeekInfo.getOverspeeddesc());

            setSeekbar();

        } catch (Exception e) {
            // Log.e("info", getClass() + "==" + e);
        }
    }

    /**
     * 设置PK条进度
     */
    private void setSeekbar() {
        for (int i = 0; i < mSeekBars.size(); i++) {
            int max = valueMy[i] + valueType[i];
            if (valueMy[i] == 0 && valueType[i] == 0) {
                mSeekBars.get(i).setMax(2);
                mSeekBars.get(i).setProgress(1);
            } else if (valueMy[i] == 0 && valueType[i] != 0) {
                mSeekBars.get(i).setMax(max);
                mSeekBars.get(i).setProgress(1);
            } else if (valueMy[i] != 0 && valueType[i] == 0) {
                mSeekBars.get(i).setMax(max + 1);
                mSeekBars.get(i).setProgress(valueMy[i]);
            } else if (valueMy[i] != 0 && valueType[i] != 0) {
                mSeekBars.get(i).setMax(max);
                mSeekBars.get(i).setProgress(valueMy[i]);
            }
        }
    }

    @Override
    public void setData(Object o) {
        mReportWeekInfo = (ReportWeekInfo)o;
        LoadDate();
    }

}

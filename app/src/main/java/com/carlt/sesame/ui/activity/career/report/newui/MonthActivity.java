
package com.carlt.sesame.ui.activity.career.report.newui;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.carlt.sesame.R;
import com.carlt.sesame.control.CPControl;
import com.carlt.sesame.data.LoginInfo;
import com.carlt.sesame.data.career.ReportMonthInfo;
import com.carlt.sesame.http.AsyncImageLoader;
import com.carlt.sesame.ui.activity.base.LoadingActivityWithTitle;
import com.carlt.sesame.ui.activity.career.report.CalendarMonth;
import com.carlt.sesame.ui.activity.career.report.CalendarMonth.OnCalendarMonthClick;
import com.carlt.sesame.ui.activity.career.report.ReportActivity;
import com.carlt.sesame.ui.view.CircleValueBar;
import com.carlt.sesame.utility.GetTypeFace;
import com.carlt.sesame.utility.MyParse;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class MonthActivity extends LoadingActivityWithTitle {
    private ImageView back;// 头部返回键

    private TextView title;// 标题文字

    private TextView txtRight;// 头部右侧文字

    private TextView mTxtName;// 用户姓名

    private TextView mTxtMonth;// 月份

    private TextView mTxtScore;// 当月平均驾驶得分
    
    private TextView mTxtScorePersent;// 当月平均驾驶得分百分比

    private TextView mTxtScoreDes;// 当月平均驾驶得分描述

    private TextView mTxtOilTotal;// 总油耗
    
    private TextView mTxtOilTotalUnit;// 总油耗单位

    private TextView mTxtOilTotalDes;// 总油耗描述
    
    private TextView mTxtOilAvg;// 平均油耗
    
    private TextView mTxtMileage;// 行车里程

    private TextView mTxtMileageDes;// 行车里程描述

    private TextView mTxtHour;// 行车时间小时
    private TextView mTxtHourUnit;// 行车时间小时(单位)
    private TextView mTxtMin;// 行车时间分钟
    private TextView mTxtMinUint;// 行车时间小时(单位)

    private TextView mTxtDurationDes;// 行车时间描述

    private TextView mTxtSpeedMax;// 最高时速

    private TextView mTxtSpeedMaxDes;// 最高时速描述

    private TextView mTxtSpeedAvg;// 平均时速描述

    private TextView mTxtSpeedAvgDes;// 平均时速描述

    private TextView mTxtMy1;// 急刹车次数-我

    private TextView mTxtType1;// 急刹车次数-同型车

    private TextView mTxtDesc1;// 急刹车评价描述

    private TextView mTxtMy2;// 急转弯次数-我

    private TextView mTxtType2;// 急转弯次数-同型车

    private TextView mTxtDesc2;// 急转弯评价描述

    private TextView mTxtMy3;// 急加速次数-我

    private TextView mTxtType3;// 急加速次数-同型车

    private TextView mTxtDesc3;// 急加速评价描述

    private TextView mTxtMy4;// 高转速次数-我

    private TextView mTxtType4;// 高转速次数-同型车

    private TextView mTxtDesc4;// 高转速评价描述

    private ImageView mImgAvadar;// 用户头像

    private ImageView mImgSex;// 用户性别

    private ImageView mImgCar;// 用户车型

    private ImageView mImg1;// 赞/不赞图标-急刹车

    private ImageView mImg2;// 赞/不赞图标-急转弯

    private ImageView mImg3;// 赞/不赞图标-急加速

    private ImageView mImg4;// 赞/不赞图标-高转速

    private int[] valueMy = new int[4];

    private int[] valueType = new int[4];

    private CircleValueBar mValueBar;// 糖葫芦valuebar
    
    private ArrayList<SeekBar> mSeekBars;// 对比条（依次为：急刹车、急转弯、急加速、高转速）
    
    private String monthInitialValue = "";
    
    private AsyncImageLoader mAsyncImageLoader = AsyncImageLoader.getInstance();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_month);
        setTitleView(R.layout.head_back);

        initTitle();
        init();
        
        try {
            monthInitialValue = getIntent().getStringExtra(ReportActivity.MONTH_INITIAL);
        } catch (Exception e) {
            // TODO: handle exception
        }
        if (monthInitialValue == null || monthInitialValue.equals("")) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            monthInitialValue = format.format(Calendar.getInstance().getTime());
        }
        
        LoadData();
    }

    private void initTitle() {
        back = (ImageView)findViewById(R.id.head_back_img1);
        title = (TextView)findViewById(R.id.head_back_txt1);
        txtRight = (TextView)findViewById(R.id.head_back_txt2);

        back.setImageResource(R.drawable.arrow_back);
        txtRight.setText("晒一晒");
        txtRight.setVisibility(View.GONE);

        back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void init() {
        mTxtName = (TextView)findViewById(R.id.head_userinfo_txt_name);

        mTxtMonth = (TextView)findViewById(R.id.report_month_txt_date);
        mTxtScore = (TextView)findViewById(R.id.report_month_txt_score);
        mTxtScorePersent=(TextView)findViewById(R.id.report_month_txt_score_pesent);
        mTxtScoreDes = (TextView)findViewById(R.id.report_month_txt_scoredes);

        mTxtOilTotal = (TextView)findViewById(R.id.report_month_txt_oiltotal);
        mTxtOilTotalDes = (TextView)findViewById(R.id.report_month_txt_oiltotal_des);
        mTxtOilTotalUnit=(TextView)findViewById(R.id.report_month_txt_oiltotal_unit);

        mTxtOilAvg = (TextView)findViewById(R.id.report_month_txt_oilavg);
        
        mTxtMileage = (TextView)findViewById(R.id.report_month_txt_mileage);
        mTxtMileageDes = (TextView)findViewById(R.id.report_month_txt_mileage_des);

        mTxtHour = (TextView)findViewById(R.id.report_month_txt_hour);
        mTxtHourUnit = (TextView)findViewById(R.id.report_month_txt_hourunit);
        mTxtMin = (TextView)findViewById(R.id.report_month_txt_min);
        mTxtMinUint = (TextView)findViewById(R.id.report_month_txt_minunit);
        mTxtDurationDes = (TextView)findViewById(R.id.report_month_txt_duration_des);

        mTxtSpeedMax = (TextView)findViewById(R.id.report_month_txt_speedmax);
        mTxtSpeedMaxDes = (TextView)findViewById(R.id.report_month_txt_speedmax_des);

        mTxtSpeedAvg = (TextView)findViewById(R.id.report_month_txt_speedavg);
        mTxtSpeedAvgDes = (TextView)findViewById(R.id.report_month_txt_speedavg_des);
        
        mTxtMy1 = (TextView)findViewById(R.id.report_pk_relative1_txt2);
        mTxtType1 = (TextView)findViewById(R.id.report_pk_relative1_txt3);
        mTxtDesc1 = (TextView)findViewById(R.id.report_pk_relative1_txt4);

        mTxtMy2 = (TextView)findViewById(R.id.report_pk_relative2_txt2);
        mTxtType2 = (TextView)findViewById(R.id.report_pk_relative2_txt3);
        mTxtDesc2 = (TextView)findViewById(R.id.report_pk_relative2_txt4);

        mTxtMy3 = (TextView)findViewById(R.id.report_pk_relative3_txt2);
        mTxtType3 = (TextView)findViewById(R.id.report_pk_relative3_txt3);
        mTxtDesc3 = (TextView)findViewById(R.id.report_pk_relative3_txt4);

        mTxtMy4 = (TextView)findViewById(R.id.report_pk_relative4_txt2);
        mTxtType4 = (TextView)findViewById(R.id.report_pk_relative4_txt3);
        mTxtDesc4 = (TextView)findViewById(R.id.report_pk_relative4_txt4);

        mImgAvadar = (ImageView)findViewById(R.id.head_userinfo_img_avadar);
        mImgSex = (ImageView)findViewById(R.id.head_userinfo_img_sex);
        mImgCar = (ImageView)findViewById(R.id.head_userinfo_img_car);
        
        mImg1 = (ImageView)findViewById(R.id.report_pk_relative1_img);
        mImg2 = (ImageView)findViewById(R.id.report_pk_relative2_img);
        mImg3 = (ImageView)findViewById(R.id.report_pk_relative3_img);
        mImg4 = (ImageView)findViewById(R.id.report_pk_relative4_img);
        
        mValueBar=(CircleValueBar)findViewById(R.id.report_month_valueBar);
        
        mSeekBars = new ArrayList<SeekBar>();

        mSeekBars.add((SeekBar)findViewById(R.id.report_pk_relative1_seekbar));
        mSeekBars.add((SeekBar)findViewById(R.id.report_pk_relative2_seekbar));
        mSeekBars.add((SeekBar)findViewById(R.id.report_pk_relative3_seekbar));
        mSeekBars.add((SeekBar)findViewById(R.id.report_pk_relative4_seekbar));

    }

    @Override
    protected void LoadSuccess(Object data) {
        final ReportMonthInfo mReportMonthInfo = (ReportMonthInfo)data;
        
        txtRight.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // 晒一晒
//                ShareControl.share(ReportMonthActivity.class, MonthActivity.this,
//                        mReportMonthInfo.getShareTitle(),
//                        mReportMonthInfo.getShareText(),
//                        mReportMonthInfo.getShareLink(), R.drawable.friends_share_img);

                CPControl.GetReportShareResult(mReportMonthInfo.getShareTitle(),
                        mReportMonthInfo.getShareText(),
                        mReportMonthInfo.getShareLink(),
                        mReportMonthInfo.getReportDate(), CPControl.REPORT_MONTH);

            }
        });
        
        mTxtName.setText(LoginInfo.getRealname());
        if (!LoginInfo.getAvatar_img().equals("")) {
            if (mAsyncImageLoader.getBitmapByUrl(LoginInfo.getAvatar_img()) != null) {
                mImgAvadar.setImageBitmap(mAsyncImageLoader.getBitmapByUrl(LoginInfo.getAvatar_img()));
            }
        } else {
            mImgAvadar.setImageResource(R.drawable.icon_default_head);
        }
        if (LoginInfo.getGender().equals(LoginInfo.GENDER_NAN)) {
            mImgSex.setImageResource(R.drawable.icon_sex_male);
        } else if (LoginInfo.getGender().equals(LoginInfo.GENDER_NV)) {
            mImgSex.setImageResource(R.drawable.icon_sex_female);
        } else {
            mImgSex.setImageResource(R.drawable.icon_sex_secret);
        }

        if (!LoginInfo.getCarlogo().equals("")) {
            if (mAsyncImageLoader.getBitmapByUrl(LoginInfo.getCarlogo()) != null) {
                mImgCar.setImageBitmap(mAsyncImageLoader.getBitmapByUrl(LoginInfo.getCarlogo()));
            }
        } else {
            mImgCar.setImageResource(R.drawable.default_car_small);
        }
        
        if (null != mReportMonthInfo.getReportDate()
                && !mReportMonthInfo.getReportDate().equals("")) {
            String[] s1 = mReportMonthInfo.getReportDate().split("-");
            StringBuffer mStringBuffer = new StringBuffer();
            if (s1.length > 1) {
                mStringBuffer.append(s1[0]);
                mStringBuffer.append("年");
                mStringBuffer.append(s1[1]);
                mStringBuffer.append("月行车报告");
                mTxtMonth.setText(mStringBuffer.toString());
            } else {
                mTxtMonth.setText("月行车报告");
            }

        }

        mTxtScore.setText(mReportMonthInfo.getAvgPoint() + "");
        mTxtScore.setTypeface(GetTypeFace.typefaceBold(null));
        mTxtScore.setTextColor(MyParse.getColorByPoint(mReportMonthInfo.getAvgPoint()));

        if (mReportMonthInfo.getAvgPointcompare() > 0) {
            mTxtScoreDes.setText("车技较上月提升");
            mTxtScorePersent.setText(mReportMonthInfo.getAvgPointcompare() + "%");
            mTxtScorePersent.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_score_up,
                    0);
        } else if (mReportMonthInfo.getAvgPointcompare() == 0) {
            mTxtScoreDes.setText("您的水平很稳定");
        } else if (mReportMonthInfo.getAvgPointcompare() == 0) {
            mTxtScoreDes.setText("车技较上月下降");
            mTxtScorePersent.setText(mReportMonthInfo.getAvgPointcompare() + "%");
            mTxtScorePersent.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                    R.drawable.arrow_score_down, 0);
        }
        
        mTxtOilTotal.setTypeface(GetTypeFace.typefaceBold(null));
        int sumfuel = MyParse.parseInt(mReportMonthInfo.getSumFuel());
        if (sumfuel < 100) {

            mTxtOilTotal.setText(sumfuel + "");
            mTxtOilTotalUnit.setText("毫升");
        } else {
            double f = sumfuel / 1000.0;
            mTxtOilTotal.setText(String.format("%.2f", f));
            mTxtOilTotalUnit.setText("升");
        }

        mTxtOilTotalDes.setText(mReportMonthInfo.getSumfueldesc());

        mTxtOilAvg.setText(mReportMonthInfo.getAvgFuel() + "");
        mTxtOilAvg.setTypeface(GetTypeFace.typefaceBold(null));

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
        
        mTxtMileage.setText(mReportMonthInfo.getSumMiles() + "");
        mTxtMileage.setTypeface(GetTypeFace.typefaceBold(null));
        mTxtMileageDes.setText(mReportMonthInfo.getSummilesdesc());
        
        mTxtHour.setTypeface(GetTypeFace.typefaceBold(null));
        mTxtMin.setTypeface(GetTypeFace.typefaceBold(null));
        
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
                mTxtHour.setText(hour);
            } else {
                mTxtHour.setVisibility(View.GONE);
                mTxtHourUnit.setVisibility(View.GONE);
            }
            if (min != null && min.length() > 0) {
                mTxtMin.setText(min);
            } else {
                mTxtMin.setVisibility(View.GONE);
                mTxtMinUint.setVisibility(View.GONE);
            }
        }

        mTxtDurationDes.setText(mReportMonthInfo.getSumtimedesc());
        
        
        mTxtSpeedMax.setText(mReportMonthInfo.getMaxSpeed() + "");
        mTxtSpeedMax.setTypeface(GetTypeFace.typefaceBold(null));

        if (null != mReportMonthInfo.getMaxspeeddesc()) {
            mTxtSpeedMaxDes.setText(mReportMonthInfo.getMaxspeeddesc());
        }

        mTxtSpeedAvg.setText(mReportMonthInfo.getAvgSpeed() + "");
        mTxtSpeedAvg.setTypeface(GetTypeFace.typefaceBold(null));

        if (null != mReportMonthInfo.getAvgspeeddesc()) {
            mTxtSpeedAvgDes.setText(mReportMonthInfo.getAvgspeeddesc());
        }
        
        valueMy[0] = mReportMonthInfo.getBrake();
        valueMy[1] = mReportMonthInfo.getTurn();
        valueMy[2] = mReportMonthInfo.getSpeedup();
        valueMy[3] = mReportMonthInfo.getOverSpeed();

        valueType[0] = mReportMonthInfo.getTypebrake();
        valueType[1] = mReportMonthInfo.getTypeturn();
        valueType[2] = mReportMonthInfo.getTypespeedup();
        valueType[3] = mReportMonthInfo.getTypeoverSpeed();

        mTxtMy1.setText(valueMy[0] + "");
        mTxtMy2.setText(valueMy[1] + "");
        mTxtMy3.setText(valueMy[2] + "");
        mTxtMy4.setText(valueMy[3] + "");

        mTxtType1.setText(valueType[0] + "");
        mTxtType2.setText(valueType[1] + "");
        mTxtType3.setText(valueType[2] + "");
        mTxtType4.setText(valueType[3] + "");

        mTxtDesc1.setText(mReportMonthInfo.getBrakedesc());
        mTxtDesc2.setText(mReportMonthInfo.getTurndesc());
        mTxtDesc3.setText(mReportMonthInfo.getSpeedupdesc());
        mTxtDesc4.setText(mReportMonthInfo.getOverspeeddesc());

        setSeekbar();
        
        super.LoadSuccess(data);
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
                mSeekBars.get(i).setProgress(valueMy[i]);
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
    protected void LoadErro(Object erro) {
        super.LoadErro(erro);
    }

    @Override
    protected void LoadData() {
        super.LoadData();
        if (monthInitialValue != null && monthInitialValue.length() > 0) {
            String[] strings = monthInitialValue.split("-");
            StringBuffer titleBuffer = new StringBuffer();
            if (strings.length > 1) {
                titleBuffer.append(strings[0]);
                titleBuffer.append("年");
                titleBuffer.append(strings[1]);
                titleBuffer.append("月");
            }
            titleBuffer.append("行车报告");
            title.setText(titleBuffer.toString());
        }
        CPControl.GetMonthReportResult(monthInitialValue, listener);
    
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
    }

    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == ReportActivity.MENU) {
            selectDate();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 弹出日期选择框
     */
    private void selectDate() {
        CalendarMonth mCalendarMonth = new CalendarMonth(MonthActivity.this,
                mCalendarMonthClick);
        mCalendarMonth.showMenu();
    }

    private OnCalendarMonthClick mCalendarMonthClick = new OnCalendarMonthClick() {

        @Override
        public void onClick(String date) {
            monthInitialValue=date;
            LoadData();

        }
    };
    
    @Override
    public void OnImgLoadFinished(String url, Bitmap mBitmap) {
        super.OnImgLoadFinished(url, mBitmap);
        if (url != null && url.equals(LoginInfo.getAvatar_img()) && mBitmap != null) {
            mImgAvadar.setImageBitmap(mBitmap);
        } else if (url != null && url.equals(LoginInfo.getCarlogo()) && mBitmap != null) {
            mImgCar.setImageBitmap(mBitmap);
        }
    }
}

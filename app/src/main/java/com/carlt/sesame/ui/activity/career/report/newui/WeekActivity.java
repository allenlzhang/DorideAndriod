package com.carlt.sesame.ui.activity.career.report.newui;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.carlt.doride.DorideApplication;
import com.carlt.sesame.R;
import com.carlt.sesame.control.CPControl;
import com.carlt.sesame.data.LoginInfo;
import com.carlt.sesame.data.career.ReportWeekChatInfo;
import com.carlt.sesame.data.career.ReportWeekInfo;
import com.carlt.sesame.http.AsyncImageLoader;
import com.carlt.sesame.ui.activity.base.LoadingActivityWithTitle;
import com.carlt.sesame.ui.activity.career.report.CalendarWeek;
import com.carlt.sesame.ui.activity.career.report.CalendarWeek.OnCalendarWeekClick;
import com.carlt.sesame.ui.activity.career.report.ReportActivity;
import com.carlt.sesame.ui.activity.career.report.ReportChartView;
import com.carlt.sesame.ui.view.CircleValueBar;
import com.carlt.sesame.utility.MyParse;
import com.carlt.sesame.utility.MyTimeUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class WeekActivity extends LoadingActivityWithTitle{

    private ImageView back;// 头部返回键

    private TextView title;// 标题文字

    private TextView txtRight;// 头部右侧文字

    private TextView mTxtName;// 用户姓名

    private TextView mTxtScore;// 分数

    private TextView mTxtLable1;// 驾驶诊断标签1

    private TextView mTxtLable2;// 驾驶诊断标签2

    private TextView mTxtLable3;// 驾驶诊断标签3
    
    private TextView mTxtGeneral;// 总体评价内容

    private TextView mTxtEvalue;// 评价（高、中、低）
    
    private TextView mTxtTime;// 行车时间

    private TextView mTxtSpeedMax;// 最高速度

    private TextView mTxtSpeedAvg;// 平均时速
    
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
    
    private ArrayList<ReportWeekChatInfo> dataList;//图标数据list
    
    private ReportChartView mReportChartView;// 图表视图
    
    private CircleValueBar mValueBar;//糖葫芦view

    private ArrayList<SeekBar> mSeekBars;// 对比条（依次为：急刹车、急转弯、急加速、高转速）
    
    private AsyncImageLoader mAsyncImageLoader = AsyncImageLoader.getInstance();
    
    private  String weekInitialValue = "";
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_week);
        setTitleView(R.layout.head_back);
        
        initTitle();
        init();
        
        try {
            weekInitialValue = getIntent().getStringExtra(
                    ReportActivity.WEEK_INITIAL);
        } catch (Exception e) {
            // TODO: handle exception
        }
        if (weekInitialValue == null || weekInitialValue.equals("")) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Calendar today = Calendar.getInstance();
            today.add(Calendar.DATE, -7);
            weekInitialValue = format.format(today.getTime());
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

        mTxtScore = (TextView)findViewById(R.id.head_drivetab_txt_score);
        mTxtLable1 = (TextView)findViewById(R.id.head_drivetab_lable1);
        mTxtLable2 = (TextView)findViewById(R.id.head_drivetab_lable2);
        mTxtLable3 = (TextView)findViewById(R.id.head_drivetab_lable3);

        mTxtGeneral = (TextView)findViewById(R.id.report_compare_txt1);
        mTxtEvalue = (TextView)findViewById(R.id.report_compare_txt2);

        mTxtTime = (TextView)findViewById(R.id.report_compare_txt3);
        mTxtSpeedMax = (TextView)findViewById(R.id.report_compare_txt4);
        mTxtSpeedAvg = (TextView)findViewById(R.id.report_compare_txt5);

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

        mReportChartView=(ReportChartView)findViewById(R.id.report_week_reportchartview);
        
        mValueBar=(CircleValueBar)findViewById(R.id.report_compare_valuebar);

        mSeekBars = new ArrayList<SeekBar>();

        mSeekBars.add((SeekBar)findViewById(R.id.report_pk_relative1_seekbar));
        mSeekBars.add((SeekBar)findViewById(R.id.report_pk_relative2_seekbar));
        mSeekBars.add((SeekBar)findViewById(R.id.report_pk_relative3_seekbar));
        mSeekBars.add((SeekBar)findViewById(R.id.report_pk_relative4_seekbar));
    }

    @Override
    protected void LoadSuccess(Object data) {
        final ReportWeekInfo mReportWeekInfo=(ReportWeekInfo)data;
        
        txtRight.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // 晒一晒
//                ShareControl.share(ReportWeekActivity.class,
//                        WeekActivity.this,
//                        mReportWeekInfo.getShareTitle(),
//                        mReportWeekInfo.getShareText(),
//                        mReportWeekInfo.getShareLink(),
//                        R.drawable.friends_share_img);

                CPControl.GetReportShareResult(
                        mReportWeekInfo.getShareTitle(),
                        mReportWeekInfo.getShareText(),
                        mReportWeekInfo.getShareLink(),
                        mReportWeekInfo.getReportDate(),
                        CPControl.REPORT_WEEK);

            }
        });
        mTxtName.setText(LoginInfo.getRealname());
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
            //mTextView3.setText(mStringBuffer.toString());
        }
        
        mTxtScore.setText(mReportWeekInfo.getAvgpoint() + "");
        
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
        
        String tag = mReportWeekInfo.getTag();
        if (null != tag && tag.length() > 0) {
            String[] tags = tag.split(",");
            if (tags != null && tags.length > 0) {
                mTxtLable1.setText(tags[0]);
                mTxtLable1.setVisibility(View.VISIBLE);

                if (tags.length == 2) {
                    mTxtLable2.setText(tags[1]);
                    mTxtLable2.setVisibility(View.VISIBLE);
                } else if (tags.length >= 3) {
                    mTxtLable2.setText(tags[1]);
                    mTxtLable2.setVisibility(View.VISIBLE);

                    mTxtLable3.setText(tags[2]);
                    mTxtLable3.setVisibility(View.VISIBLE);
                }
            }

        }
        
        dataList = mReportWeekInfo.getmChatList();
        if (dataList != null && dataList.size() > 1) {
            Message msg = new Message();
            msg.what = 1;
            msg.obj = dataList;
            mHandler.sendMessage(msg);

            mReportChartView.setVisibility(View.VISIBLE);
        } else {
            mReportChartView.setVisibility(View.GONE);
        }
        
        
        String avgFuel = mReportWeekInfo.getAvgFuel();
        float avgFuelF = 0;
        String sumMiles = mReportWeekInfo.getSumMiles();
        String sumFuel = mReportWeekInfo.getSumFuel();
        if (sumMiles != null && sumMiles.length() > 0 && sumFuel != null
                && sumFuel.length() > 0 && avgFuel != null
                && avgFuel.length() > 0) {
            StringBuffer mStringBuffer = new StringBuffer("今日行驶");
            mStringBuffer.append(sumMiles);
            mStringBuffer.append("公里，油耗");
            mStringBuffer.append(sumFuel);
            mStringBuffer.append("，平均油耗");
            mStringBuffer.append(avgFuel);
            mStringBuffer.append("升/百公里。");
            mTxtGeneral.setText(mStringBuffer);

            avgFuelF = MyParse.parseFloat(avgFuel);
        } else {
            mTxtGeneral.setText("本周行驶 公里，油耗 升，平均油耗 升/百公里。");
        }

        String typeAvgFuel = mReportWeekInfo.getTypeAvgFuel();
        float typeAvgFuelF = 0;
        if (typeAvgFuel != null && typeAvgFuel.length() > 0) {
            typeAvgFuelF = MyParse.parseFloat(typeAvgFuel);
        }

        if (avgFuelF > typeAvgFuelF) {
            mTxtEvalue.setText("高");
            mTxtEvalue
                    .setBackgroundDrawable(DorideApplication.ApplicationContext
                            .getResources().getDrawable(
                                    R.drawable.text_bg_cycle_red));
        } else {
            mTxtEvalue.setText("低");
            mTxtEvalue
                    .setBackgroundDrawable(DorideApplication.ApplicationContext
                            .getResources().getDrawable(
                                    R.drawable.text_bg_cycle_green));
        }

        String sumtime = mReportWeekInfo.getSumtime_str();
        if (sumtime != null && sumtime.length() > 0) {
            mTxtTime.setText(sumtime);
        } else {
            mTxtTime.setText("分钟");
        }

        String maxSpeed = mReportWeekInfo.getMaxSpeed();
        if (maxSpeed != null && maxSpeed.length() > 0) {
            mTxtSpeedMax.setText(mReportWeekInfo.getMaxSpeed() + "公里/小时");
        } else {
            mTxtSpeedMax.setText("公里/小时");
        }

        String avgSpeed = mReportWeekInfo.getAvgSpeed();
        if (avgSpeed != null && avgSpeed.length() > 0) {
            mTxtSpeedAvg.setText(mReportWeekInfo.getAvgSpeed() + "公里/小时");
        } else {
            mTxtSpeedAvg.setText("公里/小时");
        }

        String allAvgFuel = mReportWeekInfo.getAllAvgFuel();
        float allAvgFuelF = 0;
        if (allAvgFuel != null && allAvgFuel.length() > 0) {
            allAvgFuelF = MyParse.parseFloat(allAvgFuel);
        }
        mValueBar.setValueWithType(avgFuelF, typeAvgFuelF, allAvgFuelF);
        
        valueMy[0] = mReportWeekInfo.getBrake();
        valueMy[1] = mReportWeekInfo.getTurn();
        valueMy[2] = mReportWeekInfo.getSpeedup();
        valueMy[3] = mReportWeekInfo.getOverSpeed();

        valueType[0] = mReportWeekInfo.getTypebrake();
        valueType[1] = mReportWeekInfo.getTypeturn();
        valueType[2] = mReportWeekInfo.getTypespeedup();
        valueType[3] = mReportWeekInfo.getTypeoverSpeed();

        mTxtMy1.setText(valueMy[0] + "");
        mTxtMy2.setText(valueMy[1] + "");
        mTxtMy3.setText(valueMy[2] + "");
        mTxtMy4.setText(valueMy[3] + "");

        mTxtType1.setText(valueType[0] + "");
        mTxtType2.setText(valueType[1] + "");
        mTxtType3.setText(valueType[2] + "");
        mTxtType4.setText(valueType[3] + "");

        mTxtDesc1.setText(mReportWeekInfo.getBrakedesc());
        mTxtDesc2.setText(mReportWeekInfo.getTurndesc());
        mTxtDesc3.setText(mReportWeekInfo.getSpeedupdesc());
        mTxtDesc4.setText(mReportWeekInfo.getOverspeeddesc());

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
        // TODO Auto-generated method stub
        super.LoadErro(erro);
    }

    @Override
    protected void LoadData() {
        super.LoadData();

        if (null != weekInitialValue && weekInitialValue.length() > 0) {
            StringBuffer mStringBuffer = new StringBuffer();

            String firstdayOfWeek = MyTimeUtil.getFirstdayOfWeek(weekInitialValue);
            int number = MyTimeUtil.getWeekOfMonth(weekInitialValue);
            if (firstdayOfWeek != null && firstdayOfWeek.length() > 0
                    && number > 0) {
                String[] s1 = firstdayOfWeek.split("-");
                if (s1.length > 2) {
                    mStringBuffer.append(s1[0]);
                    mStringBuffer.append("年");
                    mStringBuffer.append(s1[1]);
                    mStringBuffer.append("月第");
                    mStringBuffer.append(number);
                    mStringBuffer.append("周行车报告");
                }
                title.setText(mStringBuffer.toString());
            } else {
                title.setText("周报");
            }

        } else {
            title.setText("周报");
        }

        CPControl.GetWeekReportResult(weekInitialValue, listener);
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
        CalendarWeek mCalendarWeek = new CalendarWeek(WeekActivity.this,
                mOnCalendarWeekClick);
        mCalendarWeek.showMenu();
    }

    private OnCalendarWeekClick mOnCalendarWeekClick = new OnCalendarWeekClick() {

        @Override
        public void onClick(String date) {
            weekInitialValue=date;
            LoadData();

        }
    };
    
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
    public void OnImgLoadFinished(String url, Bitmap mBitmap) {
        if (url != null && url.equals(LoginInfo.getAvatar_img()) && mBitmap != null) {
            mImgAvadar.setImageBitmap(mBitmap);
        } else if (url != null && url.equals(LoginInfo.getCarlogo()) && mBitmap != null) {
            mImgCar.setImageBitmap(mBitmap);
        }
    }
    
}

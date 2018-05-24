package com.carlt.doride.ui.activity.home;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.widget.TextView;

import com.carlt.doride.DorideApplication;
import com.carlt.doride.R;
import com.carlt.doride.base.LoadingActivity;
import com.carlt.doride.control.CPControl;
import com.carlt.doride.data.BaseResponseInfo;
import com.carlt.doride.data.home.MonthStatisticChartInfo;
import com.carlt.doride.data.home.ReportMonthInfo;
import com.carlt.doride.protocolparser.BaseParser;
import com.carlt.doride.ui.view.BarGraph;
import com.carlt.doride.ui.view.CalendarMonth;
import com.carlt.doride.utils.MyParse;
import com.carlt.doride.utils.MyTimeUtils;
import com.carlt.doride.utils.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MonthActivity extends LoadingActivity {

    private TextView mTxtSumFuel;    //本月总油耗
    private TextView mTxtSumMailes;    //本月总行程
    private TextView mTxtMaxSpeed;    //最高时速
    private TextView mTxtAvgFuel;
    ;    //平均油耗
    private TextView mTxtSumTime;    //本月行车时间
    private TextView mTxtAvgSpeed;    //本月平均速度


    private String monthInitialValue = "";

    //	private AsyncImageLoader mAsyncImageLoader = AsyncImageLoader.getInstance();


    private TextView mTxtFuel;// 从插上OBD盒子到现在的总油耗
    private BarGraph mBarGraghFuel;// 油耗柱状图

    private TextView mTxtMiles;// 从插上OBD盒子到现在的总里程
    private BarGraph mBarGraghMiles;// 里程柱状图

    private TextView mTxtTime;// 从插上OBD盒子到现在的总时间
    private BarGraph mBarGraghTime;// 时间柱状图


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_month);
        initTitle("行车月报");
        initView();
        //		try {
        //			monthInitialValue = getIntent().getStringExtra(
        //					ReportActivity.MONTH_INITIAL);
        //		} catch (Exception e) {
        //			// TODO: handle exception
        //		}
        if (monthInitialValue == null || monthInitialValue.equals("")) {
            monthInitialValue = getMonthAgo(Calendar.getInstance().getTime());
        }
        loadingDataUI();
        initData();

    }

    /**
     * 获取一个月前的日期
     * @param date
     *         传入的日期
     * @return
     */
    public static String getMonthAgo(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, -1);
        String monthAgo = simpleDateFormat.format(calendar.getTime());
        return monthAgo;
    }

    private void initData() {
        if (monthInitialValue != null && monthInitialValue.length() > 0) {
            StringBuffer titleBuffer = new StringBuffer();
            if (monthInitialValue.equals("0000-00-00")) {
                titleBuffer.append(MyTimeUtils.getDateFormat6());
                titleBuffer.append("行车月报");
            } else {
                String[] strings = monthInitialValue.split("-");

                if (strings.length > 1) {
                    titleBuffer.append(strings[0]);
                    titleBuffer.append("年");
                    int month = MyParse.parseInt(strings[1]);
                    if (month < 10) {
                        titleBuffer.append("0");
                    }
                    titleBuffer.append(month);
                    titleBuffer.append("月");
                }
                titleBuffer.append("行车月报");
            }
            initTitle(titleBuffer.toString());
        }

        CPControl.GetMonthReportResult(listener_month, monthInitialValue);
    }

    private void initView() {
        mTxtFuel = (TextView) findViewById(R.id.report_month_txt_fuel);
        mBarGraghFuel = (BarGraph) findViewById(R.id.report_month_bargraph_fuel);

        mTxtMiles = (TextView) findViewById(R.id.report_month_txt_miles);
        mBarGraghMiles = (BarGraph) findViewById(R.id.report_month_bargraph_miles);

        mTxtTime = (TextView) findViewById(R.id.report_month_txt_time);
        mBarGraghTime = (BarGraph) findViewById(R.id.report_month_bargraph_time);


        mTxtSumFuel = findViewById(R.id.item_report_month_txt_value1);
        mTxtSumMailes = findViewById(R.id.item_report_month_txt_value2);
        mTxtMaxSpeed = findViewById(R.id.item_report_month_txt_value3);
        mTxtAvgFuel = findViewById(R.id.item_report_month_txt_value4);
        mTxtSumTime = findViewById(R.id.item_report_month_txt_value5);
        mTxtAvgSpeed = findViewById(R.id.item_report_month_txt_value6);

    }

    private ReportMonthInfo mReportMonthInfo;

    @Override
    public void loadDataSuccess(Object data) {
        super.loadDataSuccess(data);
        if (!StringUtils.isEmpty(mReportMonthInfo.getSumfuel())) {
            mTxtSumFuel.setText(mReportMonthInfo.getSumfuel());
        } else {
            mTxtSumFuel.setText("--");
        }

        if (!StringUtils.isEmpty(mReportMonthInfo.getSummiles())) {
            mTxtSumMailes.setText(mReportMonthInfo.getSummiles());
        } else {
            mTxtSumMailes.setText("--");
        }

        if (!StringUtils.isEmpty(mReportMonthInfo.getMaxspeed())) {
            mTxtMaxSpeed.setText(mReportMonthInfo.getMaxspeed());
        } else {
            mTxtMaxSpeed.setText("--");
        }

        if (!StringUtils.isEmpty(mReportMonthInfo.getAvgfuel())) {
            mTxtAvgFuel.setText(mReportMonthInfo.getAvgfuel());
        } else {
            mTxtAvgFuel.setText("--");
        }

        if (!StringUtils.isEmpty(mReportMonthInfo.getSumtime())) {
            mTxtSumTime.setText(mReportMonthInfo.getSumtime());
        } else {
            mTxtSumTime.setText("--");
        }

        if (!StringUtils.isEmpty(mReportMonthInfo.getAvgspeed())) {
            mTxtAvgSpeed.setText(mReportMonthInfo.getAvgspeed());
        } else {
            mTxtAvgSpeed.setText("--");
        }

        MonthStatisticChartInfo mMonthStatisticChartInfo = (MonthStatisticChartInfo) ((BaseResponseInfo) data).getValue();
        if (mMonthStatisticChartInfo != null) {
            mTxtFuel.setText(monthInitialValue.split("-")[0] + "年");
            mBarGraghFuel.setMonthStatisticCharInfo(mMonthStatisticChartInfo);
            mBarGraghFuel.setBarColor(getResources().getColor(R.color.orange));
            mBarGraghFuel.setTxtColor(getResources().getColor(R.color.text_color_gray1));
            mBarGraghFuel.setTextSize(DorideApplication.dpToPx(12));
            mBarGraghFuel.setType(BarGraph.TYPE_FUEL);

            mTxtMiles.setText(monthInitialValue.split("-")[0] + "年");
            mBarGraghMiles.setMonthStatisticCharInfo(mMonthStatisticChartInfo);
            mBarGraghMiles.setBarColor(getResources().getColor(R.color.bar_gragh_miles_color));
            mBarGraghMiles.setTxtColor(getResources().getColor(R.color.text_color_gray1));
            mBarGraghMiles.setTextSize(DorideApplication.dpToPx(12));
            mBarGraghMiles.setType(BarGraph.TYPE_MILES);

            mTxtTime.setText(monthInitialValue.split("-")[0] + "年");
            mBarGraghTime.setMonthStatisticCharInfo(mMonthStatisticChartInfo);
            mBarGraghTime.setBarColor(getResources().getColor(R.color.bar_gragh_time_color));
            mBarGraghTime.setTxtColor(getResources().getColor(R.color.text_color_gray1));
            mBarGraghTime.setTextSize(DorideApplication.dpToPx(12));
            mBarGraghTime.setType(BarGraph.TYPE_TIME);
        }

        if (monthInitialValue.equals("0000-00-00")) {
        } else {
            String[] strings = monthInitialValue.split("-");
            if (strings.length > 1) {
                int month = MyParse.parseInt(strings[1]);
                mBarGraghFuel.setLightNum(month);
                mBarGraghMiles.setLightNum(month);
                mBarGraghTime.setLightNum(month);
            }
        }
    }



    private BaseParser.ResultCallback listener_month = new BaseParser.ResultCallback() {

        @Override
        public void onSuccess(BaseResponseInfo bInfo) {
            Message msg = new Message();
            msg.what = 2;
            msg.obj = bInfo;
            mHandler.sendMessage(msg);
        }

        @Override
        public void onError(BaseResponseInfo bInfo) {
            Message msg = new Message();
            msg.what = 3;
            msg.obj = bInfo;
            mHandler.sendMessage(msg);
        }

    };

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2:
                    //拉取月报数据成功
                    mReportMonthInfo = (ReportMonthInfo) ((BaseResponseInfo) msg.obj).getValue();
                    CPControl.GetMonthReportLogResult(mCallback, monthInitialValue);
                    break;
                case 3:
                    //拉取月报数据失败
                    loadonErrorUI((BaseResponseInfo) msg.obj);
//                    mIvErrorIcon.setImageResource(R.mipmap.icon_nodata);
                    loadDataError(msg.obj);
                    break;
            }
        }

    };

    @Override
    public void reTryLoadData() {
        super.reTryLoadData();
        initData();
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

    private CalendarMonth.OnCalendarMonthClick mCalendarMonthClick = new CalendarMonth.OnCalendarMonthClick() {

        @Override
        public void onClick(String date) {
            monthInitialValue = date;
            loadingDataUI();
            initData();
        }
    };

    //	@Override
    //	public void OnImgLoadFinished(String url, Bitmap mBitmap) {
    //		super.OnImgLoadFinished(url, mBitmap);
    //		if (url != null && url.equals(LoginInfo.getAvatar_img())
    //				&& mBitmap != null) {
    //			Bitmap bitmap = ImageUtils.getRoundedCornerBitmap(mBitmap, 20);
    //			mImgAvadar.setImageBitmap(bitmap);
    //		}
    //	}
}

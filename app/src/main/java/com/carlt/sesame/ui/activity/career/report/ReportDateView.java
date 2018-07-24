
package com.carlt.sesame.ui.activity.career.report;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.carlt.doride.R;
import com.carlt.sesame.control.CPControl;
import com.carlt.sesame.control.CPControl.GetResultListCallback;
import com.carlt.sesame.data.BaseResponseInfo;
import com.carlt.sesame.data.career.ReportCalendarDayInfo;
import com.carlt.sesame.utility.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

public class ReportDateView extends LinearLayout implements OnClickListener {
    private Context context;

    private TextView Top_Date;

    private ImageView btn_pre_month;

    private ImageView btn_next_month;

    private View mLoadingLayoutMain;
    
    private View mLoadingLayout;

    private TextView mLoadingTextView;

    private View mLoadingBar;

    private Calendar today = Calendar.getInstance();

    private OnItemClick mOnItemClick;

    private ArrayList<ReportDayView> mDayList = new ArrayList<ReportDayView>();

    private ArrayList<ReportCalendarDayInfo> mList;

    private View mLayError;// 错误信息展示layout

    private TextView mTextError;// 错误信息展示text

    private TextView mTextRetry;// 重试按钮

    private int start = 0;

    public ReportDateView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public ReportDateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        LayoutInflater.from(context).inflate(R.layout.report_rili_date, this, true);
        LinearLayout mainLayout = (LinearLayout)findViewById(R.id.date_view_layout);
        // 声明控件，并绑定事件
        Top_Date = (TextView)findViewById(R.id.head_calender_txt);
        btn_pre_month = (ImageView)findViewById(R.id.head_calender_img1);
        btn_next_month = (ImageView)findViewById(R.id.head_calender_img2);
        btn_pre_month.setOnClickListener(this);
        btn_next_month.setOnClickListener(this);
        // Loading控件
        mLoadingLayoutMain = findViewById(R.id.loading_activity_mainlayout);
        mLoadingLayout=findViewById(R.id.loading_activity_loading_lay);
        mLoadingTextView = (TextView)findViewById(R.id.loading_activity_loading_text);
        mLoadingBar = findViewById(R.id.loading_activity_loading_bar);
        mLoadingLayout.setOnClickListener(this);

        // 错误展示lay
        mLayError = findViewById(R.id.loading_activity_lay_error);
        mTextError = (TextView)findViewById(R.id.loading_activity_text_error);
        mTextRetry = (TextView)findViewById(R.id.loading_activity_text_retry);

        mTextRetry.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                load(year, month);
            }
        });
        mainLayout.addView(CreatCalendarMain());

    }

    private int year;

    private int month;

    public void load(int y, int m) {
        showLoading();
        year = y;
        month = m;
        Top_Date.setText(year + "年" + month + "月");
        Calendar mCalendar = Calendar.getInstance();
        mCalendar.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        mCalendar.set(year, month - 1, 1);
        int w = mCalendar.get(Calendar.DAY_OF_WEEK);
        start = w - 1;
        GetResultListCallback listener = new GetResultListCallback() {

            @Override
            public void onFinished(Object o) {
                mList = (ArrayList<ReportCalendarDayInfo>)o;
                Message msg = new Message();
                msg.what = 0;
                mHandler.sendMessage(msg);

            }

            @Override
            public void onErro(Object o) {
                Message msg = new Message();
                msg.what = 1;
                msg.obj=o;
                mHandler.sendMessage(msg);

            }
        };
        CPControl.GetUserDayPointResult(listener, year + "-" + month + "-02");
    }

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case 0:
                    // 拉取数据成功
                    if (year == today.get(Calendar.YEAR) && month >= today.get(Calendar.MONTH) + 1) {
                        btn_next_month.setImageResource(R.drawable.arrow_calendar_right_unselected);
                        btn_next_month.setClickable(false);

                    } else {

                        btn_next_month.setImageResource(R.drawable.arrow_calendar_right);
                        btn_next_month.setClickable(true);

                    }
                    for (int i = 0; i < mDayList.size(); i++) {
                        ReportDayView mDayView = mDayList.get(i);

                        int index = i - start;
                        if (index < 0 || index >= mList.size()) {
                            mDayView.setText1("");
                            mDayView.setText2("", 0);
                            mDayView.setCircleVisibility(View.GONE);
                            mDayView.setClickEnable(false);

                        } else {
                            ReportCalendarDayInfo mReportCalendarDayInfo = mList.get(index);
                            mDayView.setText1(mReportCalendarDayInfo.getDate());
                            String point = mReportCalendarDayInfo.getAvgpoint();
                            if (point == null || point.equals("")) {
                                mDayView.setCircleVisibility(View.GONE);
                                mDayView.setText2("", 0);
                                mDayView.setClickEnable(false);
                            } else if (point.equals("0")) {
                                // mDayView.setText2("--",
                                // mReportCalendarDayInfo.getPointColor());
                                mDayView.setCircleVisibility(View.VISIBLE);
                                mDayView.setText2("", 0);
                                mDayView.setClickEnable(true);
                            } else {
                                // mDayView.setText2(
                                // mReportCalendarDayInfo.getAvgpoint() + "分",
                                // mReportCalendarDayInfo.getPointColor());
                                mDayView.setCircleVisibility(View.VISIBLE);
                                mDayView.setText2("", 0);
                                mDayView.setClickEnable(true);
                            }

                        }
                    }
                    dissmissLoading();
                    break;

                case 1:
                    // 拉取数据失败
                    erroLoading(msg.obj);
                    break;
            }

        }
    };

    // 生成日历主体
    private View CreatCalendarMain() {
        LinearLayout layContent = new LinearLayout(context);
        layContent.setLayoutParams(new LayoutParams(
                android.view.ViewGroup.LayoutParams.FILL_PARENT,
                android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
        layContent.setOrientation(LinearLayout.VERTICAL);

        View div1 = new View(context);
        div1.setLayoutParams(new LayoutParams(android.view.ViewGroup.LayoutParams.FILL_PARENT,
                ReportDateConfig.getDiv_Height()));
        div1.setBackgroundColor(ReportDateConfig.Div_Color);
        layContent.addView(div1);
        for (int iRow = 0; iRow < 6; iRow++) {

            layContent.addView(CreatCalendarRow(iRow));

            View div = new View(context);
            div.setLayoutParams(new LayoutParams(android.view.ViewGroup.LayoutParams.FILL_PARENT,
                    ReportDateConfig.getDiv_Height()));
            div.setBackgroundColor(ReportDateConfig.Div_Color);
            layContent.addView(div);
        }

        return layContent;
    }

    // 生成日历中的一行，仅画矩形
    private View CreatCalendarRow(final int iRow) {

        LinearLayout layRow = new LinearLayout(context);
        layRow.setLayoutParams(new LayoutParams(android.view.ViewGroup.LayoutParams.FILL_PARENT,
                android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
        layRow.setOrientation(LinearLayout.HORIZONTAL);

        for (int iDay = 0; iDay < 7; iDay++) {
            ReportDayView mDayView = new ReportDayView(context);
            final int ii = iDay;
            OnClickListener l = new OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    OnItemClick(iRow * 7 + ii);
                }
            };
            mDayView.setOnClickListener(l);
            mDayList.add(mDayView);
            layRow.addView(mDayView);

            View div = new View(context);
            div.setLayoutParams(new LayoutParams(ReportDateConfig.getDiv_Height(),
                    android.view.ViewGroup.LayoutParams.FILL_PARENT));
            div.setBackgroundColor(ReportDateConfig.Div_Color);
            layRow.addView(div);
        }

        return layRow;
    }

    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.head_calender_img1:
                Log.e("info", "左");
                // 上一月
                if (month == 1) {
                    year--;
                    month = 12;
                } else {
                    month--;
                }
                load(year, month);
                break;
            case R.id.head_calender_img2:
                Log.e("info", "右");
                if (month == 12) {
                    year++;
                    month = 1;
                } else {
                    month++;
                }
                load(year, month);
                // 下一月

                break;
        }

    }

    private void OnItemClick(int postion) {
        if (mOnItemClick != null) {
            int index = postion - start;
            if (index >= 0 && index < mList.size()) {
                ReportCalendarDayInfo info = mList.get(index);
                String point = info.getAvgpoint();
                if (point != null && point.length() > 0) {
                    mOnItemClick.onClick(year + "-" + month + "-" + info.getDate());
                }
            }
        }

    }

    private void showLoading() {
        mLoadingBar.setVisibility(View.VISIBLE);
        mLoadingTextView.setText("等待中");
        mLoadingLayoutMain.setVisibility(View.VISIBLE);
        mLoadingLayout.setVisibility(View.VISIBLE);
        mLayError.setVisibility(View.GONE);
    }

    private void dissmissLoading() {
        mLoadingLayoutMain.setVisibility(View.GONE);
    }

    private void erroLoading(Object erro) {
        mLoadingLayoutMain.setVisibility(View.VISIBLE);
        mLoadingLayout.setVisibility(View.GONE);
        BaseResponseInfo mBaseResponseInfo = (BaseResponseInfo)erro;
        if (null != mBaseResponseInfo && null != mBaseResponseInfo.getInfo()) {
            mTextError.setText(mBaseResponseInfo.getInfo());
        } else {
            mTextError.setText("获取数据失败");
        }
        mLayError.setVisibility(View.VISIBLE);
    }

    public void setmOnItemClick(OnItemClick mOnItemClick) {
        this.mOnItemClick = mOnItemClick;
    }

    public interface OnItemClick {
        void onClick(String date);

        void onTextChange(String title);

        void onTitleStateChange(int year, int month, int day);
    }
}

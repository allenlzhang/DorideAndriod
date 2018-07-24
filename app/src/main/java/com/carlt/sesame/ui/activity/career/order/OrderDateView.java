
package com.carlt.sesame.ui.activity.career.order;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.carlt.sesame.R;
import com.carlt.sesame.control.CPControl;
import com.carlt.sesame.control.CPControl.GetResultListCallback;
import com.carlt.sesame.data.career.MonthOrderStateInfo;
import com.carlt.sesame.ui.activity.career.report.ReportDateView.OnItemClick;
import com.carlt.sesame.utility.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

public class OrderDateView extends LinearLayout implements OnClickListener {
    private Context context;

    private View mLoadingLayout;

    private TextView mLoadingTextView;

    private View mLoadingBar;
    
    private OnItemClick mOnItemClick;

    private ArrayList<OrderDayView> mDayList = new ArrayList<OrderDayView>();

    private ArrayList<MonthOrderStateInfo> mList;

    private int start = 0;

    public OrderDateView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public OrderDateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        LayoutInflater.from(context).inflate(R.layout.order_rili_date, this, true);
        LinearLayout mainLayout = (LinearLayout)findViewById(R.id.date_view_layout);
        // Loading控件
        mLoadingLayout = findViewById(R.id.loading_activity_mainlayout);
        mLoadingTextView = (TextView)findViewById(R.id.loading_activity_loading_text);
        mLoadingBar = findViewById(R.id.loading_activity_loading_bar);
        mLoadingLayout.setOnClickListener(this);
        mainLayout.addView(CreatCalendarMain());

    }

    private int year;

    private int month;

    public void load(int y, int m) {
        flag = true;
        showLoading();
        year = y;
        month = m;
        if (mOnItemClick != null) {
            mOnItemClick.onTextChange(year + "年" + month + "月");
            mOnItemClick.onTitleStateChange(year, month, 1);
        }
        Calendar mCalendar = Calendar.getInstance();
        mCalendar.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        mCalendar.set(year, month - 1, 1);
        int w = mCalendar.get(Calendar.DAY_OF_WEEK);
        start = w - 1;
        CPControl.GetMonthOrderStateResult(year + "-" + month + "-01", listener);
    }

    private GetResultListCallback listener = new GetResultListCallback() {

        @Override
        public void onFinished(Object o) {
            mList = (ArrayList<MonthOrderStateInfo>)o;
            Message msg = new Message();
            msg.what = 0;
            msg.arg1 = year;
            msg.arg2 = month;
            mHandler.sendEmptyMessage(0);
            flag = false;

        }

        @Override
        public void onErro(Object o) {
            mHandler.sendEmptyMessage(1);
            flag = false;
        }
    };

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case 0:
                    // 拉取数据成功

                    for (int i = 0; i < mDayList.size(); i++) {
                        OrderDayView mDayView = mDayList.get(i);

                        int index = i - start;
                        if (index < 0 || index >= mList.size()) {
                            mDayView.setText("");
                            mDayView.setStute(0);
                        } else {
                            MonthOrderStateInfo mMonthOrderStateInfo = mList.get(index);
                            mDayView.setText(mMonthOrderStateInfo.getDate());

                            if (mMonthOrderStateInfo.getLeft() > 0) {
                                mDayView.setStute(3);
                            } else {
                                mDayView.setStute(2);
                            }

                        }
                    }
                    dissmissLoading();
                    break;

                case 1:
                    // 拉取数据成功
                    erroLoading();
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
                DateConfig.getDiv_Height()));
        div1.setBackgroundColor(DateConfig.Div_Color);
        layContent.addView(div1);
        for (int iRow = 0; iRow < 6; iRow++) {

            layContent.addView(CreatCalendarRow(iRow));

            View div = new View(context);
            div.setLayoutParams(new LayoutParams(android.view.ViewGroup.LayoutParams.FILL_PARENT,
                    DateConfig.getDiv_Height()));
            div.setBackgroundColor(DateConfig.Div_Color);
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
            OrderDayView mDayView = new OrderDayView(context);
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
            div.setLayoutParams(new LayoutParams(DateConfig.getDiv_Height(),
                    android.view.ViewGroup.LayoutParams.FILL_PARENT));
            div.setBackgroundColor(DateConfig.Div_Color);
            layRow.addView(div);
        }

        return layRow;
    }

    // 此标识为数据是否正在拉取中
    private boolean flag = false;

    // 下一月
    public void nextMonth() {
        if (!flag) {
            flag = true;
            if (month == 12) {
                year++;
                month = 1;
            } else {
                month++;
            }
            load(year, month);
        }
    }

    // 上一月
    public void preMonth() {
        if (!flag) {
            flag = true;
            if (month == 1) {
                year--;
                month = 12;
            } else {
                month--;
            }
            load(year, month);
        }
    }

    @Override
    public void onClick(View arg0) {

    }

    private void OnItemClick(int postion) {
        if (mOnItemClick != null) {
            int index = postion - start;
            Log.e("info", "postion" + postion);
            Log.e("info", "start" + start);
            Log.e("info", "index" + index);
            if (index >= 0 && index < mList.size()) {
                MonthOrderStateInfo mMonthOrderStateInfo = mList.get(index);
                if (mMonthOrderStateInfo.getLeft() > 0) {
                    mOnItemClick.onClick(mMonthOrderStateInfo.getDate());
                }

            }
        }

    }

    private void showLoading() {
        mLoadingBar.setVisibility(View.VISIBLE);
        mLoadingTextView.setText("等待中");
        mLoadingLayout.setVisibility(View.VISIBLE);
    }

    private void dissmissLoading() {
        mLoadingLayout.setVisibility(View.GONE);
    }

    private void erroLoading() {
        mLoadingTextView.setText("获取数据失败");
        mLoadingBar.setVisibility(View.GONE);
    }

    public void setmOnItemClick(OnItemClick mOnItemClick) {
        this.mOnItemClick = mOnItemClick;
    }

}


package com.carlt.sesame.ui.activity.career.report;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.carlt.sesame.R;
import com.carlt.sesame.control.CPControl;
import com.carlt.sesame.control.CPControl.GetResultListCallback;
import com.carlt.sesame.data.career.ReportCalendarMonthInfo;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * 月报日历
 * 
 * @author daisy
 */
public class CalendarMonth extends MenuCalendar implements OnClickListener {
    private TextView Top_Date;// 月报日历的年份

    private ImageView mImageViewL;// 上一年

    private ImageView mImageViewR;// 下一年

    private Calendar today = Calendar.getInstance();

    // 月报得分
    private ArrayList<TextView> mPointTextViewList;

    // 月份数字
    private ArrayList<TextView> mDataTextViewList;

    // 月报单元格
    private ArrayList<View> mViews;

    private OnCalendarMonthClick mOnCalendarMonthClick;

    private ArrayList<ReportCalendarMonthInfo> mlist;

    private View mLoadingLayout;

    private TextView mLoadingTextView;

    private View mLoadingBar;

    public CalendarMonth(Context mContext, OnCalendarMonthClick onCalendarMonthClickListener) {
        super(mContext);
        this.mOnCalendarMonthClick = onCalendarMonthClickListener;
        View child = LayoutInflater.from(mContext).inflate(R.layout.layout_calendar_month, null);
        init(child);
        setTitle("选择月报日期");

        Top_Date = (TextView)child.findViewById(R.id.head_calender_txt);
        mImageViewL = (ImageView)child.findViewById(R.id.head_calender_img1);
        mImageViewR = (ImageView)child.findViewById(R.id.head_calender_img2);
        mImageViewL.setOnClickListener(this);
        mImageViewR.setOnClickListener(this);
        mLoadingLayout = child.findViewById(R.id.loading_activity_mainlayout);
        mLoadingTextView = (TextView)child.findViewById(R.id.loading_activity_loading_text);
        mLoadingBar = child.findViewById(R.id.loading_activity_loading_bar);
        mLoadingLayout.setOnClickListener(this);

        mDataTextViewList = new ArrayList<TextView>();
        mDataTextViewList.add((TextView)child.findViewById(R.id.layout_calender_month_txt_data1));
        mDataTextViewList.add((TextView)child.findViewById(R.id.layout_calender_month_txt_data2));
        mDataTextViewList.add((TextView)child.findViewById(R.id.layout_calender_month_txt_data3));
        mDataTextViewList.add((TextView)child.findViewById(R.id.layout_calender_month_txt_data4));
        mDataTextViewList.add((TextView)child.findViewById(R.id.layout_calender_month_txt_data5));
        mDataTextViewList.add((TextView)child.findViewById(R.id.layout_calender_month_txt_data6));
        mDataTextViewList.add((TextView)child.findViewById(R.id.layout_calender_month_txt_data7));
        mDataTextViewList.add((TextView)child.findViewById(R.id.layout_calender_month_txt_data8));
        mDataTextViewList.add((TextView)child.findViewById(R.id.layout_calender_month_txt_data9));
        mDataTextViewList.add((TextView)child.findViewById(R.id.layout_calender_month_txt_data10));
        mDataTextViewList.add((TextView)child.findViewById(R.id.layout_calender_month_txt_data11));
        mDataTextViewList.add((TextView)child.findViewById(R.id.layout_calender_month_txt_data12));

        mPointTextViewList = new ArrayList<TextView>();
        mPointTextViewList.add((TextView)child.findViewById(R.id.layout_calender_month_txt_score1));
        mPointTextViewList.add((TextView)child.findViewById(R.id.layout_calender_month_txt_score2));
        mPointTextViewList.add((TextView)child.findViewById(R.id.layout_calender_month_txt_score3));
        mPointTextViewList.add((TextView)child.findViewById(R.id.layout_calender_month_txt_score4));
        mPointTextViewList.add((TextView)child.findViewById(R.id.layout_calender_month_txt_score5));
        mPointTextViewList.add((TextView)child.findViewById(R.id.layout_calender_month_txt_score6));
        mPointTextViewList.add((TextView)child.findViewById(R.id.layout_calender_month_txt_score7));
        mPointTextViewList.add((TextView)child.findViewById(R.id.layout_calender_month_txt_score8));
        mPointTextViewList.add((TextView)child.findViewById(R.id.layout_calender_month_txt_score9));
        mPointTextViewList
                .add((TextView)child.findViewById(R.id.layout_calender_month_txt_score10));
        mPointTextViewList
                .add((TextView)child.findViewById(R.id.layout_calender_month_txt_score11));
        mPointTextViewList
                .add((TextView)child.findViewById(R.id.layout_calender_month_txt_score12));

        mViews = new ArrayList<View>();
        mViews.add(child.findViewById(R.id.layout_calender_month_linear1));
        mViews.add(child.findViewById(R.id.layout_calender_month_linear2));
        mViews.add(child.findViewById(R.id.layout_calender_month_linear3));
        mViews.add(child.findViewById(R.id.layout_calender_month_linear4));
        mViews.add(child.findViewById(R.id.layout_calender_month_linear5));
        mViews.add(child.findViewById(R.id.layout_calender_month_linear6));
        mViews.add(child.findViewById(R.id.layout_calender_month_linear7));
        mViews.add(child.findViewById(R.id.layout_calender_month_linear8));
        mViews.add(child.findViewById(R.id.layout_calender_month_linear9));
        mViews.add(child.findViewById(R.id.layout_calender_month_linear10));
        mViews.add(child.findViewById(R.id.layout_calender_month_linear11));
        mViews.add(child.findViewById(R.id.layout_calender_month_linear12));

        for (int i = 0; i < mViews.size(); i++) {
            int index = i + 1;
            mViews.get(i).setTag(index);
            mViews.get(i).setOnClickListener(this);
        }
    }

    private int year;

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case 0:
                    // 拉取数据成功

                    if (year == today.get(Calendar.YEAR)) {
                        mImageViewR.setImageResource(R.drawable.arrow_calendar_right_unselected);
                        mImageViewR.setClickable(false);

                    } else {
                        mImageViewR.setImageResource(R.drawable.arrow_calendar_right);
                        mImageViewR.setClickable(true);
                    }
                    int color1 = mContext.getResources().getColor(R.color.text_color_gray3);
                    int color2 = mContext.getResources().getColor(R.color.text_color_gray4);
                    for (int i = 0; i < 12; i++) {

                        mDataTextViewList.get(i).setTextColor(color2);
                        mPointTextViewList.get(i).setText("");
                    }
                    for (int i = 0; i < mlist.size(); i++) {
                        ReportCalendarMonthInfo info = mlist.get(i);
                        int key = info.getDate();
                        String ponit = info.getAvgpoint();

                        if (ponit.equals("0")) {
                            mPointTextViewList.get(key - 1).setText("--");

                        } else {
                            mPointTextViewList.get(key - 1).setText(ponit + "分");
                        }
                        mPointTextViewList.get(key - 1).setTextColor(info.getPointColor());
                        mDataTextViewList.get(key - 1).setTextColor(color1);
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

    @Override
    protected void onPopCreat() {
        load(Calendar.getInstance().get(Calendar.YEAR));
    }

    public void load(int y) {
        showLoading();
        year = y;
        Top_Date.setText(year + "年");
        GetResultListCallback listener = new GetResultListCallback() {

            @Override
            public void onFinished(Object o) {
                mlist = (ArrayList<ReportCalendarMonthInfo>)o;
                mHandler.sendEmptyMessage(0);

            }

            @Override
            public void onErro(Object o) {
                mHandler.sendEmptyMessage(1);

            }
        };
        CPControl.GetUserMonthPointResult(listener, year + "-02-02");
    }

    public interface OnCalendarMonthClick {
        void onClick(String date);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.head_calender_img1) {
            // 拉取上一年的数据
            year--;
            load(year);
        } else if (v.getId() == R.id.head_calender_img2) {
            // 拉取下一年的数据
            year++;
            load(year);
        } else if (v.getId() == R.id.loading_activity_mainlayout) {

        } else {

            // 拉取选中月份的数据
            String value = "";
            for (int x = 0; x < mlist.size(); x++) {
                ReportCalendarMonthInfo info = mlist.get(x);
                if (info.getDate() == ((Integer)v.getTag())) {
                    value = info.getAvgpoint();
                }

            }
            if (value.length() > 0) {

                StringBuffer mBuffer = new StringBuffer(Top_Date.getText().toString()
                        .substring(0, Top_Date.getText().toString().length() - 1));
                mBuffer.append("-");
                mBuffer.append(v.getTag());
                mBuffer.append("-1");
                mOnCalendarMonthClick.onClick(mBuffer.toString());
                dissmiss();
            } else {

            }

        }

    };

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
}


package com.carlt.sesame.ui.activity.career.order;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.carlt.doride.R;
import com.carlt.doride.http.retrofitnet.model.ContactsInfo;
import com.carlt.sesame.control.CPControl;
import com.carlt.sesame.control.CPControl.GetResultListCallback;
import com.carlt.sesame.data.BaseResponseInfo;
import com.carlt.sesame.data.SesameLoginInfo;
import com.carlt.sesame.ui.activity.base.BaseActivity;
import com.carlt.sesame.ui.activity.career.report.ReportDateView.OnItemClick;
import com.carlt.sesame.ui.view.PopBoxCreat;
import com.carlt.sesame.utility.Log;
import com.carlt.sesame.utility.UUToast;

import java.util.Calendar;

public class RiLiActivity extends BaseActivity implements OnClickListener {

    private ImageView back;// 头部返回键

    private TextView title;// 标题文字

    private TextView txtRight;// 头部右侧文字

    private OrderDateView mOrderDateView;

    private TimeView mTimeView;

    private TextView Top_Date;

    private ImageView btn_pre_month;

    private ImageView btn_next_month;

    private ImageView btn_return_month;

    private Calendar today = Calendar.getInstance();

    private int type;

    public final static int TYPE_FIX = 1;

    public final static int TYPE_Maintenance = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_rili_main);
        initTitle();
        
        type = getIntent().getIntExtra("type", 1);
        mOrderDateView = (OrderDateView)findViewById(R.id.order_rili_dateview);
        mTimeView = (TimeView)findViewById(R.id.rili_timeview);
        initAnimation();
        Top_Date = (TextView)findViewById(R.id.head_order_calender_txt);
        btn_pre_month = (ImageView)findViewById(R.id.head_order_calender_img1);
        btn_next_month = (ImageView)findViewById(R.id.head_order_calender_img2);
        btn_return_month = (ImageView)findViewById(R.id.head_order_calender_img3);
        btn_pre_month.setOnClickListener(this);
        btn_next_month.setOnClickListener(this);
        btn_return_month.setOnClickListener(this);
        mOrderDateView.setmOnItemClick(mDateViewOnItemClick);
        mTimeView.setmOnItemClick(mTimeViewOnItemClick);
        mOrderDateView.load(today.get(Calendar.YEAR), today.get(Calendar.MONTH) + 1);

    }

    private void initTitle() {
        back = (ImageView)findViewById(R.id.head_back_img1);
        title = (TextView)findViewById(R.id.head_back_txt1);
        txtRight = (TextView)findViewById(R.id.head_back_txt2);

        back.setImageResource(R.drawable.arrow_back);
        if(ContactsInfo.getInstance().name!=null&& ContactsInfo.getInstance().name.length()>0){
            title.setText(ContactsInfo.getInstance().name);
        }else{
            title.setText("预约");
        }
        
        txtRight.setVisibility(View.GONE);

        back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
    private OnItemClick mDateViewOnItemClick = new OnItemClick() {

        @Override
        public void onClick(String date) {

            ShowTimerView(date);

        }

        @Override
        public void onTextChange(String title) {
            Top_Date.setText(title);

        }

        @Override
        public void onTitleStateChange(int year, int month, int day) {
            if (mOrderDateView.getVisibility() == View.VISIBLE) {

                btn_next_month.setImageResource(R.drawable.arrow_calendar_right_order);
                btn_next_month.setClickable(true);
                Calendar c = Calendar.getInstance();
                c.set(year, month - 1, c.get(Calendar.DAY_OF_MONTH));
                Calendar now = Calendar.getInstance();
                if (c.after(now)) {
                    btn_pre_month.setImageResource(R.drawable.arrow_calendar_left_order);
                    btn_pre_month.setClickable(true);
                } else {
                    btn_pre_month.setImageResource(R.drawable.arrow_calendar_left_unselected);
                    btn_pre_month.setClickable(false);
                }
            }

        }
    };

    private Dialog mDialog;

    private OnItemClick mTimeViewOnItemClick = new OnItemClick() {

        @Override
        public void onClick(String date) {
            mDialog = PopBoxCreat.createDialogWithProgress(RiLiActivity.this, "正在预约...");
            mDialog.show();
            String[] s = date.split(",");
            CPControl.GetSubmitorderResult(s[0], s[1], type, listener);
        }

        @Override
        public void onTextChange(String title) {
            Top_Date.setText(title);
        }

        @Override
        public void onTitleStateChange(int year, int month, int day) {
            if (mTimeView.getVisibility() == View.VISIBLE) {
                btn_next_month.setImageResource(R.drawable.arrow_calendar_right_order);
                btn_next_month.setClickable(true);
                Calendar c = Calendar.getInstance();
                c.set(year, month - 1, day);
                Calendar now = Calendar.getInstance();
                if (c.after(now)) {
                    btn_pre_month.setImageResource(R.drawable.arrow_calendar_left_order);
                    btn_pre_month.setClickable(true);
                } else {
                    btn_pre_month.setImageResource(R.drawable.arrow_calendar_left_unselected);
                    btn_pre_month.setClickable(false);
                }
            }
        }
    };

    private boolean flag_v1_isShow = true;

    private void ShowOrderDateView() {

        if (!flag_v1_isShow) {
            mOrderDateView.load(mTimeView.getYear(), mTimeView.getMonth());
            mTimeView.startAnimation(ani3);
            mOrderDateView.startAnimation(ani4);
            mTimeView.setVisibility(View.GONE);
            mOrderDateView.setVisibility(View.VISIBLE);
            btn_return_month.setVisibility(View.GONE);
            flag_v1_isShow = true;
        }
    }

    private void ShowTimerView(String date) {
        if (flag_v1_isShow) {
            mTimeView.load(date);
            mTimeView.startAnimation(ani1);
            mOrderDateView.startAnimation(ani2);
            mTimeView.setVisibility(View.VISIBLE);
            mOrderDateView.setVisibility(View.GONE);
            btn_return_month.setVisibility(View.VISIBLE);
            flag_v1_isShow = false;
        }
    }

    private Animation ani1;

    private Animation ani2;

    private Animation ani3;

    private Animation ani4;

    private void initAnimation() {
        ani1 = AnimationUtils.loadAnimation(RiLiActivity.this, R.anim.rili1);
        ani2 = AnimationUtils.loadAnimation(RiLiActivity.this, R.anim.rili2);
        ani3 = AnimationUtils.loadAnimation(RiLiActivity.this, R.anim.rili3);
        ani4 = AnimationUtils.loadAnimation(RiLiActivity.this, R.anim.rili4);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_order_calender_img1:
                // 左箭头
                if (mOrderDateView.getVisibility() == View.VISIBLE) {
                    mOrderDateView.preMonth();
                } else {
                    mTimeView.preDay();
                }
                break;
            case R.id.head_order_calender_img2:
                // 右箭头
                if (mOrderDateView.getVisibility() == View.VISIBLE) {
                    mOrderDateView.nextMonth();
                } else {
                    mTimeView.nextDay();
                }
                break;
            case R.id.head_order_calender_img3:

                ShowOrderDateView();

                break;

        }

    }

    // 提交预约回调
    private GetResultListCallback listener = new GetResultListCallback() {

        @Override
        public void onFinished(Object o) {
            mHandler.sendEmptyMessage(0);

        }

        @Override
        public void onErro(Object o) {

            Message msg = new Message();
            msg.what = 1;
            msg.obj = o;
            mHandler.sendMessage(msg);

        }
    };

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    UUToast.showUUToast(RiLiActivity.this, "预约成功");
                    finish();
                    break;
                case 1:
                    String s = "";
                    BaseResponseInfo mBaseResponseInfo = (BaseResponseInfo)msg.obj;
                    if (mBaseResponseInfo != null && mBaseResponseInfo.getInfo() != null
                            && !mBaseResponseInfo.getInfo().equals("")) {
                        s = mBaseResponseInfo.getInfo();
                    }
                    UUToast.showUUToast(RiLiActivity.this, "预约失败" + s);
                    if (mDialog != null) {
                        mDialog.dismiss();
                    }

                    break;
            }
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && mTimeView.getVisibility() == View.VISIBLE) {
            Log.e("info", "aaa");
            ShowOrderDateView();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}

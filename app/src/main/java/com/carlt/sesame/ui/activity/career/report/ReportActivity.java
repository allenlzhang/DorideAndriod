
package com.carlt.sesame.ui.activity.career.report;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.carlt.sesame.R;
import com.carlt.sesame.control.CPControl;
import com.carlt.sesame.control.CPControl.GetResultListCallback;
import com.carlt.sesame.data.BaseResponseInfo;
import com.carlt.sesame.data.LoginInfo;
import com.carlt.sesame.ui.activity.base.BaseActivityGroup;
import com.carlt.sesame.ui.activity.career.report.newui.DayActivity;
import com.carlt.sesame.ui.activity.career.report.newui.MonthActivity;
import com.carlt.sesame.ui.activity.career.report.newui.WeekActivity;
import com.carlt.sesame.utility.Log;
import com.carlt.sesame.utility.UUToast;

public class ReportActivity extends BaseActivityGroup implements OnCheckedChangeListener {
    private LinearLayout container;

    public RadioButton[] tab;

    private TextView mTextView;// 选择日期按钮

    private int mCurrentTab = 0;

    public final static String MONTH_INITIAL = "month_initial";

    public final static String WEEK_INITIAL = "week_initial";

    public final static String DAY_INITIAL = "day_initial";

    private String monthInitialValue = "";

    private String weekInitialValue = "";

    private String dayInitialValue = "";

    private int checkedPos = 0;

    private View mLoadingLayout;

    private TextView mLoadingTextView;

    private View mLoadingBar;

    private View mLayError;// 错误信息展示layout

    private TextView mTextError;// 错误信息展示text

    private TextView mTextRetry;// 重试按钮

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_report_layout);
        container = (LinearLayout)findViewById(R.id.report_containerBody);
        initRadios();
        try {
            checkedPos = getIntent().getIntExtra("c", 0);
        } catch (Exception e) {

        }

        // try {
        // monthInitialValue = getIntent().getStringExtra(MONTH_INITIAL);
        // if (monthInitialValue == null || monthInitialValue.length() == 0) {
        // monthInitialValue = LoginInfo.getLately_month();
        // }
        // } catch (Exception e) {
        //
        // }
        // try {
        // weekInitialValue = getIntent().getStringExtra(WEEK_INITIAL);
        // if (weekInitialValue == null || weekInitialValue.length() == 0) {
        // weekInitialValue = LoginInfo.getLately_week();
        // }
        // } catch (Exception e) {
        //
        // }
        // try {
        // dayInitialValue = getIntent().getStringExtra(DAY_INITIAL);
        // if (dayInitialValue == null || dayInitialValue.length() == 0) {
        // dayInitialValue = LoginInfo.getLately_day();
        // }
        // } catch (Exception e) {
        //
        // }

        mTextView = (TextView)findViewById(R.id.report_date_select);
        mTextView.setOnClickListener(mListener);

        mLoadingLayout = findViewById(R.id.report_loading_lay);
        mLoadingTextView = (TextView)findViewById(R.id.report_loading_text);
        mLoadingBar = findViewById(R.id.report_loading_bar);

        mLayError = findViewById(R.id.report_loading_lay_error);
        mTextError = (TextView)findViewById(R.id.report_loading_text_error);
        mTextRetry = (TextView)findViewById(R.id.report_loading_text_retry);
        mTextRetry.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                LoadData();
            }
        });
        
        LoadData();

    }

    private void LoadData() {
        mLoadingBar.setVisibility(View.VISIBLE);
        mLoadingTextView.setText("等待中");
        mLoadingLayout.setVisibility(View.VISIBLE);
        mLayError.setVisibility(View.GONE);
        CPControl.GetReportdateResult(listener_reportdate);
    }
    
    private void LoadSuccess(Object data) {
        dayInitialValue = LoginInfo.getLately_day();
        weekInitialValue = LoginInfo.getLately_week();
        monthInitialValue = LoginInfo.getLately_month();
        tab[checkedPos].setChecked(true);
        mLoadingLayout.setVisibility(View.GONE);
        mLayError.setVisibility(View.GONE);
    }
    
    private void LoadError(Object error) {
        mLoadingLayout.setVisibility(View.GONE);
        BaseResponseInfo mBaseResponseInfo = (BaseResponseInfo)error;
        String infoMsg = "";
        if (null != mBaseResponseInfo) {
            String info = mBaseResponseInfo.getInfo();
            if (info != null && !info.equals("")) {
                infoMsg = info;
            } else {
                infoMsg = "获取数据失败...";
            }

        } else {
            infoMsg = "获取数据失败...";
        }

        mTextError.setText(infoMsg);
        UUToast.showUUToast(ReportActivity.this, mBaseResponseInfo.getInfo());
        mLayError.setVisibility(View.VISIBLE);
    }

    private GetResultListCallback listener_reportdate = new GetResultListCallback() {

        @Override
        public void onFinished(Object o) {
            Message msg = new Message();
            msg.what = 0;
            msg.obj=o;
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

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    LoadSuccess(msg.obj);
                    break;

                case 1:
                    LoadError(msg.obj);
                    break;
            }
        }

    };

    private OnClickListener mListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            onKeyDown(MENU, null);

        }
    };

    public void setCurrentView(int index) {
        mCurrentTab = index;
        if (container == null)
            container = (LinearLayout)findViewById(R.id.report_containerBody);
        Log.e("info", "index==" + index);
        Intent intent = null;
        switch (index) {
            case 0:
                intent = new Intent(ReportActivity.this, MonthActivity.class);
                intent.putExtra(MONTH_INITIAL, monthInitialValue);
                break;
            case 1:
                intent = new Intent(ReportActivity.this, WeekActivity.class);
                intent.putExtra(WEEK_INITIAL, weekInitialValue);
                break;
            case 2:
                intent = new Intent(ReportActivity.this, DayActivity.class);
                intent.putExtra(DAY_INITIAL, dayInitialValue);
                break;
        }
        container.removeAllViews();
        View view = getLocalActivityManager().startActivity("tab" + index,
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)).getDecorView();

        // view = new View(this);
        // view.setBackgroundColor(color_lv);
        view.setLayoutParams(new LayoutParams(-1, -1));

        container.addView(view);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        } else if (keyCode == MENU) {
            Activity currentActivity=this.getLocalActivityManager().getCurrentActivity();
            if(currentActivity!=null){
                currentActivity.onKeyDown(keyCode, event); 
            }else{
                tab[checkedPos].setChecked(true); 
                currentActivity.onKeyDown(keyCode, event); 
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);

    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_UP
                || event.getKeyCode() == KeyEvent.KEYCODE_DPAD_DOWN
                || event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT
                || event.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT
                || event.getKeyCode() == KeyEvent.KEYCODE_DPAD_CENTER) {
            return true;
        }

        return super.dispatchKeyEvent(event);
    }

    private void initRadios() {
        tab = new RadioButton[3];
        tab[0] = ((RadioButton)findViewById(R.id.report_tab1));
        tab[0].setOnCheckedChangeListener(this);
        tab[1] = ((RadioButton)findViewById(R.id.report_tab2));
        tab[1].setOnCheckedChangeListener(this);
        tab[2] = ((RadioButton)findViewById(R.id.report_tab3));
        tab[2].setOnCheckedChangeListener(this);

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        if (isChecked) {
            switch (buttonView.getId()) {
                case R.id.report_tab1:
                    setCurrentView(0);
                    break;
                case R.id.report_tab2:
                    setCurrentView(1);
                    break;
                case R.id.report_tab3:
                    setCurrentView(2);
                    break;

            }

        }
    }

    public final static int MENU = 5000;
}

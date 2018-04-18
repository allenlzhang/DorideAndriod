package com.carlt.yema.ui.activity.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.carlt.yema.R;
import com.carlt.yema.base.BaseActivityGroup;
import com.carlt.yema.data.BaseResponseInfo;
import com.carlt.yema.ui.view.UUToast;


/**
 * Created by Marlon on 2018/3/21.
 */

public class ReportActivity extends BaseActivityGroup implements CompoundButton.OnCheckedChangeListener{
    private LinearLayout container;

    public RadioButton[] tab;

    private TextView mTextView;// 选择日期按钮

    private int mCurrentTab = 0;

    public final static String MONTH_INITIAL = "month_initial";


    public final static String DAY_INITIAL = "day_initial";

    private String monthInitialValue = "";


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
        container = (LinearLayout) findViewById(R.id.report_containerBody);
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
        try {
            dayInitialValue = getIntent().getStringExtra(DAY_INITIAL);
            monthInitialValue = getIntent().getStringExtra(DAY_INITIAL);
        } catch (Exception e) {

        }

        mTextView = (TextView) findViewById(R.id.report_date_select);
        mTextView.setOnClickListener(mListener);

        mLoadingLayout = findViewById(R.id.report_loading_lay);
        mLoadingTextView = (TextView) findViewById(R.id.report_loading_text);
        mLoadingBar = findViewById(R.id.report_loading_bar);

        mLayError = findViewById(R.id.report_loading_lay_error);
        mTextError = (TextView) findViewById(R.id.report_loading_text_error);
        mTextRetry = (TextView) findViewById(R.id.report_loading_text_retry);
        mTextRetry.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                LoadData();
            }
        });

        LoadData();
        LoadSuccess();
    }

     protected void LoadData() {
        mLoadingBar.setVisibility(View.VISIBLE);
        mLoadingTextView.setText("等待中");
        mLoadingLayout.setVisibility(View.VISIBLE);
        mLayError.setVisibility(View.GONE);

//         EControl.GetReportdateResult(callback);
    }

    protected void LoadSuccess() {
//        if(TextUtils.isEmpty(dayInitialValue)){
//            dayInitialValue = LoginInfo.getLately_day();
//        }
//        monthInitialValue = LoginInfo.getLately_month();
        tab[checkedPos].setChecked(true);
        mLoadingLayout.setVisibility(View.GONE);
        mLayError.setVisibility(View.GONE);
    }

    private void LoadError(Object error) {
        mLoadingLayout.setVisibility(View.GONE);
        BaseResponseInfo mBaseResponseInfo = (BaseResponseInfo) error;
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

//    private BaseParser.ResultCallback callback = new BaseParser.ResultCallback() {
//
//        @Override
//        public void onSuccess(Object bInfo) {
//            Message msg = new Message();
//            msg.what = 0;
//            msg.obj = bInfo;
//            mHandler.sendMessage(msg);
//        }
//
//        @Override
//        public void onError(Object bInfo) {
//            Message msg = new Message();
//            msg.what = 1;
//            msg.obj = bInfo;
//            mHandler.sendMessage(msg);
//        }
//    };
//
//    private Handler mHandler = new Handler() {
//
//        @Override
//        public void handleMessage(Message msg) {
//            switch (msg.what) {
//                case 0:
//                    LoadSuccess(msg.obj);
//                    break;
//
//                case 1:
//                    LoadError(msg.obj);
//                    break;
//            }
//        }
//
//    };

    private View.OnClickListener mListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            try {
                onKeyDown(MENU, null);
            } catch (Exception e) {
                Log.e("info", "onKeyDown error==" + e.getMessage());
            }

        }
    };

    public void setCurrentView(int index) {
        mCurrentTab = index;
        if (container == null)
            container = (LinearLayout) findViewById(R.id.report_containerBody);
        Log.e("info", "index==" + index);
        Intent intent = null;
        switch (index) {
            case 0:
                intent = new Intent(ReportActivity.this, MonthActivity.class);
                intent.putExtra(MONTH_INITIAL, monthInitialValue);
                break;
            case 1:
                intent = new Intent(ReportActivity.this, DayActivity.class);
                intent.putExtra(DAY_INITIAL, dayInitialValue);
                break;
        }
        container.removeAllViews();
        View view = getLocalActivityManager().startActivity("tab" + index, intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)).getDecorView();

        // view = new View(this);
        // view.setBackgroundColor(color_lv);
        view.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));

        container.addView(view);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        } else if (keyCode == MENU) {
            Activity currentActivity = this.getLocalActivityManager().getCurrentActivity();
            if (currentActivity != null) {
                currentActivity.onKeyDown(keyCode, event);
            } else {
                tab[checkedPos].setChecked(true);
                currentActivity.onKeyDown(keyCode, event);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);

    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_UP || event.getKeyCode() == KeyEvent.KEYCODE_DPAD_DOWN || event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT
                || event.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT || event.getKeyCode() == KeyEvent.KEYCODE_DPAD_CENTER) {
            return true;
        }

        return super.dispatchKeyEvent(event);
    }

    private void initRadios() {
        tab = new RadioButton[2];
        tab[0] = ((RadioButton) findViewById(R.id.report_tab1));
        tab[0].setOnCheckedChangeListener(this);
        tab[1] = ((RadioButton) findViewById(R.id.report_tab2));
        tab[1].setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        if (isChecked) {
            switch (buttonView.getId()) {
                case R.id.report_tab1:
                    setCurrentView(1);
                    break;
                case R.id.report_tab2:
                    setCurrentView(0);
                    break;

            }

        }
    }

    public final static int MENU = 5000;


}

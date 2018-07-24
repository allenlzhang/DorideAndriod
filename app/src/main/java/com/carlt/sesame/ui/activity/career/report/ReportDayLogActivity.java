
package com.carlt.sesame.ui.activity.career.report;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.carlt.sesame.R;
import com.carlt.sesame.control.CPControl;
import com.carlt.sesame.data.career.CarLogInfo;
import com.carlt.sesame.ui.activity.base.LoadingActivityWithTitle;
import com.carlt.sesame.ui.adapter.DayLogAdapter;

import java.util.ArrayList;

/**
 * 行车日志
 * 
 * @author daisy
 */
public class ReportDayLogActivity extends LoadingActivityWithTitle {
    private ImageView back;// 头部返回键

    private TextView title;// 标题文字

    private TextView txtRight;// 头部右侧文字

    private ImageView imgRight;// 头部右侧图片

    private ListView mListView;

    private ArrayList<CarLogInfo> mArrayList;

    private DayLogAdapter mAdapter;

    private String date;

    public final static String DAY_LOG_DATE = "dayLogDate";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_day_log);
        setTitleView(R.layout.head_back);
        
        date = getIntent().getStringExtra(DAY_LOG_DATE);
        initTitle();
        init();
        LoadData();
    }

    private void initTitle() {
        back = (ImageView)findViewById(R.id.head_back_img1);
        title = (TextView)findViewById(R.id.head_back_txt1);
        txtRight = (TextView)findViewById(R.id.head_back_txt2);
        imgRight = (ImageView)findViewById(R.id.head_back_img2);

        back.setImageResource(R.drawable.arrow_back);
        title.setText(date + "行车日志");
        txtRight.setVisibility(View.GONE);

        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });
    }

    private void init() {
        mListView = (ListView)findViewById(R.id.activity_report_day_log_list);
    }

    @Override
    protected void LoadSuccess(Object data) {
        mArrayList = (ArrayList<CarLogInfo>)data;
        mAdapter = new DayLogAdapter(ReportDayLogActivity.this, mArrayList);
        mListView.setAdapter(mAdapter);
        super.LoadSuccess(data);
    }

    @Override
    protected void LoadErro(Object erro) {
        super.LoadErro(erro);
    }

    @Override
    protected void LoadData() {
        super.LoadData();
        CPControl.GetCarLogResult(date, listener);
    }

}

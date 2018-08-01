package com.carlt.doride.ui.activity.carstate;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.carlt.doride.R;
import com.carlt.doride.base.LoadingActivity;
import com.carlt.doride.data.BaseResponseInfo;
import com.carlt.doride.data.car.CarNowStatusInfo;
import com.carlt.doride.protocolparser.DefaultParser;
import com.carlt.doride.systemconfig.URLConfig;
import com.carlt.doride.ui.adapter.CarNowStatusAdapter;
import com.carlt.doride.ui.pull.PullToRefreshBase;
import com.carlt.doride.ui.pull.PullToRefreshListView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * 实时车况
 */
public class CarStateNowActivity extends LoadingActivity {

    private ListView              mListView;
    private PullToRefreshListView mPullToRefreshListView;
    private TextView              mTxtView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_state_now);
        initTitle("实时车况");
        initView();
        //        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadingDataUI();
        initData();
    }

    //初始化view
    private void initView() {
        mPullToRefreshListView = findViewById(R.id.activity_car_condition_list);
        mTxtView = $ViewByID(R.id.layout_sub_head_txt);


        mListView = mPullToRefreshListView.getRefreshableView();
        mListView.setDivider(getResources().getDrawable(R.drawable.list_divider_bg));
        mListView.setDividerHeight(getResources()
                .getDimensionPixelSize(R.dimen.list_divider_height));
        mListView.setVerticalScrollBarEnabled(false);
        mListView.setSelector(getResources().getDrawable(R.drawable.list_divider_bg));

        //        mPullToRefreshListView.setPullLoadEnabled(true);
        mPullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                //                下拉刷新
                initData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                //                上拉加载
            }
        });
    }

    private void initData() {
        showWaitingDialog(null);
        DefaultParser<CarNowStatusInfo> parser = new DefaultParser<>(mCallback, CarNowStatusInfo.class);
        parser.executePost(URLConfig.getM_REMOTE_STATUS(), new HashMap());
    }

    @Override
    public void loadDataSuccess(Object bInfo) {
        dissmissWaitingDialog();
        try {
            CarNowStatusInfo carNowStatusInfo = (CarNowStatusInfo) ((BaseResponseInfo) bInfo).getValue();

            if (null == carNowStatusInfo) {
                loadNodataUI();
                mPullToRefreshListView.setVisibility(View.GONE);
            } else {
                showData(carNowStatusInfo);
            }
        } catch (Exception e) {
            loadonErrorUI(null);
            mPullToRefreshListView.setVisibility(View.GONE);
        }

        mPullToRefreshListView.onPullDownRefreshComplete();
        mPullToRefreshListView.onPullUpRefreshComplete();
        setLastUpdateTime();
    }

    @Override
    public void loadDataError(Object bInfo) {
        super.loadDataError(bInfo);
        dissmissWaitingDialog();
    }

    private void setLastUpdateTime() {
        SimpleDateFormat mDateFormat = new SimpleDateFormat("MM-dd HH:mm");
        String text = mDateFormat.format(new Date(System.currentTimeMillis()));
        mPullToRefreshListView.setLastUpdatedLabel(text);
    }

    private void showData(CarNowStatusInfo carNowStatusInfo) {
        mPullToRefreshListView.setVisibility(View.VISIBLE);
        if (TextUtils.equals(carNowStatusInfo.getIsrunning(), "1")) {
            mTxtView.setText("您的爱车正在行驶中");
        } else if (TextUtils.equals(carNowStatusInfo.getIsrunning(), "0")) {
            mTxtView.setText("您的爱车正在休息");
        } else {
            mTxtView.setText("");
        }
        if (carNowStatusInfo.getList() != null && carNowStatusInfo.getList().size() != 0) {
            CarNowStatusAdapter adapter = new CarNowStatusAdapter(CarStateNowActivity.this, carNowStatusInfo.getList());
            mListView.setAdapter(adapter);
        }

    }


    @Override
    public void reTryLoadData() {
        super.reTryLoadData();
        initData();
    }
}

package com.carlt.doride.ui.activity.carstate;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.carlt.doride.R;
import com.carlt.doride.base.LoadingActivity;
import com.carlt.doride.data.car.CarNowStatusInfo;
import com.carlt.doride.http.retrofitnet.ApiRetrofit;
import com.carlt.doride.http.retrofitnet.BaseMvcObserver;
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
        mListView.setDividerHeight(getResources().getDimensionPixelSize(R.dimen.list_divider_height2));
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
        //        DefaultParser<CarNowStatusInfo> parser = new DefaultParser<>(mCallback, CarNowStatusInfo.class);
        //        String m_remote_status = URLConfig.getM_REMOTE_STATUS();
        //        String replace = m_remote_status.replace("100", "101");
        //        parser.executePost(replace, new HashMap());

        HashMap<String, Object> map = new HashMap<>();
        map.put("base", ApiRetrofit.getRemoteCommonParams());
        addDisposable(mApiService.QueryCurrentData(map), new BaseMvcObserver<CarNowStatusInfo>() {


            @Override
            public void onSuccess(CarNowStatusInfo result) {
                loadDataSuccess(result);
            }

            @Override
            public void onError(String msg) {
                dissmissWaitingDialog();
            }
        });
    }

    public void loadDataSuccess(CarNowStatusInfo carNowStatusInfo) {
        dissmissWaitingDialog();
        loadSuccessUI();
        //        try {
        //            CarNowStatusInfo carNowStatusInfo = (CarNowStatusInfo) ((BaseResponseInfo) bInfo).getValue();
        //            CarNowStatusInfo carNowStatusInfo = (CarNowStatusInfo) bInfo;
        if (carNowStatusInfo.err == null) {
            showData(carNowStatusInfo);
        } else {
            loadNodataUI();
            mPullToRefreshListView.setVisibility(View.GONE);
            showToast(carNowStatusInfo.err.msg);
        }

        //        } catch (Exception e) {
        //            loadonErrorUI(null);
        //            mPullToRefreshListView.setVisibility(View.GONE);
        //        }

        mPullToRefreshListView.onPullDownRefreshComplete();
        mPullToRefreshListView.onPullUpRefreshComplete();
        setLastUpdateTime();
    }

    public void loadDataError() {
        dissmissWaitingDialog();
    }

    private void setLastUpdateTime() {
        SimpleDateFormat mDateFormat = new SimpleDateFormat("MM-dd HH:mm");
        String text = mDateFormat.format(new Date(System.currentTimeMillis()));
        mPullToRefreshListView.setLastUpdatedLabel(text);
    }

    private void showData(CarNowStatusInfo carNowStatusInfo) {
        mPullToRefreshListView.setVisibility(View.VISIBLE);
        if (carNowStatusInfo.getIsrunning() == 1) {
            mTxtView.setText("您的爱车正在行驶中");
        } else if (carNowStatusInfo.getIsrunning() == 0) {
            mTxtView.setText("您的爱车正在休息");
        } else if (carNowStatusInfo.getIsrunning() == 2) {
            mTxtView.setText("您的爱车已上电");
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

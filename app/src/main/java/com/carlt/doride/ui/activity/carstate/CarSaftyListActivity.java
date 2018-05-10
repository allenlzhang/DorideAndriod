package com.carlt.doride.ui.activity.carstate;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.carlt.doride.R;
import com.carlt.doride.base.LoadingActivity;
import com.carlt.doride.data.BaseResponseInfo;
import com.carlt.doride.data.car.SaftyMsgInfo;
import com.carlt.doride.protocolparser.DefaultStringParser;
import com.carlt.doride.systemconfig.URLConfig;
import com.carlt.doride.ui.adapter.CarSaftyAdapter;
import com.carlt.doride.ui.pull.PullToRefreshBase;
import com.carlt.doride.ui.pull.PullToRefreshListView;
import com.carlt.doride.utils.StringUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class CarSaftyListActivity extends LoadingActivity {

    //提醒小标题
    private TextView safyHeadTV;

    private PullToRefreshListView mPullListView;
    private ListView              mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_safty_list);
        initTitle("安防提醒");
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();

        //        loadingDataUI();
        initData();
    }

    private void initView() {
        safyHeadTV = $ViewByID(R.id.layout_sub_head_txt);
        mPullListView = findViewById(R.id.activity_car_query_illegal_list);
        mListView = mPullListView.getRefreshableView();

        mListView.setDivider(getResources().getDrawable(R.drawable.list_divider_bg));
        mListView.setDividerHeight(getResources()
                .getDimensionPixelSize(R.dimen.list_divider_height));
        mListView.setVerticalScrollBarEnabled(false);
        mListView.setSelector(getResources().getDrawable(R.drawable.list_divider_bg));

        mPullListView.setPullLoadEnabled(false);
        mPullListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
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
        String safyHead = getIntent().getStringExtra("safetymsg");
        if (!StringUtils.isEmpty(safyHead)) {
            safyHeadTV.setText(safyHead);
        } else {
            safyHeadTV.setText("您还没有新的安防提醒消息");
        }
    }

    private void setLastUpdateTime() {
        SimpleDateFormat mDateFormat = new SimpleDateFormat("MM-dd HH:mm");
        String text = mDateFormat.format(new Date(System.currentTimeMillis()));
        mPullListView.setLastUpdatedLabel(text);
    }

    private void initData() {
        showWaitingDialog(null);
        DefaultStringParser parser = new DefaultStringParser(mCallback);
        HashMap map = new HashMap();
        map.put("class1", "21");
        parser.executePost(URLConfig.getM_SAFETY_MESSAGE_URL(), map);
    }

    @Override
    public void loadDataSuccess(Object bInfo) {
        dissmissWaitingDialog();
        try {
            String value = ((BaseResponseInfo<String>) bInfo).getValue();
            Gson gson = new Gson();
            Type type = new TypeToken<List<SaftyMsgInfo>>() {
            }.getType();
            List<SaftyMsgInfo> saftyMsgInfoLists = gson.fromJson(value, type);
            if (null == saftyMsgInfoLists || saftyMsgInfoLists.size() == 0) {
                loadNodataUI();
                mPullListView.setVisibility(View.GONE);
            } else {
                showData(saftyMsgInfoLists);
            }
        } catch (Exception e) {
            loadonErrorUI(null);
            mPullListView.setVisibility(View.GONE);
        }

        mPullListView.onPullDownRefreshComplete();
        mPullListView.onPullUpRefreshComplete();
        setLastUpdateTime();

    }

    @Override
    public void loadDataError(Object bInfo) {
        super.loadDataError(bInfo);
        dissmissWaitingDialog();
    }

    /**
     * 显示数据
     * @param saftyMsgInfoLists
     */
    private void showData(List<SaftyMsgInfo> saftyMsgInfoLists) {
        mPullListView.setVisibility(View.VISIBLE);
        CarSaftyAdapter adapter = new CarSaftyAdapter(CarSaftyListActivity.this, saftyMsgInfoLists);
        mListView.setAdapter(adapter);
    }

    @Override
    public void reTryLoadData() {
        super.reTryLoadData();
        initData();
    }
}

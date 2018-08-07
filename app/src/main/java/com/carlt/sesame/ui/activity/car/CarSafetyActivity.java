
package com.carlt.sesame.ui.activity.car;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.carlt.doride.R;
import com.carlt.doride.ui.pull.PullToRefreshListView;
import com.carlt.sesame.control.CPControl;
import com.carlt.sesame.control.CPControl.GetResultListCallback;
import com.carlt.sesame.data.BaseResponseInfo;
import com.carlt.sesame.data.career.SecretaryMessageInfo;
import com.carlt.sesame.data.career.SecretaryMessageInfoList;
import com.carlt.sesame.ui.activity.base.LoadingActivityWithTitle;
import com.carlt.sesame.ui.adapter.SafetyTipsAdapter;
import com.carlt.sesame.utility.UUToast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * 座驾-安防提醒页面
 *
 * @author daisy
 */
public class CarSafetyActivity extends LoadingActivityWithTitle {
    private ImageView back;// 头部返回键

    private TextView title;// 标题文字

    private TextView txtRight;// 头部右侧文字

    private ImageView imgRight;// 头部右侧图片

    private ImageView mImageViewSecretary;// 车秘书头像

    private TextView mTextViewSecretary;// 提醒消息

    private ListView mListView;// 安防提醒列表

    private SafetyTipsAdapter mAdapter;

    public final static String SAFETY_COUNT = "safety_count";

    private int count;// 未读安防提醒条数

    private PullToRefreshListView mRefreshListView;

    private final static int LIMIT = 10;

    private SecretaryMessageInfoList mInfoLists;// 接口返回的数据结构

    private boolean isLoadMore = false;

    private TextView mTxtNoData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_safety);
        setTitleView(R.layout.head_back);

        try {
            count = getIntent().getIntExtra(SAFETY_COUNT, 0);
        } catch (Exception e) {
            // TODO: handle exception
        }

        initTitle();
        initSubTitle();
        init();
        LoadData();

    }

    private void initTitle() {
        back = (ImageView) findViewById(R.id.head_back_img1);
        title = (TextView) findViewById(R.id.head_back_txt1);
        txtRight = (TextView) findViewById(R.id.head_back_txt2);
        imgRight = (ImageView) findViewById(R.id.head_back_img2);

        back.setImageResource(R.drawable.arrow_back);
        title.setText("安防提醒");
        txtRight.setVisibility(View.GONE);
        imgRight.setVisibility(View.VISIBLE);
        imgRight.setImageResource(R.drawable.icon_refresh);

        back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
        imgRight.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // 刷新
                LoadData();
            }
        });
        mTxtNoData = findViewById(R.id.car_safety_txt_no_data);
    }

    private void initSubTitle() {
        mImageViewSecretary = (ImageView) findViewById(R.id.layout_sub_head_img);
        mTextViewSecretary = (TextView) findViewById(R.id.layout_sub_head_txt);

        String s = "";
        if (count == 0) {
            s = "您还没有新的安防提醒消息";
        } else {
            s = "您有" + count + "条安防提醒消息未读";
        }
        mTextViewSecretary.setText(s);
    }

    private void init() {
        mRefreshListView = (PullToRefreshListView) findViewById(R.id.activity_car_safety_list);
        mListView = mRefreshListView.getRefreshableView();
        mListView.setDividerHeight(0);
        mListView.setVerticalScrollBarEnabled(false);
        mListView.setSelector(getResources().getDrawable(R.drawable.list_divider_bg));

        mRefreshListView.setPullLoadEnabled(true);
//        mRefreshListView.setLastUpdatedLabel("------");
       
        mRefreshListView.setOnRefreshListener(new com.carlt.doride.ui.pull.PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onPullDownToRefresh(com.carlt.doride.ui.pull.PullToRefreshBase<ListView> refreshView) {
                loadOnrefresh();
            }

            @Override
            public void onPullUpToRefresh(com.carlt.doride.ui.pull.PullToRefreshBase<ListView> refreshView) {
                loadPullUpToRefresh();
            }
        });
    }

    public void loadOnrefresh() {
        isLoadMore = false;
        CPControl.GetSecretaryMessageResult(LIMIT, 0, 21, listener);
    }

    public void loadPullUpToRefresh() {
        isLoadMore = true;
        CPControl.GetSecretaryMessageResult(LIMIT, mArrayList.size(), 21, listener);
    }

    private void setLastUpdateTime() {
        SimpleDateFormat mDateFormat = new SimpleDateFormat("MM-dd HH:mm");
        String text = mDateFormat.format(new Date(System.currentTimeMillis()));
        mRefreshListView.setLastUpdatedLabel(text);
    }

    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            mRefreshListView.onPullDownRefreshComplete();
            setLastUpdateTime();
            if (msg.what == 0) {
                mArrayList = (ArrayList<SecretaryMessageInfo>) msg.obj;
                mAdapter = new SafetyTipsAdapter(CarSafetyActivity.this, mArrayList);
                mListView.setAdapter(mAdapter);
            } else if (msg.what == 1) {
                BaseResponseInfo mInfo = (BaseResponseInfo) msg.obj;
                String info = "";
                if (mInfo == null || mInfo.getInfo() == null || mInfo.getInfo().length() < 1) {
                    info = "加载失败";
                }
                info = mInfo.getInfo();
                UUToast.showUUToast(CarSafetyActivity.this, info);
            }
        }

        ;
    };

    GetResultListCallback mLoadListener = new GetResultListCallback() {

        @Override
        public void onFinished(Object o) {
            Message msg = new Message();
            msg.what = 0;
            msg.obj = o;
            mHandler.sendMessage(msg);
        }

        @Override
        public void onErro(Object o) {
            Message msg = new Message();
            msg.what = 1;
            msg.obj = o;
            mHandler.sendMessage(msg);
        }
    };

    private ArrayList<SecretaryMessageInfo> mArrayList = new ArrayList<>();

    @Override
    protected void LoadSuccess(Object data) {
        if (data != null) {
            mInfoLists = (SecretaryMessageInfoList) data;
            if (isLoadMore) {
                mArrayList.addAll(mInfoLists.getmAllList());
            }else {
                mArrayList = mInfoLists.getmAllList();
            }
            if (mAdapter == null) {
                mAdapter = new SafetyTipsAdapter(this, mArrayList);
                mListView.setAdapter(mAdapter);
            } else {
                mAdapter.setmDataList(mArrayList);
                mAdapter.notifyDataSetChanged();
            }
            mRefreshListView.onPullDownRefreshComplete();
            mRefreshListView.onPullUpRefreshComplete();
            setLastUpdateTime();
            if (mArrayList.size()==0){
                mTxtNoData.setVisibility(View.VISIBLE);
            }else {
                mTxtNoData.setVisibility(View.GONE);
            }
        }
        super.LoadSuccess(data);
    }

    @Override
    protected void LoadErro(Object erro) {
        BaseResponseInfo mInfo = (BaseResponseInfo) erro;
        String info = "";
        if (mInfo == null || mInfo.getInfo() == null || mInfo.getInfo().length() < 1) {
            info = "加载失败";
        } else {
            info = mInfo.getInfo();
        }
        UUToast.showUUToast(CarSafetyActivity.this, info);

        super.LoadErro(erro);
    }

    @Override
    protected void LoadData() {
        super.LoadData();
        isLoadMore = false;
        CPControl.GetSecretaryMessageResult(LIMIT, 0, 21, listener);
    }
}

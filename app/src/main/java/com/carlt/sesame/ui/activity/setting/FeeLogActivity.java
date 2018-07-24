
package com.carlt.sesame.ui.activity.setting;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.carlt.sesame.R;
import com.carlt.sesame.control.CPControl;
import com.carlt.sesame.control.CPControl.GetResultListCallback;
import com.carlt.sesame.data.set.FeeLogInfo;
import com.carlt.sesame.data.set.FeeLogListInfo;
import com.carlt.sesame.ui.activity.base.LoadingActivityWithTitle;
import com.carlt.sesame.ui.adapter.FeeLogAdapter;
import com.carlt.sesame.ui.pull.PullToRefreshBase;
import com.carlt.sesame.ui.pull.PullToRefreshBase.OnRefreshListener;
import com.carlt.sesame.ui.pull.PullToRefreshListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * 购买服务记录
 * 
 * @author Administrator
 */

public class FeeLogActivity extends LoadingActivityWithTitle {
    private ImageView back;// 头部返回键

    private TextView title;// 标题文字

    private TextView txtRight;// 头部右侧文字

    private PullToRefreshListView mPullListView;

    private ListView mListView;

    private TextView mTxtEmpty;// 没有消息时的提示文字

    private final static int LIMIT = 20;

    private Dialog mDialog;

    private ArrayList<FeeLogInfo> mList = new ArrayList<FeeLogInfo>();// 数据list

    private FeeLogListInfo mInfoLists;

    private FeeLogAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feelog);
        setTitleView(R.layout.head_back);

        initTitle();
        init();
        LoadData();
    }

    private void initTitle() {
        back = (ImageView)findViewById(R.id.head_back_img1);
        title = (TextView)findViewById(R.id.head_back_txt1);
        txtRight = (TextView)findViewById(R.id.head_back_txt2);

        back.setImageResource(R.drawable.arrow_back);
        title.setText("服务购买记录");
        txtRight.setVisibility(View.GONE);
        back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void init() {
        mPullListView = (PullToRefreshListView)findViewById(R.id.feelog_list);
        mTxtEmpty = (TextView)findViewById(R.id.feelog_txt_empty);

        mListView = mPullListView.getRefreshableView();
        mListView.setDivider(getResources().getDrawable(R.drawable.list_divider_bg_line));
        mListView.setDividerHeight(0);
        mListView.setVerticalScrollBarEnabled(false);
        mListView.setSelector(getResources().getDrawable(R.drawable.list_divider_bg));

        mPullListView.setPullLoadEnabled(true);
        mPullListView.setOnRefreshListener(new OnRefreshListener<ListView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                // 下拉刷新
                PullDown();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                // 上拉刷新
                PullUp();
            }
        });
    }

    /**
     * 下拉刷新
     */
    private void PullDown() {
        // 调用接口
        CPControl.GetFeeLogResult(LIMIT, 0, listener);
    }

    /**
     * 上拉获取更多数据
     */
    private void PullUp() {
        // 调用接口
        int offset = mInfoLists.getOffset();
        CPControl.GetFeeLogResult(LIMIT, offset, listener_loadmore);
    }

    private void setLastUpdateTime() {
        SimpleDateFormat mDateFormat = new SimpleDateFormat("MM-dd HH:mm");
        String text = mDateFormat.format(new Date(System.currentTimeMillis()));
        mPullListView.setLastUpdatedLabel(text);
    }
    
    @Override
    protected void LoadSuccess(Object data) {
        mInfoLists = (FeeLogListInfo)data;
        if (mInfoLists != null) {
            mList = mInfoLists.getmFeeLogInfoList();
            if (mAdapter == null) {
                mAdapter = new FeeLogAdapter(FeeLogActivity.this, mList);
                mListView.setAdapter(mAdapter);
            } else {
                mAdapter.setmList(mList);
                mAdapter.notifyDataSetChanged();
            }

            if (mList.size() == 0) {
                mPullListView.setVisibility(View.GONE);
                mTxtEmpty.setVisibility(View.VISIBLE);
            } else {
                mPullListView.setVisibility(View.VISIBLE);
                mTxtEmpty.setVisibility(View.GONE);
            }

            mPullListView.onPullDownRefreshComplete();
            mPullListView.onPullUpRefreshComplete();
            setLastUpdateTime();
        }
        super.LoadSuccess(data);
    }

    @Override
    protected void LoadErro(Object erro) {
        // TODO Auto-generated method stub
        super.LoadErro(erro);
    }

    @Override
    protected void LoadData() {
        super.LoadData();
        CPControl.GetFeeLogResult(LIMIT, 0, listener);
    }



    // 拉取更多数据
    GetResultListCallback listener_loadmore = new GetResultListCallback() {

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

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    FeeLogListInfo mMore = (FeeLogListInfo)msg.obj;
                    if (mInfoLists != null) {
                        mInfoLists.setOffset(mMore.getOffset());
                        mInfoLists.addmFeeLogInfoList(mMore.getmFeeLogInfoList());
                        if (mMore.getmFeeLogInfoList().size() == 0) {
                            mPullListView.setPullLoadEnabled(false);
                        }
                    }
                    LoadSuccess(mInfoLists);
                    break;

                case 1:
                    LoadErro(msg.obj);
                    break;
            }
        }

    };

    @Override
    protected void onResume() {
        super.onResume();
    }

}

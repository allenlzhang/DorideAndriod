
package com.carlt.sesame.ui.activity.friends;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.carlt.sesame.R;
import com.carlt.sesame.control.CPControl;
import com.carlt.sesame.control.CPControl.GetResultListCallback;
import com.carlt.sesame.data.community.MySOSListInfo;
import com.carlt.sesame.data.community.SOSInfo;
import com.carlt.sesame.ui.activity.base.LoadingActivityWithTitle;
import com.carlt.sesame.ui.adapter.DeclarationListAdapter;
import com.carlt.sesame.ui.pull.PullToRefreshBase;
import com.carlt.sesame.ui.pull.PullToRefreshBase.OnRefreshListener;
import com.carlt.sesame.ui.pull.PullToRefreshListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DeclarationListActivity extends LoadingActivityWithTitle {
    private ImageView back;// 头部返回键

    private TextView title;// 标题文字

    private TextView mTxtNull;// 没有数据时显示的view

    private PullToRefreshListView mPullListView;

    private ListView mListView; // 用户分享列表

    private DeclarationListAdapter mAdapter;

    private MySOSListInfo mInfo;

    private final static int limit = 10;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_declaration_list);
        setTitleView(R.layout.head_back);

        initTitle();
        init();
        LoadData();
    }

    private void initTitle() {
        back = (ImageView)findViewById(R.id.head_back_img1);
        title = (TextView)findViewById(R.id.head_back_txt1);

        back.setImageResource(R.drawable.arrow_back);
        title.setText("申报纪录");

        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void init() {
        mTxtNull = (TextView)findViewById(R.id.declaration_list_btn);

        mPullListView = (PullToRefreshListView)findViewById(R.id.declaration_list_list);
        setLastUpdateTime();
        mListView = mPullListView.getRefreshableView();
        mListView.setDivider(getResources().getDrawable(R.drawable.list_divider_bg));
        mListView.setDividerHeight(getResources()
                .getDimensionPixelSize(R.dimen.list_divider_height));
        mListView.setVerticalScrollBarEnabled(false);
        mListView.setSelector(getResources().getDrawable(R.drawable.list_divider_bg));
        mListView.setOnItemClickListener(null);

        mPullListView.setPullLoadEnabled(true);
        mPullListView.setOnRefreshListener(refreshListener);
    }

    private void setLastUpdateTime() {
        SimpleDateFormat mDateFormat = new SimpleDateFormat("MM-dd HH:mm");
        String text = mDateFormat.format(new Date(System.currentTimeMillis()));
        mPullListView.setLastUpdatedLabel(text);
    }

    @Override
    protected void LoadSuccess(Object data) {
        mInfo = (MySOSListInfo)data;
        if (mInfo != null) {
            ArrayList<SOSInfo> mDataList = mInfo.getmSOSInfoList();
            if (mDataList.size() > 0) {
                mTxtNull.setVisibility(View.GONE);
            } else {
                mTxtNull.setVisibility(View.VISIBLE);
            }

            if (mAdapter == null) {
                mAdapter = new DeclarationListAdapter(DeclarationListActivity.this, mDataList);
                mListView.setAdapter(mAdapter);
                mListView.setOnItemClickListener(mItemClickListener);
            } else {
                mAdapter.setmDataList(mDataList);
                mAdapter.notifyDataSetChanged();
            }

            setLastUpdateTime();
            mPullListView.onPullDownRefreshComplete();
            mPullListView.onPullUpRefreshComplete();
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
        CPControl.GetMySOSListResult(limit, 0, listener);
    }

    private int count = 0;

    @Override
    protected void onResume() {
        super.onResume();
        if (count != 0) {
            CPControl.GetMySOSListResult(limit, 0, listener);

        }
        count++;
    }

    private OnItemClickListener mItemClickListener = new OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {
            // 跳转至详情页
            SOSInfo sosInfo = mInfo.getmSOSInfoList().get(pos);
            Intent mIntent = new Intent(DeclarationListActivity.this,
                    DeclarationDetailActivity.class);
            mIntent.putExtra(DeclarationDetailActivity.MSG_ID, sosInfo.getId());
            startActivity(mIntent);
        }
    };

    private OnRefreshListener<ListView> refreshListener = new OnRefreshListener<ListView>() {

        @Override
        public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
            // 下拉
            CPControl.GetMySOSListResult(limit, 0, listener);
        }

        @Override
        public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
            // 上拉

            CPControl.GetMySOSListResult(limit, mInfo.getOffset(), listener_loadmore);
        }
    };

    GetResultListCallback listener_loadmore = new GetResultListCallback() {

        @Override
        public void onFinished(Object o) {
            Message msg = new Message();
            msg.what = 0;
            msg.obj = o;
            mHandler_loadmore.sendMessage(msg);
        }

        @Override
        public void onErro(Object o) {
            Message msg = new Message();
            msg.what = 1;
            msg.obj = o;
            mHandler_loadmore.sendMessage(msg);
        }
    };

    private Handler mHandler_loadmore = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case 0:
                    MySOSListInfo mInfoMore = (MySOSListInfo)msg.obj;
                    mInfo.setOffset(mInfoMore.getOffset());
                    mInfo.addmSOSInfoList(mInfoMore.getmSOSInfoList());
                    if (mInfoMore.getmSOSInfoList().size() == 0) {
                        mPullListView.setPullLoadEnabled(false);
                    }
                    LoadSuccess(mInfo);
                    break;
                case 1:
                    LoadErro(msg.obj);
                    break;
            }

        }
    };
}

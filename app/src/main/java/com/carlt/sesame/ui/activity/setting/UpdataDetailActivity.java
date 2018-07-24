package com.carlt.sesame.ui.activity.setting;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.carlt.sesame.R;
import com.carlt.sesame.control.CPControl;
import com.carlt.sesame.data.usercenter.VersionLog;
import com.carlt.sesame.ui.activity.base.LoadingActivityWithTitle;
import com.carlt.sesame.ui.adapter.UpdateDetailAdapter;

import java.util.List;

import static com.carlt.sesame.R.id.activity_update_detail_lv;

public class UpdataDetailActivity extends LoadingActivityWithTitle implements View.OnClickListener,AdapterView.OnItemClickListener {


    private ImageView back;// 头部返回键

    private TextView title;// 标题文字

    private List<VersionLog> lists;

    private ListView mListView;

    private UpdateDetailAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updata_detail);
        setTitleView(R.layout.head_back);
        initTitle();
        init();
    }
    @Override
    protected void onResume() {
        super.onResume();
        LoadData();
    }

    @Override
    protected void LoadData() {
        super.LoadData();
        initData();
    }

    private void initData() {
        CPControl.GetVersionLog(new CPControl.GetResultListCallback() {
            @Override
            public void onFinished(Object o) {
                Message msg = new Message();
                msg.what = 0;
                msg.obj = o;
                myHandler.sendMessage(msg);
            }
            @Override
            public void onErro(Object o) {
                Message msg = new Message();
                msg.what = 1;
                msg.obj = o;
                myHandler.sendMessage(msg);
            }
        });
    }

    private void initTitle() {
        back = (ImageView) findViewById(R.id.head_back_img1);
        title = (TextView) findViewById(R.id.head_back_txt1);
        title.setText("版本介绍");
        back.setImageResource(R.drawable.arrow_back);
        back.setOnClickListener(this);
    }

    private void init() {
        mListView = (ListView) findViewById(activity_update_detail_lv);
        mListView.setOnItemClickListener(this);
        adapter = new UpdateDetailAdapter(this);
    }



    @Override
    public void onClick(View v) {
        if (v.equals(back)) {
            finish();
        }
    }

    @SuppressLint("HandlerLeak")
    Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {//成功
                LoadSuccess(null);
                lists = (List<VersionLog>) msg.obj;
                //填充数据
                adapter.setList(lists);
                mListView.setAdapter(adapter);
            } else if (msg.what == 1) {//失败
                LoadErro(msg.obj);
            }
        }
    };

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        VersionLog log =lists.get(position);
        if (log!=null){
            Intent intent = new Intent(UpdataDetailActivity.this,UpdataLogItemDetialActivity.class);
            intent.putExtra("versionLog",log);
            startActivity(intent);
        }
    }
}

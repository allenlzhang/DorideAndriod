package com.carlt.sesame.ui.activity.setting;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.carlt.doride.R;
import com.carlt.sesame.data.usercenter.VersionLog;
import com.carlt.sesame.ui.activity.base.LoadingActivityWithTitle;
import com.carlt.sesame.utility.StringUtils;

import java.util.ArrayList;

public class UpdataLogItemDetialActivity extends LoadingActivityWithTitle implements View.OnClickListener {


    private ImageView back;// 头部返回键

    private TextView title;// 标题文字

    private VersionLog log;

    private ListView mListView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_detail);
        setTitleView(R.layout.head_back);
        init();
        initTitle();
    }


    private void initTitle() {
        back = (ImageView) findViewById(R.id.head_back_img1);
        title = (TextView) findViewById(R.id.head_back_txt1);
        title.setText(log.getVersion());
        back.setImageResource(R.drawable.arrow_back);
        back.setOnClickListener(this);
    }

    private void init() {
        mListView = (ListView) findViewById(R.id.activity_log_detail_lv);
        log = (VersionLog) getIntent().getSerializableExtra("versionLog");
        if (log!=null) {
            LoadSuccess(null);
            LogItemAdapter adapter = new LogItemAdapter(this, log.getInfos());
            mListView.setAdapter(adapter);
        }else {
            LoadErro(null);
        }
    }


    @Override
    public void onClick(View v) {
        if (v.equals(back)) {
            finish();
        }
    }

    public class LogItemAdapter extends BaseAdapter {
        private ArrayList<String> list;
        private LayoutInflater inflater;

        public LogItemAdapter(Context context, ArrayList<String> list) {
            this.list = list;

            this.inflater = LayoutInflater.from(context);
        }


        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public String getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.update_log_detail_item, null);
                viewHolder = new ViewHolder();
                viewHolder.mTxtContent = (TextView) convertView.findViewById(R.id.update_log_txt_item_detail);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            String info = list.get(position);
            if (!StringUtils.isEmpty(info)) {
                viewHolder.mTxtContent.setText(info.trim());
            }else {
                viewHolder.mTxtContent.setText("");
            }
            return convertView;
        }

        private class ViewHolder {
            protected TextView mTxtContent;

        }
    }
}

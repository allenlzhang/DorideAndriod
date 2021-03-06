package com.carlt.doride.ui.activity.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.carlt.doride.R;
import com.carlt.doride.base.LoadingActivity;
import com.carlt.doride.control.CPControl;
import com.carlt.doride.data.BaseResponseInfo;
import com.carlt.doride.data.home.InformationCategoryInfo;
import com.carlt.doride.data.home.InformationCategoryInfoList;
import com.carlt.doride.data.home.InformationMessageInfo;
import com.carlt.doride.ui.activity.carstate.CarSaftyListActivity;
import com.carlt.doride.ui.activity.setting.MsgManageActivity;

import java.util.ArrayList;

/**
 * Created by Marlon on 2018/3/16.
 */

public class InformationCentreActivity extends LoadingActivity {

    public final static String TIPS_TITLE = "tips_title";

    public final static String TIPS_TYPE = "tips_type";

    private ListView mListView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information_centre);
        initTitle("大乘助手");
        init();
        initData();
    }

    @Override
    public void onRightClick() {
        super.onRightClick();
        Intent intent = new Intent(InformationCentreActivity.this, MsgManageActivity.class);
        startActivity(intent);
    }

    private void initData() {
        loadingDataUI();
        CPControl.GetInformationCentreInfoListResult(mCallback);
    }


    private void init() {
        mListView = (ListView) findViewById(R.id.activity_information_centre_list);
    }


    @Override
    public void loadDataSuccess(Object bInfo) {
        super.loadDataSuccess(bInfo);
        if (bInfo != null) {
            InformationCategoryInfoList mCentreInfoLists;
            mCentreInfoLists = (InformationCategoryInfoList) ((BaseResponseInfo) bInfo).getValue();
            final ArrayList<InformationCategoryInfo> mList = mCentreInfoLists.getmAllList();
            TypeAdapter mAdapter = new TypeAdapter(mList);
            mListView.setAdapter(mAdapter);
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    InformationCategoryInfo mInfo = mList.get(position);
                    String title_s = mInfo.getName();
                    int type = mInfo.getId();
//                    String lastmsg = mInfo.getLastmsg();
//                    if (type == InformationMessageInfo.C1_T2){
//                        Intent mIntent = new Intent(InformationCentreActivity.this, CarSaftyListActivity.class);
//                        mIntent.putExtra("safetymsg",lastmsg);
//                        startActivity(mIntent);
//                    }else {
                        Intent mIntent = new Intent(InformationCentreActivity.this, RemindActivity.class);
                        mIntent.putExtra(TIPS_TITLE, title_s);
                        mIntent.putExtra(TIPS_TYPE, type);
                        startActivity(mIntent);
//                    }
                }
            });

            String unreadCount = mCentreInfoLists.getUnreadCount();
        }
    }

    @Override
    public void reTryLoadData() {
        super.reTryLoadData();
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    class TypeAdapter extends BaseAdapter {

        private LayoutInflater mInflater;

        private ArrayList<InformationCategoryInfo> mList;

        public TypeAdapter(ArrayList<InformationCategoryInfo> list) {
            mInflater = LayoutInflater.from(InformationCentreActivity.this);
            mList = list;
        }

        @Override
        public int getCount() {
            if (mList != null) {
                return mList.size();
            }
            return 0;
        }

        @Override
        public Object getItem(int arg0) {
            return null;
        }

        @Override
        public long getItemId(int arg0) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup arg2) {
            convertView = mInflater.inflate(R.layout.list_item_secretary_types, null);
            TextView mTxtTile = (TextView) convertView.findViewById(R.id.activity_information_centre_title);
            TextView mTxtDes = (TextView) convertView.findViewById(R.id.activity_information_centre_des);
            ImageView mImgIcon = (ImageView) convertView.findViewById(R.id.activity_information_centre_icon);
            ImageView mImgDot = (ImageView) convertView.findViewById(R.id.activity_information_centre_dot);
            View line = convertView.findViewById(R.id.list_item_secretary_line);

            line.setVisibility(View.VISIBLE);
            InformationCategoryInfo mInfo = mList.get(position);
            if (position == mList.size() - 1) {
                line.setVisibility(View.INVISIBLE);
            }

            String string;
            // 类型名称
            string = mInfo.getName();
            if (string != null && string.length() > 0) {
                mTxtTile.setText(string);
            }

            int imgId = mInfo.getImg();
            if (imgId > 0) {
                mImgIcon.setImageResource(mInfo.getImg());
            }

            // 最后一条消息内容
            string = mInfo.getLastmsg();
            String count = mInfo.getMsgcount();
            if (Integer.parseInt(count) > 0) {
                mImgDot.setVisibility(View.VISIBLE);
                if (string != null && string.length() > 0) {
                    mTxtDes.setText("新消息：" + string);
                } else {
                    mTxtDes.setText("");
                }
            } else {
                mImgDot.setVisibility(View.GONE);
                if (string != null && string.length() > 0) {
                    mTxtDes.setText("最后一条：" + string);
                } else {
                    mTxtDes.setText("还没有消息");
                }
            }
            return convertView;
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // 获取用户计算后的结果
        setResult(2);
        finish();
    }

    @Override
    protected void initTitle(String titleString) {
        super.initTitle(titleString);
        backTV2.setVisibility(View.VISIBLE);
        backTV2.setBackgroundResource(R.drawable.icon_message_manager_bg);
    }
}

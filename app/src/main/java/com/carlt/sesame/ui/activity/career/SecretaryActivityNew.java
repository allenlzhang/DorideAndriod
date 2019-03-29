
package com.carlt.sesame.ui.activity.career;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.carlt.doride.DorideApplication;
import com.carlt.doride.R;
import com.carlt.doride.http.retrofitnet.model.GetCarInfo;
import com.carlt.sesame.control.CPControl;
import com.carlt.sesame.data.SesameLoginInfo;
import com.carlt.sesame.data.career.SecretaryCategoryInfo;
import com.carlt.sesame.data.career.SecretaryCategoryInfoList;
import com.carlt.sesame.ui.activity.base.LoadingActivityWithTitle;

import java.util.ArrayList;

//芝麻小秘书
public class SecretaryActivityNew extends LoadingActivityWithTitle {
    private ImageView back;// 头部返回键

    private TextView title;// 标题文字

    private TextView txtRight;// 头部右侧文字

    private ImageView mImageViewSecretary;// 车秘书头像

    private TextView mTextViewSecretary;// 车秘书姓名

    private ListView mListView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_career_secretary_new);
        setTitleView(R.layout.head_back);

        initTitle();
        initSubTitle();
        init();
        LoadData();
    }

    private void initTitle() {
        back = (ImageView) findViewById(R.id.head_back_img1);
        title = (TextView) findViewById(R.id.head_back_txt1);
        txtRight = (TextView) findViewById(R.id.head_back_txt2);

        back.setImageResource(R.drawable.arrow_back);
        title.setText("大乘助手");
        txtRight.setText("");
        txtRight.setVisibility(View.GONE);
        back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
        txtRight.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent mIntent = new Intent(SecretaryActivityNew.this, SecretaryAppointmentActivity.class);
                startActivity(mIntent);
            }
        });

    }

    private void initSubTitle() {
        mImageViewSecretary = (ImageView) findViewById(R.id.activity_career_secretary_img);
        mTextViewSecretary = (TextView) findViewById(R.id.activity_career_secretary_txt1);

        mTextViewSecretary.setText((GetCarInfo.getInstance().secretaryID == 1? DorideApplication.ApplicationContext.getResources().getString(R.string.register_secretary_girl):DorideApplication.ApplicationContext.getResources().getString(R.string.register_secretary_boy)) + ":");
        mImageViewSecretary.setImageResource(R.drawable.icon_secretary);
    }

    private void init() {
        mListView = (ListView) findViewById(R.id.activity_career_secretary_list);
    }

    @Override
    protected void LoadSuccess(Object data) {
        if (data != null) {
            SecretaryCategoryInfoList mCategoryInfoLists;
            mCategoryInfoLists = (SecretaryCategoryInfoList) data;
            final ArrayList<SecretaryCategoryInfo> mList = mCategoryInfoLists.getmAllList();
            TypeAdapter mAdapter = new TypeAdapter(mList);
            mListView.setAdapter(mAdapter);
            mListView.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    SecretaryCategoryInfo mInfo = mList.get(position);
                    String title_s = mInfo.getName();
                    int type = mInfo.getId();
                    Intent mIntent = new Intent(SecretaryActivityNew.this, SecretaryTipsActivity.class);
                    mIntent.putExtra(SecretaryTipsActivity.TIPS_TITLE, title_s);
                    mIntent.putExtra(SecretaryTipsActivity.TIPS_TYPE, type);
                    startActivity(mIntent);

                }
            });

            int unreadCount = mCategoryInfoLists.getUnreadCount();
            if (unreadCount > 0) {
                mTextViewSecretary.setText("大乘助手：您有未读消息哦！");
            } else {
                mTextViewSecretary.setText("大乘助手：我是您的私人汽车助理");
            }
        }
        super.LoadSuccess(data);
    }

    @Override
    protected void LoadErro(Object erro) {

        super.LoadErro(erro);
    }

    @Override
    protected void LoadData() {
        super.LoadData();
        CPControl.GetSecretaryCategoryResult(listener);

    }

    private int count = 0;

    @Override
    protected void onResume() {
        super.onResume();
        if (count > 0) {
            LoadData();
        }
        count++;
    }

    class TypeAdapter extends BaseAdapter {

        private LayoutInflater mInflater;

        private ArrayList<SecretaryCategoryInfo> mList;

        public TypeAdapter(ArrayList<SecretaryCategoryInfo> list) {
            mInflater = LayoutInflater.from(SecretaryActivityNew.this);
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
            convertView = mInflater.inflate(R.layout.sesame_list_item_secretary_types, null);
            TextView mTxtTile = (TextView) convertView.findViewById(R.id.activity_career_secretary_title);
            TextView mTxtDes = (TextView) convertView.findViewById(R.id.activity_career_secretary_des);
            ImageView mImgIcon = (ImageView) convertView.findViewById(R.id.activity_career_secretary_icon);
            ImageView mImgDot = (ImageView) convertView.findViewById(R.id.activity_career_secretary_dot);
            View line = convertView.findViewById(R.id.line);

            line.setVisibility(View.VISIBLE);
            SecretaryCategoryInfo mInfo = mList.get(position);
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
            int count = mInfo.getMsgcount();
            if (count > 0) {
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
}

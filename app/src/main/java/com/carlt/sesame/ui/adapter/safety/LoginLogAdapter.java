
package com.carlt.sesame.ui.adapter.safety;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.carlt.doride.R;
import com.carlt.sesame.data.safety.LoginLogInfo;
import com.carlt.sesame.http.AsyncImageLoader;

import java.util.ArrayList;

/**
 * 近期登录记录Adapter
 * 
 * @author daisy
 */
public class LoginLogAdapter extends BaseAdapter {
    private LayoutInflater mInflater;

    private AsyncImageLoader mAsyncImageLoader;

    private ArrayList<LoginLogInfo> mDataList;

    public LoginLogAdapter(Context context, ArrayList<LoginLogInfo> dataList) {
        mInflater = LayoutInflater.from(context);
        this.mDataList = dataList;
        mAsyncImageLoader = AsyncImageLoader.getInstance();
    }

    public void setmList(ArrayList<LoginLogInfo> mList) {
        this.mDataList = mList;
    }
    
    @Override
    public int getCount() {
        if (mDataList != null) {
            return mDataList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {
        Holder mHolder;
        if (convertView == null) {
            mHolder = new Holder();
            convertView = mInflater.inflate(R.layout.list_item_loginlog, null);
            convertView.setTag(mHolder);
            mHolder.mTxtName = (TextView)convertView.findViewById(R.id.item_loginlog_txt_name);
            mHolder.mTxtType = (TextView)convertView.findViewById(R.id.item_loginlog_txt_type);
            mHolder.mTxtTime = (TextView)convertView.findViewById(R.id.item_loginlog_txt_time);
            mHolder.mTxtModel = (TextView)convertView.findViewById(R.id.item_loginlog_txt_model);

        } else {
            mHolder = (Holder)convertView.getTag();
        }

        LoginLogInfo mInfo=mDataList.get(position);
        String s;
        s=mInfo.getName();
        if(s!=null&&s.length()>0){
            mHolder.mTxtName.setText(s);
        }else{
            mHolder.mTxtName.setText("--");
        }
        
        s=mInfo.getType();
        if(s!=null){
            if(s.equals(LoginLogInfo.TYPE_MAIN)){
                mHolder.mTxtType.setText("主机");
            }else{
                mHolder.mTxtType.setText("");
            }
        }

        s=mInfo.getTime();
        if(s!=null&&s.length()>0){
            mHolder.mTxtTime.setText(s);
        }else{
            mHolder.mTxtTime.setText("--");
        }
        
        s=mInfo.getModel();
        if(s!=null&&s.length()>0){
            mHolder.mTxtModel.setText(s);
        }else{
            mHolder.mTxtModel.setText("--");
        }
        return convertView;
    }

    class Holder {
        private TextView mTxtName;// 手机名称

        private TextView mTxtType;// 是否为主机

        private TextView mTxtTime;// 登录时间

        private TextView mTxtModel;// 手机型号

    }
}

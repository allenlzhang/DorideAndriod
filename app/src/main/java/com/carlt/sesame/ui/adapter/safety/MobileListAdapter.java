
package com.carlt.sesame.ui.adapter.safety;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.carlt.doride.R;
import com.carlt.doride.http.retrofitnet.model.OtherInfo;
import com.carlt.sesame.data.SesameLoginInfo;
import com.carlt.sesame.data.safety.MobileInfo;

import java.util.ArrayList;

/**
 * 近期登录记录Adapter
 * 
 * @author daisy
 */
public class MobileListAdapter extends BaseAdapter {
    private LayoutInflater mInflater;

    private ArrayList<MobileInfo> mDataList;

    private OnMobileBtnListener mListener;

    private boolean isInChangeStatus;// 是否是更换主机模式

    public boolean isInChangeStatus() {
        return isInChangeStatus;
    }

    public void setInChangeStatus(boolean isInChangeStatus) {
        this.isInChangeStatus = isInChangeStatus;
    }

    public MobileListAdapter(Context context, ArrayList<MobileInfo> dataList,
            OnMobileBtnListener listener) {
        mInflater = LayoutInflater.from(context);
        this.mDataList = dataList;
        mListener = listener;
    }

    public void setmList(ArrayList<MobileInfo> mList) {
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
    public View getView(final int position, View convertView, ViewGroup arg2) {
        Holder mHolder;
        if (convertView == null) {
            mHolder = new Holder();
            convertView = mInflater.inflate(R.layout.list_item_mobile, null);
            convertView.setTag(mHolder);
            mHolder.mImgChange = (ImageView)convertView.findViewById(R.id.item_mobile_img_change);
            mHolder.mTxtName = (TextView)convertView.findViewById(R.id.item_mobile_txt_name);
            mHolder.mTxtWaitingauthor = (TextView)convertView
                    .findViewById(R.id.item_mobile_txt_waitingauthor);
            mHolder.mTxtTime = (TextView)convertView.findViewById(R.id.item_mobile_txt_time);
            mHolder.mTxtModel = (TextView)convertView.findViewById(R.id.item_mobile_txt_model);

            mHolder.mTxtDel = (TextView)convertView.findViewById(R.id.item_mobile_txt_delete);
            mHolder.mTxtAgree = (TextView)convertView.findViewById(R.id.item_mobile_txt_agree);
            mHolder.mTxtRefuse = (TextView)convertView.findViewById(R.id.item_mobile_txt_refuse);
            mHolder.mViewWaitingauthor = convertView
                    .findViewById(R.id.item_mobile_lay_waitingauthor);

            if (OtherInfo.getInstance().isMain()) {
                mHolder.mTxtDel.setVisibility(View.VISIBLE);

            } else {
                mHolder.mTxtDel.setVisibility(View.GONE);
            }
        } else {
            mHolder = (Holder)convertView.getTag();
        }

        final MobileInfo mInfo = mDataList.get(position);
        String s;
        s = mInfo.getName();
        if (s != null && s.length() > 0) {
            mHolder.mTxtName.setText(s);
        } else {
            mHolder.mTxtName.setText("--");
        }

        s = mInfo.getTime();
        if (s != null && s.length() > 0) {
            mHolder.mTxtTime.setText(s);
        } else {
            mHolder.mTxtTime.setText("--");
        }

        s = mInfo.getModel();
        if (s != null && s.length() > 0) {
            mHolder.mTxtModel.setText(s);
        } else {
            mHolder.mTxtModel.setText("--");
        }

        OnClickListener mClickListener = new OnClickListener() {

            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.item_mobile_img_change:
                        // 更换主机
                        mListener.onChange(mInfo.getMobile_id(), position);
                        break;
                    case R.id.item_mobile_txt_delete:
                        // 删除
                        mListener.onDelete(mInfo.getMobile_id(), position);
                        break;
                    case R.id.item_mobile_txt_agree:
                        // 授权-同意
                        mListener.onAuthorAgree(mInfo.getMobile_id(), position);
                        break;
                    case R.id.item_mobile_txt_refuse:
                        // 授权-拒绝
                        mListener.onAuthorRefuse(mInfo.getMobile_id(), position);
                        break;
                }

            }
        };

        if (OtherInfo.getInstance().isMain()) {
            s = mInfo.getAuthorize_type();
            if (s != null) {
                if (s.equals(MobileInfo.AUTHORIZE_TYPE_UNDO)) {
                    // 未授权
                    mHolder.mTxtDel.setVisibility(View.GONE);
                    mHolder.mViewWaitingauthor.setVisibility(View.VISIBLE);
                    mHolder.mTxtWaitingauthor.setVisibility(View.VISIBLE);
                    mHolder.mTxtAgree.setOnClickListener(mClickListener);
                    mHolder.mTxtRefuse.setOnClickListener(mClickListener);
                    mHolder.mImgChange.setImageResource(R.drawable.change_master_nonclick);
                    mHolder.mImgChange.setClickable(false);
                    mHolder.mImgChange.setOnClickListener(null);
                    if (isInChangeStatus) {
                        mHolder.mImgChange.setVisibility(View.VISIBLE);
                    } else {
                        mHolder.mImgChange.setVisibility(View.GONE);
                    }
                } else if (s.equals(MobileInfo.AUTHORIZE_TYPE_DONE)) {
                    // 已授权
                    mHolder.mViewWaitingauthor.setVisibility(View.GONE);
                    mHolder.mTxtWaitingauthor.setVisibility(View.GONE);
                    mHolder.mTxtDel.setVisibility(View.VISIBLE);
                    mHolder.mTxtDel.setOnClickListener(mClickListener);
                    mHolder.mImgChange.setImageResource(R.drawable.change_master);
                    mHolder.mImgChange.setClickable(true);
                    mHolder.mImgChange.setOnClickListener(mClickListener);
                    if (isInChangeStatus) {
                        mHolder.mImgChange.setVisibility(View.VISIBLE);
                    } else {
                        mHolder.mImgChange.setVisibility(View.GONE);
                    }
                }
            }
        } else {
            mHolder.mTxtDel.setVisibility(View.GONE);
            mHolder.mViewWaitingauthor.setVisibility(View.GONE);
            mHolder.mTxtWaitingauthor.setVisibility(View.GONE);
        }

        return convertView;
    }

    class Holder {
        private ImageView mImgChange;// 更换主机按钮

        private TextView mTxtName;// 手机名称

        private TextView mTxtWaitingauthor;// 等待授权标识

        private TextView mTxtTime;// 登录时间

        private TextView mTxtModel;// 手机型号

        private TextView mTxtDel;// 删除按钮

        private TextView mTxtAgree;// 授权-同意按钮

        private TextView mTxtRefuse;// 授权-拒绝按钮

        private View mViewWaitingauthor;// 等待授权按钮

    }

    public interface OnMobileBtnListener {
        void onChange(String id, int position);// 更换主机

        void onDelete(String id, int position);// 删除

        void onAuthorAgree(String id, int position);// 同意授权

        void onAuthorRefuse(String id, int position);// 拒绝授权
    };
}

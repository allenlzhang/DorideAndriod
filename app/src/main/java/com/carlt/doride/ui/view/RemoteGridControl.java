/**
 *
 */
package com.carlt.doride.ui.view;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.carlt.doride.DorideApplication;
import com.carlt.doride.R;
import com.carlt.doride.data.remote.RemoteFunInfo;
import com.carlt.doride.utils.DisplayUtil;

import java.util.ArrayList;

/**
 * @author @Y.yun
 */
public class RemoteGridControl {

    private final static int[] imgIds = {R.drawable.horm, R.drawable.trunck, R.drawable.air_condition, R.drawable.open_lock, R.drawable.close_lock};

    private final static int[] imgIdAfters = {R.drawable.horm, R.drawable.trunck, R.drawable.open_lock, R.drawable.close_lock};

    private final static String[] names = {"闪灯鸣笛", "开启后备箱", "空调", "解锁", "落锁"};

    private final static String[] nameAfters = {"闪灯鸣笛", "开启后备箱", "解锁", "落锁"};
    Context         mCtx;
    LayoutInflater  mInflater;
    OnClickListener mOnclick;

    /**
     *
     */
    public RemoteGridControl(Context ctx) {
        this.mCtx = ctx;
        mInflater = LayoutInflater.from(mCtx);
    }

    public void setOnItemClick(OnClickListener onClick) {
        this.mOnclick = onClick;
    }

    // 只处理后装设备
    public void init4Views(LinearLayout mGridContainer) {
        LinearLayout line = createLinearLayout();
        mGridContainer.addView(line);
        int count = 2;
        for (int i = 0; i < nameAfters.length; i++) {
            if (i > 1 && i % 2 == 0) {
                line = createLinearLayout();
                mGridContainer.addView(line);
            }

            LinearLayout item = getRemoteItem(imgIdAfters[i], nameAfters[i], count);
            item.setTag(i);
            line.addView(item);
            item.setOnClickListener(mOnclick);
        }
    }

    // 处理前装/2016后装
    public void init5Views(LinearLayout mGridContainer) {
        LinearLayout line = createLinearLayout();
        mGridContainer.addView(line);
        for (int i = 0; i < names.length; i++) {
            if (i > 2 && i % 3 == 0) {
                line = createLinearLayout();
                mGridContainer.addView(line);
            }

            LinearLayout item = getRemoteItem(imgIds[i], names[i], 3);
            item.setTag(i);
            line.addView(item);
            item.setOnClickListener(mOnclick);
        }
    }

    // 处理x7
    public void init5x7Views(LinearLayout mGridContainer, ArrayList<RemoteFunInfo> mRemoteFunInfos) {
        LinearLayout line = createLinearLayout();
        mGridContainer.addView(line);
        int size = mRemoteFunInfos.size();
        if (size < 4) {
            for (int i = 0; i < size; i++) {
                int imgId = mRemoteFunInfos.get(i).getIcon_id();
                String str = mRemoteFunInfos.get(i).getName();
                LinearLayout item = getRemoteItem(imgId, str, 3);
                item.setTag(mRemoteFunInfos.get(i));
                line.addView(item);
                item.setOnClickListener(mOnclick);
            }
        }
        if (size == 4) {
            for (int i = 0; i < size; i++) {
                if (i > 1 && i % 2 == 0) {
                    line = createLinearLayout();
                    mGridContainer.addView(line);
                }
                int imgId = mRemoteFunInfos.get(i).getIcon_id();
                String str = mRemoteFunInfos.get(i).getName();
                LinearLayout item = getRemoteItem(imgId, str, 2);
                item.setTag(mRemoteFunInfos.get(i));
                line.addView(item);
                item.setOnClickListener(mOnclick);
            }
        }
        if (size > 4 && size < 7) {
            for (int i = 0; i < size; i++) {
                if (i > 2 && i % 3 == 0) {
                    line = createLinearLayout();
                    mGridContainer.addView(line);
                }
                int imgId = mRemoteFunInfos.get(i).getIcon_id();
                String str = mRemoteFunInfos.get(i).getName();
                LinearLayout item = getRemoteItem(imgId, str, 3);
                item.setTag(mRemoteFunInfos.get(i));
                line.addView(item);
                item.setOnClickListener(mOnclick);
            }
        }
        if (size > 6) {
            for (int i = 0; i < size; i++) {
                if (i > 3 && i % 4 == 0) {
                    line = createLinearLayout();
                    mGridContainer.addView(line);
                }
                int imgId = mRemoteFunInfos.get(i).getIcon_id();
                String str = mRemoteFunInfos.get(i).getName();
                LinearLayout item = getRemoteItem(imgId, str, 4);
                item.setTag(mRemoteFunInfos.get(i));
                line.addView(item);
                item.setOnClickListener(mOnclick);
            }
        }
        // for (int i = 0; i < size; i++) {
        // if (i > 2 && i % 3 == 0) {
        // line = createLinearLayout();
        // mGridContainer.addView(line);
        // }
        // int imgId = mRemoteFunInfos.get(i).getIcon_id();
        // String str = mRemoteFunInfos.get(i).getName();
        // LinearLayout item = getRemoteItem(imgId, str, 3);
        // item.setTag(mRemoteFunInfos.get(i));
        // line.addView(item);
        // item.setOnClickListener(mOnclick);
        // }
    }

    public LinearLayout createLinearLayout() {
        LinearLayout mLine = new LinearLayout(mCtx);
        mLine.setOrientation(LinearLayout.HORIZONTAL);
        mLine.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.topMargin = DisplayUtil.dip2px(mCtx, 15);
        mLine.setLayoutParams(params);
        return mLine;
    }

    public LinearLayout getRemoteItem(int imgRes, String name, int count) {
        LinearLayout v = (LinearLayout) mInflater.inflate(R.layout.grid_remote_item, null);
        ImageView mImageView = (ImageView) v.findViewById(R.id.remote_item_img_icon);
        TextView mTextView = (TextView) v.findViewById(R.id.remote_item_txt_name);
        mImageView.setImageResource(imgRes);
        mTextView.setText(name);
        LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(DorideApplication.ScreenWith / count, LinearLayout.LayoutParams.WRAP_CONTENT);
        v.setLayoutParams(llp);
        return v;
    }

}

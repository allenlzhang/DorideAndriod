
package com.carlt.doride.ui.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.carlt.doride.R;
import com.carlt.doride.data.remote.RemoteLogInfo;

import java.util.ArrayList;

/**
 * 远程登录记录Adapter
 * @author daisy
 */
public class RemoteLogAdapter extends BaseAdapter {
    private LayoutInflater mInflater;

    private ArrayList<RemoteLogInfo> mDataList;

    private Resources mResources;

    public RemoteLogAdapter(Context context, ArrayList<RemoteLogInfo> dataList) {
        mInflater = LayoutInflater.from(context);
        this.mDataList = dataList;
        mResources = context.getResources();
    }

    public void setmList(ArrayList<RemoteLogInfo> mList) {
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
    public RemoteLogInfo getItem(int position) {
        return mDataList.get(position);
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
            convertView = mInflater.inflate(R.layout.list_item_remotelog, null);
            convertView.setTag(mHolder);
            mHolder.mTxtName = (TextView) convertView.findViewById(R.id.item_remotelog_txt_name);
            mHolder.mTxtResult = (ImageView) convertView.findViewById(R.id.item_remotelog_iv_result);
            mHolder.mTxtDevice = (TextView) convertView.findViewById(R.id.item_remotelog_txt_device);
            mHolder.mTxtTime = (TextView) convertView.findViewById(R.id.item_remotelog_txt_time);

        } else {
            mHolder = (Holder) convertView.getTag();
        }

        RemoteLogInfo mInfo = mDataList.get(position);
        String s = "--";
        int type = mInfo.getLogtype();
        //        Logger.e("type-----" + type);
        if (type > 0) {
            mHolder.mTxtName.setText(adapterTypeText(type));
        } else {
            mHolder.mTxtName.setText("--");
        }


        s = mInfo.getLog_result();
        if (TextUtils.equals(s, RemoteLogInfo.RESULT_SUCCESS)) {
            mHolder.mTxtResult.setBackgroundResource(R.drawable.text_bg_green);
        } else {
            mHolder.mTxtResult.setBackgroundResource(R.drawable.text_bg_red);
        }
        s = mInfo.getLog_device_name();
        if (!TextUtils.isEmpty(s)) {
            mHolder.mTxtDevice.setText(s);
        } else {
            mHolder.mTxtDevice.setText("--");
        }

        s = mInfo.getLogtime();
        if (!TextUtils.isEmpty(s)) {
            mHolder.mTxtTime.setText(s);
        } else {
            mHolder.mTxtTime.setText("--");
        }
        return convertView;
    }

    public final static int TYPE_FLASHING               = 11;//闪灯鸣笛
    public final static int TYPE_UNLOCK                 = 21;//解锁
    public final static int TYPE_LOCK                   = 22;//落锁
    public final static int TYPE_START                  = 31;//启动
    public final static int TYPE_STOP                   = 41;//熄火
    public final static int TYPE_AIROPEN                = 51;//远程开启空调
    public final static int TYPE_AIRCLOSE               = 52;//远程关闭空调
    public final static int TYPE_AIRDEFROST             = 53;//空调/一键除霜
    public final static int TYPE_AIRCOLD                = 54;//空调/最大制冷
    public final static int TYPE_AIRHOT                 = 55;//空调/最大制热
    public final static int TYPE_AIRANION               = 56;//空调/负离子
    public final static int TYPE_AIRCLEAN               = 57;//空调/座舱清洁
    public final static int TYPE_AIRTEMP                = 58;//空调/温度调节
    public final static int TYPE_WINOPEN                = 61;//远程开窗
    public final static int TYPE_WINCLOSE               = 62;//远程关窗
    public final static int TYPE_SKYLIGHT_OPEN          = 63;//远程开启天窗
    public final static int TYPE_SKYLIGHT_CLOSE         = 64;//远程关闭天窗
    public final static int TYPE_SKYLIGHT_UP            = 65;//远程开侨天窗
    public final static int TYPE_TRUNKOPEN              = 71;//远程开启后备箱
    public final static int TYPE_TRUNKCLOSE             = 72;//远程开启后备箱
    public final static int TYPE_SEATOPEN               = 81;//远程开启座椅加热
    public final static int TYPE_SEATCLOSE              = 82;//远程关闭座椅加热
    public final static int TYPE_PURIFYOPEN             = 91;//远程开启空气净化
    public final static int TYPE_PURIFYCLOSE            = 92;//远程关闭空气净化
    public final static int TYPE_START_INPOWER          = 93;// 远程立即充电
    public final static int TYPE_TIMEING_INPOWER        = 94;// 远程定时冲电
    public final static int TYPE_STOP_INPOWER           = 95; //远程停止充电
    public final static int TYPE_CANCEL_TIMEING_INPOWER = 96; //取消定时充电

    private String adapterTypeText(int type) {
        String typeString = "--";
        switch (type) {
            case TYPE_FLASHING:
                typeString = "声光寻车";
                break;
            case TYPE_UNLOCK:
                typeString = "远程解锁";
                break;
            case TYPE_LOCK:
                typeString = "远程落锁";
                break;
            case TYPE_START:
                typeString = "远程启动";
                break;
            case TYPE_STOP:
                typeString = "远程熄火";
                break;
            case TYPE_AIROPEN:
                typeString = "远程开启空调";
                break;
            case TYPE_AIRCLOSE:
                typeString = "远程关闭空调";
                break;
            case TYPE_AIRDEFROST:
                typeString = "远程开启空调/一键除霜";
                break;
            case TYPE_AIRCOLD:
                typeString = "远程开启空调/最大制冷";
                break;
            case TYPE_AIRHOT:
                typeString = "远程开启空调/最大制热";
                break;
            case TYPE_AIRANION:
                typeString = "远程开启空调/负离子";
                break;
            case TYPE_AIRCLEAN:
                typeString = "远程开启空调/座舱清洁";
                break;
            case TYPE_AIRTEMP:
                typeString = "远程温度调节";
                break;
            case TYPE_WINOPEN:
                typeString = "远程开窗";
                break;
            case TYPE_WINCLOSE:
                typeString = "远程关窗";
                break;
            case TYPE_SKYLIGHT_OPEN:
                typeString = "远程开启天窗";
                break;
            case TYPE_SKYLIGHT_CLOSE:
                typeString = "远程关闭天窗";
                break;
            case TYPE_SKYLIGHT_UP:
                typeString = "远程开翘天窗";
                break;
            case TYPE_TRUNKOPEN:
                typeString = "远程开启后备箱";
                break;
            case TYPE_TRUNKCLOSE:
                typeString = "远程关闭后备箱";
                break;
            case TYPE_SEATOPEN:
                typeString = "远程开启座椅加热";
                break;
            case TYPE_SEATCLOSE:
                typeString = "远程关闭座椅加热";
                break;
            case TYPE_PURIFYOPEN:
                typeString = "远程开启空气净化";
                break;
            case TYPE_PURIFYCLOSE:
                typeString = "远程关闭空气净化";
                break;
            case TYPE_START_INPOWER:
                typeString = "远程立即充电";
                break;
            case TYPE_TIMEING_INPOWER:
                typeString = "远程定时冲电";
                break;
            case TYPE_STOP_INPOWER:
                typeString = "远程停止充电";
                break;
            case TYPE_CANCEL_TIMEING_INPOWER:
                typeString = "取消定时充电";
                break;
            default:
                typeString = "--";
                break;
        }
        return typeString;
    }

    static class Holder {

        private TextView mTxtName;// 操作名称

        private ImageView mTxtResult;// 操作结果

        private TextView mTxtDevice;// 操作来源

        private TextView mTxtTime;// 操作时间

    }
}

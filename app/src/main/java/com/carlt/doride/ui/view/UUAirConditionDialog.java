package com.carlt.doride.ui.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.carlt.doride.DorideApplication;
import com.carlt.doride.R;
import com.carlt.doride.control.CPControl;
import com.carlt.doride.data.remote.AirMainInfo;
import com.carlt.doride.data.remote.RemoteFunInfo;
import com.carlt.doride.protocolparser.BaseParser;
import com.carlt.doride.ui.adapter.RemoteAirAdapter;
import com.carlt.doride.utils.MyParse;
import com.carlt.doride.utils.PlayRadio;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;


public class UUAirConditionDialog extends Dialog implements OnClickListener,
        OnSeekBarChangeListener {

    protected ImageView up;
    protected ImageView down;

    protected TextView tempure;// 温度数值
    protected TextView tempuredes;// 温度描述

    protected SeekBar mSeekBar;

    protected TextView confirm;

    protected TextView cancle;

    protected TextView auto;// 全自动

    protected TextView heat_max;// 最大制热

    protected TextView cold_max;// 最大制冷

    protected TextView defrost;// 一件除霜

    protected TextView anion;// 负离子

    protected TextView clean;// 座舱清洁

    protected TextView closeAir;// 关闭空调

    protected GridView     mGrid;
    protected LinearLayout mLayFunction;
    protected int selectPos = -1;

    private RelativeLayout mLayTempuare;

    private final static int w_dip = 300;

    public BaseParser.ResultCallback mListener;

    public android.view.View.OnClickListener mViewOutClick;

    public Handler mHandler;

    public String state = "-1";

    private static final int COLOR_BG_SELECT = Color.parseColor("#3EC0EA");

    private static final int COLOR_BG_NORMAL = Color.parseColor("#F0F0F0");

    public static final int COLOR_FONT_SELECT = Color.parseColor("#43c1ea");

    public static final int COLOR_FONT_NORMAL = Color.parseColor("#333333");

    private Resources mResources;

    private boolean isSelect;// 用户是否已经对功能做出了选择

    private int clickCount;// 用户点击次数

    private PlayRadio mPlayRadio;

    private AirMainInfo              mAirMainInfo;
    private ArrayList<RemoteFunInfo> mRemoteFunInfos;

    private RemoteAirAdapter mAdapter;

    private int     tempCurrent;// 当前温度
    private boolean isShowTempRegulation;// 是否展示温度调节按钮

    public UUAirConditionDialog(Context context, AirMainInfo airMainInfo) {
        super(context, R.style.dialog);
        mResources = context.getResources();
        this.mAirMainInfo = airMainInfo;
        LayoutInflater inflater = LayoutInflater.from(context);
        final View v = inflater.inflate(R.layout.dialog_air_condition, null);
        up = (ImageView) v.findViewById(R.id.dialog_air_img_temp_right);
        down = (ImageView) v.findViewById(R.id.dialog_air_img_temp_left);

        mSeekBar = (SeekBar) v.findViewById(R.id.dialog_air_seekBar);
        tempure = (TextView) v.findViewById(R.id.dialog_air_txt_tempure);
        tempuredes = (TextView) v.findViewById(R.id.dialog_air_txt_temp_des);
        confirm = (TextView) v.findViewById(R.id.dialog_air_txt_confirm);
        cancle = (TextView) v.findViewById(R.id.dialog_air_txt_cancle);
        closeAir = (TextView) v.findViewById(R.id.dialog_air_txt_closeAir);

        // auto = (TextView) v.findViewById(R.id.dialog_air_txt_auto);
        heat_max = (TextView) v.findViewById(R.id.dialog_air_txt_heatMax);
        cold_max = (TextView) v.findViewById(R.id.dialog_air_txt_coldMax);
        defrost = (TextView) v.findViewById(R.id.dialog_air_txt_defrost);
        // anion = (TextView) v.findViewById(R.id.dialog_air_txt_anion);
        // clean = (TextView) v.findViewById(R.id.dialog_air_txt_clean);
        mLayTempuare = (RelativeLayout) v
                .findViewById(R.id.dialog_air_lay_tempure);

        mGrid = (GridView) v.findViewById(R.id.dialog_air_grid);
        mLayFunction = (LinearLayout) v.findViewById(R.id.dialog_air_lay);

        mGrid.setOnItemClickListener(mItemClickListener);
        boolean isShowTemp = false;
        if (mAirMainInfo != null) {
            isShowTemp = mAirMainInfo.isShowTemp();
            mGrid.setVisibility(View.VISIBLE);
            mLayFunction.setVisibility(View.GONE);
            mRemoteFunInfos = mAirMainInfo.getmRemoteFunInfos();
            state = mAirMainInfo.getState();
            mAdapter = new RemoteAirAdapter(context);
            Logger.e("mRemoteFunInfos.size----" + mRemoteFunInfos.size());
            mAdapter.setmDataList(mRemoteFunInfos);
            mGrid.setAdapter(mAdapter);
            if (mAirMainInfo.equals(RemoteFunInfo.MODE_TEMPREGULATION)) {
                isShowTempRegulation = true;
            } else {
                isShowTempRegulation = false;
            }
            tempCurrent = MyParse.parseInt(mAirMainInfo.getCurrentTemp());
            tempure.setText(tempCurrent + "");

            if (mAirMainInfo.isGetCurrentTempSuccess()) {
                tempuredes.setVisibility(View.VISIBLE);
            } else {
                tempuredes.setVisibility(View.GONE);
            }
        } else {
            isShowTemp = true;
            isShowTempRegulation = true;
        }

        if (isShowTemp) {
            mLayTempuare.setVisibility(View.VISIBLE);
        } else {
            mLayTempuare.setVisibility(View.GONE);
        }
        if (isShowTempRegulation) {
            up.setVisibility(View.VISIBLE);
            down.setVisibility(View.VISIBLE);
        } else {
            up.setVisibility(View.GONE);
            down.setVisibility(View.GONE);
        }

        mSeekBar.setOnSeekBarChangeListener(this);
        mSeekBar.setEnabled(false);// 默认不可以拖拽
        // auto.setOnClickListener(this);
        heat_max.setOnClickListener(this);
        cold_max.setOnClickListener(this);
        defrost.setOnClickListener(this);
        // anion.setOnClickListener(this);
        // clean.setOnClickListener(this);
        closeAir.setOnClickListener(this);

        refreshUIData(-1);
        int w = (int) (DorideApplication.ScreenDensity * w_dip);
        setCanceledOnTouchOutside(true);
        ViewGroup.LayoutParams parm = new ViewGroup.LayoutParams(w,
                LayoutParams.WRAP_CONTENT);
        setContentView(v, parm);
        setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode,
                                 KeyEvent event) {
                return true;
            }
        });

        up.setOnClickListener(this);
        down.setOnClickListener(this);
        confirm.setOnClickListener(this);
        cancle.setOnClickListener(this);

        confirm.setClickable(false);
        confirm.setTextColor(Color.parseColor("#a0a0a0"));

        mPlayRadio = PlayRadio.getInstance(context);
        initSelect();
    }

    public void setState(String state) {
        this.state = state;
        // if(state.equals("2")){
        // this.state = "8";
        // }
        initSelect();
    }

    @Override
    public void onClick(View arg0) {

        if (arg0.equals(confirm)) {

            if (mViewOutClick != null) {
                arg0.setTag(state);
                mViewOutClick.onClick(arg0);
            }
            if (mHandler != null && mListener != null) {
                mHandler.sendEmptyMessage(11);

                if (state.equals(RemoteFunInfo.MODE_CLOSE)) {
                    // 关闭空调
                    CPControl.GetRemoteAir(mListener, "2", "");
                } else {
                    CPControl.GetRemoteAir(mListener, state, tempure.getText()
                            .toString());
                }
            }
        } else if (arg0.equals(cancle)) {
            dismiss();
        } else if (arg0.equals(auto)) {
            state = RemoteFunInfo.MODE_AUTO;
            initSelect();
            mSeekBar.setEnabled(false);
            mSeekBar.setProgress(caculateProgress(22));
            if (!isSelect) {
                isSelect = true;
            }
            clickCount++;
        } else if (arg0.equals(heat_max)) {
            state = RemoteFunInfo.MODE_HEAT;
            initSelect();
            tempure.setText(32 + "");
            mSeekBar.setEnabled(false);
            mSeekBar.setProgress(mSeekBar.getMax());
            if (!isSelect) {
                isSelect = true;
            }
            clickCount++;
        } else if (arg0.equals(cold_max)) {
            state = RemoteFunInfo.MODE_COLD;
            initSelect();
            tempure.setText(18 + "");
            mSeekBar.setEnabled(false);
            mSeekBar.setProgress(0);
            if (!isSelect) {
                isSelect = true;
            }
            clickCount++;
        } else if (arg0.equals(defrost)) {
            state = RemoteFunInfo.MODE_FROG;
            initSelect();
            // up.setClickable(false);
            // up.setImageResource(R.drawable.air_arrow_up_dan);
            // down.setClickable(false);
            // down.setImageResource(R.drawable.air_arrow_down_dan);
            tempure.setText(32 + "");
            mSeekBar.setEnabled(false);
            mSeekBar.setProgress(caculateProgress(32));
            if (!isSelect) {
                isSelect = true;
            }
            clickCount++;
        } else if (arg0.equals(anion)) {
            state = RemoteFunInfo.MODE_ANION;
            initSelect();
            // up.setClickable(false);
            // up.setImageResource(R.drawable.air_arrow_up_dan);
            // down.setClickable(false);
            // down.setImageResource(R.drawable.air_arrow_down_dan);
            mSeekBar.setEnabled(false);
            if (!isSelect) {
                isSelect = true;
            }
            clickCount++;
        } else if (arg0.equals(clean)) {
            state = RemoteFunInfo.MODE_CLEAN;
            initSelect();
            // up.setClickable(false);
            // up.setImageResource(R.drawable.air_arrow_up_dan);
            // down.setClickable(false);
            // down.setImageResource(R.drawable.air_arrow_down_dan);
            mSeekBar.setEnabled(false);
            if (!isSelect) {
                isSelect = true;
            }
            clickCount++;
        } else if (arg0.equals(closeAir)) {
            state = RemoteFunInfo.MODE_CLOSE;
            initSelect();
            // tempure.setText("--");
            // onClick(confirm);
            mSeekBar.setEnabled(false);
            if (!isSelect) {
                isSelect = true;
            }
            clickCount++;
        } else if (arg0.equals(up)) {
            tempCurrent++;
            tempure.setText(tempCurrent + "");
            refreshUIData(-1);
        } else if (arg0.equals(down)) {
            Log.e("info", "down--tempCurrent==" + tempCurrent);
            tempCurrent--;
            tempure.setText(tempCurrent + "");
            refreshUIData(-1);
        }
        initConfirm();
    }

    /**
     *
     */
    public void initConfirm() {
        if (isSelect) {
            confirm.setClickable(true);
            confirm.setTextColor(Color.parseColor("#1783FE"));
        }
    }

    public void reCall() {
        if (state.equals(RemoteFunInfo.MODE_CLOSE)) {
            CPControl.GetRemoteAir(mListener, RemoteFunInfo.MODE_CLOSE, "");
        } else {
            CPControl.GetRemoteAir(mListener, state, tempure.getText()
                    .toString());
        }

    }

    public void initSelect() {
        reSet();

        if (state.equals(RemoteFunInfo.MODE_AUTO)) {
            isSelect = true;
            //tempure.setText(24 + "");
        } else if (state.equals(RemoteFunInfo.MODE_FROG)) {
            //tempure.setText(32 + "");
            isSelect = true;
            defrost.setCompoundDrawablesWithIntrinsicBounds(null,
                    mResources.getDrawable(R.mipmap.remote_frost_selected),
                    null, null);
            defrost.setTextColor(COLOR_FONT_SELECT);
        } else if (state.equals(RemoteFunInfo.MODE_COLD)) {
            //tempure.setText(18 + "");
            isSelect = true;
            cold_max.setCompoundDrawablesWithIntrinsicBounds(null,
                    mResources.getDrawable(R.mipmap.remote_cold_selected),
                    null, null);
            cold_max.setTextColor(COLOR_FONT_SELECT);
        } else if (state.equals(RemoteFunInfo.MODE_HEAT)) {
            //tempure.setText(32 + "");
            isSelect = true;
            heat_max.setCompoundDrawablesWithIntrinsicBounds(null,
                    mResources.getDrawable(R.mipmap.remote_hot_selected),
                    null, null);
            heat_max.setTextColor(COLOR_FONT_SELECT);
        } else if (state.equals(RemoteFunInfo.MODE_ANION)) {
            //tempure.setText(22 + "");
            isSelect = true;
        } else if (state.equals(RemoteFunInfo.MODE_CLEAN)) {
            //tempure.setText(22 + "");
            isSelect = true;
        } else if (state.equals(RemoteFunInfo.MODE_CLOSE)) {
            closeAir.setCompoundDrawablesWithIntrinsicBounds(null,
                    mResources.getDrawable(R.mipmap.icon_close_air_press),
                    null, null);
            closeAir.setTextColor(COLOR_FONT_SELECT);
        } else if (state.equals(RemoteFunInfo.MODE_TEMPREGULATION)) {
            closeAir.setCompoundDrawablesWithIntrinsicBounds(null,
                    mResources.getDrawable(R.mipmap.icon_close_air_press),
                    null, null);
            closeAir.setTextColor(COLOR_FONT_SELECT);
        }

        initConfirm();
    }

    public void reSet() {
        heat_max.setCompoundDrawablesWithIntrinsicBounds(null,
                mResources.getDrawable(R.mipmap.remote_hot_selected_no),
                null, null);
        heat_max.setTextColor(COLOR_FONT_NORMAL);
        cold_max.setCompoundDrawablesWithIntrinsicBounds(null,
                mResources.getDrawable(R.mipmap.remote_cold_selected_no),
                null, null);
        cold_max.setTextColor(COLOR_FONT_NORMAL);
        defrost.setCompoundDrawablesWithIntrinsicBounds(null,
                mResources.getDrawable(R.mipmap.remote_frost_selected_no),
                null, null);
        defrost.setTextColor(COLOR_FONT_NORMAL);
        closeAir.setCompoundDrawablesWithIntrinsicBounds(null,
                mResources.getDrawable(R.mipmap.icon_close_air), null, null);
        closeAir.setTextColor(COLOR_FONT_NORMAL);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress,
                                  boolean fromUser) {
        if (fromUser) {
            if (state.equals(RemoteFunInfo.MODE_COLD)
                    || state.equals(RemoteFunInfo.MODE_HEAT)) {
                state = RemoteFunInfo.MODE_AUTO;
                initSelect();
            }
        }

        int temprature = 18 + progress;
        tempure.setText(temprature + "");

    }

    private int caculateProgress(int temprature) {
        int progress = 0;
        progress = temprature - 18;
        return progress;
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        // TODO Auto-generated method stub

    }

    /**
     * UI刷新
     * @param position
     */
    private void refreshUIData(int position) {
        if (mRemoteFunInfos != null) {
            int size = mRemoteFunInfos.size();
            if (position < 0) {
                for (int i = 0; i < size; i++) {
                    RemoteFunInfo mInfo = mRemoteFunInfos.get(i);
                    if (mInfo.isSelect()) {
                        position = i;
                    }
                }
            } else {

            }
            selectPos = position;
            for (int i = 0; i < size; i++) {
                RemoteFunInfo mInfo = mRemoteFunInfos.get(i);
                if (i == position) {
                    mInfo.setSelect(true);
                    state = mInfo.getId();
                    Log.e("info", "state===air------" + state);
                    if (!state.equals(RemoteFunInfo.MODE_TEMPREGULATION)) {
                        up.setVisibility(View.GONE);
                        down.setVisibility(View.GONE);
                    } else {
                        up.setVisibility(View.VISIBLE);
                        down.setVisibility(View.VISIBLE);
                    }
                    Log.e("info", "tempCurrent==" + tempCurrent);
                    if (tempCurrent >= 32) {
                        up.setImageResource(R.mipmap.temp_right_disable);
                        up.setClickable(false);
                    } else {
                        up.setImageResource(R.drawable.temp_right);
                        up.setClickable(true);
                    }
                    if (tempCurrent <= 18) {
                        down.setImageResource(R.mipmap.temp_left_disable);
                        down.setClickable(false);
                    } else {
                        down.setImageResource(R.drawable.temp_left);
                        down.setClickable(true);
                    }
                    if (!isSelect) {
                        isSelect = true;
                    }
                } else {
                    mInfo.setSelect(false);
                }
            }
            if (tempCurrent != MyParse.parseInt(mAirMainInfo.getCurrentTemp())) {
                tempuredes.setText("");
            }

        }
    }

    private OnItemClickListener mItemClickListener = new OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                long arg3) {
            Log.e("info", "AirCondition--OnItemClickListener------------");
            clickCount++;
            refreshUIData(position);
            mAdapter.notifyDataSetChanged();
            initConfirm();
        }
    };
}

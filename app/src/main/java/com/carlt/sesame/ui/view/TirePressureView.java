
package com.carlt.sesame.ui.view;

import android.content.Context;
import android.graphics.drawable.ClipDrawable;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.Spanned;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.carlt.sesame.R;

/**
 * 胎压监测-轮胎view
 * 
 * @author daisy
 */
public class TirePressureView extends RelativeLayout {

    private TextView mTxtTireValue;//胎压值

    private ImageView mViewColor;

    private ImageView mViewMark;// 问号

    private Context mContext;

    private ClipDrawable mClipDrawable;

    private int finalProgress;// 最终大小

    private int mProgress;// 介值

    private int mProgress_new;// 当次新值

    private int mProgress_old;// 上一次的值

    private static final int MAX_PROGRESS = 10000;// ClipDrawable的level最大值（为最大值时显示全部颜色）

    private final static int SPEED_DOWN = 9;// 下降速度

    private final static int SPEED_UP = 9;// 上升速度

    public final static int ABNORMAL = 100;// 异常

    public final static int NORMAL = 200;// 正常

    public final static int UNACTIVATION = 300;// 未激活

    public final static int ACTIVATIONING = 400;// 激活中

    public final static int CONNECT_ERRO = 500;// 获取数据失败

    public TirePressureView(Context context) {
        super(context);
        mContext = context;
        mTxtTireValue = new TextView(context);
        mViewColor = new ImageView(context);
        mViewMark = new ImageView(context);
        init();
    }

    public TirePressureView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mTxtTireValue = new TextView(context);
        mViewColor = new ImageView(context);
        mViewMark = new ImageView(context);
        init();
    }

    private void init() {
        LayoutParams mLayoutParams;
        mLayoutParams = new LayoutParams(LayoutParams.FILL_PARENT,
                LayoutParams.FILL_PARENT);
        mLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        mViewColor.setLayoutParams(mLayoutParams);
        addView(mViewColor);

        mLayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        mLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        mTxtTireValue.setLayoutParams(mLayoutParams);
        mTxtTireValue.setBackgroundDrawable(mContext.getResources().getDrawable(
                R.drawable.tire_wheel_bg));
        mTxtTireValue.setGravity(Gravity.CENTER);
        addView(mTxtTireValue);

        mLayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        mLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        mViewMark.setLayoutParams(mLayoutParams);
        addView(mViewMark);

    }

    public void setTireState(int tireState,int tireValue,String tireUnit) {
    	String str="<font color=\"#000000\">"+tireValue+"</font>"  
                + "<font color= \"#666666\">"+tireUnit+"</font>"; 
    	Spanned spanned=Html.fromHtml(str);

        switch (tireState) {
            case ABNORMAL:
                // 异常
                mViewColor.setImageResource(R.drawable.tire_loading_red);
                mClipDrawable = (ClipDrawable)mViewColor.getDrawable();
                mTxtTireValue.setText(spanned);
                mViewMark.setVisibility(View.GONE);
                break;

            case NORMAL:
                // 正常
                mViewColor.setImageResource(R.drawable.tire_loading_green);
                mClipDrawable = (ClipDrawable)mViewColor.getDrawable();
                mTxtTireValue.setText(spanned);
                mViewMark.setVisibility(View.GONE);

                break;
            case UNACTIVATION:
                // 未激活
                mViewColor.setImageResource(R.drawable.tire_loading_tansparent);
                mClipDrawable = (ClipDrawable)mViewColor.getDrawable();
                mTxtTireValue.setText("未激活");
                mViewMark.setVisibility(View.GONE);
                break;
            case ACTIVATIONING:
                // 激活中
                mViewColor.setImageResource(R.drawable.tire_loading_tansparent);
                mClipDrawable = (ClipDrawable)mViewColor.getDrawable();
                mTxtTireValue.setText("激活中");
                mViewMark.setVisibility(View.GONE);
                break;
            case CONNECT_ERRO:
                // 获取数据失败
                mViewColor.setImageResource(R.drawable.tire_loading_tansparent);
                mClipDrawable = (ClipDrawable)mViewColor.getDrawable();
                mTxtTireValue.setText("");
                mViewMark.setVisibility(View.VISIBLE);
                mViewMark.setImageDrawable(mContext.getResources().getDrawable(
                        R.drawable.icon_question_mark2));
                break;
        }
    }

    public void setTireValue(int tireValue) {
    	//v1.3.0开始需要填充满全部的轮胎
    	tireValue=100;
    	//v1.3.0添加结束
        mProgress = mProgress_old;
        mProgress_new = tireValue * MAX_PROGRESS / 100;
        finalProgress = tireValue * MAX_PROGRESS / 100;

        Thread s = new Thread(r_down);
        s.start();

    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // 如果消息是本程序发送的
            switch (msg.what) {
                case 1:
                    mClipDrawable.setLevel(mProgress);
                    break;

                case 10:
                    mProgress = 0;
                    Thread s = new Thread(r_up);
                    s.start();
                    break;
            }
        }
    };

    private boolean running_dowm;

    private boolean running_up;

    Runnable r_down = new Runnable() {
        @Override
        public void run() {
            running_dowm = true;
            while (running_dowm) {
                if (mProgress <= 0) {
                    running_dowm = false;
                    mProgress = 0;
                    mHandler.sendEmptyMessage(10);
                }
                mHandler.sendEmptyMessage(1);
                mProgress -= 100;
                try {
                    Thread.sleep(SPEED_DOWN);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    Runnable r_up = new Runnable() {
        @Override
        public void run() {
            running_up = true;
            while (running_up) {
                if (mProgress >= finalProgress) {
                    running_up = false;
                    mProgress_old = mProgress_new;
                }
                mHandler.sendEmptyMessage(1);
                mProgress += 100;
                try {
                    Thread.sleep(SPEED_UP);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };

}


package com.carlt.sesame.ui.view.general;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.carlt.sesame.R;

/**
 * 通用座驾新增功能基类view
 * 
 * @author daisy
 */
public abstract class GeneralBaseView extends LinearLayout {
    private ImageView mImg;

    private TextView mTxt;

    private int[] imgIds = new int[4];// 三种状态不同的背景图id 0:不支持 1：支持-单例 2：支持-开启
                                      // 3：支持-关闭

    private String[] stateStrings = new String[4];// 三种状态不同的话术 0:不支持 1：支持-单例
                                                  // 2：支持-开启 3：支持-关闭

    private String name;// 功能名称

    public static final int STATE_UNAVAILABLE = 0;// 不支持

    public static final int STATE_AVAILABLE_SINGLE = 1;// 支持-仅有一种状态（类似按钮）

    public static final int STATE_AVAILABLE_ON = 2;// 支持-功能已开启

    public static final int STATE_AVAILABLE_OFF = 3;// 支持-功能已关闭

    protected Context context;

    public GeneralBaseView(Context context) {
        super(context);
        this.context = context;

        init();
        setOnClickListener(mClickListener);
    }

    // public GeneralBaseView(Context context, AttributeSet attrs) {
    // super(context, attrs);
    // init(context);
    // setOnClickListener(mClickListener);
    // }
    //
    // public GeneralBaseView(Context context, AttributeSet attrs, int defStyle)
    // {
    // super(context, attrs, defStyle);
    // init(context);
    // setOnClickListener(mClickListener);
    // }

    private void init() {
        LayoutInflater mInflater = LayoutInflater.from(context);
        View mView = mInflater.inflate(R.layout.layout_general_baseview, null);
        mImg = (ImageView)mView.findViewById(R.id.layout_general_baseview_img);
        mTxt = (TextView)mView.findViewById(R.id.layout_general_baseview_txt);

        addView(mView);
    }

    protected void setImgIds(int[] imgIds) {
        this.imgIds = imgIds;
    }

    protected String[] getStateStrings() {
        return stateStrings;
    }

    public void setStateStrings(String[] stateStrings) {
        this.stateStrings = stateStrings;
    }

    /**
     * 设置状态
     * 
     * @param state：view当前状态
     */
    protected void setState(int state) {
        switch (state) {
            case STATE_UNAVAILABLE:
                // 不支持
                mImg.setImageResource(imgIds[0]);
                mTxt.setText(stateStrings[0]);
                mTxt.setTextColor(Color.argb(255, 69, 178, 114));
                setClickable(false);
                break;
            case STATE_AVAILABLE_SINGLE:
                // 支持-单例
                mImg.setImageResource(imgIds[1]);
                mTxt.setText(stateStrings[1]);
                mTxt.setTextColor(Color.WHITE);

                setClickable(true);
                break;
            case STATE_AVAILABLE_ON:
                // 支持-功能开启
                mImg.setImageResource(imgIds[2]);
                mTxt.setText(stateStrings[2]);
                mTxt.setTextColor(Color.WHITE);

                setClickable(true);
                break;
            case STATE_AVAILABLE_OFF:
                // 支持-功能关闭
                mImg.setImageResource(imgIds[3]);
                mTxt.setText(stateStrings[3]);
                mTxt.setTextColor(Color.WHITE);

                setClickable(true);
                break;
        }
    }

    private OnClickListener mClickListener = new OnClickListener() {

        @Override
        public void onClick(View arg0) {
            onViewClick();

        }
    };

    /**
     * view点击事件
     */
    protected abstract void onViewClick();
}

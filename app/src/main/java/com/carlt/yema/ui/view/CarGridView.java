package com.carlt.yema.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.GridView;

/**
 * 座驾相关 GridView
 *
 * @author Administrator
 */
public class CarGridView extends GridView {
    private boolean isScrollable;//是否允許滑動


    public void setScrollable(boolean isScrollable) {
        this.isScrollable = isScrollable;
    }

    public CarGridView(Context context) {
        super(context);
    }


    public CarGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CarGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            if (isScrollable) {

            } else {
                return true;
            }/**只要简单改下这里就可以禁止Gridview进行滑动*/
        }
        return super.dispatchTouchEvent(ev);
    }


}

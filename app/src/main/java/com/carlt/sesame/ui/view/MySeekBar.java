
package com.carlt.sesame.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.SeekBar;

/**
 * 自定义SeekBar 禁止了它的拖拽功能
 * @author daisy
 *
 */
public class MySeekBar extends SeekBar {

    public MySeekBar(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public MySeekBar(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.seekBarStyle);
    }

    public MySeekBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //return super.onTouchEvent(event);
        return false;
    }
}

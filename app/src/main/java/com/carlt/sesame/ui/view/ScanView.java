
package com.carlt.sesame.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import com.carlt.sesame.R;

/**
 * 扫描框
 * 
 * @author daisy
 */
public class ScanView extends View {

    private Paint finderMaskPaint;

    private int measureedWidth;

    private int measureedHeight;

    public ScanView(Context context) {
        super(context);
        init(context);
    }

    public ScanView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRect(leftRect, finderMaskPaint);
        canvas.drawRect(topRect, finderMaskPaint);
        canvas.drawRect(rightRect, finderMaskPaint);
        canvas.drawRect(bottomRect, finderMaskPaint);
        // 画框
        zx_code_kuang.setBounds(middleRect);
        zx_code_kuang.draw(canvas);

    }

    private Rect topRect = new Rect();

    private Rect bottomRect = new Rect();

    private Rect rightRect = new Rect();

    private Rect leftRect = new Rect();

    private Rect middleRect = new Rect();

    private Drawable zx_code_kuang;
    
    public Drawable getScanFrameDrawable() {
        return zx_code_kuang;
    }

    private void init(Context context) {
        int finder_mask = context.getResources().getColor(R.color.transparent_half);
        finderMaskPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        finderMaskPaint.setColor(finder_mask);
        zx_code_kuang = context.getResources().getDrawable(R.drawable.scan_frame_bg);
    }

    // ////////////新增该方法//////////////////////
    /**
     * 根据图片size求出矩形框在图片所在位置，tip：相机旋转90度以后，拍摄的图片是横着的，所有传递参数时，做了交换
     * 
     * @param w
     * @param h
     * @return
     */
    public Rect getScanImageRect(int w, int h) {
        // 先求出实际矩形
        Rect rect = new Rect();
        rect.left = middleRect.left;
        rect.right = middleRect.right;
        float temp = h / (float)measureedHeight;
        rect.top = (int)(middleRect.top * temp);
        rect.bottom = (int)(middleRect.bottom * temp);
        return rect;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        measureedWidth = MeasureSpec.getSize(widthMeasureSpec);
        measureedHeight = MeasureSpec.getSize(heightMeasureSpec);

        int kuangWidth = zx_code_kuang.getIntrinsicWidth();
        int kuangHeight = zx_code_kuang.getIntrinsicHeight();

        int marginV = (measureedWidth - kuangWidth) / 2;
        int marginH = (measureedHeight - kuangHeight) / 2;

        middleRect.set(marginV, marginH, kuangWidth + marginV, kuangHeight + marginH);
        leftRect.set(0, middleRect.top, middleRect.left, middleRect.bottom);
        topRect.set(0, 0, measureedWidth, middleRect.top);
        rightRect.set(middleRect.right, middleRect.top, measureedWidth, middleRect.bottom);
        bottomRect.set(0, middleRect.bottom, measureedWidth, measureedHeight);
    }
}


package com.carlt.sesame.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetricsInt;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import com.carlt.sesame.R;

/**
 * 报告-圆形对比View
 * 
 * @author daisy
 */
public class CircleValueBar extends View {
    private float width;// view宽度

    private float height;// view高度

    private float heightTxt;// 底部文字的高度

    private float radius;

    private float padding;

    private float max;// 最大值

    private float mid;// 中间值

    private float min;// 最小值

    private float[] circleXs = new float[3];// 圆心横坐标

    private int[] circleColors = new int[3];// 圆颜色

    private String[] values = new String[] {
            "", "", ""
    };// 圆形上显示文字

    private String[] words = new String[] {
            "", "", ""
    };// 底部文字描述

    private float circleY;// 圆心纵坐标

    private float circleTxtY;// 圆形上文字的纵坐标

    private float bottomTxtY;// 底部文字的纵坐标

    private int circleTxtColor = Color.WHITE;// 圆形上文字的颜色

    private int bottomTxtColor = Color.argb(255, 153, 153, 153);// 底部文字的颜色

    private float circleTxtSize;// 圆形view显示的文字大小

    private float bottomTxtSize;// 底部显示的文字大小

    private final static int paddingDp = 15;// 左右空隙

    private final static int radiusDp = 16;// 圆形半径

    private final static int heightTxtDp = 30;// 底部文字的高度dip

    private Context mContext;

    public CircleValueBar(Context context) {
        super(context);
        mContext = context;
    }

    public CircleValueBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;

        DisplayMetrics dm = new DisplayMetrics();
        dm = getResources().getDisplayMetrics();

        float screenDensity = dm.density;
        heightTxt = heightTxtDp * screenDensity;
        radius = radiusDp * screenDensity;
        padding = paddingDp * screenDensity;

        circleTxtSize = mContext.getResources().getDimension(R.dimen.text_size_small_s3);
        bottomTxtSize = mContext.getResources().getDimension(R.dimen.text_size_small_s1);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        width = getWidth();
        height = getHeight();

        caculateCircleXY();
        caculateCircleTxtXY();
        caculateTxtXY();

        drawLine(canvas);
        for (int i = 0; i < 3; i++) {
            drawCircle(canvas, circleColors[i], circleXs[i], circleY);
            // 圆上文字
            drawTxtEllipsis(canvas, values[i], circleTxtColor, circleTxtSize, circleXs[i],
                    circleTxtY);
            // 底部文字
            drawTxt(canvas, words[i], bottomTxtColor, bottomTxtSize, circleXs[i], bottomTxtY);
        }
    }

    /**
     * 设置数值
     * 
     * @param myvalue 我
     * @param avgvalue 平均
     * @param maxvalue 最大
     */
    public void setValueWithMax(float myvalue, float avgvalue, float maxvalue) {
        Log.e("info", "myvalue==" + myvalue);
        Log.e("info", "avgvalue==" + avgvalue);
        Log.e("info", "maxvalue==" + maxvalue);
        max = maxvalue;
        circleColors[2] = mContext.getResources().getColor(R.color.text_color_green);
        words[2] = "最高";
        values[2] = maxvalue + "";
        if (myvalue >= avgvalue) {
            mid = myvalue;
            min = avgvalue;
            circleColors[1] = mContext.getResources().getColor(R.color.red_light);
            circleColors[0] = mContext.getResources().getColor(R.color.text_color_gray1);
            words[1] = "我";
            words[0] = "平均";
            values[1] = myvalue + "";
            values[0] = avgvalue + "";

        } else {
            mid = avgvalue;
            min = myvalue;
            circleColors[1] = mContext.getResources().getColor(R.color.text_color_gray1);
            circleColors[0] = mContext.getResources().getColor(R.color.red_light);
            words[1] = "平均";
            words[0] = "我";
            values[1] = avgvalue + "";
            values[0] = myvalue + "";
        }

        postInvalidate();
    }

    /**
     * 设置数值
     * 
     * @param myvalue 我
     * @param avgvalue 平均
     * @param maxvalue 最大
     */
    public void setValueWithMax(int myvalue, int avgvalue, int maxvalue) {
        max = maxvalue;
        circleColors[2] = mContext.getResources().getColor(R.color.text_color_green);
        words[2] = "最高";
        values[2] = maxvalue + "";
        if (myvalue >= avgvalue) {
            mid = myvalue;
            min = avgvalue;
            circleColors[1] = mContext.getResources().getColor(R.color.red_light);
            circleColors[0] = mContext.getResources().getColor(R.color.text_color_gray1);
            words[1] = "我";
            words[0] = "平均";
            values[1] = myvalue + "";
            values[0] = avgvalue + "";

        } else {
            mid = avgvalue;
            min = myvalue;
            circleColors[1] = mContext.getResources().getColor(R.color.text_color_gray1);
            circleColors[0] = mContext.getResources().getColor(R.color.red_light);
            words[1] = "平均";
            words[0] = "我";
            values[1] = avgvalue + "";
            values[0] = myvalue + "";
        }

        postInvalidate();
    }

    /**
     * 设置数值
     * 
     * @param myvalue 我
     * @param avgvalue 平均
     * @param minvalue 最低
     */
    public void setValueWithMin(float myvalue, float avgvalue, float minvalue) {
        min = minvalue;
        circleColors[0] = mContext.getResources().getColor(R.color.text_color_green);
        words[0] = "最低";
        values[0] = minvalue + "";
        if (avgvalue >= myvalue) {
            mid = myvalue;
            max = avgvalue;
            circleColors[1] = mContext.getResources().getColor(R.color.red_light);
            circleColors[2] = mContext.getResources().getColor(R.color.text_color_gray1);
            words[1] = "我";
            words[2] = "平均";
            values[1] = myvalue + "";
            values[2] = avgvalue + "";

        } else {
            mid = avgvalue;
            max = myvalue;
            circleColors[1] = mContext.getResources().getColor(R.color.text_color_gray1);
            circleColors[2] = mContext.getResources().getColor(R.color.red_light);
            words[1] = "平均";
            words[2] = "我";
            values[1] = avgvalue + "";
            values[2] = myvalue + "";
        }

        postInvalidate();
    }

    /**
     * 设置数值
     * 
     * @param myvalue 我
     * @param avgvalue 平均
     * @param minvalue 最低
     */
    public void setValueWithMin(int myvalue, int avgvalue, int minvalue) {
        min = minvalue;
        circleColors[0] = mContext.getResources().getColor(R.color.text_color_green);
        words[0] = "最低";
        values[0] = minvalue + "";
        if (avgvalue >= myvalue) {
            mid = myvalue;
            max = avgvalue;
            circleColors[1] = mContext.getResources().getColor(R.color.red_light);
            circleColors[2] = mContext.getResources().getColor(R.color.text_color_gray1);
            words[1] = "我";
            words[2] = "平均";
            values[1] = myvalue + "";
            values[2] = avgvalue + "";

        } else {
            mid = avgvalue;
            max = myvalue;
            circleColors[1] = mContext.getResources().getColor(R.color.text_color_gray1);
            circleColors[2] = mContext.getResources().getColor(R.color.red_light);
            words[1] = "平均";
            words[2] = "我";
            values[1] = avgvalue + "";
            values[2] = myvalue + "";
        }

        postInvalidate();
    }

    /**
     * 设置数值
     * 
     * @param myvalue 我
     * @param typevalue 同型车
     * @param allvalue 所有平均
     */
    public void setValueWithType(float myvalue, float typevalue, float allvalue) {
        if (allvalue >= typevalue && allvalue >= myvalue) {

            max = allvalue;
            circleColors[2] = mContext.getResources().getColor(R.color.text_color_green);
            words[2] = "平均";
            values[2] = allvalue + "";
            if (typevalue >= myvalue) {
                mid = typevalue;
                min = myvalue;
                circleColors[1] = mContext.getResources().getColor(R.color.text_color_gray1);
                circleColors[0] = mContext.getResources().getColor(R.color.red_light);
                words[1] = "同型车";
                words[0] = "我";
                values[1] = typevalue + "";
                values[0] = myvalue + "";
            } else {
                mid = myvalue;
                min = typevalue;
                circleColors[1] = mContext.getResources().getColor(R.color.red_light);
                circleColors[0] = mContext.getResources().getColor(R.color.text_color_gray1);
                words[1] = "我";
                words[0] = "同型车";
                values[1] = myvalue + "";
                values[0] = typevalue + "";
            }
        }

        if (typevalue > allvalue && typevalue >= myvalue) {
            max = typevalue;
            circleColors[2] = mContext.getResources().getColor(R.color.text_color_gray1);
            words[2] = "同型车";
            values[2] = typevalue + "";
            if (allvalue >= myvalue) {
                mid = allvalue;
                min = myvalue;
                circleColors[1] = mContext.getResources().getColor(R.color.text_color_green);
                circleColors[0] = mContext.getResources().getColor(R.color.red_light);
                words[1] = "平均";
                words[0] = "我";
                values[1] = allvalue + "";
                values[0] = myvalue + "";
            } else {
                mid = myvalue;
                min = allvalue;
                circleColors[1] = mContext.getResources().getColor(R.color.red_light);
                circleColors[0] = mContext.getResources().getColor(R.color.text_color_green);
                words[1] = "我";
                words[0] = "平均";
                values[1] = myvalue + "";
                values[0] = allvalue + "";
            }
        }

        if (myvalue > allvalue && myvalue > typevalue) {
            max = myvalue;
            circleColors[2] = mContext.getResources().getColor(R.color.red_light);
            words[2] = "我";
            values[2] = myvalue + "";
            if (allvalue >= typevalue) {
                mid = allvalue;
                min = typevalue;
                circleColors[1] = mContext.getResources().getColor(R.color.text_color_green);
                circleColors[0] = mContext.getResources().getColor(R.color.text_color_gray1);
                words[1] = "平均";
                words[0] = "同型车";
                values[1] = allvalue + "";
                values[0] = typevalue + "";
            } else {
                mid = typevalue;
                min = allvalue;
                circleColors[1] = mContext.getResources().getColor(R.color.text_color_gray1);
                circleColors[0] = mContext.getResources().getColor(R.color.text_color_green);
                words[1] = "同型车";
                words[0] = "平均";
                values[1] = typevalue + "";
                values[0] = allvalue + "";
            }
        }
        postInvalidate();
    }

    /**
     * 计算圆心坐标
     */
    private void caculateCircleXY() {
        float gap_all = max - min;
        float gap_left = mid - min;
        float length = width - padding * 2 - radius * 6;// 圆心的最大范围
        float x_s = padding + radius * 3;// 圆心起始位置
        float x_e = width - padding - radius * 3;// 圆心结束位置
        circleXs[0] = padding + radius;
        circleXs[2] = width - padding - radius;
        if (gap_all == 0) {
            circleXs[1] = width / 2;
        } else {
            if (gap_left == 0) {
                circleXs[1] = x_s;
            } else {
                float perscent = gap_left / gap_all * 100;
                float offset = length * perscent / 100;
                float x = x_s + offset;
                if (x > x_e) {
                    circleXs[1] = x_e;
                } else {
                    circleXs[1] = x;
                }
            }
        }

        circleY = radius;
    }

    /**
     * 计算圆形图案上text文字的坐标
     */
    private void caculateCircleTxtXY() {
        float targetViewHeight = radius + radius;

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(circleTxtSize);
        FontMetricsInt fontMetrics = paint.getFontMetricsInt();
        circleTxtY = (targetViewHeight - fontMetrics.bottom - fontMetrics.top) / 2;
    }

    /**
     * 计算底部日期文字坐标
     */
    private void caculateTxtXY() {
        float targetViewTop = height - heightTxt;// 底部txt的顶部Y值

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(bottomTxtSize);
        FontMetricsInt fontMetrics = paint.getFontMetricsInt();

        bottomTxtY = targetViewTop + (heightTxt - fontMetrics.bottom + fontMetrics.top) / 2
                - fontMetrics.top;
    }

    /**
     * 画线
     * 
     * @param canvas
     */
    private void drawLine(Canvas canvas) {
        int color = Color.rgb(217, 217, 217);
        float left = 0;
        float top = radius - 2;
        float right = width;
        float bottom = radius + 2;

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(color);
        canvas.drawRect(left, top, right, bottom, paint);
    }

    /**
     * 画圆
     * 
     * @param canvas
     * @param info <ParameterInfo>
     */
    private void drawCircle(Canvas canvas, int color, float circleX, float circleY) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(color);
        canvas.drawCircle(circleX, circleY, radius, paint);
    }

    /**
     * 画文字
     * 
     * @param canvas
     * @param info
     */
    private void drawTxt(Canvas canvas, String text, int color, float textSize, float txtX,
            float txtY) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(color);
        paint.setTextSize(textSize);
        paint.setTextAlign(Paint.Align.CENTER);// 水平居中

        canvas.drawText(text, txtX, txtY, paint);
    }

    /**
     * 画文字（限制长度）
     * 
     * @param canvas
     * @param text
     * @param color
     * @param textSize
     * @param txtX
     * @param txtY
     */
    private void drawTxtEllipsis(Canvas canvas, String text, int color, float textSize, float txtX,
            float txtY) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        int i = paint.breakText(text, true, radiusDp+radiusDp, null);
        Log.e("info", "text_before==" + text);
        if (text.length() > i) {
            text = text.substring(0, i);
            text = text + "..";
        }
        Log.e("info", "最大显示文字个数==" + i);
        Log.e("info", "text_after==" + text);
        drawTxt(canvas, text, color, textSize, txtX, txtY);
    }
}


package com.carlt.sesame.ui.activity.career.report;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetricsInt;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

import com.carlt.sesame.data.ReportChartParamInfo;
import com.carlt.sesame.data.career.ReportWeekChatInfo;

import java.util.ArrayList;

/**
 * 周报图表视图
 * 
 * @author daisy
 */
public class ReportChartView extends View {

    private int size;// 数据个数

    private int widthMain;// 整个view的宽度

    private int heightMain;// 整个view的高度

    private float heightTxt;// 底部文字的高度

    private float radius;

    private ArrayList<ReportChartParamInfo> arrayCircle;// 所有圆形图案的属性值

    private ArrayList<ReportChartParamInfo> arrayCircleTxt;// 所有圆形图案显示文字属性值

    private ArrayList<ReportChartParamInfo> arrayLine;// 所有圆形图案的属性值

    private ArrayList<ReportChartParamInfo> arrayTxt;// 所有底部日期txt的属性值

    private float txtSizeCircle;// 圆形view显示的文字大小

    private float txtSizeBottom;// 底部显示的文字大小

    private ArrayList<ReportWeekChatInfo> dataList;// 显示数据

    private int colorTxtCircle;// 圆形view显示的文字颜色

    private int colorTxtBottom;// 底部文字颜色

    private int colorLine;// 连接线颜色

    private int[] intValuesSort;

    private final static int heightTxtDp = 44;// 底部文字的高度dip

    private final static int radiusDp = 13;// 圆形半径

    public ReportChartView(Context context) {
        super(context);
    }

    public ReportChartView(Context context, AttributeSet attrs) {
        super(context, attrs);

        DisplayMetrics dm = new DisplayMetrics();
        dm = getResources().getDisplayMetrics();

        float screenDensity = dm.density;
        heightTxt = heightTxtDp * screenDensity;
        radius = (int)(radiusDp * screenDensity);
    }

    public void setDataList(ArrayList<ReportWeekChatInfo> dataList) {
        this.dataList = dataList;
        init();
    }

    public void setColorTxtCircle(int colorTxtCircle) {
        this.colorTxtCircle = colorTxtCircle;
    }

    public void setColorTxtBottom(int colorTxtBottom) {
        this.colorTxtBottom = colorTxtBottom;
    }

    public void setColorLine(int colorLine) {
        this.colorLine = colorLine;
    }

    public void setTxtSizeCircle(float txtSizeCircle) {
        this.txtSizeCircle = txtSizeCircle;
    }

    public void setTxtSizeBottom(float txtSizeBottom) {
        this.txtSizeBottom = txtSizeBottom;
    }

    private void init() {
        if (dataList != null && dataList.size() > 0) {
            size = dataList.size();
            int[] intValues = new int[size];
            for (int i = 0; i < size; i++) {
                ReportWeekChatInfo mInfo = dataList.get(i);
                intValues[i] = mInfo.getPoint();
            }

            intValuesSort = sort(intValues);
        }
    }

    /**
     * 给圆形区域内数据进行排序(由大到小排序)
     */
    private int[] sort(int[] intValues) {
        int length = intValues.length;
        int temp;
        int[] intValuesSort = intValues;
        for (int i = 0; i < length; i++) {
            for (int j = i; j < length; j++) {
                if (intValuesSort[i] < intValuesSort[j]) {
                    temp = intValuesSort[i];
                    intValuesSort[i] = intValuesSort[j];
                    intValuesSort[j] = temp;
                }
            }
        }
        return intValuesSort;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        widthMain = getWidth();
        heightMain = getHeight();

        if (dataList != null && size > 0) {
            caculateCircleXY();
            caculateCircleTxtXY();
            caculateLineXY();
            caculateTxtXY();
            for (int i = 0; i < size; i++) {
                ReportChartParamInfo mInfoCircle = arrayCircle.get(i);
                drawCircle(canvas, mInfoCircle);
                ReportChartParamInfo mInfoCircleTxt = arrayCircleTxt.get(i);
                drawTxt(canvas, mInfoCircleTxt);

                ReportChartParamInfo mInfoBottomTxt = arrayTxt.get(i);
                drawTxt(canvas, mInfoBottomTxt);
            }

            int sizeLine = size - 1;
            for (int i = 0; i < sizeLine; i++) {
                ReportChartParamInfo mInfo = arrayLine.get(i);
                drawLine(canvas, mInfo);
            }
        }
    }

    /**
     * 计算圆心坐标
     */
    private void caculateCircleXY() {
        arrayCircle = new ArrayList<ReportChartParamInfo>();

        float spaceLongX = widthMain - (radius + radius) * size;// 圆心所在X轴区域
        float circleSpaceX = spaceLongX / size;// X轴间距
        float startX = (spaceLongX - circleSpaceX * (size - 1)) / 2;// 第一个圆形的左侧位置

        float spaceLongY = heightMain - heightTxt - (radius + radius) * size;// 间距总长度
        float circleSpaceY = spaceLongY / size;// Y轴间距

        float circleX;
        float circleY = 0;

        ReportWeekChatInfo weekChatInfo;
        int point;

        for (int i = 0; i < size; i++) {
            circleX = startX + radius + (radius + circleSpaceX + radius) * i;

            weekChatInfo = dataList.get(i);
            point = weekChatInfo.getPoint();
            for (int j = 0; j < intValuesSort.length; j++) {
                if (point == intValuesSort[j]) {
                    circleY = radius + (radius + circleSpaceY + radius) * j;
                    break;
                }
            }

            ReportChartParamInfo mInfo = new ReportChartParamInfo();
            mInfo.setLocationX(circleX);
            mInfo.setLocationY(circleY);
            mInfo.setColor(weekChatInfo.getColor());
            arrayCircle.add(mInfo);
        }

    }

    /**
     * 计算圆形图案上text文字的坐标
     */
    private void caculateCircleTxtXY() {
        arrayCircleTxt = new ArrayList<ReportChartParamInfo>();

        float targetViewTop;
        float targetViewHeight;

        float circleTxtX;
        float circleTxtY;

        ReportWeekChatInfo weekChatInfo;
        ReportChartParamInfo mInfoCircle;

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(txtSizeCircle);
        FontMetricsInt fontMetrics = paint.getFontMetricsInt();

        for (int i = 0; i < size; i++) {
            mInfoCircle = arrayCircle.get(i);
            targetViewTop = mInfoCircle.getLocationY() - radius;
            targetViewHeight = radius + radius;

            circleTxtX = mInfoCircle.getLocationX();
            // 为了垂直居中
            circleTxtY = targetViewTop + (targetViewHeight - fontMetrics.bottom - fontMetrics.top)
                    / 2;
            ReportChartParamInfo mInfo = new ReportChartParamInfo();
            weekChatInfo = dataList.get(i);

            mInfo.setLocationX(circleTxtX);
            mInfo.setLocationY(circleTxtY);
            mInfo.setText(weekChatInfo.getPoint() + "");
            mInfo.setColor(colorTxtCircle);
            mInfo.setTextSize(txtSizeCircle);
            arrayCircleTxt.add(mInfo);
        }
    }

    /**
     * 计算连接线坐标
     */
    private void caculateLineXY() {
        arrayLine = new ArrayList<ReportChartParamInfo>();

        float lineStartX;
        float lineStartY;
        float lineEndX;
        float lineEndY;

        float circleX;
        float circleY;

        ReportChartParamInfo mInfoCircle;

        int sizeLine = size - 1;
        for (int i = 0; i < sizeLine; i++) {
            // 线左边圆圆心
            mInfoCircle = arrayCircle.get(i);
            circleX = mInfoCircle.getLocationX();
            circleY = mInfoCircle.getLocationY();
            lineStartX = circleX + radius;
            lineStartY = circleY;
            // 线右边圆圆心
            mInfoCircle = arrayCircle.get(i + 1);
            circleX = mInfoCircle.getLocationX();
            circleY = mInfoCircle.getLocationY();
            lineEndX = circleX - radius;
            lineEndY = circleY;

            ReportChartParamInfo mInfoLine = new ReportChartParamInfo();
            mInfoLine.setColor(colorLine);
            mInfoLine.setLineStartX(lineStartX);
            mInfoLine.setLineStartY(lineStartY);
            mInfoLine.setLineEndX(lineEndX);
            mInfoLine.setLineEndY(lineEndY);
            arrayLine.add(mInfoLine);
        }
    }

    /**
     * 计算底部日期文字坐标
     */
    private void caculateTxtXY() {
        arrayTxt = new ArrayList<ReportChartParamInfo>();

        float circleX;
        float txtX;
        float txtY;
        float targetViewTop = heightMain - heightTxt;// 底部txt的顶部Y值

        ReportChartParamInfo mInfoCircle;
        ReportWeekChatInfo weekChatInfo;

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(txtSizeBottom);
        FontMetricsInt fontMetrics = paint.getFontMetricsInt();
        for (int i = 0; i < size; i++) {
            mInfoCircle = arrayCircle.get(i);
            circleX = mInfoCircle.getLocationX();
            txtX = circleX;
            txtY = (int)(targetViewTop + (heightTxt - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top);

            ReportChartParamInfo mInfoTxt = new ReportChartParamInfo();
            weekChatInfo = dataList.get(i);

            mInfoTxt.setLocationX(txtX);
            mInfoTxt.setLocationY(txtY);
            mInfoTxt.setText(weekChatInfo.getDate());
            mInfoTxt.setColor(colorTxtBottom);
            mInfoTxt.setTextSize(txtSizeBottom);
            arrayTxt.add(mInfoTxt);
        }
    }

    /**
     * 画圆
     * 
     * @param canvas
     * @param info <ParameterInfo>
     */
    private void drawCircle(Canvas canvas, ReportChartParamInfo info) {
        ReportChartParamInfo mInfo = info;
        int color = mInfo.getColor();
        float circleX = mInfo.getLocationX();
        float circleY = mInfo.getLocationY();

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
    private void drawTxt(Canvas canvas, ReportChartParamInfo info) {
        ReportChartParamInfo mInfo = info;
        int color = mInfo.getColor();
        String text = mInfo.getText();
        float textSize = mInfo.getTextSize();

        float txtX = mInfo.getLocationX();
        float txtY = mInfo.getLocationY();

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(color);
        paint.setTextSize(textSize);
        paint.setTextAlign(Paint.Align.CENTER);// 水平居中

        canvas.drawText(text, txtX, txtY, paint);
    }

    /**
     * 画线
     * 
     * @param canvas
     * @param info
     */
    private void drawLine(Canvas canvas, ReportChartParamInfo info) {
        ReportChartParamInfo mInfo = info;
        int color = mInfo.getColor();
        float lineStartX = mInfo.getLineStartX();
        float lineStartY = mInfo.getLineStartY();
        float lineEndX = mInfo.getLineEndX();
        float lineEndY = mInfo.getLineEndY();

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(color);

        canvas.drawLine(lineStartX, lineStartY, lineEndX, lineEndY, paint);
    }
}

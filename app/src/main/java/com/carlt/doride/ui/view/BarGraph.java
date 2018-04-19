package com.carlt.doride.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.carlt.doride.YemaApplication;
import com.carlt.doride.data.home.MonthStatisticChartInfo;
import com.carlt.doride.data.home.MonthStatisticInfo;

import java.util.ArrayList;


/**
 * 柱状图
 * 
 * @author Daisy
 * 
 */
public class BarGraph extends View {

	private int GAP = 40; // 柱状图间距
	private int SINGLE_WIDTH = 30; // 柱状图的宽度
	private int width;// 总宽度
	private int height;// 总高度
	private MonthStatisticChartInfo mMonthStatisticChartInfo;
	private int txtColor;// 文字颜色
	private int barColor;// 柱状图颜色
	private float textSize;// 文字大小

	public void setTxtColor(int txtColor) {
		this.txtColor = txtColor;
	}

	public void setBarColor(int barColor) {
		this.barColor = barColor;
	}

	public void setTextSize(float textSize) {
		this.textSize = textSize;
	}

	public void setType(int type) {
		this.type = type;
	}

	private int type;// 类别
	public final static int TYPE_FUEL = 0;// 油耗数据
	public final static int TYPE_MILES = 1;// 里程数据
	public final static int TYPE_TIME = 2;// 行驶时间数据

	private int lightNum = -1;// 高亮展示的月份

	public BarGraph(Context context) {
		super(context);
	}

	public BarGraph(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	public BarGraph(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		// TODO Auto-generated method stub
		super.onSizeChanged(w, h, oldw, oldh);
		width = w;
	}

	private void init(Context context, AttributeSet attrs) {
//		TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs,
//				R.styleable.BarGraph,0,0);
//		try {
//
//
//			txtColor = typedArray.getColor(R.styleable.BarGraph_textColor,
//					0XFF999999);
//			barColor = typedArray.getColor(R.styleable.BarGraph_barColor,
//					0XFFB22D14);
//			textSize = typedArray.getDimension(R.styleable.BarGraph_textSize, YemaApplication.dpToPx(12));
//			type = typedArray.getInt(R.styleable.BarGraph_type, 0);
//		}finally {
//			typedArray.recycle();
//		}

		height = YemaApplication.dpToPx(124);
		GAP = YemaApplication.dpToPx(15);
		SINGLE_WIDTH = YemaApplication.dpToPx(10);
	}

	public void setMonthStatisticCharInfo(
			MonthStatisticChartInfo mMonthStatisticChartInfo) {
		this.mMonthStatisticChartInfo = mMonthStatisticChartInfo;
		invalidate();
	}

	public void setLightNum(int lightNum) {
		this.lightNum = lightNum;
		invalidate();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (mMonthStatisticChartInfo != null) {
			ArrayList<MonthStatisticInfo> monthStatisticInfos = mMonthStatisticChartInfo
					.getMonthStatisticInfos();
			int size = monthStatisticInfos.size();
			for (int i = 1; i < 13; i++) {
				float left = 10 + (SINGLE_WIDTH + GAP) * i;
				float top;

				for (int j = 0; j < size; j++) {
					MonthStatisticInfo mInfo = monthStatisticInfos.get(j);
					int month = mInfo.getMonth();
					int value = 0;
					int maxValue = 1;
					switch (type) {
					case TYPE_FUEL:
						value = mInfo.getSumfuel();
						maxValue = mMonthStatisticChartInfo
								.getMaxValue_fuel_show();
						break;
					case TYPE_MILES:
						value = mInfo.getSummiles();
						maxValue = mMonthStatisticChartInfo
								.getMaxValue_miles_show();
						break;
					case TYPE_TIME:
						value = mInfo.getSumtime();
						maxValue = mMonthStatisticChartInfo
								.getMaxValue_time_show();
						break;
					}
					top = height - (height - YemaApplication.dpToPx(25)) * value
							/ maxValue;

					if (month == i) {
						drawRoundRect(canvas, left, top);
						drawTxt(canvas, value + "",
								left + YemaApplication.dpToPx(0), top
										- YemaApplication.dpToPx(8), "#999999");
					}
				}
				if (i % 2 == 0) {
					if (lightNum == i) {
						drawTxt(canvas, i + "月", left,
								height + YemaApplication.dpToPx(14), "#333333");
					} else {
						drawTxt(canvas, i + "月", left,
								height + YemaApplication.dpToPx(14), "#999999");
					}
				}
			}
			drawLine(canvas);
		}
	}

	private void drawTxt(Canvas canvas, String content, float x, float y,
                         String color) {
		RectF rect = drawRectTxt(canvas, x, y);
		Paint mGRAYPaint = new Paint();
		mGRAYPaint.setColor(txtColor);
		mGRAYPaint.setTextAlign(Paint.Align.CENTER);
		mGRAYPaint.setTextSize(textSize);
		mGRAYPaint.setAntiAlias(true);
		Paint.FontMetrics fontMetrics = mGRAYPaint.getFontMetrics();
		float top = fontMetrics.top;// 为基线到字体上边框的距离,即上图中的top
		float bottom = fontMetrics.bottom;// 为基线到字体下边框的距离,即上图中的bottom

		int baseLineY = (int) (rect.centerY() - top / 2 - bottom / 2);// 基线中间点的y轴计算公式
		canvas.drawText(content, rect.centerX(), baseLineY, mGRAYPaint);
	}

	private void drawLine(Canvas canvas) {
		Paint mGRAYPaint = new Paint();
		mGRAYPaint.setColor(Color.parseColor("#4d4d4d"));
		mGRAYPaint.setStrokeWidth(YemaApplication.dpToPx(1));
		canvas.drawLine(0, height, width, height, mGRAYPaint);
	}

	private RectF drawRectTxt(Canvas canvas, float left, float top) {
		Paint mColumnarPaint = new Paint();
		mColumnarPaint.setColor(Color.parseColor("#2e2d33"));
		float right = left + SINGLE_WIDTH;
		float bottom = top + YemaApplication.dpToPx(4);

		RectF rect = new RectF(left, top, right, bottom);
		canvas.drawRect(rect, mColumnarPaint);
		return rect;
	}

	private void drawRoundRect(Canvas canvas, float left, float top) {
		Paint mColumnarPaint = new Paint();
		mColumnarPaint.setColor(barColor);
		float right = left + SINGLE_WIDTH;
		float bottom = height;

		RectF rect = new RectF(left, top, right, bottom);
		canvas.drawRoundRect(rect, YemaApplication.dpToPx(5),YemaApplication.dpToPx(5),mColumnarPaint);
	}
}

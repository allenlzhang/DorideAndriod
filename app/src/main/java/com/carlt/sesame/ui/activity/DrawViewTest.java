package com.carlt.sesame.ui.activity;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;
import android.widget.LinearLayout.LayoutParams;

public class DrawViewTest extends View {

	private Paint pt = new Paint();

	private RectF rect = new RectF();

	private int iWeekDay = -1;

	private final static int with = 300;
	private final static int height = 200;

	public DrawViewTest(Context context) {
		super(context);
		setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, height));
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		// 设置矩形大小
		rect.set(50, 50, 100, 100);
		rect.inset(1, 1);
		// 绘制日历头部
		drawDayHeader(canvas);
	}

	private void drawDayHeader(Canvas canvas) {
		// 画矩形，并设置矩形画笔的颜色
		pt.setColor(Color.GREEN);
		canvas.drawRect(rect, pt);
//		canvas.drawC
		// canvas.drawRect(50, 50, 100, 100, pt);
		// 写入日历头部，设置画笔参数
		pt.setTypeface(null);
		pt.setTextSize(20);
		pt.setAntiAlias(true);
		pt.setFakeBoldText(true);
		pt.setColor(Color.BLUE);

		// draw day name
		final String sDayName = "QQQ";
		final int iPosX = (int) rect.left + ((int) rect.width() >> 1)
				- ((int) pt.measureText(sDayName) >> 1);
		final int iPosY = (int) (this.getHeight()
				- (this.getHeight() - getTextHeight()) / 2 - pt
				.getFontMetrics().bottom);
		canvas.drawText(sDayName, iPosX, iPosY, pt);
	}

	// 得到字体高度
	private int getTextHeight() {
		return (int) (-pt.ascent() + pt.descent());
	}

	// 得到一星期的第几天的文本标记
	public void setData(int iWeekDay) {
		this.iWeekDay = iWeekDay;
	}
}

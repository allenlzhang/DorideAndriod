package com.carlt.chelepie.view.gif;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.View;

import com.carlt.doride.R;


public class GifView extends View implements Runnable {
	GifOpenHelper gHelper;
	private boolean isStop = true;
	int delta;
	String title;

	Bitmap bmp;

	Context context;
	// construct - refer for java
	public GifView(Context context) {
		this(context, null);
	}

	// construct - refer for xml
	public GifView(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.gifView);
		int n = ta.getIndexCount();

		for (int i = 0; i < n; i++) {
			int attr = ta.getIndex(i);

			switch (attr) {
			case R.styleable.gifView_src:
				int id = ta.getResourceId(R.styleable.gifView_src, 0);
				setSrc(id);
				break;

			case R.styleable.gifView_delay:
				int idelta = ta.getInteger(R.styleable.gifView_delay, 1);
				setDelta(idelta);
				break;

			case R.styleable.gifView_stop:
				boolean sp = ta.getBoolean(R.styleable.gifView_stop, false);
				if (!sp) {
					setStop();
				}
				break;
			}

		}

		ta.recycle();
	}

	/**
	 * 
	 */
	public void setStop() {
		isStop = false;
	}

	/**
	 */
	public void setStart(Context context) {
		isStop = true;
		this.context = context;
		Thread updateTimer = new Thread(this);
		updateTimer.start();
	}

	/**
	 * 
	 * @param id
	 */
	public void setSrc(int id) {
		gHelper = new GifOpenHelper();
		gHelper.read(GifView.this.getResources().openRawResource(id));
		bmp = gHelper.getImage();
	}

	public void setDelta(int is) {
		delta = is;
	}

	// to meaure its Width & Height
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
	}

	private int measureWidth(int measureSpec) {
		return gHelper.getWidth();
	}

	private int measureHeight(int measureSpec) {
		return gHelper.getHeigh();
	}

	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		canvas.drawBitmap(bmp, 0, 0, new Paint());
		bmp = gHelper.nextBitmap();

	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (isStop) {
			((Activity)context).runOnUiThread(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					GifView.this.postInvalidate();
				}
			});
			try {
				SystemClock.sleep(gHelper.nextDelay() / delta);
			} catch (Exception e) {
			}
		}
	}

}

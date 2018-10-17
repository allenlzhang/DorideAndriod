package com.carlt.chelepie.view;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;


import com.carlt.doride.DorideApplication;
import com.carlt.doride.R;

import java.math.BigDecimal;

public class CutSeekBar extends View {
	private static final String TAG = "CutSeekBar";
	private static final int CLICK_ON_LOW = 1;
	private static final int CLICK_ON_HIGH = 2;
	private static final int CLICK_IN_LOW_AREA = 3;
	private static final int CLICK_IN_HIGH_AREA = 4;
	private static final int CLICK_OUT_AREA = 5;
	private static final int CLICK_INVAILD = 0;
	/*
	 * private static final int[] PRESSED_STATE_SET = {
	 * android.R.attr.state_focused, android.R.attr.state_pressed,
	 * android.R.attr.state_selected, android.R.attr.state_window_focused, };
	 */
	private static final int[] STATE_NORMAL = {};
	private static final int[] STATE_PRESSED = { android.R.attr.state_pressed, android.R.attr.state_window_focused, };
	private Drawable mScrollBarBgNormal;
	private Drawable mScrollBarProgress;
	private Drawable mThumbLow;
	private Drawable mThumbHigh;
	private int mScollBarWidth = DorideApplication.ScreenHeight;
	private int mTmpHeight;
	private int mScollBarHeight;
	private int mThumbWidth;
	private int mThumbHeight;
	// private int mprogressLow;
	// private int mprogressHigh;
	private int mOffsetLow = 0;
	private int mOffsetHigh = 0;

	private int mOffsetMIN;
	private int mOffsetMAX;
	private int mDistance = 0;
	private float mMin = 0;
	private float mMax = 0;
	private int mDuration = 0;
	private int mFlag = CLICK_INVAILD;
	private int[] textStartWith;
	private int textLenth;
	private int textHeight;
	private String[] textArry;
	private OnSeekBarChangeListener mBarChangeListener;

	public CutSeekBar(Context context) {
		this(context, null);
	}

	public CutSeekBar(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public void setMinMove(int minMove) {
		this.mOffsetMIN = (int) (mDistance * minMove / (mMax - mMin));
	}

	public void setMaxMove(int maxMove) {
		this.mOffsetMAX = (int) (mDistance * maxMove / (mMax - mMin));
	}

	public CutSeekBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CutSeekBar, defStyle, 0);
		//最小值 0
		mMin = a.getFloat(R.styleable.CutSeekBar_minValue, mMin);
		//最大值 300
		mMax = a.getFloat(R.styleable.CutSeekBar_maxValue, mMax);
		//获取屏幕宽度
		mScollBarWidth = DorideApplication.ScreenHeight > DorideApplication.ScreenWith ? DorideApplication.ScreenHeight : DorideApplication.ScreenWith;

		mTmpHeight = mScollBarWidth == DorideApplication.ScreenHeight ? DorideApplication.ScreenWith : DorideApplication.ScreenHeight;
		// 初始化时间刻度文字位置
		//文字共五个地方
		textStartWith = new int[] { 117 * mScollBarWidth / 1920, 482 * mScollBarWidth / 1920, 847 * mScollBarWidth / 1920, 1212 * mScollBarWidth / 1920,
				1577 * mScollBarWidth / 1920 };
		textHeight = 30 * mTmpHeight / 1080;
		textLenth = 226 * mScollBarWidth / 1920;
		textArry = new String[] { "-00:02:00", "-00:01:00", "00:00:00", "00:01:00", "00:02:00" };

//		mScollBarHeight = a.getLayoutDimension(R.styleable.CutSeekBar_height, "layout_height");

		//可移动距离  
		mDistance = mScollBarWidth - mThumbWidth;
		//每次移动最小距离
		mDuration = (int) Math.rint(a.getFloat(R.styleable.CutSeekBar_duration, mDuration) * mDistance / (mMax - mMin));
		mOffsetMIN = 0;
		mOffsetMAX = mDistance;
		if (mMax == 0) {
			throw new RuntimeException(a.getPositionDescription() + ": You must supply a maxValue attribute.");
		}
		if (mMin > mMax) {
			throw new RuntimeException(a.getPositionDescription() + ": The minValue attribute must be smaller than the maxValue attribute.");
		}
		if (mDuration == 0) {
			throw new RuntimeException(a.getPositionDescription() + ": You must supply a duration attribute.");
		}
		if (mScollBarHeight == 0 || mScollBarHeight == -1 || mScollBarHeight == -2) {
			throw new RuntimeException(a.getPositionDescription() + ": You must supply a height attribute.");
		}
		a.recycle();
	}

	public static double formatDouble(double pDouble) {
		BigDecimal bd = new BigDecimal(pDouble);
		BigDecimal bd1 = bd.setScale(0, BigDecimal.ROUND_HALF_UP);
		pDouble = bd1.doubleValue();
		return pDouble;
	}

	public void init(Context context) {
		Resources resources = getResources();
		mScrollBarBgNormal = resources.getDrawable(R.drawable.thumbnail_cutbar_bg);
		mScrollBarProgress = resources.getDrawable(R.drawable.thumbnail_cutbar_progress);
		mThumbLow = resources.getDrawable(R.mipmap.seekbarpressure_thumb);
		mThumbHigh = resources.getDrawable(R.mipmap.seekbarpressure_thumb);
		mThumbLow.setState(STATE_NORMAL);
		mThumbHigh.setState(STATE_NORMAL);
		mThumbWidth = mThumbLow.getIntrinsicWidth();
		mThumbHeight = mThumbLow.getIntrinsicHeight();

		Log.e("info", "mThumbWidth==" + mThumbWidth);
	}

	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		mScollBarWidth = DorideApplication.ScreenHeight > DorideApplication.ScreenWith ? DorideApplication.ScreenHeight : DorideApplication.ScreenWith;
		int width = mScollBarWidth;
		int height = mThumbHeight;
		setMeasuredDimension(width, height);
	}

	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
	}

	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);


		mScrollBarBgNormal.setBounds(0, 0, mScollBarWidth, mThumbHeight);
		mScrollBarBgNormal.draw(canvas);

		for (int i = 0; i < 5; i++) {
			Rect targetRect = new Rect(textStartWith[i], mScollBarHeight - textHeight - 2, textStartWith[i] + textLenth, mScollBarHeight - 2);
			Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
			paint.setColor(getResources().getColor(R.color.transparent));
			canvas.drawRect(targetRect, paint);

			FontMetricsInt fontMetrics = paint.getFontMetricsInt();
			int baseline = targetRect.top + (targetRect.bottom - targetRect.top - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;

			paint.setTextSize(25);
			String testString = textArry[i];
			paint.setColor(getResources().getColor(R.color.text_color_gray1));
			// 下面这行是实现水平居中，drawText对应改为传入targetRect.centerX()
			paint.setTextAlign(Paint.Align.CENTER);
			canvas.drawText(testString, targetRect.centerX(), baseline, paint);
		}

		mScrollBarProgress.setBounds(mOffsetLow + mThumbWidth / 2, 0, mOffsetHigh + mThumbWidth / 2, mThumbHeight);
		mScrollBarProgress.draw(canvas);

		mThumbLow.setBounds(mOffsetLow, 0, mOffsetLow + mThumbWidth, mThumbHeight);
		mThumbLow.draw(canvas);

		mThumbHigh.setBounds(mOffsetHigh, 0, mOffsetHigh + mThumbWidth, mThumbHeight);
		mThumbHigh.draw(canvas);
		if (mBarChangeListener != null) {
			setProgressLow(formatDouble(mOffsetLow * (mMax - mMin) / mDistance + mMin));
			setProgressHigh(formatDouble(mOffsetHigh * (mMax - mMin) / mDistance + mMin));
			mBarChangeListener.onProgressChanged(this, getProgressLow(), getProgressHigh(), mMax, mMin);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent e) {
		if (e.getAction() == MotionEvent.ACTION_DOWN) {
			mFlag = getAreaFlag(e);
			Log.d(TAG, "e.getX: " + e.getX() + "mFlag: " + mFlag);
			if (mFlag == CLICK_ON_LOW) {
				mThumbLow.setState(STATE_PRESSED);
			} else if (mFlag == CLICK_ON_HIGH) {
				mThumbHigh.setState(STATE_PRESSED);
			} else if (mFlag == CLICK_IN_LOW_AREA) {
				// 点击左侧条
				mThumbLow.setState(STATE_PRESSED);

				if (e.getX() <= mOffsetMIN + mThumbWidth / 2) {
					mOffsetLow = mOffsetMIN;
				} else if (e.getX() > mOffsetMAX + mThumbWidth / 2 - mDuration) {
					mOffsetLow = mOffsetMAX - mDuration;
					mOffsetHigh = mOffsetMAX;
				} else {

					mOffsetLow = formatInt(e.getX() - (double) mThumbWidth / 2);
					if (mOffsetHigh - mDuration <= mOffsetLow) {
						mOffsetHigh = (mOffsetLow + mDuration <= mDistance) ? (mOffsetLow + mDuration) : mDistance;
						mOffsetLow = mOffsetHigh - mDuration;
					}
				}
			} else if (mFlag == CLICK_IN_HIGH_AREA) {
				// 点击右侧条
				mThumbHigh.setState(STATE_PRESSED);
				if (e.getX() < mOffsetMIN + mThumbWidth / 2 + mDuration) {
					// 左侧触底
					mOffsetHigh = mOffsetMIN + mDuration;
					mOffsetLow = mOffsetMIN;
				} else if (e.getX() > mOffsetMAX + mThumbWidth / 2) {
					// 右侧触底
					mOffsetHigh = mOffsetMAX;
				} else {
					mOffsetHigh = formatInt(e.getX() - (double) mThumbWidth / 2);
					if (mOffsetHigh - mDuration <= mOffsetLow) {
						mOffsetLow = (mOffsetHigh - mDuration >= 0) ? (mOffsetHigh - mDuration) : 0;
						mOffsetHigh = mOffsetLow + mDuration;
					}
				}
			}
		} else if (e.getAction() == MotionEvent.ACTION_MOVE) {
			if (mFlag == CLICK_ON_LOW) {
				// 滑动左侧条
				if (e.getX() <= mOffsetMIN + mThumbWidth / 2) {
					mOffsetLow = mOffsetMIN;
				} else if (e.getX() > mOffsetMAX + mThumbWidth / 2 - mDuration) {
					mOffsetLow = mOffsetMAX - mDuration;
					mOffsetHigh = mOffsetMAX;
				} else {
					mOffsetLow = formatInt(e.getX() - (double) mThumbWidth / 2);
					if (mOffsetHigh - mOffsetLow <= mDuration) {
						mOffsetHigh = (mOffsetLow + mDuration <= mDistance) ? (mOffsetLow + mDuration) : mDistance;
					}
				}
			} else if (mFlag == CLICK_ON_HIGH) {
				// 滑动右侧条
				if (e.getX() < mOffsetMIN + mThumbWidth / 2 + mDuration) {
					// 左侧触底
					mOffsetHigh = mOffsetMIN + mDuration;
					mOffsetLow = mOffsetMIN;
				} else if (e.getX() > mOffsetMAX + mThumbWidth / 2) {
					// 右侧触底
					mOffsetHigh = mOffsetMAX;
				} else {

					mOffsetHigh = formatInt(e.getX() - (double) mThumbWidth / 2);
					if (mOffsetHigh - mOffsetLow <= mDuration) {
						mOffsetLow = (mOffsetHigh - mDuration >= 0) ? (mOffsetHigh - mDuration) : 0;
					}
				}
			}
		} else if (e.getAction() == MotionEvent.ACTION_UP) {
			mThumbLow.setState(STATE_NORMAL);
			mThumbHigh.setState(STATE_NORMAL);
		}

		return true;
	}

	public int getAreaFlag(MotionEvent e) {
		// int top = mHalfScollBarHeight - mHalfThumbHeight;
		// int bottom = mHalfScollBarHeight + mHalfThumbHeight;
		int top = 50;
		int bottom = mThumbHeight + 50;
		if (e.getY() >= top && e.getY() <= bottom && e.getX() >= mOffsetLow && e.getX() <= mOffsetLow + mThumbWidth) {
			return CLICK_ON_LOW;
		} else if (e.getY() >= top && e.getY() <= bottom && e.getX() >= mOffsetHigh && e.getX() <= mOffsetHigh + mThumbWidth) {
			return CLICK_ON_HIGH;
		} else if (e.getY() >= top && e.getY() <= bottom
				&& ((e.getX() >= 0 && e.getX() < mOffsetLow) || ((e.getX() > mOffsetLow + mThumbWidth) && e.getX() <= ((double) mOffsetHigh + mOffsetLow + mThumbWidth) / 2))) {
			return CLICK_IN_LOW_AREA;
		} else if (e.getY() >= top && e.getY() <= bottom && (((e.getX() > ((double) mOffsetHigh + mOffsetLow + mThumbWidth) / 2) && e.getX() < mOffsetHigh)
				|| (e.getX() > mOffsetHigh + mThumbWidth && e.getX() <= mScollBarWidth))) {
			return CLICK_IN_HIGH_AREA;
		} else if (!(e.getX() >= 0 && e.getX() <= mScollBarWidth && e.getY() >= top && e.getY() <= bottom)) {
			return CLICK_OUT_AREA;
		} else {
			return CLICK_INVAILD;
		}
	}

	public void setMax(double max) {
		this.mMax = (float) max;
	}

	public double getMax() {
		return mMax;
	}

	public void setMin(double min) {
		this.mMin = (float) min;
	}

	public double getMin() {
		return mMin;
	}

	private double progressLow;
	private double progressHigh;

	public void setProgressLow(double progressLow) {
		this.progressLow = progressLow;
		mOffsetLow = formatInt((progressLow - mMin) / (mMax - mMin) * mDistance);
		invalidate();
	}

	public void setProgressHigh(double progressHigh) {
		this.progressHigh = progressHigh;
		mOffsetHigh = formatInt((progressHigh - mMin) / (mMax - mMin) * mDistance);
		invalidate();
	}

	public double getProgressLow() {
		return progressLow;
	}

	public double getProgressHigh() {
		return progressHigh;
	}

	public void setOnSeekBarChangeListener(OnSeekBarChangeListener mListener) {
		this.mBarChangeListener = mListener;
	}

	public interface OnSeekBarChangeListener {
		public void onProgressChanged(CutSeekBar seekBar, double progressLow, double progressHigh, double max, double min);
	}

	private int formatInt(double value) {
		BigDecimal bd = new BigDecimal(value);
		BigDecimal bd1 = bd.setScale(0, BigDecimal.ROUND_HALF_UP);
		return bd1.intValue();
	}

}
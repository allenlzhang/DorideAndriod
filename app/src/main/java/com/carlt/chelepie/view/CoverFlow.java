package com.carlt.chelepie.view;

import android.content.Context;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.Transformation;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.ImageView;

@SuppressWarnings("deprecation")
public class CoverFlow extends Gallery implements AdapterView.OnItemSelectedListener {

	private Camera mCamera = new Camera();
	private int mMaxRotationAngle = 60;// 60;
	private int mMaxZoom = -360;// -120;
	private int mCoveflowCenter;
	private boolean mAlphaMode = true;
	private int lastSelected = 0;
	OnItemSelectedListener mSelectListener;

	public CoverFlow(Context context) {
		super(context);
		this.setStaticTransformationsEnabled(true);
		super.setOnItemSelectedListener(this);
	}

	public CoverFlow(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.setStaticTransformationsEnabled(true);
		super.setOnItemSelectedListener(this);
	}

	public CoverFlow(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.setStaticTransformationsEnabled(true);
		super.setOnItemSelectedListener(this);
	}

	public int getMaxRotationAngle() {
		return mMaxRotationAngle;
	}

	public void setMaxRotationAngle(int maxRotationAngle) {
		mMaxRotationAngle = maxRotationAngle;
	}

	public boolean getAlphaMode() {
		return mAlphaMode;
	}

	public void setAlphaMode(boolean isAlpha) {
		mAlphaMode = isAlpha;
	}

	public int getMaxZoom() {
		return mMaxZoom;
	}

	public void setMaxZoom(int maxZoom) {
		mMaxZoom = maxZoom;
	}
	
	@Override
	public void setOnItemSelectedListener(OnItemSelectedListener listener) {
		this.mSelectListener = listener;
	}

	private int getCenterOfCoverflow() {
		return (getWidth() - getPaddingLeft() - getPaddingRight()) / 2 + getPaddingLeft();
	}

	private static int getCenterOfView(View view) {
		return view.getLeft() + view.getWidth() / 2;
	}

	protected boolean getChildStaticTransformation(View child, Transformation t) {
		try {
			final int childCenter = getCenterOfView(child);
			int rotationAngle = 0;
			t.clear();
			t.setTransformationType(Transformation.TYPE_MATRIX);
			View view = this.getSelectedView();
			if (view.equals(child)) {
				Log.e("info", "进入中间");
				transformImageBitmap((ImageView) child, t, 0);
			} else {
				int x = -1;
				if (mCoveflowCenter > childCenter) {
					x = 1;
				}
				rotationAngle = mMaxRotationAngle * x;
				transformImageBitmap((ImageView) child, t, rotationAngle);
			}
			return true;
		} catch (Exception e) {
			Log.e("CoverFlow", "在滑动的时候报错了。。。");
			return false;
		}
	}

	/**  
	 *   
	 */
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		mCoveflowCenter = getCenterOfCoverflow();
		super.onSizeChanged(w, h, oldw, oldh);
	}

	/**
	 * 把图像位图的角度通过
	 */
	private void transformImageBitmap(View child, Transformation t, int rotationAngle) {
		mCamera.save();
		final Matrix imageMatrix = t.getMatrix();
		final int imageHeight = child.getLayoutParams().height;
		final int imageWidth = child.getLayoutParams().width;
		final int rotation = Math.abs(rotationAngle);
		mCamera.translate(0.0f, 0.0f, 100.0f);

		// 如视图的角度更少,放大
		if (rotation <= mMaxRotationAngle) {
			float zoomAmount = (float) (mMaxZoom + (rotation * 1.5));
			mCamera.translate(0.0f, 0.0f, zoomAmount);
			if (mAlphaMode && rotationAngle != 0) {
				((ImageView) (child)).setAlpha((float) 0.8);
			} else {
				((ImageView) (child)).setAlpha((float) 1.0);
			}
		}
		mCamera.rotateY(rotationAngle);
		mCamera.getMatrix(imageMatrix);
		imageMatrix.preTranslate(-(imageWidth / 2), -(imageHeight / 2));
		imageMatrix.postTranslate((imageWidth / 2), (imageHeight / 2));
		mCamera.restore();
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		lastSelected = position;
		showView();
		if (mSelectListener != null) {
			mSelectListener.onItemSelected(parent, view, position, id);
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		showView();
		if (mSelectListener != null) {
			mSelectListener.onNothingSelected(parent);
		}
	}

	public void showView() {
		final int first = getFirstVisiblePosition();
		final int last = getLastVisiblePosition();
		final int count = getChildCount();
		if (lastSelected == first) {
			for (int i = 1; i < count - 1; i++) {
				View c = getChildAt(i);
				transformImageBitmap(c, new Transformation(), mMaxRotationAngle);
			}
		} else if (lastSelected == last) {
			for (int i = count - 2; i > 0; i--) {
				View c = getChildAt(i);
				transformImageBitmap(c, new Transformation(), mMaxRotationAngle * -1);
			}
		} else {
			int pos = lastSelected - first;
			for (int i = 0; i < count - 1; i++) {
				if (i < pos) {
					View c = getChildAt(i);
					transformImageBitmap(c, new Transformation(), mMaxRotationAngle);
				} else {
					View c = getChildAt(i);
					transformImageBitmap(c, new Transformation(), mMaxRotationAngle * -1);
				}
			}
		}
	}
}
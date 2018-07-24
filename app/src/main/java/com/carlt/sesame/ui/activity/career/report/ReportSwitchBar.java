package com.carlt.sesame.ui.activity.career.report;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.carlt.doride.DorideApplication;
import com.carlt.doride.R;

import java.util.ArrayList;

public class ReportSwitchBar extends LinearLayout {

	private ArrayList<ImageView> mImageViewList = new ArrayList<ImageView>();

	private Context context;

	private int current;

	public ReportSwitchBar(Context context) {
		super(context);
		this.context = context;
	}

	public ReportSwitchBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}

	public void Load(int size) {
		removeAllViews();
		mImageViewList.clear();

		for (int i = 0; i < size; i++) {
			ImageView mImageView = CreatImageView();
			mImageViewList.add(mImageView);
			addView(mImageView);
		}
	}

	private ImageView CreatImageView() {

		// with
		int w = (int) (5 * DorideApplication.ScreenDensity);
		// Margin
		int m = (int) (3 * DorideApplication.ScreenDensity);
		ImageView mImageView = new ImageView(context);
		LayoutParams parm = new LayoutParams(w, w);
		parm.setMargins(0, m, 0, m);
		mImageView.setLayoutParams(parm);
		mImageView.setImageResource(R.drawable.yuan1);
		return mImageView;
	}

	public void change(int p) {
		if (p < mImageViewList.size()) {
			try {
				mImageViewList.get(current).setImageResource(R.drawable.yuan1);
			} catch (Exception e) {
				// TODO: handle exception
			}

			mImageViewList.get(p).setImageResource(R.drawable.yuan2);
			current = p;
		}
	}
}

package com.carlt.yema.ui.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.carlt.yema.R;


public class ReportDayView extends LinearLayout {
	private TextView tv1;
	private TextView tv2;
	private ImageView img;
	private int color1;
	private int color2;
	private RelativeLayout layout;
	private Context mContext;
	public ReportDayView(Context context) {
		super(context);
		mContext=context;
		setFocusable(true);
		setLayoutParams(new LayoutParams(ReportDateConfig.getCell_Width(),
				ReportDateConfig.getCell_Height()));
		LayoutInflater.from(context).inflate(R.layout.report_rili_day, this,
				true);
		tv1 = (TextView) findViewById(R.id.rili_day_text1);
		tv2 = (TextView) findViewById(R.id.rili_day_text2);
		img=(ImageView)findViewById(R.id.rili_day_img);
		color1 = context.getResources().getColor(R.color.white);
		color2 = context.getResources().getColor(R.color.text_color_gray2);
		layout = findViewById(R.id.rili_day_rl);

	}

	public void setResid(boolean isToday) {
		if (isToday){
			layout.setBackgroundResource(R.color.text_color_gray0);
		}else {
			layout.setBackgroundResource(R.color.item_bg);
		}
	}

	public void setClickEnable(boolean flag) {
		if (flag) {
			// 可点击
			tv1.setTextColor(color1);
		} else {
			// 不可点击
			tv1.setTextColor(color2);
		}

	}

	public void setText1(String text) {
		if (tv1 != null) {
			tv1.setText(text);
		}
	}

	public void setText2(String text, int color) {
		if (tv2 != null) {
			tv2.setTextColor(color);
			if (text != null) {
				tv2.setText(text);
			}

		}
	}
	
	public void setCircleVisibility(int visibility){
	    img.setVisibility(visibility);
	}
}

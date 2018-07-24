package com.carlt.sesame.ui.activity.career.order;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.carlt.sesame.R;

public class OrderDayView extends LinearLayout {
	private ImageView iv;
	private TextView tv;

	public OrderDayView(Context context) {
		super(context);
		setFocusable(true);
		setLayoutParams(new LayoutParams(DateConfig.getCell_Width(),
				DateConfig.getCell_Height()));
		LayoutInflater.from(context).inflate(R.layout.order_rili_day, this,
				true);
		tv = (TextView) findViewById(R.id.rili_day_text);
		iv = (ImageView) findViewById(R.id.rili_day_img);
	}

	public void setText(String text) {
		if (tv != null) {
			if (text.length() > 8) {
				tv.setText(text.substring(8));
			} else {
				tv.setText(text);
			}

		}
	}

	public void setStute(int i) {
		if (iv != null) {
			switch (i) {
			case 0:
				iv.setImageResource(0);
				break;
			case 1:
				iv.setImageResource(R.drawable.rili_day_state1);
				break;
			case 2:
				iv.setImageResource(R.drawable.rili_day_state2);
				break;
			case 3:
				iv.setImageResource(R.drawable.rili_day_state3);
				break;
			}

		}
	}
}

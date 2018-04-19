package com.carlt.doride.ui.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.carlt.doride.R;
import com.carlt.doride.YemaApplication;


public class UUDialog extends Dialog {

	protected TextView content;

	protected TextView title;

	protected TextView watch;
	
	protected ImageView img;

	private final static int w_dip = 300;

	public UUDialog(Context context) {
		super(context, R.style.dialog);
		LayoutInflater inflater = LayoutInflater.from(context);
		// View view = inflater.inflate(R.layout.dialog_progress, null);
		View view = inflater.inflate(R.layout.dialog_progress_2, null);
		content = (TextView) view.findViewById(R.id.progress_txt_text);
		img = (ImageView) view.findViewById(R.id.progress_img_animate);
		Animation operatingAnim = AnimationUtils.loadAnimation(context, R.anim.dialog_rotate);
		LinearInterpolator lin = new LinearInterpolator();
		operatingAnim.setInterpolator(lin);  
		img.startAnimation(operatingAnim);
		// title = (TextView) view.findViewById(R.id.dialog_progress_title);
		int w = (int) (YemaApplication.ScreenDensity * 154);
		setCanceledOnTouchOutside(false);
		ViewGroup.LayoutParams parm = new ViewGroup.LayoutParams(w, w);
		setContentView(view, parm);
		setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				return true;
			}
		});
	}

	public void setContentText(String t) {
		if (content != null) {
			content.setText(t);
		}
	}

	public void setTitleText(String t) {
		if (title != null) {
//			title.setText(t);
		}
	}

}

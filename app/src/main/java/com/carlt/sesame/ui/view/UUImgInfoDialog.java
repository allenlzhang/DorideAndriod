
package com.carlt.sesame.ui.view;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

import com.carlt.doride.DorideApplication;
import com.carlt.doride.R;

public class UUImgInfoDialog extends Dialog {

	protected TextView content1;

	protected TextView content2;

	protected TextView watch;

	protected TextView btnLeft;// 左侧按钮

	protected ImageView mImgView;

	private final static int w_dip = 260;

	public UUImgInfoDialog(Context context) {
		super(context, R.style.dialog);
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.dialog_img_info, null);
		content1 = (TextView) view.findViewById(R.id.dialog_img_info_text_content1);
		content2 = (TextView) view.findViewById(R.id.dialog_img_info_text_content2);
		mImgView = (ImageView) view.findViewById(R.id.dialog_img_info_img);
		btnLeft = (TextView) view.findViewById(R.id.dialog_img_info_text_btnleft);

		watch = (TextView) view.findViewById(R.id.dialog_author_text_watch);
		int w = (int) (DorideApplication.ScreenDensity * w_dip);
//		setCancelable(false);
		setCanceledOnTouchOutside(false);
		LayoutParams parm = new LayoutParams(w, LayoutParams.WRAP_CONTENT);
		setContentView(view, parm);
	}

	public void setContentText(String content1, String content2) {
		if (this.content1 != null) {
			this.content1.setText(content1);
		} else {
			this.content1.setVisibility(View.GONE);
		}

		if (this.content2 != null) {
			this.content2.setText(content2);
		} else {
			this.content2.setVisibility(View.GONE);
		}
	}

	public void setClickListener(View.OnClickListener clicklisenter) {
		if (this.btnLeft != null) {
			this.btnLeft.setOnClickListener(clicklisenter);
		}
	}

	public void setImgResId(int id) {
		mImgView.setImageResource(id);
	}

	public void setBtnText(String btnLeft) {
		if (this.btnLeft != null) {
			this.btnLeft.setText(btnLeft);
		}
	}

}

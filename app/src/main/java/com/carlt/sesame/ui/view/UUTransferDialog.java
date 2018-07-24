
package com.carlt.sesame.ui.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

import com.carlt.doride.DorideApplication;
import com.carlt.sesame.R;
import com.carlt.sesame.ui.view.PopBoxCreat.DialogWithTitleClick;

public class UUTransferDialog extends Dialog implements View.OnClickListener {

	protected TextView content1;

	protected TextView watch;

	protected TextView btnLeft;// 左侧按钮

	private final static int COUNT = 30;

	private final static int w_dip = 300;

	private DialogWithTitleClick mDialogWithTitleClick;

	public UUTransferDialog(Context context) {
		super(context, R.style.dialog);
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.dialog_transfer, null);
		content1 = (TextView) view.findViewById(R.id.dialog_author_text_content1);

		btnLeft = (TextView) view.findViewById(R.id.dialog_author_text_btnleft);

		watch = (TextView) view.findViewById(R.id.dialog_author_text_watch);
		int w = (int) (DorideApplication.ScreenDensity * w_dip);
		setCanceledOnTouchOutside(false);
		setCancelable(false);
		LayoutParams parm = new LayoutParams(w, LayoutParams.WRAP_CONTENT);
		setContentView(view, parm);
		setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
				return false;
			}
		});
	}

	@Override
	public void show() {
		super.show();
	}

	@Override
	public void dismiss() {
		super.dismiss();

	}

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				break;
			}
		}
	};

	public void setContentText(String content1) {
		if (this.content1 != null) {
			this.content1.setText(content1);
		}

	}

	public void setBtnText(String btnLeft, String btnRight) {
		if (this.btnLeft != null) {
			this.btnLeft.setText(btnLeft);
		}
	}

	public void setmDialogWithTitleClick(DialogWithTitleClick mDialogWithTitleClick) {
		if (mDialogWithTitleClick != null) {
			this.mDialogWithTitleClick = mDialogWithTitleClick;
			this.btnLeft.setOnClickListener(this);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.dialog_author_text_btnleft:
			mDialogWithTitleClick.onLeftClick();
			break;
		}
	}

}

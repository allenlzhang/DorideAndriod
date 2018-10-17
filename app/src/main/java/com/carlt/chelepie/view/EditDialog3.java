
package com.carlt.chelepie.view;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.TextView;

import com.carlt.chelepie.control.WIFIControl;
import com.carlt.doride.DorideApplication;
import com.carlt.doride.R;
import com.carlt.doride.ui.view.PopBoxCreat;


/**
 * 输入Dialog
 * 
 * @author Administrator
 */
public class EditDialog3 extends Dialog implements OnClickListener {

	protected TextView mTxtTitle;// 标题

	protected EditText mEdt;// 编辑框1
	protected EditText mEdt2;// 编辑框2
	protected EditText mEdt3;// 编辑框3

	protected TextView mTxtBtnL;// 左侧按钮

	protected TextView mTxtBtnR;// 右侧按钮

	private PopBoxCreat.DialogWithEditClick2 mDialogWithEditClick;

	protected Context mContext;

	private final static int w_dip = 300;

	public EditDialog3(Context context) {
		super(context, R.style.dialog);
		mContext = context;

		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.dialog_withedit3, null);
		mTxtTitle = (TextView) view.findViewById(R.id.dialog_withedit2_title);
		mEdt = (EditText) view.findViewById(R.id.dialog_withedit2_edt);
		mEdt2 = (EditText) view.findViewById(R.id.dialog_withedit2_edt2);
		mEdt3 = (EditText) view.findViewById(R.id.dialog_withedit2_edt3);
		mTxtBtnL = (TextView) view.findViewById(R.id.dialog_withedit2_btnl);
		mTxtBtnR = (TextView) view.findViewById(R.id.dialog_withedit2_btnr);

		int w = (int) (DorideApplication.ScreenDensity * w_dip);
		setCanceledOnTouchOutside(false);
		LayoutParams parm = new LayoutParams(w, LayoutParams.WRAP_CONTENT);
		setContentView(view, parm);
	}

	public void setDialogWithTitleClick(PopBoxCreat.DialogWithEditClick2 mDialogWithTitleClick) {
		this.mDialogWithEditClick = mDialogWithTitleClick;
		if (this.mDialogWithEditClick != null) {
			mTxtBtnL.setOnClickListener(this);
			mTxtBtnR.setOnClickListener(this);
		}
	}

	public void setTitleString(String title) {
		if (title != null && title.length() > 0) {
			mTxtTitle.setText(title);
		}
	}

	public void setHintTxt(String hint, String hint2, String hint3) {
		if (hint != null && hint.length() > 0) {
			mEdt.setHint(hint);
			mEdt2.setHint(hint2);
			mEdt3.setHint(hint3);
		}
	}

	public void setEditType(int type) {
		if (type > 0) {
			mEdt.setInputType(type);
			mEdt2.setInputType(type);
			mEdt3.setInputType(type);
		}
	}

	public void setBtnL(String btnL) {
		if (btnL != null && btnL.length() > 0) {
			mTxtBtnL.setText(btnL);
		}
	}

	public void setBtnR(String btnR) {
		if (btnR != null && btnR.length() > 0) {
			mTxtBtnR.setText(btnR);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.dialog_withedit2_btnl:
			mDialogWithEditClick.onLeftClick(mEdt.getText().toString());
			WIFIControl.SSID_PWD = "";
			WIFIControl.SSID_CONNECT = "";
			dismiss();
			break;

		case R.id.dialog_withedit2_btnr:
			WIFIControl.SSID_PWD = mEdt.getText().toString();
			mDialogWithEditClick.onRightClick(mEdt.getText().toString(), mEdt2.getText().toString(), mEdt3.getText().toString());
			dismiss();
			break;
		}

	}

}

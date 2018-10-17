
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
public class EditDialog2 extends Dialog implements OnClickListener {

	protected TextView mTxtTitle;// 标题
	protected TextView mTxtTitle2;// 小标题

	protected EditText mEdt;// 编辑框

	protected TextView mTxtBtnL;// 左侧按钮

	protected TextView mTxtBtnR;// 右侧按钮

	private PopBoxCreat.DialogWithEditClick mDialogWithEditClick;

	protected Context mContext;

	private final static int w_dip = 300;

	public EditDialog2(Context context) {
		super(context, R.style.dialog);
		mContext = context;

		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.dialog_withedit2, null);
		mTxtTitle = (TextView) view.findViewById(R.id.dialog_withedit2_title);
		mTxtTitle2 = (TextView) view.findViewById(R.id.dialog_withedit2_title2);
		mEdt = (EditText) view.findViewById(R.id.dialog_withedit2_edt);
		mTxtBtnL = (TextView) view.findViewById(R.id.dialog_withedit2_btnl);
		mTxtBtnR = (TextView) view.findViewById(R.id.dialog_withedit2_btnr);

		int w = (int) (DorideApplication.ScreenDensity * w_dip);
		setCanceledOnTouchOutside(false);
		LayoutParams parm = new LayoutParams(w, LayoutParams.WRAP_CONTENT);
		setContentView(view, parm);
	}

	public void setDialogWithTitleClick(PopBoxCreat.DialogWithEditClick mDialogWithTitleClick) {
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
	/**
	 *  设置小标题内容
	 * @param title2
	 */
	public void setTitleString2(String title2) {
		if (title2 != null && title2.length() > 0) {
			mTxtTitle2.setVisibility(View.VISIBLE);
			mTxtTitle2.setText(title2);
		}
	}

	public void setHintTxt(String hint) {
		if (hint != null && hint.length() > 0) {
			mEdt.setHint(hint);
		}
	}

	public void setEditType(int type) {
		if (type > 0) {
			mEdt.setInputType(type);
		}
	}
	
	public EditText getEditT() {
		return mEdt;
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
			mDialogWithEditClick.onRightClick(mEdt.getText().toString());
			dismiss();
			break;
		}

	}

}

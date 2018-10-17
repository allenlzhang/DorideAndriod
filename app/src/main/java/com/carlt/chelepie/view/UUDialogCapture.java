
package com.carlt.chelepie.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.carlt.doride.DorideApplication;
import com.carlt.doride.R;


/**
 * 座驾部分相关Dialog
 * 
 * @author Administrator
 */
public class UUDialogCapture extends Dialog implements View.OnClickListener {
	private Context mContext;
	ImageView mImg;
	TextView mTxt;
	Bitmap bit;
	String fileNmae;

	public UUDialogCapture(Context context) {
		super(context);
		mContext = context;
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.dialog_image, null);
		mImg = (ImageView) view.findViewById(R.id.dialog_foot_img_option);
		mTxt = (TextView) view.findViewById(R.id.dialog_txt_filename);
		if (bit != null) {
			mImg.setImageBitmap(bit);
		}

		if (fileNmae != null) {
			mTxt.setText(fileNmae);
		}

		int w = (int) (DorideApplication.ScreenDensity * 300);
		int h = (int) (DorideApplication.ScreenDensity * 160);
		setCanceledOnTouchOutside(true);
		ViewGroup.LayoutParams parm = new ViewGroup.LayoutParams(w, h);
		setContentView(view, parm);
		setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
				return true;
			}
		});
	}

	public void setBitAndName(Bitmap bit, String fileName) {
		if (mImg != null) {
			mImg.setImageBitmap(bit);
		}

		if (mTxt != null) {
			mTxt.setText(fileName);
		}
	}

	@Override
	public void onClick(View v) {
		// 关闭dialog
		dismiss();
	}

}

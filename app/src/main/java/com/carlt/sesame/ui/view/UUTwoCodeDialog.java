
package com.carlt.sesame.ui.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

import com.carlt.doride.DorideApplication;
import com.carlt.doride.R;
import com.carlt.doride.http.retrofitnet.model.UserInfo;
import com.carlt.sesame.data.SesameLoginInfo;
import com.carlt.sesame.http.AsyncImageLoader;

public class UUTwoCodeDialog extends Dialog {

	// 头像
	private ImageView mHead;
	// 性别
	private ImageView mGender;
	// 昵称
	private TextView mNickName;
	// 二维码
	private ImageView mTwoCode;
	
	private ImageView mCancle;

	Bitmap twoCode;
	private AsyncImageLoader mAsyncImageLoader = AsyncImageLoader.getInstance();
	private View.OnClickListener mOnClick;

	private final static int w_dip = 260;

	public UUTwoCodeDialog(Context context,View.OnClickListener mOnClick) {
		super(context, R.style.dialog);
		this.mOnClick = mOnClick;
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.dialog_twocode, null);
		mHead = (ImageView) view.findViewById(R.id.dialog_two_img_head);
		mGender = (ImageView) view.findViewById(R.id.dialog_two_img_gender);
		mTwoCode = (ImageView) view.findViewById(R.id.dialog_two_img_code);
		mNickName = (TextView) view.findViewById(R.id.dialog_two_txt_nick);
		mCancle = (ImageView) view.findViewById(R.id.dialog_QRcode_img_cross);
		int w = (int) (DorideApplication.ScreenDensity * w_dip);
		mCancle.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				UUTwoCodeDialog.this.mOnClick.onClick(v);
				dismiss();
			}
		});
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

		if (twoCode != null) {
			mTwoCode.setImageBitmap(twoCode);
		}

		mNickName.setText(UserInfo.getInstance().userName);
		String imgUrl = UserInfo.getInstance().avatarFile;
		if (imgUrl != null && imgUrl.length() > 0) {
			Bitmap mBitmap = mAsyncImageLoader.getBitmapByUrl(UserInfo.getInstance().avatarFile);
			if (mBitmap != null) {
				mHead.setImageBitmap(mBitmap);
			} else {
				mHead.setImageResource(R.drawable.icon_default_head);
			}
		} else {
			mHead.setImageResource(R.drawable.icon_default_head);
		}

		if (UserInfo.getInstance().gender == 1) {
			// 男
			mGender.setImageResource(R.drawable.icon_sex_male);
		} else if (UserInfo.getInstance().gender == 2) {
			// 女
			mGender.setImageResource(R.drawable.icon_sex_female);
		} else {
			mGender.setImageResource(R.drawable.icon_sex_secret);
		}

	}

	public void setTwoCode(Bitmap mBit) {
		this.twoCode = mBit;
		if(mTwoCode != null){
			mTwoCode.setImageBitmap(twoCode);
		}
	}

}

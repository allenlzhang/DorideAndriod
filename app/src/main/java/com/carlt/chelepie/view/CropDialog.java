
package com.carlt.chelepie.view;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.carlt.doride.DorideApplication;
import com.carlt.doride.R;


/**
 * 剪裁对话框
 * 
 * @author Administrator
 */
public class CropDialog extends Dialog {
//	public class CropDialog extends DialogFragment {

	protected Context mContext;
	ImageView mImg;
	TextView mTv;
	TextView title;
	View.OnClickListener onClick;

	private final static int w_dip = 210;
	private final static int h_dip = 230;

	public CropDialog(Context context) {
		super(context, R.style.dialog);
		mContext = context;
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.dialog_crop, null);
		mImg = (ImageView) view.findViewById(R.id.dialog_connect_img_out);
		mTv = (TextView) view.findViewById(R.id.dialog_connect_txt_cancle);
		title = (TextView) view.findViewById(R.id.dialog_connect_txt_tip);
		mTv.setOnClickListener(onClick);
		int w = (int) (DorideApplication.ScreenDensity * w_dip);
		int h = (int) (DorideApplication.ScreenDensity * h_dip);
		view.setKeepScreenOn(true);
		setCanceledOnTouchOutside(false);
		setCancelable(false);
		Animation operatingAnim = AnimationUtils.loadAnimation(context, R.anim.dialog_rotate);
		LinearInterpolator lin = new LinearInterpolator();
		operatingAnim.setInterpolator(lin);
		mImg.startAnimation(operatingAnim);
		ViewGroup.LayoutParams parm = new ViewGroup.LayoutParams(w, h);
		setContentView(view, parm);
	}

	public void setOnClickListener(View.OnClickListener onClick) {
		this.onClick = onClick;
		if (mTv != null) {
			mTv.setOnClickListener(onClick);
		}
	}
	
	/**
	 * 	显示下载进度
	 * @param process
	 */
	public void setTextProcess(String process){
		StringBuffer sb = new StringBuffer();
		sb.append("视频已下载:").append(process);
		title.setText(sb.toString());
	}

}

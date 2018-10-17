
package com.carlt.chelepie.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.carlt.doride.DorideApplication;
import com.carlt.doride.R;
import com.carlt.doride.ui.view.UUDialog;

/**
 * 连接WIFI 动画Dialog
 * 
 * @author Administrator
 */
public class WIFIConnectDialog extends UUDialog {

	protected Context mContext;
	ImageView mImg;

	private final static int w_dip = 200;
	private final static int h_dip = 230;

	public WIFIConnectDialog(Context context) {
		super(context);
		mContext = context;
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.dialog_connect_wifi, null);
		mImg = (ImageView) view.findViewById(R.id.dialog_connect_img_out);
		int w = (int) (DorideApplication.ScreenDensity * w_dip);
		int h = (int) (DorideApplication.ScreenDensity * h_dip);
		view.setKeepScreenOn(true);
		setCanceledOnTouchOutside(true);
		setCancelable(true);
		Animation operatingAnim = AnimationUtils.loadAnimation(context, R.anim.dialog_rotate);
		LinearInterpolator lin = new LinearInterpolator();
		operatingAnim.setInterpolator(lin);
		mImg.startAnimation(operatingAnim);
		ViewGroup.LayoutParams parm = new ViewGroup.LayoutParams(w, h);
		setContentView(view, parm);
	}

}


package com.carlt.sesame.ui.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

import com.carlt.doride.DorideApplication;
import com.carlt.sesame.R;
import com.carlt.sesame.data.remote.CarStateInfo;

import java.util.ArrayList;

public class UUCarStateDialog extends Dialog {

	private final static int w_dip = 300;

	private ImageView mImgLock;
	private ImageView mImgDoor;
	private ImageView mImgEngine;
	private ImageView mImgAir;

	private TextView mTxtLockTip;
	private TextView mTxtDoorTip;
	private TextView mTxtEngineTip;
	private TextView mTxtAirTip;

	private TextView mTxtOk;
	private ArrayList<CarStateInfo> mDataList;

	public UUCarStateDialog(Context context, ArrayList<CarStateInfo> dataList) {
		super(context, R.style.dialog);
		this.mDataList = dataList;
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.dialog_car_state, null);
		int w = (int) (DorideApplication.ScreenDensity * w_dip);
		this.mImgLock = (ImageView) view.findViewById(R.id.dialog_carstate_img_lock);
		this.mImgDoor = (ImageView) view.findViewById(R.id.dialog_carstate_img_door);
		this.mImgEngine = (ImageView) view.findViewById(R.id.dialog_carstate_img_engine);
		this.mImgAir = (ImageView) view.findViewById(R.id.dialog_carstate_img_air);

		this.mTxtLockTip = (TextView) view.findViewById(R.id.dialog_carstate_txt_lockTip);
		this.mTxtDoorTip = (TextView) view.findViewById(R.id.dialog_carstate_txt_doorTip);
		this.mTxtEngineTip = (TextView) view.findViewById(R.id.dialog_carstate_txt_engineTip);
		this.mTxtAirTip = (TextView) view.findViewById(R.id.dialog_carstate_txt_airTip);
		this.mTxtOk = (TextView) view.findViewById(R.id.dialog_carstate_btn);

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

		this.mTxtOk.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
			}
		});

		int i = 0;
		for (CarStateInfo carStateInfo : mDataList) {
			String s;
			s = carStateInfo.getName();
			if (s != null && s.length() > 0) {
				ImageView imgIcon = null;
				TextView mTip = null;
				if (i == 0) {
					imgIcon = mImgLock;
					mTip = mTxtLockTip;
				} else if (i == 1) {
					imgIcon = mImgDoor;
					mTip = mTxtDoorTip;
				} else if (i == 2) {
					imgIcon = mImgEngine;
					mTip = mTxtEngineTip;
				} else {
					imgIcon = mImgAir;
					mTip = mTxtAirTip;
				}

				imgIcon.setImageResource(carStateInfo.getIconId());
				s = carStateInfo.getStateDes();
				String value = carStateInfo.getValue();
				if (i == 3 && value != null && value.length() > 0) {
					s = s + "/" + carStateInfo.getValue();
				}
				if (s != null && s.length() > 0) {
					mTip.setText(s);
				} else {
					mTip.setText("");
				}
			}
			i++;
		}
	}

}

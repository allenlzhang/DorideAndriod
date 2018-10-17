
package com.carlt.chelepie.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.carlt.doride.DorideApplication;
import com.carlt.doride.R;
import com.carlt.chelepie.view.gif.GifView;


public class UUDialogUpgrading extends Dialog implements OnClickListener {

	protected LayoutInflater inflater;

	TextView txtBtn;
	View line;
	TextView title;
	GifView mGifView;

	private final static int w_dip = 300;

	protected View mainView;
	
	Context context;

	public UUDialogUpgrading(Context context) {
		super(context, R.style.dialog);
		this.context = context;
		inflater = LayoutInflater.from(context);
		mainView = inflater.inflate(R.layout.dialog_upgrading, null);
		mainView.setKeepScreenOn(true);
		txtBtn = (TextView) mainView.findViewById(R.id.dialog_upgrade_txt_btn);
		title = (TextView) mainView.findViewById(R.id.dialog_upgrade_txt_tip);
		line = mainView.findViewById(R.id.line);
		mGifView = (GifView) mainView.findViewById(R.id.dialog_upgrade_gif_bg);
		int w = (int) (DorideApplication.ScreenDensity * w_dip);
		int h = (int) (DorideApplication.ScreenDensity * 160);
		setCanceledOnTouchOutside(false);
		ViewGroup.LayoutParams parm = new ViewGroup.LayoutParams(w, h);
		setContentView(mainView, parm);
		setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
				return true;
			}
		});
	}

	public void setSucc() {
		txtBtn.setText("确定");
		title.setText("更新完成");
		txtBtn.setOnClickListener(this);
		line.setVisibility(View.VISIBLE);
		mGifView.setVisibility(View.GONE);
	}

	public void setFail() {
		txtBtn.setText("确定");
		title.setText("更新失败");
		mGifView.setVisibility(View.GONE);
		txtBtn.setOnClickListener(this);
		line.setVisibility(View.VISIBLE);
	}

	public void setProgressing() {
		txtBtn.setText("请稍等");
		txtBtn.setOnClickListener(null);
		title.setText("正在更新您的设备硬件");
		mGifView.setVisibility(View.VISIBLE);
		mGifView.setStart(context);
		line.setVisibility(View.INVISIBLE);
	}

	public void setProgressNum(int num){
		StringBuilder sb = new StringBuilder();
		sb.append("已完成：").append(num).append("%");
		txtBtn.setText(sb.toString());
	}
	@Override
	public void onClick(View v) {
		dismiss();
	}

}

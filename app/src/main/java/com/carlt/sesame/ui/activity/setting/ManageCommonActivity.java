
package com.carlt.sesame.ui.activity.setting;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.carlt.doride.R;
import com.carlt.sesame.control.CPControl;
import com.carlt.sesame.control.CPControl.GetResultListCallback;
import com.carlt.sesame.data.SesameLoginInfo;
import com.carlt.sesame.ui.activity.base.LoadingActivityWithTitle;
import com.carlt.sesame.ui.view.PopBoxCreat;
import com.carlt.sesame.ui.view.UUDialog;
import com.carlt.sesame.utility.UUToast;

/**
 * 通用管理
 * 
 * @author Administrator
 */
public class ManageCommonActivity extends LoadingActivityWithTitle implements OnClickListener {

	private ImageView back;// 头部返回键

	private TextView title;// 标题文字

	private TextView txtRight;// 头部右侧文字

	private CheckBox mCheckBox1;

	private UUDialog mDialog;// 远程音效控制

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manage_common);
		setTitleView(R.layout.head_back);

		initTitle();
		init();
		LoadData();
	}

	private void initTitle() {
		back = (ImageView) findViewById(R.id.head_back_img1);
		title = (TextView) findViewById(R.id.head_back_txt1);
		txtRight = (TextView) findViewById(R.id.head_back_txt2);

		title.setText("通用管理");
		txtRight.setText("");
		txtRight.setVisibility(View.GONE);
		back.setImageResource(R.drawable.arrow_back);

		back.setOnClickListener(this);
		txtRight.setOnClickListener(this);

	}

	private void init() {
		mCheckBox1 = (CheckBox) findViewById(R.id.activity_manage_common_checkbox1);

		mCheckBox1.setOnClickListener(this);
	}

	@Override
	protected void LoadSuccess(Object data) {
		super.LoadSuccess(data);
		mCheckBox1.setChecked(SesameLoginInfo.isRemoteSoundOpen());
	}

	@Override
	protected void LoadErro(Object erro) {
		super.LoadErro(erro);
	}

	@Override
	protected void LoadData() {
		super.LoadData();
		CPControl.GetRemoteCommControl(listener);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	public void onClick(View v) {

		if (v.equals(mCheckBox1)) {
			if (mDialog != null && mDialog.isShowing()) {
				mDialog.dismiss();
			}
			mDialog = PopBoxCreat.createDialogWithProgress(context, "加载中...");
			mDialog.show();
			String close = mCheckBox1.isChecked() ? "1" : "0";
			CPControl.SetRemoteCommControl(close, musicListener);
		}else if(v.equals(back)){
			finish();
		}

	}

	private Handler mMusicHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				if (mDialog != null && mDialog.isShowing()) {
					mDialog.dismiss();
				}

				if (!(mCheckBox1.isChecked() && SesameLoginInfo.isRemoteSoundOpen())) {
					mCheckBox1.setChecked(SesameLoginInfo.isRemoteSoundOpen());
				}
				break;
			case 1:
				mCheckBox1.setChecked(SesameLoginInfo.isRemoteSoundOpen());
				UUToast.showUUToast(context, "设置失败");
				if (mDialog != null && mDialog.isShowing()) {
					mDialog.dismiss();
				}
				break;
			}
		}
	};

	private GetResultListCallback musicListener = new GetResultListCallback() {

		@Override
		public void onFinished(Object o) {
			Message msg = new Message();
			msg.what = 0;
			msg.obj = o;
			mMusicHandler.sendMessage(msg);
		}

		@Override
		public void onErro(Object o) {
			Message msg = new Message();
			msg.what = 1;
			msg.obj = o;
			mMusicHandler.sendMessage(msg);
		}
	};

}


package com.carlt.chelepie.view.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.carlt.chelepie.control.RecorderControl;
import com.carlt.chelepie.data.recorder.BaseResponseInfo;
import com.carlt.chelepie.data.recorder.PieInfo;
import com.carlt.doride.R;
import com.carlt.doride.base.BaseActivity;
import com.carlt.doride.ui.view.PopBoxCreat;
import com.carlt.doride.ui.view.UUToast;
import com.carlt.sesame.control.CPControl;


public class VideoQualityActivity extends BaseActivity {
	private ImageView back;// 头部返回键

	private TextView title;// 标题文字

	private TextView txtRight;// 头部右侧文字

	private RadioButton mRbFHD;

	private RadioButton mRbHD;

	private RadioGroup mRg;

	private int mCheckedId;
	
	private int mLastCheckId;

	private int index;

	public final static String INDEX = "index";
	
	Dialog mDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_video_quality);

		try {
			index = getIntent().getIntExtra(INDEX, 0);
		} catch (Exception e) {
			// TODO: handle exception
		}

		initTitle();
		init();
	}

	private void initTitle() {
		back = (ImageView) findViewById(R.id.head_back_img1);
		title = (TextView) findViewById(R.id.head_back_txt1);
		txtRight = (TextView) findViewById(R.id.head_back_txt2);

		back.setImageResource(R.drawable.arrow_back);
		title.setText("图像质量");

		txtRight.setVisibility(View.GONE);

		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

	}

	private CPControl.GetResultListCallback mListener = new CPControl.GetResultListCallback() {

		@Override
		public void onFinished(Object o) {
			Message msg = new Message();
			msg.what = 0;
			mHandler.sendMessage(msg);
		}

		@Override
		public void onErro(Object o) {
			Message msg = new Message();
			msg.what = 1;
			msg.obj = o;
			mHandler.sendMessage(msg);
		}
	};

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			dismissDialog();
			switch (msg.what) {
			case 0:
				setResult(ManagePieActivity.RECODE + 3);
				UUToast.showUUToast(VideoQualityActivity.this, "设置成功！");
				break;

			case 1:
				setResult(ManagePieActivity.RECODE + 3);
				BaseResponseInfo mInfo = (BaseResponseInfo) msg.obj;
				mCheckedId = mLastCheckId;
				mRg.check(mCheckedId);
				if (mInfo != null) {
					UUToast.showUUToast(VideoQualityActivity.this, "设置失败：" + mInfo.getInfo());
				} else {
					UUToast.showUUToast(VideoQualityActivity.this, "设置失败...");
				}
				break;
			}
		}

	};

	private void init() {
		mRbFHD = (RadioButton) findViewById(R.id.video_quality_rb_fhd);
		mRbHD = (RadioButton) findViewById(R.id.video_quality_rb_hd);

		mRg = (RadioGroup) findViewById(R.id.video_quality_rg);
		RadioButton rbSelected = (RadioButton) mRg.getChildAt(index);

		mRg.check(rbSelected.getId());
		mRg.setOnCheckedChangeListener(mOnCheckedChangeListener);
	}

	OnCheckedChangeListener mOnCheckedChangeListener = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			Log.e("info", "checkedId==" + checkedId);
			mLastCheckId = mCheckedId;
			mCheckedId = checkedId;
			int quality = 0;
			switch (mCheckedId) {
			case R.id.video_quality_rb_fhd:
				quality = PieInfo.SIZE_1080P;
				break;
			case R.id.video_quality_rb_hd:
				quality = PieInfo.SIZE_720P;
				break;
			}
			showDialog();
			RecorderControl.setVideoSize(quality, mListener);
		}
	};
	
	public void showDialog(){
		dismissDialog();
		mDialog = PopBoxCreat.createDialogWithProgress(VideoQualityActivity.this, "正在操作，请稍等...");
		mDialog.show();
	}

	public void dismissDialog(){
		if(mDialog != null){
			mDialog.dismiss();
		}
	}
	
}

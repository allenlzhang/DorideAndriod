package com.carlt.chelepie.view.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;


import com.carlt.chelepie.control.RecorderControl;
import com.carlt.chelepie.view.TimePickerDialog;
import com.carlt.doride.R;
import com.carlt.doride.data.BaseResponseInfo;
import com.carlt.doride.protocolparser.BaseParser;
import com.carlt.doride.ui.view.PopBoxCreat;
import com.carlt.doride.ui.view.UUDialog;
import com.carlt.doride.ui.view.UUToast;
import com.carlt.doride.utils.Log;
import com.carlt.sesame.control.CPControl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


/**
 * 存储空间管理
 * 
 * @author Administrator
 */
public class ManageTimesActivity extends ActivityWithTitle implements
		TimePickerDialog.TimePickerDialogInterface {
	private ImageView back;// 头部返回键

	private TextView title;// 标题文字

	private ImageView imgRight;// 头部右侧文字

	private TextView timeView;

	private TimePickerDialog mTimePickerDialog;

	private TextView timeOk;

	private long mTimes;
	private SimpleDateFormat sdformat = new SimpleDateFormat(
			"yyyy年MM月dd日 HH:mm", Locale.getDefault());

	private UUDialog dialogWithProgress;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manage_time);
		setTitleView(R.layout.head_back);
		initTitle();
		init();
	}

	private void initTitle() {
		back = (ImageView) findViewById(R.id.head_back_img1);
		title = (TextView) findViewById(R.id.head_back_txt1);
		imgRight = (ImageView) findViewById(R.id.head_back_img2);

		back.setImageResource(R.drawable.arrow_back);
		title.setText("记录仪时间设置");

		imgRight.setVisibility(View.INVISIBLE);

		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

	}

	private void init() {
		timeView = (TextView) findViewById(R.id.time_tv);
		mTimePickerDialog = new TimePickerDialog(this);
		timeOk = (TextView) findViewById(R.id.activity_setting_time_ok);
		mTimes = System.currentTimeMillis();
		Log.e("info", " my mTimes:" + mTimes);
		String sformat = sdformat.format(new Date(mTimes));
		dialogWithProgress = PopBoxCreat.createDialogWithProgress(
				ManageTimesActivity.this, "记录仪校时中");

		timeView.setText(sformat);
		findViewById(R.id.manage_pie_show_time).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
//						mTimePickerDialog.showDateAndTimePickerDialog();
//						 mTimePickerDialog.showTimePickerDialog();
						 mTimePickerDialog.showDateAndTimePickerDialog2();
					}
				});
		timeOk.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				timeOk.setClickable(false);
				dialogWithProgress.show();

				Log.e("info", " setting mTimes:" + mTimes);
				RecorderControl.setRecorderTime(listener_time, mTimes);
			}
		});

	}

	private BaseParser.ResultCallback listener_time = new BaseParser.ResultCallback() {

		@Override
		public void onSuccess(BaseResponseInfo bInfo) {
			Log.e("info", "onFinished");
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					UUToast.showUUToast(ManageTimesActivity.this, "校时成功");
					dialogWithProgress.dismiss();
				}
			});
			finish();
		}

		@Override
		public void onError(BaseResponseInfo bInfo) {
			Log.e("info", "onErro");
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					UUToast.showUUToast(ManageTimesActivity.this, "校时失败");
					dialogWithProgress.dismiss();
				}
			});
			finish();
		}
	};

	@Override
	public void positiveListener() {
		StringBuilder sb = new StringBuilder();
		sb.append(mTimePickerDialog.getYear()).append("年")
				.append(mTimePickerDialog.getMonth()).append("月")
				.append(mTimePickerDialog.getDay()).append("日 ")
				.append(appendTime(mTimePickerDialog.getHour())).append(":")
				.append(appendTime(mTimePickerDialog.getMinute()));
		String stringTime = sb.toString();
		timeView.setText(stringTime);
		try {
			Date date = sdformat.parse(stringTime);
			mTimes = date.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}

	}
	
	private String appendTime(int time1){
		StringBuilder sb = new StringBuilder();
		if(time1 >-1 && time1 < 10){
			return sb.append("0").append(time1).toString(); 
		}else{
			return sb.append(time1).toString();
		}
	}

	@Override
	public void negativeListener() {

	}
}

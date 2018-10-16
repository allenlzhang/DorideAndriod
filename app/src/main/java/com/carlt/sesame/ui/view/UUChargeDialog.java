package com.carlt.sesame.ui.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.carlt.doride.DorideApplication;
import com.carlt.doride.R;
import com.carlt.sesame.control.CPControl;
import com.carlt.sesame.control.CPControl.GetResultListCallback;
import com.carlt.sesame.data.BaseResponseInfo;
import com.carlt.sesame.data.remote.ChargeResultInfo;
import com.carlt.sesame.data.remote.ChargeStatusInfo;
import com.carlt.sesame.ui.view.PickerView.OnScrollChangedListener;
import com.carlt.sesame.utility.Log;
import com.carlt.sesame.utility.MyTimeUtil;
import com.carlt.sesame.utility.UUToast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class UUChargeDialog extends Dialog implements OnClickListener,
		OnCheckedChangeListener {
	private Resources mResources;

	private RadioGroup mRgFuncs;// 充电功能

	RadioButton tab[];
	private int index;
	private int currentIndex;

	private TextView mTxtTimeInfo;// 定时信息
	private TextView mTxtTimeCancel;// 取消定时

	private TextView mTxtConfirm;// 确定
	private TextView mTxtCancel;// 取消

	private PickerView mPickerDay;// 选择天
	private PickerView mPickerHour;// 选择小时
	private PickerView mPickerMinute;// 选择分钟

	private View mViewTime;// 时间选择view
	private View mViewLine2;// App第二条线
	private View mViewTimeInfo;// 定时信息view

	private ArrayList<String> listHours = new ArrayList<String>();
	private ArrayList<String> listMinutes = new ArrayList<String>();

	private final static int TYPE_TODAY = 1;
	private final static int TYPE_TOMORROW = 2;

	private int currentTypeHour;// 当前选中的类别（今天or明天）
	private int currentTypeMin;// 当前选中的类别（今天or明天）

	private final static int w_dip = 310;

	private boolean isChargeTimered;// 是否在定时充电中

	private ChargeStatusInfo mChargeStatusInfo;

	private StringBuffer chargeTime;// 预约的充电时间

	private String chargeTimeResult;// 已经设置成功的预约充电时间

	private String date;// 日期
	private String hour;// 小时
	private String minute;// 分钟

	private Dialog mDialog;

	private Context mContext;
	
	private boolean isSelect;

	public UUChargeDialog(Context context, ChargeStatusInfo chargeStatusInfo) {
		super(context, R.style.dialog);
		mResources = context.getResources();
		mChargeStatusInfo = chargeStatusInfo;
		int chargeStatus = mChargeStatusInfo.getStatus();
		if (chargeStatus == ChargeStatusInfo.STATU_UNCHARGED) {
			isChargeTimered = false;
		} else if (chargeStatus == ChargeStatusInfo.STATU_CHARGING) {
			isChargeTimered = true;
		}
		mContext = context;
		LayoutInflater inflater = LayoutInflater.from(context);
		final View v = inflater.inflate(R.layout.dialog_charge, null);

		mDialog = PopBoxCreat.createDialogWithProgress(context, "正在连接车辆");

		initRadios(v);

		mTxtTimeInfo = (TextView) v.findViewById(R.id.charge_txt_timeinfo);
		mTxtTimeCancel = (TextView) v.findViewById(R.id.charge_txt_time_cancel);
		mTxtConfirm = (TextView) v.findViewById(R.id.charge_txt_confirm);
		mTxtCancel = (TextView) v.findViewById(R.id.charge_txt_cancel);

		mPickerDay = (PickerView) v.findViewById(R.id.charge_pickerview_day);
		mPickerHour = (PickerView) v.findViewById(R.id.charge_pickerview_hour);
		mPickerMinute = (PickerView) v
				.findViewById(R.id.charge_pickerview_minute);

		mViewTime = v.findViewById(R.id.charge_lay_time);
		mViewTimeInfo = v.findViewById(R.id.charge_lay_timeinfo);
		mViewLine2 = v.findViewById(R.id.charge_line2);

		mTxtTimeCancel.setOnClickListener(this);
		mTxtConfirm.setOnClickListener(this);
		mTxtConfirm.setClickable(false);
		mTxtCancel.setOnClickListener(this);

		chargeTimeResult = mChargeStatusInfo.getChargeTimeDes();
		mTxtTimeInfo.setText(chargeTimeResult);

		ArrayList<String> listDay = new ArrayList<String>();
		listDay.add("今天");
		listDay.add("明天");
		mPickerDay.setDataList(listDay);
		currentTypeHour = TYPE_TODAY;
		currentTypeMin = TYPE_TODAY;
		setListHours();
		setListMinutes();

		mPickerDay.setOnScrollChangedListener(mScrollListenerDay);
		mPickerHour.setOnScrollChangedListener(mScrollListenerHour);
		mPickerMinute.setOnScrollChangedListener(mScrollListenerMinute);

		int w = (int) (DorideApplication.ScreenDensity * w_dip);
		setCanceledOnTouchOutside(true);
		LayoutParams parm = new LayoutParams(w,
				LayoutParams.WRAP_CONTENT);
		setContentView(v, parm);
		setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {
				return true;
			}
		});
		if(isChargeTimered){
			setCurrentView(1);
		}
	}

	private void initRadios(View v) {
		mRgFuncs = (RadioGroup) v.findViewById(R.id.charge_rg_funcs);
		tab = new RadioButton[3];
		tab[0] = ((RadioButton) v.findViewById(R.id.charge_rb_rightnow));
		tab[0].setOnCheckedChangeListener(this);
		tab[1] = ((RadioButton) v.findViewById(R.id.charge_rb_timer));
		tab[1].setOnCheckedChangeListener(this);
		tab[2] = ((RadioButton) v.findViewById(R.id.charge_rb_stop));
		tab[2].setOnCheckedChangeListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.charge_txt_time_cancel:
			// 取消定时
			if (mDialog != null && !mDialog.isShowing()) {
				mDialog.show();
			}

			if (System.currentTimeMillis()/1000-mChargeStatusInfo.getChargeTime()>0){
				if (mDialog != null && mDialog.isShowing()) {
					mDialog.dismiss();
				}
				dismiss();
				Log.e("info","当前时间--------------"+System.currentTimeMillis()+"");
				Log.e("info","定时时间--------------"+mChargeStatusInfo.getChargeTime()+"");
				PopBoxCreat.createDialogNotitleOneBtn(getContext(), "车辆已经开始充电", "", "确定", new PopBoxCreat.DialogWithTitleClick() {
					@Override
					public void onLeftClick() {

					}

					@Override
					public void onRightClick() {

					}
				});

			}else {
				CPControl.GetRemoteCancelTimeCharge(listener_cancel);
			}
			break;
		case R.id.charge_txt_cancel:
			// 取消
			dismiss();

			break;
		case R.id.charge_txt_confirm:
			// 确定
			String command = "";
			if (TextUtils.isEmpty(date)) {
				date = MyTimeUtil.getDateFormat3();
			}
			if (TextUtils.isEmpty(hour)) {
				hour = listHours.get(0);
			}
			if (TextUtils.isEmpty(minute)) {
				minute = listMinutes.get(0);
			}
			chargeTime = new StringBuffer();
			chargeTime.append(date);
			chargeTime.append(" ");
			chargeTime.append(hour);
			chargeTime.append(":");
			chargeTime.append(minute);
			switch (currentIndex) {
			case 0:
				// 立即充电
				if (mDialog != null && !mDialog.isShowing()) {
					mDialog.show();
				}
				command = 0 + "";
				chargeTime = new StringBuffer();
				break;

			case 1:
				// 定时充电
				if(isChargeTimered){
					dismiss();
					return;
				}
				if (mDialog != null && !mDialog.isShowing()) {
					mDialog.show();
				}
				command = 2 + "";
				break;
			case 2:
				// 停止充电
				if (mDialog != null && !mDialog.isShowing()) {
					mDialog.show();
				}
				command = 1 + "";
				chargeTime = new StringBuffer();
				break;
			}
			Log.e("info", "chargeTime==" + chargeTime);
			CPControl.GetRemoteCharge(command, chargeTime.toString(),
					listener_charge);
		}
	}

	/**
	 * 充电listener
	 * 
	 */
	GetResultListCallback listener_charge = new GetResultListCallback() {

		@Override
		public void onFinished(Object o) {
			Message msg = new Message();
			msg.what = 0;
			msg.obj = o;
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

	/**
	 * 取消定时充电listener
	 * 
	 */
	GetResultListCallback listener_cancel = new GetResultListCallback() {

		@Override
		public void onFinished(Object o) {
			Message msg = new Message();
			msg.what = 2;
			msg.obj = o;
			mHandler.sendMessage(msg);
		}

		@Override
		public void onErro(Object o) {
			Message msg = new Message();
			msg.what = 3;
			msg.obj = o;
			mHandler.sendMessage(msg);
		}
	};

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (mDialog != null && mDialog.isShowing()) {
				mDialog.dismiss();
			}
			BaseResponseInfo mInfo = (BaseResponseInfo) msg.obj;
			String info_show = "";
			if (mInfo != null) {
				info_show = mInfo.getInfo();
			}
			switch (msg.what) {
			case 0:
				// 设置充电成功
				if (!TextUtils.isEmpty(mInfo.getInfo())) {
					info_show = mInfo.getInfo();
				} else {
					info_show = "操作成功！";
				}
				UUToast.showUUToast(mContext, info_show);
				switch (currentIndex) {
				case 0:
					// 立即充电操作成功
					dismiss();
					break;

				case 1:
					ChargeResultInfo mrChargeResultInfo= (ChargeResultInfo) msg.obj;
					chargeTimeResult = mrChargeResultInfo.getChargeTimeDes();
					isChargeTimered = true;
					setCurrentView(1);
					break;
				case 2:
					// 停止充电操作成功
					dismiss();
					break;
				}

				break;
			case 1:
				// 设置充电失败
				if (!TextUtils.isEmpty(mInfo.getInfo())) {
					info_show = mInfo.getInfo();
				} else {
					info_show = "操作失败...";
				}
				UUToast.showUUToast(mContext, info_show);
				date="";
				hour="";
				minute="";
				if (currentIndex == 1) {
					isChargeTimered = false;
					setCurrentView(1);
				}
				break;
			case 2:
				// 取消定时充电成功
				if (!TextUtils.isEmpty(mInfo.getInfo())) {
					info_show = mInfo.getInfo();
				} else {
					info_show = "操作成功！";
				}
				UUToast.showUUToast(mContext, info_show);
				isChargeTimered = false;
				currentTypeHour = TYPE_TODAY;
				currentTypeMin = TYPE_TODAY;
				setListHours();
				setListMinutes();
				setCurrentView(1);
				mTxtConfirm.setClickable(true);
				mTxtConfirm.setTextColor(Color.parseColor("#333333"));
				break;
			case 3:
				// 取消定时充电失败
				if (!TextUtils.isEmpty(mInfo.getInfo())) {
					info_show = mInfo.getInfo();
				} else {
					info_show = "操作失败...";
				}
				break;
			case 100:
				// 每隔50s刷新时间选择UI
				setCurrentView(1);
				break;
			}
		}
	};

	private void setListHours() {
		int startHour = 0;
		listHours.clear();
		if (currentTypeHour == TYPE_TODAY) {
			startHour = getStartHour();
		} else if (currentTypeHour == TYPE_TOMORROW) {
			startHour = 0;
		}
		for (int i = startHour; i < 24; i++) {
			String hour = "";
			if (i < 10) {
				hour = "0" + i;
			} else {
				hour = i + "";
			}
			listHours.add(hour);
		}
		mPickerHour.setDataList(listHours);
	}

	private void setListMinutes() {
		listMinutes.clear();
		int startMinute = 0;
		if (currentTypeMin == TYPE_TODAY) {
			startMinute = getStartMinute();
		} else if (currentTypeMin == TYPE_TOMORROW) {
			startMinute = 0;
		}
		for (int i = startMinute; i < 60; i++) {
			String minute = "";
			if (i < 10) {
				minute = "0" + i;
			} else {
				minute = i + "";
			}
			listMinutes.add(minute);
		}
		mPickerMinute.setDataList(listMinutes);
	}

	private int getStartHour() {
		int startHour = 0;
		long time = System.currentTimeMillis();
		time = time + 10 * 60 * 1000;
		Calendar cd = Calendar.getInstance();
		cd.setTime(new Date(time));
		startHour = cd.get(Calendar.HOUR_OF_DAY);
		return startHour;
	}

	private int getStartMinute() {
		int startMinute = 0;
		long time = System.currentTimeMillis();
		time = time + 10 * 60 * 1000;
		Calendar cd = Calendar.getInstance();
		cd.setTime(new Date(time));
		startMinute = cd.get(Calendar.MINUTE);
		return startMinute;
	}

	private int curIndexDay;
	OnScrollChangedListener mScrollListenerDay = new OnScrollChangedListener() {

		@Override
		public void onScrollFinished(int curIndex) {
			if (curIndex == 0) {
				currentTypeHour = TYPE_TODAY;
				currentTypeMin = TYPE_TODAY;
				setListHours();
				setListMinutes();
				date = MyTimeUtil.getDateFormat3();
			} else if (curIndex == 1) {
				currentTypeHour = TYPE_TOMORROW;
				currentTypeMin = TYPE_TOMORROW;
				setListHours();
				setListMinutes();
				date = MyTimeUtil.getTomorrowDate();
			}
			curIndexDay=curIndex;
			Log.e("info", "curIndex=" + curIndex);

		}

		@Override
		public void onScrollChanged(int curIndex) {

		}
	};

	OnScrollChangedListener mScrollListenerHour = new OnScrollChangedListener() {

		@Override
		public void onScrollFinished(int curIndex) {
			if (curIndex != 0) {
				currentTypeMin = TYPE_TOMORROW;
				setListMinutes();
			} else {
				if(curIndexDay==0){
					//今天
					currentTypeMin = TYPE_TODAY;
				}else if(curIndexDay==1){
					//明天
					currentTypeMin = TYPE_TOMORROW;
				}
				
				setListMinutes();
			}
			hour = listHours.get(curIndex);
		}

		@Override
		public void onScrollChanged(int curIndex) {

		}
	};

	OnScrollChangedListener mScrollListenerMinute = new OnScrollChangedListener() {

		@Override
		public void onScrollFinished(int curIndex) {
			minute = listMinutes.get(curIndex);

		}

		@Override
		public void onScrollChanged(int curIndex) {

		}
	};

	private void setCurrentView(int index) {
		currentIndex = index;
		for (int i = 0; i < 3; i++) {
			if (i == index) {
				tab[i].setTextColor(mResources.getColor(R.color.blue_light));
			} else {
				tab[i].setTextColor(mResources
						.getColor(R.color.text_color_gray2));
			}
		}

		switch (index) {
		case 0:
			mViewTime.setVisibility(View.GONE);
			mViewTimeInfo.setVisibility(View.GONE);
			mViewLine2.setVisibility(View.GONE);
			break;
		case 1:
			mViewLine2.setVisibility(View.VISIBLE);
			if (!isChargeTimered) {
				tab[0].setEnabled(true);
				tab[0].setCompoundDrawablesWithIntrinsicBounds(null, mResources
						.getDrawable(R.drawable.charge_rightnow_normal), null,
						null);
				tab[2].setEnabled(true);
				tab[2].setCompoundDrawablesWithIntrinsicBounds(null,
						mResources.getDrawable(R.drawable.charge_stop_normal),
						null, null);
				setListHours();
				setListMinutes();
				mViewTime.setVisibility(View.VISIBLE);
				mViewTimeInfo.setVisibility(View.GONE);
			} else {
				tab[0].setEnabled(false);
				tab[0].setCompoundDrawablesWithIntrinsicBounds(null, mResources
						.getDrawable(R.drawable.charge_rightnow_disable), null,
						null);
				tab[2].setEnabled(false);
				tab[2].setCompoundDrawablesWithIntrinsicBounds(null,
						mResources.getDrawable(R.drawable.charge_stop_disable),
						null, null);
				mTxtTimeInfo.setText(chargeTimeResult);
				mViewTime.setVisibility(View.GONE);
				mViewTimeInfo.setVisibility(View.VISIBLE);
			}

			mHandler.sendEmptyMessageDelayed(100, 59 * 1000);
			break;

		case 2:
			mViewLine2.setVisibility(View.GONE);
			mViewTime.setVisibility(View.GONE);
			mViewTimeInfo.setVisibility(View.GONE);
			break;
		}
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (isChecked) {
			isSelect=true;
			initConfirm();
			switch (buttonView.getId()) {
			case R.id.charge_rb_rightnow:
				// 立即充电
				setCurrentView(0);
				tab[0].setCompoundDrawablesWithIntrinsicBounds(null, mResources
						.getDrawable(R.drawable.charge_rightnow_selected),
						null, null);
				tab[1].setCompoundDrawablesWithIntrinsicBounds(null,
						mResources.getDrawable(R.drawable.charge_timer_normal),
						null, null);
				tab[2].setCompoundDrawablesWithIntrinsicBounds(null,
						mResources.getDrawable(R.drawable.charge_stop_normal),
						null, null);
				break;
			case R.id.charge_rb_timer:
				// 定时充电
				setCurrentView(1);
				tab[0].setCompoundDrawablesWithIntrinsicBounds(null, mResources
						.getDrawable(R.drawable.charge_rightnow_normal), null,
						null);
				tab[1].setCompoundDrawablesWithIntrinsicBounds(null, mResources
						.getDrawable(R.drawable.charge_timer_selected), null,
						null);
				tab[2].setCompoundDrawablesWithIntrinsicBounds(null,
						mResources.getDrawable(R.drawable.charge_stop_normal),
						null, null);
				break;
			case R.id.charge_rb_stop:
				// 停止充电
				setCurrentView(2);
				tab[0].setCompoundDrawablesWithIntrinsicBounds(null, mResources
						.getDrawable(R.drawable.charge_rightnow_normal), null,
						null);
				tab[1].setCompoundDrawablesWithIntrinsicBounds(null,
						mResources.getDrawable(R.drawable.charge_timer_normal),
						null, null);
				tab[2].setCompoundDrawablesWithIntrinsicBounds(
						null,
						mResources.getDrawable(R.drawable.charge_stop_selected),
						null, null);
				break;
			}
		}

	}
	
	public void initConfirm() {
		if (isSelect /* && clickCount == 1 */) {
			mTxtConfirm.setClickable(true);
			mTxtConfirm.setTextColor(Color.parseColor("#333333"));
		}
	}

}

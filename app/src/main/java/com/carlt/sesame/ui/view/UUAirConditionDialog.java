package com.carlt.sesame.ui.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.carlt.doride.DorideApplication;
import com.carlt.doride.R;
import com.carlt.sesame.control.CPControl;
import com.carlt.sesame.control.CPControl.GetResultListCallback;
import com.carlt.sesame.data.remote.AirMainInfo;
import com.carlt.sesame.data.remote.RemoteFunInfo;
import com.carlt.sesame.ui.adapter.remote.RemoteAirAdapter;
import com.carlt.sesame.utility.PlayRadio;
import com.carlt.sesame.utility.StringUtils;

import java.util.ArrayList;

public class UUAirConditionDialog extends Dialog implements OnClickListener,
		OnSeekBarChangeListener {

	// protected ImageView up;
	// protected ImageView down;

	protected TextView tempure;

	protected SeekBar mSeekBar;

	protected TextView confirm;

	protected TextView cancle;
	
	protected ImageView mLeftArrow;	//左箭头降低温度的按钮
		
	protected ImageView mRightArrow;//右箭头增加温度的按钮

	protected TextView auto;// 全自动

	protected TextView heat_max;// 最大制热

	protected TextView cold_max;// 最大制冷

	protected TextView defrost;// 一件除霜

	protected TextView anion;// 负离子

	protected TextView clean;// 座舱清洁

	protected TextView closeAir;// 关闭空调

	protected GridView mGrid;
	
	protected LinearLayout mLayFunction;
	
	protected int selectPos;

	private RelativeLayout mLayTempuare;

	private final static int w_dip = 300;

	public GetResultListCallback mListener;

	public View.OnClickListener mViewOutClick;

	public Handler mHandler;

	private AirConditionListener airConditionListener;

	public String state = "-1";

	private static final int COLOR_BG_SELECT = Color.parseColor("#3EC0EA");

	private static final int COLOR_BG_NORMAL = Color.parseColor("#F0F0F0");

	public static final int COLOR_FONT_SELECT = Color.parseColor("#43c1ea");

	public static final int COLOR_FONT_NORMAL = Color.parseColor("#333333");

	private Resources mResources;

	private boolean isSelect;// 用户是否已经对功能做出了选择

	private int clickCount;// 用户点击次数

	private PlayRadio mPlayRadio;

	private AirMainInfo mAirMainInfo;

	private ArrayList<RemoteFunInfo> mRemoteFunInfos;

	private RemoteAirAdapter mAdapter;

	private String currentTemp = "26";

	public void setAirConditionListener(AirConditionListener airConditionListener) {
		this.airConditionListener = airConditionListener;
	}

	public UUAirConditionDialog(Context context, AirMainInfo airMainInfo) {
		super(context, R.style.dialog);
		mResources = context.getResources();
		this.mAirMainInfo = airMainInfo;
		LayoutInflater inflater = LayoutInflater.from(context);
		final View v = inflater.inflate(R.layout.sesame_dialog_air_condition, null);
//		 up = (ImageView)v.findViewById(R.id.dialog_air_img_up);
//		 down = (ImageView)v.findViewById(R.id.dialog_air_img_down);

		mLeftArrow = (ImageView)v.findViewById(R.id.dialog_air_iv_left_arrow);
		mRightArrow = (ImageView)v.findViewById(R.id.dialog_air_iv_right_arrow);
		mSeekBar = (SeekBar) v.findViewById(R.id.dialog_air_seekBar);
		tempure = (TextView) v.findViewById(R.id.dialog_air_txt_tempure);
		confirm = (TextView) v.findViewById(R.id.dialog_air_txt_confirm);
		cancle = (TextView) v.findViewById(R.id.dialog_air_txt_cancle);
		closeAir = (TextView) v.findViewById(R.id.dialog_air_txt_closeAir);

		// auto = (TextView) v.findViewById(R.id.dialog_air_txt_auto);
		heat_max = (TextView) v.findViewById(R.id.dialog_air_txt_heatMax);
		cold_max = (TextView) v.findViewById(R.id.dialog_air_txt_coldMax);
		defrost = (TextView) v.findViewById(R.id.dialog_air_txt_defrost);
		// anion = (TextView) v.findViewById(R.id.dialog_air_txt_anion);
		// clean = (TextView) v.findViewById(R.id.dialog_air_txt_clean);
		mLayTempuare = (RelativeLayout) v
				.findViewById(R.id.dialog_air_lay_tempure);

		mGrid = (GridView) v.findViewById(R.id.dialog_air_grid);
		mLayFunction = (LinearLayout) v.findViewById(R.id.dialog_air_lay);

		mGrid.setOnItemClickListener(mItemClickListener);
		boolean isShowTemp = false;
		if (mAirMainInfo != null) {
			isShowTemp = mAirMainInfo.isShowTemp();
			if (mAirMainInfo.isGetCurrentTempSuccess()) {
				if (StringUtils.isEmpty(mAirMainInfo.getCurrentTemp())) {
					currentTemp = mAirMainInfo.getCurrentTemp();
					tempure.setText(mAirMainInfo.getCurrentTemp());
				}else{
					tempure.setText(currentTemp);
				}
			}else{
				tempure.setText(currentTemp);
			}
			mGrid.setVisibility(View.VISIBLE);
			mLayFunction.setVisibility(View.GONE);
			mRemoteFunInfos = mAirMainInfo.getmRemoteFunInfos();
			state = mAirMainInfo.getState();

			// if(state.equals("2")){
			// state = "8";
			// }
			mAdapter = new RemoteAirAdapter(context);
			mAdapter.setmDataList(mRemoteFunInfos);
			if (mRemoteFunInfos.size() == 1) {
				mGrid.setNumColumns(1);
			}else if(mRemoteFunInfos.size() == 2){
				mGrid.setNumColumns(2);
			}else{
				mGrid.setNumColumns(3);
			}

			mGrid.setAdapter(mAdapter);
		} else {
			isShowTemp = true;
		}

		if (isShowTemp) {
			mLayTempuare.setVisibility(View.VISIBLE);
		} else {
			mLayTempuare.setVisibility(View.GONE);
		}

		mSeekBar.setOnSeekBarChangeListener(this);
		mSeekBar.setEnabled(false);// 默认不可以拖拽
		// auto.setOnClickListener(this);
		heat_max.setOnClickListener(this);
		cold_max.setOnClickListener(this);
		defrost.setOnClickListener(this);
		// anion.setOnClickListener(this);
		// clean.setOnClickListener(this);
		closeAir.setOnClickListener(this);
		mLeftArrow.setOnClickListener(this);
		mRightArrow.setOnClickListener(this);

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

		// up.setOnClickListener(this);
		// down.setOnClickListener(this);
		confirm.setOnClickListener(this);
		cancle.setOnClickListener(this);

		confirm.setClickable(false);
		confirm.setTextColor(Color.parseColor("#666666"));

		mPlayRadio = PlayRadio.getInstance(context);
//		initSelect();
	}

	public void setState(String state) {
		this.state = state;
		// if(state.equals("2")){
		// this.state = "8";
		// }
//		initSelect();
	}

	@Override
	public void onClick(View v) {
		if (v.equals(confirm)) {

			if (mViewOutClick != null) {
				v.setTag(state);
				mViewOutClick.onClick(v);
			}

//			if (mHandler != null && mListener != null) {
//				mHandler.sendEmptyMessage(11);
//				CPControl.GetRemoteAir(mListener, state);
//			}
			if (mHandler != null) {
				mHandler.sendEmptyMessage(11);
				airConditionListener.airCondition(state,currentTemp);
			}
		} else if (v.equals(cancle)) {
			dismiss();
		} else if (v.equals(mLeftArrow)) {
			subtractTemp();
		} else if (v.equals(mRightArrow)) {
			addTemp();
		}
		
		else if (v.equals(auto)) {
			state = "1";
//			initSelect();
			mSeekBar.setEnabled(false);
			mSeekBar.setProgress(caculateProgress(22));
			if (!isSelect) {
				isSelect = true;
			}
			clickCount++;
		} else if (v.equals(heat_max)) {
			state = "5";
//			initSelect();
			tempure.setText(32 + "");
			mSeekBar.setEnabled(false);
			mSeekBar.setProgress(mSeekBar.getMax());
			if (!isSelect) {
				isSelect = true;
			}
			clickCount++;
		} else if (v.equals(cold_max)) {
			state = "4";
//			initSelect();
			tempure.setText(18 + "");
			mSeekBar.setEnabled(false);
			mSeekBar.setProgress(0);
			if (!isSelect) {
				isSelect = true;
			}
			clickCount++;
		} else if (v.equals(defrost)) {
			state = "3";
//			initSelect();
			// up.setClickable(false);
			// up.setImageResource(R.drawable.air_arrow_up_dan);
			// down.setClickable(false);
			// down.setImageResource(R.drawable.air_arrow_down_dan);
			tempure.setText(32 + "");
			mSeekBar.setEnabled(false);
			mSeekBar.setProgress(caculateProgress(32));
			if (!isSelect) {
				isSelect = true;
			}
			clickCount++;
		} else if (v.equals(anion)) {
			state = "6";
//			initSelect();
			// up.setClickable(false);
			// up.setImageResource(R.drawable.air_arrow_up_dan);
			// down.setClickable(false);
			// down.setImageResource(R.drawable.air_arrow_down_dan);
			mSeekBar.setEnabled(false);
			if (!isSelect) {
				isSelect = true;
			}
			clickCount++;
		} else if (v.equals(clean)) {
			state = "7";
//			initSelect();
			// up.setClickable(false);
			// up.setImageResource(R.drawable.air_arrow_up_dan);
			// down.setClickable(false);
			// down.setImageResource(R.drawable.air_arrow_down_dan);
			mSeekBar.setEnabled(false);
			if (!isSelect) {
				isSelect = true;
			}
			clickCount++;
		} else if (v.equals(closeAir)) {
			state = "8";
//			initSelect();
			tempure.setText("--");
			// onClick(confirm);
			mSeekBar.setEnabled(false);
			if (!isSelect) {
				isSelect = true;
			}
			clickCount++;
		}

		initConfirm();
	}

	private void addTemp(){
		if(Integer.parseInt(currentTemp)<18||currentTemp.equals("--")){
			currentTemp = "18";
		}else{
			currentTemp = (Integer.parseInt(currentTemp)+1)+"";
		}
		
		
		tempure.setText(currentTemp);
	}
	
	private void subtractTemp(){
		if (Integer.parseInt(currentTemp)>32) {
			currentTemp = "32";
		}else{
			currentTemp = (Integer.parseInt(currentTemp)-1)+"";
		}
		
		tempure.setText(currentTemp);
	}
	
	
	/**
	 * 
	 */
	public void initConfirm() {
		if (isSelect /* && clickCount == 1 */) {
			confirm.setClickable(true);
			confirm.setTextColor(Color.parseColor("#333333"));
		}
		if (Integer.parseInt(currentTemp)>=32) {
			mRightArrow.setEnabled(false);
		}else{
			mRightArrow.setEnabled(true);
		}
		if (Integer.parseInt(currentTemp)<=18) {
			mLeftArrow.setEnabled(false);
		}else{
			mLeftArrow.setEnabled(true);
		}
		
	}

	public void reCall() {
		if (state.equals("8")) {
			CPControl.GetRemoteAir(mListener, "2");
		} else {
			CPControl.GetRemoteAir(mListener, state);
		}

	}

	// 1:开启全自动;2:关闭;3:一键除霜;4:最大制冷;5:最大制热;6:负离子;7:座舱清洁
//	public void initSelect() {
//		reSet();
//		
//		if (state.equals("1")) {
//			isSelect = true;
//			 auto.setCompoundDrawablesWithIntrinsicBounds(null,
//			 mResources.getDrawable(R.drawable.remote_auto_selected), null,
//			 null);
//			 auto.setTextColor(COLOR_FONT_SELECT);
//			tempure.setText(24 + "");
//		} else if (state.equals("3")) {
//			tempure.setText(32 + "");
//			isSelect = true;
//			mLeftArrow.setVisibility(View.GONE);
//			mRightArrow.setVisibility(View.GONE);
//			defrost.setCompoundDrawablesWithIntrinsicBounds(null,
//					mResources.getDrawable(R.drawable.remote_frost_selected),
//					null, null);
//			defrost.setTextColor(COLOR_FONT_SELECT);
//		} else if (state.equals("4")) {
//			tempure.setText(18 + "");
//			isSelect = true;
//			mLeftArrow.setVisibility(View.GONE);
//			mRightArrow.setVisibility(View.GONE);
//			cold_max.setCompoundDrawablesWithIntrinsicBounds(null,
//					mResources.getDrawable(R.drawable.remote_cold_selected),
//					null, null);
//			cold_max.setTextColor(COLOR_FONT_SELECT);
//		} else if (state.equals("5")) {
//			tempure.setText(32 + "");
//			isSelect = true;
//			mLeftArrow.setVisibility(View.GONE);
//			mRightArrow.setVisibility(View.GONE);
//			heat_max.setCompoundDrawablesWithIntrinsicBounds(null,
//					mResources.getDrawable(R.drawable.remote_hot_selected),
//					null, null);
//			heat_max.setTextColor(COLOR_FONT_SELECT);
//		} else if (state.equals("6")) {
//			tempure.setText(22 + "");
//			isSelect = true;
//			mLeftArrow.setVisibility(View.GONE);
//			mRightArrow.setVisibility(View.GONE);
//			 anion.setCompoundDrawablesWithIntrinsicBounds(null,
//			 mResources.getDrawable(R.drawable.remote_anion_selected), null,
//			 null);
//			 anion.setTextColor(COLOR_FONT_SELECT);
//		} else if (state.equals("7")) {
//			tempure.setText(22 + "");
//			isSelect = true;
//			mLeftArrow.setVisibility(View.GONE);
//			mRightArrow.setVisibility(View.GONE);
//			 clean.setCompoundDrawablesWithIntrinsicBounds(null,
//			 mResources.getDrawable(R.drawable.remote_clean_selected), null,
//			 null);
//			 clean.setTextColor(COLOR_FONT_SELECT);
//		} else if (state.equals("2")) {
//			 isSelect = true;
//			 mLeftArrow.setVisibility(View.GONE);
//				mRightArrow.setVisibility(View.GONE);
////			tempure.setText("--");
//			closeAir.setCompoundDrawablesWithIntrinsicBounds(null,
//					mResources.getDrawable(R.drawable.icon_close_air_press),
//					null, null);
//			closeAir.setTextColor(COLOR_FONT_SELECT);
//		}else if(state.equals("8")){
//			isSelect = true;
//			mLeftArrow.setVisibility(View.VISIBLE);
//			mRightArrow.setVisibility(View.VISIBLE);
//			tempure.setText(currentTemp+"");
//		}
//
//		initConfirm();
//	}

//	public void reSet() {
		// auto.setCompoundDrawablesWithIntrinsicBounds(null,
		// mResources.getDrawable(R.drawable.remote_auto_selected_no), null,
		// null);
		// auto.setTextColor(COLOR_FONT_NORMAL);
		
//		heat_max.setCompoundDrawablesWithIntrinsicBounds(null,
//				mResources.getDrawable(R.drawable.remote_hot_selected_no),
//				null, null);
//		heat_max.setTextColor(COLOR_FONT_NORMAL);
//		cold_max.setCompoundDrawablesWithIntrinsicBounds(null,
//				mResources.getDrawable(R.drawable.remote_cold_selected_no),
//				null, null);
//		cold_max.setTextColor(COLOR_FONT_NORMAL);
//		defrost.setCompoundDrawablesWithIntrinsicBounds(null,
//				mResources.getDrawable(R.drawable.remote_frost_selected_no),
//				null, null);
//		defrost.setTextColor(COLOR_FONT_NORMAL);
//		closeAir.setCompoundDrawablesWithIntrinsicBounds(null,
//				mResources.getDrawable(R.drawable.icon_close_air), null, null);
//		closeAir.setTextColor(COLOR_FONT_NORMAL);
		
		// anion.setCompoundDrawablesWithIntrinsicBounds(null,
		// mResources.getDrawable(R.drawable.remote_anion_selected_no), null,
		// null);
		// anion.setTextColor(COLOR_FONT_NORMAL);
		// clean.setCompoundDrawablesWithIntrinsicBounds(null,
		// mResources.getDrawable(R.drawable.remote_clean_selected_no), null,
		// null);
		// clean.setTextColor(COLOR_FONT_NORMAL);
//	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		if (fromUser) {
			if (state.equals("4") || state.equals("5")) {
				state = "1";
//				initSelect();
				
			}
		}

		int temprature = 18 + progress;
		tempure.setText(temprature + "");

	}

	private int caculateProgress(int temprature) {
		int progress = 0;
		progress = temprature - 18;
		return progress;
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub

	}

	private OnItemClickListener mItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int positon,
				long arg3) {
			selectPos = positon;
			clickCount++;
			if (mRemoteFunInfos != null) {
				int size = mRemoteFunInfos.size();
				for (int i = 0; i < size; i++) {
					RemoteFunInfo mInfo = mRemoteFunInfos.get(i);
					if (i == positon) {
						mInfo.setSelect(true);
						state = mInfo.getId();
						if (state == RemoteFunInfo.MODE_TEMPREGULATION) {
							mLeftArrow.setVisibility(View.VISIBLE);
							mRightArrow.setVisibility(View.VISIBLE);
						}else{
							mLeftArrow.setVisibility(View.GONE);
							mRightArrow.setVisibility(View.GONE);
						}
//						tempure.setText(mInfo.getTemperature());
						if (!isSelect) {
							isSelect = true;
						}
					} else {
						mInfo.setSelect(false);
					}
				}
				mAdapter.notifyDataSetChanged();
				initConfirm();
			}
		}
	};

	public interface AirConditionListener{
		void airCondition(String state,String ratct);
	}
}

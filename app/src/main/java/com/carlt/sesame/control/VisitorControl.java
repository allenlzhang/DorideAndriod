package com.carlt.sesame.control;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;

import com.carlt.doride.DorideApplication;
import com.carlt.sesame.R;
import com.carlt.sesame.data.remote.AirMainInfo;
import com.carlt.sesame.data.remote.CarStateInfo;
import com.carlt.sesame.data.remote.RemoteLogInfo;
import com.carlt.sesame.ui.activity.remote.RemoteMainActivity;
import com.carlt.sesame.ui.view.UUAirConditionDialog;
import com.carlt.sesame.utility.MyTimeUtil;
import com.carlt.sesame.utility.PlayRadio;
import com.carlt.sesame.utility.UUToast;

import java.util.ArrayList;

public class VisitorControl implements OnClickListener {

	//
	private RemoteMainActivity mCtx;
	// 属性文件
	private SharedPreferences mPrefers;
	// 空调对话框
	private UUAirConditionDialog mAirDialog;
	// 模拟延时处理请求
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == 100) {
				mCtx.showWaitingDialog(null);
				Message m = new Message();
				m.what = OPT_AIRCONDATION_OFF;
				opt(m, null);
			}
		};
	};
	//
	private DaoControl mDControl;
	//
	private int lastOpt;
	//
	private PlayRadio mPlayRadio;

	// --全部存String 类型 0关闭 1开启
	public static final String PREF_STATE = "pref_state";
	public static final String PREF_NAME = "pref_visitor";
	public static final String PREF_DOOR = "visitor_door";
	public static final String PREF_LOCK = "visitor_lock";
	public static final String PREF_Engine = "visitor_engine";
	public static final String PREF_AHU = "visitor_ahu";

	// -- 空调当前温度
	public static final String PREF_AHU_MODE = "visitor_ahu_mode";
	public static final String PREF_AHU_TYPE = "visitor_ahu_type";
	public static final int TIME_DELAY = 3000;
	// -- 对应的几种操作
	// 发动机
	public static final int OPT_ENGINE_ON = 0;
	public static final int OPT_ENGINE_OFF = 1;
	// 闪灯鸣笛
	public static final int OPT_LIGHT = 2;
	// 空调
	public static final int OPT_AIRCONDATION_ON = 3;
	public static final int OPT_AIRCONDATION_OFF = 4;
	// 后备箱
	public static final int OPT_TRUNK = 5;
	// 解锁落锁
	public static final int OPT_LOCK_ON = 6;
	public static final int OPT_LOCK_OFF = 7;
	// 开启、关闭天窗
	public static final int OPT_SKYLIGHT_OPEN = 8;
	public static final int OPT_SKYLIGHT_CLOSE = 9;

	public VisitorControl(RemoteMainActivity ctx) {
		this.mCtx = ctx;
		this.mPrefers = mCtx.getSharedPreferences(PREF_NAME,
				Context.MODE_PRIVATE);
		this.mDControl = DaoControl.getInstance();
		this.mPlayRadio = new PlayRadio(mCtx);
	}

	// --操作
	public void opt(Message msg, AirMainInfo airMainInfo) {
		RemoteLogInfo rlf = new RemoteLogInfo();
		rlf.setLogtime(MyTimeUtil.getDateFormat4());
		rlf.setResult("1");
		rlf.setDevice_name(DorideApplication.MODEL);
		long id = 0;

		String value = "";
		switch (msg.what) {
		case OPT_ENGINE_ON:
			lastOpt = OPT_ENGINE_ON;
			value = getValue(PREF_Engine);
			alterValue(PREF_Engine, "1");
			rlf.setLogtype(RemoteLogInfo.TYPE_START);

			id = mDControl.insertRemoteLog(rlf);
			mHandler.postDelayed(mRunnable, TIME_DELAY);
			break;
		case OPT_ENGINE_OFF:
			lastOpt = OPT_ENGINE_OFF;
			value = getValue(PREF_Engine);
			alterValue(PREF_Engine, "0");
			rlf.setLogtype(RemoteLogInfo.TYPE_START);

			id = mDControl.insertRemoteLog(rlf);
			mHandler.postDelayed(mRunnable, TIME_DELAY);
			break;
		case OPT_LIGHT:// 声光寻车 不需要状态
			lastOpt = OPT_LIGHT;
			rlf.setLogtype(RemoteLogInfo.TYPE_FLASHING);
			id = mDControl.insertRemoteLog(rlf);
			mHandler.postDelayed(mRunnable, TIME_DELAY);
			break;
		case OPT_AIRCONDATION_ON:
			lastOpt = OPT_AIRCONDATION_ON;
			value = getValue(PREF_AHU);
			// --显示空调对话框
			showAirDialog(airMainInfo);
			break;
		case OPT_AIRCONDATION_OFF:
			lastOpt = OPT_AIRCONDATION_OFF;
			value = getValue(PREF_AHU);
			alterValue(PREF_AHU, "0");
			rlf.setLogtype(RemoteLogInfo.TYPE_AIRCLOSE);
			id = mDControl.insertRemoteLog(rlf);
			mHandler.postDelayed(mRunnable, TIME_DELAY);
			break;
		case OPT_TRUNK:// 后备箱
			lastOpt = OPT_TRUNK;
			rlf.setLogtype(RemoteLogInfo.TYPE_TRUNKOPEN);
			id = mDControl.insertRemoteLog(rlf);
			mHandler.postDelayed(mRunnable, TIME_DELAY);
			break;
		case OPT_LOCK_ON:
			lastOpt = OPT_LOCK_ON;
			value = getValue(PREF_LOCK);
			rlf.setLogtype(RemoteLogInfo.TYPE_UNLOCK);
			alterValue(PREF_LOCK, "1");
			id = mDControl.insertRemoteLog(rlf);
			mHandler.postDelayed(mRunnable, TIME_DELAY);
			break;
		case OPT_LOCK_OFF:
			lastOpt = OPT_LOCK_OFF;
			value = getValue(PREF_LOCK);
			alterValue(PREF_LOCK, "0");
			rlf.setLogtype(RemoteLogInfo.TYPE_LOCK);
			id = mDControl.insertRemoteLog(rlf);
			mHandler.postDelayed(mRunnable, TIME_DELAY);
			break;
		}

	}

	public void showAirDialog(AirMainInfo airMainInfo) {
		if (mAirDialog != null && mAirDialog.isShowing()) {
			mAirDialog.dismiss();
		}

		mAirDialog = new UUAirConditionDialog(mCtx, airMainInfo);
		mAirDialog.setState(getValue(PREF_STATE));
		mAirDialog.mHandler = mHandler;
		mAirDialog.mViewOutClick = this;
		mAirDialog.show();
	}

	public void disMissAirDialog() {
		if (mAirDialog != null && mAirDialog.isShowing()) {
			mAirDialog.dismiss();
		}
	}

	@Override
	public void onClick(View v) {
		if (v.getTag() != null) {
			RemoteLogInfo rlf = new RemoteLogInfo();
			rlf.setLogtime(MyTimeUtil.getDateFormat4());
			rlf.setResult("1");
			rlf.setDevice_name(DorideApplication.MODEL);
			String state = v.getTag().toString();
			alterValue(PREF_STATE, state);
			if (state.equals("1")) {
				//全自动
				mCtx.showWaitingDialog(null);
				alterValue(PREF_AHU, "1");
				alterValue(PREF_AHU_MODE, "24");
				alterValue(PREF_AHU_TYPE, "全自动");
				rlf.setLogtype(RemoteLogInfo.TYPE_AIRDEFROST);
				mDControl.insertRemoteLog(rlf);
				mHandler.postDelayed(mRunnable, TIME_DELAY);
			} else if (state.equals("3")) {
				// --一键除霜
				mCtx.showWaitingDialog(null);
				alterValue(PREF_AHU, "1");
				alterValue(PREF_AHU_MODE, "32");
				alterValue(PREF_AHU_TYPE, "一键除霜");
				rlf.setLogtype(RemoteLogInfo.TYPE_AIRDEFROST);
				mDControl.insertRemoteLog(rlf);
				mHandler.postDelayed(mRunnable, TIME_DELAY);
			} else if (state.equals("4")) {
				// -- 最大制冷
				mCtx.showWaitingDialog(null);
				alterValue(PREF_AHU, "1");
				alterValue(PREF_AHU_MODE, "18");
				alterValue(PREF_AHU_TYPE, "最大制冷");
				rlf.setLogtype(RemoteLogInfo.TYPE_AIRCOLD);
				mDControl.insertRemoteLog(rlf);
				mHandler.postDelayed(mRunnable, TIME_DELAY);
			} else if (state.equals("5")) {
				// --最大制热
				mCtx.showWaitingDialog(null);
				alterValue(PREF_AHU, "1");
				alterValue(PREF_AHU_MODE, "32");
				alterValue(PREF_AHU_TYPE, "最大制热");
				rlf.setLogtype(RemoteLogInfo.TYPE_AIRHOT);
				mDControl.insertRemoteLog(rlf);
				mHandler.postDelayed(mRunnable, TIME_DELAY);
			} else if (state.equals("6")) {
				//负离子
				mCtx.showWaitingDialog(null);
				alterValue(PREF_AHU, "1");
				alterValue(PREF_AHU_MODE, "22");
				alterValue(PREF_AHU_TYPE, "负离子");
				rlf.setLogtype(RemoteLogInfo.TYPE_AIRHOT);
				mDControl.insertRemoteLog(rlf);
				mHandler.postDelayed(mRunnable, TIME_DELAY);
			}else if (state.equals("7")) {
				//座舱清洁
				mCtx.showWaitingDialog(null);
				alterValue(PREF_AHU, "1");
				alterValue(PREF_AHU_MODE, "22");
				alterValue(PREF_AHU_TYPE, "座舱清洁");
				rlf.setLogtype(RemoteLogInfo.TYPE_AIRHOT);
				mDControl.insertRemoteLog(rlf);
				mHandler.postDelayed(mRunnable, TIME_DELAY);
			} 
			else if (state.equals("8")) {
				mCtx.showWaitingDialog(null);
				lastOpt = OPT_AIRCONDATION_OFF;
				String value = getValue(PREF_AHU);
				alterValue(PREF_AHU, "0");
				rlf.setLogtype(RemoteLogInfo.TYPE_AIRCLOSE);
				long id = mDControl.insertRemoteLog(rlf);
				mHandler.postDelayed(mRunnable, TIME_DELAY);
			}
		}
	}

	public String getValue(String key) {
		return mPrefers.getString(key, "0");
	}

	public void alterValue(String key, String value) {
		mPrefers.edit().putString(key, value).commit();
	}

	/**
	 * 
	 */
	public void updateCarStates() {
		ArrayList<CarStateInfo> mdatas = new ArrayList<CarStateInfo>();
		String lock = getValue(PREF_LOCK);
		CarStateInfo carLock = new CarStateInfo();
		if ("1".equals(lock)) {
			carLock.setIconId(CarStateInfo.iconId_opens[0]);
			carLock.setName(CarStateInfo.names[0]);
			carLock.setStateDes("未锁");
		} else {
			carLock.setIconId(CarStateInfo.iconId_closes[0]);
			carLock.setName(CarStateInfo.names[0]);
			carLock.setStateDes("已锁");
		}
		mdatas.add(carLock);

		CarStateInfo carDoor = new CarStateInfo();
		carDoor.setIconId(CarStateInfo.iconId_closes[1]);
		carDoor.setName(CarStateInfo.names[1]);
		carDoor.setStateDes("关闭");
		mdatas.add(carDoor);

		String engine = getValue(PREF_Engine);
		CarStateInfo carEngine = new CarStateInfo();
		if ("1".equals(engine)) {
			carEngine.setIconId(CarStateInfo.iconId_opens[2]);
			carEngine.setName(CarStateInfo.names[2]);
			carEngine.setStateDes("已启动");
		} else {
			carEngine.setIconId(CarStateInfo.iconId_closes[2]);
			carEngine.setName(CarStateInfo.names[2]);
			carEngine.setStateDes("未启动");
		}
		mdatas.add(carEngine);

		String air = getValue(PREF_AHU);
		CarStateInfo carAir = new CarStateInfo();
		if ("1".equals(air)) {
			carAir.setIconId(CarStateInfo.iconId_opens[3]);
			carAir.setName(CarStateInfo.names[3]);
			carAir.setStateDes("已开启");
			carAir.setStateDes(getValue(PREF_AHU_TYPE));
			// carAir.setValue(getValue(PREF_AHU_MODE));
		} else {
			carAir.setIconId(CarStateInfo.iconId_closes[3]);
			carAir.setName(CarStateInfo.names[3]);
			carAir.setStateDes("已关闭");
		}
		mdatas.add(carAir);
		mCtx.updateCarState(mdatas);
	}

	Runnable mRunnable = new Runnable() {

		@Override
		public void run() {
			mCtx.dissmissWaitingDialog();
			disMissAirDialog();
			UUToast.showUUToast(mCtx, "操作成功");
			updateCarStates();

			switch (lastOpt) {
			case OPT_ENGINE_ON:
				mPlayRadio.playClickVoice(R.raw.remote_start);
				break;
			case OPT_ENGINE_OFF:
				mPlayRadio.playClickVoice(R.raw.remote_stop);
				break;
			case OPT_LIGHT:// 声光寻车 不需要状态
				mPlayRadio.playClickVoice(R.raw.remote_finding);
				break;
			case OPT_AIRCONDATION_ON:
				mPlayRadio.playClickVoice(R.raw.remote_air);
				break;
			case OPT_AIRCONDATION_OFF:
				mPlayRadio.playClickVoice(R.raw.remote_air);
				break;
			case OPT_TRUNK:// 后备箱
				mPlayRadio.playClickVoice(R.raw.remote_trunk);
				break;
			case OPT_LOCK_ON:
				mPlayRadio.playClickVoice(R.raw.remote_unlock);
				break;
			case OPT_LOCK_OFF:
				mPlayRadio.playClickVoice(R.raw.remote_lock);
				break;
			}
		}
	};

}

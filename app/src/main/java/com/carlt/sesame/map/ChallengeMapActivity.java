package com.carlt.sesame.map;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.maps.model.LatLng;
import com.carlt.sesame.R;
import com.carlt.sesame.control.CPControl;
import com.carlt.sesame.control.CPControl.GetResultListCallback;
import com.carlt.sesame.data.BaseResponseInfo;
import com.carlt.sesame.data.LoginInfo;
import com.carlt.sesame.data.career.ChallengeInfo;
import com.carlt.sesame.data.career.ChallengeScore;
import com.carlt.sesame.ui.activity.base.BaseActivity;
import com.carlt.sesame.ui.activity.career.ChallengeFinishedActivity;
import com.carlt.sesame.ui.view.PopBoxCreat;
import com.carlt.sesame.ui.view.PopBoxCreat.DialogWithTitleClick;
import com.carlt.sesame.ui.view.UUDialog;
import com.carlt.sesame.utility.UUToast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;
//import com.baidu.location.BDLocation;
//import com.baidu.location.BDLocationListener;
//import com.baidu.location.LocationClient;
//import com.baidu.location.LocationClientOption;
//import com.baidu.mapapi.map.BaiduMap;
//import com.baidu.mapapi.map.BitmapDescriptor;
//import com.baidu.mapapi.map.BitmapDescriptorFactory;
//import com.baidu.mapapi.map.MapStatusUpdate;
//import com.baidu.mapapi.map.MapStatusUpdateFactory;
//import com.baidu.mapapi.map.MapView;
//import com.baidu.mapapi.map.MyLocationConfiguration;
//import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
//import com.baidu.mapapi.map.MyLocationData;
//import com.baidu.mapapi.map.MyLocationData.Builder;
//import com.baidu.mapapi.map.OverlayOptions;
//import com.baidu.mapapi.map.PolylineOptions;
//import com.baidu.mapapi.model.LatLng;
//import com.baidu.mapapi.utils.DistanceUtil;

public class ChallengeMapActivity extends BaseActivity implements
		/*BDLocationListener, */OnClickListener {
	public final static String CHALLENGE_STOP = "com.hz17car.challenge.stop";

	private ImageView back;// 头部返回键

	private TextView title;// 标题文字

	private TextView txtRight;// 头部右侧文字

	private ImageView mImageViewSecretary;// 车秘书头像

	private TextView mTextViewSecretary;// 车秘书提示文字

	private View start;

	private View start_lay;

	private ChallengeInfo mChallengeInfo;

	private boolean isChallengeStart = false;// 标志位，判断挑战是否开始

	private boolean isFirstStop = true;// 标志位，是否是第一次停止

	// 本次挑战编号
	private String currentID;

	private UUDialog mDialog;

	private ChallengeScore mChallengeScore;

	private ChallengeBroadCastReceiver mReceiver;

	// 定位相关
//	private LocationClient mLocClient;

//	private MapView mMapView;
//
//	private BaiduMap mBaiduMap;

	private TextView challengeTime;

	private TextView challengeMiles;

	private TextView challengeSpeed;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 挑战页面使屏幕常亮
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		setContentView(R.layout.challenge_map_activity);
		try {
			mChallengeInfo = (ChallengeInfo) getIntent().getSerializableExtra(
					"ChallengeInfo");
		} catch (Exception e) {
			// TODO: handle exception
		}

		initTitle();
		initSubTitle();
		initMapView();
		start_lay = findViewById(R.id.challenge_map_start_lay);
		start = findViewById(R.id.challenge_map_start);

		start.setOnClickListener(this);
		start_lay.setOnClickListener(this);

		// 生成广播处理
		mReceiver = new ChallengeBroadCastReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(CHALLENGE_STOP);
		registerReceiver(mReceiver, filter);
	}

	class ChallengeBroadCastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (currentID != null && currentID.length() > 0) {
				mDialog = PopBoxCreat.createDialogWithProgress(
						ChallengeMapActivity.this, "正在获取挑战结果...");
				if (timer != null) {
					timer.cancel();
				}
				mDialog.show();
				CPControl.GetChallengeScoreResult(currentID, false,
						listener_stop);
			}

		}

	}

	// 挑战时点击返回按钮提示框的点击事件
	DialogWithTitleClick clickBack = new DialogWithTitleClick() {

		@Override
		public void onRightClick() {
			// 取消
		}

		@Override
		public void onLeftClick() {
			// 确定
			new Thread() {

				@Override
				public void run() {
					// Log.e("http", "完成完成完成完成完成完成完成完成完成完成完成完成完成完成完成完成完成完成完成");
					CPControl.GetEndChallengeResult(currentID);
				}

			}.start();
			finish();
		}
	};

	// 中断挑战时提示框的点击事件
	DialogWithTitleClick clickInterrupt = new DialogWithTitleClick() {

		@Override
		public void onRightClick() {
			// 取消
		}

		@Override
		public void onLeftClick() {
			// 确定
			if (currentID != null && currentID.length() > 0) {
				mDialog = PopBoxCreat.createDialogWithProgress(
						ChallengeMapActivity.this, "正在获取挑战结果...");

				mDialog.show();
				CPControl.GetChallengeScoreResult(currentID, true,
						listener_stop);
			}
		}
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.head_back_img1:
			// 返回键
			if (isChallengeStart) {
				PopBoxCreat.createDialogWithTitle(ChallengeMapActivity.this,
						"提示", "您确定要中断本次挑战吗?", "", "确定", "取消", clickBack);
			} else {
				finish();
			}
			break;
		case R.id.head_back_txt2:
			// 终止挑战
			PopBoxCreat.createDialogWithTitle(ChallengeMapActivity.this, "提示",
					"您确定要中断本次挑战吗?", "", "确定", "取消", clickInterrupt);
			break;
		case R.id.challenge_map_start:
			mDialog = PopBoxCreat.createDialogWithProgress(
					ChallengeMapActivity.this, "正在连接盒子...");
			mDialog.show();
			CPControl.GetStartChallengeResult(mChallengeInfo.getId(),
					listener_start);
			break;
		}

	}

	private Timer timer;

	private final static int time = 10;

	TimerTask task;

	private int sumTime = 0;

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				// 开始挑战成功
				if (mDialog != null) {
					mDialog.dismiss();
					mDialog = null;
				}
				start_lay.setVisibility(View.GONE);
				isChallengeStart = true;
				txtRight.setText("结束挑战");
				txtRight.setOnClickListener(ChallengeMapActivity.this);
				timer = new Timer();
				task = new TimerTask() {

					@Override
					public void run() {
						sumTime++;
						sendEmptyMessage(4);
						if (sumTime % time == 9) {
							CPControl.GetChallengeScoreResult(currentID, false,
									listener_polling);
						}

					}
				};
				timer.schedule(task, 1000, 1000);
				UUToast.showUUToast(ChallengeMapActivity.this, "挑战开始");
				break;
			case 1:
				// 开始挑战失败
				if (mDialog != null) {
					mDialog.dismiss();
					mDialog = null;
				}
				isChallengeStart = false;
				if (msg.obj != null) {
					UUToast.showUUToast(ChallengeMapActivity.this,
							((BaseResponseInfo) msg.obj).getInfo());
				} else {
					UUToast.showUUToast(ChallengeMapActivity.this, "加载失败");
				}

				break;
			case 2:
				// 终止挑战成功
				if (isFirstStop) {
					isChallengeStart = false;
					Intent mIntent = new Intent(ChallengeMapActivity.this,
							ChallengeFinishedActivity.class);
					mIntent.putExtra("ChallengeScore", mChallengeScore);
					mIntent.putExtra("ChallengeInfo", mChallengeInfo);
					startActivity(mIntent);
					finish();
				}
				isFirstStop = false;
				break;
			case 3:
				// 终止挑战失败
				if (mDialog != null) {
					mDialog.dismiss();
					mDialog = null;
				}
				isChallengeStart = true;

				if (msg.obj != null) {
					UUToast.showUUToast(ChallengeMapActivity.this,
							((BaseResponseInfo) msg.obj).getInfo());
				} else {
					UUToast.showUUToast(ChallengeMapActivity.this, "加载失败");
				}
				break;

			case 4:
				// 刷新时间

				SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss",
						Locale.getDefault());
				sdf.setTimeZone(TimeZone.getTimeZone("GMT+0"));
				Date date = new Date(sumTime * 1000);
				challengeTime.setText(sdf.format(date));
				break;
			}
		}
	};

	private void initTitle() {

		back = (ImageView) findViewById(R.id.head_back_img1);
		title = (TextView) findViewById(R.id.head_back_txt1);
		txtRight = (TextView) findViewById(R.id.head_back_txt2);

		back.setImageResource(R.drawable.arrow_back);
		title.setText("开始挑战");
		txtRight.setVisibility(View.VISIBLE);
		txtRight.setText("");

		back.setOnClickListener(this);

	}

	private void initSubTitle() {
		mImageViewSecretary = (ImageView) findViewById(R.id.layout_sub_head_img);
		mTextViewSecretary = (TextView) findViewById(R.id.layout_sub_head_txt);

		mImageViewSecretary.setImageResource(LoginInfo.getSecretaryImg());
		if (mChallengeInfo != null) {
			// 挑战规则
			String rule = mChallengeInfo.getInfo();
			if (rule != null && rule.length() > 0) {
				mTextViewSecretary.setText(rule);
			}
		}
		mImageViewSecretary.setImageResource(LoginInfo.getSecretaryImg());

	}

	private void initMapView() {
		// 地图初始化
//		mMapView = (MapView) findViewById(R.id.challenge_map_mapview);
//		challengeTime = (TextView) findViewById(R.id.challenge_map_txt_time);
//		challengeMiles = (TextView) findViewById(R.id.challenge_map_txt_mileage);
//		challengeSpeed = (TextView) findViewById(R.id.challenge_map_txt_speed);
//		mBaiduMap = mMapView.getMap();
//		// 传入null则，默认图标
//		BitmapDescriptor marker = BitmapDescriptorFactory
//				.fromResource(R.drawable.location_marker);
//		mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(
//				LocationMode.FOLLOWING, true, marker));
//		// 开启定位图层
//		mBaiduMap.setMyLocationEnabled(true);
//		// 定位初始化
//		mLocClient = new LocationClient(this);
//		mLocClient.registerLocationListener(this);
//		LocationClientOption option = new LocationClientOption();
//		option.setOpenGps(true);// 打开gps
//		option.setCoorType("bd09ll"); // 设置坐标类型
//		option.setScanSpan(1000);
//		mLocClient.setLocOption(option);
//		mLocClient.start();
//
//		// 隐藏缩放控件
//
//		int childCount = mMapView.getChildCount();
//
//		View zoom = null;
//
//		for (int i = 0; i < childCount; i++) {
//
//			View child = mMapView.getChildAt(i);
//			if (child instanceof ZoomControls) {
//
//				zoom = child;
//
//				break;
//
//			}
//
//		}
//		zoom.setVisibility(View.GONE);
//		// 隐藏比例尺控件
//		int count = mMapView.getChildCount();
//
//		View scale = null;
//
//		for (int i = 0; i < count; i++) {
//
//			View child = mMapView.getChildAt(i);
//
//			if (child instanceof ZoomControls) {
//
//				scale = child;
//
//				break;
//
//			}
//
//		}
//
//		scale.setVisibility(View.GONE);

	}

	/**
	 * 定位SDK监听函数
	 */
	boolean isFirstLoc = true;// 是否首次定位

	private ArrayList<LatLng> mPointList = new ArrayList<LatLng>();

//	public void onReceiveLocation(BDLocation location) {
//		// map view 销毁后不在处理新接收的位置
//		if (location == null || mMapView == null) {
//			return;
//		}
//		LatLng mLatLng = new LatLng(location.getLatitude(),
//				location.getLongitude());
//
//		if (isFirstLoc) {
//			isFirstLoc = false;
//			Builder mBuilder = new MyLocationData.Builder();
//			mBuilder.accuracy(location.getRadius());
//			mBuilder.latitude(location.getLatitude());
//			mBuilder.longitude(location.getLongitude());
//			MyLocationData locData = mBuilder.build();
//			mBaiduMap.setMyLocationData(locData);
//			MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(mLatLng,
//					18);
//			mBaiduMap.animateMapStatus(u);
//
//			mPointList.add(mLatLng);
//			return;
//		}
//		if (location.getLocType() != BDLocation.TypeGpsLocation) {
//			return;
//		}
//		if (!isChallengeStart) {
//			return;
//		}
//		mPointList.add(mLatLng);
//		int speed = (int) (location.getSpeed());
//		challengeSpeed.setText(speed + "Km/h");
//		double miles = calculationMiles();
//		challengeMiles.setText(String.format("%.1f", miles) + "km");
//
//		Builder mBuilder = new MyLocationData.Builder();
//		mBuilder.accuracy(location.getRadius());
//		mBuilder.direction(location.getDirection());
//		mBuilder.latitude(location.getLatitude());
//		mBuilder.longitude(location.getLongitude());
//		MyLocationData locData = mBuilder.build();
//		mBaiduMap.setMyLocationData(locData);
//		drawLine();
//	}

//	private void drawLine() {
////		// 添加折线
//		if (mPointList.size() > 2) {
//			mBaiduMap.clear();
//			OverlayOptions ooPolyline = new PolylineOptions().width(10)
//					.color(0xCC42C0EA).points(mPointList);
//
//			mBaiduMap.addOverlay(ooPolyline);
//		}
//
//	}

//	private double calculationMiles() {
//		double sumMiles = 0;
//
////		if (mPointList.size() > 2) {
////			for (int i = 1; i < mPointList.size(); i++) {
////				double m = DistanceUtil.getDistance(mPointList.get(i - 1),
////						mPointList.get(i));
////				sumMiles += m;
////			}
////		}
//		return sumMiles / 1000;
//	}

//	public void onReceivePoi(BDLocation poiLocation) {
//
//	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onResume() {
		super.onResume();
//		mMapView.onResume();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 退出时销毁定位
//		mLocClient.stop();
		// 关闭定位图层
//		mBaiduMap.setMyLocationEnabled(false);
//		mMapView.onDestroy();
//		mMapView = null;
		if (timer != null) {
			timer.cancel();
			timer = null;

		}
		if (task != null) {
			task.cancel();
			task = null;
		}
		unregisterReceiver(mReceiver);
	}

	GetResultListCallback listener_start = new GetResultListCallback() {

		@Override
		public void onFinished(Object o) {
			currentID = (String) o;
			mHandler.sendEmptyMessage(0);
		}

		@Override
		public void onErro(Object o) {

			Message msg = new Message();
			msg.what = 1;
			msg.obj = o;
			mHandler.sendMessage(msg);

		}
	};

	GetResultListCallback listener_stop = new GetResultListCallback() {

		@Override
		public void onFinished(Object o) {
			mChallengeScore = (ChallengeScore) o;
			mHandler.sendEmptyMessage(2);
		}

		@Override
		public void onErro(Object o) {

			Message msg = new Message();
			msg.what = 3;
			msg.obj = o;
			mHandler.sendMessage(msg);

		}
	};

	GetResultListCallback listener_polling = new GetResultListCallback() {

		@Override
		public void onFinished(Object o) {
			mChallengeScore = (ChallengeScore) o;
			if (mChallengeScore.getStatus() == ChallengeScore.STATUS_ING) {

			} else {
				mHandler.sendEmptyMessage(2);
			}

		}

		@Override
		public void onErro(Object o) {

		}
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (isChallengeStart) {
				PopBoxCreat.createDialogWithTitle(ChallengeMapActivity.this,
						"提示", "您确定要中断本次挑战吗?", "", "确定", "取消", clickBack);
			} else {
				finish();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);

	}
}

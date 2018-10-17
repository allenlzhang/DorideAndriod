package com.carlt.chelepie.view.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.carlt.chelepie.appsdk.AppsdkUtils;
import com.carlt.chelepie.control.DeviceConnControl;
import com.carlt.chelepie.control.DeviceConnListener;
import com.carlt.chelepie.control.RecorderControl;
import com.carlt.chelepie.data.recorder.BaseResponseInfo;
import com.carlt.chelepie.data.recorder.PieAttrInfo;
import com.carlt.chelepie.data.recorder.PieInfo;
import com.carlt.chelepie.download.PieDownloadControl;
import com.carlt.chelepie.manager.DeviceConnectManager;
import com.carlt.chelepie.systemconfig.ActionConfig;
import com.carlt.chelepie.utils.PlayListener;
import com.carlt.chelepie.view.PopShow;
import com.carlt.chelepie.view.gl.IVideoView;
import com.carlt.doride.DorideApplication;
import com.carlt.doride.R;
import com.carlt.doride.base.LoadingActivity;
import com.carlt.doride.systemconfig.RuningConfig;
import com.carlt.doride.ui.view.PopBoxCreat;
import com.carlt.doride.ui.view.UUPopupWindow;
import com.carlt.doride.ui.view.UUToast;
import com.carlt.doride.utils.StringUtils;
import com.carlt.sesame.control.CPControl;
import com.carlt.sesame.ui.view.MenuImageShow;

import java.util.ArrayList;


/**
 * 时间：2015-8-3 描述：全屏直播
 */
public class FullLiveActivity extends LoadingActivity implements
        OnClickListener, DeviceConnListener {
	private TextView back;// 头部返回键

	private IVideoView mVideoView;

	private TextView mTxtError;// 播放错误提示

	private TextView mTxtWifi;// 网速

	private ImageView mImgConfig;// 配置

	private ImageView mImgQuality;// 画质

	private ImageView mImg10s;// 10s视频

	private ImageView mImgSilence;// 静音模式

	private ImageView mImgSetting;// 设置

	private ImageView mImgBack;// 回放

	private ImageView mImgCapture;// 抓拍

	private ImageView mImgClip;// 剪裁

	private ImageView mImgDb;// 图库

	private PieInfo mPieInfo;

	private View mViewLoading;// 加载页

	private View mFullBG;

	/**
	 * 全屏时头布局
	 */
	private View mFullHead;

	/**
	 * 全屏时底部布局
	 */
	private View mFullBottom;

	private ImageView mToPlay;

	private Dialog mProgress;

	/**
	 * 手势控制器
	 */
	private GestureDetector mGestureDetector;

	DeviceConnControl mConnControl;

	/**
	 * 设置全屏时的头布局和脚布局在显示7秒后隐藏的handler
	 */
	private Handler bhHandler = new Handler();

	/**
	 * 设置全屏时的头布局和脚布局在显示7秒后隐藏的Runnable 对象
	 */
	private Runnable myRunnable = new Runnable() {
		@Override
		public void run() {
			if (mFullHead.getVisibility() == View.VISIBLE) {
				hideBottomHead();
			}
		}
	};
	private Runnable myRunnable2 = new Runnable() {
		@Override
		public void run() {
			mImgCapture.setClickable(true);
			mImgCapture.setImageResource(R.drawable.pie_capture);
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_fulllive);
		mProgress = PopBoxCreat.createDialogWithProgress(FullLiveActivity.this,
				"正在加载");
		mConnControl = new DeviceConnControl(this, this);
		init();
		initTitle();
		mGestureDetector = new GestureDetector(this,
				new BookOnGestureListener());
		// 设置监听WIFI变化
		loadSuccessUI();
		bhHandler.postDelayed(myRunnable, 8000);
		DorideApplication.getInstanse().setMonitor(true);
		Log.e("FullActivity", "FullLiveActivity"+"onCreate");
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return mGestureDetector.onTouchEvent(event);
	}

	private void initTitle() {
		back = (TextView) findViewById(R.id.live_back_txt_back);
		back.setOnClickListener(this);
	}

	private String[] mNames = { "亮度", "饱和度", "对比度" };

	private int[] values = new int[3];

	private int quality = 0;// 画面质量

	private RelativeLayout videoLayout;

	private void init() {

		mViewLoading = findViewById(R.id.play_lay_loading);

		mTxtWifi = (TextView) findViewById(R.id.live_back_txt_wifi);

		mImgConfig = (ImageView) findViewById(R.id.live_back_img_config);
		mImgQuality = (ImageView) findViewById(R.id.live_back_img_quality);
		mImg10s = (ImageView) findViewById(R.id.live_back_img_10s);
		mImgSilence = (ImageView) findViewById(R.id.live_back_img_silence);
		mImgSetting = (ImageView) findViewById(R.id.live_back_img_setting);

		mImgBack = (ImageView) findViewById(R.id.recorder_play_img_back);
		mImgCapture = (ImageView) findViewById(R.id.recorder_play_img_capture);
		mImgClip = (ImageView) findViewById(R.id.recorder_play_img_clip);
		mImgDb = (ImageView) findViewById(R.id.recorder_play_img_db);

		mToPlay = (ImageView) findViewById(R.id.readyToPlay);
		mFullBG = findViewById(R.id.fullLive_bg);
		mFullHead = findViewById(R.id.recorder_play_head);
		mFullBottom = findViewById(R.id.recorder_play_bottom);

		mFullBG.setOnClickListener(this);
		mToPlay.setOnClickListener(this);
		mImgConfig.setOnClickListener(this);
		mImgQuality.setOnClickListener(this);
		mImg10s.setOnClickListener(this);
		mImgSilence.setOnClickListener(this);
		mImgSetting.setOnClickListener(this);

		mImgBack.setOnClickListener(this);
		mImgCapture.setOnClickListener(this);
		mImgClip.setOnClickListener(this);
		mImgDb.setOnClickListener(this);

		mFullHead.setVisibility(View.VISIBLE);
		mFullBottom.setVisibility(View.VISIBLE);
		mImgSilence.setVisibility(View.GONE);

		videoLayout = (RelativeLayout) findViewById(R.id.fulllive_videoview);
	}

	public void loading(boolean flag) {
		if (flag) {
			mViewLoading.setVisibility(View.VISIBLE);
		} else {
			mViewLoading.setVisibility(View.GONE);
		}
	}

	@Override
	public void loadSuccessUI() {

		mPieInfo = PieInfo.getInstance();

		// 是否录音
		if (mPieInfo.getAudioEnable() == 0) {
			mImgSilence.setImageResource(R.drawable.pie_silence_close);
		} else {
			mImgSilence.setImageResource(R.drawable.pie_silence_open);
		}

		// 抓拍是否带视频
		if (mPieInfo.getRecodEnable() == 0) {
			mImg10s.setImageResource(R.drawable.pie_10s_close);
		} else {
			mImg10s.setImageResource(R.drawable.pie_10s_open);
		}

		if (mPieInfo.getResolution() == 1) {
			mImgQuality.setImageResource(R.drawable.pie_hd);
		} else {
			mImgQuality.setImageResource(R.drawable.pie_fhd);
		}

		mDataList = new ArrayList<PieAttrInfo>();
		values[0] = mPieInfo.getBrightness();
		values[1] = mPieInfo.getSaturation();
		values[2] = mPieInfo.getConstrast();
		for (int i = 0; i < mNames.length; i++) {
			PieAttrInfo mInfo = new PieAttrInfo();
			mInfo.setName(mNames[i]);
			mInfo.setValue(values[i]);
			mDataList.add(mInfo);
		}
		super.loadSuccessUI();
	}


	private ArrayList<PieAttrInfo> mDataList;

	private PopShow mPopShow;

	private MenuImageShow mImageShow;

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.readyToPlay:
			mConnControl.goConnect();
			break;
		case R.id.live_back_txt_back:
			finish();
			break;
		case R.id.live_back_img_config:
			// 配置
			type = TYPE_IMGCONFIG;
			mPopShow = new PopShow(FullLiveActivity.this);
			mPopShow.SetOnDismissListener(mOnDismissListener);
			mPopShow.showList(mImgConfig, mDataList);
			break;
		case R.id.live_back_img_quality:
			// 画质
			type = TYPE_QUALITY;
			mPopShow = new PopShow(FullLiveActivity.this);
			mPopShow.SetOnDismissListener(mOnDismissListener);
			mPopShow.setmCheckedChangedListener(mCheckedChangedListener);
			mPopShow.showRg(mImgQuality, mPieInfo.getResolution());
			break;
		case R.id.live_back_img_10s:
			// 10s视频
			type = TYPE_LINKED;
			mPopShow = new PopShow(FullLiveActivity.this);
			mPopShow.SetOnDismissListener(mOnDismissListener);
			mPopShow.showTxt(mImg10s, "自动下载拍照前后共10s视频");
			break;
		case R.id.live_back_img_silence:
			// 静音
			RecorderControl.setAudio(listener_sound);
			break;
		case R.id.live_back_img_setting:
			// 设置
			Intent mIntent1 = new Intent(FullLiveActivity.this,
					ManagePieActivity.class);
			startActivity(mIntent1);
			break;
		case R.id.recorder_play_img_back:
			// 回放
			Intent mIntent2 = new Intent(FullLiveActivity.this,PlaybackActivity.class);
			mIntent2.putExtra("isfromLive", true);
			startActivity(mIntent2);
			break;
		case R.id.recorder_play_img_capture:
			// 抓拍
			if (RuningConfig.ISCaptureRuning) {
				UUToast.showUUToast(FullLiveActivity.this, "请等待本次操作完成");
				break;
			}
			if (mProgress != null) {
				mProgress.dismiss();
			}
			mProgress = PopBoxCreat.createDialogWithProgress(
					FullLiveActivity.this, "正在通知抓拍");
			mProgress.show();
			mImgCapture.setClickable(false);
			mImgCapture.setImageResource(R.drawable.pie_capture_selected);
			bhHandler.removeCallbacks(myRunnable2);
			bhHandler.postDelayed(myRunnable2, 10000);//10秒之后可点击
			PieDownloadControl.media_StartDownload();
			RecorderControl.takePhoto(listener_photo);
			break;
		case R.id.recorder_play_img_clip:
			// 剪裁
			Intent mIntent3 = new Intent(FullLiveActivity.this,
					PlaybackActivity.class);
			mIntent3.putExtra("is_cut", true);
			startActivity(mIntent3);
			break;
		case R.id.recorder_play_img_db:
			// 跳转到相册
			Intent mIntent4 = new Intent(FullLiveActivity.this,
					MyMediaListActivity.class);
			startActivity(mIntent4);
			break;
		}
	}

	private int type;

	private final static int TYPE_IMGCONFIG = 1001;// 图片亮度相关设置

	private final static int TYPE_QUALITY = 1002;// 画面质量

	private final static int TYPE_LINKED = 1003;// 拍照关联10s短视频

	private UUPopupWindow.OnDismissListener mOnDismissListener = new UUPopupWindow.OnDismissListener() {

		@Override
		public void onDismiss() {

			switch (type) {
			case TYPE_IMGCONFIG:

				int brightness = mDataList.get(0).getValue();
				int contrast = mDataList.get(2).getValue();
				int saturation = mDataList.get(1).getValue();

				int brightness_history = mPieInfo.getBrightness();
				int contrast_history = mPieInfo.getConstrast();
				int saturation_history = mPieInfo.getSaturation();

				if (brightness == brightness_history
						&& contrast == contrast_history
						&& saturation == saturation_history) {
				} else {
					RecorderControl.setVideoColor(brightness, contrast,
							saturation, listener_imgconfig);
				}
				break;

			case TYPE_QUALITY:
				int quality_history = mPieInfo.getResolution();
				if (quality == quality_history) {
				} else {
					RecorderControl.setVideoSize(quality, listener_quality);
				}
				break;
			case TYPE_LINKED:
				// 抓拍是否带视频
				RecorderControl.setCaptureRecordVideo(listener_linked);
				break;
			}
		}
	};

	private PopShow.OnPopshowCheckedChangedListener mCheckedChangedListener = new PopShow.OnPopshowCheckedChangedListener() {

		@Override
		public void onChecked(int imgQuality) {
			quality = imgQuality;
		}
	};

	private CPControl.GetResultListCallback listener_photo = new CPControl.GetResultListCallback() {

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

	private CPControl.GetResultListCallback listener_imgconfig = new CPControl.GetResultListCallback() {

		@Override
		public void onFinished(Object o) {
			Message msg = new Message();
			msg.what = 4;
			mHandler.sendMessage(msg);

		}

		@Override
		public void onErro(Object o) {
			Message msg = new Message();
			msg.what = 5;
			msg.obj = o;
			mHandler.sendMessage(msg);

		}
	};

	private CPControl.GetResultListCallback listener_quality = new CPControl.GetResultListCallback() {

		@Override
		public void onFinished(Object o) {
			Message msg = new Message();
			msg.what = 6;
			mHandler.sendMessage(msg);

		}

		@Override
		public void onErro(Object o) {
			Message msg = new Message();
			msg.what = 7;
			msg.obj = o;
			mHandler.sendMessage(msg);

		}
	};

	private CPControl.GetResultListCallback listener_linked = new CPControl.GetResultListCallback() {

		@Override
		public void onFinished(Object o) {
			Message msg = new Message();
			msg.what = 8;
			mHandler.sendMessage(msg);
		}

		@Override
		public void onErro(Object o) {
			Message msg = new Message();
			msg.what = 9;
			msg.obj = o;
			mHandler.sendMessage(msg);
		}
	};

	private CPControl.GetResultListCallback listener_sound = new CPControl.GetResultListCallback() {

		@Override
		public void onFinished(Object o) {
			Message msg = new Message();
			msg.what = 10;
			mHandler.sendMessage(msg);
		}

		@Override
		public void onErro(Object o) {
			Message msg = new Message();
			msg.what = 11;
			msg.obj = o;
			mHandler.sendMessage(msg);
		}
	};

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				break;
			case 1:
				mHandler.sendEmptyMessageDelayed(1, 1000);
				String s = StringUtils.caculateSpeed();
				mTxtWifi.setText(s);
				break;
			case 2:
				mProgress.dismiss();
				BaseResponseInfo bInfo = (BaseResponseInfo) msg.obj;
				 if (bInfo.getFlag() == BaseResponseInfo.SUCCESS) {
                     UUToast.showUUToast(FullLiveActivity.this, bInfo.getInfo());
                 }
				break;
			case 3:
				mProgress.dismiss();
				// 抓拍失败
				BaseResponseInfo mInfo = (BaseResponseInfo) msg.obj;
//				if (mInfo != null) {
//					UUToast.showUUToast(FullLiveActivity.this, "抓拍操作失败："
//							+ mInfo.getInfo());
//				} else {
//					UUToast.showUUToast(FullLiveActivity.this, "抓拍操作失败");
//				}
				break;
			case 4:
				// 设置图片饱和度等信息成功
				UUToast.showUUToast(FullLiveActivity.this, "设置视频显示信息成功！");
				break;
			case 5:
				// 设置图片饱和度等信息失败
				UUToast.showUUToast(FullLiveActivity.this, "设置图片信息失败。。。");
				break;
			case 6:
				// 设置画面质量成功
				UUToast.showUUToast(FullLiveActivity.this, "设置画面质量成功！");
				if (mPieInfo.getResolution() == 1) {
					mImgQuality.setImageResource(R.drawable.pie_hd);
				} else {
					mImgQuality.setImageResource(R.drawable.pie_fhd);
				}
				break;

			case 7:
				// 设置画面质量失败
				UUToast.showUUToast(FullLiveActivity.this, "设置画面质量失败。。。");
				break;
			case 8:
				// 设置抓拍关联10S短视频成功
				UUToast.showUUToast(FullLiveActivity.this,
						"设置自动下载拍照前后共10s短视频成功！");
				if (mPieInfo.getRecodEnable() == 1) {
					mImg10s.setImageResource(R.drawable.pie_10s_open);
				} else {
					mImg10s.setImageResource(R.drawable.pie_10s_close);
				}
				break;
			case 9:
				// 设置抓拍关联10S短视频失败
				UUToast.showUUToast(FullLiveActivity.this,
						"设置自动下载拍照前后共10s短视频失败。。。");
				break;
			case 10:
				// 设置录像时录音成功
				UUToast.showUUToast(FullLiveActivity.this, "设置录像时录音成功！");
				if (mPieInfo.getAudioEnable() == 1) {
					mImgSilence.setImageResource(R.drawable.pie_silence_open);
				} else {
					mImgSilence.setImageResource(R.drawable.pie_silence_close);
				}
				break;
			case 11:
				// 设置录像时录音失败
				UUToast.showUUToast(FullLiveActivity.this, "设置录像时录音失败。。。");
				break;
			case 13:
				// 通知直播成功
				//	loading(false);
				UUToast.showUUToast(FullLiveActivity.this, "开启直播成功，正在加载数据");
				mVideoView.play();
				break;
			case 14:
				// 通知直播失败
				loading(false);
				UUToast.showUUToast(FullLiveActivity.this, "开启直播失败");
				mFullBG.setVisibility(View.VISIBLE);
				break;
			case 1014:
				UUToast.showUUToast(FullLiveActivity.this, "播放出现问题");
				AppsdkUtils.CMStop(ActionConfig.getSeqNum());
				mVideoView.stop();
				loading(false);
				mFullBG.setVisibility(View.VISIBLE);
				break;
			case 1015:
				loading(false);
				break;
			}
		}

	};

	private CPControl.GetResultListCallback listener_monitor = new CPControl.GetResultListCallback() {

		@Override
		public void onFinished(Object o) {
			Message msg = new Message();
			msg.what = 13;
			msg.obj = o;
			mHandler.sendMessage(msg);
		}

		@Override
		public void onErro(Object o) {
			Message msg = new Message();
			msg.what = 14;
			msg.obj = o;
			mHandler.sendMessage(msg);
		}
	};

	@Override
	protected void onPause() {
		super.onPause();
		DorideApplication.getInstanse().setToFullFlag(false);
		AppsdkUtils.CMStop(ActionConfig.getSeqNum());
		mVideoView.stop();
		videoLayout.removeAllViews();
		mConnControl.onPause();
		loading(false);
		mFullBG.setVisibility(View.VISIBLE);
		mHandler.removeMessages(1);
		Log.e("FullActivity", "FullLiveActivity"+"onPause");
	}

	@Override
	protected void onResume() {
		super.onResume();
		videoLayout.removeAllViews();
		mVideoView = DorideApplication.getInstanse().getVideoView();
		videoLayout.addView((View) mVideoView, new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		mVideoView.setmListener(mPlayListener);
		((View) mVideoView).setKeepScreenOn(true);
		mConnControl.onResume();
		if (DeviceConnectManager.isDeviceConnect()) {
			connOk();
		} else {
			mConnControl.goConnect();
		}
		checkPaiSetting();
		mHandler.sendEmptyMessage(1);
		Log.e("FullActivity", "FullLiveActivity"+"onResume");
	}

	@Override
	protected void onStop() {
		super.onStop();
		mConnControl.onStop();
		Log.e("FullActivity", "FullLiveActivity"+"onStop");
	}

	/**
	 * 页面显示的时候检查下 设置项
	 */
	public void checkPaiSetting() {
		if (mPieInfo == null) {
			return;
		}

		if (mPieInfo.getAudioEnable() == 1) {
			mImgSilence.setImageResource(R.drawable.pie_silence_open);
		} else {
			mImgSilence.setImageResource(R.drawable.pie_silence_close);
		}

		if (mPieInfo.getRecodEnable() == 1) {
			mImg10s.setImageResource(R.drawable.pie_10s_open);
		} else {
			mImg10s.setImageResource(R.drawable.pie_10s_close);
		}

		if (mPieInfo.getResolution() == 1) {
			mImgQuality.setImageResource(R.drawable.pie_hd);
		} else {
			mImgQuality.setImageResource(R.drawable.pie_fhd);
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mConnControl.onDestory();
		mConnControl = null;
		Log.e("FullActivity", "FullLiveActivity"+"onDestroy");
	}

	PlayListener mPlayListener = new PlayListener() {

		@Override
		public void onError() {
			// --播放期间出现问题
			mHandler.sendEmptyMessage(1014);
		}

		@Override
		public void onReady() {
			// ---已经准备好了
			mHandler.sendEmptyMessage(1015);
		}
	};

	@Override
	public void connOk() {
		if (!mIsShowing) {
			return;
		}
		// 加载显示
		mFullBG.setVisibility(View.GONE);
		loading(true);
		RecorderControl.getRecorderSetting((CPControl.GetResultListCallback) mCallback);
		if(!DorideApplication.getInstanse().isToFullFlag()){
			RecorderControl.startMonitor(listener_monitor);
		}else{
			mVideoView.play();
		}
	}

	@Override
	public void connError() {
		loading(false);
		mFullBG.setVisibility(View.VISIBLE);
	}

	/**
	 * 
	 * @author liuxiangfei
	 * 
	 *         Description 手势的监听器
	 * 
	 */
	class BookOnGestureListener extends GestureDetector.SimpleOnGestureListener {

		@Override
		public boolean onSingleTapUp(MotionEvent e) {

			if (mFullHead.getVisibility() == View.VISIBLE) {
				hideBottomHead();
			} else if (mFullHead.getVisibility() == View.GONE) {
				bhHandler.removeCallbacks(myRunnable);
				bhHandler.postDelayed(myRunnable, 8000);
				showBottomHead();

			}
			return true;
		}
	}

	/**
	 * 隐藏底部和顶部
	 */
	private void showBottomHead() {
		mFullHead.setVisibility(View.VISIBLE);
		mFullBottom.setVisibility(View.VISIBLE);
		AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
		alphaAnimation.setDuration(500);
		mFullHead.startAnimation(alphaAnimation);
		mFullBottom.startAnimation(alphaAnimation);
	}

	/**
	 * 隐藏底部和顶部
	 */
	private void hideBottomHead() {
		mFullHead.setVisibility(View.GONE);
		mFullBottom.setVisibility(View.GONE);
		AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
		alphaAnimation.setDuration(500);
		mFullHead.startAnimation(alphaAnimation);
		mFullBottom.startAnimation(alphaAnimation);

	}
}

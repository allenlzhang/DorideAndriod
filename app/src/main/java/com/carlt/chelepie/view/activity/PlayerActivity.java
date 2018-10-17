package com.carlt.chelepie.view.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
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
import android.widget.SeekBar;
import android.widget.TextView;


import com.carlt.chelepie.control.DaoPieDownloadControl;
import com.carlt.chelepie.control.RecorderControl;
import com.carlt.chelepie.data.recorder.PieDownloadInfo;
import com.carlt.chelepie.data.recorder.PieInfo;
import com.carlt.chelepie.utils.CodecPlayerUtil;
import com.carlt.chelepie.view.gl.HVideoPlayerView;
import com.carlt.doride.R;
import com.carlt.doride.base.BaseActivity;
import com.carlt.doride.preference.UseInfoLocal;
import com.carlt.doride.ui.view.UUToast;
import com.carlt.doride.utils.FileUtil;
import com.carlt.doride.utils.ImageUtils;
import com.carlt.doride.utils.LocalConfig;
import com.carlt.doride.utils.StringUtils;
import com.carlt.sesame.utility.MyTimeUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Date;

/**
 * 全屏播放
 * 
 * @author liu 0.底部操作框隔一段时间会自动消失，或者轻触屏幕自动消失 1.检查是否连接wifi 2.建立下载链接
 *         3.下载缓冲文件（ps流）到本地，UI显示进度条 4.解码（软解，硬解） 5.播放 5.1. 截图抓屏功能 5.2. 显示进度 5.3.
 *         拖动进度条显示相应进度 6.退出销毁回收资源
 * 
 */
public class PlayerActivity extends BaseActivity implements OnClickListener {

	/**
	 * 底部布局
	 */
	private View bottemLay;
	private View loadingLay;// 加载中
	private ImageView mCapture;
	private View mImgLay;
	private SeekBar seekBar;
	private RelativeLayout videoLayout;
	private ImageView playBtn;
	private TextView playTimeTV;
	private ImageView cutBtn;
	private ImageView downloadBtn;
	private TextView errMsg;

	private HVideoPlayerView mVideoView;
	/**
	 * 手势控制器
	 */
	private GestureDetector mGestureDetector;
	/**
	 * 设置全屏时的头布局和脚布局在显示7秒后隐藏的handler
	 */
	private Handler bhHandler = new Handler();

	private String filePath;

	/**
	 * 上一个页面传过来的
	 */
	private PieDownloadInfo pieInfo;

	/**
	 * 当前播放的,下载返回的
	 */
	private PieDownloadInfo pieDownInfo;

	private DaoPieDownloadControl mDaoPieDownloadControl = DaoPieDownloadControl
			.getInstance();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_video_fream);
		Intent intent = getIntent();
		if (intent != null) {
			pieInfo = (PieDownloadInfo) intent
					.getParcelableExtra("pieDownloadInfo");
			storeType = pieInfo.getStoreType();
		}
		initView();
		initPlayer();
	}

	private void initView() {
		bottemLay = findViewById(R.id.bottom_lay);
		seekBar = (SeekBar) findViewById(R.id.seekbar);

		loadingLay = findViewById(R.id.player_progress_loading);
		mImgLay = findViewById(R.id.player_imgLay);
		mCapture = (ImageView) findViewById(R.id.recorder_player_img_capture);
		videoLayout = (RelativeLayout) findViewById(R.id.videoLayout);
		playBtn = (ImageView) findViewById(R.id.player_play_btn);
		playTimeTV = (TextView) findViewById(R.id.play_time);
		errMsg = (TextView) findViewById(R.id.tv_err_msg);
		cutBtn = (ImageView) findViewById(R.id.player_cut_btn);
		downloadBtn = (ImageView) findViewById(R.id.player_download_btn);
		playBtn.setClickable(false);
		playBtn.setOnClickListener(this);
		cutBtn.setOnClickListener(this);
		downloadBtn.setOnClickListener(this);
		// seekbar设置监听
		seekBar.setOnSeekBarChangeListener(onseekBarListener);
	}

	/**
	 * 是否暂停中
	 */
	boolean isPause = false;

	public void bottomClick(View v) {
		//点击底部框，暂时不做处理
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.player_play_btn:
			playBtn.setClickable(false);
			if (isPause) {
				mVideoView.continuePlay();
			} else {
				mVideoView.pause();
			}
			break;
		case R.id.player_cut_btn:
			cutBtn.setClickable(false);
			cutBtn.setImageResource(R.drawable.player_cut_down);
			mVideoView.cutPic();
			break;
		case R.id.player_download_btn:
			downloadBtn.setClickable(false);
			downloadBtn.setImageResource(R.drawable.player_download_down);
			downloadVideo();
			break;
		default:
			break;
		}
	}

	/**
	 * 下载
	 */
	private void downloadVideo() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				String oldPath = null;
				 oldPath = pieDownInfo.getLocalPath();
				 if(StringUtils.isEmpty(oldPath) || !(new File(oldPath)).exists()){
					 oldPath = LocalConfig.GetMediaPath(UseInfoLocal
								.getUseInfo().getAccount(), PieInfo.getInstance()
								.getDeviceName(), LocalConfig.DIR_TEMP)
								+ pieDownInfo.getFileName();
				 }
				

				String newPath = LocalConfig.GetMediaPath(UseInfoLocal
						.getUseInfo().getAccount(), PieInfo.getInstance()
						.getDeviceName(), pieInfo.getDir(storeType))
						+ pieDownInfo.getFileName();
				try {
					int byteread = 0;
					File oldfile = new File(oldPath);
					File newfile = new File(newPath);
					if (oldfile.exists()) { // 文件存在时
						if (newfile.exists()) {
							pieInfo.setLocalPath(newPath);
							mHandler.sendEmptyMessage(CodecPlayerUtil.TYPE_DOWNLOAD_SUCCSE);
							return;
						}
						InputStream inStream = new FileInputStream(oldPath); // 读入原文件
						FileOutputStream fs = new FileOutputStream(newPath);
						byte[] buffer = new byte[1024 * 4];
						while ((byteread = inStream.read(buffer)) != -1) {
							fs.write(buffer, 0, byteread);
						}
						inStream.close();
						fs.close();
						pieInfo.setLocalPath(newPath);
						mHandler.sendEmptyMessage(CodecPlayerUtil.TYPE_DOWNLOAD_SUCCSE);
					} else {
						// 文件不存在
						mHandler.sendEmptyMessage(CodecPlayerUtil.TYPE_DOWNLOAD_ERR);
					}
				} catch (Exception e) {
					e.printStackTrace();
					FileUtil.deleteFile(new File(newPath));
					mHandler.sendEmptyMessage(CodecPlayerUtil.TYPE_DOWNLOAD_ERR);
				}
			}
		}).start();

	}

	private SeekBar.OnSeekBarChangeListener onseekBarListener = new SeekBar.OnSeekBarChangeListener() {
		/*
		 * seekbar改变时的事件监听处理
		 */
		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
                                      boolean fromUser) {
			mVideoView.playProgress(progress);
		}

		/*
		 * 按住seekbar时的事件监听处理
		 */
		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
		}

		/*
		 * 放开seekbar时的时间监听处理
		 */
		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
		}
	};

	private void initPlayer() {
		loadingLay.setVisibility(View.VISIBLE);

		//3.如果是剪裁类型不显示下载按钮
		if(storeType == PieDownloadInfo.STORE_CROP || (!StringUtils.isEmpty(pieInfo.getLocalPath()) && pieInfo.getLocalPath().contains(LocalConfig.DIR_CROP))){
			downloadBtn.setVisibility(View.GONE);
		}
		// 1.如果是本地文件直接播放
		// 2.如果是设备文件先下载缓存，在播放
		if (mDaoPieDownloadControl.isDownLoad(pieInfo)) {
			pieInfo = mDaoPieDownloadControl.getFinishedInfo(pieInfo);
			if ( pieInfo != null && pieInfo.getStatus() == PieDownloadInfo.STATUS_FINISHED) {
				downloadBtn.setClickable(false);
				downloadBtn.setImageResource(R.drawable.player_download_succes);
				if (FileUtil.isExist(pieInfo.getLocalPath())){
					playLocal(pieInfo.getLocalPath());
				}else{
					playTbox();
				}
			} else {
				playTbox();
			}
		} else {
			//检查缓存有没有
//			String tempPath = LocalConfig.GetMediaPath(UseInfoLocal
//					.getUseInfo().getAccount(), PieInfo.getInstance()
//					.getDeviceName(),LocalConfig.DIR_TEMP) + pieInfo.getFileName();
			String existPath = FileUtil.getExistPath(LocalConfig.GetMediaPathMain(UseInfoLocal
					.getUseInfo().getAccount()), pieInfo.getFileName());
			
			if(FileUtil.isExist(existPath)){
				playLocal(existPath);
			}else{
				playTbox();
			}
		}
	}

	/**
	 * 播放设备端
	 */
	private void playTbox() {
		pieDownInfo = pieInfo;
		downloadBtn.setClickable(true);
		downloadBtn.setImageResource(R.drawable.player_download_down);
		startDownLoadTemp();
	}

	/**
	 * 播放本地文件
	 */
	private void playLocal(String localPath) {
		if(localPath.contains(LocalConfig.DIR_THUMBNAIL)){
			playTbox();
		}
		bottemLay.setVisibility(View.VISIBLE);
		seekBar.setVisibility(View.VISIBLE);
		pieDownInfo = pieInfo;
		pieDownInfo.setLocalPath(localPath);
		startPlay(localPath);
		mGestureDetector = new GestureDetector(PlayerActivity.this,
				new BookOnGestureListener());
		bhHandler.postDelayed(myRunnable, 15000);
	}

	/**
	 * 开始下载缓存
	 */
	private void startDownLoadTemp() {
		if (pieInfo != null) {
			bottemLay.setVisibility(View.INVISIBLE);
			seekBar.setVisibility(View.INVISIBLE);
			pieInfo.setStoreType(PieDownloadInfo.STORE_TEMP);

			RecorderControl.getDownLoadFile(downLoadLisener, pieInfo);
		} else {
			UUToast.showUUToast(PlayerActivity.this, "未找到视频文件");
		}
		// 下载成功后播放
	}

	/**
	 * 开始播放
	 * 
	 */
	private void startPlay(String filePath) {
		videoLayout.removeAllViews();
		mVideoView = new HVideoPlayerView(this);
		videoLayout.addView((View) mVideoView, new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		mVideoView.play(filePath, mHandler);
		if(pieInfo.getStoreType() == PieDownloadInfo.STORE_CROP){
			mVideoView.configCrop();
		}
	}

	/**
	 * 缓冲文件监听
	 */
	RecorderControl.GetTranslateProgressCallback downLoadLisener = new RecorderControl.GetTranslateProgressCallback() {
		@Override
		public void onFinished(Object o1) {

			Message msg = new Message();
			msg.what = 101;
			msg.obj = o1;
			mHandler.sendMessage(msg);
		}

		@Override
		public void onErro(Object o) {
			Message msg = new Message();
			msg.what = 102;
			msg.obj = o;
			mHandler.sendMessage(msg);
		}

		@Override
		public void onTranslateProgress(Object progress) {
		}

		@Override
		public void onUpdateProgress(int progress) {
		}

	};

	/**
	 * 播放器操作返回的
	 */
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case CodecPlayerUtil.TYPE_START_PLAY:// 开始播放
				playBtn.setClickable(true);
				isPause = false;
				cutBtn.setVisibility(View.VISIBLE);
				playBtn.setImageResource(R.drawable.player_pause_selector);
				loadingLay.setVisibility(View.GONE);
				break;
			case CodecPlayerUtil.TYPE_PAUSE: // 暂停中
				playBtn.setClickable(true);
				isPause = true;
				playBtn.setImageResource(R.drawable.player_play_selector);
				break;
			case CodecPlayerUtil.TYPE_CUT_SUCCSE: // 截图成功
				cutBtn.setClickable(true);
				cutBtn.setImageResource(R.drawable.player_cut_selector);
				Log.e("info", "截图成功!!!");
				Bitmap bit = (Bitmap) msg.obj;
				if (bit != null) {
					mHandler.removeMessages(20);
					mImgLay.setVisibility(View.VISIBLE);
					mCapture.setImageBitmap(bit);
					String time = MyTimeUtil.getFormatTime(new Date());
					String fileName = MyTimeUtil.getTimeFileName("H", time)
							+ ".jpg";
					String account = UseInfoLocal.getUseInfo().getAccount();
					String deviceName = PieInfo.getInstance().getDeviceName();
					String path = LocalConfig.GetMediaPath(account, deviceName,
							LocalConfig.DIR_CAPTURE) + fileName;
					PieDownloadInfo mInfo = new PieDownloadInfo();
					mInfo.setAccout(account);
					mInfo.setDeviceName(deviceName);
					mInfo.setType(PieDownloadInfo.TYPE_JPG);
					mInfo.setStoreType(PieDownloadInfo.STORE_CAPTURE);
					mInfo.setStatus(PieDownloadInfo.STATUS_FINISHED);
					mInfo.setFileSrcName(fileName);
					mInfo.setFileName(fileName);
					mInfo.setStartTime(time);
					mInfo.setEndTime(time);
					mInfo.setCreateTime(time);
					mInfo.setLocalPath(path);
					int width = bit.getWidth();
					if (width == 1080 || width == 1920) {
						mInfo.setResolution(0);
					} else {
						mInfo.setResolution(1);
					}
					try {
						bit.compress(CompressFormat.JPEG, 100,new FileOutputStream(path));
						mInfo.setTotalLen((int) (FileUtil.getFileLength(path)));
						ImageUtils.saveImageToGallery(PlayerActivity.this, path,fileName);
						DaoPieDownloadControl.getInstance().insert(mInfo);
					} catch (FileNotFoundException e) {
						Log.e("DEBUG", "保存图片失败" + e.getMessage());
					}
					mHandler.sendEmptyMessageDelayed(20, 1000 * 2);
				} else {
					UUToast.showUUToast(PlayerActivity.this, "截图失败!");
				}
				break;
			case CodecPlayerUtil.TYPE_CUR_ERR:// 截图失败
				cutBtn.setClickable(true);
				cutBtn.setImageResource(R.drawable.player_cut_selector);
				UUToast.showUUToast(PlayerActivity.this, "截图失败!");
				break;
			case CodecPlayerUtil.TYPE_DOWNLOAD_SUCCSE: // 下载成功
				downloadBtn.setImageResource(R.drawable.player_download_succes);
				pieDownInfo.setStoreType(pieInfo.getStoreType());
				pieDownInfo.setStatus(PieDownloadInfo.STATUS_FINISHED);
				DaoPieDownloadControl.getInstance().insert(pieDownInfo);
				UUToast.showUUToast(PlayerActivity.this, "下载成功");
				break;
			case CodecPlayerUtil.TYPE_DOWNLOAD_ERR: // 下载失败
				downloadBtn.setClickable(true);
				downloadBtn
						.setImageResource(R.drawable.player_download_selector);
				UUToast.showUUToast(PlayerActivity.this, "下载失败");
				break;
			case CodecPlayerUtil.TYPE_TIME_STRING: // 显示时间
				playTimeTV.setText((String) msg.obj);
				break;
			case CodecPlayerUtil.TYPE_PROCESS: // 显示进度
				seekBar.setProgress((Integer) msg.obj);
				break;
			case CodecPlayerUtil.TYPE_END: // 结束播放了
				playBtn.setClickable(true);
				isPause = true;
				cutBtn.setVisibility(View.VISIBLE);
				playBtn.setImageResource(R.drawable.player_replay_selector);
				loadingLay.setVisibility(View.GONE);
				break;
			case 20: // 取消显示抓拍到的图片
				mImgLay.setVisibility(View.GONE);
				break;
			case 101: // 缓冲完成
				bottemLay.setVisibility(View.VISIBLE);
				seekBar.setVisibility(View.VISIBLE);
				pieDownInfo = (PieDownloadInfo) msg.obj;
				startPlay(pieDownInfo.getLocalPath());
				mGestureDetector = new GestureDetector(PlayerActivity.this,
						new BookOnGestureListener());
				bhHandler.postDelayed(myRunnable, 15000);
				break;
			case 102: // 缓冲错误
				bottemLay.setVisibility(View.INVISIBLE);
				seekBar.setVisibility(View.INVISIBLE);
				loadingLay.setVisibility(View.GONE);
//				 UUToast.showUUToast(PlayerActivity.this,
//				 "缓冲失败");
				 errMsg.setText("缓冲失败");
				break;
			}
		};
	};

	class BookOnGestureListener extends GestureDetector.SimpleOnGestureListener {

		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			if (seekBar.getVisibility() == View.VISIBLE) {
				hideShowBottomHead(false);
			} else {
				bhHandler.removeCallbacks(myRunnable);
				bhHandler.postDelayed(myRunnable, 8000);
				hideShowBottomHead(true);
			}
			return true;
		}

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (null != mGestureDetector) {
			return mGestureDetector.onTouchEvent(event);
		} else {
			return super.onTouchEvent(event);
		}
	}

	/**
	 * 设置全屏时的头布局和脚布局在显示7秒后隐藏的Runnable 对象
	 */
	private Runnable myRunnable = new Runnable() {
		@Override
		public void run() {
			if (seekBar.getVisibility() == View.VISIBLE) {
				hideShowBottomHead(false);
			}
		}
	};
	private int storeType;

	/**
	 * true 显示，false 隐藏
	 * 
	 * @param b
	 */
	private void hideShowBottomHead(boolean b) {
		if (!b) {
			bottemLay.setVisibility(View.GONE);
			seekBar.setVisibility(View.GONE);
			AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
			alphaAnimation.setDuration(500);
			seekBar.startAnimation(alphaAnimation);
			bottemLay.startAnimation(alphaAnimation);
		} else {
			seekBar.setVisibility(View.VISIBLE);
			bottemLay.setVisibility(View.VISIBLE);
			AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
			alphaAnimation.setDuration(500);
			seekBar.startAnimation(alphaAnimation);
			bottemLay.startAnimation(alphaAnimation);
		}

	}

	@Override
	protected void onPause() {
		super.onPause();
		if(loadingLay.getVisibility() == View.VISIBLE && null != pieDownInfo){
			FileUtil.deleteFile(new File(pieDownInfo.getLocalPath()));
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (null != mVideoView) {
			mVideoView.stopVideo();
		}
		downLoadLisener = null;
		RecorderControl.stopDownLoadFile(pieInfo);
	}
}

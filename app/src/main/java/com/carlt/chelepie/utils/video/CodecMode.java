package com.carlt.chelepie.utils.video;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import com.carlt.chelepie.appsdk.AppsdkUtils;
import com.carlt.chelepie.appsdk.IFreamDataListener;
import com.carlt.chelepie.control.RecorderControl;
import com.carlt.chelepie.systemconfig.ActionConfig;
import com.carlt.chelepie.utils.PlayListener;
import com.carlt.chelepie.utils.voice.AudioAACPlayer;
import com.carlt.chelepie.utils.voice.IVoicePlayer;
import com.carlt.chelepie.view.activity.FullPlayActivity;
import com.carlt.chelepie.view.gl.GLFrameRenderer;
import com.carlt.chelepie.view.gl.GLFrameSurface;
import com.carlt.chelepie.view.gl.ISimplePlayer;
import com.carlt.doride.DorideApplication;
import com.carlt.doride.protocolparser.BaseParser;
import com.carlt.doride.utils.ImageUtils;
import com.carlt.doride.utils.StringUtils;
import com.carlt.sesame.control.CPControl;
import com.hz17car.chelepie.utility.Datayuv;
import com.hz17car.chelepie.utility.FFmpegTool;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 编解码器的实现类
 *
 * @author Administrator
 *
 */
public class CodecMode implements ICodecMode, ISimplePlayer {

	private String TAG = "CodecMode";

	private GLFrameSurface glFrameSurface;

	private GLFrameRenderer mGLFRenderer;
	/**
	 * 视频数据
	 */
//	private List<byte[]> mVideoFrames = Collections.synchronizedList(new ArrayList<byte[]>());
	private ConcurrentLinkedQueue<byte[]> mVideoFrames = new ConcurrentLinkedQueue<byte[]>();


	private final static int DEF_VIDEO_WIDTH = 1920;//720

	private final static int DEF_VIDEO_HEIGHT = 1080;//576

	private int video_width = DEF_VIDEO_WIDTH;

	private int video_height = DEF_VIDEO_HEIGHT;

	// run flag
	private boolean mIsRun = false;

	/**
	 * 接收数据的开关
	 */
	private boolean isRecRunFlag = false;

	/**
	 * 编解码线程的开关
	 */
	private boolean isDecodeRunFlag = true;

	/**
	 * 暂停
	 */
	private boolean mIsPause = false;
	private boolean mIsPauseFlag = false;//表示暂停完成,绘制线程告诉解码线程可以继续解码了

	/**
	 * 是否是静音,true 表示静音
	 */
	private boolean isSlience = false;

	/**
	 * 是否是直播
	 */
	private boolean mIsMonitor = true;

	/**
	 *  是否可以播放了 等到视频和音频都有的时候再开始播放保证同步
	 */
	private boolean mIsStartPlay = false;
	/**
	 * 数据解析工具
	 */
	private FFmpegTool mFFmpegTool;
	/**
	 * 解码绘制成功的次数
	 */
	private int decode_times = 0;
	/**
	 * 解码绘制失败的此时
	 */
	private int error_times = 0;

	/**
	 * 解码的次数
	 */
	int countDecode = 0;

	int width = 0;
	int height = 0;

	/**
	 * 是否需要抓拍
	 */
	boolean isCaptrue = false;

	/**
	 * 发送广播的intent
	 */
	static Intent intent;
//	FileOutputStream streamout = null;

	private IVoicePlayer audioPlayer;

	public CodecMode(GLFrameSurface frameSurface) {
		glFrameSurface = frameSurface;
		mFFmpegTool = new FFmpegTool();
		glFrameSurface.setKeepScreenOn(true);
		glFrameSurface.setEGLContextClientVersion(2);
		dm = new DisplayMetrics();
		WindowManager windowManager = (WindowManager) DorideApplication.ApplicationContext
				.getSystemService(Context.WINDOW_SERVICE);
		windowManager.getDefaultDisplay().getMetrics(dm);
		mGLFRenderer = new GLFrameRenderer(CodecMode.this, glFrameSurface, dm);
		glFrameSurface.setRenderer(mGLFRenderer);
//		mGLFRenderer.update(video_width, video_height);
		mFFmpegTool.Init(video_width, video_height);

//		String dir = LocalConfig.GetMediaPath(UseInfoLocal.getUseInfo().getAccount(), PieInfo.getInstance().getDeviceName(), "yinpin");
//		String path = dir + "pcm";
//		File file = new File(path);
//		try {
//			streamout = new FileOutputStream(file);
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		}

		intent = new Intent();
		intent.setAction(FullPlayActivity.BROADCAST_ACTION);
	}

	@Override
	public void startToPlay() {

//		Log.e(TAG, "startToPlay:" + "mIsRun" + mIsRun + "isRecRunFlag"+ isRecRunFlag + "isDecodeRunFlag" + isDecodeRunFlag);
		error_times = 0;
		decode_times = 0;
		mIsRun = true;
		isRecRunFlag = true;

		if (mIsMonitor) {
			if(video_width!=DEF_VIDEO_WIDTH){
				mGLFRenderer.update(DEF_VIDEO_WIDTH, DEF_VIDEO_HEIGHT);
				mFFmpegTool.Init(DEF_VIDEO_WIDTH,DEF_VIDEO_HEIGHT);
			}
			receivethread = new Thread(new ReceiveThread());
			receivethread.start();// 直播

		} else {// 重播是有声音的
			try {

				audioPlayer = new AudioAACPlayer();
				audioPlayer.setSlience(false);
				audioPlayer.startPlayAudio();
			} catch (Exception e) {
				Log.e(TAG, "音频播放出错"+e.getMessage());

			}
			receiveReplayThread = new Thread(new ReceiveReplayThread());
			receiveReplayThread.start();
		}

		decodeFrameThread = new Thread(new DecodeFrameThread());
		decodeFrameThread.start();

		isDecodeRunFlag = true;
		if (mIsMonitor) {
			drawthread1 = new Thread(new DrawThread());
			drawthread1.start();
			//测试直播音频
//				voicethread = new Thread(new VoiceThread());
//				voicethread.start();
		} else {
			drawthread2 = new Thread(new DrawThread());
			drawthread2.start();
		}
	}

	@Override
	public void stopToPlay() {

		countDecode = 0;
		mIsRun = false;
		isRecRunFlag = false;// 关闭开关
		isDecodeRunFlag = false;// 关闭解码的开关
		mIsPause = false;
		mIsPauseFlag = false;
		mIsStartPlay = false;
		mVideoFrames.clear();
		mFFmpegTool.getDecodeDatas().clear();
		receivethread = null;
		receiveReplayThread = null;
		StringUtils.setSpeed(0);
		StringUtils.setSpeeds("0.0B/S");
		if (audioPlayer != null) {
			audioPlayer.stopPlayAudio();
			audioPlayer = null;
		}
		new Thread(new Runnable() {
			@Override
			public void run() {
				if(mIsMonitor){
					if(!DorideApplication.getInstanse().isToFullFlag()){
						AppsdkUtils.CloseStreamClient(AppsdkUtils.CLIENTKKEY_MONITOR);
					}
				}else{
					AppsdkUtils.CloseStreamClient(AppsdkUtils.CLIENTKKEY_REPLAY);
				}
			}
		}).start();

	}

	@Override
	public void release() {
//		mFFmpegTool.Release();
	}

	@Override
	public void pauseToPlay(BaseParser.ResultCallback pauseCallback) {
		this.pauseCallback = pauseCallback;
		mIsPause = true;
		audioPlayer.pause();

	}

	@Override
	public void playcontinue() {
		mIsPause = false;
		mIsPauseFlag = false;
		pausebitmap = null;
		audioPlayer.continuePlay();
	}

	@Override
	public void setSilence(boolean slience) {
		isSlience = slience;
	}

	@Override
	public void onPlayStart() {

	}

	@Override
	public void onReceiveState(int state) {

	}

	//解码
	public class DecodeFrameThread implements Runnable {

		long time1 = 0;
		long time2 = 0;

		@Override
		public void run() {

			while (mIsRun) {

				if(!mIsMonitor){//走录播的流程
					if(!mIsStartPlay){
						continue;
					}
				}
				if(mIsPauseFlag){
					continue;
				}
				if(mVideoFrames.size()>0){
					try {
						time1 = System.currentTimeMillis();
						//  解码
						byte[] readbuf = mVideoFrames.poll();
						int res = mFFmpegTool.DecodeFrame(readbuf, 0, readbuf.length);
						if(res>0 && countDecode < 1){
							int[] size = new int[2];
							mFFmpegTool.GetSize(size);
							video_width = size[0];
							video_height = size[1];
							Log.e(TAG, "video_width:"+size[0]);
							Log.e(TAG, "video_height:"+size[1]);
							countDecode++;
							mGLFRenderer.update(video_width, video_height);
						}
						time2 = System.currentTimeMillis();
						// 40 是每一帧的间隔时间，一秒25帧
						int sleepTime = 40 - (int)(time2 - time1);
						if( sleepTime > 0){
							SystemClock.sleep(time2 - time1);
						}
					} catch (Exception e) {
						Log.e(TAG, "解码出错"+e.getMessage());
					}
				}
			}
		}

	}
	/**
	 * 接收重播数据的线程
	 *
	 * @author liu
	 */
	public class ReceiveReplayThread implements Runnable {
		int times = 0;
		@Override
		public void run() {

			IFreamDataListener readFListener = new IFreamDataListener() {

				@Override
				public void voiceData(byte[] vcDatas) {
					if (!isSlience) {
						audioPlayer.getAllVoiceData().add(vcDatas);
					}
				}
				@Override
				public void popdata(byte[] bs) {

					if (isRecRunFlag && mIsRun) {
						//TODO  缓存以及等待
						cachePlayData(mVideoFrames.size());
						mVideoFrames.offer(bs);
						Log.e(TAG, "mVideoFrames 长度:"+mVideoFrames.size());
						times++;
						if (!mIsStartPlay) {
							if (times > 3 ||(mVideoFrames.size() > 0 || audioPlayer.getAllVoiceData().size() > 0)) {
								mIsStartPlay = true;
							}
						}
					}
				}
				@Override
				public void errData() {
				}
			};
			if(mIsRun){
				AppsdkUtils.ReadFrame(readFListener);
			}
//				try {
//					AppsdkUtils.CloseStreamClient(AppsdkUtils.CLIENTKKEY_REPLAY);
//				} catch (Exception ex) {
//					Log.i(TAG, "AppsdkUtils.CloseStreamClient(AppsdkUtils.CLIENTKKEY_REPLAY);线程结束了");
//				}
			Log.i(TAG, "线程结束了");
			isRecRunFlag = true;
		}
	}

	/**
	 * @author LIU 接收直播数据线程
	 */
	private class ReceiveThread implements Runnable {

		@Override
		public void run() {

			// 拿到数据解码去
			IFreamDataListener listener = new IFreamDataListener() {

				@Override
				public void popdata(final byte[] readbuf) {
					if (null != readbuf && readbuf.length > 0) {
						if (isRecRunFlag && mIsRun) {
							mVideoFrames.offer(readbuf);
						}
					}
				}

				@Override
				public void errData() {
//					mIsRun = false;
//					playListener.onError();
//					AppsdkUtils.CloseStreamClient(AppsdkUtils.CLIENTKKEY_MONITOR);
				}

				@Override
				public void voiceData(byte[] vcDatas) {
				}
			};
			if(mIsRun){
				AppsdkUtils.ReadMonitorFrame(listener);
			}
		}
	}

//	int speedCount = 0;//计算小于500速度的次数

	/**
	 *
	 * @author 直播和重播的 绘制线程
	 */
	private class DrawThread implements Runnable {

		@Override
		public void run() {
			while (mIsRun) {
				if (!isDecodeRunFlag) {
					continue;
				}
				try {
					List<Datayuv> datas = mFFmpegTool.getDecodeDatas();
					if (datas.size() > 0) {
						error_times = 0;
						Datayuv dyv = datas.remove(0);
						if(dyv == null || dyv.yData.length + dyv.uData.length + dyv.vData.length == 0){
							return;
						}
						//如果暂停，则需要截图最后一桢
						if(isCaptrue || mIsPause){
							//抓拍
							processCapBitmap(dyv);
						}
						if(mIsPause){
							continue;
						}
						mGLFRenderer.update(dyv.yData, dyv.uData, dyv.vData);
//						Log.d(TAG, "绘制时间戳:"+SystemClock.currentThreadTimeMillis());
						if (decode_times != -1) {
							if (decode_times > 10) {
								if (playListener != null) {
									playListener.onReady();
								}
								decode_times = -1;
							} else {
								decode_times++;
							}
						}
					}else{
						SystemClock.sleep(20);
					}
				} catch (Exception ex) {
					error_times++;
					if (decode_times != -1) {
						decode_times = 0;
					}
					if (error_times >= 1 * 100) {
//						playListener.onError();
//						isDecodeRunFlag = false;
						countDecode = 0;
					}
					Log.e(TAG, "播放出错" + ex.getMessage());
				}
			}
		}
	}

	private void processCapBitmap(Datayuv dyv) {
		byte[] yuvData = new byte[dyv.yData.length + dyv.uData.length + dyv.vData.length];
		System.arraycopy(dyv.yData,0,yuvData,0,dyv.yData.length);
		System.arraycopy(dyv.uData,0,yuvData, dyv.yData.length, dyv.uData.length);
		System.arraycopy(dyv.vData,0,yuvData,dyv.yData.length + dyv.uData.length,dyv.vData.length);
		Bitmap bitmap = ImageUtils.convertYuv2Bitmap(yuvData, video_width, video_height);

		if(bitmap != null) {
			if(isCaptrue && !mIsPause){//如果不是暂停抓拍
				mlistener.CaptureOK(bitmap);
				isCaptrue = false;
			}else if(!isCaptrue && mIsPause){//如果只是暂停
				pausebitmap = bitmap;
				mIsPauseFlag = true;
				mhandler.sendEmptyMessage(1);//暂停需再主线程
			}else{
				//do nothing
			}
		}
	}


	private PlayListener playListener;

	private DisplayMetrics dm;

	private Thread receivethread;

	private Thread receiveReplayThread;

	/**
	 * 直播绘制
	 */
	private Thread drawthread1;

	/**
	 * 重播绘制
	 */
	private Thread drawthread2;

	private Thread voicethread;
	/**
	 * 解码线程
	 */
	private Thread decodeFrameThread;

	public void setListener(PlayListener listener) {
		this.playListener = listener;
	}

	@Override
	public void setIsMonitor(boolean isMonitor) {
		mIsMonitor = isMonitor;
	}

	@Override
	public String getLastPlayTime() {
		return DorideApplication.getInstanse().getPlayStringtime();
	}

	private ICaptureListener mlistener;

	private Bitmap pausebitmap;

	BaseParser.ResultCallback pauseCallback;

	@Override
	public void getCapture(ICaptureListener listener) {
		isCaptrue = true;
		mlistener = listener;
		if(pausebitmap != null && mIsPause){
			mlistener.CaptureOK(pausebitmap);
			isCaptrue = false;
		}else if(!mIsPause){
			//异步获取图片//processCapBitmap
		}else if(pausebitmap == null && mIsPause){
			mlistener.CaptureERR();
		}
	}

	public interface ICaptureListener{
		void CaptureOK(Bitmap bitmap);
		void CaptureERR();
	}

	//缓存数据
	private void cachePlayData(int datasize) {
		if (!mIsMonitor) {// 回放
//			// 重播缓存
//			//数据不够等待数据
//			long speed = StringUtils.caculateSpeedLong();
//			if (speed < (140 * 1024)) {//如果传输速度很慢
//				speedCount++;
//			}
//			if(speedCount > 2 && datasize < 10){ //超过2 帧都很慢.，缓存不OK,且缓存数据没有了
//				mIscecheOK = false;
//				intent.putExtra("mIscecheOK", false);
//				CPApplication.getInstance().sendBroadcast(intent);
//			}
//			if(datasize > 150){ //缓存150帧数据
//				speedCount = 0;
//				if(!mIscecheOK){
//				 intent.putExtra("mIscecheOK", true);
//				 CPApplication.getInstance().sendBroadcast(intent);
//				}
//				mIscecheOK = true;
//			}


			//缓存数据
			if(datasize > 600){ //超过缓存600帧数据，暂停发送
				Log.e(TAG, "发送暂停接收数据");
				long result = AppsdkUtils.CPPause(ActionConfig.getSeqNum());
				mhandler.sendEmptyMessageDelayed(0, 9000);
			}
		}
	}

	Handler mhandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(msg.what == 0){//等待缓冲数据
				Log.e(TAG, "发送继续接收数据继续");
				long result = AppsdkUtils.CPContinue(ActionConfig.getSeqNum());
			}else if (msg.what == 1){//回放的暂停命令
				RecorderControl.PausePlayBack(pauseCallback);
			}
		};
	};


}

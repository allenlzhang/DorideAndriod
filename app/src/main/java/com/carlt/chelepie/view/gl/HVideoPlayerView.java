package com.carlt.chelepie.view.gl;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.carlt.chelepie.utils.CodecPlayerUtil;


/**
 * 
 * 播放器
 * 自定义View用于显示接受到的画面
 * 
 * @author liu
 * 
 */
public class HVideoPlayerView extends SurfaceView implements SurfaceHolder.Callback {
	SurfaceHolder holder;// 控制SurfaceView的Holder对象
	public static final String TAG_TIME = "TimeMills";
	public static final String TAG_LIST = "ListSize";
	
	private static String TAG = "HVideoView";
	boolean mIsMonitor = true;
	CodecPlayerUtil mCodecUtil;
	boolean isSlience = false;

	public HVideoPlayerView(Context context) {
		super(context);
		initAll(context);
	}

	public HVideoPlayerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initAll(context);
	}

	public void initAll(Context context) {
		this.holder = this.getHolder();
		holder.addCallback(this);
		Log.e(TAG, "initAll");
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		Log.e(TAG, "surfaceCreated");
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		Log.e(TAG, "surfaceChanged");
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		Log.e(TAG, "surfaceDestroyed");
	}

	/**
	 *  开始播放
	 * @param path
	 * @param handler
	 */
	public void play(String path, Handler handler) {
			mCodecUtil = new CodecPlayerUtil(handler);
			mCodecUtil.initDecoder(holder.getSurface());
			mCodecUtil.play(path);
	}
	
	public void configCrop(){
		mCodecUtil.setHightWidth(1920,1080);
	}
	/**
	 * 跳到相应的播放进度
	 */
	public void playProgress(int progress){

	}

	/**
	 * 暂停播放
	 */

	public void pause() {
		mCodecUtil.pause();
	}

	/**
	 * 继续播放
	 */
	public void continuePlay() {
		mCodecUtil.continuePlay();
	}

	/**
	 * 剪裁
	 */
	public void cutPic() {
		mCodecUtil.getCapture();
	}

	public void stopVideo() {
		mCodecUtil.stopPlay();
	}
	
	
}

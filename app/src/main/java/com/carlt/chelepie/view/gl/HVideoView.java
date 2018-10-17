package com.carlt.chelepie.view.gl;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.carlt.chelepie.utils.CodecUtil;
import com.carlt.chelepie.utils.CodecUtil_2;
import com.carlt.chelepie.utils.PlayListener;
import com.carlt.chelepie.utils.video.CodecMode;
import com.carlt.doride.DorideApplication;
import com.carlt.doride.utils.Log;
import com.carlt.sesame.control.CPControl;


/**
 * 自定义View用于显示接受到的画面
 * 
 * @author Administrator
 * 
 */
public class HVideoView extends SurfaceView implements SurfaceHolder.Callback ,IVideoView{
	SurfaceHolder holder;// 控制SurfaceView的Holder对象
	public static final String TAG_TIME = "TimeMills";
	public static final String TAG_LIST = "ListSize";
	
	private static String TAG = "HVideoView";
	boolean mIsMonitor = true;
	PlayListener mListener;
	CodecUtil mCodecUtil;
	CodecUtil_2 mCodecUtil2;
	boolean isSlience = false;

	@Override
	public void setmListener(PlayListener mListener) {
		this.mListener = mListener;
	}

	public HVideoView(Context context) {
		super(context);
		initAll(context);
	}

	public HVideoView(Context context, AttributeSet attrs) {
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
		DorideApplication.getInstanse().setVideoView(null);
	}

	@Override
	public void play() {
		if (mIsMonitor) {
			mCodecUtil = new CodecUtil();
			mCodecUtil.setListener(mListener);
			mCodecUtil.initDecoder(holder.getSurface());
			mCodecUtil.play();
		} else {
			mCodecUtil2 = new CodecUtil_2();
			mCodecUtil2.setListener(mListener);
			mCodecUtil2.setSilence(isSlience);
			mCodecUtil2.initDecoder(holder.getSurface());
			mCodecUtil2.play();
		}
	}

	@Override
	public void stop() {
		if (mIsMonitor) {
			if (null != mCodecUtil) {
				mCodecUtil.setListener(null);
				mCodecUtil.stopPlay();
				mCodecUtil = null;
			}
		} else {
			if (null != mCodecUtil2) {
				mCodecUtil2.setListener(null);
				mCodecUtil2.stopPlay();
				mCodecUtil2 = null;
			}
		}
	}

	@Override
	public void pause(CPControl.GetResultListCallback pauseCallback) {
		if (!mIsMonitor && null != mCodecUtil2) {
			mCodecUtil2.pausePlay();
		}
	}

	@Override
	public void continuePlay() {
		if (!mIsMonitor && null != mCodecUtil2) {
			mCodecUtil2.continuePlay();
		}
	}

	@Override
	public void setIsMonitor(boolean isMonitor) {
		mIsMonitor = isMonitor;
	}

	@Override
	public void setSilence(boolean silence) {
		if (!mIsMonitor) {
			isSlience = silence;
			if(mCodecUtil2 != null){
				mCodecUtil2.setSilence(isSlience);
			}
		}
	}

	@Override
	public void getCapture(CodecMode.ICaptureListener listener) {
		if (mCodecUtil2 != null) {
			 mCodecUtil2.getCapture(listener);
		}
	}
	
	@Override
	public String getLastPlayTime() {
		if (mCodecUtil2 != null) {
			return mCodecUtil2.getLastPlayTime();
		}
		return null;
	}

	@Override
	public void setProgressPercentage(int rage) {
		
	}

	@Override
	public void onReadyplay() {
		
	}

	@Override
	public void onPlayerror() {
		
	}

}

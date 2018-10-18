package com.carlt.chelepie.view.gl;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceHolder;

import com.carlt.chelepie.utils.PlayListener;
import com.carlt.chelepie.utils.video.CodecMode;
import com.carlt.chelepie.utils.video.CodecVideoPresenter;
import com.carlt.doride.DorideApplication;
import com.carlt.doride.protocolparser.BaseParser;
import com.carlt.doride.utils.Log;
import com.carlt.sesame.control.CPControl;


/**
 * 自定义View用于显示接受到的画面
 * 
 * @author Administrator
 * com.linewin.chelepie.ui.view.gl.HHVideoView
 */
public class HHVideoView extends GLFrameSurface implements ISimplePlayer,IVideoView {
	
	private String TAG = "HHVideoView";

	/**
	 * 监听直播成功还是失败
	 */
	private PlayListener mListener;
	
	/**
	 * 编解码业务类
	 */
	private CodecVideoPresenter videoPresenter;

	/**
	 * 是否是直播
	 */
	private boolean mIsMonitor = true;

	private boolean isSlience;
	
	@Override
	public void setmListener(PlayListener mListener) {
		this.mListener = mListener;
	}

	public HHVideoView(Context context) {
		super(context);
		init();
	}

	public HHVideoView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		super.surfaceDestroyed(holder);
		Log.e(TAG, "surfaceDestroyed");
		DorideApplication.getInstanse().setVideoView(null);
	}
	
	private void init() {
		Log.e(TAG, "surfaceinit");
		videoPresenter = new CodecVideoPresenter(this,this);
	}
	/**
	 * 对外的播放方法
	 */
	@Override
	public void play() {
		videoPresenter.startPlay();
	}

	@Override
	public void stop() {
		videoPresenter.stopPlay();
	}

	@Override
	public void pause(BaseParser.ResultCallback pauseCallback) {
		if (null != videoPresenter) {
			videoPresenter.pausePlay(pauseCallback);
		}
	}

	@Override
	public void continuePlay() {
		if (null != videoPresenter) {
			videoPresenter.continuePlay();
		}
	}

	@Override
	public void setIsMonitor(boolean isMonitor) {
		mIsMonitor = isMonitor;
		videoPresenter.setIsMonitor(isMonitor);
	}

	/**
	 * 是否是静音,true 表示静音
	 */
	@Override
	public void setSilence(boolean silence) {
		if (!mIsMonitor) {
			isSlience = silence;
			if(videoPresenter != null){
				videoPresenter.setSilence(isSlience);
			}
		}
	}

	@Override
	public void getCapture(CodecMode.ICaptureListener listener) {
		if (videoPresenter != null) {
			 videoPresenter.getCapture(listener);
		}
	}

	@Override
	public String getLastPlayTime() {
		if (videoPresenter != null) {
			return videoPresenter.getLastPlayTime();
		}
		return null;
	}

	@Override
	public void onPlayStart() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onReceiveState(int state) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setProgressPercentage(int rage) {
		// TODO Auto-generated method stub
		
	}

	//callback
	@Override
	public void onReadyplay() {
		mListener.onReady();
	}
	//callback
	@Override
	public void onPlayerror() {
		mListener.onError();
	}

}

package com.carlt.chelepie.view.gl;


import com.carlt.chelepie.utils.PlayListener;
import com.carlt.chelepie.utils.video.CodecMode;
import com.carlt.sesame.control.CPControl;

/**
 * @author Administrator
 * 播放显示器模型
 */
public interface IVideoView {
	
	/**
	 * 播放
	 */
	void play() ;
	
	/**
	 * 停止
	 */
	void stop() ;
	
	/**
	 * 暂停
	 * @param pauseCallback 
	 */
	void pause(CPControl.GetResultListCallback pauseCallback) ;
	
	/**
	 * 继续播放
	 */
	void continuePlay() ;
	
	/**
	 * 设置静音,true 表示静音， false 表示 取消静音
	 */
	void setSilence(boolean flag) ;
	
	/**
	 * 设置是是回放还是直播，true 表示 直播，false 表示 回放
	 */
	void setIsMonitor(boolean flag) ;
	
	/**
	 *  设置进度
	 * @param rage
	 */
	void setProgressPercentage(int rage);
	
	/**
	 * 截屏
	 * @return
	 */
	void getCapture(CodecMode.ICaptureListener listener);
	
	/**
	 * 解码渲染ok播放准备好
	 */
	void onReadyplay();
	
	/**
	 * 播放出错了
	 */
 	void onPlayerror();
 	
 	String getLastPlayTime();
 	
 	void setmListener(PlayListener mListener);

}

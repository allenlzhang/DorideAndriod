package com.carlt.chelepie.utils.video;


import com.carlt.sesame.control.CPControl;

/**
 * 
 * @author LIU
 *
 * 控制视频播放以及解码的接口
 */
public interface ICodecMode {
	
	/**
	 * 开始播放
	 */
	void startToPlay();
	
	/**
	 * 停止播放
	 */
	void stopToPlay();
	/**
	 * 释放资源
	 */
	void release();
	
	/**
	 * 暂停播放
	 * @param pauseCallback 
	 */
	void pauseToPlay(CPControl.GetResultListCallback pauseCallback);
	
	/**
	 * 继续播放
	 */
	void playcontinue();

	/**
	 * 设置为静音
	 * @param isSlience
	 */
	void setSilence(boolean isSlience);

	/**
	 * 设置是否为直播
	 * @param isMonitor
	 */
	void setIsMonitor(boolean isMonitor);

	/**
	 *  获取抓拍
	 * @return
	 */
	void getCapture(CodecMode.ICaptureListener listener);

	String getLastPlayTime();


}

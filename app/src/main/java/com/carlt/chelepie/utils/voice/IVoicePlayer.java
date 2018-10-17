package com.carlt.chelepie.utils.voice;

import java.util.List;


/**
 * 音频播放器接口
 * @author liu
 *
 */
public interface IVoicePlayer {
	
	
	/**
	 * 初始化音频播放器并开始播放
	 */
	public void startPlayAudio();
	
	/**
	 * 终止播放
	 */
	public void stopPlayAudio();
	/**
	 * 暂停
	 */
	public void pause();
	/**
	 * 继续
	 */
	public void continuePlay();
	/**
	 * 是否静音 true 表示静音， false 表示有声音
	 */
	public void setSlience(boolean flag);
	/**
	 * 得到音频数据对象
	 * @return
	 */
	public List<byte[]> getAllVoiceData();

}

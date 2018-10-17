package com.carlt.chelepie.utils.voice;

import android.os.SystemClock;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 
 * 音频播放工具
 * @author liu
 *
 */
public class AudioAACPlayer implements IVoicePlayer {
	
	/**
	 * 音频数据
	 */
	public List<byte[]> mAudioFrames = Collections.synchronizedList(new ArrayList<byte[]>());
	
	private String TAG = "AudioAACPlayer";
	
	boolean mIsRun = true;
	
	boolean isSlience = true;

	private AACDecoder decoder;

	private Thread voicethread;
	
	   //根据帧率获取的解码每帧需要休眠的时间,根据实际帧率进行操作
    private int PRE_FRAME_TIME = 1000 / 25;
	//是否处于播放中
    private boolean isPlayingVoice = false;
	public AudioAACPlayer() {
	}
	
	@Override
	public void startPlayAudio() {
		
		isPlayingVoice = true;
		mIsRun = true;
		if(voicethread == null){
			voicethread = new Thread(new VoiceRunnable());
		}
		
		if(!voicethread.isAlive()){
			voicethread.start();
		}
		if(decoder == null){
			decoder = new AACDecoder();
		}
		decoder.start();
	}
	
	@Override
	public void stopPlayAudio(){
		mIsRun = false;
		if(null != decoder){
			decoder.stop();
		}
		mAudioFrames.clear();
		voicethread = null;
		decoder = null;
		
	}
	
	private void playAudio(byte[] frames, int i, int length) {
		if(isSlience){
			return;
		}
		long startTime = System.currentTimeMillis();
		decoder.decode(frames, i, length);
		sleepThread(startTime, System.currentTimeMillis());
	}
	
    /**
     *  计算每一帧休眠时间修眠
     * @param startTime
     * @param endTime
     */
    private void sleepThread(long startTime, long endTime) {
        //根据读文件和解码耗时，计算需要休眠的时间
        long time = PRE_FRAME_TIME - (endTime - startTime);
        if (time > 0) {
            try {
                Thread.sleep(time);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
	
	/**
	 * 处理声音的线程
	 */
	private class VoiceRunnable implements Runnable {

		@Override
		public void run() {
			while (mIsRun) {
				
				if (mAudioFrames.size() < 1 || !isPlayingVoice) {
					SystemClock.sleep(2);
					continue;
				}
				byte[] frames = null;
				if(null != mAudioFrames && !mAudioFrames.isEmpty() && mAudioFrames.size()>0){
					try {
						frames = mAudioFrames.remove(0);
					} catch (Exception e) {
						Log.d(TAG, "decodeThread err ! ! !");
					}
				}else{
					continue;
				}
				playAudio(frames, 0, frames.length);
			}
			Log.d(TAG, "decodeThread over ! ! !");
		}
	}

	@Override
	public void pause() {
		isPlayingVoice = false;
	}

	@Override
	public void continuePlay() {
		isPlayingVoice = true;
	}

	@Override
	public void setSlience(boolean flag) {
		isSlience = flag;
	}

	@Override
	public List<byte[]> getAllVoiceData() {
		return mAudioFrames;
	}

}

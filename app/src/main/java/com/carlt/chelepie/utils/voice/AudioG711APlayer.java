package com.carlt.chelepie.utils.voice;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.SystemClock;
import android.util.Log;

import com.hz17car.chelepie.utility.AudioDecode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 
 * 音频播放工具
 * @author liu
 *
 */
public class AudioG711APlayer implements IVoicePlayer{
	
	/**
	 * 音频数据
	 */
	public List<byte[]> mAudioFrames = Collections.synchronizedList(new ArrayList<byte[]>());
	
	private final static String AUDIO_TYPE = "audio/g711-alaw"; // G711A 采样率 8k

	private String TAG = "AudioG711APlayer";
	
	boolean mIsRun = true;
	
	/**
	 * 是否是静音
	 */
	boolean isSlience = false;

	public final static int minSize = AudioTrack.getMinBufferSize(8192, 2,
			AudioFormat.ENCODING_PCM_16BIT);

	private AudioTrack audioTrack = null;
	

	private Thread voicethread;
		
	   //根据帧率获取的解码每帧需要休眠的时间,根据实际帧率进行操作
    private int PRE_FRAME_TIME = 1000 / 30;
	//是否处于播放中
    private boolean isPlayingVoice = false;
	public AudioG711APlayer() {
	}
	
	@Override
	public void startPlayAudio() {
		
		isPlayingVoice = true;
		mIsRun = true;
		
		try {
			audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, 8000, 1,
			AudioFormat.ENCODING_PCM_16BIT, minSize * 2,
			AudioTrack.MODE_STREAM);
			audioTrack.setStereoVolume(1.0f, 1.0f);
			audioTrack.play();
		} catch (Exception e) {
			Log.e(TAG, "音频播放出错"+e.getMessage());
			
		}
		
		if(voicethread == null){
			voicethread = new Thread(new VoiceRunnable());
		}
		voicethread.start();
	}
	
	@Override
	public void stopPlayAudio(){
		mIsRun = false;
		mAudioFrames.clear();
		voicethread = null;
		audioTrack = null;
		
	}
	
	private void playAudio(byte[] frames, int moffset, int length) {
		// Log.d("Media", "onFrame Thread:.....音频数据。。。。");
		if(isSlience){
			return;
		}
		byte[] outs = new byte[minSize * 2];
		int len1 = -1;
		try {
			len1 = AudioDecode.DecodeAudio(frames, moffset, length, outs);
		} catch (Exception e) {
//			Log.d(TAG, "音频数据。。。。len1"+len1);
		}
		// 解码成功
//		 Log.d(TAG, "音频数据。。。。len1"+len1);
		if (len1 > 0) {
			
			audioTrack.write(outs, 0, len1);
		}
	
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

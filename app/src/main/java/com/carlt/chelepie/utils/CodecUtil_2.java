package com.carlt.chelepie.utils;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.media.MediaCodec;
import android.media.MediaFormat;
import android.util.Log;
import android.view.Surface;

import com.carlt.chelepie.appsdk.AppsdkUtils;
import com.carlt.chelepie.appsdk.IFreamDataListener;
import com.carlt.chelepie.utils.video.CodecMode;
import com.carlt.chelepie.utils.voice.AudioAACPlayer;
import com.carlt.chelepie.utils.voice.IVoicePlayer;
import com.hz17car.chelepie.utility.AVIConverter;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author @Y.yun
 * 回放
 *         MediaCodecC Util
 */
public class CodecUtil_2 {
	private final static String MIME_TYPE = "video/avc"; // H.264 Advanced Video
	private static int VIDEO_WIDTH = 1920;
	private static int VIDEO_HEIGHT = 1080;
	private static int TIME_INTERNAL = 40;
	private static int FRAMERATE = 25;
	public final String TAG = "DEBUG||" + this.getClass().getSimpleName();

	private List<byte[]> mVideoFrames = Collections.synchronizedList(new ArrayList<byte[]>());
	private int mCount;
	private boolean mIsRun = true;
	private boolean mIsPause = false;
	private boolean mIsRecRun = false;
	private boolean mIsDecodeRun = false;
	/**
	 *  是否可以播放了 等到视频和音频都有的时候再开始播放保证同步
	 */
	private boolean mIsStartPlay = false;
	private MediaFormat mediaFormat;
	private Surface mSurface;
	MediaCodec videoCodec;
	PlayListener mListener;
	int width = 0;
	int height = 0;
	int decode_times = 0;
	int error_times = 0;
	Bitmap bitmap = null;
	AVIConverter mConverter = new AVIConverter();
	byte[] bitMapBuf = null;
	String lastTime;
	
	/**
	 * 解码线程
	 * 
	 * @author Administrator
	 * 
	 */
	private Thread videoThread = null;
	
	private Thread receiveThread = null;

	private int interval = TIME_INTERNAL;
	
	private IVoicePlayer voicePlayer ;

	/**
	 * 
	 */
	public CodecUtil_2() {
		mConverter.H264Init();
		voicePlayer = new AudioAACPlayer();
	}

	public void initDecoder(Surface ms) {
		mSurface = ms;

		if (videoCodec == null) {
			try {
				videoCodec = MediaCodec.createDecoderByType(MIME_TYPE);
			} catch (IOException e) {
				e.printStackTrace();
			}
			mediaFormat = MediaFormat.createVideoFormat(MIME_TYPE, VIDEO_WIDTH, VIDEO_HEIGHT);
		}

	}

	/**
	 * @param buf
	 * @param offset
	 * @param length
	 * @return 解码视频
	 */
	private boolean onFrame(byte[] buf, int offset, int length) {
		if (mIsPause || !mIsRun) {
			return true;
		}

		if (buf[1] == 0) {
			bitMapBuf = buf;
		}

		try {
			ByteBuffer[] inputBuffers = videoCodec.getInputBuffers();
			int inputBufferIndex = videoCodec.dequeueInputBuffer(0);
			if (inputBufferIndex >= 0) {
				ByteBuffer inputBuffer = inputBuffers[inputBufferIndex];
				inputBuffer.clear();
				inputBuffer.put(buf, offset, length);
				videoCodec.queueInputBuffer(inputBufferIndex, 0, length, mCount * interval, MediaCodec.BUFFER_FLAG_SYNC_FRAME);
				mCount++;
			} else {
				return false;
			}

			MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
			int outputBufferIndex = videoCodec.dequeueOutputBuffer(bufferInfo, 100);
			while (outputBufferIndex >= 0) {
				error_times = 0;
				if (decode_times != -1) {
					if (decode_times > 25) {
						if(mListener != null){
							mListener.onReady();
						}
						decode_times = -1;
					} else {
						decode_times++;
					}
				}
				videoCodec.releaseOutputBuffer(outputBufferIndex, true);
				outputBufferIndex = videoCodec.dequeueOutputBuffer(bufferInfo, 0);
				Log.e(TAG, "===========准备获取图片=======");
			}
		} catch (Exception ex) {
			// 状态出错
			error_times++;
			if (decode_times != -1) {
				decode_times = 0;
			}
			if (error_times >= 25 * 6) {
				if(mListener != null){
					mListener.onError();
				}
			}
			release();
			configNewCodec();
			return false;
		}
		return true;
	}

	public void play() {
		mIsPause = false;
		mIsRun = true;
		if (videoCodec == null) {
			configNewCodec();
		}


		if (!mIsRecRun) {
			mIsRecRun = true;
			receiveThread = new ReceiveThread();
			receiveThread.start();
		}

		if (!mIsDecodeRun) {
			mIsDecodeRun = true;
			videoThread = new DecodeVideoThread();
			videoThread.start();
			voicePlayer.startPlayAudio();
		}
	}

	public void release() {
		try {
			mediaFormat = null;
			videoCodec.release();
		} catch (Exception e) {
			e.printStackTrace();
		}

		videoCodec = null;
	}

	public void configNewCodec() {
		try {
			mediaFormat = MediaFormat.createVideoFormat(MIME_TYPE, VIDEO_WIDTH, VIDEO_HEIGHT);
			TIME_INTERNAL = 1000 / FRAMERATE;
			interval = TIME_INTERNAL;
			videoCodec = MediaCodec.createDecoderByType(MIME_TYPE);
			videoCodec.configure(mediaFormat, mSurface, null, 0);
			videoCodec.start();
		} catch (Exception ex) {
			Log.e(TAG, "VideoCodec配置出错!!!" + ex.getMessage());
		}
	}

	public boolean isInitDecoder() {
		return videoCodec == null;
	}

	/**
	 * 停止播放
	 */
	public void stopPlay() {
		mIsRun = false;
		mIsRecRun = false;
		mIsDecodeRun = false;
		mIsStartPlay = false;
		bitMapBuf = null;
		mVideoFrames.clear();
		voicePlayer.stopPlayAudio();
		new Thread(new Runnable() {

			@Override
			public void run() {
				release();
				try {
					AppsdkUtils.CloseStreamClient(AppsdkUtils.CLIENTKKEY_MONITOR);
					AppsdkUtils.CloseStreamClient(AppsdkUtils.CLIENTKKEY_REPLAY);
				} catch (Exception ex) {
				}
			}
		}).start();
	}

	public class DecodeVideoThread extends Thread {
		public void run() {
			mIsDecodeRun = true;
			while (mIsRun) {
				if (!mIsStartPlay) {
					continue;
				}

				if (mVideoFrames.size() < 1) {
					try {
						Thread.sleep(1);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					continue;
				}

				byte[] frames = mVideoFrames.remove(0);
				
				try {
					onFrame(frames, 0, frames.length);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}

			mIsDecodeRun = false;
			Log.d(TAG, "decodeThread over ! ! !");
		};
	};


	public class ReceiveThread extends Thread {
		int times = 0;
		public void run() {
			mIsRecRun = true;
			IFreamDataListener readFListener = new IFreamDataListener() {
				
				@Override
				public void voiceData(byte[] vcDatas) {
					voicePlayer.getAllVoiceData().add(vcDatas);
				}
				
				@Override
				public void popdata(byte[] bs) {
					times++;
					mVideoFrames.add(bs);
					//声像同步
					if (!mIsStartPlay) {
						if (times > 3 || (mVideoFrames.size() > 0 && voicePlayer.getAllVoiceData().size() > 0)) {
							mIsStartPlay = true;
						}
					}
				}
				
				@Override
				public void errData() {
					mIsRun = false;
					if (mListener != null) {
						mListener.onError();
					}
					mIsRecRun = false;
					try {
						AppsdkUtils.CloseStreamClient(AppsdkUtils.CLIENTKKEY_MONITOR);
						AppsdkUtils.CloseStreamClient(AppsdkUtils.CLIENTKKEY_REPLAY);
					} catch (Exception ex) {
					}
				}
			};
			if(mIsRun){
				AppsdkUtils.ReadFrame(readFListener);
			}
			try {
				AppsdkUtils.CloseStreamClient(AppsdkUtils.CLIENTKKEY_MONITOR);
				AppsdkUtils.CloseStreamClient(AppsdkUtils.CLIENTKKEY_REPLAY);
			} catch (Exception ex) {
			}

			Log.i(TAG, "线程结束了");
			mIsRecRun = false;
		}
	}

	/**
	 * @param mListener
	 */
	public void setListener(PlayListener mListener) {
		this.mListener = mListener;
	}

	/**
	 * 暂停
	 */
	public void pausePlay() {
		mIsPause = true;
	}

	public void continuePlay() {
		mIsPause = false;
	}

	/**
	 * 设置静音
	 */
	public void setSilence(boolean sile) {
		voicePlayer.setSlience(sile);
	}

	public void getCapture(CodecMode.ICaptureListener listener) {
		if (bitMapBuf != null) {
			byte[] bs = new byte[1920 * 1080 * 2 + 1024];
			int offset = 0;
			int length = 0;
			int flag = bitMapBuf[1];
			if (flag == 0) {
				offset = 18;
			} else if (flag == 1) {
				offset = 10;
			}

			length = bitMapBuf.length - offset;
			int len = mConverter.H264Decode(bitMapBuf, offset, length, bs);
			if (len > 0) {
				ByteBuffer bbf = ByteBuffer.wrap(bs, 0, len);
				if(width <=0 || height <= 0){
					width = VIDEO_WIDTH;
					height = VIDEO_HEIGHT;
				}
				Bitmap bitmap = Bitmap.createBitmap(width, height, Config.RGB_565);
				bitmap.copyPixelsFromBuffer(bbf);
//				return bitmap;
			}
		}

//		return null;
	}

	/**
	 * @return
	 */
	public String getLastPlayTime() {
		return lastTime;
	}

	public String add0(int y) {
		return y < 10 ? ("0" + y) : ("" + y);
	}

}

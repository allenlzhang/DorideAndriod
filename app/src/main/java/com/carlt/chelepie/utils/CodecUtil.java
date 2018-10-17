package com.carlt.chelepie.utils;

import android.media.MediaCodec;
import android.media.MediaFormat;
import android.view.Surface;


import com.carlt.chelepie.appsdk.AppsdkUtils;
import com.carlt.chelepie.appsdk.IFreamDataListener;
import com.carlt.doride.DorideApplication;
import com.carlt.doride.utils.Log;
import com.carlt.doride.utils.StringUtils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author @Y.yun
 * 
 * 直播
 *         MediaCodecC Util
 */
public class CodecUtil {
	private static String MIME_TYPE = "video/avc"; // H.264 Advanced Video
	private static int VIDEO_WIDTH = 1280;
	private static int VIDEO_HEIGHT = 720;
	private static int TIME_INTERNAL_MONITOR = 37;
	private static int FRAMERATE = 27;
	public final String TAG = "DEBUG||" + this.getClass().getSimpleName();
	List<byte[]> mFrames = Collections.synchronizedList(new ArrayList<byte[]>());
	PlayListener listener;

	public PlayListener getListener() {
		return listener;
	}

	public void setListener(PlayListener listener) {
		this.listener = listener;
	}

	int mCount;
	int aCount;
	int decode_times = 0;
	int error_times = 0;
	boolean mIsRun = true;
	boolean mIsRecRun = false;
	boolean mIsDecodeRun = false;
	boolean isShow = true;

	MediaCodec videoCodec;
	Surface mSurface;

	private MediaFormat mediaFormat;

	public void setMode(int mode) {
		interval = TIME_INTERNAL_MONITOR;
	}

	private int interval = TIME_INTERNAL_MONITOR;

	/**
	 * 
	 */
	public CodecUtil() {
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
		if (!mIsRun) {
			return true;
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
		} catch (IllegalStateException ex) {
			// 状态出错
			release();
			configNewCodec();
			return false;
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		try {
			MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
			int outputBufferIndex = videoCodec.dequeueOutputBuffer(bufferInfo, 100);
			while (outputBufferIndex >= 0) {
				error_times = 0;
				if (decode_times != -1) {
					if (decode_times > 15) {
						listener.onReady();
						decode_times = -1;
					} else {
						decode_times++;
					}
				}
				videoCodec.releaseOutputBuffer(outputBufferIndex, true);
				outputBufferIndex = videoCodec.dequeueOutputBuffer(bufferInfo, 0);
			}
		} catch (Exception ex) {
			error_times++;
			if (decode_times != -1) {
				decode_times = 0;
			}
			if (error_times >= 25 * 6) {
				listener.onError();
			}
			Log.e(TAG, "播放已停止" + ex.getMessage());
			release();
			configNewCodec();
		}

		return true;
	}

	public void play() {
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
			decodeThread = new DecodeThread();
			decodeThread.start();
		}
	}

	/**
	 * 
	 */
	public void configNewCodec() {
		try {
			mediaFormat = MediaFormat.createVideoFormat(MIME_TYPE, VIDEO_WIDTH, VIDEO_HEIGHT);
			TIME_INTERNAL_MONITOR = 1000 / FRAMERATE;
			interval = TIME_INTERNAL_MONITOR;
			videoCodec = MediaCodec.createDecoderByType(MIME_TYPE);
			videoCodec.configure(mediaFormat, mSurface, null, 0);
			videoCodec.start();
		} catch (Exception ex) {
			Log.e(TAG, "VideoCodec配置出错!!!" + ex.getMessage());
		}
	}

	public void release() {
		try {
			if (videoCodec != null) {
				mediaFormat = null;
				// videoCodec.stop();
				videoCodec.release();
				System.gc();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		videoCodec = null;
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
		mFrames.clear();
		StringUtils.setSpeed(0);
		StringUtils.setSpeeds("0.0B/S");
		
		new Thread(new Runnable() {
			@Override
			public void run() {
					if(!DorideApplication.getInstanse().isToFullFlag()){
						AppsdkUtils.CloseStreamClient(AppsdkUtils.CLIENTKKEY_MONITOR);
					}
			}
		}).start();
		
	}

	/**
	 * 解码线程
	 * 
	 * @author Administrator
	 * 
	 */
	private Thread decodeThread = null;
	private Thread receiveThread = null;

	private class DecodeThread extends Thread {
		public void run() {
			mIsDecodeRun = true;
			while (mIsRun) {
				if (mFrames.size() < 1) {
					try {
						Thread.sleep(1);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					continue;
				}

				byte[] frames = mFrames.remove(0);
				
				try {
					if (isShow) {
						onFrame(frames,0, frames.length);
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}

			mIsDecodeRun = false;
		}
	}

	public class ReceiveThread extends Thread {
		public void run() {
			mIsRecRun = true;
			try {
				// 拿到数据解码去
				IFreamDataListener iFreamDatalistener = new IFreamDataListener() {
					@Override
					public void popdata(final byte[] readbuf) {
						if (null != readbuf && readbuf.length > 0) {
							if (mIsRun) {
								mFrames.add(readbuf);
							}
						}
					}

					@Override
					public void errData() {
//						if (listener != null) {
//							listener.onError();
//						}
//						AppsdkUtils.CloseStreamClient(AppsdkUtils.CLIENTKKEY_MONITOR);
//						mIsRecRun = false;
					}

					@Override
					public void voiceData(byte[] vcDatas) {
						
					}
				};
				//启动获取数据
				if(mIsRun){
					AppsdkUtils.ReadMonitorFrame(iFreamDatalistener);
				}
	
			} catch (Exception e) {
				if (listener != null) {
					listener.onError();
				}
				AppsdkUtils.CloseStreamClient(AppsdkUtils.CLIENTKKEY_MONITOR);
				mIsRecRun = false;
			}
		}
	};

	public String add0(int y) {
		return y < 10 ? ("0" + y) : ("" + y);
	}

}

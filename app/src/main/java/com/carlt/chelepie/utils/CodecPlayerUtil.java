package com.carlt.chelepie.utils;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.media.MediaCodec;
import android.media.MediaFormat;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.Surface;

import com.carlt.chelepie.appsdk.CapBufferData;
import com.carlt.chelepie.utils.voice.AudioAACPlayer;
import com.carlt.chelepie.utils.voice.IVoicePlayer;
import com.carlt.sesame.utility.MyTimeUtil;
import com.hz17car.chelepie.utility.AVIConverter;


import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

import static java.lang.Thread.sleep;

/**
 * @author @Y.yun
 * 
 * 直播
 *         MediaCodecC Util
 */
public class CodecPlayerUtil {
	private static String MIME_TYPE = "video/avc"; // H.264 Advanced Video
	private static int VIDEO_WIDTH = 720;
	private static int VIDEO_HEIGHT = 576;
	private static int TIME_INTERNAL_MONITOR = 36;
	public final static int FRAMERATE = 28;
	private  final String TAG = this.getClass().getSimpleName();
	private int PRE_FRAME_TIME = 1000 / FRAMERATE;
	private int video_width = VIDEO_WIDTH;
	private int video_height = VIDEO_HEIGHT;
	
	private int width = 0;
	private int height = 0;
	
	
	public  void setHightWidth(int w,int h){
		width = w;
		height = h;
	}
	
	private int mCount;
	private int decode_times = 0;
	private int error_times = 0;

	private MediaCodec videoCodec;
	private Surface mSurface;

	private MediaFormat mediaFormat;

	public void setMode(int mode) {
		interval = TIME_INTERNAL_MONITOR;
	}

	private int interval = TIME_INTERNAL_MONITOR;
	
	private Handler mHandler;
	
	//最后一帧时间戳
	private long parseLastTime;
	//第一帧时间戳
	private long parseFirstTime = 0;
	
	//总时长
	private String timeAll;
	
	private AVIConverter mConverter = new AVIConverter();
	
	private int frameTotal = 0;
	// 栈
	private Stack<byte[]> stack = new Stack<byte[]>();
	
	
	/**
	 * 到达视频结尾
	 */
	private boolean isEnding = false;
	
	//处于抓拍中
	private boolean isCaptureing = false;
	/**
	 * 标记是否渲染中
	 */
	private boolean isFrameRun = false;
	
	/**
	 * 是否暂停
	 */
	private boolean isPause = false;
	
	private FileInputStream inputStream;
	
	private String playPath;
	
	private IVoicePlayer audioPlayer;
	
	//用于产生截图的缓存
	private CapBufferData capBufferData = null;

	public CodecPlayerUtil(Handler handler) {
		
		mHandler = handler;
		
		capBufferData = new CapBufferData();
	}

	
	@SuppressLint("NewApi")
	public void initDecoder(Surface ms) {
		mSurface = ms;
		mConverter.H264Init();
	}

	/**
	 * @param buf
	 * @param offset
	 * @param length
	 * @return 解码视频
	 */
	@SuppressLint("NewApi")
	public boolean onFrame(byte[] buf, int offset, int length) {
		if(!isCaptureing){
			capBufferData.add(buf);
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
			Log.e(TAG, "VideoCodec配置出错!!! onFrame" + ex.getMessage());
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
					if (decode_times > 5) {
//						listener.onReady();
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
//				listener.onError();
			}
			Log.e(TAG, "播放已停止" + ex.getMessage());
			release();
			configNewCodec();
		}

		return true;
	}
	

	/**
	 *  播放开始
	 * @param path
	 */
	public void play(String path) {
		
		this.playPath = path;
		
		//初始化播放器，为了解决第一次播放，会有绿屏现象
		initFrameDecod();
		
	}


	//正式开始播放
	private void playNow(final String path ,final int seek) {
		Log.e("CodecPlayerUtil", "playNow:=================== " + path );
		isEnding = false;
		if (videoCodec == null) {
			configNewCodec();
		}
		isFrameRun = true;
		isPause = false;
		audioPlayer = new AudioAACPlayer();
		audioPlayer.startPlayAudio();
		audioPlayer.setSlience(false);

		new AsyncTask<Void, Void, Void>(){
			@Override
			protected Void doInBackground(Void... params) {
				FileInputStream input = null;
				try {
					if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
						FileInputStream input2 = new FileInputStream(path);
						parseLastTime = parseLastTime(input2);
						//播放
						input = new FileInputStream(path);
						parseMain(input);
					}

				} catch (IOException e) {
					e.printStackTrace();
					Log.e(TAG, "ERR"+e.getMessage());
				}
				finally{
					if(input != null){
						try {
							input.close();
						} catch (IOException e) {
							e.printStackTrace();
							Log.e(TAG, "ERR"+e.getMessage());
						}
					}
				}

				return null;
			}

		}.execute();
	}

	private void playNow(final  String path){
		playNow(path,20);
	}
	

	/**
	 *		最后一帧时间戳 
	 */
	private long parseLastTime(FileInputStream inputStream){
		long times = 0;
		long len = 1024 * 400;
		long size = 0;
		try {
			size = inputStream.getChannel().size();
			if(size > len){
				inputStream.skip(size - len);
			}
			byte[] flag = new byte[4];
			while (inputStream.read(flag) > 0) {
				switch (ByteBuffer.wrap(flag).order(ByteOrder.BIG_ENDIAN).getInt()) {
				case 0x000001ba:
					inputStream.skip(9);// 72 bits
					inputStream.skip(inputStream.read() & 7); // 8bits
					break;
				case 0x000001bc:
					int pravitedatalength = (inputStream.read() << 8 | inputStream.read());
					byte[] buf = new byte[pravitedatalength];
					inputStream.read(buf, 0, pravitedatalength);
					byte[] timeRange = Arrays.copyOfRange(buf, 10, 15);
					times = MyTimeUtil.paserFramData2(timeRange);//当前时间
					Log.e(TAG, "playtime last:"+times);
					break;
				default:
					break;
					}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return times;
	}

	 private void parseMain(FileInputStream input) {
		inputStream = input;
		try {
			int totalLenth = inputStream.available();
			int process = 0;
			Log.e(TAG,"开始时间："+ System.currentTimeMillis());

			byte[] flag = new byte[4];
			byte[] h264Head = new byte[5];

			int uFlag = 0, pesHLen = 0, pesHExtLen;
				while (isFrameRun) {
					if(isPause){
						SystemClock.sleep(100);
						continue;
					}
					if(inputStream.read(flag) <= 0){
						break;
					}
//					SystemClock.sleep(14);
					long timeMillis1 = System.currentTimeMillis();
					switch (uFlag = ByteBuffer.wrap(flag).order(ByteOrder.BIG_ENDIAN).getInt()) {
					case 0x000001ba: //442
						inputStream.skip(9);// 72 bits
						inputStream.skip(inputStream.read() & 7); // 8bits
						break;
					case 0x000001bc: //444
//						inputStream.skip(inputStream.read() << 8 | inputStream.read());
						int pravitedatalength = (inputStream.read() << 8 | inputStream.read());
						byte[] buf = new byte[pravitedatalength];
						inputStream.read(buf, 0, pravitedatalength);
						byte[] timeRange = Arrays.copyOfRange(buf, 10, 15);
//						Log.e("PSM", Arrays.toString(timeRange));
						long times = MyTimeUtil.paserFramData2(timeRange);//当前时间
						
						if(parseFirstTime == 0){
							parseFirstTime = times;//第一帧时间戳
						}
						
						//142byte 是一毫秒
						if(parseLastTime == 0){
							timeAll = MyTimeUtil.formartTime1((int)(totalLenth / 142));
						}else{
							timeAll = MyTimeUtil.formartTime1(parseLastTime - parseFirstTime);
						}
						//总时间
						
						String timePlay = MyTimeUtil.formartTime1(times - parseFirstTime);
						//时间
						Message msg = new Message();
						msg.what = TYPE_TIME_STRING;
						msg.obj = timePlay + "/" + timeAll;
						mHandler.sendMessage(msg);
						
						break;
					default:
						pesHLen = inputStream.read() << 8 | inputStream.read();
						inputStream.skip(2);
						inputStream.skip(pesHExtLen = inputStream.read()); // 跳过扩展长度

						if ((uFlag & 0xf0) == 0xe0) {// 视频 uFlag = 480 ,0xf0 == 224  0xe0 = true

							if (pesHLen - 3 - pesHExtLen > 0) {
								inputStream.read(h264Head);
								int length = pesHLen - 3 - pesHExtLen - 5;
								byte[] h264data = new byte[length + 5];
								byte[] h264body = new byte[length];
								inputStream.read(h264body);
								System.arraycopy(h264Head, 0, h264data, 0,
										h264Head.length);
								System.arraycopy(h264body, 0, h264data,
										h264Head.length, length);

								if (!(h264Head[0] == 0 && h264Head[1] == 0
										&& h264Head[2] == 0 && h264Head[3] == 1)) {// 验证是否是一帧数据
									byte[] last = stack.pop();
									byte[] tmp = new byte[last.length
											+ h264data.length];
									System.arraycopy(last, 0, tmp, 0,
											last.length);
									System.arraycopy(h264data, 0, tmp,
											last.length, h264data.length);
									stack.push(tmp);
								} else {// 当以0001开头，那么表示上一帧数据已经完成
									if (stack.size() > 0) {
										byte[] pop = stack.pop();
										byte[] logbyte = new byte[5];
										System.arraycopy(pop, 0, logbyte, 0, 5);
										frameTotal++;
										byte[] pop2 = new byte[pop.length + 2];
										pop2[0] = 1;
										pop2[1] = 1;
										System.arraycopy(pop, 0, pop2, 2,pop.length);
//										listener2.popdata(pop2);
										
										//解码播放
										onFrame(pop2,0, pop2.length);
										if(frameTotal > 3){
											mHandler.sendEmptyMessage(TYPE_START_PLAY);
										}
										long timeMillis2 = System.currentTimeMillis();
										long sleepTime = PRE_FRAME_TIME - (timeMillis2 - timeMillis1);
										if(sleepTime > 0){
											SystemClock.sleep(sleepTime);
										}
										
										//计算进度
										int lastLenth = inputStream.available();
										int i1 = (int) (((float)(totalLenth - lastLenth) / (float)(totalLenth)) * 100);
										if(i1 > process){
											process = i1;
											Log.e(TAG, "process :"+process);
											Message msg2 = new Message();
											msg2.what = TYPE_PROCESS;
											msg2.obj = process;
											mHandler.sendMessage(msg2);
										}
									
									}
									stack.push(h264data);
								}
							} else {
								// System.out.println("私有帧\n");
							}
						} else if ((uFlag & 0xe0) == 0xc0) {// 音频数据
//							Log.e("voice:", "voice : " + (pesHLen - 3 - pesHExtLen) + "");
							int len = pesHLen - 3 - pesHExtLen;
							byte[] vcDatas = new byte[len];
							inputStream.read(vcDatas);
							audioPlayer.getAllVoiceData().add(vcDatas);
						}
						break;
					}
				}

				inputStream.close();
				
		} catch (Exception e) {
			try {
				inputStream.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		
		mHandler.sendEmptyMessage(TYPE_END);
		isEnding = true;
		Log.e(TAG,"结束时间："+ System.currentTimeMillis());
	}
	
	 
	 private void parseMainTest(FileInputStream inputStream) {
			int frameTotalTest = 0;
			try {
				Log.e(TAG,"开始时间："+ System.currentTimeMillis());
				byte[] flag = new byte[4];
				byte[] h264Head = new byte[5];
				int uFlag = 0, pesHLen = 0, pesHExtLen;
					while (frameTotalTest < 15) {
						if(inputStream.read(flag) <= 0){
							break;
						}
						switch (uFlag = ByteBuffer.wrap(flag).order(ByteOrder.BIG_ENDIAN).getInt()) {
						case 0x000001ba:
							inputStream.skip(9);// 72 bits
							inputStream.skip(inputStream.read() & 7); // 8bits
							break;
						case 0x000001bc:
							int pravitedatalength = (inputStream.read() << 8 | inputStream.read());
							byte[] buf = new byte[pravitedatalength];
							inputStream.read(buf, 0, pravitedatalength);
							break;
						default:
							pesHLen = inputStream.read() << 8 | inputStream.read();
							inputStream.skip(2);
							inputStream.skip(pesHExtLen = inputStream.read()); // 跳过扩展长度
							if ((uFlag & 0xf0) == 0xe0) {// 视频

								if (pesHLen - 3 - pesHExtLen > 0) {
									inputStream.read(h264Head);
									int length = pesHLen - 3 - pesHExtLen - 5;
									byte[] h264data = new byte[length + 5];
									byte[] h264body = new byte[length];
									inputStream.read(h264body);
									System.arraycopy(h264Head, 0, h264data, 0,
											h264Head.length);
									System.arraycopy(h264body, 0, h264data,
											h264Head.length, length);
									if (!(h264Head[0] == 0 && h264Head[1] == 0
											&& h264Head[2] == 0 && h264Head[3] == 1)) {// 验证是否是一帧数据
										byte[] last = stack.pop();
										byte[] tmp = new byte[last.length
												+ h264data.length];
										System.arraycopy(last, 0, tmp, 0,
												last.length);
										System.arraycopy(h264data, 0, tmp,
												last.length, h264data.length);
										stack.push(tmp);
									} else {// 当以0001开头，那么表示上一帧数据已经完成
										if (stack.size() > 0) {
											byte[] pop = stack.pop();
											byte[] logbyte = new byte[5];
											System.arraycopy(pop, 0, logbyte, 0, 5);
											frameTotalTest++;
											byte[] pop2 = new byte[pop.length + 2];
											pop2[0] = 1;
											pop2[1] = 1;
											System.arraycopy(pop, 0, pop2, 2,pop.length);
											//解码播放
											onFrame(pop2,0, pop2.length);
										}
										stack.push(h264data);
									}
								} else {
									// System.out.println("私有帧\n");
								}
							} else if ((uFlag & 0xe0) == 0xc0) {// 音频数据
								int len = pesHLen - 3 - pesHExtLen;
								byte[] vcDatas = new byte[len];
								inputStream.read(vcDatas);
							}
							break;
						}
					}
					inputStream.close();
					
			} catch (Exception e) {
				try {
					inputStream.close();
				} catch (IOException e1) {
				}
			}
			stack.clear();
		}
		
	/**
	 * 
	 */
	@SuppressLint("NewApi")
	public void configNewCodec() {
		try {
			mediaFormat = MediaFormat.createVideoFormat(MIME_TYPE, VIDEO_WIDTH, VIDEO_HEIGHT);
			TIME_INTERNAL_MONITOR = 1000 / FRAMERATE;
			interval = TIME_INTERNAL_MONITOR;
			videoCodec = MediaCodec.createDecoderByType(MIME_TYPE);
			  byte[] header_sps = {0, 0, 0, 1, 103, 66, 0, 42, (byte) 149, (byte) 168, 30, 0, (byte) 137, (byte) 249, 102, (byte) 224, 32, 32, 32, 64};
              byte[] header_pps = {0, 0, 0, 1, 104, (byte) 206, 60, (byte) 128, 0, 0, 0, 1, 6, (byte) 229, 1, (byte) 151, (byte) 128};
              mediaFormat.setByteBuffer("csd-0", ByteBuffer.wrap(header_sps));
              mediaFormat.setByteBuffer("csd-1", ByteBuffer.wrap(header_pps));
             //设置帧率
			mediaFormat.setInteger(MediaFormat.KEY_FRAME_RATE, FRAMERATE);
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
	 * 暂停
	 */
	public void pause() {
		audioPlayer.pause();
		isPause = true;
		mHandler.sendEmptyMessage(TYPE_PAUSE);
	}
	
	/**
	 * 继续
	 */
	public void continuePlay() {
		isPause = false;
		isFrameRun = true;
		audioPlayer.continuePlay();
		if(isEnding){
			rePlay();
			mHandler.sendEmptyMessage(TYPE_START_PLAY);
		}else{
			mHandler.sendEmptyMessage(TYPE_START_PLAY);
		}
	}
	
	public void stopPlay() {
		try {
			inputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(null != audioPlayer){
			audioPlayer.stopPlayAudio();
		}
		isFrameRun = false;
		release();
	}

	//抓拍拿到的bitmap
	private Bitmap bitmap = null;
	
	
	/**
	 * 	抓拍
	 */
	public void getCapture() {
		isCaptureing = true;
		testBitmap();
	}

	/**
	 * 抓拍方法,缓存用于抓拍的数据
	 * 
	 */
	private void testBitmap() {

		List<byte[]> list = capBufferData.getList();
		int size = list.size();
		for (int i = 0; i < size; i++) {
			byte[] bs = list.get(i);
			byte[] bs22 = new byte[1920 * 1080 * 2 + 1024];
			int len2 = mConverter.H264Decode(bs, 0, bs.length, bs22);
			// Log.e("H264Decode","capture H264Decode:"+ len2+"");
			if (len2 > 0) {
				// Log.e(TAG, "抓拍成功数据:" + Arrays.toString(bs));
				ByteBuffer bbf = ByteBuffer.wrap(bs22, 0, len2);
				if (width <= 0 || height <= 0) {
					width = video_width;
					height = video_height;
				}
				bitmap = Bitmap.createBitmap(width, height, Config.RGB_565);
				bbf.position(0);
				bitmap.copyPixelsFromBuffer(bbf);
			} else {
				// Log.e(TAG, "抓拍失败数据:" + Arrays.toString(bs));
			}
		}
		if (null != bitmap) {
			Message msg = new Message();
			msg.what = TYPE_CUT_SUCCSE;
			msg.obj = bitmap;
			mHandler.sendMessage(msg);
		} else {
			mHandler.sendEmptyMessage(TYPE_CUR_ERR);
		}
//		capBufferData.clear();
		isCaptureing = false;
	}

	public void rePlay(){
		 stopPlay();
		 isEnding = false;
		 play(playPath);
	}
	

	boolean isInitOK = false;
	
	//播放一小端，方便初始化
	private void initFrameDecod() {
		if(isInitOK){
			stopPlay();
			playNow(playPath);
			return;
		}
		isInitOK = true;
		isEnding = false;
		if (videoCodec == null) {
			configNewCodec();
		}
		new AsyncTask<Void, Void, Void>(){
			@Override
			protected Void doInBackground(Void... params) {
				FileInputStream input = null;
				try {
					 if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
						//播放
						input = new FileInputStream(playPath);
						//尝试播放，消除绿屏幕
						parseMainTest(input);
						stopPlay();
						playNow(playPath);
					 }

				} catch (IOException e) {
					e.printStackTrace();
					Log.e(TAG, "ERR"+e.getMessage());
				}
				finally{
					if(input != null){
						try {
							input.close();
						} catch (IOException e) {
							e.printStackTrace();
							Log.e(TAG, "ERR"+e.getMessage());
						}
					}
				}
			
				return null;
			}
			
		}.execute();
	}
	
	public static final int TYPE_START_PLAY = 1;
	public static final int TYPE_PAUSE = 2;
	public static final int TYPE_CUT_SUCCSE = 3;
	public static final int TYPE_CUR_ERR = 4;
	public static final int TYPE_DOWNLOAD_SUCCSE = 5;
	public static final int TYPE_DOWNLOAD_ERR = 6;
	public static final int TYPE_TIME_STRING = 7;
	public static final int TYPE_PROCESS = 8;
	public static final int TYPE_END = 9;

	//跳转到进度位置播放
	public void VideoSeek(){

	}
}

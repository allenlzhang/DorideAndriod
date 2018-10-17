package com.carlt.chelepie.appsdk;


import android.util.Log;

import com.carlt.chelepie.manager.DeviceConnectManager;
import com.carlt.doride.DorideApplication;
import com.carlt.doride.utils.StringUtils;
import com.carlt.sesame.utility.MyTimeUtil;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Stack;

public class FrameReader {
	private String TAG = "CodecMode";
	
	
	//解析速率（没解析一次休眠的时间），调整帧率
//	private final static int ENCODETIMERTIMES = 30; 

	/**
	 * 帧数
	 */
//	int frameTotal = 0;
	
	/**
	 * 数据包
	 */
	int psBag = 0;

	/**
	 * 读取到的流
	 */
	InputStream stream;
	
	/**
	 * 每读取到帧数据的监听
	 */
	IFreamDataListener listener;

	/**
	 * 存放已经解好部分数据帧的栈
	 */
	Stack<byte[]> stack = new Stack<byte[]>();
	
	/**
	 * 从设备读取到数据队列
	 */
//	Queue<byte[]> popQueue = new LinkedBlockingQueue<byte[]>();
	
	/**
	 * 停止解析流ps得到数据
	 */
	private boolean isStop = false;
	

//	//解析视频包数，第一个跳过40个字节
//	private int count;
	InputStream stream2 = null;
	

	FileOutputStream streamout = null;
	
	
	
	public FrameReader() {
		super();
	}

	/**
	 * 解析主函数,拿到数据
	 */
	public void parseMain(IFreamDataListener listener1,String clientKey) {
		this.listener = listener1;
		isStop = false;
		Long clientId = AppsdkUtils.getStreamClientID(clientKey);
		try {
			
//			String dir = LocalConfig.GetMediaPath(UseInfoLocal.getUseInfo().getAccount(), PieInfo.getInstance().getDeviceName(), "yinpin");
//			stream2 = CPApplication.getInstanse().getAssets().open("pinyin4");
//			String path = dir + "pinyin2";
//			File file = new File(path);
//			streamout = new FileOutputStream(file);

		while(DeviceConnectManager.isDeviceConnect() && null != clientId){
			byte[] bufs = AppsdkUtils.ReadClientFrame(clientId);
//			Log.e("frameALL", Arrays.toString(bufs));
			if(null != bufs){
				if( 0 != bufs[0]){
					if(listener != null){
						listener.errData();
					}
					break;
				}
				psBag++;
				byte[] framBuf = new byte[bufs.length-1];
				System.arraycopy(bufs, 1, framBuf, 0, framBuf.length);
//				Log.e("frame", Arrays.toString(framBuf));
				
				caculateSpeed(framBuf.length);
				readFrame(framBuf);
			}
			if(isStop){
				listener = null;
				break;
			}
		}		
		}finally{
//			try {
////				streamout.close();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
		}
	}
	private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
	
	
	/**
	 * 停止解析ps流
	 * @param flag
	 */
	public void setStopParsePS(boolean flag){
		isStop = flag;
//		count = 0;
	}
	
	
	
	private long lastTimeStamp = 0;
	private long nowTotalRxBytes;
	
	private DecimalFormat df = new DecimalFormat("0.00");
	
	private void caculateSpeed(long bytes) {
		String speeds = "";
		nowTotalRxBytes = nowTotalRxBytes + bytes;
		long nowTimeStamp = System.currentTimeMillis();
		if(lastTimeStamp == 0){
			lastTimeStamp = nowTimeStamp;
		}
		long i = nowTimeStamp - lastTimeStamp;
		if(i > 1000){//计算一秒的byte 数
			// i是时间差
			long speed = (nowTotalRxBytes / i) * 1000;
			StringUtils.setSpeed(speed);
			// kb/s
			if (speed < 1024) {
				speeds = df.format(speed) + "B/S";
			} else if (speed >= 1024 && speed < (1024 * 1024)) {
				float temp = (float) speed / 1024;
				speeds = df.format(temp) + "KB/S";
			} else if (speed >= (1024 * 1024)) {
				float temp = (float) speed / 1024 / 1024;
				speeds = df.format(temp) + "MB/S";
			}
			StringUtils.setSpeeds(speeds);
			Log.e("info", "当前网速=" + speeds);
			nowTimeStamp = 0;
			lastTimeStamp = 0 ;
			nowTotalRxBytes = 0;
		}
	}
	
	/**
	 * 读取到帧数据
	 */
	private void readFrame(byte[] freams ) {
		try {
			stream = new ByteArrayInputStream(freams);
			byte[] flag = new byte[4];
			byte[] h264Head = new byte[5];

			int uFlag = 0, pesHLen = 0, pesHExtLen;

//			stream.
			while (stream.read(flag) > 0) {
				switch (uFlag = ByteBuffer.wrap(flag).order(ByteOrder.BIG_ENDIAN).getInt()) {
				case 0x000001ba:
//					stream.skip(9);//72 bits
//					stream.skip(stream.read() & 7); //8bits
					
					byte[] skip1 = new byte[9]; 
					stream.read(skip1);
//					Log.e("PSM","skip1:"+ Arrays.toString(skip1));
					byte[] skip2 = new byte[stream.read() & 7]; 
					stream.read(skip2);
//					Log.e("PSM","skip2:"+ Arrays.toString(skip2));
					break;
				case 0x000001bc:
//					stream.skip(stream.read() << 8 | stream.read());//psm
					int pravitedatalength = (stream.read() << 8 | stream.read());
					byte[] buf = new byte[pravitedatalength];
					int read = stream.read(buf, 0, pravitedatalength);
					if(!DorideApplication.getInstanse().isMonitor()){
						byte[] timeRange = Arrays.copyOfRange(buf, 10, 15);
						String framData = MyTimeUtil.paserFramData(timeRange);
						DorideApplication.getInstanse().setPlayStringtime(framData);
//					Log.e("PSM", Arrays.toString(timeRange));
						Log.e("playtime", framData);
					}
					break;
				default:
					pesHLen = stream.read() << 8 | stream.read();
					stream.skip(2);
					stream.skip(pesHExtLen = stream.read()); // 跳过扩展长度

					if ((uFlag & 0xf0) == 0xe0) {//视频

						if (pesHLen - 3 - pesHExtLen > 0) {
							stream.read(h264Head);
							int length = pesHLen - 3 - pesHExtLen - 5;
							byte[] h264data = new byte[length + 5];
							byte[] h264body = new byte[length];
							stream.read(h264body);
							System.arraycopy(h264Head, 0, h264data, 0,h264Head.length);
							System.arraycopy(h264body, 0, h264data,h264Head.length, length);

							if (!(h264Head[0] == 0 && h264Head[1] == 0
									&& h264Head[2] == 0 && h264Head[3] == 1)) {// 验证是否是一帧数据
								byte[] last = stack.pop();
								byte[] tmp = new byte[last.length
										+ h264data.length];
								System.arraycopy(last, 0, tmp, 0, last.length);
								System.arraycopy(h264data, 0, tmp, last.length,
										h264data.length);
								stack.push(tmp);
							} else {// 当以0001开头，那么表示上一帧数据已经完成
								if (stack.size() > 0) {
									byte[] pop = stack.pop();
									byte[] logbyte = new byte[5];
									System.arraycopy(pop, 0, logbyte, 0, 5);
//									int i = pop.length;
//									frameTotal++;
//									Log.e(TAG, "H264帧完整数据长度" + ":" + i
//											+ ".目前有多少帧数据：" + frameTotal);
									if(null != listener){
										byte[] pop2 = new byte[pop.length+2];
										pop2[0] = 1;
										pop2[1] = 1;
										System.arraycopy(pop, 0, pop2, 2, pop.length);
										listener.popdata(pop2);
//										listener.popdata(pop);
//										Log.e(TAG, "视频长度pop"+pop2.length);
									}
								}
								stack.push(h264data);
							}
						} else {
							Log.e(TAG,"私有帧\n");
						}
					}else if((uFlag & 0xe0) == 0xc0){//音频数据
						int len = pesHLen - 3 - pesHExtLen;
						byte[] vcDatas = new byte[len];
						stream.read(vcDatas);
						if(listener != null){
						listener.voiceData(vcDatas);
						}
					}
					break;
				}
			}

			stream.close();
		} catch (Exception e) {
			if(listener != null){
				listener.errData();
			}
//			Log.e("frame", "解码出错");
			try {
				stream.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public void setPlayListener(IFreamDataListener listener2) {
		this.listener = listener2;
	}
	
}

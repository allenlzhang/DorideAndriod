/**
 * 
 */
package com.hz17car.chelepie.utility;

/**
 *
 * @author @Y.yun
 * 
 */
public class AVIConverter {

	private int mWorker;

	/**
	 * 
	 */
	public AVIConverter() {
		mWorker = 0;
	}

	static {
		System.loadLibrary("avutil-54");
	    System.loadLibrary("postproc-53");
	    System.loadLibrary("swresample-1");
	    System.loadLibrary("swscale-3");
	    System.loadLibrary("avcodec-56");
	    System.loadLibrary("avformat-56");
	    System.loadLibrary("avfilter-5");
	    System.loadLibrary("avdevice-56");
	    System.loadLibrary("convert");
	}

	
	public native int Native_create(String fielName);

	
	public native int Native_write(byte[] content, int offset, int len);

	
	public native int Native_end();
	
	public int getmWorker() {
		return mWorker;
	}

	public void setmWorker(int mWorker) {
		this.mWorker = mWorker;
	}
	
	public native int H264Init();

	public native int H264Close();

	public native int H264Decode(byte[] data, int offset, int len, byte[] out);
	
	public native static int Native_DecodeAudio(byte[] data, int offset, int size, byte[] out);

}

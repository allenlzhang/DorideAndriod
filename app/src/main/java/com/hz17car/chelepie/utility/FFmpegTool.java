/**
 * 
 */
package com.hz17car.chelepie.utility;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author @Y.yun
 * 
 * YUV解码工具
 */
public class FFmpegTool {

	// 成员名不可更改 C++层使用
	private int mDecoder;
	private List<Datayuv> mDatas = Collections.synchronizedList(new ArrayList<Datayuv>());

	public void setDecoder(int decode) {
		this.mDecoder = decode;

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
		System.loadLibrary("ffmpegTools");
	}

	public native int Init(int w, int h);
	
	public native int Update(int w, int h);

	public native int Release();
	
	//索引0 表示宽，索引1表示高
	public native int GetSize(int[] size);

	public native int DecodeFrame(byte[] data, int offset, int len);

	// 方法名不可更改 C++层使用
	public void putYUVData(byte[] y, byte[] u, byte[] v) {
		Datayuv dd = new Datayuv();
		dd.yData = y;
		dd.uData = u;
		dd.vData = v;
		mDatas.add(dd);
	}

	public List<Datayuv> getDecodeDatas() {
		return mDatas;
	}

}

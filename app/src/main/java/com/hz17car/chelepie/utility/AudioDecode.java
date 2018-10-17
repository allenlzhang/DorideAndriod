/**
 * 
 */
package com.hz17car.chelepie.utility;

/**
 *
 * @author @Y.yun
 * 
 */
public class AudioDecode {

	static {
		System.loadLibrary("native");
	}

	public native static int DecodeInit();

	public native static int DecodeAudio(byte[] data, int offste, int size, byte[] outs);

	public native static int DecodeRelease();

	public native static int EncodeInit();

	public native static int EncodeAudio(byte[] data, int offste, int size, byte[] outs);

	public native static int EncodeRelease();

}

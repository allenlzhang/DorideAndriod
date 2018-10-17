package com.carlt.chelepie.utils;

/**
 *	大端小端读写数据
 * @author @Y.yun
 * 
 */
public final class BitConverter {
	private BitConverter() {
	}
	
	/**
	 * 小端写入
	 * 
	 * @return
	 */
	public static byte[] littleEndianPut(short action) {
		byte[] result = new byte[2];
		result[0] = (byte) (action & 0XFF);
		result[1] = (byte) (action >> 8 & 0XFF);
		return result;
	}

	/**
	 * 小端写入 
	 * 
	 * @return
	 */
	public static byte[] littleEndianPut(int action) {
		byte[] result = new byte[4];
		result[0] = (byte) (action & 0XFF);
		result[1] = (byte) (action >> 8 & 0XFF);
		result[2] = (byte) (action >> 16 & 0XFF);
		result[3] = (byte) (action >> 24 & 0XFF);
		return result;
	}


	/**
	 * 小端写入
	 * 
	 * @param action
	 * @return
	 */
	public static byte[] littleEndianPut(long action) {
		byte[] buf = new byte[8];
		for (int i = 0; i < 8; i++) {
			buf[i] = (byte) (action & 0XFF);//
			action = action >> 8; // 向右移8位
		}
		return buf;
	}
	
	/**
	 * 大端写入
	 * 
	 * @param action
	 * @return
	 */
	public static byte[] bigEndianPut(short action) {
		byte[] result = new byte[2];
		result[1] = (byte) (action & 0XFF);
		result[0] = (byte) (action >> 8 & 0XFF);
		return result;
	}
	
	/**
	 * 大端写入
	 * 
	 * @param action
	 * @return
	 */
	public static byte[] bigEndianPut(int action) {
		byte[] result = new byte[4];
		result[3] = (byte) (action & 0XFF);
		result[2] = (byte) (action >> 8 & 0XFF);
		result[1] = (byte) (action >> 16 & 0XFF);
		result[0] = (byte) (action >> 24 & 0XFF);
		return result;
	}
	
	/**
	 * 大端写入
	 * 
	 * @param action
	 * @return
	 */
	public static byte[] bigEndianPut(long action) {
		byte[] buf = new byte[8];
		for (int i = 7; i >= 0; i--) {
			buf[i] = (byte) (action & 0XFF);//
			action = action >> 8; // 向右移8位
		}
		return buf;
	}
	
	/**
	 * @param temp
	 *            小端读取
	 * @return
	 */
	public static short littleEndianReadShort(byte[] temp, int index) {
		short result = 0;
		result |= (short) (temp[index] & 0XFF);
		result |= (short) (temp[index + 1] & 0XFF) << 8;
		return result;
	}
	
	/**
	 * @param temp
	 *            小端读取
	 * @return
	 */
	public static int littleEndianReadInt(byte[] temp, int index) {
		int result = 0;
		result |= temp[index] & 0XFF;
		result |= (temp[index + 1] & 0XFF) << 8;
		result |= (temp[index + 2] & 0XFF) << 16;
		result |= (temp[index + 3] & 0XFF) << 24;
		return result;
	}

	/**
	 * 小端读取
	 * 
	 * @param buf
	 * @param index
	 * @return
	 */
	public static long littleEndianReadLong(byte[] buf, int index) {
		long result = 0;
		result |= buf[index] & 0xFF;
		result |= (buf[index + 1] & 0xFF) << 8;
		result |= (buf[index + 2] & 0xFF) << 16;
		result |= (buf[index + 3] & 0xFF) << 24;
		result |= (buf[index + 4] & 0xFF) << 32;
		result |= (buf[index + 5] & 0xFF) << 40;
		result |= (buf[index + 6] & 0xFF) << 48;
		result |= (buf[index + 7] & 0xFF) << 56;
		return result;
	}
	
	/**
	 * 大端读取
	 * 
	 * @param temp
	 * @return
	 */
	public static short bigEndianReadShort(byte[] temp, int index) {
		short result = 0;
		result |= (short) (temp[index + 1] & 0XFF);
		result |= (short) (temp[index + 0] & 0XFF) << 8;
		return result;
	}

	/**
	 * 大端读取
	 * 
	 * @param temp
	 * @return
	 */
	public static int bigEndianReadInt(byte[] temp, int index) {
		int result = 0;
		result |= (temp[index + 3] & 0XFF);
		result |= (temp[index + 2] & 0XFF) << 8;
		result |= (temp[index + 1] & 0XFF) << 16;
		result |= (temp[index + 0] & 0XFF) << 24;
		return result;
	}

	/**
	 * 大端读取
	 * 
	 * @param buf
	 * @param index
	 * @return
	 */
	public static long bigEndianReadLong(byte[] buf, int index) {
		long result = 0;
		result |= buf[index + 7] & 0xFF;
		result |= (buf[index + 6] & 0xFF) << 8;
		result |= (buf[index + 5] & 0xFF) << 16;
		result |= (buf[index + 4] & 0xFF) << 24;
		result |= (buf[index + 3] & 0xFF) << 32;
		result |= (buf[index + 2] & 0xFF) << 40;
		result |= (buf[index + 1] & 0xFF) << 48;
		result |= (buf[index] & 0xFF) << 56;
		return result;
	}
}

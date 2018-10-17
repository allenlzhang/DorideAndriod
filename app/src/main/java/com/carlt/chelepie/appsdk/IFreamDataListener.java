package com.carlt.chelepie.appsdk;

/**
 * 
 * @author liu
 *
 * 从ps流中拿到数据
 */
public interface IFreamDataListener {
	/**
	 * 成功拿到帧数据
	 * @param bs
	 */
	void popdata(byte[] bs);
	
	/**
	 * 解析出错
	 */
	void errData();
	
	/**
	 *  音频信息
	 * @param vcDatas
	 */
	void voiceData(byte[] vcDatas);;
}

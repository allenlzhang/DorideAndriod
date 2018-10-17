package com.carlt.chelepie.appsdk;

import android.util.Log;

import com.carlt.chelepie.data.recorder.FileInfo;
import com.carlt.doride.DorideApplication;
import com.carlt.doride.utils.FileUtil;
import com.carlt.sesame.utility.MyTimeUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import appsdk.Appsdk;

/**
 * 车乐拍视频解码的控制协议
 * 
 * @author liu
 * 
 */
public class AppsdkUtils {

	private AppsdkUtils() {
	}
	
	public static final int ENDFLAG_SUCC = 1;//成功
	public static final int ENDFLAG_FAIL = 0;//失败
	public static final int ENDFLAG_ING = 2;//正在传输

	/**
	 * 存放所有子链接id
	 */
	private static Map<String, Long> streamclientMap = new HashMap<String, Long>();
	
	/**
	 * 存放子线程id  的key
	 */
	public static final String CLIENTKKEY_FILE = "FILE";
	public static final String CLIENTKKEY_JPG = "PIC";
	public static final String CLIENTKKEY_THUMBNAIL = "THUMBNAIL";
	public static final String CLIENTKKEY_VEDIO = "VEDIO";
	public static final String CLIENTKKEY_MONITOR = "MONITOR";
	public static final String CLIENTKKEY_REPLAY = "REPLAY";
	public static final String CLIENTKKEY_CUT_VEDIO = "CUT_VEDIO";
	
	/**
	 * 是否是测试，用假数据 false 表示不会使用测试数据
	 */
	public static final boolean isTestFlag = false;

	private static FrameReader frameReader;
	private static FrameReader frameReader2;
	
	/**
	 * 测试假数据
	 */
	private static byte[] getTestRes(){
		byte[] str = null ;
		byte[] result = null;
		byte[] by1 = new byte[]{0,0,0};;
		try {
			str = FileUtil.ToString(DorideApplication.ApplicationContext.getAssets().open("test.json")).getBytes();
			result = new byte[str.length+3];
			System.arraycopy(by1, 0, result, 0, by1.length);
			System.arraycopy(str, 0, result, by1.length, str.length);
		} catch (Exception e) {
			result = by1;
		}
		return result;
	}
	
	/**
	 * 会话相关 //登陆
	 * 
	 * @param name
	 * @param psw
	 * @param encrypt
	 * @return 0 表示成功
	 */
	public static long CLogin(String name, String psw, int encrypt) {
		if(isTestFlag){
			return 0;
		}
		return Appsdk.cLogin(name, psw, encrypt);
	}
	
	/**
	 * 对时
	 * @param time
	 * @param sqnum
	 * force	0：非强制， 1：强制生效。其他保留
	 * @return
	 */
	public static long CSetTime(long time, int force,int sqnum) {
		if(isTestFlag){
			return 0;
		}
		
		String times = MyTimeUtil.commonFormat.format(new Date(time));
		Log.e("info", "对时时间：========"+ time);
		return Appsdk.cSetTime(times, force, sqnum);
	}

	/**
	 * 退出
	 * 
	 * @return
	 */
	public static long CLogout() {
		if(isTestFlag){
			return 0;
		}
		return Appsdk.cLogout();
	}

	/**
	 * // 直播认领
	 * 
	 * @return
	 */
	public static long CMReady(int streamType, int seqNum) {
		if(isTestFlag){
			return 0;
		}
		return Appsdk.cmReady(streamType, seqNum);
	}

	/**
	 * 停止直播
	 * 
	 * @param seqNum
	 * @return
	 */
	public static long CMStop(int seqNum) {
		if(isTestFlag){
			return 0;
		}
		if(null != frameReader2){
			frameReader2.setStopParsePS(true);
		}
		return Appsdk.cmStop(seqNum);
	}

	/**
	 * 注册绑定 /////////////////////////////////////////////////////
	 * 
	 * @return 绑定激活设备 0成功，1账号已经绑定，2设备已经绑定，3激活失败
	 * @param mUid
	 *       用户id
	 * @param mSqnum
	 *            
	 */
	public static long CBindDevice(String mUid, int mSqnum) {
		if(isTestFlag){
			return 0;
		}
		return Appsdk.cBindDevice(mUid, mSqnum);
	}

	/**
	 * 主链接是否可用
	 * 
	 * @return 0 可以 ，-1 不可以
	 */
	public static long CCAvailable() {
		if(isTestFlag){
			return 0;
		}
		return Appsdk.ccAvailable();
	}

	/**
	 * 控制抓拍
	 * 
	 * @param mSqnum
	 * @return 0 是成功
	 */
	public static long CCapture(int mSqnum) {
		if(isTestFlag){
			return 0;
		}
		return Appsdk.cCapture(mSqnum);
	}

	/**
	 * 删除文件
	 */
	public static long CDeleteFile(FileInfo fileInfo, int mSqnum) {
		if(isTestFlag){
			return 0;
		}
		// TODO something
		 return Appsdk.cDeleteFile(fileInfo.getStreamType(), fileInfo.getRecordType(), fileInfo.getMinRecordType(),
				 fileInfo.getFileNo(), fileInfo.getFileLength(), fileInfo.getFilename(), fileInfo.getStartTime(), fileInfo.getEndTime(), mSqnum);
	}

	/**
	 * 以文件名下载文件 --参数要传入一个Fileinfo 详见文档
	 * @param seqNum
	 */
	public static long CPDownloadFileByName(FileInfo fileInfo,int seqNum ) {
		if(isTestFlag){
			return 0;
		}
		 return Appsdk.cpDownloadFileByName(fileInfo.getStreamType(), fileInfo.getRecordType(), fileInfo.getMinRecordType(),
				 fileInfo.getFileNo(), fileInfo.getFileLength(), fileInfo.getFilename(), fileInfo.getStartTime(), fileInfo.getEndTime(), seqNum);
	}

	/**
	 * 格式化分区
	 * 
	 * @param mSqnum
	 * @return
	 */
	public static long CFormatStorage(int mSqnum) {
		if(isTestFlag){
			return 0;
		}
		return Appsdk.cFormatStorage(mSqnum);
	}

	/**
	 * 
	 * 搜索图片信息
	 * 
	 * htype 录像主类型 0:全天定时录制，1: 命令抓拍触发，2：碰撞事件触发
	 * 
	 * @return 0 表示成功
	 */
	public static long CGThumbnail(String beginTime, String endTime, int htype, int seqNum) {
		if(isTestFlag){
			return 0;
		}
		return Appsdk.cgThumbnail(beginTime, endTime, htype, seqNum);
	}

	/**
	 * 获取音频配置
	 * 
	 * @param seqNum
	 * @return 0 ok
	 */
	public static long CGetAudioConfig(int seqNum) {
		if(isTestFlag){
			return 0;
		}
		return Appsdk.cGetAudioConfig(seqNum);
	}

	/**
	 * 0 不可用, 1 可用
	 * 
	 * @param seqNum
	 * @return 0 表示成功
	 */
	public static long CSetAudioConfig(int mic_enable, int seqNum) {
		if(isTestFlag){
			return 0;
		}
		return Appsdk.cSetAudioConfig(mic_enable, seqNum);
	}

	/**
	 * 获取抓拍是否录制视频
	 * 
	 * @param seqNum
	 * @return
	 */
	public static long CGetCaptureRecord(int seqNum) {
		if(isTestFlag){
			return 0;
		}
		return Appsdk.cGetCaptureRecord(seqNum);
	}

	/**
	 * //设置抓拍是否录制视频
	 * 
	 * @param seqNum
	 * @return
	 */
	public static long CSetCaptureRecord(int iRecord, int seqNum) {
		if(isTestFlag){
			return 0;
		}
		return Appsdk.cSetCaptureRecord(iRecord, seqNum);
	}

	/**
	 * 设置视频 亮度，对比度，饱和度
	 * 
	 * @param brightness
	 * @param contrast
	 * @param saturation
	 * @param seqNum
	 * @return
	 */
	public static long CSetVideoColor(int brightness, int contrast,int saturation, int seqNum) {
		if(isTestFlag){
			return 0;
		}
		return Appsdk.cSetVideoColor(brightness, contrast, saturation, seqNum);
	}

	/**
	 * eventType 2,碰撞,1 抓拍,0全天循环录制 搜索文件信息。。。搜索视频
	 * 
	 * @param iRecord
	 * @param seqNum
	 * @return
	 */
	public static long CGetFilelist(String beginTime, String endTime, int eventType, int seqNum) {
		if(isTestFlag){
			return 0;
		}
		return Appsdk.cGetFilelist(beginTime, endTime, eventType, seqNum);
	}

	/**
	 * 获取设备存储信息
	 * 
	 * @param seqNum
	 * @return
	 */
	public static long CGetStorageInfo(int seqNum) {
		if(isTestFlag){
			return 0;
		}
		return Appsdk.cGetStorageInfo(seqNum);
	}

	/**
	 * 获取设备信息车乐拍固件版本
	 * 
	 * @param seqNum
	 * @return
	 */
	public static long CGetSystemInfo(int seqNum) {
		if(isTestFlag){
			return 0;
		}
		return Appsdk.cGetSystemInfo(seqNum);
	}

	/**
	 * 获取视频 亮度，高度，饱和度
	 * 
	 * @param seqNum
	 * @return
	 */
	public static long CGetVideoColor(int seqNum) {
		if(isTestFlag){
			return 0;
		}
		return Appsdk.cGetVideoColor(seqNum);
	}

	/**
	 * 获取视频 分辨率，码率信息 --返回MSGID有问题
	 * 
	 * @param seqNum
	 * @return
	 */
	public static long CGetVideoSize(int seqNum) {
		if(isTestFlag){
			return 0;
		}
		return Appsdk.cGetVideoSize(seqNum);
	}

	/**
	 * 视频配置 0:1080p,1:720p
	 * 
	 * @param size
	 * @param seqNum
	 * @return
	 */
	public static long CSetVideoSize(int size, int seqNum) {
		if(isTestFlag){
			return 0;
		}
		return Appsdk.cSetVideoSize(size, seqNum);
	}

	/**
	 * 回放关闭, 会关闭连接
	 * 
	 * @param seqNum
	 * @return
	 */
	public static long CPClose(int seqNum) {
		if(isTestFlag){
			return 0;
		}
		return Appsdk.cpClose(seqNum);
	}

	/**
	 * 回放恢复暂停
	 * 
	 * @param seqNum
	 * @return
	 */
	public static long CPContinue(int seqNum) {
		if(isTestFlag){
			return 0;
		}
		return Appsdk.cpContinue(seqNum);
	}

	/**
	 * 停止下载回放视频
	 * 
	 * @param seqNum
	 * @return
	 */
	public static long CPDownloadStop(int seqNum) {
		if(isTestFlag){
			return 0;
		}
		long cpDownloadStop = Appsdk.cpDownloadStop(seqNum);
		return cpDownloadStop;
	}

	/**
	 * 下载回放视频数据，，，也就是剪裁(认领)
	 * 
	 * @param startTime
	 * @param endTime
	 * @param seqNum
	 * @return
	 */
	public static long CPDownloadByTime(String startTime, String endTime, int seqNum) {
		if(isTestFlag){
			return 0;
		}
		return Appsdk.cpDownloadByTime(startTime, endTime, seqNum);
	}

	/**
	 * 主连接发送回放认领
	 * 
	 * @param startTime
	 * @param endTime
	 * @param seqNum
	 * @return
	 */
	public static long CPReady(String startTime, String endTime, int seqNum) {
		if(isTestFlag){
			return 0;
		}
		return Appsdk.cpReady(startTime, endTime, seqNum);
	}


	/**
	 * 回放加速 - level 加速级别
	 * 
	 * @param level
	 * @param seqNum
	 */
	public static long CPFast(long level, int seqNum) {
		if(isTestFlag){
			return 0;
		}
		return Appsdk.cpFast(level, seqNum);
	}

	/**
	 * // 回放减速 - level 减速级别
	 * 
	 * @param level
	 * @param seqNum
	 * @return
	 */
	public static long CPSlow(long level, int seqNum) {
		if(isTestFlag){
			return 0;
		}
		return Appsdk.cpSlow(level, seqNum);
	}

	/**
	 * 回放定位 - t 定位绝对时间
	 * 
	 * @param t
	 * @param seqNum
	 * @return
	 */
	public static long CPLocate(String t, int seqNum) {
		if(isTestFlag){
			return 0;
		}
		return Appsdk.cpLocate(t, seqNum);
	}

	/**
	 * 回放暂停
	 * 
	 * @param seqNum
	 * @return
	 */
	public static long CPPause(int seqNum) {
		if(isTestFlag){
			return 0;
		}
		return Appsdk.cpPause(seqNum);
	}

	/**
	 * 回放停止
	 * 
	 * @param seqNum
	 * @return
	 */
	public static long CPStop(int seqNum) {
		if(isTestFlag){
			return 0;
		}
		if(frameReader != null){
		frameReader.setStopParsePS(true);
		}
		return Appsdk.cpStop(seqNum);
	}

	/**
	 * 设置WIFI 信息
	 * 
	 * @param wifiname
	 * @param key 原始密码
	 * @param psw 新密码
	 * @param seqNum
	 * @return
	 */
	public static long CSetWIFIInfo(String wifiname, String key, String psw, int seqNum) {
		if(isTestFlag){
			return 0;
		}
		return Appsdk.cSetWIFIInfo(wifiname, key, psw, seqNum);
	}

	/**
	 * 关闭主链接
	 */
	public static void CloseCtrlSocket() {
		if(isTestFlag){
			return ;
		}
		Appsdk.closeCtrlSocket();
	}

	/**
	 * 返回最后的错误消息,
	 * 
	 * @param id
	 * @return 1下载完成  ,0 是没有完成
	 */
	public static long GetEndflag(long id) {
		if(isTestFlag){
			return 0;
		}
		return Appsdk.getEndflag(id);
	}

	/**
	 * 建立到硬件的主Sockeet连接 Socket连接
	 * 
	 * @param addr
	 * @return
	 */
	public static long InitCtrlSocket(String addr) {
		if(isTestFlag){
			return 0;
		}
		return Appsdk.initCtrlSocket(addr);
	}

	/**
	 * 读取主链接返回消息
	 * 
	 * @return
	 */
	public static byte[] ReadCtrlMsg() {
		if(isTestFlag){
			return  getTestRes();
		}
		return Appsdk.readCtrlMsg();
	}

	/**
	 *  创建子线程
	 * @return 返回线程的id ,-1 为出错
	 * @param clientKey  子线程名字
	 */
	public static long InitStreamClient(String clientKey) {
		if(isTestFlag){
			return 0;
		}
		long clientId = Appsdk.initStreamClient();
		streamclientMap.put(clientKey, clientId);
		return clientId;
	}

	/**
	 * 读取子线程数据
	 * 
	 * @param id
	 * @return
	 */
	public static byte[] ReadClientFrame(long id) {
		if(isTestFlag){
			return  getTestRes();
		}
		return Appsdk.readClientFrame(id);
	}

	/**
	 * 关闭子链接
	 * 
	 * @param clientKey
	 *            子链接的名字
	 * @param id
	 */
	public static void CloseStreamClient(String clientKey, long id) {
		if(isTestFlag){
			return ;
		}
		streamclientMap.remove(clientKey);
		try {
			Appsdk.closeStreamClient(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 关闭子链接
	 * 
	 * @param clientKey
	 *            子链接的名字
	 */
	public static void CloseStreamClient(String clientKey) {
		if(isTestFlag){
			return ;
		}
		Long id = streamclientMap.get(clientKey);
		if(null != id){
			streamclientMap.remove(clientKey);
		Appsdk.closeStreamClient(id);
		}
	}
	
	/**
	 * 通过子链接名字获取线程id
	 * @param clientKey
	 * @return id
	 */
	public static Long getStreamClientID(String clientKey) {
		if(isTestFlag){
			return (long) 0;
		}
		try {
			
			Long id = streamclientMap.get(clientKey);
			return id;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 发送握手
	 * 
	 * @param id
	 * @param handShake的json数据，前两个字节是msgid
	 * @return 0 是成功
	 * @throws JSONException
	 */
	public static long SendHandShake(long id, String handShake) {
		if(isTestFlag){
			return 0;
		}
		
		byte[] bytes = handShake.getBytes();
		byte[] buf = new byte[bytes.length-2];
		System.arraycopy(bytes, 2, buf, 0, bytes.length-2);
		JSONObject mJson;
		String mhandShake = null;
		try {
			mJson = new JSONObject(new String(buf));
			mhandShake = mJson.getString("handShake");
			return Appsdk.sendHandShake(id, mhandShake);
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("SendHandShake", e.getClass().getSimpleName());
			return -1;
		}
		
	}

	/**
	 * 心跳
	 */
	public static void StartHeartBeat() {
		if(isTestFlag){
			return ;
		}
		Appsdk.startHeartBeat();
	}

	/**
	 * 升级终止
	 * 
	 * @return
	 */
	public static long UpgradeAbort() {
		if(isTestFlag){
			return 0;
		}
		// TODO not used
		return Appsdk.upgradeAbort();
	}
	
	/**
	 * 
	 * @return
	 */
	public static long Reboot(int seqNum) {
		if(isTestFlag){
			return 0;
		}
		// TODO not used
		return Appsdk.cReboot(seqNum);
	}

	/**
	 * 升级数据包
	 * 
	 * @param buft
	 * @param isLast
	 * @return
	 */
	public static long UpgradeData(byte[] buft, boolean isLast) {
		if(isTestFlag){
			return 0;
		}
		return Appsdk.upgradeData(buft, isLast);
	}

	/**
	 * 升级开始
	 */
	public static long UpgradeInit() {
		if(isTestFlag){
			return 0;
		}
		return Appsdk.upgradeInit();
	}

	/**
	 * 获取直播数据
	 * 
	 * @return
	 */
	public static void ReadMonitorFrame(IFreamDataListener listener) {
		if(isTestFlag){
			return ;
		}
		if(frameReader2 == null){
			frameReader2 = new FrameReader();
		}
		frameReader2.parseMain(listener,AppsdkUtils.CLIENTKKEY_MONITOR);
	}

	/**
	 * 获取回放数据
	 * 
	 * @return
	 */
	public static void ReadFrame(IFreamDataListener listener) {
		if(isTestFlag){
			return ;
		}
		frameReader = new FrameReader();
		frameReader.parseMain(listener,AppsdkUtils.CLIENTKKEY_REPLAY);
	}
	
	/**
	 * 关闭所有子链接
	 */
	public static void closeAllSClient() {
		if(isTestFlag){
			return ;
		}
		for (String key : streamclientMap.keySet()) {
			Long id = streamclientMap.get(key);
			streamclientMap.remove(key);
			CloseStreamClient(key,id);
		}
	}
}

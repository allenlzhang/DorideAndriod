package com.carlt.chelepie.systemconfig;

/**
 * @author @Y.yun 车乐拍 硬件Action 定义
 */
public class ActionConfig {

	/** 登录字符串 */
	public static final String LoginStr = "{\"sUserName\": \"Carlt\",\"sPassword\": \"Carlt12345\",\"iEncryptType\": 0,\"iLoginType\": 2}";
	private static int mSeqNum = 10000;//--包序列号从10000开始
	/** // 登陆; */
	public static final short MID_LOGIN = 1000;
	/** // 登陆返回; */
	public static final short MID_LOGIN_RES = 1001;
	
	/** // 获取设备固件版本 */
	public static final short MID_GET_HARDVESION = 1002;
	/** // 获取设备固件版本回复 */
	public static final short MID_GET_HARDVESION_RES = 1003;
	
	/** // 查询系统/存储信息 */
	public static final short MID_SYS_INFO = 1004;
	/** // 查询系统/存储信息回复 */
	public static final short MID_SYS_INFO_RES = 1005;
	/** // 设置视频配置 */
	public static final short MID_CONFIG_SET = 1018;
	/** // 设置视频配置回复 */
	public static final short MID_CONFIG_SET_RES = 1019;
	/** // 格式化SDCard */
	public static final short MID_DISK_FORMART = 1006;
	/** // 格式化SDCard回复 */
	public static final short MID_DISK_FORMART_RES = 1007;
	/** // 绑定激活 */
	public static final short MID_BIND = 1008;
	/** // 绑定激活回复 */
	public static final short MID_BIND_RES = 1009;
	/** // 获取设备是否录制音频 */
	public static final short MID_GET_VOICE_STATUS = 1010;
	/** // 获取设备是否录制音频回复 */
	public static final short MID_GET_VOICE_STATUS_RES = 1011;
	/** // 设置是否录制音频 */
	public static final short MID_CONFIG_VOICE_STATUS = 1012;
	/** // 设置是否录制音频回复 */
	public static final short MID_CONFIG_VOICE_STATUS_RES = 1013;
	/** // 获取视频分辨率*/
	public static final short MID_GET_VEDIO_DISPLAY_SIZE = 1014;
	/** // 获取视频分辨率回复 */
	public static final short MID_GET_VEDIO_DISPLAY_SIZE_RES = 1015;
	/** // 设置视频分辨率*/
	public static final short MID_CONFIG_VEDIO_DISPLAY_SIZE = 1016;
	/** // 设置视频分辨率回复 */
	public static final short MID_CONFIG_VEDIO_DISPLAY_SIZE_RES = 1017;
	/** // 设置视频亮度，饱和度，对比度*/
	public static final short MID_CONFIG_VEDIO_DISPLAY = 1018;
	/** // 设置视频亮度，饱和度，对比度回复 */
	public static final short MID_CONFIG_VEDIO_DISPLAY_RES = 1019;
	/** // 获取视频亮度，饱和度，对比度*/
	public static final short MID_GET_VEDIO_DISPLAY_RGB = 1020;
	/** // 获取视频亮度，饱和度，对比度回复 */
	public static final short MID_GET_VEDIO_DISPLAY_RGB_RES = 1021;
	/** // 设置设备WIFI 信息 wifi名和密码*/
	public static final short MID_CONFIG_WIFI = 1022;
	/** // 设置设备WIFI 信息 wifi名和密码回复 */
	public static final short MID_CONFIG_WIFI_RES = 1023;
	/** // 设置抓拍是否录制视频*/
	public static final short MID_CONFIG_CATCH_VEDIO_OR_NOT = 1024;
	/** // 设置抓拍是否录制视频回复 */
	public static final short MID_CONFIG_CATCH_VEDIO_OR_NOT_RES = 1025;
	/** // 获取抓拍是否录制视频*/
	public static final short MID_GET_CAPTURE_VEDIO_OR_NOT = 1026;
	/** // 获取抓拍是否录制视频回复 */
	public static final short MID_GET_CAPTURE_VEDIO_OR_NOT_RES = 1027;
	/** // 控制抓拍*/
	public static final short MID_CAPTURE = 1028;
	/** // 抓拍完成后，记录仪主动推送信息*/
	public static final short MID_CAPTURE_FILE = 1200;
	/** // 控制抓拍回复 */
	public static final short MID_CAPTURE_RES = 1029;
	/** // 搜索文件命令*/
	public static final short MID_SEARCH_FILE = 1030;
	/** // 搜索文件命令回复 */
	public static final short MID_SEARCH_FILE_RES = 1031;
	/** // 删除文件*/
	public static final short MID_DEL_FILE = 1032;
	/** // 删除文件回复 */
	public static final short MID_DEL_FILE_RES = 1033;
	
	/** // 升级开始，停止*/
	public static final short MID_UPGRADE = 1034;
	/** // 升级开始，停止回复 */
	public static final short MID_UPGRADE_RES = 1035;
	
	/** // 发送升级数据*/
	public static final short MID_UPGRADE_DATA = 1036;
	/** // 发送升级数据回复 */
	public static final short MID_UPGRADE_DATA_RES = 1037;
	
	/** // 升级进度主动返回(升级进度) */
	public static final short MID_UPGRADE_PROGRESS = 1202;
	
	/** // 回放，按时间下载，按文件名下载 的认领请求*/
	public static final short MID_REPLAY_AND_DOWNLOAD = 1042;
	/** // 回放，按时间下载，按文件名下载 的认领请求 回复 */
	public static final short MID_REPLAY_AND_DOWNLOAD_RES = 1043;


	/** // 登出; */
	public static final short MID_LOGOUT = 1038;
	/** // 登出回复; */
	public static final short MID_LOGOUT_RES = 1039;
	/** // 直播开始 */
	public static final short MID_MONITOR = 1040;
	/** // 直播开始回复 */
	public static final short MID_MONITOR_RES = 1041;
	/** // 心跳; */
	public static final short MID_HEARTBEAT = 1100;
	/** // 心跳回复 */
	public static final short MID_HEARTBEAT_RES = 1101;
	/**
	 * 格式化SD卡进度回复
	 */
	public static final short MID_FORMATSD_RES = 1204;
	
	/** // 抓拍WIFI通知 */
	public static final short MID_NOTIFY_CAPTURE = 2000;
	
	//对时
	public static final short MID_NOTIFY_TIME = 1104;
	
	public static final short MID_NOTIFY_TIME_RES = 1105;
	
	// typedef enum RET_CODE
	public static final class RetCode {
		/** */
		public static final int OK = 100;
		/** /// 未知错误 */
		public static final int UNKNOWNERROR = 101;
		/** /// 版本不支持 */
		public static final int NOTSUPPORT = 102;
		/** /// 非法请求 */
		public static final int NOTVALID = 103;
		/** /// 该用户已经登录 */
		public static final int LOGINED = 104;
		/** /// 该用户未登录 */
		public static final int UNLOGINED = 105;
		/** /// 用户名密码错误 */
		public static final int USERORPWDERROR = 106;
		/** /// 超时 */
		public static final int TIMEOUT = 107;
		/** /// 查找失败，没有找到对应文件 */
		public static final int NOTFOUND = 108;
		/** /// SDCard 错误 */
		public static final int SDCARDERR = 109;
		/** /// 升级失败 */
		public static final int UPDATEERR = 110;
		/** /// 记录仪内部操作失败 */
		public static final int HARDWARE = 111;
		/** /// 参数错误 */
		public static final int PARAMERR = 112;
//		
//		/** /// 该用户不存在 */
//		public static final int USENOTEXIST = 113;
//		/** /// 该用户组已经存在 */
//		public static final int GROUPEXIST = 114;
//		/** /// 该用户组不存在 */
//		public static final int GROUPNOTEXIST = 115;
//		/** /// 成功，设备需要重启 */
//		public static final int NEEDREBOOT = 150;
//		/** /// < 消息格式错误 */
//		public static final int NETIP_ERR_MSGFORMATERR = 117;
//		/** /// < 未设置云台协议 */
//		public static final int NETIP_ERR_NOPTZPROTOCOL = 118;
//		/** /// < 没有查询到文件 */
//		public static final int NETIP_ERR_NOFILEFOUND = 119;
//		/** /// < 配置为启用 */
//		public static final int CONFIG_NOT_ENABLE = 120;
//		/** // 数字通道未连接 */
//		public static final int MEDIA_CHN_NOTCONNECT = 121;
//		/** // Nat视频链接达到最大，不允许新的Nat视频链接 */
//		public static final int NATCONNET_REACHED_MAX = 122;
//		/** // Tcp视频链接达到最大，不允许新的Tcp视频链接 */
//		public static final int TCPCONNET_REACHED_MAX = 123;
	}
	
	public synchronized static int getSeqNum() {
		mSeqNum += 1;
		return mSeqNum;
	}

}

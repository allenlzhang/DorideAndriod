
package com.carlt.chelepie.data.recorder;

public class PieInfo {

	// 设备ID
	private String deviceName;

	// 麦克风增强开关
	private boolean MicEnhance;

	// 麦克风增强（设置麦克风增强的时候谭工说要给他，具体干啥的不知道）
	private boolean MicGain;

	// 摄像模式
	private String CameraMode;
	

	private int brightness = 0;// 亮度
	private int constrast = 0;// 对比度
	private int saturation = 0;// 饱和度

	private int recordEnable = 0;// 抓拍是否带视频 1 是关联，0是不关联
	private int audioEnable = 0;// 是否录制,音频录音是否可用，0不可用，1可用
	private int resolution = 1;// 分辨率,1-720p,0-1080p
	private int totalSpace = 0;// M SDCard 总空间
	private int remainSpace = 0;// M SDCard 剩余空间
	private String buildTime = "";// buildTime 软件发布时间
	//内置软件版本号
	private String softVersion;
	//硬件版本信息
	private String hardVersion;
	//加密版本信息
	private String encryptVersion;
	
	
	public String getSoftVersion() {
		return softVersion;
	}

	public void setSoftVersion(String softVersion) {
		this.softVersion = softVersion;
	}

	public String getHardVersion() {
		return hardVersion;
	}

	public void setHardVersion(String hardVersion) {
		this.hardVersion = hardVersion;
	}

	public String getEncryptVersion() {
		return encryptVersion;
	}

	public void setEncryptVersion(String encryptVersion) {
		this.encryptVersion = encryptVersion;
	}

	public int getBrightness() {
		return brightness;
	}

	public void setBrightness(int brightness) {
		this.brightness = brightness;
	}

	public int getConstrast() {
		return constrast;
	}

	public void setConstrast(int constrast) {
		this.constrast = constrast;
	}

	public int getSaturation() {
		return saturation;
	}

	public void setSaturation(int saturation) {
		this.saturation = saturation;
	}

	public int getRecodEnable() {
		return recordEnable;
	}

	public void setRecodEnable(int recordEnable) {
		this.recordEnable = recordEnable;
	}

	public int getAudioEnable() {
		return audioEnable;
	}

	public void setAudioEnable(int audioEnable) {
		this.audioEnable = audioEnable;
	}

	public int getResolution() {
		return resolution;
	}

	public void setResolution(int resolution) {
		this.resolution = resolution;
	}

	public int getTotalSpace() {
		return totalSpace;
	}

	public void setTotalSpace(int totalSpace) {
		this.totalSpace = totalSpace;
	}

	public int getRemainSpace() {
		return remainSpace;
	}

	public void setRemainSpace(int remainSpace) {
		this.remainSpace = remainSpace;
	}

	public String getBuildTime() {
		return buildTime;
	}

	public void setBuildTime(String buildTime) {
		this.buildTime = buildTime;
	}

	public final static String CAMERAMODE1 = "16:9";
	public final static String CAMERAMODE2 = "4:3";
	public final static String CAMERAMODE3 = "2:1";
	public final static String IMAGEQUALITY_FHD = "FHD";// 1080p
	public final static String IMAGEQUALITY_HD = "HD";// 720p
	public final static String IMAGEQUALITY_SD = "SD";

	public final static int SIZE_1080P = 0;
	public final static int SIZE_720P = 1;

	// 图片码率度(谭工给的，暂时没啥用)
	private String video_quality_level;

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public boolean isMicEnhance() {
		return MicEnhance;
	}

	public void setMicEnhance(boolean micEnhance) {
		MicEnhance = micEnhance;
	}

	public boolean isMicGain() {
		return MicGain;
	}

	public void setMicGain(boolean micGain) {
		MicGain = micGain;
	}

	public String getCameraMode() {
		return CameraMode;
	}

	public void setCameraMode(String cameraMode) {
		CameraMode = cameraMode;
	}

	private static PieInfo mPieInfo;

	public static PieInfo getInstance() {
		if (mPieInfo == null) {
			mPieInfo = new PieInfo();
		}
		return mPieInfo;
	}

	private PieInfo() {
	};
}

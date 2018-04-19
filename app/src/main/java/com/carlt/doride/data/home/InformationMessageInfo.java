package com.carlt.doride.data.home;

public class InformationMessageInfo {

	private String id;
	// 关联ID
	// 跳转至远程诊断页面需要传的id
	private String relid;

	// 是否官方消息
	private int isoffical;
	// 标题
	private String title;
	// 内容
	private String content;
	// 内容索引
	private String contentReference;
	private boolean istop;
	// class1:一级分类:
	// 11 用车提醒，21 安防故障，31 奖品活动 41 行车信息 99 官方消息
	private int class1;
	public final static int C1_ALL = 0;
	public final static int C1_T1 = 11;// 11 用车提醒
	public final static int C1_T2 = 21;// 21 安防提醒
	public final static int C1_T3 = 31;// 31 奖品活动
	public final static int C1_T4 = 41;// 41 行车信息
	public final static int C1_T5 = 51;// 51 故障提醒
	public final static int C1_T6 = 61;// 61养护提醒
	public final static int C1_T7 = 62;// 62 车辆关怀
	public final static int C1_T9 = 99;// 99 官方消息
	public final static int C1_CHALLENGE = 100;// 挑战

	// class2:二级分类:
	// 1101 保养提醒,1102 预约成功,1103 违章提醒
	// 2101 安防提醒, 2102 故障提醒,2103 胎压提醒
	// 3101 奖品,3102 活动
	// 4101 日报,4102 周报,4103 月报,4104 解锁勋章,4105 创造新记录,4106 升级驾驶证
	private int class2;
	public final static int C1_T1_T1 = 1101;// 1101 保养提醒
	public final static int C1_T1_T2 = 1102;// 1102 预约成功
	public final static int C1_T1_T3 = 1103;// 1103 违章提醒
	public final static int C1_T1_T4 = 1104;// 1104 激活盒子
	public final static int C1_T1_T5 = 1105;// 1104 盒子失去连接

	public final static int C1_T2_T1 = 2101;// 2101 振动提醒
	public final static int C1_T2_T2 = 2102;// 2102 故障提醒
	public final static int C1_T2_T3 = 2103;// 2103 胎压提醒
	public final static int C1_T2_T4 = 2104;// 2104 缺电提醒
	public final static int C1_T2_T5 = 2105;// 2105 启动提醒
	public final static int C1_T2_T6 = 2106;// 2106 门窗提醒
	public final static int C1_T2_T7 = 2107;// 2107 拖车提醒
	public final static int C1_T2_T8 = 2108;// 2108 防盗报警提醒
	public final static int C1_T2_T9 = 2109;// 2108 熄火提醒
	public final static int C1_T2_T10 = 2110;// 2108 碰撞提醒

	public final static int C1_T3_T1 = 3101;// 3101 奖品
	public final static int C1_T3_T2 = 3102;// 3102 活动

	public final static int C1_T4_T1 = 4101;// 4101 日报
	public final static int C1_T4_T2 = 4102;// 4102 周报
	public final static int C1_T4_T3 = 4103;// 4103 月报
	public final static int C1_T4_T4 = 4104;// 4104 解锁勋章
	public final static int C1_T4_T5 = 4105;// 4105 创造新记录
	public final static int C1_T4_T6 = 4106;// 4106 升级驾驶证

	public final static int C1_T6_T1 = 6101;// 6101 养护建议

	// 奖品图片URL仅在奖品活动分类下有
	private String img;

	// 仅在行车信息分类下有
	private String miles;
	private String fuel;
	private String point;
	private String avgfuel;
	private String sumtime;
	private String maxspeed;
	private String date;
	// 消息创建时间
	private String createdate;

	// 内容索引最大字数
	public final static int ReferenceSize = 80;
	// 0:没有显示详情按钮
	// 1:代表点击展示详情
	// 2:代表点击收起详情
	public final static int FLAG_NONE = 0;
	public final static int FLAG_REFERENCE = 1;
	public final static int FLAG_ALL = 2;
	private int detial_flag;

	// 此标识用于奖品和活动
	// 0 : 未领取或未报名或问题未发送
	// 1 : 已领取或者已报名或问题已发送
	public final static int GOT_NO = 0;
	public final static int GOT_YES = 1;
	private int isgot;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}


	public int getIsoffical() {
		return isoffical;
	}

	public void setIsoffical(int isoffical) {
		this.isoffical = isoffical;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getClass1() {
		return class1;
	}

	public void setClass1(int class1) {
		this.class1 = class1;
	}

	public int getClass2() {
		return class2;
	}

	public void setClass2(int class2) {
		this.class2 = class2;
	}

	public String getCreatedate() {
		return createdate;
	}

	public void setCreatedate(String createdate) {
		this.createdate = createdate;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getMiles() {
		return miles;
	}

	public void setMiles(String miles) {
		this.miles = miles;
	}

	public String getFuel() {
		return fuel;
	}

	public boolean isIstop() {
		return istop;
	}

	public void setIstop(boolean istop) {
		this.istop = istop;
	}

	public void setFuel(String fuel) {
		this.fuel = fuel;
	}

	public String getPoint() {
		return point;
	}

	public void setPoint(String point) {
		this.point = point;
	}

	public String getAvgfuel() {
		return avgfuel;
	}

	public void setAvgfuel(String avgfuel) {
		this.avgfuel = avgfuel;
	}

	public String getSumtime() {
		return sumtime;
	}

	public void setSumtime(String sumtime) {
		this.sumtime = sumtime;
	}

	public String getMaxspeed() {
		return maxspeed;
	}

	public void setMaxspeed(String maxspeed) {
		this.maxspeed = maxspeed;
	}

	public String getContentReference() {
		return contentReference;
	}

	public void setContentReference(String contentReference) {
		this.contentReference = contentReference;
	}

	public int getDetial_flag() {
		return detial_flag;
	}

	public void setDetial_flag(int detial_flag) {
		this.detial_flag = detial_flag;
	}

	public String getRelid() {
		return relid;
	}

	public void setRelid(String relid) {
		this.relid = relid;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public int getIsgot() {
		return isgot;
	}

	public void setIsgot(int isgot) {
		this.isgot = isgot;
	}

}

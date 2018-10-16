package com.carlt.sesame.data.career;

public class ReportAllInfo {

//	/** cartype 同型车 **/
//	private String cartypemaxmaxspeed = ""; // 最高的最高时速
//	private String cartypeavgmaxspeed = ""; // 平均最高时速
//	private String cartypeminminfuel = ""; // 最低的日最低油耗
//	private String cartypeavgminfuel = ""; // 平均日最低油耗
//	private String cartypemaxmaxmiles = ""; // 最高的日最高里程
//	private String cartypeavgmaxmiles = ""; // 平均日最高里程
//	private String cartypemaxmaxfuel = ""; // 最高的日最多耗油量
//	private String cartypeavgmaxfuel = ""; // 平均日最多耗油量
//	private String cartypemaxmaxpoint = ""; // 最高的日最高得分
//	private String cartypeavgmaxpoint = ""; // 平均日最高得分
//	private String cartypemaxmaxavgspeed = ""; // 最高的日最高平均时速
//	private String cartypeavgmaxavgspeed = ""; // 平均日最高平均时速
//	private String cartypemaxmaxsumtime = ""; // 最高的日最多行车时间
//	private String cartypeavgmaxsumtime = ""; // 平均日最多行车时间
//	private String cartypeAvgspeed = ""; // 平均速度
//	private String cartypeAvgfuel = ""; // 平均油耗
//	private String cartypeMaxmiles = "";// 单日最高里程
//	private String cartypeAvgmiles = ""; // 平均历程
	/** all 全部 **/

	private String allmaxmaxspeed = ""; // 最高的最高时速
	private String allavgmaxspeed = ""; // 平均最高时速
	private String allminminfuel = ""; // 最低的日最低油耗
	private String allavgminfuel = ""; // 平均日最低油耗
	private String allmaxmaxmiles = ""; // 最高的日最高里程
	private String allavgmaxmiles = ""; // 平均日最高里程
	private String allmaxmaxfuel = ""; // 最高的日最多耗油量
	private String allavgmaxfuel = ""; // 平均日最多耗油量
	private String allmaxmaxpoint = ""; // 最高的日最高得分
	private String allavgmaxpoint = ""; // 平均日最高得分
	private String allmaxmaxavgspeed = ""; // 最高的日最高平均时速
	private String allavgmaxavgspeed = ""; // 平均日最高平均时速
	private String allmaxmaxsumtime = ""; // 最高的日最多行车时间
	private String allavgmaxsumtime = ""; // 平均日最多行车时间
	private String allAvgspeed = ""; // 平均速度
	private String allAvgfuel = ""; // 平均油耗
	private String allMaxmiles = "";// 单日最高里程
	private String allAvgmiles = ""; // 平均历程

	private String shareText = "";
	private String shareTitle = "";
	private String shareLink = "";

	/** user **/
	// 主键
	private String id = "";
	// 用户ID
	private String userId = "";
	// 生涯总共驾驶时间（小时）
	private String sumTime = "";
	// 生涯平均驾驶得分
	private String avgPoint = "";
	// 车技战胜百分比，如50,最大100
	private String rank = "";
	// 总油耗
	private String sumFuel = "";
	// 总里程
	private String sumMiles = "";
	// 平均速度
	private String avgSpeed = "";
	// 平均油耗
	private String avgFuel = "";
	// 最高时速
	private String maxSpeed = "";
	// 最高时速发生时间
	private String maxSpeedtime = "";
	// 最低油耗
	private String minFuel = "";
	// 最低油耗发生时间
	private String minFueltime = "";
	// 单日最高里程
	private String maxMiles = "";
	// 单日最高里程发生时间
	private String maxMilestime = "";
	// 单日最多油耗
	private String maxFuel = "";
	// 单日最多油耗发生时间
	private String maxFueltime = "";
	// 单日最高得分
	private String maxPoint = "";
	// 单日最高得分发生时间
	private String maxPointtime = "";
	// 日最高平均时速
	private String maxAvgspeed = "";
	// 日最高平均时速发生时间
	private String maxavgspeedtime = "";
	// 日最多行车时间
	private String maxSumtime = "";
	// 日最多行车时间发生时间
	private String maxsumtimetime = "";

	// "isshared": "0"
	private String isshared = "";

	// "今天我的车技堪比舒马赫"
	private String pointdesc = "";
	// "喝掉整个西湖水了"
	private String sumfueldesc = "";
	// "能绕地球两圈了"
	private String summilesdesc = "";
	// "乌龟"
	private String avgspeeddesc = "";
	// "平均油耗高"
	private String avgfueldesc = "";

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getSumTime() {
		return sumTime;
	}

	public void setSumTime(String sumTime) {
		this.sumTime = sumTime;
	}

	public String getAvgPoint() {
		return avgPoint;
	}

	public void setAvgPoint(String avgPoint) {
		this.avgPoint = avgPoint;
	}

	public String getRank() {
		return rank;
	}

	public void setRank(String rank) {
		this.rank = rank;
	}

	public String getSumFuel() {
		return sumFuel;
	}

	public void setSumFuel(String sumFuel) {
		this.sumFuel = sumFuel;
	}

	public String getSumMiles() {
		return sumMiles;
	}

	public void setSumMiles(String sumMiles) {
		this.sumMiles = sumMiles;
	}

	public String getAvgSpeed() {
		return avgSpeed;
	}

	public void setAvgSpeed(String avgSpeed) {
		this.avgSpeed = avgSpeed;
	}

	public String getAvgFuel() {
		return avgFuel;
	}

	public void setAvgFuel(String avgFuel) {
		this.avgFuel = avgFuel;
	}

	public String getMaxSpeed() {
		return maxSpeed;
	}

	public void setMaxSpeed(String maxSpeed) {
		this.maxSpeed = maxSpeed;
	}

	public String getMaxSpeedtime() {
		return maxSpeedtime;
	}

	public void setMaxSpeedtime(String maxSpeedtime) {
		this.maxSpeedtime = maxSpeedtime;
	}

	public String getMinFuel() {
		return minFuel;
	}

	public void setMinFuel(String minFuel) {
		this.minFuel = minFuel;
	}

	public String getMinFueltime() {
		return minFueltime;
	}

	public void setMinFueltime(String minFueltime) {
		this.minFueltime = minFueltime;
	}

	public String getMaxMiles() {
		return maxMiles;
	}

	public void setMaxMiles(String maxMiles) {
		this.maxMiles = maxMiles;
	}

	public String getMaxMilestime() {
		return maxMilestime;
	}

	public void setMaxMilestime(String maxMilestime) {
		this.maxMilestime = maxMilestime;
	}

	public String getMaxFuel() {
		return maxFuel;
	}

	public void setMaxFuel(String maxFuel) {
		this.maxFuel = maxFuel;
	}

	public String getMaxFueltime() {
		return maxFueltime;
	}

	public void setMaxFueltime(String maxFueltime) {
		this.maxFueltime = maxFueltime;
	}

	public String getMaxPoint() {
		return maxPoint;
	}

	public void setMaxPoint(String maxPoint) {
		this.maxPoint = maxPoint;
	}

	public String getMaxPointtime() {
		return maxPointtime;
	}

	public void setMaxPointtime(String maxPointtime) {
		this.maxPointtime = maxPointtime;
	}

	public String getMaxAvgspeed() {
		return maxAvgspeed;
	}

	public void setMaxAvgspeed(String maxAvgspeed) {
		this.maxAvgspeed = maxAvgspeed;
	}

	public String getMaxavgspeedtime() {
		return maxavgspeedtime;
	}

	public void setMaxavgspeedtime(String maxavgspeedtime) {
		this.maxavgspeedtime = maxavgspeedtime;
	}

	public String getIsshared() {
		return isshared;
	}

	public void setIsshared(String isshared) {
		this.isshared = isshared;
	}

	public String getPointdesc() {
		return pointdesc;
	}

	public void setPointdesc(String pointdesc) {
		this.pointdesc = pointdesc;
	}

	public String getSumfueldesc() {
		return sumfueldesc;
	}

	public void setSumfueldesc(String sumfueldesc) {
		this.sumfueldesc = sumfueldesc;
	}

	public String getSummilesdesc() {
		return summilesdesc;
	}

	public void setSummilesdesc(String summilesdesc) {
		this.summilesdesc = summilesdesc;
	}

	public String getAvgspeeddesc() {
		return avgspeeddesc;
	}

	public void setAvgspeeddesc(String avgspeeddesc) {
		this.avgspeeddesc = avgspeeddesc;
	}

	public String getAvgfueldesc() {
		return avgfueldesc;
	}

	public void setAvgfueldesc(String avgfueldesc) {
		this.avgfueldesc = avgfueldesc;
	}

//	public String getCartypeAvgspeed() {
//		return cartypeAvgspeed;
//	}
//
//	public void setCartypeAvgspeed(String cartypeAvgspeed) {
//		this.cartypeAvgspeed = cartypeAvgspeed;
//	}
//
//	public String getCartypeAvgfuel() {
//		return cartypeAvgfuel;
//	}
//
//	public void setCartypeAvgfuel(String cartypeAvgfuel) {
//		this.cartypeAvgfuel = cartypeAvgfuel;
//	}
//
//	public String getCartypeMaxmiles() {
//		return cartypeMaxmiles;
//	}
//
//	public void setCartypeMaxmiles(String cartypeMaxmiles) {
//		this.cartypeMaxmiles = cartypeMaxmiles;
//	}
//
//	public String getCartypeAvgmiles() {
//		return cartypeAvgmiles;
//	}
//
//	public void setCartypeAvgmiles(String cartypeAvgmiles) {
//		this.cartypeAvgmiles = cartypeAvgmiles;
//	}
//
//	public String getCartypemaxmaxspeed() {
//		return cartypemaxmaxspeed;
//	}
//
//	public void setCartypemaxmaxspeed(String cartypemaxmaxspeed) {
//		this.cartypemaxmaxspeed = cartypemaxmaxspeed;
//	}
//
//	public String getCartypeavgmaxspeed() {
//		return cartypeavgmaxspeed;
//	}
//
//	public void setCartypeavgmaxspeed(String cartypeavgmaxspeed) {
//		this.cartypeavgmaxspeed = cartypeavgmaxspeed;
//	}
//
//	public String getCartypeminminfuel() {
//		return cartypeminminfuel;
//	}
//
//	public void setCartypeminminfuel(String cartypeminminfuel) {
//		this.cartypeminminfuel = cartypeminminfuel;
//	}
//
//	public String getCartypeavgminfuel() {
//		return cartypeavgminfuel;
//	}
//
//	public void setCartypeavgminfuel(String cartypeavgminfuel) {
//		this.cartypeavgminfuel = cartypeavgminfuel;
//	}
//
//	public String getCartypemaxmaxmiles() {
//		return cartypemaxmaxmiles;
//	}
//
//	public void setCartypemaxmaxmiles(String cartypemaxmaxmiles) {
//		this.cartypemaxmaxmiles = cartypemaxmaxmiles;
//	}
//
//	public String getCartypeavgmaxmiles() {
//		return cartypeavgmaxmiles;
//	}
//
//	public void setCartypeavgmaxmiles(String cartypeavgmaxmiles) {
//		this.cartypeavgmaxmiles = cartypeavgmaxmiles;
//	}
//
//	public String getCartypemaxmaxfuel() {
//		return cartypemaxmaxfuel;
//	}
//
//	public void setCartypemaxmaxfuel(String cartypemaxmaxfuel) {
//		this.cartypemaxmaxfuel = cartypemaxmaxfuel;
//	}
//
//	public String getCartypeavgmaxfuel() {
//		return cartypeavgmaxfuel;
//	}
//
//	public void setCartypeavgmaxfuel(String cartypeavgmaxfuel) {
//		this.cartypeavgmaxfuel = cartypeavgmaxfuel;
//	}
//
//	public String getCartypemaxmaxpoint() {
//		return cartypemaxmaxpoint;
//	}
//
//	public void setCartypemaxmaxpoint(String cartypemaxmaxpoint) {
//		this.cartypemaxmaxpoint = cartypemaxmaxpoint;
//	}
//
//	public String getCartypeavgmaxpoint() {
//		return cartypeavgmaxpoint;
//	}
//
//	public void setCartypeavgmaxpoint(String cartypeavgmaxpoint) {
//		this.cartypeavgmaxpoint = cartypeavgmaxpoint;
//	}
//
//	public String getCartypemaxmaxavgspeed() {
//		return cartypemaxmaxavgspeed;
//	}
//
//	public void setCartypemaxmaxavgspeed(String cartypemaxmaxavgspeed) {
//		this.cartypemaxmaxavgspeed = cartypemaxmaxavgspeed;
//	}
//
//	public String getCartypeavgmaxavgspeed() {
//		return cartypeavgmaxavgspeed;
//	}
//
//	public void setCartypeavgmaxavgspeed(String cartypeavgmaxavgspeed) {
//		this.cartypeavgmaxavgspeed = cartypeavgmaxavgspeed;
//	}
//
//	public String getCartypemaxmaxsumtime() {
//		return cartypemaxmaxsumtime;
//	}
//
//	public void setCartypemaxmaxsumtime(String cartypemaxmaxsumtime) {
//		this.cartypemaxmaxsumtime = cartypemaxmaxsumtime;
//	}
//
//	public String getCartypeavgmaxsumtime() {
//		return cartypeavgmaxsumtime;
//	}
//
//	public void setCartypeavgmaxsumtime(String cartypeavgmaxsumtime) {
//		this.cartypeavgmaxsumtime = cartypeavgmaxsumtime;
//	}

	public String getAllmaxmaxspeed() {
		return allmaxmaxspeed;
	}

	public void setAllmaxmaxspeed(String allmaxmaxspeed) {
		this.allmaxmaxspeed = allmaxmaxspeed;
	}

	public String getAllavgmaxspeed() {
		return allavgmaxspeed;
	}

	public void setAllavgmaxspeed(String allavgmaxspeed) {
		this.allavgmaxspeed = allavgmaxspeed;
	}

	public String getAllminminfuel() {
		return allminminfuel;
	}

	public void setAllminminfuel(String allminminfuel) {
		this.allminminfuel = allminminfuel;
	}

	public String getAllavgminfuel() {
		return allavgminfuel;
	}

	public void setAllavgminfuel(String allavgminfuel) {
		this.allavgminfuel = allavgminfuel;
	}

	public String getAllmaxmaxmiles() {
		return allmaxmaxmiles;
	}

	public void setAllmaxmaxmiles(String allmaxmaxmiles) {
		this.allmaxmaxmiles = allmaxmaxmiles;
	}

	public String getAllavgmaxmiles() {
		return allavgmaxmiles;
	}

	public void setAllavgmaxmiles(String allavgmaxmiles) {
		this.allavgmaxmiles = allavgmaxmiles;
	}

	public String getAllmaxmaxfuel() {
		return allmaxmaxfuel;
	}

	public void setAllmaxmaxfuel(String allmaxmaxfuel) {
		this.allmaxmaxfuel = allmaxmaxfuel;
	}

	public String getAllavgmaxfuel() {
		return allavgmaxfuel;
	}

	public void setAllavgmaxfuel(String allavgmaxfuel) {
		this.allavgmaxfuel = allavgmaxfuel;
	}

	public String getAllmaxmaxpoint() {
		return allmaxmaxpoint;
	}

	public void setAllmaxmaxpoint(String allmaxmaxpoint) {
		this.allmaxmaxpoint = allmaxmaxpoint;
	}

	public String getAllavgmaxpoint() {
		return allavgmaxpoint;
	}

	public void setAllavgmaxpoint(String allavgmaxpoint) {
		this.allavgmaxpoint = allavgmaxpoint;
	}

	public String getAllmaxmaxavgspeed() {
		return allmaxmaxavgspeed;
	}

	public void setAllmaxmaxavgspeed(String allmaxmaxavgspeed) {
		this.allmaxmaxavgspeed = allmaxmaxavgspeed;
	}

	public String getAllavgmaxavgspeed() {
		return allavgmaxavgspeed;
	}

	public void setAllavgmaxavgspeed(String allavgmaxavgspeed) {
		this.allavgmaxavgspeed = allavgmaxavgspeed;
	}

	public String getAllmaxmaxsumtime() {
		return allmaxmaxsumtime;
	}

	public void setAllmaxmaxsumtime(String allmaxmaxsumtime) {
		this.allmaxmaxsumtime = allmaxmaxsumtime;
	}

	public String getAllavgmaxsumtime() {
		return allavgmaxsumtime;
	}

	public void setAllavgmaxsumtime(String allavgmaxsumtime) {
		this.allavgmaxsumtime = allavgmaxsumtime;
	}

	public String getAllAvgspeed() {
		return allAvgspeed;
	}

	public void setAllAvgspeed(String allAvgspeed) {
		this.allAvgspeed = allAvgspeed;
	}

	public String getAllAvgfuel() {
		return allAvgfuel;
	}

	public void setAllAvgfuel(String allAvgfuel) {
		this.allAvgfuel = allAvgfuel;
	}

	public String getAllMaxmiles() {
		return allMaxmiles;
	}

	public void setAllMaxmiles(String allMaxmiles) {
		this.allMaxmiles = allMaxmiles;
	}

	public String getAllAvgmiles() {
		return allAvgmiles;
	}

	public void setAllAvgmiles(String allAvgmiles) {
		this.allAvgmiles = allAvgmiles;
	}

	public String getMaxSumtime() {
		return maxSumtime;
	}

	public void setMaxSumtime(String maxSumtime) {
		this.maxSumtime = maxSumtime;
	}

	public String getMaxsumtimetime() {
		return maxsumtimetime;
	}

	public void setMaxsumtimetime(String maxsumtimetime) {
		this.maxsumtimetime = maxsumtimetime;
	}

	public String getShareText() {
		return shareText;
	}

	public void setShareText(String shareText) {
		this.shareText = shareText;
	}

	public String getShareTitle() {
		return shareTitle;
	}

	public void setShareTitle(String shareTitle) {
		this.shareTitle = shareTitle;
	}

	public String getShareLink() {
		return shareLink;
	}

	public void setShareLink(String shareLink) {
		this.shareLink = shareLink;
	}

}

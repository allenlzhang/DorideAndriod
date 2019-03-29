package com.carlt.doride.http.retrofitnet.model;

/**
 * Created by Marlon on 2019/3/22.
 */
public class GetCarInfo {

    private static GetCarInfo getCarInfo = null;

    private GetCarInfo(){}

    public static GetCarInfo getInstance(){
        if (getCarInfo == null){
            synchronized (GetCarInfo.class){
                if (getCarInfo == null){
                    getCarInfo = new GetCarInfo();
                }
            }
        }
        return getCarInfo;
    }

    public int      id;                 // id
    public int      cuscarId;           // 车辆档案id
    public String   orgId;              // 组织id
    public String   productId;          // 产品id
    public int      uid ;               // 用户id
    public int      dealerid;           // 所属经销商ID
    public int      deviceid;           // 设备id
    public String   deviceidstring;     // 设备String
    public String   serialCar;          // 车辆编号
    public int      deviceType;         // 设备类型，0=正常，1=更换设备
    public String   brandTitle;         // 品牌名称
    public String   modelTitle;         // 车系名称
    public String   optionTitle;        // 车型名称
    public int      brandid;            // 品牌ID
    public int      modelid;            // 车系ID
    public int      optionid;           // 车型ID
    public int      styleId;         // 具体车款ID
    public int      carType;            // 车辆类型 1=耗油车辆，2=耗电车辆，3-油电混合
    public String   carName;            // 车辆名称
    public String   carLogo;            // 车辆logo
    public String   color;              // 颜色
    public String   carProvince;        // 车牌省
    public String   carNO;              // 车牌号
    public String   standCarNo;         // 车架号
    public String   shortStandCarNO;    //查询违章车架号
    public String   province;           //省份码
    public String   city;               //城市码
    public String   engineno;           // 发动机号
    public String   registno;           // 登记证书
    public int      canQueryVio;        // 是否可以查询违章
    public int      enablePushVio;      // 是否允许推送违章消息
    public int      cityCodeId;         // 违章查询-城市代码ID
    public String   cityCode;           // 城市代码
    public int      secretaryID;        // 车秘书ID
    public int      licenceLevelID;     // 驾驶证等级ID
    public int      credit;             // 总积分
    public int      friends;            // 好友数量
    public String   licenceNumber;      // 驾驶证编号
    public String   licenceDate;        // 驾驶证编号
    public int      unReadMessage;      // 未读的车秘书消息数
    public String   latestMessage;      // 最新的一条车秘书消息
    public int      safetyCount;        // 安防提醒条数
    public String   safetyMsg;          // 最后一条安防提醒内容
    public int      updateDate;         // 更新时间
    public int      createDate;         // 绑定时间
    public long      buyDate;            // 购车时间
    public int      maintenInfo;        // 保养阶段
    public int      maintenMiles;       // 上次保养里程
    public long      maintenDate;        // 上次保养时间
    public int      maintenNextMiles;   // 下次保养公里数
    public long      maintenNextDate;    // 下次保养日期
    public int      isNextMain;         // 是否为新的保养阶段,1是(APP可以点击已保养按钮)
    public int      fixedMiles;         // APP人工里程修正
    public int      autorestart;        // 是否支持发动机启动技术,1支持,0不支持
    public int      plateDate;          // 上牌日期
    public int      insuranceId;        // 保险公司id
    public String   insuranceName;      // 保险公司名称
    public int      NDTC;               // 最新故障数量
    public int      applicantDate;      // 投保日期
    public int      applicantLong;      // 投保时间
    // 省略经销商流失
    public String   carPin;             // 车辆pin码
    public int      carPinStatus;       // 车辆pin码
    public String   bindvin;            // 绑定vin码
    public int      installOrder;       // '绑定方式：1=前装绑定，2=后装绑定'
    // 省略部分保险数据
    public int      certificateId;      // 车辆凭证附件ID（行驶证或购车发票）
    public int      firstActiveTime;    // 首次激活时间
    public BaseErr    err ;               // 错误描述
     // 附加参数
    public String   vin;                // 车架号(standcarno bindvin)
    public String   deviceNum;          // 设备号(同deviceidString)
    public int      withTbox;           // 出厂是否内置T-box（前后装） 1-是(前装) 2-否(后装)
    public int      remoteStatus;       // 远程激活状态,设备激活状态 0-未激活  1-正在激活  2-激活成功  3-激活失败
    public int      recodeStatus;       // 行车记录仪激活状态
    public int      machineStatus;      // 车机激活状态
    public int      isUpgrade;          // 是否升级 0- 正常 1-升级中
    public int      withNetwork;        // T-BOX供网: 1-支持 2-不支持
    // car_member_car_ext 车辆信息扩展表
    public int      driverLicenseImg;   // 驾驶证照片
    public String   pledge;             // 押金信息
    public String   insurance;          // 投保公司信息
    public int      chargetime;         // 定时充电时间
    public int      inspectTime;        // 上次年检时间
    public int      registerTime;       // 注册到车管所时间
    public int      nextInspectTime;    // 下次年检时间
    // car_authorization 车辆授权
    public int      type;               // 1 获取我的车辆信息，2获取被授权列表信息，3 获取我和被授权的列表
    public int      authStatus;         // 授权状态,1未授权，2授权中
    public int      authType;           // 授权类型，1授权别人，2 被授权
    public int      authStartTime;      // 授权开始时间
    public int      authEndTime;        // 授权结束时间
    public int      authId;             // 授权id
    public int      dorcenCarDisplay;   // 座驾显示界面: 0-旧界面 1-新界面
    public void setCarInfo(GetCarInfo carInfo){
        id = carInfo.id;
        cuscarId = carInfo.cuscarId;
        orgId = carInfo.orgId;
        productId = carInfo.productId;
        uid  = carInfo.uid ;
        dealerid = carInfo.dealerid;
        deviceid = carInfo.deviceid;
        deviceidstring = carInfo.deviceidstring;
        serialCar = carInfo.serialCar;
        deviceType = carInfo.deviceType;
        brandTitle = carInfo.brandTitle;
        modelTitle = carInfo.modelTitle;
        optionTitle = carInfo.optionTitle;
        brandid = carInfo.brandid;
        modelid = carInfo.modelid;
        optionid = carInfo.optionid;
        styleId = carInfo.styleId;
        carType = carInfo.carType;
        carName = carInfo.carName;
        carLogo = carInfo.carLogo;
        color = carInfo.color;
        carProvince = carInfo.carProvince;
        carNO = carInfo.carNO;
        standCarNo = carInfo.standCarNo;
        shortStandCarNO = carInfo.shortStandCarNO;
        province = carInfo.province;
        city = carInfo.city;
        engineno = carInfo.engineno;
        registno = carInfo.registno;
        canQueryVio = carInfo.canQueryVio;
        enablePushVio = carInfo.enablePushVio;
        cityCodeId = carInfo.cityCodeId;
        cityCode = carInfo.cityCode;
        secretaryID = carInfo.secretaryID;
        licenceLevelID = carInfo.licenceLevelID;
        credit = carInfo.credit;
        friends = carInfo.friends;
        licenceNumber = carInfo.licenceNumber;
        licenceDate = carInfo.licenceDate;
        unReadMessage = carInfo.unReadMessage;
        latestMessage = carInfo.latestMessage;
        safetyCount = carInfo.safetyCount;
        safetyMsg = carInfo.safetyMsg;
        updateDate = carInfo.updateDate;
        createDate = carInfo.createDate;
        buyDate = carInfo.buyDate;
        maintenInfo = carInfo.maintenInfo;
        maintenMiles = carInfo.maintenMiles;
        maintenDate = carInfo.maintenDate;
        maintenNextMiles = carInfo.maintenNextMiles;
        maintenNextDate = carInfo.maintenNextDate;
        isNextMain = carInfo.isNextMain;
        fixedMiles = carInfo.fixedMiles;
        autorestart = carInfo.autorestart;
        plateDate = carInfo.plateDate;
        insuranceId = carInfo.insuranceId;
        insuranceName = carInfo.insuranceName;
        NDTC = carInfo.NDTC;
        applicantDate = carInfo.applicantDate;
        applicantLong = carInfo.applicantLong;
        carPin = carInfo.carPin;
        carPinStatus = carInfo.carPinStatus;
        bindvin = carInfo.bindvin;
        installOrder = carInfo.installOrder;
        certificateId = carInfo.certificateId;
        firstActiveTime = carInfo.firstActiveTime;
        err  = carInfo.err ;
        vin = carInfo.vin;
        deviceNum = carInfo.deviceNum;
        withTbox = carInfo.withTbox;
        remoteStatus = carInfo.remoteStatus;
        recodeStatus = carInfo.recodeStatus;
        machineStatus = carInfo.machineStatus;
        isUpgrade = carInfo.isUpgrade;
        withNetwork = carInfo.withNetwork;
        driverLicenseImg = carInfo.driverLicenseImg;
        pledge = carInfo.pledge;
        insurance = carInfo.insurance;
        chargetime = carInfo.chargetime;
        inspectTime = carInfo.inspectTime;
        registerTime = carInfo.registerTime;
        nextInspectTime = carInfo.nextInspectTime;
        type = carInfo.type;
        authStatus = carInfo.authStatus;
        authType = carInfo.authType;
        authStartTime = carInfo.authStartTime;
        authEndTime = carInfo.authEndTime;
        authId = carInfo.authId;
        dorcenCarDisplay = carInfo.dorcenCarDisplay;
    }
    public void initCarInfo(){
        id = 0;
        cuscarId = 0;
        orgId = "";
        productId = "";
        uid  = 0;
        dealerid = 0;
        deviceid = 0;
        deviceidstring = "";
        serialCar = "";
        deviceType = 0;
        brandTitle = "";
        modelTitle = "";
        optionTitle = "";
        brandid = 0;
        modelid = 0;
        optionid = 0;
        styleId = 0;
        carType = 0;
        carName = "";
        carLogo = "";
        color = "";
        carProvince = "";
        carNO = "";
        standCarNo = "";
        shortStandCarNO = "";
        province = "";
        city = "";
        engineno = "";
        registno = "";
        canQueryVio = 0;
        enablePushVio = 0;
        cityCodeId = 0;
        cityCode = "";
        secretaryID = 0;
        licenceLevelID = 0;
        credit = 0;
        friends = 0;
        licenceNumber = "";
        licenceDate = "";
        unReadMessage = 0;
        latestMessage = "";
        safetyCount = 0;
        safetyMsg = "";
        updateDate = 0;
        createDate = 0;
        buyDate = 0;
        maintenInfo = 0;
        maintenMiles = 0;
        maintenDate = 0;
        maintenNextMiles = 0;
        maintenNextDate = 0;
        isNextMain = 0;
        fixedMiles = 0;
        autorestart = 0;
        plateDate = 0;
        insuranceId = 0;
        insuranceName = "";
        NDTC = 0;
        applicantDate = 0;
        applicantLong = 0;
        carPin = "";
        carPinStatus = 0;
        bindvin = "";
        installOrder = 0;
        certificateId = 0;
        firstActiveTime = 0;
        err  = null;
        vin = "";
        deviceNum = "";
        withTbox = 0;
        remoteStatus = 0;
        recodeStatus = 0;
        machineStatus = 0;
        isUpgrade = 0;
        withNetwork = 0;
        driverLicenseImg = 0;
        pledge = "";
        insurance = "";
        chargetime = 0;
        inspectTime = 0;
        registerTime = 0;
        nextInspectTime = 0;
        type = 0;
        authStatus = 0;
        authType = 0;
        authStartTime = 0;
        authEndTime = 0;
        authId = 0;
        dorcenCarDisplay = 0;
    }
}

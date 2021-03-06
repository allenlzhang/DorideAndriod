package com.carlt.doride.http.retrofitnet.model;

import com.carlt.doride.systemconfig.URLConfig;
import com.carlt.doride.utils.SharepUtil;

import java.io.Serializable;

/**
 * Created by Marlon on 2019/3/22.
 */
public class GetCarInfo implements Serializable {

    private static GetCarInfo getCarInfo = null;

    private GetCarInfo() {
    }

    public static GetCarInfo getInstance() {
        if (getCarInfo == null) {
            synchronized (GetCarInfo.class) {
                if (getCarInfo == null) {
                    getCarInfo = new GetCarInfo();
                }
            }
        }
        return getCarInfo;
    }

    public int     id;                 // id
    public int     cuscarId;           // 车辆档案id
    public String  orgId           = "";              // 组织id
    public String  productId       = "";          // 产品id
    public int     uid;               // 用户id
    public int     dealerid;           // 所属经销商ID
    public int     deviceid;           // 设备id
    public String  deviceidstring  = "";     // 设备String
    public String  serialCar       = "";          // 车辆编号
    public int     deviceType;         // 设备类型，0=正常，1=更换设备
    public String  brandTitle      = "";         // 品牌名称
    public String  modelTitle      = "";         // 车系名称
    public String  optionTitle     = "";        // 车型名称
    public int     brandid;            // 品牌ID
    public int     modelid;            // 车系ID
    public int     optionid;           // 车型ID
    public int     styleId;         // 具体车款ID
    public int     carType;            // 车辆类型 1=耗油车辆，2=耗电车辆，3-油电混合
    public String  carName         = "";            // 车辆名称
    public String  carLogo         = "";            // 车辆logo
    public String  color           = "";              // 颜色
    public String  carProvince     = "";        // 车牌省
    public String  carNO           = "";              // 车牌号
    public String  standCarNo      = "";         // 车架号
    public String  shortStandCarNO = "";    //查询违章车架号
    public String  province        = "";           //省份码
    public String  city            = "";               //城市码
    public String  engineno        = "";           // 发动机号
    public String  registno        = "";           // 登记证书
    public int     canQueryVio;        // 是否可以查询违章
    public int     enablePushVio;      // 是否允许推送违章消息
    public int     cityCodeId;         // 违章查询-城市代码ID
    public String  cityCode        = "";           // 城市代码
    public int     secretaryID;        // 车秘书ID
    public int     licenceLevelID;     // 驾驶证等级ID
    public int     credit;             // 总积分
    public int     friends;            // 好友数量
    public String  licenceNumber   = "";      // 驾驶证编号
    public String  licenceDate     = "";        // 驾驶证编号
    public int     unReadMessage;      // 未读的车秘书消息数
    public String  latestMessage   = "";      // 最新的一条车秘书消息
    public int     safetyCount;        // 安防提醒条数
    public String  safetyMsg       = "";          // 最后一条安防提醒内容
    public int     updateDate;         // 更新时间
    public int     createDate;         // 绑定时间
    public long    buyDate;            // 购车时间
    public int     maintenInfo;        // 保养阶段
    public int     maintenMiles;       // 上次保养里程
    public long    maintenDate;        // 上次保养时间
    public int     maintenNextMiles;   // 下次保养公里数
    public long    maintenNextDate;    // 下次保养日期
    public int     isNextMain;         // 是否为新的保养阶段,1是(APP可以点击已保养按钮)
    public int     fixedMiles;         // APP人工里程修正
    public int     autorestart;        // 是否支持发动机启动技术,1支持,0不支持
    public int     plateDate;          // 上牌日期
    public int     insuranceId;        // 保险公司id
    public String  insuranceName   = "";      // 保险公司名称
    public int     NDTC;               // 最新故障数量
    public int     applicantDate;      // 投保日期
    public int     applicantLong;      // 投保时间
    // 省略经销商流失
    public String  carPin          = "";             // 车辆pin码
    public int     carPinStatus;       // 车辆pin码
    public String  bindvin         = "";            // 绑定vin码
    public int     installOrder;       // '绑定方式：1=前装绑定，2=后装绑定'
    // 省略部分保险数据
    public int     certificateId;      // 车辆凭证附件ID（行驶证或购车发票）
    public int     firstActiveTime;    // 首次激活时间
    public BaseErr err;               // 错误描述
    // 附加参数
    public String  vin             = "";                // 车架号(standcarno bindvin)
    public String  deviceNum       = "";          // 设备号(同deviceidString)
    public int     withTbox;           // 出厂是否内置T-box（前后装） 1-是(前装) 2-否(后装)
    public int     remoteStatus;       // 远程激活状态,设备激活状态 0-未激活  1-正在激活  2-激活成功  3-激活失败
    public int     recodeStatus;       // 行车记录仪激活状态
    public int     machineStatus;      // 车机激活状态
    public int     isUpgrade;          // 是否升级 0- 正常 1-升级中
    public int     withNetwork;        // T-BOX供网: 1-支持 2-不支持
    // car_member_car_ext 车辆信息扩展表
    public int     driverLicenseImg;   // 驾驶证照片
    public String  pledge          = "";             // 押金信息
    public String  insurance       = "";          // 投保公司信息
    public int     chargetime;         // 定时充电时间
    public int     inspectTime;        // 上次年检时间
    public int     registerTime;       // 注册到车管所时间
    public int     nextInspectTime;    // 下次年检时间
    // car_authorization 车辆授权
    public int     type;               // 1 获取我的车辆信息，2获取被授权列表信息，3 获取我和被授权的列表
    public int     authStatus;         // 授权状态,1未授权，2授权中
    public int     authType;           // 授权类型，1授权别人，2 被授权
    public int     authStartTime;      // 授权开始时间
    public int     authEndTime;        // 授权结束时间
    public int     authId;             // 授权id
    public int     dorcenCarDisplay;   // 座驾显示界面: 0-旧界面 1-新界面
    public boolean isFail;//是否激活失败过

    @Override
    public String toString() {
        return "GetCarInfo{" +
                "id=" + id +
                ", cuscarId=" + cuscarId +
                ", orgId='" + orgId + '\'' +
                ", productId='" + productId + '\'' +
                ", uid=" + uid +
                ", dealerid=" + dealerid +
                ", deviceid=" + deviceid +
                ", deviceidstring='" + deviceidstring + '\'' +
                ", serialCar='" + serialCar + '\'' +
                ", deviceType=" + deviceType +
                ", brandTitle='" + brandTitle + '\'' +
                ", modelTitle='" + modelTitle + '\'' +
                ", optionTitle='" + optionTitle + '\'' +
                ", brandid=" + brandid +
                ", modelid=" + modelid +
                ", optionid=" + optionid +
                ", styleId=" + styleId +
                ", carType=" + carType +
                ", carName='" + carName + '\'' +
                ", carLogo='" + carLogo + '\'' +
                ", color='" + color + '\'' +
                ", carProvince='" + carProvince + '\'' +
                ", carNO='" + carNO + '\'' +
                ", standCarNo='" + standCarNo + '\'' +
                ", shortStandCarNO='" + shortStandCarNO + '\'' +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", engineno='" + engineno + '\'' +
                ", registno='" + registno + '\'' +
                ", canQueryVio=" + canQueryVio +
                ", enablePushVio=" + enablePushVio +
                ", cityCodeId=" + cityCodeId +
                ", cityCode='" + cityCode + '\'' +
                ", secretaryID=" + secretaryID +
                ", licenceLevelID=" + licenceLevelID +
                ", credit=" + credit +
                ", friends=" + friends +
                ", licenceNumber='" + licenceNumber + '\'' +
                ", licenceDate='" + licenceDate + '\'' +
                ", unReadMessage=" + unReadMessage +
                ", latestMessage='" + latestMessage + '\'' +
                ", safetyCount=" + safetyCount +
                ", safetyMsg='" + safetyMsg + '\'' +
                ", updateDate=" + updateDate +
                ", createDate=" + createDate +
                ", buyDate=" + buyDate +
                ", maintenInfo=" + maintenInfo +
                ", maintenMiles=" + maintenMiles +
                ", maintenDate=" + maintenDate +
                ", maintenNextMiles=" + maintenNextMiles +
                ", maintenNextDate=" + maintenNextDate +
                ", isNextMain=" + isNextMain +
                ", fixedMiles=" + fixedMiles +
                ", autorestart=" + autorestart +
                ", plateDate=" + plateDate +
                ", insuranceId=" + insuranceId +
                ", insuranceName='" + insuranceName + '\'' +
                ", NDTC=" + NDTC +
                ", applicantDate=" + applicantDate +
                ", applicantLong=" + applicantLong +
                ", carPin='" + carPin + '\'' +
                ", carPinStatus=" + carPinStatus +
                ", bindvin='" + bindvin + '\'' +
                ", installOrder=" + installOrder +
                ", certificateId=" + certificateId +
                ", firstActiveTime=" + firstActiveTime +
                ", err=" + err +
                ", vin='" + vin + '\'' +
                ", deviceNum='" + deviceNum + '\'' +
                ", withTbox=" + withTbox +
                ", remoteStatus=" + remoteStatus +
                ", recodeStatus=" + recodeStatus +
                ", machineStatus=" + machineStatus +
                ", isUpgrade=" + isUpgrade +
                ", withNetwork=" + withNetwork +
                ", driverLicenseImg=" + driverLicenseImg +
                ", pledge='" + pledge + '\'' +
                ", insurance='" + insurance + '\'' +
                ", chargetime=" + chargetime +
                ", inspectTime=" + inspectTime +
                ", registerTime=" + registerTime +
                ", nextInspectTime=" + nextInspectTime +
                ", type=" + type +
                ", authStatus=" + authStatus +
                ", authType=" + authType +
                ", authStartTime=" + authStartTime +
                ", authEndTime=" + authEndTime +
                ", authId=" + authId +
                ", dorcenCarDisplay=" + dorcenCarDisplay +
                '}';
    }

    public void setCarInfo(GetCarInfo info) {
        SharepUtil.putByBean(URLConfig.CAR_INFO,info);
        GetCarInfo beanFromSp = SharepUtil.getBeanFromSp(URLConfig.CAR_INFO);
        id = beanFromSp.id;
        cuscarId = beanFromSp.cuscarId;
        orgId = beanFromSp.orgId;
        productId = beanFromSp.productId;
        uid = beanFromSp.uid;
        dealerid = beanFromSp.dealerid;
        deviceid = beanFromSp.deviceid;
        deviceidstring = beanFromSp.deviceidstring;
        serialCar = beanFromSp.serialCar;
        deviceType = beanFromSp.deviceType;
        brandTitle = beanFromSp.brandTitle;
        modelTitle = beanFromSp.modelTitle;
        optionTitle = beanFromSp.optionTitle;
        brandid = beanFromSp.brandid;
        modelid = beanFromSp.modelid;
        optionid = beanFromSp.optionid;
        styleId = beanFromSp.styleId;
        carType = beanFromSp.carType;
        carName = beanFromSp.carName;
        carLogo = beanFromSp.carLogo;
        color = beanFromSp.color;
        carProvince = beanFromSp.carProvince;
        carNO = beanFromSp.carNO;
        standCarNo = beanFromSp.standCarNo;
        shortStandCarNO = beanFromSp.shortStandCarNO;
        province = beanFromSp.province;
        city = beanFromSp.city;
        engineno = beanFromSp.engineno;
        registno = beanFromSp.registno;
        canQueryVio = beanFromSp.canQueryVio;
        enablePushVio = beanFromSp.enablePushVio;
        cityCodeId = beanFromSp.cityCodeId;
        cityCode = beanFromSp.cityCode;
        secretaryID = beanFromSp.secretaryID;
        licenceLevelID = beanFromSp.licenceLevelID;
        credit = beanFromSp.credit;
        friends = beanFromSp.friends;
        licenceNumber = beanFromSp.licenceNumber;
        licenceDate = beanFromSp.licenceDate;
        unReadMessage = beanFromSp.unReadMessage;
        latestMessage = beanFromSp.latestMessage;
        safetyCount = beanFromSp.safetyCount;
        safetyMsg = beanFromSp.safetyMsg;
        updateDate = beanFromSp.updateDate;
        createDate = beanFromSp.createDate;
        buyDate = beanFromSp.buyDate;
        maintenInfo = beanFromSp.maintenInfo;
        maintenMiles = beanFromSp.maintenMiles;
        maintenDate = beanFromSp.maintenDate;
        maintenNextMiles = beanFromSp.maintenNextMiles;
        maintenNextDate = beanFromSp.maintenNextDate;
        isNextMain = beanFromSp.isNextMain;
        fixedMiles = beanFromSp.fixedMiles;
        autorestart = beanFromSp.autorestart;
        plateDate = beanFromSp.plateDate;
        insuranceId = beanFromSp.insuranceId;
        insuranceName = beanFromSp.insuranceName;
        NDTC = beanFromSp.NDTC;
        applicantDate = beanFromSp.applicantDate;
        applicantLong = beanFromSp.applicantLong;
        carPin = beanFromSp.carPin;
        carPinStatus = beanFromSp.carPinStatus;
        bindvin = beanFromSp.bindvin;
        installOrder = beanFromSp.installOrder;
        certificateId = beanFromSp.certificateId;
        firstActiveTime = beanFromSp.firstActiveTime;
        err = beanFromSp.err;
        vin = beanFromSp.vin;
        deviceNum = beanFromSp.deviceNum;
        withTbox = beanFromSp.withTbox;
        remoteStatus = beanFromSp.remoteStatus;
        recodeStatus = beanFromSp.recodeStatus;
        machineStatus = beanFromSp.machineStatus;
        isUpgrade = beanFromSp.isUpgrade;
        withNetwork = beanFromSp.withNetwork;
        driverLicenseImg = beanFromSp.driverLicenseImg;
        pledge = beanFromSp.pledge;
        insurance = beanFromSp.insurance;
        chargetime = beanFromSp.chargetime;
        inspectTime = beanFromSp.inspectTime;
        registerTime = beanFromSp.registerTime;
        nextInspectTime = beanFromSp.nextInspectTime;
        type = beanFromSp.type;
        authStatus = beanFromSp.authStatus;
        authType = beanFromSp.authType;
        authStartTime = beanFromSp.authStartTime;
        authEndTime = beanFromSp.authEndTime;
        authId = beanFromSp.authId;
        dorcenCarDisplay = beanFromSp.dorcenCarDisplay;
    }

    public void initCarInfo() {
        id = 0;
        cuscarId = 0;
        orgId = "";
        productId = "";
        uid = 0;
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
        err = null;
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

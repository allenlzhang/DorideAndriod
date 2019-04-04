package com.carlt.doride.http.retrofitnet.model;

import java.io.Serializable;

/**
 * Created by Marlon on 2019/3/19.
 */
public class UserInfo implements Serializable {

    private static UserInfo userInfo = null;

    private UserInfo (){}

    public static UserInfo getInstance(){
        if (userInfo == null){
            synchronized (UserInfo.class){
                if (userInfo == null){
                    userInfo = new UserInfo();
                }
            }
        }
        return userInfo;
    }

    public int id;
    public String orgId = "";    // 机构ID
    public String account = "";  // 登录账号
    public String accountId = "";// 账号id
    public String mobile = "";   // 手机号
    public String productId = "";// 产品ID
    public String appId = "";    // 应用ID
    public int accountState;// 账户状态，车联网平台　１正常，２冻结
    public int authenState; // 是否实名认证
    public int ownerState;  // 成为车主
    public int lastlogin;   // 最后登录时间
    public int avatarId;    // 头像ID
    public int createDate;  // 用户创建时间
    public int logoutState; // 用户注销状态
    public BaseErr err;     // 错误描述
    public String password = ""; // 登录密码
    public int gender;      // 性别
    public String loginoauth = ""; // 登陆oauth随机码
    public int ip;          // 用户IP来源
    public int thisLogin;   // 本次登录时间
    public String authenName = ""; // 认证名字
    public String authenCard = ""; // 身份证号码
    public String realName = "";   // 真是姓名
    public int dealerId;      // 经销商ID
    public int loginVersion;  // 登录系统版本
    public String lastLoginAppType = ""; // 最后登录平台类型
    public int loginTimes;      // 登录次数
    public int remotePwdSwitch; // 5分钟免密开关  1-開
    public String remotePwd = "";    // 远程密码
    public int userFreeze;// 用户冻结
    public String avatarFile = ""; // 用户头像文件路径
    public int alipayAuth;  // 支付宝实名认证状态  1-未认证  2-已认证
    public int faceId;      // 人脸图片id
    public String faceFile = ""; // 人脸图片文件路径
    public int identityAuth; // 身份证实名认证状态  1-未认证  2-已认证
    public int alipayLogin; // 是否绑定支付宝登录  1-未绑定  2-已绑定
    public int wechatLogin; // 是否绑定微信登录  1-未绑定  2-已绑定
    public String userName = ""; //用户名
    public String isAuthen = ""; //是否实名认证（旧版）
    public int lessPwdSwitch; //免密开关
    public int isSetRemotePwd; //是否设置远程密码 1-已设置 0-未设置
    public void setUserInfo(UserInfo userInfo){
        id = userInfo.id;
        orgId = userInfo.orgId;
        account = userInfo.account;
        accountId = userInfo.accountId;
        mobile = userInfo.mobile;
        productId = userInfo.productId;
        appId = userInfo.appId;
        accountState = userInfo.accountState;
        authenState = userInfo.authenState;
        ownerState = userInfo.ownerState;
        lastlogin = userInfo.lastlogin;
        avatarId = userInfo.avatarId;
        createDate = userInfo.createDate;
        logoutState = userInfo.logoutState;
        err = userInfo.err;
        password = userInfo.password;
        gender = userInfo.gender;
        loginoauth = userInfo.loginoauth;
        ip = userInfo.ip;
        thisLogin = userInfo.thisLogin;
        authenName = userInfo.authenName;
        authenCard = userInfo.authenCard;
        realName = userInfo.realName;
        dealerId = userInfo.dealerId;
        loginVersion = userInfo.loginVersion;
        lastLoginAppType = userInfo.lastLoginAppType;
        loginTimes = userInfo.loginTimes;
        remotePwdSwitch = userInfo.remotePwdSwitch;
        remotePwd = userInfo.remotePwd;
        userFreeze = userInfo.userFreeze;
        avatarFile = userInfo.avatarFile;
        alipayAuth = userInfo.alipayAuth;
        faceId = userInfo.faceId;
        faceFile = userInfo.faceFile;
        identityAuth = userInfo.identityAuth;
        alipayLogin = userInfo.alipayLogin;
        wechatLogin = userInfo.wechatLogin;
        userName = userInfo.userName;
        isAuthen = userInfo.isAuthen;
        lessPwdSwitch = userInfo.lessPwdSwitch;
        isSetRemotePwd = userInfo.isSetRemotePwd;
    }
    public void initUserInfo(){
        id = 0;
        orgId = "";
        account = "";
        accountId = "";
        mobile = "";
        productId = "";
        appId = "";
        accountState = 0;
        authenState = 0;
        ownerState = 0;
        lastlogin = 0;
        avatarId = 0;
        createDate = 0;
        logoutState = 0;
        err = null;
        password = "";
        gender = 0;
        loginoauth = "";
        ip = 0;
        thisLogin = 0;
        authenName = "";
        authenCard = "";
        realName = "";
        dealerId = 0;
        loginVersion = 0;
        lastLoginAppType = "";
        loginTimes = 0;
        remotePwdSwitch = 0;
        remotePwd = "";
        userFreeze = 0;
        avatarFile = "";
        alipayAuth = 0;
        faceId = 0;
        faceFile = "";
        identityAuth = 0;
        alipayLogin = 0;
        wechatLogin = 0;
        userName = "";
        isAuthen = "";
        lessPwdSwitch = 0;
        isSetRemotePwd = 0;
    }
    @Override
    public String toString() {
        return "UserInfo{" +
                "id=" + id +
                ", orgId='" + orgId + '\'' +
                ", account='" + account + '\'' +
                ", accountId='" + accountId + '\'' +
                ", mobile='" + mobile + '\'' +
                ", productId='" + productId + '\'' +
                ", appId='" + appId + '\'' +
                ", accountState=" + accountState +
                ", authenState=" + authenState +
                ", ownerState=" + ownerState +
                ", lastlogin=" + lastlogin +
                ", avatarId=" + avatarId +
                ", createDate=" + createDate +
                ", logoutState=" + logoutState +
                ", err=" + err +
                ", password='" + password + '\'' +
                ", gender=" + gender +
                ", loginoauth='" + loginoauth + '\'' +
                ", ip=" + ip +
                ", thisLogin=" + thisLogin +
                ", authenName='" + authenName + '\'' +
                ", authenCard='" + authenCard + '\'' +
                ", realName='" + realName + '\'' +
                ", dealerId=" + dealerId +
                ", loginVersion=" + loginVersion +
                ", lastLoginAppType='" + lastLoginAppType + '\'' +
                ", loginTimes=" + loginTimes +
                ", remotePwdSwitch=" + remotePwdSwitch +
                ", remotePwd='" + remotePwd + '\'' +
                ", userFreeze=" + userFreeze +
                ", avatarFile='" + avatarFile + '\'' +
                ", alipayAuth=" + alipayAuth +
                ", faceId=" + faceId +
                ", faceFile='" + faceFile + '\'' +
                ", identityAuth=" + identityAuth +
                ", alipayLogin=" + alipayLogin +
                ", wechatLogin=" + wechatLogin +
                ", userName='" + userName + '\'' +
                ", isAuthen='" + isAuthen + '\'' +
                ", lessPwdSwitch=" + lessPwdSwitch +
                ", isSetRemotePwd=" + isSetRemotePwd +
                '}';
    }
}

package com.carlt.sesame.control;

import android.app.Activity;
import android.os.Handler;
import android.text.TextUtils;

import com.carlt.doride.DorideApplication;
import com.carlt.doride.http.retrofitnet.model.ContactsInfo;
import com.carlt.doride.http.retrofitnet.model.GetCarInfo;
import com.carlt.doride.http.retrofitnet.model.OtherInfo;
import com.carlt.doride.http.retrofitnet.model.UserInfo;
import com.carlt.doride.protocolparser.TrafficPackagePurchaseListParser;
import com.carlt.doride.utils.MyTimeUtils;
import com.carlt.sesame.data.BaseResponseInfo;
import com.carlt.sesame.data.SesameLoginInfo;
import com.carlt.sesame.data.UploadImgInfo;
import com.carlt.sesame.data.car.CarInfo;
import com.carlt.sesame.data.car.CarStatuInfo;
import com.carlt.sesame.data.car.CheckFaultInfo;
import com.carlt.sesame.data.car.HelpPhoneInfo;
import com.carlt.sesame.data.car.PhysicalExaminationInfo;
import com.carlt.sesame.data.car.PostViolationInfo;
import com.carlt.sesame.data.car.ViolationInfo;
import com.carlt.sesame.data.career.ChallengeInfo;
import com.carlt.sesame.data.career.ChallengeScore;
import com.carlt.sesame.data.career.DayOrderStateInfo;
import com.carlt.sesame.data.career.DiagnoseInfo;
import com.carlt.sesame.data.career.GotMediaInfo;
import com.carlt.sesame.data.career.LicenceLevelInfo;
import com.carlt.sesame.data.career.MedalInfo;
import com.carlt.sesame.data.career.MonthOrderStateInfo;
import com.carlt.sesame.data.career.PrizeInfo;
import com.carlt.sesame.data.career.RecommendSalesInfo;
import com.carlt.sesame.data.career.ReportAllInfo;
import com.carlt.sesame.data.career.ReportDayInfo;
import com.carlt.sesame.data.career.ReportMonthInfo;
import com.carlt.sesame.data.career.ReportWeekInfo;
import com.carlt.sesame.data.career.SecretaryMessageInfo;
import com.carlt.sesame.data.career.SecretaryMessageInfoList;
import com.carlt.sesame.data.career.UserLicenseInfo;
import com.carlt.sesame.data.community.FriendDetialInfo;
import com.carlt.sesame.data.community.FriendFeedInfo;
import com.carlt.sesame.data.community.FriendPKInfo;
import com.carlt.sesame.data.community.InviteFriendInfo;
import com.carlt.sesame.data.community.MyAppointmentListInfo;
import com.carlt.sesame.data.community.MyFriendInfo;
import com.carlt.sesame.data.community.MySOSListInfo;
import com.carlt.sesame.data.community.SubmitSOSInfo;
import com.carlt.sesame.data.remote.AirMainInfo;
import com.carlt.sesame.data.remote.RemoteFunInfo;
import com.carlt.sesame.data.remote.RemoteLogListInfo;
import com.carlt.sesame.data.safety.LoginLogListInfo;
import com.carlt.sesame.data.safety.MobileListInfo;
import com.carlt.sesame.data.set.CityStringInfo;
import com.carlt.sesame.data.set.FeeOrderInfo;
import com.carlt.sesame.data.set.ModifyCarInfo;
import com.carlt.sesame.data.set.PushSetInfo;
import com.carlt.sesame.data.set.TransferNewCheckInfo;
import com.carlt.sesame.data.set.TransferOldCheckInfo;
import com.carlt.sesame.data.usercenter.VersionLog;
import com.carlt.sesame.preference.RemotePswInfo;
import com.carlt.sesame.protocolstack.AdvertiseInfoParser;
import com.carlt.sesame.protocolstack.BaseParser;
import com.carlt.sesame.protocolstack.DefaultParser;
import com.carlt.sesame.protocolstack.DeviceIsOnlineParser;
import com.carlt.sesame.protocolstack.DeviceUpdateInfoParser;
import com.carlt.sesame.protocolstack.LoginInfoParser;
import com.carlt.sesame.protocolstack.UserOtherInfoParser;
import com.carlt.sesame.protocolstack.VersionInfoParser;
import com.carlt.sesame.protocolstack.car.AllCityInfoListParser;
import com.carlt.sesame.protocolstack.car.CarInfoListParser;
import com.carlt.sesame.protocolstack.car.CarInfoParser;
import com.carlt.sesame.protocolstack.car.CarMainParser;
import com.carlt.sesame.protocolstack.car.CarModeInfoListParser;
import com.carlt.sesame.protocolstack.car.CarModeInfoListV2Parser;
import com.carlt.sesame.protocolstack.car.CarModeInfoListV3Parser;
import com.carlt.sesame.protocolstack.car.CarStatuListParser;
import com.carlt.sesame.protocolstack.car.CheckFaultListParser;
import com.carlt.sesame.protocolstack.car.City2ListParser;
import com.carlt.sesame.protocolstack.car.CityListJUHEParser;
import com.carlt.sesame.protocolstack.car.HelpPhoneListParser;
import com.carlt.sesame.protocolstack.car.ProvinceListJUHEParser;
import com.carlt.sesame.protocolstack.car.ProvinceListParser;
import com.carlt.sesame.protocolstack.car.RecommendMaintainParser;
import com.carlt.sesame.protocolstack.car.TireProgressParser;
import com.carlt.sesame.protocolstack.car.TiredirectParser;
import com.carlt.sesame.protocolstack.car.TirepressureParser;
import com.carlt.sesame.protocolstack.car.ViolationInfoListParser;
import com.carlt.sesame.protocolstack.career.CarLogListParser;
import com.carlt.sesame.protocolstack.career.CareerlParser;
import com.carlt.sesame.protocolstack.career.ChallengeListParser;
import com.carlt.sesame.protocolstack.career.ChallengeScoreParser;
import com.carlt.sesame.protocolstack.career.ChallengeStartParser;
import com.carlt.sesame.protocolstack.career.DayOrderStateParser;
import com.carlt.sesame.protocolstack.career.DiagnoseListParser;
import com.carlt.sesame.protocolstack.career.GpsInfoListParser;
import com.carlt.sesame.protocolstack.career.MonthOrderStateParser;
import com.carlt.sesame.protocolstack.career.MyLicenceParser;
import com.carlt.sesame.protocolstack.career.PrizeDetailParser;
import com.carlt.sesame.protocolstack.career.PrizeListParser;
import com.carlt.sesame.protocolstack.career.RecommendSalesParser;
import com.carlt.sesame.protocolstack.career.ReportAllParser;
import com.carlt.sesame.protocolstack.career.ReportCalendarDayParser;
import com.carlt.sesame.protocolstack.career.ReportCalendarMonthParser;
import com.carlt.sesame.protocolstack.career.ReportCalendarWeekParser;
import com.carlt.sesame.protocolstack.career.ReportDateParser;
import com.carlt.sesame.protocolstack.career.ReportDayParser;
import com.carlt.sesame.protocolstack.career.ReportMonthParser;
import com.carlt.sesame.protocolstack.career.ReportWeekParser;
import com.carlt.sesame.protocolstack.career.SafetyMessageListParser;
import com.carlt.sesame.protocolstack.career.SecretaryCategoryListParser;
import com.carlt.sesame.protocolstack.career.SecretaryMessageListParser;
import com.carlt.sesame.protocolstack.community.FeedDetialInfoParser;
import com.carlt.sesame.protocolstack.community.FindingIndexParser;
import com.carlt.sesame.protocolstack.community.FriendsPKParser;
import com.carlt.sesame.protocolstack.community.FriendsShareListParser;
import com.carlt.sesame.protocolstack.community.InviteFriendListParser;
import com.carlt.sesame.protocolstack.community.InvitePrizeParser;
import com.carlt.sesame.protocolstack.community.MyFriendDetailParser;
import com.carlt.sesame.protocolstack.community.MyFriendListParser;
import com.carlt.sesame.protocolstack.community.MyOrderDetialParser;
import com.carlt.sesame.protocolstack.community.MyOrderListParser;
import com.carlt.sesame.protocolstack.community.MySOSDetialListParser;
import com.carlt.sesame.protocolstack.community.MySOSListParser;
import com.carlt.sesame.protocolstack.remote.CarStateInfoParser;
import com.carlt.sesame.protocolstack.remote.ChargeResultParser;
import com.carlt.sesame.protocolstack.remote.ChargeStatusInfoParser;
import com.carlt.sesame.protocolstack.remote.RemoteLogListParser;
import com.carlt.sesame.protocolstack.remote.RemoteMainInfoParser;
import com.carlt.sesame.protocolstack.safety.AutherStateParser;
import com.carlt.sesame.protocolstack.safety.AuthorInfoParser;
import com.carlt.sesame.protocolstack.safety.LoginLogListParser;
import com.carlt.sesame.protocolstack.safety.MobileListParser;
import com.carlt.sesame.protocolstack.safety.SafetyMainInfoParser;
import com.carlt.sesame.protocolstack.set.CityInfosParser;
import com.carlt.sesame.protocolstack.set.EditPhoneNum111Parser;
import com.carlt.sesame.protocolstack.set.FeeLogListParser;
import com.carlt.sesame.protocolstack.set.FeeMainInfoParser;
import com.carlt.sesame.protocolstack.set.FeeOrderInfoParser;
import com.carlt.sesame.protocolstack.set.ModifyCarInfoParser;
import com.carlt.sesame.protocolstack.set.PushSetParser;
import com.carlt.sesame.protocolstack.set.UploadImgParser;
import com.carlt.sesame.protocolstack.usercenter.ExtInfoParser;
import com.carlt.sesame.protocolstack.usercenter.TransferQrCodeParser;
import com.carlt.sesame.systemconfig.OnDateChageConfig;
import com.carlt.sesame.systemconfig.URLConfig;
import com.carlt.sesame.ui.activity.setting.ManageMessageActivity;
import com.carlt.sesame.utility.CipherUtils;
import com.carlt.sesame.utility.CreatPostString;
import com.carlt.sesame.utility.FileUtil;
import com.carlt.sesame.utility.Log;
import com.carlt.sesame.utility.MyParse;
import com.carlt.sesame.utility.MyTimeUtil;
import com.tencent.android.tpush.XGPushManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class CPControl {

    public interface GetResultListCallback {
        void onFinished(Object o);

        void onErro(Object o);

    }

    public interface GetResultList2Callback {
        void onFinished(Object o1, Object o2, Object o3);

        void onErro(Object o);

    }

    public interface GetResultList3Callback {
        void onFinished();

        void onSuccess(Object o1);

        void onErro();

    }

    // public static void GetCS() {
    // new Thread() {
    // @Override
    // public void run() {
    // // 链接地址
    // String url = "http://zfhfjijian.beilin.gov.cn/dovote.aspx?id=63";
    // // Post参数
    // String post =
    // "ctl00$ContentPlaceHolder1$phonea=1&ctl00$ContentPlaceHolder1$namea=a";
    //
    // try {
    // HttpClient httpclient = new DefaultHttpClient();
    // HttpPost httppost = new HttpPost(url);
    // AbstractHttpEntity entity = new StringEntity(post, "UTF-8");
    // entity.setContentType("application/x-www-form-urlencoded");
    // httppost.setEntity(entity);
    // // MultipartEntity mEntity = new MultipartEntity();
    // // mEntity.addPart("ctl00$ContentPlaceHolder1$RadioGroup1ctl00", new
    // StringBody("满意"));
    // // mEntity.addPart("ctl00$ContentPlaceHolder1$phonea", new
    // StringBody(""));
    // // mEntity.addPart("ctl00$ContentPlaceHolder1$namea", new
    // StringBody(""));
    // // httppost.setEntity(mEntity);
    // Log.e("http", "Http请求--" + url);
    // HttpResponse response = httpclient.execute(httppost);
    // int code = response.getStatusLine().getStatusCode();
    // // 检验状态码，如果成功接收数据
    // if (code == 200) {
    //
    // Log.e("info", "entity==" + EntityUtils.toString(response.getEntity(),
    // "UTF-8"));
    //
    // } else {
    // Log.e("info", "HttpPostor--connect--e==" + code + "错误");
    // }
    // } catch (Exception e) {
    // Log.e("info", "HttpPostor--connect--e=="+e);
    // }
    //
    // }
    // }.start();
    // }

    //
    // /**
    // * 登录接口 onFinished表示成功
    // */
    // public static void GetLoginResult(final String userName,
    // final String psWord, final GetResultListCallback listener) {
    //
    // if (listener == null)
    // return;
    // new Thread() {
    // @Override
    // public void run() {
    // // 链接地址
    // String url = URLConfig.getM_LOGIN_URL();
    // // Post参数
    // String post = CreatPostString.getLogin(userName, psWord);
    //
    // UserInfoParser mLoginParser = new UserInfoParser();
    // BaseResponseInfo mBaseResponseInfo = mLoginParser
    // .getBaseResponseInfo(url, post);
    //
    // if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
    // // GetUserInfoResult();
    // listener.onFinished(mBaseResponseInfo);
    // } else {
    // listener.onErro(mBaseResponseInfo);
    // }
    // }
    //
    // }.start();
    // }

    /**
     * 版本升级接口 成功返回<VersionInfo>
     **/
    public static void GetCityInfos() {
        new Thread() {
            @Override
            public void run() {
                DaoControl dao = DaoControl.getInstance();
                ArrayList<CityStringInfo> infos = dao.getCityStringInfoList();
                CityStringInfo info = null;
                if (infos.size() > 0) {
                    info = infos.get(0);
                    String time = info.getTime();
                    try {
                        Date date = MyTimeUtil.commonFormat.parse(time);
                        int days = MyTimeUtil.getDateDays(new Date(), date);
                        // 是否大于一个月
                        if (days < 7) {
                            // 不更新
                            return;
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

                // 链接地址
                String url = URLConfig.CITYS_INFO_URL;
                // Post参数
                String post = CreatPostString.getCitys();

                CityInfosParser mParser = new CityInfosParser();
                BaseResponseInfo mBaseResponseInfo = mParser
                        .getOtherBaseResponseInfo(url, post);

            }
        }.start();
    }

    /**
     * 使用车行易
     **/
    public static void GetCityInfos2() {
        new Thread() {
            @Override
            public void run() {
                DaoControl dao = DaoControl.getInstance();
                ArrayList<CityStringInfo> infos = dao.getCity2StringInfoList();
                CityStringInfo info = null;
                if (infos.size() > 0) {
                    info = infos.get(0);
                    String time = info.getTime();
                    try {
                        Date date = MyTimeUtil.commonFormat.parse(time);
                        int days = MyTimeUtil.getDateDays(new Date(), date);
                        // 是否大于一个月
                        if (days < 7) {
                            // 不更新
                            return;
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

                // 链接地址
                String url = URLConfig.CITYS2_INFO_URL;
                // Post参数
                String post = CreatPostString.getCitys2();

                DefaultParser mParser = new DefaultParser();
                BaseResponseInfo mBaseResponseInfo = mParser
                        .getOtherBaseResponseInfo(url, post);
                String citys = mParser.getmResult();
                infos = dao.getCity2StringInfoList();
                boolean isFirst = false;
                if (infos.size() > 1) {
                    info = infos.get(0);
                } else {
                    isFirst = true;
                    info = new CityStringInfo();
                }
                info.setTxt(citys);
                info.setTime(MyTimeUtil.commonFormat.format(new Date()));
                if (isFirst) {
                    dao.insertCity2StringInfo(info);
                } else {
                    dao.updataCity2StringInfo(info);
                }
            }
        }.start();
    }

    /**
     * 版本升级接口 成功返回<VersionInfo>
     **/
    public static void GetVersion(final GetResultListCallback listener) {
        new Thread() {
            @Override
            public void run() {
                // 链接地址
                String url = URLConfig.getM_VERSION_URL();
                // Post参数
                String post = CreatPostString.getVersion();

                VersionInfoParser mParser = new VersionInfoParser();
                BaseResponseInfo mBaseResponseInfo = mParser
                        .getBaseResponseInfo(url, post);

                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
                    listener.onFinished(mParser.getReturn());
                } else {
                    listener.onErro(mBaseResponseInfo);
                }
            }
        }.start();
    }

    /**
     * 版本升级接口 成功返回<VersionInfo>
     **/
    public static void GetCurVersion(final GetResultListCallback listener) {
        new Thread() {
            @Override
            public void run() {
                // 链接地址
                String url = URLConfig.getM_CUR_VERSION_URL();
                // Post参数
                String post = CreatPostString.getCurVersion();

                VersionInfoParser mParser = new VersionInfoParser();
                BaseResponseInfo mBaseResponseInfo = mParser
                        .getBaseResponseInfo(url, post);

                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
                    listener.onFinished(mParser.getReturn());
                } else {
                    listener.onErro(mBaseResponseInfo);
                }
            }
        }.start();
    }

    /**
     * 新登录接口 onFinished表示成功
     */
    public static void GetLogin(final String userName, final String psWord,
                                final GetResultListCallback listener) {

        if (listener == null)
            return;
        new Thread() {
            @Override
            public void run() {
                // 链接地址
                String url = URLConfig.getM_LOGIN_URL();
                // Post参数
                String post = CreatPostString.getLogin(userName, psWord);
                //                if (TextUtils.isEmpty(post)) {
                //                    UUToast.showUUToast(DorideApplication.getAppContext(),"权限未获取到，请在权限管理打开获取手机IMEI权限");
                //                    System.exit(0);
                //                    return;
                //                }
                LoginInfoParser mLoginParser = new LoginInfoParser();
                // BaseResponseInfo mBaseResponseInfo;
                // //为了防止app在后台时间长久后，再次唤醒出现的Bug
                // if (TokenInfo.getToken() != null &&
                // TokenInfo.getToken().length() > 0) {
                // mBaseResponseInfo = new BaseResponseInfo();
                // mBaseResponseInfo.setFlag(200);
                // mBaseResponseInfo.setInfo("");
                // } else {
                // mBaseResponseInfo = mLoginParser.getBaseResponseInfo(url,
                // post);
                // }

                BaseResponseInfo mBaseResponseInfo = mLoginParser
                        .getBaseResponseInfo(url, post);
                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
                    listener.onFinished(mBaseResponseInfo);
                } else {
                    listener.onErro(mBaseResponseInfo);
                }
            }

        }.start();
    }

    /**
     * 绑定车乐盒子 调用成功执行onFinished，无返回数据<null>
     */

    public static void GetBindDeviceResult(final String deviceId,
                                           final GetResultListCallback listener) {
        if (listener == null)
            return;
        new Thread() {
            @Override
            public void run() {
                // 链接地址
                String url = URLConfig.getM_BIND_DEVICE_URL();
                // Post参数
                String post = CreatPostString.getBindDevice(deviceId);

                DefaultParser mParser = new DefaultParser();
                BaseResponseInfo mBaseResponseInfo = mParser
                        .getBaseResponseInfo(url, post);
                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
                    GetCarInfo.getInstance().deviceNum = deviceId;
                    listener.onFinished(null);
                } else {
                    listener.onErro(mBaseResponseInfo);
                }

            }

        }.start();
    }

    /**
     * 激活设备 onFinished表示成功
     */
    public static void GetDeviceidActivateResult(final String pin,
                                                 final GetResultListCallback listener) {

        if (listener == null)
            return;
        new Thread() {
            @Override
            public void run() {

                // 链接地址
                String url = URLConfig.getM_DEVICEACTIVATE_URL();
                // Post参数
                String post = CreatPostString.getDeviceidActivte(pin);

                DefaultParser mParser = new DefaultParser();
                BaseResponseInfo mBaseResponseInfo = mParser
                        .getBaseResponseInfo(url, post);
                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
                    listener.onFinished(mBaseResponseInfo);
                } else {
                    listener.onErro(mBaseResponseInfo);
                }
            }

        }.start();
    }

    /**
     * 获取设备升级状态 onFinished表示成功
     */
    public static void GetDeviceUpdateResult(final String deviceid,
                                             final GetResultListCallback listener) {

        if (listener == null)
            return;
        new Thread() {
            @Override
            public void run() {

                // 链接地址
                String url = URLConfig.getM_DEVICEUPDATE_URL();
                // Post参数
                String post = CreatPostString.getDeviceUpdate(deviceid);

                DeviceUpdateInfoParser mParser = new DeviceUpdateInfoParser();
                BaseResponseInfo mBaseResponseInfo = mParser
                        .getBaseResponseInfo(url, post);
                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
                    listener.onFinished(mParser.getReturn());
                } else {
                    listener.onErro(mBaseResponseInfo);
                }
            }

        }.start();
    }

    /**
     * 激活设备 onFinished返回boolean
     */
    public static void GetDeviceidIsOnLineResult(
            final GetResultListCallback listener) {

        if (listener == null)
            return;
        new Thread() {
            @Override
            public void run() {
                // 链接地址
                String url = URLConfig.getM_CAROBD_URL();
                // Post参数
                String post = CreatPostString.getDeviceidIsOnLine();

                DeviceIsOnlineParser mParser = new DeviceIsOnlineParser();
                BaseResponseInfo mBaseResponseInfo = mParser
                        .getBaseResponseInfo(url, post);
                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
                    listener.onFinished(mParser.getReturn());
                } else {
                    listener.onFinished(false);
                }
            }

        }.start();
    }

    /**
     * 获取用户相关信息 onFinished返回String
     */
    public static void GetExtInfoResult(final GetResultListCallback listener) {

        if (listener == null)
            return;
        new Thread() {
            @Override
            public void run() {
                // 链接地址
                String url = URLConfig.getM_GETEXTINFO_URL();
                // Post参数
                String post = CreatPostString.getExtInfo();
                ExtInfoParser mParser = new ExtInfoParser();
                BaseResponseInfo mBaseResponseInfo = mParser
                        .getBaseResponseInfo(url, post);
                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
                    listener.onFinished(mParser.getReturn());
                } else {
                    listener.onErro(mBaseResponseInfo);
                }
            }

        }.start();
    }

    /**
     * 获取生涯首页数据 onFinished返还<CareerInfo>
     */
    public static void GetCareerResult(final GetResultListCallback listener) {

        if (listener == null)
            return;
        new Thread() {
            @Override
            public void run() {
                // 链接地址
                String url = URLConfig.getM_CAREER_URL();
                // Post参数
                String post = CreatPostString.getCareer();

                CareerlParser mParser = new CareerlParser();
                BaseResponseInfo mBaseResponseInfo = mParser
                        .getBaseResponseInfo(url, post);

                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
                    listener.onFinished(mParser.getReturn());
                } else {
                    listener.onErro(mBaseResponseInfo);
                }
            }

        }.start();
    }

    /**
     * 获取经销商车型列表 onFinished返还<CarModeInfo>
     */
    public static void GetDealerModelListResult(final int index,
                                                final String id, final GetResultListCallback listener) {

        if (listener == null)
            return;
        new Thread() {
            @Override
            public void run() {
                // 链接地址
                String url = URLConfig.getM_BRANDLLIST_URL();
                // Post参数
                String post = CreatPostString.getDealerModel(index, id);

                CarModeInfoListParser mParser = new CarModeInfoListParser();
                BaseResponseInfo mBaseResponseInfo = mParser
                        .getBaseResponseInfo(url, post);

                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {

                    listener.onFinished(mParser.getReturn());
                } else {
                    listener.onErro(mBaseResponseInfo);
                }
            }

        }.start();
    }

    /**
     * 获取经销商车型列表(针对获取车系级别列表) onFinished返还<CarModeInfo>
     */
    public static void GetDealerModelListV1Result(final String id,
                                                  final GetResultListCallback listener) {

        if (listener == null)
            return;
        new Thread() {
            @Override
            public void run() {
                // 链接地址
                String url = URLConfig.getM_MODELLIST_URL();
                // Post参数
                String post = CreatPostString.getDealerModelV1(id);

                CarModeInfoListV2Parser mParser = new CarModeInfoListV2Parser();
                BaseResponseInfo mBaseResponseInfo = mParser
                        .getBaseResponseInfo(url, post);

                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {

                    listener.onFinished(mParser.getReturn());
                } else {
                    listener.onErro(mBaseResponseInfo);
                }
            }

        }.start();
    }

    /**
     * 获取经销商车型列表(针对获取车款级别列表) onFinished返还<CarModeInfo>
     * @param id
     *         车型ID
     * @param is_before
     *         1：前装 0：后装
     */
    public static void GetDealerModelListV2Result(final String id,
                                                  final String is_before, final GetResultListCallback listener) {

        if (listener == null)
            return;
        new Thread() {
            @Override
            public void run() {
                // 链接地址
                String url = URLConfig.getM_CARLIST_URL();
                // Post参数
                String post = CreatPostString.getDealerModelV2(id, is_before);

                CarModeInfoListV3Parser mParser = new CarModeInfoListV3Parser();
                BaseResponseInfo mBaseResponseInfo = mParser
                        .getBaseResponseInfo(url, post);

                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {

                    listener.onFinished(mParser.getReturn());
                } else {
                    listener.onErro(mBaseResponseInfo);
                }
            }

        }.start();
    }

    /**
     * 修改密码接口 onFinished表示成功
     */
    public static void GetEditPasswordResult(final String oldPassword,
                                             final String newPassword, final GetResultListCallback listener) {

        if (listener == null)
            return;
        new Thread() {
            @Override
            public void run() {
                // 链接地址
                String url = URLConfig.getM_EDITPASSWORD_URL();
                // Post参数
                String newPwd = CipherUtils.md5(newPassword);
                String oldPwd = CipherUtils.md5(oldPassword);
                String post = CreatPostString.getEditPassword(oldPwd,
                        newPwd);

                DefaultParser mParser = new DefaultParser();
                BaseResponseInfo mBaseResponseInfo = mParser
                        .getBaseResponseInfo(url, post);
                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
                    listener.onFinished(null);
                } else {
                    listener.onErro(mBaseResponseInfo);
                }

            }

        }.start();
    }

    /**
     * * 修改手机号第一步，验证旧手机
     * @param oldPhoneNum
     *         旧手机号
     * @param oldValidate
     *         验证码
     * @param listener
     * @return String 返回一个串码code,在第二步时需要用到
     */
    public static void getEditPhoneNum111(final String oldPhoneNum,
                                          final String oldValidate, final GetResultListCallback listener) {

        if (listener == null)
            return;
        new Thread() {
            @Override
            public void run() {
                // 链接地址
                String url = URLConfig.getM_AUTHMOBILE_URL();
                // Post参数
                String post = CreatPostString.getAuthPhone(oldPhoneNum,
                        oldValidate);

                EditPhoneNum111Parser mParser = new EditPhoneNum111Parser();
                BaseResponseInfo mBaseResponseInfo = mParser
                        .getBaseResponseInfo(url, post);
                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
                    listener.onFinished(mParser.getReturn());
                } else {
                    listener.onErro(mBaseResponseInfo);
                }

            }

        }.start();

    }

    /**
     * * 修改手机号第二步，验证新手机
     * @param newPhoneNum
     *         新手机号
     * @param newValidate
     *         验证码
     * @param code
     *         第一步返回的串码
     * @param listener
     * @return null
     */
    public static void getEditPhoneNum222(final String newPhoneNum,
                                          final String newValidate, final String code,
                                          final GetResultListCallback listener) {

        if (listener == null)
            return;
        new Thread() {
            @Override
            public void run() {
                // 链接地址
                String url = URLConfig.getM_EDITMOBILE_URL();
                // Post参数
                String post = CreatPostString.getEditPhone(code, newPhoneNum,
                        newValidate);

                DefaultParser mParser = new DefaultParser();
                BaseResponseInfo mBaseResponseInfo = mParser
                        .getBaseResponseInfo(url, post);
                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
                    UserInfo.getInstance().mobile = newPhoneNum;
                    listener.onFinished(newPhoneNum);
                } else {
                    listener.onErro(mBaseResponseInfo);
                }
            }

        }.start();
    }

    /**
     * 发送验证码接口 onFinished表示成功
     */
    // 注册
    public final static String VALIDATE_TYPE_REGISTER = "1";

    // 找回密码
    public final static String VALIDATE_TYPE_FINDPASSWORD = "2";

    // 修改密码
    public final static String VALIDATE_TYPE_EDITPASSWORD = "3";

    // 修改手机
    public final static String VALIDATE_TYPE_EDITPHONE = "4";

    // 绑定微信（APP暂时用不到）
    public final static String VALIDATE_TYPE_BINDWEICHAT = "5";

    // 验证旧手机
    public final static String VALIDATE_TYPE_COMPAREOLDPHONE = "6";

    // 找回远程密码
    public final static String VALIDATE_TYPE_FINDPASSWORDREMOTE = "7";

    // 车辆过户
    public final static String VALIDATE_TYPE_TRANSFER = "8";

    // 车主认证
    public final static String VALIDATE_TYPE_AUTHORMAIN = "9";

    // 更换设备
    public final static String VALIDATE_TYPE_CHANGE = "10";

    public static void GetVoiceValidateResult(final String type,
                                              final String phoneNum, final GetResultListCallback listener) {
        GetValidateResult(type, phoneNum, "1", listener);
    }

    public static void GetMessageValidateResult(final String type,
                                                final String phoneNum, final GetResultListCallback listener) {
        GetValidateResult(type, phoneNum, "0", listener);
    }

    private static void GetValidateResult(final String type,
                                          final String phoneNum, final String voiceVerify,
                                          final GetResultListCallback listener) {

        if (listener == null)
            return;
        new Thread() {
            @Override
            public void run() {
                // 链接地址
                // String url = URLConfig.getM_VALIDATE_URL();
                String url = URLConfig.getM_VALIDATE_NEW_URL();
                // Post参数
                String post = CreatPostString.getValidate(type, phoneNum,
                        voiceVerify);

                DefaultParser mParser = new DefaultParser();
                BaseResponseInfo mBaseResponseInfo = mParser
                        .getBaseResponseInfo(url, post);
                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {

                    listener.onFinished("发送成功");
                } else {
                    listener.onErro(mBaseResponseInfo);
                }

            }

        }.start();
    }

    /**
     * 找回密码-第二步对比验证码接口 onFinished表示成功
     */
    public static void GetRetrievePasswordResult(final String phoneNum,
                                                 final String newPassWord, final String validate,
                                                 final GetResultListCallback listener) {

        if (listener == null)
            return;
        new Thread() {
            @Override
            public void run() {
                // 链接地址
                String url = URLConfig.getM_RETRIEVEPASSWORD_URL();
                // Post参数
                String md5 = CipherUtils.md5(newPassWord);
                String post = CreatPostString.getRetrievePassword(phoneNum,
                        md5, validate);

                DefaultParser mParser = new DefaultParser();
                BaseResponseInfo mBaseResponseInfo = mParser
                        .getBaseResponseInfo(url, post);
                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
                    listener.onFinished(null);
                } else {
                    listener.onErro(mBaseResponseInfo);
                }

            }

        }.start();
    }

    /**
     * 校验验证码
     * @param phoneNum
     *         手机号
     * @param type
     *         发送验证码类型
     * @param code
     *         验证码
     * @param listener
     *         回调
     */
    public static void GetValidateCheckResult(final String phoneNum,
                                              final String type, final String code,
                                              final GetResultListCallback listener) {

        if (listener == null)
            return;
        new Thread() {
            @Override
            public void run() {
                // 链接地址
                String url = URLConfig.getM_VALIDATE_CHECK_URL();
                // Post参数
                String post = CreatPostString.getValidateCheck(phoneNum, type,
                        code);
                DefaultParser mParser = new DefaultParser();
                BaseResponseInfo mBaseResponseInfo = mParser
                        .getBaseResponseInfo(url, post);
                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {

                    listener.onFinished(mBaseResponseInfo);
                } else {
                    listener.onErro(mBaseResponseInfo);
                }

            }

        }.start();
    }

    /**
     * 行车报告日历信息--月报 调用成功会返回ArrayList<ReportCalendarMonthInfo> 用于月报日历选择处
     */

    public static void GetUserMonthPointResult(
            final GetResultListCallback listener, final String date) {
        GetUserCalendarResult(listener, date, REPORT_MONTH);
    }

    /**
     * 行车报告日历信息--周报 调用成功会返回 ArrayList<WeekInfo> 用于周报日历选择处
     */

    public static void GetUserWeekPointResult(
            final GetResultListCallback listener, final String date) {
        GetUserCalendarResult(listener, date, REPORT_WEEK);
    }

    /**
     * 行车报告日历信息--日报 调用成功会返回ArryList<ReportCalendarDayInfo> 用于日报日历选择处
     */

    public static void GetUserDayPointResult(
            final GetResultListCallback listener, final String date) {
        GetUserCalendarResult(listener, date, REPORT_DAY);
    }

    private static void GetUserCalendarResult(
            final GetResultListCallback listener, final String date,
            final String type) {

        if (listener == null)
            return;
        new Thread() {
            @Override
            public void run() {
                // 链接地址

                String url = URLConfig.getM_USER_MONTH_POINT_URL();
                // Post参数
                String post = CreatPostString.getReportCalendar(date);
                BaseParser mParser = null;
                if (type.equals(REPORT_MONTH)) {
                    // 月报
                    mParser = new ReportCalendarMonthParser();
                    url = URLConfig.getM_USER_MONTH_POINT_URL();

                } else if (type.equals(REPORT_WEEK)) {
                    // 周报
                    mParser = new ReportCalendarWeekParser();
                    url = URLConfig.getM_USER_WEEK_POINT_URL();
                } else if (type.equals(REPORT_DAY)) {
                    // 日报
                    mParser = new ReportCalendarDayParser();
                    url = URLConfig.getM_USER_DAY_POINT_URL();
                }

                if (mParser != null) {
                    BaseResponseInfo mBaseResponseInfo = mParser
                            .getBaseResponseInfo(url, post);
                    if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
                        Object o = mParser.getReturn();
                        listener.onFinished(o);
                    } else {
                        listener.onErro(mBaseResponseInfo);
                    }
                }

            }

        }.start();
    }

    /**
     * 行车报告总报 调用成功会返回<ReportAllInfo>
     */
    public static void GetAllReportResult(final GetResultListCallback listener) {
        new Thread() {

            @Override
            public void run() {
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                GetUserReportResult(REPORT_ALL, "", listener);
            }

        }.start();

    }

    /**
     * 行车报告日报 调用成功会返回<ReportDayInfo>
     */
    public static void GetDayReportResult(String date,
                                          final GetResultListCallback listener) {
        GetUserReportResult(REPORT_DAY, date, listener);
    }

    /**
     * 行车报告周报 调用成功会返回<ReportMonthInfo>
     */
    public static void GetWeekReportResult(String date,
                                           final GetResultListCallback listener) {
        GetUserReportResult(REPORT_WEEK, date, listener);
    }

    /**
     * 行车报告月报 调用成功会返回<ReportMonthInfo>
     */
    public static void GetMonthReportResult(String date,
                                            final GetResultListCallback listener) {
        GetUserReportResult(REPORT_MONTH, date, listener);
    }

    public final static String REPORT_DAY = "day";

    public final static String REPORT_ALL = "all";

    public final static String REPORT_WEEK = "week";

    public final static String REPORT_MONTH = "month";

    /**
     * 行车报告接口
     */
    private static void GetUserReportResult(final String reportType,
                                            final String date, final GetResultListCallback listener) {

        if (listener == null)
            return;
        new Thread() {
            @Override
            public void run() {
                // 链接地址
                String url = URLConfig.getM_REPORT_URL();
                // Post参数
                String post = CreatPostString.getReport(reportType, date);

                if (reportType.equals(REPORT_ALL)) {
                    // 总报
                    ReportAllParser mParser = new ReportAllParser();
                    BaseResponseInfo mBaseResponseInfo = mParser
                            .getBaseResponseInfo(url, post);
                    if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
                        ReportAllInfo mReportAllInfo = mParser.getReturn();
                        listener.onFinished(mReportAllInfo);
                    } else {
                        listener.onErro(mBaseResponseInfo);
                    }
                } else if (reportType.equals(REPORT_DAY)) {
                    // 日报
                    ReportDayParser mParser = new ReportDayParser();
                    BaseResponseInfo mBaseResponseInfo = mParser
                            .getBaseResponseInfo(url, post);
                    if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
                        ReportDayInfo mReportDayInfo = mParser.getReturn();
                        listener.onFinished(mReportDayInfo);
                    } else {
                        listener.onErro(mBaseResponseInfo);
                    }
                } else if (reportType.equals(REPORT_WEEK)) {
                    // 周报
                    ReportWeekParser mParser = new ReportWeekParser();
                    BaseResponseInfo mBaseResponseInfo = mParser
                            .getBaseResponseInfo(url, post);
                    if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
                        ReportWeekInfo mReportWeekInfo = mParser.getReturn();
                        listener.onFinished(mReportWeekInfo);
                    } else {
                        listener.onErro(mBaseResponseInfo);
                    }
                } else if (reportType.equals(REPORT_MONTH)) {
                    // 月报
                    ReportMonthParser mParser = new ReportMonthParser();
                    BaseResponseInfo mBaseResponseInfo = mParser
                            .getBaseResponseInfo(url, post);
                    if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
                        ReportMonthInfo mReportMonthInfo = mParser.getReturn();
                        listener.onFinished(mReportMonthInfo);
                    } else {
                        listener.onErro(mBaseResponseInfo);
                    }
                }

            }

        }.start();
    }

    /**
     * 行车报告日期接口
     */
    public static void GetReportdateResult(final GetResultListCallback listener) {

        if (listener == null)
            return;
        new Thread() {
            @Override
            public void run() {
                // 链接地址
                String url = URLConfig.getM_REPORTDATE_URL();
                // Post参数
                String post = CreatPostString.getReportdate();

                ReportDateParser mParser = new ReportDateParser();
                BaseResponseInfo mBaseResponseInfo = mParser
                        .getBaseResponseInfo(url, post);
                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
                    listener.onFinished(mParser.getReturn());
                } else {
                    listener.onErro(mBaseResponseInfo);
                }

            }

        }.start();
    }

    /**
     * 行车日志接口 调用成功会返回ArrayList<CarLogInfo>
     */
    public static void GetCarLogResult(final String date,
                                       final GetResultListCallback listener) {

        if (listener == null)
            return;
        new Thread() {
            @Override
            public void run() {
                // 链接地址
                String url = URLConfig.getM_CARLOG_URL();
                // Post参数
                String post = CreatPostString.getCarLog(date);

                CarLogListParser mParser = new CarLogListParser();
                BaseResponseInfo mBaseResponseInfo = mParser
                        .getBaseResponseInfo(url, post);
                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
                    listener.onFinished(mParser.getReturn());
                } else {
                    listener.onErro(mBaseResponseInfo);
                }

            }

        }.start();
    }

    /**
     * 行车轨迹回放接口 调用成功会返回ArrayList<ReportGpsInfo>
     * @param gpsStartTime
     *         开始时间 （1455419954）
     * @param gpsStopTime
     *         结束时间 （1455425054）
     * @param runSn
     *         GPS序号
     */
    public static void GetCoorResult(final String gpsStartTime,
                                     final String gpsStopTime, final String runSn,
                                     final GetResultListCallback listener) {

        if (listener == null)
            return;
        new Thread() {
            @Override
            public void run() {
                // 链接地址
                String url = URLConfig.getM_GETCOOR_URL();
                // Post参数
                String post = CreatPostString.getCoor(gpsStartTime,
                        gpsStopTime, runSn);

                GpsInfoListParser mParser = new GpsInfoListParser();
                BaseResponseInfo mBaseResponseInfo = mParser
                        .getBaseResponseInfo(url, post);
                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
                    listener.onFinished(mParser.getReturn());
                } else {
                    listener.onErro(mBaseResponseInfo);
                }

            }

        }.start();
    }

    /**
     * 获取车秘书分類列表 调用成功会返回ArrayList<SecretaryCategoryInfoList>
     */

    public static void GetSecretaryCategoryResult(
            final GetResultListCallback listener) {

        if (listener == null)
            return;
        new Thread() {
            @Override
            public void run() {
                // 链接地址
                String url = URLConfig.getM_SECRETARY_CATEGORY_URL();
                // Post参数
                String post = CreatPostString.getSecretaryCategory();

                SecretaryCategoryListParser mParser = new SecretaryCategoryListParser();
                BaseResponseInfo mBaseResponseInfo = mParser
                        .getBaseResponseInfo(url, post);
                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {

                    listener.onFinished(mParser.getReturn());
                } else {
                    listener.onErro(mBaseResponseInfo);
                }

            }

        }.start();
    }

    /**
     * 获取车秘书消息详情 返回<String>
     */

    public static void GetSecretaryByIdResult(final String id,
                                              final GetResultListCallback listener) {

        if (listener == null)
            return;
        new Thread() {
            @Override
            public void run() {
                // 链接地址
                String url = URLConfig.getM_SECRETARY_GETBIID_URL();

                // Post参数
                String post = CreatPostString.getSecretaryById(id);

                DefaultParser mParser = new DefaultParser();

                BaseResponseInfo mBaseResponseInfo = mParser
                        .getBaseResponseInfo(url, post);
                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
                    listener.onFinished(mParser.getValue("content"));
                } else {
                    listener.onErro(mBaseResponseInfo);
                }

            }

        }.start();
    }

    /**
     * 删除车秘书消息 成功调用onFinished 返还null
     */

    public static void GetSecretaryDeleteResult(final int class1,
                                                final String id, final GetResultListCallback listener) {

        if (listener == null)
            return;
        new Thread() {
            @Override
            public void run() {
                // 链接地址
                String url = URLConfig.getM_SECRETARY_DELETE_URL();
                // Post参数
                String post = CreatPostString.getSecretaryDelete(class1, id);

                DefaultParser mParser = new DefaultParser();
                BaseResponseInfo mBaseResponseInfo = mParser
                        .getBaseResponseInfo(url, post);
                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
                    listener.onFinished(null);
                } else {
                    listener.onErro(mBaseResponseInfo);
                }

            }

        }.start();
    }

    /**
     * 获取车秘书提醒信息列表 调用成功会返回ArrayList<SecretaryMessageInfoList>
     */

    public static void GetSecretaryMessageResult(final int class1,
                                                 final GetResultListCallback listener) {

        if (listener == null)
            return;
        new Thread() {
            @Override
            public void run() {
                // 链接地址
                String url = URLConfig.getM_SECRETARY_MESSAGE_URL();
                // Post参数
                String post = CreatPostString.getSecretaryMessage(class1);

                SecretaryMessageListParser mParser = new SecretaryMessageListParser();
                BaseResponseInfo mBaseResponseInfo = mParser
                        .getBaseResponseInfo(url, post);
                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {

                    listener.onFinished(mParser.getReturn());
                } else {
                    listener.onErro(mBaseResponseInfo);
                }

            }

        }.start();
    }

    /**
     * 获取车秘书提醒信息列表 调用成功会返回ArrayList<SecretaryMessageInfoList>
     */

    public static void GetSecretaryMessageResult(final int limit,
                                                 final int offset, final int class1,
                                                 final GetResultListCallback listener) {

        if (listener == null)
            return;
        new Thread() {
            @Override
            public void run() {

                // 链接地址
                String url = URLConfig.getM_SECRETARY_MESSAGE_URL();
                // Post参数
                String post = CreatPostString.getSecretaryMessage(class1,
                        limit, offset);

                SecretaryMessageListParser mParser = new SecretaryMessageListParser();
                BaseResponseInfo mBaseResponseInfo = mParser
                        .getBaseResponseInfo(url, post);
                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
                    SecretaryMessageInfoList mSecretaryMessageInfoList = mParser
                            .getReturn();
                    mSecretaryMessageInfoList.setOffset(limit + offset);
                    listener.onFinished(mParser.getReturn());
                } else {
                    listener.onErro(mBaseResponseInfo);
                }

            }

        }.start();
    }

    /**
     * 获取安防提醒信息列表 调用成功会返回ArrayList<SecretaryMessageInfo>
     */

    public static void GetSafetyMessageResult(
            final GetResultListCallback listener) {

        if (listener == null)
            return;
        new Thread() {
            @Override
            public void run() {
                // 链接地址
                String url = URLConfig.getM_SAFETY_MESSAGE_URL();
                // Post参数
                String post = CreatPostString.getSaftyMessage(21);

                SafetyMessageListParser mParser = new SafetyMessageListParser();
                BaseResponseInfo mBaseResponseInfo = mParser
                        .getBaseResponseInfo(url, post);
                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {

                    listener.onFinished(mParser.getReturn());
                } else {
                    listener.onErro(mBaseResponseInfo);
                }

            }

        }.start();
    }

    /**
     * 我的奖品列表 调用成功会返回ArrayList<PrizeInfo>
     */

    public static void GetPrizeListResult(final GetResultListCallback listener) {

        if (listener == null)
            return;
        new Thread() {
            @Override
            public void run() {
                // 链接地址
                String url = URLConfig.getM_PRIZELIST_URL();

                // Post参数
                String post = CreatPostString.getPrize();
                PrizeListParser mParser = new PrizeListParser();
                BaseResponseInfo mBaseResponseInfo = mParser
                        .getBaseResponseInfo(url, post);
                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
                    ArrayList<PrizeInfo> mList = mParser.getReturn();
                    listener.onFinished(mList);
                } else {
                    listener.onErro(mBaseResponseInfo);
                }

            }

        }.start();
    }

    /**
     * 我的奖品详情 调用成功会返回<PrizeInfo>
     */

    public static void GetPrizeDetailResult(final String id,
                                            final GetResultListCallback listener) {

        if (listener == null)
            return;
        new Thread() {
            @Override
            public void run() {
                // 链接地址
                String url = URLConfig.getM_PRIZEDETAIL_URL();
                // Post参数
                String post = CreatPostString.getPrizeDetail(id);
                PrizeDetailParser mParser = new PrizeDetailParser();
                BaseResponseInfo mBaseResponseInfo = mParser
                        .getBaseResponseInfo(url, post);
                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
                    PrizeInfo mPrizeInfo = mParser.getReturn();
                    listener.onFinished(mPrizeInfo);
                } else {
                    listener.onErro(mBaseResponseInfo);
                }

            }

        }.start();
    }

    /**
     * 实时车况列表 调用成功会返回ArrayList<CarStatuInfo>
     */

    public static void GetCarStatuListResult(
            final GetResultListCallback listener) {

        if (listener == null)
            return;
        new Thread() {
            @Override
            public void run() {

                // 链接地址
                String url = URLConfig.getM_CAR_STATU_URL();
                // Post参数
                String post = CreatPostString.getCarStatus();

                CarStatuListParser mParser = new CarStatuListParser();
                BaseResponseInfo mBaseResponseInfo = mParser
                        .getBaseResponseInfo(url, post);
                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS
                        && mParser.getIsrunning() == 1) {
                    ArrayList<CarStatuInfo> mList = mParser.getReturn();
                    listener.onFinished(mList);
                } else {
                    if (mParser.getIsrunning() == 0) {
                        mBaseResponseInfo.setInfo("车辆未在行驶中，不能读取实时数据");
                    }
                    listener.onErro(mBaseResponseInfo);
                }

            }

        }.start();
    }

    /**
     * 导航同步到车
     * @param position
     *         纬度,经度 目标坐标（高德系坐标）
     * @param location
     *         位置名称
     */

    public static void GetNavigationResult(final String position,
                                           final String location, final GetResultListCallback listener) {

        if (listener == null)
            return;
        new Thread() {
            @Override
            public void run() {

                // 链接地址
                String url = URLConfig.getM_CARRELATED_NAVIGATION_URL();
                // Post参数
                String post = CreatPostString.getNavigation(position, location,
                        DorideApplication.MODEL_NAME);

                DefaultParser mParser = new DefaultParser();
                BaseResponseInfo mBaseResponseInfo = mParser
                        .getBaseResponseInfo(url, post);
                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
                    listener.onFinished(mBaseResponseInfo);
                } else {
                    listener.onErro(mBaseResponseInfo);
                }

            }

        }.start();
    }


    /**
     * 违章查询列表 调用成功会返回ArrayList<ViolationInfo> mPostViolationInfo不为空则视为第一次查询
     */
    public static void GetViolationListNewResult(
            final PostViolationInfo mPostViolationInfo,
            final GetResultListCallback listener) {

        if (listener == null)
            return;
        new Thread() {
            @Override
            public void run() {
                // 链接地址
                // Post参数
                String post = "";
                String url = URLConfig.CAR_BREAK_URL;// 车辆违章查询地址
                if (mPostViolationInfo != null) {
                    post = CreatPostString
                            .getViolationParams(mPostViolationInfo);
                }

                ViolationInfoListParser mParser = new ViolationInfoListParser();
                BaseResponseInfo mBaseResponseInfo = mParser
                        .getOtherBaseResponseInfo(url, post);
                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
                    GetCarInfo.getInstance().canQueryVio = 1;
                    ArrayList<ViolationInfo> mList = mParser.getReturn();
                    listener.onFinished(mList);
                } else {

                    listener.onErro(mBaseResponseInfo);
                }

            }

        }.start();
    }


    /**
     * 保存违章车辆信息 调用成功会返回<null>
     * @param carInfo
     *         车辆信息
     */
    public static void GetSaveCarResult(final CarInfo carInfo,
                                        final GetResultListCallback listener) {

        if (listener == null)
            return;
        new Thread() {
            @Override
            public void run() {
                // 链接地址
                // Post参数
                String url = URLConfig.getM_SAVECAR_URL();
                String post = CreatPostString.getSaveCar(carInfo.getCityCode(),
                        carInfo.getCityName(), carInfo.getCarNo(),
                        carInfo.getEngineNo(), carInfo.getStandcarNo(),
                        carInfo.getRegistNo());

                DefaultParser mParser = new DefaultParser();
                BaseResponseInfo mBaseResponseInfo = mParser
                        .getBaseResponseInfo(url, post);
                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
                    listener.onFinished(mBaseResponseInfo);
                    String carNo = carInfo.getCarNo();
                    String carNoMain = GetCarInfo.getInstance().carNO;
                    if (carNo.equals(carNoMain)) {
                        String carCityName = carInfo.getCityName();
                        String carCityCode = carInfo.getCityCode();
                        String carEnginNo = carInfo.getEngineNo();
                        String standcarno = carInfo.getStandcarNo();
                        String registno = carInfo.getRegistNo();
                        if (!TextUtils.isEmpty(carCityName)) {
                            GetCarInfo.getInstance().city = carCityName;
                        }
                        if (!TextUtils.isEmpty(carCityCode)) {
                            GetCarInfo.getInstance().cityCode = carCityCode;
                        }
                        if (!TextUtils.isEmpty(carEnginNo)) {
                            GetCarInfo.getInstance().engineno = carEnginNo;
                        }
                        if (!TextUtils.isEmpty(standcarno)) {
                            GetCarInfo.getInstance().vin = standcarno;
                        }
                        if (!TextUtils.isEmpty(registno)) {
                            GetCarInfo.getInstance().registno = registno;
                        }
                    }
                } else {

                    listener.onErro(mBaseResponseInfo);
                }

            }

        }.start();
    }

    /**
     * 获取违章车辆列表 调用成功会返回<ArrayList<CarInfo>>
     */
    public static void GetCarInfoListResult(final GetResultListCallback listener) {

        if (listener == null)
            return;
        new Thread() {
            @Override
            public void run() {
                // 链接地址
                // Post参数
                String url = URLConfig.getM_GETCARLIST_URL();
                String post = CreatPostString.getCarList();

                CarInfoListParser mParser = new CarInfoListParser();
                BaseResponseInfo mBaseResponseInfo = mParser
                        .getBaseResponseInfo(url, post);
                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
                    listener.onFinished(mParser.getReturn());
                } else {

                    listener.onErro(mBaseResponseInfo);
                }

            }

        }.start();
    }

    /**
     * 获取单个违章车辆信息 调用成功会返回<CarInfo>
     * @param id
     *         violation/getCarList接口得到的主键id
     */
    public static void GetCarInfoResult(final String id,
                                        final GetResultListCallback listener) {

        if (listener == null)
            return;
        new Thread() {
            @Override
            public void run() {
                // 链接地址
                // Post参数
                String url = URLConfig.getM_GETCARINFO_URL();
                String post = CreatPostString.getCarInfo(id);

                CarInfoParser mParser = new CarInfoParser();
                BaseResponseInfo mBaseResponseInfo = mParser
                        .getBaseResponseInfo(url, post);
                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
                    listener.onFinished(mParser.getReturn());
                } else {

                    listener.onErro(mBaseResponseInfo);
                }

            }

        }.start();
    }

    /**
     * 获取删除违章车辆 调用成功会返回<null>
     * @param id
     *         violation/getCarList接口得到的主键id
     */
    public static void GetDelCarResult(final String id,
                                       final GetResultListCallback listener) {

        if (listener == null)
            return;
        new Thread() {
            @Override
            public void run() {
                // 链接地址
                // Post参数
                String url = URLConfig.getM_DELCAR_URL();
                String post = CreatPostString.getDelCar(id);

                DefaultParser mParser = new DefaultParser();
                BaseResponseInfo mBaseResponseInfo = mParser
                        .getBaseResponseInfo(url, post);
                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
                    listener.onFinished(mParser.getReturn());
                } else {

                    listener.onErro(mBaseResponseInfo);
                }

            }

        }.start();
    }

    /**
     * 获取设置为我的车辆 调用成功会返回<null>
     * @param id
     *         violation/getCarList接口得到的主键id
     */
    public static void GetSetMyCarResult(final String id,
                                         final GetResultListCallback listener) {

        if (listener == null)
            return;
        new Thread() {
            @Override
            public void run() {
                // 链接地址
                // Post参数
                String url = URLConfig.getM_SETMYCAR_URL();
                String post = CreatPostString.getSetMyCar(id);

                DefaultParser mParser = new DefaultParser();
                BaseResponseInfo mBaseResponseInfo = mParser
                        .getBaseResponseInfo(url, post);
                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
                    listener.onFinished(mParser.getReturn());
                } else {
                    listener.onErro(mBaseResponseInfo);
                }

            }

        }.start();
    }

    /**
     * 获取某月预约状态 调用成功会返回ArrayList<MonthOrderStateInfo>
     */

    public static void GetMonthOrderStateResult(final String date,
                                                final GetResultListCallback listener) {

        if (listener == null)
            return;
        new Thread() {
            @Override
            public void run() {
                // 链接地址
                String url = URLConfig.getM_MONTHORDERSTATE_URL();
                // Post参数
                String post = CreatPostString.getMonthOrderState(date);

                MonthOrderStateParser mParser = new MonthOrderStateParser();
                BaseResponseInfo mBaseResponseInfo = mParser
                        .getBaseResponseInfo(url, post);
                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
                    ArrayList<MonthOrderStateInfo> mList = mParser.getReturn();
                    listener.onFinished(mList);
                } else {
                    listener.onErro(mBaseResponseInfo);
                }

            }

        }.start();
    }

    /**
     * 获取某天预约状态 调用成功会返回ArrayList<DayOrderStateInfo>
     */

    public static void GetDayOrderStateResult(final String date,
                                              final GetResultListCallback listener) {

        if (listener == null)
            return;
        new Thread() {
            @Override
            public void run() {
                // 链接地址
                String url = URLConfig.getM_DAYORDERSTATE_URL();
                // Post参数
                String post = CreatPostString.getDayOrderState(date);

                DayOrderStateParser mParser = new DayOrderStateParser();
                BaseResponseInfo mBaseResponseInfo = mParser
                        .getBaseResponseInfo(url, post);
                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
                    ArrayList<DayOrderStateInfo> mList = mParser.getReturn();
                    listener.onFinished(mList);
                } else {
                    listener.onErro(mBaseResponseInfo);
                }

            }

        }.start();
    }

    /**
     * 提交预约请求 type:1-维修 2-保养 调用成功执行onFinished
     */

    public static void GetSubmitorderResult(final String date,
                                            final String time, final int type,
                                            final GetResultListCallback listener) {

        if (listener == null)
            return;
        new Thread() {
            @Override
            public void run() {
                // 链接地址
                String url = URLConfig.getM_SUBMITORDER_URL();
                // Post参数
                String post = CreatPostString.getSubmitOrder(date, time, type);

                DefaultParser mParser = new DefaultParser();
                BaseResponseInfo mBaseResponseInfo = mParser
                        .getBaseResponseInfo(url, post);
                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
                    listener.onFinished(mBaseResponseInfo);
                } else {
                    listener.onErro(mBaseResponseInfo);
                }

            }

        }.start();
    }

    /**
     * 领取奖品请求 onFinished 返还String 直接显示 onErro 返还String 直接显示
     */

    public static void GetReceivePrizeResult(final SecretaryMessageInfo mInfo,
                                             final GetResultListCallback listener) {

        if (listener == null)
            return;
        new Thread() {
            @Override
            public void run() {
                // 链接地址
                String url = URLConfig.getM_RECEIVEPRIZE_URL();
                // Post参数
                String post = CreatPostString.getReceivePrize(mInfo.getRelid());

                DefaultParser mParser = new DefaultParser();
                BaseResponseInfo mBaseResponseInfo = mParser
                        .getBaseResponseInfo(url, post);
                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
                    if (mInfo.getIsgot() == SecretaryMessageInfo.GOT_NO) {
                        mInfo.setIsgot(SecretaryMessageInfo.GOT_YES);
                        listener.onFinished("领取成功");
                    }

                } else {
                    listener.onErro("领取失败 " + mBaseResponseInfo.getInfo());
                }

            }

        }.start();
    }

    /**
     * 活动报名请求 onFinished 返还String 直接显示 onErro 返还String 直接显示
     */

    public static void GetActivitySignResult(final SecretaryMessageInfo mInfo,
                                             final GetResultListCallback listener) {

        if (listener == null)
            return;
        new Thread() {
            @Override
            public void run() {
                // 链接地址
                String url = URLConfig.getM_ACTIVITYSIGN_URL();
                // Post参数
                String post = CreatPostString.getActivitySign(mInfo.getRelid());

                DefaultParser mParser = new DefaultParser();
                BaseResponseInfo mBaseResponseInfo = mParser
                        .getBaseResponseInfo(url, post);
                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
                    if (mInfo.getIsgot() == SecretaryMessageInfo.GOT_NO) {
                        mInfo.setIsgot(SecretaryMessageInfo.GOT_YES);
                        listener.onFinished("报名成功");
                    } else {
                        mInfo.setIsgot(SecretaryMessageInfo.GOT_NO);
                        listener.onFinished("取消报名成功");
                    }

                } else {
                    listener.onErro("报名失败 " + mBaseResponseInfo.getInfo());
                }

            }

        }.start();
    }

    /**
     * 获取求援电话列表 调用成功会返回ArrayList<HelpPhoneInfo>
     */

    public static void GetHelpPhoneListResult(
            final GetResultListCallback listener) {

        if (listener == null)
            return;
        new Thread() {
            @Override
            public void run() {
                // 链接地址
                String url = URLConfig.getM_HELPPHONE_URL();
                // Post参数
                String post = CreatPostString.getHelpPhone();

                HelpPhoneListParser mParser = new HelpPhoneListParser();
                BaseResponseInfo mBaseResponseInfo = mParser
                        .getBaseResponseInfo(url, post);
                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
                    ArrayList<HelpPhoneInfo> mList = mParser.getReturn();
                    listener.onFinished(mList);
                } else {
                    listener.onErro(mBaseResponseInfo);
                }

            }

        }.start();
    }

    /**
     * 获取胎压数据 调用成功会返回<TirepressureInfo>
     */

    public static void GetTirepressureResult(
            final GetResultListCallback listener) {

        if (listener == null)
            return;
        new Thread() {
            @Override
            public void run() {
                // 链接地址
                String url = URLConfig.getM_TIREPRESSURE_URL();
                // Post参数
                String post = CreatPostString.getTirepressure(true);

                TirepressureParser mParser = new TirepressureParser();
                BaseResponseInfo mBaseResponseInfo = mParser
                        .getBaseResponseInfo(url, post);
                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
                    listener.onFinished(mParser.getReturn());
                } else {
                    listener.onErro(mBaseResponseInfo);
                }

            }

        }.start();
    }

    /**
     * 获取直接式胎压检测数据 调用成功会返回<TirepressureInfo>
     */

    public static void GetTireDirectResult(final GetResultListCallback listener) {

        if (listener == null)
            return;
        new Thread() {
            @Override
            public void run() {
                // 链接地址
                String url = URLConfig.getM_DIRECTPRESSURE_URL();
                // Post参数
                String post = CreatPostString.getTiredirect();

                TiredirectParser mParser = new TiredirectParser();
                BaseResponseInfo mBaseResponseInfo = mParser
                        .getBaseResponseInfo(url, post);
                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
                    listener.onFinished(mParser.getReturn());
                } else {
                    listener.onErro(mBaseResponseInfo);
                }

            }

        }.start();
    }

    /**
     * 获取胎压激活进度数据 调用成功会返回<TirepressureInfo>
     */

    public static void GetTireProgressResult(
            final GetResultListCallback listener) {

        if (listener == null)
            return;
        new Thread() {
            @Override
            public void run() {
                // 链接地址
                String url = URLConfig.getM_TIREPROGRESS_URL();
                // Post参数
                String post = CreatPostString.getTireprogress();

                TireProgressParser mParser = new TireProgressParser();
                BaseResponseInfo mBaseResponseInfo = mParser
                        .getBaseResponseInfo(url, post);
                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
                    listener.onFinished(mParser.getReturn());
                } else {
                    listener.onErro(mBaseResponseInfo);
                }

            }

        }.start();
    }

    /**
     * 获取自学习 调用成功会返回<null>
     */

    public static void GetTirepresLearnResult(
            final GetResultListCallback listener) {

        if (listener == null)
            return;
        new Thread() {
            @Override
            public void run() {
                // 链接地址
                String url = URLConfig.getM_TIREPRESLEARN_URL();
                // Post参数
                String post = CreatPostString.getTirepreslearn();

                DefaultParser mParser = new DefaultParser();
                BaseResponseInfo mBaseResponseInfo = mParser
                        .getBaseResponseInfo(url, post);
                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
                    listener.onFinished(null);
                } else {
                    listener.onErro(mBaseResponseInfo);
                }

            }

        }.start();
    }

    /**
     * 获取体检数据 调用成功会返回<PhysicalExaminationInfo>
     */

    public static void GetPhysicalExaminationResult(
            final GetResultListCallback listener) {

        if (listener == null)
            return;
        new Thread() {
            @Override
            public void run() {
                // 链接地址
                String url = URLConfig.getM_CHECKFAULT_URL();
                // Post参数
                String post = CreatPostString.getCheckFault();
                CheckFaultListParser mParser = new CheckFaultListParser();
                BaseResponseInfo mBaseResponseInfo = mParser
                        .getBaseResponseInfo(url, post);
                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS
                        && mParser.getIsrunning() == 1) {
                    PhysicalExaminationInfo mPhysicalExaminationInfo = new PhysicalExaminationInfo();
                    ArrayList<CheckFaultInfo> listP = new ArrayList<CheckFaultInfo>();
                    ArrayList<CheckFaultInfo> listB = new ArrayList<CheckFaultInfo>();
                    ArrayList<CheckFaultInfo> listC = new ArrayList<CheckFaultInfo>();
                    ArrayList<CheckFaultInfo> listU = new ArrayList<CheckFaultInfo>();
                    try {
                        InputStream is = DorideApplication.ApplicationContext
                                .getAssets().open("obdcode.txt");
                        BufferedReader in = new BufferedReader(
                                new InputStreamReader(is));
                        String line = "";
                        while ((line = in.readLine()) != null) {
                            CheckFaultInfo mCheckFaultInfo = new CheckFaultInfo();
                            String code = line.substring(0, 6);
                            String name = line.substring(6).trim();
                            mCheckFaultInfo.setCode(code);
                            mCheckFaultInfo.setCn(name);
                            if (code.startsWith("P")) {
                                listP.add(mCheckFaultInfo);
                            } else if (code.startsWith("B")) {
                                listB.add(mCheckFaultInfo);
                            } else if (code.startsWith("C")) {
                                listC.add(mCheckFaultInfo);
                            } else if (code.startsWith("U")) {
                                listU.add(mCheckFaultInfo);
                            }
                        }

                    } catch (IOException e) {
                        Log.e("info", "GetPhysicalExaminationResult---e==" + e);
                    }
                    mPhysicalExaminationInfo.setListP(listP);
                    mPhysicalExaminationInfo.setListB(listB);
                    mPhysicalExaminationInfo.setListC(listC);
                    mPhysicalExaminationInfo.setListU(listU);

                    mPhysicalExaminationInfo.setMistakeListP(mParser
                            .getMistakeListP());
                    mPhysicalExaminationInfo.setMistakeListB(mParser
                            .getMistakeListB());
                    mPhysicalExaminationInfo.setMistakeListC(mParser
                            .getMistakeListC());
                    mPhysicalExaminationInfo.setMistakeListU(mParser
                            .getMistakeListU());
                    mPhysicalExaminationInfo.setPoint(mParser.getPoint());
                    mPhysicalExaminationInfo.setIsrunning(mParser
                            .getIsrunning());
                    mPhysicalExaminationInfo.setId(mParser.getId());
                    listener.onFinished(mPhysicalExaminationInfo);

                    mPhysicalExaminationInfo.setShareTitle(mParser
                            .getShareTitle());
                    mPhysicalExaminationInfo.setShareTitle(mParser
                            .getShareText());
                    mPhysicalExaminationInfo.setShareTitle(mParser
                            .getShareLink());
                } else {
                    if (mParser.getIsrunning() == 0) {
                        mBaseResponseInfo.setInfo("车辆未在行驶中，不能获取数据");
                    }
                    listener.onErro(mBaseResponseInfo);
                }

            }

        }.start();
    }

    /**
     * 聚合 获取违章查询省份 onFinished(o1,o2,o3); o1:ArrayList<ProvinceInfo> o2:ArrayList
     * <CityInfo> o3:null
     */

    public static void GetProvinceListResultJUHE(
            final GetResultList2Callback listener) {

        if (listener == null)
            return;
        new Thread() {
            @Override
            public void run() {

                ProvinceListJUHEParser mParser = new ProvinceListJUHEParser();
                CityListJUHEParser mParser2 = new CityListJUHEParser(0, "rm");
                listener.onFinished(mParser.getmProvinceInfoList(), mParser2.getmCityInfoList(), null);
            }

        }.start();
    }

    /**
     * 获取违章查询省份 onFinished(o1,o2,o3); o1:ArrayList<ProvinceInfo> o2:ArrayList
     * <CityInfo> o3:null
     */

    public static void GetProvinceListResult(
            final GetResultList2Callback listener) {

        if (listener == null)
            return;
        new Thread() {
            @Override
            public void run() {

                ProvinceListParser mParser = new ProvinceListParser();
                City2ListParser mParser2 = new City2ListParser(0, "rm");
                listener.onFinished(mParser.getmProvinceInfoList(),
                        mParser2.getmCityInfoList(), null);
            }

        }.start();
    }

    /**
     * 获取所有城市
     * @param listener
     */
    public static void GetAllCityInfoResult(
            final GetResultList2Callback listener) {

        if (listener == null)
            return;
        new Thread() {
            @Override
            public void run() {
                AllCityInfoListParser mParser = new AllCityInfoListParser();
                listener.onFinished(null, mParser.getmCityInfoList(), null);
            }

        }.start();
    }

    /**
     * @param listener
     *         初始化违章信息查询
     */
    public static void InitViolation(final PostViolationInfo mInfo,
                                     final GetResultListCallback listener) {

        new Thread() {
            @Override
            public void run() {

                // 链接地址
                String url = URLConfig.getM_INITVIOLATION_NEW_URL();
                // Post参数
                String post = CreatPostString.getinitViolation(mInfo);
                DefaultParser mParser = new DefaultParser();
                BaseResponseInfo mBaseResponseInfo = mParser
                        .getBaseResponseInfo(url, post);
                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
                    GetCarInfo.getInstance().cityCode = mInfo.getCityCodeId();
                    GetCarInfo.getInstance().registno = mInfo.getRegistno();
                    GetCarInfo.getInstance().carNO = mInfo.getCarno();
                    GetCarInfo.getInstance().engineno = mInfo.getEngineno();
                    GetCarInfo.getInstance().shortStandCarNO = mInfo.getStandcarno();
                    GetCarInfo.getInstance().canQueryVio = 1;
                    if (listener != null) {
                        listener.onFinished(mBaseResponseInfo);
                    }
                } else {
                    if (listener != null) {
                        listener.onErro(mBaseResponseInfo);
                    }
                }

            }
        }.start();
    }

    /**
     * 获取推荐顾问<RecommendSalesInfo>
     */

    private static RecommendSalesInfo GetRecommendSalesResult() {

        // 链接地址
        String url = URLConfig.getM_RECOMMENDSALES_URL();
        //
        // RecommendSalesInfo mRecommendSalesInfo =
        // RecommendSales.getmRecommendSalesInfo();
        // // Post参数
        // String post = "";
        // if (mRecommendSalesInfo != null) {
        // post =
        // CreatPostString.getRecommendSales(mRecommendSalesInfo.getId());
        // } else {
        //
        // }

        String post = CreatPostString.getRecommendSales("");
        RecommendSalesParser mParser = new RecommendSalesParser();
        BaseResponseInfo mBaseResponseInfo = mParser.getBaseResponseInfo(url,
                post);
        if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
            RecommendSalesInfo mRecommendSalesInfo = mParser.getReturn();
            // RecommendSales.setmRecommendSalesInfo(mRecommendSalesInfo);
            return mRecommendSalesInfo;
        } else {
            return null;
        }
    }

    /**
     * 获取远程诊断 onFinished<DiagnoseInfo>
     */

    public static void GetDiagnoseListResult(final String id,
                                             final GetResultListCallback listener) {
        if (listener == null)
            return;
        new Thread() {
            @Override
            public void run() {
                // 链接地址
                String url = URLConfig.getM_DIAGNOSE_URL();
                // Post参数
                String post = CreatPostString.getDiagnoseList(id);
                DiagnoseListParser mParser = new DiagnoseListParser();
                BaseResponseInfo mBaseResponseInfo = mParser
                        .getBaseResponseInfo(url, post);
                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
                    RecommendSalesInfo rs = GetRecommendSalesResult();
                    if (rs != null) {
                        ArrayList<CheckFaultInfo> mList = mParser.getReturn();
                        DiagnoseInfo mDiagnoseInfo = new DiagnoseInfo();
                        mDiagnoseInfo.setmCheckFaultInfoList(mList);
                        mDiagnoseInfo.setmRecommendSalesInfo(rs);
                        listener.onFinished(mDiagnoseInfo);
                    } else {
                        Log.e("info", "无顾问");
                    }
                } else {
                    listener.onErro(mBaseResponseInfo);
                }
            }

        }.start();
    }

    public final static int SUBMITPROBLEM_phone = 1;

    public final static int SUBMITPROBLEM_send = 2;

    /**
     * 发送问题 onFinished表示成功
     */
    public static void GetSubmitproblemResult(final String relid,
                                              final String salesid, final int diagway,
                                              final GetResultListCallback listener) {

        new Thread() {
            @Override
            public void run() {
                // 链接地址
                String url = URLConfig.getM_SUBMITPROBLEM_URL();
                // Post参数
                String post = CreatPostString.getSubmitproblem(relid, salesid,
                        diagway);

                DefaultParser mParser = new DefaultParser();
                BaseResponseInfo mBaseResponseInfo = mParser
                        .getBaseResponseInfo(url, post);
                if (listener != null) {
                    if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
                        listener.onFinished(null);
                    } else {
                        listener.onErro(mBaseResponseInfo);
                    }
                }

            }

        }.start();
    }

    /**
     * 获取违章查询城市ArrayList<CityInfo>
     */

    public static void GetCityListResultJUHE(final String provinceCode,
                                             final GetResultListCallback listener) {

        if (listener == null)
            return;
        new Thread() {
            @Override
            public void run() {
                // CityListParser mParser = new CityListParser(1, provinceCode);
                // listener.onFinished(mParser.getmCityInfoList());

                CityListJUHEParser mParser = new CityListJUHEParser(1,
                        provinceCode);
                listener.onFinished(mParser.getmCityInfoList());
            }

        }.start();
    }

    /**
     * 获取违章查询城市ArrayList<CityInfo>
     */

    public static void GetCityListResult(final String provinceCode,
                                         final GetResultListCallback listener) {

        if (listener == null)
            return;
        new Thread() {
            @Override
            public void run() {
                // CityListParser mParser = new CityListParser(1, provinceCode);
                // listener.onFinished(mParser.getmCityInfoList());

                City2ListParser mParser = new City2ListParser(1, provinceCode);
                listener.onFinished(mParser.getmCityInfoList());
            }

        }.start();
    }

    /**
     * 驾驶证页面 调用成功会返回<UserLicenseInfo>
     */
    public static void GetMyLicenseResult(final GetResultListCallback listener) {
        new Thread() {

            @Override
            public void run() {

                // 链接地址
                String url = URLConfig.getM_MYLICENCE_URL();
                // Post参数
                String post = CreatPostString.getMyLicence();

                MyLicenceParser MyLicenceParser = new MyLicenceParser();
                BaseResponseInfo mBaseResponseInfo = MyLicenceParser
                        .getBaseResponseInfo(url, post);

                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
                    UserLicenseInfo mUserMediaInfo = MyLicenceParser
                            .getReturn();
                    listener.onFinished(mUserMediaInfo);
                } else {
                    listener.onErro(mBaseResponseInfo);
                }
            }

        }.start();

    }

    /**
     * 驾驶证等级列表 调用成功会返回ArrayList<LicenceLevelInfo>
     */

    public static void GetLicenceLevelResult(
            final GetResultListCallback listener) {

        if (listener == null)
            return;
        new Thread() {
            @Override
            public void run() {
                ArrayList<LicenceLevelInfo> mList;
                mList = DaoControl.getInstance().getLicenceLevelList();
                listener.onFinished(mList);
            }

        }.start();
    }

    public static ArrayList<MedalInfo> MedalSort(ArrayList<GotMediaInfo> rule) {
        ArrayList<MedalInfo> allList = DaoControl.getInstance().getMedalList();
        ArrayList<MedalInfo> gotList = new ArrayList<MedalInfo>();
        ArrayList<MedalInfo> ungotList = new ArrayList<MedalInfo>();

        for (int i = 0; i < allList.size(); i++) {
            MedalInfo mMedalInfo = allList.get(i);
            boolean finded = false;
            String id = mMedalInfo.getId();
            for (int j = 0; j < rule.size(); j++) {
                GotMediaInfo mGotMediaInfo = rule.get(j);
                if (id.equals(mGotMediaInfo.getMedalid())) {
                    finded = true;
                    break;
                }
            }
            mMedalInfo.setIsgot(finded);
            if (finded) {
                gotList.add(mMedalInfo);
            } else {
                ungotList.add(mMedalInfo);
            }

        }
        gotList.addAll(ungotList);
        return gotList;

    }

    /**
     * 获取挑战列表 调用成功会返回ArrayList<ChallengeInfo>
     */

    public static void GetChallengeListResult(
            final GetResultListCallback listener) {

        if (listener == null)
            return;
        new Thread() {
            @Override
            public void run() {

                // 链接地址
                String url = URLConfig.getM_CHALLENGELIST_URL();
                // Post参数
                String post = CreatPostString.getChallengeList();

                ChallengeListParser mParser = new ChallengeListParser();
                BaseResponseInfo mBaseResponseInfo = mParser
                        .getBaseResponseInfo(url, post);
                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
                    ArrayList<ChallengeInfo> infoList = mParser.getReturn();
                    listener.onFinished(infoList);
                } else {
                    listener.onErro(mBaseResponseInfo);
                }

            }

        }.start();
    }

    /**
     * 获取开始挑战信息 调用成功会返回String ID;
     */
    public static void GetStartChallengeResult(final String id,
                                               final GetResultListCallback listener) {

        if (listener == null)
            return;
        new Thread() {
            @Override
            public void run() {

                // 链接地址
                String url = URLConfig.getM_STARTCHALLENGE_URL();
                // Post参数
                String post = CreatPostString.getStartChallenge(id);

                ChallengeStartParser mParser = new ChallengeStartParser();
                BaseResponseInfo mBaseResponseInfo = mParser
                        .getBaseResponseInfo(url, post);
                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
                    String id = mParser.getReturn();
                    listener.onFinished(id);
                } else {
                    listener.onErro(mBaseResponseInfo);
                }

            }

        }.start();
    }

    /**
     * 获取终止挑战信息 调用成功会onFinished;
     */
    public static BaseResponseInfo GetEndChallengeResult(final String id) {

        // 链接地址
        String url = URLConfig.getM_ENDCHALLENGE_URL();
        // Post参数
        String post = CreatPostString.getEndChallenge(id);

        DefaultParser mParser = new DefaultParser();
        BaseResponseInfo mBaseResponseInfo = mParser.getBaseResponseInfo(url,
                post);
        return mBaseResponseInfo;
    }

    /**
     * 获取当次挑战成绩 调用成功会返回<ChallengeScore>
     */

    public static void GetChallengeScoreResult(final String id,
                                               final boolean flag, final GetResultListCallback listener) {

        if (listener == null)
            return;
        new Thread() {
            @Override
            public void run() {
                // BaseResponseInfo result = GetEndChallengeResult(id);
                // if (result.getFlag() != BaseResponseInfo.SUCCESS) {
                // listener.onErro(result);
                // return;
                // }
                if (flag) {
                    // 先调用终止挑战
                    GetEndChallengeResult(id);
                }

                // 链接地址
                String url = URLConfig.getM_CHALLENGESCORE_URL();
                // Post参数
                String post = CreatPostString.getChallengeScore(id);

                ChallengeScoreParser mParser = new ChallengeScoreParser();
                BaseResponseInfo mBaseResponseInfo = mParser
                        .getBaseResponseInfo(url, post);
                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
                    ChallengeScore mChallengeScore = mParser.getReturn();
                    listener.onFinished(mChallengeScore);
                } else {
                    listener.onErro(mBaseResponseInfo);
                }

            }

        }.start();
    }

    /**
     * 获取挑战历史最佳成绩 调用成功会返回<ChallengeScore>
     */

    public static void GetChallengeBestResult(final String id,
                                              final GetResultListCallback listener) {

        if (listener == null)
            return;
        new Thread() {
            @Override
            public void run() {
                // 链接地址
                String url = URLConfig.getM_CHALLENGEBEST_URL();
                // Post参数
                String post = CreatPostString.getChallengeBest(id);

                ChallengeScoreParser mParser = new ChallengeScoreParser();
                BaseResponseInfo mBaseResponseInfo = mParser
                        .getBaseResponseInfo(url, post);
                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
                    ChallengeScore mChallengeScore = mParser.getReturn();
                    listener.onFinished(mChallengeScore);
                } else {
                    listener.onErro(mBaseResponseInfo);
                }

            }

        }.start();
    }

    /**
     * 添加好友 调用成功会返回<String> userid
     */
    public static void GetAddFriendResult(final String userid,
                                          final GetResultListCallback listener) {
        GetUpdataFriendResult(userid, FRIEND_ADD, listener);
    }

    /**
     * 解除好友 调用成功会返回<String> userid
     */
    public static void GetRemoveFriendResult(final String userid,
                                             final GetResultListCallback listener) {
        GetUpdataFriendResult(userid, FRIEND_REMOVE, listener);
    }

    public final static String FRIEND_ADD = "add";

    public final static String FRIEND_REMOVE = "remove";

    public static void GetUpdataFriendResult(final String userid,
                                             final String action, final GetResultListCallback listener) {

        if (listener == null)
            return;
        new Thread() {
            @Override
            public void run() {
                // 链接地址
                String url = URLConfig.getM_UPDATEFRIEND_URL();
                // Post参数
                String post = CreatPostString.getUpdataFriend(userid, action);

                DefaultParser mParser = new DefaultParser();
                BaseResponseInfo mBaseResponseInfo = mParser
                        .getBaseResponseInfo(url, post);
                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
                    listener.onFinished(userid);
                } else {
                    listener.onErro(mBaseResponseInfo);
                }

            }

        }.start();
    }

    /**
     * 通过手机号添加好友 调用成功会返回<FriendDetialInfo>
     */

    public static void GetSearchFriendResult(final String phoneNum,
                                             final GetResultListCallback listener) {

        if (listener == null)
            return;
        new Thread() {
            @Override
            public void run() {
                // 链接地址
                String url = URLConfig.getM_SEARCHFRIEND_URL();
                // Post参数
                String post = CreatPostString.getSearchFriend(phoneNum);

                MyFriendDetailParser mParser = new MyFriendDetailParser();
                BaseResponseInfo mBaseResponseInfo = mParser
                        .getBaseResponseInfo(url, post);
                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
                    listener.onFinished(mParser.getReturn());
                } else {
                    listener.onErro(mBaseResponseInfo);
                }

            }

        }.start();
    }

    /**
     * 获取车友列表 调用成功会返回ArrayList<MyFriendInfo>
     */

    public static void GetMyFriendListResult(
            final GetResultListCallback listener) {

        if (listener == null)
            return;
        new Thread() {
            @Override
            public void run() {
                // 链接地址
                String url = URLConfig.getM_FRIEND_LIST_URL();
                // Post参数
                String post = CreatPostString.getMyFriendList();

                MyFriendListParser mParser = new MyFriendListParser();
                BaseResponseInfo mBaseResponseInfo = mParser
                        .getBaseResponseInfo(url, post);
                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
                    ArrayList<MyFriendInfo> mList = mParser.getReturn();
                    listener.onFinished(mList);
                } else {
                    listener.onErro(mBaseResponseInfo);
                }

            }

        }.start();
    }

    /**
     * 获取邀请好友列表 调用成功会返回ArrayList<InviteFriendInfo>
     */

    public static void GetInviteFriendListResult(
            final GetResultListCallback listener) {

        if (listener == null)
            return;
        new Thread() {
            @Override
            public void run() {
                // 链接地址
                String url = URLConfig.getM_FRIEND_LIST_URL();
                // Post参数
                String post = CreatPostString.getMyFriendList();

                InviteFriendListParser mParser = new InviteFriendListParser();
                BaseResponseInfo mBaseResponseInfo = mParser
                        .getBaseResponseInfo(url, post);
                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
                    ArrayList<InviteFriendInfo> mList = mParser.getReturn();
                    listener.onFinished(mList);
                } else {
                    listener.onErro(mBaseResponseInfo);
                }

            }

        }.start();
    }

    /**
     * 获取车友详情 调用成功会返回<FriendDetialInfo>
     */

    public static void GetMyFriendDetialResult(final String id,
                                               final GetResultListCallback listener) {

        if (listener == null)
            return;
        new Thread() {
            @Override
            public void run() {
                // 链接地址
                String url = URLConfig.getM_FRIEND_DETIAL_URL();
                // Post参数
                String post = CreatPostString.getMyFriendDetial(id);

                MyFriendDetailParser mParser = new MyFriendDetailParser();
                BaseResponseInfo mBaseResponseInfo = mParser
                        .getBaseResponseInfo(url, post);
                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
                    // 车友基础信息
                    FriendDetialInfo mFriendDetialInfo = mParser.getReturn();

                    listener.onFinished(mFriendDetialInfo);
                } else {
                    listener.onErro(mBaseResponseInfo);
                }

            }

        }.start();
    }

    //
    // /**
    // * 获取车友动态列表 调用成功会返回<FriendFeedInfo>
    // */
    //
    // public static void GetFriendsShareInfoListResult(
    // final GetResultListCallback listener) {
    //
    // if (listener == null)
    // return;
    // new Thread() {
    // @Override
    // public void run() {
    // // 链接地址
    // String url = URLConfig.getM_FRIENDS_SHARE_LIST_URL();
    // // Post参数
    // String post = CreatPostString.getFriendsShareList();
    //
    // FriendsShareListParser mParser = new FriendsShareListParser();
    // BaseResponseInfo mBaseResponseInfo = mParser
    // .getBaseResponseInfo(url, post);
    // if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
    // FriendFeedInfo mFriendFeedInfo = mParser.getReturn();
    // listener.onFinished(mFriendFeedInfo);
    // } else {
    // listener.onErro(mBaseResponseInfo);
    // }
    //
    // }
    //
    // }.start();
    // }
    //

    /**
     * 获取车友动态列表 调用成功会返回<FriendFeedInfo>
     */

    public static void GetFriendsShareInfoListResult(final int limit,
                                                     final int offset, final GetResultListCallback listener) {

        if (listener == null)
            return;
        new Thread() {
            @Override
            public void run() {
                // 链接地址
                String url = URLConfig.getM_FRIENDS_SHARE_LIST_URL();
                // Post参数
                String post = CreatPostString
                        .getFriendsShareList(limit, offset);

                FriendsShareListParser mParser = new FriendsShareListParser();
                BaseResponseInfo mBaseResponseInfo = mParser
                        .getBaseResponseInfo(url, post);
                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
                    FriendFeedInfo mFriendFeedInfo = mParser.getReturn();
                    mFriendFeedInfo.setOffset(offset + limit);
                    listener.onFinished(mFriendFeedInfo);
                } else {
                    listener.onErro(mBaseResponseInfo);
                }

            }

        }.start();
    }

    /**
     * 车友PK onFinished(o1) o1 mFriendPKInfo
     */

    public static void GetFriendsPKResult(final GetResultListCallback listener,
                                          final String userid) {

        if (listener == null)
            return;
        new Thread() {
            @Override
            public void run() {
                // 链接地址
                String url = URLConfig.getM_FRIENDS_PK_URL();
                // Post参数
                String post = CreatPostString.getFriendspk(userid);

                FriendsPKParser mParser = new FriendsPKParser();
                BaseResponseInfo mBaseResponseInfo = mParser
                        .getBaseResponseInfo(url, post);
                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
                    FriendPKInfo mFriendPKInfo = mParser.getReturn();
                    listener.onFinished(mFriendPKInfo);
                } else {
                    listener.onErro(mBaseResponseInfo);
                }

            }

        }.start();
    }

    /**
     * 车友动态点赞接口 调用成功会返回<String> id
     */

    public static void GetFriendPokeListResult(final String id,
                                               final GetResultListCallback listener) {

        if (listener == null)
            return;
        new Thread() {
            @Override
            public void run() {
                // 链接地址
                String url = URLConfig.getM_FRIENDS_POKE_URL();
                // Post参数
                String post = CreatPostString.getFriendPoke(id);

                DefaultParser mParser = new DefaultParser();
                BaseResponseInfo mBaseResponseInfo = mParser
                        .getBaseResponseInfo(url, post);
                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
                    listener.onFinished(id);
                } else {
                    listener.onErro(id);
                }

            }

        }.start();
    }

    /**
     * 获取推送设置 调用成功会返回<PushSetInfo>
     */

    public static void GetPushSetResult(final GetResultListCallback listener) {

        if (listener == null)
            return;
        new Thread() {
            @Override
            public void run() {
                // 链接地址
                String url = URLConfig.getM_PUSH_SET_URL();
                // Post参数
                String post = CreatPostString.getPushSet();

                PushSetParser mParser = new PushSetParser();
                BaseResponseInfo mBaseResponseInfo = mParser
                        .getBaseResponseInfo(url, post);
                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
                    listener.onFinished(mParser.getReturn());
                } else {
                    listener.onErro(mBaseResponseInfo);
                }

            }

        }.start();

    }

    /**
     * 首次推送设置 调用成功执行onFinished，无返回数据<null>
     */
    public static void GetFirstUpdatePushSetResult(
            final PushSetInfo mPushSetInfo, final GetResultListCallback listener) {
        new Thread() {
            @Override
            public void run() {
                // 链接地址
                String url = URLConfig.getM_UPDATE_PUSH_URL();
                // Post参数
                String post = CreatPostString
                        .getFirstUpdatePushSet(mPushSetInfo);

                DefaultParser mParser = new DefaultParser();
                BaseResponseInfo mBaseResponseInfo = mParser
                        .getBaseResponseInfo(url, post);
                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
                    listener.onFinished(null);
                } else {
                    listener.onErro(mBaseResponseInfo);
                }

            }

        }.start();
    }

    /**
     * 更新推送设置 调用成功执行onFinished，无返回数据<null>
     */
    public static void GetUpdatePushSetResult(final String pushSetItemName,
                                              final String pushSetItemValue, final GetResultListCallback listener) {
        new Thread() {
            @Override
            public void run() {
                // 链接地址
                String url = URLConfig.getM_UPDATE_PUSH_URL();
                // Post参数
                String post = CreatPostString.getUpdatePushSet(pushSetItemName,
                        pushSetItemValue);

                DefaultParser mParser = new DefaultParser();
                BaseResponseInfo mBaseResponseInfo = mParser
                        .getBaseResponseInfo(url, post);
                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
                    String tag = UserInfo.getInstance().dealerId + "_31";
                    if (pushSetItemName
                            .equals(ManageMessageActivity.KEY_NAMES[6])) {
                        if (pushSetItemValue.equals("0")) {
                            // 不允许推送奖品活动信息
                            XGPushManager.setTag(
                                    DorideApplication.ApplicationContext, tag);
                        } else {
                            XGPushManager.deleteTag(
                                    DorideApplication.ApplicationContext, tag);
                        }
                    }
                    listener.onFinished(null);
                    Log.e("info", "更新推送设置 成功");
                } else {
                    listener.onErro(mBaseResponseInfo);
                    Log.e("info", "更新推送设置  失败");
                }

            }

        }.start();
    }

    /**
     * 修改车辆信息调用成功执行onFinished() 无返回值
     */

    public static void GetUpdateCarInfoResult(
            final ModifyCarInfo modifyCarInfo,
            final GetResultListCallback listener) {

        if (listener == null)
            return;
        new Thread() {
            @Override
            public void run() {
                String brandid = modifyCarInfo.getBrandid();
                String optionid = modifyCarInfo.getOptionid();
                String carid = modifyCarInfo.getCarid();
                String summiles = modifyCarInfo.getSummiles();
                String buydate = modifyCarInfo.getBuydate();

                // 链接地址
                String url = URLConfig.getM_EDITCARINFO_URL();
                // Post参数
                String post = CreatPostString.getModifyCarInfo(summiles,
                        buydate);

                ModifyCarInfoParser mParser = new ModifyCarInfoParser();
                BaseResponseInfo mBaseResponseInfo = mParser
                        .getBaseResponseInfo(url, post);
                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
                    OnDateChageConfig.ModifyCarChanged = true;
                    GetCarInfo.getInstance().brandid = Integer.parseInt(brandid);
                    GetCarInfo.getInstance().optionid = Integer.parseInt(optionid);
                    GetCarInfo.getInstance().styleId = Integer.parseInt(carid);
                    GetCarInfo.getInstance().carName = modifyCarInfo.getCarname();
                    GetCarInfo.getInstance().carLogo = modifyCarInfo.getCarlogo();
                    try {
                        GetCarInfo.getInstance().buyDate = MyTimeUtils.FORMAT.parse(buydate).getTime()/1000;
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    // String isSupSpecFunc = mParser.getValue("isSupSpecFunc");
                    //
                    // if (isSupSpecFunc.equals("1")) {
                    // LoginInfo.setSupport(true);
                    // ;
                    // } else {
                    // LoginInfo.setSupport(false);
                    // }
                    listener.onFinished(null);
                } else {
                    listener.onErro(mBaseResponseInfo);
                }

            }

        }.start();
    }

    private static final String seasame_haohua = "2195223";
    private static final String seasame_jiben  = "2195222";

    /**
     * 修改车辆车型信息 调用成功执行onFinished() 无返回值
     */
    public static void GetUpdateCarTypeResult(
            final ModifyCarInfo modifyCarInfo,
            final GetResultListCallback listener) {

        if (listener == null)
            return;
        new Thread() {
            @Override
            public void run() {
                String brandid = modifyCarInfo.getBrandid();
                String optionid = modifyCarInfo.getOptionid();
                String carid = modifyCarInfo.getCarid();
                // 链接地址
                String url = URLConfig.getM_MODIFYCAR_URL();
                // Post参数
                String post = CreatPostString.getModifyCarType(brandid,
                        optionid, carid);

                DefaultParser mParser = new DefaultParser();
                BaseResponseInfo mBaseResponseInfo = mParser
                        .getBaseResponseInfo(url, post);
                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
                    OnDateChageConfig.ModifyCarChanged = true;
                    GetCarInfo.getInstance().brandid = Integer.parseInt(brandid);
                    GetCarInfo.getInstance().optionid = Integer.parseInt(optionid);
                    GetCarInfo.getInstance().styleId = Integer.parseInt(carid);
                    GetCarInfo.getInstance().carName = modifyCarInfo.getCarname();
                    GetCarInfo.getInstance().carLogo = modifyCarInfo.getCarlogo();
                    // String isSupSpecFunc = mParser.getValue("isSupSpecFunc");
                    //
                    // if (isSupSpecFunc.equals("1")) {
                    // LoginInfo.setSupport(true);
                    // } else {
                    // LoginInfo.setSupport(false);
                    // }

                    // String carID = mParser.getValue("carid");
                    // if (carID.isEmpty()) {
                    // if (carid.equals(seasame_haohua)) {
                    // carID = seasame_jiben;
                    // } else {
                    // carID = seasame_haohua;
                    // }
                    // }
                    // if (carID.equals(seasame_haohua)) {
                    // // 豪华型
                    // LoginInfo.setRemoteControl("1");
                    // } else if (carID.equals(seasame_jiben)) {
                    // // 基本型
                    // LoginInfo.setRemoteControl("0");
                    // }

                    String year = modifyCarInfo.getYear();
                    Log.e("info", "carYear==" + year);
//                    SesameLoginInfo.setCar_year(MyParse.parseInt(year));
                    // if (year != null && !year.equals("")) {
                    // if (year.equals("2017")) {
                    // if (deviceType.equals(LoginInfo.DEVICETYPE_AFTER)) {
                    // LoginInfo
                    // .setDeviceType(LoginInfo.DEVICETYPE_AFTER2016);
                    // }
                    // } else {
                    // if (deviceType
                    // .equals(LoginInfo.DEVICETYPE_AFTER2016)) {
                    // LoginInfo
                    // .setDeviceType(LoginInfo.DEVICETYPE_AFTER);
                    // }
                    // }
                    // }

                    listener.onFinished(null);
                } else {
                    listener.onErro(mBaseResponseInfo);
                }

            }

        }.start();
    }

    /**
     * 修改车辆里程数信息 调用成功执行onFinished() 无返回值
     */

    public static void GetUpdateCarMileageResult(final String summiles,
                                                 final GetResultListCallback listener) {

        if (listener == null)
            return;
        new Thread() {
            @Override
            public void run() {
                // 链接地址
                String url = URLConfig.getM_EDITCARINFO_URL();
                // Post参数
                String post = CreatPostString.getModifyCarMileage(summiles);

                ModifyCarInfoParser mParser = new ModifyCarInfoParser();
                BaseResponseInfo mBaseResponseInfo = mParser
                        .getBaseResponseInfo(url, post);
                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
                    listener.onFinished(null);
                } else {
                    listener.onErro(mBaseResponseInfo);
                }

            }

        }.start();
    }

    /**
     * 修改车辆购买日期信息 调用成功执行onFinished() 无返回值
     */

    public static void GetUpdateCarDateResult(final String buydate,
                                              final GetResultListCallback listener) {

        if (listener == null)
            return;
        new Thread() {
            @Override
            public void run() {
                // 链接地址
                String url = URLConfig.getM_EDITCARINFO_URL();
                // Post参数
                String post = CreatPostString.getModifyCarDate(buydate);

                ModifyCarInfoParser mParser = new ModifyCarInfoParser();
                BaseResponseInfo mBaseResponseInfo = mParser
                        .getBaseResponseInfo(url, post);
                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
                    OnDateChageConfig.ModifyCarChanged = true;
                    try {
                        GetCarInfo.getInstance().buyDate = MyTimeUtils.FORMAT.parse(buydate).getTime()/1000;
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    listener.onFinished(null);
                } else {
                    listener.onErro(mBaseResponseInfo);
                }

            }

        }.start();
    }

    /**
     * 修改车辆上次保养里程 调用成功执行onFinished() 无返回值
     */

    public static void GetUpdateCarMaintenMilesResult(final String miles,
                                                      final GetResultListCallback listener) {

        if (listener == null)
            return;
        new Thread() {
            @Override
            public void run() {
                // 链接地址
                String url = URLConfig.getM_EDITCARINFO_URL();
                // Post参数
                String post = CreatPostString.getMaintenMiles(miles);

                ModifyCarInfoParser mParser = new ModifyCarInfoParser();
                BaseResponseInfo mBaseResponseInfo = mParser
                        .getBaseResponseInfo(url, post);
                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
                    OnDateChageConfig.ModifyCarChanged = true;
                    GetCarInfo.getInstance().maintenMiles = Integer.parseInt(miles);

                    listener.onFinished(null);
                } else {
                    listener.onErro(mBaseResponseInfo);
                }

            }

        }.start();
    }

    /**
     * 修改车辆上次保养日期 调用成功执行onFinished() 无返回值
     */

    public static void GetUpdateCarMaintenDateResult(final String date,
                                                     final GetResultListCallback listener) {

        if (listener == null)
            return;
        new Thread() {
            @Override
            public void run() {
                // 链接地址
                String url = URLConfig.getM_EDITCARINFO_URL();
                // Post参数
                String post = CreatPostString.getMaintenDate(date);

                ModifyCarInfoParser mParser = new ModifyCarInfoParser();
                BaseResponseInfo mBaseResponseInfo = mParser
                        .getBaseResponseInfo(url, post);
                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
                    OnDateChageConfig.ModifyCarChanged = true;
                    try {
                        GetCarInfo.getInstance().maintenDate = MyTimeUtils.FORMAT.parse(date).getTime()/1000;
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    listener.onFinished(null);
                } else {
                    listener.onErro(mBaseResponseInfo);
                }

            }

        }.start();
    }

    /**
     * 修改姓名 调用成功执行onFinished，返回数据<String>新属性
     */
    public static void GetUpadeNameResult(final String newValue,
                                          final GetResultListCallback listener) {

        if (listener == null) {
            return;
        }
        new Thread() {
            @Override
            public void run() {
                BaseResponseInfo flag = GetUpdateUserInfoResult(ACTIVION_NAME,
                        newValue, listener);
                if (flag == null) {
                    OnDateChageConfig.RealNameChanged = true;
                    UserInfo.getInstance().realName  = newValue;
                    listener.onFinished(newValue);
                } else {
                    listener.onErro(flag);
                }
            }

        }.start();
    }

    /**
     * 记录养护日志 调用成功执行onFinished() 无返回值
     */

    public static void GetMaintainLogResult(final GetResultListCallback listener) {

        if (listener == null)
            return;
        new Thread() {
            @Override
            public void run() {
                // 链接地址
                String url = URLConfig.getM_MAINTAINLOG_URL();
                // Post参数
                String post = CreatPostString.getMaintainLog();

                DefaultParser mParser = new DefaultParser();
                BaseResponseInfo mBaseResponseInfo = mParser
                        .getBaseResponseInfo(url, post);
                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {

                    GetCarInfo.getInstance().isNextMain = 0;
                    listener.onFinished(null);
                } else {
                    listener.onErro(mBaseResponseInfo);
                }

            }

        }.start();
    }

    /**
     * 修改性别 调用成功执行onFinished，返回数据<String>新属性 1男 2女
     */
    public static void GetUpadeGenderResult(final String newValue,
                                            final GetResultListCallback listener) {

        if (listener == null) {
            return;
        }
        new Thread() {
            @Override
            public void run() {

                BaseResponseInfo flag = GetUpdateUserInfoResult(
                        ACTIVION_GENDER, newValue, listener);
                if (flag == null) {
                    UserInfo.getInstance().gender = Integer.parseInt(newValue);
                    listener.onFinished(newValue);
                } else {
                    listener.onErro(flag);
                }
            }

        }.start();
    }

    /**
     * 修改头像 调用成功执行onFinished，返回数据<String>新属性
     */
    public static void GetUpadeAvatarResult(final String filePath,
                                            final GetResultListCallback listener) {
        if (listener == null) {
            return;
        }
        new Thread() {
            @Override
            public void run() {
                UploadImgInfo mUploadImgInfo = GetUploadImgResult(
                        UPLOADIMG_AVATAR, filePath);
                if (mUploadImgInfo != null) {
                    String id = mUploadImgInfo.getId();
                    if (id != null && id.length() > 0) {
                        BaseResponseInfo flag = GetUpdateUserInfoResult(
                                ACTIVION_AVATAR, id, listener);
                        if (flag == null) {
                            OnDateChageConfig.AvatarChanged = true;
                            UserInfo.getInstance().avatarFile = mUploadImgInfo.getFilePath();
                            listener.onFinished(filePath);
                        } else {
                            listener.onErro(flag);
                        }

                    }
                } else {
                    BaseResponseInfo flag = new BaseResponseInfo();
                    flag.setInfo("上传图片错误");
                    listener.onErro(flag);
                }
            }

        }.start();

    }

    private final static String ACTIVION_AVATAR = "avatar";

    private final static String ACTIVION_NAME = "realname";

    private final static String ACTIVION_GENDER = "gender";

    /**
     * 更新用户信息 return == null 代表成功 return != null 代表失败
     */
    private static BaseResponseInfo GetUpdateUserInfoResult(
            final String action, final String newValue,
            final GetResultListCallback listener) {

        // 链接地址
        String url = URLConfig.getM_EDITINFO_URL();
        // Post参数
        String post = CreatPostString.getUpdateUserInfo(action, newValue);

        DefaultParser mParser = new DefaultParser();
        BaseResponseInfo mBaseResponseInfo = mParser.getBaseResponseInfo(url,
                post);
        if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
            return null;
        } else {
            return mBaseResponseInfo;
        }

    }

    /**
     * 上传图片 调用成功执行onFinished ，返回<UploadImgInfo>
     */

    private static final String UPLOADIMG_AVATAR = "avatar";

    private static final String UPLOADIMG_SOS = "avatar";

    public static UploadImgInfo GetUploadImgResult(final String action,
                                                   final String filePath) {
        // 链接地址
        String url = URLConfig.getM_UPLOAD_IMG_URL();
        // Post参数
        File file = new File(filePath);
        UploadImgParser mParser = new UploadImgParser();
        BaseResponseInfo mBaseResponseInfo = mParser.getBaseResponseInfo(url,
                action, file);
        Log.e("info",
                "mBaseResponseInfo.getFlag()==" + mBaseResponseInfo.getFlag());
        if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
            return mParser.getReturn();

        } else {
            return null;
        }

    }

    /**
     * 行车报告分享回调 onFinished
     */
    public static void GetReportShareResult(final String sharetitle,
                                            final String sharetext, final String sharelink, final String date,
                                            final String reportType) {
        new Thread() {
            @Override
            public void run() {
                // 链接地址
                String url = URLConfig.getM_REPORTSHARE_URL();
                // Post参数
                String post = CreatPostString.getReportShare(sharetitle,
                        sharetext, sharelink, date, reportType);

                DefaultParser mParser = new DefaultParser();
                BaseResponseInfo mBaseResponseInfo = mParser
                        .getBaseResponseInfo(url, post);

            }

        }.start();

    }

    /**
     * 驾驶证等级分享回调 onFinished
     */
    public static void GetLicenseShareResult(final String licenseId) {
        new Thread() {
            @Override
            public void run() {
                // 链接地址
                String url = URLConfig.getM_LICENSESHARE_URL();
                // Post参数
                String post = CreatPostString.getLicenseShare(licenseId);
                DefaultParser mParser = new DefaultParser();
                BaseResponseInfo mBaseResponseInfo = mParser
                        .getBaseResponseInfo(url, post);

            }

        }.start();

    }

    /**
     * 邀请好友奖品列表 返还<InvitePrizeInfo>
     */
    public static void GetInvitePrizeResult(final GetResultListCallback listener) {
        if (listener == null) {
            return;
        }
        new Thread() {
            @Override
            public void run() {
                // 链接地址
                String url = URLConfig.getM_INVITE_PRIZE_URL();
                // Post参数
                String post = CreatPostString.getInvitePrize();
                InvitePrizeParser mParser = new InvitePrizeParser();
                BaseResponseInfo mBaseResponseInfo = mParser
                        .getBaseResponseInfo(url, post);
                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
                    listener.onFinished(mParser.getReturn());
                } else {
                    listener.onErro(mBaseResponseInfo);
                }
            }

        }.start();

    }

    /**
     * 座驾首页 返还<CarMainInfo>
     */
    public static void GetCarMainResult(final GetResultListCallback listener) {
        Log.e("info", "GetCarMainResult----------");
        if (listener == null) {
            return;
        }
        new Thread() {
            @Override
            public void run() {
                // 链接地址
                String url = URLConfig.getM_CAR_MAIN_URL();
                // Post参数
                String post = CreatPostString.getCarMain();
                CarMainParser mParser = new CarMainParser();
                BaseResponseInfo mBaseResponseInfo = mParser
                        .getBaseResponseInfo(url, post);
                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
                    listener.onFinished(mParser.getReturn());
                } else {
                    listener.onErro(mBaseResponseInfo);
                }
            }

        }.start();
    }

    /**
     * 远程-首页 <RemoteMainInfo>
     */
    public static void GetRemoteMainResult(final GetResultListCallback listener) {
        if (listener == null) {
            return;
        }
        new Thread() {
            @Override
            public void run() {
                // 链接地址
                String url = URLConfig.getM_REMOTE_INDEX_URL();
                // Post参数
                String post = CreatPostString.getRemoteMain();
                RemoteMainInfoParser mParser = new RemoteMainInfoParser();
                BaseResponseInfo mBaseResponseInfo = mParser
                        .getBaseResponseInfo(url, post);
                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
                    listener.onFinished(mParser.getReturn());
                } else {
                    listener.onErro(mBaseResponseInfo);
                }
            }

        }.start();

    }

    /**
     * 远程-校验远程密码
     * @param remote_pwd
     *         远程密码(md5加密传输)
     */
    public static void GetRemotePswVerify(final String remote_pwd,
                                          final GetResultListCallback listener) {
        if (listener == null) {
            return;
        }
        new Thread() {
            @Override
            public void run() {
                // 链接地址
                String url = URLConfig.getM_SAFE_REMOTEPWDVERIFY_URL();
                // Post参数
                String remote_pwd_md5 = FileUtil.stringToMD5(remote_pwd);
                String post = CreatPostString
                        .getRemotePswVerify(remote_pwd_md5);
                DefaultParser mParser = new DefaultParser();
                BaseResponseInfo mBaseResponseInfo = mParser
                        .getBaseResponseInfo(url, post);
                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
                    listener.onFinished(mParser.getReturn());
                } else {
                    listener.onErro(mBaseResponseInfo);
                }
            }

        }.start();

    }

    /**
     * 远程-声光寻车 成功返回onFinished(null)
     */
    public static void GetCarLocating(final GetResultListCallback listener) {
        if (listener == null) {
            return;
        }
        new Thread() {
            @Override
            public void run() {
                // 链接地址
                String url = URLConfig.getM_CAR_LOCATING_URL();
                // Post参数
                String post = CreatPostString
                        .getCarLocating(DorideApplication.MODEL_NAME);
                DefaultParser mParser = new DefaultParser();
                BaseResponseInfo mBaseResponseInfo = mParser
                        .getBaseResponseInfo(url, post);
                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
                    listener.onFinished(null);
                } else {
                    listener.onErro(mBaseResponseInfo);
                }
            }

        }.start();

    }

    /**
     * 流量包-充值记录<TrafficPackagePurchaseLogListInfo>
     * @param limit
     *         每页条数
     * @param offset
     *         偏移量
     */
    public static void GetTrafficPackageLogResult(final String url,final int limit, final int offset, final int package_type,
                                                  final GetResultListCallback listener) {

        if (listener == null)
            return;
        new Thread() {
            @Override
            public void run() {
                // Post参数
                String post = CreatPostString.getFeeLog(limit, offset, package_type);

                TrafficPackagePurchaseListParser mParser = new TrafficPackagePurchaseListParser();
                BaseResponseInfo mBaseResponseInfo = mParser
                        .getBaseResponseInfo(url, post);
                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
                    listener.onFinished(mParser.getReturn());
                } else {
                    listener.onErro(mBaseResponseInfo);
                }
            }
        }.start();
    }

    /**
     * 远程启动 --启动 成功返回onFinished(null)
     */
    public static void GetRemoteStart(final GetResultListCallback listener) {
        if (listener == null) {
            return;
        }
        new Thread() {
            @Override
            public void run() {
                // 链接地址
                String url = URLConfig.getM_REMOTESTART_URL();
                // Post参数
                String post = CreatPostString
                        .getRemoteStart(DorideApplication.MODEL_NAME);
                DefaultParser mParser = new DefaultParser();
                BaseResponseInfo mBaseResponseInfo = mParser
                        .getBaseResponseInfo(url, post);
                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {

                    listener.onFinished(null);
                } else {
                    listener.onErro(mBaseResponseInfo);
                }
            }

        }.start();

    }

    /**
     * 远程启动 --取消 成功返回onFinished(null)
     */
    public static void GetCancelRemoteStart(final GetResultListCallback listener) {
        if (listener == null) {
            return;
        }
        new Thread() {
            @Override
            public void run() {
                // 链接地址
                String url = URLConfig.getM_CANCEL_REMOTESTART_URL();
                // Post参数
                String post = CreatPostString
                        .getCancelRemoteStart(DorideApplication.MODEL_NAME);
                DefaultParser mParser = new DefaultParser();
                BaseResponseInfo mBaseResponseInfo = mParser
                        .getBaseResponseInfo(url, post);
                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
                    listener.onFinished(null);
                } else {
                    listener.onErro(mBaseResponseInfo);
                }
            }

        }.start();

    }

    /**
     * 远程打开后备箱 成功返回onFinished(null)
     */
    public static void GetRemoteTrunk(final GetResultListCallback listener) {
        if (listener == null) {
            return;
        }
        new Thread() {
            @Override
            public void run() {
                // 链接地址
                String url = URLConfig.getM_OPENTRUNK_URL();
                // Post参数
                String post = CreatPostString
                        .getRemoteTrunk(DorideApplication.MODEL_NAME);
                DefaultParser mParser = new DefaultParser();
                BaseResponseInfo mBaseResponseInfo = mParser
                        .getBaseResponseInfo(url, post);
                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
                    listener.onFinished(null);
                } else {
                    listener.onErro(mBaseResponseInfo);
                }
            }

        }.start();

    }

    /**
     * 远程启动空调 成功返回onFinished(null)
     */
    public static void GetRemoteAir(final GetResultListCallback listener,
                                    final String racoc) {
        if (listener == null) {
            return;
        }
        new Thread() {
            @Override
            public void run() {
                // 链接地址
                String url = URLConfig.getM_AIRCONDITIONER_URL();
                // Post参数
                String post = CreatPostString.getRemoteAir(racoc,
                        //						oc,
                        DorideApplication.MODEL_NAME);
                DefaultParser mParser = new DefaultParser();
                BaseResponseInfo mBaseResponseInfo = mParser
                        .getBaseResponseInfo(url, post);
                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
                    listener.onFinished(null);
                } else {
                    listener.onErro(mBaseResponseInfo);
                }
            }

        }.start();

    }

    /**
     * 远程开窗 成功返回onFinished(null)
     */
    public static void GetRemoteOpenwin(final GetResultListCallback listener) {
        if (listener == null) {
            return;
        }
        new Thread() {
            @Override
            public void run() {
                // 链接地址
                String url = URLConfig.getM_WINDOW_URL();
                // Post参数
                String post = CreatPostString
                        .getRemoteOpenwin(DorideApplication.MODEL_NAME);
                DefaultParser mParser = new DefaultParser();
                BaseResponseInfo mBaseResponseInfo = mParser
                        .getBaseResponseInfo(url, post);
                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
                    listener.onFinished(null);
                } else {
                    listener.onErro(mBaseResponseInfo);
                }
            }

        }.start();

    }

    // 远程-开启座椅加热
    public static void GetHeatSeatOpen(final GetResultListCallback listener) {
        if (listener == null) {
            return;
        }
        new Thread() {
            @Override
            public void run() {
                // 链接地址
                String url = URLConfig.getM_SEATHEAT_URL();
                // Post参数
                String post = CreatPostString
                        .getSeatHeatOpen(DorideApplication.MODEL_NAME);
                DefaultParser mParser = new DefaultParser();
                BaseResponseInfo mBaseResponseInfo = mParser
                        .getBaseResponseInfo(url, post);
                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
                    listener.onFinished(null);
                } else {
                    listener.onErro(mBaseResponseInfo);
                }
            }

        }.start();

    }

    // 远程-关闭座椅加热
    public static void GetHeatSeatClose(final GetResultListCallback listener) {
        if (listener == null) {
            return;
        }
        new Thread() {
            @Override
            public void run() {
                // 链接地址
                String url = URLConfig.getM_SEATHEAT_URL();
                // Post参数
                String post = CreatPostString
                        .getSeatHeatClose(DorideApplication.MODEL_NAME);
                DefaultParser mParser = new DefaultParser();
                BaseResponseInfo mBaseResponseInfo = mParser
                        .getBaseResponseInfo(url, post);
                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
                    listener.onFinished(null);
                } else {
                    listener.onErro(mBaseResponseInfo);
                }
            }

        }.start();

    }

    /**
     * 远程-开启空气净化
     */
    public static void GetAirCylinderOpen(final GetResultListCallback listener) {
        if (listener == null) {
            return;
        }
        new Thread() {
            @Override
            public void run() {
                // 链接地址
                String url = URLConfig.getM_AIRCYLINDER_URL();
                // Post参数
                String post = CreatPostString
                        .getAirCylinderOpen(DorideApplication.MODEL_NAME);
                DefaultParser mParser = new DefaultParser();
                BaseResponseInfo mBaseResponseInfo = mParser
                        .getBaseResponseInfo(url, post);
                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
                    listener.onFinished(null);
                } else {
                    listener.onErro(mBaseResponseInfo);
                }
            }

        }.start();

    }

    /**
     * 远程-关闭空气净化
     */
    public static void GetAirCylinderClose(final GetResultListCallback listener) {
        if (listener == null) {
            return;
        }
        new Thread() {
            @Override
            public void run() {
                // 链接地址
                String url = URLConfig.getM_AIRCYLINDER_URL();
                // Post参数
                String post = CreatPostString
                        .getAirCylinderClose(DorideApplication.MODEL_NAME);
                DefaultParser mParser = new DefaultParser();
                BaseResponseInfo mBaseResponseInfo = mParser
                        .getBaseResponseInfo(url, post);
                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
                    listener.onFinished(null);
                } else {
                    listener.onErro(mBaseResponseInfo);
                }
            }

        }.start();

    }

    /**
     * 远程关窗 成功返回onFinished(null)
     */
    public static void GetRemoteClosewin(final GetResultListCallback listener) {
        if (listener == null) {
            return;
        }
        new Thread() {
            @Override
            public void run() {
                // 链接地址
                String url = URLConfig.getM_WINDOW_URL();
                // Post参数
                String post = CreatPostString
                        .getRemoteClosewin(DorideApplication.MODEL_NAME);
                DefaultParser mParser = new DefaultParser();
                BaseResponseInfo mBaseResponseInfo = mParser
                        .getBaseResponseInfo(url, post);
                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
                    listener.onFinished(null);
                } else {
                    listener.onErro(mBaseResponseInfo);
                }
            }

        }.start();

    }

    /**
     * 远程-落锁、解锁 成功返回onFinished(null)
     * @param lock
     *         1开锁，2上锁
     */
    public static void GetRemoteLock(final String lock,
                                     final GetResultListCallback listener) {
        if (listener == null) {
            return;
        }
        new Thread() {
            @Override
            public void run() {
                // 链接地址
                String url = URLConfig.getM_LOCKING_URL();
                // Post参数
                String post = CreatPostString.getRemoteLock(lock, DorideApplication.MODEL_NAME);
                DefaultParser mParser = new DefaultParser();
                BaseResponseInfo mBaseResponseInfo = mParser.getBaseResponseInfo(url, post);
                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
                    listener.onFinished(null);
                } else {
                    listener.onErro(mBaseResponseInfo);
                }
            }

        }.start();

    }

    /**
     * 获取远程充电返回onFinished <ChargeResultInfo>
     * @param listener
     * @param command
     *         0 立即开始充电，1 停止充电，2 定时充电
     * @param chargeTime
     *         action 为2 时可用 充电时间 2017-08-08 11:28
     */
    public static void GetRemoteCharge(final String command,
                                       final String chargeTime, final GetResultListCallback listener) {
        if (listener == null) {
            return;
        }
        new Thread() {
            @Override
            public void run() {
                // 链接地址
                String url = URLConfig.getM_CHARGE_URL();
                // Post参数
                String post = CreatPostString.getRemoteCharge(command,
                        chargeTime, DorideApplication.MODEL_NAME);
                ChargeResultParser mParser = new ChargeResultParser();
                BaseResponseInfo mBaseResponseInfo = mParser
                        .getBaseResponseInfo(url, post);
                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
                    listener.onFinished(mParser.getReturn());
                } else {
                    listener.onErro(mBaseResponseInfo);
                }
            }

        }.start();

    }

    /**
     * 取消远程定时充电返回 onFinished<null>
     * @param listener
     */
    public static void GetRemoteCancelTimeCharge(
            final GetResultListCallback listener) {
        if (listener == null) {
            return;
        }
        new Thread() {
            @Override
            public void run() {
                // 链接地址
                String url = URLConfig.getM_CANCLETIMECHARGE_URL();
                // Post参数
                String post = CreatPostString
                        .getRemoteCancelTimeCharge(DorideApplication.MODEL_NAME);
                DefaultParser mParser = new DefaultParser();
                BaseResponseInfo mBaseResponseInfo = mParser
                        .getBaseResponseInfo(url, post);
                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
                    listener.onFinished(mBaseResponseInfo);
                } else {
                    listener.onErro(mBaseResponseInfo);
                }
            }

        }.start();

    }

    /**
     * 获取远程充电状态返回onFinished<ChargeStatusInfo>
     * @param listener
     */
    public static void GetRemoteChargeStatus(
            final GetResultListCallback listener) {
        if (listener == null) {
            return;
        }
        new Thread() {
            @Override
            public void run() {
                // 链接地址
                String url = URLConfig.getM_CHARGESTATUS_URL();
                // Post参数
                String post = CreatPostString.getRemoteChargeStatus();
                ChargeStatusInfoParser mParser = new ChargeStatusInfoParser();
                BaseResponseInfo mBaseResponseInfo = mParser
                        .getBaseResponseInfo(url, post);
                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
                    listener.onFinished(mParser.getReturn());
                } else {
                    listener.onErro(mBaseResponseInfo);
                }
            }

        }.start();

    }

    /**
     * 远程-车辆状态 成功返回<CarStateInfo>
     *         设备类型
     */
    public static void GetRemoteCarState(final GetResultListCallback listener) {
        if (listener == null) {
            return;
        }
        new Thread() {
            @Override
            public void run() {
                // 链接地址
                String url = URLConfig.getM_STATUS_URL();
                // Post参数
                String post = CreatPostString.getRemoteCarStates();
                CarStateInfoParser mParser = new CarStateInfoParser();
                BaseResponseInfo mBaseResponseInfo = mParser
                        .getBaseResponseInfo(url, post);
                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
                    listener.onFinished(mParser.getReturn());
                } else {
                    listener.onErro(mBaseResponseInfo);
                }
            }

        }.start();

    }

    /**
     * 远程-车辆实时温度 成功返回 AirMainInfo
     */
    public static void GetRemoteCarTemp(final GetResultListCallback listener,
                                        final AirMainInfo airMainInfo) {
        if (listener == null) {
            return;
        }
        new Thread() {
            @Override
            public void run() {
                // 链接地址
                String url = URLConfig.getM_STATUS_URL();
                // Post参数
                String post = CreatPostString.getRemoteCarStates();
                DefaultParser mParser = new DefaultParser();
                String temp = "";
                String airState = "";

                BaseResponseInfo mBaseResponseInfo = mParser
                        .getBaseResponseInfo(url, post);
                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
                    temp = mParser.getValue("ACTemp");
                    boolean isGetCurrentTempSuccess;
                    if (TextUtils.isEmpty(temp)) {
                        temp = "--";
                        isGetCurrentTempSuccess = false;
                    } else {
                        if (temp.equals("0")) {
                            temp = "--";
                            isGetCurrentTempSuccess = false;
                        } else if (temp.equals("255")) {
                            temp = "--";
                            isGetCurrentTempSuccess = false;
                        } else {
                            isGetCurrentTempSuccess = true;
                        }
                    }
                    //测试数据
                    //temp = "35";
                    //测试数据结束
                    airMainInfo.setCurrentTemp(temp);
                    Log.e("info", "temp==------------" + temp);
                    airMainInfo.setGetCurrentTempSuccess(isGetCurrentTempSuccess);

                    airState = mParser.getValue("AC");
                    ArrayList<RemoteFunInfo> remoteFunInfos = airMainInfo.getmRemoteFunInfos();
                    for (int i = 0; i < remoteFunInfos.size(); i++) {
                        RemoteFunInfo item = remoteFunInfos.get(i);
                        String id = item.getId();

                        if (id.equals(airState)) {
                            item.setSelect(true);
                            //                            break;
                        } else {
                            item.setSelect(false);
                        }
                    }
                    airMainInfo.setState(airState);
                    listener.onFinished(airMainInfo);
                } else {
                    airMainInfo.setCurrentTemp("26");
                    airMainInfo.setGetCurrentTempSuccess(false);
                    airMainInfo.setState("-1");
                    listener.onErro(airMainInfo);
                }
            }
        }.start();
    }

    /**
     * 远程-操作记录 成功返回<RemoteLogListInfo>
     */
    public static void GetRemoteLog(final int limit, final int offset,
                                    final GetResultListCallback listener) {
        if (listener == null) {
            return;
        }
        new Thread() {
            @Override
            public void run() {
                // 链接地址
                String url = URLConfig.getM_REMOTEOPERATION_URL();
                // Post参数
                String post = CreatPostString.getRemoteLog(limit + "", offset
                        + "");
                RemoteLogListParser mParser = new RemoteLogListParser();
                BaseResponseInfo mBaseResponseInfo = mParser
                        .getBaseResponseInfo(url, post);
                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
                    RemoteLogListInfo mRemoteLogListInfo = mParser.getReturn();
                    mRemoteLogListInfo.setOffset(offset + limit);
                    listener.onFinished(mParser.getReturn());
                } else {
                    listener.onErro(mBaseResponseInfo);
                }
            }

        }.start();

    }

    /**
     * 获取车辆里程数--成功返回<String>
     */
    public static void getCarMangerInfo(final GetResultListCallback listener) {
        if (listener == null) {
            return;
        }
        new Thread() {
            @Override
            public void run() {
                // 链接地址
                String url = URLConfig.getM_GETCARSETTING_URL();
                // Post参数
                String post = CreatPostString.getCarMangerInfo();
                DefaultParser mParser = new DefaultParser();
                BaseResponseInfo mBaseResponseInfo = mParser
                        .getBaseResponseInfo(url, post);
                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
                    String summiles = mParser.getValue("summiles");
                    GetCarInfo.getInstance().maintenMiles = Integer.parseInt(mParser.getValue("mainten_miles"));
                    try {
                        GetCarInfo.getInstance().maintenDate = MyTimeUtils.FORMAT.parse(mParser.getValue("mainten_date")).getTime()/1000;
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    GetCarInfo.getInstance().brandid = Integer.parseInt(mParser.getValue("brandid"));
                    GetCarInfo.getInstance().optionid = Integer.parseInt(mParser.getValue("optionid"));
                    GetCarInfo.getInstance().styleId = Integer.parseInt(mParser.getValue("carid"));

                    listener.onFinished(summiles);
                } else {
                    listener.onErro(mBaseResponseInfo);
                }
            }

        }.start();

    }

    /**
     * 发现首页--成功返回<FindingIndexInfo>
     */
    public static void getFindingIndex(final GetResultListCallback listener) {

        if (listener == null) {
            return;
        }
        new Thread() {
            @Override
            public void run() {
                // 链接地址
                String url = URLConfig.getM_FINDING_URL();
                // Post参数
                String post = CreatPostString.getFindingIndex();

                FindingIndexParser mParser = new FindingIndexParser();
                BaseResponseInfo mBaseResponseInfo = mParser
                        .getBaseResponseInfo(url, post);
                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
                    listener.onFinished(mParser.getReturn());
                } else {
                    listener.onErro(mBaseResponseInfo);
                }
            }

        }.start();
    }

    /**
     * 获取我的记录动态列表 调用成功会返回<FriendFeedInfo>
     */

    public static void GetFriendsMyFeedListResult(final int limit,
                                                  final int offset, final GetResultListCallback listener) {

        if (listener == null)
            return;
        new Thread() {
            @Override
            public void run() {
                // 链接地址
                String url = URLConfig.getM_FRIENDS_MYFEED_LIST_URL();
                // Post参数
                String post = CreatPostString
                        .getFriendsShareList(limit, offset);

                FriendsShareListParser mParser = new FriendsShareListParser();
                BaseResponseInfo mBaseResponseInfo = mParser
                        .getBaseResponseInfo(url, post);
                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
                    FriendFeedInfo mFriendFeedInfo = mParser.getReturn();
                    mFriendFeedInfo.setOffset(offset + limit);
                    listener.onFinished(mFriendFeedInfo);
                } else {
                    listener.onErro(mBaseResponseInfo);
                }

            }

        }.start();
    }

    /**
     * 获取动态详情 调用成功会返回<FriendFeedDetialInfo>
     */

    public static void GetFeedDetialResult(final String id,
                                           final GetResultListCallback listener) {

        if (listener == null)
            return;
        new Thread() {
            @Override
            public void run() {
                // 链接地址
                String url = URLConfig.getM_FEED_DETIAL_URL();
                // Post参数
                String post = CreatPostString.getFriendsShareList(id);

                FeedDetialInfoParser mParser = new FeedDetialInfoParser();
                BaseResponseInfo mBaseResponseInfo = mParser
                        .getBaseResponseInfo(url, post);
                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
                    listener.onFinished(mParser.getReturn());
                } else {
                    listener.onErro(mBaseResponseInfo);
                }

            }

        }.start();
    }

    /**
     * 删除动态 调用成功会返回<null>
     */

    public static void GetDeleteFeedResult(final String id,
                                           final GetResultListCallback listener) {

        if (listener == null)
            return;
        new Thread() {
            @Override
            public void run() {
                // 链接地址
                String url = URLConfig.getM_DELETE_FEED_URL();
                // Post参数
                String post = CreatPostString.getDeleteFeed(id);

                DefaultParser mParser = new DefaultParser();
                BaseResponseInfo mBaseResponseInfo = mParser
                        .getBaseResponseInfo(url, post);
                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
                    listener.onFinished(null);
                } else {
                    listener.onErro(mBaseResponseInfo);
                }

            }

        }.start();
    }

    /**
     * 我的申报列表 调用成功会返回<MySOSListInfo>
     */

    public static void GetMySOSListResult(final int limit, final int offset,
                                          final GetResultListCallback listener) {

        if (listener == null)
            return;
        new Thread() {
            @Override
            public void run() {
                // 链接地址
                String url = URLConfig.getM_SOS_LIST_URL();
                // Post参数
                String post = CreatPostString.getMySOS(limit, offset);

                MySOSListParser mParser = new MySOSListParser();
                BaseResponseInfo mBaseResponseInfo = mParser
                        .getBaseResponseInfo(url, post);
                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
                    MySOSListInfo mMySOSListInfo = mParser.getReturn();
                    mMySOSListInfo.setOffset(offset + limit);
                    listener.onFinished(mMySOSListInfo);
                } else {
                    listener.onErro(mBaseResponseInfo);
                }

            }

        }.start();
    }

    /**
     * 申报详情 调用成功会返回<SOSDetialInfo>
     */

    public static void GetSOSDetialResult(final String id,
                                          final GetResultListCallback listener) {

        if (listener == null)
            return;
        new Thread() {
            @Override
            public void run() {
                // 链接地址
                String url = URLConfig.getM_SOS_DETIAL_URL();
                // Post参数
                String post = CreatPostString.getSOSDetial(id);

                MySOSDetialListParser mParser = new MySOSDetialListParser();
                BaseResponseInfo mBaseResponseInfo = mParser
                        .getBaseResponseInfo(url, post);
                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
                    listener.onFinished(mParser.getReturn());
                } else {
                    listener.onErro(mBaseResponseInfo);
                }

            }

        }.start();
    }

    /**
     * 我的预约列表 调用成功会返回<MyAppointmentListInfo>
     */

    public static void GetAppointmentInfoListResult(final int limit,
                                                    final int offset, final GetResultListCallback listener) {

        if (listener == null)
            return;
        new Thread() {
            @Override
            public void run() {
                // 链接地址
                String url = URLConfig.getM_ORDER_LIST_URL();
                // Post参数
                String post = CreatPostString.getMyOrder(limit, offset);
                MyOrderListParser mParser = new MyOrderListParser();
                BaseResponseInfo mBaseResponseInfo = mParser
                        .getBaseResponseInfo(url, post);
                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
                    MyAppointmentListInfo mMyAppointmentListInfo = mParser
                            .getReturn();
                    mMyAppointmentListInfo.setOffset(offset + limit);
                    listener.onFinished(mMyAppointmentListInfo);
                } else {
                    listener.onErro(mBaseResponseInfo);
                }

            }

        }.start();
    }

    /**
     * 预约详情 调用成功会返回<AppointmentDetialInfo>
     */

    public static void GetAppointDetialResult(final String id,
                                              final GetResultListCallback listener) {

        if (listener == null)
            return;
        new Thread() {
            @Override
            public void run() {
                // 链接地址
                String url = URLConfig.getM_ORDER_DETIAL_URL();
                // Post参数
                String post = CreatPostString.getAppiontDetial(id);

                MyOrderDetialParser mParser = new MyOrderDetialParser();
                BaseResponseInfo mBaseResponseInfo = mParser
                        .getBaseResponseInfo(url, post);
                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
                    listener.onFinished(mParser.getReturn());
                } else {
                    listener.onErro(mBaseResponseInfo);
                }

            }

        }.start();
    }

    /**
     * 提交评价 调用成功会返回<null>
     */

    public static void GeSubmitEvaluationResult(final String id,
                                                final int star, final String content, final String spent,
                                                final GetResultListCallback listener) {

        if (listener == null)
            return;
        new Thread() {
            @Override
            public void run() {
                // 链接地址
                String url = URLConfig.getM_SUBMIT_EVALUATION_URL();
                // Post参数
                String post = CreatPostString.getSubmitEvaluation(id, star,
                        content, spent);

                DefaultParser mParser = new DefaultParser();
                BaseResponseInfo mBaseResponseInfo = mParser
                        .getBaseResponseInfo(url, post);
                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
                    listener.onFinished(null);
                } else {
                    listener.onErro(mBaseResponseInfo);
                }

            }

        }.start();
    }

    /**
     * 上传故障申报图片接口
     */

    public static void GetUpLoadSOSImgResult(final ArrayList<String> urls,
                                             final GetResultList3Callback listener) {

        if (listener == null)
            return;
        new Thread() {
            @Override
            public void run() {
                for (int i = 0; i < urls.size(); i++) {
                    UploadImgInfo mUploadImgInfo = GetUploadImgResult(
                            UPLOADIMG_SOS, urls.get(i));
                    if (mUploadImgInfo == null) {
                        listener.onErro();
                    } else {
                        mUploadImgInfo.setLocalfilePath(urls.get(i));
                        listener.onSuccess(mUploadImgInfo);
                    }
                }

                listener.onFinished();

            }

        }.start();
    }

    /**
     * 提交救援请求 调用成功会返回<null>
     */

    public static void GeSubmitSOSResult(final SubmitSOSInfo mSubmitSOSInfo,
                                         final GetResultListCallback listener) {

        if (listener == null)
            return;
        new Thread() {
            @Override
            public void run() {
                // 链接地址
                String url = URLConfig.getM_SUBMIT_SOS_URL_URL();
                // Post参数
                String post = CreatPostString.getSubmitSOS(mSubmitSOSInfo);

                DefaultParser mParser = new DefaultParser();
                BaseResponseInfo mBaseResponseInfo = mParser
                        .getBaseResponseInfo(url, post);
                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
                    listener.onFinished(null);
                } else {
                    listener.onErro(mBaseResponseInfo);
                }

            }

        }.start();
    }

    /**
     * 获取用户基本信息
     * @param listener
     */
    public static void GetUserOtherInfoResult(
            final GetResultListCallback listener) {

        new Thread() {
            @Override
            public void run() {
                // 链接地址
                String url = URLConfig.getM_USEROTHERINFO_URL();
                // Post参数
                String post = CreatPostString.getUserInfo();
                UserOtherInfoParser mUserInfoParser = new UserOtherInfoParser();
                BaseResponseInfo mBaseResponseInfo = mUserInfoParser
                        .getBaseResponseInfo(url, post);

                if (null != listener) {
                    if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
                        listener.onFinished(mBaseResponseInfo);
                    } else {
                        listener.onErro(mBaseResponseInfo);
                    }
                }

            }

        }.start();
    }


    /**
     * 获取养护建议 调用成功执行onFinished 返回<ArrayList<MaintainLogInfo>>
     */
    public static void GetRecommendMaintainResult(
            final GetResultListCallback listener) {

        if (listener == null)
            return;
        new Thread() {
            @Override
            public void run() {
                // 链接地址
                String url = URLConfig.getM_RECOMMEND_MAINTAIN_URL();
                // Post参数
                String post = CreatPostString.getRecommendMaintain();

                RecommendMaintainParser mParser = new RecommendMaintainParser();
                BaseResponseInfo mBaseResponseInfo = mParser
                        .getBaseResponseInfo(url, post);
                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
                    listener.onFinished(mParser.getReturn());
                } else {
                    listener.onErro(mBaseResponseInfo);
                }

            }

        }.start();
    }

    /**
     * 安全-获取首页数据<SafetyMainInfo>
     * @param move_deviceid
     *         发起请求的设备ID
     */
    public static void GetSafetyMainInfoResult(
            final GetResultListCallback listener, final String move_deviceid) {

        if (listener == null)
            return;
        new Thread() {
            @Override
            public void run() {
                // 链接地址
                String url = URLConfig.getM_SAFE_SAFEINDEX_URL();
                // Post参数
                String post = CreatPostString.getSafetyIndex(move_deviceid);

                SafetyMainInfoParser mParser = new SafetyMainInfoParser();
                BaseResponseInfo mBaseResponseInfo = mParser
                        .getBaseResponseInfo(url, post);
                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
                    listener.onFinished(mParser.getReturn());
                } else {
                    listener.onErro(mBaseResponseInfo);
                }

            }

        }.start();
    }

    /**
     * 安全-获取登录日志<LoginLogListInfo>
     * @param limit
     *         每页条数
     * @param offset
     *         偏移量
     */

    public static void GetLogLoginListResult(final int limit, final int offset,
                                             final GetResultListCallback listener) {

        if (listener == null)
            return;
        new Thread() {
            @Override
            public void run() {

                // 链接地址
                String url = URLConfig.getM_SAFE_GETLOGINLOG_URL();
                // Post参数
                String post = CreatPostString.getLoginLog(limit + "", offset
                        + "");

                LoginLogListParser mParser = new LoginLogListParser();
                BaseResponseInfo mBaseResponseInfo = mParser
                        .getBaseResponseInfo(url, post);
                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
                    LoginLogListInfo mLoginLogListInfo = mParser.getReturn();
                    mLoginLogListInfo.setOffset(limit + offset);
                    listener.onFinished(mParser.getReturn());
                } else {
                    listener.onErro(mBaseResponseInfo);
                }

            }

        }.start();
    }

    /**
     * 安全-获取授权设备列表<MobileListInfo>
     * @param limit
     *         每页条数
     * @param offset
     *         偏移量
     * @param move_deviceid
     *         设备唯一标识id
     */

    public static void GetMoblieListResult(final int limit, final int offset,
                                           final String move_deviceid, final GetResultListCallback listener) {

        if (listener == null)
            return;
        new Thread() {
            @Override
            public void run() {

                // 链接地址
                String url = URLConfig.getM_SAFE_GETAUTHORIZEDEVICE_URL();
                // Post参数
                String post = CreatPostString.getAuthorizeDevice(limit + "",
                        offset + "", move_deviceid);

                MobileListParser mParser = new MobileListParser();
                BaseResponseInfo mBaseResponseInfo = mParser
                        .getBaseResponseInfo(url, post);
                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
                    MobileListInfo mMobileListInfo = mParser.getReturn();
                    mMobileListInfo.setOffset(limit + offset);
                    listener.onFinished(mParser.getReturn());
                } else {
                    listener.onErro(mBaseResponseInfo);
                }

            }

        }.start();
    }

    /**
     * 安全-删除授权设备<null>
     * @param id
     *         设备id
     */

    public static void GetDelMobileResult(final String id,
                                          final GetResultListCallback listener) {

        if (listener == null)
            return;
        new Thread() {
            @Override
            public void run() {
                // 链接地址
                String url = URLConfig.getM_SAFE_DELAUTHORIZEDEVICE_URL();
                // Post参数
                String post = CreatPostString.getDelAuthorizeDevice(id);

                DefaultParser mParser = new DefaultParser();
                BaseResponseInfo mBaseResponseInfo = mParser
                        .getBaseResponseInfo(url, post);
                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
                    listener.onFinished(mBaseResponseInfo);
                } else {
                    listener.onErro(mBaseResponseInfo);
                }

            }

        }.start();
    }

    /**
     * 安全-冻结、解冻账户<null>
     * @param is_freezing
     *         0解冻，1冻结
     * @param password
     *         登录密码(可选，解冻时需要)
     */

    public static void GetFreezingResult(final String is_freezing,
                                         final String password, final GetResultListCallback listener) {

        if (listener == null)
            return;
        new Thread() {
            @Override
            public void run() {
                // 链接地址
                String url = URLConfig.getM_SAFE_FREEZING_URL();
                // Post参数
                String post = CreatPostString
                        .getFreezing(is_freezing, password);

                DefaultParser mParser = new DefaultParser();
                BaseResponseInfo mBaseResponseInfo = mParser
                        .getBaseResponseInfo(url, post);
                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
                    listener.onFinished(mBaseResponseInfo);
                    Log.e("info", "is_freezing==" + is_freezing);
                    if (is_freezing.equals("2")) {
                        UserInfo.getInstance().userFreeze = 2;
                    } else {
                        UserInfo.getInstance().userFreeze = 1;
                    }
                } else {
                    listener.onErro(mBaseResponseInfo);
                }

            }

        }.start();
    }

    /**
     * 安全-发送授权请求<null>
     * @param move_deviceid
     *         设备唯一标识id
     * @param move_device_name
     *         设备的用户名（蓝牙共享出去的名字）
     * @param move_model
     *         设备型号 如iPhone6S
     */

    public static void GetSendAuthorizeResult(final String mobile,
                                              final String move_deviceid, final String move_device_name,
                                              final String move_model, final GetResultListCallback listener) {

        if (listener == null)
            return;
        new Thread() {
            @Override
            public void run() {
                // 链接地址
                String url = URLConfig.getM_SAFE_SENDAUTHORIZE_URL();
                // Post参数
                String post = CreatPostString.getSendAuthorize(mobile,
                        move_deviceid, move_device_name, move_model);

                DefaultParser mParser = new DefaultParser();
                BaseResponseInfo mBaseResponseInfo = mParser
                        .getBaseResponseInfo(url, post);
                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
                    listener.onFinished(mBaseResponseInfo);
                } else {
                    listener.onErro(mBaseResponseInfo);
                }

            }

        }.start();
    }

    /**
     * 安全-获取授权页面数据<AutherInfo>
     */

    public static void GetAuthorizePageResult(
            final GetResultListCallback listener) {

        if (listener == null)
            return;
        new Thread() {
            @Override
            public void run() {
                // 链接地址
                String url = URLConfig.getM_SAFE_AUTHORIZEPAGE_URL();
                // Post参数
                String post = CreatPostString.getAuthorizePageInfo();

                AuthorInfoParser mParser = new AuthorInfoParser();
                BaseResponseInfo mBaseResponseInfo = mParser
                        .getBaseResponseInfo(url, post);
                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
                    listener.onFinished(mParser.getReturn());
                } else {
                    listener.onErro(mBaseResponseInfo);
                }

            }

        }.start();
    }

    /**
     * 安全-授权处理<null>
     * @param isallow
     *         是否允许使用 1允许 2拒绝
     * @param move_deviceid
     *         发起请求授权的设备id
     */

    public static void GetDealAuthorize(final String isallow,
                                        final String move_deviceid, final GetResultListCallback listener) {

        if (listener == null)
            return;
        new Thread() {
            @Override
            public void run() {
                // 链接地址
                String url = URLConfig.getM_SAFE_DEALAUTHORIZE_URL();
                // Post参数
                String post = CreatPostString.getDealAuthorize(isallow,
                        move_deviceid);

                DefaultParser mParser = new DefaultParser();
                BaseResponseInfo mBaseResponseInfo = mParser
                        .getBaseResponseInfo(url, post);
                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
                    listener.onFinished(null);
                } else {
                    listener.onErro(mBaseResponseInfo);
                }

            }

        }.start();
    }

    /**
     * 安全-子设备获取授权状态<AuthorResultInfo>
     * @param move_deviceid
     *         设备id
     */

    public static void GetAuthorizeStatusResult(final String mobile,
                                                final String move_deviceid, final GetResultListCallback listener) {

        if (listener == null)
            return;
        new Thread() {
            @Override
            public void run() {
                // 链接地址
                String url = URLConfig.getM_SAFE_GETAUTHORIZESTATUS_URL();
                // Post参数
                String post = CreatPostString.getAuthorizeStatus(mobile,
                        move_deviceid);

                AutherStateParser mParser = new AutherStateParser();
                BaseResponseInfo mBaseResponseInfo = mParser
                        .getBaseResponseInfo(url, post);
                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
                    listener.onFinished(mParser.getReturn());
                } else {
                    listener.onErro(mBaseResponseInfo);
                }
            }

        }.start();
    }

    /**
     * 安全-获取更新主设备授权状态<null>
     * @param authorize_switch
     *         授权开关 0关闭，1开启
     */

    public static void GetUpdateAuthorizeStatusResult(
            final String authorize_switch, final GetResultListCallback listener) {

        if (listener == null)
            return;
        new Thread() {
            @Override
            public void run() {
                // 链接地址
                String url = URLConfig.getM_SAFE_UPDATESWITCH_URL();
                // Post参数
                String post = CreatPostString
                        .getUpdateAuthorizeStatus(authorize_switch);

                DefaultParser mParser = new DefaultParser();
                BaseResponseInfo mBaseResponseInfo = mParser
                        .getBaseResponseInfo(url, post);
                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
                    listener.onFinished(mBaseResponseInfo);
                } else {
                    listener.onErro(mBaseResponseInfo);
                }

            }

        }.start();
    }

    /**
     * 安全-实名认证<null>
     * @param authen_name
     *         真实姓名
     * @param authen_card
     *         身份证号
     */

    public static void GetRealNameResult(final String authen_name,
                                         final String authen_card, final GetResultListCallback listener) {

        if (listener == null)
            return;
        new Thread() {
            @Override
            public void run() {
                // 链接地址
                String url = URLConfig.getM_SAFE_REALAUTHEN_URL();
                // Post参数
                String post = CreatPostString.getRealauthen(authen_name,
                        authen_card);

                DefaultParser mParser = new DefaultParser();
                BaseResponseInfo mBaseResponseInfo = mParser
                        .getBaseResponseInfo(url, post);
                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
                    listener.onFinished(mBaseResponseInfo);
                    UserInfo.getInstance().isAuthen = "1";
                    UserInfo.getInstance().authenName = authen_name;

                    String cardPre = authen_card.substring(0, 6);
                    String cardSuf = authen_card.substring(
                            authen_card.length() - 2, authen_card.length());
                    StringBuffer authen_card_handled = new StringBuffer(cardPre);
                    authen_card_handled.append("**********");
                    authen_card_handled.append(cardSuf);
                    UserInfo.getInstance().authenCard = authen_card_handled.toString();
                } else {
                    listener.onErro(mBaseResponseInfo);
                }
            }
        }.start();
    }

    /**
     * 安全-身份验证<null>
     * @param authen_name
     *         真实姓名
     * @param authen_card
     *         身份证号
     */

    public static void GetAuthenticationResult(final String authen_name,
                                               final String authen_card, final GetResultListCallback listener) {

        if (listener == null)
            return;
        new Thread() {
            @Override
            public void run() {
                // 链接地址
                String url = URLConfig.getM_SAFE_AUTHENTICATION_URL();
                // Post参数
                String post = CreatPostString.getRealauthen(authen_name,
                        authen_card);

                DefaultParser mParser = new DefaultParser();
                BaseResponseInfo mBaseResponseInfo = mParser
                        .getBaseResponseInfo(url, post);
                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
                    listener.onFinished(mBaseResponseInfo);
                } else {
                    listener.onErro(mBaseResponseInfo);
                }
            }
        }.start();
    }

    /**
     * 安全-设置远程密码<null>
     * @param remote_pwd
     *         远程密码(未经md5加密的)
     */

    public static void GetSetRemotePwdResult(final String remote_pwd,
                                             final GetResultListCallback listener) {

        if (listener == null)
            return;
        new Thread() {
            @Override
            public void run() {
                // 链接地址
                String url = URLConfig.getM_SAFE_SETREMOTEPWD_URL();
                // Post参数
                String remote_pwd_md5 = FileUtil.stringToMD5(remote_pwd);
                String post = CreatPostString.getSetRomotePsw(remote_pwd_md5);

                DefaultParser mParser = new DefaultParser();
                BaseResponseInfo mBaseResponseInfo = mParser
                        .getBaseResponseInfo(url, post);
                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
                    RemotePswInfo.setRemotePsw(remote_pwd);
                    UserInfo.getInstance().isSetRemotePwd = 1;
                    listener.onFinished(mBaseResponseInfo);
                } else {
                    UserInfo.getInstance().isSetRemotePwd = 0;
                    listener.onErro(mBaseResponseInfo);
                }
            }
        }.start();
    }

    /**
     * 安全-修改远程密码<null>
     * @param old_remote_pwd
     *         当前远程密码(未经md5加密的)
     * @param new_remote_pwd
     *         新的远程密码(未经md5加密的)
     */

    public static void GetResetRemotePwdResult(final String old_remote_pwd,
                                               final String new_remote_pwd, final GetResultListCallback listener) {

        if (listener == null)
            return;
        new Thread() {
            @Override
            public void run() {
                // 链接地址
                String url = URLConfig.getM_SAFE_RESETREMOTEPWD_URL();
                // Post参数
                String old_remote_pwd_md5 = FileUtil
                        .stringToMD5(old_remote_pwd);
                String new_remote_pwd_md5 = FileUtil
                        .stringToMD5(new_remote_pwd);
                String post = CreatPostString.getResetRomotePsw(
                        old_remote_pwd_md5, new_remote_pwd_md5);

                DefaultParser mParser = new DefaultParser();
                BaseResponseInfo mBaseResponseInfo = mParser
                        .getBaseResponseInfo(url, post);
                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
                    RemotePswInfo.setRemotePsw(new_remote_pwd);
                    listener.onFinished(mBaseResponseInfo);
                } else {
                    listener.onErro(mBaseResponseInfo);
                }
            }
        }.start();
    }

    /**
     * 安全-重置远程密码<null>
     * @param authen_name
     *         已认证姓名
     * @param authen_card
     *         已认证身份证号
     * @param mobile
     *         用户手机号
     * @param remote_pwd
     *         新的远程密码(未经md5加密的)
     * @param validate_code
     *         短信验证码
     */

    public static void GetForgetRemotePwdResult(final String authen_name,
                                                final String authen_card, final String mobile,
                                                final String remote_pwd, final String validate_code,
                                                final GetResultListCallback listener) {

        if (listener == null)
            return;
        new Thread() {
            @Override
            public void run() {
                // 链接地址
                String url = URLConfig.getM_SAFE_FORGETREMOTEPWD_URL();
                // Post参数
                String remote_pwd_md5 = FileUtil.stringToMD5(remote_pwd);
                String post = CreatPostString.getForgetRomotePsw(authen_name,
                        authen_card, mobile, remote_pwd_md5, validate_code);

                DefaultParser mParser = new DefaultParser();
                BaseResponseInfo mBaseResponseInfo = mParser
                        .getBaseResponseInfo(url, post);
                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
                    RemotePswInfo.setRemotePsw(remote_pwd);
                    UserInfo.getInstance().isSetRemotePwd = 1;
                    listener.onFinished(mBaseResponseInfo);
                } else {
                    listener.onErro(mBaseResponseInfo);
                }
            }
        }.start();
    }

    /**
     * 安全-更新远程控制五分钟无需密码开关状态<null>
     * @param lesspwd_switch
     *         1:开启;0:关闭
     */

    public static void GetUpdateLessPwdResult(final String lesspwd_switch,
                                              final GetResultListCallback listener) {

        if (listener == null)
            return;
        new Thread() {
            @Override
            public void run() {
                // 链接地址
                String url = URLConfig.getM_SAFE_UPDATELESSPWDSWITCH_URL();
                // Post参数
                String post = CreatPostString.getUpdateLessPwd(lesspwd_switch);

                DefaultParser mParser = new DefaultParser();
                BaseResponseInfo mBaseResponseInfo = mParser
                        .getBaseResponseInfo(url, post);
                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
                    if (lesspwd_switch.equals("1")) {
                        UserInfo.getInstance().remotePwdSwitch = 1;
                    } else {
                        UserInfo.getInstance().remotePwdSwitch = 0;
                    }

                    listener.onFinished(mBaseResponseInfo);
                } else {
                    listener.onErro(mBaseResponseInfo);
                }
            }
        }.start();
    }

    /**
     * 安全-更换主机<null>
     */
    public final static String CHANGE_BY_ID    = "1";// 通过验证身份方式更换
    public final static String CHANGE_BY_PHONE = "2";// 通过验证手机方式更换

    public static void GetChangeMainDeviceResult(final String type,
                                                 final String authen_name, final String authen_card,
                                                 final String old_mobile, final String new_mobile,
                                                 final String validate, final String move_deviceid,
                                                 final String move_device_name, final GetResultListCallback listener) {

        if (listener == null)
            return;
        new Thread() {
            @Override
            public void run() {
                // 链接地址
                String url = URLConfig.getM_SAFE_CHANGEMAINDEVICE_URL();
                // Post参数
                String post = CreatPostString.getChangeMainDevice(type,
                        authen_name, authen_card, old_mobile, new_mobile,
                        validate, move_deviceid, move_device_name);

                DefaultParser mParser = new DefaultParser();
                BaseResponseInfo mBaseResponseInfo = mParser
                        .getBaseResponseInfo(url, post);
                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
                    if (new_mobile != null && !new_mobile.equals("")) {
                        UserInfo.getInstance().mobile = new_mobile;
                    }
                    OtherInfo.getInstance().setIsMain(true);
                    listener.onFinished(mBaseResponseInfo);
                } else {
                    listener.onErro(mBaseResponseInfo);
                }
            }
        }.start();
    }

    /**
     * 安全-无授权登录<null>
     * @param mobile
     *         登录手机
     * @param authen_name
     *         用户真实姓名
     * @param authen_card
     *         用户已认证身份证号
     * @param remote_pwd
     *         远程密码(md5加密传输)
     * @param move_deviceid
     *         主机设备唯一标识码
     * @param move_device_name
     *         主机设备名称
     * @param move_model
     *         设备型号 如iPhone6S
     */
    public static void GetNoAuthorizeLoginResult(final String mobile,
                                                 final String authen_name, final String authen_card,
                                                 final String remote_pwd, final String move_deviceid,
                                                 final String move_device_name, final String move_model,
                                                 final GetResultListCallback listener) {

        if (listener == null)
            return;
        new Thread() {
            @Override
            public void run() {
                // 链接地址
                String url = URLConfig.getM_SAFE_NOAUTHORIZELOGIN_URL();
                // Post参数
                String remote_pwd_md5 = FileUtil.stringToMD5(remote_pwd);
                String post = CreatPostString.getNoAuthorizeLogin(mobile,
                        authen_name, authen_card, remote_pwd_md5,
                        move_deviceid, move_device_name, move_model);

                DefaultParser mParser = new DefaultParser();
                BaseResponseInfo mBaseResponseInfo = mParser
                        .getBaseResponseInfo(url, post);
                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
                    listener.onFinished(mBaseResponseInfo);
                } else {
                    listener.onErro(mBaseResponseInfo);
                }
            }
        }.start();
    }

    /**
     * 安全-安全验证登录<null>
     * @param old_mobile
     *         旧主机号
     * @param old_password
     *         旧登录密码
     * @param authen_name
     *         用户真实姓名
     * @param authen_card
     *         用户已认证身份证号
     * @param remote_pwd
     *         远程密码(md5加密传输)
     * @param new_mobile
     *         新主机号
     * @param validate
     *         新主机号验证码
     * @param new_password
     *         新登录密码
     * @param move_deviceid
     *         主机设备唯一标识码
     * @param move_device_name
     *         主机设备名称
     * @param move_model
     *         设备型号 如iPhone6S
     */
    public static void GetNoAuthorizeChangeDeviceResult(
            final String old_mobile, final String old_password,
            final String authen_name, final String authen_card,
            final String remote_pwd, final String new_mobile,
            final String validate, final String new_password,
            final String move_deviceid, final String move_device_name,
            final String move_model, final GetResultListCallback listener) {

        if (listener == null)
            return;
        new Thread() {
            @Override
            public void run() {
                // 链接地址
                String url = URLConfig.getM_SAFE_NOAUTHORIZECHANGEDEVICE_URL();
                // Post参数
                String old_password_md5 = FileUtil.stringToMD5(old_password);
                String remote_pwd_md5 = FileUtil.stringToMD5(remote_pwd);
                String new_password_md5 = FileUtil.stringToMD5(new_password);
                String post = CreatPostString.getNoAuthorizeChangeDevice(
                        old_mobile, old_password_md5, authen_name, authen_card,
                        remote_pwd_md5, new_mobile, validate, new_password_md5,
                        move_deviceid, move_device_name, move_model);

                DefaultParser mParser = new DefaultParser();
                BaseResponseInfo mBaseResponseInfo = mParser
                        .getBaseResponseInfo(url, post);
                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
                    listener.onFinished(mBaseResponseInfo);
                } else {
                    listener.onErro(mBaseResponseInfo);
                }
            }
        }.start();
    }

    /**
     * 安全-更换主机-主机认证<null>
     * @param old_mobile
     *         旧手机号
     * @param new_mobile
     *         新手机号
     * @param validate
     *         新手机验证码
     */
    public static void GetMainDeviceAuthorizeResult(final String old_mobile,
                                                    final String new_mobile, final String validate,
                                                    final GetResultListCallback listener) {

        if (listener == null)
            return;
        new Thread() {
            @Override
            public void run() {
                // 链接地址
                String url = URLConfig.getM_SAFE_MAINDEVICEAUTHORIZE_URL();
                // Post参数
                String post = CreatPostString.getMainDeviceAuthorize(
                        old_mobile, new_mobile, validate);
                DefaultParser mParser = new DefaultParser();
                BaseResponseInfo mBaseResponseInfo = mParser
                        .getBaseResponseInfo(url, post);
                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
                    listener.onFinished(mBaseResponseInfo);
                } else {
                    listener.onErro(mBaseResponseInfo);
                }
            }
        }.start();
    }

    /**
     * 续费-获取续费主页数据<FeeMainInfo>
     */

    public static void GetFeeMainResult(final GetResultListCallback listener) {

        if (listener == null)
            return;
        new Thread() {
            @Override
            public void run() {
                // 链接地址
                String url = URLConfig.getM_RENEW_FEELIST_URL();
                // Post参数
                String post = CreatPostString.getFeeMain();

                FeeMainInfoParser mParser = new FeeMainInfoParser();
                BaseResponseInfo mBaseResponseInfo = mParser
                        .getBaseResponseInfo(url, post);
                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
                    listener.onFinished(mParser.getReturn());
                } else {
                    listener.onErro(mBaseResponseInfo);
                }

            }

        }.start();
    }

    /**
     * 续费-获取订单信息<FeeOrderInfo>
     * @param order_year
     *         订单年限
     * @param order_money
     *         订单价格
     */
    public static void GetFeeOrderResult(final GetResultListCallback listener,
                                         final String order_year, final String order_money) {

        if (listener == null)
            return;
        new Thread() {
            @Override
            public void run() {
                // 链接地址
                String url = URLConfig.getM_RENEW_ALIPAY_URL();
                // Post参数
                String post = CreatPostString.getFeeOrder(order_year,
                        order_money);

                FeeOrderInfoParser mParser = new FeeOrderInfoParser();
                BaseResponseInfo mBaseResponseInfo = mParser
                        .getBaseResponseInfo(url, post);
                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
                    listener.onFinished(mParser.getReturn());
                    FeeOrderInfo mOrderInfo = mParser.getReturn();
                    Log.e("info",
                            "mOrderInfo.result==" + mOrderInfo.getOrderCout());
                } else {
                    listener.onErro(mBaseResponseInfo);
                }

            }

        }.start();
    }

    /**
     * 续费-验证支付回调<FeeOrderInfo>
     */
    public static void GetFeeCheckResult(final GetResultListCallback listener,
                                         final String resultStatus, final String result) {

        if (listener == null)
            return;
        new Thread() {
            @Override
            public void run() {
                // 链接地址
                String url = URLConfig.getM_RENEW_SYNCAPI_URL();
                // Post参数
                String post = CreatPostString.getFeeCheck(resultStatus, result);

                DefaultParser mParser = new DefaultParser();
                BaseResponseInfo mBaseResponseInfo = mParser
                        .getBaseResponseInfo(url, post);
                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
                    listener.onFinished(mBaseResponseInfo);
                } else {
                    listener.onErro(mBaseResponseInfo);
                }
            }

        }.start();
    }

    /**
     * 续费-续费记录<FeeLogListInfo>
     * @param limit
     *         每页条数
     * @param offset
     *         偏移量
     */
    public static void GetFeeLogResult(final int limit, final int offset,
                                       final GetResultListCallback listener) {

        if (listener == null)
            return;
        new Thread() {
            @Override
            public void run() {
                // 链接地址
                String url = URLConfig.getM_RENEW_PAYLOG_URL();
                // Post参数
                String post = CreatPostString.getFeeLog(limit, offset);

                FeeLogListParser mParser = new FeeLogListParser();
                BaseResponseInfo mBaseResponseInfo = mParser
                        .getBaseResponseInfo(url, post);
                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
                    listener.onFinished(mParser.getReturn());
                } else {
                    listener.onErro(mBaseResponseInfo);
                }
            }
        }.start();
    }

    public static void GetToCheckPwd(final String pwd,
                                     final GetResultListCallback listener) {
        if (listener == null) {
            return;
        }
        new Thread() {
            @Override
            public void run() {
                // 链接地址
                String url = URLConfig.getM_USERCENTER_CHECK_PWD();
                // Post参数
                String md5 = CipherUtils.md5(pwd);
                Log.i("DEBUG", md5);
                String post = CreatPostString.getChekcPwd(md5);
                DefaultParser mParser = new DefaultParser();
                BaseResponseInfo mBaseResponseInfo = mParser
                        .getBaseResponseInfo(url, post);
                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
                    listener.onFinished(mBaseResponseInfo);
                } else {
                    listener.onErro(mBaseResponseInfo);
                }
            }
        }.start();
    }

    public static void GetToPay(final Handler payHandler, final Activity act,
                                final String orderInfo, final boolean h5) {
        if (payHandler == null) {
            return;
        }
        new Thread() {
            @Override
            public void run() {
                //                PayTask alipay = new PayTask(act);
                //                String result = alipay.pay(orderInfo, true);
                //                Message msg = new Message();
                //                msg.what = 0;
                //                msg.obj = result;
                //                payHandler.sendMessage(msg);
            }
        }.start();
    }


    /**
     * 旧车主获取二维码
     */
    public static void GetTransferQrCodeResult(final String realName,
                                               final String authen_card, final String phone, final String code,
                                               final GetResultListCallback listener) {

        if (listener == null)
            return;
        new Thread() {
            @Override
            public void run() {
                // 链接地址
                String url = URLConfig.getM_TRANSFER_GET_QRCODE_URL();
                // Post参数
                String post = CreatPostString.getQrCode(realName, authen_card,
                        code, phone);
                TransferQrCodeParser mParser = new TransferQrCodeParser();
                BaseResponseInfo mBaseResponseInfo = mParser
                        .getBaseResponseInfo(url, post);
                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
                    listener.onFinished(mParser.getReturn());
                } else {
                    listener.onErro(mBaseResponseInfo);
                }
            }
        }.start();
    }

    /**
     * 新车主过户
     * @param outtingid
     * @param listener
     */
    public static void GetTransferNewOptResult(final String outtingid,
                                               final GetResultListCallback listener) {

        if (listener == null)
            return;
        new Thread() {
            @Override
            public void run() {
                // 链接地址
                String url = URLConfig.getM_TRANSFER_NEW_OPERATION();
                // Post参数
                String post = CreatPostString.getTransferNewOpt(outtingid);
                DefaultParser mParser = new DefaultParser();
                BaseResponseInfo mBaseResponseInfo = mParser
                        .getBaseResponseInfo(url, post);
                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
                    listener.onFinished(mBaseResponseInfo);
                } else {
                    listener.onErro(mBaseResponseInfo);
                }
            }
        }.start();
    }

    /**
     * 旧车主操作过户
     * @param outtingid
     * @param isagree
     * @param listener
     */
    public static void GetTransferOldOptResult(final String outtingid,
                                               final String isagree, final GetResultListCallback listener) {

        if (listener == null)
            return;
        new Thread() {
            @Override
            public void run() {
                // 链接地址
                String url = URLConfig.getM_TRANSFER_OLD_OPERATION();
                // Post参数
                String post = CreatPostString.getTransferOldOpt(outtingid,
                        isagree);
                DefaultParser mParser = new DefaultParser();
                BaseResponseInfo mBaseResponseInfo = mParser
                        .getBaseResponseInfo(url, post);
                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
                    listener.onFinished(mBaseResponseInfo);
                } else {
                    listener.onErro(mBaseResponseInfo);
                }
            }
        }.start();
    }

    /**
     * 老车主检测是否有过户请求
     * @param listener
     */
    public static void GetTransferOldCheckResult(
            final GetResultListCallback listener) {
        if (listener == null)
            return;
        new Thread() {
            @Override
            public void run() {
                // 链接地址
                String url = URLConfig.getM_TRANSFER_OLD_CHECKING();
                // Post参数
                String post = CreatPostString.getTransferOldCheck();
                DefaultParser mParser = new DefaultParser();
                BaseResponseInfo mBaseResponseInfo = mParser
                        .getBaseResponseInfo(url, post);
                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
                    TransferOldCheckInfo toi = new TransferOldCheckInfo();
                    toi.setIshave(mParser.getValue("ishave"));// 1==有，2==没有
                    toi.setOuttingid(mParser.getValue("outtingid"));
                    toi.setMobile(mParser.getValue("mobile"));
                    listener.onFinished(toi);
                } else {
                    listener.onErro(mBaseResponseInfo);
                }
            }
        }.start();
    }

    /**
     * 新车主检测过户结果
     */
    public static void GetTransferNewCheckResult(final String outtingid,
                                                 final GetResultListCallback listener) {
        if (listener == null)
            return;
        new Thread() {
            @Override
            public void run() {
                // 链接地址
                String url = URLConfig.getM_TRANSFER_NEW_CHECKING();
                // Post参数
                String post = CreatPostString.getTransferNewCheck(outtingid);
                DefaultParser mParser = new DefaultParser();
                BaseResponseInfo mBaseResponseInfo = mParser
                        .getBaseResponseInfo(url, post);
                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
                    TransferNewCheckInfo tni = new TransferNewCheckInfo();
                    tni.setStatus(mParser.getValue("status"));// 1=已发出请求等待处理，2=已同意过户，3=拒绝过户
                    listener.onFinished(tni);
                } else {
                    listener.onErro(mBaseResponseInfo);
                }
            }
        }.start();
    }

    public static void SetRemoteCommControl(final String sound_switch,
                                            final GetResultListCallback listener) {
        if (listener == null)
            return;
        new Thread() {
            @Override
            public void run() {
                // 链接地址
                String url = URLConfig.getM_SAFE_REMOTE_CTL_SOUND_URL();
                // Post参数
                String post = CreatPostString
                        .getSoundSwitchControl(sound_switch);
                DefaultParser mParser = new DefaultParser();
                BaseResponseInfo mBaseResponseInfo = mParser
                        .getBaseResponseInfo(url, post);
                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
                    // 1:开启; 0:关闭
                    if ("0".equals(sound_switch)) {
                        OtherInfo.getInstance().setRemoteSoundOpen(false);
                    } else {
                        OtherInfo.getInstance().setRemoteSoundOpen(true);
                    }
                    listener.onFinished(mBaseResponseInfo);
                } else {
                    listener.onErro(mBaseResponseInfo);
                }
            }
        }.start();
    }

    // 新车主取消过户
    public static void TransferNewCancleControl(final String outtingId,
                                                final GetResultListCallback listener) {
        if (listener == null)
            return;
        new Thread() {
            @Override
            public void run() {
                // 链接地址
                String url = URLConfig.getM_TRANSFER_NEW_CANCLE_URL();
                // Post参数
                String post = CreatPostString.getTransferNewCancle(outtingId);
                DefaultParser mParser = new DefaultParser();
                BaseResponseInfo mBaseResponseInfo = mParser
                        .getBaseResponseInfo(url, post);
                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
                    listener.onFinished(mBaseResponseInfo);
                } else {
                    listener.onErro(mBaseResponseInfo);
                }
            }
        }.start();
    }

    public static void GetRemoteCommControl(final GetResultListCallback listener) {
        if (listener == null)
            return;
        new Thread() {
            @Override
            public void run() {
                // 链接地址
                String url = URLConfig.getM_SAFE_APPSETTING_URL();
                // Post参数
                String post = CreatPostString.getCommSetInfo();
                DefaultParser mParser = new DefaultParser();
                BaseResponseInfo mBaseResponseInfo = mParser
                        .getBaseResponseInfo(url, post);
                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
                    // 1:开启; 0:关闭
                    String value = mParser.getValue("sound_switch");
                    if ("0".equals(value)) {
                        OtherInfo.getInstance().setRemoteSoundOpen(false);
                    } else {
                        OtherInfo.getInstance().setRemoteSoundOpen(true);
                    }
                    listener.onFinished(mBaseResponseInfo);
                } else {
                    listener.onErro(mBaseResponseInfo);
                }
            }
        }.start();
    }

    /**
     * 更换设备
     * @param deviceid
     * @param listener
     */
    public static void GetCarChangeDevice(final String deviceid,
                                          final GetResultListCallback listener) {
        if (listener == null)
            return;
        new Thread() {
            @Override
            public void run() {
                // 链接地址
                String url = URLConfig.getM_CAR_CHANGEDEVICE_URL();
                // Post参数
                String post = CreatPostString.getReBindParams(deviceid);
                DefaultParser mParser = new DefaultParser();
                BaseResponseInfo mBaseResponseInfo = mParser
                        .getBaseResponseInfo(url, post);
                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
                    listener.onFinished(mBaseResponseInfo);
                } else {
                    listener.onErro(mBaseResponseInfo);
                }
            }
        }.start();
    }

    /**
     * 获取广告信息
     */
    public final static String ADVERT_TYPE_COMMON = "1";
    public final static String ADVERT_TYPE_FEE    = "2";

    public static void GetAdvert(final String advert_type,
                                 final GetResultListCallback listener) {
        if (listener == null)
            return;
        new Thread() {
            @Override
            public void run() {
                // 链接地址
                String url = URLConfig.getM_ADVERT_GETADVERT_URL();
                // Post参数
                String post = CreatPostString.getAdvert(advert_type);
                AdvertiseInfoParser mParser = new AdvertiseInfoParser();
                BaseResponseInfo mBaseResponseInfo = mParser
                        .getBaseResponseInfo(url, post);
                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
                    listener.onFinished(mParser.getReturn());
                } else {
                    listener.onErro(mBaseResponseInfo);
                }
            }
        }.start();
    }

    /**
     * 获取车辆位置信息
     * @param listener
     */
    public static void GetCarExtInfo(final GetResultListCallback listener) {

        if (listener == null)
            return;
        new Thread() {
            @Override
            public void run() {
                // 链接地址
                String url = URLConfig.getM_CAR_GETCAREXTINFO();
                // Post参数
                String post = CreatPostString.getEmptyParams();
                DefaultParser mParser = new DefaultParser();
                BaseResponseInfo mBaseResponseInfo = mParser
                        .getBaseResponseInfo(url, post);
                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
                    listener.onFinished(mParser.getValue("position"));
                    // listener.onFinished("108.899101,34.222398");
                } else {
                    listener.onErro(mBaseResponseInfo);
                }
            }
        }.start();
    }

    /**
     * 获取经销商信息
     * @param listener
     */
    public static void GetDealerInfoResult(final GetResultListCallback listener) {

        if (listener == null)
            return;
        new Thread() {
            @Override
            public void run() {
                // 链接地址
                String url = URLConfig.getM_DEALER_GETDEALERINFO();
                // Post参数
                String post = CreatPostString.getDealerInfo();
                DefaultParser mParser = new DefaultParser();
                BaseResponseInfo mBaseResponseInfo = mParser
                        .getBaseResponseInfo(url, post);
                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
                    ContactsInfo.getInstance().salesHotLine = mParser.getValue("tel");
                    ContactsInfo.getInstance().serviceHotLine = mParser.getValue("tel1");
                    ContactsInfo.getInstance().name = mParser.getValue("username");
                    ContactsInfo.getInstance().address = mParser.getValue("addres");
                    ContactsInfo.getInstance().map = mParser.getValue("map");
                    // 测试用
                    // LoginInfo.setDealerTel("");
                    // LoginInfo.setServiceTel("");
                    // 测试结束
                    listener.onFinished(mBaseResponseInfo);
                } else {
                    listener.onErro(mBaseResponseInfo);
                }
            }
        }.start();
    }

    public static SimpleDateFormat dateFormat = new SimpleDateFormat(
            "yyyy年MM月dd日");

    /**
     * 获取经销商信息
     * @param listener
     */
    public static void GetVersionLog(final GetResultListCallback listener) {

        if (listener == null)
            return;
        new Thread() {
            @Override
            public void run() {
                // 链接地址
                String url = URLConfig.getM_CAR_GETVERSIONLOG();
                // Post参数
                String post = CreatPostString.getVersionLogInfo();
                DefaultParser mParser = new DefaultParser();
                BaseResponseInfo mBaseResponseInfo = mParser
                        .getBaseResponseInfo(url, post);
                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
                    List<VersionLog> logList = new ArrayList<VersionLog>();
                    JSONObject jsonObject = mParser.getmJson();
                    try {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            VersionLog versionLogs = new VersionLog();
                            String version = jsonObject1.getString("version");
                            String createdate = jsonObject1.getString("createdate");
                            JSONArray jsonArray1 = jsonObject1.getJSONArray("info");

                            long dateTimes = 0;
                            try {
                                dateTimes = Long.parseLong(createdate);
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                            }
                            Log.e("dateTimes", "dateTimes" + dateTimes);
                            Calendar c = Calendar.getInstance();
                            c.setTimeInMillis(dateTimes * 1000);
                            Date date1 = c.getTime();
                            String formatDate = dateFormat.format(date1);
                            ArrayList<String> listString = new ArrayList<String>();
                            for (int j = 0; j < jsonArray1.length(); j++) {
                                String string = jsonArray1.getString(j);
                                listString.add(string);
                            }
                            versionLogs.setInfos(listString);
                            versionLogs.setVersion(version);
                            versionLogs.setCreatedate(createdate);
                            versionLogs.setCreatedateString(formatDate);
                            logList.add(versionLogs);
                        }
                        listener.onFinished(logList);
                    } catch (Exception e) {
                        e.printStackTrace();
                        mBaseResponseInfo.setInfo("网络错误");
                        listener.onErro(mBaseResponseInfo);
                    }

                } else {
                    listener.onErro(mBaseResponseInfo);
                }
            }
        }.start();
    }

    public static void GetUpdateMoveDeviceidResult(final String move_deviceid, final String validate, final String mobile,
                                                   final GetResultListCallback listener) {

        if (listener == null)
            return;
        new Thread() {
            @Override
            public void run() {
                // 链接地址
                String url = URLConfig.getM_UPDATEMOVEDEVICEID();
                // Post参数
                String post = CreatPostString
                        .getUpdateMoveDeviceid(move_deviceid, validate, mobile);
                DefaultParser mParser = new DefaultParser();
                BaseResponseInfo mBaseResponseInfo = mParser
                        .getBaseResponseInfo(url, post);
                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
                    listener.onFinished(mBaseResponseInfo);
                } else {
                    listener.onErro(mBaseResponseInfo);
                }
            }
        }.start();
    }

}

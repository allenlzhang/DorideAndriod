package com.carlt.doride.http.retrofitnet;


import com.carlt.doride.data.car.CarNowStatusInfo;
import com.carlt.doride.data.car.WaringLampInfo;
import com.carlt.doride.data.remote.RemoteDirectPressureInfo;
import com.carlt.doride.http.retrofitnet.model.ActivateStepInfo;
import com.carlt.doride.http.retrofitnet.model.AuthCarInfo;
import com.carlt.doride.http.retrofitnet.model.BaseErr;
import com.carlt.doride.http.retrofitnet.model.CarConfigRes;
import com.carlt.doride.http.retrofitnet.model.CarInfoRsp;
import com.carlt.doride.http.retrofitnet.model.ContactsInfo;
import com.carlt.doride.http.retrofitnet.model.GetCarInfo;
import com.carlt.doride.http.retrofitnet.model.RemoteCarStateInfo;
import com.carlt.doride.http.retrofitnet.model.RemoteCommonInfo;
import com.carlt.doride.http.retrofitnet.model.SmsToken;
import com.carlt.doride.http.retrofitnet.model.User;
import com.carlt.doride.http.retrofitnet.model.UserInfo;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Description:
 * Company    : carlt
 * Author     : zhanglei
 * Date       : 2019/3/5 11:52
 */
public interface ApiService {
    //用户统一登录
    @POST("app/User/Login")
    Observable<User> commonLogin(@Body HashMap<String, Object> map);

    @POST("app/User/GetUserInfo")
    Observable<UserInfo> getUserInfo(@Body HashMap<String, Object> token);

    // 激活设备
    @POST("app/DeviceActive/active")
    Observable<BaseErr> active(@Body Map<String, Object> param);

    // 获取激活日志
    @POST("app/DeviceActive/getLogs")
    Observable<ActivateStepInfo> getLogs(@Body Map<String, Object> param);

    //获取短信验证码token
    @POST("app/SmsCode/GetSmsToken")
    Observable<SmsToken> getSmsToken(@Body Map<String, String> params);

    //发送短信验证码
    @POST("app/SmsCode/SendSmsCode")
    Observable<BaseErr> SendSmsCode(@Body Map<String, Object> params);

    //用户统一注册
    @POST("app/User/Reg")
    Observable<BaseErr> commonReg(@Body Map<String, Object> params);

    //获取我的车辆和被授权车辆
    @POST("app/CarAuth/GetMyCarList")
    Observable<AuthCarInfo> getMyCarList(@Body Map<String, Object> param);

    //获取车辆信息
    @POST("app/Car/GetCarInfo")
    Observable<GetCarInfo> getCarInfo(@Body Map<String, Integer> param);

    //联系我们
    @POST("app/Support/Contacts")
    Observable<ContactsInfo> contacts(@Body Map<String, Object> param);

    // 获取指定车款远程配置项
    @POST("app/Config/GetCarConfig")
    Observable<CarConfigRes> GetCarConfig(@Body Map<String, Object> param);

    // 0x8F 读取车辆信息(首页里程等信息)
    @POST("abiz/GetCarInfo/Issued")
    Observable<CarInfoRsp> Issued(@Body Map<String, Object> param);

    @POST("abiz/RemoteStart/Issued")
    Observable<RemoteCommonInfo> RemoteStart(@Body Map<String, Object> param);

    @POST("abiz/RemoteStall/Issued")
    Observable<RemoteCommonInfo> RemoteStall(@Body Map<String, Object> param);

    @POST("abiz/RemoteLock/Issued")
    Observable<RemoteCommonInfo> RemoteLock(@Body Map<String, Object> param);

    @POST("abiz/RemoteWindow/Issued")
    Observable<RemoteCommonInfo> RemoteWindow(@Body Map<String, Object> param);

    @POST("abiz/SkyLight/Issued")
    Observable<RemoteCommonInfo> SkyLight(@Body Map<String, Object> param);

    @POST("abiz/RemoteTrunk/Issued")
    Observable<RemoteCommonInfo> RemoteTrunk(@Body Map<String, Object> param);

    @POST("abiz/ChairHeating/Issued")
    Observable<RemoteCommonInfo> ChairHeating(@Body Map<String, Object> param);

    @POST("abiz/CarLocating/Issued")
    Observable<RemoteCommonInfo> CarLocating(@Body Map<String, Object> param);

    @POST("abiz/State/Issued")
    Observable<RemoteCarStateInfo> carState(@Body Map<String, Object> param);

    @POST("abiz/AirCondition/Issued")
    Observable<RemoteCommonInfo> AirCondition(@Body Map<String, Object> param);

    @POST("abiz/QueryCurrentData/Issued")
    Observable<CarNowStatusInfo> QueryCurrentData(@Body Map<String, Object> param);

    @POST("abiz/WarningLights/Issued")
    Observable<WaringLampInfo> WarningLights(@Body Map<String, Object> param);

    @POST("abiz/DirectTirePressure/Issued")
    Observable<RemoteDirectPressureInfo> DirectTirePressure(@Body Map<String, Object> param);

    @POST("abiz/Navigation/Issued")
    Observable<RemoteCommonInfo> Navigation(@Body Map<String, Object> param);


}

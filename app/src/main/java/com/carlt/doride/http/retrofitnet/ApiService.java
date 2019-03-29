package com.carlt.doride.http.retrofitnet;


import com.carlt.doride.http.retrofitnet.model.ActivateStepInfo;
import com.carlt.doride.http.retrofitnet.model.AuthCarInfo;
import com.carlt.doride.http.retrofitnet.model.BaseErr;
import com.carlt.doride.http.retrofitnet.model.CarConfigRes;
import com.carlt.doride.http.retrofitnet.model.ContactsInfo;
import com.carlt.doride.http.retrofitnet.model.GetCarInfo;
import com.carlt.doride.http.retrofitnet.model.SmsToken;
import com.carlt.doride.http.retrofitnet.model.User;
import com.carlt.doride.http.retrofitnet.model.UserInfo;
import com.carlt.sesame.data.car.CarInfo;

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
    @POST("User/Login")
    Observable<User> commonLogin(@Body HashMap<String, Object> map);

    @POST("User/GetUserInfo")
    Observable<UserInfo> getUserInfo(@Body HashMap<String, Object> token);

    // 激活设备
    @POST("DeviceActive/active")
    Observable<BaseErr> active(@Body Map<String, Object> param);

    // 获取激活日志
    @POST("DeviceActive/getLogs")
    Observable<ActivateStepInfo> getLogs(@Body Map<String, Object> param);

    //获取短信验证码token
    @POST("SmsCode/GetSmsToken")
    Observable<SmsToken> getSmsToken(@Body Map<String, String> params);

    //发送短信验证码
    @POST("SmsCode/SendSmsCode")
    Observable<BaseErr> SendSmsCode(@Body Map<String, Object> params);

    //用户统一注册
    @POST("User/Reg")
    Observable<BaseErr> commonReg(@Body Map<String, Object> params);

    //获取我的车辆和被授权车辆
    @POST("CarAuth/GetMyCarList")
    Observable<AuthCarInfo> getMyCarList(@Body Map<String, Object> param);

    //获取车辆信息
    @POST("Car/GetCarInfo")
    Observable<GetCarInfo> getCarInfo(@Body Map<String, Integer> param);

    //联系我们
    @POST("Support/Contacts")
    Observable<ContactsInfo>contacts(@Body Map<String, Object> param);

    // 获取指定车款远程配置项
    @POST("Config/GetCarConfig")
    Observable<CarConfigRes>GetCarConfig(@Body Map<String, Object> param);
}

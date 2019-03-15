package com.carlt.doride.http.retrofitnet;


import com.carlt.doride.http.retrofitnet.model.ActivateStepInfo;
import com.carlt.doride.http.retrofitnet.model.BaseErr;

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

    //    @POST("User/Login")
    //    Observable<User> commonLogin(@Body HashMap<String, Object> map);

    //    @POST("User/GetUserInfo")
    //    Observable<UserInfo> getUserInfo(@Body HashMap<String, Object> token);

    // 激活设备
    @POST("DeviceActive/active")
    Observable<BaseErr> active(@Body Map<String, Object> param);

    // 获取激活日志
    @POST("DeviceActive/getLogs")
    Observable<ActivateStepInfo> getLogs(@Body Map<String, Object> param);
}

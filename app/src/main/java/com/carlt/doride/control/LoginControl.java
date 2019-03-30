package com.carlt.doride.control;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.carlt.doride.DorideApplication;
import com.carlt.doride.MainActivity;
import com.carlt.doride.http.retrofitnet.ApiRetrofit;
import com.carlt.doride.http.retrofitnet.ApiService;
import com.carlt.doride.http.retrofitnet.BaseMvcObserver;
import com.carlt.doride.http.retrofitnet.model.CarConfigRes;
import com.carlt.doride.http.retrofitnet.model.ContactsInfo;
import com.carlt.doride.http.retrofitnet.model.GetCarInfo;
import com.carlt.doride.http.retrofitnet.model.User;
import com.carlt.doride.http.retrofitnet.model.UserInfo;
import com.carlt.doride.ui.activity.login.DeviceBindActivity;
import com.carlt.doride.ui.activity.login.UserLoginActivity;
import com.carlt.doride.ui.view.UUToast;
import com.carlt.doride.ui.view.UUUpdateDialog;
import com.carlt.doride.utils.CipherUtils;
import com.carlt.sesame.control.CPControl;
import com.carlt.sesame.preference.TokenInfo;
import com.carlt.sesame.ui.SesameMainActivity;
import com.carlt.sesame.ui.activity.safety.FreezeActivity;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;


/**
 * 登录控制
 *
 * @author daisy
 */
public class LoginControl {
    public static Activity                            mCtx;
    public static UUUpdateDialog.DialogUpdateListener mDialogUpdateListener;

    public static void logic(final Activity mContext) {
        mCtx = mContext;
        DorideApplication.getInstanse().setIsshowupdata(false);
//        String className = mContext.getClass().getName();
        // 判断是否绑定设备
//        String s = GetCarInfo.getInstance().deviceNum;
//        String s2 = GetCarInfo.getInstance().deviceNum;
//        Log.e("info", "deviceidstring==" + s);
//        Log.e("info", "deviceidstring==" + s2);
        String vin = GetCarInfo.getInstance().vin;
        if (!TextUtils.isEmpty(vin)) {

            int activate_status = GetCarInfo.getInstance().remoteStatus;
            Log.e("info", "activate_status==" + activate_status);
            //            activate_status=2;
            switch (activate_status) {
                case 3:
                    //激活失败
//                    Intent loginIntent = new Intent(mContext, DeviceBindActivity.class);
//                    mContext.startActivity(loginIntent);
//                    break;
                case 0:
                    //未激活
                case 1:
                    //正在激活
                case 2:
                    //激活成功
                    Intent mainIntent;
                    if (GetCarInfo.getInstance().carType == 1) {
                        // 大乘绑定和激活合并，如果激活直接进入主页
                        mainIntent = new Intent(mContext, MainActivity.class);
                        mContext.startActivity(mainIntent);
                        mContext.finish();
                    } else if (GetCarInfo.getInstance().carType == 2) {
                        if (UserInfo.getInstance().userFreeze == 2) {
                            // 处在冻结状态
                            mainIntent = new Intent(mContext, FreezeActivity.class);
                            mainIntent.putExtra(FreezeActivity.FROM_NAME, mContext.getClass().getName());
                        } else {
                            mainIntent = new Intent(mContext, SesameMainActivity.class);
                        }
                        mContext.startActivity(mainIntent);
                        mContext.finish();
                    }


                    break;
                default:
                    Intent loginIntent1 = new Intent(mContext, UserLoginActivity.class);
                    mContext.startActivity(loginIntent1);
                    break;
            }

        } else {
            Intent loginIntent = new Intent(mContext, DeviceBindActivity.class);
            mContext.startActivity(loginIntent);
        }
    }

    public static void parseLoginInfo(JSONObject data) {
        if (data != null) {
            int app_type = data.optInt("app_type", 1);
            //            LoginInfo.setApp_type(app_type);
            if (app_type == 2) {
                //  芝麻
                parseSesameLoginInfo(data);
            } else if (app_type == 1) {
                // 大乘
                parseDorideLoginInfo(data);
            } else if (app_type == 0) {
                //                parseDorideLoginInfo(data);
                parseToken(data);
            }


        }
    }

    private static void parseToken(JSONObject data) {
        JSONObject member = data.optJSONObject("member");
        TokenInfo.setToken((member.optString("access_token", "")));
    }

    private static void parseSesameLoginInfo(JSONObject mJSON_data) {
        JSONObject member = mJSON_data.optJSONObject("member");
        TokenInfo.setToken((member.optString("access_token", "")));

    }

    private static void parseDorideLoginInfo(JSONObject data) {
        JSONObject member = data.optJSONObject("member");

        TokenInfo.setToken(member.optString("access_token", ""));

        TokenInfo.setToken((member.optString("access_token", "")));


    }


    protected static ApiService                      mApiService = ApiRetrofit.getInstance().getService(ApiService.class);
    private static   CompositeDisposable             compositeDisposable;
    private static   CPControl.GetResultListCallback callback;

    public static void setCallback(CPControl.GetResultListCallback callback) {
        LoginControl.callback = callback;
    }

    private static void addDisposable(Observable<?> observable, BaseMvcObserver observer) {
        if (compositeDisposable == null) {
            compositeDisposable = new CompositeDisposable();
        }
        compositeDisposable.add(observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(observer));


    }

    /**
     * 登录
     * @param account
     * @param password
     */
    public static void Login(String account, String password) {
        HashMap<String, Object> mMap = new HashMap<>();
        mMap.put("version", 100);
        mMap.put("moveDeviceName", DorideApplication.MODEL_NAME);
        mMap.put("loginModel", DorideApplication.MODEL);
        mMap.put("loginSoftType", "Android");
        mMap.put("moveDeviceid", DorideApplication.NIMEI);
        mMap.put("mobile", account);
        mMap.put("password", CipherUtils.md5(password));
        mMap.put("loginType", 1);
        mMap.put("pwdReally", password);
        addDisposable(mApiService.commonLogin(mMap), new BaseMvcObserver<User>() {
            @Override
            public void onSuccess(User result) {
                LoginSuccess(result);
            }

            @Override
            public void onError(String msg) {
                callback.onErro(msg);
            }
        });
    }

    private static void LoginSuccess(User result) {
        if (result.err != null) {
            callback.onErro(result.err.msg);
        } else {
            TokenInfo.setToken(result.token);
            HashMap<String, Object> prams = new HashMap<>();
            prams.put("token", result.token);
            getUserInfo(prams);
        }
    }

    /**
     * 获取用户信息
     * @param prams
     */
    public static void getUserInfo(HashMap<String, Object> prams) {
        addDisposable(mApiService.getUserInfo(prams), new BaseMvcObserver<UserInfo>() {
            @Override
            public void onSuccess(UserInfo result) {
                getUserInfoSuccess(result);
            }

            @Override
            public void onError(String msg) {
                callback.onErro(msg);
                UUToast.showUUToast(mCtx, msg);
            }
        });
    }

    private static void getUserInfoSuccess(UserInfo result) {
        if (result.err != null) {
            callback.onErro(result.err.msg);
        } else {
            Log.e("userInfo", result.toString());
            UserInfo.getInstance().setUserInfo(result);
            Log.e("userInfo", UserInfo.getInstance().toString());
            getCarInfo(new HashMap<String, Integer>());
        }
    }

    /**
     * 获取车辆信息
     * @param param
     */
    public static void getCarInfo(Map<String, Integer> param) {
        addDisposable(mApiService.getCarInfo(param), new BaseMvcObserver<GetCarInfo>() {
            @Override
            public void onSuccess(GetCarInfo result) {
                carInfoSuccess(result);
            }

            @Override
            public void onError(String msg) {
                callback.onErro(msg);
            }
        });
    }

    private static void carInfoSuccess(GetCarInfo carInfo) {
        if (carInfo.err != null) {
            callback.onErro(carInfo.err.msg);
        } else {
            contacts(new HashMap<String, Object>());

//            HashMap<String,Object> params = new HashMap<>();
//            params.put("brandCarId",carInfo.styleId);
//            params.put("productId",carInfo.productId);
//            params.put("deviceIdString",carInfo.deviceNum);
//            GetCarConfig(params);

            GetCarInfo.getInstance().setCarInfo(carInfo);
            Log.e("carInfo", carInfo.toString());
            callback.onFinished(null);
        }
    }

    /**
     * 联系我们
     * @param param
     */
    private static void contacts(Map<String,Object> param){
        addDisposable(mApiService.contacts(param), new BaseMvcObserver<ContactsInfo>() {
            @Override
            public void onSuccess(ContactsInfo result) {
                ContactsInfo.getInstance().setContactsInfo(result);
            }

            @Override
            public void onError(String msg) {

            }
        });
    }

    private static void GetCarConfig(Map<String, Object> param) {
        addDisposable(mApiService.GetCarConfig(param), new BaseMvcObserver<CarConfigRes>() {
            @Override
            public void onSuccess(CarConfigRes result) {
                CarConfigRes.getInstance().setCarConfigRes(result);
            }

            @Override
            public void onError(String msg) {

            }
        });
    }
}

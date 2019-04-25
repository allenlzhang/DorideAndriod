package com.carlt.doride.http.retrofitnet;

import com.blankj.utilcode.util.LogUtils;
import com.carlt.doride.BuildConfig;
import com.carlt.doride.http.retrofitnet.cookies.CookiesManager;
import com.carlt.doride.http.retrofitnet.model.GetCarInfo;
import com.carlt.doride.systemconfig.URLConfig;
import com.carlt.sesame.preference.TokenInfo;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Description:
 * Company    : carlt
 * Author     : zhanglei
 * Date       : 2019/3/5 11:49
 */
public class ApiRetrofit implements IApiService {
    //    private static final   String       TAG           = "ApiRetrofit===>";
    public static volatile ApiRetrofit  sApiRetrofit;
    //    public static final    String       BASE_URL      = "http://test.linewin.cc:8888/app/";
    //        public static final    String       BASE_URL      = "http://www.wanandroid.com/";
    //    public static final    String       TEST_ACCESSID = "18644515396614518644";   //autoGo 测试
    private                Retrofit     mRetrofit;
    private                OkHttpClient mHttpClient;
    private                ApiService   mApiService;
    private static         Interceptor  mInterceptor = chain -> {
        Request request = chain.request();

        //                HttpUrl.Builder authorizedUrlBuilder = request.url().newBuilder()
        //                .addQueryParameter("deviceID", String.valueOf(GetCarInfo.getInstance().deviceid))
        //                .addQueryParameter("carId", GetCarInfo.getInstance().id);
        HttpUrl oldUrl = request.url();
        HttpUrl baseUrl = HttpUrl.parse(URLConfig.getAutoGoUrl());
        HttpUrl newBuilder = oldUrl.newBuilder()
                .scheme(baseUrl.scheme())
                .host(baseUrl.host())
                .port(baseUrl.port())
                .build();


        Request.Builder newRequest = request.newBuilder()
                .url(newBuilder)
                .header("Carlt-Access-Id", URLConfig.getAutoGoAccessId())
                .header("Content-Type", "application/json")
                .header("Carlt-Token", TokenInfo.getToken())
                .header("Carlt-Remote-Token", TokenInfo.getToken())
                .method(request.method(), request.body());

        //            long startTime = System.currentTimeMillis();

        //            Response response = chain.proceed(newRequest.build());
        //            long endTime = System.currentTimeMillis();
        //            long duration = endTime - startTime;
        //            String content = response.body().string();
        //            Log.e(TAG, "----------Request Start----------------");
        //            Log.e(TAG, "| " + request.toString() + request.headers());
        //            Log.e(TAG, "| Response:" + content);
        //            Log.e(TAG, "----------Request End:" + duration + "毫秒----------");
        return chain.proceed(newRequest.build());
    };

    public static HashMap getRemoteCommonParams() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("carId", GetCarInfo.getInstance().id);
        map.put("deviceID", GetCarInfo.getInstance().deviceNum);


        return map;
    }

    public static ApiRetrofit getInstance() {
        if (sApiRetrofit == null) {
            synchronized (Object.class) {
                if (sApiRetrofit == null) {
                    sApiRetrofit = new ApiRetrofit();
                }
            }
        }
        return sApiRetrofit;
    }

    private static HttpLoggingInterceptor getLogInterceptor() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        if (BuildConfig.DEBUG) {
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        } else {
            interceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
        }
        return interceptor;
    }

    private ApiRetrofit() {


        mHttpClient = new OkHttpClient.Builder()
                .addInterceptor(mInterceptor)
                .addInterceptor(getLogInterceptor())
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .cookieJar(new CookiesManager())
                .build();
        mRetrofit = new Retrofit.Builder()
                .baseUrl(URLConfig.AUTO_TEST_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                //                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(mHttpClient)
                .build();
        mApiService = mRetrofit.create(ApiService.class);
    }

    public void changeBaseUrl(String baseUrl) {
        LogUtils.e("baseUrl===>" + baseUrl);
        mRetrofit = null;
        mRetrofit = new Retrofit.Builder()
                .client(mHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(baseUrl)
                .build();
        mApiService = mRetrofit.create(ApiService.class);
    }

    public ApiService getApiService() {
        return mApiService;
    }

    @Override
    public <T> T getService(Class<T> service) {
        return mRetrofit.create(service);
    }
}

package com.carlt.doride.http.retrofitnet;

import com.carlt.doride.BuildConfig;
import com.carlt.doride.http.retrofitnet.cookies.CookiesManager;
import com.carlt.doride.model.LoginInfo;
import com.carlt.doride.systemconfig.URLConfig;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
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
    private static final   String       TAG           = "ApiRetrofit===>";
    public static volatile ApiRetrofit  sApiRetrofit;
    public static final    String       BASE_URL      = "http://test.linewin.cc:8888/app/";
    //        public static final    String       BASE_URL      = "http://www.wanandroid.com/";
    public static final    String       TEST_ACCESSID = "18644515396614518644";   //autoGo 测试
    private                Retrofit     mRetrofit;
    private                OkHttpClient mHttpClient;
    private                ApiService   mApiService;
    private static         Interceptor  mInterceptor  = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            Request.Builder newRequest = request.newBuilder()
                    .header("Carlt-Access-Id", URLConfig.getAutoGoAccessId())
                    .header("Content-Type", "application/json")
                    .header("Carlt-Token", LoginInfo.getAccess_token())
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
        }
    };

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
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .cookieJar(new CookiesManager())
                .build();
        mRetrofit = new Retrofit.Builder()
                .baseUrl(URLConfig.getAutoGoUrl())
                .addConverterFactory(GsonConverterFactory.create())
                //                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(mHttpClient)
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

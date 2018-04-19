package com.carlt.doride.http;


import android.text.TextUtils;

import com.carlt.doride.data.BaseResponseInfo;
import com.carlt.doride.exception.RequestHttpException;
import com.carlt.doride.model.ListModel;
import com.carlt.doride.model.LoginInfo;
import com.carlt.doride.utils.CreateHashMap;
import com.carlt.doride.utils.ILog;
import com.carlt.doride.utils.ParameterizedTypeImpl;
import com.google.gson.Gson;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * 创建网络请求
 * createxx() 获取Observable<BaseResponseInfo>,订阅此被观察者
 * 里面封装了网络的访问，采用gson　完成json －＞　Object 的转换，完全正确会回调onNext()
 * 否则会回调onError(ex) 业务错误 ex 为 com.linewin.crm.exception.RequestHttpException
 * Created by yyun on 17-11-2.
 */
public class HttpRequetCreater {
    private static OkHttpClient mHttpClient = new OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(new LogInterceptor())
            .build();

    /**
     * @param url
     * @param <T>
     * @return
     */
    public static <T> Observable<BaseResponseInfo<T>> createGet(String url) {
        return createGet(url, null);
    }

    public static <T> Observable<BaseResponseInfo<T>> createGet(String url, HashMap<String, String> param) {
        return createGet(null, url, param);
    }

    public static <T> Observable<BaseResponseInfo<T>> createGet(Type type, String url) {
        return createGet(type, url, null);
    }

    /**
     * 创建Get
     *
     * @param type
     * @param url
     * @param param
     * @param <T>   返回的BaseResponseInfo<T> 通过本类的 getTypexxxx() 获取
     * @return
     */
    public static <T> Observable<BaseResponseInfo<T>> createGet(Type type, String url, HashMap<String, String> param) {
        return createRequest(type, url, param, "GET");
    }

    //Post
    public static <T> Observable<BaseResponseInfo<T>> createPost(String url) {
        return createPost(url, null);
    }

    public static <T> Observable<BaseResponseInfo<T>> createPost(String url, HashMap<String, String> param) {
        return createPost(null, url, param);
    }

    public static <T> Observable<BaseResponseInfo<T>> createPost(Type type, String url) {
        return createPost(type, url, null);
    }

    public static <T> Observable<BaseResponseInfo<T>> createPost(Type type, String url, HashMap<String, String> param) {
        return createRequest(type, url, param, "POST");
    }

    //创建网络请求 Observable
    private static <T> Observable<BaseResponseInfo<T>> createRequest(final Type type, final String url, final HashMap<String, String> param, final String method) {
        return Observable.create(new ObservableOnSubscribe<BaseResponseInfo<T>>() {
            @Override
            public void subscribe(ObservableEmitter<BaseResponseInfo<T>> e) throws Exception {
                HashMap<String, String> mp = new HashMap<>();
                String requestUrl = url;
                if (param != null) {
                    mp = param;
                }
                CreateHashMap.addDefault(mp);
                Response reponse = getRequestExecute(mp, requestUrl, method);
                BaseResponseInfo<T> mInfo = null;
                if (!reponse.isSuccessful()) {
                    mInfo = new BaseResponseInfo<>();
                    mInfo.setFlag(reponse.code());
                    mInfo.setInfo(reponse.code() + " " + BaseResponseInfo.NET_ERROR);
                    RequestHttpException ex = new RequestHttpException(mInfo.getInfo());
                    ex.setmInfo(mInfo);
                    ex.setUrl(url);
                    e.onError(ex);
                    return;
                }

                String json = reponse.body().string();
                try {
                    if (null != type) {
                        Gson gson = new Gson();
                        mInfo = gson.fromJson(json, type);
                    } else {
                        Gson gson = new Gson();
                        mInfo = gson.fromJson(json, BaseResponseInfo.class);
                    }
                } catch (Exception ex) {
                    ILog.e("http-Erp~", "Error:" + ex.getMessage());
                    mInfo = new BaseResponseInfo<>();
                    mInfo.setFlag(BaseResponseInfo.ERRO);
                    mInfo.setInfo("json parse error");
                }

                if (mInfo.isSuccessful()) {
                    e.onNext(mInfo);
                    e.onComplete();
                } else {
                    RequestHttpException ex = new RequestHttpException(mInfo.getInfo());
                    ex.setmInfo(mInfo);
                    ex.setUrl(url);
                    e.onError(ex);
                }
            }
        });
    }

    /**
     * 获取　Request
     *
     * @param mp
     * @param requestUrl
     * @param method
     * @return
     */
    private static Response getRequestExecute(HashMap<String, String> mp, String requestUrl, String method) throws IOException {
        Request request;
        if ("GET".equals(method)) {//Get Request
            if (!TextUtils.isEmpty(LoginInfo.getAccess_token())) {
                mp.put("token", LoginInfo.getAccess_token());
            }
            requestUrl = requestUrl + CreatString(mp);
            request = new Request.Builder()
                    .url(requestUrl)
                    .get()
                    .build();
        } else {//Post Request
            if (!TextUtils.isEmpty(LoginInfo.getAccess_token())) {
                requestUrl = requestUrl + "?token=" + LoginInfo.getAccess_token();
            }
            FormBody.Builder formBuilder = new FormBody.Builder();
            Iterator<String> iterators = mp.keySet().iterator();
            while (iterators.hasNext()) {
                String key = iterators.next();
                String value = mp.get(key);
                formBuilder.add(key, value);
                ILog.e("http", "param--" + key + ":" + value);
            }

            RequestBody rBody = formBuilder.build();
            request = new Request.Builder()
                    .url(requestUrl)
                    .post(rBody)
                    .build();
        }
        ILog.e("http", "http请求--" + request.toString());
        Call call = mHttpClient.newCall(request);
        return call.execute();
    }


    //Get sync
    public static <T> BaseResponseInfo<T> getSync(String url, HashMap<String, String> param) throws IOException {
        return getSync(null, url, param);
    }

    //Get sync
    public static <T> BaseResponseInfo<T> getSync(Type type, String url, HashMap<String, String> param) throws IOException {

        Response reponse = getRequestExecute(param, url, "GET");
        BaseResponseInfo<T> mInfo = null;
        if (!reponse.isSuccessful()) {
            mInfo = new BaseResponseInfo<>();
            mInfo.setFlag(reponse.code());
            mInfo.setInfo(reponse.code() + " " + BaseResponseInfo.NET_ERROR);
            return mInfo;
        }

        String json = reponse.body().string();
        try {
            if (null != type) {
                Gson gson = new Gson();
                mInfo = gson.fromJson(json, type);
            } else {
                Gson gson = new Gson();
                mInfo = gson.fromJson(json, BaseResponseInfo.class);
            }
        } catch (Exception ex) {
            ILog.e("http-Erp~", "Error:" + ex.getMessage());
            mInfo = new BaseResponseInfo<>();
            mInfo.setFlag(BaseResponseInfo.ERRO);
            mInfo.setInfo("json parse error");
        }
        return mInfo;
    }

    //Post sync
    public static <T> BaseResponseInfo<T> postSync(String url) throws IOException {
        return postSync(null, url, new HashMap<String, String>());
    }

    //Post sync
    public static <T> BaseResponseInfo<T> postSync(String url, HashMap<String, String> param) throws IOException {
        return postSync(null, url, param);
    }

    //Post sync
    public static <T> BaseResponseInfo<T> postSync(Type type, String url, HashMap<String, String> param) throws IOException {
        CreateHashMap.addDefault(param);
        Response reponse = getRequestExecute(param, url, "POST");
        BaseResponseInfo<T> mInfo = null;
        if (!reponse.isSuccessful()) {
            mInfo = new BaseResponseInfo<>();
            mInfo.setFlag(reponse.code());
            mInfo.setInfo(reponse.code() + " " + BaseResponseInfo.NET_ERROR);
            return mInfo;
        }

        String json = reponse.body().string();
        try {
            if (null != type) {
                Gson gson = new Gson();
                mInfo = gson.fromJson(json, type);
            } else {
                Gson gson = new Gson();
                mInfo = gson.fromJson(json, BaseResponseInfo.class);
            }
        } catch (Exception ex) {
            ILog.e("http-Erp~", "Error:" + ex.getMessage());
            mInfo = new BaseResponseInfo<>();
            mInfo.setFlag(BaseResponseInfo.ERRO);
            mInfo.setInfo("json parse error");
        }
        return mInfo;
    }

    private static String CreatString(HashMap<String, String> mMap) {
        StringBuffer sb = new StringBuffer();
        Iterator iter = mMap.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            String key = entry.getKey().toString();
            String value = entry.getValue().toString();
            sb.append("&");
            sb.append(key);
            sb.append("=");
            sb.append(value);
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(0);
        }
        return "?" + sb.toString();
    }

    /**
     * 返回 BaseResponseInfo<T>
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> Type getType(Class<T> clazz) {
        Type type = new ParameterizedTypeImpl(BaseResponseInfo.class, new Type[]{clazz});
        return type;
    }

    /**
     * 返回类似BaseResponseInfo<List<T>>
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> Type getTypeList(Class<T> clazz) {
        // 生成List<T> 中的 List<T>
        Type listType = new ParameterizedTypeImpl(List.class, new Class[]{clazz});
        // 根据List<T>生成完整的Result<List<T>>
        Type type = new ParameterizedTypeImpl(BaseResponseInfo.class, new Type[]{listType});
        return type;
    }

    /**
     * 返回类似BaseResponseInfo<Map<key,value>>
     *
     * @param key
     * @param value
     * @param <T>
     * @return
     */
    public static <T> Type getTypeMap(Class<T> key, Class<T> value) {
        // 生成List<T> 中的 List<T>
        Type mapType = new ParameterizedTypeImpl(Map.class, new Class[]{key, value});
        // 根据List<T>生成完整的Result<List<T>>
        Type type = new ParameterizedTypeImpl(BaseResponseInfo.class, new Type[]{mapType});
        return type;
    }

    /**
     * 专为应付服务端返回的list格式 如下
     * {
     * "code": 200,
     * "data": {
     * "list": [
     * {
     * "carId": 19297,
     * "mobile": 13088997510,
     * "dealerId": 250774,
     * "counselorId": 0,
     * "licensePlateNum": "京G55558",
     * "realName": "sy20171018",
     * "realNamePinyin": "sy20171018",
     * "customCarModel": "",
     * "headImageUrl": "http://ossimage.linewin.cc/Array"
     * }
     * ],
     * "count": 1930
     * },
     * "msg": "",
     * "version": "100",
     * "request": "/index.php"
     * }
     *
     * @param value
     * @param <T>   这个为 ListModel<T> 中的　Ｔ
     * @return　BaseResponseInfo<ListModel<T>>
     */
    public static <T> Type getTypeModelList(Class<T> value) {
        // 生成ListModel<T> 中的　T
        Type listType = new ParameterizedTypeImpl(ListModel.class, new Class[]{value});
        Type type = new ParameterizedTypeImpl(BaseResponseInfo.class, new Type[]{listType});
        return type;
    }

}

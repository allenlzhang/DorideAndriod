package com.carlt.doride.http;


import android.text.TextUtils;

import com.carlt.doride.model.LoginInfo;
import com.carlt.doride.systemconfig.URLConfig;
import com.carlt.doride.utils.FileUtil;
import com.carlt.doride.utils.ILog;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * http连接类
 * Created by Administrator on 2017/8/22 0022.
 */

public class HttpLinker {

    private static final String TAG = "HttpLinker";

    private static OkHttpClient mHttpClient = new OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(new LogInterceptor())
            .build();

    private static OkHttpClient mHttpClientPic = new OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            //            .addInterceptor(new LogPicInterceptor())
            .build();

    public static void post(String url, HashMap<String, String> param, Callback callback) {
        param.put("client_id", URLConfig.getClientID());
        if (!TextUtils.isEmpty(LoginInfo.getAccess_token())) {
            param.put("token", LoginInfo.getAccess_token());
        }

        Logger.e("client_id---" + URLConfig.getClientID() + "/n" + "token---" + LoginInfo.getAccess_token());
        FormBody.Builder formBuilder = new FormBody.Builder();
        Iterator<String> iterators = param.keySet().iterator();
        while (iterators.hasNext()) {
            String key = iterators.next();
            String value = param.get(key);
            formBuilder.add(key, value);
            ILog.e("http", "param--" + key + ":" + value);
        }

        RequestBody rBody = formBuilder.build();
        Request request = new Request.Builder()
                .url(url)
                .post(rBody)
                .build();
        ILog.e("http", "http请求--" + request.toString());

        Call call = mHttpClient.newCall(request);
        call.enqueue(callback);
    }


    public static Response postSync(String url, HashMap<String, String> param) throws IOException {
        if (!TextUtils.isEmpty(LoginInfo.getAccess_token())) {
            url = url + "?token=" + LoginInfo.getAccess_token();
        }
        FormBody.Builder formBuilder = new FormBody.Builder();
        Iterator<String> iterators = param.keySet().iterator();
        while (iterators.hasNext()) {
            String key = iterators.next();
            String value = param.get(key);
            formBuilder.add(key, value);
            ILog.e("http", "param--" + key + ":" + value);
        }

        RequestBody rBody = formBuilder.build();
        Request request = new Request.Builder()
                .url(url)
                .post(rBody)
                .build();
        ILog.e("http", "http请求--" + request.toString());

        Call call = mHttpClient.newCall(request);
        return call.execute();
    }

    public static void get(String url, HashMap<String, String> param, Callback callback) {
        if (!TextUtils.isEmpty(LoginInfo.getAccess_token())) {
            param.put("token", LoginInfo.getAccess_token());
        }
        String urlP = url + CreatString(param);
        Request request = new Request.Builder()
                .url(urlP)
                .get()
                .build();
        ILog.e("http", "http请求--" + request.toString());
        Call call = mHttpClient.newCall(request);
        call.enqueue(callback);
    }

    public static Response getSync(String url, HashMap<String, String> param) throws IOException {
        if (!TextUtils.isEmpty(LoginInfo.getAccess_token())) {
            param.put("token", LoginInfo.getAccess_token());
        }
        String urlP = url + CreatString(param);
        Request request = new Request.Builder()
                .url(urlP)
                .get()
                .build();
        ILog.e("http", "http请求--" + request.toString());
        Call call = mHttpClient.newCall(request);
        return call.execute();
    }


    public static void uploadFile(String url, HashMap<String, Object> map, File file, Callback callback) {
        if (!TextUtils.isEmpty(LoginInfo.getAccess_token())) {
            url = url + "?token=" + LoginInfo.getAccess_token();
        }
        MultipartBody.Builder multipartBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        if (file != null) {
            // MediaType.parse() 里面是上传的文件类型。
            RequestBody body = RequestBody.create(MediaType.parse("image/*"), file);
            String filename = file.getName();
            // 参数分别为， 请求key ，文件名称 ， RequestBody
            multipartBuilder.addFormDataPart("headImage", filename, body);
        }
        if (map != null) {
            // map 里面是请求中所需要的 key 和 value
            for (Map.Entry entry : map.entrySet()) {
                multipartBuilder.addFormDataPart(String.valueOf(entry.getKey()), String.valueOf(entry.getValue()));
            }
        }
        RequestBody rBody = multipartBuilder.build();
        Request request = new Request.Builder()
                .url(url)
                .post(rBody)
                .build();
        // readTimeout("请求超时时间" , 时间单位);
        Call call = mHttpClientPic.newCall(request);
        call.enqueue(callback);
    }


    //上传图片
    public static Response uploadImage(String url, HashMap<String, Object> map, File file) throws IOException {
        if (!TextUtils.isEmpty(LoginInfo.getAccess_token())) {
            url = url + "?token=" + LoginInfo.getAccess_token();
        }
        MultipartBody.Builder multipartBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        if (file != null) {
            // MediaType.parse() 里面是上传的文件类型。
            String mimeTypeName = FileUtil.getFileMimeType(file);
            MediaType mediaType = null;
            if (!TextUtils.isEmpty(mimeTypeName)) {
                mediaType = MediaType.parse(mimeTypeName);

                RequestBody body = RequestBody.create(mediaType, file);
                String filename = file.getName();
                // 参数分别为， 请求key ，文件名称 ， RequestBody
                multipartBuilder.addFormDataPart("fileData", filename, body);
            }
        }
        if (map != null) {
            // map 里面是请求中所需要的 key 和 value
            for (Map.Entry entry : map.entrySet()) {
                multipartBuilder.addFormDataPart(String.valueOf(entry.getKey()), String.valueOf(entry.getValue()));
            }
        }
        RequestBody rBody = multipartBuilder.build();
        Request request = new Request.Builder()
                .url(url)
                .post(rBody)
                .build();
        // readTimeout("请求超时时间" , 时间单位);
        Call call = mHttpClientPic.newCall(request);
        return call.execute();
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
}

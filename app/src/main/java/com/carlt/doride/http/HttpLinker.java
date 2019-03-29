package com.carlt.doride.http;


import android.text.TextUtils;
import android.util.Log;

import com.carlt.doride.model.LoginInfo;
import com.carlt.doride.systemconfig.URLConfig;
import com.carlt.doride.utils.FileUtil;
import com.carlt.doride.utils.ILog;
import com.carlt.sesame.preference.TokenInfo;
import com.google.gson.Gson;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.AbstractHttpEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

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
            .sslSocketFactory(createSSLSocketFactory())
            .hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            })
            .build();

    private static OkHttpClient mHttpClientPic = new OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            //            .addInterceptor(new LogPicInterceptor())
            .build();


    private static SSLSocketFactory createSSLSocketFactory() {
        SSLSocketFactory ssfFactory = null;

        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, new TrustManager[]{new TrustAllCerts()}, new SecureRandom());

            ssfFactory = sc.getSocketFactory();
        } catch (Exception e) {
        }

        return ssfFactory;
    }

    public static class TrustAllCerts implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }

    public static void post(String url, HashMap<String, String> param, Callback callback) {
        param.put("client_id", URLConfig.getClientID());
        if (!TextUtils.isEmpty(TokenInfo.getToken())) {
            param.put("token", TokenInfo.getToken());
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
//        MediaType mediaType = MediaType.parse("application/json;charset=utf-8");
//        RequestBody rBody = RequestBody.create(mediaType, new Gson().toJson(param));
        Request request = new Request.Builder()
//                .header("Content-Type", "application/json")
//                .header("Carlt-Access-Id", "18644515396614518644")
                .url(url)
                .post(rBody)
                .build();
        ILog.e("http", "http请求---------参数-----" + request.toString());

        Call call = mHttpClient.newCall(request);
        call.enqueue(callback);
    }


    public static Response postSync(String url, HashMap<String, String> param) throws IOException {
        if (!TextUtils.isEmpty(TokenInfo.getToken())) {
            url = url + "?token=" + TokenInfo.getToken();
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
        if (!TextUtils.isEmpty(TokenInfo.getToken())) {
            param.put("token", TokenInfo.getToken());
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
        if (!TextUtils.isEmpty(TokenInfo.getToken())) {
            param.put("token", TokenInfo.getToken());
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
        if (!TextUtils.isEmpty(TokenInfo.getToken())) {
            url = url + "?token=" + TokenInfo.getToken();
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
        if (!TextUtils.isEmpty(TokenInfo.getToken())) {
            url = url + "?token=" + TokenInfo.getToken();
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


    public boolean connect(String url, String post, int timeOutType) {
        boolean result = false;
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url);
            HttpParams params = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(params, 30000); // 设置连接超时
            HttpConnectionParams.setSoTimeout(params, 30000); // 设置请求超时
            httppost.setParams(params);
            AbstractHttpEntity entity = new StringEntity(post, "UTF-8");
            entity.setContentType("application/x-www-form-urlencoded");

            try {
                String[] sreq = EntityUtils.toString(entity, "UTF-8")
                        .split("&");
                for (int a = 0; a < sreq.length; a++) {
                    Log.e("http", "Http请求--参数" + a + ":::" + sreq[a]);
                }
            } catch (Exception e) {

            }
            httppost.setEntity(entity);
            HttpResponse response = httpclient.execute(httppost);

            HttpEntity mEntity = response.getEntity();
            // 检验状态码，如果成功接收数据

        } catch (Exception e) {
            Log.e("info", "HttpPostor--connect--e==" + e);
        }
        return result;
    }
}

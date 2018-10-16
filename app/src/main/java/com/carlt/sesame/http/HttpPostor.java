package com.carlt.sesame.http;

import com.carlt.sesame.utility.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.AbstractHttpEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import java.io.InputStream;

public class HttpPostor {
	public final static int CONNECT_OK = 200;

	public final static String ERRO = "网络不稳定，请稍后再试";
	private HttpEntity mEntity;
	public final static int LONG = 1;
	public final static int SHORT = 2;

	public boolean connect(String url, String post) {
		return connect(url, post, SHORT);
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
			Log.e("http",
					"Http请求--" + url + "?"
							+ EntityUtils.toString(entity, "UTF-8"));
			try {
				String[] sreq = EntityUtils.toString(entity, "UTF-8")
						.split("&");
				for (int a = 0; a < sreq.length; a++) {
					Log.e("http", "Http请求--参数" + a + ":::" + sreq[a]);
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
			httppost.setEntity(entity);
			HttpResponse response = httpclient.execute(httppost);
			code = response.getStatusLine().getStatusCode();
			// 检验状态码，如果成功接收数据
			if (code == CONNECT_OK) {
				mEntity = response.getEntity();
				result = true;
			} else {
				Log.e("info", "HttpPostor--connect--e==" + code + "错误");
			}
		} catch (Exception e) {
			Log.e("info", "HttpPostor--connect--e=="+e);
		}
		return result;
	}
	
	public boolean connect_get(String url, String post) {
		boolean result = false;
		try {
			HttpClient httpclient = new DefaultHttpClient();
//			HttpPost httppost = new HttpPost(url);
			HttpGet httpGet = new HttpGet(url + "?" + post);
			HttpParams params = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(params, 30000); // 设置连接超时
			HttpConnectionParams.setSoTimeout(params, 30000); // 设置请求超时
//			httppost.setParams(params);
			httpGet.setParams(params);
//			AbstractHttpEntity entity = new StringEntity(post, "UTF-8");
//			entity.setContentType("application/x-www-form-urlencoded");
//			Log.e("http",
//					"Http请求--" + url + "?"
//							+ EntityUtils.toString(entity, "UTF-8"));
//			try {
//				String[] sreq = EntityUtils.toString(entity, "UTF-8")
//						.split("&");
//				for (int a = 0; a < sreq.length; a++) {
//					Log.e("http", "Http请求--参数" + a + ":::" + sreq[a]);
//				}
//			} catch (Exception e) {
//				// TODO: handle exception
//			}
			HttpResponse response = httpclient.execute(httpGet);
			code = response.getStatusLine().getStatusCode();
			// 检验状态码，如果成功接收数据
			if (code == CONNECT_OK) {
				mEntity = response.getEntity();
				result = true;
			} else {
				Log.e("info", "HttpPostor--connect--e==" + code + "错误");
			}
		} catch (Exception e) {
			Log.e("info", "HttpPostor--connect--e=="+e);
		}
		return result;
	}

	private int code = 0;

	public int getCode() {
		return code;
	}

	public String getResult() {
		String result = null;
		try {
			result = EntityUtils.toString(mEntity, "UTF-8");
		} catch (Exception e) {
			Log.e("info", "HttpPostor--getResult--e==");
		}
		return result;
	}

	public InputStream getInputStream() {

		InputStream result = null;
		try {
			result = mEntity.getContent();
		} catch (Exception e) {
			Log.e("info", "HttpPostor--getInputStream--e==");
		}
		return result;
	}
}

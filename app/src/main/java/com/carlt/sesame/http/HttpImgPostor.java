package com.carlt.sesame.http;

import com.carlt.doride.http.retrofitnet.model.UserInfo;
import com.carlt.sesame.data.SesameLoginInfo;
import com.carlt.sesame.preference.TokenInfo;
import com.carlt.sesame.systemconfig.URLConfig;
import com.carlt.sesame.utility.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.InputStream;

public class HttpImgPostor {
	public final static int CONNECT_OK = 200;

	public final static String ERRO = "网络连接失败";
	private HttpEntity mResultEntity;

	public boolean connect(String url, String action, ContentBody ContentBody) {
		boolean result = false;
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(url);

			MultipartEntity mEntity = new MultipartEntity();
			mEntity.addPart("client_id",
					new StringBody(URLConfig.getClientID()));
			if (UserInfo.getInstance().dealerId != 0 ) {
				mEntity.addPart("dealerId", new StringBody(UserInfo.getInstance().dealerId+""));
			}

			mEntity.addPart("token", new StringBody(TokenInfo.getToken()));
			mEntity.addPart("fileOwner", new StringBody(action));
			mEntity.addPart("fileData", ContentBody);
			httppost.setEntity(mEntity);
			Log.e("http", "Http请求--" + url);
			Log.e("http", "fileOwner--"+action);
			HttpResponse response = httpclient.execute(httppost);
			int code = response.getStatusLine().getStatusCode();
			// 检验状态码，如果成功接收数据
			if (code == CONNECT_OK) {
				mResultEntity = response.getEntity();
				result = true;
			} else {
				Log.e("info", "HttpPostor--connect--e==" + code + "错误");
			}
		} catch (Exception e) {
			Log.e("info", "HttpPostor--connect--e==");
		}
		return result;
	}

	public String getResult() {
		String result = null;
		try {
			result = EntityUtils.toString(mResultEntity, "UTF-8");
		} catch (Exception e) {
			Log.e("info", "HttpPostor--getResult--e==");
		}
		return result;
	}

	public InputStream getInputStream() {

		InputStream result = null;
		try {
			result = mResultEntity.getContent();
		} catch (Exception e) {
			Log.e("info", "HttpPostor--getInputStream--e==");
		}
		return result;
	}
}

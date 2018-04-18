package com.carlt.yema.http;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.util.Vector;

/**
 * 作者：秋良
 * 
 * 描述：Http连接工具类
 * 
 * 规约：所有关于Http连接请求应调用此类方法执行，或继承此类。 以便Http请求能够集中处理
 */
public class HttpConnector {
	public static String LOG_TAG = HttpConnector.class.getSimpleName();

	/**
	 * 代理
	 */
	private Proxy proxy = null;

	/**
	 * 连接成功的返回值
	 */
	public final static int CONNECT_OK = 0;

	/**
	 * 连接失败时的返回值
	 */
	public final static int CONNECT_UNAVAILABLE = 1;

	/**
	 * 连接暂时不可用时的返回值
	 */
	public final static int CONNECT_TEMPORARY_UNAVAILABLE = 2;

	/**
	 * 连接出错返回值
	 */
	public final static int CONNECT_EXCEPTION = 3;

	/**
	 * 未连接上服务器
	 */
	public final static int CONNECT_UNCONNECT = 4;

	/**
	 * 保存请求属性的Vector
	 */
	private final Vector<String[]> propertys = new Vector<String[]>();

	/**
	 * 请求方式
	 */
	private String requestMethod = METHOD_GET;

	/**
	 * GET请求方法
	 */
	public final static String METHOD_GET = "GET";

	/**
	 * POST请求方法
	 */
	public final static String METHOD_POST = "POST";

	/**
	 * 返回值
	 */
	private int responseCode = -1;

	/**
	 * HttpConnection类对象
	 */
	private HttpURLConnection connection = null;

	private int timeout = -1;

	private int retryNum = 3;

	public HttpConnector() {
		// 构造方法
	}

	/**
	 * 连接(断点续传)
	 * 
	 * @param url
	 *            链接地址
	 * @param data
	 *            post数据
	 * @param startPos
	 *            请求数据起始点
	 * @param endPos
	 *            请求数据终点
	 * @return 连接结果
	 */
	public int connect(String url, byte[] data, String startPos, String endPos) {

		int result = -1;
		try {
			for (int i = 0; i < retryNum; i++) {
				URL myURL = new URL(url);
				if (proxy != null) {
					connection = (HttpURLConnection) myURL
							.openConnection(proxy);
				} else
					connection = (HttpURLConnection) myURL.openConnection();
				if (connection == null) {
					result = CONNECT_UNAVAILABLE;
					return result;
				}

				// 设置连接时长
				if (timeout > 0) {
					connection.setReadTimeout(timeout);
					connection.setConnectTimeout(timeout);
				}
				// 设置HTTP请求头信息
				int size = propertys.size();
				for (int d = 0; d < size; d++) {
					String[] temp = propertys.elementAt(d);
					connection.setRequestProperty(temp[0], temp[1]);
				}
				if (startPos != null) {
					StringBuffer sProperty = new StringBuffer();
					sProperty.append("bytes=");
					sProperty.append(startPos);
					if (endPos != null) {
						sProperty.append("-");
						sProperty.append(endPos);
					}
					connection
							.setRequestProperty("RANGE", sProperty.toString());
				}
				connection.setRequestProperty("Accept", "*/*");
				// 设置HTTP请求方法
				connection.setRequestMethod(requestMethod);
				// post数据
				if (requestMethod.equals(METHOD_POST) && data != null) {
					connection.setDoInput(true);
					connection.setDoOutput(true);
					connection.setUseCaches(false);
					connection.setInstanceFollowRedirects(true);
					connection.setRequestProperty("Content-Type",
							"application/x-gzip");
					connection.setRequestProperty("Content-Length",
							Integer.toString(data.length));
					this.sendData(data);

				}
				// 获取返回值
				try {
					responseCode = connection.getResponseCode();
				} catch (IOException e1) {
					e1.printStackTrace();
				}

				if (responseCode == -1) {
					connection.disconnect();
					connection = null;
					result = CONNECT_UNCONNECT;
					return result;
				}
				// 判断是否连接成功
				if (responseCode == HttpURLConnection.HTTP_OK
						|| responseCode == HttpURLConnection.HTTP_PARTIAL) {
					result = CONNECT_OK;
					return result;
				} else if (responseCode == HttpURLConnection.HTTP_MOVED_TEMP
						|| responseCode == HttpURLConnection.HTTP_MOVED_PERM) {// 跳转

				} else if (responseCode == HttpURLConnection.HTTP_FORBIDDEN
						|| responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
					// 这种情况表示服务不可用，无需重试
					connection.disconnect();
					connection = null;
					result = CONNECT_UNAVAILABLE;
					return result;
				} else {
					// 这种情况表示服务暂时不可用，调用者可根据情况进行重试
					connection.disconnect();
					connection = null;
					result = CONNECT_TEMPORARY_UNAVAILABLE;
					return result;
				}
			}
		} catch (Exception e) {
			Log.e("info", "e=="+e);
			result = CONNECT_EXCEPTION;
		}
		return result;
	}

	/**
	 * 连接(断点续传)
	 * 
	 * @param url
	 * @return 连接结果
	 */
	public int connect(String url, byte[] data) {
		return connect(url, data, null, null);
	}

	public void setProxy(Proxy proxy) {
		this.proxy = proxy;
	}

	/**
	 * 发送数据
	 */
	private void sendData(byte[] data) {
		if (connection != null && data.length > 0) {
			try {
				OutputStream os = connection.getOutputStream();
				os.write(data);
				os.flush();
				os.close();
				os = null;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 获取数据长度
	 * 
	 * @return
	 */
	public long getDataLength() {
		long length = -1;
		if (connection != null) {
			length = connection.getContentLength();
		}
		return length;
	}

	/**
	 * 获取返回值
	 * 
	 * @return
	 */
	public int getResponseCode() {
		return responseCode;
	}

	public String getHeaderField(String key) {
		if (connection != null) {
			return connection.getHeaderField(key);
		}
		return null;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	/**
	 * 设置请求属性(头信息等)
	 * 
	 * @param key
	 * @param value
	 */
	public void setRequestProperty(String key, String value) {
		propertys.addElement(new String[] { key, value });
	}

	/**
	 * 设置请求方法
	 * 
	 * @param method
	 */
	public void setRequestMethod(String method) {
		this.requestMethod = method;
	}

	/**
	 * 关闭连接
	 */
	public void disconnect() {
		if (connection != null) {
			connection.disconnect();
			connection = null;
		}
	}

	/**
	 * 获取HttpConnection上的InputStream
	 * 
	 * @return
	 */
	public InputStream getInputStream() {
		InputStream is = null;
		if (connection != null) {
			try {
				is = connection.getInputStream();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return is;
	}

	/**
	 * 获取HttpConnection上的OutputStream
	 * 
	 * @return
	 */
	public OutputStream getOutputStream() {
		OutputStream os = null;
		if (connection != null) {
			try {
				os = connection.getOutputStream();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return os;
	}

	/**
	 * 完整的获取数据
	 * 
	 * @return
	 */
	public byte[] getData() {
		byte[] data = null;
		if (connection != null) {
			try {
				InputStream is = getInputStream();
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				byte[] temp = new byte[512];
				int i = 0;
				while ((i = is.read(temp)) != -1) {
					baos.write(temp, 0, i);
				}
				data = baos.toByteArray();
				baos.close();
				is.close();
				is = null;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return data;
	}

	/**
	 * 获取代理
	 */

	public static Proxy getProxy(Context context) {
		try {
			ConnectivityManager conManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo network = conManager.getActiveNetworkInfo();
			if (network != null
					&& network.getType() == ConnectivityManager.TYPE_WIFI) {
				return null;
			}
			String proxyHost = android.net.Proxy.getDefaultHost();

			// 代理模式
			if (proxyHost != null && !proxyHost.equals("")) {
				Proxy p = new Proxy(java.net.Proxy.Type.HTTP,
						new InetSocketAddress(
								android.net.Proxy.getDefaultHost(),
								android.net.Proxy.getDefaultPort()));
				return p;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}

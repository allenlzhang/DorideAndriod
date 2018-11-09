package com.carlt.chelepie.download;

import com.carlt.doride.http.HttpConnector;
import java.io.InputStream;

public class HttpDownloadThread extends DownloadBaseThread {

	public HttpDownloadThread(DownloadBaseInfo mDownloadBaseInfo, OnDownloadListner downloadListner) {
		super(mDownloadBaseInfo, downloadListner);
	}

	@Override
	public InputStream requestData(String URL, long startPos) {
		HttpConnector hc = new HttpConnector();

		int state = hc.connect(URL, null, startPos + "", mDownloadBaseInfo.getEndPos());
		InputStream data = null;
		if (state != HttpConnector.CONNECT_OK) {
			mDownloadBaseInfo.setStatus(DownloadBaseInfo.STATUS_UNLOAD);
			mDownloadListner.onStart(mDownloadBaseInfo);
			mDownloadListner.onFailed("网络出了点问题哦，请检查您的网络");
		} else {
			data = hc.getInputStream();
			totalLength = hc.getDataLength() + haveDownSize;
			mDownloadBaseInfo.setLengthTotal(totalLength + "");
		}

		return data;

	}

}

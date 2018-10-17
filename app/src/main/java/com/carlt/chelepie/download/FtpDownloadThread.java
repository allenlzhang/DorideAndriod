
package com.carlt.chelepie.download;


import com.carlt.chelepie.ftp.FtpConnector;
import com.carlt.doride.utils.FileUtil;
import com.carlt.doride.utils.LocalConfig;

import java.io.InputStream;
import java.util.concurrent.CountDownLatch;

public class FtpDownloadThread extends DownloadBaseThread {

    private FtpConnector mFtpConnector;

    public FtpDownloadThread(DownloadBaseInfo mDownloadBaseInfo, DownloadBaseThread.OnDownloadListner downloadListner,
                             String pathPrefix) {
        super(mDownloadBaseInfo, downloadListner);
        FileUtil.openOrCreatDir(LocalConfig.mMediaFileSavePath_Absolute + pathPrefix);
        FileUtil.openOrCreatDir(LocalConfig.mMediaFileSavePath_SD + pathPrefix);
        setAbsolutePath(LocalConfig.mMediaFileSavePath_Absolute + pathPrefix);
        setSDPath(LocalConfig.mMediaFileSavePath_SD + pathPrefix);
        this.mFtpConnector = new FtpConnector();

    }

    @Override
    public InputStream requestData(String URL, long startPos) {
        InputStream data = null;
        if (mFtpConnector != null) {

            int code = mFtpConnector.openConnect();
            if (code == FtpConnector.CODE_SUCCESS) {
                totalLength = mFtpConnector.getFileSize(URL);
                data = mFtpConnector.downloadFile(URL, startPos);
            } else {
                mDownloadBaseInfo.setStatus(DownloadBaseInfo.STATUS_UNLOAD);
                mDownloadListner.onStatusChanged(mDownloadBaseInfo);
                mDownloadListner.onFailed("网络出了点问题哦，请检查您的网络");
                return null;
            }
        }
        return data;
    }

    @Override
    public void run() {
        super.run();

        if (latch != null) {
            latch.countDown();
        }
    }

    private CountDownLatch latch;

    public void setLatch(CountDownLatch latch) {
        this.latch = latch;
    }

}

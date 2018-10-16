
package com.carlt.doride.http;

import com.carlt.doride.preference.DownloadInfo;
import com.carlt.doride.utils.FileUtil;
import com.carlt.doride.utils.LocalConfig;
import com.carlt.doride.utils.Log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

public class FileDownloadThread implements Runnable {

    private String apkUrl;// 下载URL

    private long haveDownSize;// 已经下载的大小

    private long totalLength;// 下载文件的总大小

    private RandomAccessFile savedFile;

    private OnDownloadListner mDownloadListner;

    private boolean isStop;// 是否停止ֹ

    private boolean isFirstDown;// 是否是第一次下载

    private DownloadInfo mDownloadInfo;

    private final String SUFFIX = ".temp";

    public FileDownloadThread(String apkUrl, OnDownloadListner downloadListner) {
        this.apkUrl = apkUrl;
        mDownloadListner = downloadListner;
        mDownloadInfo = new DownloadInfo();
        String length = mDownloadInfo.getTotalLength();
        if (length != null && length.length() > 0) {
            totalLength = Long.parseLong(length);
        }
    }

    @Override
    public void run() {
        int tmp = apkUrl.lastIndexOf("/");
        if (tmp >= 0) {
            String fileURL = null;

            String tempFileURL_sdcard = LocalConfig.mDownLoadFileSavePath_SD
                    + apkUrl.substring(tmp + 1) + SUFFIX;

            String tempFileURL_internal = LocalConfig.mDownLoadFileSavePath_Absolute
                    + apkUrl.substring(tmp + 1) + SUFFIX;

            if (FileUtil.isExCardExist()) {
                // SD卡存在
                if (FileUtil.isExist(tempFileURL_sdcard)) {
                    // 存在.temp文件
                    fileURL = tempFileURL_sdcard;
                    haveDownSize = FileUtil.getFileLength(fileURL);
                    isFirstDown = false;
                } else {
                    // 都不存在
                    haveDownSize = 0;
                    isFirstDown = true;
                }

            } else {
                // SD卡不存在
                if (FileUtil.isExist(tempFileURL_internal)) {
                    // 存在.temp文件
                    fileURL = tempFileURL_internal;
                    haveDownSize = FileUtil.getFileLength(fileURL);
                    isFirstDown = false;
                } else {
                    // 都不存在
                    haveDownSize = 0;
                    isFirstDown = true;
                }
            }

            if (haveDownSize >= 0) {
                InputStream data = requestData(apkUrl, haveDownSize + "");
                if (null != data) {
                    int defaultPro = (int)(haveDownSize * 100 / totalLength);
                    mDownloadListner.onStarted(defaultPro);

                    if (FileUtil.isExCardExist()) {
                        // SD卡存在，优先保存在SD卡
                        fileURL = tempFileURL_sdcard;
                    } else {
                        // SD卡不存在，保存在内部存储器
                        fileURL = tempFileURL_internal;
                    }
                    try {
                        savedFile = new RandomAccessFile(fileURL, "rw");
                        byte[] buffer = new byte[1024];
                        int len = 0;
                        savedFile.seek(haveDownSize);
                        while ((len = data.read(buffer)) != -1 && haveDownSize < totalLength) {
                            if (isStop == false) {
                                savedFile.write(buffer, 0, len);
                                haveDownSize += len;
                                int progress = (int)(haveDownSize * 100 / totalLength);
                                mDownloadListner.onProgress(progress);
                                Log.e("info", "haveDownSize==" + haveDownSize);
                            } else {
                                break;
                            }
                        }

                        if (testFile(savedFile)) {
                            int pos = fileURL.lastIndexOf(".");
                            String newPath = fileURL.substring(0, pos);
                            File mFile = FileUtil.renameFile(fileURL, newPath);
                            if (mFile != null && mFile.length() > 0) {
                                mDownloadListner.onFinished(mFile);
                            }
                        }
                        savedFile.close();
                        data.close();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    try {
                        haveDownSize = savedFile.length();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }

        }
    }

    private InputStream requestData(final String URL, String startPos) {
        HttpConnector hc = new HttpConnector();
        hc.setTimeout(10000);

        int state = HttpConnector.CONNECT_UNAVAILABLE;
        state = hc.connect(URL, null, startPos + "", "");

        if (state != HttpConnector.CONNECT_OK) {
            mDownloadListner.onFailed("网络出了点问题哦，请检查您的网络");
            return null;
        }
        InputStream data = null;

        if (state == HttpConnector.CONNECT_OK) {
            data = hc.getInputStream();
            if (isFirstDown) {
                totalLength = hc.getDataLength();
                mDownloadInfo.setTotalLength(totalLength + "");
            }
        }

        return data;

    }

    /**
     * 已下载文件完整性验证
     * 
     * @param file
     * @return
     */
    private boolean testFile(RandomAccessFile file) {
        boolean isComplete = false;

        try {
            Log.e("info", "file.length==" + file.length());
            if (file.length() == totalLength) {
                isComplete = true;
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return isComplete;
    }

    public boolean isStop() {
        return isStop;
    }

    public void setStop(boolean isStop) {
        this.isStop = isStop;
    }

    public interface OnDownloadListner {
        void onStarted(int defaultPro);// 下载开始

        void onProgress(int progress);// 下载中

        void onFinished(File file);// 下载完成

        void onFailed(String error);// 下载出错
    };

}

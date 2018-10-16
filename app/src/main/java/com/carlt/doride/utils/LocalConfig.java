
package com.carlt.doride.utils;

import android.os.Environment;

import com.carlt.doride.DorideApplication;

public class LocalConfig {

    /**
     * 是否有SD卡信息获取
     * 
     * @return true
     */
    public static boolean hasSDCard() {
        boolean mHasSDcard = false;
        if (Environment.MEDIA_MOUNTED.endsWith(Environment.getExternalStorageState())) {
            // UULog.i(LOG_TAG, "Has SDcard");
            mHasSDcard = true;
        } else {
            // UULog.i(LOG_TAG, "No SDcard");
            mHasSDcard = false;
        }

        return mHasSDcard;
    }

    /**
     * 记录图片资源保存PATH SD卡
     */
    public static String mImageCacheSavePath_SD = Environment.getExternalStorageDirectory()
            .toString() + "/doride/img/";

    /**
     * 记录图片资源保存PATH 内部存储器
     */
    public static String mImageCacheSavePath_Absolute = DorideApplication.getInstanse()
            .getCacheDir().getAbsolutePath() + "/doride/img/";

    /**
     * 记录下载文件保存PATH SD卡
     */
    public static String mDownLoadFileSavePath_SD = Environment.getExternalStorageDirectory()
            .toString() + "/doride/down/";

    /**
     * 记录下载文件保存PATH 内部存储器
     */
    public static String mDownLoadFileSavePath_Absolute = DorideApplication.getInstanse()
            .getCacheDir().getAbsolutePath() + "/doride/down/";

    /**
     * 记录下载文件保存PATH SD卡
     */
    public static String mErroLogSavePath_SD = Environment.getExternalStorageDirectory().toString()
            + "/doride/log/";

    /**
     * 记录轨迹文件保存PATH SD卡
     */
    public static String mTracksSavePath_SD = Environment
            .getExternalStorageDirectory().toString() + "/doride/tracks/";

    /**
     *  获取下载文件的路径
     * @param carListener 车牌号
     * @return
     */
    public static String GetDownLoadMediaPath(String carListener) {
        if (FileUtil.openOrCreatDir(mDownLoadFileSavePath_SD)) {
            String path = mDownLoadFileSavePath_SD + carListener + "/" ;
            if (FileUtil.openOrCreatDir(path)) {
                return path;
            }
        }

        if (FileUtil.openOrCreatDir(mDownLoadFileSavePath_Absolute)) {
            String path = mDownLoadFileSavePath_Absolute + carListener + "/";
            if (FileUtil.openOrCreatDir(path)) {
                return path;
            }
        }

        return null;
    }

}


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
     * 记录我的媒体库保存PATH SD卡
     */
    public static String mMediaFileSavePath_SD = Environment.getExternalStorageDirectory().toString() + "/ChelerPie/media/";

    /**
     * 记录我的媒体库保存PATH 内部存储器
     */
    public static String mMediaFileSavePath_Absolute = DorideApplication.ApplicationContext.getCacheDir().getAbsolutePath() + "/ChelerPie/media/";

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
    public static final String DIR_TEMP = "temp";// 剪裁目录名称
    public static final String DIR_CROP = "crop";// 剪裁目录名称
    public static final String DIR_CAPTURE = "capture";// 抓拍目录名称
    public static final String DIR_EVENT = "event";// 事件目录名称
    public static final String DIR_THUMBNAIL = "thumbnail";// 缩略图
    public static final String DIR_ALLLISTS = "all_lists";// 全天录制

    /**
     * 获取 车乐拍媒体路径
     *
     * @param account
     * @param type
     * @return null 代表 SDCard 存储有问题
     */
    public static String GetMediaPath(String account, String device, String type) {
        if (FileUtil.openOrCreatDir(mMediaFileSavePath_SD)) {
            String path = mMediaFileSavePath_SD + account + "/" + device + "/" + type + "/";
            if (FileUtil.openOrCreatDir(path)) {
                return path;
            }
        }

        if (FileUtil.openOrCreatDir(mMediaFileSavePath_Absolute)) {
            String path = mMediaFileSavePath_Absolute + account + "/" + device + "/" + type + "/";
            if (FileUtil.openOrCreatDir(path)) {
                return path;
            }
        }

        return null;
    }
    /**
     * 获取 车乐拍媒体上层路径（只用账号）
     *
     * @param account
     * @return null 代表 SDCard 存储有问题
     */
    public static String GetMediaPathMain(String account) {
        if (FileUtil.openOrCreatDir(mMediaFileSavePath_SD)) {
            String path = mMediaFileSavePath_SD + account + "/";
            if (FileUtil.openOrCreatDir(path)) {
                return path;
            }
        }

        if (FileUtil.openOrCreatDir(mMediaFileSavePath_Absolute)) {
            String path = mMediaFileSavePath_Absolute + account + "/";
            if (FileUtil.openOrCreatDir(path)) {
                return path;
            }
        }

        return null;
    }
}

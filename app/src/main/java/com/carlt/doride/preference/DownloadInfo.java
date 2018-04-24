
package com.carlt.doride.preference;

import android.content.Context;
import android.content.SharedPreferences;

import com.carlt.doride.DorideApplication;
import com.carlt.doride.utils.Log;

public class DownloadInfo {
    private final static String DOWNLOAD_INFO = "DOWNLOAD_INFO";

    private final static String TOTAL_LENGTH = "total_length";// 需要下载文件的总长度

    private static String totalLength;

    public String getTotalLength() {
        readConfig();
        return totalLength;
    }

    public void setTotalLength(String totalLength) {
        this.totalLength = totalLength;
        saveConfig();
    }

    public static boolean readConfig() {
        SharedPreferences sp = DorideApplication.ApplicationContext.getSharedPreferences(DOWNLOAD_INFO,
                Context.MODE_PRIVATE);
        if (null != sp) {
            try {
                totalLength = sp.getString(TOTAL_LENGTH, "");
                return true;
            } catch (Exception e) {
                Log.e("info", "e==" + e);
                e.printStackTrace();
            }
        }
        return false;
    }

    public static boolean saveConfig() {
        SharedPreferences sp = DorideApplication.ApplicationContext.getSharedPreferences(DOWNLOAD_INFO,
                Context.MODE_PRIVATE);
        if (null != sp) {
            SharedPreferences.Editor editor = sp.edit();
            if (null != editor) {
                editor.putString(TOTAL_LENGTH, totalLength);
                editor.commit();
                return true;
            }
        }
        return false;
    }

}

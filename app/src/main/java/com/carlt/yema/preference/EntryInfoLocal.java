
package com.carlt.yema.preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.carlt.yema.YemaApplication;

/**
 * 用户进入胎压主页信息统计
 * 
 * @author daisy
 */
public class EntryInfoLocal {
    private static final String ENTRY_INFO = "entry_info";// 进入信息
    
    private static int entryTimes;
    
    private static String userId;
    
    public static int getEntryTimes(String userId) {
        EntryInfoLocal.userId=userId;
        readConfig();
        return entryTimes;
    }

    public static void setEntryTimes(int entryTimes) {
        EntryInfoLocal.entryTimes = entryTimes;
        saveConfig();
    }

    private static boolean readConfig() {
        SharedPreferences sp = YemaApplication.getInstanse().getSharedPreferences(ENTRY_INFO,
                Context.MODE_PRIVATE);

        if (sp != null) {
            try {
                entryTimes = sp.getInt(userId, 0);

                return true;
            } catch (Exception e) {
                Log.e("info", EntryInfoLocal.class.getName() + "_e==" + e);
                e.printStackTrace();
            }
        }
        return false;
    }

    private static boolean saveConfig() {
        SharedPreferences sp = YemaApplication.getInstanse().getSharedPreferences(ENTRY_INFO,
                Context.MODE_PRIVATE);

        if (sp != null) {
            SharedPreferences.Editor editor = sp.edit();
            if (editor != null) {
                try {
                    editor.putInt(userId, entryTimes);
                    editor.apply();
                    return true;
                } catch (Exception e) {
                    Log.e("info", EntryInfoLocal.class.getName() + "_e==" + e);
                    e.printStackTrace();
                }
            }
        }

        return false;
    }
}

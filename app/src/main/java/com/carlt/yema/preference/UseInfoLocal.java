
package com.carlt.yema.preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.carlt.yema.YemaApplication;
import com.carlt.yema.data.UseInfo;


/**
 * 用户使用App信息
 * 
 * @author daisy
 */
public class UseInfoLocal {
    private final static String USE_INFO = "use_info";

    private final static String TIMES = "times";// 登录次数

    private final static String ACCOUNT = "account";// 登录账号

    private final static String PASSWORD = "password";// 登录密码
    
    private static UseInfo useInfo = new UseInfo();

    public static UseInfo getUseInfo() {
        readConfig();
        return useInfo;
    }

    public static void setUseInfo(UseInfo useInfo) {
        UseInfoLocal.useInfo = useInfo;
        saveConfig();
    }

    private static boolean readConfig() {
        SharedPreferences sp = YemaApplication.getInstanse().getSharedPreferences(USE_INFO,
                Context.MODE_PRIVATE);
        if (null != sp) {
            try {
                int times = sp.getInt(TIMES, 0);
                String account = sp.getString(ACCOUNT, "");
                String password = sp.getString(PASSWORD, "");

                useInfo.setTimes(times);
                useInfo.setAccount(account);
                useInfo.setPassword(password);
                return true;
            } catch (Exception e) {
                Log.e("info", "e==" + e);
                e.printStackTrace();
            }
        }
        return false;
    }

    private static boolean saveConfig() {
        SharedPreferences sp = YemaApplication.getInstanse().getSharedPreferences(USE_INFO,
                Context.MODE_PRIVATE);
        if (null != sp && useInfo != null) {
            SharedPreferences.Editor editor = sp.edit();
            if (null != editor) {
                editor.putInt(TIMES, useInfo.getTimes());
                editor.putString(ACCOUNT, useInfo.getAccount());
                editor.putString(PASSWORD, useInfo.getPassword());
                editor.apply();
                return true;
            }
        }
        return false;
    }

}

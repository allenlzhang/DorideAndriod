
package com.carlt.sesame.preference;

import android.content.Context;
import android.content.SharedPreferences;

import com.carlt.doride.DorideApplication;
import com.carlt.sesame.utility.Log;

/**
 * 用户远程密码信息
 * 
 * @author Administrator
 */
public class TokenInfo {
    private final static String TOKEN = "token";

    private static String token = "";

    private final static String VALUE = "value";

    public static String getToken() {
        readConfig();
        return token;
    }

    public static void setToken(String token) {
        TokenInfo.token = token;
        saveConfig(token);
    }

    private static boolean readConfig() {
        SharedPreferences sp = DorideApplication.ApplicationContext.getSharedPreferences(TOKEN,
                Context.MODE_PRIVATE);
        if (null != sp) {
            try {
                String mToken = sp.getString(VALUE, "");
                token = mToken;
                return true;
            } catch (Exception e) {
                Log.e("info", "e==" + e);
                e.printStackTrace();
            }
        }
        return false;
    }

    private static boolean saveConfig(String value_token) {
        SharedPreferences sp = DorideApplication.ApplicationContext.getSharedPreferences(TOKEN,
                Context.MODE_PRIVATE);
        if (null != sp) {
            SharedPreferences.Editor editor = sp.edit();
            if (null != editor) {
                editor.putString(VALUE, value_token);
                editor.commit();
                return true;
            }
        }
        return false;
    }
}


package com.carlt.sesame.preference;

import android.content.Context;
import android.content.SharedPreferences;

import com.carlt.doride.DorideApplication;
import com.carlt.sesame.data.VisitorInfo;
import com.carlt.sesame.utility.DesCoderUtil;
import com.carlt.sesame.utility.Log;

/**
 * 用户使用App信息
 * 
 * @author daisy
 */
public class UseInfoVisitor {
    private final static String USE_VISITOR = "use_visitor";

    private final static String TIMES = "times";// 登录次数

    private final static String ACCOUNT = "account_visitor";// 登录账号

    private final static String PASSWORD = "password_visitor";// 登录密码

    private final static String PASSWORD_REMOTE = "password__visitor_remote";// 远程密码

    private static VisitorInfo visitorInfo = new VisitorInfo();

    public static VisitorInfo getVisitorInfo() {
        readConfig();
        return visitorInfo;
    }

    public static void setVisitorInfo(VisitorInfo misitorInfo) {
        UseInfoVisitor.visitorInfo = visitorInfo;
        saveConfig();
    }

    private static boolean readConfig() {
        SharedPreferences sp = DorideApplication.ApplicationContext.getSharedPreferences(USE_VISITOR,
                Context.MODE_PRIVATE);
        if (null != sp) {
            try {
                int times = sp.getInt(TIMES, 0);
                String accountDes = sp.getString(ACCOUNT, "");
                String passwordDes = sp.getString(PASSWORD, "");
                String passwordRemoteDes = sp.getString(PASSWORD_REMOTE, "");

                String account = DesCoderUtil.decryptStr(accountDes, null);
                String password = DesCoderUtil.decryptStr(passwordDes, null);
                String passwordRemote= DesCoderUtil.decryptStr(passwordRemoteDes, null);
                visitorInfo.setTimes(times);
                visitorInfo.setAccount(account);
                visitorInfo.setPassword(password);
                visitorInfo.setPasswordRemote(passwordRemote);
                return true;
            } catch (Exception e) {
                Log.e("info", "e==" + e);
                e.printStackTrace();
            }
        }
        return false;
    }

    private static boolean saveConfig() {
        SharedPreferences sp = DorideApplication.ApplicationContext.getSharedPreferences(USE_VISITOR,
                Context.MODE_PRIVATE);
        if (null != sp && visitorInfo != null) {
            SharedPreferences.Editor editor = sp.edit();
            if (null != editor) {
                String account = visitorInfo.getAccount();
                String accountDes = DesCoderUtil.encryptStr(account, null);
                String password = visitorInfo.getPassword();
                String passwordDes = DesCoderUtil.encryptStr(password, null);
                String passwordRemote = visitorInfo.getPassword();
                String passwordRemoteDes = DesCoderUtil.encryptStr(passwordRemote, null);
                
                editor.putInt(TIMES, visitorInfo.getTimes());
                editor.putString(ACCOUNT, accountDes);
                editor.putString(PASSWORD, passwordDes);
                editor.putString(PASSWORD_REMOTE, passwordRemoteDes);
                editor.commit();
                return true;
            }
        }
        return false;
    }

}

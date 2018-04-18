package com.carlt.yema.preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.carlt.yema.YemaApplication;
import com.carlt.yema.data.UseInfo;
import com.carlt.yema.utils.FileUtil;

/**
 * 用户远程密码信息
 * 
 * @author Administrator
 *
 */
public class RemotePswInfo {
    private final static String REMOTE_PSW="remote_psw";
    
    private static String remotePswMd5="";
    
    public static String getRemotePswMd5() {
        UseInfo mUseInfo=UseInfoLocal.getUseInfo();
        if(mUseInfo!=null){
            String key_account=mUseInfo.getAccount();
            readConfig(key_account);
        }
        return remotePswMd5;
    }

    public static void setRemotePsw(String remotePsw) {
        String remotePswMd5= FileUtil.stringToMD5(remotePsw);
        RemotePswInfo.remotePswMd5 = remotePswMd5;
        UseInfo mUseInfo=UseInfoLocal.getUseInfo();
        if(mUseInfo!=null){
            String key_account=mUseInfo.getAccount();
            saveConfig(key_account,remotePswMd5);
        }
    }

    private static boolean readConfig(String key_account) {
        SharedPreferences sp = YemaApplication.getInstanse().getSharedPreferences(REMOTE_PSW,
                Context.MODE_PRIVATE);
        if (null != sp) {
            try {
                String password_remote=sp.getString(key_account, "");

                remotePswMd5=password_remote;
                return true;
            } catch (Exception e) {
                Log.e("info", "e==" + e);
                e.printStackTrace();
            }
        }
        return false;
    }
    
    private static boolean saveConfig(String key_account, String value_psw) {
        SharedPreferences sp = YemaApplication.getInstanse().getSharedPreferences(REMOTE_PSW,
                Context.MODE_PRIVATE);
        if (null != sp) {
            SharedPreferences.Editor editor = sp.edit();
            if (null != editor) {
                editor.putString(key_account, value_psw);
                editor.apply();
                return true;
            }
        }
        return false;
    }
}

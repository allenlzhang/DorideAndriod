package com.carlt.doride.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import com.carlt.doride.DorideApplication;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import io.reactivex.annotations.NonNull;

public class SharepUtil {
    public static SharedPreferences preferences = DorideApplication.getAppContext().getSharedPreferences("Doride", Context.MODE_PRIVATE);


    // SP 保存实体类
    public static <T> boolean putByBean(String key, T t) {

        ByteArrayOutputStream bos;
        ObjectOutputStream oos = null;
        try {
            bos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(bos);
            oos.writeObject(t);
            byte[] bytes = bos.toByteArray();
            String ObjStr = Base64.encodeToString(bytes, Base64.DEFAULT);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(key, ObjStr);
            editor.apply();
            return true;
        } catch (IOException e) {

            e.printStackTrace();
            return false;
        } finally {
            if (oos != null) {
                try {
                    oos.flush();
                    oos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
            }
        }
    }

    public static <T extends Object> T getBeanFromSp(@NonNull String keyNme) {
        byte[] bytes = Base64.decode(preferences.getString(keyNme, ""), Base64.DEFAULT);
        ByteArrayInputStream bis;
        ObjectInputStream ois = null;
        T obj = null;
        try {
            bis = new ByteArrayInputStream(bytes);
            ois = new ObjectInputStream(bis);
            obj = (T) ois.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (ois != null) {
                try {
                    ois.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return obj;
    }


    public static void put( final String key, final String value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
    }
    public static void putInt( final String key, final int value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }
    public static void putBoolean( final String key, final Boolean value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }
    public static void putBoolean( String key, boolean value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static SharedPreferences getPreferences() {

        return preferences;
    }

    public static void cleanUser() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("token");
        editor.remove("user");
        editor.apply();
    }

    public static void cleanKey(String key){
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(key);
        editor.apply();
    }

    public static void cleanAllKey() {
        preferences.edit().clear().apply();
    }
}

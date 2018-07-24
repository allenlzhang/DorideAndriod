
package com.carlt.sesame.preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.carlt.doride.DorideApplication;
import com.carlt.sesame.data.car.CarInfo;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * 用户最后一次查询的违章车辆信息
 * 
 * @author daisy
 */
public class CarInfoLocal {
    private static final String CAR_INFO = "car_info";// 违章车辆信息

    private static CarInfo carInfo;
    
    private static String phone;//用户手机号
    
    public static CarInfo getCarInfo(String phone) {
        
        CarInfoLocal.phone = phone;
        readConfig();
        return carInfo;
    }

    public static void setCarInfo(CarInfo carInfo,String phone) {
        CarInfoLocal.carInfo = carInfo;
        saveConfig();
    }

    private static boolean readConfig() {
        SharedPreferences sp = DorideApplication.ApplicationContext.getSharedPreferences(CAR_INFO,
                Context.MODE_PRIVATE);

        if (sp != null) {
            try {
                String carinfo=sp.getString(phone, "");
                carInfo=deSerialization(carinfo);

                return true;
            } catch (Exception e) {
                Log.e("info", CarInfoLocal.class.getName() + "_e==" + e);
                e.printStackTrace();
            }
        }
        return false;
    }

    private static boolean saveConfig() {
        SharedPreferences sp = DorideApplication.ApplicationContext.getSharedPreferences(CAR_INFO,
                Context.MODE_PRIVATE);

        if (sp != null) {
            SharedPreferences.Editor editor = sp.edit();
            if (editor != null) {
                try {
                    String carinfo=serialize(carInfo);
                    editor.putString(phone, carinfo);
                    editor.commit();
                    return true;
                } catch (Exception e) {
                    Log.e("info", CarInfoLocal.class.getName() + "_e==" + e);
                    e.printStackTrace();
                }
            }
        }

        return false;
    }
    
    /**
     * 序列化对象
     * 
     * @param person
     * @return
     * @throws IOException
     */
    private static String serialize(CarInfo carInfo) throws IOException {
        long startTime = System.currentTimeMillis();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                byteArrayOutputStream);
        objectOutputStream.writeObject(carInfo);
        String serStr = byteArrayOutputStream.toString("ISO-8859-1");
        serStr = java.net.URLEncoder.encode(serStr, "UTF-8");
        objectOutputStream.close();
        byteArrayOutputStream.close();
        Log.e("serial", "serialize str =" + serStr);
        long endTime = System.currentTimeMillis();
        Log.e("serial", "序列化耗时为:" + (endTime - startTime));
        return serStr;
    }

    /**
     * 反序列化对象
     * 
     * @param str
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private static CarInfo deSerialization(String str) throws IOException,
            ClassNotFoundException {
        long startTime = System.currentTimeMillis();
        String redStr = java.net.URLDecoder.decode(str, "UTF-8");
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
                redStr.getBytes("ISO-8859-1"));
        ObjectInputStream objectInputStream = new ObjectInputStream(
                byteArrayInputStream);
        CarInfo carInfo = (CarInfo) objectInputStream.readObject();
        objectInputStream.close();
        byteArrayInputStream.close();
        long endTime = System.currentTimeMillis();
        Log.e("serial", "反序列化耗时为:" + (endTime - startTime));
        return carInfo;
    }
}

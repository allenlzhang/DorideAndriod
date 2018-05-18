package com.carlt.doride.utils;

import com.carlt.doride.DorideApplication;
import com.carlt.doride.model.LoginInfo;

import java.util.HashMap;

/**
 * Created by Administrator on 2017/8/23 0023.
 */

public class CreateHashMap {

    private static HashMap create(String keys[], String values[]) {
        HashMap<String, String> params = new HashMap<>();
        String token= LoginInfo.getAccess_token();
        ILog.e("info","token=="+token);
//        if(!TextUtils.isEmpty(token)){
//            params.put("token", LoginInfo.getToken());
//        }
        if(keys != null && values != null ){
            int length_key = keys.length;
            int length_value = values.length;
            if ( length_key > 0 && length_value > 0) {
                for (int i = 0; i < length_key; i++) {
                    params.put(keys[i], values[i]);
                }
            }
        }

        return params;
    }


    public static void addDefault(HashMap<String,String> param){
        HashMap<String,String> mp = create(new String[]{},new String[]{});
        if(param != null){
            param.putAll(mp);
        }else{
            param = mp;
        }
    }


    public static HashMap getNullData(){
        return create(null,null);
    }

    /**
     *行车报告 日报月报参数
     * @param date
     * @return
     */
    public static HashMap getDayReportMap(String date){
        String keys[] = {"date"};
        String values[] = {date};
        return create(keys,values);
    }

    /**
     * 信息中心 提醒参数
     * @param class1
     * @return
     */
    public static HashMap getMessageMap(int class1,int limit,int offset){
        String keys[] = {"class1","limit","offset"};
        String values[] = {class1+"",limit+"",offset+""};
        return create(keys,values);
    }

    /**
     * GPS 行车轨迹参数
     * @param gpsStartTime
     * @param gpsStopTime
     * @param runSn
     * @return
     */
    public static HashMap getReportGpsMap(String gpsStartTime,String gpsStopTime,String runSn){
        String key[] = {"gpsStartTime","gpsStopTime","runSn"};
        String values[] = {gpsStartTime,gpsStopTime,runSn};
        return create(key,values);
    }

    /**
     * 提醒删除参数
     * @param class1
     * @param messageid
     * @return
     */
    public static HashMap getRemindDefaultMap(int class1,int messageid){
        String key[] = {"class1","messageid"};
        String values[] = {class1+"",messageid+""};
        return create(key,values);
    }

    /**
     *
     * @param sound_switch
     * @return
     */
    public static HashMap getControlSound(String sound_switch){
        String key[] = {"sound_switch"};
        String values[] = {sound_switch};
        return create(key,values);
    }

    public static HashMap getUpdatePushSet(String close2,String close3){
        String key[] = {"startup","report"};
        String values[] = {close2,close3};
        return create(key,values);
    }


    // 生成登录参数
    public static HashMap getLogin(String userName, String psWord) {
        StringBuffer sysinfo = new StringBuffer(DorideApplication.ANDROID_VERSION);
        sysinfo.append(",");
        sysinfo.append(DorideApplication.DISPLAY);
        sysinfo.append(",");
        sysinfo.append(DorideApplication.MODEL_NAME);
        String keys[] = {"mobile","password","move_deviceid","move_device_name","move_model","softtype","version","sysinfo"};
        String values[] ={userName,CipherUtils.md5(psWord),DorideApplication.NIMEI,DorideApplication.MODEL_NAME,DorideApplication.MODEL,"android",
                DorideApplication.Version_API + "",sysinfo.toString()};

        return create(keys,values);
    }


}

package com.carlt.sesame.data;

/**
 * Description:
 * Company    : carlt
 * Author     : zhanglei
 * Date       : 2018/8/2 16:37
 */
public class SesameBindDeviceInfo {

    /**
     * code : 200
     * data : {"isbind":1,"need_pin":0}
     * msg : 
     * request : /126/car/bindDevice
     * version : v125
     */

    public int code;
    public DataBean data;
    public String   msg;
    public String   request;
    public String   version;



    public static class DataBean {
        /**
         * isbind : 1
         * need_pin : 0
         */

        public int isbind;
        public int need_pin;

    }
}

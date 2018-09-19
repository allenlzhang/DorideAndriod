package com.carlt.doride.data;

/**
 * Description:
 * Company    : carlt
 * Author     : zhanglei
 * Date       : 2018/8/29 18:06
 */
public class ActivateInfo {

    /**
     * code : 200
     * data : {"activate_start_time":"","activate_status":2}
     * msg : 
     * request : /110/car/checkIsActivate
     * version : v110
     */

    public int code;
    public DataBean data;
    public String   msg;
    public String   request;
    public String   version;


    public static class DataBean {
        /**
         * activate_start_time : 
         * activate_status : 2
         */

        public String activate_start_time;
        public int activate_status;


    }
}

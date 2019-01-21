package com.carlt.doride.data.flow;


/**
 * Created by marller on 2018\3\22 0022.
 */

public class TrafficPackageWarnningInfo {
    /**
     * code : 200
     * data : {"carid":2216027,"consume_data":0,"final_service_data_end":"2018-12-22","is_deadline":0,"limit_warning":1,"next_package_size":0,"next_package_type":0,"next_service_data_end":"","next_service_data_start":"","package_size":1024,"package_type":3,"residual_data":1024,"service_data_end":"2018-12-22","service_data_start":"2018-06-23","service_status":1,"share_data_total":1024,"updatetime":"2018-09-01"}
     * msg :
     * request : /163/package/getInfo
     * version : v163
     */

    public int      code;
    public DataBean data;
    public String   msg;
    public String   request;
    public String   version;


    public static class DataBean {
        /**
         * carid : 2216027
         * consume_data : 0
         * final_service_data_end : 2018-12-22
         * is_deadline : 0
         * limit_warning : 1
         * next_package_size : 0
         * next_package_type : 0
         * next_service_data_end :
         * next_service_data_start :
         * package_size : 1024
         * package_type : 3
         * residual_data : 1024
         * service_data_end : 2018-12-22
         * service_data_start : 2018-06-23
         * service_status : 1
         * share_data_total : 1024
         * updatetime : 2018-09-01
         */
        public String carid;//车辆id
        public String simid;//SIMID
        public String consume_data;//本月已耗共享流量
        public String final_service_data_end;//最终流量套餐结束时间
        public String is_deadline;//是否为套餐到期日 0-否 1-是
        public String limit_warning;//流量预警状态：1=非预警状态，2=预警状态
        public String next_package_size;//新流量套餐每月流量大小（统一单位M），若无新套餐则为0
        public String next_package_type;//新流量套餐类型 1-免费流量 2-加油包 3-续套餐 4-改套餐，若无新套餐则为0
        public String next_service_data_end;//新流量套餐结束时间，若无新套餐则为空
        public String next_service_data_start;//新流量套餐开始时间，若无新套餐则为空
        public String package_size;//	当前套餐每月流量大小（统一单位M）
        public String package_type;//当前流量套餐类型 1-免费流量 2-加油包 3-续套餐 4-改套餐
        public String residual_data;//本月剩余共享流量
        public String service_data_end;//	流量套餐结束时间
        public String service_data_start;//	流量套餐开始时间
        public String service_status;//车机供网状态：1=正常，2=已停止供网
        public String share_data_total;//本月共享总流量
        public String updatetime;//查询数据时间
        //		public int carid;
        //		public int    consume_data;
        //		public String final_service_data_end;
        //		public int    is_deadline;
        //		public int    limit_warning;
        //		public int    next_package_size;
        //		public int    next_package_type;
        //		public String next_service_data_end;
        //		public String next_service_data_start;
        //		public int    package_size;
        //		public int    package_type;
        //		public int    residual_data;
        //		public String service_data_end;
        //		public String service_data_start;
        //		public int    service_status;
        //		public int    share_data_total;
        //		public String updatetime;


    }


}

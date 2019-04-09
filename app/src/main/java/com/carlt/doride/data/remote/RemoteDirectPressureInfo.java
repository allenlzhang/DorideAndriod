package com.carlt.doride.data.remote;

import com.carlt.doride.http.retrofitnet.model.BaseErr;

import java.util.List;

/**
 * 直接式胎压监测 返回
 * Created by liu on 2018/3/30.
 * "pressure_status":1,
 * "pressure_value":250,
 * "temperature_value":100, 胎温度
 * "temperature_unit":℃,
 * "pressure_unit":"kpa"
 * 参数名	类型	说明
 * pressure_status	int	胎压状态，1：正常；0：异常
 * pressure_value	int	胎压值
 * pressure_unit	string	单位
 */

public class RemoteDirectPressureInfo {
    /**
     * list : [{"pressureState":1,"pressureUint":"kpa","pressureValue":8},{"pressureState":2,"pressureUint":"kpa","pressureValue":9},{"pressureState":1,"pressureUint":"kpa","pressureValue":2},{"pressureState":2,"pressureUint":"kpa","pressureValue":1}]
     * recTime : 1554711106
     */

    public int            recTime;
    public List<ListBean> list;
    public BaseErr        err;

    public static class ListBean {
        /**
         * pressureState : 1
         * pressureUint : kpa
         * pressureValue : 8
         */

        public int    pressureState;
        public String pressureUint;
        public int    pressureValue;


    }
    /**
     * list : [{"pressure_status":1,"pressure_value":250,"pressure_unit":"kpa"},{"pressure_status":1,"pressure_value":250,"pressure_unit":"kpa"},{"pressure_status":1,"pressure_value":250,"pressure_unit":"kpa"},{"pressure_status":1,"pressure_value":250,"pressure_unit":"kpa"}]
     * rectime : 1541664038
     */

    //    public int rectime;
    //    public List<ListBean> list;
    //
    //
    //
    //    public static class ListBean {
    //        /**
    //         * pressure_status : 1
    //         * pressure_value : 250
    //         * pressure_unit : kpa
    //         */
    //
    //        public int pressure_status;
    //        public int    pressure_value;
    //        public String pressure_unit;
    //
    //
    //    }


}

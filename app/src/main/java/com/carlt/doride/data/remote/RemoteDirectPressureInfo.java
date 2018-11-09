package com.carlt.doride.data.remote;

import java.util.List;

/**
 * 直接式胎压监测 返回
 * Created by liu on 2018/3/30.
 *    "pressure_status":1,
 "pressure_value":250,
 "temperature_value":100, 胎温度
 "temperature_unit":℃,
 "pressure_unit":"kpa"
 参数名	类型	说明
 pressure_status	int	胎压状态，1：正常；0：异常
 pressure_value	int	胎压值
 pressure_unit	string	单位
 *
 */

public class RemoteDirectPressureInfo {
    /**
     * list : [{"pressure_status":1,"pressure_value":250,"pressure_unit":"kpa"},{"pressure_status":1,"pressure_value":250,"pressure_unit":"kpa"},{"pressure_status":1,"pressure_value":250,"pressure_unit":"kpa"},{"pressure_status":1,"pressure_value":250,"pressure_unit":"kpa"}]
     * rectime : 1541664038
     */

    public int rectime;
    public List<ListBean> list;



    public static class ListBean {
        /**
         * pressure_status : 1
         * pressure_value : 250
         * pressure_unit : kpa
         */

        public int pressure_status;
        public int    pressure_value;
        public String pressure_unit;


    }


    //
//    private int pressure_status;
//    private String pressure_value;
//    private String temperature_value;
//    private String temperature_unit;
//    private String pressure_unit;
//
//    public int getPressure_status() {
//        return pressure_status;
//    }
//    public void setPressure_status(int pressure_status) {
//        this.pressure_status = pressure_status;
//    }
//
//    public String getPressure_value() {
//        return pressure_value;
//    }
//
//    public void setPressure_value(String pressure_value) {
//        this.pressure_value = pressure_value;
//    }
//
//    public String getTemperature_value() {
//        return temperature_value;
//    }
//
//    public void setTemperature_value(String temperature_value) {
//        this.temperature_value = temperature_value;
//    }
//
//    public String getTemperature_unit() {
//        return temperature_unit;
//    }
//
//    public void setTemperature_unit(String temperature_unit) {
//        this.temperature_unit = temperature_unit;
//    }
//
//    public String getPressure_unit() {
//        return pressure_unit;
//    }
//
//    public void setPressure_unit(String pressure_unit) {
//        this.pressure_unit = pressure_unit;
//    }
}

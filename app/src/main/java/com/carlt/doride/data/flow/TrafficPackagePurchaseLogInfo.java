
package com.carlt.doride.data.flow;


import com.carlt.sesame.data.BaseResponseInfo;

/**
 * 近期登录记录info
 * 
 * @author Administrator
 */
public class TrafficPackagePurchaseLogInfo extends BaseResponseInfo {

    private String package_cost;//流量包价格

    private String addtime;// 付款时间

    private String package_name;// 流量包名称

    private String buy_time;
    public String package_month;
    public String service_data_start;
    public String service_data_end;

    public String getPackage_name() {

        return package_name;
    }

    public void setPackage_name(String package_name) {
        this.package_name = package_name;
    }

    public String getPackage_cost() {
        return package_cost;
    }

    public void setPackage_cost(String package_cost) {
        this.package_cost = package_cost;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    public String getBuy_time() {
        return buy_time;
    }

    public void setBuy_time(String buy_time) {
        this.buy_time = buy_time;
    }
}

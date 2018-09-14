package com.carlt.doride.data.flow;

import java.util.List;

public class PackageDataInfo {
    /**
     * code : 200
     * data : {"change":[[{"id":92,"name":"2G","package_month":6,"package_size":2048,"price":198,"sort":"1"},{"id":93,"name":"2G","package_month":12,"package_size":2048,"price":388,"sort":"2"},{"id":94,"name":"2G","package_month":24,"package_size":2048,"price":698,"sort":"3"}],[{"id":95,"name":"3G","package_month":6,"package_size":3072,"price":198.1,"sort":"4"},{"id":96,"name":"3G","package_month":12,"package_size":3072,"price":518,"sort":"5"},{"id":97,"name":"3G","package_month":24,"package_size":3072,"price":998,"sort":"6"}],[{"id":90,"name":"5G","package_month":6,"package_size":5120,"price":418,"sort":"7"},{"id":89,"name":"5G","package_month":12,"package_size":5120,"price":798,"sort":"8"},{"id":91,"name":"5G","package_month":24,"package_size":5120,"price":1398,"sort":"9"}]],"refuel":[[{"id":101,"name":"1M","package_month":0,"package_size":1,"price":0.01,"sort":"1"}],[{"id":77,"name":"2M","package_month":0,"package_size":2,"price":0.02,"sort":"2"}],[{"id":78,"name":"500M","package_month":0,"package_size":500,"price":0.03,"sort":"3"}],[{"id":79,"name":"1G","package_month":0,"package_size":1024,"price":0.04,"sort":"4"}]],"renew":[[{"id":80,"name":"1G","package_month":6,"package_size":1024,"price":0.01,"sort":"1"},{"id":81,"name":"1G","package_month":12,"package_size":1024,"price":0.02,"sort":"2"},{"id":82,"name":"1G","package_month":24,"package_size":1024,"price":0.03,"sort":"3"}],[{"id":83,"name":"2G","package_month":6,"package_size":2048,"price":0.02,"sort":"4"},{"id":84,"name":"2G","package_month":12,"package_size":2048,"price":0.03,"sort":"5"},{"id":85,"name":"2G","package_month":24,"package_size":2048,"price":0.04,"sort":"6"}],[{"id":86,"name":"3G","package_month":6,"package_size":3072,"price":0.03,"sort":"7"},{"id":87,"name":"3G","package_month":12,"package_size":3072,"price":0.4,"sort":"8"},{"id":88,"name":"3G","package_month":24,"package_size":3072,"price":0.5,"sort":"9"}],[{"id":99,"name":"5G","package_month":3,"package_size":5000,"price":0.01,"sort":"10"}]]}
     * msg :
     * request : /163/package/getProductList
     * version : v163
     */

    public int      code;
    public DataBean data;
    public String   msg;
    public String   request;
    public String   version;


    public static class DataBean {
        public List<List<FlowPriceInfo>> change;
        public List<List<FlowPriceInfo>> refuel;
        public List<List<FlowPriceInfo>> renew;

        @Override
        public String toString() {
            return "PackageDataInfo [change=" + change + ", refuel=" + refuel
                    + ", renew=" + renew + "]";
        }


    }

    @Override
    public String toString() {
        return "PackageDataInfo{" +
                "code=" + code +
                ", data=" + data +
                ", msg='" + msg + '\'' +
                ", request='" + request + '\'' +
                ", version='" + version + '\'' +
                '}';
    }
}

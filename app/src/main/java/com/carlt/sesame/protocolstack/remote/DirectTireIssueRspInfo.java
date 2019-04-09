package com.carlt.sesame.protocolstack.remote;

import com.carlt.doride.http.retrofitnet.model.BaseErr;

import java.util.List;

/**
 * Created by Marlon on 2019/4/8.
 */
public class DirectTireIssueRspInfo {
    public List<PressureItem> list;
    public int recTime = 0; // 最后一次胎压上报时间
    public BaseErr err;
    public class PressureItem{
        public int pressureState; // 胎压状态:1正常 2：异常
        public int pressureValue; // 胎压值
        public String pressureUint; // 胎压单位
    }
}

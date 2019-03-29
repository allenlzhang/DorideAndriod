
package com.carlt.sesame.protocolstack.usercenter;

import com.carlt.sesame.data.SesameLoginInfo;
import com.carlt.sesame.protocolstack.BaseParser;

import org.json.JSONObject;

/**
 * 用户相关信息解析
 * 
 * @author Administrator
 */
public class ExtInfoParser extends BaseParser {
    private String service_time_expire;// 服务时间即将到期，1=是，0=否

    public String getReturn() {
        return service_time_expire;
    }

    @Override
    protected void parser() {
        JSONObject mJSON_data = mJson.optJSONObject("data");
        service_time_expire = mJSON_data.optString("service_time_expire");
    }


}

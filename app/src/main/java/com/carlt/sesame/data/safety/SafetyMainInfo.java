package com.carlt.sesame.data.safety;

import com.carlt.sesame.data.BaseResponseInfo;

/**
 * 安全中心首页数据
 * @author Administrator
 *
 */
public class SafetyMainInfo extends BaseResponseInfo{
    boolean hasAuthorize;//是否有授权请求 1有0没有
    
    boolean isNoneedpsw;//是否是五分钟内无需输入远程密码

    public boolean isHasAuthorize() {
        return hasAuthorize;
    }

    public void setHasAuthorize(boolean hasAuthorize) {
        this.hasAuthorize = hasAuthorize;
    }

    public boolean isNoneedpsw() {
        return isNoneedpsw;
    }

    public void setNoneedpsw(boolean isNoneedpsw) {
        this.isNoneedpsw = isNoneedpsw;
    }
    
}

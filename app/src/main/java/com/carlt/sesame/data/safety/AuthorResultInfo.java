package com.carlt.sesame.data.safety;

import com.carlt.sesame.data.BaseResponseInfo;

/**
 * 授权结果
 * @author Administrator
 */
public class AuthorResultInfo extends BaseResponseInfo {
    public final static String Status_undo     = "0";//未处理
    public final static String Status_authered = "1";//已允许
    public final static String Status_refused  = "2";//已拒绝


    private String result_status;


    public String getResult_status() {
        return result_status;
    }


    public void setResult_status(String result_status) {
        this.result_status = result_status;
    }
}

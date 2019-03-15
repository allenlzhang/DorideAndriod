package com.carlt.doride.http.retrofitnet.model;

/**
 * Description:
 * Company    : carlt
 * Author     : zhanglei
 * Date       : 2019/3/5 13:50
 */
public class BaseModel<T> {
    public T      data;
    public int    errorCode;
    public String errorMsg;

    @Override
    public String toString() {
        return "BaseModel{" +
                "data=" + data +
                ", errorCode=" + errorCode +
                ", errorMsg='" + errorMsg + '\'' +
                '}';
    }
}

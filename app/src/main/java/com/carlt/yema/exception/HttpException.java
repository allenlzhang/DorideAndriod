package com.carlt.yema.exception;

/**
 * Created by yyun on 17-11-3.
 */

public class HttpException extends BaseException {

    public HttpException(){
        super();
    }

    public HttpException(String msg){
        super(msg);
    }

    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}

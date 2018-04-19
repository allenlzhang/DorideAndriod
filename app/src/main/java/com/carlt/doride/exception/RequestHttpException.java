package com.carlt.doride.exception;

/**
 * Created by yyun on 17-11-3.
 */

public class RequestHttpException extends HttpException {

    public RequestHttpException(){
        super();
    }

    public RequestHttpException(String msg){
        super(msg);
    }
}

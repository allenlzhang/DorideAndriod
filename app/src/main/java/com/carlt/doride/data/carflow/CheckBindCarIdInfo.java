package com.carlt.doride.data.carflow;

/**
 * Created by Marlon on 2018/12/21.
 */
public class CheckBindCarIdInfo {
    public int    code;
    public Data data;
    public String error;
    public String msg;

    public class Data{
        public int result;
        public String ccid;
    }
}

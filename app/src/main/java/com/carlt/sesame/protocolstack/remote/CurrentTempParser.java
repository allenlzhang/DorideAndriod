package com.carlt.sesame.protocolstack.remote;


import android.text.TextUtils;

import com.carlt.doride.http.retrofitnet.model.RemoteCarStateInfo;
import com.carlt.sesame.data.remote.AirMainInfo;
import com.carlt.sesame.data.remote.RemoteFunInfo;
import com.carlt.sesame.utility.Log;

import java.util.ArrayList;

/**
 * Created by Marlon on 2019/4/8.
 */
public class CurrentTempParser {

    private AirMainInfo airMainInfo;

    public CurrentTempParser(AirMainInfo airMainInfo){
        this.airMainInfo = airMainInfo;
    }
    public AirMainInfo parser(RemoteCarStateInfo remoteCarStateInfo){
        if (remoteCarStateInfo == null){
            remoteCarStateInfo = new RemoteCarStateInfo();
        }
        String temp = "";
        String airState = "";
        if (remoteCarStateInfo.err!=null) {
            temp = remoteCarStateInfo.ACTemp + "";
            boolean isGetCurrentTempSuccess;
            if (TextUtils.isEmpty(temp)) {
                temp = "--";
                isGetCurrentTempSuccess = false;
            } else {
                if (temp.equals("0")) {
                    temp = "--";
                    isGetCurrentTempSuccess = false;
                } else if (temp.equals("255")) {
                    temp = "--";
                    isGetCurrentTempSuccess = false;
                } else {
                    isGetCurrentTempSuccess = true;
                }
            }
            //测试数据
            //temp = "35";
            //测试数据结束
            airMainInfo.setCurrentTemp(temp);
            Log.e("info", "temp==------------" + temp);
            airMainInfo.setGetCurrentTempSuccess(isGetCurrentTempSuccess);

            airState = remoteCarStateInfo.AC + "";
            ArrayList<RemoteFunInfo> remoteFunInfos = airMainInfo.getmRemoteFunInfos();
            for (int i = 0; i < remoteFunInfos.size(); i++) {
                RemoteFunInfo item = remoteFunInfos.get(i);
                String id = item.getId();

                if (id.equals(airState)) {
                    item.setSelect(true);
                    //                            break;
                } else {
                    item.setSelect(false);
                }
            }
            airMainInfo.setState(airState);
        }else {
            airMainInfo.setCurrentTemp("26");
            airMainInfo.setGetCurrentTempSuccess(false);
            airMainInfo.setState("-1");
        }
        return airMainInfo;
    }
}

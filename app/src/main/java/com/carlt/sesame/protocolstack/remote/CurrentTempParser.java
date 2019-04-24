package com.carlt.sesame.protocolstack.remote;


import android.text.TextUtils;

import com.blankj.utilcode.util.LogUtils;
import com.carlt.doride.http.retrofitnet.model.RemoteCarStateInfo;
import com.carlt.sesame.data.remote.AirMainInfo;
import com.carlt.sesame.data.remote.RemoteFunInfo;

import java.util.ArrayList;

/**
 * Created by Marlon on 2019/4/8.
 */
public class CurrentTempParser {

    private AirMainInfo airMainInfo;

    public CurrentTempParser(AirMainInfo airMainInfo) {
        this.airMainInfo = airMainInfo;
    }

    public AirMainInfo parser(RemoteCarStateInfo remoteCarStateInfo) {
        if (remoteCarStateInfo == null) {
            remoteCarStateInfo = new RemoteCarStateInfo();
        }
        String temp = "";
        String airState = "";
        if (remoteCarStateInfo.err == null) {
            temp = remoteCarStateInfo.ACTemp;
            boolean isGetCurrentTempSuccess;
            if (TextUtils.isEmpty(temp)) {
                temp = "--";
                isGetCurrentTempSuccess = false;
            } else {
                if (temp.equals("0.0")) {
                    temp = "0";
                    isGetCurrentTempSuccess = false;
                } else {
                    isGetCurrentTempSuccess = true;
                }
                //                if (temp.equals("0")) {
                //                    temp = "--";
                //                    isGetCurrentTempSuccess = false;
                //                } else if (temp.equals("255")) {
                //                    temp = "--";
                //                    isGetCurrentTempSuccess = false;
                //                } else {
                //                    isGetCurrentTempSuccess = true;
                //                }
            }

            airMainInfo.setCurrentTemp(temp);
            airMainInfo.setGetCurrentTempSuccess(isGetCurrentTempSuccess);

            airState = remoteCarStateInfo.AC + "";
            ArrayList<RemoteFunInfo> remoteFunInfos = airMainInfo.getmRemoteFunInfos();
            for (int i = 0; i < remoteFunInfos.size(); i++) {
                RemoteFunInfo item = remoteFunInfos.get(i);
                String id = item.getId();

                if (id.equals(airState)) {
                    LogUtils.e(id);
                    item.setSelect(true);
                } else {
                    item.setSelect(false);
                }
            }
            airMainInfo.setState(airState);
        } else {
            airMainInfo.setCurrentTemp("0");
            airMainInfo.setGetCurrentTempSuccess(false);
            airMainInfo.setState("-1");
        }
        return airMainInfo;
    }
}

package com.carlt.sesame.protocolstack.remote;

import com.carlt.doride.http.retrofitnet.model.RemoteCarStateInfo;
import com.carlt.sesame.data.remote.CarStateInfo;

import java.util.ArrayList;

/**
 * Created by Marlon on 2019/4/4.
 */
public class CarStateParser {

    private ArrayList<CarStateInfo> mCarStateInfos = new ArrayList<CarStateInfo>();
    public ArrayList<CarStateInfo> parser(RemoteCarStateInfo remoteCarStateInfo){
        String[] names = CarStateInfo.names;
        int[] iconId_opens = CarStateInfo.iconId_opens_carmain;
        int[] iconId_closes = CarStateInfo.iconId_closes_carmain;
        int length = names.length;
        String[] apiAttrNames = { String.valueOf(remoteCarStateInfo.doorLockStatus), String.valueOf(remoteCarStateInfo.doorCloseStatus),
                String.valueOf(remoteCarStateInfo.engine), String.valueOf(remoteCarStateInfo.AC) };
        String[] stateClose = { "已锁", "已关", "已熄火", "关闭" };
        String[] stateOpen = { "未锁", "未关", "已启动", "已开启" };

        // // 前装设备or后装2016款
        for (int i = 0; i < length; i++) {
            CarStateInfo mInfo = new CarStateInfo();
            mInfo.setName(names[i]);
            String state = apiAttrNames[i];
            if (i == 3) {
                if (state.equals("2")) {
                    mInfo.setIconId(iconId_closes[i]);
                    mInfo.setStateDes(stateClose[i]);
                } else {
                    mInfo.setIconId(iconId_opens[i]);
                    mInfo.setStateDes(stateOpen[i]);
                    String temp = remoteCarStateInfo.ACTemp;
                    double tem1 = Double.valueOf(temp);
                    int temp_int = (int) Math.rint(tem1);
                    if (temp_int != 0) {
                        mInfo.setValue(temp_int + "℃");
                    } else {
                        mInfo.setValue("--℃");
                    }

                }
            } else {
                if (state.equals("4294967295")) {
                    mInfo.setIconId(iconId_closes[i]);
                    mInfo.setStateDes(stateClose[i]);
                } else {
                    mInfo.setIconId(iconId_opens[i]);
                    mInfo.setStateDes(stateOpen[i]);
                }
            }
            mCarStateInfos.add(mInfo);
        }
        return mCarStateInfos;
    }

}

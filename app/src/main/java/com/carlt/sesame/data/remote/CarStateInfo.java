
package com.carlt.sesame.data.remote;

import com.carlt.doride.R;
import com.carlt.sesame.data.BaseResponseInfo;

public class CarStateInfo extends BaseResponseInfo {
    public final static int[] iconId_opens = {
            R.drawable.remote_unlock, R.drawable.remote_door_open, R.drawable.remote_engine_start,
            R.drawable.remote_air_open
    };

    public final static int[] iconId_closes = {
            R.drawable.remote_lock, R.drawable.remote_door_close, R.drawable.remote_engine_stop,
            R.drawable.remote_air_colse
    };
    
    public final static int[] iconId_opens_carmain = {
            R.drawable.car_unlock, R.drawable.car_door_open, R.drawable.car_engine_start,
            R.drawable.car_air_open
    };
    
    public final static int[] iconId_closes_carmain = {
            R.drawable.car_locked, R.drawable.car_door_close, R.drawable.car_engine_stop,
            R.drawable.car_air_close
    };

    public final static String[] names = {
            "车锁", "车门", "发动机", "空调"
    };

    int iconId;// 状态iconid

    String name;// 状态名称

    String stateDes;// 状态描述

    String value;// 此状态下的相关数值
    
    String state;//后台给的原始状态

    public int getIconId() {
        return iconId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStateDes() {
        return stateDes;
    }

    public void setStateDes(String stateDes) {
        this.stateDes = stateDes;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}

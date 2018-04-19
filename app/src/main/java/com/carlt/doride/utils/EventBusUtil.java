package com.carlt.doride.utils;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by yyun on 17-10-31.
 */

public class EventBusUtil {
    private static EventBus mInstance;


    public synchronized static EventBus getInstance(){
        if (mInstance == null ){
            mInstance = EventBus.getDefault();
        }
        return mInstance;
    }

}

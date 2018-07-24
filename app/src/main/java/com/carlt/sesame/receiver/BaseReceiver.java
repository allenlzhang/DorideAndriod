
package com.carlt.sesame.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BaseReceiver extends BroadcastReceiver {

    public final static String ACTION_UPDATE = "com.carlt.sesame.action_update";// 硬件升级action

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub

    }

}


package com.carlt.yema.base;

import android.app.ActivityGroup;
import android.util.Log;
import android.view.KeyEvent;

import com.carlt.yema.control.ActivityControl;


public class BaseActivityGroup extends ActivityGroup {

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.e("info", "KEYCODE_BACK");
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            ActivityControl.exit(BaseActivityGroup.this);
            Log.e("info", "KEYCODE_BACK");
            return true;

        }
        return super.onKeyDown(keyCode, event);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}

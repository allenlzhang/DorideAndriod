
package com.carlt.sesame.ui.activity.base;

import android.app.ActivityGroup;
import android.util.Log;
import android.view.KeyEvent;

import com.carlt.sesame.control.ActivityControl;

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
//        MobclickAgent.onPageStart(this.getClass().getName());
//        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
//        MobclickAgent.onPageEnd(this.getClass().getName());
//        MobclickAgent.onPause(this);
    }
}

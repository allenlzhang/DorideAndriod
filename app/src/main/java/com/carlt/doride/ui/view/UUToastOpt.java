package com.carlt.doride.ui.view;


import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.carlt.doride.R;

/**
 * 描述：此类用于用户提示信息显示
 */
public class UUToastOpt extends Toast {

    private static LayoutInflater inflater;

    private static Toast toast;

    private static UUToastOpt uuTo;

    private static TextView text;

    private UUToastOpt(Context context) {
        super(context);
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.toast_opt, null);
        text = (TextView) layout.findViewById(R.id.toast_message);
        toast = new Toast(context);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.setGravity(Gravity.CENTER, 0, 0);
    }

    public static void showUUToast(Context context, CharSequence tex, int duration) {
        if (uuTo == null) {
            uuTo = new UUToastOpt(context);
        } else {
            uuTo.cancel();
            uuTo = new UUToastOpt(context);
        }
        //        uuTo.cancel();
        //        uuTo = null;
        //        uuTo = new UUToastOpt(context);
        //        text.setText(tex);
        text.setText(tex);
        toast.show();
    }

    public static void showUUToast(Context context, CharSequence tex) {
        showUUToast(context, tex, Toast.LENGTH_LONG);
    }

}

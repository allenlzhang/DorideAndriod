
package com.carlt.sesame.ui.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

import com.carlt.doride.DorideApplication;
import com.carlt.sesame.R;

/**
 * 胎压监测贴士dialog
 * 
 * @author daisy
 */
public class CreateDialogTips {

    public interface DialogBtnClick {
        void onClick();
    };

    public static void createDialogTips(Context context, final DialogBtnClick click) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_tiretips, null);
        final Dialog dialogI = new Dialog(context, R.style.dialog);
        View content = (TextView)view.findViewById(R.id.dialog_withtitle_content1);
        TextView btn = (TextView)view.findViewById(R.id.tiretips_btn);

        btn.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {

                click.onClick();
                dialogI.dismiss();

            }
        });

        int w = (int)(DorideApplication.ScreenDensity * 280);
        LayoutParams parm = new LayoutParams(w, LayoutParams.WRAP_CONTENT);
        dialogI.setContentView(view, parm);

        dialogI.setCanceledOnTouchOutside(false);
        dialogI.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                // TODO Auto-generated method stub
                return true;
            }
        });
        dialogI.show();
    }

}

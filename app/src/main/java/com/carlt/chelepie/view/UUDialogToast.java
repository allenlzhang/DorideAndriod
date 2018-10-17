
package com.carlt.chelepie.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

import com.carlt.chelepie.view.gif.GifView;
import com.carlt.doride.DorideApplication;
import com.carlt.doride.R;


/**
 * 提示信息Dialog
 * 
 * @author Administrator
 */
public class UUDialogToast extends Dialog {

    protected TextView title;

    protected TextView toast;

    protected ImageView imgIcon;

    protected GifView gifIcon;

    private final static int w_dip = 300;
    
    Context context;

    public UUDialogToast(Context context) {
        super(context, R.style.dialog);
        this.context = context;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_toast, null);

        title = (TextView)view.findViewById(R.id.dialog_toast_txt_title);
        toast = (TextView)view.findViewById(R.id.dialog_toast_txt_toast);

        imgIcon = (ImageView)view.findViewById(R.id.dialog_toast_img_icon);
        gifIcon = (GifView)view.findViewById(R.id.dialog_toast_gif_icon);

        int w = (int)(DorideApplication.ScreenDensity * w_dip);
        setCanceledOnTouchOutside(false);
        LayoutParams parm = new LayoutParams(w, LayoutParams.WRAP_CONTENT);
        setContentView(view, parm);
        setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                // TODO Auto-generated method stub
                return true;
            }
        });
    }

    public void setTitleText(String s) {
        if (title != null) {
            title.setText(s);
        }
    }

    public void setToastText(String s) {
        if (toast != null) {
            toast.setText(s);
        }
    }

    public void setImgIcon(int resId) {
        if (resId < 0) {
            gifIcon.setVisibility(View.VISIBLE);
            imgIcon.setVisibility(View.GONE);
        } else {
            gifIcon.setVisibility(View.GONE);
            imgIcon.setVisibility(View.VISIBLE);
            imgIcon.setImageResource(resId);
        }
    }

    public void setGifIcon(int resId) {
        if (resId < 0) {
            imgIcon.setVisibility(View.VISIBLE);
            gifIcon.setVisibility(View.GONE);
        } else {
            imgIcon.setVisibility(View.GONE);
            gifIcon.setVisibility(View.VISIBLE);
            gifIcon.setSrc(resId);
            gifIcon.setStart(context);
        }
    }

}

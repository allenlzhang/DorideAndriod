
package com.carlt.sesame.ui.view;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

import com.carlt.doride.DorideApplication;
import com.carlt.sesame.R;

/**
 * 游客登录提示对话框
 * 
 * @author Daisy
 */
public class VistorTipDialog extends Dialog implements OnClickListener{

    protected TextView btn;// "我知道了"按钮

    private final static int w_dip = 238;
    private final static int h_dip = 288;

    public VistorTipDialog(Context context) {
        super(context, R.style.dialog);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_visitortips, null);

        btn = (TextView)view.findViewById(R.id.dialog_visitor_btn);
        btn.setOnClickListener(this);
        int w = (int)(DorideApplication.ScreenDensity * w_dip);
        int h = (int)(DorideApplication.ScreenDensity * h_dip);
        setCanceledOnTouchOutside(false);
        LayoutParams parm = new LayoutParams(w,h);
        setContentView(view, parm);
    }

    @Override
    public void onClick(View v) {
        dismiss();
    }

}

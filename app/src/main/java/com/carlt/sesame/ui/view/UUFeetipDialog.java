
package com.carlt.sesame.ui.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.ImageView;

import com.carlt.doride.R;
import com.carlt.sesame.http.AsyncImageLoader;
import com.carlt.sesame.http.AsyncImageLoader.AsyncImageLoaderListener;

/**
 * 续费提醒对话框
 * 
 * @author Daisy
 */
public class UUFeetipDialog extends Dialog implements OnClickListener,
        AsyncImageLoaderListener {

    protected ImageView imgBg;// 背景图片

    protected ImageView imgCha;// 叉号按钮

    private String fileUrl;// 图片地址

    private AsyncImageLoader mAsyncImageLoader = AsyncImageLoader.getInstance();
    
    private View viewMain;
    
    private WindowManager windowManager;

    public UUFeetipDialog(Context context) {
        super(context, R.style.dialog);
        LayoutInflater inflater = LayoutInflater.from(context);
        viewMain = inflater.inflate(R.layout.dialog_feetip, null);
        imgBg = (ImageView)viewMain.findViewById(R.id.feetip_img_bg);
        imgCha = (ImageView)viewMain.findViewById(R.id.feetip_img_cha);

        imgCha.setOnClickListener(this);

        setCanceledOnTouchOutside(false);
        LayoutParams parm = new LayoutParams(LayoutParams.FILL_PARENT,
                LayoutParams.FILL_PARENT);
        setContentView(viewMain, parm);
        setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                // TODO Auto-generated method stub
                return false;
            }
        });
        
        mAsyncImageLoader.setmListener(this);

        // getWindow().setBackgroundDrawableResource(R.drawable.feetip_bg);
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;

        if (this.fileUrl != null && this.fileUrl.length() > 0) {
            Bitmap mBitmap = mAsyncImageLoader.getBitmapByUrl(fileUrl);
            if (mBitmap != null) {
                Drawable drawable = new BitmapDrawable(mBitmap);
                getWindow().setBackgroundDrawable(drawable);
            } else {
                dismiss();
            }
        } else {
            dismiss();
        }
    }
    

    public void setWindowManager(WindowManager windowManager) {
        this.windowManager = windowManager;
        
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = (int)(display.getWidth()); //设置宽度
        getWindow().setAttributes(lp);
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    @Override
    public void onClick(View v) {
        dismiss();
    }

    @Override
    public void OnImgLoadFinished(String url, Bitmap mBitmap) {
        if(url!=null&&!url.equals("")&&mBitmap!=null){
            if(url.equals(this.fileUrl)){
                Drawable drawable=new BitmapDrawable(mBitmap);
                //getWindow().setBackgroundDrawable(drawable);
                viewMain.setBackgroundDrawable(drawable);
                
            }
        }else{
            dismiss();
        }
    }

}

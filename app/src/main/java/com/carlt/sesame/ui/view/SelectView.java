
package com.carlt.sesame.ui.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

import com.carlt.sesame.R;

/**
 * 
 * @author daisy
 */
public abstract class SelectView {
    private LayoutInflater inflater;

    private UUPopupWindow menuPop;

    protected View menuView_main;

    private Animation ani1;

    private Animation ani2;

    private Context mContext;

    private LinearLayout menu_bg;

    private ImageView mBack;

    private TextView mTitle;

    private TextView mShare;

    private String types[];

    public void SetOnDismissListener(OnDismissListener mOnDismissListener) {
        menuPop.setOnDismissListener(mOnDismissListener);
    }

    public SelectView(Context mContext) {

        this.mContext = mContext;
        inflater = LayoutInflater.from(mContext);
        menuView_main = inflater.inflate(R.layout.menu_calendar_lay, null);
        menu_bg = (LinearLayout)menuView_main.findViewById(R.id.menu_calendar_lay);
		Display disp = ((Activity) mContext).getWindowManager().getDefaultDisplay();
		Point outP = new Point();
		disp.getSize(outP);
		int mHeight = outP.y;
        menuPop = new UUPopupWindow(menuView_main, LayoutParams.MATCH_PARENT,
        	mHeight - getTopHeight(mContext));
        mTitle = (TextView)menuView_main.findViewById(R.id.head_back_txt1);
        mShare = (TextView)menuView_main.findViewById(R.id.head_back_txt2);
        mBack = (ImageView)menuView_main.findViewById(R.id.head_back_img1);
        mBack.setImageResource(R.drawable.arrow_back);
        mShare.setVisibility(View.GONE);
        mBack.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dissmiss();
            }
        });
        initAnimation();
        menuView_main.setFocusableInTouchMode(true);
        menuView_main.setOnKeyListener(new OnKeyListener() {

            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_MENU && menuPop.isShowing()
                        && event.getAction() == KeyEvent.ACTION_UP) {
                    menuPop.dismiss();

                } else if (keyCode == KeyEvent.KEYCODE_BACK && menuPop.isShowing()
                        && event.getAction() == KeyEvent.ACTION_UP) {
                    menuPop.dismiss();

                }
                return true;
            }
        });
//        menuPop.setBackgroundDrawable(new BitmapDrawable());
        menuPop.initAni(menuView_main, ani2);
        menuPop.setFocusable(true);
        menuPop.update();
        menuPop.setOutsideTouchable(false);
        
        menuView_main.setOnKeyListener(new OnKeyListener() {
			
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if(keyCode == KeyEvent.KEYCODE_BACK){
					onBackPressed();
					return true;
				}
				return false;
			}
		});
    }

    protected void init(View child) {
        menu_bg.addView(child);
    }

    protected void setTitle(String title) {
        mTitle.setText(title);
    }

    private void initAnimation() {

        ani1 = AnimationUtils.loadAnimation(mContext, R.anim.enter_menu_personevaluation);
        ani2 = AnimationUtils.loadAnimation(mContext, R.anim.exit_menu_personevaluation);

    }

    public void showMenu() {

		menuPop.showAtLocation(menuView_main, 0, 0, getTopHeight(mContext));
        menuView_main.startAnimation(ani1);
        ani1.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationRepeat(Animation arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationEnd(Animation arg0) {
                onPopCreat();

            }
        });

    }

    public void dissmiss() {
//        menuPop.dismiss();
        menuPop.dismissMenu();
    }

    /**
     */
    public void setOnBackClickListner() {
        mBack.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                onBackClick();
            }
        });

    }

    protected abstract void onPopCreat();

    protected abstract void onBackClick();
    
    
    private int getTopHeight(Context ctx){
    	Display disp = ((Activity) ctx).getWindowManager().getDefaultDisplay();  
        Point outP = new Point();  
        disp.getSize(outP);  
        int mWidth = outP.x ;  
        int mHeight = outP.y; 
        
        Rect outRect = new Rect();  
        ((Activity) ctx).getWindow().getDecorView().getWindowVisibleDisplayFrame(outRect);  
        System.out.println("top:"+outRect.top +" ; left: "+outRect.left) ;  
        int mWidth2 = outRect.width() ;  
        int mHeight2 = outRect.height();  
    	
    	return mHeight - mHeight2;
    }
    
    protected void onBackPressed(){
    	dissmiss();
    }

}

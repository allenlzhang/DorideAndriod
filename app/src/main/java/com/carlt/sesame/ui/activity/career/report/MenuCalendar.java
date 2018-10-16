package com.carlt.sesame.ui.activity.career.report;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.carlt.doride.R;
import com.carlt.sesame.ui.view.UUPopupWindow;

public abstract class MenuCalendar {
	private LayoutInflater inflater;

	private UUPopupWindow menuPop;

	protected RelativeLayout menuView_main;
	
	protected LinearLayout mContiner;

	private Animation ani1;

	private Animation ani2;

	protected Context mContext;

	private ImageView mBack;
	private TextView mTitle;
	private TextView mShare;

	// private View loading;
	//
	// private TextView loading_text;
	//
	// private View loading_bar;

	public void SetOnDismissListener(OnDismissListener mOnDismissListener) {
		menuPop.setOnDismissListener(mOnDismissListener);
	}

	public MenuCalendar(Context mContext) {

		this.mContext = mContext;
		inflater = LayoutInflater.from(mContext);
		menuView_main = (RelativeLayout) inflater.inflate(
				R.layout.sesame_menu_calendar_lay, null);
		mContiner = (LinearLayout)menuView_main.findViewById(R.id.menu_calendar_lay);
		Display disp = ((Activity) mContext).getWindowManager().getDefaultDisplay();
		Point outP = new Point();
		disp.getSize(outP);
		int mHeight = outP.y;
		menuPop = new UUPopupWindow(menuView_main, LayoutParams.MATCH_PARENT,
			mHeight - getTopHeight(mContext));
		mTitle = (TextView) menuView_main.findViewById(R.id.head_back_txt1);
		mShare = (TextView) menuView_main.findViewById(R.id.head_back_txt2);
		mBack = (ImageView) menuView_main.findViewById(R.id.head_back_img1);
//		mBack.setImageResource(R.drawable.arrow_back);
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

				} else if (keyCode == KeyEvent.KEYCODE_BACK
						&& menuPop.isShowing()
						&& event.getAction() == KeyEvent.ACTION_UP) {
					menuPop.dismiss();

				}
				return true;
			}
		});
		menuPop.setBackgroundDrawable(new BitmapDrawable());
		menuPop.initAni(menuView_main, ani2);
		menuPop.setFocusable(true);
		menuPop.update();
		menuPop.setOutsideTouchable(false);
	}

	protected void init(View child) {
		mContiner.addView(child);
	}

	protected void setTitle(String title) {
		mTitle.setText(title);
	}

	private void initAnimation() {

		ani1 = AnimationUtils.loadAnimation(mContext,
				R.anim.enter_menu_personevaluation);
		ani2 = AnimationUtils.loadAnimation(mContext,
				R.anim.exit_menu_personevaluation);

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

	// private GetResultListCallback listener = new GetResultListCallback() {
	// @Override
	// public void onFinished(Object o) {
	// Message msg = new Message();
	// msg.what = 0;
	// msg.obj = o;
	// mHandler.sendMessage(msg);
	//
	// }
	//
	// @Override
	// public void onErro(Object o) {
	// Message msg = new Message();
	// msg.what = 1;
	// msg.obj = o;
	// mHandler.sendMessage(msg);
	// }
	// };
	//
	// private Handler mHandler = new Handler() {
	//
	// @Override
	// public void handleMessage(Message msg) {
	// switch (msg.what) {
	// case 0:
	//
	// LoadSuccess(msg.obj);
	// loading.setVisibility(View.GONE);
	// break;
	// case 1:
	// LoadErro(msg.obj);
	// loading_text.setText("加载失败");
	// loading_bar.setVisibility(View.INVISIBLE);
	// loading.setVisibility(View.VISIBLE);
	// break;
	// }
	// }
	// };

	public void dissmiss() {
		menuPop.dismiss();
	}

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
	
	protected abstract void onPopCreat();

}

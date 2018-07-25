
package com.carlt.sesame.ui.view;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

import com.carlt.doride.R;


public class MenuImageShow {
    private LayoutInflater inflater;

    private UUPopupWindow menuPop;

    private View menuView_main;

    private Animation ani1;

    private Animation ani2;

    private View menu_bg;

    public void SetOnDismissListener(OnDismissListener mOnDismissListener) {
        menuPop.setOnDismissListener(mOnDismissListener);
    }

    public MenuImageShow(Context mContext) {
        inflater = LayoutInflater.from(mContext);
        menuView_main = inflater.inflate(R.layout.menu_image_show_lay, null);

        menuPop = new UUPopupWindow(menuView_main, LayoutParams.FILL_PARENT,
                LayoutParams.WRAP_CONTENT);
        menu_bg = menuView_main.findViewById(R.id.menu_food_change_list_background);

        menu_bg.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dissmiss();
            }
        });
        initAnimation(mContext);
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
        menuPop.setBackgroundDrawable(new BitmapDrawable());
        menuPop.initAni(menuView_main, ani2);
        menuPop.setFocusable(true);
        menuPop.update();
        menuPop.setOutsideTouchable(false);

    }

    private void initAnimation(Context mContext) {

        ani1 = AnimationUtils.loadAnimation(mContext, R.anim.enter_menu_personevaluation);
        ani2 = AnimationUtils.loadAnimation(mContext, R.anim.exit_menu_personevaluation);

    }

    /**
     * @param res 背景图
     * @param view 显示在该view的下方
     * @param txt 显示的文字
     */
    public void showMenu(int res, View view, String txt) {

        TextView mTextView = (TextView)menuView_main.findViewById(R.id.menu_image_show_lay_txt);
        mTextView.setBackgroundResource(res);
        mTextView.setText(txt);
        // menuPop.showAtLocation(menuView_main, 0, 0, 0);
        menuPop.showAsDropDown(view);
        menuView_main.startAnimation(ani1);
    }

    public void dissmiss() {
        menuPop.dismiss();
    }
}

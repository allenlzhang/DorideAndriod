
package com.carlt.doride.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.carlt.doride.R;

/**
 * 有加载效果的Activity的基类
 *
 * @author Administrator
 */
public class TitleActivityNoLoading extends BaseActivity {
    protected ImageView back;//返回键
    protected TextView title;//标题
//    protected TextView title_right;//右侧按钮or文字
    protected TextView mTxtDes;// 描述文字

    private RelativeLayout mLayMain;
    private View mMainView;

    LayoutInflater mInflater;

    private ClickTitleListener mClickTitleListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(R.layout.activity_titlenoloading);
        mLayMain = (RelativeLayout) findViewById(R.id.titlenoloading_lay_mainlayout);

        back = (ImageView) findViewById(R.id.back);
        title = (TextView) findViewById(R.id.title);
//        title_right = (TextView) findViewById(R.id.title_txt_right);

        setMainView(layoutResID);
    }

    /**
     * 设置该页面Title展示相关数据
     *
     * @param content            标题内容（必填）
     * @param titleRight         标题右侧按钮文字(可为null)
     * @param titleRightColor    标题右侧按钮颜色(可为null)
     * @param titleRightBg       标题右侧按钮背景(可为-1)
     * @param clickTitleListener 标题两个按钮点击事件(可为null,为null时调用系统的返回方法，右侧按钮点击无效果)
     */
    protected void initTitle(String content, String titleRight, String titleRightColor, int titleRightBg, ClickTitleListener clickTitleListener) {
        if (!TextUtils.isEmpty(content)) {
            title.setText(content);
        }
        if (!TextUtils.isEmpty(titleRight)) {
//            title_right.setText(titleRight);
        }

        if (!TextUtils.isEmpty(titleRightColor)) {
//            title_right.setTextColor(Color.parseColor(titleRightColor));
        }
        if (titleRightBg > 0) {
//            title_right.setBackgroundResource(titleRightBg);
        }
        if (clickTitleListener != null) {
            mClickTitleListener = clickTitleListener;
        }
        back.setOnClickListener(onClickListener);
//        title_right.setOnClickListener(onClickListener);
    }

    /**
     * Change the title associated with this activity.  If this is a
     * top-level activity, the title for its window will change.  If it
     * is an embedded activity, the parent can do whatever it wants
     * with it.
     *
     * @param title
     */
    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
    }

    /**
     * 添加主View
     *
     * @param layoutId
     */
    private void setMainView(int layoutId) {
        mMainView = LayoutInflater.from(this).inflate(layoutId, null);
        LayoutParams mLayoutParams = new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        mLayoutParams.addRule(RelativeLayout.BELOW, R.id.titlenoloading_lay_title);
        mMainView.setLayoutParams(mLayoutParams);
        mLayMain.addView(mMainView);
    }


    public void addFragment(int id, Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(id, fragment)
                .commit();
    }

    public void removeFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .remove(fragment)
                .commit();
    }

    private OnClickListener onClickListener=new OnClickListener() {
        @Override
        public void onClick(View v) {
//            switch (v.getId()) {
//                case R.id.title_img_back:
//                    //标题头左侧按钮
//                    if (mClickTitleListener != null) {
//                        mClickTitleListener.titleLeftClik();
//                    } else {
//                        onBackPressed();
//                    }
//                    break;
//                case R.id.title_txt_right:
//                    //标题头右侧按钮
//                    if (mClickTitleListener != null) {
//                        mClickTitleListener.titleRightClik();
//                    } else {
//
//                    }
//                    break;
//
//            }
        }
    };

//    protected CRControl.GetResultListCallback listener = new CRControl.GetResultListCallback() {
//
//        @Override
//        public void onFinished(Object o) {
//            Message msg = new Message();
//            msg.what = 0;
//            msg.obj = o;
//            mHandler.sendMessage(msg);
//        }
//
//        @Override
//        public void onErro(Object o) {
//            Message msg = new Message();
//            msg.what = 1;
//            msg.obj = o;
//            mHandler.sendMessage(msg);
//
//        }
//    };
//
//    private Handler mHandler = new Handler() {
//
//        @Override
//        public void handleMessage(Message msg) {
//            switch (msg.what) {
//                case 0:
//                    LoadSuccess(msg.obj);
//                    break;
//
//                case 1:
//                    LoadError(msg.obj);
//                    break;
//            }
//        }
//    };

    /**
     * 标题头按钮点击事件
     */
    public interface ClickTitleListener {
        void titleLeftClik();//点击最左侧按钮（一般为返回键）

        void titleRightClik();//点击最右侧按钮
    }
}

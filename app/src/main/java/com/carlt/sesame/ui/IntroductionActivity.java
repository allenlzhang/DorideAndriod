
package com.carlt.sesame.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.carlt.sesame.R;
import com.carlt.sesame.data.UseInfo;
import com.carlt.sesame.preference.UseInfoLocal;
import com.carlt.sesame.ui.activity.base.BaseActivity;
import com.carlt.sesame.ui.activity.usercenter.login.LoginActivity;
import com.carlt.sesame.ui.adapter.IntrodutionAdapter;

import java.util.ArrayList;

/**
 * 引导页
 * 
 * @author daisy
 */
public class IntroductionActivity extends BaseActivity {

    private ViewPager mViewPager;

    private View mViewClick;// 最后一屏的点击区域

    private ArrayList<View> mViews;

    private ArrayList<ImageView> mImageViewDots;// 底部点点的list

    private ArrayList<ImageView> mImageViewCars;// 底部的汽车icon

    private IntrodutionAdapter mAdapter;

    private LayoutInflater mInflater;

    private UseInfo mUseInfo;// 本地记录用户使用app情况

    private int useTimes;

    // 引导图片资源
    private static final int[] layoutIds = {
            R.layout.layout_introdution1,

            R.layout.layout_introdution2, R.layout.layout_introdution3,

            R.layout.layout_introdution4
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduction);

        mUseInfo = UseInfoLocal.getUseInfo();
        useTimes = mUseInfo.getTimes();// 用户使用app次数

        mInflater = LayoutInflater.from(this);

        init();
    }

    private void init() {

        mViewPager = (ViewPager)findViewById(R.id.activity_introduction_viewpager);

        mViews = new ArrayList<View>();
        mImageViewDots = new ArrayList<ImageView>();
        mImageViewCars = new ArrayList<ImageView>();

        mImageViewDots.add((ImageView)findViewById(R.id.activity_introduction_img_dot1));
        mImageViewDots.add((ImageView)findViewById(R.id.activity_introduction_img_dot2));
        mImageViewDots.add((ImageView)findViewById(R.id.activity_introduction_img_dot3));
        mImageViewDots.add((ImageView)findViewById(R.id.activity_introduction_img_dot4));

        mImageViewCars.add((ImageView)findViewById(R.id.activity_introduction_img_car1));
        mImageViewCars.add((ImageView)findViewById(R.id.activity_introduction_img_car2));
        mImageViewCars.add((ImageView)findViewById(R.id.activity_introduction_img_car3));
        mImageViewCars.add((ImageView)findViewById(R.id.activity_introduction_img_car4));

        int size = layoutIds.length;
        for (int i = 0; i < size; i++) {
            View mView = mInflater.inflate(layoutIds[i], null);
            mViews.add(mView);
            ImageView mImageView1 = mImageViewCars.get(i);
            ImageView mImageView2 = mImageViewDots.get(i);
            if (i == 0) {
                mImageView1.setVisibility(View.VISIBLE);
                mImageView2.setImageResource(R.drawable.introduction_dot_light);
            } else {
                mImageView1.setVisibility(View.INVISIBLE);
                mImageView2.setImageResource(R.drawable.introduction_dot_dark);
            }
        }

        mViewClick = mViews.get(3).findViewById(R.id.layout_introdution4_view);
        mViewClick.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (mHandler != null) {
                    mHandler.removeMessages(0);
                }
                Intent mIntent = new Intent(IntroductionActivity.this, LoginActivity.class);
                startActivity(mIntent);
                overridePendingTransition(R.anim.enter_alpha, R.anim.exit_alpha);
                finish();
            }
        });

        mAdapter = new IntrodutionAdapter(mViews);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOnPageChangeListener(mChangeListener);
    }

    private OnPageChangeListener mChangeListener = new OnPageChangeListener() {

        @Override
        public void onPageSelected(int arg0) {

            for (int i = 0; i < mImageViewCars.size(); i++) {
                ImageView mImageView1 = mImageViewCars.get(i);
                ImageView mImageView2 = mImageViewDots.get(i);
                if (i == arg0) {
                    mImageView1.setVisibility(View.VISIBLE);
                    mImageView2.setImageResource(R.drawable.introduction_dot_light);
                } else {
                    mImageView1.setVisibility(View.INVISIBLE);
                    mImageView2.setImageResource(R.drawable.introduction_dot_dark);
                }
            }

            if (arg0 == layoutIds.length - 1) {
                useTimes++;
                mUseInfo.setTimes(useTimes);
                UseInfoLocal.setUseInfo(mUseInfo);

                new Thread() {

                    @Override
                    public void run() {
                        mHandler.sendEmptyMessageDelayed(0, 3000);
                    }

                }.start();
            }

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            Intent mIntent = new Intent(IntroductionActivity.this, LoginActivity.class);
            startActivity(mIntent);
            overridePendingTransition(R.anim.enter_alpha, R.anim.exit_alpha);
            finish();
        };
    };

}

package com.carlt.doride;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.carlt.doride.base.BaseActivity;
import com.carlt.doride.base.BaseFragment;
import com.carlt.doride.control.ActivityControl;
import com.carlt.doride.ui.fragment.CarMainFragment;
import com.carlt.doride.ui.fragment.HomeFragment;
import com.carlt.doride.ui.fragment.RemoteMainFragment;
import com.carlt.doride.ui.fragment.SettingMainFragment;

public class MainActivity extends BaseActivity implements View.OnClickListener{

    private HomeFragment mHomeFragment;
    private CarMainFragment mCarMainFragment;
    private RemoteMainFragment mRemoteMainFragment;
    private SettingMainFragment mSettingMainFragment;

    private LinearLayout mTabHome;
    private LinearLayout mTabCar;
    private LinearLayout mTabRemote;
    private LinearLayout mTabSetting;

    private ImageView mIvTabHome;
    private ImageView mIvTabCar;
    private ImageView mIvTabRemote;
    private ImageView mIvTabSetting;

    private TextView mTxtTabHome;
    private TextView mTxtTabCar;
    private TextView mTxtTabRemote;
    private TextView mTxtTabSetting;

    private FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        mFragmentManager = getFragmentManager();
        setTabSelection(0);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        super.onNewIntent(intent);
    }

    private void init() {
        mTabHome = $ViewByID(R.id.tab_ll_home);
        mTabCar = $ViewByID(R.id.tab_ll_car);
        mTabRemote = $ViewByID(R.id.tab_ll_remote);
        mTabSetting = $ViewByID(R.id.tab_ll_setting);
        mIvTabHome = $ViewByID(R.id.tab_iv_home);
        mIvTabCar = $ViewByID(R.id.tab_iv_car);
        mIvTabRemote = $ViewByID(R.id.tab_iv_remote);
        mIvTabSetting = $ViewByID(R.id.tab_iv_setting);
        mTxtTabHome = $ViewByID(R.id.tab_txt_home);
        mTxtTabCar = $ViewByID(R.id.tab_txt_car);
        mTxtTabRemote = $ViewByID(R.id.tab_txt_remote);
        mTxtTabSetting = $ViewByID(R.id.tab_txt_setting);
        mTabHome.setOnClickListener(this);
        mTabCar.setOnClickListener(this);
        mTabRemote.setOnClickListener(this);
        mTabSetting.setOnClickListener(this);
    }

    private void setTabSelection(int index) {
        clearSelection();
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        hideFragments(transaction);
        switch (index) {
            case 0:
                mIvTabHome.setImageResource(R.drawable.tab_home_selected);
                mTxtTabHome.setTextColor(getResources().getColor(R.color.blue_txt));
                if (mHomeFragment == null){
                    mHomeFragment = new HomeFragment();
                    transaction.add(R.id.content,mHomeFragment);
                }else {
                    transaction.show(mHomeFragment);
                }
                break;
            case 1:
                mIvTabCar.setImageResource(R.drawable.tab_car_selected);
                mTxtTabCar.setTextColor(getResources().getColor(R.color.blue_txt));
                if (mCarMainFragment == null){
                    mCarMainFragment = new CarMainFragment();
                    transaction.add(R.id.content,mCarMainFragment);
                }else {
                    transaction.show(mCarMainFragment);
                }
                break;
            case 2:
                mIvTabRemote.setImageResource(R.drawable.tab_remote_selected);
                mTxtTabRemote.setTextColor(getResources().getColor(R.color.blue_txt));
                if (mRemoteMainFragment == null){
                    mRemoteMainFragment = new RemoteMainFragment();
                    transaction.add(R.id.content,mRemoteMainFragment);
                }else{
                    transaction.show(mRemoteMainFragment);
                }
                break;
            case 3:
                mIvTabSetting.setImageResource(R.drawable.tab_my_selected);
                mTxtTabSetting.setTextColor(getResources().getColor(R.color.blue_txt));
                if (mSettingMainFragment == null){
                    mSettingMainFragment = new SettingMainFragment();
                    transaction.add(R.id.content,mSettingMainFragment);
                }else {
                    transaction.show(mSettingMainFragment);
                }
                break;
            default:
                break;
        }
        transaction.commit();
    }

    private void clearSelection() {
        mIvTabHome.setImageResource(R.drawable.tab_home);
        mTxtTabHome.setTextColor(getResources().getColor(R.color.text_color_gray1));
        mIvTabCar.setImageResource(R.drawable.tab_car);
        mTxtTabCar.setTextColor(getResources().getColor(R.color.text_color_gray1));
        mIvTabRemote.setImageResource(R.drawable.tab_remote);
        mTxtTabRemote.setTextColor(getResources().getColor(R.color.text_color_gray1));
        mIvTabSetting.setImageResource(R.drawable.tab_my);
        mTxtTabSetting.setTextColor(getResources().getColor(R.color.text_color_gray1));
    }

    private void hideFragments(FragmentTransaction transaction){
        if (mHomeFragment != null){
            transaction.hide(mHomeFragment);
        }
        if (mCarMainFragment != null){
            transaction.hide(mCarMainFragment);
        }
        if (mRemoteMainFragment != null){
            transaction.hide(mRemoteMainFragment);
        }
        if (mSettingMainFragment != null){
            transaction.hide(mSettingMainFragment);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            Intent intent = new Intent(MainActivity.this, UserLoginActivity.class);
//            startActivity(intent);
//            finish();
            ActivityControl.exit(this);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        ((BaseFragment) (fragmentAdapter.getItem(0))).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tab_ll_home:
                setTabSelection(0);
                break;
            case R.id.tab_ll_car:
                setTabSelection(1);
                break;
            case R.id.tab_ll_remote:
                setTabSelection(2);
                break;
            case R.id.tab_ll_setting:
                setTabSelection(3);
                break;
        }
    }
}

package com.carlt.yema.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.carlt.yema.ui.fragment.CarMainFragment;
import com.carlt.yema.ui.fragment.HomeFragment;
import com.carlt.yema.ui.fragment.RemoteMainFragment;
import com.carlt.yema.ui.fragment.SettingMainFragment;


/**
 * Created by Marlon on 2017/12/26.
 */

public class FragmentAdapter extends FragmentStatePagerAdapter {
    private int numTab;

    private  HomeFragment homeFragment;

    private  CarMainFragment carMainFragment;

    private  RemoteMainFragment remoteMainFragment;

    private SettingMainFragment settingFragment;


    public FragmentAdapter(FragmentManager fm, int numTab) {
        super(fm);
        this.numTab = numTab;
        homeFragment  = new HomeFragment();
        carMainFragment = new CarMainFragment();
        remoteMainFragment  = new RemoteMainFragment();
        settingFragment   = new SettingMainFragment();
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return homeFragment;
            case 1:
                return carMainFragment;
            case 2:
                return remoteMainFragment;
            case 3:
                return settingFragment;
        }
        return null;
    }

    @Override
    public int getCount() {
        return numTab;
    }
}

package com.carlt.doride.ui.fragment;


import com.carlt.doride.base.BaseFragment;
import com.carlt.doride.http.retrofitnet.model.GetCarInfo;

import java.util.HashMap;

/**
 * Created by tfpc on 2016/11/3.
 */
public class FragmentFactory {
    public static HashMap<Integer, BaseFragment> saveFrament = new HashMap<>();

    public static BaseFragment getFragment(int position) {
        BaseFragment fragment = saveFrament.get(position);
        int dorcenCarDisplay = GetCarInfo.getInstance().dorcenCarDisplay;
        if (fragment == null) {
            switch (position) {
                case 0:
                    fragment = new HomeFragment();
                    break;
                case 1:
                    if (dorcenCarDisplay == 0) {
                        fragment = new CarMainFragment();
                    } else if (dorcenCarDisplay == 1) {
                        fragment = new CarMainFragment2();
                    }
                    //                    fragment = new RemoteFragment();
                    break;
                case 2:
                    fragment = new RemoteMainFragment();
                    break;
                case 3:
                    fragment = new RecorderMainFragment();
                    break;
                case 4:
                    fragment = new SettingMainFragment();
                default:

            }
            saveFrament.put(position, fragment);
        }
        return fragment;
    }


}

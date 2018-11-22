package com.carlt.doride.ui.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.carlt.chelepie.control.WIFIControl;
import com.carlt.chelepie.data.recorder.PieInfo;
import com.carlt.chelepie.data.recorder.UpgradeInfo;
import com.carlt.chelepie.protocolstack.recorder.UpdateFileParser;
import com.carlt.chelepie.view.activity.DownloadUpgradeActivity;
import com.carlt.doride.DorideApplication;
import com.carlt.doride.R;
import com.carlt.doride.base.BaseFragment;
import com.carlt.doride.control.ActivityControl;
import com.carlt.doride.data.BaseResponseInfo;
import com.carlt.doride.data.car.DealerInfo;
import com.carlt.doride.data.flow.TrafficPackageWarnningInfo;
import com.carlt.doride.model.LoginInfo;
import com.carlt.doride.protocolparser.BaseParser;
import com.carlt.doride.protocolparser.car.CarDealerParser;
import com.carlt.doride.systemconfig.URLConfig;
import com.carlt.doride.ui.activity.setting.AboutDorideActivity;
import com.carlt.doride.ui.activity.setting.AccountSecurityActivity;
import com.carlt.doride.ui.activity.setting.CarManagerActivity;
import com.carlt.doride.ui.activity.setting.DeviceManageActivity;
import com.carlt.doride.ui.activity.setting.FlowPackageRechargeActivity;
import com.carlt.doride.ui.activity.setting.MsgManageActivity;
import com.carlt.doride.ui.activity.setting.PersonInfoActivity;
import com.carlt.doride.ui.activity.setting.TravelAlbumActivity;
import com.carlt.doride.ui.view.PopBoxCreat;
import com.carlt.doride.utils.CacheUtils;
import com.carlt.doride.utils.DensityUtil;
import com.carlt.doride.utils.LoadLocalImageUtil;
import com.carlt.doride.utils.StringUtils;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


/**
 * Created by marller on 2018\3\14 0014.
 */

public class SettingMainFragment extends BaseFragment implements View.OnClickListener,WIFIControl.WIFIConnectListener {

    private static final String TAG = SettingMainFragment.class.getSimpleName();

    public static SettingMainFragment newInstance() {
        return new SettingMainFragment();
    }

    private View btn_person_info;//用户信息item
    private View llFlowRecharge;//流量充值
    private View icFlowDot;//流量提醒
    private View lineFlow;
    private View btn_travel_album;//旅行相册item
    private View btn_account_security;//账号与安全item
    private View btn_car_manager;//车辆管理item
    private View btn_msg_manager;//消息管理item
    private View btn_device_manager;//设备管理item
    //    private View btn_clean_cache;//清除缓存item
    private View btn_contact_us;//联系我们item
    private View btn_about_yema;//关于item

    private TextView btn_sign_out;//退出登录按钮
    private TextView cache_size;//退出登录按钮
    private TextView contact_us_phone;//联系我们的电话号码
    private TextView tx_person_name;//联系我们的电话号码

    private ImageView avatar;

    private DealerInfo mDealerInfo;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    protected View inflateView(LayoutInflater inflater) {
        View parent = inflater.inflate(R.layout.activity_setting_main, null, false);
        return parent;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            WIFIControl.rigisterWIFIConnectListener(this);
            WIFIControl.DisConnectChelePai();
            loadData();
        }else {
            WIFIControl.unRigisterWIFIConnectListener(this);
        }
    }

    @Override
    public void init(View parent) {
        getActivateStatus("正在激活中");
        btn_person_info = parent.findViewById(R.id.btn_person_info);
        lineFlow = parent.findViewById(R.id.lineFlow);
        llFlowRecharge = parent.findViewById(R.id.llFlowRecharge);
        icFlowDot = parent.findViewById(R.id.icFlowDot);
        llFlowRecharge.setOnClickListener(this);
        btn_person_info.setOnClickListener(this);
        btn_travel_album = parent.findViewById(R.id.btn_travel_album);
        btn_travel_album.setOnClickListener(this);
        btn_account_security = parent.findViewById(R.id.btn_account_security);
        btn_account_security.setOnClickListener(this);
        btn_car_manager = parent.findViewById(R.id.btn_car_manager);
        btn_car_manager.setOnClickListener(this);
        btn_msg_manager = parent.findViewById(R.id.btn_msg_manager);
        btn_msg_manager.setOnClickListener(this);
        btn_device_manager = parent.findViewById(R.id.btn_device_manager);
        btn_device_manager.setOnClickListener(this);
        //        btn_clean_cache = parent.findViewById(R.id.btn_clean_cache);
        //        btn_clean_cache.setOnClickListener(this);
        btn_contact_us = parent.findViewById(R.id.btn_contact_us);
        btn_contact_us.setOnClickListener(this);
        btn_about_yema = parent.findViewById(R.id.btn_about_yema);
        btn_about_yema.setOnClickListener(this);
        btn_sign_out = parent.findViewById(R.id.btn_sign_out);
        btn_sign_out.setOnClickListener(this);
        cache_size = parent.findViewById(R.id.cache_size);
        contact_us_phone = parent.findViewById(R.id.contact_us_phone);
        tx_person_name = parent.findViewById(R.id.tx_person_name);
        avatar = parent.findViewById(R.id.avatar);

    }

    @Override
    public void onResume() {
        showUserUI();
        loadData();
        super.onResume();
    }

    private void showUserUI() {
        try {
            cache_size.setText(CacheUtils.getTotalCacheSize(this.getActivity()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!TextUtils.isEmpty(LoginInfo.getAvatar_img())) {
            LoadLocalImageUtil.getInstance().displayCircleFromWeb(LoginInfo.getAvatar_img(), avatar, R.mipmap.default_avater);
        }
        if (!TextUtils.isEmpty(LoginInfo.getRealname())) {
            tx_person_name.setText(LoginInfo.getRealname());
        }
        if (LoginInfo.getTbox_type().equals("4G")) {
            //            initFlowInfo();
            if (LoginInfo.getFlowWarn().equals("2")) {
                icFlowDot.setVisibility(View.VISIBLE);
            } else {
                icFlowDot.setVisibility(View.GONE);
            }
            llFlowRecharge.setVisibility(View.VISIBLE);
            lineFlow.setVisibility(View.VISIBLE);
        } else {
            llFlowRecharge.setVisibility(View.GONE);
            lineFlow.setVisibility(View.GONE);
        }
    }

    private void initFlowInfo() {
        //        getFlowProductList();
        //        loadingDataUI();
        OkGo.<String>post(URLConfig.getmTrafficWarnningUrl())
                .params("client_id", URLConfig.getClientID())
                .params("dealerId", LoginInfo.getDealerId())
                .params("token", LoginInfo.getAccess_token())
                .params("deviceType", "android")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        LogUtils.e("======" + response.body());
                        if (response.isSuccessful()) {
                            parseFlowInfoJson(response);
                        }

                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        //                        loadonErrorUI(new BaseResponseInfo());
                    }
                });
    }

    private void parseFlowInfoJson(Response<String> response) {
        if (response != null) {
            JSONObject jo = null;
            try {
                jo = new JSONObject(response.body());
                int code = jo.getInt("code");
                String msg = jo.getString("msg");
                Gson gson = new Gson();
                if (code == 200) {
                    //                    loadSuccessUI();
                    TrafficPackageWarnningInfo warnningInfo = gson.fromJson(response.body(), TrafficPackageWarnningInfo.class);
                    if (Integer.valueOf(warnningInfo.data.limit_warning) == 2) {
                        icFlowDot.setVisibility(View.VISIBLE);
                        lineFlow.setVisibility(View.VISIBLE);
                    } else {
                        icFlowDot.setVisibility(View.GONE);
                        lineFlow.setVisibility(View.GONE);
                    }
                } else {
                    //                    BaseResponseInfo baseResponseInfo = new BaseResponseInfo();
                    //                    baseResponseInfo.setFlag(BaseResponseInfo.ERRO);
                    //                    baseResponseInfo.setInfo(msg);
                    //                    loadonErrorUI(baseResponseInfo);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_person_info:
                Intent personInfo = new Intent(this.getActivity(), PersonInfoActivity.class);
                startActivity(personInfo);
                break;
            case R.id.btn_travel_album:
                Intent travelAlbum = new Intent(this.getActivity(), TravelAlbumActivity.class);
                startActivity(travelAlbum);
                break;
            case R.id.btn_account_security:
                Intent certifiedIntent = new Intent(this.getActivity(), AccountSecurityActivity.class);
                startActivity(certifiedIntent);
                break;
            case R.id.btn_car_manager:
                Intent carManager = new Intent(this.getActivity(), CarManagerActivity.class);
                startActivity(carManager);
                break;
            case R.id.btn_msg_manager:
                Intent msgManager = new Intent(this.getActivity(), MsgManageActivity.class);
                startActivity(msgManager);
                break;
            case R.id.btn_device_manager:
                Intent devManager = new Intent(this.getActivity(), DeviceManageActivity.class);
                startActivity(devManager);
                break;
            //            case R.id.btn_clean_cache:
            //                showCleanCacheDialog();
            //                break;
            case R.id.btn_contact_us:
                //                showDialog();
                Logger.e("-=--=" + mDealerInfo);
                if (null != mDealerInfo && !TextUtils.isEmpty(mDealerInfo.getServiceTel())) {
                    PopBoxCreat.createDialogNotitle(getActivity(), "拨打电话", mDealerInfo.getServiceTel(), "取消", "拨打", new PopBoxCreat.DialogWithTitleClick() {
                        @Override
                        public void onLeftClick() {

                        }

                        @Override
                        public void onRightClick() {
                            goToDial(mDealerInfo.getServiceTel());
                        }
                    });
                }
                break;
            case R.id.btn_about_yema:
                Intent aboutYema = new Intent(this.getActivity(), AboutDorideActivity.class);
                startActivity(aboutYema);

                break;
            case R.id.btn_sign_out:
                //注销记录仪版本
                PieInfo.getInstance().setSoftVersion(null);
                RecorderMainFragment.upGradeFilePath = null;
                ActivityControl.logout(this.getActivity());
                break;
            case R.id.llFlowRecharge:
                //流量包充值
                startActivity(new Intent(getActivity(), FlowPackageRechargeActivity.class));
                break;
        }
    }

    /**
     * 获取经销商信息
     */
    @Override
    public void loadData() {
        //        showUserUI();
        loadingDataUI();
        upgradeProgram();
        CarDealerParser parser = new CarDealerParser(dealerCallback);
        HashMap<String, String> params = new HashMap<>();
        parser.executePost(URLConfig.getM_GET_DEALER_INFO(), params);
    }

    private BaseParser.ResultCallback dealerCallback = new BaseParser.ResultCallback() {
        @Override
        public void onSuccess(BaseResponseInfo bInfo) {
            loadSuccessUI();
            Logger.e(TAG + bInfo.toString());
            mDealerInfo = (DealerInfo) bInfo.getValue();
            contact_us_phone.setText(mDealerInfo.getServiceTel());
        }

        @Override
        public void onError(BaseResponseInfo bInfo) {
            loadonErrorUI(bInfo);
            Logger.e(TAG, bInfo.toString());
        }
    };

    private void showCleanCacheDialog() {
        final Dialog dialog = new Dialog(getActivity(), R.style.CleanCacheDialog);
        View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.clean_cache_dialog, null);
        //获得dialog的window窗口
        Window window = dialog.getWindow();
        //设置dialog在屏幕底部
        window.setGravity(Gravity.CENTER);
        //设置dialog弹出时的动画效果，从屏幕底部向上弹出
        window.getDecorView().setPadding(0, 0, 0, 0);
        //获得window窗口的属性
        android.view.WindowManager.LayoutParams lp = window.getAttributes();
        //设置窗口宽度为充满全屏
        lp.width = DensityUtil.dip2px(getActivity(), 286);
        //设置窗口高度为包裹内容
        lp.height = DensityUtil.dip2px(getActivity(), 159);
        lp.alpha = 0.9f;
        //将设置好的属性set回去
        window.setAttributes(lp);
        //将自定义布局加载到dialog上
        dialog.setContentView(dialogView);
        dialogView.findViewById(R.id.clean_cache_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });
        dialogView.findViewById(R.id.clean_cache_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cleanCache();
                try {
                    cache_size.setText(CacheUtils.getTotalCacheSize(SettingMainFragment.this.getActivity()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

    }


    /**
     * 清除缓存
     */
    private void cleanCache() {
        CacheUtils.clearAllCache(DorideApplication.getInstanse().getApplicationContext());
    }

    /**
     * 跳转到拨号界面
     */
    private void goToDial(String phoneNumber) {
        Intent dialIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
        startActivity(dialIntent);
    }

    @Override
    public void onWIFIChange(int action) {

    }


}

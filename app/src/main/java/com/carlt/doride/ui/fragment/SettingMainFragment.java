package com.carlt.doride.ui.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.carlt.chelepie.control.WIFIControl;
import com.carlt.chelepie.data.recorder.PieInfo;
import com.carlt.doride.DorideApplication;
import com.carlt.doride.R;
import com.carlt.doride.base.BaseFragment;
import com.carlt.doride.control.ActivityControl;
import com.carlt.doride.data.BaseResponseInfo;
import com.carlt.doride.data.car.DealerInfo;
import com.carlt.doride.data.carflow.CheckBindInfo;
import com.carlt.doride.data.carflow.CheckInitInfo;
import com.carlt.doride.data.flow.TrafficPackageWarnningInfo;
import com.carlt.doride.model.LoginInfo;
import com.carlt.doride.protocolparser.BaseParser;
import com.carlt.doride.protocolparser.car.CarDealerParser;
import com.carlt.doride.systemconfig.URLConfig;
import com.carlt.doride.ui.activity.scan.CarFlowPackageRechargeActivity;
import com.carlt.doride.ui.activity.scan.CheckPhoneActivity;
import com.carlt.doride.ui.activity.scan.InitCarSimActivity;
import com.carlt.doride.ui.activity.scan.ScanActivity;
import com.carlt.doride.ui.activity.setting.AboutDorideActivity;
import com.carlt.doride.ui.activity.setting.AccountSecurityActivity;
import com.carlt.doride.ui.activity.setting.CarManagerActivity;
import com.carlt.doride.ui.activity.setting.DeviceManageActivity;
import com.carlt.doride.ui.activity.setting.FlowPackageRechargeActivity;
import com.carlt.doride.ui.activity.setting.MsgManageActivity;
import com.carlt.doride.ui.activity.setting.PersonInfoActivity;
import com.carlt.doride.ui.activity.setting.TravelAlbumActivity;
import com.carlt.doride.ui.view.PopBoxCreat;
import com.carlt.doride.ui.view.UUToast;
import com.carlt.doride.utils.CacheUtils;
import com.carlt.doride.utils.DensityUtil;
import com.carlt.doride.utils.LoadLocalImageUtil;
import com.google.gson.Gson;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.zip.CRC32;


/**
 * Created by marller on 2018\3\14 0014.
 */

public class SettingMainFragment extends BaseFragment implements View.OnClickListener, WIFIControl.WIFIConnectListener {

    private static final String TAG = SettingMainFragment.class.getSimpleName();

    public static SettingMainFragment newInstance() {
        return new SettingMainFragment();
    }

    private View btn_person_info;//用户信息item
    private View llFlowRecharge;//流量充值
    private View llCarFlowRecharge;//流量充值
    private View icFlowDot;//流量提醒
    private View icCarFlowDot;//车机流量提醒
    private View lineFlow;
    private View lineCarFlow;
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
    private TextView tvFlow;
    private TextView tvCarFlow;

    private ImageView avatar;
    private ImageView ivScan;

    private DealerInfo mDealerInfo;

    private String ccid;
    private String scanTime;

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
        } else {
            WIFIControl.unRigisterWIFIConnectListener(this);
        }
    }

    @Override
    public void init(View parent) {
        getActivateStatus("正在激活中");
        btn_person_info = parent.findViewById(R.id.btn_person_info);
        lineFlow = parent.findViewById(R.id.lineFlow);
        lineCarFlow = parent.findViewById(R.id.lineCarFlow);
        llFlowRecharge = parent.findViewById(R.id.llFlowRecharge);
        llCarFlowRecharge = parent.findViewById(R.id.llCarFlowRecharge);
        icFlowDot = parent.findViewById(R.id.icFlowDot);
        icCarFlowDot = parent.findViewById(R.id.icCarFlowDot);
        llFlowRecharge.setOnClickListener(this);
        llCarFlowRecharge.setOnClickListener(this);
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
        tvFlow = parent.findViewById(R.id.tvFlow);
        tvCarFlow = parent.findViewById(R.id.tvCarFlow);
        avatar = parent.findViewById(R.id.avatar);
        ivScan = parent.findViewById(R.id.ivScan);
        ivScan.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        loadData();
        super.onResume();
    }

    private void showUserUI() {
        isCountData = false;
        carid = LoginInfo.getcId();
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
//        if (LoginInfo.getTbox_type().equals("4G")) {


        if (LoginInfo.getFlowWarn().equals("2")) {
            icFlowDot.setVisibility(View.VISIBLE);
        } else {
            icFlowDot.setVisibility(View.GONE);
        }
//            llFlowRecharge.setVisibility(View.VISIBLE);
//            lineFlow.setVisibility(View.VISIBLE);
//        } else {
//
//            llFlowRecharge.setVisibility(View.GONE);
//            lineFlow.setVisibility(View.GONE);
//        }
        isSupportTData();
        checkCarIdIsBind();

    }

    /**
     * 检查carid是否已经绑定
     */
    private void checkCarIdIsBind() {
        OkGo.<String>post(URLConfig.getCAR_CHECK_BIND_URL())
                .params("carid", Integer.valueOf(carid))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        LogUtils.e(response.body());
                        //                        parseCheckJson(response);
                        //                        loadingDialog.dismiss();
                        Gson gson = new Gson();
                        CheckBindInfo checkBindInfo = gson.fromJson(response.body(), CheckBindInfo.class);
                        if (checkBindInfo.code == 0) {
                            if (checkBindInfo.data == 1) {
                                countDataPackage(1);
//                                tvCarFlow.setText("车机流量充值");
//                                tvFlow.setText("T-box流量充值");
//                                llCarFlowRecharge.setVisibility(View.VISIBLE);
//                                lineCarFlow.setVisibility(View.VISIBLE);
//                            } else {
//                                tvCarFlow.setText("流量充值");
//                                tvFlow.setText("流量充值");
//                                llCarFlowRecharge.setVisibility(View.GONE);
//                                lineCarFlow.setVisibility(View.GONE);
                            }
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        LogUtils.e(response);
                        //                        loadingDialog.dismiss();
                    }
                });
    }

    /**
     * 流量包-提醒
     * type 0 Tbox 1车机
     */
    private void initFlowInfo(final int type) {
        //        getFlowProductList();
        //        loadingDataUI();
        //URLConfig.getmTrafficWarnningUrl()
        String url = "";
        if (type == 0){
            url = URLConfig.getmTrafficWarnningUrl();
        }else{
            url = URLConfig.getCAR_FLOW_PACKAGE_INFO_URL();
        }
        OkGo.<String>post(url)
                .params("client_id", URLConfig.getClientID())
                .params("dealerId", LoginInfo.getDealerId())
                .params("token", LoginInfo.getAccess_token())
                .params("deviceType", "android")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        LogUtils.e("======" + response.body());
                        if (response.isSuccessful()) {
                            parseFlowInfoJson(response,type);
                        }

                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        //                        loadonErrorUI(new BaseResponseInfo());
                    }
                });
    }

    private void parseFlowInfoJson(Response<String> response,int type) {
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
                        if (type == 0) {
                            icFlowDot.setVisibility(View.VISIBLE);
                        }else {
                            icCarFlowDot.setVisibility(View.VISIBLE);
                        }
                    } else {
                        if (type == 0) {
                            icFlowDot.setVisibility(View.GONE);
                        }else {
                            icCarFlowDot.setVisibility(View.GONE);
                        }
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
            case R.id.llCarFlowRecharge:
//                startActivity(new Intent(getActivity(), CarFlowPackageRechargeActivity.class));
                checkInitIsOk();
                break;

            case R.id.ivScan:
                IntentIntegrator.forSupportFragment(SettingMainFragment.this)
                        .setCaptureActivity(ScanActivity.class)
                        .setPrompt("")
                        .initiateScan();
//                checkCcid();
                break;
        }
    }

    /**
     * 检查车机GPRS初始化是否成功
     */
    private void checkInitIsOk() {
        OkGo.<String>post(URLConfig.getCAR_CHECK_INIT_IS_OK())
                .params("carid", carid)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        parseCheckInitIsOk(response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        LogUtils.e(response);
                        loadSuccessUI();
                    }
                });
    }

    private void parseCheckInitIsOk(Response<String> response) {
        String body = response.body();
        Gson gson = new Gson();
        CheckInitInfo info = gson.fromJson(body, CheckInitInfo.class);
        if (info != null) {
            if (info.code == 0) {
                if (info.data.status == 3) {
                    countDataPackage(2);
                } else {
                    Intent intent = new Intent(mCtx, InitCarSimActivity.class);
                    intent.putExtra("carid",carid);
                    intent.putExtra("ccid",info.data.ccid);
                    startActivity(intent);
                }
            } else {
                ToastUtils.showShort(info.error);
            }

        }
    }

    //    private String carid = "2216301";
    private int carid;

    /**
     * 检查ccid是否已经绑定
     */
    private void checkCcid(String ccid) {
        loadingDataUI();
        OkGo.<String>post(URLConfig.getCAR_CHECK_CCID_URL())
                .params("carid", carid)
                .params("ccid", ccid)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        LogUtils.e(response.body());
                        parseCheckJson(response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        loadSuccessUI();
                        LogUtils.e(response);
                    }
                });
    }

    private void parseCheckJson(Response<String> response) {
        String body = response.body();
        Gson gson = new Gson();
        CheckBindInfo checkBindInfo = gson.fromJson(body, CheckBindInfo.class);
        if (checkBindInfo.code == 0) {
            //成功
            switch (checkBindInfo.data) {
                case 0:
                    loadSuccessUI();
                    //操作失败
                    break;
                case 1:
                    loadSuccessUI();
                    //未绑定
                    showUnBindDialog();
                    break;
                case 2:
                    //已绑定（自己的车）
                    checkInitIsOk();
                    break;
                case 3:
                    loadSuccessUI();
                    //已绑定（非自己的车）
                    ToastUtils.showShort("该车机已被绑定，不能重复绑定");
                    break;
            }

        } else {
            loadSuccessUI();
            //失败
            ToastUtils.showShort(checkBindInfo.error);
        }
    }

    private void showUnBindDialog() {
        PopBoxCreat.createDialogNotitle(mCtx, "温馨提示", "您的账户尚未与车机建立绑定关系，请您先进行车机绑定，并进行车机流量激活！", "取消", "确定", new PopBoxCreat.DialogWithTitleClick() {
            @Override
            public void onLeftClick() {

            }

            @Override
            public void onRightClick() {
                Intent intent = new Intent(mCtx, CheckPhoneActivity.class);
                intent.putExtra("carid", carid);
                intent.putExtra("ccid", ccid);
                startActivity(intent);
                //                bindSim();
            }
        });
    }


//    public static final String ccid = "89860429111890177338";

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null && result.getContents() != null) {
//            UUToast.showUUToast(mCtx, result.getContents());
            String contents = result.getContents();
            decodeQRcode(contents);
        }
    }

    private void decodeQRcode(String strHex) {
        Log.e("result===", strHex);
        byte[] bytes = Base64.decode(strHex, Base64.DEFAULT);
        //        byte[] bytes = hexString2Bytes(strHex);
        String s = bytes2HexString(bytes);
        Log.e("===", s);
        LogUtils.e(Arrays.toString(bytes));
        if (bytes[0] != (byte) 0xcc || bytes[1] != (byte) 0xd7) {
            LogUtils.e("头错误");
            Toast.makeText(getContext(), "协议头不是我们的协议头，所以此数据不做解析", Toast.LENGTH_LONG).show();
            return;
        }
        int version = ((int) bytes[2]) >> 3;
        LogUtils.e("版本" + version);

        int secType = bytes[2] & 0x07;
        LogUtils.e("加密类型" + secType);
        byte[] mask = new byte[]{0, 0};
        //        if (secType == 1) {
        mask[0] = bytes[3];
        mask[1] = bytes[4];
        //        }


        int bodyLen = bytes[7] << 8 | bytes[8];
        LogUtils.e("数据长度" + bodyLen);
        int crc = bytes[8 + bodyLen] << 24 | bytes[8 + bodyLen + 1] << 16 | bytes[8 + bodyLen + 2] << 8 | bytes[8 + bodyLen + 3];
        LogUtils.e("crc:" + crc);

        byte[] raw = new byte[bodyLen];
        //        if (secType == 1) {
        for (int i = 0; i < bodyLen; i++) {
            raw[i] = (byte) (bytes[9 + i] ^ mask[i % 2]);
        }
        //        }
        //        String s2 = bytes2HexString(raw);
        //        Log.e(Tag, "s2-----------" + s2);
        //校验
        byte[] mbs = Arrays.copyOfRange(bytes, 7, 9);
        String s1 = bytes2HexString(mbs);
        Integer len = Integer.valueOf(s1, 16);
        //        byte[] content = Arrays.copyOfRange(bytes, 9, 9 + len);
        byte[] regex = Arrays.copyOfRange(bytes, 9 + len, len + 13);
        //        String crc32 = Utils.getCRC32(raw);
        //        byte[] crc32Bys = intToBytes(crc32);
        CRC32 crc321 = new CRC32();
        crc321.update(raw);
        long value = crc321.getValue();
        String localCrc32 = Long.toHexString(value).toUpperCase();
        LogUtils.e("CRC32One----：" + Long.toHexString(value).toUpperCase());
        String re = bytes2HexString(regex);
        //        Log.e(Tag, "CRC32Re----：" + Long.toHexString(value1).toUpperCase());
        //        String crc1 = bytes2HexString(crc32Bys);
        LogUtils.e("校验码是：" + re);
        LogUtils.e("crc32是：" + localCrc32);
        if (!re.equalsIgnoreCase(localCrc32)) {
            Toast.makeText(getActivity(), "crc校验不通过，所以此数据不做解析", Toast.LENGTH_LONG).show();
            return;
        }


        String kv = new String(raw);
        LogUtils.e("数据是: " + kv);
        String[] str = kv.split("&");
        for (String aStr : str) {
            if (aStr.startsWith("ccid")) {
                ccid = aStr;
            }
            if (aStr.startsWith("time")) {
                scanTime = aStr;
            }
        }
        if (!TextUtils.isEmpty(ccid)) {
            ccid = ccid.trim().substring(ccid.indexOf("=") + 1);
        } else {
            ccid = "";
        }
        if (!TextUtils.isEmpty(scanTime)) {
            scanTime = scanTime.trim().substring(scanTime.indexOf("=") + 1);
            LogUtils.e("scanTime == " + scanTime);
            long time = Long.parseLong(scanTime);
            if ((System.currentTimeMillis() / 1000 - time) <= 3000) {
                checkCcid(ccid);
            } else {
                ToastUtils.showShort("二维码已过期");
            }
        }

    }

    public static String bytes2HexString(byte[] b) {
        String r = "";

        for (int i = 0; i < b.length; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            r += hex.toUpperCase();
        }

        return r;
    }

    /**
     * 获取经销商信息
     */
    @Override
    public void loadData() {
        showUserUI();
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

    /**
     * 是否支持T-box流量充值（V140）
     */
    private void isSupportTData() {
        String isSupportTDataUrl = URLConfig.getM_ISSUPPORTTDATA();
        OkGo.<String>post(isSupportTDataUrl)
                .params("client_id", URLConfig.getClientID())
                .params("token", LoginInfo.getAccess_token())
                .params("deviceIdString", LoginInfo.getDeviceidstring())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        parseIsSupport(response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        LogUtils.e(response);
                    }
                });
    }

    private void parseIsSupport(Response<String> response) {
        String body = response.body();
        try {
            JSONObject jsonObject = new JSONObject(body);
            JSONObject object = jsonObject.getJSONObject("data");
            if (object != null) {
                boolean isSupport = object.optBoolean("isSupport", false);
                if (isSupport) {
                    countDataPackage(0);
                }
            }

        } catch (Exception e) {
            LogUtils.e(e);
        }

    }

    /**
     * 判断T-box、车机是否配置流量产品（V140）
     */
    private void countDataPackage(final int type) {
        String countDataPackageUrl = URLConfig.getM_COUNTDATAPACKGE();
        OkGo.<String>post(countDataPackageUrl)
                .params("client_id", URLConfig.getClientID())
                .params("token", LoginInfo.getAccess_token())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        loadSuccessUI();
                        parseCountDataPackage(response, type);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        loadSuccessUI();
                    }
                });
    }

    boolean isCountData = false;

    private void parseCountDataPackage(Response<String> response, int type) {
        LogUtils.e("判断T-box、车机是否配置流量产品----type---" + type);
        String body = response.body();
        try {
            JSONObject jsonObject = new JSONObject(body);
            int code = jsonObject.optInt("code",-1);
            String msg = jsonObject.optString("msg","");
            if (code == BaseResponseInfo.NO_TOKEN){
                ActivityControl.onTokenDisable();
                ToastUtils.setGravity(Gravity.CENTER, 0, 0);
                ToastUtils.setBackgroundColor(R.drawable.toast_bg);
                ToastUtils.setMessageColor(Color.WHITE);
                ToastUtils.showLong(msg);
                return;
            }
            JSONObject data = jsonObject.getJSONObject("data");
            if (data != null) {
                int tboxDataNum = data.optInt("tboxDataNum", 0);
                String machineDataNum = data.optString("machineDataNum", "");
                int tboxRenewNum = data.optInt("tboxRenewNum", 0);
                switch (type){
                    case 0:
                        if (tboxDataNum != 0){
                            if (isCountData) {
                                llFlowRecharge.setVisibility(View.VISIBLE);
                                lineFlow.setVisibility(View.VISIBLE);
                                llCarFlowRecharge.setVisibility(View.VISIBLE);
                                lineCarFlow.setVisibility(View.VISIBLE);
                                tvFlow.setText("T-box流量充值");
                                tvCarFlow.setText("车机流量充值");
                            } else {
                                isCountData = true;
                                llFlowRecharge.setVisibility(View.VISIBLE);
                                lineFlow.setVisibility(View.VISIBLE);
                                llCarFlowRecharge.setVisibility(View.GONE);
                                lineCarFlow.setVisibility(View.GONE);
                                tvFlow.setText("流量充值");
                            }
                            initFlowInfo(type);
                        }
                        break;
                    case 1:
                        if (!TextUtils.isEmpty(machineDataNum) && Integer.parseInt(machineDataNum) != 0){
                            if (isCountData) {
                                llFlowRecharge.setVisibility(View.VISIBLE);
                                lineFlow.setVisibility(View.VISIBLE);
                                llCarFlowRecharge.setVisibility(View.VISIBLE);
                                lineCarFlow.setVisibility(View.VISIBLE);
                                tvFlow.setText("T-box流量充值");
                                tvCarFlow.setText("车机流量充值");
                            } else {
                                isCountData = true;
                                llFlowRecharge.setVisibility(View.GONE);
                                lineFlow.setVisibility(View.GONE);
                                llCarFlowRecharge.setVisibility(View.VISIBLE);
                                lineCarFlow.setVisibility(View.VISIBLE);
                                tvCarFlow.setText("流量充值");
                            }
                            initFlowInfo(type);
                        }
                        break;
                    case 2:
                        if (!TextUtils.isEmpty(machineDataNum) && Integer.parseInt(machineDataNum) != 0) {
                            Intent intent = new Intent(mCtx, CarFlowPackageRechargeActivity.class);
                            startActivity(intent);
                        }else {
                            ToastUtils.showShort("暂未获取到商品列表");
                        }
                        break;
                }
            }
        } catch (Exception e) {
            LogUtils.e(e);
        }

    }
}

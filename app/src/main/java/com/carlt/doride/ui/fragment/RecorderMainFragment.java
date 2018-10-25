package com.carlt.doride.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.carlt.chelepie.appsdk.AppsdkUtils;
import com.carlt.chelepie.control.RecorderControl;
import com.carlt.chelepie.control.WIFIControl;
import com.carlt.chelepie.manager.DeviceConnectManager;
import com.carlt.chelepie.systemconfig.ActionConfig;
import com.carlt.chelepie.utils.PlayListener;
import com.carlt.chelepie.view.EditDialog2;
import com.carlt.chelepie.view.WIFIConnectDialog;
import com.carlt.chelepie.view.WifiListDialog;
import com.carlt.chelepie.view.activity.FullLiveActivity;
import com.carlt.chelepie.view.activity.ManagePieActivity;
import com.carlt.chelepie.view.activity.MyMediaListActivity;
import com.carlt.chelepie.view.gl.HHVideoView;
import com.carlt.chelepie.view.gl.HVideoView;
import com.carlt.chelepie.view.gl.IVideoView;
import com.carlt.doride.DorideApplication;
import com.carlt.doride.R;
import com.carlt.doride.base.BaseFragment;
import com.carlt.doride.base.BeforeGoToBackground;
import com.carlt.chelepie.view.UUDialogUpgrading;
import com.carlt.doride.control.ActivityControl;
import com.carlt.doride.data.BaseResponseInfo;
import com.carlt.doride.eventbus.FullScreenMessage;
import com.carlt.doride.model.LoginInfo;
import com.carlt.doride.protocolparser.BaseParser;
import com.carlt.doride.ui.view.PopBoxCreat;
import com.carlt.doride.ui.view.UUToast;
import com.carlt.doride.utils.FileUtil;
import com.carlt.doride.utils.Log;
import com.carlt.sesame.control.CPControl;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;

/**
 * Created by Marlon on 2018/10/16.
 */
public class RecorderMainFragment extends BaseFragment implements
        View.OnClickListener, DeviceConnectManager.NotifyListener, WIFIControl.WIFIConnectListener,
        BeforeGoToBackground {
    private static final String TAG = "RecorderMainFragment";
    View view;
    private View btnSet;// 标题左边按钮(记录仪设置)
    private View btnConnect;// 标题右边按钮

    private TextView mTxtAlbum;// 进入相册

    private View layReady;// 提示view
    private View layVideo;// 直播
    private IVideoView videoView;// 软解码播放器
    private TextView playView;
    private View proBar;// 加载进度条
    private ImageView imgFull;// 全屏
    private RelativeLayout videoLayout;

    UUDialogUpgrading mUpgradeDialog;
    WIFIConnectDialog mWIFIDialog;
    WIFIControl mWifiControl = null;
    String localUrl = null;
    boolean isLivePlay = false;
    EditDialog2 mDialog;
    private WifiListDialog mWifiListDialog;

    private static GotoMainIndexListener mGotoMainIndexListener;

    //点击了右上角
    private boolean isClickTitleRight = false;

    /**
     *  从不同页面返回的 code... 相当于 startActivityForResult 方法
     */
    private int backCode ;

    public final static void setmGotoMainIndexListener(
            GotoMainIndexListener gotoMainIndexListener) {
        mGotoMainIndexListener = gotoMainIndexListener;
    }



    @Override
    protected View inflateView(LayoutInflater inflater) {
        view = inflater.inflate(R.layout.activity_recordermain, null, false);
        EventBus.getDefault().register(this);
        return view;
    }

    @Override
    public void init(View view) {
        btnSet = view.findViewById(R.id.recordermain_img_set);
        btnConnect = view.findViewById(R.id.recorder_main_connect);
        mTxtAlbum = view.findViewById(R.id.recordermain_txt_album);

        layVideo = view.findViewById(R.id.recordermain_lay_play);
        layReady = view.findViewById(R.id.recordermain_lay_ready);
        proBar = view.findViewById(R.id.recordermain_proBar);
        imgFull = view.findViewById(R.id.recordermain_img_full);
        playView = view.findViewById(R.id.recordermain_txt_play);

        videoLayout = view.findViewById(R.id.recordermain_videov_layout);

        btnConnect.setOnClickListener(this);
        btnSet.setOnClickListener(this);
        imgFull.setOnClickListener(this);
        playView.setOnClickListener(this);
        mTxtAlbum.setOnClickListener(this);
        mWifiControl = WIFIControl.getInstance();
        registerBeforeGoToBackGround(this);

        try {
            //Todo 看下逻辑
            localUrl = getActivity().getIntent().getExtras().getString("filePath");
        } catch (Exception e) {
        }

        loadSuccessUI();
        showVideoLay(false);
        DorideApplication.getInstanse().setMonitor(true);
    }

    @Override
    public void loadSuccessUI() {
        super.loadSuccessUI();
    }

    private void showVideoLay(boolean flag) {
        if (flag) {
            proBar.setVisibility(View.VISIBLE);
            layVideo.setVisibility(View.VISIBLE);
            videoLayout.setVisibility(View.VISIBLE);
            layReady.setVisibility(View.GONE);
        } else {
            layVideo.setVisibility(View.GONE);
            videoLayout.setVisibility(View.INVISIBLE);
            layReady.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.recordermain_img_set:
                // 左上角
                Intent mIntent1 = new Intent(getActivity(),
                        ManagePieActivity.class);
                startActivity(mIntent1);
                break;

            case R.id.recorder_main_connect:
                //右上角
                if(!DeviceConnectManager.isDeviceConnect()){
                    showConnectDialog();
                    isClickTitleRight = true;
                    WIFIControl.StartConnectChelePai();
                }else{
                    UUToast.showUUToast(mCtx, "设备已连接");
                }

                break;
            case R.id.recordermain_img_full:
                // 全屏
                DorideApplication.getInstanse().setToFullFlag(true);
                Intent mIntent3 = new Intent(getActivity(), FullLiveActivity.class);

                startActivity(mIntent3);
                break;

            case R.id.recordermain_txt_album:
                // 我的记录仪媒体列表
                Intent mIntent4 = new Intent(getActivity(),
                        MyMediaListActivity.class);
                startActivity(mIntent4);
                break;

            case R.id.recordermain_txt_play:
                // / --点击播放按钮开始播放
                startPlay();
                break;
        }

    }

    private void startPlay() {
        isLivePlay = true;
        if (DeviceConnectManager.isDeviceConnect()) {
            showVideoLay(true);
            proBar.setVisibility(View.VISIBLE);
            RecorderControl.startMonitor(listener_monitor);
        } else {
            showConnectDialog();
            WIFIControl.StartConnectChelePai();
        }
    }

    private BaseParser.ResultCallback listener_monitor = new BaseParser.ResultCallback() {

        @Override
        public void onSuccess(BaseResponseInfo o) {
            Message msg = new Message();
            msg.what = 13;
            msg.obj = o;
            mHandler.sendMessage(msg);
        }

        @Override
        public void onError(BaseResponseInfo o) {
            Message msg = new Message();
            msg.what = 14;
            msg.obj = o;
            mHandler.sendMessage(msg);
        }
    };

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2:
                    // 固件升级成功
                    String info1 = (String) msg.obj;
                    UUToast.showUUToast(mCtx, info1
                            + ", 设备即将重启...");
                    break;
                case 3:
                    // 固件升级失败
                    String info2 = (String) msg.obj;
                    UUToast.showUUToast(mCtx, info2);
                    break;
                case 13:
                    // 通知直播成功
                    if (!mIsShowing) {
                        // 页面不显示 .. 不开启直播
                        break;
                    }
                    proBar.setVisibility(View.VISIBLE);
                    videoView.play();
                    break;
                case 14:
                    // 通知直播失败
                    UUToast.showUUToast(mCtx, "开启直播失败，请重试");
                    proBar.setVisibility(View.GONE);
                    showVideoLay(false);
                    isLivePlay = false;
                    break;
                case DeviceConnectManager.DEVICE_DISCONNECT:
                    // WIFI连接断开了 ，通知这里看是否重连
                    // isLivePlay = false;
                    dissmissConnectDialog();
                    showVideoLay(false);
                    UUToast.showUUToast(mCtx,
                            "网络发生异常中断，请检查您的网络环境并尝试重新连接");
                    break;
                    // action = 1002  Wifi连接成功后 通知DeviceConnectManager , 再次更新 状态
                case DeviceConnectManager.DEVICE_CONNECT:
                    UUToast.showUUToast(mCtx, "设备Wi-Fi已连接");
                    dissmissConnectDialog();
                    if (!mIsShowing) {
                        // 页面不显示 .. 不开启直播
                        break;
                    }
                    if (localUrl != null) {
                        // if (localUrl != null && DeviceIdUtils.getNeedUpdate()) {
                        RecorderControl.GetDeviceUpdate(mUpGradeCallback, localUrl);
                        mUpgradeDialog = new UUDialogUpgrading(mCtx);
                        mUpgradeDialog.setProgressing();
                        mUpgradeDialog.show();
                    } else {
                        //点击右上角连接按钮，只是连接，不开启直播
                        if(isClickTitleRight){
                            return;
                        }
                        if (isLivePlay) {
                            showVideoLay(true);
                            proBar.setVisibility(View.VISIBLE);
                            // 开启直播,添加 返回回调
                            RecorderControl.startMonitor(listener_monitor);
                        }
                    }
                    break;
                case DeviceConnectManager.DEVICE_CONNECT_TIMEOUT:
                    dissmissConnectDialog();
                    UUToast.showUUToast(mCtx,
                            "未能成功连接您的设备Wi-Fi，请检查您的网络环境并尝试重新连接");
                    showVideoLay(false);
                    break;
                case DeviceConnectManager.DEVICE_CONNECT_LOGINERR:
                    dissmissConnectDialog();
                    UUToast.showUUToast(mCtx,
                            "未能成功连接您的设备Wi-Fi，请检查您的网络环境并尝试重新连接");
                    showVideoLay(false);
                    break;
                    //10012
                case WIFIControl.WIFI_CONNECT_OK:
                    UUToast.showUUToast(mCtx, "Wi-Fi连接成功");
                    DeviceConnectManager.StartMessgeLoop();
                    break;
                case WIFIControl.WIFI_NOT_OPEN:
                    dissmissConnectDialog();
                    UUToast.showUUToast(mCtx,
                            "网络链接不可用，请先检查您的Wi-Fi是否已开启");
                    showVideoLay(false);
                    break;
                case WIFIControl.WIFI_NOT_FOUND:
                    dissmissConnectDialog();
                    UUToast.showUUToast(mCtx,
                            "未发现您的设备Wi-Fi，请检查设备是否正确安装");
                case WIFIControl.WIFI_CONNECT_TIMEOUT:
                    dissmissConnectDialog();
                    UUToast.showUUToast(mCtx,
                            "未能成功连接您的设备，请检查您的网络环境并尝试重新连接");
                    showVideoLay(false);
                    break;
                case WIFIControl.WIFI_CHELE_PWD_ERROR:
                    UUToast.showUUToast(mCtx,
                            "设备Wi-Fi密码错误，请重新输入密码");
                    dissmissConnectDialog();
                    showInputPwd();
                    showVideoLay(false);
                    break;
                case WIFIControl.WIFI_CHELE_DISCONNECT:
                    break;
                case WIFIControl.WIFI_NO_INFO:
                    UUToast.showUUToast(mCtx, "请选择设备Wi-Fi");
                    dissmissConnectDialog();
                    if (mWifiListDialog != null) {
                        mWifiListDialog.dismiss();
                    }
                    mWifiListDialog = PopBoxCreat.createDialogWifilist(
                            mCtx, mDialogWithEditClick);
                    mWifiListDialog.show();
                    break;
                case 1010:
                    mUpgradeDialog.setSucc();
                    break;
                case 1011:
                    mUpgradeDialog.setFail();
                    break;
                case 1012:
                    break;
                case 1013:
                    // 固件升级进度
                    int i1 = (Integer) msg.obj;
                    if (null != mUpgradeDialog) {
                        mUpgradeDialog.setProgressNum(i1);
                    }
                    break;
                case 1014:
                    UUToast.showUUToast(mCtx, "不好意思,播放出现问题");
                    proBar.setVisibility(View.VISIBLE);
                    AppsdkUtils.CMStop(ActionConfig.getSeqNum());
                    videoView.stop();
                    showVideoLay(false);
                    break;
                case 1015:
                    proBar.setVisibility(View.GONE);
                    break;
            }
        }

    };

    private PopBoxCreat.DialogWithEditClick mDialogWithEditClick = new PopBoxCreat.DialogWithEditClick() {

        @Override
        public void onRightClick(String editContent) {
            // 确定-连接车乐拍wifi--TODO
            // 连接wifi的代码还没有写
            if (WIFIControl.SSID_CONNECT.length() < 1
                    || WIFIControl.SSID_PWD.length() < 8) {
                WIFIControl.SSID_PWD = "";
                UUToast.showUUToast(mCtx, "设备密码错误，请重新输入密码");
                showInputPwd();
                return;
            }
            showConnectDialog();
            WIFIControl.StartConnectChelePai();
        }

        @Override
        public void onLeftClick(String editContent) {
            // 取消
            WIFIControl.SSID_CONNECT = "";
            WIFIControl.SSID_PWD = "";
        }
    };

    private void dissmissConnectDialog() {
        if (mWIFIDialog != null) {
            mWIFIDialog.dismiss();
        }
    }

    private void showConnectDialog() {
        dissmissConnectDialog();
        mWIFIDialog = new WIFIConnectDialog(mCtx);
        mWIFIDialog.show();
    }

    @Override
    public void onPause() {
        super.onPause();
        backCode = 0;
    }

    @Override
    public void onStop() {
        super.onStop();
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Logger.e(hidden + "========================");
        if (!hidden) {

            //测试代码开始
            LoginInfo.isDrivingRecorder = true;
            //测试代码结束
            if (!LoginInfo.isDrivingRecorder) {
                PopBoxCreat.DialogWithTitleClick click = new PopBoxCreat.DialogWithTitleClick() {

                    @Override
                    public void onRightClick() {
                        // 重新登录-回到登录页
                        ActivityControl.onLogout(mCtx);
                    }

                    @Override
                    public void onLeftClick() {
                        // 返回主页面
                        mGotoMainIndexListener.gotoMianIndex();
                    }
                };
                PopBoxCreat.createDialogWithNodismiss(mCtx,
                        "温馨提示", "您的爱车可能未绑定大乘智享设备，请联系您的经销商进行设备绑定", "", "返回主页",
                        "重新登录", click);

            } else {
                mIsShowing = true;
                doSomeForResume();
            }
        }else {
            doSomeForPause();
        }
    }

    /**
     *
     */
    private void doSomeForResume(){
        DorideApplication.getInstanse().setToFullFlag(false);
        Logger.e("onResume",
                "RecorderMainFragment..........................................................");
        videoLayout.removeAllViews();
        videoView = DorideApplication.getInstanse().getVideoView();
        boolean supportopenGLES20 = DorideApplication.getInstanse().supportopenGLES20;
        if (supportopenGLES20){
            if (((HHVideoView)videoView).getParent()!=null){
                ((ViewGroup)((HHVideoView)videoView).getParent()).removeView((HHVideoView)videoView);
            }
        }else {
            if (((HVideoView)videoView).getParent()!=null){
                ((ViewGroup)((HVideoView)videoView).getParent()).removeView((HVideoView)videoView);
            }
        }
        videoLayout.addView((View) videoView,
                new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));
        videoView.setmListener(mPlayListener);
        // 设置监听WIFI变化
        WIFIControl.rigisterWIFIConnectListener(this);
        DeviceConnectManager.addNotifyListener(this);

        // 如果是从直播全屏页面返回,则再次开启直播
        if(backCode == FullLiveActivity.BACK_CODE){
            startPlay();
        }

    }

    private void doSomeForPause(){
        backCode = 0;
        mIsShowing = false;
        if (!DorideApplication.getInstanse().isToFullFlag()) {// 如果是跳转到全屏幕页面，则不停止接收数据
            AppsdkUtils.CMStop(ActionConfig.getSeqNum());
        }
        if (videoView != null) {
            videoView.stop();
            videoLayout.removeAllViews();
            showVideoLay(false);
        }
        WIFIControl.unRigisterWIFIConnectListener(this);
        DeviceConnectManager.removeNotifyListener(this);
        dissmissConnectDialog();
    }

    @Override
    public void onResume() {
        super.onResume();
        doSomeForResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("LoginErr", "......RecorderMainFragment......finish........了");
        unRegisterBeforeGoToBackGround(this);
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void loadData() {

    }

    @Override
    public void notifyAction(int action) {
        if (!mIsShowing) {
            // 页面不显示 .. 不开启直播
            return;
        }
      // 成功返回 1002
       Logger.e("notifyAction===========================" + action);
        mHandler.sendEmptyMessage(action);
    }

    @Override
    public void onWIFIChange(int action) {

        if (!mIsShowing) {
            // 页面不显示 .. 不开启直播
            return;
        }
        mHandler.sendEmptyMessage(action);
    }

    RecorderControl.GetTranslateProgressCallback mUpGradeCallback = new RecorderControl.GetTranslateProgressCallback() {

        @Override
        public void onUpdateProgress(int progress) {

            Message msg = Message.obtain();
            msg.what = 1013;
            msg.obj = progress;
            mHandler.sendMessage(msg);
        }

        @Override
        public void onTranslateProgress(Object progress) {
            mHandler.sendEmptyMessage(1012);
        }

        @Override
        public void onFinished(Object o1) {
            mHandler.sendEmptyMessage(1010);
            FileUtil.deleteFile(new File(localUrl));
            localUrl = null;
        }

        @Override
        public void onErro(Object o) {
            mHandler.sendEmptyMessage(1011);
            localUrl = null;
        }
    };

    PlayListener mPlayListener = new PlayListener() {

        @Override
        public void onError() {
            // --播放期间出现问题
            mHandler.sendEmptyMessage(1014);
        }

        @Override
        public void onReady() {
            // ---已经准备好了
            mHandler.sendEmptyMessage(1015);
        }
    };

    @Override
    public void doBeforeGoToBackground() {
//		WIFIControl.DisConnectChelePai();
    }

    public void showInputPwd() {
        if (mDialog != null) {
            mDialog.dismiss();
        }

        mDialog = PopBoxCreat.createDialogWithedit2(mCtx,
                WIFIControl.SSID_CONNECT, "请输入", InputType.TYPE_CLASS_TEXT
                        | InputType.TYPE_TEXT_VARIATION_PASSWORD, "取消", "确定",
                new PopBoxCreat.DialogWithEditClick() {

                    @Override
                    public void onRightClick(String editContent) {
                        if (editContent != null && editContent.length() > 7) {
                            WIFIControl.SSID_PWD = editContent;
                            showConnectDialog();
                            WIFIControl.StartConnectChelePai();
                        } else {
                            UUToast.showUUToast(mCtx,
                                    "设备密码错误，请重新输入密码");
                            showInputPwd();
                        }
                    }

                    @Override
                    public void onLeftClick(String editContent) {
                        WIFIControl.SSID_CONNECT = "";
                        WIFIControl.SSID_PWD = "";
                    }
                });
        mDialog.show();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // TODO Auto-generated method stub
        super.onSaveInstanceState(outState);
    }

    public interface GotoMainIndexListener {
        void gotoMianIndex();
    }

    //eventBus 方法

    @Subscribe
    public void onEvent(FullScreenMessage message){
        backCode = message.message;

    }
}

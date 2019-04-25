package com.carlt.doride.ui.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.carlt.chelepie.control.WIFIControl;
import com.carlt.doride.DorideApplication;
import com.carlt.doride.R;
import com.carlt.doride.base.BaseFragment;
import com.carlt.doride.control.CPControl;
import com.carlt.doride.data.BaseResponseInfo;
import com.carlt.doride.data.remote.AirMainInfo;
import com.carlt.doride.data.remote.CarStateInfo;
import com.carlt.doride.data.remote.RemoteFunInfo;
import com.carlt.doride.data.remote.RemoteMainInfo;
import com.carlt.doride.http.retrofitnet.ApiRetrofit;
import com.carlt.doride.http.retrofitnet.BaseMvcObserver;
import com.carlt.doride.http.retrofitnet.model.GetCarInfo;
import com.carlt.doride.http.retrofitnet.model.RemoteCarStateInfo;
import com.carlt.doride.http.retrofitnet.model.RemoteCarStateInfoPresenter;
import com.carlt.doride.http.retrofitnet.model.RemoteCommonInfo;
import com.carlt.doride.http.retrofitnet.model.UserInfo;
import com.carlt.doride.protocolparser.BaseParser;
import com.carlt.doride.protocolparser.CarOperationConfigParser;
import com.carlt.doride.systemconfig.URLConfig;
import com.carlt.doride.ui.activity.login.ActivateAccActivity;
import com.carlt.doride.ui.activity.login.ActivateStepActivity;
import com.carlt.doride.ui.activity.login.UpDateActivity;
import com.carlt.doride.ui.activity.remote.RemoteLogActivity;
import com.carlt.doride.ui.activity.remote.RemotePswResetActivity3;
import com.carlt.doride.ui.activity.setting.VcodeResetRemotePasswdActivity;
import com.carlt.doride.ui.adapter.RemoteStatesAdapter;
import com.carlt.doride.ui.view.MyGridView;
import com.carlt.doride.ui.view.PopBoxCreat;
import com.carlt.doride.ui.view.RemoteGridControl;
import com.carlt.doride.ui.view.UUAirConditionDialog;
import com.carlt.doride.ui.view.UUDialogRemote;
import com.carlt.doride.ui.view.UUToast;
import com.carlt.doride.ui.view.UUToastOpt;
import com.carlt.doride.ui.view.UUToastOptError;
import com.carlt.doride.ui.view.passwordtextview.SelectPopupWindow;
import com.carlt.doride.utils.MyParse;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by liu on 2018/3/16.
 * 远程页面
 */

public class RemoteMainFragment extends BaseFragment implements
        AdapterView.OnItemClickListener, View.OnClickListener, View.OnTouchListener, SelectPopupWindow.OnPopWindowClickListener, WIFIControl.WIFIConnectListener {
    public final static String ACTION_REMOTE_SETPSW = "com.carlt.doride.action_remote_setpsw";

    public final static String ACTION_REMOTE_RESETPSW = "com.carlt.doride.action_remote_resetpsw";

    public final static String ACTION_REMOTE_FORGETPSW = "com.carlt.doride.action_remote_forgetpsw ";


    private static String TAG = "RemoteMainFragment";
    private        View   view;
    //效果声音播放组件
    //    private PlayRadio mPlayRadio;

    private View mTxtState;// 车辆状态

    private View mTxtRecorder;// 远程记录

    private TextView mTxtUnspport;// 远程项都不支持时的话术

    private ImageView mImgStart;// 远程启动

    private ImageView mImgStop;// 远程熄火

    private View mViewStopmask;// 熄火按钮蒙版
    private View mViewUnsupport;// 远程项都不支持时的view

    private View mViewNormal;// 车辆状态未出现是的View

    // 汽车状态相关
    private View mViewState;// 汽车状态

    private MyGridView mGridViewState;// 汽车状态

    private ImageView mImgArrow;// 收起箭头

    private LinearLayout mRContainer;

    // 汽车状态相关结束
    private RemoteStatesAdapter mAdapterStates;

    private Dialog mDialog;

    private String password;

    private int selectedPos;// 被点击的item的pos

    boolean isReCall = false;

    UUAirConditionDialog airDialog;

    private long startTime;// 第一次点击按钮时间

    private boolean isFirstClick = true;// 是否是第一次点击

    private final static long INVALID_DURATION = 5 * 60 * 1000;// 密码实效时长

    private RemoteGridControl mRControl;

    //天窗对话框
    private UUDialogRemote          uuDialogRemote;
    private ArrayList<CarStateInfo> mCarStateDataList;
    private CarStateInfo            carStateInfo;
    private int                     remoteStatus;

    @Override
    public void onResume() {
        addDisposable(mApiService.getCarInfo(new HashMap<>()), new BaseMvcObserver<GetCarInfo>() {
            @Override
            public void onSuccess(GetCarInfo result) {
                GetCarInfo.getInstance().setCarInfo(result);
                remoteStatus = result.remoteStatus;
            }

            @Override
            public void onError(String msg) {

            }
        });
        //        remoteStatus = GetCarInfo.getInstance().remoteStatus;
        super.onResume();
        //        CPControl.GetRemoteCarState(mListener_states);

        loadData();
    }

    @Override
    public void reTryLoadData() {
        loadData();
    }

    @Override
    protected View inflateView(LayoutInflater inflater) {
        view = inflater.inflate(R.layout.activity_remote_main, null, false);
        return view;
    }

    CarOperationConfigParser carOperationConfigParser;

    @Override
    public void loadData() {
        upgradeProgram();
        loadingDataUI();
        carOperationConfigParser = new CarOperationConfigParser<String>(new BaseParser.ResultCallback() {
            @Override
            public void onSuccess(BaseResponseInfo bInfo) {
                mViewError.setVisibility(View.GONE);
                fl_base_content.setVisibility(View.VISIBLE);
                DorideApplication.getInstanse().setRemoteMainInfo(carOperationConfigParser.getReturn());
                Logger.e(TAG, "onSuccess parser2 " + carOperationConfigParser.getReturn());
                loadSuss();
            }

            @Override
            public void onError(BaseResponseInfo bInfo) {
                Logger.e(TAG, "onError" + bInfo.toString());
                //                    actLoadError((BaseResponseInfo) bInfo);
                loadonErrorUI(bInfo);
                if (bInfo.getFlag() == 1014) {
                    mTxtRetryError.setText("点击激活设备");
                    mTxtRetryError.setOnClickListener(v -> {
                        Intent activateIntent = new Intent(mCtx, ActivateAccActivity.class);

                        startActivity(activateIntent);
                    });
                }
                errorUi();
            }
        });
        HashMap params2 = new HashMap();
        String m_car_curcarconfig_url = URLConfig.getM_CAR_CURCARCONFIG_URL();
        String replace = m_car_curcarconfig_url.replace("126", "130");
        carOperationConfigParser.executePost(replace, params2);

    }

    private void errorUi() {
        fl_base_content.setVisibility(View.GONE);
        mViewUnsupport.setVisibility(View.GONE);
        mViewError.setVisibility(View.VISIBLE);
    }

    private ArrayList<RemoteFunInfo> mRemoteFunInfos;

    private AirMainInfo mAirMainInfo1;

    private void loadSuss() {
        loadSuccessUI();
        RemoteMainInfo mRemoteMainInfo = DorideApplication.getInstanse().getRemoteMainInfo();
        //        Logger.e("RemoteMainInfo----" + mRemoteMainInfo);
        if (mRemoteMainInfo != null) {
            mAirMainInfo1 = mRemoteMainInfo.getmAirMainInfo();
            mRemoteFunInfos = mRemoteMainInfo.getmRemoteFunInfos();
            RemoteFunInfo startFunInfo = mRemoteMainInfo.getmFunInfoStart();
            RemoteFunInfo stopFunInfo = mRemoteMainInfo.getmFunInfoStop();
            if (startFunInfo != null && stopFunInfo != null) {

                //                mImgStop.setImageResource(R.mipmap.remote_stop_selected);
                //                mImgStop.setClickable(true);
                String stateStart = startFunInfo.getState();
                String stateStop = stopFunInfo.getState();
                int size = mRemoteFunInfos.size();
                Logger.e("mRemoteFunInfos.size()----" + size);
                if (size <= 0 && !stateStart.equals(RemoteFunInfo.STATE_SUPPORT)
                        && !stateStop.equals(RemoteFunInfo.STATE_SUPPORT)) {
                    mViewUnsupport.setVisibility(View.VISIBLE);
                    mTxtState.setVisibility(View.GONE);
                    mTxtRecorder.setVisibility(View.GONE);
                    mViewNormal.setVisibility(View.GONE);
                    return;
                } else {
                    mTxtState.setVisibility(View.VISIBLE);
                    mTxtRecorder.setVisibility(View.VISIBLE);
                    mViewUnsupport.setVisibility(View.GONE);
                    mViewNormal.setVisibility(View.VISIBLE);
                    mRContainer.removeAllViews();
                    mRControl.init5x7Views(mRContainer, mRemoteFunInfos);
                }
            } else {
                mImgStart.setImageResource(R.drawable.remote_start);
                //                mImgStop.setImageResource(R.mipmap.remote_stop_disable);
                //                mImgStop.setClickable(false);
                mViewStopmask.setVisibility(View.VISIBLE);
                mImgStart.setClickable(true);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mReceiver != null) {
            try {
                getActivity().unregisterReceiver(mReceiver);
            } catch (Exception e) {

            }


        }
    }

    @Override
    public void init(View view) {
        getActivateStatus("正在激活中", true);
        mTxtState = $ViewByID(R.id.state_car_iv);
        mTxtRecorder = $ViewByID(R.id.remote_history_iv);
        mTxtUnspport = (TextView) $ViewByID(R.id.remote_main_txt_unspport);
        mViewNormal = $ViewByID(R.id.remote_main_lay_normal);
        mViewState = $ViewByID(R.id.remote_main_lay_state);
        mViewUnsupport = $ViewByID(R.id.remote_main_lay_unspport);
        mGridViewState = (MyGridView) $ViewByID(R.id.remote_main_gridView_state);
        mImgArrow = (ImageView) $ViewByID(R.id.remote_main_img_arrow);
        mImgStart = (ImageView) $ViewByID(R.id.remote_main_img_start);
        mImgStop = (ImageView) $ViewByID(R.id.remote_main_img_stop);
        mViewStopmask = $ViewByID(R.id.remote_main_view_stop_mask);
        mRContainer = (LinearLayout) $ViewByID(R.id.remote_main_line_function);
        mRControl = new RemoteGridControl(getActivity());
        // 生成广播处理
        mReceiver = new RemoteReceiver();
        IntentFilter filter = new IntentFilter();
        //        mRControl.setOnItemClick(mItemClick1);
        filter.addAction(ACTION_REMOTE_SETPSW);
        filter.addAction(ACTION_REMOTE_RESETPSW);
        filter.addAction(ACTION_REMOTE_FORGETPSW);
        getActivity().registerReceiver(mReceiver, filter);

        mRControl.setOnItemClick(mItemClick1);
        mTxtState.setOnClickListener(this);
        mTxtRecorder.setOnClickListener(this);
        mImgStart.setOnClickListener(this);
        mImgStop.setOnClickListener(this);
        mViewStopmask.setOnClickListener(this);
        mViewNormal.setOnClickListener(this);
        mViewNormal.setOnTouchListener(this);
        mImgArrow.setOnClickListener(this);
        mViewUnsupport.setMinimumWidth(DorideApplication.ScreenWith);
        mViewUnsupport.setMinimumHeight(DorideApplication.ScreenHeight
                - DorideApplication.dpToPx(44) - DorideApplication.dpToPx(56));
    }


    RemoteFunInfo skyWindowsInfo = null;

    private BaseParser.ResultCallback mListener = new BaseParser.ResultCallback() {

        @Override
        public void onSuccess(BaseResponseInfo bInfo) {
            mHandler.sendEmptyMessage(0);
        }

        @Override
        public void onError(BaseResponseInfo o) {
            Message msg = new Message();
            msg.what = 1;
            msg.obj = o;
            mHandler.sendMessage(msg);
        }
    };

    /**
     * 主页上条目点击事件
     */
    View.OnClickListener mItemClick1 = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if (hasActivate()) {
                return;
            }
            RemoteFunInfo mInfo = (RemoteFunInfo) v.getTag();
            selectedPos = MyParse.parseInt(mInfo.getId());
            Logger.e("---" + mInfo.toString());
            if (selectedPos == 5) {
                //天窗
                skyWindowsInfo = mInfo;
            } else {
                skyWindowsInfo = null;
            }
            clickLogic();
        }
    };

    /**
     * 调用远程接口
     */
    private void GetResult() {
        if (selectedPos == 10) {
            showWaitingDialog("正在获取空调状态...");
        } else {
            showWaitingDialog(null);
        }
        switch (selectedPos) {
            case -2:
                //远程启动
                //                                CPControl.GetRemoteStart(mListener);
                remoteStartEngine();
                break;
            case -1:
                // 远程熄火
                //                CPControl.GetCancelRemoteStart(mListener);
                remoteStopEngine();
                break;

            case 1:
                // 远程解锁
                //                CPControl.GetRemoteLock("1", mListener);

            case 2:
                // 远程落锁
                //                CPControl.GetRemoteLock("2", mListener);
                dissmissWaitingDialog();
                if (null != mCarStateDataList && mCarStateDataList.size() != 0) {

                    carStateInfo = mCarStateDataList.get(0);
                }
                PopBoxCreat.createDialogRemote(getActivity(), "车锁", "解锁", "落锁", R.drawable.remote_unlock_selector, R.drawable.remote_lock_selector, new PopBoxCreat.onDialogRemoteClick() {
                    @Override
                    public void onItemOneClick(View v) {
                        showWaitingDialog(null);

                        //                        CPControl.GetRemoteLock("1", mListener);
                        remoteLock(1);
                    }

                    @Override
                    public void onItemTwoClick(View v) {
                        showWaitingDialog(null);
                        //                        CPControl.GetRemoteLock("2", mListener);
                        remoteLock(2);
                    }
                });
                break;
            case 4://升起车窗，关窗
                //
                //                CPControl.GetRemoteClosewin(mListener);
                //                break;
            case 3:
                // 降下车窗,开窗
                //                CPControl.GetRemoteOpenwin(mListener);

                dissmissWaitingDialog();
                if (null != mCarStateDataList && mCarStateDataList.size() != 0) {

                    carStateInfo = mCarStateDataList.get(2);
                }
                PopBoxCreat.createDialogRemote(getActivity(), "车窗", "开启", "关闭", R.drawable.remote_win_down_selector, R.drawable.remote_win_up_selector, new PopBoxCreat.onDialogRemoteClick() {
                    @Override
                    public void onItemOneClick(View v) {
                        showWaitingDialog(null);
                        //                        CPControl.GetRemoteChangeWinState("1", mListener);
                        remoteChangeWinState(1);
                    }

                    @Override
                    public void onItemTwoClick(View v) {
                        showWaitingDialog(null);
                        //                        CPControl.GetRemoteChangeWinState("2", mListener);
                        remoteChangeWinState(2);
                    }
                });
                break;
            case 5:
                // 天窗
                dissmissWaitingDialog();
                if (skyWindowsInfo != null) {
                    uuDialogRemote = new UUDialogRemote(getActivity());
                    uuDialogRemote.setTitleMsg("天窗");
                    uuDialogRemote.setItemClick(mItemClick2);
                    uuDialogRemote.show();
                    uuDialogRemote.LoadSuccess(skyWindowsInfo.getApiFieldLists());
                }
                break;
            case 6:
                // 远程开启天窗
                //                CPControl.GetRemoteSkylight("1", mListener);
                remoteSkylight(1);
                break;
            case 7:
                // 远程关闭天窗
                //                CPControl.GetRemoteSkylight("2", mListener);
                remoteSkylight(2);
                break;
            case 8:
                // 远程天窗开撬
                //                CPControl.GetRemoteSkylight("3", mListener);
                remoteSkylight(3);
                break;
            case 9:
                // 远程天窗关撬
                //                CPControl.GetRemoteSkylight("4", mListener);
                remoteSkylight(4);
                break;

            case 10:
                // 远程开启空调
                mHandler.sendEmptyMessage(6);
                break;
            case 11:
                // 闪灯鸣笛
                //                CPControl.GetCarLocating(mListener);
                remoteCarLocating();
                break;
            case 12:
                // 远程开启后备箱
                //                CPControl.GetRemoteTrunk(mListener);

                dissmissWaitingDialog();
                PopBoxCreat.createDialogRemote(getActivity(), "后备箱", "开启", "关闭", R.drawable.remote_open_truck_selector, R.drawable.remote_close_truck_selector, new PopBoxCreat.onDialogRemoteClick() {
                    @Override
                    public void onItemOneClick(View v) {
                        showWaitingDialog(null);
                        //                        CPControl.GetRemoteTrunk("1", mListener);
                        remoteTrunk(1);
                    }

                    @Override
                    public void onItemTwoClick(View v) {
                        showWaitingDialog(null);
                        //                        CPControl.GetRemoteTrunk("2", mListener);
                        remoteTrunk(2);
                    }
                });
                break;
            case 14:
                //             仅支持关闭后备箱
                //                CPControl.GetRemoteTrunk("2", mListener);
                remoteTrunk(2);
                break;
            case 15:
                //            仅支持打开后备箱
                //                CPControl.GetRemoteTrunk("1", mListener);
                remoteTrunk(1);
                break;
            case 13:
                // 远程座椅加热
                //                CPControl.GetRemoteChairHeating(mListener);
                //                UUToast.showUUToast(getActivity(), "车锁");

                dissmissWaitingDialog();
                PopBoxCreat.createDialogRemote(getActivity(), "座椅加热", "前排座椅加热", "关闭座椅加热", R.drawable.remote_open_seat_hot_selector, R.drawable.remote_close_seat_hot_selector, new PopBoxCreat.onDialogRemoteClick() {
                    @Override
                    public void onItemOneClick(View v) {
                        showWaitingDialog(null);
                        //                        CPControl.GetRemoteChairHeating("1", mListener);
                        remoteChairHeating(1);
                    }

                    @Override
                    public void onItemTwoClick(View v) {
                        showWaitingDialog(null);
                        //                        CPControl.GetRemoteChairHeating("2", mListener);
                        remoteChairHeating(2);
                    }
                });
                break;

        }
    }

    private void remoteCarStateAir() {
        HashMap remoteCommonParams = ApiRetrofit.getRemoteCommonParams();

        HashMap<String, Object> map = new HashMap<>();
        map.put("base", remoteCommonParams);
        addDisposable(mApiService.carState(map), new BaseMvcObserver<RemoteCarStateInfo>() {

            @Override
            public void onSuccess(RemoteCarStateInfo result) {
                dissmissWaitingDialog();
                if (result.err == null) {
                    parseData(result);
                } else {
                    UUToast.showUUToast(mCtx, result.err.msg);
                }
            }

            @Override
            public void onError(String msg) {
                dissmissWaitingDialog();
                LogUtils.e(msg);
                UUToast.showUUToast(mCtx, msg);
            }
        });

    }

    private void parseData(RemoteCarStateInfo result) {
        boolean isGetCurrentTempSuccess;
        if (result.AC != 2) {
            String acTemp = result.ACTemp;
            if (TextUtils.isEmpty(acTemp)) {
                acTemp = "0.0";
                isGetCurrentTempSuccess = false;
            } else {
                if (acTemp.equals("0.0")) {
                    acTemp = "0.0";
                    isGetCurrentTempSuccess = false;
                } else {
                    isGetCurrentTempSuccess = true;
                }
            }
            mAirMainInfo1.setCurrentTemp(acTemp);
            Log.e("info", "temp==------------" + acTemp);
            mAirMainInfo1.setGetCurrentTempSuccess(isGetCurrentTempSuccess);
            String airState = String.valueOf(result.AC);
            ArrayList<RemoteFunInfo> remoteFunInfos = mAirMainInfo1
                    .getmRemoteFunInfos();
            for (int i = 0; i < remoteFunInfos.size(); i++) {
                RemoteFunInfo item = remoteFunInfos.get(i);
                String id = item.getId();
                if (id.equals(airState)) {
                    item.setSelect(true);
                    //                            break;
                } else {
                    item.setSelect(false);
                }
            }
            mAirMainInfo1.setState(airState);
            //            mListener_temp.onSuccess(airMainInfo);
        } else {
            mAirMainInfo1.setCurrentTemp("0.0");
            mAirMainInfo1.setGetCurrentTempSuccess(false);
            mAirMainInfo1.setState("-1");
            //            mListener_temp.onError(mBaseResponseInfo);
        }
        //        AirMainInfo mAirMainInfo2 = (AirMainInfo) msg.obj;
        Logger.e("---" + mAirMainInfo1.toString());
        if (mAirMainInfo1 != null) {
            if (!isReCall) {
                if (airDialog == null || !airDialog.isShowing()) {

                    airDialog = new UUAirConditionDialog(
                            getActivity(), mAirMainInfo1);
                    airDialog.mListener = mListener;
                    airDialog.mHandler = mHandler;
                    airDialog.show();
                }
            } else {
                airDialog.reCall();
            }
        }

    }

    /**
     * 获取车辆状态
     */
    private void remoteCarState() {
        HashMap remoteCommonParams = ApiRetrofit.getRemoteCommonParams();

        HashMap<String, Object> map = new HashMap<>();
        map.put("base", remoteCommonParams);
        addDisposable(mApiService.carState(map), new BaseMvcObserver<RemoteCarStateInfo>() {

            @Override
            public void onSuccess(RemoteCarStateInfo result) {
                dissmissWaitingDialog();
                if (result.err == null) {
                    RemoteCarStateInfoPresenter rcsp = new RemoteCarStateInfoPresenter(result);
                    mCarStateDataList = rcsp.parser();
                    if (mCarStateDataList != null && mCarStateDataList.size() > 0) {
                        mGridViewState.setNumColumns(3);
                        if (mAdapterStates == null) {
                            mAdapterStates = new RemoteStatesAdapter(
                                    getActivity(), mCarStateDataList);
                            mGridViewState.setAdapter(mAdapterStates);
                        } else {
                            mAdapterStates.setmDataList(mCarStateDataList);
                            mAdapterStates.notifyDataSetChanged();
                        }
                        dissmissWaitingDialog();
                        mViewState.setVisibility(View.VISIBLE);
                    } else {
                        dissmissWaitingDialog();
                        mViewState.setVisibility(View.GONE);
                        UUToast.showUUToast(mCtx, "暂未获取到车辆状态数据");
                    }
                } else {
                    UUToast.showUUToast(mCtx, result.err.msg);
                }
            }

            @Override
            public void onError(String msg) {
                dissmissWaitingDialog();
                LogUtils.e(msg);
                UUToast.showUUToast(mCtx, msg);
            }
        });
    }

    /**
     * 一键寻车
     */
    private void remoteCarLocating() {
        HashMap remoteCommonParams = ApiRetrofit.getRemoteCommonParams();

        HashMap<String, Object> map = new HashMap<>();
        map.put("base", remoteCommonParams);
        map.put("moveDeviceName", DorideApplication.MODEL_NAME);
        addDisposable(mApiService.CarLocating(map), new BaseMvcObserver<RemoteCommonInfo>() {
            @Override
            public void onSuccess(RemoteCommonInfo result) {
                //                dissmissWaitingDialog();
                //
                //                if (result.err == null) {
                //                    if (TextUtils.isEmpty(result.msg)) {
                //                        UUToastOpt.showUUToast(mCtx, "操作成功");
                //                    } else {
                //                        UUToastOptError.showUUToast(mCtx, result.msg);
                //                    }
                //                } else {
                //                    UUToastOptError.showUUToast(mCtx, result.err.msg);
                //                }
                Message message = mHandler.obtainMessage();
                message.what = Remote_Result;
                message.obj = result;
                mHandler.sendMessage(message);

            }

            @Override
            public void onError(String msg) {
                //                dissmissWaitingDialog();
                //                LogUtils.e(msg);
                //                UUToastOptError.showUUToast(mCtx, msg);
                mHandler.sendEmptyMessage(Remote_Err);
            }
        });
    }

    /**
     * @param state
     *         1开启，2关闭
     */
    private void remoteChairHeating(int state) {

        HashMap remoteCommonParams = ApiRetrofit.getRemoteCommonParams();

        HashMap<String, Object> map = new HashMap<>();
        map.put("base", remoteCommonParams);
        map.put("moveDeviceName", DorideApplication.MODEL_NAME);
        map.put("rshoc", state);
        addDisposable(mApiService.ChairHeating(map), new BaseMvcObserver<RemoteCommonInfo>() {
            @Override
            public void onSuccess(RemoteCommonInfo result) {
                //                dissmissWaitingDialog();

                //                if (result.err == null) {
                //                    if (TextUtils.isEmpty(result.msg)) {
                //                        UUToastOpt.showUUToast(mCtx, "操作成功");
                //                    } else {
                //                        UUToastOptError.showUUToast(mCtx, result.msg);
                //                    }
                //                } else {
                //                    UUToastOptError.showUUToast(mCtx, result.err.msg);
                //                }
                Message message = mHandler.obtainMessage();
                message.what = Remote_Result;
                message.obj = result;
                mHandler.sendMessage(message);
            }

            @Override
            public void onError(String msg) {
                //                dissmissWaitingDialog();
                //                LogUtils.e(msg);
                //                UUToastOptError.showUUToast(mCtx, msg);
                mHandler.sendEmptyMessage(Remote_Err);
            }
        });
    }

    /**
     * @param state
     *         1开启，2关闭
     */
    private void remoteTrunk(int state) {
        HashMap remoteCommonParams = ApiRetrofit.getRemoteCommonParams();

        HashMap<String, Object> map = new HashMap<>();
        map.put("base", remoteCommonParams);
        map.put("moveDeviceName", DorideApplication.MODEL_NAME);
        map.put("rtlu", state);
        addDisposable(mApiService.RemoteTrunk(map), new BaseMvcObserver<RemoteCommonInfo>() {
            @Override
            public void onSuccess(RemoteCommonInfo result) {
                //                dissmissWaitingDialog();

                //                if (result.err == null) {
                //                    if (TextUtils.isEmpty(result.msg)) {
                //                        UUToastOpt.showUUToast(mCtx, "操作成功");
                //                    } else {
                //                        UUToastOptError.showUUToast(mCtx, result.msg);
                //                    }
                //                } else {
                //                    UUToastOptError.showUUToast(mCtx, result.err.msg);
                //                }
                Message message = mHandler.obtainMessage();
                message.what = Remote_Result;
                message.obj = result;
                mHandler.sendMessage(message);
            }

            @Override
            public void onError(String msg) {
                //                dissmissWaitingDialog();
                //                LogUtils.e(msg);
                //                UUToastOptError.showUUToast(mCtx, msg);
                mHandler.sendEmptyMessage(Remote_Err);
            }
        });
    }

    /**
     * @param state
     *         1:开启，2：关闭 ，3:天窗开翘，4: 天窗关翘
     */
    private void remoteSkylight(int state) {
        HashMap remoteCommonParams = ApiRetrofit.getRemoteCommonParams();

        HashMap<String, Object> map = new HashMap<>();
        map.put("base", remoteCommonParams);
        map.put("moveDeviceName", DorideApplication.MODEL_NAME);
        map.put("rwoc", state);
        addDisposable(mApiService.SkyLight(map), new BaseMvcObserver<RemoteCommonInfo>() {
            @Override
            public void onSuccess(RemoteCommonInfo result) {
                //                dissmissWaitingDialog();
                //
                //                if (result.err == null) {
                //                    if (TextUtils.isEmpty(result.msg)) {
                //                        UUToastOpt.showUUToast(mCtx, "操作成功");
                //                    } else {
                //                        UUToastOptError.showUUToast(mCtx, result.msg);
                //                    }
                //                } else {
                //                    UUToastOptError.showUUToast(mCtx, result.err.msg);
                //                }
                Message message = mHandler.obtainMessage();
                message.what = Remote_Result;
                message.obj = result;
                mHandler.sendMessage(message);
            }

            @Override
            public void onError(String msg) {
                //                dissmissWaitingDialog();
                //                LogUtils.e(msg);
                //                UUToastOptError.showUUToast(mCtx, msg);
                mHandler.sendEmptyMessage(Remote_Err);
            }
        });
    }

    /**
     * @param winState
     *         1-开窗;2-关窗;3-天窗开翘;4-天窗关翘;5-天窗开启;6-天窗关闭
     */
    private void remoteChangeWinState(int winState) {
        HashMap remoteCommonParams = ApiRetrofit.getRemoteCommonParams();

        HashMap<String, Object> map = new HashMap<>();
        map.put("base", remoteCommonParams);
        map.put("moveDeviceName", DorideApplication.MODEL_NAME);
        map.put("rwoc", winState);
        addDisposable(mApiService.RemoteWindow(map), new BaseMvcObserver<RemoteCommonInfo>() {
            @Override
            public void onSuccess(RemoteCommonInfo result) {
                //                dissmissWaitingDialog();
                //
                //                if (result.err == null) {
                //                    if (TextUtils.isEmpty(result.msg)) {
                //                        UUToastOpt.showUUToast(mCtx, "操作成功");
                //                    } else {
                //                        UUToastOptError.showUUToast(mCtx, result.msg);
                //                    }
                //                } else {
                //                    UUToastOptError.showUUToast(mCtx, result.err.msg);
                //                }
                Message message = mHandler.obtainMessage();
                message.what = Remote_Result;
                message.obj = result;
                mHandler.sendMessage(message);
            }

            @Override
            public void onError(String msg) {
                //                dissmissWaitingDialog();
                //                LogUtils.e(msg);
                //                UUToastOptError.showUUToast(mCtx, msg);
                mHandler.sendEmptyMessage(Remote_Err);
            }
        });
    }

    /**
     * @param lockState
     *         1:开锁，2上锁
     */
    private void remoteLock(int lockState) {
        HashMap remoteCommonParams = ApiRetrofit.getRemoteCommonParams();

        HashMap<String, Object> map = new HashMap<>();
        map.put("base", remoteCommonParams);
        map.put("moveDeviceName", DorideApplication.MODEL_NAME);
        map.put("lock", lockState);
        addDisposable(mApiService.RemoteLock(map), new BaseMvcObserver<RemoteCommonInfo>() {
            @Override
            public void onSuccess(RemoteCommonInfo result) {
                //                dissmissWaitingDialog();
                //
                //                if (result.err == null) {
                //                    if (TextUtils.isEmpty(result.msg)) {
                //                        UUToastOpt.showUUToast(mCtx, "操作成功");
                //                    } else {
                //                        UUToastOptError.showUUToast(mCtx, result.msg);
                //                    }
                //                } else {
                //                    UUToastOptError.showUUToast(mCtx, result.err.msg);
                //                }
                Message message = mHandler.obtainMessage();
                message.what = Remote_Result;
                message.obj = result;
                mHandler.sendMessage(message);
            }

            @Override
            public void onError(String msg) {
                //                dissmissWaitingDialog();
                //                LogUtils.e(msg);
                //                UUToastOptError.showUUToast(mCtx, msg);
                mHandler.sendEmptyMessage(Remote_Err);
            }
        });
    }

    /**
     * 远程熄火
     */
    private void remoteStopEngine() {
        HashMap remoteCommonParams = ApiRetrofit.getRemoteCommonParams();

        HashMap<String, Object> map = new HashMap<>();
        map.put("base", remoteCommonParams);
        map.put("moveDeviceName", DorideApplication.MODEL_NAME);
        addDisposable(mApiService.RemoteStall(map), new BaseMvcObserver<RemoteCommonInfo>() {
            @Override
            public void onSuccess(RemoteCommonInfo result) {
                //                dissmissWaitingDialog();
                //
                //                if (result.err == null) {
                //                    if (TextUtils.isEmpty(result.msg)) {
                //                        UUToastOpt.showUUToast(mCtx, "操作成功");
                //                    } else {
                //                        UUToastOptError.showUUToast(mCtx, result.msg);
                //                    }
                //                } else {
                //                    UUToastOptError.showUUToast(mCtx, result.err.msg);
                //                }
                Message message = mHandler.obtainMessage();
                message.what = Remote_Result;
                message.obj = result;
                mHandler.sendMessage(message);
            }

            @Override
            public void onError(String msg) {
                //                dissmissWaitingDialog();
                //                LogUtils.e(msg);
                //                UUToastOptError.showUUToast(mCtx, msg);
                mHandler.sendEmptyMessage(Remote_Err);
            }
        });
    }

    /**
     * 远程启动
     */
    private void remoteStartEngine() {
        HashMap remoteCommonParams = ApiRetrofit.getRemoteCommonParams();

        HashMap<String, Object> map = new HashMap<>();
        map.put("base", remoteCommonParams);
        map.put("moveDeviceName", DorideApplication.MODEL_NAME);
        addDisposable(mApiService.RemoteStart(map), new BaseMvcObserver<RemoteCommonInfo>() {
            @Override
            public void onSuccess(RemoteCommonInfo result) {
                //                dissmissWaitingDialog();
                //                if (result.err == null) {
                //                    if (TextUtils.isEmpty(result.msg)) {
                //                        UUToastOpt.showUUToast(mCtx, "操作成功");
                //                    } else {
                //                        UUToastOptError.showUUToast(mCtx, result.msg);
                //                    }
                //                } else {
                //                    UUToastOptError.showUUToast(mCtx, result.err.msg);
                //                }
                Message message = mHandler.obtainMessage();
                message.what = Remote_Result;
                message.obj = result;
                mHandler.sendMessage(message);
            }

            @Override
            public void onError(String msg) {
                //                dissmissWaitingDialog();
                //                LogUtils.e(msg);
                //                UUToastOptError.showUUToast(mCtx, msg);
                mHandler.sendEmptyMessage(Remote_Err);
                //                Message message = mHandler.obtainMessage();
                //                message.what = Remote_Err;
                //                message.obj = msg;
                //                mHandler.sendMessage(message);
            }
        });
    }

    // 校验远程密码
    private BaseParser.ResultCallback mListener_verify = new BaseParser.ResultCallback() {

        @Override
        public void onSuccess(BaseResponseInfo bInfo) {
            Message msg = new Message();
            msg.what = 4;
            msg.obj = bInfo;
            mHandler.sendMessage(msg);
        }

        @Override
        public void onError(BaseResponseInfo bInfo) {
            Message msg = new Message();
            msg.what = 5;
            msg.obj = bInfo;
            mHandler.sendMessage(msg);
        }
    };


    // 获取远程状态
    private BaseParser.ResultCallback mListener_states = new BaseParser.ResultCallback() {

        @Override
        public void onSuccess(BaseResponseInfo bInfo) {
            Logger.e("---" + bInfo.toString());
            Message msg = new Message();
            msg.what = 2;
            msg.obj = bInfo;
            mHandler.sendMessage(msg);
        }

        @Override
        public void onError(BaseResponseInfo bInfo) {
            Message msg = new Message();
            msg.what = 3;
            msg.obj = bInfo;
            mHandler.sendMessage(msg);
        }
    };

    // 获取车辆温度
    private BaseParser.ResultCallback mListener_temp = new BaseParser.ResultCallback() {

        @Override
        public void onSuccess(BaseResponseInfo bInfo) {
            Message msg = new Message();
            msg.what = 8;
            msg.obj = bInfo;
            mHandler.sendMessage(msg);
        }

        @Override
        public void onError(BaseResponseInfo bInfo) {
            Message msg = new Message();
            msg.what = 9;
            msg.obj = bInfo;
            mHandler.sendMessage(msg);
        }

    };

    /**
     * 天窗的对话框里点击事件
     */
    View.OnClickListener mItemClick2 = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            RemoteFunInfo mInfo = (RemoteFunInfo) v.getTag();
            selectedPos = MyParse.parseInt(mInfo.getId());
            if (uuDialogRemote != null && uuDialogRemote.isShowing()) {
                uuDialogRemote.dismiss();
            }
            GetResult();
        }
    };
    public static final int     Remote_Result = 111;
    public static final int     Remote_Err    = 222;
    @SuppressLint("HandlerLeak")
    private             Handler mHandler      = new Handler() {

        private ArrayList<CarStateInfo> mDataList;

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Remote_Result:
                    //          远程操作返回结果

                    dissmissWaitingDialog();
                    if (airDialog != null && airDialog.isShowing()) {
                        airDialog.dismiss();
                    }
                    if (uuDialogRemote != null && uuDialogRemote.isShowing()) {
                        uuDialogRemote.dismiss();
                    }
                    RemoteCommonInfo result = (RemoteCommonInfo) msg.obj;


                    if (result.err == null) {
                        if (TextUtils.isEmpty(result.msg)) {
                            UUToastOpt.showUUToast(mCtx, "操作成功");
                        } else {
                            UUToastOptError.showUUToast(mCtx, result.msg);
                            //                            UUToastOptError.getUuTo().showUUToast(result.msg);
                        }
                    } else {

                        if (result.err.code == 1020) {
                            Intent intent = new Intent(mCtx, UpDateActivity.class);
                            startActivity(intent);
                        } else {
                            UUToastOptError.showUUToast(mCtx, result.err.msg);
                            //                            UUToastOptError.getUuTo().showUUToast(result.err.msg);
                        }
                    }
                    break;
                case Remote_Err:
                    //                    远程操作失败(一般是网络问题)
                    dissmissWaitingDialog();
                    if (airDialog != null && airDialog.isShowing()) {
                        airDialog.dismiss();
                    }
                    if (uuDialogRemote != null && uuDialogRemote.isShowing()) {
                        uuDialogRemote.dismiss();
                    }
                    UUToastOptError.showUUToast(mCtx, "操作失败");
                    //                    UUToastOptError.getUuTo().showUUToast("操作失败");
                    break;


                case 0:
                    //                    远程操作成功
                    Logger.e("---远程操作成功");
                    UUToastOpt.showUUToast(mCtx, "操作成功");
                    dissmissWaitingDialog();
                    if (airDialog != null) {
                        isReCall = false;
                        airDialog.dismiss();
                    }

                    if (uuDialogRemote != null && uuDialogRemote.isShowing()) {
                        uuDialogRemote.dismiss();
                    }

                    break;

                case 1:
                    dissmissWaitingDialog();
                    if (airDialog != null && airDialog.isShowing()) {
                        airDialog.dismiss();
                    }
                    BaseResponseInfo mInfo1 = (BaseResponseInfo) msg.obj;
                    if (uuDialogRemote != null && uuDialogRemote.isShowing()) {
                        uuDialogRemote.dismiss();
                    }
                    if (mInfo1 != null) {
                        if (mInfo1.getFlag() == 1020) {
                            Intent intent = new Intent(mCtx, UpDateActivity.class);
                            startActivity(intent);
                            //                            UUToastOptError.showUUToast(getActivity(), "硬件升级提示");
                        } else {
                            UUToastOptError.showUUToast(getActivity(), mInfo1.getInfo());
                            //                            UUToastOptError.getUuTo().showUUToast(mInfo1.getInfo());
                        }

                    }
                    break;

                case 2:
                    // 获取车辆状态成功
                    mCarStateDataList = (ArrayList<CarStateInfo>) ((BaseResponseInfo) msg.obj).getValue();

                    if (mCarStateDataList != null && mCarStateDataList.size() > 0) {
                        mGridViewState.setNumColumns(3);
                        if (mAdapterStates == null) {
                            mAdapterStates = new RemoteStatesAdapter(
                                    getActivity(), mCarStateDataList);
                            mGridViewState.setAdapter(mAdapterStates);
                        } else {
                            mAdapterStates.setmDataList(mCarStateDataList);
                            mAdapterStates.notifyDataSetChanged();
                        }
                        dissmissWaitingDialog();
                        mViewState.setVisibility(View.VISIBLE);
                    } else {
                        dissmissWaitingDialog();
                        mViewState.setVisibility(View.GONE);
                        UUToast.showUUToast(getActivity(), "暂未获取到车辆状态数据");
                    }
                    break;
                case 3:
                    dissmissWaitingDialog();
                    BaseResponseInfo mInfo = (BaseResponseInfo) msg.obj;
                    if (mInfo != null) {
                        UUToast.showUUToast(getActivity(),
                                mInfo.getInfo());
                    }
                    break;
                case 4:
                    //                    校验远程密码成功
                    dissmissWaitingDialog();
                    mHandler.sendEmptyMessage(10);
                    break;
                case 5:
                    //                    校验远程密码失败
                    dissmissWaitingDialog();
                    BaseResponseInfo mInfo2 = (BaseResponseInfo) msg.obj;
                    if (mInfo2 != null) {
                        UUToast.showUUToast(getActivity(),
                                mInfo2.getInfo());
                    }
                    isFirstClick = true;
                    break;
                case 6:
                    //                                        CPControl.GetRemoteCarTemp(mListener_temp, mAirMainInfo1);
                    remoteCarStateAir();
                    break;

                case 7:
                    dissmissWaitingDialog();
                    // 获取远程空调功能失败
                    BaseResponseInfo mInfo7 = (BaseResponseInfo) msg.obj;
                    if (mInfo7 != null) {
                        UUToast.showUUToast(getActivity(),
                                mInfo7.getInfo());
                    }
                    break;
                case 8:
                    // 获取远程车辆温度成功
                    dissmissWaitingDialog();
                    // 获取远程空调功能成功
                    AirMainInfo mAirMainInfo2 = (AirMainInfo) msg.obj;
                    Logger.e("---" + mAirMainInfo2.toString());
                    if (!isReCall) {
                        if (airDialog == null || !airDialog.isShowing()) {

                            airDialog = new UUAirConditionDialog(
                                    getActivity(), mAirMainInfo2);
                            airDialog.mListener = mListener;
                            airDialog.mHandler = mHandler;
                            airDialog.show();
                        }
                    } else {
                        airDialog.reCall();
                    }
                    break;
                case 9:
                    // 获取远程车辆温度失败
                    dissmissWaitingDialog();
                    BaseResponseInfo mInfo9 = (BaseResponseInfo) msg.obj;
                    if (mInfo9 != null) {
                        UUToast.showUUToast(getActivity(),
                                mInfo9.getInfo());
                    }
                    break;
                case 10:
                    //                    处理远程校验密码成功后逻辑
                    GetResult();
                    break;
                case 11:
                    showWaitingDialog(null);
                    // 调用接口
                    break;
                case 13:
                    dissmissWaitingDialog();
                    ArrayList<CarStateInfo> mDataList2 = (ArrayList<CarStateInfo>) msg.obj;
                    String state2 = "";
                    for (int i = 0; i < mDataList2.size(); i++) {
                        CarStateInfo csi = mDataList2.get(i);
                        if (csi.getName().equals(CarStateInfo.names[3])) {
                            state2 = csi.getState();
                        }
                    }
                    if (airDialog == null || !airDialog.isShowing()) {
                        airDialog = new UUAirConditionDialog(
                                getActivity(), null);
                        airDialog.setState(state2);
                        airDialog.mListener = mListener;
                        airDialog.mHandler = mHandler;
                        airDialog.show();
                    }
                    break;
                case 14:
                    dissmissWaitingDialog();
                    if (airDialog == null || !airDialog.isShowing()) {
                        // UUToast.showUUToast(getActivity(), "获取空调状态超时");
                        airDialog = new UUAirConditionDialog(
                                getActivity(), null);
                        airDialog.mListener = mListener;
                        airDialog.mHandler = mHandler;
                        airDialog.show();
                    }
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onClick(View v) {
        if (hasActivate())
            return;

        switch (v.getId()) {
            case R.id.state_car_iv:
                // 车辆状态
                if (mViewState.getVisibility() == View.VISIBLE) {
                    mViewState.setVisibility(View.GONE);
                } else {
                    showWaitingDialog("正在获取数据...");
                    //                    CPControl.GetRemoteCarState(mListener_states);
                    remoteCarState();
                }
                break;
            case R.id.remote_history_iv:
                // 远程记录
                Intent mIntent = new Intent(getActivity(), RemoteLogActivity.class);
                startActivity(mIntent);

                break;
            case R.id.remote_main_img_arrow:
                // 收起状态栏
                mViewState.setVisibility(View.GONE);
                break;
            case R.id.remote_main_img_start:
                // 远程启动
                selectedPos = -2;
                clickLogic();
                break;
            case R.id.remote_main_img_stop:
                // 远程熄火
                selectedPos = -1;
                clickLogic();
                break;
            case R.id.remote_main_lay_normal:
                // 车辆状态view消失
                dismissCarstateView();
                break;
        }
    }


    private boolean hasActivate() {

        //        LogUtils.e(remoteStatus);
        switch (remoteStatus) {
            case 0:
                PopBoxCreat.createDialogNotitle(mCtx, "温馨提示",
                        "设备还未激活",
                        "确定", "去激活", new PopBoxCreat.DialogWithTitleClick() {
                            @Override
                            public void onLeftClick() {

                            }

                            @Override
                            public void onRightClick() {
                                Intent activateIntent = new Intent(mCtx, ActivateAccActivity.class);
                                int id = GetCarInfo.getInstance().id;
                                String vin = GetCarInfo.getInstance().vin;
                                activateIntent.putExtra("carID", String.valueOf(id));
                                activateIntent.putExtra("vin", vin);
                                startActivity(activateIntent);
                            }
                        });
                break;
            case 1:
                showActivateState("设备正在激活中...");
                break;
            case 3:
                showActivateState("激活失败");
                break;
        }
        if (remoteStatus != 2) {
            return true;
        }
        return false;
    }

    private void showActivateState(String msg) {
        PopBoxCreat.createDialogNotitle(mCtx, "温馨提示",
                msg,
                "确定", "查看详情", new PopBoxCreat.DialogWithTitleClick() {
                    @Override
                    public void onLeftClick() {

                    }

                    @Override
                    public void onRightClick() {
                        Intent activateIntent = new Intent(mCtx, ActivateStepActivity.class);
                        int id = GetCarInfo.getInstance().id;
                        String vin = GetCarInfo.getInstance().vin;
                        activateIntent.putExtra("carID", id);
                        activateIntent.putExtra("vin", vin);
                        activateIntent.putExtra("From", ActivateStepActivity.from_Droide);
                        startActivity(activateIntent);
                    }
                });
    }

    private void dismissCarstateView() {
        if (mViewState.getVisibility() == View.VISIBLE) {
            mViewState.setVisibility(View.GONE);
        }
    }

    /**
     * 点击逻辑
     */
    private void clickLogic() {
        if (hasActivate())
            return;
        boolean hasRemotePswMd5 = UserInfo.getInstance().isSetRemotePwd == 1;
        //TODO test data
        //        hasRemotePswMd5 = false;

        if (mViewState.getVisibility() == View.VISIBLE) {
            // 车辆状态view打开
            mViewState.setVisibility(View.GONE);
        } else {
            if (hasRemotePswMd5) {
                if (isFirstClick) {
                    showEditPop();
                } else {
                    if (UserInfo.getInstance().remotePwdSwitch == 1) {
                        if (getTimeOutStatus()) {
                            showEditPop();
                        } else {
                            GetResult();
                        }
                    } else {
                        showEditPop();
                    }

                }
            } else {
                PopBoxCreat.DialogWithTitleClick click = new PopBoxCreat.DialogWithTitleClick() {
                    @Override
                    public void onRightClick() {
                        Intent mIntent = new Intent(getActivity(), RemotePswResetActivity3.class);
                        mIntent.putExtra(RemotePswResetActivity3.TYPE, RemotePswResetActivity3.TYPE_REMOTE);
                        startActivity(mIntent);
                    }

                    @Override
                    public void onLeftClick() {
                        // TODO Auto-generated method stub

                    }
                };
                PopBoxCreat.createDialogNotitle(getActivity(),
                        "设置远程控制", "为保障车辆安全请先设置远程控制密码", "取消", "设置密码", click);

            }
        }
    }

    private void showEditPop() {
        SelectPopupWindow pwdPop = new SelectPopupWindow(getActivity(), this);
        Rect rect = new Rect();
        getActivity().getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        int winHeight = getActivity().getWindow().getDecorView().getHeight();
        pwdPop.showAtLocation(getActivity().getWindow().getDecorView(), Gravity.BOTTOM, 0, winHeight - rect.bottom);
        pwdPop.tvForgetPwd.setOnClickListener(view -> {
            Intent resetLoginPasswdByPhone = new Intent(getActivity(), VcodeResetRemotePasswdActivity.class);
            startActivity(resetLoginPasswdByPhone);
        });
    }

    @Override
    public void onPopWindowClickListener(String psw, boolean complete) {
        if (complete) {
            if (isFirstClick) {
                startTime = System.currentTimeMillis();
                isFirstClick = false;
            }
            showWaitingDialog("正在验证您的远程密码...");
            CPControl.GetRemotePswVerify(psw, mListener_verify);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        isFirstClick = true;
        Logger.e("---hidden----" + hidden);
        if (!hidden) {
            WIFIControl.rigisterWIFIConnectListener(this);
            WIFIControl.DisConnectChelePai();
            loadData();
        } else {
            WIFIControl.unRigisterWIFIConnectListener(this);
            if (mViewState.getVisibility() == View.VISIBLE) {
                mViewState.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        isFirstClick = true;
    }

    private boolean getTimeOutStatus() {
        boolean isTimeOut = false;

        long currentTime = System.currentTimeMillis();
        long diffrence = currentTime - startTime;
        if (diffrence > INVALID_DURATION) {
            isTimeOut = true;
            isFirstClick = true;
        } else {
            isTimeOut = false;
        }
        return isTimeOut;
    }

    private void showEditDialog() {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.dialog_withedit_new, null);
        final Dialog dialogI = new Dialog(getActivity(),
                R.style.dialog);
        final EditText editPassword = (EditText) view
                .findViewById(R.id.dialog_withedit_new_edt);

        ImageView imgCancel = (ImageView) view
                .findViewById(R.id.dialog_withedit_new_cancel);
        TextView btn1 = (TextView) view
                .findViewById(R.id.dialog_withedit_new_btn1);
        TextView btn2 = (TextView) view
                .findViewById(R.id.dialog_withedit_new_btn2);

        TextView btnForget = (TextView) view
                .findViewById(R.id.dialog_withedit_forget_psw);

        btnForget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resetLoginPasswdByPhone = new Intent(getActivity(), VcodeResetRemotePasswdActivity.class);
                startActivity(resetLoginPasswdByPhone);
            }
        });

        // 正常模式
        editPassword.setEnabled(true);
        editPassword.setFocusableInTouchMode(true);
        editPassword.requestFocus();


        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            public void run() {
                InputMethodManager inputManager = (InputMethodManager) editPassword
                        .getContext().getSystemService(
                                Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(editPassword, 0);
            }

        }, 100);

        btn2.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                password = editPassword.getText().toString();
                if (password == null || password.length() < 1) {
                    UUToast.showUUToast(getActivity(), "您的密码不能为空哦...");
                    return;
                }
                if (isFirstClick) {
                    startTime = System.currentTimeMillis();
                    isFirstClick = false;
                }

                showWaitingDialog("正在验证您的远程密码...");
                CPControl.GetRemotePswVerify(password, mListener_verify);
                dialogI.dismiss();
            }
        });

        btn1.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                dialogI.dismiss();
            }
        });

        imgCancel.setOnClickListener(v -> dialogI.dismiss());

        int w = (int) (DorideApplication.ScreenDensity * 300);
        ViewGroup.LayoutParams parm = new ViewGroup.LayoutParams(w,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogI.setContentView(view, parm);
        dialogI.show();
    }


    @Override
    public boolean onTouch(View view, MotionEvent event) {
        Log.e("info", "event.getAction()==" + event.getAction());
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            dismissCarstateView();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    public void showWaitingDialog(String msg) {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }

        if (msg == null) {
            msg = "正在连接爱车...";
        }

        mDialog = PopBoxCreat.createDialogWithProgress(getActivity(),
                msg);
        mDialog.show();
    }

    public void dissmissWaitingDialog() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

    private RemoteReceiver mReceiver;

    @Override
    public void onWIFIChange(int action) {
        mHandler.sendEmptyMessage(action);
    }


    public class RemoteReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ACTION_REMOTE_SETPSW)) {
                // 设置密码成功
                startTime = System.currentTimeMillis();
                isFirstClick = false;
            } else if (action.equals(ACTION_REMOTE_RESETPSW)) {
                // 修改密码成功
                isFirstClick = true;
            } else if (action.equals(ACTION_REMOTE_FORGETPSW)) {
                // 重置密码成功
                isFirstClick = true;
            }
        }
    }
}

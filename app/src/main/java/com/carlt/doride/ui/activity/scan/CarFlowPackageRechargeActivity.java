package com.carlt.doride.ui.activity.scan;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.carlt.doride.R;
import com.carlt.doride.base.LoadingActivity;
import com.carlt.doride.control.CPControl;
import com.carlt.doride.data.BaseResponseInfo;
import com.carlt.doride.data.flow.FlowPackageOrderInfo;
import com.carlt.doride.data.flow.FlowPriceInfo;
import com.carlt.doride.data.flow.PackageDataInfo;
import com.carlt.doride.data.flow.TrafficPackageWarnningInfo;
import com.carlt.doride.model.LoginInfo;
import com.carlt.doride.protocolparser.BaseParser;
import com.carlt.doride.systemconfig.URLConfig;
import com.carlt.doride.ui.activity.setting.TrafficPackagePurchaseLogActivity;
import com.carlt.doride.ui.adapter.FlowPriceListAdapter;
import com.carlt.doride.ui.adapter.PackageTypeAdapter;
import com.carlt.doride.ui.view.CircleProgress;
import com.carlt.doride.ui.view.GridViewForScrollView;
import com.carlt.doride.ui.view.ListViewForScrollView;
import com.carlt.doride.ui.view.PopBoxCreat;
import com.carlt.doride.ui.view.SegmentControl;
import com.carlt.doride.ui.view.UUPopupWindow;
import com.carlt.doride.ui.view.UUToast;
import com.carlt.sesame.data.set.PayResult;
import com.carlt.sesame.ui.view.UUImgInfoDialog;
import com.carlt.sesame.utility.MyTimeUtil;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Description :  流量包充值
 */
public class CarFlowPackageRechargeActivity extends LoadingActivity {

    @BindView(R.id.trffic_used_precent)
    CircleProgress trfficUsedPrecent;
    @BindView(R.id.traffic_used_txt)
    TextView trafficUsedTxt;
    @BindView(R.id.traffic_remain_txt)
    TextView trafficRemainTxt;
    @BindView(R.id.traffic_tips_layout)
    LinearLayout trafficTipsLayout;
    @BindView(R.id.tvCurrentPackage)
    TextView tvCurrentPackage;
    @BindView(R.id.segment_control)
    SegmentControl segmentControl;
    @BindView(R.id.tvEmptyHint)
    TextView tvEmptyHint;
    @BindView(R.id.tvPriceEmptyHint)
    TextView tvPriceEmptyHint;
    @BindView(R.id.gv_package_wrap)
    GridViewForScrollView GvPackageWrap;
    @BindView(R.id.lvPriceList)
    ListViewForScrollView lvPriceList;

    @BindView(R.id.sv)
    ScrollView sv;
    @BindView(R.id.car_traffic_txt_simid)
    TextView carTrafficTxtSimid;
    private int position = 0;
    private Dialog mPorgressDialog;
    private String title = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_traffic_package_purchase);
        ButterKnife.bind(this);
        //        CPControl.GetToPay(mPayHandler,
        //                FlowPackageRechargeActivity.this, p2, false);
        title = getIntent().getStringExtra("title");
        initData();
        initView();
    }

    @Override
    public void reTryLoadData() {
        initData();
    }

    private void initData() {
        //        getFlowProductList();
        loadingDataUI();
        String car_flow_package_info_url = URLConfig.getCAR_FLOW_PACKAGE_INFO_URL();
        OkGo.<String>post(car_flow_package_info_url)
                .params("client_id", URLConfig.getClientID())
                .params("dealerId", LoginInfo.getDealerId())
                .params("token", LoginInfo.getAccess_token())
                .params("deviceType", "android")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        LogUtils.e(response.body());
                        if (response.isSuccessful()) {
                            parseFlowInfoJson(response);
                        }

                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        loadonErrorUI(new BaseResponseInfo());
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
                    loadSuccessUI();
                    warnningInfo = gson.fromJson(response.body(), TrafficPackageWarnningInfo.class);
                    updateView(warnningInfo);
                    getFlowProductList();
                } else {
                    BaseResponseInfo baseResponseInfo = new BaseResponseInfo();
                    baseResponseInfo.setFlag(BaseResponseInfo.ERRO);
                    baseResponseInfo.setInfo(msg);
                    loadonErrorUI(baseResponseInfo);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }


    }

    private void getFlowProductList() {
        String car_flow_product_list_url = URLConfig.getCAR_FLOW_PRODUCT_LIST_URL();
        OkGo.<String>post(car_flow_product_list_url)
                .params("client_id", URLConfig.getClientID())
                .params("dealerId", LoginInfo.getDealerId())
                .params("token", LoginInfo.getAccess_token())
                .params("deviceType", "android")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        LogUtils.e(response.body());
                        if (response.isSuccessful()) {
                            parseFlowProductJson(response);
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);

                    }
                });
    }

    private void updateView(TrafficPackageWarnningInfo warnningInfo) {
        if (!TextUtils.isEmpty(warnningInfo.data.simid)) {
            carTrafficTxtSimid.setText("SIMID："+warnningInfo.data.simid);
        }else {
            carTrafficTxtSimid.setText("SIMID：--");
        }
        float totalPackage = Float.valueOf(warnningInfo.data.share_data_total);
        float usedPackage = Float.valueOf(warnningInfo.data.consume_data);
        float remainPackage = Float.valueOf(warnningInfo.data.residual_data);
        trfficUsedPrecent.setMaxValue(totalPackage);
        trfficUsedPrecent.setValue(remainPackage);
        trfficUsedPrecent.setUnit("共" + packageDateParse(totalPackage));
        trafficUsedTxt.setText(String.format(this.getResources()
                        .getString(R.string.traffic_used_status),
                packageDateParse(usedPackage)));
        trafficRemainTxt.setText(String.format(this.getResources()
                        .getString(R.string.traffic_remain_status),
                packageDateParse(remainPackage)));

        int package_type = Integer.valueOf(warnningInfo.data.package_type);
        int service_status = Integer.valueOf(warnningInfo.data.service_status);
        int is_deadline = Integer.valueOf(warnningInfo.data.is_deadline);
        int package_size1 = Integer.valueOf(warnningInfo.data.package_size);
        String package_size = packageDateParse(Float.valueOf(package_size1));
        int next_package_type = Integer
                .valueOf(warnningInfo.data.next_package_type);
        // service_status=2;
        // next_package_type=0;
        // package_type=2;
        switch (service_status) {
            // 流量正常
            case 1:

                if (package_type == 1 && is_deadline == 0) {
                    tvCurrentPackage.setText("首年免费套餐：" + package_size
                            + "/月，服务到期时间" + warnningInfo.data.service_data_end);
                }

                if (package_type != 1 && package_type != 2 && is_deadline == 0) {
                    tvCurrentPackage.setText("当前套餐 :" + package_size
                            + "/月，服务到期时间" + warnningInfo.data.service_data_end);
                }
                if (package_type != 2 && is_deadline == 1
                        && next_package_type != 0) {
                    tvCurrentPackage.setText("当前套餐即将过期，新的套餐流量将于"
                            + warnningInfo.data.next_service_data_start + "开始生效");
                }
                if (package_type != 2 && is_deadline == 1
                        && next_package_type == 0) {
                    tvCurrentPackage.setText("当前套餐即将过期，请您及时续套餐");
                }

                if (package_type == 2) {
                    tvCurrentPackage.setText("当前流量 :" + package_size
                            + "/月，服务到期时间" + warnningInfo.data.service_data_end);
                }
                break;
            case 2:
                // 已停止供网
                if (package_type != 2 || next_package_type == 0) {

                    if (package_type == 0 && next_package_type == 0) {
                        tvCurrentPackage.setText("流量已停用，请您充值加油包或者续套餐");
                    } else {

                        tvCurrentPackage.setText("流量已停用，请您充值加油包");
                    }
                } else {

                    tvCurrentPackage.setText("流量已停用，请您充值加油包或者续套餐");
                }

                break;
            default:
                break;
        }
    }

    private void initView() {
        if (TextUtils.isEmpty(title)) {
            initTitle("流量充值");
        }else{
            initTitle(title);
        }
        tvRight.setText("充值记录");
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CarFlowPackageRechargeActivity.this, CarTrafficPackagePurchaseLogActivity.class));
            }
        });
        if (mPopupWindow == null) {
            initPopwindow();
        }
        if (mPorgressDialog == null) {
            mPorgressDialog = PopBoxCreat.createDialogWithProgress(this,
                    "数据提交中...");
        }
        sv.smoothScrollTo(0, 0);
        GvPackageWrap.setFocusable(false);
        segmentControl.setSelectedIndex(0);
        segmentControl
                .setOnSegmentControlClickListener(new SegmentControl.OnSegmentControlClickListener() {

                    @Override
                    public void onSegmentControlClick(int index) {
                        switch (index) {
                            case 0:
                                // 加油包
                                position = 0;
                                tvEmptyHint.setVisibility(View.GONE);
                                GvPackageWrap.setVisibility(View.VISIBLE);
                                tvPriceEmptyHint.setVisibility(View.GONE);
                                showFlowPackageInfos(refuleItems, refulePriceInfos);

                                break;
                            case 1:
                                // 改套餐
                                position = 1;
                                if (isCurrrentMoth(warnningInfo.data.service_data_end)) {
                                    tvEmptyHint.setVisibility(View.VISIBLE);
                                    GvPackageWrap.setVisibility(View.GONE);
                                    lvPriceList.setVisibility(View.GONE);
                                } else {
                                    tvEmptyHint.setVisibility(View.GONE);
                                    GvPackageWrap.setVisibility(View.VISIBLE);
                                    lvPriceList.setVisibility(View.VISIBLE);
                                }
                                tvPriceEmptyHint.setVisibility(View.GONE);
                                showFlowPackageInfos(changeItems, changePriceInfos);
                                break;
                            case 2:
                                // 续套餐
                                position = 2;
                                tvPriceEmptyHint.setVisibility(View.GONE);
                                tvEmptyHint.setVisibility(View.GONE);
                                GvPackageWrap.setVisibility(View.VISIBLE);
                                showFlowPackageInfos(renewItems, renewPriceInfos);
                                break;
                            default:
                                break;
                        }
                        lvPriceList.setVisibility(View.GONE);
                    }

                });
    }

    private Boolean isCurrrentMoth(String endDate) {
        if (TextUtils.isEmpty(endDate)) {
            return true;
        }
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");// 设置日期格式
        // 拿到当前时间
        String date = df.format(new Date());
        String[] currentDate = date.split("-");
        String[] split = endDate.split("-");
        String currentYear = currentDate[0];
        String endYear = split[0];
        if (Integer.valueOf(currentYear) < Integer.valueOf(endYear)) {
            return false;
        }
        String mm = split[1];
        if (currentYear.equals(endYear) && currentDate[1].equals(mm)) {
            return true;
        } else {
            return false;
        }

    }

    private PackageTypeAdapter adapter;
    private TrafficPackageWarnningInfo warnningInfo;

    private void showFlowPackageInfos(ArrayList<String> changeItems,
                                      ArrayList<FlowPriceInfo> changePriceInfos) {


        if (tvEmptyHint.VISIBLE == View.VISIBLE) {
            if (Integer.valueOf(warnningInfo.data.next_package_type) == 0) {
                tvEmptyHint.setText("当前套餐已过期或剩余时间不足，请先续套餐");
            } else {
                tvEmptyHint.setText("当前套餐已过期或剩余时间不足，无法更改");
            }
        }
        if (changeItems.size() > 0 && changeItems.size() > 0) {
            adapter = new PackageTypeAdapter(
                    CarFlowPackageRechargeActivity.this, changeItems,
                    changePriceInfos);
            if (changeItems.size() % 2 == 0) {
                GvPackageWrap.setNumColumns(2);
            } else {
                GvPackageWrap.setNumColumns(3);
            }
            GvPackageWrap.setAdapter(adapter);
        }
        GvPackageWrap.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                ArrayList<FlowPriceInfo> flowPriceDataList = new ArrayList<FlowPriceInfo>();
                adapter.setSeclection(pos);
                adapter.notifyDataSetChanged();
                String item = adapter.getItem(pos);

                switch (position) {
                    case 0:
                        // 加油包

                        for (int i = 0; i < refulePriceInfos.size(); i++) {
                            FlowPriceInfo flowPriceInfo = refulePriceInfos.get(i);
                            if (flowPriceInfo.name.equals(item)) {
                                flowPriceDataList.add(flowPriceInfo);
                            }
                        }
                        break;
                    case 1:
                        // 改套餐

                        changePackageFlow(item);

                        break;
                    case 2:
                        // 续套餐

                        for (int i = 0; i < renewPriceInfos.size(); i++) {
                            FlowPriceInfo flowPriceInfo = renewPriceInfos.get(i);
                            if (flowPriceInfo.name.equals(item)) {
                                flowPriceDataList.add(flowPriceInfo);
                            }
                        }
                        break;
                    default:
                        break;
                }

                showPackagePriceList(flowPriceDataList, null);

            }
        });
    }

    /**
     * Description : 改套餐请求接口拉取套餐种类
     */
    private void changePackageFlow(String item) {
        tvPriceEmptyHint.setVisibility(View.GONE);
        mPorgressDialog.show();
        CPControl.getCarCalculatePrice(item, "4", new BaseParser.ResultCallback() {
            @Override
            public void onSuccess(BaseResponseInfo bInfo) {
                //                LogUtils.e(bInfo.getValue());
                mPorgressDialog.dismiss();
                parsePackagePrice(bInfo);
            }

            @Override
            public void onError(BaseResponseInfo bInfo) {
                mPorgressDialog.dismiss();
            }
        });

    }

    private View mPopupWindowView;

    private UUPopupWindow mPopupWindow;// 支付时底部弹出的popupWindow

    private Animation ani1;
    private TextView tvCommodityDetail;
    private float packageSize = 0.0f;

    private void initPopwindow() {

        ani1 = AnimationUtils.loadAnimation(this,
                R.anim.enter_pay_popwindow);
        Animation ani2 = AnimationUtils.loadAnimation(this,
                R.anim.exit_pay_popuwindow);

        LayoutInflater mInflater = LayoutInflater.from(this);
        mPopupWindowView = mInflater.inflate(R.layout.popupwindow_select_pay,
                null);
        mPopupWindow = new UUPopupWindow(mPopupWindowView,
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        View mPopupWindowBg = mPopupWindowView
                .findViewById(R.id.layout_edit_head_icon);
        mPopupWindowBg.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();
            }
        });

        mPopupWindowView.setFocusableInTouchMode(true);
        mPopupWindowView.setOnKeyListener(new View.OnKeyListener() {

            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_BACK
                        && mPopupWindow.isShowing()
                        && event.getAction() == KeyEvent.ACTION_UP) {
                    mPopupWindow.dismiss();

                }
                return true;
            }
        });
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        mPopupWindow.setBackgroundDrawable(dw);
        mPopupWindow.initAni(mPopupWindowView, ani2);
        mPopupWindow.setFocusable(true);
        mPopupWindow.update();
        mPopupWindow.setOutsideTouchable(false);

        View.OnClickListener mListener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.tvCommodityDetail:
                        break;

                    case R.id.tvAliPay:
                        if (mPopupWindow != null && mPopupWindow.isShowing()) {
                            mPopupWindow.dismiss();
                        }

                        CPControl.getCarFlowPackageOrderResult(String.valueOf(mFlowPriceInfo.id), new BaseParser.ResultCallback() {
                            @Override
                            public void onSuccess(BaseResponseInfo bInfo) {
                                if (mFlowPriceInfo != null) {
                                    packageSize = mFlowPriceInfo.package_size;
                                }
                                parseOrderInfoJson(bInfo);
                            }

                            @Override
                            public void onError(BaseResponseInfo bInfo) {
                                String meesg = "订单信息获取失败";
                                if (bInfo != null) {
                                    meesg = bInfo.getInfo();
                                }
                                UUToast.showUUToast(CarFlowPackageRechargeActivity.this, meesg);
                            }
                        });
                        break;
                    case R.id.tvCancel:
                        mPopupWindow.dismiss();
                        break;
                    default:
                        break;
                }

            }
        };

        tvCommodityDetail = (TextView) mPopupWindowView
                .findViewById(R.id.tvCommodityDetail);
        TextView tvAliPay = (TextView) mPopupWindowView
                .findViewById(R.id.tvAliPay);
        TextView tvCancel = (TextView) mPopupWindowView
                .findViewById(R.id.tvCancel);

        // FlowPriceInfo info = mPopupWindow.getInfo();

        tvCommodityDetail.setOnClickListener(mListener);
        tvAliPay.setOnClickListener(mListener);
        tvCancel.setOnClickListener(mListener);

    }

    public static final String p2 = "alipay_sdk=alipay-sdk-php-20180705&app_id=2018091161327162&biz_content=%7B%22total_fee%22%3A%220.01%22%2C%22body%22%3A%221M%22%2C%22subject%22%3A%221M%22%2C%22out_trade_no%22%3A%2220180915151255142573%22%7D&charset=UTF-8&format=json&method=alipay.trade.app.pay&notify_url=http%3A%2F%2Fpay.linewin.cc%2Fpackage%2FaliAppPay%2FdorideAsynCallback&sign_type=RSA2&timestamp=2018-09-15+15%3A12%3A55&version=1.0&sign=rWAyN1%2BkaaXBj4N3vjh0IW2XJAgOBTE2yJsFaM7n5tuqkDFtUqGec3hg378Lrceb6abzFpM4PYYZ%2BcGAkLOhAngNIJJDkfkGD5DmKJh8iuLQnXzVTv9XxAEv%2BIS%2BA9%2Bp3gaqK5yDL5nWQQof0bxlRN8TQpNq22YyAwodYQKV0Y0Ou7ybWkeHsRMAWf5P%2FEXJNQ020EH8N3bXPZnJdxgM9UAYkRJNS2P07nnqpQDHBAzAGyeGhFVadfSzJYu%2BOD%2FhA616K2%2FYxHb0Q78jKsQSVPPGu6n7rKh%2FFumyVcaDVj%2ByCAl%2BN0mD4TAOieBFakT88%2Fvv7UZs3CVgde%2F1kCjciA%3D%3D";


    private void parseOrderInfoJson(BaseResponseInfo bInfo) {
        Object value = bInfo.getValue();
        Gson gson = new Gson();
        FlowPackageOrderInfo orderInfo = gson.fromJson(value.toString(), FlowPackageOrderInfo.class);
        //调起支付
        CPControl.GetToPay(mPayHandler,
                CarFlowPackageRechargeActivity.this, orderInfo.request_param, false);
        //                CPControl.GetToPay(mPayHandler,
        //                        FlowPackageRechargeActivity.this, p2, false);
    }

    private Date mSuccTime;
    private Dialog mDialog;
    @SuppressLint("HandlerLeak")
    public Handler mPayHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                case 1: {
                    PayResult payResult = new PayResult((String) msg.obj);
                    /**
                     * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/ doc2/
                     * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId= 103665&
                     * docType=1) 建议商户依赖异步通知
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    LogUtils.e("===========" + payResult);
                    mSuccTime = new Date();
                    if (resultStatus.equals("9000") || resultStatus.equals("8000")) {
                        String[] result = resultInfo.split("&");
                        StringBuffer json = new StringBuffer("{");
                        for (int i = 0; i < result.length; i++) {
                            String temp = result[i];
                            String key = temp.substring(0, temp.indexOf("="));
                            String value = temp.substring(temp.indexOf("=") + 1);
                            json.append("\"" + key + "\"");
                            json.append(":");
                            json.append(value);
                            json.append(",");
                        }
                        json.deleteCharAt(json.length() - 1);
                        json.append("}");
                        mDialog = PopBoxCreat.createDialogWithProgress(
                                CarFlowPackageRechargeActivity.this,
                                "正在验证支付结果请稍等。。。");
                        mDialog.show();
                        LogUtils.d("C_DEBUG", json.toString());
                        LogUtils.d("C_DEBUG", resultInfo);
                        //                        checkPayResult(resultStatus, json.toString());
                        checkPayResult(resultStatus, resultInfo);
                    } else {
                        UUToast.showUUToast(CarFlowPackageRechargeActivity.this,
                                "支付失败!");
                    }
                    break;
                }
                default:
                    UUToast.showUUToast(CarFlowPackageRechargeActivity.this,
                            "支付失败!");
                    break;
            }
        }

    };
    UUImgInfoDialog uuImgInfoDialog;

    private void checkPayResult(String resultStatus, String json) {
        CPControl.getFlowPackageCheckPayResult(resultStatus, json, new BaseParser.ResultCallback() {
            @Override
            public void onSuccess(BaseResponseInfo bInfo) {
                LogUtils.e("===========", bInfo.getValue());
                mDialog.dismiss();
                initData();
                segmentControl.setSelectedIndex(0);
                String time1 = "";
                String time2 = "";
                if (mSuccTime == null) {
                    return;
                }

                time1 = MyTimeUtil.getDateYMD(mSuccTime);
                time2 = MyTimeUtil.getHM(mSuccTime);
                uuImgInfoDialog = PopBoxCreat.createUUImgInfoDialog(
                        CarFlowPackageRechargeActivity.this, "充值成功", "您好，您于"
                                + time1 + "\n" + time2 + "，成功充值流量"
                                + packageDateParse(packageSize), "确认",
                        new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                uuImgInfoDialog.dismiss();
                            }
                        });
                uuImgInfoDialog.mImgView.setVisibility(View.GONE);
                uuImgInfoDialog.content1.setVisibility(View.VISIBLE);
                uuImgInfoDialog.content2.setVisibility(View.VISIBLE);
                uuImgInfoDialog.show();
            }

            @Override
            public void onError(BaseResponseInfo bInfo) {
                mDialog.dismiss();
                LogUtils.e("onError----" + bInfo.getValue());
                if (bInfo != null && bInfo.getInfo() != null) {
                    UUToast.showUUToast(CarFlowPackageRechargeActivity.this,
                            bInfo.getInfo());
                } else {
                    UUToast.showUUToast(CarFlowPackageRechargeActivity.this,
                            "支付失败");
                }
            }
        });
    }

    private FlowPriceInfo mFlowPriceInfo;

    private void showPackagePriceList(ArrayList<FlowPriceInfo> flowPriceDataList, BaseResponseInfo bInfo) {
        if (flowPriceDataList != null && flowPriceDataList.size() != 0) {
            lvPriceList.setVisibility(View.VISIBLE);
            tvPriceEmptyHint.setVisibility(View.GONE);
            FlowPriceListAdapter priceListAdapter = new FlowPriceListAdapter(
                    this, flowPriceDataList);
            lvPriceList.setAdapter(priceListAdapter);
            sv.smoothScrollTo(0, 0);
            priceListAdapter
                    .setOnTvRechargeListener(new FlowPriceListAdapter.TvRechargeClickListener() {

                        @Override
                        public void onTvRechargeClick(
                                List<FlowPriceInfo> mDataList, int pos) {

                            mFlowPriceInfo = mDataList.get(pos);
                            if (mFlowPriceInfo != null) {

                                tvCommodityDetail.setText("流量充值 " + mFlowPriceInfo.price);
                            }

                            LogUtils.e("mFlowPriceInfo-----",
                                    mFlowPriceInfo.toString());
                            mPopupWindowView.startAnimation(ani1);
                            mPopupWindow.showAtLocation(mPopupWindowView,
                                    Gravity.BOTTOM, 0, 0);
                        }
                    });
            // priceListAdapter.notifyDataSetChanged();

        } else {
            lvPriceList.setVisibility(View.GONE);
            tvPriceEmptyHint.setVisibility(View.VISIBLE);
            if (bInfo != null) {
                tvPriceEmptyHint.setText(bInfo.getInfo());
            }
        }
    }

    protected void parsePackagePrice(BaseResponseInfo bInfo) {
        String flowTerm = "";
        String finalDate = "";
        LogUtils.e("====", bInfo.toString());
        //        bInfo.setValue("[]");
        Object value = bInfo.getValue();
        if (warnningInfo.data != null) {
            flowTerm = warnningInfo.data.service_data_end;
            finalDate = warnningInfo.data.final_service_data_end;
        }
        try {
            JSONArray ja = new JSONArray(value.toString());
            ArrayList<FlowPriceInfo> changePrices = new ArrayList<>();
            for (int i = 0; i < ja.length(); i++) {
                FlowPriceInfo info1 = new FlowPriceInfo();
                JSONObject joCh = ja.getJSONObject(i);
                info1.id = joCh.optInt("id");
                String optString = joCh.optString("package_size");
                info1.package_size = Integer.valueOf(optString);
                String price = joCh.optString("price");
                info1.price = price + "元";
                info1.packDes = "套餐总流量修改";
                info1.flowTerm = "次月1日生效，" + "至" + flowTerm;
                changePrices.add(info1);
            }
            LogUtils.e("changePrices====",
                    changePrices.toString());
            showPackagePriceList(changePrices, bInfo);

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private ArrayList<String> changeItems = new ArrayList<>();
    private ArrayList<String> refuleItems = new ArrayList<>();
    private ArrayList<String> renewItems = new ArrayList<>();
    private ArrayList<FlowPriceInfo> changePriceInfos = new ArrayList<>();
    private ArrayList<FlowPriceInfo> refulePriceInfos = new ArrayList<>();
    private ArrayList<FlowPriceInfo> renewPriceInfos = new ArrayList<>();

    //解析流量包json
    private void parseFlowProductJson(Response<String> response) {
        changeItems.clear();
        refuleItems.clear();
        renewItems.clear();
        changePriceInfos.clear();
        refulePriceInfos.clear();
        renewPriceInfos.clear();
        if (response != null) {
            Gson gson = new Gson();
            PackageDataInfo dataInfo = gson.fromJson(response.body(),
                    PackageDataInfo.class);

            List<List<FlowPriceInfo>> change = dataInfo.data.change;
            List<List<FlowPriceInfo>> refuel = dataInfo.data.refuel;
            List<List<FlowPriceInfo>> renew = dataInfo.data.renew;
            String flowTerm = "";
            String finalDate = "2018-03-12";
            int packageType = -1;
            if (warnningInfo != null) {
                flowTerm = warnningInfo.data.service_data_end;
                finalDate = warnningInfo.data.final_service_data_end;
                packageType = Integer.valueOf(warnningInfo.data.package_type);
            }

            LogUtils.e("dataInfo====", dataInfo.toString());
            for (int i = 0; i < change.size(); i++) {
                String name = change.get(i).get(0).name;
                changeItems.add(name);
                List<FlowPriceInfo> list = change.get(i);
                for (int j = 0; j < list.size(); j++) {
                    FlowPriceInfo flowPriceInfo = list.get(j);
                    changePriceInfos.add(flowPriceInfo);
                }
            }
            for (int i = 0; i < refuel.size(); i++) {
                String name = refuel.get(i).get(0).name;
                refuleItems.add(name);
                List<FlowPriceInfo> list = refuel.get(i);
                for (int j = 0; j < list.size(); j++) {
                    FlowPriceInfo flowPriceInfo = list.get(j);
                    String price = flowPriceInfo.price;
                    flowPriceInfo.price = price + "元";
                    flowPriceInfo.packDes = "当月加油包";
                    flowPriceInfo.flowTerm = "充值成功后生效，当月有效";
                    refulePriceInfos.add(flowPriceInfo);
                }
            }
            for (int i = 0; i < renew.size(); i++) {
                String name = renew.get(i).get(0).name;
                renewItems.add(name);
                List<FlowPriceInfo> list = renew.get(i);
                for (int j = 0; j < list.size(); j++) {
                    FlowPriceInfo flowPriceInfo = list.get(j);

                    String price = flowPriceInfo.price;
                    flowPriceInfo.price = price + "元";
                    String monthToYear = monthToYear(flowPriceInfo.package_month);
                    flowPriceInfo.packDes = "延长套餐"
                            + monthToYear + "服务时间";

                    String endDate = doDate(finalDate,
                            flowPriceInfo.package_month);

                    if (!TextUtils.isEmpty(endDate)) {
                        String dateNextDay = dateNextDay(finalDate);
                        flowPriceInfo.flowTerm = dateNextDay + "生效，" + "至"
                                + endDate;
                    }
                    if (packageType == 0 || packageType == 2) {
                        String currrentDate = getCurrrentdate();
                        String end = doDate(currrentDate,
                                flowPriceInfo.package_month);
                        flowPriceInfo.flowTerm = currrentDate + "生效，" + "至"
                                + end;
                    }
                    renewPriceInfos.add(flowPriceInfo);
                }
            }
        }

        showFlowPackageInfos(refuleItems, refulePriceInfos);

    }

    private String monthToYear(int package_month) {
        String year = "";

        if (package_month > 0 && package_month % 12 == 0) {
            year = package_month / 12 + "年";
        } else {
            return package_month + "个月";
        }
        return year;

    }

    private String doDate(String d, int m) {
        String resultDate = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date now;
        try {
            now = sdf.parse(d);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(now);
            calendar.add(Calendar.MONTH, m);
            Date time = calendar.getTime();
            resultDate = sdf.format(time);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return resultDate;
        }
        return resultDate;
    }

    // 日期次日
    public String dateNextDay(String _date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cl = Calendar.getInstance();
        Date date = null;
        if (warnningInfo.data.package_type.equals("2")) {
            //        if ("2".equals("2")) {
            long currentTimeMillis = System.currentTimeMillis();
            return sdf.format(currentTimeMillis);
        }
        try {
            date = (Date) sdf.parse(_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        cl.setTime(date);
        // if ("pre".equals(option)) {
        // // 时间减一天
        // cl.add(Calendar.DAY_OF_MONTH, -1);
        //
        // } else if ("next".equals(option)) {
        // // 时间加一天
        // cl.add(Calendar.DAY_OF_YEAR, 1);
        // } else {
        // // do nothing
        // }
        cl.add(Calendar.DAY_OF_YEAR, 1);
        date = cl.getTime();
        return sdf.format(date);
    }

    private String getCurrrentdate() {

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");// 设置日期格式
        // 拿到当前时间
        String date = df.format(new Date());
        return date;

    }

    private String packageDateParse(float packageSize) {
        String size = null;
        if (packageSize == 0.0f || packageSize == Float.NaN) {
            size = String.format("%.2f", 0.0f) + "M";

        } else if (Math.abs(packageSize) >= 1024f) {
            size = String.format("%.2f", packageSize / 1024) + "G";
        } else {
            size = String.format("%.2f", packageSize) + "M";
        }
        return size;
    }
}

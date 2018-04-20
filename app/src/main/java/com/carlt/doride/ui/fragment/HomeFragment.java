package com.carlt.doride.ui.fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.carlt.doride.R;
import com.carlt.doride.base.BaseFragment;
import com.carlt.doride.control.CPControl;
import com.carlt.doride.data.BaseResponseInfo;
import com.carlt.doride.data.home.InformationCategoryInfo;
import com.carlt.doride.data.home.InformationCategoryInfoList;
import com.carlt.doride.data.home.MilesInfo;
import com.carlt.doride.protocolparser.BaseParser;
import com.carlt.doride.ui.activity.home.InformationCentreActivity;
import com.carlt.doride.ui.activity.home.ReportActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by Marlon on 2018/3/15.
 */

public class HomeFragment extends BaseFragment implements View.OnClickListener {
    private ImageView mIvReport;
    private RelativeLayout mRlInformationCentre;
    private TextView mTxtDate;
    private String currentDate;
    private TextView mTextView3;
    private TextView mTextView4;
    private TextView mTxtObd;   //仪表盘里程
    private TextView mTxtEnduranceMile; //续航里程
    private TextView mTxtAvgSpeed;  //平均速度
    private TextView mTxtAvgFuel;   //平均油耗
    private MilesInfo milesInfo;    //大乘远程 读取里程实体类

    @Override
    protected View inflateView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_home, null);
        return view;
    }

    @Override
    public void init(View view) {
        mIvReport = $ViewByID(R.id.activity_home_iv_report);
        mRlInformationCentre = $ViewByID(R.id.activity_home_relative2);
        mTxtDate = $ViewByID(R.id.home_txt_date);
        mTextView3 = $ViewByID(R.id.activity_home_txt3);
        mTextView4 = $ViewByID(R.id.activity_home_txt4);
        mTxtObd = $ViewByID(R.id.layout_report_grid_linear2_txt2);
        mTxtEnduranceMile = $ViewByID(R.id.layout_report_grid_linear1_txt2);
        mTxtAvgSpeed = $ViewByID(R.id.layout_report_grid_linear3_txt2);
        mTxtAvgFuel = $ViewByID(R.id.layout_report_grid_linear4_txt2);
        mIvReport.setOnClickListener(this);
        mRlInformationCentre.setOnClickListener(this);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        currentDate = format.format(new Date(System.currentTimeMillis()));
        mTxtDate.setText(currentDate);
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void loadData() {
        super.loadData();
        CPControl.GetInformationCentreInfoListResult(callback);
        CPControl.GetMilesInfoResult(remoteCallback);

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_home_iv_report:  //跳转行车报告
                Intent mIntent = new Intent(getContext(), ReportActivity.class);
                mIntent.putExtra("c", 0);
                mIntent.putExtra(ReportActivity.DAY_INITIAL, currentDate);
                startActivity(mIntent);
                break;
            case R.id.activity_home_relative2:  //跳转信息中心
                Intent mIntent1 = new Intent(getContext(), InformationCentreActivity.class);
                getActivity().startActivityForResult(mIntent1,REQUESTCODE);
                break;
        }
    }

    private final static int REQUESTCODE = 1; // 返回的结果码

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUESTCODE){
                Log.e(TAG,"loadData  onActivityResult");
                loadData();
        }
    }

    BaseParser.ResultCallback callback = new BaseParser.ResultCallback() {
        @Override
        public void onSuccess(BaseResponseInfo bInfo) {
            Message message = new Message();
            message.what = 0;
            message.obj = bInfo;
            mHandler.sendMessage(message);
        }

        @Override
        public void onError(BaseResponseInfo bInfo) {
            Message message = new Message();
            message.what = 1;
            message.obj = bInfo;
            mHandler.sendMessage(message);
        }
    };

    BaseParser.ResultCallback remoteCallback = new BaseParser.ResultCallback() {
        @Override
        public void onSuccess(BaseResponseInfo bInfo) {
            Message message = new Message();
            message.what = 2;
            message.obj = bInfo;
            mHandler.sendMessage(message);
        }

        @Override
        public void onError(BaseResponseInfo bInfo) {
            Message message = new Message();
            message.what = 3;
            message.obj = bInfo;
            mHandler.sendMessage(message);
        }
    };

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    actLoadSuccess((BaseResponseInfo) msg.obj);
                    break;
                case 1:
                    actLoadError((BaseResponseInfo) msg.obj);
                    break;
                case 2:
                    loadRemoteSuccess((BaseResponseInfo) msg.obj);
                    break;
                case 3:
                    loadRemoteError((BaseResponseInfo) msg.obj);
                    break;
            }
        }
    };

    private void loadRemoteError(BaseResponseInfo bInfo) {
//        UUToast.showUUToast(getContext(),bInfo.getInfo());
        mTxtObd.setText("--");
        mTxtEnduranceMile.setText("--");
        mTxtAvgSpeed.setText("--");
        mTxtAvgFuel.setText("--");
    }

    protected void loadRemoteSuccess(BaseResponseInfo bInfo) {
        milesInfo = (MilesInfo) (bInfo.getValue());
        if (milesInfo != null) {
            mTxtObd.setText(milesInfo.getObd() + "");
            mTxtEnduranceMile.setText(milesInfo.getEnduranceMile() + "");
            mTxtAvgSpeed.setText(milesInfo.getAvgSpeed() + "");
            mTxtAvgFuel.setText(milesInfo.getAvgFuel() + "");
        }
    }


    @Override
    protected void actLoadNoData() {
        super.actLoadNoData();
    }

    @Override
    protected void actLoadError(BaseResponseInfo bInfo) {
        super.actLoadError(bInfo);
    }

    @Override
    protected void actLoadSuccess(BaseResponseInfo binfo) {
        super.actLoadSuccess(binfo);
        InformationCategoryInfoList infoList = (InformationCategoryInfoList) binfo.getValue();
        if (infoList != null) {
            // 车秘书未读信息条数
            String unreadmessage = "";
            // 车秘书最新消息内容
            String latestmessage = "";

            ArrayList<InformationCategoryInfo> mInformationCategoryInfos = infoList.getmAllList();
            ArrayList<InformationCategoryInfo> unReadMsgInfos = new ArrayList<>();
            ArrayList<InformationCategoryInfo> readMsgInfos = new ArrayList<>();
            for (int i = 0; i <mInformationCategoryInfos.size() ; i++) {
                if (Integer.parseInt(mInformationCategoryInfos.get(i).getMsgcount())>0){
                    unReadMsgInfos.add(mInformationCategoryInfos.get(i));
                }else {
                    readMsgInfos.add(mInformationCategoryInfos.get(i));
                }
            }
            if (unReadMsgInfos.size()>0){
                lastTime(unReadMsgInfos);
                latestmessage = unReadMsgInfos.get(0).getLastmsg();
                unreadmessage = unReadMsgInfos.get(0).getMsgcount();
            }else {
                lastTime(readMsgInfos);
                latestmessage = readMsgInfos.get(0).getLastmsg();
                unreadmessage = readMsgInfos.get(0).getMsgcount();
            }



            if (unreadmessage != null && unreadmessage.length() > 0) {
                if (unreadmessage.equals("0")) {
                    mTextView3.setVisibility(View.GONE);
                    if (latestmessage != null && latestmessage.length() > 0
                            && !latestmessage.equals("null")) {
                        mTextView4.setText("最后一条：" + latestmessage + "");
                    } else {
                        mTextView4.setText("最后一条：");
                    }
                } else {
                    mTextView3.setVisibility(View.VISIBLE);
                    if (latestmessage != null && latestmessage.length() > 0
                            && !latestmessage.equals("null")) {
                        mTextView4.setText("新消息：" + latestmessage + "");
                    } else {
                        mTextView4.setText("新消息：");
                    }
                }
            } else {
                mTextView3.setVisibility(View.GONE);
                if (latestmessage != null && latestmessage.length() > 0
                        && !latestmessage.equals("null")) {
                    mTextView4.setText(latestmessage + "");
                } else {
                    mTextView4.setText("");
                }
            }
        }
    }
    private void lastTime(ArrayList<InformationCategoryInfo> list){
        Collections.sort(list, new Comparator<InformationCategoryInfo>() {
            @Override
            public int compare(InformationCategoryInfo o1, InformationCategoryInfo o2) {
                Date date1 = new Date(Long.parseLong(o1.getMsgdate()));
                Date date2 = new Date(Long.parseLong(o2.getMsgdate()));

                return date2.compareTo(date1);
            }
        });
    }
}

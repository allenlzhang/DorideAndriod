package com.carlt.doride.protocolparser;

import android.text.TextUtils;

import com.carlt.doride.R;
import com.carlt.doride.data.remote.AirMainInfo;
import com.carlt.doride.data.remote.RemoteFunInfo;
import com.carlt.doride.data.remote.RemoteMainInfo;
import com.carlt.doride.model.LoginInfo;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class CarOperationConfigParser<T> extends BaseParser<T>  {

    private RemoteMainInfo mRemoteMainInfo = new RemoteMainInfo();

    private AirMainInfo mAirMainInfo = new AirMainInfo();

    private int supportCount=0;

    public CarOperationConfigParser(ResultCallback callback) {
        super(callback);
        //TODO TEST DATA
//        setTestFileName("json_CarOperationConfigParser.txt") ;
//        setTest(true);
    }

    public RemoteMainInfo getReturn() {
        return mRemoteMainInfo;
    }

    @Override
    protected void parser() throws Exception {
        JsonObject mJSON_data = mJson.getAsJsonObject("data");
        if (mJSON_data != null) {



           String directPSTsupervise =  mJSON_data.get("directPSTsupervise").getAsInt() + "";
           String navigationSync =  mJSON_data.get("navigationSync").getAsInt() + "";

            mRemoteMainInfo.setDirectPSTsupervise(directPSTsupervise);
            mRemoteMainInfo.setNavigationSync(navigationSync);

            RemoteFunInfo mFunInfo;
            String state = "";

            mFunInfo = new RemoteFunInfo();
            mFunInfo.setId("-2");
            mFunInfo.setApi_field("remoteStart");
            mFunInfo.setName("start");
            mFunInfo.setIcon_id(0);
            mFunInfo.setState( mJSON_data.get("remoteStart").getAsInt() + "");
            mRemoteMainInfo.setmFunInfoStart(mFunInfo);

            mFunInfo = new RemoteFunInfo();
            mFunInfo.setId("-1");
            mFunInfo.setApi_field("remoteStart");
            mFunInfo.setName("stop");
            mFunInfo.setIcon_id(0);
            mFunInfo.setState(mJSON_data.get("remoteStart").getAsInt() + "");
            mRemoteMainInfo.setmFunInfoStop(mFunInfo);

            mFunInfo = new RemoteFunInfo();
            mFunInfo.setId("1");
            mFunInfo.setApi_field("remoteLocked");
            mFunInfo.setName("车门解锁");
            mFunInfo.setIcon_id(R.drawable.remote_openlock_selector);
            state = mJSON_data.get("remoteLocked").getAsInt()+"";
            mFunInfo.setState(state);
            if (state.equals(RemoteFunInfo.STATE_SUPPORT)) {
                mRemoteMainInfo.addmRemoteFunInfos(mFunInfo);
            }

            mFunInfo = new RemoteFunInfo();
            mFunInfo.setId("2");
            mFunInfo.setApi_field("remoteLocked");
            mFunInfo.setName("车门落锁");
            mFunInfo.setIcon_id(R.drawable.remote_lock_selector);
            state = mJSON_data.get("remoteLocked").getAsInt()+"";
            mFunInfo.setState(state);
            if (state.equals(RemoteFunInfo.STATE_SUPPORT)) {
                mRemoteMainInfo.addmRemoteFunInfos(mFunInfo);
            }

            mFunInfo = new RemoteFunInfo();
            mFunInfo.setId("12");
            mFunInfo.setApi_field("remoteTrunk");
            mFunInfo.setName("开启后备箱");
            mFunInfo.setIcon_id(R.drawable.trunck);
            state = mJSON_data.get("remoteTrunk").getAsInt()+"";
            mFunInfo.setState(state);
            if (state.equals(RemoteFunInfo.STATE_SUPPORT)) {
                mRemoteMainInfo.addmRemoteFunInfos(mFunInfo);
            }

            mFunInfo = new RemoteFunInfo();
            mFunInfo.setId("10");
            mFunInfo.setApi_field("remoteAirconditioner");
            mFunInfo.setName("空调");
            mFunInfo.setIcon_id(R.drawable.air_condition);
            state = mJSON_data.get("remoteAirconditioner").getAsInt()+"";
            mFunInfo.setState(state);
            if (state.equals(RemoteFunInfo.STATE_SUPPORT)) {
                mRemoteMainInfo.addmRemoteFunInfos(mFunInfo);
            }

            mFunInfo = new RemoteFunInfo();
            mFunInfo.setId("11");
            mFunInfo.setApi_field("SLCarLocating");
            mFunInfo.setName("一键寻车");
            mFunInfo.setIcon_id(R.drawable.horm);
            state = mJSON_data.get("SLCarLocating").getAsInt()+"";
            mFunInfo.setState(state);
            if (state.equals(RemoteFunInfo.STATE_SUPPORT)) {
                mRemoteMainInfo.addmRemoteFunInfos(mFunInfo);
            }

            mFunInfo = new RemoteFunInfo();
            mFunInfo.setId("4");
            mFunInfo.setApi_field("remoteWinSw");
            mFunInfo.setName("升起车窗");
            mFunInfo.setIcon_id(R.drawable.remote_rise_up_window_selector);
            state = mJSON_data.get("remoteWinSw").getAsInt()+"";
            mFunInfo.setState(state);
            if (state.equals(RemoteFunInfo.STATE_SUPPORT)) {
                mRemoteMainInfo.addmRemoteFunInfos(mFunInfo);
            }

            mFunInfo = new RemoteFunInfo();
            mFunInfo.setId("3");
            mFunInfo.setApi_field("remoteWinSw");
            mFunInfo.setName("降下车窗");
            mFunInfo.setIcon_id(R.drawable.remote_rise_down_window_selector);
            state = mJSON_data.get("remoteWinSw").getAsInt()+"";
            mFunInfo.setState(state);
            if (state.equals(RemoteFunInfo.STATE_SUPPORT)) {
                mRemoteMainInfo.addmRemoteFunInfos(mFunInfo);
            }

            // 四个子项目，加一个主项目
            RemoteFunInfo mFunInfo6 = new RemoteFunInfo();
            mFunInfo6.setId("6");
            mFunInfo6.setApi_field("remoteSwitchSkylight");
            mFunInfo6.setName("开启天窗");
            mFunInfo6.setIcon_id(R.drawable.remote_top_win_open_selector);
            String state6 = mJSON_data.get("remoteSwitchSkylight").getAsInt()+"";
            mFunInfo6.setState(state6);

            RemoteFunInfo mFunInfo8 = new RemoteFunInfo();
            mFunInfo8.setId("8");
            mFunInfo8.setApi_field("remoteSkylightPry");
            mFunInfo8.setName("天窗开翘");
            mFunInfo8.setIcon_id(R.drawable.remote_top_win_open1_selector);
            String state8 = mJSON_data.get("remoteSkylightPry").getAsInt()+"";
            mFunInfo8.setState(state8);

            RemoteFunInfo mFunInfo7 = new RemoteFunInfo();
            mFunInfo7.setId("7");
            mFunInfo7.setApi_field("remoteSwitchSkylight");
            mFunInfo7.setName("关闭天窗");
            mFunInfo7.setIcon_id(R.drawable.remote_top_win_close1_selector);
            String state7 = "";
//			state7 = "1";
            if(state8.equals(RemoteFunInfo.STATE_SUPPORT) || state6.equals(RemoteFunInfo.STATE_SUPPORT)){
                state7 = RemoteFunInfo.STATE_SUPPORT;
            }else{
                state7 = RemoteFunInfo.STATE_NONSUPPORT;
            }
            mFunInfo7.setState(state7);

            mFunInfo = new RemoteFunInfo();
            mFunInfo.setId("5");
            mFunInfo.setApi_field("top_windows");
            mFunInfo.setName("天窗");
            mFunInfo.setIcon_id(R.drawable.remote_top_windows);
            List<RemoteFunInfo> apiFieldLists2 = new ArrayList<RemoteFunInfo>();
            if (state6.equals(RemoteFunInfo.STATE_SUPPORT)) {
                apiFieldLists2.add(mFunInfo6);
            }
            if (state8.equals(RemoteFunInfo.STATE_SUPPORT)) {
                apiFieldLists2.add(mFunInfo8);
            }

            if (state7.equals(RemoteFunInfo.STATE_SUPPORT)) {
                apiFieldLists2.add(mFunInfo7);
            }

            mFunInfo.setApiFieldLists(apiFieldLists2);
            if (state6.equals(RemoteFunInfo.STATE_SUPPORT)
                    || state8.equals(RemoteFunInfo.STATE_SUPPORT)) {
                mFunInfo.setState(RemoteFunInfo.STATE_SUPPORT);
                mRemoteMainInfo.addmRemoteFunInfos(mFunInfo);
            } else {
                mFunInfo.setState(RemoteFunInfo.STATE_NONSUPPORT);
            }

            LoginInfo.setRemoteMainInfo(mRemoteMainInfo);

            String remote_airconditioner_item=mJSON_data.get("remoteAirconditioner_item").getAsString();
            if(!TextUtils.isEmpty(remote_airconditioner_item)){
                String[] items=remote_airconditioner_item.split(",");
                int index;
                index=remote_airconditioner_item.indexOf("1");
                if(index!=-1){
                    mFunInfo = new RemoteFunInfo();
                    mFunInfo.setId(RemoteFunInfo.MODE_AUTO);
                    mFunInfo.setApi_field("automatic");
                    mFunInfo.setName("全自动");
                    mFunInfo.setIcon_id(R.drawable.remote_auto);
                    mFunInfo.setIcon_id_seleced(R.mipmap.remote_auto_selected);
                    mFunInfo.setIcon_id_seleced_no(R.mipmap.remote_auto_selected_no);
                    state = RemoteFunInfo.STATE_SUPPORT;
                    mFunInfo.setState(state);
                    mFunInfo.setTemperature("24");
                    if (state.equals(RemoteFunInfo.STATE_SUPPORT)) {
                        mAirMainInfo.addmRemoteFunInfos(mFunInfo);
                    }

                    supportCount++;
                }

                index=remote_airconditioner_item.indexOf("5");
                if(index!=-1){
                    mFunInfo = new RemoteFunInfo();
                    mFunInfo.setId(RemoteFunInfo.MODE_HEAT);
                    mFunInfo.setApi_field("maxhot");
                    mFunInfo.setName("最大制热");
                    mFunInfo.setIcon_id(R.drawable.remote_hot);
                    mFunInfo.setIcon_id_seleced(R.mipmap.remote_hot_selected);
                    mFunInfo.setIcon_id_seleced_no(R.mipmap.remote_hot_selected_no);
                    state = RemoteFunInfo.STATE_SUPPORT;
                    mFunInfo.setState(state);
                    mFunInfo.setTemperature("32");
                    if (state.equals(RemoteFunInfo.STATE_SUPPORT)) {
                        mAirMainInfo.addmRemoteFunInfos(mFunInfo);
                    }

                    supportCount++;
                }

                index=remote_airconditioner_item.indexOf("4");
                if(index!=-1){
                    mFunInfo = new RemoteFunInfo();
                    mFunInfo.setId(RemoteFunInfo.MODE_COLD);
                    mFunInfo.setApi_field("maxcold");
                    mFunInfo.setName("最大制冷");
                    mFunInfo.setIcon_id(R.drawable.remote_cold);
                    mFunInfo.setIcon_id_seleced(R.mipmap.remote_cold_selected);
                    mFunInfo.setIcon_id_seleced_no(R.mipmap.remote_cold_selected_no);
                    state = RemoteFunInfo.STATE_SUPPORT;
                    mFunInfo.setState(state);
                    mFunInfo.setTemperature("18");
                    if (state.equals(RemoteFunInfo.STATE_SUPPORT)) {
                        mAirMainInfo.addmRemoteFunInfos(mFunInfo);
                    }

                    supportCount++;
                }

                index=remote_airconditioner_item.indexOf("3");
                if(index!=-1){
                    mFunInfo = new RemoteFunInfo();
                    mFunInfo.setId(RemoteFunInfo.MODE_FROG);
                    mFunInfo.setApi_field("defrost");
                    mFunInfo.setName("一键除霜");
                    mFunInfo.setIcon_id(R.drawable.remote_frost);
                    mFunInfo.setIcon_id_seleced(R.mipmap.remote_frost_selected);
                    mFunInfo.setIcon_id_seleced_no(R.mipmap.remote_frost_selected_no);
                    state = RemoteFunInfo.STATE_SUPPORT;
                    mFunInfo.setState(state);
                    mFunInfo.setTemperature("32");
                    if (state.equals(RemoteFunInfo.STATE_SUPPORT)) {
                        mAirMainInfo.addmRemoteFunInfos(mFunInfo);
                    }

                    supportCount++;
                }


                index=remote_airconditioner_item.indexOf("8");
                if(index!=-1){
                    mFunInfo = new RemoteFunInfo();
                    mFunInfo.setId(RemoteFunInfo.MODE_TEMPREGULATION);
                    mFunInfo.setApi_field("tempAdjustment");
                    mFunInfo.setName("温度调节");
                    mFunInfo.setIcon_id(R.drawable.remote_regulation);
                    mFunInfo.setIcon_id_seleced(R.mipmap.remote_regulation_selected);
                    mFunInfo.setIcon_id_seleced_no(R.mipmap.remote_regulation_selected_no);
                    state = RemoteFunInfo.STATE_SUPPORT;
                    mFunInfo.setState(state);
                    mFunInfo.setTemperature("18");
                    if (state.equals(RemoteFunInfo.STATE_SUPPORT)) {
                        mAirMainInfo.addmRemoteFunInfos(mFunInfo);
                    }

                    supportCount++;
                }


                index=remote_airconditioner_item.indexOf("2");
                if(index!=-1){
                    mFunInfo = new RemoteFunInfo();
                    mFunInfo.setId(RemoteFunInfo.MODE_CLOSE);
                    mFunInfo.setApi_field("close");
                    mFunInfo.setName("关闭空调");
                    mFunInfo.setIcon_id(R.drawable.remote_close_air2);
                    mFunInfo.setIcon_id_seleced(R.mipmap.icon_close_air_press);
                    mFunInfo.setIcon_id_seleced_no(R.mipmap.icon_close_air);
                    state = RemoteFunInfo.STATE_SUPPORT;
                    mFunInfo.setState(state);
                    mFunInfo.setTemperature("--");
                    if (state.equals(RemoteFunInfo.STATE_SUPPORT)) {
                        mAirMainInfo.addmRemoteFunInfos(mFunInfo);
                    }
                    supportCount++;
                }

                index=remote_airconditioner_item.indexOf("9");
                if(index!=-1){
                    mAirMainInfo.setShowTemp(true);
                }else{
                    mAirMainInfo.setShowTemp(false);
                }

                mAirMainInfo.setFunctionCount(supportCount+"");

                mRemoteMainInfo.setmAirMainInfo(mAirMainInfo);
            }
        }

    }


}

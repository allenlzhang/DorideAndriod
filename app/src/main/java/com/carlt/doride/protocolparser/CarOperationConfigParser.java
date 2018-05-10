package com.carlt.doride.protocolparser;

import android.text.TextUtils;

import com.carlt.doride.R;
import com.carlt.doride.data.remote.AirMainInfo;
import com.carlt.doride.data.remote.RemoteFunInfo;
import com.carlt.doride.data.remote.RemoteMainInfo;
import com.carlt.doride.model.LoginInfo;
import com.google.gson.JsonObject;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

public class CarOperationConfigParser<T> extends BaseParser<T> {

    private RemoteMainInfo mRemoteMainInfo = new RemoteMainInfo();

    private AirMainInfo mAirMainInfo = new AirMainInfo();

    private int supportCount = 0;

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


            String directPSTsupervise = mJSON_data.get("directPSTsupervise").getAsInt() + "";
            String navigationSync = mJSON_data.get("navigationSync").getAsInt() + "";

            mRemoteMainInfo.setDirectPSTsupervise(directPSTsupervise);
            mRemoteMainInfo.setNavigationSync(navigationSync);

            RemoteFunInfo mFunInfo;
            String state = "";

            mFunInfo = new RemoteFunInfo();
            mFunInfo.setId("-2");
            mFunInfo.setApi_field("remoteStart");
            mFunInfo.setName("start");
            mFunInfo.setIcon_id(0);
            mFunInfo.setState(mJSON_data.get("remoteStart").getAsInt() + "");
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
            mFunInfo.setName("车锁");
            mFunInfo.setIcon_id(R.drawable.remote_openlock_selector);
            state = mJSON_data.get("remoteLocked").getAsInt() + "";
            mFunInfo.setState(state);
            if (state.equals(RemoteFunInfo.STATE_SUPPORT)) {
                mRemoteMainInfo.addmRemoteFunInfos(mFunInfo);
            }

            mFunInfo = new RemoteFunInfo();
            mFunInfo.setId("3");
            mFunInfo.setApi_field("remoteWinSw");
            mFunInfo.setName("车窗");
            mFunInfo.setIcon_id(R.drawable.remote_rise_down_window_selector);
            state = mJSON_data.get("remoteWinSw").getAsInt() + "";
            mFunInfo.setState(state);
            if (state.equals(RemoteFunInfo.STATE_SUPPORT)) {
                mRemoteMainInfo.addmRemoteFunInfos(mFunInfo);
            }
            // 四个子项目，加一个主项目
            RemoteFunInfo mFunInfo6 = new RemoteFunInfo();
            mFunInfo6.setId("6");
            mFunInfo6.setApi_field("remoteSwitchSkylight");
            mFunInfo6.setName("打开天窗");
            mFunInfo6.setIcon_id(R.drawable.remote_top_win_open_selector);
            String state6 = mJSON_data.get("remoteSwitchSkylight").getAsInt() + "";
            mFunInfo6.setState(state6);

            RemoteFunInfo mFunInfo8 = new RemoteFunInfo();
            mFunInfo8.setId("8");
            mFunInfo8.setApi_field("remoteSkylightPry");
            mFunInfo8.setName("天窗开翘");
            mFunInfo8.setIcon_id(R.drawable.remote_top_win_open1_selector);
            String state8 = mJSON_data.get("remoteSkylightPry").getAsInt() + "";
            mFunInfo8.setState(state8);

            //            RemoteFunInfo mFunInfo9 = new RemoteFunInfo();
            //            mFunInfo9.setId("9");
            //            mFunInfo9.setApi_field("remoteSkylightPry");
            //            mFunInfo9.setName("天窗关翘");
            //            mFunInfo9.setIcon_id(R.drawable.remote_top_win_close1_selector);
            //            String state9 = mJSON_data.get("remoteSkylightPry").getAsInt() + "";
            //            mFunInfo9.setState(state9);


            RemoteFunInfo mFunInfo7 = new RemoteFunInfo();
            mFunInfo7.setId("7");
            mFunInfo7.setApi_field("remoteSwitchSkylight");
            mFunInfo7.setName("关闭天窗");
            mFunInfo7.setIcon_id(R.drawable.remote_top_win_close1_selector);
            String state7 = "";
            //			state7 = "1";
            if (state8.equals(RemoteFunInfo.STATE_SUPPORT) || state6.equals(RemoteFunInfo.STATE_SUPPORT)) {
                state7 = RemoteFunInfo.STATE_SUPPORT;
            } else {
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
            //            if (state9.equals(RemoteFunInfo.STATE_SUPPORT)) {
            //                apiFieldLists2.add(mFunInfo9);
            //            }

            mFunInfo.setApiFieldLists(apiFieldLists2);
            if (state6.equals(RemoteFunInfo.STATE_SUPPORT)
                    || state8.equals(RemoteFunInfo.STATE_SUPPORT)) {
                mFunInfo.setState(RemoteFunInfo.STATE_SUPPORT);
                mRemoteMainInfo.addmRemoteFunInfos(mFunInfo);
            } else {
                mFunInfo.setState(RemoteFunInfo.STATE_NONSUPPORT);
            }

            mFunInfo = new RemoteFunInfo();
            mFunInfo.setId("10");
            mFunInfo.setApi_field("remoteAirconditioner");
            mFunInfo.setName("空调");
            mFunInfo.setIcon_id(R.drawable.air_condition);
            state = mJSON_data.get("remoteAirconditioner").getAsInt() + "";
            mFunInfo.setState(state);
            if (state.equals(RemoteFunInfo.STATE_SUPPORT)) {
                mRemoteMainInfo.addmRemoteFunInfos(mFunInfo);
            }


            mFunInfo = new RemoteFunInfo();
            mFunInfo.setId("13");
            mFunInfo.setApi_field("remoteSeatHeating");
            mFunInfo.setName("座椅加热");
            mFunInfo.setIcon_id(R.drawable.remote_seat_heating);
            state = mJSON_data.get("remoteSeatHeating").getAsInt() + "";
//            Logger.e("remoteSeatHeating----" + mJSON_data.get("remoteSeatHeating").getAsInt() + "");
            mFunInfo.setState(state);
            if (state.equals(RemoteFunInfo.STATE_SUPPORT)) {
                mRemoteMainInfo.addmRemoteFunInfos(mFunInfo);
            }
            //          同时支持关闭和打开后备箱
            RemoteFunInfo mFunInfo12 = new RemoteFunInfo();
            mFunInfo12.setId("12");
            mFunInfo12.setApi_field("remoteTrunk");
            mFunInfo12.setName("打开后备箱");
            mFunInfo12.setIcon_id(R.drawable.trunck);
            String state12 = mJSON_data.get("remoteTrunkOn").getAsInt() + "";
            String remoteTrunkOff = mJSON_data.get("remoteTrunkOff").getAsInt() + "";
//            Logger.e("remoteTrunkOn----" + mJSON_data.get("remoteTrunkOn").getAsInt() + "");
//            Logger.e("remoteTrunkOff----" + mJSON_data.get("remoteTrunkOff").getAsInt() + "");
            mFunInfo12.setState(state12);

            //             仅支持关闭后备箱
            RemoteFunInfo mFunInfo14 = new RemoteFunInfo();
            mFunInfo14.setId("14");
            mFunInfo14.setApi_field("remoteTrunk");
            mFunInfo14.setName("关闭后备箱");
            mFunInfo14.setIcon_id(R.drawable.trunck);
            String state14 = mJSON_data.get("remoteTrunkOff").getAsInt() + "";
//            Logger.e("remoteTrunkOff----" + mJSON_data.get("remoteTrunkOff").getAsInt() + "");
            mFunInfo14.setState(state14);
            //            仅支持打开后备箱
            RemoteFunInfo mFunInfo15 = new RemoteFunInfo();
            mFunInfo15.setId("15");
            mFunInfo15.setApi_field("remoteTrunk");
            mFunInfo15.setName("打开后备箱");
            mFunInfo15.setIcon_id(R.drawable.trunck);
            String state15 = mJSON_data.get("remoteTrunkOn").getAsInt() + "";
//            Logger.e("remoteTrunkOn----" + mJSON_data.get("remoteTrunkOn").getAsInt() + "");
//            Logger.e("remoteTrunkOff----" + mJSON_data.get("remoteTrunkOff").getAsInt() + "");
            mFunInfo15.setState(state15);

            if (state12.equals(RemoteFunInfo.STATE_SUPPORT) && remoteTrunkOff.equals(RemoteFunInfo.STATE_SUPPORT)) {
                mRemoteMainInfo.addmRemoteFunInfos(mFunInfo12);
            } else if (state14.equals(RemoteFunInfo.STATE_SUPPORT)) {
                mRemoteMainInfo.addmRemoteFunInfos(mFunInfo14);
            } else if (state15.equals(RemoteFunInfo.STATE_SUPPORT)) {
                mRemoteMainInfo.addmRemoteFunInfos(mFunInfo15);
            }


            mFunInfo = new RemoteFunInfo();
            mFunInfo.setId("11");
            mFunInfo.setApi_field("SLCarLocating");
            mFunInfo.setName("一键寻车");
            mFunInfo.setIcon_id(R.drawable.horm);
            state = mJSON_data.get("SLCarLocating").getAsInt() + "";
            mFunInfo.setState(state);
            if (state.equals(RemoteFunInfo.STATE_SUPPORT)) {
                mRemoteMainInfo.addmRemoteFunInfos(mFunInfo);
            }


            LoginInfo.setRemoteMainInfo(mRemoteMainInfo);

            String remote_airconditioner_item = mJSON_data.get("remoteAirconditioner_item").getAsString();
            Logger.e("-----" + remote_airconditioner_item);
            if (!TextUtils.isEmpty(remote_airconditioner_item)) {
                String[] items = remote_airconditioner_item.split(",");
                int index;
                index = remote_airconditioner_item.indexOf("1");
                if (index != -1) {
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

                index = remote_airconditioner_item.indexOf("5");
                if (index != -1) {
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

                index = remote_airconditioner_item.indexOf("4");
                if (index != -1) {
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

                index = remote_airconditioner_item.indexOf("3");
                if (index != -1) {
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


                index = remote_airconditioner_item.indexOf("8");
                if (index != -1) {
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
                index = remote_airconditioner_item.indexOf("6");
                if (index != -1) {
                    mFunInfo = new RemoteFunInfo();
                    mFunInfo.setId(RemoteFunInfo.MODE_ANION);
//                    mFunInfo.setApi_field("close");
                    mFunInfo.setName("负离子");
                    mFunInfo.setIcon_id(R.drawable.remote_anion_selecter);
                    mFunInfo.setIcon_id_seleced(R.drawable.ic_anion_selected);
                    mFunInfo.setIcon_id_seleced_no(R.drawable.ic_anion_normal);
                    state = RemoteFunInfo.STATE_SUPPORT;
                    mFunInfo.setState(state);
                    mFunInfo.setTemperature("--");
                    if (state.equals(RemoteFunInfo.STATE_SUPPORT)) {
                        mAirMainInfo.addmRemoteFunInfos(mFunInfo);
                    }
                    supportCount++;
                }

                index = remote_airconditioner_item.indexOf("7");
                if (index != -1) {
                    mFunInfo = new RemoteFunInfo();
                    mFunInfo.setId(RemoteFunInfo.MODE_CLEAN);
//                    mFunInfo.setApi_field("close");
                    mFunInfo.setName("座舱清洁");
                    mFunInfo.setIcon_id(R.drawable.remote_anion_selecter);
                    mFunInfo.setIcon_id_seleced(R.drawable.ic_anion_selected);
                    mFunInfo.setIcon_id_seleced_no(R.drawable.ic_anion_normal);
                    state = RemoteFunInfo.STATE_SUPPORT;
                    mFunInfo.setState(state);
                    mFunInfo.setTemperature("--");
                    if (state.equals(RemoteFunInfo.STATE_SUPPORT)) {
                        mAirMainInfo.addmRemoteFunInfos(mFunInfo);
                    }
                    supportCount++;
                }

                index = remote_airconditioner_item.indexOf("2");
                if (index != -1) {
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

                index = remote_airconditioner_item.indexOf("9");
                if (index != -1) {
                    mAirMainInfo.setShowTemp(true);
                } else {
                    mAirMainInfo.setShowTemp(false);
                }

                mAirMainInfo.setFunctionCount(supportCount + "");

                mRemoteMainInfo.setmAirMainInfo(mAirMainInfo);
            }
        }

    }


}

package com.carlt.doride.http.retrofitnet.model;

import com.carlt.doride.data.remote.CarStateInfo;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class RemoteCarStateInfoPresenter {


    private RemoteCarStateInfo mRemoteCarStateInfo;

    public RemoteCarStateInfoPresenter(RemoteCarStateInfo carStateInfos) {
        mRemoteCarStateInfo = carStateInfos;
    }

    public ArrayList<CarStateInfo> parser() {
        ArrayList<CarStateInfo> mCarStateInfos = new ArrayList<>();
        Gson gson = new Gson();
        String s = gson.toJson(mRemoteCarStateInfo);
//        LogUtils.e(s);
        //        String data = mJson.get("data").toString();
        JSONObject mJSON_data = null;
        try {
            mJSON_data = new JSONObject(s);

            String[] names = CarStateInfo.names;
            int[] iconId_opens = CarStateInfo.iconId_opens;
            int[] iconId_closes = CarStateInfo.iconId_closes;
            int length = names.length;
            //			String[] apiAttrNames = { "doorLockStatus", "doorCloseStatus", "windowStatus", "engine", "AC" ,"skyWindowStatus"};
            String[] apiAttrNames = {"engine", "doorCloseStatus", "doorLockStatus", "windowStatus", "skyWindowStatus", "AC", "bonnet"};
            String[] stateClose = {"已熄火", "已关", "已锁", "已关", "已关", "关闭", "关闭"};
            String[] stateOpen = {"已启动", "未关", "未锁", "未关", "未关", "已开启", "打开"};

            //            String[] apiAttrNamesAfter = {"doorLockStatus", "doorCloseStatus", "engine"};
            //            String[] stateCloseAfter = {"已锁", "已关", "已熄火"};
            //            String[] stateOpenAfter = {"未锁", "未关", "已启动"};
            //            int lengthAfter = apiAttrNamesAfter.length;
            for (int i = 0; i < length; i++) {
                CarStateInfo mInfo = new CarStateInfo();
                mInfo.setName(names[i]);
                //                String state = mJSON_data.optString(apiAttrNames[i], "");
                long state = mJSON_data.optLong(apiAttrNames[i]);
                mInfo.status = state;
                //                long l = state >> 32;
                //                LogUtils.e(l);
                //				if (StringUtils.isEmpty(state)) {
                //					//不支持该项目
                //					continue;
                //				}
                //				if (state.equals("255")) {
                //					continue;
                //				}

                if (i == 2) {
                    if (state == 4294967295L) {
                        mInfo.setIconId(iconId_closes[i]);
                        mInfo.setStateDes(stateClose[i]);
                        mCarStateInfos.add(mInfo);
                    } else if (state == -1 || state == 255) {
                        mInfo.setIconId(iconId_closes[i]);
                        mInfo.setStateDes("--");

                    } else {
                        mInfo.setIconId(iconId_opens[i]);
                        mInfo.setStateDes(stateOpen[i]);
                        mCarStateInfos.add(mInfo);
                    }

                } else if (i == 4) {//,"开翘"
                    if (state == 4294967295L) {
                        mInfo.setIconId(iconId_closes[i]);
                        mInfo.setStateDes(stateClose[i]);
                        mCarStateInfos.add(mInfo);
                    } else if (state == -1 || state == 255) {
                        mInfo.setIconId(iconId_closes[i]);
                        mInfo.setStateDes("--");
                    } else if (state == 2) {
                        mInfo.setIconId(iconId_opens[i]);
                        mInfo.setStateDes("未关|开翘");
                        mCarStateInfos.add(mInfo);
                    } else {
                        mInfo.setIconId(iconId_opens[i]);
                        mInfo.setStateDes(stateOpen[i]);
                        mCarStateInfos.add(mInfo);
                    }

                } else if (i == 5) {
                    // 支持空调
                    if (state == 2) {
                        mInfo.setIconId(iconId_closes[i]);
                        mInfo.setStateDes(stateClose[i]);
                        mCarStateInfos.add(mInfo);
                    } else if (state == -1 || state == 255) {
                        mInfo.setIconId(iconId_closes[i]);
                        mInfo.setStateDes("--");
                    } else {
                        mInfo.setIconId(iconId_opens[i]);
                        String stateDes = "";
                        if (state == 1) {
                            stateDes = "全自动";
                        } else if (state == 3) {
                            stateDes = "一键除霜";
                        } else if (state == 4) {
                            stateDes = "最大制冷";
                        } else if (state == 5) {
                            stateDes = "最大制热";
                        } else if (state == 6) {
                            stateDes = "负离子";
                        } else if (state == 7) {
                            stateDes = "座舱清洁";
                        } else {
                            stateDes = "其他开启模式";
                        }

                        mInfo.setStateDes(stateOpen[i]);
                        mInfo.setStateDes(stateDes);
                        String temp = mJSON_data.optString("ACTemp");
                        if (temp != null && !temp.equals("")) {
                            double temp_double = Double.valueOf(temp);
                            int temp_int = (int) Math.rint(temp_double);
                            mInfo.setValue(temp_int + "℃");
                        } else {
                            mInfo.setValue("--℃");
                        }
                        mCarStateInfos.add(mInfo);
                    }

                } else if (i == 6) {
                    //  引擎盖
                    if (state == 4294967295L) {
                        mInfo.setIconId(iconId_closes[i]);
                        mInfo.setStateDes(stateClose[i]);
                        mCarStateInfos.add(mInfo);

                    } else if (state == -1 || state == 255) {
                        mInfo.setIconId(0);
                        mInfo.setStateDes("");
                    } else {
                        mInfo.setIconId(iconId_opens[i]);
                        mInfo.setStateDes(stateOpen[i]);
                        mCarStateInfos.add(mInfo);
                    }
                } else {
                    if (state == 4294967295L) {
                        mInfo.setIconId(iconId_closes[i]);
                        mInfo.setStateDes(stateClose[i]);
                        mCarStateInfos.add(mInfo);
                    } else if (state == -1 || state == 255) {
                        mInfo.setIconId(iconId_closes[i]);
                        mInfo.setStateDes("--");
                    } else {
                        mInfo.setIconId(iconId_opens[i]);
                        mInfo.setStateDes(stateOpen[i]);
                        mCarStateInfos.add(mInfo);
                    }
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //        mBaseResponseInfo.setValue(mCarStateInfos);
        return mCarStateInfos;
    }
}

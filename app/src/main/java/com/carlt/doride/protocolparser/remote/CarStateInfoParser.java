package com.carlt.doride.protocolparser.remote;

import com.carlt.doride.data.remote.CarStateInfo;
import com.carlt.doride.protocolparser.BaseParser;
import com.carlt.doride.utils.StringUtils;

import org.json.JSONObject;

import java.util.ArrayList;


public class CarStateInfoParser extends BaseParser {

    private ArrayList<CarStateInfo> mCarStateInfos = new ArrayList<CarStateInfo>();

    public CarStateInfoParser(ResultCallback callback) {
        super(callback);
        //TODO TEST DATA
        //		setTestFileName("json_carstate.txt") ;
        //		setTest(true);
    }

    @Override
    protected void parser() throws Exception {
        String data = mJson.get("data").toString();
        JSONObject mJSON_data = new JSONObject(data);
        if (mJSON_data != null) {
            String[] names = CarStateInfo.names;
            int[] iconId_opens = CarStateInfo.iconId_opens;
            int[] iconId_closes = CarStateInfo.iconId_closes;
            int length = names.length;
            //			String[] apiAttrNames = { "doorLockStatus", "doorCloseStatus", "windowStatus", "engine", "AC" ,"skyWindowStatus"};
            String[] apiAttrNames = {"engine", "doorCloseStatus", "doorLockStatus", "windowStatus", "skyWindowStatus", "AC"};
            String[] stateClose = {"已熄火", "已关", "已锁", "已关", "已关", "关闭"};
            String[] stateOpen = {"已启动", "未关", "未锁", "未关", "未关", "已开启"};

            String[] apiAttrNamesAfter = {"doorLockStatus", "doorCloseStatus",
                    "engine"};
            String[] stateCloseAfter = {"已锁", "已关", "已熄火"};
            String[] stateOpenAfter = {"未锁", "未关", "已启动"};
            int lengthAfter = apiAttrNamesAfter.length;
            for (int i = 0; i < length; i++) {
                CarStateInfo mInfo = new CarStateInfo();
                mInfo.setName(names[i]);
                String state = mJSON_data.optString(apiAttrNames[i], "");
                mInfo.setState(state);
                //				if (StringUtils.isEmpty(state)) {
                //					//不支持该项目
                //					continue;
                //				}
                //				if (state.equals("255")) {
                //					continue;
                //				}
                if (i == 2) {
                    if (state.equals("0")) {
                        mInfo.setIconId(iconId_closes[i]);
                        mInfo.setStateDes(stateClose[i]);
                    } else if (StringUtils.isEmpty(state) || state.equals("255")) {
                        mInfo.setIconId(iconId_closes[i]);
                        mInfo.setStateDes("--");

                    } else {
                        mInfo.setIconId(iconId_opens[i]);
                        mInfo.setStateDes(stateOpen[i]);
                    }
                } else if (i == 4) {//,"开翘"
                    if (state.equals("0")) {
                        mInfo.setIconId(iconId_closes[i]);
                        mInfo.setStateDes(stateClose[i]);

                    } else if (StringUtils.isEmpty(state) || state.equals("255")) {
                        mInfo.setIconId(iconId_closes[i]);
                        mInfo.setStateDes("--");
                    } else if (state.equals("2")) {
                        mInfo.setIconId(iconId_opens[i]);
                        mInfo.setStateDes("未关|开翘");
                    } else {
                        mInfo.setIconId(iconId_opens[i]);
                        mInfo.setStateDes(stateOpen[i]);
                    }
                } else if (i == 5) {
                    // 支持空调
                    if (state.equals("2")) {
                        mInfo.setIconId(iconId_closes[i]);
                        mInfo.setStateDes(stateClose[i]);

                    } else if (StringUtils.isEmpty(state) || state.equals("255")) {
                        mInfo.setIconId(iconId_closes[i]);
                        mInfo.setStateDes("--");
                    } else {
                        mInfo.setIconId(iconId_opens[i]);
                        String stateDes = "已开启";
                        if (state.equals("1")) {
                            stateDes = "全自动";
                        } else if (state.equals("3")) {
                            stateDes = "一键除霜";
                        } else if (state.equals("4")) {
                            stateDes = "最大制冷";
                        } else if (state.equals("5")) {
                            stateDes = "最大制热";
                        } else if (state.equals("6")) {
                            stateDes = "负离子";
                        } else if (state.equals("7")) {
                            stateDes = "座舱清洁";
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

                    }
                } else {
                    if (state.equals("0")) {
                        mInfo.setIconId(iconId_closes[i]);
                        mInfo.setStateDes(stateClose[i]);

                    } else if (StringUtils.isEmpty(state) || state.equals("255")) {
                        mInfo.setIconId(iconId_closes[i]);
                        mInfo.setStateDes("--");
                    } else {
                        mInfo.setIconId(iconId_opens[i]);
                        mInfo.setStateDes(stateOpen[i]);
                    }
                }
                mCarStateInfos.add(mInfo);
            }
        }

        mBaseResponseInfo.setValue(mCarStateInfos);
    }
}

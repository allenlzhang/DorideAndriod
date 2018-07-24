
package com.carlt.sesame.protocolstack.community;

import com.carlt.sesame.data.community.AppointmentInfo;
import com.carlt.sesame.data.community.MyAppointmentListInfo;
import com.carlt.sesame.protocolstack.BaseParser;
import com.carlt.sesame.utility.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class MyOrderListParser extends BaseParser {

    private MyAppointmentListInfo mMyAppointmentListInfo = new MyAppointmentListInfo();

    public MyAppointmentListInfo getReturn() {
        return mMyAppointmentListInfo;
    }

    @Override
    protected void parser() {
        try {

            JSONArray mJSON_data = mJson.getJSONArray("data");
            Date now = new Date(System.currentTimeMillis());
            long currentTimeMillis = now.getTime();
            long ONEDAY = 1000 * 60 * 60 * 24;
            for (int i = 0; i < mJSON_data.length(); i++) {
                JSONObject temp = (JSONObject)mJSON_data.get(i);
                AppointmentInfo mAppointmentInfo = new AppointmentInfo();
                mAppointmentInfo.setId(temp.optString("id"));
                mAppointmentInfo.setDes(temp.optString("info"));
                mAppointmentInfo.setSpent(temp.optString("price"));

                mAppointmentInfo.setTyep(temp.optInt("type"));
                mAppointmentInfo.setTime(temp.optString("arrivetime"));
                String arrivedate = temp.optString("arrivedate");
                mAppointmentInfo.setDate(arrivedate);

                int status = temp.optInt("status");
                int evaluate_star = temp.optInt("evaluate_star", 0);
                switch (status) {
                    case 1:
                        mAppointmentInfo.setStatus(AppointmentInfo.STATUS_UNACCEPTANCE);
                        break;
                    case 2:
                        mAppointmentInfo.setStatus(AppointmentInfo.STATUS_ACCEPTANCEED);
                        if (arrivedate != null & !arrivedate.equals("")) {
                            String[] array_arrivedate = arrivedate.split("-");
                            if (array_arrivedate.length > 2) {
                                Date Instance = new Date();
                                Instance.setYear(Integer.parseInt(array_arrivedate[0]) - 1900);
                                Instance.setMonth(Integer.parseInt(array_arrivedate[1]) - 1);
                                Instance.setDate(Integer.parseInt(array_arrivedate[2]));
                                long t = Instance.getTime();
                                long offset = t - currentTimeMillis;
                                String TimeDes = "";
                                if (offset < 0) {
                                    TimeDes = "已过期";
                                } else {
                                    int d = (int)(offset / ONEDAY);
                                    switch (d) {
                                        case 0:
                                            TimeDes = "今天";
                                            break;
                                        case 1:
                                            TimeDes = "明天";
                                            break;
                                        case 2:
                                            TimeDes = "后天";
                                            break;
                                        default:
                                            TimeDes = d + "天后";
                                            break;
                                    }
                                }
                                mAppointmentInfo.setTimeDes(TimeDes);
                            }

                        }
                        if (evaluate_star == 0) {
                            mAppointmentInfo.setStatus(AppointmentInfo.STATUS_UNEVALUATION);
                            mAppointmentInfo.setTimeDes("");
                        } else if (evaluate_star > 0) {
                            mAppointmentInfo.setStatus(AppointmentInfo.STATUS_FINISHED);
                            mAppointmentInfo.setTimeDes("");
                        }
                        break;
                    case 3:
                        mAppointmentInfo.setStatus(AppointmentInfo.STATUS_ERRO);
                        break;
                }

                mAppointmentInfo.setStatus_show(temp.optString("status_show"));
                mAppointmentInfo.setStar(evaluate_star);
                mMyAppointmentListInfo.addmAppointmentInfoList(mAppointmentInfo);
                Log.e("info", "==" + mAppointmentInfo.getTimeDes());
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}

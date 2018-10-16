
package com.carlt.sesame.protocolstack.community;

import com.carlt.sesame.data.community.AppointmentDetialInfo;
import com.carlt.sesame.data.community.AppointmentInfo;
import com.carlt.sesame.protocolstack.BaseParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class MyOrderDetialParser extends BaseParser {

    private AppointmentDetialInfo mAppointmentDetialInfo = new AppointmentDetialInfo();

    public AppointmentDetialInfo getReturn() {
        return mAppointmentDetialInfo;
    }

    @Override
    protected void parser() {
        try {
            Date now = new Date(System.currentTimeMillis());
            long currentTimeMillis = now.getTime();
            long ONEDAY = 1000 * 60 * 60 * 24;
            JSONObject mJSON_data = mJson.getJSONObject("data");
            mAppointmentDetialInfo.setId(mJSON_data.optString("id"));
            mAppointmentDetialInfo.setDes(mJSON_data.optString("info"));
            mAppointmentDetialInfo.setSpent(mJSON_data.optString("price"));

            mAppointmentDetialInfo.setTyep(mJSON_data.optInt("type"));

            mAppointmentDetialInfo.setConsultantName(mJSON_data.optString("adviser_name"));
            mAppointmentDetialInfo.setConsultantPhone(mJSON_data.optString("adviser_phone"));
            mAppointmentDetialInfo.setAddress_4s(mJSON_data.optString("dealer_address"));
            mAppointmentDetialInfo.setName_4s(mJSON_data.optString("dealer_name"));
            mAppointmentDetialInfo.setEvaluation(mJSON_data.optString("evaluate_content"));
            mAppointmentDetialInfo.setTime(mJSON_data.optString("arrivetime"));
            String arrivedate = mJSON_data.optString("arrivedate");
            mAppointmentDetialInfo.setDate(arrivedate);

            int status = mJSON_data.optInt("status");
            int evaluate_star = mJSON_data.optInt("evaluate_star", 0);
            switch (status) {
                case 1:
                    mAppointmentDetialInfo.setStatus(AppointmentInfo.STATUS_UNACCEPTANCE);
                    break;
                case 2:
                    mAppointmentDetialInfo.setStatus(AppointmentInfo.STATUS_ACCEPTANCEED);
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
                            mAppointmentDetialInfo.setTimeDes(TimeDes);

                        }

                    }
                    if (evaluate_star == 0) {
                        mAppointmentDetialInfo.setStatus(AppointmentInfo.STATUS_UNEVALUATION);
                        mAppointmentDetialInfo.setTimeDes("");
                    } else {
                        mAppointmentDetialInfo.setStatus(AppointmentInfo.STATUS_FINISHED);
                        mAppointmentDetialInfo.setTimeDes("");
                    }
                    break;
                case 3:
                    mAppointmentDetialInfo.setStatus(AppointmentInfo.STATUS_ERRO);
                    break;
            }
            mAppointmentDetialInfo.setStar(evaluate_star);
            mAppointmentDetialInfo.setStatus_show(mJSON_data.optString("status_show"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


}

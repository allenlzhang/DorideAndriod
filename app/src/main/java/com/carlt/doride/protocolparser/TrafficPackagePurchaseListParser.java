package com.carlt.doride.protocolparser;


import com.carlt.doride.data.flow.TrafficPackagePurchaseLogInfo;
import com.carlt.doride.data.flow.TrafficPackagePurchaseLogListInfo;
import com.carlt.sesame.protocolstack.BaseParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;

/**
 * 流量充值日志解析
 *
 * @author Administrator
 */
public class TrafficPackagePurchaseListParser extends BaseParser {

    private TrafficPackagePurchaseLogListInfo mTrafficPackagePurchaseLogListInfo = new TrafficPackagePurchaseLogListInfo();


    public TrafficPackagePurchaseLogListInfo getReturn() {
        return mTrafficPackagePurchaseLogListInfo;
    }

    @Override
    protected void parser() {
        try {
            JSONObject mJSON_data = mJson.getJSONObject("data");
            JSONObject list = mJSON_data.getJSONObject("list");
            getBuyTimeList(list);
            int i = mJSON_data.optInt("has_next");
            boolean hasMore = true;
            if (i == -1) {
                hasMore = false;
            } else if (i == 1) {
                hasMore = true;
            }
            mTrafficPackagePurchaseLogListInfo.setHas_next(hasMore);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void getBuyTimeList(JSONObject list) {
        Iterator<String> iterator = list.keys();
        ArrayList<String> months = new ArrayList<String>();
        while (iterator.hasNext()) {
            months.add(iterator.next());
        }
        Collections.sort(months, new Comparator<String>() {
            @Override
            public int compare(String lhs, String rhs) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM");
                Date date1 = null, date2 = null;
                try {
                    date1 = simpleDateFormat.parse(lhs);
                    date2 = simpleDateFormat.parse(rhs);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return date2.compareTo(date1);
            }
        });
        for (int i = 0; i < months.size(); i++) {
            try {
                JSONArray array = list.getJSONArray(months.get(i));
                TrafficPackagePurchaseLogInfo mlogInfo = new TrafficPackagePurchaseLogInfo();
                mlogInfo.setBuy_time(months.get(i));
                mTrafficPackagePurchaseLogListInfo.addTrafficPackagePurchaseLogInfo(mlogInfo);
                for (int j = 0; j < array.length(); j++) {
                    JSONObject item = array.getJSONObject(j);
                    TrafficPackagePurchaseLogInfo logInfo = new TrafficPackagePurchaseLogInfo();
                    logInfo.setAddtime(item.getString("addtime"));
                    logInfo.setPackage_cost(item.getString("package_cost"));
                    logInfo.setPackage_name(item.getString("package_name"));
                    logInfo.service_data_start=item.getString("service_data_start");
                    logInfo.service_data_end=item.getString("service_data_end");
                    logInfo.package_month=item.getString("package_month");
                    mTrafficPackagePurchaseLogListInfo.addTrafficPackagePurchaseLogInfo(logInfo);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }


}

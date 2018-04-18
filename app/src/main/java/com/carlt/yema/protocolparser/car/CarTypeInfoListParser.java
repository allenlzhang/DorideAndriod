package com.carlt.yema.protocolparser.car;

import com.carlt.yema.data.car.CarModeInfo;
import com.carlt.yema.protocolparser.BaseParser;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by marller on 2018\3\30 0030.
 */

public class CarTypeInfoListParser extends BaseParser{

    ArrayList<CarModeInfo> mCarModeInfos=new ArrayList<>();

    public CarTypeInfoListParser(ResultCallback callback) {
        super(callback);
    }

    @Override
    protected void parser() throws Exception {
        JsonObject list=mJson.get("data").getAsJsonObject();
        getCarTypeList(list);
        mBaseResponseInfo.setValue(mCarModeInfos);
    }

    public void getCarTypeList(JsonObject list) {
        Set<String> set = list.keySet();
        ArrayList<String> months = new ArrayList<String>();
        Iterator<String> iterator=set.iterator();
        while (iterator.hasNext()) {
            months.add(iterator.next());
        }
        Collections.sort(months, new Comparator<String>() {
            @Override
            public int compare(String lhs, String rhs) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy" + "");
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
                JsonArray array = list.getAsJsonArray(months.get(i));
                CarModeInfo mModeInfo = new CarModeInfo();
                mModeInfo.setFactory_year(months.get(i));
                mCarModeInfos.add(mModeInfo);
                for (int j = 0; j < array.size(); j++) {
                    JsonObject item = array.get(j).getAsJsonObject();
                    CarModeInfo modeInfo = new CarModeInfo();
                    modeInfo.setId(item.get("id").getAsString());
                    modeInfo.setTitle(item.get("title").getAsString());
                    mCarModeInfos.add(modeInfo);
                }
        }
    }
}

package com.carlt.doride.protocolparser.home;

import com.carlt.doride.data.BaseResponseInfo;
import com.carlt.doride.data.home.MilesInfo;
import com.carlt.doride.protocolparser.BaseParser;
import com.carlt.doride.utils.ILog;
import com.google.gson.JsonObject;

/**
 * Created by Marlon on 2018/3/27.
 */

public class MilesInfoParser extends BaseParser<MilesInfo> {



    public MilesInfoParser(ResultCallback callback) {
        super(callback);
    }

    @Override
    protected void parser(){
        try{
            JsonObject mJSON_data = mJson.getAsJsonObject("data");
            MilesInfo info = new MilesInfo();
            info.setObd(mJSON_data.get("obd").getAsDouble());
            info.setEnduranceMile(mJSON_data.get("enduranceMile").getAsDouble());
            info.setAvgSpeed(mJSON_data.get("avgSpeed").getAsDouble());
            info.setAvgFuel(mJSON_data.get("avgFuel").getAsDouble());
            mBaseResponseInfo.setValue(info);
        }catch (Exception e){
            ILog.e(TAG, "--e==" + e);
            mBaseResponseInfo.setFlag(BaseResponseInfo.ERRO);
            mBaseResponseInfo.setInfo(MSG_ERRO);
        }
    }
}

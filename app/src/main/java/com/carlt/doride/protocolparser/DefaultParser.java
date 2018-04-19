package com.carlt.doride.protocolparser;

import com.google.gson.Gson;
import com.google.gson.JsonObject;


public class DefaultParser<T> extends BaseParser<T> {

    private static JsonObject mJSON_data;

    public DefaultParser(ResultCallback callback, Class clazz) {
        super(callback, clazz);
    }

    @Override
    protected void parser() throws Exception {
            mJSON_data = mJson.get("data").getAsJsonObject();
            T t = new Gson().fromJson(mJSON_data, clazz);
            mBaseResponseInfo.setValue(t);

    }

}

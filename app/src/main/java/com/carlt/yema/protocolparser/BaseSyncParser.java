package com.carlt.yema.protocolparser;

import android.text.TextUtils;

import com.carlt.yema.data.BaseResponseInfo;
import com.carlt.yema.http.HttpLinker;
import com.google.gson.Gson;
import com.google.gson.JsonParser;

import java.lang.reflect.Type;
import java.util.HashMap;

import okhttp3.Response;


public class BaseSyncParser extends BaseParser {
    public Response mResponse;
    private Type mType;

    public BaseSyncParser() {
        this(null);
    }

    private BaseSyncParser(ResultCallback callback) {
        super(callback);
    }

    @Override
    protected void parser() throws Exception {
        String json = mResponse.body().string();
        JsonParser parser = new JsonParser();
        mJson = parser.parse(json).getAsJsonObject();
        if(null != mType){
            Gson gson = new Gson();
            mBaseResponseInfo = gson.fromJson(mJson, mType);
        }else {
            Gson gson = new Gson();
            mBaseResponseInfo = gson.fromJson(mJson, BaseResponseInfo.class);
        }
    }


    @Override
    public void executePost(String url, HashMap params) {
        try {
            mResponse = HttpLinker.postSync(url, params);
            if (mResponse.isSuccessful()) {
                parser();
            } else {
                mBaseResponseInfo.setFlag(mResponse.code());
            }
        } catch (Exception ex) {
//            ILog.e("http", ex.getMessage());
            mBaseResponseInfo.setFlag(BaseResponseInfo.ERRO);
        }

        if (!mBaseResponseInfo.isSuccessful()) {
            if (TextUtils.isEmpty(mBaseResponseInfo.getInfo())) {
                mBaseResponseInfo.setInfo(MSG_ERRO);
            }
        }
    }

    @Override
    public void executeGet(String url, HashMap params) {
        try {
            mResponse = HttpLinker.getSync(url, params);
            if (mResponse.isSuccessful()) {
                parser();
            } else {
                mBaseResponseInfo.setFlag(mResponse.code());
            }
        } catch (Exception ex) {
//            ILog.e("http", ex.getMessage());
            mBaseResponseInfo.setFlag(BaseResponseInfo.ERRO);
        }

        if (!mBaseResponseInfo.isSuccessful()) {
            if (TextUtils.isEmpty(mBaseResponseInfo.getInfo())) {
                mBaseResponseInfo.setInfo(MSG_ERRO);
            }
        }
    }

    public void setType(Type type){
        if (type == null) {
            throw new NullPointerException();
        }
        mType = type;
    }

    public <T> T getResult() {
        return (T) mBaseResponseInfo;
    }

}

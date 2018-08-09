package com.carlt.doride.protocolparser;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.carlt.doride.DorideApplication;
import com.carlt.doride.control.ActivityControl;
import com.carlt.doride.data.BaseResponseInfo;
import com.carlt.doride.http.HttpLinker;
import com.carlt.doride.ui.activity.login.UpDateActivity;
import com.carlt.doride.ui.view.UUToast;
import com.carlt.doride.utils.FileUtil;
import com.carlt.doride.utils.ILog;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public abstract class BaseParser<T> {
    public final static String MSG_ERRO     = "网络太差，连接失败了";
    public final static String MSG_ERRO_TIP = "数据获取失败，请稍后再试";
    public static       String TAG          = "http";
    protected BaseResponseInfo<T> mBaseResponseInfo;
    protected Class<T>            clazz;
    protected JsonObject          mJson;
    private   ResultCallback      mResultCallback;
    protected boolean isTest = false;// 是否是测试数据
    protected String  testFileName;// 本地模拟数据名称
    public    Handler mHandler;

    private static final String LOGIN_ACTION = "com.carlt.doride.LOGIN";

    public BaseParser(ResultCallback callback) {
        //TAG = this.getClass().getName();
        mBaseResponseInfo = new BaseResponseInfo();
        this.mResultCallback = callback;
        if (this.mResultCallback != null) {
            initHandler();
        }
    }

    public BaseParser(ResultCallback callback, Class<T> clazz) {
        this.clazz = clazz;
        mBaseResponseInfo = new BaseResponseInfo();
        this.mResultCallback = callback;
        if (this.mResultCallback != null) {
            initHandler();
        }
    }

    public void setTestFileName(String testFileName) {
        this.testFileName = testFileName;
    }

    public void setTest(boolean test) {
        isTest = test;
    }

    @SuppressLint("HandlerLeak")
    public void initHandler() {
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 0:
                        if (mResultCallback != null) {
                            mResultCallback.onSuccess(mBaseResponseInfo);
                        }
                        break;
                    case 1:
                        if (mResultCallback != null) {

                            mResultCallback.onError(mBaseResponseInfo);
                            if( mBaseResponseInfo.getFlag() == 1020){

                                Intent intent =new Intent(DorideApplication.ApplicationContext, UpDateActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                DorideApplication.ApplicationContext.startActivity(intent);
                            }
                        }
                        break;
                    case 2:
                        ActivityControl.onTokenDisable();

                        UUToast.showUUToast(DorideApplication.getInstanse(), mBaseResponseInfo.getInfo());
                        break;
                    default:
                        break;
                }
            }
        };
    }

    public void executePost(String url, HashMap params) {
        if (isTest) {
            InputStream in = null;
            try {

                in = DorideApplication.getInstanse().getAssets().open(
                        testFileName);
                String str = FileUtil.ToString(in);

                JsonParser jp = new JsonParser();
                JsonElement element = jp.parse(str);
                mJson = element.getAsJsonObject();
                int flag = mJson.get("code").getAsInt();
                String info = mJson.get("msg").getAsString();
                mBaseResponseInfo.setFlag(flag);
                mBaseResponseInfo.setInfo(info);
                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
                    parser();
                    mHandler.sendEmptyMessage(0);
                } else if (mBaseResponseInfo.getFlag() == BaseResponseInfo.NO_TOKEN) {
                    mHandler.sendEmptyMessage(2);
                } else {
                    mHandler.sendEmptyMessage(1);
                }
            } catch (Exception ex) {
                mBaseResponseInfo.setFlag(BaseResponseInfo.ERRO);
                mBaseResponseInfo.setInfo(MSG_ERRO);
                if (null != mHandler) {
                    mHandler.sendEmptyMessage(1);
                }
                ILog.e(TAG, ex.getMessage() + "");
                ex.printStackTrace();
            }
        } else {
            HttpLinker.post(url, params, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Message msg = new Message();
                    ILog.e(TAG, "onFailure==" + e.getMessage());
                    mBaseResponseInfo.setFlag(BaseResponseInfo.ERRO);
                    mBaseResponseInfo.setInfo(MSG_ERRO);
                    mHandler.sendEmptyMessage(1);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String str = response.body().string();
                        try {
                            JsonParser jp = new JsonParser();
                            JsonElement element = jp.parse(str);
                            mJson = element.getAsJsonObject();
                            int flag = mJson.get("code").getAsInt();
                            String info = mJson.get("msg").getAsString();
                            mBaseResponseInfo.setFlag(flag);
                            mBaseResponseInfo.setInfo(info);
                            if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
                                parser();
                                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.ERRO) {
                                    mHandler.sendEmptyMessage(1);
                                    ILog.e(TAG, "请求失败：" + str);
                                } else if (mBaseResponseInfo.getFlag() == BaseResponseInfo.NO_TOKEN) {
                                    mHandler.sendEmptyMessage(2);
                                } else {
                                    mHandler.sendEmptyMessage(0);
                                }
                            } else if (mBaseResponseInfo.getFlag() == BaseResponseInfo.NO_TOKEN) {
                                mHandler.sendEmptyMessage(2);
                            } else {
                                mHandler.sendEmptyMessage(1);
                                ILog.e(TAG, "请求失败：" + str);
                            }
                        } catch (Exception ex) {
                            mBaseResponseInfo.setFlag(BaseResponseInfo.ERRO);
                            mBaseResponseInfo.setInfo(MSG_ERRO);
                            mHandler.sendEmptyMessage(1);
                            ILog.e(TAG, ex.getMessage() + "");
                            ex.printStackTrace();
                        }
                    } else {
                        mBaseResponseInfo.setFlag(BaseResponseInfo.ERRO);
                        mBaseResponseInfo.setInfo(response.code() + MSG_ERRO);
                        mHandler.sendEmptyMessage(1);
                    }
                }
            });
        }
    }


    public void executeGet(String url, HashMap params) {
        if (isTest) {
            InputStream in = null;
            try {
                in = DorideApplication.getInstanse().getAssets().open(
                        testFileName);
                String str = FileUtil.ToString(in);
                JsonParser jp = new JsonParser();
                JsonElement element = jp.parse(str);
                mJson = element.getAsJsonObject();
                int flag = mJson.get("code").getAsInt();
                String info = mJson.get("msg").getAsString();
                mBaseResponseInfo.setFlag(flag);
                mBaseResponseInfo.setInfo(info);
                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
                    parser();
                    mHandler.sendEmptyMessage(0);
                } else if (mBaseResponseInfo.getFlag() == BaseResponseInfo.NO_TOKEN) {
                    mHandler.sendEmptyMessage(2);
                } else {
                    mHandler.sendEmptyMessage(1);
                }
                ILog.e(TAG, "Http本地--" + str);
            } catch (Exception ex) {
                mBaseResponseInfo.setFlag(BaseResponseInfo.ERRO);
                mBaseResponseInfo.setInfo(MSG_ERRO);
                mHandler.sendEmptyMessage(1);
                ILog.e(TAG, ex.getMessage() + "");
                ex.printStackTrace();
            }
        } else {
            HttpLinker.get(url, params, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Message msg = new Message();
                    ILog.e(TAG, "onFailure==" + e.getMessage());
                    mBaseResponseInfo.setFlag(BaseResponseInfo.ERRO);
                    mBaseResponseInfo.setInfo(MSG_ERRO);
                    mHandler.sendEmptyMessage(1);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String str = response.body().string();
                        try {
                            JsonParser jp = new JsonParser();
                            JsonElement element = jp.parse(str);
                            mJson = element.getAsJsonObject();
                            int flag = mJson.get("code").getAsInt();
                            String info = mJson.get("msg").getAsString();
                            mBaseResponseInfo.setFlag(flag);
                            mBaseResponseInfo.setInfo(info);
                            if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
                                parser();
                                mHandler.sendEmptyMessage(0);
                            } else if (mBaseResponseInfo.getFlag() == BaseResponseInfo.NO_TOKEN) {
                                mHandler.sendEmptyMessage(2);
                            }
                        } catch (Exception ex) {
                            mBaseResponseInfo.setFlag(BaseResponseInfo.ERRO);
                            mBaseResponseInfo.setInfo(MSG_ERRO);
                            mHandler.sendEmptyMessage(1);
                            ex.printStackTrace();
                        }
                    } else {
                        mBaseResponseInfo.setFlag(BaseResponseInfo.ERRO);
                        mBaseResponseInfo.setInfo(response.code() + MSG_ERRO);
                        mHandler.sendEmptyMessage(1);
                    }
                }
            });
        }
    }

    // 解析
    protected abstract void parser() throws Exception;

    public interface ResultCallback {
        void onSuccess(BaseResponseInfo bInfo);

        void onError(BaseResponseInfo bInfo);
    }

}

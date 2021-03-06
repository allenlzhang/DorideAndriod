package com.carlt.doride.push;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.blankj.utilcode.util.LogUtils;
import com.carlt.doride.DorideApplication;
import com.carlt.doride.R;
import com.carlt.doride.control.CPControl;
import com.carlt.doride.data.BaseResponseInfo;
import com.carlt.doride.data.car.SecretaryMessageInfo;
import com.carlt.doride.protocolparser.BaseParser;
import com.carlt.doride.ui.fragment.CarMainFragment;
import com.carlt.doride.utils.MyParse;
import com.carlt.sesame.preference.TokenInfo;
import com.orhanobut.logger.Logger;
import com.tencent.android.tpush.XGPushBaseReceiver;
import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushRegisterResult;
import com.tencent.android.tpush.XGPushShowedResult;
import com.tencent.android.tpush.XGPushTextMessage;

import org.json.JSONException;
import org.json.JSONObject;

import static android.content.Context.NOTIFICATION_SERVICE;

public class MessageReceiver extends XGPushBaseReceiver {

    // 通知展示
    @Override
    public void onNotifactionShowedResult(Context context, XGPushShowedResult notifiShowedRlt) {

        Logger.e("push" + "推送1");

        if (context == null || notifiShowedRlt == null) {
            return;
        }
        // context.sendBroadcast(intent);
    }

    @Override
    public void onUnregisterResult(Context context, int errorCode) {
        Logger.e("push" + "推送2");

    }

    @Override
    public void onSetTagResult(Context context, int errorCode, String tagName) {
        Logger.e("push" + "推送3");
        if (context == null) {
            return;
        }
        String text = null;
        if (errorCode == XGPushBaseReceiver.SUCCESS) {
            text = "\"" + tagName + "\"设置成功";
        } else {
            text = "\"" + tagName + "\"设置失败,错误码：" + errorCode;
        }

    }

    @Override
    public void onDeleteTagResult(Context context, int errorCode, String tagName) {
        Logger.e("push" + "推送4");
        if (context == null) {
            return;
        }
        String text = null;
        if (errorCode == XGPushBaseReceiver.SUCCESS) {
            text = "\"" + tagName + "\"删除成功";
        } else {
            text = "\"" + tagName + "\"删除失败,错误码：" + errorCode;
        }

    }

    // 通知点击回调 actionType=1为该消息被清除，actionType=0为该消息被点击
    @Override
    public void onNotifactionClickedResult(Context context, XGPushClickedResult message) {
        Logger.e("push" + "推送5");
        if (context == null || message == null) {
            return;
        }
        if (message.getActionType() == XGPushClickedResult.NOTIFACTION_CLICKED_TYPE) {
            // 通知在通知栏被点击啦。。。。。
            // APP自己处理点击的相关动作
            // 这个动作可以在activity的onResume也能监听，请看第3点相关内容
            // "通知被打开 :"
        } else if (message.getActionType() == XGPushClickedResult.NOTIFACTION_DELETED_TYPE) {
            // 通知被清除啦。。。。
            // APP自己处理通知被清除后的相关动作
            // "通知被清除 :"
        }
        // 获取自定义key-value
        String customContent = message.getCustomContent();
        Logger.e("push" + "customContent_click==" + customContent);
        String content = message.getContent().replaceAll("&quot;", "");
        Logger.e("push" + "content_click==" + content);
        String title = message.getTitle();
        Logger.e("push" + "titl_clicke==" + title);
        int class1 = 0;
        String class2 = "";
        String reportDate = "";
        if (customContent != null && customContent.length() != 0) {
            try {
                JSONObject obj = new JSONObject(customContent);
                // key1为前台配置的key
                if (!obj.isNull("key")) {
                    class1 = MyParse.parseInt(obj.optString("class1"));
                    class2 = obj.optString("class2");
                    reportDate = obj.optString("reportdate");
                    Logger.e("push" + "class1==" + class1);
                    Logger.e("push" + "class2==" + class2);
                    Logger.e("push" + "reportDate==" + reportDate);
                }
            } catch (JSONException e) {
                Logger.e("info" + "onNotifactionClickedResult--e==" + e);
            }
        }
    }

    boolean isRegister = false;

    @Override
    public void onRegisterResult(Context context, int errorCode, XGPushRegisterResult message) {

        Logger.e("push" + "推送注册-errorCode==" + errorCode);
        String xgtoken = message.getToken();
        Logger.e("push" + "推送注册-token-111111==" + xgtoken);
        LogUtils.e("token===>" + TokenInfo.getToken());
        CPControl.GetPushXgTokenResult(xgtoken, DorideApplication.NIMEI, listener);
    }

    private BaseParser.ResultCallback listener = new BaseParser.ResultCallback() {
        @Override
        public void onSuccess(BaseResponseInfo bInfo) {
            Logger.e("push" + "信鸽推送token保存至后台成功!");
        }

        @Override
        public void onError(BaseResponseInfo bInfo) {
            Logger.e("push" + "信鸽推送token保存至后台失败...");
        }
    };

    // 消息透传
    @Override
    public void onTextMessage(Context context, XGPushTextMessage message) {
        Logger.e("push" + "推送7");

        String content = message.getContent().replaceAll("&quot;", "");
        String title = message.getTitle();
        Logger.e("push" + "title==" + title);
        Logger.e("push" + "content==" + content);
        int class1 = 0;
        String class2 = "";
        String reportDate = "";
        if (content != null && content.length() != 0) {
            try {
                JSONObject obj = new JSONObject(content);
                // key1为前台配置的key
                class1 = MyParse.parseInt(obj.optString("class1"));
                class2 = obj.optString("class2");
                reportDate = obj.optString("reportdate");
                Logger.e("push" + "class1==" + class1);
                Logger.e("push" + "class2==" + class2);
                Logger.e("push" + "reportDate==" + reportDate);
            } catch (JSONException e) {
                Logger.e("push" + "onTextMessage--e==" + e);
            }
        }
        Intent i = null;
        switch (class1) {
            case SecretaryMessageInfo.C1_T2:
                // // 21 安防故障
                if (TokenInfo.getToken() != null && TokenInfo.getToken().length() > 0) {
                    Intent intent2 = new Intent();
                    intent2.setAction(CarMainFragment.CARMAIN_SAFETY);
                    context.sendBroadcast(intent2);
                }
                showNotification(context, title, class1);
                break;
            case PushService.Feetip_Message:
                // 续费推送（只有主机才能展示）
                showNotification(context, title, class1);
                break;
            default:
                showNotification(context, title, class1);
                break;
        }

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void showNotification(Context context, String title, int type) {
        String id = "1";
        String name = "1";
        Intent intent = new Intent(context, PushService.class);
        intent.putExtra(PushService.CLASS1, type);
        // intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
        // | Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent contentIntent = PendingIntent.getService(context, R.string.app_name + (int) System.currentTimeMillis(), intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationManager nm = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        //        Notification n = new Notification(R.drawable.ic_launcher, title, System.currentTimeMillis());
        //        n.flags = Notification.FLAG_AUTO_CANCEL;
        //        n.defaults = Notification.DEFAULT_SOUND;
        //        n.audioStreamType = android.media.AudioManager.ADJUST_LOWER;
        //        n.setLatestEventInfo(context, context.getResources().getString(R.string.app_name), title,
        //                contentIntent);

        // 适配8.0 通知
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(id, name, NotificationManager.IMPORTANCE_LOW);

            nm.createNotificationChannel(mChannel);
            Notification n2 = new Notification.Builder(context)
                    .setDefaults(Notification.DEFAULT_SOUND)
                    .setContentTitle(context.getResources().getString(R.string.app_name))
                    .setContentText(title)
                    .setChannelId(id)
                    .setContentIntent(contentIntent)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setWhen(System.currentTimeMillis())
                    .build();
            n2.flags = Notification.FLAG_AUTO_CANCEL;
            n2.audioStreamType = android.media.AudioManager.ADJUST_LOWER;
            nm.notify((int) System.currentTimeMillis(), n2);


        } else {
            Notification n = new Notification.Builder(context)
                    .setDefaults(Notification.DEFAULT_SOUND)
                    .setContentTitle(context.getResources().getString(R.string.app_name))
                    .setContentText(title)
                    .setContentIntent(contentIntent)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setWhen(System.currentTimeMillis())
                    .build();
            n.flags = Notification.FLAG_AUTO_CANCEL;
            n.audioStreamType = android.media.AudioManager.ADJUST_LOWER;

            // 每次通知完，通知ID需要变更，避免消息覆盖掉
            nm.notify((int) System.currentTimeMillis(), n);
        }


    }

}


package com.carlt.sesame.push;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.carlt.doride.DorideApplication;
import com.carlt.doride.R;
import com.carlt.sesame.control.CPControl;
import com.carlt.sesame.control.CPControl.GetResultListCallback;
import com.carlt.sesame.data.LoginInfo;
import com.carlt.sesame.data.career.SecretaryMessageInfo;
import com.carlt.sesame.ui.activity.car.CarMainActivity;
import com.carlt.sesame.utility.Log;
import com.carlt.sesame.utility.MyParse;
import com.tencent.android.tpush.XGPushBaseReceiver;
import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushRegisterResult;
import com.tencent.android.tpush.XGPushShowedResult;
import com.tencent.android.tpush.XGPushTextMessage;

import org.json.JSONException;
import org.json.JSONObject;

public class MessageReceiver extends XGPushBaseReceiver {
    private Intent intent = new Intent("com.qq.xgdemo.activity.UPDATE_LISTVIEW");

    public static final String LogTag = "TPushReceiver";

    // 通知展示
    @Override
    public void onNotifactionShowedResult(Context context, XGPushShowedResult notifiShowedRlt) {

        Log.e("push", "推送1");
        if (context == null || notifiShowedRlt == null) {
            return;
        }
        // context.sendBroadcast(intent);
    }

    @Override
    public void onUnregisterResult(Context context, int errorCode) {
        Log.e("push", "推送2");

    }

    @Override
    public void onSetTagResult(Context context, int errorCode, String tagName) {
        Log.e("push", "推送3");
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
        Log.e("push", "推送4");
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
        Log.e("push", "推送5");
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
        Log.e("push", "customContent==" + customContent);
        if (customContent != null && customContent.length() != 0) {
            try {
                JSONObject obj = new JSONObject(customContent);
                // key1为前台配置的key
                if (!obj.isNull("key")) {
                    String v1 = obj.getString("key");
                    String v2 = obj.getString("class1");
                    String v3 = obj.getString("class2");
                    String v4 = obj.getString("reportdate");
                    Log.e("push", "v1==" + v1);
                    Log.e("push", "v2==" + v2);
                    Log.e("push", "v3==" + v3);
                    Log.e("push", "v4==" + v4);
                }
            } catch (JSONException e) {
                Log.e("push", "onNotifactionClickedResult--e==" + e);
            }
        }
    }

    boolean isRegister = false;

    @Override
    public void onRegisterResult(Context context, int errorCode, XGPushRegisterResult message) {

        Log.e("push", "推送注册-errorCode==" + errorCode);
        String xgtoken = message.getToken();
        Log.e("push", "推送注册-token-111111==" + xgtoken);
        CPControl.GetPushXgTokenResult(xgtoken, DorideApplication.NIMEI, listener);
    }

    private GetResultListCallback listener = new GetResultListCallback() {
        @Override
        public void onFinished(Object o) {
            Log.e("push", "信鸽推送token保存至后台成功!");
        }

        @Override
        public void onErro(Object o) {
            Log.e("push", "信鸽推送token保存至后台失败...");
        }
    };

    // 消息透传
    @Override
    public void onTextMessage(Context context, XGPushTextMessage message) {
        Log.e("push", "推送7");

        String content = message.getContent().replaceAll("&quot;", "");
        String title = message.getTitle();
        Log.e("info", "title==" + title);
        Log.e("info", "content==" + content);
        int class1 = 0;
        int class2 = 0;
        String reportDate = "";
        if (content != null && content.length() != 0) {
            try {
                JSONObject obj = new JSONObject(content);
                // key1为前台配置的key
                class1 = MyParse.parseInt(obj.optString("class1"));
                class2 =  MyParse.parseInt(obj.optString("class2"));
                reportDate = obj.optString("reportdate");
                Log.e("push", "class1==" + class1);
                Log.e("push", "class2==" + class2);
                Log.e("push", "reportDate==" + reportDate);
            } catch (JSONException e) {
                Log.e("push", "onTextMessage--e==" + e);
            }
        }
        Intent i = null;
        switch (class1) {
            case SecretaryMessageInfo.C1_T2:
                // // 21 安防故障
                if (LoginInfo.getToken() != null && LoginInfo.getToken().length() > 0) {
                    Intent intent2 = new Intent();
                    intent2.setAction(CarMainActivity.CARMAIN_SAFETY);
                    context.sendBroadcast(intent2);
                }
                showNotification(context, title, class1,class2);
                break;
            case LoginInfo.Author_Message:
                // 授权推送（只有主机才能展示）
                if (LoginInfo.isMain()) {
                    showNotification(context, title, class1,class2);
                } else {

                }
                break;
            case LoginInfo.Feetip_Message:
                // 续费推送（只有主机才能展示）
                if (LoginInfo.isMain()) {
                    showNotification(context, title, class1,class2);
                } else {

                }
                break;
            default:
                showNotification(context, title, class1,class2);
                break;
        }

    }

    private void showNotification(Context context, String title, int type,int type2) {
        Intent intent = new Intent(context, PushService.class);
        intent.putExtra(PushService.CLASS1, type);
        intent.putExtra(PushService.CLASS2, type2);
        // intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
        // | Intent.FLAG_ACTIVITY_NEW_TASK);
        // PendingIntent contentIntent = PendingIntent.getService(context,
        // R.string.app_name, intent,
        // PendingIntent.FLAG_UPDATE_CURRENT);
        
        // 通知ID需要变更，避免Intent被覆盖
        PendingIntent contentIntent = PendingIntent.getService(context,
                (int)System.currentTimeMillis(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationManager nm = (NotificationManager)context
                .getSystemService(Context.NOTIFICATION_SERVICE);
//        Notification n = new Notification(R.drawable.ic_launcher, title, System.currentTimeMillis());
//        Notification notification=new Notification();
//        n.flags = Notification.FLAG_AUTO_CANCEL;
//        n.defaults = Notification.DEFAULT_SOUND;
//        n.audioStreamType = android.media.AudioManager.ADJUST_LOWER;
//        n.setLatestEventInfo(context, context.getResources().getString(R.string.app_name), title,
//                contentIntent);
        Notification n1 = new Notification.Builder(context)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setContentTitle(context.getResources().getString(R.string.app_name))
                .setContentText(title)
                .setContentIntent(contentIntent)
                .setSmallIcon(R.drawable.ic_launcher)
                .setWhen(System.currentTimeMillis())
                .build();
        n1.flags = Notification.FLAG_AUTO_CANCEL;
        n1.audioStreamType = android.media.AudioManager.ADJUST_LOWER;

        // 每次通知完，通知ID需要变更，避免消息覆盖掉
        nm.notify((int)System.currentTimeMillis(), n1);
    }

}

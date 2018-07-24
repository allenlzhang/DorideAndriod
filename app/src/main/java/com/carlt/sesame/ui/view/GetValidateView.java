
package com.carlt.sesame.ui.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.carlt.sesame.R;
import com.carlt.sesame.control.CPControl;
import com.carlt.sesame.control.CPControl.GetResultListCallback;
import com.carlt.sesame.data.BaseResponseInfo;
import com.carlt.sesame.ui.activity.career.report.MenuCalendar;
import com.carlt.sesame.utility.UUToast;

/**
 * 获取语音验证码View
 * 
 * @author daisy
 */
public class GetValidateView extends MenuCalendar {

    private TextView mTextView;// 请求语音播报验证码按钮

    private String mType;// 验证码类型

    private String mPhoneNum;// 电话号码

    private Context mContext;

    public GetValidateView(Context context, String type, String phoneNum) {
        super(context);
        View child = LayoutInflater.from(context).inflate(R.layout.layout_get_validate, null);
        init(child);
        setTitle("语音播报验证码");

        mType = type;
        mPhoneNum = phoneNum;
        mContext = context;

        mTextView = (TextView)child.findViewById(R.id.layout_get_validate_txt);

        mTextView.setOnClickListener(mClickListener);
    }

    private OnClickListener mClickListener = new OnClickListener() {

        @Override
        public void onClick(View arg0) {
            // 调用语音播报验证码接口
            setClickStatus(false);
            CPControl.GetVoiceValidateResult(mType, mPhoneNum, listener);

        }
    };

    // 语音获取验证码的回调
    private GetResultListCallback listener = new GetResultListCallback() {

        @Override
        public void onFinished(Object o) {
            Message msg = new Message();
            msg.what = 0;
            mHandler.sendMessage(msg);
        }

        @Override
        public void onErro(Object o) {
            Message msg = new Message();
            msg.what = 1;
            msg.obj = o;
            mHandler.sendMessage(msg);

        }
    };

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case 0:
                    dissmiss();
                    break;
                case 1:
                    BaseResponseInfo mBaseResponseInfo = (BaseResponseInfo)msg.obj;
                    if (mBaseResponseInfo != null && mBaseResponseInfo.getInfo() != null) {
                        UUToast.showUUToast(mContext, "出错了：" + mBaseResponseInfo.getInfo());
                    } else {
                        UUToast.showUUToast(mContext, "出错了...");
                    }
                    setClickStatus(true);
                    break;

            }
        };
    };

    @Override
    protected void onPopCreat() {
        // TODO Auto-generated method stub

    }

    /**
     * 设置请求语音播报验证码能够点击
     */
    public void setClickStatus(boolean isClickable) {
        if (isClickable == true) {
            mTextView.setOnClickListener(mClickListener);
            mTextView.setBackgroundResource(R.drawable.bottom_btn_bg);
        }else{
            mTextView.setOnClickListener(null);
            mTextView.setBackgroundResource(R.drawable.bottom_btn_bg_gray);
        }
    }

    public interface GetVoiceValidateListener {
        void onClick();
    };
}

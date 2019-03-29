
package com.carlt.sesame.ui.activity.setting;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.carlt.doride.R;
import com.carlt.doride.http.retrofitnet.model.GetCarInfo;
import com.carlt.sesame.control.CPControl;
import com.carlt.sesame.control.CPControl.GetResultListCallback;
import com.carlt.sesame.data.BaseResponseInfo;
import com.carlt.sesame.data.SesameLoginInfo;
import com.carlt.sesame.ui.activity.base.BaseActivity;
import com.carlt.sesame.ui.view.PopBoxCreat;
import com.carlt.sesame.utility.UUToast;

/**
 * 修改行车里程界面
 * 
 * @author Administrator
 */
public class EditMileageMaintainActivity extends BaseActivity {
    private ImageView back;// 头部返回键

    private TextView title;// 标题文字

    private TextView txtRight;// 头部右侧文字

    private View mViewMileage;// 公里数外层layout

    private EditText mEdtMileage;// 公里数

    private TextView mBtnSure;// 确定按钮

    private String mileage;//上次保养里程
    private Dialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_mileage);

        mileage= GetCarInfo.getInstance().maintenMiles+"";

        initTitle();
        init();
    }

    private void initTitle() {
        back = (ImageView)findViewById(R.id.head_back_img1);
        title = (TextView)findViewById(R.id.head_back_txt1);
        txtRight = (TextView)findViewById(R.id.head_back_txt2);

        back.setImageResource(R.drawable.arrow_back);
        title.setText("修改上次保养里程");

        txtRight.setVisibility(View.GONE);

        back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void init() {
        mViewMileage = findViewById(R.id.edit_mileage_layout_num);
        mEdtMileage = (EditText)findViewById(R.id.edit_mileage_edt_num);
        mBtnSure = (TextView)findViewById(R.id.edit_mileage_btn);

        mEdtMileage.setOnFocusChangeListener(mChangeListener);
        mBtnSure.setOnClickListener(mClickListener);

        if (mileage != null && mileage.length() > 0) {
            mEdtMileage.setText(mileage);
        }
    }

    private OnFocusChangeListener mChangeListener = new OnFocusChangeListener() {

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                mViewMileage.setBackgroundResource(R.drawable.edittext_bg_focused);
            } else {
                mViewMileage.setBackgroundResource(R.drawable.edittext_bg_focused_no);
            }
        }
    };

    private OnClickListener mClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            // 调用修改行驶里程接口
            String s = mEdtMileage.getText().toString();
            if (s.equals(mileage)) {
                UUToast.showUUToast(EditMileageMaintainActivity.this, "您还没有对上次保养里程进行修改哦！");
            } else {
                mileage = s;
                if(mDialog==null){
                    mDialog= PopBoxCreat.createDialogWithProgress(EditMileageMaintainActivity.this, "数据提交中...");
                }
                mDialog.show();
                CPControl.GetUpdateCarMaintenMilesResult(s, listener);
            }

        }
    };

    // 修改里程回调
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

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    // 修改成功
                    if(mDialog!=null&&mDialog.isShowing()){
                        mDialog.dismiss();
                    }
                    int resultCode = ManageCarActivity.REQUESTCODE + 2;
                    Intent data = new Intent();
                    data.putExtra(ManageCarActivity.NEWDATA,mileage);
                    setResult(resultCode, data);
                    UUToast.showUUToast(EditMileageMaintainActivity.this, "修改上次保养里程成功！");
                    finish();
                    break;

                case 1:
                    // 修改失败
                    if(mDialog!=null&&mDialog.isShowing()){
                        mDialog.dismiss();
                    }
                    BaseResponseInfo mInfo = (BaseResponseInfo)msg.obj;
                    if (mInfo != null) {
                        String s = mInfo.getInfo();
                        if (s != null && s.length() > 0) {
                            UUToast.showUUToast(EditMileageMaintainActivity.this, s);
                        } else {
                            UUToast.showUUToast(EditMileageMaintainActivity.this, "修改上次保养里程失败...");
                        }
                    } else {
                        UUToast.showUUToast(EditMileageMaintainActivity.this, "修改上次保养里程失败...");
                    }

                    break;
            }
        }

    };
}

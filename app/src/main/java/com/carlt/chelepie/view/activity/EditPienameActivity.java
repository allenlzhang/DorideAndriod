
package com.carlt.chelepie.view.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.carlt.chelepie.control.RecorderControl;
import com.carlt.doride.DorideApplication;
import com.carlt.doride.R;
import com.carlt.doride.base.BaseActivity;
import com.carlt.doride.data.BaseResponseInfo;
import com.carlt.doride.model.LoginInfo;
import com.carlt.doride.protocolparser.BaseParser;
import com.carlt.doride.ui.view.PopBoxCreat;
import com.carlt.doride.ui.view.UUToast;
import com.carlt.sesame.control.CPControl;


/**
 * 更改车乐拍名称
 * 
 * @author Administrator
 */
public class EditPienameActivity extends BaseActivity implements OnClickListener {
    private ImageView back;// 头部返回键

    private TextView title;// 标题文字

    private TextView txtRight;// 头部右侧文字

    private EditText mEdtName;// 车乐拍名
    
    private TextView mTxtCommit;//提交
    
    private TextView mTxtName;//车乐拍名

    private String name;

    public final static String NAME = "name";

    private Dialog mDialog;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_piename);

        try {
            name = getIntent().getStringExtra(NAME);
            if(!name.equals(DorideApplication.ssid)){
                name= DorideApplication.ssid;
            }
        } catch (Exception e) {
            // TODO: handle exception
        }

        initTitle();
        init();
    }

    private void initTitle() {
        back = (ImageView)findViewById(R.id.head_back_img1);
        title = (TextView)findViewById(R.id.head_back_txt1);
        txtRight = (TextView)findViewById(R.id.head_back_txt2);

        back.setImageResource(R.drawable.arrow_back);
        title.setText("摄像头名称");

        txtRight.setVisibility(View.GONE);

        back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void init() {
        mEdtName = (EditText)findViewById(R.id.edit_piename_edt_name);
        mTxtCommit=(TextView)findViewById(R.id.edit_piename_btn);
        
        mTxtName=(TextView)findViewById(R.id.edit_piename_txt_name);
        
        mEdtName.setText(name);
        mTxtName.setText("摄像头名称："+name);
        mTxtCommit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // 修改名称
        String nameNew = mEdtName.getText().toString();
        if (nameNew == null || nameNew.length() <= 0) {
            UUToast.showUUToast(EditPienameActivity.this, "您还没有输入摄像头名称哦...");
            return;
        } else if (nameNew.equals(name)) {
            UUToast.showUUToast(EditPienameActivity.this, "您还没有更改摄像头名称哦...");
            return;
        } else {

            if (mDialog == null) {
                mDialog = PopBoxCreat.createDialogWithProgress(EditPienameActivity.this, "提交中...");
            }
            mDialog.show();
            RecorderControl.EditPieName(nameNew, mListener);

        }
    }

    private BaseParser.ResultCallback mListener = new BaseParser.ResultCallback() {

        @Override
        public void onSuccess(BaseResponseInfo bInfo) {
            Message msg = new Message();
            msg.what = 0;
            mHandler.sendMessage(msg);
        }

        @Override
        public void onError(BaseResponseInfo o) {
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
                    if (mDialog != null) {
                        mDialog.dismiss();
                    }
                    UUToast.showUUToast(EditPienameActivity.this, "摄像头名称修改成功！");
                    String name = mEdtName.getText().toString();
                    Intent mIntent = new Intent();
                    mIntent.putExtra(NAME, name);
                    setResult(RESULT_OK, mIntent);
                    finish();
                    break;

                case 1:
                    if (mDialog != null) {
                        mDialog.dismiss();
                    }
                    BaseResponseInfo mInfo = (BaseResponseInfo)msg.obj;
                    if (mInfo != null) {
                        UUToast.showUUToast(EditPienameActivity.this,
                                "摄像头名称修改失败:" + mInfo.getInfo());
                    } else {
                        UUToast.showUUToast(EditPienameActivity.this, "摄像头名称修改失败...");
                    }
                    break;
            }
        }

    };

}

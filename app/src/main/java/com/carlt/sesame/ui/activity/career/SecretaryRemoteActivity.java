
package com.carlt.sesame.ui.activity.career;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.carlt.doride.R;
import com.carlt.sesame.control.CPControl;
import com.carlt.sesame.control.CPControl.GetResultListCallback;
import com.carlt.sesame.data.BaseResponseInfo;
import com.carlt.sesame.data.LoginInfo;
import com.carlt.sesame.data.car.CheckFaultInfo;
import com.carlt.sesame.data.career.DiagnoseInfo;
import com.carlt.sesame.data.career.RecommendSalesInfo;
import com.carlt.sesame.http.AsyncImageLoader;
import com.carlt.sesame.ui.activity.base.LoadingActivityWithTitle;
import com.carlt.sesame.ui.adapter.RemoteFaultAdapter;
import com.carlt.sesame.ui.view.PopBoxCreat;
import com.carlt.sesame.utility.Log;
import com.carlt.sesame.utility.UUToast;

import java.util.ArrayList;

/**
 * 远程诊断页面
 * 
 * @author daisy
 */
public class SecretaryRemoteActivity extends LoadingActivityWithTitle implements OnClickListener {

    private ImageView back;// 头部返回键

    private TextView title;// 标题文字

    private TextView txtRight;// 头部右侧文字

    private ImageView mImageViewSecretary;// 车秘书头像

    private TextView mTextViewSecretary;// 车秘书提示

    private ImageView mImageView2;// 顾问头像

    private TextView mTextView1;// 顾问姓名

    private TextView mTextView2;// 顾问所属公司

    private TextView mTextView3;// 发送问题文字

    private ListView mListView;// 问题列表

    private View mView1;// 我要申报

    private View mView2;// 电话咨询

    private RemoteFaultAdapter mAdapter;

    private DiagnoseInfo mDiagnoseInfo;

    private RecommendSalesInfo mRecommendSalesInfo;// 顾问信息

    private AsyncImageLoader mAsyncImageLoader = AsyncImageLoader.getInstance();

    public static final String TYPE_SECRETARY = "type_secretary";// 从车秘书跳转过来的

    public static final String TYPE_TEST = "type_test";// 从爱车体检跳转过来的

    public static final String ID = "id";

    public static final String SEND_STATUS = "send_status";

    private String mDataId;// 发送问题时的关联id

    private int send_status;// 问题发送状态

    private Dialog mDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_career_secretary_remote);
        setTitleView(R.layout.head_back);

        try {
            mDataId = getIntent().getStringExtra(ID);
            send_status = getIntent().getIntExtra(SEND_STATUS, -1);
        } catch (Exception e) {
        }

        initTitle();
        initSubTitle();
        init();

        LoadData();
    }

    private void initTitle() {
        back = (ImageView)findViewById(R.id.head_back_img1);
        title = (TextView)findViewById(R.id.head_back_txt1);
        txtRight = (TextView)findViewById(R.id.head_back_txt2);

        back.setImageResource(R.drawable.arrow_back);
        title.setText("远程诊断");
        txtRight.setText("维修预约");

        back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
        txtRight.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(SecretaryRemoteActivity.this,
                        SecretaryAppointmentActivity.class);
                startActivity(mIntent);
            }
        });

    }

    private void initSubTitle() {
        mImageViewSecretary = (ImageView)findViewById(R.id.layout_sub_head_img);
        mTextViewSecretary = (TextView)findViewById(R.id.layout_sub_head_txt);

        mImageViewSecretary.setImageResource(LoginInfo.getSecretaryImg());
        mTextViewSecretary.setText("根据你的情况，为你推荐了一位汽车顾问");
    }

    private void init() {
        mDialog = PopBoxCreat.createDialogWithProgress(SecretaryRemoteActivity.this, "信息提交中...");

        mImageView2 = (ImageView)findViewById(R.id.activity_career_secretary_remote_img2);

        mTextView1 = (TextView)findViewById(R.id.activity_career_secretary_remote_txt1);
        mTextView2 = (TextView)findViewById(R.id.activity_career_secretary_remote_txt2);
        mTextView3 = (TextView)findViewById(R.id.activity_career_secretary_remote_txt3);

        mListView = (ListView)findViewById(R.id.activity_career_secretary_remote_list);

        mView1 = findViewById(R.id.activity_career_secretary_remote_layout1);
        mView2 = findViewById(R.id.activity_career_secretary_remote_layout2);

        // if (send_status == 0) {
        // // 未发送问题
        // mTextView3.setText("发送问题");
        // mTextView3.setTextColor(getResources().getColor(R.color.text_color_gray2));
        // } else if (send_status == 1) {
        // // 已发送问题
        // mTextView3.setText("问题已发送");
        // mTextView3.setTextColor(getResources().getColor(R.color.text_color_gray1));
        // }

    }

    @Override
    protected void LoadSuccess(Object data) {

        mDiagnoseInfo = (DiagnoseInfo)data;
        mView1.setOnClickListener(this);
        // if (send_status == 0) {
        // // 未发送问题
        // mView1.setOnClickListener(this);
        // } else {
        // // 已发送问题
        // mView1.setOnClickListener(null);
        // }

        mView2.setOnClickListener(this);
        if (mDiagnoseInfo != null) {
            mRecommendSalesInfo = mDiagnoseInfo.getmRecommendSalesInfo();

            String string;

            StringBuffer stringBuffer = new StringBuffer();
            // 顾问姓名
            string = mRecommendSalesInfo.getName();
            if (string != null && string.length() > 0) {
                stringBuffer.append(string);
            }
            // 顾问职位
            string = mRecommendSalesInfo.getPosition();
            if (string != null && string.length() > 0 && !string.equals("null")) {
                stringBuffer.append("(");
                stringBuffer.append(string);
                stringBuffer.append(")");
            } else {
                stringBuffer.append("()");
            }
            if (stringBuffer != null && stringBuffer.length() > 0) {
                mTextView1.setText(stringBuffer.toString());
            }

            // 顾问公司
            string = mRecommendSalesInfo.getCompany();
            if (string != null && string.length() > 0) {
                mTextView2.setText(mRecommendSalesInfo.getCompany());
            }

            string = mRecommendSalesInfo.getImgUrl();
            if (string != null && string.length() > 0) {
                if (mAsyncImageLoader.getBitmapByUrl(string) != null) {
                    mImageView2.setImageBitmap(mAsyncImageLoader.getBitmapByUrl(string));
                }
            } else {
                mImageView2.setImageResource(R.drawable.icon_default_head);
            }

            ArrayList<CheckFaultInfo> mArrayList = mDiagnoseInfo.getmCheckFaultInfoList();
            mAdapter = new RemoteFaultAdapter(SecretaryRemoteActivity.this, mArrayList);
            mListView.setAdapter(mAdapter);

            super.LoadSuccess(data);
        }
    }

    @Override
    protected void LoadErro(Object erro) {

        super.LoadErro(erro);
    }

    @Override
    protected void LoadData() {
        super.LoadData();
        if (mDataId != null && mDataId.length() > 0) {
            CPControl.GetDiagnoseListResult(mDataId, listener);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_career_secretary_remote_layout1:
                // 发问题
                // if (mDataId != null && mDataId.length() > 0 &&
                // mRecommendSalesInfo != null) {
                // String salesid = mRecommendSalesInfo.getId();
                // if (salesid != null && salesid.length() > 0) {
                // mDialog.show();
                // CPControl.GetSubmitproblemResult(mDataId, salesid,
                // CPControl.SUBMITPROBLEM_send, listener2);
                // }
                // }
                ArrayList<CheckFaultInfo> mList = mDiagnoseInfo.getmCheckFaultInfoList();
                StringBuffer mBuffer = new StringBuffer();
                for (int i = 0; i < mList.size(); i++) {
                    CheckFaultInfo mInfo = mList.get(i);
                    mBuffer.append("[");
                    mBuffer.append(mInfo.getScope());
                    mBuffer.append("]");
                    mBuffer.append("故障代码：");
                    mBuffer.append(mInfo.getCode());
                    mBuffer.append(",");
                    mBuffer.append(mInfo.getContent());
                    mBuffer.append("\n");
                }
//                Intent mIntent = new Intent(SecretaryRemoteActivity.this, DeclarationActivity.class);
//                mIntent.putExtra(DeclarationActivity.MSG_INFO, mBuffer.toString());
//                startActivity(mIntent);
                break;
            case R.id.activity_career_secretary_remote_layout2:
                // 拨打电话,调用系统的拨号服务实现电话拨打功能
                if (mRecommendSalesInfo != null) {
                    String phone_number = mRecommendSalesInfo.getMobile();
                    if (phone_number != null && !phone_number.equals("")) {
                        // 调用系统的拨号服务实现电话拨打功能
                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
                                + phone_number.trim()));
                        try {
                        	SecretaryRemoteActivity.this.startActivity(intent);
    					} catch (Exception e) {
    						Log.e("info", e.getMessage());
    						if (e instanceof SecurityException) {
    							UUToast.showUUToast(SecretaryRemoteActivity.this, "请到设置页面打开芝麻乐园的通话权限");
    						}
    					}
                    }
                    String salesid = mRecommendSalesInfo.getId();
                    CPControl.GetSubmitproblemResult(mDataId, salesid,
                            CPControl.SUBMITPROBLEM_phone, null);
                }

                break;
        }

    }

    private GetResultListCallback listener2 = new GetResultListCallback() {

        @Override
        public void onFinished(Object o) {
            mHandler.sendEmptyMessage(2);

        }

        @Override
        public void onErro(Object o) {
            // TODO Auto-generated method stub
            Message msg = new Message();
            msg.what = 3;
            msg.obj = o;
            mHandler.sendMessage(msg);
        }
    };

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2:
                    if (mDialog != null && mDialog.isShowing()) {
                        mDialog.dismiss();
                    }
                    mTextView3.setText("问题已发送");
                    mView1.setOnClickListener(null);
                    UUToast.showUUToast(SecretaryRemoteActivity.this, "问题发送成功！");
                    finish();
                    break;
                case 3:
                    if (mDialog != null && mDialog.isShowing()) {
                        mDialog.dismiss();
                    }
                    BaseResponseInfo mBaseResponseInfo = (BaseResponseInfo)msg.obj;
                    UUToast.showUUToast(SecretaryRemoteActivity.this,
                            "问题发送失败：" + mBaseResponseInfo.getInfo());
                    break;
            }
        };
    };

    @Override
    public void OnImgLoadFinished(String url, Bitmap mBitmap) {
        super.OnImgLoadFinished(url, mBitmap);
        if (url.equals(LoginInfo.getSecretaryImg())) {
            mImageViewSecretary.setImageBitmap(mBitmap);
        } else if (url.equals(mRecommendSalesInfo.getImgUrl())) {
            mImageView2.setImageBitmap(mBitmap);
        }
    }

    int count;

    @Override
    protected void onResume() {
        super.onResume();
        if (count > 1) {
            CPControl.GetDiagnoseListResult(mDataId, listener);
        }
        count++;

    }

}

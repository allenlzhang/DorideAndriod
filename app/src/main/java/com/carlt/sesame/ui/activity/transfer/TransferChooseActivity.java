
package com.carlt.sesame.ui.activity.transfer;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.carlt.sesame.R;
import com.carlt.sesame.control.LoginControl;
import com.carlt.sesame.ui.activity.base.BaseActivity;
import com.carlt.sesame.ui.activity.usercenter.scan.ScanActivity;
import com.carlt.sesame.ui.view.UUTransferDialog;

/**
 * 首次过户选择页面
 * 
 * @author Administrator
 */
public class TransferChooseActivity extends BaseActivity implements OnClickListener {
	// 头部返回键
	private ImageView back;
	// 标题文字
	private TextView title;
	// 头部右侧文字
	private TextView txtRight;
	// 进入过户流程
	private ImageView mChoose;
	// 正常绑定激活
	private ImageView mNormal;
	//等待过户对话框
	private UUTransferDialog mDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_transfer_choose);

		initTitle();
		init();
	}

	private void initTitle() {
		back = (ImageView) findViewById(R.id.head_back_img1);
		title = (TextView) findViewById(R.id.head_back_txt1);
		txtRight = (TextView) findViewById(R.id.head_back_txt2);

		back.setImageResource(R.drawable.arrow_back);
		title.setText("服务选择");

		txtRight.setVisibility(View.GONE);

		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

	}

	private void init() {
		mChoose = (ImageView) findViewById(R.id.transfer_choose_img_choose);
		mNormal = (ImageView) findViewById(R.id.transfer_choose_img_normal);

		mChoose.setOnClickListener(this);
		mNormal.setOnClickListener(this);
	}

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				
				break;

			case 1:
				break;
			}
		}

	};

	@Override
	public void onClick(View v) {
		if (mChoose.equals(v)) {
			Intent intent = new Intent(this, ScanActivity.class);
			startActivityForResult(intent, 0);
		} else if (mNormal.equals(v)) {
			LoginControl.logic(this);
			finish();
		}
	}
	
	
	
}

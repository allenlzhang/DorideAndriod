
package com.carlt.sesame.ui.activity.transfer;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.carlt.doride.R;
import com.carlt.sesame.control.ActivityControl;
import com.carlt.sesame.control.CPControl;
import com.carlt.sesame.control.CPControl.GetResultListCallback;
import com.carlt.sesame.data.BaseResponseInfo;
import com.carlt.sesame.ui.activity.base.BaseActivity;
import com.carlt.sesame.ui.view.PopBoxCreat;
import com.carlt.sesame.ui.view.PopBoxCreat.DialogWithTitleClick;
import com.carlt.sesame.utility.UUToast;

/**
 * 过户处理页面
 * 
 * @author Administrator
 */
public class TransferHandleActivity extends BaseActivity implements OnClickListener {
	// 头部返回键
	private ImageView back;
	// 标题文字
	private TextView title;
	// 头部右侧文字
	private TextView txtRight;
	// 同意过户
	private TextView mTxtAgree;
	// 拒绝过户
	private TextView mTxtRefurse;
	//--过户人
	private TextView mTxtTransferName;

	String outtingid;
	String mobile;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_transfer_handle);

		outtingid = getIntent().getStringExtra("outtingid");
		mobile = getIntent().getStringExtra("mobile");

		initTitle();
		init();
	}

	private void initTitle() {
		back = (ImageView) findViewById(R.id.head_back_img1);
		title = (TextView) findViewById(R.id.head_back_txt1);
		txtRight = (TextView) findViewById(R.id.head_back_txt2);

		back.setImageResource(R.drawable.arrow_back);
		title.setText("过户处理");

		txtRight.setVisibility(View.GONE);

		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

	}

	private void init() {
		mTxtAgree = (TextView) findViewById(R.id.transfer_handle_txt_agree);
		mTxtRefurse = (TextView) findViewById(R.id.transfer_handle_txt_refurse);
		mTxtTransferName = (TextView) findViewById(R.id.transfer_handle_txt_info);

		mTxtTransferName.setText("收到" + mobile + "过户请求");
		mTxtAgree.setOnClickListener(this);
		mTxtRefurse.setOnClickListener(this);
	}

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 3:
				UUToast.showUUToast(context, "操作成功！");
//				Intent intent = new Intent(context,SesameMainActivity.class);
//				startActivity(intent);
				ActivityControl.onLogout(context);
				finish();
				break;
			case 4:
				BaseResponseInfo bse = (BaseResponseInfo) msg.obj;
				if(null != bse && !TextUtils.isEmpty(bse.getInfo())){
					UUToast.showUUToast(context, bse.getInfo());
				}else{
					UUToast.showUUToast(context, "操作失败！");
				}
				break;
			case 5:
				UUToast.showUUToast(context, "操作成功！");
//				Intent intent = new Intent(context,SesameMainActivity.class);
//				startActivity(intent);
				onBackPressed();
				break;

			case 6:
				BaseResponseInfo bse2 = (BaseResponseInfo) msg.obj;
				if(null != bse2 && !TextUtils.isEmpty(bse2.getInfo())){
					UUToast.showUUToast(context, bse2.getInfo());
				}else{
					UUToast.showUUToast(context, "操作失败！");
				}
				break;
			}
		}

	};

	@Override
	public void onClick(View v) {
		if (mTxtAgree.equals(v)) {
			PopBoxCreat.createDialogNotitle(context, "确定同意过户吗", "", "取消", "确认", new DialogWithTitleClick() {

				@Override
				public void onRightClick() {
					CPControl.GetTransferOldOptResult(outtingid, "1", optCallback);
				}

				@Override
				public void onLeftClick() {

				}
			});
		} else if (mTxtRefurse.equals(v)) {
			PopBoxCreat.createDialogNotitle(context, "确定拒绝过户吗", "", "取消", "确认", new DialogWithTitleClick() {

				@Override
				public void onRightClick() {
					CPControl.GetTransferOldOptResult(outtingid, "2", optRefurseCallback);
				}

				@Override
				public void onLeftClick() {

				}
			});
		}
	}

	public GetResultListCallback optCallback = new GetResultListCallback() {

		@Override
		public void onFinished(Object o) {
			Message msg = new Message();
			msg.what = 3;
			msg.obj = o;
			mHandler.sendMessage(msg);
		}

		@Override
		public void onErro(Object o) {
			Message msg = new Message();
			msg.what = 4;
			msg.obj = o;
			mHandler.sendMessage(msg);
		}
	};
	
	public GetResultListCallback optRefurseCallback = new GetResultListCallback() {

		@Override
		public void onFinished(Object o) {
			Message msg = new Message();
			msg.what = 5;
			msg.obj = o;
			mHandler.sendMessage(msg);
		}

		@Override
		public void onErro(Object o) {
			Message msg = new Message();
			msg.what = 6;
			msg.obj = o;
			mHandler.sendMessage(msg);
		}
	};


}

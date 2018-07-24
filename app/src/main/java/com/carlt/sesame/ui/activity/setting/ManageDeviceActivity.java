
package com.carlt.sesame.ui.activity.setting;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.carlt.sesame.R;
import com.carlt.sesame.data.LoginInfo;
import com.carlt.sesame.ui.activity.base.LoadingActivityWithTitle;

/**
 * 通用管理
 * 
 * @author Administrator
 */
public class ManageDeviceActivity extends LoadingActivityWithTitle implements OnClickListener {

	private ImageView back;// 头部返回键

	private TextView title;// 标题文字

	private TextView txtRight;// 头部右侧文字

	private TextView txtDeviceNum;// 绑定的设备号

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manage_device);
		setTitleView(R.layout.head_back);

		initTitle();
		init();
		LoadSuccess(null);
	}

	private void initTitle() {
		back = (ImageView) findViewById(R.id.head_back_img1);
		title = (TextView) findViewById(R.id.head_back_txt1);
		txtRight = (TextView) findViewById(R.id.head_back_txt2);

		title.setText("设备管理");
//		txtRight.setText("更换设备");
//		if(LoginInfo.isMain()){
//			txtRight.setVisibility(View.VISIBLE);
//		}else{
//			txtRight.setVisibility(View.GONE);
//		}
		
		back.setImageResource(R.drawable.arrow_back);

		back.setOnClickListener(this);
		txtRight.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, ManageCheckActivity.class);
				startActivity(intent);
			}
		});

	}

	private void init() {
		txtDeviceNum = (TextView) findViewById(R.id.manageDevice_txt_devicnum);
		txtDeviceNum.setText(LoginInfo.getDeviceidstring());
	}

	@Override
	protected void LoadSuccess(Object data) {
		super.LoadSuccess(data);

	}

	@Override
	protected void LoadErro(Object erro) {
		super.LoadErro(erro);
	}

	@Override
	protected void LoadData() {
		super.LoadData();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	public void onClick(View v) {
		if(v.equals(back)){
			finish();
		}
	}

}

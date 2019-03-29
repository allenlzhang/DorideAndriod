package com.carlt.sesame.ui.activity.career;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.carlt.doride.R;
import com.carlt.doride.http.retrofitnet.model.GetCarInfo;
import com.carlt.sesame.data.SesameLoginInfo;
import com.carlt.sesame.ui.activity.base.LoadingActivityWithTitle;
import com.carlt.sesame.ui.activity.career.order.RiLiActivity;

/**
 * 预约页面
 * 
 * @author daisy
 */
public class SecretaryAppointmentActivity extends LoadingActivityWithTitle
		implements OnClickListener {

	private ImageView back;// 头部返回键

	private TextView title;// 标题文字

	private TextView txtRight;// 头部右侧文字

	private ImageView mImageViewSecretary;// 车秘书头像

	private TextView mTextViewSecretary;// 车秘书提醒

	private View mView1;// 保养预约

	private View mView2;// 维修预约

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_career_secretary_appointment);
		setTitleView(R.layout.head_back);
		initTitle();
		initSubTitle();
		init();
		LoadData();

	}

	private void initTitle() {
		back = (ImageView) findViewById(R.id.head_back_img1);
		title = (TextView) findViewById(R.id.head_back_txt1);
		txtRight = (TextView) findViewById(R.id.head_back_txt2);

		back.setImageResource(R.drawable.arrow_back);
		title.setText("选择服务类型");
		txtRight.setVisibility(View.GONE);

		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

	}

	private void initSubTitle() {
		mImageViewSecretary = (ImageView) findViewById(R.id.layout_sub_head_img);
		mTextViewSecretary = (TextView) findViewById(R.id.layout_sub_head_txt);

		mImageViewSecretary.setImageResource(GetCarInfo.getInstance().secretaryID == 1?R.drawable.secretary_female:R.drawable.secretary_male);
		mTextViewSecretary.setText("欢迎提前预约，可节省大量时间哦	");
	}

	private void init() {
		mView1 = findViewById(R.id.activity_career_secretary_appointment_relative1);
		mView2 = findViewById(R.id.activity_career_secretary_appointment_relative2);

		mView1.setOnClickListener(this);
		mView2.setOnClickListener(this);
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
		LoadSuccess(null);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.activity_career_secretary_appointment_relative1:
			// 跳转到保养预约
			Intent intent1 = new Intent(SecretaryAppointmentActivity.this,
					RiLiActivity.class);
			intent1.putExtra("type", RiLiActivity.TYPE_Maintenance);
			startActivity(intent1);
			finish();
			break;
		case R.id.activity_career_secretary_appointment_relative2:
			// 跳转到维修预约

			Intent intent2 = new Intent(SecretaryAppointmentActivity.this,
					RiLiActivity.class);
			intent2.putExtra("type", RiLiActivity.TYPE_FIX);
			startActivity(intent2);
			finish();
			break;
		}
	}
}

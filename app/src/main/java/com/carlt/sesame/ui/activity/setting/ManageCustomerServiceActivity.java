package com.carlt.sesame.ui.activity.setting;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.carlt.sesame.R;
import com.carlt.sesame.ui.activity.base.BaseActivity;

/**
 * 设置-车辆管理页面
 * 
 * @author daisy
 */
public class ManageCustomerServiceActivity extends BaseActivity {
	
	private ImageView back;// 头部返回键

	private TextView title;// 标题文字

	/**
	 * 拨打客服热线
	 */
	private RelativeLayout layoutCall;

	/**
	 * 是否显示 拖动滑块的checkbox
	 */
	private CheckBox isshowDragCB;



	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manage_customer_service);
		initTitle();
		init();
		setListener();

	}

	

	private void initTitle() {
		
	       	back = (ImageView)findViewById(R.id.head_back_img1);
	        title = (TextView)findViewById(R.id.head_back_txt1);

	        title.setText("联系客服");

	        back.setImageResource(R.drawable.arrow_back);
	        back.setOnClickListener(new OnClickListener() {

	            @Override
	            public void onClick(View v) {
	                finish();
	            }
	        });
		
	}
	private void init() {
		layoutCall = (RelativeLayout) findViewById(R.id.manage_customer_service_layout1);
		isshowDragCB = (CheckBox) findViewById(R.id.manage_customer_service_cb);
	}

	@Override
	protected void onResume() {
		super.onResume();
		isshowDragCB.setChecked(dragViewCtr.isShowFlag());
	}
	
	private void setListener() {
		layoutCall.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dragViewCtr.showDialog();
			}
		});		
		
		isshowDragCB.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				dragViewCtr.setShow(isChecked);
				if(isChecked){
					dragViewCtr.showDragCallView();
				}
				else{
					dragViewCtr.hideDragCallView();
				}
			}
		});
	}
}

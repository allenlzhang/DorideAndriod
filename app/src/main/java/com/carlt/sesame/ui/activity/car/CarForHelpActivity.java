package com.carlt.sesame.ui.activity.car;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.carlt.doride.R;
import com.carlt.sesame.control.CPControl;
import com.carlt.sesame.data.car.HelpPhoneInfo;
import com.carlt.sesame.ui.activity.base.LoadingActivityWithTitle;
import com.carlt.sesame.ui.adapter.ForHelpAdapter;
import com.carlt.sesame.utility.Log;
import com.carlt.sesame.utility.UUToast;

import java.util.ArrayList;

/**
 * 座驾-一键求援页面
 * 
 * @author daisy
 */
public class CarForHelpActivity extends LoadingActivityWithTitle {
    private ImageView back;// 头部返回键

    private TextView title;// 标题文字

    private TextView txtRight;// 头部右侧文字

	private ListView mListView;// 电话列表

	private ForHelpAdapter mAdapter;

	private ArrayList<HelpPhoneInfo> mHelpPhoneInfoList;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_car_for_help);
		setTitleView(R.layout.head_back);
		
		initTitle();
		init();
		LoadData();

	}
	
	private void initTitle() {
        back = (ImageView)findViewById(R.id.head_back_img1);
        title = (TextView)findViewById(R.id.head_back_txt1);
        txtRight = (TextView)findViewById(R.id.head_back_txt2);

        back.setImageResource(R.drawable.arrow_back);
        title.setText("一键求援");
        txtRight.setVisibility(View.GONE);

        back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

	private void init() {
		mListView = (ListView) findViewById(R.id.activity_car_for_help_list);
	}

	@Override
	protected void LoadSuccess(Object data) {
		mHelpPhoneInfoList = (ArrayList<HelpPhoneInfo>) data;
		mAdapter = new ForHelpAdapter(this, mHelpPhoneInfoList);
		mListView.setAdapter(mAdapter);
		OnItemClickListener l = new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				HelpPhoneInfo info = mHelpPhoneInfoList.get(position);
				if (!info.isFlag()) {
					// 此处添加拨号代码 ;
					// 调用系统的拨号服务实现电话拨打功能
					String phone_number = info.getPhone();
					if (phone_number != null && !phone_number.equals("")) {

						// 调用系统的拨号服务实现电话拨打功能
						Intent intent = new Intent(Intent.ACTION_CALL,
								Uri.parse("tel:" + phone_number.trim()));
						try {
							CarForHelpActivity.this.startActivity(intent);
						} catch (Exception e) {
							Log.e("info", e.getMessage());
							if (e instanceof SecurityException) {
								UUToast.showUUToast(CarForHelpActivity.this, "请到设置页面打开芝麻乐园的通话权限");
							}
						}
					}
				}

			}
		};
		mListView.setOnItemClickListener(l);
		super.LoadSuccess(data);
	}

	@Override
	protected void LoadErro(Object erro) {

		super.LoadErro(erro);
	}

	@Override
	protected void LoadData() {
		super.LoadData();
		CPControl.GetHelpPhoneListResult(listener);

	}
}

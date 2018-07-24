
package com.carlt.sesame.ui.activity.car;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
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
import com.carlt.sesame.data.car.CarInfo;
import com.carlt.sesame.ui.activity.base.LoadingActivityWithTitle;
import com.carlt.sesame.ui.adapter.CarListAdapter;
import com.carlt.sesame.ui.adapter.CarListAdapter.OnItemBtnClick;
import com.carlt.sesame.ui.view.PopBoxCreat;
import com.carlt.sesame.ui.view.PopBoxCreat.DialogWithTitleClick;
import com.carlt.sesame.utility.UUToast;

import java.util.ArrayList;

/**
 * 违章-车辆列表页面
 * 
 * @author daisy
 */
public class CarListActivity extends LoadingActivityWithTitle {
	private ImageView back;// 头部返回键

	private TextView title;// 标题文字

	private TextView txtRight;// 头部右侧文字

	private ImageView imgRight;// 头部右侧图片

	private ListView mListView;// 电话列表

	private CarListAdapter mAdapter;

	private ArrayList<CarInfo> mCarInfos;

	private Dialog mDialog;
	
	public static final String CAR_ADD = "car_add";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_carlist);
		setTitleView(R.layout.head_back);

		initTitle();
		init();
		LoadData();
	}

	private void initTitle() {
		back = (ImageView) findViewById(R.id.head_back_img1);
		title = (TextView) findViewById(R.id.head_back_txt1);
		txtRight = (TextView) findViewById(R.id.head_back_txt2);
		imgRight = (ImageView) findViewById(R.id.head_back_img2);

		back.setImageResource(R.drawable.arrow_back);
		title.setText("车辆列表");
		txtRight.setVisibility(View.GONE);
		imgRight.setVisibility(View.VISIBLE);
		imgRight.setImageResource(R.drawable.violation_add);

		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				CarInfo carInfo = null;
				for (CarInfo carInfoItem : mCarInfos) {
					if (carInfoItem.getType().equals(CarInfo.TYPE_MY)) {
						carInfo = carInfoItem;
						break;
					}
				}
				Intent mIntent = new Intent(CarListActivity.this, CarFillIllegalActivity.class);
				mIntent.putExtra(CarFillIllegalActivity.CLASS_NAME, CarListActivity.class.getName());
				if (carInfo != null) {
					mIntent.putExtra(CarFillIllegalActivity.CAR_INFO, carInfo);
				}
				startActivity(mIntent);
				finish();
			}
		});
		imgRight.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent mIntent = new Intent();
				CarInfo cInfo = new CarInfo();
				cInfo.setCarNo(CAR_ADD);
				mIntent.putExtra(CarFillIllegalActivity.CAR_INFO, cInfo);
				setResult(1, mIntent);
				finish();
			}
		});
	}

	private void init() {
		mListView = (ListView) findViewById(R.id.carlist_list);
	}

	@Override
	protected void LoadSuccess(Object data) {
		mCarInfos = (ArrayList<CarInfo>) data;
		if (mAdapter == null) {
			mAdapter = new CarListAdapter(CarListActivity.this, mCarInfos, mItemBtnClick);
		}
		mListView.setAdapter(mAdapter);
		super.LoadSuccess(data);
	}

	private int optionPos;// 进行操作的item position

	private OnItemBtnClick mItemBtnClick = new OnItemBtnClick() {

		@Override
		public void onDelete(int position, final CarInfo mCarInfo) {
			DialogWithTitleClick mDialogWithTitleClick = new DialogWithTitleClick() {

				@Override
				public void onRightClick() {
					mDialog = PopBoxCreat.createDialogWithProgress(CarListActivity.this, "处理中...");
					mDialog.show();
					CPControl.GetDelCarResult(mCarInfo.getId(), listener_delete);

				}

				@Override
				public void onLeftClick() {

				}
			};
			PopBoxCreat.createDialogNotitle(CarListActivity.this, "是否删除该车辆信息", "", "取消", "确定", mDialogWithTitleClick);
			optionPos = position;

		}

		@Override
		public void onToBeMycar(int position, final CarInfo mCarInfo) {
			optionPos = position;
			DialogWithTitleClick mDialogWithTitleClick = new DialogWithTitleClick() {

				@Override
				public void onRightClick() {
					mDialog = PopBoxCreat.createDialogWithProgress(CarListActivity.this, "处理中...");
					mDialog.show();
					CPControl.GetSetMyCarResult(mCarInfo.getId(), listener_mycar);

				}

				@Override
				public void onLeftClick() {
				}
			};
			String carNo = mCarInfo.getCarNo();
			StringBuffer mBuffer = new StringBuffer("将");
			mBuffer.append("\"");
			mBuffer.append(carNo);
			mBuffer.append("\"");
			mBuffer.append("设置为我的车辆");
			PopBoxCreat.createDialogNotitle(CarListActivity.this,
				"设置为我的车辆",
				mBuffer.toString(),
				"取消",
				"确定",
				mDialogWithTitleClick);

		}

		@Override
		public void onToFill(int position, CarInfo mCarInfo) {
			// 跳转至信息回填页面
			Log.e("info", "onItemClick--------");
			Log.e("info", "mCarInfo.getCityName()==" + mCarInfo.getCityName());
			Intent mIntent = new Intent();
			mIntent.putExtra(CarFillIllegalActivity.CAR_INFO, mCarInfo);
			setResult(1, mIntent);
			finish();
		}

	};

	@Override
	protected void LoadErro(Object erro) {

		super.LoadErro(erro);
	}

	@Override
	protected void LoadData() {
		super.LoadData();
		CPControl.GetCarInfoListResult(listener);
	}

	/**
	 * 删除listener
	 */
	private GetResultListCallback listener_delete = new GetResultListCallback() {

		@Override
		public void onFinished(Object o) {
			Message msg = new Message();
			msg.what = 2;
			msg.obj = o;
			mHandler.sendMessage(msg);
		}

		@Override
		public void onErro(Object o) {
			Message msg = new Message();
			msg.what = 3;
			msg.obj = o;
			mHandler.sendMessage(msg);
		}
	};

	/**
	 * 设置为我的车辆listener
	 */
	private GetResultListCallback listener_mycar = new GetResultListCallback() {

		@Override
		public void onFinished(Object o) {
			Message msg = new Message();
			msg.what = 4;
			msg.obj = o;
			mHandler.sendMessage(msg);
		}

		@Override
		public void onErro(Object o) {
			Message msg = new Message();
			msg.what = 5;
			msg.obj = o;
			mHandler.sendMessage(msg);
		}
	};

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			BaseResponseInfo mInfo;
			String infoMsg = "";
			switch (msg.what) {
			case 2:
				// 删除成功
				if (mDialog != null) {
					mDialog.dismiss();
				}
				mInfo = (BaseResponseInfo) msg.obj;
				if (mInfo != null) {
					String info = mInfo.getInfo();
					if (info != null && !info.equals("")) {
						infoMsg = info;
					} else {
						infoMsg = "删除成功！";
					}
				} else {
					infoMsg = "删除成功！";
				}
				UUToast.showUUToast(CarListActivity.this, infoMsg);
				mCarInfos.remove(optionPos);
				mAdapter.notifyDataSetChanged();
				break;

			case 3:
				// 删除失败
				if (mDialog != null) {
					mDialog.dismiss();
				}
				mInfo = (BaseResponseInfo) msg.obj;
				if (mInfo != null) {
					String info = mInfo.getInfo();
					if (info != null && !info.equals("")) {
						infoMsg = info;
					} else {
						infoMsg = "删除失败...";
					}
				} else {
					infoMsg = "删除失败...";
				}
				UUToast.showUUToast(CarListActivity.this, infoMsg);
				break;
			case 4:
				// 设置成我的车辆成功
				if (mDialog != null) {
					mDialog.dismiss();
				}
				mInfo = (BaseResponseInfo) msg.obj;
				if (mInfo != null) {
					String info = mInfo.getInfo();
					if (info != null && !info.equals("")) {
						infoMsg = info;
					} else {
						infoMsg = "设置成功！";
					}
				} else {
					infoMsg = "设置成功！";
				}
				UUToast.showUUToast(CarListActivity.this, infoMsg);
				for (int i = 0; i < mCarInfos.size(); i++) {
					CarInfo mCarInfo = mCarInfos.get(i);
					if (i == optionPos) {
						mCarInfo.setType(CarInfo.TYPE_MY);
						LoginInfo.setCarcity(mCarInfo.getCityName());
						LoginInfo.setCity_code(mCarInfo.getCityCode());
						LoginInfo.setCarno(mCarInfo.getCarNo());
						LoginInfo.setEngineno(mCarInfo.getEngineNo());
						LoginInfo.setShortstandcarno(mCarInfo.getStandcarNo());
						LoginInfo.setRegistno(mCarInfo.getRegistNo());
					} else {
						mCarInfo.setType(CarInfo.TYPE_OTHER);
					}
				}
				mAdapter.notifyDataSetChanged();
				break;

			case 5:
				// 设置成我的车辆失败
				if (mDialog != null) {
					mDialog.dismiss();
				}
				mInfo = (BaseResponseInfo) msg.obj;
				if (mInfo != null) {
					String info = mInfo.getInfo();
					if (info != null && !info.equals("")) {
						infoMsg = info;
					} else {
						infoMsg = "设置失败...";
					}
				} else {
					infoMsg = "设置失败...";
				}
				UUToast.showUUToast(CarListActivity.this, infoMsg);
				break;
			}

		}

	};
}

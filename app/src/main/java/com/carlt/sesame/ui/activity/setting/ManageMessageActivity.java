
package com.carlt.sesame.ui.activity.setting;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.carlt.sesame.R;
import com.carlt.sesame.control.CPControl;
import com.carlt.sesame.control.CPControl.GetResultListCallback;
import com.carlt.sesame.data.BaseResponseInfo;
import com.carlt.sesame.data.set.PushSetInfo;
import com.carlt.sesame.ui.activity.base.LoadingActivityWithTitle;
import com.carlt.sesame.ui.view.PopBoxCreat;
import com.carlt.sesame.utility.UUToast;

/**
 * 设置-消息管理
 * 
 * @author daisy
 */
public class ManageMessageActivity extends LoadingActivityWithTitle {
	private ImageView back;// 头部返回键

	private TextView title;// 标题文字

	private TextView txtRight;// 头部右侧文字

	private CheckBox mCheckBox1;// 推送日报

	private CheckBox mCheckBox2;// 推送行车周报、月报

	// private CheckBox mCheckBox3;// 推送行车月报

	private CheckBox mCheckBox4;// 推送故障信息

	private CheckBox mCheckBox5;// 推送爱车振动

	private CheckBox mCheckBox6;// 推送车辆启动

	private CheckBox mCheckBox7;// 推送优惠券及活动

	// private CheckBox mCheckBox8;// 推送盒子失连

	private CheckBox mCheckBox9;// 获得勋章、升级驾驶证、创造新记录

	// private CheckBox mCheckBox10;// 升级驾驶证
	//
	// private CheckBox mCheckBox11;// 创造新记录

	private CheckBox mCheckBox12;// 蜂鸣器

	// private RelativeLayout mRelativeLayout;// 选择程度

	// private SeekBar mSeekBar;

	private PushSetInfo mOldPushSetInfo;

	private Dialog mDialog;

	private boolean hasChanged = false;

	private int changed_view_id;// 数值改变的view id

	private int changed_value;// 改变后的数值

	private boolean isNormal = true;// 是否是正常点击checkbox

	public final static String[] KEY_NAMES = { "dayreport", "weekreport", "trouble", "security", "vibstrength",
			"startup", "dealer", "gotmedal", "DBBuzzerSw" };

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manage_message);
		setTitleView(R.layout.head_back);
		initTitle();
		init();

		LoadData();
	}

	private void initTitle() {
		back = (ImageView) findViewById(R.id.head_back_img1);
		title = (TextView) findViewById(R.id.head_back_txt1);
		txtRight = (TextView) findViewById(R.id.head_back_txt2);

		back.setImageResource(R.drawable.arrow_back);
		title.setText("消息管理");
		txtRight.setVisibility(View.GONE);

		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

	}

	private void init() {
		mCheckBox1 = (CheckBox) findViewById(R.id.activity_manage_message_checkbox1);
		mCheckBox2 = (CheckBox) findViewById(R.id.activity_manage_message_checkbox2);
		mCheckBox4 = (CheckBox) findViewById(R.id.activity_manage_message_checkbox4);
		mCheckBox5 = (CheckBox) findViewById(R.id.activity_manage_message_checkbox5);
		mCheckBox6 = (CheckBox) findViewById(R.id.activity_manage_message_checkbox6);
		mCheckBox7 = (CheckBox) findViewById(R.id.activity_manage_message_checkbox7);
		mCheckBox9 = (CheckBox) findViewById(R.id.activity_manage_message_checkbox9);
		mCheckBox12 = (CheckBox) findViewById(R.id.activity_manage_message_checkbox12);
		// mRelativeLayout =
		// (RelativeLayout)findViewById(R.id.activity_manage_message_layout_check);
		// mSeekBar =
		// (SeekBar)findViewById(R.id.activity_manage_message_seekbar);
		// OnSeekBarChangeListener l = new OnSeekBarChangeListener() {
		//
		// @Override
		// public void onStopTrackingTouch(SeekBar seekBar) {
		// int p = seekBar.getProgress();
		// if (p < 25) {
		// p = 0;
		// } else if (p > 75) {
		// p = 100;
		// } else {
		// p = 50;
		// }
		// mSeekBar.setProgress(p);
		// int level = mOldPushSetInfo.getVibstrength();
		// int p_old = 0;
		// switch (level) {
		// case 1:
		// p_old = 0;
		// break;
		// case 2:
		// p_old = 50;
		// break;
		// case 3:
		// p_old = 100;
		// break;
		// }
		// if (p != p_old) {
		// hasChanged = true;
		// changed_view_id = seekBar.getId();
		// changed_value = p / 50 + 1;
		// pushData();
		// }
		// }
		//
		// @Override
		// public void onStartTrackingTouch(SeekBar seekBar) {
		// }
		//
		// @Override
		// public void onProgressChanged(SeekBar seekBar, int progress, boolean
		// fromUser) {
		// }
		// };
		// mSeekBar.setOnSeekBarChangeListener(l);
	}

	@Override
	protected void LoadData() {
		super.LoadData();
		CPControl.GetPushSetResult(listener);
	};

	@Override
	protected void LoadSuccess(Object data) {
		mOldPushSetInfo = (PushSetInfo) data;

		// 0:不允许 1：允许
		if (mOldPushSetInfo.getDayreport() == 0) {
			mCheckBox1.setChecked(false);
		} else if (mOldPushSetInfo.getDayreport() == 1) {
			mCheckBox1.setChecked(true);
		}

		if (mOldPushSetInfo.getWeekreport() == 0) {
			mCheckBox2.setChecked(false);
		} else if (mOldPushSetInfo.getWeekreport() == 1) {
			mCheckBox2.setChecked(true);
		}

		if (mOldPushSetInfo.getTrouble() == 0) {
			mCheckBox4.setChecked(false);
		} else if (mOldPushSetInfo.getTrouble() == 1) {
			mCheckBox4.setChecked(true);
		}

		// if (mOldPushSetInfo.getSecurity() == 0) {
		// mCheckBox5.setChecked(false);
		// mRelativeLayout.setVisibility(View.GONE);
		// } else if (mOldPushSetInfo.getSecurity() == 1) {
		// mCheckBox5.setChecked(true);
		// mRelativeLayout.setVisibility(View.VISIBLE);
		// }

		// int progress = mOldPushSetInfo.getVibstrength();
		//
		// switch (progress) {
		// case 1:
		// mSeekBar.setProgress(0);
		// break;
		// case 2:
		// mSeekBar.setProgress(50);
		// break;
		// case 3:
		// mSeekBar.setProgress(100);
		// break;
		// }

		if (mOldPushSetInfo.getStartup() == 0) {
			mCheckBox6.setChecked(false);
		} else if (mOldPushSetInfo.getStartup() == 1) {
			mCheckBox6.setChecked(true);
		}

		if (mOldPushSetInfo.getDealer() == 0) {
			mCheckBox7.setChecked(false);
		} else if (mOldPushSetInfo.getDealer() == 1) {
			mCheckBox7.setChecked(true);
		}
		if (mOldPushSetInfo.getGotmedal() == 0) {
			mCheckBox9.setChecked(false);
		} else if (mOldPushSetInfo.getGotmedal() == 1) {
			mCheckBox9.setChecked(true);
		}
		if (mOldPushSetInfo.getBuzz() == 0) {
			mCheckBox12.setChecked(false);
		} else if (mOldPushSetInfo.getBuzz() == 1) {
			mCheckBox12.setChecked(true);
		}

		OnCheckedChangeListener lis = new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton view, boolean isCkeck) {
				hasChanged = true;
				changed_view_id = view.getId();
				if (isCkeck) {
					// if (view.getId() ==
					// R.id.activity_manage_message_checkbox5) {
					// mRelativeLayout.setVisibility(View.VISIBLE);
					// }
					changed_value = 1;
				} else {
					// if (view.getId() ==
					// R.id.activity_manage_message_checkbox5) {
					// mRelativeLayout.setVisibility(View.GONE);
					// }
					changed_value = 0;
				}
				if (isNormal) {
					pushData();
				} else {
					isNormal = true;
				}
			}
		};

		mCheckBox1.setOnCheckedChangeListener(lis);
		mCheckBox2.setOnCheckedChangeListener(lis);
		mCheckBox4.setOnCheckedChangeListener(lis);
		mCheckBox5.setOnCheckedChangeListener(lis);
		mCheckBox6.setOnCheckedChangeListener(lis);
		mCheckBox7.setOnCheckedChangeListener(lis);
		mCheckBox9.setOnCheckedChangeListener(lis);
		mCheckBox12.setOnCheckedChangeListener(lis);
		super.LoadSuccess(data);
	}

	@Override
	protected void LoadErro(Object erro) {
		// TODO Auto-generated method stub
		super.LoadErro(erro);
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		// if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0)
		// {
		// // pushData();
		// return true;
		// }

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			return true;
		}
		return super.onKeyUp(keyCode, event);
	}

	private void pushData() {

		if (hasChanged) {
			String keyName = "";
			switch (changed_view_id) {
			case R.id.activity_manage_message_checkbox1:
				// 允许推送行车日报
				keyName = KEY_NAMES[0];
				break;

			case R.id.activity_manage_message_checkbox2:
				// 允许推送行车周报和月报
				keyName = KEY_NAMES[1];
				break;
			case R.id.activity_manage_message_checkbox4:
				// 允许推送车辆故障信息
				keyName = KEY_NAMES[2];
				break;
			// case R.id.activity_manage_message_checkbox5:
			// // 允许推送爱车振动
			// keyName = KEY_NAMES[3];
			// break;
			// case R.id.activity_manage_message_seekbar:
			// // 设置振动提醒强度
			// keyName = KEY_NAMES[4];
			// break;
			case R.id.activity_manage_message_checkbox6:
				// 允许推送车辆启动
				keyName = KEY_NAMES[5];
				break;
			case R.id.activity_manage_message_checkbox7:
				// 允许推送优惠券及活动
				keyName = KEY_NAMES[6];
				break;
			case R.id.activity_manage_message_checkbox9:
				// 允许推送获得勋章、升级驾驶证、创造新纪录
				keyName = KEY_NAMES[7];
				break;
			case R.id.activity_manage_message_checkbox12:
				// 允许蜂鸣器报警
				keyName = KEY_NAMES[8];
				break;
			}
			if (mDialog == null) {
				mDialog = PopBoxCreat.createDialogWithProgress(ManageMessageActivity.this, "加载中...");
			}
			mDialog.show();
			CPControl.GetUpdatePushSetResult(keyName, changed_value + "", listener_push);
		}
	}

	private GetResultListCallback listener_push = new GetResultListCallback() {

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
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				// 修改成功
				if (mDialog != null && mDialog.isShowing()) {
					mDialog.dismiss();
				}
				// UUToast.showUUToast(ManageMessageActivity.this, "设置成功！");
				switch (changed_view_id) {
				case R.id.activity_manage_message_checkbox1:
					// 允许推送行车日报
					mOldPushSetInfo.setDayreport(changed_value);
					break;

				case R.id.activity_manage_message_checkbox2:
					// 允许推送行车周报和月报
					mOldPushSetInfo.setWeekreport(changed_value);
					mOldPushSetInfo.setMonthreport(changed_value);
					break;
				// case R.id.activity_manage_message_checkbox3:
				//
				// break;
				case R.id.activity_manage_message_checkbox4:
					// 允许推送车辆故障信息
					mOldPushSetInfo.setTrouble(changed_value);
					break;
				// case R.id.activity_manage_message_checkbox5:
				// // 允许推送爱车振动
				// mOldPushSetInfo.setSecurity(changed_value);
				// break;
				// case R.id.activity_manage_message_seekbar:
				// // 设置振动提醒强度
				// mOldPushSetInfo.setVibstrength(changed_value);
				// break;
				case R.id.activity_manage_message_checkbox6:
					// 允许推送车辆启动
					mOldPushSetInfo.setStartup(changed_value);
					break;
				case R.id.activity_manage_message_checkbox7:
					// 允许推送优惠券及活动
					mOldPushSetInfo.setDealer(changed_value);
					break;
				case R.id.activity_manage_message_checkbox8:
					// 允许推送盒子失联
					mOldPushSetInfo.setLostconnect(changed_value);
					break;
				case R.id.activity_manage_message_checkbox9:
					// 允许推送获得勋章、升级驾驶证、创造新纪录
					mOldPushSetInfo.setGotmedal(changed_value);
					mOldPushSetInfo.setLicense(changed_value);
					mOldPushSetInfo.setNewrecord(changed_value);
					break;
				// case R.id.activity_manage_message_checkbox10:
				//
				// break;
				// case R.id.activity_manage_message_checkbox11:
				//
				// break;
				case R.id.activity_manage_message_checkbox12:
					// 允许蜂鸣器报警
					mOldPushSetInfo.setBuzz(changed_value);
					break;
				}
				break;

			case 1:
				// 修改失败
				if (mDialog != null && mDialog.isShowing()) {
					mDialog.dismiss();
				}
				BaseResponseInfo mInfo = (BaseResponseInfo) msg.obj;
				if (mInfo != null) {
					String s = mInfo.getInfo();
					if (s != null && s.length() > 0) {
						UUToast.showUUToast(ManageMessageActivity.this, s);
					} else {
						UUToast.showUUToast(ManageMessageActivity.this, "设置失败...");
					}
				} else {
					UUToast.showUUToast(ManageMessageActivity.this, "设置失败...");
				}

				int value;
				switch (changed_view_id) {
				case R.id.activity_manage_message_checkbox1:
					// 允许推送行车日报
					isNormal = false;
					value = mOldPushSetInfo.getDayreport();
					if (value == 1) {
						mCheckBox1.setChecked(true);
					} else if (value == 0) {
						mCheckBox1.setChecked(false);
					}
					break;

				case R.id.activity_manage_message_checkbox2:
					// 允许推送行车周报和月报
					isNormal = false;
					value = mOldPushSetInfo.getWeekreport();
					if (value == 1) {
						mCheckBox2.setChecked(true);
					} else if (value == 0) {
						mCheckBox2.setChecked(false);
					}
					break;
				// case R.id.activity_manage_message_checkbox3:
				//
				// break;
				case R.id.activity_manage_message_checkbox4:
					// 允许推送车辆故障信息
					isNormal = false;
					value = mOldPushSetInfo.getTrouble();
					if (value == 1) {
						mCheckBox4.setChecked(true);
					} else if (value == 0) {
						mCheckBox4.setChecked(false);
					}
					break;
				// case R.id.activity_manage_message_checkbox5:
				// // 允许推送爱车振动
				// isNormal = false;
				// value = mOldPushSetInfo.getSecurity();
				// if (value == 1) {
				// mCheckBox5.setChecked(true);
				// } else if (value == 0) {
				// mCheckBox5.setChecked(false);
				// }
				// break;
				// case R.id.activity_manage_message_seekbar:
				// // 设置振动提醒强度
				// value = mOldPushSetInfo.getVibstrength();
				// mSeekBar.setProgress((value - 1) * 50);
				// break;
				case R.id.activity_manage_message_checkbox6:
					// 允许推送车辆启动
					isNormal = false;
					value = mOldPushSetInfo.getStartup();
					if (value == 1) {
						mCheckBox6.setChecked(true);
					} else if (value == 0) {
						mCheckBox6.setChecked(false);
					}
					break;
				case R.id.activity_manage_message_checkbox7:
					// 允许推送优惠券及活动
					isNormal = false;
					value = mOldPushSetInfo.getDealer();
					if (value == 1) {
						mCheckBox7.setChecked(true);
					} else if (value == 0) {
						mCheckBox7.setChecked(false);
					}
					break;
				// case R.id.activity_manage_message_checkbox8:
				// // 允许推送盒子失联
				// break;
				case R.id.activity_manage_message_checkbox9:
					// 允许推送获得勋章、升级驾驶证、创造新纪录
					isNormal = false;
					value = mOldPushSetInfo.getGotmedal();
					if (value == 1) {
						mCheckBox9.setChecked(true);
					} else if (value == 0) {
						mCheckBox9.setChecked(false);
					}
					break;
				// case R.id.activity_manage_message_checkbox10:
				//
				// break;
				// case R.id.activity_manage_message_checkbox11:
				//
				// break;
				case R.id.activity_manage_message_checkbox12:
					// 允许蜂鸣器报警
					isNormal = false;
					value = mOldPushSetInfo.getBuzz();
					if (value == 1) {
						mCheckBox12.setChecked(true);
					} else if (value == 0) {
						mCheckBox12.setChecked(false);
					}
					break;
				}
				break;
			}
		}
	};
}

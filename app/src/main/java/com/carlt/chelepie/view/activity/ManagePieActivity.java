
package com.carlt.chelepie.view.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.carlt.chelepie.control.DeviceConnControl;
import com.carlt.chelepie.control.DeviceConnListener;
import com.carlt.chelepie.control.RecorderControl;
import com.carlt.chelepie.control.WIFIControl;
import com.carlt.chelepie.data.recorder.PieInfo;
import com.carlt.chelepie.view.UUDialogToast;
import com.carlt.doride.DorideApplication;
import com.carlt.doride.R;
import com.carlt.doride.base.LoadingActivity;
import com.carlt.doride.data.BaseResponseInfo;
import com.carlt.doride.data.UseInfo;
import com.carlt.doride.model.LoginInfo;
import com.carlt.doride.preference.UseInfoLocal;
import com.carlt.doride.protocolparser.BaseParser;
import com.carlt.doride.ui.view.PopBoxCreat;
import com.carlt.doride.ui.view.UUToast;
import com.carlt.doride.utils.StringUtils;


/**
 * 车乐拍设置
 * 
 * @author Administrator
 */
public class ManagePieActivity extends LoadingActivity implements OnClickListener, DeviceConnListener {
	private ImageView back;// 头部返回键

	private TextView title;// 标题文字

	private ImageView imgRight;// 头部右侧文字

	private View mViewName;

	private View mViewPsw;

	private View mViewDelete;

	private View mViewMode;

	private View mViewQuality;

	private View mViewStorage;

	private View mViewSettingTime;

	private View mViewImgconfig;// 设置图片亮度、对比度饱和度等信息

	private TextView mTxtName;// 摄像头名称

	private TextView mTxtPsw;// 车乐拍wifi连接密码

	private TextView mTxtDelete;// 删除摄像头（此功能暂无）

	private TextView mTxtMode;// 摄像模式(此功能暂无)

	private TextView mTxtQuality;// 图像质量

	private TextView mTxtStorage;// 存储空间管理

	private CheckBox mCbLink;// 拍照关联视频

	private CheckBox mCbSound;// 录像时录音

	private CheckBox mCbMic;// 麦克风增强(此功能暂无)

	private PieInfo mPieInfo;// 全部设置信息

	public final static int RECODE = 100;

	private Dialog mDialog;

	private String wifiName;// wifi名称

	private DeviceConnControl mConnControl;
	
	UUDialogToast mUuDialogToast;

	public static int doWhat = 0;//0,点击右上角的，1修改WIFI 名称密码后自动重连的
	int reConnTimes = 0;



	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manage_pie);
		initTitle("设备设置");
		init();
		mConnControl = new DeviceConnControl(this, this);
		LoadData();
	}


//	private void initTitle() {
//		back = (ImageView) findViewById(R.id.head_back_img1);
//		title = (TextView) findViewById(R.id.head_back_txt1);
//		imgRight = (ImageView) findViewById(R.id.head_back_img2);
//
//		back.setImageResource(R.drawable.arrow_back);
//		title.setText("设备设置");
//		imgRight.setVisibility(View.VISIBLE);
//
//		back.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				finish();
//			}
//		});
//
//		imgRight.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				if(!DeviceConnectManager.isDeviceConnect()){
//					mConnControl.goConnect();
//					doWhat = 0;
//					reConnTimes = 0;
//				}else{
//					UUToast.showUUToast(ManagePieActivity.this, "设备已连接");
//				}
//			}
//		});
//
//	}

	private void init() {
		mViewName = findViewById(R.id.manage_pie_lay_name);
		mViewPsw = findViewById(R.id.manage_pie_lay_psw);
		mViewDelete = findViewById(R.id.manage_pie_lay_delete);
		mViewMode = findViewById(R.id.manage_pie_lay_mode);
		mViewQuality = findViewById(R.id.manage_pie_lay_quality);
		mViewStorage = findViewById(R.id.manage_pie_lay_storage);
		mViewSettingTime = findViewById(R.id.manage_pie_setting_time);
		mViewImgconfig = findViewById(R.id.manage_pie_lay_imgconfig);

		mTxtName = (TextView) findViewById(R.id.manage_pie_txt_name);
		mTxtPsw = (TextView) findViewById(R.id.manage_pie_txt_psw);
		mTxtDelete = (TextView) findViewById(R.id.manage_pie_txt_delete);
		mTxtMode = (TextView) findViewById(R.id.manage_pie_txt_mode);
		mTxtQuality = (TextView) findViewById(R.id.manage_pie_txt_quality);
		mTxtStorage = (TextView) findViewById(R.id.manage_pie_txt_storage);

		mCbLink = (CheckBox) findViewById(R.id.manage_pie_cb_linked);
		mCbSound = (CheckBox) findViewById(R.id.manage_pie_cb_sound);
		mCbMic = (CheckBox) findViewById(R.id.manage_pie_cb_mic);

		mViewName.setOnClickListener(this);
		mViewPsw.setOnClickListener(this);
		mViewDelete.setOnClickListener(this);
		mViewMode.setOnClickListener(this);
		mViewQuality.setOnClickListener(this);
		mViewStorage.setOnClickListener(this);
		mViewSettingTime.setOnClickListener(this);
		mViewImgconfig.setOnClickListener(this);

		
		mCbSound.setOnClickListener(this);
		mCbLink.setOnClickListener(this);
	}

	@Override
	public void loadDataSuccess(Object data) {
		mPieInfo = PieInfo.getInstance();
		if (mPieInfo != null) {

			String s;
			// s = mPieInfo.getDeviceName();
//			s = LoginInfo.ssid;
			s = WIFIControl.getLocalWIFIName();
			if (s != null && s.length() > 0) {
				mTxtName.setText(s);
			}

			s = mPieInfo.getCameraMode();
			if (s != null && s.length() > 0) {
				mTxtMode.setText(s);
			}

			int resol = mPieInfo.getResolution();
			if (resol == PieInfo.SIZE_1080P) {
				mTxtQuality.setText("全高清（FHD）");
			} else {
				mTxtQuality.setText("高清（HD）");
			}

			if (mPieInfo.getRecodEnable() == 1) {
				mCbLink.setChecked(true);
			} else {
				mCbLink.setChecked(false);
			}

			if (mPieInfo.getAudioEnable() == 1) {
				mCbSound.setChecked(true);
			} else {
				mCbSound.setChecked(false);
			}
			if (mPieInfo.getRecodEnable() == 1) {
				mCbLink.setChecked(true);
			} else {
				mCbLink.setChecked(false);
			}

			if (mPieInfo.isMicEnhance()) {
				mCbMic.setChecked(true);
			} else {
				mCbMic.setChecked(false);
			}
		} else {
		}

		super.loadDataSuccess(data);
	}
	
	public void checkChange(){
		mPieInfo = PieInfo.getInstance();
		if (mPieInfo != null) {

			String s;
			// s = mPieInfo.getDeviceName();
			s = LoginInfo.ssid;
			if (s != null && s.length() > 0) {
				mTxtName.setText(s);
			}

			s = mPieInfo.getCameraMode();
			if (s != null && s.length() > 0) {
				mTxtMode.setText(s);
			}

			int resol = mPieInfo.getResolution();
			if (resol == PieInfo.SIZE_1080P) {
				mTxtQuality.setText("全高清（FHD）");
			} else {
				mTxtQuality.setText("高清（HD）");
			}

			if (mPieInfo.getRecodEnable() == 1) {
				mCbLink.setChecked(true);
			} else {
				mCbLink.setChecked(false);
			}

			if (mPieInfo.getAudioEnable() == 1) {
				mCbSound.setChecked(true);
			} else {
				mCbSound.setChecked(false);
			}

			if (mPieInfo.isMicEnhance()) {
				mCbMic.setChecked(true);
			} else {
				mCbMic.setChecked(false);
			}
		} 
	}


	@Override
	public void loadonErrorUI(BaseResponseInfo error) {
		super.loadonErrorUI(error);
	}



	protected void LoadData() {
		loadingDataUI();
		RecorderControl.getSysInfo(mCallback);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.manage_pie_lay_name:
			
			// 修改wifi名称
			final PopBoxCreat.DialogWithEditClick dialogWithEditClick1 = new PopBoxCreat.DialogWithEditClick() {

				@Override
				public void onRightClick(String editContent) {
					// 修改wifi名称
					String name = mTxtName.getText().toString();
					if(StringUtils.isEmpty(name)){
						name = WIFIControl.SSID_CONNECT ;
						if(StringUtils.isEmpty(name)){
							UseInfo uInfo = UseInfoLocal.getUseInfo();
							if (uInfo != null && !TextUtils.isEmpty(uInfo.getAccount())) {
								SharedPreferences sharp = DorideApplication.ApplicationContext.getSharedPreferences("wifi_info", 0);
								name = sharp.getString(uInfo.getAccount() + "_n", null);
							}
						}
					}
					String[] split = name.split("-");
					StringBuffer name2 = new StringBuffer();
					String strings = null;
					if(split.length>2){
						for (int i = 1; i < split.length; i++) {
							if(i>1){
								name2.append("-").append(split[i]);
							}else{
								name2.append(split[i]);
							}
						}
						strings = name2.toString();
					}else{
						strings = split[1];
					}
					if (editContent == null || editContent.length() <= 0) {
						UUToast.showUUToast(ManagePieActivity.this, "您还没有输入设备Wi-Fi名称哦...");
						return;
					} else if (editContent.equals(strings)) {
						UUToast.showUUToast(ManagePieActivity.this, "您还没有更改设备Wi-Fi名称哦...");
						return;
					} else {
						wifiName = editContent;
						if (mDialog == null) {
							mDialog = PopBoxCreat.createDialogWithProgress(ManagePieActivity.this, "提交中...");
						}
						mDialog.show();
						RecorderControl.EditPieName(editContent, listener_named);

					}

				}

				@Override
				public void onLeftClick(String editContent) {
					// TODO Auto-generated method stub

				}
			};
			
			PopBoxCreat.DialogWithEditClick dialogClick = new PopBoxCreat.DialogWithEditClick() {

				@Override
				public void onLeftClick(String editContent) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void onRightClick(String editContent) {
					UseInfo uInfo = UseInfoLocal.getUseInfo();
					String ssid_pwd = "";
					if (uInfo != null && !TextUtils.isEmpty(uInfo.getAccount())) {
						SharedPreferences sharp = DorideApplication.ApplicationContext.getSharedPreferences("wifi_info", 0);
						ssid_pwd = sharp.getString(uInfo.getAccount() + "_p", null);
					}
					if(TextUtils.equals(ssid_pwd, editContent.trim())){
						String s = mTxtName.getText().toString();
						PopBoxCreat.createDialogWithedit2(ManagePieActivity.this, "设备Wi-Fi命名", s, InputType.TYPE_CLASS_TEXT, "取消", "确认", dialogWithEditClick1).show();
					}else{
						UUToast.showUUToast(ManagePieActivity.this, "设备Wi-Fi密码错误");
					}
					
				}};
			String s1 = "设备Wi-Fi密码";
			PopBoxCreat.createDialogWithedit2(ManagePieActivity.this, "请您输入设备Wi-Fi密码", s1, InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD, "取消", "确认", dialogClick).show();

			break;
		case R.id.manage_pie_lay_psw:
			// 修改设备wifi密码

			PopBoxCreat.DialogWithEditClick2 dialogWithEditClick2 = new PopBoxCreat.DialogWithEditClick2() {

				@Override
				public void onLeftClick(String editContent) {
				}
				@Override
				public void onRightClick(String editContent,
                                         String editContent2, String editContent3) {
					UseInfo uInfo = UseInfoLocal.getUseInfo();
					String pwd = "";
					if (uInfo != null && !StringUtils.isEmpty(uInfo.getAccount())) {
						SharedPreferences sharp = DorideApplication.ApplicationContext.getSharedPreferences("wifi_info", 0);
						pwd = sharp.getString(uInfo.getAccount() + "_p", "");
					}
					// 修改密码
					 if (!TextUtils.equals(pwd, editContent)) {
						UUToast.showUUToast(ManagePieActivity.this, "原密码输入错误");
						return;
					} else if (editContent2 == null || editContent2.length() <= 0 || editContent3 == null || editContent3.length() <= 0) {
						UUToast.showUUToast(ManagePieActivity.this, "输入信息不能为空");
						return;
					} else if (editContent2.length() < 8 || editContent3.length() < 8) {
						UUToast.showUUToast(ManagePieActivity.this, "密码长度不能小于8位");
						return;
					}else if (TextUtils.equals(pwd, editContent2)) {
						UUToast.showUUToast(ManagePieActivity.this, "新密码不能与旧密码一样");
						return;
					}else if (!TextUtils.equals(editContent2, editContent3)) {
						UUToast.showUUToast(ManagePieActivity.this, "两次输入的新密码不一致");
						return;
					}  else if (editContent2.matches(".*[\\u4e00-\\u9faf].*") || editContent3.matches(".*[\\u4e00-\\u9faf].*") ) {
						UUToast.showUUToast(ManagePieActivity.this, "密码不能包含中文");
						return;
					} else {
						if (mDialog == null) {
							mDialog = PopBoxCreat.createDialogWithProgress(ManagePieActivity.this, "提交中...");
						}
						mDialog.show();
						RecorderControl.EditPiePassword(editContent2, listener_psw);
					}
				}
			};
			int inputType =  InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD;
			PopBoxCreat.createDialogWithedit3(ManagePieActivity.this, "设置设备Wi-Fi密码", "请输入原密码",	"请输入新密码","再次确认密码", 
					inputType, "取消", "确认", dialogWithEditClick2).show();

			break;
		case R.id.manage_pie_lay_delete:
			// 删除设备（此功能暂无）
			break;
		case R.id.manage_pie_lay_mode:
			// 设置摄像模式（此功能暂无）
			break;
		case R.id.manage_pie_lay_quality:
			// 设置图像质量
			Intent mIntent5 = new Intent(ManagePieActivity.this, VideoQualityActivity.class);
			int quality = mPieInfo.getResolution();
			int index = 0;
			if (quality == PieInfo.SIZE_1080P) {
				index = 0;
			} else if (quality == PieInfo.SIZE_720P) {
				index = 1;
			}
			mIntent5.putExtra(VideoQualityActivity.INDEX, index);
			startActivityForResult(mIntent5, RECODE + 3);
			break;

		case R.id.manage_pie_lay_imgconfig:
			break;
		case R.id.manage_pie_lay_storage:
			// 存储空间管理
			Intent mIntent6 = new Intent(ManagePieActivity.this, ManageStorageActivity.class);
			startActivity(mIntent6);
			break;
		case R.id.manage_pie_setting_time:
			// 设置记录仪时间
			Intent mIntent7 = new Intent(ManagePieActivity.this, ManageTimesActivity.class);
			startActivity(mIntent7);
			break;
		case R.id.manage_pie_cb_sound:
			// 设置打开关闭音频
			RecorderControl.setAudio(listener_stream_sound);
			mCbSound.setClickable(false);
			break;
		case R.id.manage_pie_cb_linked:
			// 抓拍关联视频
			RecorderControl.setCaptureRecordVideo(listener_capture);
			mCbLink.setClickable(false);
			break;
		}
	}

	// 摄像头命名
	private BaseParser.ResultCallback listener_named = new BaseParser.ResultCallback() {

		@Override
		public void onSuccess(BaseResponseInfo bInfo) {
			Message msg = new Message();
			msg.what = 6;
			mHandler.sendMessage(msg);
		}

		@Override
		public void onError(BaseResponseInfo bInfo) {
			Message msg = new Message();
			msg.what = 7;
			msg.obj = bInfo;
			mHandler.sendMessage(msg);
		}
	};

	// 设置摄像头密码
	private BaseParser.ResultCallback listener_psw = new BaseParser.ResultCallback() {

		@Override
		public void onSuccess(BaseResponseInfo bInfo) {
			Message msg = new Message();
			msg.what = 8;
			mHandler.sendMessage(msg);
		}

		@Override
		public void onError(BaseResponseInfo bInfo) {
			Message msg = new Message();
			msg.what = 9;
			msg.obj = bInfo;
			mHandler.sendMessage(msg);
		}
	};

	// 录像时录音
	private BaseParser.ResultCallback listener_stream_sound = new BaseParser.ResultCallback() {

		@Override
		public void onSuccess(BaseResponseInfo bInfo) {
			mHandler.sendEmptyMessage(2);
		}

		@Override
		public void onError(BaseResponseInfo bInfo) {
			mHandler.sendEmptyMessage(3);
		}
	};
	
	// 抓拍关联视频
	private BaseParser.ResultCallback listener_capture = new BaseParser.ResultCallback() {

		@Override
		public void onSuccess(BaseResponseInfo bInfo) {
			mHandler.sendEmptyMessage(4);
		}

		@Override
		public void onError(BaseResponseInfo bInfo) {
			mHandler.sendEmptyMessage(5);
		}
	};


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case RECODE + 1:
				// 名字
				if (data != null) {
					String s = data.getStringExtra(EditPienameActivity.NAME);
					if (s != null && s.length() > 0) {
						mTxtName.setText(s);
					}
				}
				break;

			case RECODE + 2:
				// 摄像模式
				break;
			case RECODE + 3:
				// 图像质量
				break;
			}
		}
	}

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 2:
				// 录像时录音成功
				if (mPieInfo.getAudioEnable() == 1) {
					UUToast.showUUToast(ManagePieActivity.this, "设置录像时录音成功！");
					mCbSound.setChecked(true);
				} else {
					UUToast.showUUToast(ManagePieActivity.this, "设置录像时静音成功！");
					mCbSound.setChecked(false);
				}
				mCbSound.setClickable(true);
				break;
			case 3:
				// 录像时录音失败
				if (mPieInfo.getAudioEnable() == 0) {
					UUToast.showUUToast(ManagePieActivity.this, "设置录像时录音失败");
					mCbSound.setChecked(false);
				} else {
					UUToast.showUUToast(ManagePieActivity.this, "设置录像时静音失败！");
					mCbSound.setChecked(true);
				}
				mCbSound.setClickable(true);
				break;
			case 4:
				// 抓拍关联视频 成功
				if (mPieInfo.getRecodEnable() == 1) {
					UUToast.showUUToast(ManagePieActivity.this, "设置抓拍关联视频成功！");
					mCbLink.setChecked(true);
				} else {
					UUToast.showUUToast(ManagePieActivity.this, "设置取消抓拍关联视频成功！");
					mCbLink.setChecked(false);
				}
				mCbLink.setClickable(true);
				break;
			case 5:
				// 抓拍关联视频失败
				if (mPieInfo.getRecodEnable() == 0) {
					UUToast.showUUToast(ManagePieActivity.this, "设置抓拍关联视频失败");
					mCbLink.setChecked(false);
				} else {
					UUToast.showUUToast(ManagePieActivity.this, "设置取消抓拍关联视频失败！");
					mCbLink.setChecked(true);
				}
				mCbLink.setClickable(true);
				break;
			case 6:
				// 修改wifi名称成功
				if (mDialog != null) {
					mDialog.dismiss();
				}
				mTxtName.setText(WIFIControl.getLocalWIFIName());
				PopBoxCreat.createDialogWithTitle(ManagePieActivity.this, "设备Wi-Fi命名", "设备Wi-Fi命名成功。请重新连接！", "", "确定", "", new PopBoxCreat.DialogWithTitleClick() {
					@Override
					public void onRightClick() {
					}
					
					@Override
					public void onLeftClick() {
					}
				});
				//TODO 断开重连车乐拍
				WIFIControl.DisConnectChelePai();
				LoadData();
//				restartWifi(cname);
				break;
			case 7:
				// 修改wifi名称失败
				if (mDialog != null) {
					mDialog.dismiss();
				}
				UUToast.showUUToast(ManagePieActivity.this, "设备Wi-Fi命名失败...");
				break;
			case 8:
				// 设置wifi密码成功
				if (mDialog != null) {
					mDialog.dismiss();
				}
				PopBoxCreat.createDialogWithTitle(ManagePieActivity.this, "设置设备Wi-Fi密码", "设置设备Wi-Fi密码成功。请重新连接！", "", "确定", "", new PopBoxCreat.DialogWithTitleClick() {
					@Override
					public void onRightClick() {
					}
					
					@Override
					public void onLeftClick() {
					}
				});
				//TODO 断开重连车乐拍
				WIFIControl.DisConnectChelePai();
				LoadData();
				break;
			case 9:
				// 设置wifi密码失败
				if (mDialog != null) {
					mDialog.dismiss();
				}
				UUToast.showUUToast(ManagePieActivity.this, "设置Wi-Fi密码失败...");
				break;
			}
		}
	};


	@Override
	protected void onResume() {
		super.onResume();
		mTxtName.setText(WIFIControl.getLocalWIFIName());
		mConnControl.onResume();
		checkChange();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mConnControl.onDestory();
	}

	@Override
	public void connOk() {
		LoadData();
	}

	@Override
	public void connError() {
		
		if(doWhat == 0){
			BaseResponseInfo bInfo = new BaseResponseInfo();
			bInfo.setFlag(BaseResponseInfo.ERRO);
			bInfo.setInfo("未能连接到设备,请重新连接");
			loadonErrorUI(bInfo);
		}else{
			if(reConnTimes < 2){
				reConnTimes ++;
				mHandler.postDelayed(new Runnable() {
					
					@Override
					public void run() {
						mConnControl.goConnect();
					}
				}, 1500);
			}else{
				reConnTimes = 0;
				doWhat = 0;
				BaseResponseInfo bInfo = new BaseResponseInfo();
				bInfo.setFlag(BaseResponseInfo.ERRO);
				bInfo.setInfo("未能连接到设备,请重新连接");
				loadonErrorUI(bInfo);
			}
		}
	}
}

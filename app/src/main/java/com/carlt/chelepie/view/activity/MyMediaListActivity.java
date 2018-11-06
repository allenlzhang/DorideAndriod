package com.carlt.chelepie.view.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.carlt.chelepie.control.DeviceConnControl;
import com.carlt.chelepie.control.DeviceConnListener;
import com.carlt.chelepie.control.RecorderControl;
import com.carlt.chelepie.control.WIFIControl;
import com.carlt.chelepie.data.recorder.PieDownloadInfo;
import com.carlt.chelepie.data.recorder.PieDownloadListInfo;
import com.carlt.chelepie.manager.DeviceConnectManager;
import com.carlt.chelepie.view.EditDialog2;
import com.carlt.chelepie.view.WIFIConnectDialog;
import com.carlt.chelepie.view.adapter.MediaViewAdapter;
import com.carlt.chelepie.view.fragment.MediaAllFragment;
import com.carlt.doride.DorideApplication;
import com.carlt.doride.R;
import com.carlt.doride.base.BaseFragment;
import com.carlt.doride.data.BaseResponseInfo;
import com.carlt.doride.eventbus.FullScreenMessage;
import com.carlt.doride.protocolparser.BaseParser;
import com.carlt.doride.ui.view.PopBoxCreat;
import com.carlt.doride.ui.view.UUToast;
import com.carlt.sesame.control.CPControl;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.carlt.chelepie.view.activity.FullLiveActivity.BACK_CODE;

/**
 * 记录仪相册
 * 
 * @author Daisy
 * 
 */
public class MyMediaListActivity extends FragmentActivity implements OnClickListener, DeviceConnListener {
	private ImageView back;// 头部返回键
	private TextView title;// 标题文字
	private TextView txtRight;// 头部右侧文字

	private View mViewLoading;// 加载layout
	private View mViewError;// 加载layout
	private ImageView mImgError;// 加载错误图片
	private TextView mTxtError;// 错误提示
	private TextView mTxtRetry;// 重试按钮

	private TextView mTxtAll;// 全部
	private TextView mTxtCapture;// 抓拍
	private TextView mTxtEvent;// 事件
	private TextView[] tabs;

	private View mUnderLine;// 滚动的下划线
	private ViewPager mViewPager;//

	private LinearLayout nodataLl;  // 沒有数据页面
	private int current_index = 0;

	private ArrayList<BaseFragment> fragments;

	private MediaViewAdapter mAdapter;

	/**
	 * 下划线View宽度
	 */
	private int lineWidth;

	/**
	 * 选项卡总数
	 */
	private static final int TAB_COUNT = 3;

	private static final int TAB_0 = 0;

	private static final int TAB_1 = 1;

	private static final int TAB_2 = 2;

	// wifi连接对话框
	private WIFIConnectDialog mWIFIDialog;
	private EditDialog2 mDialog;

	private DeviceConnControl mConnControl;
	public static int doWhat = 0;// 0,点击右上角的，1修改WIFI 名称密码后自动重连的
	int reConnTimes = 0;
	
	boolean isDataFrushing = false;//正在刷新中

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_medialist);
		mConnControl = new DeviceConnControl(this, this);
		initTitle();
		initOther();
		init();
	}

	private void initTitle() {
		back = (ImageView) findViewById(R.id.head_back_img1);
		title = (TextView) findViewById(R.id.head_back_txt1);
		txtRight = (TextView) findViewById(R.id.head_back_txt2);
		View titleline = findViewById(R.id.main_page_head_line);

		back.setImageResource(R.drawable.arrow_back);
		title.setText("相册");
		txtRight.setVisibility(View.VISIBLE);
		txtRight.setTextColor(getResources().getColor(R.color.blue_light));
		txtRight.setText("已下载");
		titleline.setVisibility(View.GONE);

		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				EventBus.getDefault().post(new FullScreenMessage(BACK_CODE));
				finish();
			}
		});
//已下载
		txtRight.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// 跳转至已下载列表
				Intent mIntent = new Intent(MyMediaListActivity.this,
						HasDownListActivity.class);
				startActivity(mIntent);
			}
		});
	}

	private void initOther() {
		mViewLoading = findViewById(R.id.medialist_lay_loading);
		mViewError = findViewById(R.id.medialist_lay_error);
		mImgError = (ImageView) findViewById(R.id.medialist_img_error);
		mTxtError = (TextView) findViewById(R.id.medialist_txt_errorinfo);
		mTxtRetry = (TextView) findViewById(R.id.medialist_txt_retryinfo);

		mTxtRetry.setOnClickListener(this);
	}

	private void init() {
		mTxtAll = (TextView) findViewById(R.id.medialist_txt_all);
		mTxtCapture = (TextView) findViewById(R.id.medialist_txt_capture);
		mTxtEvent = (TextView) findViewById(R.id.medialist_txt_event);
		mUnderLine = findViewById(R.id.medialist_view_underline);
		mViewPager = (ViewPager) findViewById(R.id.medialist_vpager);

		lineWidth = DorideApplication.ScreenWith / TAB_COUNT;

		initViewpager();
		mTxtAll.setOnClickListener(this);
		mTxtCapture.setOnClickListener(this);
		mTxtEvent.setOnClickListener(this);
		if(DeviceConnectManager.isDeviceConnect()){
			connOk();
		}else{
			BaseResponseInfo mInfo=new BaseResponseInfo();
			mInfo.setFlag(0);
			mInfo.setInfo("未连接记录仪Wi-Fi");
			LoadErroUI(mInfo);
		}
	}

	private void initViewpager() {
		fragments = new ArrayList<BaseFragment>();
		fragments.add(new MediaAllFragment());
		fragments.add(new MediaAllFragment());
		fragments.add(new MediaAllFragment());
		mAdapter = new MediaViewAdapter(getSupportFragmentManager(), fragments);
		mViewPager.setAdapter(mAdapter);
		mViewPager.setOffscreenPageLimit(2);
	
		tabs = new TextView[] { mTxtAll, mTxtCapture, mTxtEvent };
		mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
					int one = lineWidth;

					@Override
					public void onPageScrolled(int position,
							float positionOffset, int positionOffsetPixels) {
					}

					@Override
					public void onPageSelected(int position) {
						// 下划线开始移动前的位置
						float fromX = one * current_index;
						// 下划线移动完毕后的位置
						float toX = one * position;
						Animation animation = new TranslateAnimation(fromX,
								toX, 0, 0);
						animation.setFillAfter(true);
						animation.setDuration(500);
						// 给图片添加动画
						mUnderLine.startAnimation(animation);
						// 当前Tab的字体变成红色
						tabs[position].setTextColor(Color.parseColor("#3EC0EA"));
						tabs[current_index].setTextColor(Color
								.parseColor("#999999"));
						current_index = position;
					//	Logger.e(current_index + "==========================");
						//((MediaAllFragment)fragments.get(current_index)).notifyAdapter();
					}

					@Override
					public void onPageScrollStateChanged(int state) {
					}
				});
		
		mViewPager.setCurrentItem(0);
	}

	@Override
	protected void onResume() {
		super.onResume();
		mConnControl.onResume();
		for (int i = 0; i < fragments.size(); i++) {
			((MediaAllFragment)fragments.get(i)).notifyAdapter();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mConnControl.onDestory();
		dissmissConnectDialog();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.medialist_txt_all:
			mViewPager.setCurrentItem(TAB_0);
			break;
		case R.id.medialist_txt_capture:
			mViewPager.setCurrentItem(TAB_1);
			break;
		case R.id.medialist_txt_event:
			mViewPager.setCurrentItem(TAB_2);
			break;
		case R.id.medialist_txt_retryinfo:

			if(!DeviceConnectManager.isDeviceConnect()){
				mConnControl.goConnect();
				doWhat = 0;
				reConnTimes = 0;
			}else{
				UUToast.showUUToast(MyMediaListActivity.this, "设备已连接");
			}
		
			break;
		}

	}

	@Override
	public void connOk() {
		//开始加载数据
		LoadDataUI();
		isDataFrushing = true;
		RecorderControl.getCaptureFilelist(listener1, null, false);
		mViewPager.setCurrentItem(TAB_0);
		
		//缩略图下载完成后，刷新页面
		((MediaAllFragment)fragments.get(0)).setOnPicMediaLisenter(new MediaAllFragment.OnPicMediaLisenter() {
			@Override
			public void onRefreshing() {
				if(!isDataFrushing){
					isDataFrushing = true;
					RecorderControl.getCaptureFilelist(listener1, null, false);
				}
			}
			@Override
			public void onPicDownLoadFinished() {
				for (int j = 0; j < fragments.size(); j++) {
					((MediaAllFragment)fragments.get(j)).notifyAdapter();
				}
			}
		});
		((MediaAllFragment)fragments.get(1)).setOnPicMediaLisenter(new MediaAllFragment.OnPicMediaLisenter() {

			@Override
			public void onRefreshing() {
				if(!isDataFrushing){
					isDataFrushing = true;
					RecorderControl.getCaptureFilelist(listener1, null, false);
				}
			}
			@Override
			public void onPicDownLoadFinished() {

			}
		});
		((MediaAllFragment)fragments.get(2)).setOnPicMediaLisenter(new MediaAllFragment.OnPicMediaLisenter() {

			@Override
			public void onRefreshing() {
				if(!isDataFrushing){
					isDataFrushing = true;
					RecorderControl.getCaptureFilelist(listener1, null, false);
				}
			}
			@Override
			public void onPicDownLoadFinished() {}
		});
	}

 	public BaseParser.ResultCallback listener1 = new BaseParser.ResultCallback() {

		@Override
		public void onSuccess(BaseResponseInfo o) {
			Message msg = new Message();
			msg.obj = o;
			msg.what = 1 ;
			mHandler.sendMessage(msg);
		}

		@Override
		public void onError(BaseResponseInfo o) {
			Message msg = new Message();
			msg.obj = o;
			msg.what = 2 ;
			mHandler.sendMessage(msg);
		}
	};
	BaseParser.ResultCallback listener2 = new BaseParser.ResultCallback() {

		@Override
		public void onSuccess(BaseResponseInfo o) {
			Message msg = new Message();
			msg.obj = o;
			msg.what = 3 ;
			mHandler.sendMessage(msg);
		}

		@Override
		public void onError(BaseResponseInfo o) {
			Message msg = new Message();
			msg.obj = o;
			msg.what = 4 ;
			mHandler.sendMessage(msg);
		}
	};
	
	@Override
	public void connError() {
		if (doWhat == 0) {
			BaseResponseInfo bInfo = new BaseResponseInfo();
			bInfo.setFlag(BaseResponseInfo.ERRO);
			bInfo.setInfo("未连接摄像头Wi-Fi");
			LoadErroUI(bInfo);
		} else {
			if (reConnTimes < 2) {
				reConnTimes++;
				mHandler.postDelayed(new Runnable() {

					@Override
					public void run() {
						mConnControl.goConnect();
					}
				}, 1500);
			} else {
				reConnTimes = 0;
				doWhat = 0;
				BaseResponseInfo bInfo = new BaseResponseInfo();
				bInfo.setFlag(BaseResponseInfo.ERRO);
				bInfo.setInfo("未连接摄像头Wi-Fi");
				LoadErroUI(bInfo);
			}
		}

	}


	public void showInputPwd() {
		if (mDialog != null) {
			mDialog.dismiss();
		}

		mDialog = PopBoxCreat.createDialogWithedit2(MyMediaListActivity.this,
				WIFIControl.SSID_CONNECT, "请输入", InputType.TYPE_CLASS_TEXT
						| InputType.TYPE_TEXT_VARIATION_PASSWORD, "取消", "确定",
				new PopBoxCreat.DialogWithEditClick() {

					@Override
					public void onRightClick(String editContent) {
						if (editContent != null && editContent.length() > 7) {
							WIFIControl.SSID_PWD = editContent;
							showConnectDialog();
							WIFIControl.StartConnectChelePai();
						} else {
							UUToast.showUUToast(MyMediaListActivity.this,
									"设备密码错误，请重新输入密码");
							showInputPwd();
						}
					}

					@Override
					public void onLeftClick(String editContent) {
						WIFIControl.SSID_CONNECT = "";
						WIFIControl.SSID_PWD = "";
					}
				});
		mDialog.show();
	}

	private PopBoxCreat.DialogWithEditClick mDialogWithEditClick = new PopBoxCreat.DialogWithEditClick() {

		@Override
		public void onRightClick(String editContent) {
			// 确定-连接车乐拍wifi--TODO
			// 连接wifi的代码还没有写
			if (WIFIControl.SSID_CONNECT.length() < 1
					|| WIFIControl.SSID_PWD.length() < 8) {
				WIFIControl.SSID_PWD = "";
				UUToast.showUUToast(MyMediaListActivity.this, "设备密码错误，请重新输入密码");
				showInputPwd();
				return;
			}
			showConnectDialog();
			WIFIControl.StartConnectChelePai();
		}

		@Override
		public void onLeftClick(String editContent) {
			// 取消
			WIFIControl.SSID_CONNECT = "";
			WIFIControl.SSID_PWD = "";
		}
	};

	private void dissmissConnectDialog() {
		if (mWIFIDialog != null) {
			mWIFIDialog.dismiss();
		}
	}

	private void showConnectDialog() {
		dissmissConnectDialog();
		mWIFIDialog = new WIFIConnectDialog(this);
		mWIFIDialog.show();
	}

	List<PieDownloadInfo> allInfoList = new ArrayList<PieDownloadInfo>();
	List<PieDownloadInfo> capInfoList = new ArrayList<PieDownloadInfo>();
	List<PieDownloadInfo> eventInfoList = new ArrayList<PieDownloadInfo>();
	
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				allInfoList.clear();
				capInfoList.clear();
				PieDownloadListInfo infoList1 =  (PieDownloadListInfo) msg.obj;

				for (int i = 0; i < infoList1.getArrays().size(); i++) {
					allInfoList.add(infoList1.getArrays().get(i));
					capInfoList.add(infoList1.getArrays().get(i));
				}
				LoadSuccessUI();
				Collections.sort(allInfoList);
				Collections.sort(capInfoList);
//				fragments.get(0).loadSuccessUI();
//				fragments.get(1).loadSuccessUI();
                if(fragments.get(0).isVisible()){
					((MediaAllFragment)fragments.get(0)).loadSuccess(allInfoList);
                }
                if(fragments.get(1).isVisible()){
					((MediaAllFragment)fragments.get(1)).loadSuccess(capInfoList);

                }
				RecorderControl.getEventFilelist(listener2, null, false);
				break;
			case 2:
//				LoadErroUI(msg.obj);
				RecorderControl.getEventFilelist(listener2, null, false);
				break;
			case 3:
				isDataFrushing = false;
				eventInfoList.clear();
				PieDownloadListInfo infoList2 =  (PieDownloadListInfo) msg.obj;
				for (int i = 0; i < infoList2.getArrays().size(); i++) {
					allInfoList.add(infoList2.getArrays().get(i));
					eventInfoList.add(infoList2.getArrays().get(i));
				}
				LoadSuccessUI();
				Collections.sort(allInfoList);
				Collections.sort(eventInfoList);
				((MediaAllFragment)fragments.get(0)).downloadThumbnail(allInfoList);

				if(fragments.get(2).isVisible()){

					((MediaAllFragment)fragments.get(2)).loadSuccess(eventInfoList);

				}
				break;
			case 4:
				isDataFrushing = false;
				LoadErroUI(msg.obj);
				break;

			default:
				break;
			}
		}
	};
	protected void LoadDataUI() {
		mViewLoading.setVisibility(View.VISIBLE);
		mViewError.setVisibility(View.GONE);

	}
	protected void LoadSuccessUI() {
		mViewLoading.setVisibility(View.GONE);
		mViewError.setVisibility(View.GONE);

	}
	protected void LoadErroUI(Object erro) {
		mViewLoading.setVisibility(View.GONE);
		mViewError.setVisibility(View.VISIBLE);
		if (erro != null) {
			BaseResponseInfo mInfo = (BaseResponseInfo) erro;
			String info = mInfo.getInfo();
			mTxtError.setText(info);
			if (mInfo.getFlag() == 0) {
				// 记录仪wifi未连接
				mImgError.setImageResource(R.drawable.connect_wifi_tip);
				mTxtRetry.setText("连接记录仪Wi-Fi");
			}
		}
	}

}

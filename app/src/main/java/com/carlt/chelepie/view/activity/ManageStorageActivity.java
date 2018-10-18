
package com.carlt.chelepie.view.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;


import com.carlt.chelepie.control.DeviceConnControl;
import com.carlt.chelepie.control.DeviceConnListener;
import com.carlt.chelepie.control.RecorderControl;
import com.carlt.chelepie.data.recorder.PieInfo;
import com.carlt.chelepie.manager.DeviceConnectManager;
import com.carlt.chelepie.protocolstack.recorder.RecorderFormatSDParser;
import com.carlt.doride.R;
import com.carlt.doride.base.LoadingActivity;
import com.carlt.doride.data.BaseResponseInfo;
import com.carlt.doride.protocolparser.BaseParser;
import com.carlt.doride.ui.view.PopBoxCreat;
import com.carlt.doride.ui.view.UUDialog;
import com.carlt.doride.ui.view.UUToast;
import com.carlt.doride.utils.FileUtil;
import com.carlt.doride.utils.LocalConfig;

import java.io.File;
import java.text.DecimalFormat;

/**
 * 存储空间管理
 * 
 * @author Administrator
 */
public class ManageStorageActivity extends LoadingActivity implements OnClickListener, DeviceConnListener {
	private ImageView back;// 头部返回键

	private TextView title;// 标题文字

	private ImageView imgRight;// 头部右侧文字

	private TextView mTxtHasuseBox;// 车乐拍（摄像头）已用空间

	private TextView mTxtUnuseBox;// 车乐拍（摄像头）可用空间

	private TextView mTxtHasuseLocal;// 手机已用空间

	private TextView mTxtUnuseLocal;// 手机可用空间

	private TextView mTxtFormat;// 格式化存储卡

	private TextView mTxtEmpty;// 清空应用缓存

	private Dialog mDialog;

	DeviceConnControl mConnControl;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manage_storage);
		mConnControl = new DeviceConnControl(this, this);
		initTitle();
		init();
		LoadData();
	}

	private void initTitle() {
		back = (ImageView) findViewById(R.id.head_back_img1);
		title = (TextView) findViewById(R.id.head_back_txt1);
		imgRight = (ImageView) findViewById(R.id.head_back_img2);

		back.setImageResource(R.drawable.arrow_back);
		title.setText("存储空间管理");

		imgRight.setVisibility(View.VISIBLE);

		imgRight.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!DeviceConnectManager.isDeviceConnect()) {
					mConnControl.goConnect();
				} else {
					UUToast.showUUToast(ManageStorageActivity.this, "设备已连接");
				}
			}
		});

		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

	}

	private void init() {
		mTxtHasuseBox = (TextView) findViewById(R.id.manage_storage_txt_hasuse_box);
		mTxtUnuseBox = (TextView) findViewById(R.id.manage_storage_txt_unuse_box);
		mTxtHasuseLocal = (TextView) findViewById(R.id.manage_storage_txt_hasuse_local);
		mTxtUnuseLocal = (TextView) findViewById(R.id.manage_storage_txt_unuse_local);

		mTxtFormat = (TextView) findViewById(R.id.manage_storage_txt_format);
		mTxtEmpty = (TextView) findViewById(R.id.manage_storage_txt_empty);

		mTxtFormat.setOnClickListener(this);
		mTxtEmpty.setOnClickListener(this);

	}

	@Override
	public void loadDataSuccess(Object data) {
		PieInfo mInfo = PieInfo.getInstance();
		if (mInfo != null) {
			String srem = "";
			String suse = "";
			int total = mInfo.getTotalSpace();
			int remain = mInfo.getRemainSpace();
			int use = total - remain;
			if (remain <= 0) {
				use = 0;
				remain = total;
			}
			suse = use + "MB";
			srem = remain + "MB";
			DecimalFormat decimalFormat = new DecimalFormat("0.00");
			if (remain > 500) {
				srem = decimalFormat.format(remain / 1024.0) + "G";
			}

			if (use > 500) {
				suse = decimalFormat.format(use / 1024.0) + "G";
			}

			mTxtHasuseBox.setText("已用空间:" + suse);
			mTxtUnuseBox.setText("可用空间:" + srem);
		}

		long total = Environment.getExternalStorageDirectory().getTotalSpace();
		long free = Environment.getExternalStorageDirectory().getFreeSpace();
		DecimalFormat decimalFormat = new DecimalFormat("0.00");
		float fuse = (float) (total - free) / 1024 / 1024;
		float ffree = (float) free / 1024 / 1024;
		String suse = decimalFormat.format(fuse) + "MB";
		String srem = decimalFormat.format(ffree) + "MB";

		if (ffree > 500) {
			srem = decimalFormat.format(ffree / 1024.0) + "G";
		}

		if (fuse > 500) {
			suse = decimalFormat.format(fuse / 1024.0) + "G";
		}

		mTxtHasuseLocal.setText("已用空间:" + suse);
		mTxtUnuseLocal.setText("可用空间:" + srem);
		super.loadDataSuccess(data);
	}


	@Override
	public void loadonErrorUI(BaseResponseInfo error) {
		super.loadonErrorUI(error);
	}

	protected void LoadData() {
		RecorderControl.getRecorderSD(mCallback);
	}

	PopBoxCreat.DialogWithTitleClick click = new PopBoxCreat.DialogWithTitleClick() {

		@Override
		public void onRightClick() {
			// 取消

		}

		@Override
		public void onLeftClick() {
			// 确定
			if (mDialog == null) {
				mDialog = PopBoxCreat.createDialogWithProgress(ManageStorageActivity.this, "格式化...");
			}
			((UUDialog)mDialog).setContentText("格式化...");
			mDialog.show();
			RecorderFormatSDParser.JinduResultListCallback jinduListener = new RecorderFormatSDParser.JinduResultListCallback() {
				@Override
				public void onFinished(final int o) {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							((UUDialog)mDialog).setContentText("格式化"+ o +"%");
						}
					});
				}
			};
			RecorderControl.formatRecorderSD(mListener, jinduListener);

		}
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.manage_storage_txt_format:
			// 格式化车乐拍
			PopBoxCreat.createDialogWithTitle(ManageStorageActivity.this, "提示", "您确定要格式化数据么？", "", "确定", "取消", click);
			break;

		case R.id.manage_storage_txt_empty:
			// 清空本地缓存
			new Thread(new Runnable() {

				@Override
				public void run() {
					File f1 = new File(LocalConfig.mErroLogSavePath_SD);
					if (f1.exists()) {
						File[] fs = f1.listFiles();
						for (int i = 0; i < fs.length; i++) {
							fs[i].delete();
						}
					}

					File f2 = new File(LocalConfig.mImageCacheSavePath_SD);
					if (f2.exists()) {
						File[] fs = f2.listFiles();
						for (int i = 0; i < fs.length; i++) {
							fs[i].delete();
						}
					}

					File f3 = new File(LocalConfig.mImageCacheSavePath_Absolute);
					if (f3.exists()) {
						File[] fs = f3.listFiles();
						for (int i = 0; i < fs.length; i++) {
							fs[i].delete();
						}
					}

					File f4 = new File(LocalConfig.mDownLoadFileSavePath_Absolute);
					if (f4.exists()) {
						File[] fs = f4.listFiles();
						for (int i = 0; i < fs.length; i++) {
							fs[i].delete();
						}
					}
//					File f5 = new File(LocalConfig.mMediaFileSavePath_SD);
//					if (f5.exists()) {
//						deleteDir(f5);
//					}
//					File f6 = new File(LocalConfig.mMediaFileSavePath_Absolute);
//					if (f6.exists()) {
//						deleteDir(f6);
//					}
					FileUtil.deleteTemp(LocalConfig.mMediaFileSavePath_SD,0);

					mHandler.sendEmptyMessage(11);
				}
			}).start();
			break;
		}

	}

    private  boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            //递归删除目录中的子目录下
            for (int i=0; i<children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }
	
	private BaseParser.ResultCallback mListener = new BaseParser.ResultCallback() {

		@Override
		public void onSuccess(BaseResponseInfo bInfo) {
			Message msg = new Message();
			msg.what = 0;
			mHandler.sendMessage(msg);
		}

		@Override
		public void onError(BaseResponseInfo o) {
			Message msg = new Message();
			msg.what = 1;
			msg.obj = o;
			mHandler.sendMessage(msg);
		}

	};

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				if (mDialog != null) {
					mDialog.dismiss();
				}
//				UUToast.showUUToast(ManageStorageActivity.this, "格式化存储卡成功,车乐拍将会重启，请等待重启成功后重新连接！");
				UUToast.showUUToast(ManageStorageActivity.this, "格式化存储卡成功！");
				PieInfo pInfo = PieInfo.getInstance();
				if (pInfo != null) {
					pInfo.setRemainSpace(pInfo.getTotalSpace());
					String suse = "";
					String totals = "";
					int total = pInfo.getTotalSpace();
					suse = 0 + "MB";
					DecimalFormat decimalFormat = new DecimalFormat("0.00");
					if (total > 500) {
						totals = decimalFormat.format(total / 1024.0) + "G";
					}
					mTxtHasuseBox.setText("已用空间:" + suse);
					mTxtUnuseBox.setText("可用空间:" + totals);
				}
				break;

			case 1:
				if (mDialog != null) {
					mDialog.dismiss();
				}

				BaseResponseInfo mInfo = (BaseResponseInfo) msg.obj;
				if (mInfo != null) {
					UUToast.showUUToast(ManageStorageActivity.this, "格式化存储卡失败:" + mInfo.getInfo());
				} else {
					UUToast.showUUToast(ManageStorageActivity.this, "格式化存储卡失败...");
				}
				break;
			case 11:
				long total = Environment.getExternalStorageDirectory().getTotalSpace();
				long free = Environment.getExternalStorageDirectory().getFreeSpace();
				DecimalFormat decimalFormat = new DecimalFormat("0.00");
				float fuse = (float) (total - free) / 1024 / 1024;
				float ffree = (float) free / 1024 / 1024;
				String suse = decimalFormat.format(fuse) + "MB";
				String srem = decimalFormat.format(ffree) + "MB";

				if (ffree > 500) {
					srem = decimalFormat.format(ffree / 1024.0) + "G";
				}

				if (fuse > 500) {
					suse = decimalFormat.format(fuse / 1024.0) + "G";
				}

				mTxtHasuseLocal.setText("已用空间:" + suse);
				mTxtUnuseLocal.setText("可用空间:" + srem);
				UUToast.showUUToast(ManageStorageActivity.this, "缓存已清空");
				break;
			}
		}

	};

	protected void onResume() {
		super.onResume();
		mConnControl.onResume();
	};

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
	}

}

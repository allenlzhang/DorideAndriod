package com.carlt.sesame.ui.activity.friends;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.carlt.sesame.R;
import com.carlt.sesame.control.CPControl;
import com.carlt.sesame.control.CPControl.GetResultList3Callback;
import com.carlt.sesame.data.UploadImgInfo;
import com.carlt.sesame.systemconfig.LocalConfig;
import com.carlt.sesame.ui.activity.base.BaseActivity;
import com.carlt.sesame.ui.upload.FolderImgListActivity;
import com.carlt.sesame.ui.upload.ImgDetialActivity;
import com.carlt.sesame.ui.upload.ImgEditAdapter;
import com.carlt.sesame.ui.upload.ImgsActivity;
import com.carlt.sesame.ui.view.PopBoxCreat;
import com.carlt.sesame.ui.view.UUDialog;
import com.carlt.sesame.utility.UUToast;

import java.io.File;
import java.util.ArrayList;

/**
 * 故障图片页面
 * 
 * @author daisy
 */
public class DeclarationPhotoActivity extends BaseActivity {
	private ImageView back;// 头部返回键

	private TextView title;// 标题文字

	private GridView mGridView;

	private ImgEditAdapter mAdapter;

	private ArrayList<UploadImgInfo> images;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_declaration_photo);

		initTitle();

		mGridView = (GridView) findViewById(R.id.declaration_photo_grid);
		images = getIntent().getParcelableArrayListExtra("images");
		mAdapter = new ImgEditAdapter(DeclarationPhotoActivity.this, images);
		mGridView.setAdapter(mAdapter);
		mGridView.setOnItemClickListener(l);
	}

	private void initTitle() {
		back = (ImageView) findViewById(R.id.head_back_img1);
		title = (TextView) findViewById(R.id.head_back_txt1);

		back.setImageResource(R.drawable.arrow_back);
		title.setText("故障照片");

		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	private OnItemClickListener l = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			if (position == (mAdapter.getCount() - 2)) {
				usePhoto();
			} else if (position == (mAdapter.getCount() - 1)) {
				useCamero();
			} else {
				Intent mIntent = new Intent(DeclarationPhotoActivity.this,
						ImgDetialActivity.class);
				mIntent.putExtra(ImgDetialActivity.INDEX, position);
				ArrayList<String> mImgList = new ArrayList<String>();
				for (int i = 0; i < images.size(); i++) {
					mImgList.add(images.get(i).getLocalfilePath());
				}
				mIntent.putStringArrayListExtra(ImgDetialActivity.IMGLIST,
						mImgList);
				mIntent.putExtra(ImgDetialActivity.DELETE, true);
				startActivityForResult(mIntent, REQ_DETIAL);
			}

		}
	};

	public final static int MAX_COUNT = 8;

	public void usePhoto() {
		if (images.size() > MAX_COUNT - 1) {
			UUToast.showUUToast(DeclarationPhotoActivity.this, "最多只能上传"
					+ MAX_COUNT + "张图片");
			return;
		}
		Intent intent = new Intent();
		intent.putExtra(ImgsActivity.CURRENTCOUNT, images.size());
		intent.setClass(this, FolderImgListActivity.class);
		startActivityForResult(intent, REQ_ALBUM);
	}

	public void useCamero() {
		if (images.size() > MAX_COUNT - 1) {
			UUToast.showUUToast(DeclarationPhotoActivity.this, "最多只能上传"
					+ MAX_COUNT + "张图片");
			return;
		}
		ImageName = System.currentTimeMillis() + ".jpg";
		// 调用系统的拍照功能
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(
				LocalConfig.mImageCacheSavePath_SD, ImageName)));
		startActivityForResult(intent, REQ_PHOTOHRAPH);
	}

	public static final int REQ_PHOTOHRAPH = 1;// 拍照

	private final static int REQ_ALBUM = 2;// 相册
	private final static int REQ_DETIAL = 3;// 查看大图

	private final static int RESULT_CODE = 200;

	public static final int RESULT_NONE = 0;

	private String ImageName;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_CODE) {
			if (requestCode == REQ_ALBUM) {
				Bundle bundle = data.getExtras();
				if (bundle != null) {
					ArrayList<String> files = bundle
							.getStringArrayList("files");
					if (files != null) {
						AddData(files);

					}
				}
			} else if (requestCode == REQ_DETIAL) {

				Bundle bundle = data.getExtras();
				if (bundle != null) {
					ArrayList<String> mImgList = bundle
							.getStringArrayList(ImgDetialActivity.IMGLIST);
					if (mImgList != null) {
						for (int j = 0; j < images.size(); j++) {
							boolean flag = false;
							String u_old = images.get(j).getLocalfilePath();
							for (int i = 0; i < mImgList.size(); i++) {
								String u_new = mImgList.get(i);
								if (u_old.equals(u_new)) {
									flag = true;
									break;
								}
							}
							if (flag) {
								continue;
							}
							images.remove(j);
							j--;
							mAdapter.notifyDataSetChanged();
						}

					}
				}

			}

		}

		if (resultCode == RESULT_NONE)
			return;
		// 拍照
		if (requestCode == REQ_PHOTOHRAPH) {
			String iconPath = LocalConfig.mImageCacheSavePath_SD + ImageName;
			ArrayList<String> mFiles = new ArrayList<String>();
			mFiles.add(iconPath);
			AddData(mFiles);
			return;
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	private UUDialog mUUDialog;
	private int upload_count = 0;
	private int upload_index = 0;
	private int upload_erro = 0;
	private GetResultList3Callback listener = new GetResultList3Callback() {

		@Override
		public void onSuccess(Object o1) {
			Message msg = new Message();
			msg.what = 0;
			msg.obj = o1;
			// 上传成功一张
			mHandler.sendMessage(msg);
		}

		@Override
		public void onFinished() {
			// 上传任务结束
			mHandler.sendEmptyMessage(1);
		}

		@Override
		public void onErro() {
			// 上传失败一张
			mHandler.sendEmptyMessage(2);

		}
	};

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				// 上传成功一张
				upload_index++;
				UploadImgInfo mUploadImgInfo = (UploadImgInfo) msg.obj;
				mUUDialog.setContentText("正在上传 " + upload_index + "/"
						+ upload_count);
				images.add(mUploadImgInfo);
				mAdapter.notifyDataSetChanged();
				break;
			case 1:
				// 上传任务结束
				if (mUUDialog != null) {
					mUUDialog.dismiss();
				}
				StringBuffer sb1 = new StringBuffer();
				sb1.append("共上传");
				sb1.append(upload_count);
				sb1.append("张，成功");
				sb1.append(upload_count - upload_erro);
				sb1.append("张，失败");
				sb1.append(upload_erro);
				sb1.append("张");
				UUToast.showUUToast(DeclarationPhotoActivity.this,
						sb1.toString());
				break;
			case 2:
				// 上传失败一张
				upload_index++;
				mUUDialog.setTitle("正在上传 " + upload_index + "/" + upload_count);
				break;
			}
		}

	};

	private void AddData(ArrayList<String> data) {
		upload_index = 0;
		upload_count = data.size();
		mUUDialog = PopBoxCreat.createDialogWithProgress(
				DeclarationPhotoActivity.this, "正在上传 " + upload_index + "/"
						+ upload_count);
		mUUDialog.show();
		CPControl.GetUpLoadSOSImgResult(data, listener);

	}

	@Override
	public void finish() {
		Intent mIntent = new Intent();
		mIntent.putParcelableArrayListExtra("images", images);
		setResult(RESULT_CODE, mIntent);
		super.finish();
	}

}

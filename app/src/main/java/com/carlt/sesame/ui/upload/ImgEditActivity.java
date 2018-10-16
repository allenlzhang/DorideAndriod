package com.carlt.sesame.ui.upload;
//package com.hz17car.carparticle.ui.upload;
//
//import java.io.File;
//import java.util.ArrayList;
//
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.net.Uri;
//import android.os.Bundle;
//import android.provider.MediaStore;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.AdapterView.OnItemClickListener;
//import android.widget.GridView;
//
//import com.hz17car.carparticle.R;
//import com.hz17car.carparticle.systemconfig.LocalConfig;
//import com.hz17car.carparticle.ui.activity.base.BaseActivity;
//
//public class ImgEditActivity extends BaseActivity {
//	private GridView mGridView;
//	private ImgEditAdapter mAdapter;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.edit_img_activity);
//		mGridView = (GridView) findViewById(R.id.edit_img_activity_list);
//		mAdapter = new ImgEditAdapter(ImgEditActivity.this);
//		mGridView.setAdapter(mAdapter);
//		mGridView.setOnItemClickListener(l);
//	}
//
//	private OnItemClickListener l = new OnItemClickListener() {
//
//		@Override
//		public void onItemClick(AdapterView<?> parent, View view, int position,
//				long id) {
//			if (position == (mAdapter.getCount() - 2)) {
//				usePhoto();
//			} else if (position == (mAdapter.getCount() - 1)) {
//				useCamero();
//			} else {
//
//			}
//
//		}
//	};
//
//	public void usePhoto() {
//		Intent intent = new Intent();
//		intent.setClass(this, FolderImgListActivity.class);
//		startActivityForResult(intent, REQ_ALBUM);
//	}
//
//	public void useCamero() {
//		ImageName = System.currentTimeMillis() + ".jpg";
//		// 调用系统的拍照功能
//		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(
//				LocalConfig.mImageCacheSavePath_SD, ImageName)));
//		startActivityForResult(intent, REQ_PHOTOHRAPH);
//	}
//
//	public static final int REQ_PHOTOHRAPH = 1;// 拍照
//	private final static int REQ_ALBUM = 2;// 相册
//	private final static int RESULT_CODE = 200;
//	public static final int RESULT_NONE = 0;
//	private String ImageName;
//
//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		if (resultCode == RESULT_CODE) {
//			if (requestCode == REQ_ALBUM) {
//				Bundle bundle = data.getExtras();
//				if (bundle != null) {
//					ArrayList<String> files = bundle
//							.getStringArrayList("files");
//					if (files != null) {
//						mAdapter.addData(files);
//
//					}
//				}
//			}
//
//		}
//
//		if (resultCode == RESULT_NONE)
//			return;
//		// 拍照
//		if (requestCode == REQ_PHOTOHRAPH) {
//			String iconPath = LocalConfig.mImageCacheSavePath_SD + ImageName;
//			mAdapter.addData(iconPath);
//			return;
//		}
//
//		super.onActivityResult(requestCode, resultCode, data);
//	}
//}

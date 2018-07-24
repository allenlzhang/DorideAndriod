package com.carlt.sesame.ui.upload;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.carlt.sesame.R;
import com.carlt.sesame.ui.MainActivity;
import com.carlt.sesame.ui.activity.friends.DeclarationPhotoActivity;
import com.carlt.sesame.ui.upload.ImgsAdapter.OnItemClickClass;
import com.carlt.sesame.utility.UUToast;

import java.util.ArrayList;

public class ImgsActivity extends Activity {

	private FileImgInfo mFileImgInfo;
	private GridView imgGridView;
	private ImgsAdapter imgsAdapter;

	private TextView hasSelectCount;
	ArrayList<String> filelist = new ArrayList<String>();;
	public final static String CURRENTCOUNT = "currentcount";
	private int currentCount;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_img_activity);
		Bundle bundle = getIntent().getExtras();
		mFileImgInfo = bundle.getParcelable("data");
		currentCount = bundle.getInt(CURRENTCOUNT, 0);
		imgGridView = (GridView) findViewById(R.id.select_img_activity_gridView1);
		hasSelectCount = (TextView) findViewById(R.id.select_img_activity_text);
		imgsAdapter = new ImgsAdapter(this, mFileImgInfo.filecontent,
				onItemClickClass);
		imgGridView.setAdapter(imgsAdapter);

	}

	ImgsAdapter.OnItemClickClass onItemClickClass = new OnItemClickClass() {
		@Override
		public void OnItemClick(View v, int Position, CheckBox checkBox) {
			String filapath = mFileImgInfo.filecontent.get(Position);

			if (checkBox.isChecked()) {
				checkBox.setChecked(false);
				filelist.remove(filapath);
				hasSelectCount.setText("已选择(" + filelist.size() + ")张");
			} else {
				if ((currentCount + filelist.size()) == DeclarationPhotoActivity.MAX_COUNT) {
					UUToast.showUUToast(ImgsActivity.this, "最多只能上传"
							+ DeclarationPhotoActivity.MAX_COUNT + "张图片",
							Toast.LENGTH_SHORT);
					return;
				}
				try {
					checkBox.setChecked(true);
					filelist.add(filapath);
					hasSelectCount.setText("已选择(" + filelist.size() + ")张");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	};

	public void tobreak(View view) {
		finish();
	}

	public void sendfiles(View view) {
		Intent intent = new Intent(this, MainActivity.class);
		Bundle bundle = new Bundle();
		bundle.putStringArrayList("files", filelist);
		intent.putExtras(bundle);
		setResult(RESULT_CODE, intent);
		finish();

	}

	private final static int RESULT_CODE = 200;
}

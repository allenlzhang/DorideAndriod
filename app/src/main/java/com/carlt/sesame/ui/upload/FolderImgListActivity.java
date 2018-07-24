package com.carlt.sesame.ui.upload;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.carlt.sesame.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FolderImgListActivity extends Activity implements
		OnItemClickListener {

	private ListView listView;
	private FolderImgListAdapter listAdapter;
	private List<FileImgInfo> locallist;
	private int currentCount = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.folder_img_activity);
		currentCount = currentCount = getIntent()
				.getIntExtra(ImgsActivity.CURRENTCOUNT, 0);
		listView = (ListView) findViewById(R.id.folder_img_activity_list);
		Util util = new Util(this);
		locallist = util.LocalImgFileList();
		List<HashMap<String, String>> listdata = new ArrayList<HashMap<String, String>>();
		Bitmap bitmap[] = null;
		if (locallist != null) {
			bitmap = new Bitmap[locallist.size()];
			for (int i = 0; i < locallist.size(); i++) {
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("filecount", locallist.get(i).filecontent.size() + "张");
				map.put("imgpath",
						locallist.get(i).filecontent.get(0) == null ? null
								: (locallist.get(i).filecontent.get(0)));
				map.put("filename", locallist.get(i).filename);
				listdata.add(map);
			}
		}
		listAdapter = new FolderImgListAdapter(this, listdata);
		listView.setAdapter(listAdapter);
		listView.setOnItemClickListener(this);

	}

	private final static int REQ_CODE = 100;

	private final static int RESULT_CODE = 200;

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Intent intent = new Intent(this, ImgsActivity.class);
		Bundle bundle = new Bundle();
		bundle.putParcelable("data", locallist.get(arg2));
		intent.putExtra(ImgsActivity.CURRENTCOUNT, currentCount);
		intent.putExtras(bundle);
		startActivityForResult(intent, REQ_CODE);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode == RESULT_CODE) {
			if (requestCode == REQ_CODE) {
				setResult(RESULT_CODE, data);
				finish();
			}

		}
		super.onActivityResult(requestCode, resultCode, data);
	}

}

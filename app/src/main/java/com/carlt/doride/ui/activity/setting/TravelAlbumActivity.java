package com.carlt.doride.ui.activity.setting;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.carlt.doride.R;
import com.carlt.doride.base.LoadingActivity;
import com.carlt.doride.data.BaseResponseInfo;
import com.carlt.doride.data.set.AlbumImageInfo;
import com.carlt.doride.protocolparser.AlbumImageParser;
import com.carlt.doride.protocolparser.BaseParser;
import com.carlt.doride.protocolparser.DefaultStringParser;
import com.carlt.doride.systemconfig.URLConfig;
import com.carlt.doride.ui.adapter.AlbumImageAdapter;
import com.carlt.doride.ui.view.PopBoxCreat;
import com.carlt.doride.ui.view.UUToast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class TravelAlbumActivity extends LoadingActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private static final String TAG = TravelAlbumActivity.class.getName();

    private boolean idEditing = true;//是否为可编辑状态

    private ArrayList<AlbumImageInfo> albumImageInfos;

    private AlbumImageAdapter adapter;//相册适配器

    private GridView album_list;//相册列表

    private View album_image_item_opt;//底部操作

    private TextView album_select_all;//全选按钮

    private TextView album_delete;//删除按钮

    private boolean isSelectAll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_album);
        initTitle("旅行相册");
        setBtnOptVisible(true);
        setBtnOptText("编辑");
        setOnBtnOptClickListener(this);
        initComponent();
    }

    private void initComponent() {
        album_list = $ViewByID(R.id.album_list);
        album_list.setOnItemClickListener(this);
        album_image_item_opt = $ViewByID(R.id.album_image_item_opt);
        album_select_all = $ViewByID(R.id.album_select_all);
        album_select_all.setOnClickListener(this);
        album_delete = $ViewByID(R.id.album_delete);
        album_delete.setOnClickListener(this);
    }

    @Override
    public void reTryLoadData() {
        super.reTryLoadData();
        initData();
    }

    @Override
    protected void onResume() {
        initData();
        super.onResume();
    }


    private void initData() {
        loadingDataUI();
        AlbumImageParser parser = new AlbumImageParser(callback);
        HashMap<String, String> params = new HashMap<>();
        parser.executePost(URLConfig.getAlbumUrl(URLConfig.ALBUM_QUERY), params);
    }

    /**
     * 查询相册回调
     */
    private BaseParser.ResultCallback callback = new BaseParser.ResultCallback() {
        @Override
        public void onSuccess(BaseResponseInfo bInfo) {
            loadSuccessUI();
            albumImageInfos = (ArrayList<AlbumImageInfo>) bInfo.getValue();
            adapter = new AlbumImageAdapter(TravelAlbumActivity.this, albumImageInfos);
            album_list.setAdapter(adapter);
        }

        @Override
        public void onError(BaseResponseInfo bInfo) {
            bInfo.setInfo("暂未拉取到图片");
            loadonErrorUI(bInfo);
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnOpt:
                changeAlbumStatus();
                break;
            case R.id.album_select_all:
                isSelectAll=true;
                selectAll();
                break;
            case R.id.album_delete:
                if (isSelectAll) {
                    PopBoxCreat.createDialogWithTitle(this, "温馨提示", "确认是否删除所选内容", null, "取消", "删除", new PopBoxCreat.DialogWithTitleClick() {
                        @Override
                        public void onLeftClick() {
                            idEditing=false;
                            changeAlbumStatus();
                        }

                        @Override
                        public void onRightClick() {
                            deleteAllImages();
                        }
                    });
                } else {
                    deleteImages();
                }
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (album_image_item_opt.getVisibility() == View.GONE) {
            AlbumImageInfo info = (AlbumImageInfo) adapterView.getItemAtPosition(i);
            Intent imageIntent = new Intent(this, PhotoDisplayActivity.class);
            imageIntent.putExtra("imagePath", info.getImagePath());
            imageIntent.putExtra("imageId", info.getId());
            imageIntent.putExtra("imageName", info.getUploadTime());
            startActivityForResult(imageIntent,0);
        } else {
            if (!adapter.isIsHide()) {
                AlbumImageAdapter.ViewHolder holder = (AlbumImageAdapter.ViewHolder) view.getTag();
                holder.status.toggle();
                AlbumImageAdapter.getIsSelected().put(i, holder.status.isChecked());
            }

        }
    }

    /*
        * 删除照片
        * */
    private void deleteImages() {
        DefaultStringParser parser = new DefaultStringParser(deleteCallback);
        HashMap<String, String> params = new HashMap<>();
        if (AlbumImageAdapter.getIsSelected() != null) {
            Set<Integer> idx = AlbumImageAdapter.getIsSelected().keySet();
            Log.e(TAG,"Hash大小"+idx.size());
            Log.e(TAG,"albumImageInfos"+albumImageInfos.size());
            Iterator<Integer> iterator = idx.iterator();
            StringBuilder builder = new StringBuilder();
            while (iterator.hasNext()) {
                int selectIndex = iterator.next();
                if (AlbumImageAdapter.getIsSelected().get(selectIndex)) {
                    int imageId = albumImageInfos.get(selectIndex).getId();
                    builder.append(imageId).append(",");
                    albumImageInfos.remove(selectIndex);
                }
            }
            if (builder.length()<=0) {
                UUToast.showUUToast(this,"您还没有选择照片");
                return;
            }
            String paramIdx = builder.substring(0, builder.length() - 1);
            params.put("id", paramIdx);
            parser.executePost(URLConfig.getAlbumUrl(URLConfig.ALBUM_DELETE), params);
        }

    }

    /*
    * 删除照片
    * */
    private void deleteAllImages() {
        DefaultStringParser parser = new DefaultStringParser(deleteCallback);
        HashMap<String, String> params = new HashMap<>();
        StringBuilder builder = new StringBuilder();
        if (albumImageInfos != null && albumImageInfos.size() > 0) {
            for (int i = 0; i < albumImageInfos.size(); i++) {
                AlbumImageInfo albumImageInfo = albumImageInfos.get(i);
                builder.append(albumImageInfo.getId()).append(",");
            }
            albumImageInfos.removeAll(albumImageInfos);
        }
        String paramIdx = builder.substring(0, builder.length() - 1);
        params.put("id", paramIdx);
        parser.executePost(URLConfig.getAlbumUrl(URLConfig.ALBUM_DELETE), params);
    }

    /**
     * 删除接口回调
     */
    private BaseParser.ResultCallback deleteCallback = new BaseParser.ResultCallback() {
        @Override
        public void onSuccess(BaseResponseInfo bInfo) {
            UUToast.showUUToast(TravelAlbumActivity.this, "相册删除成功");
            initData();
            idEditing=false;
            changeAlbumStatus();
        }

        @Override
        public void onError(BaseResponseInfo bInfo) {
            UUToast.showUUToast(TravelAlbumActivity.this, "相册删除失败");
        }
    };

    private void changeAlbumStatus() {
        if (idEditing) {
            setBtnOptText(getResources().getString(R.string.travel_album_cancel));
            album_image_item_opt.setVisibility(View.VISIBLE);
            idEditing = false;
            isSelectAll=false;
            if (null != adapter) {
                adapter.setIsHide(false);
                adapter.notifyDataSetChanged();
            }
        } else {
            setBtnOptText(getResources().getString(R.string.travel_album_edit));
            idEditing = true;
            album_image_item_opt.setVisibility(View.GONE);
            cancelSelectall();
        }
    }

    /**
     * 全选
     */
    private void selectAll() {
        if (adapter != null) {
            if (null != albumImageInfos && albumImageInfos.size() > 0) {
                for (int i = 0; i < albumImageInfos.size(); i++) {
                    AlbumImageAdapter.getIsSelected().put(i, true);
                }
            }
            adapter.notifyDataSetChanged();
        }

    }

    /**
     * 全不选
     */
    private void cancelSelectall() {
        // 遍历list的长度，将已选的按钮设为未选
        if (adapter != null) {
            if (null != albumImageInfos && albumImageInfos.size() > 0) {
                for (int i = 0; i < albumImageInfos.size(); i++) {
                    AlbumImageAdapter.getIsSelected().put(i, false);
                }
            }
            adapter.setIsHide(true);
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * 删除选中项
     */
    private void deleteSelectedImage() {
        if (adapter != null) {
            AlbumImageAdapter.getIsSelected().clear();
            adapter.setAlbumImageInfos(albumImageInfos);
            adapter.notifyDataSetChanged();
            cancelSelectall();
        }
    }

}

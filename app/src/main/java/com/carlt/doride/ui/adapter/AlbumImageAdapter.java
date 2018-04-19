package com.carlt.doride.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.carlt.doride.R;
import com.carlt.doride.data.set.AlbumImageInfo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by marller on 2018\4\1 0001.
 */

public class AlbumImageAdapter extends BaseAdapter {

    private static final String TAG = AlbumImageAdapter.class.getName();

    private LayoutInflater inflater;

    private Context context;

    private ArrayList<AlbumImageInfo> albumImageInfos;

    private static HashMap<Integer, Boolean> isSelected;

    private boolean isHide = true;


    public AlbumImageAdapter(Context context, ArrayList<AlbumImageInfo> albumImageInfos) {
        this.context = context;
        this.albumImageInfos = albumImageInfos;
        isSelected = new HashMap<>();
        inflater = LayoutInflater.from(context);
        if (this.albumImageInfos!=null&&this.albumImageInfos.size()>0) {
            //按时间排序
            Collections.sort(this.albumImageInfos, new Comparator<AlbumImageInfo>() {
                @Override
                public int compare(AlbumImageInfo albumImageInfo, AlbumImageInfo albumImageInfo1) {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date1 = null, date2 = null;
                    try {
                        date1 = simpleDateFormat.parse(albumImageInfo.getUploadTime());
                        date2 = simpleDateFormat.parse(albumImageInfo1.getUploadTime());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    return date2.compareTo(date1);
                }
            });
            checkedInit();
        }
    }

    private void checkedInit() {
        for (int i = 0; i < albumImageInfos.size(); i++) {
            getIsSelected().put(i, false);
        }
    }

    public boolean isIsHide() {
        return isHide;
    }

    public ArrayList<AlbumImageInfo> getAlbumImageInfos() {
        return albumImageInfos;
    }

    public void setAlbumImageInfos(ArrayList<AlbumImageInfo> albumImageInfos) {
        this.albumImageInfos = albumImageInfos;
    }

    /**
     * 设置CheckBox是否全部隐藏
     */
    public void setIsHide(boolean isHide) {
        this.isHide = isHide;
    }

    @Override
    public int getCount() {
        return albumImageInfos == null ? 0 : albumImageInfos.size();
    }

    @Override
    public AlbumImageInfo getItem(int i) {
        return albumImageInfos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        AlbumImageInfo mAlbumImageInfo = getItem(i);
        if (view == null) {
            viewHolder = new ViewHolder();
            view = inflater.inflate(R.layout.album_image_list_item, null);
            viewHolder.status = view.findViewById(R.id.album_image_check);
            viewHolder.image = view.findViewById(R.id.album_image);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.status.setChecked(getIsSelected().get(i));
        if (isHide) {
            viewHolder.status.setVisibility(View.GONE);
        } else {
            viewHolder.status.setVisibility(View.VISIBLE);
        }
        Glide.with(context).load(mAlbumImageInfo.getThumbnailPath()).into(viewHolder.image);
        return view;
    }

    public static class ViewHolder {
        public  CheckBox status;
        public  ImageView image;
    }

    public static HashMap<Integer, Boolean> getIsSelected() {
        return isSelected;
    }

    public static void setIsSelected(HashMap<Integer, Boolean> isSelected) {
        AlbumImageAdapter.isSelected = isSelected;
    }

}

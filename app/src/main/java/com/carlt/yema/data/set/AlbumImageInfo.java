package com.carlt.yema.data.set;

import com.carlt.yema.data.BaseResponseInfo;

/**
 * Created by marller on 2018\4\1 0001.
 */

public class AlbumImageInfo extends BaseResponseInfo {

    private int Id;
    private String ImagePath;
    private String ThumbnailPath;
    private String UploadTime;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        this.Id = id;
    }

    public String getImagePath() {
        return ImagePath;
    }

    public void setImagePath(String imagePath) {
        this.ImagePath = imagePath;
    }

    public String getThumbnailPath() {
        return ThumbnailPath;
    }

    public void setThumbnailPath(String thumbnailPath) {
        this.ThumbnailPath = thumbnailPath;
    }

    public String getUploadTime() {
        return UploadTime;
    }

    public void setUploadTime(String uploadTime) {
        this.UploadTime = uploadTime;
    }
}

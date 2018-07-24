package com.carlt.sesame.data;

import android.os.Parcel;
import android.os.Parcelable;

public class UploadImgInfo implements Parcelable {
	private String id;

	private String filePath;

	private String localfilePath;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getLocalfilePath() {
		return localfilePath;
	}

	public void setLocalfilePath(String localfilePath) {
		this.localfilePath = localfilePath;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(id);
		dest.writeString(filePath);
		dest.writeString(localfilePath);

	}

	public static final Creator<UploadImgInfo> CREATOR = new Creator<UploadImgInfo>() {

		@Override
		public UploadImgInfo[] newArray(int size) {
			return null;
		}

		@Override
		public UploadImgInfo createFromParcel(Parcel source) {
			UploadImgInfo ft = new UploadImgInfo();
			ft.setId(source.readString());
			ft.setFilePath(source.readString());
			ft.setLocalfilePath(source.readString());
			return ft;
		}

	};

}

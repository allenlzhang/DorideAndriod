package com.carlt.yema.data;

import android.os.Parcel;
import android.os.Parcelable;

public class UploadInfo implements Parcelable {
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

	public static final Parcelable.Creator<UploadInfo> CREATOR = new Creator<UploadInfo>() {

		@Override
		public UploadInfo[] newArray(int size) {
			return null;
		}

		@Override
		public UploadInfo createFromParcel(Parcel source) {
			UploadInfo ft = new UploadInfo();
			ft.setId(source.readString());
			ft.setFilePath(source.readString());
			ft.setLocalfilePath(source.readString());
			return ft;
		}

	};

}

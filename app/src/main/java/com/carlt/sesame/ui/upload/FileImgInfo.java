package com.carlt.sesame.ui.upload;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("ParcelCreator")
public class FileImgInfo implements Parcelable {
	public String filename;
	public List<String> filecontent=new ArrayList<String>();
	
	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(filename);
		dest.writeList(filecontent);
	}
	
	public static final Creator<FileImgInfo> CREATOR=new Creator<FileImgInfo>() {
		
		@Override
		public FileImgInfo[] newArray(int size) {
			return null;
		}
		
		@Override
		public FileImgInfo createFromParcel(Parcel source) {
			FileImgInfo ft=new FileImgInfo();
			ft.filename= source.readString();
			ft.filecontent= source.readArrayList(FileImgInfo.class.getClassLoader());
			return ft;
		}
		
		
	};
}

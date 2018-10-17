package com.carlt.chelepie.download;

/**
 * 下载文件信息
 * 
 * @author Administrator
 * 
 */
public class DownloadBaseInfo {
	private String id;// 下载ID

	private int status;// 状态

	public final static int STATUS_UNLOAD = 1;// 未下载
	public final static int STATUS_LOADING = 2;// 下载中
	public final static int STATUS_PAUSE = 3;// 暂停
	public final static int STATUS_FINISHED = 4;// 已完成

	private String length;// 文件大小

	private String lengthTotal;// 文件完整大小

	private int persent;// 下载百分比

	private String url;// 文件url

	private String pathLocal;// 文件本地路径

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getLength() {
		return length;
	}

	public void setLength(String length) {
		this.length = length;
	}

	public String getLengthTotal() {
		return lengthTotal;
	}

	public String getEndPos() {
		if (lengthTotal != null && lengthTotal.length() > 0) {
			try {
				int endpos = Integer.parseInt(lengthTotal);
				return endpos - 1 + "";
			} catch (Exception ex) {
				return null;
			}
		}
		return null;
	}

	public void setLengthTotal(String lengthTotal) {
		this.lengthTotal = lengthTotal;
	}

	public int getPersent() {
		return persent;
	}

	public void setPersent(int persent) {
		this.persent = persent;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getPathLocal() {
		return pathLocal;
	}

	public void setPathLocal(String pathLocal) {
		this.pathLocal = pathLocal;
	}

}

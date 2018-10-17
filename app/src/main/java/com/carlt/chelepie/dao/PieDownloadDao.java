/**
 * 
 */
package com.carlt.chelepie.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.carlt.chelepie.data.recorder.PieDownloadInfo;
import com.carlt.sesame.dao.DaoConfig;

import java.util.ArrayList;


/**
 *
 * @author @Y.yun
 * 
 */
public class PieDownloadDao extends BaseDao {

	
	public final static String DB_NAME = "download_db";
	public final static String TABLE_NAME = "pie_downlaod";
	public final static String PDW_ID = "_id";
	public final static String PDW_STATUS = "_status";
	public final static String PDW_FILENAME = "_filename";
	public final static String PDW_FILESRCNAME = "_filesrcname";
	public final static String PDW_TOTALLEN = "_totallen";
	public final static String PDW_STARTTIME = "_starttime";
	public final static String PDW_ENDTIME = "_endtime";// 结束时间
	public final static String PDW_TYPE = "_type";// 文件类型
	public final static String PDW_DOWNLOADLEN = "_downloadlen";// 文件类型
	public final static String PDW_LOCALPATH = "_localpath";// 文件本地存储地址
	public final static String PDW_THUMBNAILPATH = "_thumbnailpath";// 缩略图地址
	public final static String PDW_STORETYPE = "_storetype";// 存储类型
	public final static String PDW_CREATETIME = "_createtime";// 创建时间
	public final static String PDW_SUCCTIME = "_succtime";// 成功时间
	public final static String PDW_RESOLUTION = "_resolution";// 分辨率
	public final static String PDW_ACCOUNT = "_account";// 账户
	public final static String PDW_DEVICE = "_deviceid";
	
	public final static String PDW_FILENO = "_fileNo";
	public final static String PDW_RECORDTYPE = "_recordtype";
	public final static String PDW_MINRECORDTYPE = "_minrecordtype";
	public final static String PDW_STREAMTYPE = "_streamType";

	Context mCxt;

	public PieDownloadDao(Context cxt) {
		super(cxt, DB_NAME, null, DaoConfig.DATABASE_VERSION);
		this.mCxt = cxt;
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

	public long insert(PieDownloadInfo downloadInfo) {
		String s1 = PDW_FILESRCNAME + "=" + "? and " + PDW_ACCOUNT + "= ? and " + PDW_DEVICE + "= ? ";
		int delete = Db.delete(TABLE_NAME, s1, new String[] { downloadInfo.getFileSrcName(), downloadInfo.getAccout(), downloadInfo.getDeviceName() });
		long id = Db.insert(TABLE_NAME, null, getContentValues(downloadInfo));
		Log.e("db_insert", id+"");
		if (id > 0) {
			downloadInfo.setId((int) id);
		}
		return id;
	}

	public boolean delete(PieDownloadInfo downloadInfo) {
		String s = PDW_ID + "=" + "? ";
		return Db.delete(TABLE_NAME, s, new String[] { downloadInfo.getId() + "" }) > 0;
	}
	
	public boolean deleteByCondation(String where, String[] whereArgs) {
		return Db.delete(TABLE_NAME, where, whereArgs) > 0;
	}

	public boolean update(PieDownloadInfo downloadInfo) {
		String s = PDW_ID + "=" + "? ";
		return Db.update(TABLE_NAME, getContentValues(downloadInfo), s, new String[] { downloadInfo.getId() + "" }) > 0;
	}

	/**
	 * 以文件名获取，fileSrcName
	 * 
	 * @return
	 */
	public PieDownloadInfo get(String fileSrcName, String account, String deviceName) {
		PieDownloadInfo info = null;
		String s = PDW_FILESRCNAME + " = " + " ?  and " + PDW_ACCOUNT + "= ? and " + PDW_DEVICE + "= ? ";
		Cursor cursor = null;
		try {
			cursor = Db.query(TABLE_NAME, null, s, new String[] { fileSrcName, account, deviceName }, null, null, null);
			if (cursor.moveToNext()) {
				info = new PieDownloadInfo();
				int index = 0;
				info.setId(cursor.getInt(index));
				index++;
				info.setResolution(cursor.getInt(index));
				index++;
				info.setStatus(cursor.getInt(index));
				index++;
				info.setStoreType(cursor.getInt(index));
				index++;
				info.setTotalLen(cursor.getInt(index));
				index++;
				info.setDownloadLen(cursor.getInt(index));
				index++;
				info.setType(cursor.getInt(index));
				index++;
				info.setFileName(cursor.getString(index));
				index++;
				info.setFileSrcName(cursor.getString(index));
				index++;
				info.setStartTime(cursor.getString(index));
				index++;
				info.setEndTime(cursor.getString(index));
				index++;
				info.setLocalPath(cursor.getString(index));
				index++;
				info.setThumbnailPath(cursor.getString(index));
				index++;
				info.setCreateTime(cursor.getString(index));
				index++;
				info.setSuccTime(cursor.getString(index));
				index++;
				info.setAccout(cursor.getString(index));
				index++;
				info.setDeviceName(cursor.getString(index));
				index++;
				info.setFileNo(Long.parseLong(cursor.getString(index)));
				index++;
				info.setRecordType(Long.parseLong(cursor.getString(index)));
				index++;
				info.setMinRecordType(Long.parseLong(cursor.getString(index)));
				index++;
				info.setStreamType(Long.parseLong(cursor.getString(index)));
				index++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}

		return info;
	}
	
	/**
	 * 以文件名获取，fileName
	 * 
	 * @param fileName
	 * @return
	 */
	public PieDownloadInfo getByFilename(String fileName, String account, String deviceName) {
		PieDownloadInfo info = null;
		String s = PDW_FILENAME + " = " + " ?  and " + PDW_ACCOUNT + "= ? ";
		Cursor cursor = null;
		try {
			cursor = Db.query(TABLE_NAME, null, s, new String[] { fileName, account }, null, null, null);
			if (cursor.moveToNext()) {
				info = new PieDownloadInfo();
				int index = 0;
				info.setId(cursor.getInt(index));
				index++;
				info.setResolution(cursor.getInt(index));
				index++;
				info.setStatus(cursor.getInt(index));
				index++;
				info.setStoreType(cursor.getInt(index));
				index++;
				info.setTotalLen(cursor.getInt(index));
				index++;
				info.setDownloadLen(cursor.getInt(index));
				index++;
				info.setType(cursor.getInt(index));
				index++;
				info.setFileName(cursor.getString(index));
				index++;
				info.setFileSrcName(cursor.getString(index));
				index++;
				info.setStartTime(cursor.getString(index));
				index++;
				info.setEndTime(cursor.getString(index));
				index++;
				info.setLocalPath(cursor.getString(index));
				index++;
				info.setThumbnailPath(cursor.getString(index));
				index++;
				info.setCreateTime(cursor.getString(index));
				index++;
				info.setSuccTime(cursor.getString(index));
				index++;
				info.setAccout(cursor.getString(index));
				index++;
				info.setDeviceName(cursor.getString(index));
				index++;
				info.setFileNo(Long.parseLong(cursor.getString(index)));
				index++;
				info.setRecordType(Long.parseLong(cursor.getString(index)));
				index++;
				info.setMinRecordType(Long.parseLong(cursor.getString(index)));
				index++;
				info.setStreamType(Long.parseLong(cursor.getString(index)));
				index++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}

		return info;
	}

	public ArrayList<PieDownloadInfo> getList(String account) {
		String s = PDW_ACCOUNT + "=" + "?";
		Cursor cursor = Db.query(TABLE_NAME, null, s, new String[] { account }, null, null, null);
		ArrayList<PieDownloadInfo> arrays = null;
		arrays = new ArrayList<PieDownloadInfo>();
		try {
			while (cursor.moveToNext()) {
				PieDownloadInfo info = new PieDownloadInfo();
				int index = 0;
				info.setId(cursor.getInt(index));
				index++;
				info.setResolution(cursor.getInt(index));
				index++;
				info.setStatus(cursor.getInt(index));
				index++;
				info.setStoreType(cursor.getInt(index));
				index++;
				info.setTotalLen(cursor.getInt(index));
				index++;
				info.setDownloadLen(cursor.getInt(index));
				index++;
				info.setType(cursor.getInt(index));
				index++;
				info.setFileName(cursor.getString(index));
				index++;
				info.setFileSrcName(cursor.getString(index));
				index++;
				info.setStartTime(cursor.getString(index));
				index++;
				info.setEndTime(cursor.getString(index));
				index++;
				info.setLocalPath(cursor.getString(index));
				index++;
				info.setThumbnailPath(cursor.getString(index));
				index++;
				info.setCreateTime(cursor.getString(index));
				index++;
				info.setSuccTime(cursor.getString(index));
				index++;
				info.setAccout(cursor.getString(index));
				index++;
				info.setDeviceName(cursor.getString(index));
				index++;
				info.setFileNo(Long.parseLong(cursor.getString(index)));
				index++;
				info.setRecordType(Long.parseLong(cursor.getString(index)));
				index++;
				info.setMinRecordType(Long.parseLong(cursor.getString(index)));
				index++;
				info.setStreamType(Long.parseLong(cursor.getString(index)));
				index++;
				arrays.add(info);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (!cursor.isClosed()) {
				cursor.close();
			}
		}

		return arrays;
	}

	/**
	 * 靠条件查找
	 * 
	 * @param where
	 * @param values
	 * @return
	 */
	public ArrayList<PieDownloadInfo> getListByCondition(String where, String[] values) {
		Cursor cursor = Db.query(TABLE_NAME, null, where, values, null, null, null);
		ArrayList<PieDownloadInfo> arrays = null;
		try {
			while (cursor.moveToNext()) {
				if (arrays == null) {
					arrays = new ArrayList<PieDownloadInfo>();
				}

				PieDownloadInfo info = new PieDownloadInfo();
				int index = 0;
				info.setId(cursor.getInt(index));
				index++;
				info.setResolution(cursor.getInt(index));
				index++;
				info.setStatus(cursor.getInt(index));
				index++;
				info.setStoreType(cursor.getInt(index));
				index++;
				info.setTotalLen(cursor.getInt(index));
				index++;
				info.setDownloadLen(cursor.getInt(index));
				index++;
				info.setType(cursor.getInt(index));
				index++;
				info.setFileName(cursor.getString(index));
				index++;
				info.setFileSrcName(cursor.getString(index));
				index++;
				info.setStartTime(cursor.getString(index));
				index++;
				info.setEndTime(cursor.getString(index));
				index++;
				info.setLocalPath(cursor.getString(index));
				index++;
				info.setThumbnailPath(cursor.getString(index));
				index++;
				info.setCreateTime(cursor.getString(index));
				index++;
				info.setSuccTime(cursor.getString(index));
				index++;
				info.setAccout(cursor.getString(index));
				index++;
				info.setDeviceName(cursor.getString(index));
				index++;
				info.setFileNo(Long.parseLong(cursor.getString(index)));
				index++;
				info.setRecordType(Long.parseLong(cursor.getString(index)));
				index++;
				info.setMinRecordType(Long.parseLong(cursor.getString(index)));
				index++;
				arrays.add(info);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (!cursor.isClosed()) {
				cursor.close();
			}
		}

		return arrays;
	}

	private ContentValues getContentValues(PieDownloadInfo mDownloadInfo) {
		ContentValues contentValues = new ContentValues();
		// contentValues.put(PDW_ID, mDownloadInfo.getId());
		contentValues.put(PDW_STATUS, mDownloadInfo.getStatus());
		contentValues.put(PDW_FILENAME, mDownloadInfo.getFileName());
		contentValues.put(PDW_FILESRCNAME, mDownloadInfo.getFileSrcName());
		contentValues.put(PDW_TOTALLEN, mDownloadInfo.getTotalLen());
		contentValues.put(PDW_STARTTIME, mDownloadInfo.getStartTime());
		contentValues.put(PDW_ENDTIME, mDownloadInfo.getEndTime());
		contentValues.put(PDW_TYPE, mDownloadInfo.getType());
		contentValues.put(PDW_DOWNLOADLEN, mDownloadInfo.getDownloadLen());
		contentValues.put(PDW_LOCALPATH, mDownloadInfo.getLocalPath());
		contentValues.put(PDW_THUMBNAILPATH, mDownloadInfo.getThumbnailPath());
		contentValues.put(PDW_STORETYPE, mDownloadInfo.getStoreType());
		contentValues.put(PDW_CREATETIME, mDownloadInfo.getCreateTime());
		contentValues.put(PDW_SUCCTIME, mDownloadInfo.getSuccTime());
		contentValues.put(PDW_RESOLUTION, mDownloadInfo.getResolution());
		contentValues.put(PDW_ACCOUNT, mDownloadInfo.getAccout());
		contentValues.put(PDW_DEVICE, mDownloadInfo.getDeviceName());
		
		contentValues.put(PDW_FILENO, ""+mDownloadInfo.getFileNo());
		contentValues.put(PDW_RECORDTYPE, ""+mDownloadInfo.getRecordType());
		contentValues.put(PDW_MINRECORDTYPE, ""+mDownloadInfo.getMinRecordType());
		contentValues.put(PDW_STREAMTYPE, ""+mDownloadInfo.getStreamType());

		return contentValues;
	}

	@Override
	protected void initTable() {
		StringBuilder builder = new StringBuilder();
		builder.append("CREATE TABLE IF NOT EXISTS ");
		builder.append(TABLE_NAME);
		builder.append(" (");
		builder.append(PDW_ID);
		builder.append(" integer primary key autoincrement,");
		builder.append(PDW_RESOLUTION);
		builder.append(" integer,");
		builder.append(PDW_STATUS);
		builder.append(" integer,");
		builder.append(PDW_STORETYPE);
		builder.append(" integer,");
		builder.append(PDW_TOTALLEN);
		builder.append(" integer,");
		builder.append(PDW_DOWNLOADLEN);
		builder.append(" integer,");
		builder.append(PDW_TYPE);
		builder.append(" integer,");
		builder.append(PDW_FILENAME);
		builder.append(" text,");
		builder.append(PDW_FILESRCNAME);
		builder.append(" text,");
		builder.append(PDW_STARTTIME);
		builder.append(" text,");
		builder.append(PDW_ENDTIME);
		builder.append(" text,");
		builder.append(PDW_LOCALPATH);
		builder.append(" text,");
		builder.append(PDW_THUMBNAILPATH);
		builder.append(" text,");
		builder.append(PDW_CREATETIME);
		builder.append(" text,");
		builder.append(PDW_SUCCTIME);
		builder.append(" text,");
		builder.append(PDW_ACCOUNT);
		builder.append(" text,");
		builder.append(PDW_DEVICE);
		builder.append(" text,");
		builder.append(PDW_FILENO);
		builder.append(" text,");
		builder.append(PDW_RECORDTYPE);
		builder.append(" text,");
		builder.append(PDW_MINRECORDTYPE);
		builder.append(" text,");
		builder.append(PDW_STREAMTYPE);
		builder.append(" text");
		builder.append(" )");
		Db.execSQL(builder.toString());
	}

}

package com.carlt.sesame.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.carlt.sesame.data.remote.RemoteLogInfo;
import com.carlt.sesame.utility.Log;

import java.util.ArrayList;

public class VisitorDao extends SQLiteOpenHelper {

	// 违章信息表
	public final static String TABLE_VISITOR = "visitor_log_table";

	// ID
	public final static String VISITOR_ID = "_id";
	public final static String VISITOR_TYPE = "_log_type";
	// 远程操作项
	public final static String VISITOR_NAME = "_log_name";
	// 远程日志图标
	public final static String VISITOR_ICON = "_log_icon";
	// 记录日志时间
	public final static String VISITOR_DATE = "_log_time";
	// 操作设备名称
	public final static String VISITOR_DEVICE = "_device_name";
	// 操作结果
	public final static String VISITOR_RESULT = "_result";

	public static final String DATABASE_NAME = "visitor_db";

	protected Context mContext;

	protected SQLiteDatabase Db;

	public VisitorDao(Context context) {
		super(context, VisitorDao.DATABASE_NAME, null, DaoConfig.DATABASE_VERSION);
		this.mContext = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Db = db;
		initTable();
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {

	}

	public long insert(RemoteLogInfo logInfo) {
		long id = Db.insert(TABLE_VISITOR, null, getContentValues(logInfo));
		return id;
	}

	/**
	 * @param limit
	 *            获取条数
	 * @param offset
	 *            起始位置
	 * @return
	 */
	public ArrayList<RemoteLogInfo> getLists(int limit, int offset) {
		Cursor cursor = Db.query(TABLE_VISITOR,
			null,
			null,
			null,
			null,
			null,
			VISITOR_ID + " DESC ",
			offset + "," + limit);
		ArrayList<RemoteLogInfo> arrays = new ArrayList<RemoteLogInfo>();
		try {
			while (cursor.moveToNext()) {
				RemoteLogInfo info = new RemoteLogInfo();
				int index = 1;
				info.setLogtype(cursor.getInt(index));
				index++;
				info.setLogName(cursor.getString(index));
				index++;
				info.setLogIcon(cursor.getInt(index));
				index++;
				info.setLogtime(cursor.getString(index));
				index++;
				info.setDevice_name(cursor.getString(index));
				index++;
				info.setResult(cursor.getString(index));
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
	
	/**
	 *
	 */
	public ArrayList<RemoteLogInfo> getLists() {
		Cursor cursor = Db.query(TABLE_VISITOR,
			null,
			null,
			null,
			null,
			null,
			VISITOR_ID + " DESC ");
		ArrayList<RemoteLogInfo> arrays = new ArrayList<RemoteLogInfo>();
		try {
			while (cursor.moveToNext()) {
				RemoteLogInfo info = new RemoteLogInfo();
				int index = 1;
				info.setLogtype(cursor.getInt(index));
				index++;
				info.setLogName(cursor.getString(index));
				index++;
				info.setLogIcon(cursor.getInt(index));
				index++;
				info.setLogtime(cursor.getString(index));
				index++;
				info.setDevice_name(cursor.getString(index));
				index++;
				info.setResult(cursor.getString(index));
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

	public ContentValues getContentValues(RemoteLogInfo rlf) {
		ContentValues values = new ContentValues();
		values.put(VISITOR_TYPE, rlf.getLogtype());
		values.put(VISITOR_NAME, rlf.getLogName());
		values.put(VISITOR_ICON, rlf.getLogIcon());
		values.put(VISITOR_DATE, rlf.getLogtime());
		values.put(VISITOR_DEVICE, rlf.getDevice_name());
		values.put(VISITOR_RESULT, rlf.getResult());

		return values;
	}

	// 打开
	public void open() throws SQLException {
		Db = getWritableDatabase();
	}

	// 关闭
	public void closeDB() {
		close();
	}

	// 建立体重信息表
	private void initTable() {
		// 创建
		StringBuffer sql = new StringBuffer();
		sql.append("CREATE TABLE IF NOT EXISTS ").append(TABLE_VISITOR).append(" ( ").append(VISITOR_ID)
				.append("  integer primary key autoincrement,").append(VISITOR_TYPE).append(" text ,")
				.append(VISITOR_NAME).append(" text ,").append(VISITOR_ICON).append(" text ,").append(VISITOR_DATE)
				.append(" text ,").append(VISITOR_DEVICE).append(" text ,").append(VISITOR_RESULT).append(" text ")
				.append(" )");

		Log.e("info", " ==" + sql.toString());
		Db.execSQL(sql.toString());
	}

}

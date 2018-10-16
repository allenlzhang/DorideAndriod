package com.carlt.sesame.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.carlt.sesame.data.set.CityStringInfo;
import com.carlt.sesame.utility.Log;

import java.util.ArrayList;

public class CityStringDao extends SQLiteOpenHelper {

	//城市信息表
	public final static String TABLE_CITY = "city_info_table";
	//主键
	public final static String CITY_KEY = "_key";
	//内容
	public final static String CITY_TXT = "_txt";
	//更新时间
	public final static String CITY_TIME = "_time";

	public static final String DATABASE_NAME = "city_db";
	protected Context mContext;

	protected SQLiteDatabase Db;

	public CityStringDao(Context context) {
		super(context, CityStringDao.DATABASE_NAME, null, DaoConfig.DATABASE_VERSION);
		this.mContext = context;
	}

	// 勋章信息表
	private void initTable() {
		// 创建
		StringBuffer sql = new StringBuffer();
		sql.append("CREATE TABLE IF NOT EXISTS ").append(TABLE_CITY).append(" ( ").append(CITY_KEY).append("  integer primary key autoincrement,").append(CITY_TXT)
				.append(" text ,").append(CITY_TIME).append(" text ").append(" )");

		Log.e("info", " ==" + sql.toString());
		Db.execSQL(sql.toString());
	}

	// 插入一条信息
	public boolean insert(CityStringInfo mStrInfo) {
		String s1 = CITY_KEY + "='" + mStrInfo.getId() + "'";
		Db.delete(TABLE_CITY, s1, null);
		return Db.insert(TABLE_CITY, null, getContentValues(mStrInfo)) > -1;
	}

	// 更新入一条信息
	public boolean update(CityStringInfo mStrInfo) {
		String s1 = CITY_KEY + "='" + mStrInfo.getId() + "'";
		int i = Db.update(TABLE_CITY, getContentValues(mStrInfo), s1, null);

		return i > -1;
	}

	public CityStringInfo get(String id) {
		CityStringInfo mInfo = null;
		String s1 = CITY_KEY + "='" + id + "'";
		Cursor cur = Db.query(TABLE_CITY, new String[] { CITY_KEY, CITY_TXT, CITY_TIME }, s1, null, null, null, null);
		if (!cur.moveToFirst()) {
			return null;
		}
		for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
			mInfo = new CityStringInfo();
			mInfo.setId(cur.getInt(0));
			mInfo.setTxt(cur.getString(1));
			mInfo.setTime(cur.getString(2));
		}
		
		cur.close();
		return mInfo;

	}

	public ArrayList<CityStringInfo> get() {
		ArrayList<CityStringInfo> mList = new ArrayList<CityStringInfo>();
		CityStringInfo mInfo = null;
		Cursor cur = Db.query(TABLE_CITY, new String[] { CITY_KEY, CITY_TXT, CITY_TIME }, null, null, null, null, null);
		if (!cur.moveToFirst()) {
			return mList;
		}
		for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
			mInfo = new CityStringInfo();
			mInfo.setId(cur.getInt(0));
			mInfo.setTxt(cur.getString(1));
			mInfo.setTime(cur.getString(2));
			mList.add(mInfo);
		}
		
		cur.close();
		return mList;

	}

	private ContentValues getContentValues(CityStringInfo mStrInfo) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(CITY_KEY, mStrInfo.getId());
		contentValues.put(CITY_TXT, mStrInfo.getTxt());
		contentValues.put(CITY_TIME, mStrInfo.getTime());

		return contentValues;

	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Db = db;
		initTable();
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {

	}

	// 打开
	public void open() throws SQLException {
		Db = getWritableDatabase();
	}

	// 关闭
	public void closeDB() {
		close();
	}
}

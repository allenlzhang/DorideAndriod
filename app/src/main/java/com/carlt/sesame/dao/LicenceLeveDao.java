package com.carlt.sesame.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.carlt.sesame.data.career.LicenceLevelInfo;
import com.carlt.sesame.utility.Log;

import java.util.ArrayList;

public class LicenceLeveDao extends SQLiteOpenHelper {

	// 驾驶证等级信息表
	public final static String TABLE_LICENCE = "licence_leve_table";
	// 主键
	public final static String LICENCE_KEY = "_key";
	// ID
	public final static String LICENCE_ID = "_id";
	// 名称
	public final static String LICENCE_NAME = "_name";
	// 图片2（激活状态）
	public final static String LICENCE_IMG2 = "_img2";
	// 图片1（未激活状态）
	public final static String LICENCE_IMG1 = "_img1";
	// 等级
	public final static String LICENCE_LEVEL = "_level";
	// 分数
	public final static String LICENCE_POINT = "_point";

	public static final String DATABASE_NAME = "licence_db";
	protected Context mContext;

	protected SQLiteDatabase Db;

	public LicenceLeveDao(Context context) {
		super(context, LicenceLeveDao.DATABASE_NAME, null,
				DaoConfig.DATABASE_VERSION);
		this.mContext = context;
	}

	// 驾驶证等级信息表
	private void initTable() {
		// 创建
		StringBuffer sql = new StringBuffer();
		sql.append("CREATE TABLE IF NOT EXISTS ").append(TABLE_LICENCE)
				.append(" ( ").append(LICENCE_KEY)
				.append("  integer primary key autoincrement,")
				.append(LICENCE_ID).append(" text ,").append(LICENCE_NAME)
				.append(" text ,").append(LICENCE_POINT).append(" integer ,")
				.append(LICENCE_LEVEL).append(" integer ,")
				.append(LICENCE_IMG1).append(" text ,").append(LICENCE_IMG2)
				.append(" text ").append(" )");

		Log.e("info", " ==" + sql.toString());
		Db.execSQL(sql.toString());
	}

	// 插入一条体重信息
	public boolean insert(LicenceLevelInfo mLicenceLevelInfo) {
		String s1 = LICENCE_ID + "='" + mLicenceLevelInfo.getId() + "'";
		Db.delete(TABLE_LICENCE, s1, null);
		return Db.insert(TABLE_LICENCE, null,
				getContentValues(mLicenceLevelInfo)) > -1;
	}

	// 更新入一条体重信息
	public boolean update(LicenceLevelInfo mLicenceLevelInfo) {
		String s1 = LICENCE_ID + "='" + mLicenceLevelInfo.getId() + "'";
		int i = Db.update(TABLE_LICENCE, getContentValues(mLicenceLevelInfo),
				s1, null);
		return i > -1;
	}

	public LicenceLevelInfo get(String id) {
		LicenceLevelInfo mLicenceLevelInfo = null;
		String s1 = LICENCE_ID + "='" + id + "'";
		Cursor cur = Db.query(TABLE_LICENCE, new String[] { LICENCE_ID,
				LICENCE_NAME, LICENCE_LEVEL, LICENCE_IMG1, LICENCE_IMG2,
				LICENCE_POINT }, s1, null, null, null, null);
		if (!cur.moveToFirst()) {
			return null;
		}
		for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
			mLicenceLevelInfo = new LicenceLevelInfo();
			mLicenceLevelInfo.setId(cur.getString(0));
			mLicenceLevelInfo.setName(cur.getString(1));
			mLicenceLevelInfo.setLevel(cur.getInt(2));
			mLicenceLevelInfo.setIconUrl1(cur.getString(3));
			mLicenceLevelInfo.setIconUrl2(cur.getString(4));
			mLicenceLevelInfo.setPoint(cur.getInt(5));

		}

		return mLicenceLevelInfo;

	}

	public ArrayList<LicenceLevelInfo> get() {
		ArrayList<LicenceLevelInfo> mList = new ArrayList<LicenceLevelInfo>();
		Cursor cur = Db.query(TABLE_LICENCE, new String[] { LICENCE_ID,
				LICENCE_NAME, LICENCE_LEVEL, LICENCE_IMG1, LICENCE_IMG2,
				LICENCE_POINT }, null, null, null, null, null);
		if (!cur.moveToFirst()) {
			return null;
		}
		for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
			LicenceLevelInfo mLicenceLevelInfo = new LicenceLevelInfo();
			mLicenceLevelInfo.setId(cur.getString(0));
			mLicenceLevelInfo.setName(cur.getString(1));
			mLicenceLevelInfo.setLevel(cur.getInt(2));
			mLicenceLevelInfo.setIconUrl1(cur.getString(3));
			mLicenceLevelInfo.setIconUrl2(cur.getString(4));
			mLicenceLevelInfo.setPoint(cur.getInt(5));
			mList.add(mLicenceLevelInfo);

		}

		return mList;

	}

	private ContentValues getContentValues(LicenceLevelInfo mLicenceLevelInfo) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(LICENCE_ID, mLicenceLevelInfo.getId());
		contentValues.put(LICENCE_NAME, mLicenceLevelInfo.getName());
		contentValues.put(LICENCE_IMG1, mLicenceLevelInfo.getIconUrl1());
		contentValues.put(LICENCE_IMG2, mLicenceLevelInfo.getIconUrl2());
		contentValues.put(LICENCE_LEVEL, mLicenceLevelInfo.getLevel());
		contentValues.put(LICENCE_POINT, mLicenceLevelInfo.getPoint());

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

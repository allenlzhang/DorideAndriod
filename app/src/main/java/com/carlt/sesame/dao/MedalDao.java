package com.carlt.sesame.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.carlt.sesame.data.career.MedalInfo;
import com.carlt.sesame.utility.Log;

import java.util.ArrayList;

public class MedalDao extends SQLiteOpenHelper {

	// 勋章信息表
	public final static String TABLE_MEDAL = "medal_table";
	// 主键
	public final static String MEDAL_KEY = "_key";
	// ID
	public final static String MEDAL_ID = "_id";
	// 名称
	public final static String MEDAL_NAME = "_name";
	// 图片1（未激活状态）
	public final static String MEDAL_IMG1 = "_img1";
	// 图片2（激活状态）
	public final static String MEDAL_IMG2 = "_img2";
	// 等级
	public final static String MEDAL_LEVEL = "_level";
	// 描述
	public final static String MEDAL_DESC = "_desc";
	// 是否获得 0否 1是
	public final static String MEDAL_ISGOT = "_isgot";

	public static final String DATABASE_NAME = "medal_db";
	protected Context mContext;

	protected SQLiteDatabase Db;

	public MedalDao(Context context) {
		super(context, MedalDao.DATABASE_NAME, null, DaoConfig.DATABASE_VERSION);
		this.mContext = context;
	}

	// 勋章信息表
	private void initTable() {
		// 创建
		StringBuffer sql = new StringBuffer();
		sql.append("CREATE TABLE IF NOT EXISTS ").append(TABLE_MEDAL)
				.append(" ( ").append(MEDAL_KEY)
				.append("  integer primary key autoincrement,")
				.append(MEDAL_ID).append(" text ,").append(MEDAL_NAME)
				.append(" text ,").append(MEDAL_DESC).append(" text ,")
				.append(MEDAL_LEVEL).append(" integer ,").append(MEDAL_ISGOT)
				.append(" integer ,").append(MEDAL_IMG1).append(" text ,")
				.append(MEDAL_IMG2).append(" text ").append(" )");

		Log.e("info", " ==" + sql.toString());
		Db.execSQL(sql.toString());
	}

	// 插入一条信息
	public boolean insert(MedalInfo mMedalInfo) {
		String s1 = MEDAL_ID + "='" + mMedalInfo.getId() + "'";
		Db.delete(TABLE_MEDAL, s1, null);
		return Db.insert(TABLE_MEDAL, null, getContentValues(mMedalInfo)) > -1;
	}

	// 更新入一条信息
	public boolean update(MedalInfo mMedalInfo) {
		String s1 = MEDAL_ID + "='" + mMedalInfo.getId() + "'";
		int i = Db.update(TABLE_MEDAL, getContentValues(mMedalInfo), s1, null);

		return i > -1;
	}

	public MedalInfo get(String id) {
		MedalInfo mMedal = null;
		String s1 = MEDAL_ID + "='" + id + "'";
		Cursor cur = Db.query(TABLE_MEDAL, new String[] { MEDAL_ID, MEDAL_NAME,
				MEDAL_LEVEL, MEDAL_IMG1, MEDAL_IMG2, MEDAL_DESC }, s1, null,
				null, null, null);
		if (!cur.moveToFirst()) {
			return null;
		}
		for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
			mMedal = new MedalInfo();
			mMedal.setId(cur.getString(0));
			mMedal.setName(cur.getString(1));
			mMedal.setLevel(cur.getInt(2));
			mMedal.setIconUrl1(cur.getString(3));
			mMedal.setIconUrl2(cur.getString(4));
			mMedal.setIsgot(false);
			mMedal.setDescription(cur.getString(5));

		}
		return mMedal;

	}

	public ArrayList<MedalInfo> get() {
		ArrayList<MedalInfo> mList = new ArrayList<MedalInfo>();
		MedalInfo mMedal = null;
		Cursor cur = Db.query(TABLE_MEDAL, new String[] { MEDAL_ID, MEDAL_NAME,
				MEDAL_LEVEL, MEDAL_IMG1, MEDAL_IMG2, MEDAL_DESC }, null, null,
				null, null, null);
		if (!cur.moveToFirst()) {
			return mList;
		}
		for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
			mMedal = new MedalInfo();
			mMedal.setId(cur.getString(0));
			mMedal.setName(cur.getString(1));
			mMedal.setLevel(cur.getInt(2));
			mMedal.setIconUrl1(cur.getString(3));
			mMedal.setIconUrl2(cur.getString(4));
			mMedal.setIsgot(false);

			mMedal.setDescription(cur.getString(5));
			mList.add(mMedal);
		}
		return mList;

	}

	private ContentValues getContentValues(MedalInfo mMedal) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(MEDAL_ID, mMedal.getId());
		contentValues.put(MEDAL_NAME, mMedal.getName());
		contentValues.put(MEDAL_IMG1, mMedal.getIconUrl1());
		contentValues.put(MEDAL_IMG2, mMedal.getIconUrl2());
		contentValues.put(MEDAL_LEVEL, mMedal.getLevel());
		contentValues.put(MEDAL_DESC, mMedal.getDescription());

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

package com.carlt.sesame.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.carlt.sesame.data.car.PostViolationInfo;
import com.carlt.sesame.utility.Log;

import java.util.ArrayList;

public class IllegalInfoDao extends SQLiteOpenHelper {

	// 城市信息表
	public final static String TABLE_ILLEGAL = "illegal_info_table";
	// 主键
	public final static String ILLEGAL_KEY = "_id";
	// 内容
	public final static String ILLEGAL_TXT = "_txt";
	// 更新时间
	public final static String ILLEGAL_TIME = "_time";
	// 城市ID
	public final static String ILLEGAL_CITYCODEID = "_cityCodeId";
	// 车牌号码 7位
	public final static String ILLEGAL_CARNO = "_carno";
	// 发动机号
	public final static String ILLEGAL_ENGINENO = "_engineno";
	// 车架号
	public final static String ILLEGAL_STANDCARNO = "_standcarno";
	// 登记证书号
	public final static String ILLEGAL_REGISTNO = "_registno";

	public static final String DATABASE_NAME = "illegal_db";
	protected Context mContext;
	protected SQLiteDatabase Db;

	public IllegalInfoDao(Context context) {
		super(context, IllegalInfoDao.DATABASE_NAME, null, DaoConfig.DATABASE_VERSION);
		this.mContext = context;
	}

	// 勋章信息表
	private void initTable() {
		// 创建
		StringBuffer sql = new StringBuffer();
		sql.append("CREATE TABLE IF NOT EXISTS ").append(TABLE_ILLEGAL).append(" ( ").append(ILLEGAL_KEY).append("  integer primary key autoincrement,").append(ILLEGAL_TXT)
				.append(" text ,").append(ILLEGAL_TIME).append(" text ,").append(ILLEGAL_CITYCODEID).append(" text ,").append(ILLEGAL_CARNO).append(" text ,").append(ILLEGAL_ENGINENO)
				.append(" text ,").append(ILLEGAL_STANDCARNO).append(" text ,").append(ILLEGAL_REGISTNO).append(" text ").append(" )");

		Log.e("info", " ==" + sql.toString());
		Db.execSQL(sql.toString());
		Log.e("info", "表创建成功  ==" + sql.toString());
	}

	// 插入一条信息
	public boolean insert(PostViolationInfo mStrInfo) {
		String s1 = ILLEGAL_CARNO + "='" + mStrInfo.getCarno() + "'";
		Db.delete(TABLE_ILLEGAL, s1, null);
		return Db.insert(TABLE_ILLEGAL, null, getContentValues(mStrInfo)) > -1;
	}

	// 更新入一条信息
	public boolean update(PostViolationInfo mStrInfo) {
		String s1 = ILLEGAL_CARNO + "='" + mStrInfo.getCarno() + "'";
		int i = Db.update(TABLE_ILLEGAL, getContentValues(mStrInfo), s1, null);

		return i > -1;
	}

	public PostViolationInfo get(String carno) {
		PostViolationInfo mInfo = null;
		String s1 = ILLEGAL_CARNO + "= ?";
		Cursor cur = Db.query(TABLE_ILLEGAL, null, s1, new String[] { carno }, null, null, null);
		if (!cur.moveToFirst()) {
			return null;
		}

		for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
			mInfo = new PostViolationInfo();
			int index = 0;
			mInfo.setId(cur.getInt(index++));
			mInfo.setInfos(cur.getString(index++));
			mInfo.setTime(cur.getLong(index++));
			mInfo.setCityCodeId(cur.getString(index++));
			mInfo.setCarno(cur.getString(index++));
			mInfo.setEngineno(cur.getString(index++));
			mInfo.setStandcarno(cur.getString(index++));
			mInfo.setRegistno(cur.getString(index++));
		}

		cur.close();
		return mInfo;

	}

	public ArrayList<PostViolationInfo> get() {
		ArrayList<PostViolationInfo> mList = new ArrayList<PostViolationInfo>();
		PostViolationInfo mInfo = null;
		Cursor cur = Db.query(TABLE_ILLEGAL, null, null, null, null, null, null);

		if (!cur.moveToFirst()) {
			return mList;
		}

		for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
			int index = 0;
			mInfo = new PostViolationInfo();
			mInfo.setId(cur.getInt(index++));
			mInfo.setInfos(cur.getString(index++));
			mInfo.setTime(cur.getLong(index++));
			mInfo.setCityCodeId(cur.getString(index++));
			mInfo.setCarno(cur.getString(index++));
			mInfo.setEngineno(cur.getString(index++));
			mInfo.setStandcarno(cur.getString(index++));
			mInfo.setRegistno(cur.getString(index++));
			mList.add(mInfo);
		}

		cur.close();
		return mList;

	}
	
	public boolean delete(String carNo) {
		String s1 = ILLEGAL_CARNO + "='" + carNo + "'";
		return Db.delete(TABLE_ILLEGAL, s1 , null) > 0;
	}

	private ContentValues getContentValues(PostViolationInfo mInfo) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(ILLEGAL_TXT, mInfo.getInfos());
		contentValues.put(ILLEGAL_TIME, mInfo.getTime());
		contentValues.put(ILLEGAL_CITYCODEID, mInfo.getCityCodeId());
		contentValues.put(ILLEGAL_CARNO, mInfo.getCarno());
		contentValues.put(ILLEGAL_ENGINENO, mInfo.getEngineno());
		contentValues.put(ILLEGAL_STANDCARNO, mInfo.getStandcarno());
		contentValues.put(ILLEGAL_REGISTNO, mInfo.getRegistno());

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

package com.carlt.chelepie.dao;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public abstract class BaseDao extends SQLiteOpenHelper {

	protected Context mContext;

	protected SQLiteDatabase Db;

	protected String TAG = "DEBUG||" + this.getClass().getSimpleName();

	public BaseDao(Context context, String name, CursorFactory factory, int version) {
		super(context, name, null, version);
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

	// 打开
	public void open() throws SQLException {
		Db = getWritableDatabase();
	}

	// 关闭
	public void closeDB() {
		close();
	}

	protected abstract void initTable();

}

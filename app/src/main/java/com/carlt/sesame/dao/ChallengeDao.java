
package com.carlt.sesame.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.carlt.sesame.data.career.ChallengeInfo;
import com.carlt.sesame.utility.Log;

import java.util.ArrayList;

public class ChallengeDao extends SQLiteOpenHelper {

    // 挑战信息表
    public final static String TABLE_CHALLENGE = "challenge_table";

    // 主键
    public final static String CHALLENGE_KEY = "_key";

    // ID
    public final static String CHALLENGE_ID = "_id";

    // 名称
    public final static String CHALLENGE_NAME = "_name";

    // 类型
    public final static String CHALLENGE_TYPE = "_type";

    public final static String CHALLENGE_FUEL = "_fuel";

    public final static String CHALLENGE_MILES = "_miles";

    public final static String CHALLENGE_TIME = "_time";

    public final static String CHALLENGE_SPEED = "_speed";

    public final static String CHALLENGE_POINT = "_point";

    public final static String CHALLENGE_INFO = "_info";

    public final static String CHALLENGE_SORT = "_sort";

    public final static String CHALLENGE_STATUS = "_status";

    public static final String DATABASE_NAME = "challenge_db";

    protected Context mContext;

    protected SQLiteDatabase Db;

    public ChallengeDao(Context context) {
        super(context, ChallengeDao.DATABASE_NAME, null, DaoConfig.DATABASE_VERSION);
        this.mContext = context;
    }

    // 挑战信息表
    private void initTable() {
        // 创建
        StringBuffer sql = new StringBuffer();
        sql.append("CREATE TABLE IF NOT EXISTS ").append(TABLE_CHALLENGE).append(" ( ")
                .append(CHALLENGE_KEY).append("  integer primary key autoincrement,")
                .append(CHALLENGE_ID).append(" text ,").append(CHALLENGE_NAME).append(" text ,")
                .append(CHALLENGE_TYPE).append(" integer ,").append(CHALLENGE_FUEL)
                .append(" text DEFAULT 'nul',").append(CHALLENGE_MILES)
                .append(" text DEFAULT 'nul',").append(CHALLENGE_TIME)
                .append(" text DEFAULT 'nul',").append(CHALLENGE_SPEED)
                .append(" text DEFAULT 'nul',").append(CHALLENGE_INFO)
                .append(" text DEFAULT 'nul',").append(CHALLENGE_POINT)
                .append(" text DEFAULT 'nul',").append(CHALLENGE_STATUS)
                .append(" text DEFAULT 'nul',").append(CHALLENGE_SORT).append(" integer ")
                .append(" )");

        Log.e("info", " ==" + sql.toString());
        Db.execSQL(sql.toString());
    }

    // 插入
    public boolean insert(ChallengeInfo mChallengeInfo) {
        String s1 = CHALLENGE_ID + "='" + mChallengeInfo.getId() + "'";
        Db.delete(TABLE_CHALLENGE, s1, null);
        return Db.insert(TABLE_CHALLENGE, null, getContentValues(mChallengeInfo)) > -1;
    }

    // 更新
    public boolean update(ChallengeInfo mChallengeInfo) {
        String s1 = CHALLENGE_ID + "='" + mChallengeInfo.getId() + "'";
        int i = Db.update(TABLE_CHALLENGE, getContentValues(mChallengeInfo), s1, null);
        return i > -1;
    }

    public ChallengeInfo get(String id) {
        ChallengeInfo mChallengeInfo = null;
        String s1 = CHALLENGE_ID + "='" + id + "'";
        Cursor cur = Db.query(TABLE_CHALLENGE, new String[] {
                CHALLENGE_ID, CHALLENGE_NAME, CHALLENGE_TYPE, CHALLENGE_FUEL, CHALLENGE_MILES,
                CHALLENGE_TIME, CHALLENGE_SPEED, CHALLENGE_POINT, CHALLENGE_SORT, CHALLENGE_INFO,
                CHALLENGE_STATUS
        }, s1, null, null, null, null);
        if (!cur.moveToFirst()) {
            return null;

        }
        for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
            mChallengeInfo = new ChallengeInfo();
            mChallengeInfo.setId(cur.getString(0));
            mChallengeInfo.setName(cur.getString(1));
            mChallengeInfo.setType(cur.getInt(2));
            mChallengeInfo.setFuel(cur.getString(3));
            mChallengeInfo.setMiles(cur.getString(4));
            mChallengeInfo.setTime(cur.getString(5));
            mChallengeInfo.setSpeed(cur.getString(6));
            mChallengeInfo.setPoint(cur.getString(7));
            mChallengeInfo.setSort(cur.getInt(8));
            mChallengeInfo.setInfo(cur.getString(9));
            mChallengeInfo.setStatus(cur.getString(10));

        }

        return mChallengeInfo;

    }

    public ArrayList<ChallengeInfo> get() {
        ArrayList<ChallengeInfo> mList = new ArrayList<ChallengeInfo>();
        Cursor cur = Db.query(TABLE_CHALLENGE, new String[] {
                CHALLENGE_ID, CHALLENGE_NAME, CHALLENGE_TYPE, CHALLENGE_FUEL, CHALLENGE_MILES,
                CHALLENGE_TIME, CHALLENGE_SPEED, CHALLENGE_POINT, CHALLENGE_SORT, CHALLENGE_INFO,
                CHALLENGE_STATUS
        }, null, null, null, null, null);
        if (!cur.moveToFirst()) {
            return null;

        }
        for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
            ChallengeInfo mChallengeInfo = new ChallengeInfo();
            mChallengeInfo.setId(cur.getString(0));
            mChallengeInfo.setName(cur.getString(1));
            mChallengeInfo.setType(cur.getInt(2));
            mChallengeInfo.setFuel(cur.getString(3));
            mChallengeInfo.setMiles(cur.getString(4));
            mChallengeInfo.setTime(cur.getString(5));
            mChallengeInfo.setSpeed(cur.getString(6));
            mChallengeInfo.setPoint(cur.getString(7));
            mChallengeInfo.setSort(cur.getInt(8));
            mChallengeInfo.setInfo(cur.getString(9));
            mChallengeInfo.setStatus(cur.getString(10));

            mList.add(mChallengeInfo);
        }

        return mList;

    }

    private ContentValues getContentValues(ChallengeInfo mChallengeInfo) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(CHALLENGE_ID, mChallengeInfo.getId());
        contentValues.put(CHALLENGE_NAME, mChallengeInfo.getName());
        contentValues.put(CHALLENGE_TYPE, mChallengeInfo.getType());
        contentValues.put(CHALLENGE_FUEL, mChallengeInfo.getFuel());
        contentValues.put(CHALLENGE_MILES, mChallengeInfo.getMiles());
        contentValues.put(CHALLENGE_TIME, mChallengeInfo.getTime());
        contentValues.put(CHALLENGE_SPEED, mChallengeInfo.getSpeed());
        contentValues.put(CHALLENGE_POINT, mChallengeInfo.getPoint());
        contentValues.put(CHALLENGE_SORT, mChallengeInfo.getSort());
        contentValues.put(CHALLENGE_INFO, mChallengeInfo.getInfo());
        contentValues.put(CHALLENGE_STATUS, mChallengeInfo.getStatus());

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

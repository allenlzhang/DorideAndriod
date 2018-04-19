package com.carlt.doride.dao;

import android.app.Application;

import com.carlt.doride.dao.db.DbManagerImpl;
import com.carlt.doride.dao.db.IDbManager;


/**
 * DB 管理统一入口
 */
public final class DBManager {
    private static Application mApp;

    private DBManager() {
    }

    public static void init(Application app){
        mApp = app;
    }

    public static Application app() {
        if (mApp == null) {
            throw new NullPointerException("DBManager need Application !!!!");
        }
        return mApp;
    }

    public static IDbManager getDb(IDbManager.DaoConfig daoConfig) {
        return DbManagerImpl.getInstance(daoConfig);
    }


    public static IDbManager getCarModelDb(){
        IDbManager.DaoConfig config = new IDbManager.DaoConfig();
        config.setDbName("Yema_CarInfo.db");
        config.setDbVersion(1);
        config.setAllowTransaction(true);
        return getDb(config);
    }


}

package com.birdex.bird.greendao;

import android.database.sqlite.SQLiteDatabase;

import com.birdex.bird.MyApplication;

/**
 * Created by hyj on 2016/4/19.
 */
public class DaoUtils {

    //设置数据库的操作
    private static SQLiteDatabase db=null;
    private static DaoMaster daoMaster=null;
    private static DaoSession daoSession=null;

    public static DaoSession getDaoSession(){
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(MyApplication.getInstans(), "Bird", null);
        db = helper.getWritableDatabase();
        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
        return daoSession;
    }

}

package com.birdex.bird.greendao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import de.greenrobot.dao.AbstractDaoMaster;
import de.greenrobot.dao.identityscope.IdentityScopeType;

import com.birdex.bird.greendao.NotifiMsgDao;
import com.birdex.bird.greendao.warehouseDao;
import com.birdex.bird.greendao.marketDao;
import com.birdex.bird.greendao.priceUnitDao;
import com.birdex.bird.greendao.servicetypeDao;
import com.birdex.bird.greendao.boxDao;
import com.birdex.bird.greendao.qgmodelDao;
import com.birdex.bird.greendao.tapeDao;
import com.birdex.bird.greendao.businessmodelDao;
import com.birdex.bird.greendao.ticketDao;
import com.birdex.bird.greendao.cityDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * Master of DAO (schema version 1): knows all DAOs.
*/
public class DaoMaster extends AbstractDaoMaster {
    public static final int SCHEMA_VERSION = 1;

    /** Creates underlying database table using DAOs. */
    public static void createAllTables(SQLiteDatabase db, boolean ifNotExists) {
        NotifiMsgDao.createTable(db, ifNotExists);
        warehouseDao.createTable(db, ifNotExists);
        marketDao.createTable(db, ifNotExists);
        priceUnitDao.createTable(db, ifNotExists);
        servicetypeDao.createTable(db, ifNotExists);
        boxDao.createTable(db, ifNotExists);
        qgmodelDao.createTable(db, ifNotExists);
        tapeDao.createTable(db, ifNotExists);
        businessmodelDao.createTable(db, ifNotExists);
        ticketDao.createTable(db, ifNotExists);
        cityDao.createTable(db, ifNotExists);
    }
    
    /** Drops underlying database table using DAOs. */
    public static void dropAllTables(SQLiteDatabase db, boolean ifExists) {
        NotifiMsgDao.dropTable(db, ifExists);
        warehouseDao.dropTable(db, ifExists);
        marketDao.dropTable(db, ifExists);
        priceUnitDao.dropTable(db, ifExists);
        servicetypeDao.dropTable(db, ifExists);
        boxDao.dropTable(db, ifExists);
        qgmodelDao.dropTable(db, ifExists);
        tapeDao.dropTable(db, ifExists);
        businessmodelDao.dropTable(db, ifExists);
        ticketDao.dropTable(db, ifExists);
        cityDao.dropTable(db, ifExists);
    }
    
    public static abstract class OpenHelper extends SQLiteOpenHelper {

        public OpenHelper(Context context, String name, CursorFactory factory) {
            super(context, name, factory, SCHEMA_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.i("greenDAO", "Creating tables for schema version " + SCHEMA_VERSION);
            createAllTables(db, false);
        }
    }
    
    /** WARNING: Drops all table on Upgrade! Use only during development. */
    public static class DevOpenHelper extends OpenHelper {
        public DevOpenHelper(Context context, String name, CursorFactory factory) {
            super(context, name, factory);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.i("greenDAO", "Upgrading schema from version " + oldVersion + " to " + newVersion + " by dropping all tables");
            dropAllTables(db, true);
            onCreate(db);
        }
    }

    public DaoMaster(SQLiteDatabase db) {
        super(db, SCHEMA_VERSION);
        registerDaoClass(NotifiMsgDao.class);
        registerDaoClass(warehouseDao.class);
        registerDaoClass(marketDao.class);
        registerDaoClass(priceUnitDao.class);
        registerDaoClass(servicetypeDao.class);
        registerDaoClass(boxDao.class);
        registerDaoClass(qgmodelDao.class);
        registerDaoClass(tapeDao.class);
        registerDaoClass(businessmodelDao.class);
        registerDaoClass(ticketDao.class);
        registerDaoClass(cityDao.class);
    }
    
    public DaoSession newSession() {
        return new DaoSession(db, IdentityScopeType.Session, daoConfigMap);
    }
    
    public DaoSession newSession(IdentityScopeType type) {
        return new DaoSession(db, type, daoConfigMap);
    }
    
}

package com.birdex.bird.greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.birdex.bird.greendao.city;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "CITY".
*/
public class cityDao extends AbstractDao<city, Long> {

    public static final String TABLENAME = "CITY";

    /**
     * Properties of entity city.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Area = new Property(1, String.class, "Area", false, "AREA");
        public final static Property AreaID = new Property(2, String.class, "AreaID", false, "AREA_ID");
        public final static Property AreaName = new Property(3, String.class, "AreaName", false, "AREA_NAME");
        public final static Property ParentID = new Property(4, String.class, "ParentID", false, "PARENT_ID");
    };


    public cityDao(DaoConfig config) {
        super(config);
    }
    
    public cityDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"CITY\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: id
                "\"AREA\" TEXT," + // 1: Area
                "\"AREA_ID\" TEXT," + // 2: AreaID
                "\"AREA_NAME\" TEXT," + // 3: AreaName
                "\"PARENT_ID\" TEXT);"); // 4: ParentID
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"CITY\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, city entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String Area = entity.getArea();
        if (Area != null) {
            stmt.bindString(2, Area);
        }
 
        String AreaID = entity.getAreaID();
        if (AreaID != null) {
            stmt.bindString(3, AreaID);
        }
 
        String AreaName = entity.getAreaName();
        if (AreaName != null) {
            stmt.bindString(4, AreaName);
        }
 
        String ParentID = entity.getParentID();
        if (ParentID != null) {
            stmt.bindString(5, ParentID);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public city readEntity(Cursor cursor, int offset) {
        city entity = new city( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // Area
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // AreaID
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // AreaName
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4) // ParentID
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, city entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setArea(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setAreaID(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setAreaName(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setParentID(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(city entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(city entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}
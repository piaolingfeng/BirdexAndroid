package com.birdex.bird.greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.birdex.bird.greendao.market;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "MARKET".
*/
public class marketDao extends AbstractDao<market, Void> {

    public static final String TABLENAME = "MARKET";

    /**
     * Properties of entity market.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, String.class, "id", false, "ID");
        public final static Property Status = new Property(1, String.class, "status", false, "STATUS");
        public final static Property Display_order = new Property(2, String.class, "display_order", false, "DISPLAY_ORDER");
        public final static Property Name = new Property(3, String.class, "name", false, "NAME");
    };


    public marketDao(DaoConfig config) {
        super(config);
    }
    
    public marketDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"MARKET\" (" + //
                "\"ID\" TEXT," + // 0: id
                "\"STATUS\" TEXT," + // 1: status
                "\"DISPLAY_ORDER\" TEXT," + // 2: display_order
                "\"NAME\" TEXT);"); // 3: name
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"MARKET\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, market entity) {
        stmt.clearBindings();
 
        String id = entity.getId();
        if (id != null) {
            stmt.bindString(1, id);
        }
 
        String status = entity.getStatus();
        if (status != null) {
            stmt.bindString(2, status);
        }
 
        String display_order = entity.getDisplay_order();
        if (display_order != null) {
            stmt.bindString(3, display_order);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(4, name);
        }
    }

    /** @inheritdoc */
    @Override
    public Void readKey(Cursor cursor, int offset) {
        return null;
    }    

    /** @inheritdoc */
    @Override
    public market readEntity(Cursor cursor, int offset) {
        market entity = new market( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // status
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // display_order
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3) // name
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, market entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setStatus(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setDisplay_order(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setName(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
     }
    
    /** @inheritdoc */
    @Override
    protected Void updateKeyAfterInsert(market entity, long rowId) {
        // Unsupported or missing PK type
        return null;
    }
    
    /** @inheritdoc */
    @Override
    public Void getKey(market entity) {
        return null;
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}

package com.birdex.bird.greendao;

import android.database.sqlite.SQLiteDatabase;

import com.birdex.bird.MyApplication;
import com.birdex.bird.entity.City;
import com.birdex.bird.entity.Contact;
import com.birdex.bird.util.Constant;

import java.util.List;

import de.greenrobot.dao.query.QueryBuilder;

/**
 * Created by hyj on 2016/4/19.
 */
public class DaoUtils {

    //设置数据库的操作
    private static SQLiteDatabase db=null;
    private static DaoMaster daoMaster=null;
    private static DaoSession daoSession=null;

    private static cityDao cityDao;
    private static marketDao marketDao;
    private static businessmodelDao businessmodelDao;
    private static qgmodelDao qgmodelDao;

    public static DaoSession getDaoSession(){
        if(daoSession == null) {
            DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(MyApplication.getInstans(), Constant.DBName, null);
            db = helper.getWritableDatabase();
            daoMaster = new DaoMaster(db);
            daoSession = daoMaster.newSession();
        }
        return daoSession;
    }

    public static City getcity(){
        cityDao = DaoUtils.getDaoSession().getCityDao();
        // 取得省
        QueryBuilder<city> queryBuilder = cityDao.queryBuilder().where(com.birdex.bird.greendao.cityDao.Properties.Area.eq("1"));
        List<city> p = queryBuilder.list();
        // 取得 市
        queryBuilder = cityDao.queryBuilder().where(com.birdex.bird.greendao.cityDao.Properties.Area.eq("2"));
        List<city> c = queryBuilder.list();
        // 取得 区
        queryBuilder = cityDao.queryBuilder().where(com.birdex.bird.greendao.cityDao.Properties.Area.eq("3"));
        List<city> a = queryBuilder.list();

        City myCity = new City();
        myCity.setAreas(a);
        myCity.setCities(c);
        myCity.setProvinces(p);
        return myCity;
    }

    public static List<market> getmarkest(){
        marketDao = DaoUtils.getDaoSession().getMarketDao();

        // 取得所有的 market
        QueryBuilder<market> queryBuilder = marketDao.queryBuilder();
        return queryBuilder.list();
    }

    public static List<businessmodel> getbusinessmodel(){
        businessmodelDao = DaoUtils.getDaoSession().getBusinessmodelDao();

        QueryBuilder<businessmodel> queryBuilder = businessmodelDao.queryBuilder();
        return queryBuilder.list();
    }

    public static List<qgmodel> getqgmodel() {
        qgmodelDao = DaoUtils.getDaoSession().getQgmodelDao();

        QueryBuilder<qgmodel> qgmodelQueryBuilder = qgmodelDao.queryBuilder();
        return qgmodelQueryBuilder.list();
    }
}

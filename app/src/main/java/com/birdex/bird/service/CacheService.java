package com.birdex.bird.service;

import android.app.Service;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.birdex.bird.MyApplication;
import com.birdex.bird.api.BirdApi;
import com.birdex.bird.entity.Area;
import com.birdex.bird.entity.Category;
import com.birdex.bird.entity.City;
import com.birdex.bird.entity.ServiceType;
import com.birdex.bird.entity.ServiceTypes;
import com.birdex.bird.greendao.DaoMaster;
import com.birdex.bird.greendao.DaoSession;
import com.birdex.bird.greendao.box;
import com.birdex.bird.greendao.boxDao;
import com.birdex.bird.greendao.businessmodel;
import com.birdex.bird.greendao.businessmodelDao;
import com.birdex.bird.greendao.city;
import com.birdex.bird.greendao.cityDao;
import com.birdex.bird.greendao.market;
import com.birdex.bird.greendao.marketDao;
import com.birdex.bird.greendao.priceUnit;
import com.birdex.bird.greendao.priceUnitDao;
import com.birdex.bird.greendao.qgmodel;
import com.birdex.bird.greendao.qgmodelDao;
import com.birdex.bird.greendao.servicetype;
import com.birdex.bird.greendao.servicetypeDao;
import com.birdex.bird.greendao.tape;
import com.birdex.bird.greendao.tapeDao;
import com.birdex.bird.greendao.ticket;
import com.birdex.bird.greendao.ticketDao;
import com.birdex.bird.greendao.warehouse;
import com.birdex.bird.greendao.warehouseDao;
import com.birdex.bird.util.JsonHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by hyj on 2016/4/18.
 */
public class CacheService extends Service {

    private List<warehouse> warehouseList = new ArrayList<>();
    private List<market> marketList = new ArrayList<>();
    private List<priceUnit> priceUnitList = new ArrayList<>();
    private List<ServiceTypes> serviceTypesList = new ArrayList<>();
    private List<box> boxList = new ArrayList<>();
    private List<qgmodel> qgModels = new ArrayList<>();
    // 先不做这个
    private List<Category> categoryList = new ArrayList<>();

    private List<tape> tapeList = new ArrayList<>();
    private List<businessmodel> businessModelList = new ArrayList<>();
    private List<ticket> ticketList = new ArrayList<>();
    private City cityList;

    //设置数据库的操作
    private SQLiteDatabase db=null;
    private DaoMaster daoMaster=null;
    private DaoSession daoSession=null;

    private marketDao marketDao;
    private businessmodelDao businessmodelDao;
    private qgmodelDao qgmodelDao;
    private warehouseDao warehouseDao;
    private cityDao cityDao;
    private priceUnitDao priceUnitDao;
    private boxDao boxDao;
    private tapeDao tapeDao;
    private ticketDao ticketDao;
    private servicetypeDao servicetypeDao;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 执行访问接口，数据库插入操作
        saveData();
        return super.onStartCommand(intent, flags, startId);
    }


    private void insertData(){

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "Bird", null);
        db = helper.getWritableDatabase();
        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();

        insertMarket();
        insertBusinessModel();
        insertQgModel();
        insertWarehouses();
        insertArea();
        insertPrice();
        insertCategories();
        insertBox();
        insertTapes();
        insertTickets();
        insertService();
    }

    private void insertService(){
        servicetypeDao = daoSession.getServicetypeDao();
        if(serviceTypesList.size()>0){
            for(ServiceTypes s:serviceTypesList){
                List<ServiceType> list = s.getServiceTypeList();
                for(ServiceType serviceType:list){
                    servicetype st = new servicetype();
                    st.setKey(s.getKey());
                    st.setId(serviceType.getId());
                    st.setIs_show(serviceType.getIs_show());
                    st.setName(serviceType.getName());
                    servicetypeDao.insert(st);
                }
            }
        }
    }

    private void insertTickets(){
        ticketDao = daoSession.getTicketDao();
        if(ticketList.size()>0){
            for(ticket t:ticketList){
                ticketDao.insert(t);
            }
        }
    }

    private void insertTapes(){
        tapeDao = daoSession.getTapeDao();
        if(tapeList.size()>0){
            for(tape t:tapeList){
                tapeDao.insert(t);
            }
        }
    }

    private void insertBox(){
        boxDao = daoSession.getBoxDao();
        if(boxList.size()>0){
            for(box b:boxList){
                boxDao.insert(b);
            }
        }
    }

    private void insertCategories(){
        // 先不做

    }

    private void insertPrice(){
        priceUnitDao = daoSession.getPriceUnitDao();

        if(priceUnitList.size()>0){
            for(priceUnit p:priceUnitList){
                priceUnitDao.insert(p);
            }
        }
    }

    private void insertArea(){
        cityDao = daoSession.getCityDao();

        if(cityList != null){
            List<Area> provinces = cityList.getProvinces();
            for(Area p:provinces){
                city c = new city();
                c.setAreaID(p.getAreaID());
                c.setAreaName(p.getAreaName());
                c.setParentID(p.getParentID());
                cityDao.insert(c);
            }
            List<Area> cities = cityList.getCities();
            for(Area p:cities){
                city c = new city();
                c.setAreaID(p.getAreaID());
                c.setAreaName(p.getAreaName());
                c.setParentID(p.getParentID());
                cityDao.insert(c);
            }
            List<Area> areas = cityList.getAreas();
            for(Area p:areas){
                city c = new city();
                c.setAreaID(p.getAreaID());
                c.setAreaName(p.getAreaName());
                c.setParentID(p.getParentID());
                cityDao.insert(c);
            }
        }
    }

    private void insertWarehouses(){
        warehouseDao = daoSession.getWarehouseDao();

        if(warehouseList.size()>0){
            for(warehouse w:warehouseList){
                warehouseDao.insert(w);
            }
        }
    }

    private void insertQgModel(){
        qgmodelDao = daoSession.getQgmodelDao();

        if(qgModels.size()>0){
            for(qgmodel q:qgModels){
                qgmodelDao.insert(q);
            }
        }
    }

    private void insertBusinessModel(){
        businessmodelDao = daoSession.getBusinessmodelDao();

        if(businessModelList.size()>0){
            for(businessmodel b:businessModelList){
                businessmodelDao.insert(b);
            }
        }
    }


    private void insertMarket(){
        marketDao = daoSession.getMarketDao();

        if(marketList.size()>0){
            for(market mar:marketList){
                marketDao.insert(mar);
            }
        }
    }

    private void saveData(){
        // 先获取接口数据
        RequestParams params = new RequestParams();
        // 币种
        params.add("price_units","RMB");
        // 分类
        params.add("categories","test");
        // 主营市场
        params.add("markets","test");
        // 业务模式
        params.add("business_models","test");
        // 清关模式
        params.add("qg_models","test");
        // 包装箱使用类型
        params.add("boxs","test");
        // 包装胶带使用要求
        params.add("tapes","test");
        // 小票纸使用要求
        params.add("tickets","test");
        // 渠道服务方式
        params.add("service_types","test");
        // 仓库
        params.add("warehouses","test");
        // 获取城市配置（包括省，市，区县）
        params.add("city","Beijing");

        BirdApi.getConfig(MyApplication.getInstans(),params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    if("0".equals(response.getString("error"))){
                        JSONObject data = (JSONObject) response.get("data");
                        JSONObject warehouses = (JSONObject) data.get("warehouses");
                        setWarehouses(warehouses);

                        JSONObject markets = (JSONObject) data.get("markets");
                        setMarkets(markets);

                        JSONArray priceUnits = (JSONArray) data.get("price_units");
                        setPriceUnits(priceUnits);

                        JSONObject serviceTypes = (JSONObject) data.get("service_types");
                        setServiceTypes(serviceTypes);

                        JSONObject boxs = (JSONObject) data.get("boxs");
                        setBoxs(boxs);

                        JSONObject qgModel = (JSONObject) data.get("qg_models");
                        setQgModel(qgModel);

//                        JSONObject categories = (JSONObject) data.get("categories");
//                        setCategories(categories);

                        JSONObject tapes = (JSONObject) data.get("tapes");
                        setTapes(tapes);

                        JSONObject businessModels = (JSONObject) data.get("business_models");
                        setBusinessModels(businessModels);

                        JSONObject tickets = (JSONObject) data.get("tickets");
                        setTickets(tickets);

                        JSONObject city = (JSONObject) data.get("city");
                        cityList = JsonHelper.parseObject(city, City.class);

                        // 执行数据插入操作
                        insertData();

                    } else {
                        // 获取失败

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    private void setWarehouses(JSONObject warehouses) throws JSONException {
        if(warehouses != null) {
            Iterator<String> warehousesIter = warehouses.keys();
            while (warehousesIter.hasNext()) {
                String warehouseStr = warehousesIter.next();
                JSONObject mm = (JSONObject) warehouses.get(warehouseStr);
                warehouse warehouse = JsonHelper.parseObject(mm,warehouse.class);
                warehouseList.add(warehouse);
            }
        }
    }

    private void setMarkets(JSONObject markets) throws JSONException {
        Iterator<String> marketIter = markets.keys();
        while (marketIter.hasNext()) {
            String ma = marketIter.next();
            JSONObject mm = (JSONObject) markets.get(ma);
            market market1 = JsonHelper.parseObject(mm, market.class);
            marketList.add(market1);
        }
    }

    private void setPriceUnits(JSONArray priceUnits) throws JSONException {
        for(int i=0;i<priceUnits.length();i++){
            JSONObject jb = priceUnits.getJSONObject(i);
            priceUnit priceUnit = JsonHelper.parseObject(jb,priceUnit.class);
            priceUnitList.add(priceUnit);
        }
    }

    private void setServiceTypes(JSONObject serviceTypes) throws JSONException{
        Iterator<String> stringIterator = serviceTypes.keys();
        while (stringIterator.hasNext()){
            ServiceTypes types = new ServiceTypes();
            String ser = stringIterator.next();
            JSONArray mm = (JSONArray) serviceTypes.get(ser);
            List<ServiceType> serviceTypes1 = new ArrayList<>();
            for(int i=0;i<mm.length();i++){
                JSONObject jb = mm.getJSONObject(i);
                ServiceType serviceType = JsonHelper.parseObject(jb,ServiceType.class);
                serviceTypes1.add(serviceType);
            }
            types.setKey(ser);
            types.setServiceTypeList(serviceTypes1);

            serviceTypesList.add(types);
        }
    }

    private void setBoxs(JSONObject boxs) throws JSONException {
        Iterator<String> boxIt = boxs.keys();
        while (boxIt.hasNext()){
            String key = boxIt.next();
            JSONObject jb = boxs.getJSONObject(key);
            box box = JsonHelper.parseObject(jb,box.class);
            boxList.add(box);
        }
    }

    private void setQgModel(JSONObject qgModel) throws JSONException {
        Iterator<String> models = qgModel.keys();
        while (models.hasNext()) {
            String mo = models.next();
            JSONObject moo = (JSONObject) qgModel.get(mo);
            qgModels.add(JsonHelper.parseObject(moo, qgmodel.class));
        }
    }

    private void setCategories(JSONObject categories) throws JSONException {
//        Iterator<String> catIt = categories.keys();
//        while (catIt.hasNext()){
//            String key = catIt.next();
//            JSONObject jb = categories.getJSONObject(key);
//
//            List<Category>  list = gson2list(jb.toString());
//            System.out.println(list);
//
//        }

        categoryList = gsonCategoryList(categories.toString());
    }

    private void setTapes(JSONObject tapes){
        tapeList = gsonTapeList(tapes.toString());
    }

    private void setBusinessModels(JSONObject businessModels){
        businessModelList = gsonBusList(businessModels.toString());
    }

    private void setTickets(JSONObject tickets){
        ticketList = gsonTicketList(tickets.toString());
    }

    /*
 * 使用gson解析
 */
    public ArrayList<Category> gsonCategoryList(String json){
        ArrayList<Category> list=new ArrayList<>();
        try{
            Type type=new TypeToken<Map<String,Category>>(){}.getType();
            Gson gson=new Gson();
            Map<String,Category> map=gson.fromJson(json,type);
//            Map<String,InventoryActivityEntity> map= GsonHelper.json2Map(json,InventoryActivityEntity.class);
            if(map!=null){
                for (Map.Entry<String, Category> entry : map.entrySet()) {
                    list.add(entry.getValue());
                }
            }
        }catch (Exception ex){

        }
        return list;
    }

    public ArrayList<tape> gsonTapeList(String json){
        ArrayList<tape> list=new ArrayList<>();
        try{
            Type type=new TypeToken<Map<String,tape>>(){}.getType();
            Gson gson=new Gson();
            Map<String,tape> map=gson.fromJson(json,type);
//            Map<String,InventoryActivityEntity> map= GsonHelper.json2Map(json,InventoryActivityEntity.class);
            if(map!=null){
                for (Map.Entry<String, tape> entry : map.entrySet()) {
                    list.add(entry.getValue());
                }
            }
        }catch (Exception ex){

        }
        return list;
    }

    public ArrayList<businessmodel> gsonBusList(String json){
        ArrayList<businessmodel> list=new ArrayList<>();
        try{
            Type type=new TypeToken<Map<String,businessmodel>>(){}.getType();
            Gson gson=new Gson();
            Map<String,businessmodel> map=gson.fromJson(json,type);
//            Map<String,InventoryActivityEntity> map= GsonHelper.json2Map(json,InventoryActivityEntity.class);
            if(map!=null){
                for (Map.Entry<String, businessmodel> entry : map.entrySet()) {
                    list.add(entry.getValue());
                }
            }
        }catch (Exception ex){

        }
        return list;
    }

    public ArrayList<ticket> gsonTicketList(String json){
        ArrayList<ticket> list=new ArrayList<>();
        try{
            Type type=new TypeToken<Map<String,ticket>>(){}.getType();
            Gson gson=new Gson();
            Map<String,ticket> map=gson.fromJson(json,type);
//            Map<String,InventoryActivityEntity> map= GsonHelper.json2Map(json,InventoryActivityEntity.class);
            if(map!=null){
                for (Map.Entry<String, ticket> entry : map.entrySet()) {
                    list.add(entry.getValue());
                }
            }
        }catch (Exception ex){

        }
        return list;
    }

}

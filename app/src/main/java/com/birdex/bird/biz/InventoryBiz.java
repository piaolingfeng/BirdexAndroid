package com.birdex.bird.biz;

import com.birdex.bird.entity.InventoryDetailEntity;
import com.birdex.bird.entity.InventoryEntity;
import com.birdex.bird.entity.InventoryOrderEntity;
import com.birdex.bird.entity.InventoryStockEntity;
import com.birdex.bird.util.StringUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;

/**
 * Created by huwei on 16/4/5.
 */
public class InventoryBiz {
    /*
     *将服务器获取的数据解析成展现的数据集合
     *
     */
    public ArrayList<InventoryEntity> parseJson2List(JSONObject jroot) throws JSONException{
        ArrayList<InventoryEntity> list=new ArrayList<>();
        if(jroot!=null){
            String value=null;
            Iterator<String> iterator=jroot.keys();
            String key="";
            JSONObject jobj=null;
            while (iterator.hasNext()){
                //获取的对象key
                key=iterator.next();
                jobj=jroot.getJSONObject(key);
                //初始化一个实体对象
                InventoryEntity entity=new InventoryEntity();
                //详细
                value=jobj.getString("detail");
                if(value!=null){
                    entity.setDetail(value);
                }else{
                    entity.setDetail("");
                }
                //model
                value=jobj.getString("model");
                if(value!=null){
                    entity.setModel(value);
                }else{
                    entity.setModel("");
                }
                //商品图片
                value=jobj.getString("pic");
                if(value!=null){
                    entity.setPic(value);
                }else{
                    entity.setPic("");
                }
                //upc码
                value=jobj.getString("upc");
                if(value!=null){
                    entity.setUpc(value);
                }else{
                    entity.setUpc("");
                }
                //商品外部编码
                value=jobj.getString("external_no");
                if(value!=null){
                    entity.setExternal_no(value);
                }else{
                    entity.setExternal_no("");
                }
                //缺省category_id_1、category_id_2、
                //价值
                value=jobj.getString("price");
                if(value!=null){
                    entity.setPrice(value);
                }else {
                    entity.setPrice("");
                }
                //商品唯一编码
                value=jobj.getString("product_code");
                if(value!=null){
                    entity.setProduct_code(value);
                }else{
                    entity.setProduct_code("");
                }
                //商品名称
                value=jobj.getString("name");
                if(value!=null){
                    entity.setName(value);
                }else {
                    entity.setName("");
                }
                //缺省row_num
                //设置brand
                value=jobj.getString("brand");
                if(value!=null){
                    entity.setBrand(value);
                }else {
                    entity.setBrand("");
                }
                //价值单位
                value=jobj.getString("price_unit");
                if(value!=null){
                    entity.setPrice_unit(value);
                }else{
                    entity.setPrice_unit("");
                }
                //----------------进一步解析集合------------------------
                ArrayList<InventoryStockEntity> stockList=new ArrayList<>();
                stockList=parseJson2StockList(jobj.getJSONArray("stock"));
                entity.setStock(stockList);
                list.add(entity);
            }
        }
        return list;
    }
    /*
     *进一步解析仓库详细内容
     */
    private ArrayList<InventoryDetailEntity> parseJson2StockDetailList(JSONArray array){
        ArrayList<InventoryDetailEntity> list=new ArrayList<>();
        if(array!=null){
            if(array.length()>0){
                InventoryDetailEntity entity=null;
                JSONObject jobj=null;
                for(int i=0;i<array.length();i++){
                    entity=new InventoryDetailEntity();
                    try{
                        jobj=array.getJSONObject(i);
                        if(jobj.has("shortage_stock")){
                            //盘亏数量
                            entity.setShortage_stock(StringUtils.getNumString(jobj.getString("shortage_stock")));
                            //spoilage_stock: "损耗（品质问题）数量",
                            entity.setSpoilage_stock(StringUtils.getNumString(jobj.getString("spoilage_stock")));
                            //status: 状态。10表示正常,20表示库存紧张,30表示断货，40表示发往仓库
                            entity.setStatus(jobj.getInt("status"));
                            //overdraft_stock: "允许超售库存量",
                            entity.setOverdraft_stock(StringUtils.getNumString(jobj.getString("overdraft_stock")));
                            //out_stock: "出库总数",
                            entity.setOut_stock(StringUtils.getNumString(jobj.getString("out_stock")));
                            //lose_stock: "丢失数量",
                            entity.setLose_stock(StringUtils.getNumString(jobj.getString("lose_stock")));
                            //in_stock: "入库总数",
                            entity.setIn_stock(StringUtils.getNumString(jobj.getString("in_stock")));
                            //
                            entity.setWms_stock(StringUtils.getNumString(jobj.getString("wms_stock")));
                            //所有损坏库存
                            entity.setAll_damage_stock(jobj.getInt("all_damage_stock"));
                            //expire_stock: "过期数量",
                            entity.setExpire_stock(StringUtils.getNumString(jobj.getString("expire_stock")));

                            //----------------将数组的json对象进行下一步解析---------------------
                            ArrayList<InventoryOrderEntity> orderlist=new ArrayList<>();
                            orderlist=parseJson2OrderList(jobj.getJSONArray("stock_detail"));
                            entity.setStock_detail(orderlist);
                            //damage_stock: "破损数量",
                            entity.setDamage_stock(StringUtils.getNumString(jobj.getString("damage_stock")));
                            //external_no: "商品外部编码"
                            entity.setExternal_no(StringUtils.getString(jobj.getString("external_no")));

                            entity.setOut_of_stock(StringUtils.getNumString(jobj.getString("out_of_stock")));

                            entity.setIning_stock(StringUtils.getNumString(jobj.getString("ining_stock")));

                            //warning_stock: 警界库存数量,
                            entity.setWarning_stock(StringUtils.getNumString(jobj.getString("warning_stock")));
                            //product_code: "商品唯一编码",
                            entity.setProduct_code(StringUtils.getString(jobj.getString("product_code")));
                            //block_stock: "订单占用库存量",
                            entity.setBlock_stock(StringUtils.getNumString(jobj.getString("block_stock")));

                            entity.setWarehouse_code(StringUtils.getString(jobj.getString("warehouse_code")));
                            //overage_stock: "盘盈数量"
                            entity.setOverage_stock(StringUtils.getNumString(jobj.getString("overage_stock")));
                        }
                        if(jobj.has("stock")){
                            //stock 商品库存
                            entity.setStock(StringUtils.getNumString(jobj.getString("stock")));
                        }
                        if(jobj.has("status_name")){
                            //status_name: 状态名称
                            entity.setStatus_name(StringUtils.getString(jobj.getString("status_name")));
                        }

                        list.add(entity);
                    }catch (JSONException jex){
                        jex.printStackTrace();
                    }
                }
            }
        }
        return list;
    }
    /*
    *解析订单里的货物信息
    *
    */
    private ArrayList<InventoryOrderEntity> parseJson2OrderList(JSONArray array){
        ArrayList<InventoryOrderEntity> list=new ArrayList<>();
        if(array!=null){
            if(array.length()>0){
                InventoryOrderEntity entity=null;
                JSONObject jobj=null;
                for(int i=0;i<array.length();i++){
                    entity=new InventoryOrderEntity();
                    try {
                        jobj=array.getJSONObject(i);
                        //入库通知单唯一编码
                        if(jobj.has("StoreCode")){
                            entity.setStorage_code(StringUtils.getString(jobj.getString("StoreCode")));
                        }
                        //跟踪单号
                        entity.setTrackingNo(StringUtils.getString(jobj.getString("TrackingNo")));
                        //数量
                        entity.setQuantity(jobj.getInt("Quantity"));
                        //到期时间
                        entity.setExpire(StringUtils.getString(jobj.getString("Expire")));
                        list.add(entity);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                }
            }
        }
        return list;
    }
    /*
    *仓库的实体
    */
    private ArrayList<InventoryStockEntity>  parseJson2StockList(JSONArray array) {
        ArrayList<InventoryStockEntity> list=new ArrayList<>();
        if(array!=null){
            if(array.length()>0){
                JSONObject jobj=null;
                InventoryStockEntity entity=null;
                for(int i=0;i<array.length();i++){
                    try {
                        jobj=array.getJSONObject(i);
                        entity=new InventoryStockEntity();
                        entity.setWarehouse_name(StringUtils.getString(jobj.getString("warehouse_name")));
                        entity.setWarehouse_code(StringUtils.getString(jobj.getString("warehouse_code")));
                        //----------------将数组的json对象进行下一步解析---------------------
                        ArrayList<InventoryDetailEntity> orderlist=new ArrayList<>();
                        orderlist=parseJson2StockDetailList(jobj.getJSONArray("detail"));
                        entity.setDetail(orderlist);
                        list.add(entity);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return list;
    }
}

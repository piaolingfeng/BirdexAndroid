package com.birdex.bird.biz;

import com.birdex.bird.entity.InventoryEntity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by huwei on 16/4/5.
 */
public class InventoryBiz {
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
                list.add(entity);
            }
        }
        return list;
    }
}

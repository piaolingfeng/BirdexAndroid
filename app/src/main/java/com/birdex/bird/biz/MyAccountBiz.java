package com.birdex.bird.biz;

import com.birdex.bird.entity.TransactionEntity;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by huwei on 16/3/25.
 * 解析数据、对数据进行操作
 */
public class MyAccountBiz {
    /*
     * json对象转成对象集合
     */
    public ArrayList<TransactionEntity> praseJson2List(JSONArray json) {
        try{
            ArrayList<TransactionEntity> list=new ArrayList<>();
            for(int i=0;i<json.length();i++){
                JSONObject jsonObj=(JSONObject)json.get(i);
                TransactionEntity entity=new TransactionEntity();
                entity.setTransactional_number(jsonObj.getString("transactional_number"));
                entity.setTransaction_type(jsonObj.getString("transaction_type"));
//                entity.setTransaction_amountOut(jsonObj.getDouble("transaction_amount_out"));
                if(!"".equals(jsonObj.getString("transaction_amount_out"))){
                    entity.setTransaction_amountOut(Double.valueOf(jsonObj.getString("transaction_amount_out")));
                }
                if(!"".equals(jsonObj.getString("transaction_amount_in"))){
                    entity.setTransaction_amountOut(Double.valueOf(jsonObj.getString("transaction_amount_in")));
                }
//                entity.setTransaction_amountIn(jsonObj.getDouble("transaction_amount_in"));
                entity.setOrder_code(jsonObj.getString("order_code"));
                entity.setRemarks(jsonObj.getString("remarks"));
                entity.setTrading_time(jsonObj.getString("trading_time"));
                list.add(entity);
            }
            return list;
        }catch (Exception ex){

        }
        return null;
    }
}

package com.birdex.bird.entity;

import java.io.Serializable;

/**
 * Created by huwei on 16/4/5.
 *
 *订单的物品信息
 *
 */
public class InventoryOrderEntity implements Serializable{
    //storage_code: "入库通知单唯一编码",
    private String storage_code;
    //TrackingNo跟踪单号
    private String TrackingNo;
    //数量
    private int Quantity;
    //到期时间
    private String Expire;
    public String getExpire() {
        return Expire;
    }

    public void setExpire(String expire) {
        Expire = expire;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }

    public String getTrackingNo() {
        return TrackingNo;
    }

    public void setTrackingNo(String trackingNo) {
        TrackingNo = trackingNo;
    }

    public String getStorage_code() {
        return storage_code;
    }

    public void setStorage_code(String storage_code) {
        this.storage_code = storage_code;
    }
}

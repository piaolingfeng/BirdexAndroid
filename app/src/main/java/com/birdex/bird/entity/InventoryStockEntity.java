package com.birdex.bird.entity;

import java.util.ArrayList;

/**
 * Created by huwei on 16/4/5.
 * 仓库对象
 */
public class InventoryStockEntity {
    public String getWarehouse_name() {
        return warehouse_name;
    }

    public void setWarehouse_name(String warehouse_name) {
        this.warehouse_name = warehouse_name;
    }

    public String getWarehouse_code() {
        return warehouse_code;
    }

    public void setWarehouse_code(String warehouse_code) {
        this.warehouse_code = warehouse_code;
    }

    public ArrayList<InventoryDetailEntity> getDetail() {
        return detail;
    }

    public void setDetail(ArrayList<InventoryDetailEntity> detail) {
        this.detail = detail;
    }

    //warehouse_name 仓库名称
    private String warehouse_name ;
    //仓库编码
    private String warehouse_code;
    //多个仓库详情
    private ArrayList<InventoryDetailEntity> detail;
}

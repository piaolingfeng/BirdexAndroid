package com.birdex.bird.entity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by huwei on 16/4/5.
 * 仓库详细：
 * 缺省wms_stock、all_damage_stock
 * stock_detail订单的集合详情
 */
public class InventoryDetailEntity implements Serializable {
    public String getShortage_stock() {
        return shortage_stock;
    }

    public void setShortage_stock(String shortage_stock) {
        this.shortage_stock = shortage_stock;
    }

    public String getSpoilage_stock() {
        return spoilage_stock;
    }

    public void setSpoilage_stock(String spoilage_stock) {
        this.spoilage_stock = spoilage_stock;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getOverdraft_stock() {
        return overdraft_stock;
    }

    public void setOverdraft_stock(String overdraft_stock) {
        this.overdraft_stock = overdraft_stock;
    }

    public String getOut_stock() {
        return out_stock;
    }

    public void setOut_stock(String out_stock) {
        this.out_stock = out_stock;
    }

    public String getLose_stock() {
        return lose_stock;
    }

    public void setLose_stock(String lose_stock) {
        this.lose_stock = lose_stock;
    }

    public String getIn_stock() {
        return in_stock;
    }

    public void setIn_stock(String in_stock) {
        this.in_stock = in_stock;
    }

    public String getWms_stock() {
        return wms_stock;
    }

    public void setWms_stock(String wms_stock) {
        this.wms_stock = wms_stock;
    }

    public int getAll_damage_stock() {
        return all_damage_stock;
    }

    public void setAll_damage_stock(int all_damage_stock) {
        this.all_damage_stock = all_damage_stock;
    }

    public String getExpire_stock() {
        return expire_stock;
    }

    public void setExpire_stock(String expire_stock) {
        this.expire_stock = expire_stock;
    }

    public String getDamage_stock() {
        return damage_stock;
    }

    public void setDamage_stock(String damage_stock) {
        this.damage_stock = damage_stock;
    }

    public String getExternal_no() {
        return external_no;
    }

    public void setExternal_no(String external_no) {
        this.external_no = external_no;
    }

    public String getOut_of_stock() {
        return out_of_stock;
    }

    public void setOut_of_stock(String out_of_stock) {
        this.out_of_stock = out_of_stock;
    }

    public String getIning_stock() {
        return ining_stock;
    }

    public void setIning_stock(String ining_stock) {
        this.ining_stock = ining_stock;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getStatus_name() {
        return status_name;
    }

    public void setStatus_name(String status_name) {
        this.status_name = status_name;
    }

    public String getWarning_stock() {
        return warning_stock;
    }

    public void setWarning_stock(String warning_stock) {
        this.warning_stock = warning_stock;
    }

    public String getProduct_code() {
        return product_code;
    }

    public void setProduct_code(String product_code) {
        this.product_code = product_code;
    }

    public String getBlock_stock() {
        return block_stock;
    }

    public void setBlock_stock(String block_stock) {
        this.block_stock = block_stock;
    }

    public String getWarehouse_code() {
        return warehouse_code;
    }

    public void setWarehouse_code(String warehouse_code) {
        this.warehouse_code = warehouse_code;
    }

    public String getOverage_stock() {
        return overage_stock;
    }

    public void setOverage_stock(String overage_stock) {
        this.overage_stock = overage_stock;
    }

    public ArrayList<InventoryOrderEntity> getStock_detail() {
        return stock_detail;
    }

    public void setStock_detail(ArrayList<InventoryOrderEntity> stock_detail) {
        this.stock_detail = stock_detail;
    }

    //shortage_stock: "盘亏数量"
    private String shortage_stock;
    //spoilage_stock: "损耗（品质问题）数量",
    private String spoilage_stock;
    //status: 状态。10表示正常,20表示库存紧张,30表示断货，40表示发往仓库
    private int status;
    //overdraft_stock: "允许超售库存量",
    private String overdraft_stock;
    //out_stock: "出库总数",
    private String out_stock;
    //lose_stock: "丢失数量",
    private String lose_stock;
    //in_stock: "入库总数",
    private String in_stock;
    //
    private String wms_stock;
    //所有损坏库存
    private int all_damage_stock;
    //expire_stock: "过期数量",
    private String expire_stock;
    //
    private ArrayList<InventoryOrderEntity> stock_detail;
    //damage_stock: "破损数量",
    private String damage_stock;
    //external_no: "商品外部编码"
    private String external_no;
    //
    private String out_of_stock;
    //
    private String ining_stock;
    //stock 商品库存
    private String stock;
    //status_name: 状态名称
    private String status_name;
    //warning_stock: 警界库存数量,
    private String warning_stock;
    //product_code: "商品唯一编码",
    private String product_code;
    //block_stock: "订单占用库存量",
    private String block_stock;
    //
    private String warehouse_code;
    //overage_stock: "盘盈数量"
    private String overage_stock;
}

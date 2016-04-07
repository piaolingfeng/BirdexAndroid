package com.birdex.bird.entity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by huwei on 16/4/5.
 */
public class InventoryEntity implements Serializable {
    //物品详细
    private String detail;

    //
    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getUpc() {
        return upc;
    }

    public void setUpc(String upc) {
        this.upc = upc;
    }

    public String getExternal_no() {
        return external_no;
    }

    public void setExternal_no(String external_no) {
        this.external_no = external_no;
    }

    public String getCategory_id_1() {
        return category_id_1;
    }

    public void setCategory_id_1(String category_id_1) {
        this.category_id_1 = category_id_1;
    }

    public String getCategory_id_2() {
        return category_id_2;
    }

    public void setCategory_id_2(String category_id_2) {
        this.category_id_2 = category_id_2;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getProduct_code() {
        return product_code;
    }

    public void setProduct_code(String product_code) {
        this.product_code = product_code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRow_num() {
        return row_num;
    }

    public void setRow_num(int row_num) {
        this.row_num = row_num;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getPrice_unit() {
        return price_unit;
    }

    public void setPrice_unit(String price_unit) {
        this.price_unit = price_unit;
    }

    public ArrayList<InventoryStockEntity> getStock() {
        return stock;
    }

    public void setStock(ArrayList<InventoryStockEntity> stock) {
        this.stock = stock;
    }

    private String model;
    //物品图片
    private String pic;
    //UPC码
    private String upc;
    //商品外部编码
    private String external_no;

    //
    private String category_id_1;
    //
    private String category_id_2;
    //价值
    private String price;
    //商品唯一编码
    private String product_code;
    //商品名称
    private String name;
    //
    private int row_num;
    //
    private String brand;
    //价值单位
    private String price_unit;
    //
    private ArrayList<InventoryStockEntity> stock;


}

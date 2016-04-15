package com.birdex.bird.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chuming.zhuang on 2016/4/15.
 */
public class InventorySimpleEntity {
    int page_total = 0;//;// 总页数,

    List<InventorySimpleProduct> products = new ArrayList<>();//  商品库存列表

    public int getPage_total() {
        return page_total;
    }

    public void setPage_total(int page_total) {
        this.page_total = page_total;
    }

    public List<InventorySimpleProduct> getProducts() {
        return products;
    }

    public void setProducts(List<InventorySimpleProduct> products) {
        this.products = products;
    }

    public class InventorySimpleProduct {

        String product_code;// "商品唯一编码",
        String upc;// "商品UPC",
        String name;// "商品名称",
        String pic;// "商品图片",
        String external_no;// "商品外部编码"
        String available_stock;// "可用库存"
        String last_storage_time;// "最后存储时间"
        String stock;// 商品库存

        public String getProduct_code() {
            return product_code;
        }

        public void setProduct_code(String product_code) {
            this.product_code = product_code;
        }

        public String getUpc() {
            return upc;
        }

        public void setUpc(String upc) {
            this.upc = upc;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

        public String getExternal_no() {
            return external_no;
        }

        public void setExternal_no(String external_no) {
            this.external_no = external_no;
        }

        public String getAvailable_stock() {
            return available_stock;
        }

        public void setAvailable_stock(String available_stock) {
            this.available_stock = available_stock;
        }

        public String getLast_storage_time() {
            return last_storage_time;
        }

        public void setLast_storage_time(String last_storage_time) {
            this.last_storage_time = last_storage_time;
        }

        public String getStock() {
            return stock;
        }

        public void setStock(String stock) {
            this.stock = stock;
        }
    }
}

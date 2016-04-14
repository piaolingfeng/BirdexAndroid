package com.birdex.bird.entity;

import java.util.List;

/**
 * Created by chuming.zhuang on 2016/4/6.
 */
public class OrderListProductEntity {
    String product_code="";//: "商品唯一编码",
    String external_no="";//: "商品编码",
    String name="";//: "商品名称",
    String upc="";//: "UPC码",
    String pic="";//: "商品图片",
    String nums="";//: "数量"
    String error="";
    public String getProduct_code() {
        return product_code;
    }

    public void setProduct_code(String product_code) {
        this.product_code = product_code;
    }

    public String getExternal_no() {
        return external_no;
    }

    public void setExternal_no(String external_no) {
        this.external_no = external_no;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUpc() {
        return upc;
    }

    public void setUpc(String upc) {
        this.upc = upc;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getNums() {
        return nums;
    }

    public void setNums(String nums) {
        this.nums = nums;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}

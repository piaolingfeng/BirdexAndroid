package com.birdex.bird.entity;

import java.io.Serializable;

/**
 * Created by hyj on 2016/4/6.
 */
public class ContactDetail implements Serializable {

    // 收货人姓名
    private String receiver_name;
    // 收货人手机号
    private String receiver_mobile;
    // 收货人地址省份id
    private String receiver_province_id;
    // 收货人地址省份名称
    private String receiver_province;
    // 收货人地址城市id
    private String receiver_city_id;
    // 收货人地址城市名称
    private String receiver_city;
    // 收货人地址区县id
    private String receiver_area_id;
    // 收货人地址区县名称
    private String receiver_area;
    // 收货人详细地址
    private String receiver_address;

    public String getReceiver_name() {
        return receiver_name;
    }

    public void setReceiver_name(String receiver_name) {
        this.receiver_name = receiver_name;
    }

    public String getReceiver_mobile() {
        return receiver_mobile;
    }

    public void setReceiver_mobile(String receiver_mobile) {
        this.receiver_mobile = receiver_mobile;
    }

    public String getReceiver_province_id() {
        return receiver_province_id;
    }

    public void setReceiver_province_id(String receiver_province_id) {
        this.receiver_province_id = receiver_province_id;
    }

    public String getReceiver_province() {
        return receiver_province;
    }

    public void setReceiver_province(String receiver_province) {
        this.receiver_province = receiver_province;
    }

    public String getReceiver_city_id() {
        return receiver_city_id;
    }

    public void setReceiver_city_id(String receiver_city_id) {
        this.receiver_city_id = receiver_city_id;
    }

    public String getReceiver_city() {
        return receiver_city;
    }

    public void setReceiver_city(String receiver_city) {
        this.receiver_city = receiver_city;
    }

    public String getReceiver_area_id() {
        return receiver_area_id;
    }

    public void setReceiver_area_id(String receiver_area_id) {
        this.receiver_area_id = receiver_area_id;
    }

    public String getReceiver_area() {
        return receiver_area;
    }

    public void setReceiver_area(String receiver_area) {
        this.receiver_area = receiver_area;
    }

    public String getReceiver_address() {
        return receiver_address;
    }

    public void setReceiver_address(String receiver_address) {
        this.receiver_address = receiver_address;
    }
}

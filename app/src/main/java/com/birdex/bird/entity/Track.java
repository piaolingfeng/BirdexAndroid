package com.birdex.bird.entity;

import java.util.List;

/**
 * 物流轨迹
 * Created by hyj on 2016/4/7.
 */
public class Track {

    // 订单号
    private String order_oms_no;

    // 客户单号
    private String order_no;

    // 收件人
    private String receiver_name;

    // 状态
    private String status;

    // 出库时间
    private String checkout_time;

    // 已运送
    private String tracking_length;

    // 轨迹列表
    private List<Tracking> trackings;

    public String getOrder_oms_no() {
        return order_oms_no;
    }

    public void setOrder_oms_no(String order_oms_no) {
        this.order_oms_no = order_oms_no;
    }

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public String getReceiver_name() {
        return receiver_name;
    }

    public void setReceiver_name(String receiver_name) {
        this.receiver_name = receiver_name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCheckout_time() {
        return checkout_time;
    }

    public void setCheckout_time(String checkout_time) {
        this.checkout_time = checkout_time;
    }

    public String getTracking_length() {
        return tracking_length;
    }

    public void setTracking_length(String tracking_length) {
        this.tracking_length = tracking_length;
    }

    public List<Tracking> getTrackings() {
        return trackings;
    }

    public void setTrackings(List<Tracking> trackings) {
        this.trackings = trackings;
    }
}

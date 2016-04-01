package com.birdex.bird.entity;

/**
 * Created by chuming.zhuang on 2016/3/28.
 */
public class TodayDataEntity {
    int warning_stock_count;
    int today_checkout_order_count;
    int today_wait_checkout_order_count;
    int transport_order_count;
    int today_sign_order_count;
    int exception_order_count;
    int no_pass_order_count;
    int today_confirm_storage_count;
    int wait_confirm_storage_count;
    int no_pass_storage_count;

    public int getWarning_stock_count() {
        return warning_stock_count;
    }

    public void setWarning_stock_count(int warning_stock_count) {
        this.warning_stock_count = warning_stock_count;
    }

    public int getToday_checkout_order_count() {
        return today_checkout_order_count;
    }

    public void setToday_checkout_order_count(int today_checkout_order_count) {
        this.today_checkout_order_count = today_checkout_order_count;
    }

    public int getToday_wait_checkout_order_count() {
        return today_wait_checkout_order_count;
    }

    public void setToday_wait_checkout_order_count(int today_wait_checkout_order_count) {
        this.today_wait_checkout_order_count = today_wait_checkout_order_count;
    }

    public int getTransport_order_count() {
        return transport_order_count;
    }

    public void setTransport_order_count(int transport_order_count) {
        this.transport_order_count = transport_order_count;
    }

    public int getToday_sign_order_count() {
        return today_sign_order_count;
    }

    public void setToday_sign_order_count(int today_sign_order_count) {
        this.today_sign_order_count = today_sign_order_count;
    }

    public int getException_order_count() {
        return exception_order_count;
    }

    public void setException_order_count(int exception_order_count) {
        this.exception_order_count = exception_order_count;
    }

    public int getNo_pass_order_count() {
        return no_pass_order_count;
    }

    public void setNo_pass_order_count(int no_pass_order_count) {
        this.no_pass_order_count = no_pass_order_count;
    }

    public int getToday_confirm_storage_count() {
        return today_confirm_storage_count;
    }

    public void setToday_confirm_storage_count(int today_confirm_storage_count) {
        this.today_confirm_storage_count = today_confirm_storage_count;
    }

    public int getWait_confirm_storage_count() {
        return wait_confirm_storage_count;
    }

    public void setWait_confirm_storage_count(int wait_confirm_storage_count) {
        this.wait_confirm_storage_count = wait_confirm_storage_count;
    }

    public int getNo_pass_storage_count() {
        return no_pass_storage_count;
    }

    public void setNo_pass_storage_count(int no_pass_storage_count) {
        this.no_pass_storage_count = no_pass_storage_count;
    }
}

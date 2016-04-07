package com.birdex.bird.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chuming.zhuang on 2016/4/6.
 * 订单状态
 */
public class OrderStatus {
    List<Status> data = new ArrayList<>();
    int error = 0;//: 0

    public List<Status> getData() {
        return data;
    }

    public void setData(List<Status> data) {
        this.data = data;
    }

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public class Status {
        int status = 0;//:60,
        String status_name = "";//s:"已签收"

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getStatus_name() {
            return status_name;
        }

        public void setStatus_name(String status_name) {
            this.status_name = status_name;
        }
    }
}

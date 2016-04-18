package com.birdex.bird.entity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by huwei on 16/4/13.
 * 待入库按照显示将InventoryActivityEntity精简
 */
public class SimpleWillinEntity implements Serializable{
    //warehouse_name 仓库名称
    private String warehouse_name="";
    //仓库编码
    private String warehouse_code="";
    private int count=0;

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

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public ArrayList<InventoryTransInfo> getTranlist() {
        return tranlist;
    }

    public void setTranlist(ArrayList<InventoryTransInfo> tranlist) {
        this.tranlist = tranlist;
    }

    public ArrayList<InventoryTransInfo> tranlist=null;
    public class InventoryTransInfo implements Serializable{
        private String storage_code="";

        public String getStorage_no() {
            return storage_no;
        }

        public void setStorage_no(String storage_no) {
            this.storage_no = storage_no;
        }

        public String getStorage_code() {
            return storage_code;
        }

        public void setStorage_code(String storage_code) {
            this.storage_code = storage_code;
        }

        public String getBatch_no() {
            return batch_no;
        }

        public void setBatch_no(String batch_no) {
            this.batch_no = batch_no;
        }

        public String getTrack_no() {
            return track_no;
        }

        public void setTrack_no(String track_no) {
            this.track_no = track_no;
        }

        public String getCreated_time() {
            return created_time;
        }

        public void setCreated_time(String created_time) {
            this.created_time = created_time;
        }

        public String getTrack_type() {
            return track_type;
        }

        public void setTrack_type(String track_type) {
            this.track_type = track_type;
        }

        public String getNums() {
            return nums;
        }

        public void setNums(String nums) {
            this.nums = nums;
        }

        public String getTrack_type_name() {
            return track_type_name;
        }

        public void setTrack_type_name(String track_type_name) {
            this.track_type_name = track_type_name;
        }

        private String storage_no="";
        private String batch_no="";
        private String track_no="";
        private String created_time="";
        private String track_type="";
        private String nums="";
        private String track_type_name="";
        //状态
        private int status=0;

        public String getStatus_name() {
            return status_name;
        }

        public void setStatus_name(String status_name) {
            this.status_name = status_name;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        private String status_name="";
    }
}

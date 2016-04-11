package com.birdex.bird.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chuming.zhuang on 2016/4/8.
 */
public class PredicitionEntity {
    int error = 1;
    Predicition data = new Predicition();

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public Predicition getData() {
        return data;
    }

    public void setData(Predicition data) {
        this.data = data;
    }

    public class Predicition {
        int count = 0;//: "总数",
        List<PredicitionDetail> storages = new ArrayList<>();


        public List<PredicitionDetail> getStorages() {
            return storages;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public void setStorages(List<PredicitionDetail> storages) {
            this.storages = storages;
        }

        public class PredicitionDetail {
            String storage_no = "";//: "入库单号",
            String warehouse_name = "";//: "仓库名称",
            String status = "";//: "状态， 参考获取状态列表",
            String status_name = "";//: "状态名称",
            String created_time = "";//: "创建世界",
            String updated_time = "";//: “最后更新时间”,

            String need_pick_up = "";//: "是否需要提货，1表示需要，0表示不需要"

            String verify_fail_detail;
            String track_type_name;//eg:普通物流
            String track_no;
            String storage_code;
            String storage_wms_no;

            public String getStorage_no() {
                return storage_no;
            }

            public void setStorage_no(String storage_no) {
                this.storage_no = storage_no;
            }

            public String getWarehouse_name() {
                return warehouse_name;
            }

            public void setWarehouse_name(String warehouse_name) {
                this.warehouse_name = warehouse_name;
            }

            public String getVerify_fail_detail() {
                return verify_fail_detail;
            }

            public void setVerify_fail_detail(String verify_fail_detail) {
                this.verify_fail_detail = verify_fail_detail;
            }

            public String getTrack_type_name() {
                return track_type_name;
            }

            public void setTrack_type_name(String track_type_name) {
                this.track_type_name = track_type_name;
            }

            public String getTrack_no() {
                return track_no;
            }

            public void setTrack_no(String track_no) {
                this.track_no = track_no;
            }

            public String getStorage_code() {
                return storage_code;
            }

            public void setStorage_code(String storage_code) {
                this.storage_code = storage_code;
            }

            public String getStorage_wms_no() {
                return storage_wms_no;
            }

            public void setStorage_wms_no(String storage_wms_no) {
                this.storage_wms_no = storage_wms_no;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getStatus_name() {
                return status_name;
            }

            public void setStatus_name(String status_name) {
                this.status_name = status_name;
            }

            public String getCreated_time() {
                return created_time;
            }

            public void setCreated_time(String created_time) {
                this.created_time = created_time;
            }

            public String getUpdated_time() {
                return updated_time;
            }

            public void setUpdated_time(String updated_time) {
                this.updated_time = updated_time;
            }

            public String getNeed_pick_up() {
                return need_pick_up;
            }

            public void setNeed_pick_up(String need_pick_up) {
                this.need_pick_up = need_pick_up;
            }
        }
    }


}

package com.birdex.bird.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chuming.zhuang on 2016/4/6.
 */
public class WarehouseEntity {

    List<WarehouseDetail> data = new ArrayList<>();
    int error = 0;

    public List<WarehouseDetail> getData() {
        return data;
    }

    public void setData(List<WarehouseDetail> data) {
        this.data = data;
    }

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public class WarehouseDetail {
        String warehouse_code = "";//: "HKG",
        String vmid = "";//: "56051158",
        String name = "";//: "香港"


        public String getWarehouse_code() {
            return warehouse_code;
        }

        public void setWarehouse_code(String warehouse_code) {
            this.warehouse_code = warehouse_code;
        }

        public String getVmid() {
            return vmid;
        }

        public void setVmid(String vmid) {
            this.vmid = vmid;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}

package com.birdex.bird.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by chuming.zhuang on 2016/4/12.
 */
public class PredicitionDetailEntity {
    PredicitionData data = new PredicitionData();
    int error = 1;

    public PredicitionData getData() {
        return data;
    }

    public void setData(PredicitionData data) {
        this.data = data;
    }

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public class PredicitionData {

        String storage_code = "";//30c17e4e4e58689b20d1cb7787268a0e",
        String storage_notice_no = "";//": null,
        String storage_no = "";//": "BS160405219366",
        String storage_wms_no = "";//: null,
        String company_code = "";//: "ef49c8c73b471c7bbb90b4745045be4d",
        String company_short_name = "";//: "gegejia",
        String user_code = "";//: "f12ec985bb1eff163644a2e3eb97f92f",
        String track_no = "";//: "BL20160405001",
        String order_no = "";//: "",
        String batch_no = "";//: "",
        String warehouse_code = "";//: "PDX",
        String old_warehouse_code = "";//: null,
        String expected_arrival_time = "";//": "0",
        String track_type = "";//: "1",
        String need_pick_up = "";//: "0",
        String pick_up_scrip = "";//: "",
        String remark = "";//: "",
        String box_nums = "";//: "0",
        String product_nums = "";//: "0",
        String board_nums = "";//: "0",
        String status = "";//: "23",
        String storaged_time = "";//: null,
        String created_time = "";//: "2016-04-05 11:27:51",
        String updated_time = "";//:"1459826898",
        String from = "";//:"DEFAULT",
        String verify_fail_detail = "";//":null,
        String send_notice = "";//:"0",
        String warehouse_name = "";//:"美国俄勒冈 免税",
        String old_warehouse_name = "";//:"",
        String track_type_name = "";//:"普通物流",
        String status_name = "";//:"清点中",
        List<PredicitionDetailProduct> products = new ArrayList<>();//":[

        public String getStorage_code() {
            return storage_code;
        }

        public void setStorage_code(String storage_code) {
            this.storage_code = storage_code;
        }

        public String getStorage_notice_no() {
            return storage_notice_no;
        }

        public void setStorage_notice_no(String storage_notice_no) {
            this.storage_notice_no = storage_notice_no;
        }

        public String getStorage_no() {
            return storage_no;
        }

        public void setStorage_no(String storage_no) {
            this.storage_no = storage_no;
        }

        public String getStorage_wms_no() {
            return storage_wms_no;
        }

        public void setStorage_wms_no(String storage_wms_no) {
            this.storage_wms_no = storage_wms_no;
        }

        public String getCompany_code() {
            return company_code;
        }

        public void setCompany_code(String company_code) {
            this.company_code = company_code;
        }

        public String getCompany_short_name() {
            return company_short_name;
        }

        public void setCompany_short_name(String company_short_name) {
            this.company_short_name = company_short_name;
        }

        public String getUser_code() {
            return user_code;
        }

        public void setUser_code(String user_code) {
            this.user_code = user_code;
        }

        public String getTrack_no() {
            return track_no;
        }

        public void setTrack_no(String track_no) {
            this.track_no = track_no;
        }

        public String getOrder_no() {
            return order_no;
        }

        public void setOrder_no(String order_no) {
            this.order_no = order_no;
        }

        public String getBatch_no() {
            return batch_no;
        }

        public void setBatch_no(String batch_no) {
            this.batch_no = batch_no;
        }

        public String getWarehouse_code() {
            return warehouse_code;
        }

        public void setWarehouse_code(String warehouse_code) {
            this.warehouse_code = warehouse_code;
        }

        public String getOld_warehouse_code() {
            return old_warehouse_code;
        }

        public void setOld_warehouse_code(String old_warehouse_code) {
            this.old_warehouse_code = old_warehouse_code;
        }

        public String getExpected_arrival_time() {
            return expected_arrival_time;
        }

        public void setExpected_arrival_time(String expected_arrival_time) {
            this.expected_arrival_time = expected_arrival_time;
        }

        public String getTrack_type() {
            return track_type;
        }

        public void setTrack_type(String track_type) {
            this.track_type = track_type;
        }

        public String getNeed_pick_up() {
            return need_pick_up;
        }

        public void setNeed_pick_up(String need_pick_up) {
            this.need_pick_up = need_pick_up;
        }

        public String getPick_up_scrip() {
            return pick_up_scrip;
        }

        public void setPick_up_scrip(String pick_up_scrip) {
            this.pick_up_scrip = pick_up_scrip;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getBox_nums() {
            return box_nums;
        }

        public void setBox_nums(String box_nums) {
            this.box_nums = box_nums;
        }

        public String getProduct_nums() {
            return product_nums;
        }

        public void setProduct_nums(String product_nums) {
            this.product_nums = product_nums;
        }

        public String getBoard_nums() {
            return board_nums;
        }

        public void setBoard_nums(String board_nums) {
            this.board_nums = board_nums;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getStoraged_time() {
            return storaged_time;
        }

        public void setStoraged_time(String storaged_time) {
            this.storaged_time = storaged_time;
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

        public String getFrom() {
            return from;
        }

        public void setFrom(String from) {
            this.from = from;
        }

        public String getVerify_fail_detail() {
            return verify_fail_detail;
        }

        public void setVerify_fail_detail(String verify_fail_detail) {
            this.verify_fail_detail = verify_fail_detail;
        }

        public String getSend_notice() {
            return send_notice;
        }

        public void setSend_notice(String send_notice) {
            this.send_notice = send_notice;
        }

        public String getWarehouse_name() {
            return warehouse_name;
        }

        public void setWarehouse_name(String warehouse_name) {
            this.warehouse_name = warehouse_name;
        }

        public String getOld_warehouse_name() {
            return old_warehouse_name;
        }

        public void setOld_warehouse_name(String old_warehouse_name) {
            this.old_warehouse_name = old_warehouse_name;
        }

        public String getTrack_type_name() {
            return track_type_name;
        }

        public void setTrack_type_name(String track_type_name) {
            this.track_type_name = track_type_name;
        }

        public String getStatus_name() {
            return status_name;
        }

        public void setStatus_name(String status_name) {
            this.status_name = status_name;
        }

        public List<PredicitionDetailProduct> getProducts() {
            return products;
        }

        public void setProducts(List<PredicitionDetailProduct> products) {
            this.products = products;
        }

        public class PredicitionDetailProduct {
            String storage_code = "";//:"30c17e4e4e58689b20d1cb7787268a0e",
            String track_no = "";//:"BL20160405001",
            String company_code = "";//:"ef49c8c73b471c7bbb90b4745045be4d",
            String warehouse_code = "";//:"PDX",
            String product_code = "";//:"17dc1c3cb5ae3d7e4e7359d0d6cd9993",
            String product_type = "";//:"10",
            String name = "";//:"儿童自行车",
            String external_no = "";//:"2016030700112",
            String upc = "";//:"2016030700112",
            String brand = "";//:"halolo",
            String model = "";//:"辆",
            String price = "";//:"135.00",
            String price_unit = "";//:"CNY",
            String box = "";//:"",
            String nums = "";//:"1",
            String real_nums = "";//:"0",
            String damaged_nums = "";//:"",
            String status = "";//:"20",
            String created_time = "";//:"1459826896",
            String updated_time = "";//:"1459826896",
            DetailProductRemark remark = new DetailProductRemark();

            public class DetailProductRemark {
                String msg = "";//:"",
                List<String> url = new ArrayList<>() ;//:[]

                public String getMsg() {
                    return msg;
                }

                public void setMsg(String msg) {
                    this.msg = msg;
                }

            }

            String confirm_time = "";//:null,
            String confirm_user_account = "";//:null,
            String expired_date = "";//:null,
            String status_name = "";//:"已审核"

            String review_time="";//: "发起复核的时间",
            String review_remark="";//: "复核理由"

            public String getReview_time() {
                return review_time;
            }

            public void setReview_time(String review_time) {
                this.review_time = review_time;
            }

            public String getReview_remark() {
                return review_remark;
            }

            public void setReview_remark(String review_remark) {
                this.review_remark = review_remark;
            }

            public String getStorage_code() {
                return storage_code;
            }

            public void setStorage_code(String storage_code) {
                this.storage_code = storage_code;
            }

            public String getTrack_no() {
                return track_no;
            }

            public void setTrack_no(String track_no) {
                this.track_no = track_no;
            }

            public String getCompany_code() {
                return company_code;
            }

            public void setCompany_code(String company_code) {
                this.company_code = company_code;
            }

            public String getWarehouse_code() {
                return warehouse_code;
            }

            public void setWarehouse_code(String warehouse_code) {
                this.warehouse_code = warehouse_code;
            }

            public String getProduct_code() {
                return product_code;
            }

            public void setProduct_code(String product_code) {
                this.product_code = product_code;
            }

            public String getProduct_type() {
                return product_type;
            }

            public void setProduct_type(String product_type) {
                this.product_type = product_type;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getExternal_no() {
                return external_no;
            }

            public void setExternal_no(String external_no) {
                this.external_no = external_no;
            }

            public String getUpc() {
                return upc;
            }

            public void setUpc(String upc) {
                this.upc = upc;
            }

            public String getBrand() {
                return brand;
            }

            public void setBrand(String brand) {
                this.brand = brand;
            }

            public String getModel() {
                return model;
            }

            public void setModel(String model) {
                this.model = model;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            public String getPrice_unit() {
                return price_unit;
            }

            public void setPrice_unit(String price_unit) {
                this.price_unit = price_unit;
            }

            public String getBox() {
                return box;
            }

            public void setBox(String box) {
                this.box = box;
            }

            public String getNums() {
                return nums;
            }

            public void setNums(String nums) {
                this.nums = nums;
            }

            public String getReal_nums() {
                return real_nums;
            }

            public void setReal_nums(String real_nums) {
                this.real_nums = real_nums;
            }

            public String getDamaged_nums() {
                return damaged_nums;
            }

            public void setDamaged_nums(String damaged_nums) {
                this.damaged_nums = damaged_nums;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
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

            public DetailProductRemark getRemark() {
                return remark;
            }

            public void setRemark(DetailProductRemark remark) {
                this.remark = remark;
            }

            public String getConfirm_time() {
                return confirm_time;
            }

            public void setConfirm_time(String confirm_time) {
                this.confirm_time = confirm_time;
            }

            public String getConfirm_user_account() {
                return confirm_user_account;
            }

            public void setConfirm_user_account(String confirm_user_account) {
                this.confirm_user_account = confirm_user_account;
            }

            public String getExpired_date() {
                return expired_date;
            }

            public void setExpired_date(String expired_date) {
                this.expired_date = expired_date;
            }

            public String getStatus_name() {
                return status_name;
            }

            public void setStatus_name(String status_name) {
                this.status_name = status_name;
            }
        }
    }
}
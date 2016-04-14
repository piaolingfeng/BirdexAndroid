package com.birdex.bird.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chuming.zhuang on 2016/4/5.
 */
public class OrderListEntity {
    OrderListNum data = new OrderListNum();

    public OrderListNum getData() {
        return data;
    }

    public void setData(OrderListNum data) {
        this.data = data;
    }

    public class OrderListNum {
        int count = 0;//: "总页数";
        int page_num = 0;
        List<Orders> orders = new ArrayList<>();

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public int getPage_num() {
            return page_num;
        }

        public void setPage_num(int page_num) {
            this.page_num = page_num;
        }

        public List<Orders> getOrders() {
            return orders;
        }

        public void setOrders(List<Orders> orders) {
            this.orders = orders;
        }


        public class Orders {
            String order_code = "";//: "订单唯一编码",
            String warehouse_code = "";//: "仓库编码",
            String created_time = "";//: "订单创建时间",
            String order_oms_no = "";//: "订单号",
            String service_type = "";//: "服务方式编码",
            String verify_fail_detail;//": "",
            String verify_id_card_result;//": "20"
            String service_type_name = "";//: "服务方式名称",
            String warehouse_name = "";//: "仓库名称",
            String receiver_name = "";//: "收件人 ",
            String receiver_mobile = "";//: "手机号码",
            String receiver_phone;//":
            String status = "";//: "状态",
            String status_name = "";//: "状态名称",
            String weight = "";//: "重量",
            String price = "";//: "费用",
            List<OrderListProductEntity> products = new ArrayList<>();

            public String getOrder_code() {
                return order_code;
            }

            public void setOrder_code(String order_code) {
                this.order_code = order_code;
            }

            public String getWarehouse_code() {
                return warehouse_code;
            }

            public void setWarehouse_code(String warehouse_code) {
                this.warehouse_code = warehouse_code;
            }

            public String getCreated_time() {
                return created_time;
            }

            public void setCreated_time(String created_time) {
                this.created_time = created_time;
            }

            public String getOrder_oms_no() {
                return order_oms_no;
            }

            public void setOrder_oms_no(String order_oms_no) {
                this.order_oms_no = order_oms_no;
            }

            public String getService_type() {
                return service_type;
            }

            public void setService_type(String service_type) {
                this.service_type = service_type;
            }

            public String getService_type_name() {
                return service_type_name;
            }

            public void setService_type_name(String service_type_name) {
                this.service_type_name = service_type_name;
            }

            public String getWarehouse_name() {
                return warehouse_name;
            }

            public void setWarehouse_name(String warehouse_name) {
                this.warehouse_name = warehouse_name;
            }

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

            public String getWeight() {
                return weight;
            }

            public void setWeight(String weight) {
                this.weight = weight;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            public List<OrderListProductEntity> getProducts() {
                return products;
            }

            public void setProducts(List<OrderListProductEntity> products) {
                this.products = products;
            }

            public String getVerify_fail_detail() {
                return verify_fail_detail;
            }

            public void setVerify_fail_detail(String verify_fail_detail) {
                this.verify_fail_detail = verify_fail_detail;
            }

            public String getVerify_id_card_result() {
                return verify_id_card_result;
            }

            public void setVerify_id_card_result(String verify_id_card_result) {
                this.verify_id_card_result = verify_id_card_result;
            }

            public String getReceiver_phone() {
                return receiver_phone;
            }

            public void setReceiver_phone(String receiver_phone) {
                this.receiver_phone = receiver_phone;
            }
        }
    }
}

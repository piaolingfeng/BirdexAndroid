package com.birdex.bird.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chuming.zhuang on 2016/4/12.
 */
public class OrderDetailEntity {

    Ordertail data = new Ordertail();

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    int error = 1;

    public Ordertail getData() {
        return data;
    }

    public void setData(Ordertail data) {
        this.data = data;
    }

    public class Ordertail {
        String order_code;//: "订单唯一编号",
        String order_wms_no;//: "订单在仓库中的编号",
        String order_oms_no;//: "订单号",
        String company_code;//: "公司的唯一编码",
        String user_code;//: "创建人唯一编码",
        String track_no;//: “物流单号”,
        String order_no;//: "商家单号",
        String warehouse_code;//: "仓库唯一编码",
        String receiver_name;//: "收件人",
        String receiver_province_id;//: "收件人所在省份id",
        String receiver_province;//: "收件人所在省份",
        String receiver_city_id;//: "收件人所在城市id",
        String receiver_city;//: "收件人所在城市",
        String receiver_area_id;//: "收件人所在区县",
        String receiver_area;//: "收件人所在区县id",
        String receiver_address;//: "详细地址",
        String receiver_mobile;//: "手机号",
        String receiver_phone;//: "固话",
        String receiver_zip;//: "邮编",
        String receiver_id_card;//: "身份证",
        String service_type;//: "服务方式",
        String need_insurance;//: "是否购买保险",
        String need_verify_id_card;//: "是否需要验证身份证",
        String verify_id_card_result;//: "身份证验证结果，10表示待验证，20表示验证通过，30表示验证不通过",
        String need_steady;//: “是否加固”,
        String remark;//: "备注",
        String paid_company;//: "支付平台",
        String paid_time;//: "支付时间",
        String paid_no;//: "支付单号",
        String expected_weight;//: “预估重量”,
        String expected_price;//: ‘预估费用’,
        String weight;//: ’实际重量‘,
        String price;//: ’实际费用‘,
        String status;//: "状态",
        String reated_time;//: "1444728777",
        String warehouse_name;//: "仓库名称",
        String status_name;//: "状态名称",
        String service_type_name;//: "服务方式名称",
        List<OrderListProductEntity> products = new ArrayList<>();

        public String getNeed_verify_id_card() {
            return need_verify_id_card;
        }

        public void setNeed_verify_id_card(String need_verify_id_card) {
            this.need_verify_id_card = need_verify_id_card;
        }

        public String getVerify_id_card_result() {
            return verify_id_card_result;
        }

        public void setVerify_id_card_result(String verify_id_card_result) {
            this.verify_id_card_result = verify_id_card_result;
        }

        public String getOrder_code() {
            return order_code;
        }

        public void setOrder_code(String order_code) {
            this.order_code = order_code;
        }

        public String getOrder_wms_no() {
            return order_wms_no;
        }

        public void setOrder_wms_no(String order_wms_no) {
            this.order_wms_no = order_wms_no;
        }

        public String getOrder_oms_no() {
            return order_oms_no;
        }

        public void setOrder_oms_no(String order_oms_no) {
            this.order_oms_no = order_oms_no;
        }

        public String getCompany_code() {
            return company_code;
        }

        public void setCompany_code(String company_code) {
            this.company_code = company_code;
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

        public String getWarehouse_code() {
            return warehouse_code;
        }

        public void setWarehouse_code(String warehouse_code) {
            this.warehouse_code = warehouse_code;
        }

        public String getReceiver_name() {
            return receiver_name;
        }

        public void setReceiver_name(String receiver_name) {
            this.receiver_name = receiver_name;
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

        public String getReceiver_mobile() {
            return receiver_mobile;
        }

        public void setReceiver_mobile(String receiver_mobile) {
            this.receiver_mobile = receiver_mobile;
        }

        public String getReceiver_phone() {
            return receiver_phone;
        }

        public void setReceiver_phone(String receiver_phone) {
            this.receiver_phone = receiver_phone;
        }

        public String getReceiver_zip() {
            return receiver_zip;
        }

        public void setReceiver_zip(String receiver_zip) {
            this.receiver_zip = receiver_zip;
        }

        public String getReceiver_id_card() {
            return receiver_id_card;
        }

        public void setReceiver_id_card(String receiver_id_card) {
            this.receiver_id_card = receiver_id_card;
        }

        public String getService_type() {
            return service_type;
        }

        public void setService_type(String service_type) {
            this.service_type = service_type;
        }

        public String getNeed_insurance() {
            return need_insurance;
        }

        public void setNeed_insurance(String need_insurance) {
            this.need_insurance = need_insurance;
        }

        public String getNeed_steady() {
            return need_steady;
        }

        public void setNeed_steady(String need_steady) {
            this.need_steady = need_steady;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getPaid_company() {
            return paid_company;
        }

        public void setPaid_company(String paid_company) {
            this.paid_company = paid_company;
        }

        public String getPaid_time() {
            return paid_time;
        }

        public void setPaid_time(String paid_time) {
            this.paid_time = paid_time;
        }

        public String getPaid_no() {
            return paid_no;
        }

        public void setPaid_no(String paid_no) {
            this.paid_no = paid_no;
        }

        public String getExpected_weight() {
            return expected_weight;
        }

        public void setExpected_weight(String expected_weight) {
            this.expected_weight = expected_weight;
        }

        public String getExpected_price() {
            return expected_price;
        }

        public void setExpected_price(String expected_price) {
            this.expected_price = expected_price;
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

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getReated_time() {
            return reated_time;
        }

        public void setReated_time(String reated_time) {
            this.reated_time = reated_time;
        }

        public String getWarehouse_name() {
            return warehouse_name;
        }

        public void setWarehouse_name(String warehouse_name) {
            this.warehouse_name = warehouse_name;
        }

        public String getStatus_name() {
            return status_name;
        }

        public void setStatus_name(String status_name) {
            this.status_name = status_name;
        }

        public String getService_type_name() {
            return service_type_name;
        }

        public void setService_type_name(String service_type_name) {
            this.service_type_name = service_type_name;
        }

        public List<OrderListProductEntity> getProducts() {
            return products;
        }

        public void setProducts(List<OrderListProductEntity> products) {
            this.products = products;
        }
    }
}


package com.birdex.bird.entity;

/**
 * Created by chuming.zhuang on 2016/4/7.
 */
public class OrderRequestEntity {
    int page_no = 1;//	N	1	页码
    String page_size = "20";//	N	20	每页显示条数
    String keyword = "";//	N		订单单号、商品名称、外部编码、UPC的关键字
    String warehouse_cod = "";//e	N		仓库唯一编码。例如：HKG
    String start_date = "";//	N		商品创建时间，区间开始日期，格式：2015-09-24
    String end_date = "";//	N		商品创建时间，区间结束日期，格式：2015-09-24
    String status = "";//	N		订单状态10:'待审核'; 11: '已删除'; 2:'等待出库'; 20: '准备出库'; 21: '包裹出库中'; 30: '审核不通过'; 40: '已出库'; 5: '运输中'; 50: '包裹空运中'; 51: '待清关'; 52: '包裹清关中'; 53: '包裹已清关'; 60: '已签收';
    String count = "";    //N		总条数，服务器只有在不传值的情况下才重新总条数
    String service_type = "";    //N		服务方式
    String receiver_moblie = "";//	N		收件人手机号

    public int getPage_no() {
        return page_no;
    }

    public void setPage_no(int page_no) {
        this.page_no = page_no;
    }

    public String getPage_size() {
        return page_size;
    }

    public void setPage_size(String page_size) {
        this.page_size = page_size;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getWarehouse_cod() {
        return warehouse_cod;
    }

    public void setWarehouse_cod(String warehouse_cod) {
        this.warehouse_cod = warehouse_cod;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getService_type() {
        return service_type;
    }

    public void setService_type(String service_type) {
        this.service_type = service_type;
    }

    public String getReceiver_moblie() {
        return receiver_moblie;
    }

    public void setReceiver_moblie(String receiver_moblie) {
        this.receiver_moblie = receiver_moblie;
    }
}

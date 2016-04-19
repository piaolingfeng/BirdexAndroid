package com.birdex.bird.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chuming.zhuang on 2016/4/18.
 */
public class MsgListEntity {
    MsgList data = new MsgList();
    int error = 0;//

    public class MsgList {
        int count = 0;//: 总数量,
        int page_num = 20;//: 请求数量

        List<MsgListMessages> messages = new ArrayList<>();//: [

        public class MsgListMessages {
            String id = "";//:
            String msg_no = "";//: "消息编码",
            String company_code = "";//: "公司Code",
            String msg_type = "";//: "消息类型，具体参考“未读消息统计”接口",
            String msg_title = "";//: "消息标题",
            MsgContent msg_content = new MsgContent();//

            public class MsgContent {
                String order_code = "";//: "订单Code",
                String order_oms_no = "";//: "订单编码",
                List<OrderListProductEntity> products = new ArrayList<>();//: [
                String receiver_province = "";//: "广东省",
                String receiver_city = "";//: "深圳市",
                String receiver_area = "";//: "宝安区",
                String receiver_address = "";//: "呵呵s",
                String receiver_mobile = "";//: "手机号（或 电话）",
                String receiver_name = "";//: "名字",
                String verify_fail_detail = "";//: "审核不通过的原因，或身份证异常的原因"

                //库存内容
                String product_code = "";//: "04b117311cda2da844564b22f177ab31",
                String external_no = "";//: "SN2015110707",
                String name = "";//: "皮鞋",
                String stock = "";//: "11",
                String warning_stock = "";//: "12",
                String overdraft_stock = "";//: "0"

                public String getProduct_code() {
                    return product_code;
                }

                public void setProduct_code(String product_code) {
                    this.product_code = product_code;
                }

                public String getExternal_no() {
                    return external_no;
                }

                public void setExternal_no(String external_no) {
                    this.external_no = external_no;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getStock() {
                    return stock;
                }

                public void setStock(String stock) {
                    this.stock = stock;
                }

                public String getWarning_stock() {
                    return warning_stock;
                }

                public void setWarning_stock(String warning_stock) {
                    this.warning_stock = warning_stock;
                }

                public String getOverdraft_stock() {
                    return overdraft_stock;
                }

                public void setOverdraft_stock(String overdraft_stock) {
                    this.overdraft_stock = overdraft_stock;
                }

                public String getOrder_code() {
                    return order_code;
                }

                public void setOrder_code(String order_code) {
                    this.order_code = order_code;
                }

                public String getOrder_oms_no() {
                    return order_oms_no;
                }

                public void setOrder_oms_no(String order_oms_no) {
                    this.order_oms_no = order_oms_no;
                }

                public List<OrderListProductEntity> getProducts() {
                    return products;
                }

                public void setProducts(List<OrderListProductEntity> products) {
                    this.products = products;
                }

                public String getReceiver_province() {
                    return receiver_province;
                }

                public void setReceiver_province(String receiver_province) {
                    this.receiver_province = receiver_province;
                }

                public String getReceiver_city() {
                    return receiver_city;
                }

                public void setReceiver_city(String receiver_city) {
                    this.receiver_city = receiver_city;
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

                public String getVerify_fail_detail() {
                    return verify_fail_detail;
                }

                public void setVerify_fail_detail(String verify_fail_detail) {
                    this.verify_fail_detail = verify_fail_detail;
                }

                public String getReceiver_mobile() {
                    return receiver_mobile;
                }

                public void setReceiver_mobile(String receiver_mobile) {
                    this.receiver_mobile = receiver_mobile;
                }

                public String getReceiver_name() {
                    return receiver_name;
                }

                public void setReceiver_name(String receiver_name) {
                    this.receiver_name = receiver_name;
                }
            }
            String table_code="";
            String created_date = "";//:"2016-04-15 12:26:59",
            String updated_date = "";//:"2016-04-15 12:26:59",
            String read_status = "";//:"0",0表示未读，1表示已读
            String push_status = "";//:"0"

            public String getTable_code() {
                return table_code;
            }

            public void setTable_code(String table_code) {
                this.table_code = table_code;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getMsg_no() {
                return msg_no;
            }

            public void setMsg_no(String msg_no) {
                this.msg_no = msg_no;
            }

            public String getCompany_code() {
                return company_code;
            }

            public void setCompany_code(String company_code) {
                this.company_code = company_code;
            }

            public String getMsg_type() {
                return msg_type;
            }

            public void setMsg_type(String msg_type) {
                this.msg_type = msg_type;
            }

            public String getMsg_title() {
                return msg_title;
            }

            public void setMsg_title(String msg_title) {
                this.msg_title = msg_title;
            }

            public MsgContent getMsg_content() {
                return msg_content;
            }

            public void setMsg_content(MsgContent msg_content) {
                this.msg_content = msg_content;
            }

            public String getCreated_date() {
                return created_date;
            }

            public void setCreated_date(String created_date) {
                this.created_date = created_date;
            }

            public String getUpdated_date() {
                return updated_date;
            }

            public void setUpdated_date(String updated_date) {
                this.updated_date = updated_date;
            }

            public String getRead_status() {
                return read_status;
            }

            public void setRead_status(String read_status) {
                this.read_status = read_status;
            }

            public String getPush_status() {
                return push_status;
            }

            public void setPush_status(String push_status) {
                this.push_status = push_status;
            }
        }

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

        public List<MsgListMessages> getMessages() {
            return messages;
        }

        public void setMessages(List<MsgListMessages> messages) {
            this.messages = messages;
        }
    }

    public MsgList getData() {
        return data;
    }

    public void setData(MsgList data) {
        this.data = data;
    }

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }
}

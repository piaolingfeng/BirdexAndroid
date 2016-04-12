package com.birdex.bird.entity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by huwei on 16/4/11.
 */
public class InventoryActivityEntity implements Serializable{
    //物品详细
    private String detail;

    //
    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getUpc() {
        return upc;
    }

    public void setUpc(String upc) {
        this.upc = upc;
    }

    public String getExternal_no() {
        return external_no;
    }

    public void setExternal_no(String external_no) {
        this.external_no = external_no;
    }

    public String getCategory_id_1() {
        return category_id_1;
    }

    public void setCategory_id_1(String category_id_1) {
        this.category_id_1 = category_id_1;
    }

    public String getCategory_id_2() {
        return category_id_2;
    }

    public void setCategory_id_2(String category_id_2) {
        this.category_id_2 = category_id_2;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getProduct_code() {
        return product_code;
    }

    public void setProduct_code(String product_code) {
        this.product_code = product_code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRow_num() {
        return row_num;
    }

    public void setRow_num(int row_num) {
        this.row_num = row_num;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getPrice_unit() {
        return price_unit;
    }

    public void setPrice_unit(String price_unit) {
        this.price_unit = price_unit;
    }

    public ArrayList<InventoryStockEntity> getStock() {
        return stock;
    }

    public void setStock(ArrayList<InventoryStockEntity> stock) {
        this.stock = stock;
    }

    private String model;
    //物品图片
    private String pic;
    //UPC码
    private String upc;
    //商品外部编码
    private String external_no;

    //
    private String category_id_1;
    //
    private String category_id_2;
    //价值
    private String price;
    //商品唯一编码
    private String product_code;
    //商品名称
    private String name;
    //
    private int row_num;
    //
    private String brand;
    //价值单位
    private String price_unit;
    //
    private ArrayList<InventoryStockEntity> stock;
    public class InventoryStockEntity implements Serializable{
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

        public ArrayList<InventoryDetailEntity> getDetail() {
            return detail;
        }

        public void setDetail(ArrayList<InventoryDetailEntity> detail) {
            this.detail = detail;
        }

        //warehouse_name 仓库名称
        private String warehouse_name ;
        //仓库编码
        private String warehouse_code;
        //多个仓库详情
        private ArrayList<InventoryDetailEntity> detail;
        public class InventoryDetailEntity implements Serializable {
            public String getShortage_stock() {
                return shortage_stock;
            }

            public void setShortage_stock(String shortage_stock) {
                this.shortage_stock = shortage_stock;
            }

            public String getSpoilage_stock() {
                return spoilage_stock;
            }

            public void setSpoilage_stock(String spoilage_stock) {
                this.spoilage_stock = spoilage_stock;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public String getOverdraft_stock() {
                return overdraft_stock;
            }

            public void setOverdraft_stock(String overdraft_stock) {
                this.overdraft_stock = overdraft_stock;
            }

            public String getOut_stock() {
                return out_stock;
            }

            public void setOut_stock(String out_stock) {
                this.out_stock = out_stock;
            }

            public String getLose_stock() {
                return lose_stock;
            }

            public void setLose_stock(String lose_stock) {
                this.lose_stock = lose_stock;
            }

            public String getIn_stock() {
                return in_stock;
            }

            public void setIn_stock(String in_stock) {
                this.in_stock = in_stock;
            }

            public String getWms_stock() {
                return wms_stock;
            }

            public void setWms_stock(String wms_stock) {
                this.wms_stock = wms_stock;
            }

            public int getAll_damage_stock() {
                return all_damage_stock;
            }

            public void setAll_damage_stock(int all_damage_stock) {
                this.all_damage_stock = all_damage_stock;
            }

            public String getExpire_stock() {
                return expire_stock;
            }

            public void setExpire_stock(String expire_stock) {
                this.expire_stock = expire_stock;
            }

            public String getDamage_stock() {
                return damage_stock;
            }

            public void setDamage_stock(String damage_stock) {
                this.damage_stock = damage_stock;
            }

            public String getExternal_no() {
                return external_no;
            }

            public void setExternal_no(String external_no) {
                this.external_no = external_no;
            }

            public String getOut_of_stock() {
                return out_of_stock;
            }

            public void setOut_of_stock(String out_of_stock) {
                this.out_of_stock = out_of_stock;
            }

            public String getIning_stock() {
                return ining_stock;
            }

            public void setIning_stock(String ining_stock) {
                this.ining_stock = ining_stock;
            }

            public String getStock() {
                return stock;
            }

            public void setStock(String stock) {
                this.stock = stock;
            }

            public String getStatus_name() {
                return status_name;
            }

            public void setStatus_name(String status_name) {
                this.status_name = status_name;
            }

            public String getWarning_stock() {
                return warning_stock;
            }

            public void setWarning_stock(String warning_stock) {
                this.warning_stock = warning_stock;
            }

            public String getProduct_code() {
                return product_code;
            }

            public void setProduct_code(String product_code) {
                this.product_code = product_code;
            }

            public String getBlock_stock() {
                return block_stock;
            }

            public void setBlock_stock(String block_stock) {
                this.block_stock = block_stock;
            }

            public String getWarehouse_code() {
                return warehouse_code;
            }

            public void setWarehouse_code(String warehouse_code) {
                this.warehouse_code = warehouse_code;
            }

            public String getOverage_stock() {
                return overage_stock;
            }

            public void setOverage_stock(String overage_stock) {
                this.overage_stock = overage_stock;
            }

            public ArrayList<InventoryOrderEntity> getStock_detail() {
                return stock_detail;
            }

            public void setStock_detail(ArrayList<InventoryOrderEntity> stock_detail) {
                this.stock_detail = stock_detail;
            }

            //shortage_stock: "盘亏数量"
            private String shortage_stock;
            //spoilage_stock: "损耗（品质问题）数量",
            private String spoilage_stock;
            //status: 状态。10表示正常,20表示库存紧张,30表示断货，40表示发往仓库
            private int status;
            //overdraft_stock: "允许超售库存量",
            private String overdraft_stock;
            //out_stock: "出库总数",
            private String out_stock;
            //lose_stock: "丢失数量",
            private String lose_stock;
            //in_stock: "入库总数",
            private String in_stock;
            //
            private String wms_stock;
            //所有损坏库存
            private int all_damage_stock;
            //expire_stock: "过期数量",
            private String expire_stock;
            //
            private ArrayList<InventoryOrderEntity> stock_detail;
            //damage_stock: "破损数量",
            private String damage_stock;
            //external_no: "商品外部编码"
            private String external_no;
            //
            private String out_of_stock;
            //
            private String ining_stock;
            //stock 商品库存
            private String stock;
            //status_name: 状态名称
            private String status_name;
            //warning_stock: 警界库存数量,
            private String warning_stock;
            //product_code: "商品唯一编码",
            private String product_code;
            //block_stock: "订单占用库存量",
            private String block_stock;
            //
            private String warehouse_code;
            //overage_stock: "盘盈数量"
            private String overage_stock;
            public class InventoryOrderEntity implements Serializable{
                //storage_code: "入库通知单唯一编码",
                private String storage_code;
                //TrackingNo跟踪单号
                private String TrackingNo;
                //数量
                private int Quantity;
                //到期时间
                private String Expire;
                public String getExpire() {
                    return Expire;
                }

                public void setExpire(String expire) {
                    Expire = expire;
                }

                public int getQuantity() {
                    return Quantity;
                }

                public void setQuantity(int quantity) {
                    Quantity = quantity;
                }

                public String getTrackingNo() {
                    return TrackingNo;
                }

                public void setTrackingNo(String trackingNo) {
                    TrackingNo = trackingNo;
                }

                public String getStorage_code() {
                    return storage_code;
                }

                public void setStorage_code(String storage_code) {
                    this.storage_code = storage_code;
                }
            }
        }
    }
}

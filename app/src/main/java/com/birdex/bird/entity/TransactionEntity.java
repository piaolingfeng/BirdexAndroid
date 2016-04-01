package com.birdex.bird.entity;

import java.io.Serializable;

/**
 * Created by huwei on 16/3/25.
 */
public class TransactionEntity implements Serializable{
    public String getTrading_time() {
        return trading_time;
    }

    public void setTrading_time(String trading_time) {
        this.trading_time = trading_time;
    }

    public String getOrder_code() {
        return order_code;
    }

    public void setOrder_code(String order_code) {
        this.order_code = order_code;
    }

    public String getTransaction_type() {
        return transaction_type;
    }

    public void setTransaction_type(String transaction_type) {
        this.transaction_type = transaction_type;
    }

    public String getTransactional_number() {
        return transactional_number;
    }

    public void setTransactional_number(String transactional_number) {
        this.transactional_number = transactional_number;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    //交易时间
    private String  trading_time;
    //交易类型
    private String transaction_type;
    //订单号
    private String order_code;
    //交易流水
    private String transactional_number;
    //存入交易额
    private double transaction_amountIn;

    public double getTransaction_amountOut() {
        return transaction_amountOut;
    }

    public void setTransaction_amountOut(double transaction_amountOut) {
        this.transaction_amountOut = transaction_amountOut;
    }

    public double getTransaction_amountIn() {
        return transaction_amountIn;
    }

    public void setTransaction_amountIn(double transaction_amountIn) {
        this.transaction_amountIn = transaction_amountIn;
    }

    //支出交易额
    private double transaction_amountOut;
    //标记
    private String remarks;
}

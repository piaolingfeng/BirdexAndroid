package com.birdex.bird.entity;

/**
 * Created by birdex on 16/4/25.
 */
public class EncryptEntity {
    private String OperationUrl="";
    private String TradeNo="";
    private String Message="";

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public String getOperationUrl() {
        return OperationUrl;
    }

    public void setOperationUrl(String operationUrl) {
        OperationUrl = operationUrl;
    }

    public String getTradeNo() {
        return TradeNo;
    }

    public void setTradeNo(String tradeNo) {
        TradeNo = tradeNo;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getOperationType() {
        return OperationType;
    }

    public void setOperationType(String operationType) {
        OperationType = operationType;
    }

    private String Code="";
    private String OperationType="";
}

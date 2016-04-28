package com.birdex.bird.entity;

/**
 * Created by hyj on 2016/4/11.
 */
public class Wallet {

    private String type;
    private String balance;
    private String name;

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    //设置选中
    private boolean check=false;
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

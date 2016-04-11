package com.birdex.bird.entity;

import java.util.List;

/**
 * Created by hyj on 2016/4/11.
 */
public class AccountDetail {

    private String user_id;
    private String wallet;
    private List<Wallet> wallets;
    private String credit;
    private String left_credit;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getWallet() {
        return wallet;
    }

    public void setWallet(String wallet) {
        this.wallet = wallet;
    }

    public List<Wallet> getWallets() {
        return wallets;
    }

    public void setWallets(List<Wallet> wallets) {
        this.wallets = wallets;
    }

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

    public String getLeft_credit() {
        return left_credit;
    }

    public void setLeft_credit(String left_credit) {
        this.left_credit = left_credit;
    }
}

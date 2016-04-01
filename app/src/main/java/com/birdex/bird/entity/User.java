package com.birdex.bird.entity;

/**
 * Created by hyj on 2016/3/25.
 */
public class User {

    private String company_code;
    private String company_name;
    private String company_short_name;
    private String user_code;

    public String getCompany_code() {
        return company_code;
    }

    public void setCompany_code(String company_code) {
        this.company_code = company_code;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getUser_code() {
        return user_code;
    }

    public void setUser_code(String user_code) {
        this.user_code = user_code;
    }

    public String getCompany_short_name() {
        return company_short_name;
    }

    public void setCompany_short_name(String company_short_name) {
        this.company_short_name = company_short_name;
    }
}

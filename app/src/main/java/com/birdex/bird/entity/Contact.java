package com.birdex.bird.entity;

/**
 * Created by hyj on 2016/3/31.
 *
 * 联系人信息
 *
 */
public class Contact {

    // "company_code":"ef49c8c73b471c7bbb90b4745045be4d","phone":"15011112222","contact_type":"10","email":"55@163.com","name":"55"

    private String company_code;
    private String phone;
    private String contact_type;
    private String email;

    public String getCompany_code() {
        return company_code;
    }

    public void setCompany_code(String company_code) {
        this.company_code = company_code;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getContact_type() {
        return contact_type;
    }

    public void setContact_type(String contact_type) {
        this.contact_type = contact_type;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;

}

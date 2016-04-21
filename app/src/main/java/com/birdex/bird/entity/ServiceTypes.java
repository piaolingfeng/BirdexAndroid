package com.birdex.bird.entity;

import java.util.List;

/**
 * Created by hyj on 2016/4/18.
 */
public class ServiceTypes {

    private String key;
    private List<ServiceType> serviceTypeList;

    public List<ServiceType> getServiceTypeList() {
        return serviceTypeList;
    }

    public void setServiceTypeList(List<ServiceType> serviceTypeList) {
        this.serviceTypeList = serviceTypeList;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

}

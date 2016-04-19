package com.birdex.bird.entity;

import com.birdex.bird.greendao.city;

import java.util.List;

/**
 * Created by hyj on 2016/3/23.
 */
public class City {

    private List<city> provinces;
    private List<city> areas;
    private List<city> cities;

    public List<city> getProvinces() {
        return provinces;
    }

    public void setProvinces(List<city> provinces) {
        this.provinces = provinces;
    }

    public List<city> getAreas() {
        return areas;
    }

    public void setAreas(List<city> areas) {
        this.areas = areas;
    }

    public List<city> getCities() {
        return cities;
    }

    public void setCities(List<city> cities) {
        this.cities = cities;
    }
}

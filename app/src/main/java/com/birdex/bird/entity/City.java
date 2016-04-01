package com.birdex.bird.entity;

import java.util.List;

/**
 * Created by hyj on 2016/3/23.
 */
public class City {

    private List<Area> provinces;
    private List<Area> areas;
    private List<Area> cities;

    public List<Area> getProvinces() {
        return provinces;
    }

    public void setProvinces(List<Area> provinces) {
        this.provinces = provinces;
    }

    public List<Area> getAreas() {
        return areas;
    }

    public void setAreas(List<Area> areas) {
        this.areas = areas;
    }

    public List<Area> getCities() {
        return cities;
    }

    public void setCities(List<Area> cities) {
        this.cities = cities;
    }
}

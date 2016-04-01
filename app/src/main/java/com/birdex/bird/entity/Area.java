package com.birdex.bird.entity;

/**
 * Created by hyj on 2016/3/23.
 */
public class Area {

    private String AreaID;
    private String AreaName;
    private String ParentID;

    public String getAreaID() {
        return AreaID;
    }

    public void setAreaID(String areaID) {
        AreaID = areaID;
    }

    public String getAreaName() {
        return AreaName;
    }

    public void setAreaName(String areaName) {
        AreaName = areaName;
    }

    public String getParentID() {
        return ParentID;
    }

    public void setParentID(String parentID) {
        ParentID = parentID;
    }
}

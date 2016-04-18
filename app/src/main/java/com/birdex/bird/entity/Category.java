package com.birdex.bird.entity;

import java.util.List;
import java.util.Map;

/**
 * Created by hyj on 2016/4/18.
 */
public class Category {

    private String id;
    private String pid;
    private String name;
    private Map<String,Category> children;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, Category> getChildren() {
        return children;
    }

    public void setChildren(Map<String, Category> children) {
        this.children = children;
    }
}

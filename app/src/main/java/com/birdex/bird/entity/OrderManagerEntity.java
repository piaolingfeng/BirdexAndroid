package com.birdex.bird.entity;

import java.io.Serializable;

/**
 * Created by chuming.zhuang on 2016/3/22.
 */
public class OrderManagerEntity implements Serializable {
    private String name = "";
    private int count = 0;
    private boolean del_state = false;
    private boolean choose_state = false;

    public boolean isChoose_state() {
        return choose_state;
    }

    public void setChoose_state(boolean choose_state) {
        this.choose_state = choose_state;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isDel_state() {
        return del_state;
    }

    public void setDel_state(boolean del_state) {
        this.del_state = del_state;
    }
}

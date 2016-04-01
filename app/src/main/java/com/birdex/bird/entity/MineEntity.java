package com.birdex.bird.entity;

/**
 * Created by huwei on 16/3/31.
 */
public class MineEntity {
    public enum Type{
        Top,Middle,Bottom
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    private String title;

    public Type getShowType() {
        return showType;
    }

    public void setShowType(Type showType) {
        this.showType = showType;
    }

    private Type showType;
}

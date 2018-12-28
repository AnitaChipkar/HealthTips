package com.anitatheone.healthtips;

public class Health {
    private String name;
    private String like;
    private String views;

    public Health(String name , String like , String views)
    {
        this.name = name;
        this.like = like;
        this.views = views;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLike() {
        return like;
    }

    public void setLike(String like) {
        this.like = like;
    }

    public String getViews() {
        return views;
    }

    public void setViews(String views) {
        this.views = views;
    }
}

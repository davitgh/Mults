package com.davidgh.mults.models;

/**
 * Created by davidgh on 2/16/18.
 */

public class SingleMultModel {
    private String name, img;
    private int rating;

    public SingleMultModel() {
    }

    public SingleMultModel(String name, String img, int rating) {
        this.name = name;
        this.img = img;
        this.rating = rating;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}

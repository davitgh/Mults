package com.davidgh.mults.models;

/**
 * Created by davidgh on 3/13/18.
 */

public class Review {
    private int id;
    private String image, name, date, review;
    private float rating;

    public Review(int id, String image, String name, String date, String review, float rating) {
        this.id = id;
        this.image = image;
        this.name = name;
        this.date = date;
        this.review = review;
        this.rating = rating;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}

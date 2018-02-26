package com.davidgh.mults.models;

/**
 * Created by davidgh on 2/25/18.
 */

public class Detail {
    private int id;
    private String name;
    private String image;
    private String video;
    private String attribute;
    private String rating;
    private String description;
    private String actors;

    public Detail() {
    }

    public Detail(int id, String name, String image, String video, String attribute, String rating, String description, String actors) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.video = video;
        this.attribute = attribute;
        this.rating = rating;
        this.description = description;
        this.actors = actors;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getActors() {
        return actors;
    }

    public void setActors(String actors) {
        this.actors = actors;
    }
}

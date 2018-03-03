package com.davidgh.mults.models;

/**
 * Created by davidgh on 3/3/18.
 */

public class Actor {
    private long id;
    private String name;
    private String image;
    private String role;
    private long mult_id; // TODO: Try without this one

    public Actor(long id, String name, String image, String role, long mult_id) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.role = role;
        this.mult_id = mult_id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public long getMult_id() {
        return mult_id;
    }

    public void setMult_id(long mult_id) {
        this.mult_id = mult_id;
    }
}

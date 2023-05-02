package com.example.easytutonotes.model;


import java.io.Serializable;


public class Note implements Serializable {
    private int id;
    private String title;
    private String description;
    private String createdTime;
    private String imagePath;
    private String color;
    private String weblink;
    private boolean pinned;

    public Note() {
    }

    public Note(int id, String title, String description, String createdTime, boolean pinned) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.createdTime = createdTime;
        this.pinned = pinned;
    }

    public Note(String title, String description, String createdTime, boolean pinned) {
        this.title = title;
        this.description = description;
        this.createdTime = createdTime;
        this.pinned = pinned;
    }

    public boolean isPinned() {
        return pinned;
    }

    public void setPinned(boolean pinned) {
        this.pinned = pinned;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getWeblink() {
        return weblink;
    }

    public void setWeblink(String weblink) {
        this.weblink = weblink;
    }

    public String toString(){
        return title +": "+createdTime;
    }
}

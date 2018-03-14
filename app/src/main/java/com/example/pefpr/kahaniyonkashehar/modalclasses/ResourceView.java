package com.example.pefpr.kahaniyonkashehar.modalclasses;

/**
 * Created by Ameya on 23-Nov-17.
 */

public class ResourceView {
    private String name;
    private String resourceID;
    private int thumbnail;

    public ResourceView() {
    }

    public ResourceView(String name, String numOfSongs, int thumbnail) {
        this.name = name;
        this.resourceID = numOfSongs;
        this.thumbnail = thumbnail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getResourceID() {
        return resourceID;
    }

    public void setNumOfSongs(String numOfSongs) {
        this.resourceID = numOfSongs;
    }

    public int getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(int thumbnail) {
        this.thumbnail = thumbnail;
    }
}